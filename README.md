## Navigation
***
# Design of Computing Algorithms - COS 485
An introduction to the design and analysis of algorithms. This course develops useful algorithm design techniques such as divide-and-conquer, dynamic programming, the greedy method, searching, iterative improvement, and randomized algorithms. We will study important problems such as sorting, searching, minimum spanning tree, single source shortest path, traveling salesperson, graph search and others. This course develops mathematical techniques for analyzing and comparing the execution times of algorithms and for finding lower bounds on the best possible algorithms.



## General Information
All of these programs required Scaffold.jar, and a testing jar file, to test their performance. Both were provided by my professor. 

I used VSCode and needed to make a few changes to my environment to run the tests. 
- I created a launch.json file to run the test (.vscode/launch.json).
- I added a .classpath entry for both the test jar and scaffold.jar

The goal for each program is to run the test jar file and recieve at least "Good" on every test. "Good" means that the program matches the performance of the professor's accepted solution to the problem. 

***

## Program 1
#### Source File: MinAndMax.java
#### Test Results File: report.pdf
#### Instructions:
 * You should implement the static method: minAndMax()
 * The input is:
    * an array of Value objects
   
 * These objects are comparable using the compareTo() method.
 * Your goal is to find both the minimum and maximum values using at most 1.5 N comparisons.

 * You should return an array of two Value objects the minimum and maximum values.
#### My Strategy:
- This program was pretty straightforward. I figured that I could keep it at or under 1.5N comparisons by comparing every two objects in the array to each other, then comparing the greater of the two with the current maximum, and the smaller of the two with the current minimum.
#### How to run: 
- In IDE, click Run then select minAndMax.MinAndMaxTester jar. 
- In VSCode, on Run and Debug screen, select Debug (Launch)-MinAndMaxTester<prog1> config.

## Program 2
#### Source File: PeakFinder.java
#### Test Results File: report.pdf, test7.pdf
#### Instructions:
 * You should implement the static method: findPeak
 * The input is:
    * a ReadOnlyArrayList<Integer> that only provides .size() and .get(index)
  
 * This array is like a mountain profile. The first part of the array rises with every value to a peak value, and then the values decrease with every value until the end.
  
 * Your task is to efficiently find the index of the peak value while looking at as few values as possible. 
  
 * You should return the index of the peak value.
#### My Strategy:
- My first thought was that Binary Search would be an easy and efficient solution to this. Like the first program, this was pretty straightforward, but only because I tried implementing Binary Search on my first try. Had I attempted another solution, it probably would have been more difficult. 
#### How to run: 
- In IDE, click Run then select peakFinder.PeakFinderTester jar. 
- In VSCode, on Run and Debug screen, select Launch config.

## Program 4
#### Source File: MountainClimber.java
#### Test Results File: prog4-report.pdf, prog4-test4.pdf
#### Instructions:
 * You should implement the static method: findRoute
 * The input is:
    * the number of rows and columns of a 2D array of integer heights
    * a staring Point where row = start.y and col = start.x
    * a goal Point
 
 * You should return an ArrayList of Points(x = col, y = row) for the least cost path from the start to the goal.
#### My Strategy:
- I tried implementing Dijkstra's algorithm. I wanted to implement this because it is a fairly efficient method for finding the least cost path. 
- I created a Vertex class to handle navigation
- This was my first time trying to implement Dijkstra's algorithm, so I was having some trouble grasping it conceptually. Inside the MountainClimber.java, at the top, I include my thought process for trying to implement the algorithm.
- Here is a summary of my thought process:
    - To help me understand this better conceptually, vertex weight = distance
    - for each vertex V in the graph, set the distance to V to infinity, 
    - set the previous vertex to undefined
    - Each vertex needs a distance value from the source and a value for the previous node
    - I need to initialize them with an infinite distance and unknown previous node.
    - This could be a map that identifies by an array {row, column}
    - Use lists of unsettled/settled points. Use these to determine which vertex gets evaluated next. 
#### How to run: 
- In IDE, click Run then select mountainClimber.MountainClimberTester jar. 
- In VSCode, on Run and Debug screen, select Debug (Launch)-MountainClimberTester<prog4> config.

## Program 5
#### Source File: PegJump.java
#### Test Results File: prog5-report.pdf, prog5-test5.pdf
#### Instructions:
 * You should implement the static methods:

 * solvePegJump 
    - finds a solution and the number of nodes examined in the search 
    - it should fill in the jumpList argument with the jumps that form your solution

 * The input is a PegJumpPuzzle object, which has:
    * a size, the number of holes numbered 0 .. size()-1
    * the startHole that is initially empty
    * an ArrayList of allowed jumps, which are triples (from, over, dest)
    * a jump takes the peg 'from' over the peg in 'over' (removing it) and into 'dest'
#### My Strategy:
- Inside the PegJump.java, at the top, I include my thought process for trying to implement the algorithm.
- I knew I needed to implement backtracking, so I used recursion. 
- So I created my recursive method to call from solvePegJump, and a list to hold all of the Jumps, and recurse through it, removing any jumps from the list that didn't work, and saving the jumps that did to another list.
- The accepted solution did not need the last jump to end in the starting hole. I was given extra credit for that. 
#### How to run: 
- In IDE, click Run then select pegJump.PegJumpTester jar.
- In VSCode, on Run and Debug screen, select Debug (Launch)-PegJumpPuzzle<prog5> config.

## Program 7
#### Source File: Districting.java
#### Test Results File: prog7-report.pdf, prog7-test5.pdf
#### Instructions:
 * You should implement the static method: makeDistricts
 * The input is:
    * the number of rows and columns of
    * a 2D array of integer populations in each array cell in the populations 
    * the number of districts desired
  
 * Your task is to break it up into districts that are:
    * contiguous - (all cells in a region are connected by shared edges)
    * close to equal in population
     
 * You should return a 2D array of the district numbers for each cell. 
 * District number should start at 1.
 
 * Note: this assignment can be animated. If you would like to watch your algorithm as it works, you can inserts calls to:
    * DistrictingTester.show(districts);
    * at any points in your program where it would be useful to see the current state of the districts array. Read more detailed instructions using the [About] button in the scaffold.
 
 * The starting code contains example animations calls.
#### My Strategy:
- This was a complicated problem that I probably had a complicated and unique solution to. I worked on this with a team member, but took a leading role in the algorithm's design and implementation.

What is the algorithm?
- First, we populate the districts with the function given to us.
- Next, we have a loop that finds the smallest population district and attempts to add a new zone to it. It looks at zones sequentially on the grid. If no new zone is found, it goes to the next lowest district and tries again. There are many, many tests to see If adding the zone is a good move or not. Some tests include:
    - Test if it improves the % difference
    - Test if it will cut another zone in half
    - Test if the node was recently changed
    - Test if it is stuck in a loop
    - Test if this change will eliminate a district
    - Test if the district we are adding to is still connected to the zone
- To prevent it from looping between the same few values, it keeps track of the last few hundred moves. 
- Test success threshold starts at 1% and increases(resetting the test) if it passes a threshold of failures. We added some default “success variables” to make this process go quicker. The percent increase and the test threshold could be changed to make the process go even quicker. 
- It looks great on paper, getting "Great" on the test results, but the district shapes aren't ideal in reality. It creates long, snaking districts because my algorithm checks to see if the zone stays connected by looking at the 3x3 square around the zone in question.  If I had used depth-first search, it would look better.

#### How to run: 
- In IDE, click Run then select districting.DistrictingTester jar.
- In VSCode, on Run and Debug screen, select Debug (Launch)-DistrictingTester<prog7> config.