import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF unionFind, backwashUF;
    private final int gridRowCount;
    private final int virtualTop;
    private final int virtualBottom;
    private final boolean[] isOpen;
    private int openSitesCount;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }
        // create n-by-n grid, with all sites blocked
        // A site is blocked if it's not connected
        openSitesCount = 0;
        gridRowCount = n;
        int gridCellCount = gridRowCount * gridRowCount;
        isOpen = new boolean[gridCellCount];
        virtualBottom = gridCellCount + 1;
        virtualTop = gridCellCount;

        // Grid n x n plus 2 extra nodes: virtual top and bottom
        unionFind = new WeightedQuickUnionUF(gridCellCount + 2);
        backwashUF = new WeightedQuickUnionUF(gridCellCount + 2);
    }

    private void validate(int row, int col) {
        if (row <= 0 || row > gridRowCount) {
            throw new IllegalArgumentException("row not in [1, N]");
        }
        if (col <= 0 || col > gridRowCount) {
            throw new IllegalArgumentException("col not in [1, N]");
        }
    }

    private int toOneDimension(int row, int col) {
        // Important: (1,1) is the top-left corner
        // Example gridRowCount = 2
        // 0 1 is equivalent to (1,1) (1,2)
        // 2 3                  (2,1) (2,2)
        // toOneDimension(1,1) == 0
        // toOneDimension(1,2) == 1
        return ((row - 1) * gridRowCount) + (col - 1);
    }

    public void open(int row, int col) {
        validate(row, col);
        // This method does at most 4 unions: top, left, right, bottom
        int currentNodeIndex = toOneDimension(row, col);

        // Connect with above neighbour if exists
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                unionFind.union(currentNodeIndex, toOneDimension(row - 1, col));
                backwashUF.union(currentNodeIndex, toOneDimension(row - 1, col));
            }
        } else {
            unionFind.union(currentNodeIndex, virtualTop);
            backwashUF.union(currentNodeIndex, virtualTop);
        }
        // Connect to left neighbour if exists
        if (col > 1 && isOpen(row, col - 1)) {
            unionFind.union(currentNodeIndex, toOneDimension(row, col - 1));
            backwashUF.union(currentNodeIndex, toOneDimension(row, col - 1));
        }
        // Connect to right neighbour if exists
        if (col < gridRowCount && isOpen(row, col + 1)) {
            unionFind.union(currentNodeIndex, toOneDimension(row, col + 1));
            backwashUF.union(currentNodeIndex, toOneDimension(row, col + 1));
        }
        // Connect to below neighbour if exists
        if (row < gridRowCount) {
            if (isOpen(row + 1, col)) {
                unionFind.union(currentNodeIndex, toOneDimension(row + 1, col));
                backwashUF.union(currentNodeIndex, toOneDimension(row + 1, col));
            }
        } else {
            unionFind.union(currentNodeIndex, virtualBottom);
        }

        if (!isOpen[currentNodeIndex]) {
            isOpen[currentNodeIndex] = true;
            openSitesCount++;
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isOpen[toOneDimension(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        int currentNodeIndex = toOneDimension(row, col);
        return unionFind.connected(currentNodeIndex, virtualTop) && backwashUF.connected(currentNodeIndex, virtualTop);
    }

    public int numberOfOpenSites() {
        return openSitesCount;
    }

    public boolean percolates() {
        return unionFind.connected(virtualTop, virtualBottom);
    }

    public static void main(String[] args) {
        // test client (optional)
    }
}
