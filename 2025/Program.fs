open System
open System.Reflection

// Discover all DayXX modules with a 'run' function
let allDayModules =
    Assembly.GetExecutingAssembly().GetTypes()
    |> Array.filter (fun t -> t.IsSealed && t.IsAbstract && t.Name.StartsWith("Day"))
    |> Array.choose (fun t ->
        t.GetMethod("run", BindingFlags.Public ||| BindingFlags.Static)
        |> Option.ofObj
        |> Option.bind (fun runMethod ->
            match Int32.TryParse(t.Name.Substring(3)) with
            | true, n -> Some(n, runMethod)
            | _ -> None))
    |> Array.sortBy fst

[<EntryPoint>]
let main argv =
    match argv |> Array.toList with
    | [ dayStr ] ->
        match Int32.TryParse(dayStr) with
        | true, dayNum ->
            match allDayModules |> Array.tryFind (fun (n, _) -> n = dayNum) with
            | Some(_, runMethod) -> runMethod.Invoke(null, [||]) |> ignore
            | None -> printfn "Day %d not found" dayNum
        | _ -> printfn "Invalid day number: %s" dayStr
    | _ -> printfn "Usage: dotnet run <dayNumber>"

    0
