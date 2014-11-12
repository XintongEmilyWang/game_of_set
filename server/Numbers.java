package lab5.server;

/**
 *   Numbers
 *   helper class to support randomly generated cards to random location 
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 Numbers.java
 */

public class Numbers {
	private int MAX;
	private int count = 0;
	public int[] nums;

	/**
	 * constructor
	 * @param x
	 */
	public Numbers(int x) {
		MAX = x;
		nums = new int[MAX];
		while (count < MAX)
			drawNum();
	}

	/**
	 * randomly draw number
	 */
	public void drawNum() {
		int num = (int) (Math.random() * MAX) + 1;
		int index = 0;
		boolean loop = true;
		while (loop) {
			if (num == nums[index])
				loop = false;
			if (num != nums[index] && index == count) {
				nums[count] = num;
				count++;
				loop = false;
			}
			index++;
		}
	}
}
