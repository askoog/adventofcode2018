package com.askoog.aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Dec01 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec01.txt").toURI()));
		int sum = lines.stream().mapToInt(line -> {
			int val = Integer.parseInt(line.substring(1));
			switch (line.charAt(0)) {
				case '+':
					return val;
				case '-':
					return -val;
				default:
					throw new RuntimeException("Invalid value " + line);	
			}
		}).sum();
		System.out.println(sum);
	}

}
