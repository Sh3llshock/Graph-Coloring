# Graph Coloring

Interactive graph coloring tool and algorithm playground.
Build, load, and color graphs with classic heuristics and exact search,
then compare results (colors used, time, conflicts) on the same instance.

## Features

- Run multiple algorithms:
  - DSATUR 
  - Jones–Plassmann
  - Backtracking (exact, optional cutoffs)
  - Brute-force for small graphs
- Draw graphs or import from file (edge list / adjacency list).
- Gamemodes such as classic, random and undo for interactive play
- Compare results: number of colors, conflicts, runtime.
- Visualize colorings on the graph; highlight conflicts.
- Scoring system based on time and correctness of each move 

## Background

Graph coloring assigns a color (integer label) to each vertex so that
adjacent vertices receive different colors. The goal is to minimize the number
of colors (the chromatic number). The problem is NP-hard, so heuristics are
common for large graphs, while exact search is used on smaller instances.

## Tech used

- Language: **Java** (Java 17+)
- UI: **JavaFX**
- Build: **Gradle** (wrapper) or **Maven** (optional)
