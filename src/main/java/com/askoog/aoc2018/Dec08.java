package com.askoog.aoc2018;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Dec08 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec08.txt").toURI()));

		List<Integer> collect = Arrays.stream(lines.get(0).split(" ")).map(Integer::parseInt).collect(toList());
		Queue<Integer> input = new ArrayDeque<>(collect);
		Stack<Node> nodes = new Stack<>();
		Node root = new Node(input.poll(), input.poll());
		Node node = root; 
		nodes.add(node);
		List<Integer> metadata = new ArrayList<>();
		while(!input.isEmpty()) {
			Node n = nodes.peek();
			System.out.println("poll " + n.getNumChildren() + " " + n.getNumMetadataItems());
			if (n.hasAllChildren()) {
				nodes.pop();
				for (int i = 0; i < n.getNumMetadataItems(); i++) {
					int m = input.poll();
					System.out.println("+" + m);
					metadata.add(m);
					n.addMetadata(m);
				}
			} else {
				Node child = new Node(input.poll(), input.poll());
				System.out.println(child.getNumChildren() + " " + child.getNumMetadataItems());
				nodes.add(child);
				n.addChild(child);
			}
			
		}
		
		int sum = metadata.stream().mapToInt(i -> (int)i).sum();
		System.out.println(sum);
		
		int value = root.getValue();
		System.out.println(value);
		
	}


	private static class Node {
		int numChildren;
		int numMetadataItems;
		List<Node> children = new ArrayList<>();
		List<Integer> metadata = new ArrayList<>();
		
		public Node(int numChildren, int numMetadataItems) {
			super();
			this.numChildren = numChildren;
			this.numMetadataItems = numMetadataItems;
		}

		private boolean hasAllChildren() {
			return getNumChildren() == getChildren().size();
		}		
		
		public List<Node> getChildren() {
			return children;
		}
		
		public void addChild(Node n) {
			children.add(n);
		}
		
		public void addMetadata(int metadata) {
			this.metadata.add(metadata);
		}

		public int getNumChildren() {
			return numChildren;
		}

		public int getNumMetadataItems() {
			return numMetadataItems;
		}

		public int getValue() {
			if (numChildren == 0) {
				return metadata.stream().mapToInt(i -> (int)i).sum();
			} else {
				int sum = 0;
				for (Integer m : metadata) {
					m--;
					if (m >= 0 && m < children.size()) {
						sum += children.get(m).getValue();
					}
				} 
				return sum;
			}
		}
		
	}
}
