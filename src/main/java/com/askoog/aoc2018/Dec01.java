package com.askoog.aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Dec01 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec01.txt").toURI()));

		sum(lines);

		findFirstDuplicate(lines);
	}

	private static void findFirstDuplicate(List<String> lines) {
		Set<Integer> matched = new HashSet<>();
		AtomicInteger freq = new AtomicInteger(0);
		matched.add(0);
		while (true) {
			OptionalInt duplicate = findFirstDuplicate(lines, matched, freq);
			if (duplicate.isPresent()) {
				System.out.println("First duplicate: " + duplicate.getAsInt());
				break;
			}
		}
	}

	private static OptionalInt findFirstDuplicate(List<String> lines, Set<Integer> matched, AtomicInteger freq) {
		return lines.stream().mapToInt(i -> freq.addAndGet(toInteger(i))).filter(i -> !matched.add(i)).findFirst();
	}

	private static void sum(List<String> lines) {
		int sum = lines.stream().mapToInt(Dec01::toInteger).sum();
		System.out.println("Sum: " + sum);
	}

	private static int toInteger(String line) {
		int val = Integer.parseInt(line.substring(1));
		switch (line.charAt(0)) {
			case '+':
				return val;
			case '-':
				return -val;
			default:
				throw new RuntimeException("Invalid value " + line);
		}
	}

}
