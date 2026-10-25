
import java.util.*;

public class Maze {

    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.cells());
        int[] result = new int[Math.max(0, w.cells() - 1)];
        int taken = 0;

        for (int i = 0; i < w.count(); i++) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                result[taken++] = i;
            }
        }

        return Arrays.copyOf(result, taken);
    }

    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];

        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        Arrays.sort(order,
                Comparator.comparingInt(i -> w.cost()[i]));

        DisjointSet dsu = new DisjointSet(w.cells());
        int[] result = new int[Math.max(0, w.cells() - 1)];
        int taken = 0;

        for (int index : order) {
            if (dsu.union(w.a()[index], w.b()[index])) {
                result[taken++] = index;

                if (taken == result.length) {
                    break;
                }
            }
        }

        return Arrays.copyOf(result, taken);
    }

    public static long primCost(Input.Walls w) {
        int n = w.cells();

        if (n == 0) {
            return 0;
        }

        int[][] edges = new int[n][];
        int[] degree = new int[n];

        for (int i = 0; i < w.count(); i++) {
            degree[w.a()[i]]++;
            degree[w.b()[i]]++;
        }

        for (int i = 0; i < n; i++) {
            edges[i] = new int[degree[i]];
        }

        int[] filled = new int[n];

        for (int i = 0; i < w.count(); i++) {
            edges[w.a()[i]][filled[w.a()[i]]++] = i;
            edges[w.b()[i]][filled[w.b()[i]]++] = i;
        }

        boolean[] seen = new boolean[n];

        PriorityQueue<Integer> pq = new PriorityQueue<>(
                Comparator.comparingInt(i -> w.cost()[i])
        );

        seen[0] = true;

        for (int edge : edges[0]) {
            pq.add(edge);
        }

        long total = 0;
        int reached = 1;

        while (!pq.isEmpty() && reached < n) {
            int edge = pq.poll();

            int a = w.a()[edge];
            int b = w.b()[edge];

            if (seen[a] && seen[b]) {
                continue;
            }

            int next = seen[a] ? b : a;

            seen[next] = true;
            reached++;
            total += w.cost()[edge];

            for (int e : edges[next]) {
                int other = w.a()[e] == next
                        ? w.b()[e] : w.a()[e];

                if (!seen[other]) {
                    pq.add(e);
                }
            }
        }

        return total;
    }
}
