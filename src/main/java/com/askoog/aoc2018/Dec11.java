package com.askoog.aoc2018;

public class Dec11 {

	public static void main(String[] args) throws Exception {
		int serial = 9221;
		int xsize = 300;
		int ysize = 300;
		//dump(grid);

		assert calculateLevel(122, 79, 57) == -5;
		assert calculateLevel(217, 196, 39) == 0;
		assert calculateLevel(101, 153, 71) == 4;

		int[] coord = getMaxCoord(18, 300, 300, 3);
		System.out.println(coord[0] + "," + coord[1] + ", " + coord[2]);
		coord = getMaxCoord(42, 300, 300, 3);
		System.out.println(coord[0] + "," + coord[1] + ", " + coord[2]);

		// Part 1
		coord = getMaxCoord(serial, xsize, ysize, 3);
		System.out.println(coord[0] + "," + coord[1] + ", " + coord[2]);

		// Part 2
		int[] best = new int[4];
		for (int i = 1; i <= xsize; i++) {
			coord = getMaxCoord(serial, xsize, ysize, i);
			if (coord[2] > best[2]) {
				best = coord;
				System.out.println("? " + best[0] + "," + best[1] + ", " + best[3] + " = " + best[2]);
			}
		}
		System.out.println(best[0] + "," + best[1] + ", " + best[3] + " = " + best[2]);
	}

	private static int[] getMaxCoord(int serial, int xsize, int ysize, int size) {
		int[][] grid = createGrid(xsize, ysize, serial);
		int[][] square = toSquare(grid, size);
		return getMaxCoord(grid, square, size);
	}

	private static void dump(int[][] square) {
		for (int y = 0; y < square[0].length; y++) {
			for (int x = 0; x < square.length; x++) {
				System.out.print(square[x][y] + " ");
			}
			System.out.println();
		}
	}

	private static int[] getMaxCoord(int[][] grid, int[][] square, int size) {
		int[] coord = new int[4];
		int max = 0;
		for (int x = 0; x < grid.length - size; x++) {
			for (int y = 0; y < grid[x].length - size; y++) {
				if (max < square[x][y]) {
					max = square[x][y];
					coord[0] = x + 1;
					coord[1] = y + 1;
					coord[2] = max;
					coord[3] = size;
				}
			}
		}
		return coord;
	}

	private static int[][] toSquare(int[][] grid, int size) {
		int[][] square = new int[grid.length][grid[0].length];
		for (int x = 0; x < grid.length - size; x++) {
			for (int y = 0; y < grid[x].length - size; y++) {
				int sum = 0;
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						sum += grid[x + i][y + j];
					}
				}
				square[x][y] = sum;
			}
		}
		return square;
	}

	private static int[][] createGrid(int xsize, int ysize, int serial) {
		int[][] grid = new int[xsize][ysize];

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				grid[x][y] = calculateLevel(x, y, serial);
			}
		}
		return grid;
	}

	private static int calculateLevel(int x, int y, int serial) {
		// Find the fuel cell's rack ID, which is its X coordinate plus 10.
		int id = (x + 1) + 10;
		// Begin with a power level of the rack ID times the Y coordinate.
		int level = id * (y + 1);
		// Increase the power level by the value of the grid serial number (your puzzle input).
		level += serial;
		// Set the power level to itself multiplied by the rack ID.
		level = level * id;
		// Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
		if (level < 100) {
			level = 0;
		} else {
			String s = String.valueOf(level);
			level = Character.digit(s.charAt(s.length() - 3), 10);
		}
		// Subtract 5 from the power level.
		level -= 5;
		return level;
	}
}
