package com.quandoo.assignment.gameoflife.models;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class LifeTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldCellExistWhenWasAdded() {
        Life life = new Life(1000);
        life.initializeGrid();

        assertTrue(life.isCellExists(1, 1));
    }

    @Test
    public void shouldCellDieWhenIsLonely() {
        Life life = new Life(1000);
        life.resetGrid(Life.getGrid());

        Life.getGrid()[1][1] = 1;
        life.generateNextGeneration();

        assertFalse(life.isCellExists(1, 1));
    }

    @Test
    public void shouldLivingCellStillLiveWhenHasTwoOrThreeNeighbours() {
        // test for two neighbours
        Life life = new Life(1000);
        life.resetGrid(Life.getGrid());

        Life.getGrid()[2][2] = 1;
        Life.getGrid()[3][2] = 1;
        Life.getGrid()[2][3] = 1;
        life.generateNextGeneration();

        assertTrue(life.isCellExists(2, 2));

        // test for three neighbours
        life = new Life(1000);
        life.resetGrid(Life.getGrid());

        Life.getGrid()[2][2] = 1;
        Life.getGrid()[3][2] = 1;
        Life.getGrid()[2][3] = 1;
        Life.getGrid()[3][3] = 1;
        life.generateNextGeneration();

        assertTrue(life.isCellExists(2, 2));
    }

    @Test
    public void shouldLivingCellDieWhenHasLessThanTwoNeighbours() {
        Life life = new Life(1000);
        life.resetGrid(Life.getGrid());

        Life.getGrid()[2][2] = 1;
        Life.getGrid()[3][2] = 1;
        life.generateNextGeneration();

        assertFalse(life.isCellExists(2, 2));
    }

    @Test
    public void shouldLivingCellDieWhenHasMoreThanThreeNeighbours() {
        Life life = new Life(1000);
        life.resetGrid(Life.getGrid());

        Life.getGrid()[2][2] = 1;
        Life.getGrid()[3][2] = 1;
        Life.getGrid()[2][3] = 1;
        Life.getGrid()[3][3] = 1;
        Life.getGrid()[1][3] = 1;
        life.generateNextGeneration();

        assertFalse(life.isCellExists(2, 2));
    }

}