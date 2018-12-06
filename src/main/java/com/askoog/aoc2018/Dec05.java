package com.askoog.aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Dec05 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec05.txt").toURI()));
		String line = lines.get(0);

		String original = line;
		int min = Integer.MAX_VALUE;
		for (char c = 'a'; c <= 'z'; c++) {
			System.out.println("" + c + " " + (char)(c-32));
			line = original.replaceAll("" + c, "");
			line = line.replaceAll("" + (char)(c - 32), "");
			
			int length = removeAllUnits(line);
			System.out.println(length);
			min = Math.min(min,  length);
		}
		System.out.println("Min lentgh: " + min);
	}

	private static int removeAllUnits(String line) {
		char[] chars = line.toCharArray();
		for (int i = 0; i < chars.length - 1; i++) {
			if (chars[i] == chars[i+1] + 32 || 
					chars[i] == chars[i+1] - 32) {
				char[] c2 = new char[chars.length - 2];
				System.arraycopy(chars, 0, c2, 0, i);
				System.arraycopy(chars, i + 2, c2, i, chars.length - i - 2);
				i = -1;
				chars = c2;
			}
		}
		return chars.length;
	}
}
