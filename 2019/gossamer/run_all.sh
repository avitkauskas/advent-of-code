#!/usr/bin/env bash

OUTFILE="run_all_results.txt"
TIMEOUT_S=10
TMPD="$(mktemp -d)"
trap 'rm -rf "$TMPD"' EXIT

: > "$OUTFILE"

run_with_timeout() {
  local secs="$1"; shift
  if [ "$secs" -eq 0 ]; then
    "$@"
    return $?
  fi
  "$@" &
  local pid=$!
  local flag="$TMPD/tmo_$$_$RANDOM"
  ( sleep "$secs" && touch "$flag"; kill -TERM "$pid" 2>/dev/null; sleep 1; kill -KILL "$pid" 2>/dev/null ) &
  local killer=$!
  wait "$pid"
  local rc=$?
  kill "$killer" 2>/dev/null
  wait "$killer" 2>/dev/null
  if [ -f "$flag" ]; then
    rm -f "$flag"
    return 124
  fi
  rm -f "$flag"
  return "$rc"
}

run_build() {
  local label="$1"; shift
  local log="$TMPD/build_$$_$RANDOM.log"
  "$@" > "$log" 2>&1
  local rc=$?
  if [ "$rc" -ne 0 ]; then
    {
      echo "'$label' FAILED (exit $rc):"
      grep -v '^note: building with LLVM' "$log"
    } | tee -a "$OUTFILE" >&2
  fi
  rm -f "$log"
  return "$rc"
}

run_captured() {
  local secs="$1" label="$2" out="$3"; shift 3
  run_with_timeout "$secs" "$@" > "$out" 2>/dev/null
  local rc=$?
  if [ "$rc" -eq 124 ]; then
    echo "WARNING! '$label' took longer than ${secs}s and was killed." | tee -a "$OUTFILE" >&2
  fi
  return "$rc"
}

declare -A timed_out=()

for i in $(seq -w 1 25); do
  d="d$i"
  r1="$TMPD/$d.run1"
  r2="$TMPD/$d.run2"
  r3="$TMPD/$d.run3"

  {
    echo "============================================"
    echo "== $d =="
    echo "============================================"
  } >> "$OUTFILE"

  run_build "gos build $d" gos build "$d"
  run_build "gos build --release $d" gos build --release "$d"

  run_captured "$TIMEOUT_S" "gos run $d" "$r1" gos run "$d"; rc1=$?
  run_captured "$TIMEOUT_S" "$d/target/debug/$d" "$r2" "$d/target/debug/$d"; rc2=$?
  run_captured "$TIMEOUT_S" "$d/target/release/$d" "$r3" "$d/target/release/$d"; rc3=$?

  timed_out[$d]=0
  [ "$rc1" -eq 124 ] && timed_out[$d]=1
  [ "$rc2" -eq 124 ] && timed_out[$d]=1
  [ "$rc3" -eq 124 ] && timed_out[$d]=1

  if [ "$rc1" -eq "$rc2" ] && [ "$rc1" -eq "$rc3" ] \
     && cmp -s "$r1" "$r2" && cmp -s "$r1" "$r3"; then
    {
      echo "Outputs of $d match:"
      cat "$r1"
    } | tee -a "$OUTFILE"
  else
    {
      echo "WARNING! Outputs DIFFER for $d:"
      echo "--- gos run $d (exit $rc1) ---"
      cat "$r1"
      echo "--- $d/target/debug/$d (exit $rc2) ---"
      cat "$r2"
      echo "--- $d/target/release/$d (exit $rc3) ---"
      cat "$r3"
    } | tee -a "$OUTFILE"
  fi
  echo
done

echo "============= BENCHMARKS =============" >> "$OUTFILE"
for i in $(seq -w 1 25); do
  d="d$i"
  if [ "${timed_out[$d]:-0}" -eq 1 ]; then
    echo "WARNING! $d: a run command timed out; skipping benchmark." | tee -a "$OUTFILE" >&2
  else
    echo "Running benchmark for $d"
    {
      echo "Benchmark of $d:"
      v repeat "$d/target/release/$d" "$d/target/debug/$d" "gos run $d"
      echo
    } >> "$OUTFILE" 2>&1
  fi
  echo
done

echo "Done. Results written to $OUTFILE"