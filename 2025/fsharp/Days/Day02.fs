module Day02

open Utils
open System.Text.RegularExpressions

let parseRanges (input: string) =
    input.Split(',')
    |> Array.map (fun part ->
        let a = part.Split('-')
        int64 a[0], int64 a[1])

let isInvalidIdPart1 (n: int64) =
    let s = string n
    Regex.IsMatch(s, "^(.+?)\\1$")

let isInvalidIdPart2 (n: int64) =
    let s = string n
    Regex.IsMatch(s, "^(.+?)\\1+$")

let sumInvalidIds (isInvalid: int64 -> bool) ranges =
    ranges
    |> Seq.sumBy (fun (startId, endId) -> seq { startId..endId } |> Seq.filter isInvalid |> Seq.sum)

let part1 ranges = sumInvalidIds isInvalidIdPart1 ranges
let part2 ranges = sumInvalidIds isInvalidIdPart2 ranges

let run () =
    let input = Input.readInputText ()
    let ranges = parseRanges input

    printfn "Day 02 - Part 1: %d" (part1 ranges)
    printfn "Day 02 - Part 2: %d" (part2 ranges)
