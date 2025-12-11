module Day11

open System.Collections.Generic
open Utils

let parse (lines: string[]) : IDictionary<string, list<string>> =
    lines
    |> Array.map (fun line ->
        let p: string[] = line.Split ": "
        p.[0], p.[1].Split ' ' |> Array.toList)
    |> dict

let countPaths (g: IDictionary<string, list<string>>) start req =
    let a, b =
        match req with
        | [] -> "", ""
        | [ x; y ] -> x, y
        | _ -> failwith "expected 0 or 2 required nodes"

    let memo = Dictionary<(string * bool * bool), int64>()

    let rec dfs node sa sb (stack: HashSet<_>) =
        if stack.Contains node then
            0L
        else
            let sa, sb = sa || node = a, sb || node = b
            let key = node, sa, sb

            match memo.TryGetValue key with
            | true, v -> v
            | _ ->
                let res =
                    if node = "out" then
                        if req.IsEmpty || sa && sb then 1L else 0L
                    else
                        stack.Add node |> ignore
                        let sum = g.[node] |> List.sumBy (fun c -> dfs c sa sb stack)
                        stack.Remove node |> ignore
                        sum

                memo.[key] <- res
                res

    dfs start false false (HashSet())

let run () =
    let g = parse (Input.readInputLines ())
    printfn "Day 11 - Part 1: %d" (countPaths g "you" [])
    printfn "Day 11 - Part 2: %d" (countPaths g "svr" [ "dac"; "fft" ])
