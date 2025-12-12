module Day12

open System
open Utils

let part1 (text: string) =
    let sections =
        text.Trim().Split("\n\n")
        |> Array.map (fun s -> s.Split('\n') |> Array.toList)
        |> Array.toList

    let pieceAreas =
        sections
        |> List.take (sections.Length - 1)
        |> List.map (List.sumBy (Seq.filter ((=) '#') >> Seq.length))
        |> Array.ofList

    sections
    |> List.last
    |> List.filter (fun line ->
        let parts = line.Split(':')
        let dims = parts.[0].Split('x')
        let regionArea = int dims.[0] * int dims.[1]

        let totalPieceArea =
            parts.[1].Trim().Split(' ')
            |> Array.mapi (fun i count -> pieceAreas.[i] * int count)
            |> Array.sum

        totalPieceArea <= regionArea)
    |> List.length

let run () =
    let input = Input.readInputText ()
    printfn "Day 12 - Part 1: %d" (part1 input)
