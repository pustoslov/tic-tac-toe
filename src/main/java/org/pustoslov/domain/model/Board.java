package org.pustoslov.domain.model;

public class Board {
  private final int[][] matrix;
  public static final int SIZE = 3;

  public Board() {
    matrix = new int[SIZE][SIZE];
  }

  public Board(int[][] matrix) {
    this.matrix = matrix;
  }

  public int[][] getMatrix() {
    return matrix;
  }

  public int getCell(int row, int col) {
    if (row < 0 || row >= matrix.length || col < 0 || col >= matrix.length) {
      throw new IndexOutOfBoundsException("Invalid cell coordinates: " + row + "," + col);
    }
    return matrix[row][col];
  }

  public void setCell(int row, int col, int value) {
    if (row < 0 || row >= matrix.length || col < 0 || col >= matrix.length) {
      throw new ArrayIndexOutOfBoundsException("Invalid cell coordinates: " + row + "," + col);
    }
    matrix[row][col] = value;
  }
}
