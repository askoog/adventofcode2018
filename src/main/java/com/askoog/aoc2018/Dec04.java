package com.askoog.aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dec04 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec04.txt").toURI()));
		Collections.sort(lines);
		
	
		int numGuards = getMaxNumberOfGuards(lines);
		
		int[][] guards = new int[numGuards + 1][60];
		int[] totalSleep = new int[numGuards + 1];

		sumSleepTimes(lines, guards, totalSleep);
		
		// Find the guard that sleeps the most
		int bestGuard = findMostSleepingGuard(totalSleep);

		System.out.println("Best guard " + bestGuard);
		
		int bestMinute = getBestMinuteForGuard(guards[bestGuard]);

		System.out.println("Best minute: " + bestMinute);
		System.out.println(bestMinute * bestGuard);

		calculateMostFrequentSleepMinute(guards);
		
	}

	private static void calculateMostFrequentSleepMinute(int[][] guards) {
		int guard;
		int max;
		max = 0;
		guard = 0;
		int minute = 0;
		for (int i = 0; i < guards.length; i++) {
			for (int j = 0; j < guards[i].length; j++) {
				if (guards[i][j] > max) {
					max = guards[i][j];
					guard = i;
					minute = j;
				}
			}
		}
		
		System.out.println("Guard " + guard + " sleeps " + max + " times @ " + minute);
		System.out.println(guard * minute);
	}

	private static int getBestMinuteForGuard(int[] sleepingGuard) {
		int max = 0;
		int best = 0;
		for (int i = 0; i < sleepingGuard.length; i++) {
			if (sleepingGuard[i] > max) {
				max = sleepingGuard[i];
				best = i;
			}
		}
		return best;
	}

	private static void sumSleepTimes(List<String> lines, int[][] guards, int[] totalSleep) {
		// calculate sleep time occurrences per minute for the guards and total sleep per guard
		Pattern startPattern = Pattern.compile(".*:([0-9][0-9])\\] Guard #([0-9]*) begins shift");
		Pattern sleepPattern = Pattern.compile(".*:([0-9][0-9])\\] falls asleep");
		Pattern wakeupPattern = Pattern.compile(".*:([0-9][0-9])\\] wakes up");
		
		int guard = -1;
		int startTime = -1;
		for (String line : lines) {
			Matcher m = startPattern.matcher(line);
			if (m.matches()) {
				guard = Integer.parseInt(m.group(2));
			}
			m = sleepPattern.matcher(line);
			if (m.matches()) {
				startTime = Integer.parseInt(m.group(1));
			}
			m = wakeupPattern.matcher(line);
			if (m.matches()) {
				int endTime = Integer.parseInt(m.group(1));
				// System.out.println("Guard "+ guard + " sleeps " + startTime + "-" + endTime);
				for (int i = startTime; i < endTime; i++) {
					guards[guard][i] = guards[guard][i] +1;
				}
				totalSleep[guard] = totalSleep[guard] + (endTime - startTime);
			}
		}
	}

	private static int findMostSleepingGuard(int[] totalSleep) {
		int bestGuard = -1;
		int max = -1;
		for (int i = 0; i < totalSleep.length; i++) {
			if (totalSleep[i] > max) {
				bestGuard = i;
				max = totalSleep[i];
			}
		}
		//System.out.println("Max sleep "+ max);
		return bestGuard;
	}

	private static int getMaxNumberOfGuards(List<String> lines) {
		Pattern startPattern = Pattern.compile(".*:([0-9][0-9])\\] Guard #([0-9]*) begins shift");
		int numGuards = 0;
		
		for (String line : lines) {
			Matcher m = startPattern.matcher(line);
			if (m.matches()) {
				int g = Integer.parseInt(m.group(2));
				numGuards = Math.max(numGuards, g);
			}
		}
		return numGuards;
	}
	
	private static class Guard {
		int id;
		int[] sleeptime = new int[60];
	}
}
