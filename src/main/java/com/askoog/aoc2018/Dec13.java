package com.askoog.aoc2018;

import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Dec13 {

	public static void main(String[] args) throws Exception {
		List<String> lines = Files.readAllLines(Paths.get(Dec01.class.getResource("/dec13.txt").toURI()));

		Cell[][] field = new Cell[lines.get(0).length()][lines.size()];
		List<Cart> carts = new ArrayList<>();

		parseInput(lines, field, carts);

		dump(field, carts);
		while(carts.size() > 1) {
			carts.sort(Cart::compareTo);
			for (Cart cart : carts) {
				cart.move(field);
				List<Cart> crashes = carts.stream().filter(c -> c.isPos(cart.x, cart.y)).collect(toList());
				if (crashes.size() > 1) {
					System.out.println("CRASH! " + cart.x + "," + cart.y);
					crashes.forEach(c -> c.crashed = true);
				}
			}
			carts = carts.stream().filter(c -> !c.crashed).collect(toList());
		}
		System.out.println("Last cart:" + carts.get(0).x + "," + carts.get(0).y);
	}

	private static void parseInput(List<String> lines, Cell[][] field, List<Cart> carts) {
		int x;
		int y = -1;
		for (String string : lines) {
			y++;
			x = -1;
			for (char c : string.toCharArray()) {
				x++;
				switch (c) {
					case '|':
						field[x][y] = Cell.UPDOWN;
						break;
					case '-':
						field[x][y] = Cell.LEFTRIGHT;
						break;
					case '/':
						field[x][y] = Cell.RIGHTTURN;
						break;
					case '\\':
						field[x][y] = Cell.LEFTTURN;
						break;
					case '+':
						field[x][y] = Cell.INTERSECTION;
						break;
					case '>':
						field[x][y] = Cell.LEFTRIGHT;
						carts.add(new Cart(Direction.RIGHT, x, y));
						break;
					case '<':
						field[x][y] = Cell.LEFTRIGHT;
						carts.add(new Cart(Direction.LEFT, x, y));
						break;
					case '^':
						field[x][y] = Cell.UPDOWN;
						carts.add(new Cart(Direction.UP, x, y));
						break;
					case 'v':
						field[x][y] = Cell.UPDOWN;
						carts.add(new Cart(Direction.DOWN, x, y));
						break;
					default:
						field[x][y] = Cell.NULL;
				}
			}
		}
	}

	private static void dump(Cell[][] field, List<Cart> carts) {
		System.out.println(toString(field, carts));
	}

	private static String toString(Cell[][] field, List<Cart> carts) {
		StringBuilder sb = new StringBuilder();
		for (int y1 = 0; y1 < field[0].length; y1++) {
			for (int x1 = 0; x1 < field.length; x1++) {

				int x2 = x1;
				int y2 = y1;
				if (field[x1][y1] == Cell.CRASH) {
					sb.append('X');
				} else {
					char c1 = carts.stream().filter(c -> c.isPos(x2, y2)).map(Cart::toChar).findFirst()
							.orElse(cellToChar(field[x1][y1]));
					sb.append(c1);
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private static char cellToChar(Cell c) {
		switch (c) {
			case UPDOWN:
				return '|';
			case LEFTRIGHT:
				return '-';
			case RIGHTTURN:
				return '/';
			case LEFTTURN:
				return '\\';
			case INTERSECTION:
				return '+';
			case CRASH:
				return 'X';
			default:
				return ' ';
		}
	}

	enum Cell {
		UPDOWN, LEFTRIGHT, RIGHTTURN, LEFTTURN, INTERSECTION, NULL, CRASH
	}

	enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	private static class Cart implements Comparable<Cart>{
		Direction d;
		int x;
		int y;
		int intersectionCount = 0;
		boolean crashed = false;
		
		public Cart(Direction d, int x, int y) {
			this.d = d;
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Cart o) {
			if (y != o.y) {
				return y < o.y ? -1 : 1;
			} 
			return x < o.x ? -1 : 1;
		}
		
		public boolean isPos(int x1, int y1) {
			return this.x == x1 && this.y == y1;
		}

		public char toChar() {
			switch (d) {
				case UP:
					return '^';
				case DOWN:
					return 'v';
				case LEFT:
					return '<';
				case RIGHT:
					return '>';
				default:
					return '?';
			}
		}

		public void move(Cell[][] field) {
			switch (field[x][y]) {
				case UPDOWN:
					if (d == Direction.UP) {
						y--;
					} else if (d == Direction.DOWN) {
						y++;
					}
					break;
				case LEFTRIGHT:
					if (d == Direction.LEFT) {
						x--;
					} else if (d == Direction.RIGHT) {
						x++;
					}
					break;
				case RIGHTTURN:
					if (d == Direction.UP) {
						x++;
						d = Direction.RIGHT;
					} else if (d == Direction.DOWN) {
						x--;
						d = Direction.LEFT;
					} else if (d == Direction.LEFT) {
						y++;
						d = Direction.DOWN;
					} else if (d == Direction.RIGHT) {
						y--;
						d = Direction.UP;
					}
					break;
				case LEFTTURN:
					if (d == Direction.UP) {
						x--;
						d = Direction.LEFT;
					} else if (d == Direction.DOWN) {
						x++;
						d = Direction.RIGHT;
					} else if (d == Direction.RIGHT) {
						y++;
						d = Direction.DOWN;
					} else if (d == Direction.LEFT) {
						y--;
						d = Direction.UP;
					}
					break;
				case INTERSECTION:
					
					if (d == Direction.UP) {
						if (intersectionCount == 0) {
							d = Direction.LEFT;
							x--;
						} else if (intersectionCount == 1) {
							y--;
						} else if (intersectionCount == 2) {
							d = Direction.RIGHT;
							x++;
						}
					} else if (d == Direction.DOWN) {
						if (intersectionCount == 0) {
							d = Direction.RIGHT;
							x++;
						} else if (intersectionCount == 1) {
							y++;
						} else if (intersectionCount == 2) {
							d = Direction.LEFT;
							x--;
						}
					} else if (d == Direction.LEFT) {
						if (intersectionCount == 0) {
							d = Direction.DOWN;
							y++;
						} else if (intersectionCount == 1) {
							x--;
						} else if (intersectionCount == 2) {
							d = Direction.UP;
							y--;
						}
					} else if (d == Direction.RIGHT) {
						if (intersectionCount == 0) {
							d = Direction.UP;
							y--;
						} else if (intersectionCount == 1) {
							x++;
						} else if (intersectionCount == 2) {
							d = Direction.DOWN;
							y++;
						}
					}
					intersectionCount=(intersectionCount+1) % 3;
					break;
				default:
					System.out.println("HUH?");	
			}
		}

	}
}
