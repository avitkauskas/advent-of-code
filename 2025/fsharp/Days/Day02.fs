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

let sumInvalidIds (isInvalid: int64 -> bool) input =
    parseRanges input
    |> Seq.sumBy (fun (startId, endId) -> seq { startId..endId } |> Seq.filter isInvalid |> Seq.sum)

let part1 input = sumInvalidIds isInvalidIdPart1 input
let part2 input = sumInvalidIds isInvalidIdPart2 input

let run () =
    let input = Input.readInputText ()

    printfn "Day 02 - Part 1: %d" (part1 input)
    printfn "Day 02 - Part 2: %d" (part2 input)
