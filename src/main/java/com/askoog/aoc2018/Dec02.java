package com.askoog.aoc2018;

import static java.util.stream.Collectors.toMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Dec02 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec02.txt").toURI()));

		long duplicates = lines.stream().filter(line -> {
			return toMapOfOccurance(line).values().stream().filter(i -> i == 2).findFirst().isPresent();
		}).count();
		long triplets = lines.stream().filter(line -> {
			return toMapOfOccurance(line).values().stream().filter(i -> i == 3).findFirst().isPresent();
		}).count();
		
		System.out.println(duplicates + " * " + triplets + " = " + (duplicates * triplets));
	
	
		lines.stream().filter(line -> findOffByOne(line, lines)).findFirst();
	}

	private static boolean findOffByOne(String line, List<String> lines) {
		for (String string : lines) {
			int diff = 0;
			StringBuilder common = new StringBuilder(); 
			for (int i = 0; i < string.length(); i++) {
				if (string.charAt(i) != line.charAt(i)) {
					diff++;
				} else {
					common.append(string.charAt(i));
				}
			}
			if (diff == 1) {
				System.out.println(line + " " + string + " common=" + common);
				return true;
			}
		}
		return false;
	}

	private static Map<Character, Integer> toMapOfOccurance(String line) {
		return line.chars().mapToObj(c -> (char)c).collect(toMap(Function.identity(), c -> 1, Integer::sum));
	}
	
}
