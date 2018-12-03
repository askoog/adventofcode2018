package com.askoog.aoc2018;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dec03 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec03.txt").toURI()));
		List<Row> rows = lines.stream().map(Row::new).collect(toList());

		int maxX = rows.stream().mapToInt(Row::getMaxX).max().getAsInt();
		int maxY = rows.stream().mapToInt(Row::getMaxY).max().getAsInt();

		int[][] fabric = new int[Math.max(1000, maxX)][Math.max(1000, maxY)];

		rows.stream().forEach(r -> {
			for (int x = 0; x < r.getSizeX(); x++) {
				for (int y = 0; y < r.getSizeY(); y++) {
					fabric[x + r.getXOffset()][y + r.getYOffset()] = fabric[x + r.getXOffset()][y + r.getYOffset()] + 1;
				}
			}
		});
		int numOverlaps = 0;
		for (int x = 0; x < fabric.length; x++) {
			for (int y = 0; y < fabric[x].length; y++) {
				if (fabric[x][y] > 1) {
					numOverlaps++;
				}
			}
		}

		Row nonOverlappingRow = rows.stream().filter(r -> {
			for (int x = 0; x < r.getSizeX(); x++) {
				for (int y = 0; y < r.getSizeY(); y++) {
					if (fabric[x + r.getXOffset()][y + r.getYOffset()] > 1) {
						return false;
					}
				}
			}
			return true;
		}).findFirst().get();

		System.out.println(maxX + " " + maxY);
		System.out.println(numOverlaps);
		System.out.println(nonOverlappingRow);
	}

	private static class Row {
		//		#1 @ 7,589: 24x11
		int id;
		int x;
		int y;
		int sizeX;
		int sizeY;

		public Row(String line) {
			Pattern p = Pattern.compile("#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)");
			Matcher matcher = p.matcher(line);
			if (matcher.matches()) {
				id = Integer.parseInt(matcher.group(1));
				x = Integer.parseInt(matcher.group(2));
				y = Integer.parseInt(matcher.group(3));
				sizeX = Integer.parseInt(matcher.group(4));
				sizeY = Integer.parseInt(matcher.group(5));
			} else {
				throw new IllegalArgumentException("Invalid input " + line);
			}

		}

		public int getId() {
			return id;
		}

		public int getXOffset() {
			return x;
		}

		public int getYOffset() {
			return y;
		}

		public int getSizeX() {
			return sizeX;
		}

		public int getSizeY() {
			return sizeY;
		}

		public int getMaxX() {
			return x + sizeX;
		}

		public int getMaxY() {
			return y + sizeY;
		}

		@Override
		public String toString() {
			return "Row [id=" + id + ", x=" + x + ", y=" + y + ", sizeX=" + sizeX + ", sizeY=" + sizeY + "]";
		}

	}
}
