package com.askoog.aoc2018;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dec06 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec06.txt").toURI()));
		String line = lines.get(0);

		List<int[]> coordinates = lines.stream().map(l -> l.split(","))
				.map(s -> new int[] { Integer.parseInt(s[0].trim()), Integer.parseInt(s[1].trim()) })
				.collect(toList());
//		solvePart1(coordinates);
		solvePart2(coordinates);
	}

	private static void solvePart2(List<int[]> coordinates) {
		int xmax = coordinates.stream().mapToInt(i -> i[0]).max().getAsInt();
		int ymax = coordinates.stream().mapToInt(i -> i[1]).max().getAsInt();
		
		System.out.println(xmax + "," + ymax);
	
		// Calculate cells with distance to all coordinates < 10000
		int[][] field = new int[xmax + 1][ymax + 1];
		for (int x = 0; x <= xmax; x++) {
			for (int y = 0; y <= ymax; y++) {
				int sum = 0;
				for (int j = 0; j < coordinates.size(); j++) {
					int[] c = coordinates.get(j);
					int dist = Math.abs(x-c[0]) + Math.abs(y-c[1]);
					sum += dist;
				}
				if (sum < 10000) {
					field[x][y] = sum;
				}
			}
		}
		//dump(field);
		
		int max = 0;
		int[][] field2 = new int[xmax + 1][ymax + 1];
		// Find max area with cells having distance to all coordinates < 10000
		for (int x = 0; x <= xmax; x++) {
			for (int y = 0; y <= ymax; y++) {
				// If we've checked neighbors, do not calculate again
				if (x > 0 && field2[x - 1][y] != 0) {
					field2[x][y] = field2[x - 1][y];
				} else if (y > 0 && field2[x][y - 1] != 0) {
					field2[x][y] = field2[x][y - 1];
				} else {
					int size  = getFieldSize(field, x, y);
					field2[x][y] = size;
					if (size > max) {
						max = size;
					}
				}
			}
		}
		System.out.println(max);	
	}

	// Flood fill with a queue to avoid stack overflow on large fields
	private static int getFieldSize(int[][]field, int x0, int y0) {
		boolean[][] visited = new boolean[field.length][field[0].length];
		List<int[]> queue = new ArrayList<>();
		queue.add(new int[] {x0,y0});

		int fieldSize = 0;
		while (!queue.isEmpty()) {
			int[] c = queue.remove(0);
			int x = c[0];
			int y = c[1];
			if (!visited[x][y]) {
				visited[x][y] = true;
				if (field[x][y] != 0) {
					fieldSize++;
					if (x > 0) {
						queue.add(new int[]{ x - 1, y});
					}
					if (y > 0) {
						queue.add(new int[]{ x, y - 1});
					}
					if (x < field.length - 1) {
						queue.add(new int[]{ x + 1, y});
					}
					if (y < field[x].length - 1) {
						queue.add(new int[]{ x, y + 1});
					}
				}
			}
		}
		return fieldSize;
	}

	private static void solvePart1(List<int[]> coordinates) {
		int xmax = coordinates.stream().mapToInt(i -> i[0]).max().getAsInt();
		int ymax = coordinates.stream().mapToInt(i -> i[1]).max().getAsInt();
		
		System.out.println(xmax + "," + ymax);
	
		int[][] field = new int[xmax + 1][ymax + 1];
		for (int x = 0; x <= xmax; x++) {
			for (int y = 0; y <= ymax; y++) {
				int min = Integer.MAX_VALUE;
				boolean duplicate = false;
				for (int j = 0; j < coordinates.size(); j++) {
					int[] c = coordinates.get(j);
					int dist = Math.abs(x-c[0]) + Math.abs(y-c[1]);
					if (dist == min) {
						// Equal dist to two coords
						duplicate = true;
					} else if (dist < min) {
						field[x][y] = j;
						min = dist;
						duplicate = false;						
						System.out.println(x + "," + y + " best match so far " + j + "("+c[0] + "," +c[1]+") with dist " + min);
					}					
				}
				if (duplicate) {
					field[x][y] = -1;
				}					
			}
			System.out.println();
		}
		dump(field);
		
		int[] sizes = new int[coordinates.size()];
		for (int y = 0; y <= ymax; y++) {
			int c = field[0][y];
			if (c >= 0) {
				sizes[c] = -1;
			}
			
			c = field[xmax][y];
			if (c >= 0) {
				sizes[c] = -1;
			}
		}
		for (int x = 0; x <= xmax; x++) {
			int c = field[x][0];
			if (c >= 0) {
				sizes[c] = -1;
			}
			
			c = field[x][ymax];
			if (c >= 0) {
				sizes[c] = -1;
			}
		}

		for (int x = 0; x <= xmax; x++) {
			for (int y = 0; y <= ymax; y++) {
				int c = field[x][y];
				if (c >= 0 && sizes[c] != -1) {
					sizes[c] = sizes[c] + 1;
				}

			}
		}
		for (int j : sizes) {
			System.out.println(j);
		}
		int max = Arrays.stream(sizes).max().getAsInt();
		System.out.println(max);
	}

	private static void dump(int[][] field) {
		for (int y = 0; y < field[0].length; y++) {
			for (int x = 0; x < field.length; x++) {
				if (field[x][y] == -1) {
					System.out.print(".");
				} else {
					System.out.print(field[x][y]);
				}		
			}
			System.out.println();
		}
	}
}
