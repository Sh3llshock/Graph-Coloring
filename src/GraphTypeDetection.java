import java.util.*;

// 🟥🟥🟥 Performance profiling is necessary for the following implementations as they were based on a plethora of unrelated sources and thus have no runtime analysis
// while not necessarily accurate the following should be treated as baseline from a purely algorithmic standpoint:
// keep in mind improper data structure use and I/O overhead is not included here which may greatly slow down these implementations, see performance profiling comments
// isChordal: O(n^2)
// isPerfectEliminationOrdering: O(n^3)
// isInterval: O(n^2)
// validateIntervalGraphProperties: O(n^3)
// isTree: O(n + m)
// isConnected: O(n + m)
// dfs: O(n + m)
// hasCycle: O(n + m)
// detectCycle O(n + m)
// isPerfectGraph: O(n + m)
// hasOddHole: O(n^2 + nm)
// detectOddCycleBFS: O(n + m)
// verifyChromaticEqualsClique: O(2^n * n^2)   (seeking an alternative approach)
// checkSubgraphChromaticClique: O(n^2)
// computeChromaticNumber : O(n^2)
// computeCliqueNumber: O(2^n * n^2) //see clique/chrom verification
// generateComplementGraph: O(n^2)
// performLexBFS: O(n^2)
// isClique: O(n^2)
// isLexicographicallyLarger: O(n)  🏎

//🟩🟩🟩 good news however lex bfs can be replaced with a more efficient implementation;
// The same can be said for perfect graph detection
// Additionally all of these should pe parralelisable, yay!
// I have seen a few papers regarding the clique number calculations with some pretty interesting heuristics


public class GraphTypeDetection {
    public static boolean[] checkTypes() {
        SharedData sharedData = SharedData.getInstance();
        return new boolean[]{
                isChordal(sharedData),
                isInterval(sharedData),
                isTree(sharedData),
                isConnected(sharedData),
           //     isPerfectGraph(sharedData) //scrapping temporarily
        };
    }

    private static boolean isChordal(SharedData sharedData) {
        List<Integer> lexBFSOrdering = performLexBFS(sharedData);
        return isPerfectEliminationOrdering(sharedData, lexBFSOrdering);
    }

    private static boolean isPerfectEliminationOrdering(SharedData sharedData, List<Integer> pOrder) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        Set<Integer> activeVertices = new HashSet<>();

