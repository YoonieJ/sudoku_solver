import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;

    private final Random random = new Random();

    public enum Difficulty {
        EASY(40),
        MEDIUM(32),
        HARD(26),
        EXPERT(22);

        final int clueCount;

        Difficulty(int clueCount) {
            this.clueCount = clueCount;
        }
    }

    public int[][] generateFullBoard() {
        int[][] board = new int[SIZE][SIZE];
        fillBoard(board);
        return board;
    }

    public int[][] generatePuzzle(Difficulty difficulty) {
        int[][] puzzle = generateFullBoard();

        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                positions.add(new int[] {r, c});
            }
        }
        Collections.shuffle(positions, random);

        int clues = SIZE * SIZE;
        for (int[] pos : positions) {
            if (clues <= difficulty.clueCount) {
                break;
            }
            int r = pos[0];
            int c = pos[1];
            int backup = puzzle[r][c];
            puzzle[r][c] = 0;

            int[][] copy = deepCopy(puzzle);
            if (SudokuSolver.countSolutions(copy, 2) == 1) {
                clues--;
            } else {
                puzzle[r][c] = backup;
            }
        }
        return puzzle;
    }

    private boolean fillBoard(int[][] board) {
        int[] cell = findEmptyCell(board);
        if (cell == null) {
            return true;
        }
        int row = cell[0];
        int col = cell[1];

        List<Integer> values = new ArrayList<>();
        for (int v = 1; v <= SIZE; v++) {
            values.add(v);
        }
        Collections.shuffle(values, random);

        for (int val : values) {
            if (SudokuSolver.isValidPlacement(board, row, col, val)) {
                board[row][col] = val;
                if (fillBoard(board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private int[] findEmptyCell(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    return new int[] {r, c};
                }
            }
        }
        return null;
    }

    private int[][] deepCopy(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }
}
