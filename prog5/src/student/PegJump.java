package student;
/* 
 * This Student class is meant to contain your algorithm.
 * You should implement the static methods:
 *
 *   solvePegJump - finds a solution and the number of nodes examined in the search
 *                  it should fill in the jumpList argument with the jumps that form
 *                  your solution
 *
 * The input is a PegJumpPuzzle object, which has:
 *   a size, the number of holes numbered 0 .. size()-1
 *   the startHole that is initially empty
 *   an ArrayList of allowed jumps, which are triples (from, over, dest)
 *   a jump takes the peg 'from' over the peg in 'over' (removing it) and into 'dest'
 */
import java.util.ArrayList;
import java.util.Iterator;
import pegJump.*;

public class PegJump {

	public static double solvePegJump(PegJumpPuzzle puzzle, ArrayList<Jump> jumpList) {	
	    // initialize the puzzle
		// make an array to keep tracks of where the pegs are
		// and put pegs in all holes except the starting hole
		boolean pegs[] = new boolean[puzzle.numHoles()];  // hole numbers start at 0
		for (int i = 0; i < puzzle.numHoles(); i++)  
			pegs[i] = true;                      // fill all holes
    	pegs[puzzle.getStartHole()] = false;     // clear starting hole

		// start doing jumps
		for (int i = puzzle.numHoles() -1; i > 0; i--) {

		}

		// so it is passed by reference
		// wasn't sure of a quicker way to do this to be honest
		int[] jumpsFound = new int[1];


		//-----------------------  Design Notes  ----------------------------//
		// I want it to backtrack to solve everything
		// So how I can do this is find something that can be changed, like the peg we are on, and track it
		
		// I assume that jumpIterator will find remaining jumps based on the jumpList arraylist. 
		// So to backtrack, I just need to remove the last jump

		// so it will call solve, go through the whole thing once, then return the number of jumps that it found
		//jumpsFound = solve(jumpCnt, pegs, jumpList, puzzle);
		// What do I want it to do next?
		// add the recursion
		// do {
		// 	jumpsFound = solve(pegs, jumpList, puzzle);
		// } while (jumpsFound < puzzle.numHoles() - 1);

		// now how do I make it backtrack?
		// if I use recursion, then i just need to start the recursion,
		// and then remove the jump from the jumplist if it doesn't work, recursion will handle the rest
		//jumpList will handle the backtracking

		// while (jumpsFound < puzzle.numHoles() - 2) {
		// 	System.out.println(jumpsFound);
		// 	System.out.println(jumpList.size());
		// 	jumpsFound--;
		// 	jumpList.remove(jumpsFound);
		// 	jumpsFound = solve(jumpsFound, pegs, jumpList, puzzle);
		// }

		// now I need to actually recurse it. Because I just have a loop right now. This isn't recursion
		//--------------------------------------------------------------------//

		if (!solve(jumpsFound, pegs, jumpList, puzzle)) {
			System.out.println("Solution not found");
		}

		return jumpsFound[0]; 
	}	

	private static boolean solve(int jumpsFound[], boolean[] pegs, ArrayList<Jump> jumpList, PegJumpPuzzle puzzle) {
		Iterator<Jump> jitr = puzzle.jumpIterator();

		while (jitr.hasNext()) {
			Jump j = jitr.next();
			int from = j.getFrom();
			int over = j.getOver();
			int dest = j.getDest();
			if (pegs[from] && pegs[over] && !pegs[dest]) {
				// found a valid jump
				jumpList.add(j);       // add to the result list
				pegs[from] = false;    // do the jump
				pegs[over] = false;
				pegs[dest] = true;
				jumpsFound[0]++;

				// recurse
				solve(jumpsFound, pegs, jumpList, puzzle);

				// tests
				//System.out.println(jumpsFound);
				//System.out.println(jumpList.size());
				int trueCount = 0;
				for (int i = 0; i < pegs.length; i++) {
					if (pegs[i] == true) {
						trueCount++;
					}
				}
				boolean isStartHole = pegs[puzzle.getStartHole()];

				if (trueCount == 1 &&  isStartHole) {
					return true;
				} else {
					pegs[from] = true;    // undo the jump
					pegs[over] = true;
					pegs[dest] = false;
					// Most important part for backtracking, remove jump from list
					jumpList.remove(jumpList.size() - 1);
				}
			}
		}
		// else
		return false;
	}
}