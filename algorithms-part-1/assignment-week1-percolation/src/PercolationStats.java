import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_INTERVAL = 1.96;
    private final int trialsCount;
    private final double[] ratios;

    /**
     * Performs T independent computational experiments on an N-by-N grid.
     */
    public PercolationStats(int n, int trials) {
        Percolation currentPercolation;
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Given n <= 0 or trials <= 0");
        }
        trialsCount = trials;
        ratios = new double[trialsCount];
        for (int expNum = 0; expNum < trialsCount; expNum++) {
            currentPercolation = new Percolation(n);
            while (!currentPercolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!currentPercolation.isOpen(row, col)) {
                    currentPercolation.open(row, col);
                }
            }
            ratios[expNum] = (double) currentPercolation.numberOfOpenSites() / (n * n);
        }
    }

    /**
     * Sample mean of percolation threshold.
     */
    public double mean() {
        return StdStats.mean(ratios);
    }

    /**
     * Sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return StdStats.stddev(ratios);
    }

    /**
     * Returns lower bound of the 95% confidence interval.
     */
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_INTERVAL * stddev()) / Math.sqrt(trialsCount));
    }

    /**
     * Returns upper bound of the 95% confidence interval.
     */
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_INTERVAL * stddev()) / Math.sqrt(trialsCount));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
