public class SudokuSolver {
    public static final int SIZE = 9;
    private static final int BOX_SIZE = 3;

    public static boolean isValidPlacement(int[][] board, int row, int col, int val) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == val || board[i][col] == val) {
                return false;
            }
        }
        int boxRow = (row / BOX_SIZE) * BOX_SIZE;
        int boxCol = (col / BOX_SIZE) * BOX_SIZE;
        for (int r = 0; r < BOX_SIZE; r++) {
            for (int c = 0; c < BOX_SIZE; c++) {
                if (board[boxRow + r][boxCol + c] == val) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean solve(int[][] board) {
        int[] cell = findEmptyCell(board);
        if (cell == null) {
            return true;
        }
        int row = cell[0];
        int col = cell[1];
        for (int val = 1; val <= SIZE; val++) {
            if (isValidPlacement(board, row, col, val)) {
                board[row][col] = val;
                if (solve(board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    public static int countSolutions(int[][] board, int cap) {
        int[] cell = findEmptyCell(board);
        if (cell == null) {
            return 1;
        }
        int row = cell[0];
        int col = cell[1];
        int count = 0;
        for (int val = 1; val <= SIZE && count < cap; val++) {
            if (isValidPlacement(board, row, col, val)) {
                board[row][col] = val;
                count += countSolutions(board, cap);
                board[row][col] = 0;
            }
        }
        return count;
    }

    private static int[] findEmptyCell(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    return new int[] {r, c};
                }
            }
        }
        return null;
    }
}
