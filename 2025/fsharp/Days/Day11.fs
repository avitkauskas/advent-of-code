module Day11

open System.Collections.Generic
open Utils

let parse (lines: string[]) =
    lines
    |> Array.map (fun line ->
        let parts = line.Split(": ")
        let src = parts.[0]
        let dsts = parts.[1].Split(' ') |> Array.toList
        src, dsts)
    |> dict

/// Unified solver: either 0 required nodes, or exactly 2 required nodes.
let countPaths (graph: IDictionary<string, list<string>>) (startNode: string) (required: string list) : int64 =

    // We assume required is [] or [a;b]
    let reqA, reqB =
        match required with
        | [] -> "", ""
        | [ a; b ] -> a, b
        | _ -> failwith "Incorrect use: required must be [] or [a; b]"

    let memo = Dictionary<(string * bool * bool), int64>()

    let rec dfs node seenA seenB (stack: HashSet<string>) =
        if stack.Contains(node) then
            0L
        else
            let seenA' = seenA || node = reqA
            let seenB' = seenB || node = reqB

            let key = (node, seenA', seenB')

            match memo.TryGetValue(key) with
            | true, v -> v
            | _ ->
                let result =
                    if node = "out" then
                        // If no required nodes ⇒ always count
                        // If two required nodes ⇒ need both seen
                        if required.IsEmpty || (seenA' && seenB') then 1L else 0L
                    else
                        stack.Add(node) |> ignore
                        let sum = graph.[node] |> List.sumBy (fun child -> dfs child seenA' seenB' stack)
                        stack.Remove(node) |> ignore
                        sum

                memo[key] <- result
                result

    dfs startNode false false (HashSet())


let run () =
    let lines = Input.readInputLines ()
    let graph = parse lines

    let part1 = countPaths graph "you" []
    printfn "Day 11 - Part 1: %d" part1

    let part2 = countPaths graph "svr" [ "dac"; "fft" ]
    printfn "Day 11 - Part 2: %d" part2
