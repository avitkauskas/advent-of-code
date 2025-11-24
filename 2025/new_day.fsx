open System
open System.IO

if fsi.CommandLineArgs.Length < 2 then
    printfn "Usage: dotnet fsi new_day.fsx <dayNumber>"
    Environment.Exit(1)

let parseDayArg (arg: string) =
    match Int32.TryParse(arg) with
    | true, v -> Some v
    | false, _ ->
        printfn "Error: '%s' is not a valid integer." arg
        None

let day =
    match parseDayArg fsi.CommandLineArgs.[1] with
    | Some d -> d
    | None ->
        Environment.Exit(1)
        0

let dayStr = sprintf "%02d" day
let filename = sprintf "Days/Day%s.fs" dayStr

if File.Exists(filename) then
    printfn "Error: %s already exists. Aborting." filename
    Environment.Exit(1)

let dir = Path.GetDirectoryName(filename)

if not (String.IsNullOrEmpty dir) && not (Directory.Exists dir) then
    Directory.CreateDirectory(dir) |> ignore

let template = File.ReadAllText "day.template"
let content = template.Replace("{day}", string day).Replace("{dayStr}", dayStr)

File.WriteAllText(filename, content)

printfn "Created %s" filename
