import java.util.Scanner;

public class Main {
    private static final int SIZE = 9;

    private static final String ANSI_RESET = "[0m";
    private static final String ANSI_RED = "[31m";
    private static final String ANSI_GREEN = "[32m";
    private static final String ANSI_BOLD = "[1m";

    private static int[][] solution;
    private static int[][] userBoard;
    private static boolean[][] given;

    public static void main(String[] args) {
        System.out.println("Generating a hard Sudoku puzzle...");
        SudokuGenerator generator = new SudokuGenerator();
        int[][] puzzle = generator.generatePuzzle(SudokuGenerator.Difficulty.HARD);

        solution = deepCopy(puzzle);
        SudokuSolver.solve(solution);

        userBoard = deepCopy(puzzle);
        given = new boolean[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                given[r][c] = puzzle[r][c] != 0;
            }
        }

        printInstructions();
        printBoard(userBoard, null);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "set":
                    handleSet(parts);
                    break;
                case "clear":
                    handleClear(parts);
                    break;
                case "show":
                    printBoard(userBoard, null);
                    break;
                case "check":
                    handleCheck();
                    break;
                case "solve":
                    printBoard(solution, null);
                    System.out.println("(This is the full solution.)");
                    break;
                case "help":
                    printInstructions();
                    break;
                case "quit":
                case "exit":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown command. Type 'help' for a list of commands.");
            }
        }
        scanner.close();
    }

    private static void handleSet(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Usage: set <row 1-9> <col 1-9> <value 1-9>");
            return;
        }
        try {
            int row = Integer.parseInt(parts[1]) - 1;
            int col = Integer.parseInt(parts[2]) - 1;
            int val = Integer.parseInt(parts[3]);
            if (!inBounds(row, col) || val < 1 || val > 9) {
                System.out.println("Row/col must be 1-9, value must be 1-9.");
                return;
            }
            if (given[row][col]) {
                System.out.println("That cell is a given clue and cannot be changed.");
                return;
            }
            userBoard[row][col] = val;
            printBoard(userBoard, null);
        } catch (NumberFormatException e) {
            System.out.println("Usage: set <row 1-9> <col 1-9> <value 1-9>");
        }
    }

    private static void handleClear(String[] parts) {
        if (parts.length != 3) {
            System.out.println("Usage: clear <row 1-9> <col 1-9>");
            return;
        }
        try {
            int row = Integer.parseInt(parts[1]) - 1;
            int col = Integer.parseInt(parts[2]) - 1;
            if (!inBounds(row, col)) {
                System.out.println("Row/col must be 1-9.");
                return;
            }
            if (given[row][col]) {
                System.out.println("That cell is a given clue and cannot be changed.");
                return;
            }
            userBoard[row][col] = 0;
            printBoard(userBoard, null);
        } catch (NumberFormatException e) {
            System.out.println("Usage: clear <row 1-9> <col 1-9>");
        }
    }

    private static void handleCheck() {
        boolean[][] wrong = new boolean[SIZE][SIZE];
        boolean complete = true;
        boolean anyWrong = false;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (userBoard[r][c] == 0) {
                    complete = false;
                } else if (userBoard[r][c] != solution[r][c]) {
                    wrong[r][c] = true;
                    anyWrong = true;
                }
            }
        }

        printBoard(userBoard, wrong);

        if (anyWrong) {
            System.out.println(ANSI_RED + "There are incorrect entries (highlighted above). Keep trying!" + ANSI_RESET);
        } else if (!complete) {
            System.out.println("No mistakes so far, but the puzzle isn't complete yet.");
        } else {
            System.out.println(ANSI_GREEN + "Congratulations! You solved the puzzle correctly!" + ANSI_RESET);
        }
    }

    private static boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    private static int[][] deepCopy(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            copy[i] = board[i].clone();
        }
        return copy;
    }

    private static void printInstructions() {
        System.out.println();
        System.out.println(ANSI_BOLD + "=== Sudoku (Hard) ===" + ANSI_RESET);
        System.out.println("Commands:");
        System.out.println("  set <row> <col> <val>  - fill a cell (1-9 for row/col/val)");
        System.out.println("  clear <row> <col>      - clear a cell you filled in");
        System.out.println("  show                   - reprint the board");
        System.out.println("  check                  - check your progress (highlights mistakes)");
        System.out.println("  solve                  - reveal the full solution");
        System.out.println("  help                   - show this message");
        System.out.println("  quit                   - exit");
    }

    private static void printBoard(int[][] board, boolean[][] wrong) {
        System.out.println();
        System.out.println("    1 2 3   4 5 6   7 8 9");
        System.out.println("  +-------+-------+-------+");
        for (int r = 0; r < SIZE; r++) {
            StringBuilder sb = new StringBuilder();
            sb.append(r + 1).append(" | ");
            for (int c = 0; c < SIZE; c++) {
                int val = board[r][c];
                String cellStr = val == 0 ? "." : String.valueOf(val);
                if (wrong != null && wrong[r][c]) {
                    cellStr = ANSI_RED + cellStr + ANSI_RESET;
                } else if (given[r][c]) {
                    cellStr = ANSI_BOLD + cellStr + ANSI_RESET;
                }
                sb.append(cellStr).append(" ");
                if ((c + 1) % 3 == 0) {
                    sb.append("| ");
                }
            }
            System.out.println(sb.toString());
            if ((r + 1) % 3 == 0) {
                System.out.println("  +-------+-------+-------+");
            }
        }
    }
}
