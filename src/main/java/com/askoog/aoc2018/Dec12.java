package com.askoog.aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dec12 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec12.txt").toURI()));

		char[] chars = lines.remove(0).replaceAll("initial state: ", "").toCharArray();
		boolean[] pots = new boolean[chars.length * 5]; // Magic number, give enough room for the pots to expand right
		int offset = 10; // But they will not expand much to the left
		int idx = offset;
		for (char c : chars) {
			pots[idx++] = c == '#';
		}

		dump(pots);

		lines.remove(0);
		List<Rule> rules = new ArrayList<>();
		for (String line : lines) {
			boolean[] rule = new boolean[5];
			for (int i = 0; i < rule.length; i++) {
				rule[i] = line.charAt(i) == '#';
			}
			Rule r = new Rule(rule, line.substring(line.indexOf(">") + 1).trim().charAt(0) == '#');
			if (r.result) { // Only keep positive rules
				rules.add(r);
			}
		}
		dump(pots);
		
		List<Integer> sums = new ArrayList<>();
		int iterations = 0;
		while(true) {			
			pots = iteration(pots, rules);
			iterations++;
			dump(pots);
			// Try to find a pattern, does the sum of pots change by the same increment?
			int sum = sumPotPositions(pots, offset);
			sums.add(sum);
			if (sums.size() > 10) {
				int diff = sum - sums.get(sums.size() - 2);
				boolean pattern = true;
				for (int i = 0; i < 10; i++) {
					if (sums.get(sums.size() - i - 1) - sums.get(sums.size() - i - 2) != diff) {
						pattern = false;
					}
				}
				if (pattern) {
					break;
				}
			}
		}
		int sum = sumPotPositions(pots, offset);
		pots = iteration(pots, rules);
		int diff = sumPotPositions(pots, offset) - sum;

		long total = (50_000_000_000L - iterations)*diff + sum;
		System.out.println(total);
	}

	private static int sumPotPositions(boolean[] pots, int offset) {
		int sum = 0;
		for (int i = 0; i < pots.length; i++) {
			if (pots[i]) {
				sum += (i - offset);
			}
		}
		return sum;
	}

	private static boolean[] iteration(boolean[] pots, List<Rule> rules) {
		boolean[] newPots = new boolean[pots.length];
		for (int i = 3; i < pots.length; i++) {
			final int index = i - 2;
			newPots[i] = rules.stream().filter(r -> r.matches(pots, index)).map(r -> r.result).findFirst().orElse(false);
		}
		return newPots;
	}

	private static void dump(boolean[] pots) {
		for (boolean b : pots) {
			System.out.print(b ? '#' : '.');
		}
		System.out.println();
	}

	private static class Rule {
		private final boolean[] rule;
		private final boolean result;

		public Rule(boolean[] rule, boolean result) {
			this.rule = rule;
			this.result = result;
		}

		@Override
		public String toString() {
			return "Rule [rule=" + Arrays.toString(rule) + ", result=" + result + "]";
		}

		public boolean matches(boolean[] input, int offset) {
			if (offset + rule.length >= input.length) {
				return false;
			}
			for (int i = 0; i < rule.length; i++) {
				if (input[i + offset] != rule[i]) {
					return false;
				}
			}
			return true;
		}
	}
}
