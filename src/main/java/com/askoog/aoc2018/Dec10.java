package com.askoog.aoc2018;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class Dec10 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec10.txt").toURI()));

		Pattern p = Pattern
				.compile("position=<\\s*(-?[0-9]+),\\s*(-?[0-9]+)> velocity=<\\s*(-?[0-9]+),\\s*(-?[0-9]+)>");
		List<LightPoint> collect = lines.stream()
				.map(p::matcher)
				.peek(matcher -> matcher.matches())
				.map(matcher -> new LightPoint(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
						Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))))
				.collect(toList());
		
		int lastXSize = Integer.MAX_VALUE;
		int lastYSize = Integer.MAX_VALUE;

		boolean[][] lastField = new boolean[0][0];
		for (int i = 0; i < 100_000; i++) {

			collect.forEach(LightPoint::move);
			int xmin = collect.stream().mapToInt(LightPoint::getX).min().getAsInt();
			int ymin = collect.stream().mapToInt(LightPoint::getY).min().getAsInt();
			int xmax = collect.stream().mapToInt(LightPoint::getX).max().getAsInt();
			int ymax = collect.stream().mapToInt(LightPoint::getY).max().getAsInt();

			int xsize = xmax - xmin + 1;
			int ysize = ymax - ymin + 1;
			
			if (xsize > lastXSize && ysize > lastYSize) {
				System.out.println(i + " " + xmin + " - " + xmax + ",  " + ymin + " - " + ymax);
				for (int y = 0; y < lastField[0].length; y++) {
					for (int x = 0; x < lastField.length; x++) {
						System.out.print(lastField[x][y] ? '#' : '.');
					}
					System.out.println();
				}
				return;
			}
			if (xsize < 200) {
				boolean[][] field = new boolean[xmax - xmin + 1][ymax - ymin + 1];
				collect.stream().forEach(l -> field[l.getX() - xmin][l.getY() - ymin] = true);
				lastField = field;
			}
			lastXSize = xsize;
			lastYSize = ysize;
		}

	}

	private static class LightPoint {
		private int x;
		private int y;
		private final int xVelocity;
		private final int yVelocity;

		public LightPoint(int x, int y, int xVelocity, int yVelocity) {
			super();
			this.x = x;
			this.y = y;
			this.xVelocity = xVelocity;
			this.yVelocity = yVelocity;
		}

		public void move() {
			x += xVelocity;
			y += yVelocity;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

	}
}
