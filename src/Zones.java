
import java.util.*;

public class Zones {

    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();

        int[][] forward = Graphs.slidesFrom(s);
        int[][] backward = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];

        int[] finishOrder = new int[n];
        int finishCount = 0;

        int[] stack = new int[n];
        int[] nextEdge = new int[n];

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            int top = 0;
            stack[top] = start;
            visited[start] = true;

            while (top >= 0) {
                int cell = stack[top];

                if (nextEdge[cell] < forward[cell].length) {
                    int edge = forward[cell][nextEdge[cell]++];
                    int next = s.to()[edge];

                    if (!visited[next]) {
                        visited[next] = true;
                        stack[++top] = next;
                    }
                } else {
                    finishOrder[finishCount++] = cell;
                    top--;
                }
            }
        }

        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);

        int zone = 0;

        for (int i = finishCount - 1; i >= 0; i--) {
            int start = finishOrder[i];

            if (zoneOf[start] != -1) {
                continue;
            }

            int top = 0;
            stack[top] = start;
            zoneOf[start] = zone;

            while (top >= 0) {
                int cell = stack[top--];

                for (int edge : backward[cell]) {
                    int prev = s.from()[edge];

                    if (zoneOf[prev] == -1) {
                        zoneOf[prev] = zone;
                        stack[++top] = prev;
                    }
                }
            }

            zone++;
        }

        return zoneOf;
    }

    public static long[] routes(
            Input.Slides s, int[] zoneOf, int zoneCount) {

        int[] outDegree = new int[zoneCount];

        for (int i = 0; i < s.count(); i++) {
            int from = zoneOf[s.from()[i]];
            int to = zoneOf[s.to()[i]];

            if (from != to) {
                outDegree[from]++;
            }
        }

        int[][] edgeByZone = new int[zoneCount][];

        for (int i = 0; i < zoneCount; i++) {
            edgeByZone[i] = new int[outDegree[i]];
        }

        int[] filled = new int[zoneCount];

        for (int i = 0; i < s.count(); i++) {
            int from = zoneOf[s.from()[i]];
            int to = zoneOf[s.to()[i]];

            if (from != to) {
                edgeByZone[from][filled[from]++] = i;
            }
        }

        int[] longest = new int[zoneCount];
        Arrays.fill(longest, 1);

        long infinity = Long.MAX_VALUE / 4;
        long[] distance = new long[zoneCount];
        Arrays.fill(distance, infinity);

        distance[zoneOf[0]] = 0;

        int stages = zoneCount == 0 ? 0 : 1;

        for (int zone = 0; zone < zoneCount; zone++) {
            for (int edge : edgeByZone[zone]) {
                int next = zoneOf[s.to()[edge]];

                longest[next] = Math.max(
                        longest[next], longest[zone] + 1
                );

                stages = Math.max(stages, longest[next]);

                if (distance[zone] != infinity) {
                    distance[next] = Math.min(
                            distance[next],
                            distance[zone] + s.time()[edge]
                    );
                }
            }
        }

        return new long[]{
                stages,
                distance[zoneOf[s.n() - 1]]
        };
    }
}
