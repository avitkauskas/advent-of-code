module Day08

open Utils
open System

type Point = int * int * int

type UnionFind(n: int) =
    let parent = Array.init n id
    let size = Array.create n 1

    member _.Find(e: int) =
        let rec findRoot x =
            if parent.[x] = x then
                x
            else
                let r = findRoot parent.[x]
                parent.[x] <- r
                r

        findRoot e

    member this.Union(a: int, b: int) =
        let ra, rb = this.Find a, this.Find b

        if ra <> rb then
            if size.[ra] < size.[rb] then
                parent.[ra] <- rb
                size.[rb] <- size.[rb] + size.[ra]
            else
                parent.[rb] <- ra
                size.[ra] <- size.[ra] + size.[rb]

    member this.ComponentSizes() =
        [| 0 .. n - 1 |] |> Array.groupBy this.Find |> Array.map (snd >> Array.length)

let parsePoint (line: string) =
    match line.Split(',') with
    | [| x; y; z |] -> int x, int y, int z
    | _ -> failwith $"Invalid point format: {line}"

let distSquared ((x1, y1, z1): Point) ((x2, y2, z2): Point) : int64 =
    let inline sqr x = int64 x * int64 x
    sqr (x1 - x2) + sqr (y1 - y2) + sqr (z1 - z2)

let generateSortedEdges (points: Point[]) =
    let n = points.Length

    [| for i in 0 .. n - 2 do
           for j in i + 1 .. n - 1 -> distSquared points.[i] points.[j], i, j |]
    |> Array.sortBy (fun (d, _, _) -> d)

let part1 pointsLength (edges: (int64 * int * int)[]) =
    let uf = UnionFind pointsLength

    edges
    |> Array.take (min 1000 edges.Length)
    |> Array.iter (fun (_, i, j) -> uf.Union(i, j))

    let sizes = uf.ComponentSizes() |> Array.sortDescending

    match sizes with
    | [||]
    | [| _ |]
    | [| _; _ |] -> failwith "Less than 3 components"
    | _ -> int64 sizes.[0] * int64 sizes.[1] * int64 sizes.[2] |> int

let part2 (points: Point[]) (edges: (int64 * int * int)[]) =
    let uf = UnionFind points.Length
    let mutable components = points.Length

    let lastEdge =
        edges
        |> Array.tryFind (fun (_, i, j) ->
            if uf.Find i <> uf.Find j then
                uf.Union(i, j)
                components <- components - 1
                components = 1
            else
                false)

    match lastEdge with
    | Some(_, i, j) ->
        let (x1, _, _), (x2, _, _) = points.[i], points.[j]
        int64 x1 * int64 x2
    | None -> failwith "No edge found"

let run () =
    let input = Input.readInputLines ()
    let points = input |> Array.map parsePoint
    let edges = generateSortedEdges points

    printfn "Day 08 - Part 1: %d" (part1 points.Length edges)
    printfn "Day 08 - Part 2: %d" (part2 points edges)
