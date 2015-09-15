package com.quandoo.assignment.gameoflife.models;

public class Life {

    public static final int ROWS = 20;
    public static final int COLS = 20;
    private static final int[][] _lifeGrid = new int[COLS][ROWS];
    public static int CELL_SIZE;
    public static int AREA;

    public Life(int length) {
        this.AREA = length;
        CELL_SIZE = length / ROWS;
        initializeGrid();
    }

    public static int[][] getGrid() {
        return _lifeGrid;
    }

    public void initializeGrid() {
        resetGrid(_lifeGrid);

        _lifeGrid[8][(ROWS / 2) - 1] = 1;
        _lifeGrid[8][(ROWS / 2) + 1] = 1;
        _lifeGrid[9][(ROWS / 2) - 1] = 1;
        _lifeGrid[9][(ROWS / 2) + 1] = 1;
        _lifeGrid[10][(ROWS / 2) - 1] = 1;
        _lifeGrid[10][(ROWS / 2)] = 1;
        _lifeGrid[10][(ROWS / 2) + 1] = 1;

        _lifeGrid[1][1] = 1;
        _lifeGrid[1][2] = 1;
        _lifeGrid[1][3] = 1;
        _lifeGrid[2][2] = 1;
        _lifeGrid[2][3] = 1;
        _lifeGrid[2][4] = 1;
    }

    public void generateNextGeneration() {
        int neighbours;
        int minimum = 2;
        int maximum = 3;
        int spawn = 3;

        int[][] nextGenerationLifeGrid = new int[COLS][ROWS];

        for (int h = 0; h < COLS; h++) {
            for (int w = 0; w < ROWS; w++) {
                neighbours = calculateNeighbours(h, w);

                if (_lifeGrid[h][w] != 0) {
                    if ((neighbours >= minimum) && (neighbours <= maximum)) {
                        nextGenerationLifeGrid[h][w] = neighbours;
                    }
                } else {
                    if (neighbours == spawn) {
                        nextGenerationLifeGrid[h][w] = spawn;
                    }
                }
            }
        }
        copyGrid(nextGenerationLifeGrid, _lifeGrid);
    }

    public void resetGrid(int[][] grid) {
        for (int h = 0; h < COLS; h++) {
            for (int w = 0; w < ROWS; w++) {
                grid[h][w] = 0;
            }
        }
    }

    private int calculateNeighbours(int y, int x) {
        int total = (_lifeGrid[y][x] != 0) ? -1 : 0;
        for (int h = -1; h <= +1; h++) {
            for (int w = -1; w <= +1; w++) {
                if (_lifeGrid[(COLS + (y + h)) % COLS][(ROWS + (x + w))
                        % ROWS] != 0) {
                    total++;
                }
            }
        }
        return total;
    }

    private void copyGrid(int[][] source, int[][] destination) {
        for (int h = 0; h < COLS; h++) {
            for (int w = 0; w < ROWS; w++) {
                destination[h][w] = source[h][w];
            }
        }
    }

    public boolean isCellExists(int h, int w) {
        return _lifeGrid[h][w] != 0;
    }
}