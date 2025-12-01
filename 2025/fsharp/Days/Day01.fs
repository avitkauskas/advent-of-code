module Day01

open Utils

let input = Input.readInputLines ()

let parseLine (s: string) =
    let sign = if s.[0] = 'L' then -1 else 1
    let n = int s.[1..]
    sign, n

let moves = input |> List.map parseLine
let startPos = 50

let normalize x =
    let r = x % 100
    if r < 0 then r + 100 else r

let part1 moves startPos =
    let _, zeroCount =
        moves
        |> List.fold
            (fun (pos, count) (sign, value) ->
                let newPos = normalize (pos + sign * value)
                let newCount = if newPos = 0 then count + 1 else count
                newPos, newCount)
            (startPos, 0)

    zeroCount

let countZeroDuringRotation pos sign value =
    let dist = if sign = 1 then 100 - pos else pos
    let dist = if dist = 0 then 100 else dist
    if value < dist then 0 else 1 + (value - dist) / 100

let part2 moves startPos =
    let _, zeroCount =
        moves
        |> List.fold
            (fun (pos, count) (sign, value) ->
                let newPos = normalize (pos + sign * value)
                let newCount = countZeroDuringRotation pos sign value
                newPos, count + newCount)
            (startPos, 0)

    zeroCount

let run () =
    printfn "Day 01 - Part 1: %d" (part1 moves startPos)
    printfn "Day 01 - Part 2: %d" (part2 moves startPos)
