module Day05

open Utils

let parseRange (s: string) =
    let p = s.Split('-')
    int64 p[0], int64 p[1]

let mergeRanges =
    List.sortBy fst
    >> List.fold
        (fun acc (s, e) ->
            match acc with
            | [] -> [ s, e ]
            | (s0, e0) :: rest -> if s <= e0 then (s0, max e e0) :: rest else (s, e) :: acc)
        []
    >> List.rev

let isFresh merged id =
    merged |> List.exists (fun (s, e) -> s <= id && id <= e)

let part1 merged ids =
    ids |> List.filter (isFresh merged) |> List.length

let part2 merged =
    merged |> List.sumBy (fun (s, e) -> e - s + 1L)

let run () =
    let blocks = Input.readInputText().Trim().Split("\n\n")

    let ranges =
        blocks.[0].Split('\n') |> Array.map parseRange |> Array.toList |> mergeRanges

    let ids = blocks.[1].Split('\n') |> Array.map int64 |> Array.toList

    printfn "Day 05 - Part 1: %d" (part1 ranges ids)
    printfn "Day 05 - Part 2: %d" (part2 ranges)
