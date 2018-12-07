package com.askoog.aoc2018;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dec07 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec07.txt").toURI()));

		Map<String, Step> steps = new HashMap<>();
		Pattern p = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");

		for (String line : lines) {

			Matcher matcher = p.matcher(line);
			if (matcher.matches()) {
				String from = matcher.group(1);
				String to = matcher.group(2);
				Step fromStep = steps.computeIfAbsent(from, Step::new);
				Step toStep = steps.computeIfAbsent(to, Step::new);

				toStep.addDependency(fromStep);

			}
		}
		StringBuilder sb = new StringBuilder();
		
		int numWorkers = 5;
		int secondsPerStep = 60;
		List<Step> currentWork = new ArrayList<>();
		Step lastStep = new Step("");
		AtomicInteger now = new AtomicInteger(0);
		
		while(steps.values().stream().filter(s -> !s.isDone(now.get())).findAny().isPresent()) {
			lastStep.setDone(true);
			sb.append(lastStep.getName());

			List<Step> collect = steps.values().stream().filter(s -> s.isReady(now.get()))
					.sorted(Comparator.comparing(Step::getName)).collect(toList());
			
			while (currentWork.size() < numWorkers && !collect.isEmpty()) {
				Step step = collect.remove(0);
				currentWork.add(step);
				System.out.println(now + " " + step.getName());
				step.start(lastStep.getEndTime() + step.getName().charAt(0) - 'A' + 1 + secondsPerStep);
			}
			Collections.sort(currentWork, Comparator.comparing(Step::getEndTime));
			if (!currentWork.isEmpty()) {
				lastStep = currentWork.remove(0);
			}
			now.set(lastStep.getEndTime());
		}
		System.out.println(sb);
		System.out.println(lastStep.getEndTime() );
	}

	private static class Step {
		private final Set<Step> dependencies = new HashSet<>();

		private final String name;
		private boolean done = false;
		private int endTime;

		private boolean started;

		public Step(String name) {
			this.name = name;
		}

		public void start(int endTime) {
			this.started = true;
			this.endTime = endTime;
		}
		
		public boolean isStarted() {
			return started;
		}

		public void setDone(boolean done) {
			this.done = done;
		}
		
		public int getEndTime() {
			return endTime;
		}

		public boolean isDone(int now) {
			return done && now >= endTime;
		}

		public String getName() {
			return name;
		}

		public void addDependency(Step step) {
			dependencies.add(step);
		}

		public boolean isReady(int now) {
			return !isStarted() && !isDone(now) && dependencies.stream().allMatch(s -> s.isDone(now));
		}

		@Override
		public String toString() {
			return "Step [name=" + name + ", done=" + done + ", dependencies=" + dependencies + "]";
		}

	}
}
