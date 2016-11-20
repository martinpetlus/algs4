import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private final double[] fractions;
    
    private final int t;
    
    private final int n;
    
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        
        this.t = trials;
        this.n = n;
        this.fractions = new double[trials];
        
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            
            do {
                while (true) {
                    int row = randomInt(n);
                    int col = randomInt(n);
                    
                    if (!p.isOpen(row, col)) {
                        p.open(row, col);
                        break;
                    }
                }
            } while (!p.percolates());
            
            fractions[i] = ((double) countOpen(p)) / (n * n);
        }
    }
    
    private static int randomInt(int n) {
        return StdRandom.uniform(n) + 1;
    }
    
    private double nominator() {
        return 1.96 * stddev();
    }
    
    private int countOpen(Percolation p) {
        int count = 0;
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                if (p.isOpen(row, col)) count++;
            }
        }
        return count;
    }
    
    public double mean() {
        return StdStats.mean(fractions);
    }
        
    public double stddev() {
        return StdStats.stddev(fractions);
    }
        
    public double confidenceLo() {
        return mean() - nominator() / Math.sqrt(t);
    }
        
    public double confidenceHi() {
        return mean() + nominator() / Math.sqrt(t);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(n, t);
        
        System.out.format("mean                    = %f%n", stats.mean());
        System.out.format("stddev                  = %f%n", stats.stddev());
        System.out.format("95%% confidence interval = %f, %f%n",
                          stats.confidenceLo(), stats.confidenceHi());
    }
}