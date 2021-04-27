package student;

/* 
 * This class is meant to contain your algorithm.
 * You should implement the static method: minAndMax()
 * The input is:
 *   an array of Value objects
 *   
 * These objects are comparable using the compareTo() method.
 * Your goal is to find both the minimum and maximum values using at most 1.5 N comparisons.
 * 
 * You should return an array of two Value objects the minimum and maximum values.
 */

import minAndMax.Value;
 
public class MinAndMax {


	public static Value[] minAndMax(Value[] values) {
		Value t1;
		Value t2;
		Value min = values[0];
		Value max = values[0];
		for (int i = 0; i < values.length; i+=2) {
			t1 = values[i];
			t2 = values[i+1];

			if(t1.compareTo(t2) > 0) {
				if (t1.compareTo(max) > 0){
					max = t1;
				}
				if (t2.compareTo(min) < 0) {
					min = t2;
				}
			} else {
				if (t2.compareTo(max) > 0){
					max = t2;
				}
				if (t1.compareTo(min) < 0) {
					min = t1;
				}
			}
		}
		return new Value[] {min, max};
	}
}