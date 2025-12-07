module Day07

open Utils

let initialState (lines: string[]) =
    let height = lines.Length
    let width = lines.[0].Length
    let startCol = lines.[0] |> Seq.findIndex ((=) 'S')
    height, width, startCol

let part1 (lines: string[]) =
    let height, width, startCol = initialState lines

    let splitters =
        lines
        |> Array.map (fun line ->
            line
            |> Seq.indexed
            |> Seq.choose (fun (i, c) -> if c = '^' then Some i else None)
            |> Set.ofSeq)

    let mutable beams = Set.singleton startCol
    let mutable splits = 0

    for r in 1 .. height - 1 do
        let rowSplitters = splitters.[r]

        let hits = Set.intersect beams rowSplitters
        splits <- splits + hits.Count

        let straightBeams = Set.difference beams hits
        let splitBeams = hits |> Seq.collect (fun c -> [ c - 1; c + 1 ]) |> Set.ofSeq

        beams <- Set.union straightBeams splitBeams

    splits

let part2 (lines: string[]) : bigint =
    let height, width, startCol = initialState lines

    let counts = Array.create width bigint.Zero
    counts.[startCol] <- bigint.One

    for r in 1 .. height - 1 do
        let line = lines.[r]
        let next = Array.create width bigint.Zero

        for c in 0 .. width - 1 do
            let n = counts.[c]

            if n <> bigint.Zero then
                if line.[c] = '^' then
                    next.[c - 1] <- next.[c - 1] + n
                    next.[c + 1] <- next.[c + 1] + n
                else
                    next.[c] <- next.[c] + n

        Array.blit next 0 counts 0 width

    Array.fold (+) bigint.Zero counts

let run () =
    let input = Input.readInputLines ()
    printfn "Day 07 - Part 1: %A" (part1 input)
    printfn "Day 07 - Part 2: %A" (part2 input)
