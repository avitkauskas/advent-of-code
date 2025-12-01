module Utils

open System
open System.IO
open System.Text.RegularExpressions
open System.Runtime.CompilerServices

let private ensureFileExists filename =
    if not (File.Exists filename) then
        printfn "Input file not found: %s" filename
        Environment.Exit(1)

let readInputLinesForDay dayNumber =
    let filename = sprintf "../inputs/day%02d.txt" dayNumber
    ensureFileExists filename
    File.ReadAllLines filename |> Array.toList

let readInputTextForDay dayNumber =
    let filename = sprintf "../inputs/day%02d.txt" dayNumber
    ensureFileExists filename
    File.ReadAllText filename

let private dayFromCaller (callerPath: string) =
    if String.IsNullOrEmpty callerPath then
        failwith "Caller file path is empty."

    let file = Path.GetFileNameWithoutExtension callerPath

    match file with
    | null
    | "" -> failwithf "Caller file path '%s' does not contain a valid filename." callerPath
    | file ->
        let m = Regex.Match(file, @"Day(\d{2})", RegexOptions.IgnoreCase)

        if m.Success then
            int m.Groups.[1].Value
        else
            failwithf "Could not determine AoC day from filename '%s'. Expected 'DayNN'." file

type Input =
    static member readInputLines([<CallerFilePath>] ?callerPath: string) =
        let caller = defaultArg callerPath ""
        let day = dayFromCaller caller
        readInputLinesForDay day

    static member readInputText([<CallerFilePath>] ?callerPath: string) =
        let caller = defaultArg callerPath ""
        let day = dayFromCaller caller
        readInputTextForDay day
