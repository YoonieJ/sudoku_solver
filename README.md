# sudoku solver

Terminal sudoku. Generates a hard puzzle, you solve it, it tells you if you got it right.

## Run it

Needs a JDK (21+ is fine, probably works on older too).

```
javac -d out src/*.java
java -cp out Main
```

## Commands

```
set <row> <col> <val>   fill in a cell
clear <row> <col>       clear a cell you filled in
show                    reprint the board
check                   see if you're right so far (wrong cells show in red)
solve                   just show the answer
quit
```

Rows/cols/values are all 1-9. Given clues (the bold numbers) can't be overwritten.

## How the puzzle gets made

`SudokuGenerator` fills a random full board, then pulls clues out one at a time.
After each removal it runs the solver in counting mode to make sure the puzzle
still has exactly one solution — if pulling a cell would make it ambiguous, that
cell stays put. Stops once it's down to ~26 clues, which is roughly what a "hard"
newspaper puzzle looks like.

## Files

- `SudokuSolver.java` — backtracking solver, also counts solutions
- `SudokuGenerator.java` — builds the full grid and carves out the puzzle
- `Main.java` — the actual game loop
