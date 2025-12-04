module Day04

open Utils

let neighborOffsets = [ -1, -1; -1, 0; -1, 1; 0, -1; 0, 1; 1, -1; 1, 0; 1, 1 ]

let countNeighbors (grid: char[][]) (r, c) =
    neighborOffsets
    |> List.sumBy (fun (dr, dc) ->
        let r', c' = r + dr, c + dc

        if
            r' >= 0
            && r' < grid.Length
            && c' >= 0
            && c' < grid.[r'].Length
            && grid.[r'].[c'] = '@'
        then
            1
        else
            0)

let findRemovableRolls (grid: char[][]) =
    seq {
        for r in 0 .. grid.Length - 1 do
            for c in 0 .. grid.[r].Length - 1 do
                if grid.[r].[c] = '@' then
                    if countNeighbors grid (r, c) < 4 then
                        yield (r, c)
    }

let part1 (grid: char[][]) = findRemovableRolls grid |> Seq.length

let part2 (grid: char[][]) =
    let rec loop removed =
        let removable = findRemovableRolls grid |> Seq.toList

        match removable with
        | [] -> removed
        | items ->
            items |> List.iter (fun (r, c) -> grid.[r].[c] <- '.')
            loop (removed + items.Length)

    loop 0

let run () =
    let parsed = Input.readInputLines () |> Array.map Seq.toArray

    printfn "Day 04 - Part 1: %d" (part1 parsed)
    printfn "Day 04 - Part 2: %d" (part2 parsed)
