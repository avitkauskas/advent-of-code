module Day06

open System
open System.Text.RegularExpressions
open Utils

let part1 (lines: string[]) =
    let rows =
        lines |> Array.map (fun line -> Regex.Split(line.Trim(), "\s+") |> Array.toList)

    let cols = rows |> List.ofArray |> List.transpose

    cols
    |> List.sumBy (fun col ->
        let reversed = List.rev col
        let op = reversed.Head
        let nums = reversed.Tail |> List.map int64

        match op with
        | "+" -> nums |> List.sum
        | "*" -> nums |> List.fold (*) 1L
        | _ -> failwithf "Invalid operator %s" op)

let part2 (lines: string[]) =
    let height = lines.Length
    let width = lines.[0].Length

    let operators =
        lines.[height - 1]
        |> Seq.mapi (fun i c -> i, c)
        |> Seq.filter (fun (_, c) -> c = '+' || c = '*')
        |> Seq.toList

    let ranges =
        operators
        |> List.mapi (fun i (opCol, opChar) ->
            let endCol =
                if i < operators.Length - 1 then
                    let (nextOpCol, _) = operators.[i + 1]
                    nextOpCol - 2
                else
                    width - 1

            (opCol, endCol, opChar))

    let readNumberColumn (col: int) =
        let chars =
            [ for row in 0 .. height - 2 do
                  let c = lines.[row].[col]

                  if c <> ' ' then
                      yield c ]

        chars |> Array.ofList |> String |> int64

    let solveProblem (startCol, endCol, opChar) =
        let numbers = [ for col in startCol..endCol -> readNumberColumn col ]

        match opChar with
        | '+' -> numbers |> List.sum
        | '*' -> numbers |> List.fold (*) 1L
        | _ -> failwith "Unknown operator"

    ranges |> List.map solveProblem |> List.sum

let run () =
    let input = Input.readInputLines ()
    printfn "Day 06 - Part 1: %d" (part1 input)
    printfn "Day 06 - Part 2: %d" (part2 input)
