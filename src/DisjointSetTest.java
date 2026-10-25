
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);

        if (!ok) {
            failed++;
        }
    }

    public static void main(String[] args) {
        DisjointSet d = new DisjointSet(6);

        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart");

        check(d.union(0, 1), "union(0, 1)");
        check(d.connected(0, 1), "0 and 1 are connected");

        check(!d.union(1, 0), "duplicate union returns false");
        check(d.count() == 5, "5 sets after one union");

        d.union(2, 3);
        d.union(1, 3);

        check(d.connected(0, 2), "0 and 2 are connected");
        check(d.count() == 3, "3 components remain");

        check(d.find(4) == 4 && d.find(5) == 5,
                "single cells are their own roots");

        DisjointSet big = new DisjointSet(10000);

        for (int i = 1; i < 10000; i++) {
            big.union(i - 1, i);
        }

        check(big.connected(0, 9999), "large chain connected");
        check(big.count() == 1, "one component remains");
        check(!big.union(200, 200), "self union returns false");
        check(!big.union(0, 9999), "same group union returns false");
        check(big.count() == 1, "component count unchanged");

        if (failed == 0) {
            System.out.println("All checks passed.");
        } else {
            System.out.println(failed + " checks failed.");
        }
    }
}
