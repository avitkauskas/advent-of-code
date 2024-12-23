use std::collections::{HashMap, HashSet};

fn find_all_triangles(graph: &HashMap<String, HashSet<String>>) -> Vec<HashSet<String>> {
    let mut triangles = Vec::new();
    let nodes: Vec<&String> = graph.keys().collect();

    for (i, &a) in nodes.iter().enumerate() {
        for (j, &b) in nodes.iter().enumerate().skip(i + 1) {
            if !graph[a].contains(b) {
                continue;
            }
            for &c in nodes.iter().skip(j + 1) {
                if graph[a].contains(c) && graph[b].contains(c) {
                    let mut triangle = HashSet::new();
                    triangle.insert(a.clone());
                    triangle.insert(b.clone());
                    triangle.insert(c.clone());
                    triangles.push(triangle);
                }
            }
        }
    }
    triangles
}

fn find_max_clique(graph: &HashMap<String, HashSet<String>>) -> HashSet<String> {
    let mut max_clique = HashSet::new();
    let mut r = HashSet::new();
    let mut p: HashSet<String> = graph.keys().cloned().collect();
    let mut x = HashSet::new();

    bron_kerbosch(graph, &mut r, &mut p, &mut x, &mut max_clique);
    max_clique
}

fn bron_kerbosch(
    graph: &HashMap<String, HashSet<String>>,
    r: &mut HashSet<String>,
    p: &mut HashSet<String>,
    x: &mut HashSet<String>,
    max_clique: &mut HashSet<String>,
) {
    if p.is_empty() && x.is_empty() {
        if r.len() > max_clique.len() {
            max_clique.clear();
            max_clique.extend(r.iter().cloned());
        }
        return;
    }

    let pivot = p
        .iter()
        .chain(x.iter())
        .max_by_key(|v| {
            p.iter()
                .filter(|n| {
                    graph
                        .get(*v)
                        .map_or(false, |neighbors| neighbors.contains(*n))
                })
                .count()
        })
        .unwrap()
        .clone();

    let candidates: Vec<_> = p
        .iter()
        .filter(|v| {
            !graph
                .get(&pivot)
                .map_or(false, |neighbors| neighbors.contains(*v))
        })
        .cloned()
        .collect();

    for v in candidates {
        let neighbors = graph.get(&v).unwrap();

        let mut new_p: HashSet<String> = p
            .iter()
            .filter(|n| neighbors.contains(*n))
            .cloned()
            .collect();
        let mut new_x: HashSet<String> = x
            .iter()
            .filter(|n| neighbors.contains(*n))
            .cloned()
            .collect();

        r.insert(v.clone());
        bron_kerbosch(graph, r, &mut new_p, &mut new_x, max_clique);
        r.remove(&v);

        p.remove(&v);
        x.insert(v);
    }
}

fn main() {
    let input = aoc2024::read_input!();

    let mut graph: HashMap<String, HashSet<String>> = HashMap::new();

    for line in input.lines() {
        let (a, b) = line.split_once('-').unwrap();
        graph
            .entry(a.to_string())
            .or_insert_with(HashSet::new)
            .insert(b.to_string());
        graph
            .entry(b.to_string())
            .or_insert_with(HashSet::new)
            .insert(a.to_string());
    }

    // Part 1
    let triangles = find_all_triangles(&graph);
    let t_triangles = triangles
        .iter()
        .filter(|triangle| triangle.iter().any(|node| node.starts_with('t')))
        .count();
    println!("Part 1: {}", t_triangles);

    // Part 2
    let max_clique = find_max_clique(&graph);
    let mut password: Vec<String> = max_clique.into_iter().collect();
    password.sort();
    println!("Part 2: {}", password.join(","));
}
