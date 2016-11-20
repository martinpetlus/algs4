import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF quickUnion;
    private final WeightedQuickUnionUF quickUnion2;
    
    private final boolean[] sites;
    
    private final int n;
    
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        
        this.n = n;
        this.sites = new boolean[n * n + 2];
        this.quickUnion = new WeightedQuickUnionUF(n * n + 2);
        this.quickUnion2 = new WeightedQuickUnionUF(n * n + 2);
    }
    
    private void check(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    private int index(int row, int col) {
        return (row - 1) * n + col;
    }
    
    private int topSiteIndex() {
        return 0;
    }
    
    private int bottomSiteIndex() {
        return n * n + 1;
    }
    
    private boolean isIndexOpen(int index) {
        return sites[index];
    }
    
    public void open(int row, int col) {
        check(row, col);
        
        if (isOpen(row, col)) return;
        
        int i = index(row, col);
        sites[i] = true;
        
        if (row == 1) {
            quickUnion.union(i, topSiteIndex());
            quickUnion2.union(i, topSiteIndex());
        }
        
        if (row == n) {
            quickUnion.union(i, bottomSiteIndex());
        }
        
        if (row > 1) {
            int up = index(row - 1, col);
            if (isIndexOpen(up)) {
                quickUnion.union(up, i);
                quickUnion2.union(up, i);
            }
        }
        
        if (col < n) {
            int right = index(row, col + 1);
            if (isIndexOpen(right)) {
                quickUnion.union(right, i);
                quickUnion2.union(right, i);
            }
        }
        
        if (row < n) {
            int down = index(row + 1, col);
            if (isIndexOpen(down)) {
                quickUnion.union(down, i);
                quickUnion2.union(down, i);
            }
        }
        
        if (col > 1) {
            int left = index(row, col - 1);
            if (isIndexOpen(left)) {
                quickUnion.union(left, i);
                quickUnion2.union(left, i);
            }
        }
    }
    
    public boolean isOpen(int row, int col) {
        check(row, col);
        return isIndexOpen(index(row, col));
    }
    
    public boolean isFull(int row, int col) {
        check(row, col);
        
        return (
            isOpen(row, col) &&
            quickUnion2.connected(topSiteIndex(), index(row, col))
        );
    }
    
    public boolean percolates() {
        return quickUnion.connected(topSiteIndex(), bottomSiteIndex());
    }
    
//    public static void main(String[] args) {
//        Percolation p = new Percolation(3);
//        p.open(1,3);
//        p.open(2,3);
//        p.open(3,3);
//        p.open(3,1);
//        System.out.println(p.isFull(3,1));
//    }
}