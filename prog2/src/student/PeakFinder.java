package student;

/* 
 * This class is meant to contain your algorithm.
 * You should implement the static method: findPeak
 * The input is:
 *   a ReadOnlyArrayList<Integer> that only provides .size() and .get(index)
 *   
 * This array is like a mountain profile. The first part of the array rises with every
 * value to a peak value, and then the values decrease with every value until the end.
 * 
 * Your task is to efficiently find the index of the peak value while looking at as few values
 * as possible. 
 * 
 * You should return the index of the peak value.
 */

import peakFinder.ReadOnlyArrayList;

public class PeakFinder {


	// I have implemented binary search
	public static int findPeak(ReadOnlyArrayList<Integer> values) {
		int first = 0;
		int last = values.size() - 1;
		int index = (first + last) / 2;

		int value;
		int rightValue;
		int leftValue;

		while (first <= last) {
			value = values.get(index);

			// check for array bounds
			if (index < last) {
				rightValue = values.get(index+1);
			} else {
				rightValue = values.get(index);
			}

			// check right value
			if (value < rightValue) {
				first = index + 1;
				index = (first + last) / 2;
			} else {
				// check for array bounds
				if (index > first) {
					leftValue = values.get(index-1);
				} else {
					leftValue = values.get(index);
				}

				// check left value
				if (value < leftValue) {
					last = index - 1;
					index = (first + last) / 2;
				} else {
					// check to see if the peak was found
					if (value >= leftValue && value >= rightValue) {
						return index;
					}
				}
			}
		}
		return index;
	}

}