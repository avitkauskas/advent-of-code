module Day09

open System
open System.Diagnostics
open System.Collections.Generic
open Utils

// -------------------- parsing / helpers --------------------
let parsePoint (line: string) =
    let parts = line.Split(',')
    int64 parts.[0], int64 parts.[1]

let rectangleArea (x1: int64, y1: int64) (x2: int64, y2: int64) =
    let w = abs (x1 - x2) + 1L
    let h = abs (y1 - y2) + 1L
    w * h

// -------------------- segment representation --------------------
// horizontal: (y, xmin, xmax) with xmin <= xmax
// vertical:   (x, ymin, ymax) with ymin <= ymax

// Build lists of horizontal and vertical segments from the polygon order
let buildSegments (points: (int64 * int64)[]) =
    let n = points.Length
    let horiz = ResizeArray<int64 * int64 * int64>()
    let vert = ResizeArray<int64 * int64 * int64>()

    for i = 0 to n - 1 do
        let (x1, y1) = points.[i]
        let (x2, y2) = points.[(i + 1) % n]

        if x1 = x2 then
            // vertical
            let ymin = min y1 y2
            let ymax = max y1 y2
            vert.Add(x1, ymin, ymax)
        elif y1 = y2 then
            // horizontal
            let xmin = min x1 x2
            let xmax = max x1 x2
            horiz.Add(y1, xmin, xmax)
        else
            failwith "Non axis-aligned edge encountered (input guarantees axis-aligned)."

    horiz.ToArray(), vert.ToArray()

// -------------------- geometry predicates --------------------

// check if point (px,py) lies exactly on any segment (boundary)
let pointOnBoundary (px: int64, py: int64) (horiz: (int64 * int64 * int64)[]) (vert: (int64 * int64 * int64)[]) =
    // check horizontal segments
    let mutable idx = 0
    let mutable on = false

    while idx < horiz.Length && not on do
        let (y, xmin, xmax) = horiz.[idx]

        if py = y && px >= xmin && px <= xmax then
            on <- true

        idx <- idx + 1

    idx <- 0

    while idx < vert.Length && not on do
        let (x, ymin, ymax) = vert.[idx]

        if px = x && py >= ymin && py <= ymax then
            on <- true

        idx <- idx + 1

    on

// point-in-polygon for orthogonal polygon using ray to +X (right).
// Assumes polygon is simple. If point is on boundary returns true.
let pointInsideOrOn (px: int64, py: int64) (horiz: (int64 * int64 * int64)[]) (vert: (int64 * int64 * int64)[]) =
    if pointOnBoundary (px, py) horiz vert then
        true
    else
        // count intersections of a ray from (px,py) to +infinity with vertical edges
        // use half-open rule: count vertical edges where ymin <= py < ymax and xv > px
        let mutable count = 0
        let mutable i = 0

        while i < vert.Length do
            let (xv, ymin, ymax) = vert.[i]

            if xv > px && ymin <= py && py < ymax then
                count <- count + 1

            i <- i + 1

        (count % 2) = 1

// check whether the vertical rectangle edge at x = xr with y in [ylo..yhi]
// has any proper intersection with polygon horizontal edges.
// We treat intersections strictly inside the edge (exclude touching at rectangle endpoints).
let verticalEdgeProperlyCrosses (xr: int64) (ylo: int64) (yhi: int64) (horiz: (int64 * int64 * int64)[]) =
    let mutable i = 0
    let mutable crosses = false

    while i < horiz.Length && not crosses do
        let (y, xmin, xmax) = horiz.[i]

        if y > ylo && y < yhi && xmin <= xr && xr <= xmax then
            crosses <- true

        i <- i + 1

    crosses

// check whether the horizontal rectangle edge at y = yr with x in [xlo..xhi]
// has any proper intersection with polygon vertical edges.
let horizontalEdgeProperlyCrosses (yr: int64) (xlo: int64) (xhi: int64) (vert: (int64 * int64 * int64)[]) =
    let mutable i = 0
    let mutable crosses = false

    while i < vert.Length && not crosses do
        let (x, ymin, ymax) = vert.[i]

        if x > xlo && x < xhi && ymin <= yr && yr <= ymax then
            crosses <- true

        i <- i + 1

    crosses

// Check whether rectangle with corners (x1,y1) and (x2,y2) is fully inside or on boundary
// of polygon represented by horiz/vert segments.
let rectangleInsideOrOn
    (x1: int64, y1: int64)
    (x2: int64, y2: int64)
    (horiz: (int64 * int64 * int64)[])
    (vert: (int64 * int64 * int64)[])
    =
    let xmin = min x1 x2
    let xmax = max x1 x2
    let ymin = min y1 y2
    let ymax = max y1 y2

    // 1) corners must be inside or on boundary
    if not (pointInsideOrOn (xmin, ymin) horiz vert) then
        false
    elif not (pointInsideOrOn (xmin, ymax) horiz vert) then
        false
    elif not (pointInsideOrOn (xmax, ymin) horiz vert) then
        false
    elif not (pointInsideOrOn (xmax, ymax) horiz vert) then
        false
    else if
        // 2) rectangle edges must not properly cross polygon edges.
        // (touching at endpoints or running along boundary is allowed)
        verticalEdgeProperlyCrosses xmin ymin ymax horiz
    then
        false
    elif verticalEdgeProperlyCrosses xmax ymin ymax horiz then
        false
    elif horizontalEdgeProperlyCrosses ymin xmin xmax vert then
        false
    elif horizontalEdgeProperlyCrosses ymax xmin xmax vert then
        false
    else
        true

// -------------------- part2 main --------------------
let part2 (lines: string[]) =
    let sw = Stopwatch()
    sw.Start()

    let reds = lines |> Array.map parsePoint
    let n = reds.Length

    // build polygon segments
    let horiz, vert = buildSegments reds

    // Quick sanity: number of segments
    // printfn "Segments: horiz=%d vert=%d  (t=%d ms)" horiz.Length vert.Length sw.ElapsedMilliseconds

    let mutable best = 0L
    // iterate all pairs of red points
    for i = 0 to n - 2 do
        let (x1, y1) = reds.[i]

        for j = i + 1 to n - 1 do
            let (x2, y2) = reds.[j]
            // skip degenerate (zero area) rectangles quickly
            if x1 <> x2 && y1 <> y2 then
                // test whether rectangle is fully inside/on polygon
                if rectangleInsideOrOn (x1, y1) (x2, y2) horiz vert then
                    let area = rectangleArea (x1, y1) (x2, y2)

                    if area > best then
                        best <- area

    // final timing
    // printfn "Part2 done. Best = %d  time=%d ms" best sw.ElapsedMilliseconds
    best

// -------------------- part1 (int64 safe) --------------------
let part1 (lines: string[]) =
    let pts = lines |> Array.map parsePoint
    let n = pts.Length
    let mutable best = 0L

    for i = 0 to n - 2 do
        for j = i + 1 to n - 1 do
            let a = rectangleArea pts.[i] pts.[j]

            if a > best then
                best <- a

    best

let run () =
    let input = Input.readInputLines ()
    printfn "Day 09 - Part 1: %d" (part1 input)
    printfn "Day 09 - Part 2: %d" (part2 input)
