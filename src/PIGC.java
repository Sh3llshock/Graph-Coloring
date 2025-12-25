public class PIGC {

    public static void main(String[] args) {
        int[][] adjMatrix = {
                {0, 1, 1, 1, 0, 0},
                {1, 0, 1, 0, 0, 0},
                {1, 1, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 1},
                {0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0}
        };

        int len = adjMatrix.length;
        int[] colors = new int[len];
        boolean[] completed = new boolean[len];

        // splitting the matrix ito more threads, for now 2
        int[] grp1 = new int[len/2];
        int[] grp2 = new int[len - len/2];

        for (int i = 0; i < grp1.length; i++) {
            grp1[i] = i;
        }
        for (int i = 0; i < grp2.length; i++) {
            grp2[i] = i + grp1.length;
        }

        for (int i = 0; i < len; i++) {
            colors[i] = -1;
        }

        while (true) {
            Thread thread1 = new Thread(() -> colorSet(grp1, adjMatrix, colors));
            Thread thread2 = new Thread(() -> colorSet(grp2, adjMatrix, colors));

            thread1.start();
            thread2.start();

            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            boolean issues = resolveIssues(adjMatrix, colors, completed);

            if (!issues) {break;}
        }

        for (int i = 0; i < colors.length; i++) {
            System.out.println("Node: " + i + " color: " + colors[i]);
        }
    }


    private static void colorSet(int[] set, int[][] adjMatrix, int[] colors) {
        for (int node : set) {
            if (colors[node] == -1) {
                colors[node] = smallestColor(node, adjMatrix, colors);
            }
        }
    }

    private static int smallestColor(int node, int[][] adjMatrix, int[] colors) {
        boolean[] usedColors = new boolean[adjMatrix.length];

        for (int i = 0; i < adjMatrix.length; i++) {
            if (adjMatrix[node][i] == 1 && colors[i] != -1) {
                usedColors[colors[i]] = true;
            }
        }

        for (int c = 0; c < adjMatrix.length; c++) {
            if (!usedColors[c]) {
                return c;
            }
        }
        return 0;

    }

    private static boolean resolveIssues(int[][] adjMatrix, int[] colors, boolean[] completed) {
        boolean issues = false;

        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = i + 1; j < adjMatrix.length; j++) {

                if (adjMatrix[i][j] == 1 && colors[i] == colors[j] && colors[i] != -1) {
                    issues = true;

                    if (!completed[i]) {
                        colors[i] = -1;
                    } else if (!completed[j]) {
                        colors[j] = -1;
                    }
                }
            }
        }


        return issues;
    }
}
