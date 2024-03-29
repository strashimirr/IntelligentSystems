package com.intelligent.systems.siliding.blocks;


import java.util.ArrayList;
import java.util.List;


class Puzzle implements Comparable {
    private int[][] puzzle;
    private int distanceTravelled = 0;
    private String direction;
    private Puzzle parent;

    Puzzle(int[][] puzzle, int distanceTravelled, String direction) {
        this.puzzle = new int[puzzle.length][puzzle.length];
        for (int i = 0; i < puzzle.length; i++)
            System.arraycopy(puzzle[i], 0, this.puzzle[i], 0, puzzle[i].length);
        this.distanceTravelled = distanceTravelled;
        this.direction = direction;
    }

    private int getHeuristicEvaluation() {
        return this.getDistanceTravelled() + this.getManhattanDistance();
    }


    List<Puzzle> getChildren() {
        Coordinates zeroCoordinates = getZeroCoordinates();
        List<Puzzle> puzzles = new ArrayList<>();
        if (zeroCoordinates.getX() > 0)
            puzzles.add(swapTiles(zeroCoordinates, new Coordinates(zeroCoordinates.getX() - 1, zeroCoordinates.getY()), "down"));
        if (zeroCoordinates.getX() < this.puzzle.length - 1)
            puzzles.add(swapTiles(zeroCoordinates, new Coordinates(zeroCoordinates.getX() + 1, zeroCoordinates.getY()), "up"));
        if (zeroCoordinates.getY() > 0)
            puzzles.add(swapTiles(zeroCoordinates, new Coordinates(zeroCoordinates.getX(), zeroCoordinates.getY() - 1), "right"));
        if (zeroCoordinates.getY() < this.puzzle.length - 1)
            puzzles.add(swapTiles(zeroCoordinates, new Coordinates(zeroCoordinates.getX(), zeroCoordinates.getY() + 1), "left"));
        return puzzles;
    }


    private Puzzle swapTiles(Coordinates zeroCoordinates, Coordinates coordinates, String direction) {
        Puzzle swappedPuzzle = new Puzzle(this.puzzle, this.getDistanceTravelled(), direction);
        swappedPuzzle.setParent(this);
        swappedPuzzle.setTileAtCoordinates(zeroCoordinates, this.getTileAtCoordinates(coordinates));
        swappedPuzzle.setTileAtCoordinates(coordinates, 0);
        return swappedPuzzle;
    }

    private void setTileAtCoordinates(Coordinates coordinates, int i) {
        this.puzzle[coordinates.getX()][coordinates.getY()] = i;
    }

    private int getTileAtCoordinates(Coordinates coordinates) {
        return this.puzzle[coordinates.getX()][coordinates.getY()];
    }

    private int getPermutations() {
        int sum = 0;
        List<Integer> pastList = new ArrayList<>();
        for (int[] aPuzzle : this.puzzle) {
            for (int integer : aPuzzle) {
                sum += getPermutationsForNumber(integer, pastList);
            }
        }
        return sum;
    }

    private int getPermutationsForNumber(int integer, List<Integer> pastList) {
        int temp = integer;
        for (Integer listInteger : pastList) {
            if (temp > listInteger)
                temp--;
        }
        pastList.add(integer);
        temp--;
        return temp;
    }

    private Coordinates getZeroCoordinates() {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if (puzzle[i][j] == 0)
                    return new Coordinates(i, j);
            }
        }
        return null;
    }

    private int getManhattanDistance() {
        int puzzleSize = this.puzzle.length;
        int manhattanSum = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                int currentNumber = this.puzzle[i][j];
                manhattanSum += Math.abs((currentNumber / puzzleSize) - i);
                manhattanSum += Math.abs((currentNumber % puzzleSize) - j);
            }
        }
        return manhattanSum;
    }

    @Override
    public int compareTo(Object o) {
        int compare = Integer.compare(this.getHeuristicEvaluation(), ((Puzzle) o).getHeuristicEvaluation());
        if (compare != 0)
            return compare;
        else return Integer.compare(this.getManhattanDistance(), ((Puzzle) o).getManhattanDistance());
    }

    int getDistanceTravelled() {
        return distanceTravelled;
    }

    private void setDistanceTravelled(int distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    boolean isGoal() {
        setDistanceTravelled(getDistanceTravelled() + 1);
        return getManhattanDistance() == 0;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Puzzle getParent() {
        return parent;
    }

    public void setParent(Puzzle parent) {
        this.parent = parent;
    }
}