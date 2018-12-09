package com.askoog.aoc2018;

import java.util.Arrays;

public class Dec09 {

	public static void main(String[] args) throws Exception {

		int[][] examples = new int[][] { { 9, 25, 32 }, { 10, 1618, 8317 }, { 13, 7999, 146373 }, { 17, 1104, 2764 },
				{ 21, 6111, 54718 }, { 30, 5807, 37305 }
		};

		for (int[] example : examples) {

			int numPlayers = example[0];
			int numMarbles = example[1];

			long max = play(numPlayers, numMarbles);
			if (max != example[2]) {
				System.out.println("WRONG");
			}
		}
		
		play(463 , 71787 ); 
		play(463 , 7178700); 
	}


	
	private static long play(int numPlayers, int numMarbles) {
		int currentMarble = 1;
		int currentPlayer = 0;
		long[] scores = new long[numPlayers];
		// Initial, naive implementation using with a list<Integer>, which took a looooong time to execute for the larger example...
		// Instead, build our own ring structure of marbles...
		Marble m = new Marble();
		
		while (currentMarble <= numMarbles) {
			if (currentMarble % 23 == 0) {
				scores[currentPlayer] += currentMarble;
				m = m.left(7);
				scores[currentPlayer] += m.value;
				m = m.remove();
			} else {
				m = m.right(1);
				m = m.addRight(currentMarble);
			}
			currentMarble++;
			currentPlayer = (currentPlayer + 1) % numPlayers;
		}
		long max = Arrays.stream(scores).max().getAsLong();
		System.out.println(max);
		return max;
	}

	private static class Marble {
		int value;
		Marble left;
		Marble right;
		
		public Marble() {
			value = 0;
			left = this;
			right = this;
		}
		
		public Marble(int value, Marble left, Marble right) {
			super();
			this.value = value;
			this.left = left;
			left.right = this;
			this.right = right;
			right.left = this;
		}
		
		public Marble addRight(int value) {
			return new Marble(value, this, this.right);
		}
		
		public Marble remove() {
			this.left.right = this.right;
			this.right.left = this.left;
			return right;
		}
		
		public Marble left(int i) {
			if (i == 0) {
				return this;
			}
			return left.left(i - 1);
		}
		public Marble right(int i) {
			if (i == 0) {
				return this;
			}
			return right.right(i - 1);
		}

		public void dump() {
			Marble m = right;
			System.out.print(value);
			while(m != this) {
				System.out.print(" " + m.value);
				m = m.right;
			}
			System.out.println();
		}
	}}
