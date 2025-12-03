module Day03

open Utils

let pickMaxDigits k lines =
    let pick line =
        let digits = line |> Seq.map (fun c -> int c - int '0') |> Seq.toArray
        let n = digits.Length

        let _, _, value =
            ((0, k, 0L), seq { 1..k })
            ||> Seq.fold (fun (start, rem, acc) _ ->
                let pos = seq { start .. n - rem } |> Seq.maxBy (fun i -> digits.[i])
                (pos + 1, rem - 1, acc * 10L + int64 digits.[pos]))

        value

    lines |> Array.sumBy pick

let run () =
    let lines = Input.readInputLines ()
    printfn "Day 03 - Part 1: %d" (pickMaxDigits 2 lines)
    printfn "Day 03 - Part 2: %d" (pickMaxDigits 12 lines)
