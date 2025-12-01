module Day01

open Utils

let input = Input.readInputLines ()

let parseLine (s: string) =
    let n = int s.[1..]
    if s.[0] = 'L' then -n else n

let moves = input |> List.map parseLine
let startPos = 50

let part1 moves startPos =
    let _, zeroCount =
        moves
        |> List.fold
            (fun (pos, count) move ->
                let newPos = (pos + move) % 100
                let newCount = if newPos = 0 then count + 1 else count
                newPos, newCount)
            (startPos, 0)

    zeroCount

let countZeroDuringRotation pos move =
    let dist = if move > 0 then (100 - pos) % 100 else (100 + pos) % 100
    let dist = if dist = 0 then 100 else dist
    let move = abs move
    if move < dist then 0 else 1 + (move - dist) / 100

let part2 moves startPos =
    let _, zeroCount =
        moves
        |> List.fold
            (fun (pos, count) move ->
                let newPos = (pos + move) % 100
                let newCount = countZeroDuringRotation pos move
                newPos, count + newCount)
            (startPos, 0)

    zeroCount

let run () =
    printfn "Day 01 - Part 1: %d" (part1 moves startPos)
    printfn "Day 01 - Part 2: %d" (part2 moves startPos)
