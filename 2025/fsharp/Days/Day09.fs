module Day09

open System
open System.Collections.Generic
open Utils

let parsePoint (line: string) =
    let parts = line.Split(',')
    int64 parts.[0], int64 parts.[1]

let rectangleArea (x1: int64, y1: int64) (x2: int64, y2: int64) =
    (abs (x1 - x2) + 1L) * (abs (y1 - y2) + 1L)

let buildSegments (points: (int64 * int64)[]) =
    let n = points.Length
    let horiz, vert = ResizeArray(), ResizeArray()

    for i in 0 .. n - 1 do
        let (x1, y1), (x2, y2) = points.[i], points.[(i + 1) % n]

        if x1 = x2 then
            let ymin, ymax = min y1 y2, max y1 y2
            vert.Add(x1, ymin, ymax)
        elif y1 = y2 then
            let xmin, xmax = min x1 x2, max x1 x2
            horiz.Add(y1, xmin, xmax)
        else
            failwith "Non axis-aligned edge encountered."

    horiz.ToArray(), vert.ToArray()

let pointOnBoundary (px: int64, py: int64) horiz vert =
    Array.exists (fun (y, xmin, xmax) -> py = y && px >= xmin && px <= xmax) horiz
    || Array.exists (fun (x, ymin, ymax) -> px = x && py >= ymin && py <= ymax) vert

let pointInsideOrOn (px: int64, py: int64) horiz vert =
    if pointOnBoundary (px, py) horiz vert then
        true
    else
        let crossings =
            vert
            |> Array.filter (fun (xv, ymin, ymax) -> xv > px && ymin <= py && py < ymax)

        crossings.Length % 2 = 1

let verticalEdgeProperlyCrosses (xr: int64) (ylo: int64) (yhi: int64) horiz =
    Array.exists (fun (y, xmin, xmax) -> y > ylo && y < yhi && xmin <= xr && xr <= xmax) horiz

let horizontalEdgeProperlyCrosses (yr: int64) (xlo: int64) (xhi: int64) vert =
    Array.exists (fun (x, ymin, ymax) -> x > xlo && x < xhi && ymin <= yr && yr <= ymax) vert

let rectangleInsideOrOn (x1: int64, y1: int64) (x2: int64, y2: int64) horiz vert =
    let xmin, xmax = min x1 x2, max x1 x2
    let ymin, ymax = min y1 y2, max y1 y2

    let cornersInside =
        [ (xmin, ymin); (xmin, ymax); (xmax, ymin); (xmax, ymax) ]
        |> List.forall (fun p -> pointInsideOrOn p horiz vert)

    cornersInside
    && not (
        verticalEdgeProperlyCrosses xmin ymin ymax horiz
        || verticalEdgeProperlyCrosses xmax ymin ymax horiz
        || horizontalEdgeProperlyCrosses ymin xmin xmax vert
        || horizontalEdgeProperlyCrosses ymax xmin xmax vert
    )

let part1 (points: (int64 * int64)[]) =
    let n = points.Length

    [ for i in 0 .. n - 2 do
          for j in i + 1 .. n - 1 do
              yield rectangleArea points.[i] points.[j] ]
    |> List.max

let part2 (points: (int64 * int64)[]) =
    let horiz, vert = buildSegments points
    let n = points.Length

    [ for i in 0 .. n - 2 do
          let (x1, y1) = points.[i]

          for j in i + 1 .. n - 1 do
              let (x2, y2) = points.[j]

              if x1 <> x2 && y1 <> y2 && rectangleInsideOrOn (x1, y1) (x2, y2) horiz vert then
                  yield rectangleArea (x1, y1) (x2, y2) ]
    |> List.max

let run () =
    let lines = Input.readInputLines ()
    let points = lines |> Array.map parsePoint
    printfn "Day 09 - Part 1: %d" (part1 points)
    printfn "Day 09 - Part 2: %d" (part2 points)
