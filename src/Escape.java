
import java.util.*;

public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();

        if (n == 0) {
            return new Result(new int[0], 0, 0);
        }

        int[][] open = Graphs.open(w, broken);
        int deadEnds = 0;

        for (int[] neighbours : open) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        int[] stack = new int[n];
        int[] nextDirection = new int[n];

        int top = 0;
        int visitedCount = 1;

        stack[0] = 0;
        visited[0] = true;

        int cols = w.cols();

        while (top >= 0 && !visited[n - 1]) {
            int cell = stack[top];

            if (nextDirection[cell] == 4) {
                top--;
                continue;
            }

            int direction = nextDirection[cell]++;
            int row = cell / cols;
            int col = cell % cols;
            int neighbour = -1;

            if (direction == 0 && row > 0) {
                neighbour = cell - cols;
            }
            if (direction == 1 && col + 1 < cols) {
                neighbour = cell + 1;
            }
            if (direction == 2 && row + 1 < w.rows()) {
                neighbour = cell + cols;
            }
            if (direction == 3 && col > 0) {
                neighbour = cell - 1;
            }

            if (neighbour == -1 || visited[neighbour]) {
                continue;
            }

            boolean hasDoor = false;

            for (int candidate : open[cell]) {
                if (candidate == neighbour) {
                    hasDoor = true;
                    break;
                }
            }

            if (!hasDoor) {
                continue;
            }

            visited[neighbour] = true;
            visitedCount++;
            parent[neighbour] = cell;
            stack[++top] = neighbour;
        }

        if (!visited[n - 1]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }

        int length = 0;

        for (int x = n - 1; x != -1; x = parent[x]) {
            length++;
        }

        int[] path = new int[length];
        int current = n - 1;

        for (int i = length - 1; i >= 0; i--) {
            path[i] = current;
            current = parent[current];
        }

        return new Result(path, visitedCount, deadEnds);
    }
}

