module Day09

open Utils

type Segment =
    | Horizontal of y: int64 * xMin: int64 * xMax: int64
    | Vertical of x: int64 * yMin: int64 * yMax: int64

let parsePoint (line: string) =
    match line.Split(',') with
    | [| x; y |] -> int64 x, int64 y
    | _ -> failwith "Invalid point format"

let rectangleArea (x1, y1) (x2, y2) =
    (abs (x1 - x2) + 1L) * (abs (y1 - y2) + 1L)

let buildSegments points =
    let makeSegment (x1, y1) (x2, y2) =
        match x1 = x2, y1 = y2 with
        | true, false -> Vertical(x1, min y1 y2, max y1 y2)
        | false, true -> Horizontal(y1, min x1 x2, max x1 x2)
        | _ -> failwith "Non axis-aligned edge"

    points
    |> Array.pairwise
    |> Array.append [| Array.last points, Array.head points |]
    |> Array.map (fun (p1, p2) -> makeSegment p1 p2)
    |> Array.partition (function
        | Horizontal _ -> true
        | Vertical _ -> false)

let pointOnSegment px py segment =
    match segment with
    | Horizontal(y, xMin, xMax) -> py = y && px >= xMin && px <= xMax
    | Vertical(x, yMin, yMax) -> px = x && py >= yMin && py <= yMax

let isOnBoundary (px, py) horizontal vertical =
    Array.exists (pointOnSegment px py) horizontal
    || Array.exists (pointOnSegment px py) vertical

let rayCrossesSegment px py segment =
    match segment with
    | Vertical(xv, yMin, yMax) -> xv > px && yMin <= py && py < yMax
    | Horizontal _ -> false

let isInside (px, py) horizontal vertical =
    isOnBoundary (px, py) horizontal vertical
    || (vertical
        |> Array.filter (rayCrossesSegment px py)
        |> Array.length
        |> fun count -> count % 2 = 1)

let crossesVerticalEdge xEdge yMin yMax segment =
    match segment with
    | Horizontal(y, xMin, xMax) -> y > yMin && y < yMax && xMin <= xEdge && xEdge <= xMax
    | Vertical _ -> false

let crossesHorizontalEdge yEdge xMin xMax segment =
    match segment with
    | Vertical(x, yMin, yMax) -> x > xMin && x < xMax && yMin <= yEdge && yEdge <= yMax
    | Horizontal _ -> false

let rectangleInsidePolygon (x1, y1) (x2, y2) horizontal vertical =
    let xMin, xMax = min x1 x2, max x1 x2
    let yMin, yMax = min y1 y2, max y1 y2

    let corners = [| (xMin, yMin); (xMin, yMax); (xMax, yMin); (xMax, yMax) |]
    let cornersValid = Array.forall (fun p -> isInside p horizontal vertical) corners

    if not cornersValid then
        false
    else
        not (
            Array.exists (crossesVerticalEdge xMin yMin yMax) horizontal
            || Array.exists (crossesVerticalEdge xMax yMin yMax) horizontal
            || Array.exists (crossesHorizontalEdge yMin xMin xMax) vertical
            || Array.exists (crossesHorizontalEdge yMax xMin xMax) vertical
        )

let findMaxArea points selector =
    let n = Array.length points

    seq {
        for i in 0 .. n - 2 do
            for j in i + 1 .. n - 1 -> selector points.[i] points.[j]
    }
    |> Seq.max

let part1 points = findMaxArea points rectangleArea

let part2 points =
    let horizontal, vertical = buildSegments points

    findMaxArea points (fun p1 p2 ->
        if rectangleInsidePolygon p1 p2 horizontal vertical then
            rectangleArea p1 p2
        else
            0L)

let run () =
    let points = Input.readInputLines () |> Array.map parsePoint
    printfn "Day 09 - Part 1: %d" (part1 points)
    printfn "Day 09 - Part 2: %d" (part2 points)