        for (int vertex : pOrder) {
            Set<Integer> neighbors = new HashSet<>(drawnCircles[vertex].connections);
            neighbors.retainAll(activeVertices);

            if (!isClique(sharedData, neighbors)) {
                return false;
            }
            activeVertices.add(vertex);
        }
        return true;
    }

    private static boolean isInterval(SharedData sharedData) {
        List<Integer> lexBFSOrdering = performLexBFS(sharedData);
        return validateIntervalGraphProperties(sharedData, lexBFSOrdering);
    }

    private static boolean validateIntervalGraphProperties(SharedData sharedData, List<Integer> pOrder) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        Set<Integer> activeVertices = new HashSet<>();

        for (int vertex : pOrder) {
            Set<Integer> neighbors = new HashSet<>(drawnCircles[vertex].connections);
            neighbors.retainAll(activeVertices);

            // Interval graphs require the active vertices to form a clique
            if (!isClique(sharedData, neighbors)) {
                return false;
            }
            activeVertices.add(vertex);
        }
        return true;
    }

    private static boolean isTree(SharedData sharedData) {
        int numVertices = sharedData.getNumberOfVertices();
        int numEdges = sharedData.getNumberOfEdges();

        if (numEdges != numVertices - 1) {
            return false; // Trees must have exactly (vertices - 1) edges
        }

        return isConnected(sharedData) && !hasCycle(sharedData);
    }

    private static boolean isConnected(SharedData sharedData) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        boolean[] visited = new boolean[sharedData.getNumberOfVertices()];

        dfs(0, visited, drawnCircles);

        for (boolean vertexVisited : visited) {
            if (!vertexVisited) {
                return false;
            }
        }
        return true;
    }

    private static void dfs(int vertex, boolean[] visited, DrawnNodes[] drawnCircles) {
        visited[vertex] = true;
        for (int neighbor : drawnCircles[vertex].connections) {
            if (!visited[neighbor]) {
                dfs(neighbor, visited, drawnCircles);
            }
        }
    }

    private static boolean hasCycle(SharedData sharedData) {
        boolean[] visited = new boolean[sharedData.getNumberOfVertices()];
        return detectCycle(-1, 0, visited, sharedData.getDrawnCircles());
    }

    private static boolean detectCycle(int parent, int vertex, boolean[] visited, DrawnNodes[] drawnCircles) {
        visited[vertex] = true;
        for (int neighbor : drawnCircles[vertex].connections) {
            if (!visited[neighbor]) {
                if (detectCycle(vertex, neighbor, visited, drawnCircles)) {
                    return true;
                }
            } else if (neighbor != parent) {
                return true; // Found a cycle
            }
        }
        return false;
    }

    private static boolean isPerfectGraph(SharedData sharedData) {
        if (hasOddHole(sharedData)) {
            return false;
        }

        if (hasOddHole(generateComplementGraph(sharedData))) {
            return false;
        }

        return verifyChromaticEqualsClique(sharedData);
    }

    private static boolean hasOddHole(SharedData sharedData) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        for (int vertex = 0; vertex < sharedData.getNumberOfVertices(); vertex++) {
            if (detectOddCycleBFS(vertex, drawnCircles)) {
                return true;
            }
        }
        return false;
    }

    private static boolean detectOddCycleBFS(int startNode, DrawnNodes[] drawnCircles) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> levels = new HashMap<>();
        queue.add(startNode);
        levels.put(startNode, 0);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : drawnCircles[current].connections) {
                if (!levels.containsKey(neighbor)) {
                    levels.put(neighbor, levels.get(current) + 1);
                    queue.add(neighbor);
                } else if (levels.get(neighbor) % 2 == levels.get(current) % 2) {
                    int cycleLength = levels.get(current) + levels.get(neighbor) + 1;
                    if (cycleLength >= 5 && cycleLength % 2 == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean verifyChromaticEqualsClique(SharedData sharedData) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        int numVertices = sharedData.getNumberOfVertices();

        for (int subset = 1; subset < (1 << numVertices); subset++) {
            Set<Integer> vertices = new HashSet<>();
            for (int vertex = 0; vertex < numVertices; vertex++) {
                if ((subset & (1 << vertex)) != 0) {
                    vertices.add(vertex);
                }
            }

            if (!checkSubgraphChromaticClique(drawnCircles, vertices)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkSubgraphChromaticClique(DrawnNodes[] drawnCircles, Set<Integer> vertices) {
        int chromaticNumber = computeChromaticNumber(drawnCircles, new ArrayList<>(vertices));
        int cliqueNumber = computeCliqueNumber(drawnCircles, new ArrayList<>(vertices));
        return chromaticNumber == cliqueNumber;
    }

    private static int computeChromaticNumber(DrawnNodes[] drawnCircles, List<Integer> subgraphVertices) {
        Map<Integer, Integer> colors = new HashMap<>();
        for (int vertex : subgraphVertices) {
            Set<Integer> neighborColors = new HashSet<>();
            for (int neighbor : drawnCircles[vertex].connections) {
                if (colors.containsKey(neighbor)) {
                    neighborColors.add(colors.get(neighbor));
                }
            }

            int color = 0;
            while (neighborColors.contains(color)) {
                color++;
            }
            colors.put(vertex, color);
        }

        return colors.values().stream().max(Integer::compareTo).orElse(0) + 1;
    }

    private static int computeCliqueNumber(DrawnNodes[] drawnCircles, List<Integer> vertices) {
        int maxCliqueSize = 0;
        for (int subset = 1; subset < (1 << vertices.size()); subset++) {
            List<Integer> subsetVertices = new ArrayList<>();
            for (int i = 0; i < vertices.size(); i++) {
                if ((subset & (1 << i)) != 0) {
                    subsetVertices.add(vertices.get(i));
                }
            }

            if (isClique(drawnCircles, subsetVertices)) {
                maxCliqueSize = Math.max(maxCliqueSize, subsetVertices.size());
            }
        }

        return maxCliqueSize;
    }

    private static SharedData generateComplementGraph(SharedData sharedData) {
        int numVertices = sharedData.getNumberOfVertices();
        DrawnNodes[] originalGraph = sharedData.getDrawnCircles();
        DrawnNodes[] complementGraph = new DrawnNodes[numVertices];

        for (int i = 0; i < numVertices; i++) {
            complementGraph[i] = new DrawnNodes();
            complementGraph[i].connections = new ArrayList<>();
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j && !originalGraph[i].connections.contains(j)) {
                    complementGraph[i].connections.add(j);
                }
            }
        }

        SharedData complementSharedData = SharedData.getInstance();
        complementSharedData.setDrawnNodes(complementGraph);

        return complementSharedData;
    }

    //helpers
    private static List<Integer> performLexBFS(SharedData sharedData) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        List<Integer> order = new ArrayList<>();
        Map<Integer, LinkedHashSet<Integer>> labels = new HashMap<>();

        for (int vertex = 0; vertex < sharedData.getNumberOfVertices(); vertex++) {
            labels.put(vertex, new LinkedHashSet<>());
        }

        for (int i = 0; i < sharedData.getNumberOfVertices(); i++) {
            int chosenVertex = -1;

            for (int vertex = 0; vertex < sharedData.getNumberOfVertices(); vertex++) {
                if (!order.contains(vertex) &&
                        (chosenVertex == -1 || isLexicographicallyLarger(labels.get(vertex), labels.get(chosenVertex)))) {
                    chosenVertex = vertex;
                }
            }

            order.add(chosenVertex);

            for (int neighbor : drawnCircles[chosenVertex].connections) {
                if (!order.contains(neighbor)) {
                    labels.get(neighbor).add(i);
                }
            }
        }
        return order;
    }

    //And that is why you need a design document, on the other side why teach us if we're not going to use it :)
    private static boolean isClique(SharedData sharedData, Set<Integer> vertices) {
        DrawnNodes[] drawnCircles = sharedData.getDrawnCircles();
        for (int vertex1 : vertices) {
            for (int vertex2 : vertices) {
                if (vertex1 != vertex2 && !drawnCircles[vertex1].connections.contains(vertex2)) {
                    return false;
                }
            }
        }
        return true;
    }

    //And that is why you need a design document, on the other side why teach us if we're not going to use it :)
    private static boolean isClique(DrawnNodes[] drawnCircles, List<Integer> vertices) {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                if (!drawnCircles[vertices.get(i)].connections.contains(vertices.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isLexicographicallyLarger(Set<Integer> a, Set<Integer> b) {
        Iterator<Integer> itA = a.iterator();
        Iterator<Integer> itB = b.iterator();
        while (itA.hasNext() && itB.hasNext()) {
            int compare = Integer.compare(itB.next(), itA.next());
            if (compare != 0) {
                return compare > 0;
            }
        }
        return itB.hasNext();
    }
}