package nl.remcoder.adventofcode.day18;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Part2 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(ClassLoader.getSystemResource("day18/input").toURI()));

        LumberSpot[][] grid = new LumberSpot[lines.size()][lines.get(0).length()];

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '.':
                        LumberSpot openGround = new LumberSpot();
                        openGround.lumberState = LumberState.OPEN_GROUND;
                        grid[y][x] = openGround;
                        break;
                    case '|':
                        LumberSpot trees = new LumberSpot();
                        trees.lumberState = LumberState.TREES;
                        grid[y][x] = trees;
                        break;
                    case '#':
                        LumberSpot lumberYard = new LumberSpot();
                        lumberYard.lumberState = LumberState.LUMBERYARD;
                        grid[y][x] = lumberYard;
                        break;
                }
            }
        }

        List<LumberStateContainer> previousStates = new ArrayList<>();

        previousStates.add(new LumberStateContainer(translateGridToStates(grid)));

        for (int minute = 0; minute < 1000000000; minute++) {
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    LumberSpot lumberSpot = grid[y][x];
                    int amountOfTrees = countTrees(grid, y, x);
                    int amountOfLumberYards = countLumberYards(grid, y, x);
                    switch (lumberSpot.lumberState) {
                        case OPEN_GROUND:
                            if (amountOfTrees >= 3) {
                                lumberSpot.canChange = true;
                            }
                            break;
                        case TREES:
                            if (amountOfLumberYards >= 3) {
                                lumberSpot.canChange = true;
                            }
                            break;
                        case LUMBERYARD:
                            if (amountOfTrees == 0 || amountOfLumberYards == 0) {
                                lumberSpot.canChange = true;
                            }
                            break;
                    }
                }
            }

            for (LumberSpot[] lumberSpots : grid) {
                for (LumberSpot lumberSpot : lumberSpots) {
                    lumberSpot.change();
                }
            }

            LumberStateContainer lumberStateContainer = new LumberStateContainer(translateGridToStates(grid));

            if (!previousStates.contains(lumberStateContainer)) {
                previousStates.add(lumberStateContainer);
            } else {
                int distance = previousStates.size() - previousStates.indexOf(lumberStateContainer);

                int remainder = 1000000000 - previousStates.indexOf(lumberStateContainer);

                int offset = remainder % distance;

                System.out.println(countGridValue(previousStates.get(previousStates.indexOf(lumberStateContainer) + offset).lumberStates));

                break;
            }
        }
    }

    private static class LumberStateContainer {
        final LumberState[][] lumberStates;

        private LumberStateContainer(LumberState[][] lumberStates) {
            this.lumberStates = lumberStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof LumberStateContainer)) {
                return false;
            }

            LumberStateContainer that = (LumberStateContainer) o;

            return Arrays.deepEquals(lumberStates, that.lumberStates);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(lumberStates);
        }
    }

    private static LumberState[][] translateGridToStates(LumberSpot[][] grid) {
        LumberState[][] lumberStates = new LumberState[grid.length][grid[0].length];

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                lumberStates[y][x] = grid[y][x].lumberState;
            }
        }

        return lumberStates;
    }

    private static int countGridValue(LumberState[][] grid) {
        int amountOfTrees = 0;
        int amountOfLumberYards = 0;
        for (LumberState[] lumberStates : grid) {
            for (LumberState lumberState : lumberStates) {
                switch (lumberState) {
                    case OPEN_GROUND:
                        break;
                    case TREES:
                        amountOfTrees++;
                        break;
                    case LUMBERYARD:
                        amountOfLumberYards++;
                        break;
                }
            }
        }

        return amountOfLumberYards * amountOfTrees;
    }

    private static int countGridValue(LumberSpot[][] grid) {
        int amountOfTrees = 0;
        int amountOfLumberYards = 0;
        for (LumberSpot[] lumberSpots : grid) {
            for (LumberSpot lumberSpot : lumberSpots) {
                switch (lumberSpot.lumberState) {
                    case OPEN_GROUND:
                        break;
                    case TREES:
                        amountOfTrees++;
                        break;
                    case LUMBERYARD:
                        amountOfLumberYards++;
                        break;
                }
            }
        }

        return amountOfLumberYards * amountOfTrees;
    }

    private static void printGrid(LumberSpot[][] grid) {
        for (LumberSpot[] lumberSpots : grid) {
            for (LumberSpot lumberSpot : lumberSpots) {
                switch (lumberSpot.lumberState) {
                    case OPEN_GROUND:
                        System.out.print(".");
                        break;
                    case TREES:
                        System.out.print("|");
                        break;
                    case LUMBERYARD:
                        System.out.print("#");
                        break;
                }
            }
            System.out.println();
        }
    }

    private static int countTrees(LumberSpot[][] grid, int y, int x) {
        int amountOfTrees = 0;

        if (y != 0) {
            if (x != 0) {
                if (grid[y - 1][x - 1].lumberState == LumberState.TREES) {
                    amountOfTrees++;
                }
            }
            if (grid[y - 1][x].lumberState == LumberState.TREES) {
                amountOfTrees++;
            }
            if (x != grid[y].length - 1) {
                if (grid[y - 1][x + 1].lumberState == LumberState.TREES) {
                    amountOfTrees++;
                }
            }
        }
        if (x != 0) {
            if (grid[y][x - 1].lumberState == LumberState.TREES) {
                amountOfTrees++;
            }
        }
        if (x != grid[y].length - 1) {
            if (grid[y ][x + 1].lumberState == LumberState.TREES) {
                amountOfTrees++;
            }
        }
        if (y != grid.length - 1) {
            if (x != 0) {
                if (grid[y + 1][x - 1].lumberState == LumberState.TREES) {
                    amountOfTrees++;
                }
            }
            if (grid[y + 1][x].lumberState == LumberState.TREES) {
                amountOfTrees++;
            }
            if (x != grid[y].length - 1) {
                if (grid[y + 1][x + 1].lumberState == LumberState.TREES) {
                    amountOfTrees++;
                }
            }
        }
        return amountOfTrees;
    }

    private static int countLumberYards(LumberSpot[][] grid, int y, int x) {
        int amountOfLumberYards = 0;

        if (y != 0) {
            if (x != 0) {
                if (grid[y - 1][x - 1].lumberState == LumberState.LUMBERYARD) {
                    amountOfLumberYards++;
                }
            }
            if (grid[y - 1][x].lumberState == LumberState.LUMBERYARD) {
                amountOfLumberYards++;
            }
            if (x != grid[y].length - 1) {
                if (grid[y - 1][x + 1].lumberState == LumberState.LUMBERYARD) {
                    amountOfLumberYards++;
                }
            }
        }
        if (x != 0) {
            if (grid[y][x - 1].lumberState == LumberState.LUMBERYARD) {
                amountOfLumberYards++;
            }
        }
        if (x != grid[y].length - 1) {
            if (grid[y ][x + 1].lumberState == LumberState.LUMBERYARD) {
                amountOfLumberYards++;
            }
        }
        if (y != grid.length - 1) {
            if (x != 0) {
                if (grid[y + 1][x - 1].lumberState == LumberState.LUMBERYARD) {
                    amountOfLumberYards++;
                }
            }
            if (grid[y + 1][x].lumberState == LumberState.LUMBERYARD) {
                amountOfLumberYards++;
            }
            if (x != grid[y].length - 1) {
                if (grid[y + 1][x + 1].lumberState == LumberState.LUMBERYARD) {
                    amountOfLumberYards++;
                }
            }
        }
        return amountOfLumberYards;
    }

    private static class LumberSpot {
        LumberState lumberState;
        boolean canChange;

        void change() {
            if (canChange) {
                switch (lumberState) {
                    case OPEN_GROUND:
                        lumberState = LumberState.TREES;
                        break;
                    case TREES:
                        lumberState = LumberState.LUMBERYARD;
                        break;
                    case LUMBERYARD:
                        lumberState = LumberState.OPEN_GROUND;
                        break;
                }
            }
            canChange = false;
        }
    }

    private enum LumberState {
        OPEN_GROUND,
        TREES,
        LUMBERYARD
    }
}
