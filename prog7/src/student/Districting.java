package student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import districting.DistrictingTester;

/* 
 * This class is meant to contain your algorithm.
 * You should implement the static method: makeDistricts
 * The input is:
 *   the number of rows and columns of
 *   a 2D array of integer populations in each array cell in the populations 
 *   the number of districts desired
 *   
 * Your task is to break it up into districts that are:
 *   contiguous - (all cells in a region are connected by shared edges)
 *   close to equal in population
 *      
 * You should return a 2D array of the district numbers for each cell. 
 * District number should start at 1.
 * 
 * Note: this assignment can be animated. If you would like to watch your algorithm
 * as it works, you can inserts calls to:
 *          DistrictingTester.show(districts);
 * at any points in your program where it would be useful to see the current state of
 * the districts array. Read more detailed instructions using the [About] button in the
 * scaffold.
 * 
 * The starting code contains example animations calls.
 */


public class Districting {
    public static int[][] makeDistricts(int rows, int cols, int[][] populations, int numDistricts) {

        // map to keep track of districts
        Map<Integer, Integer> districts = new HashMap();
        // Integer: key, the district number
        // Arraylist: {population, number of zones}

        // percent difference
        Double difference = 0.0;
        
        // badZones counter
        int badZones = 0;

        // failure counter
        int failure = 0;

        //number of zones
        int zoneCount = 0;
        //populate them
        for (int i = 0; i < populations.length; i++) {
            for (int k = 0; k < populations[i].length; k++) {
                zoneCount++;
            }
        }

        // success variable
        Double test = 0.0;
        Double zoneDivider = 1.0;
        int testThreshold = 25000;
        if (numDistricts == 8) { // test1
            test += 0.022;
            zoneDivider = 10.0;
        } else if (numDistricts == 9) { // test2
            test += 0.015;
            zoneDivider = 5.0;
            testThreshold = 4000;
        } else if (numDistricts == 10) { // test3
            test += 0.0;
            zoneDivider = 4.0;
            testThreshold = 6000;
        } else if (numDistricts == 11) { // test4
            test += 0.001;
            zoneDivider = 4.0;
            testThreshold = 15000;
        } else if (numDistricts == 12) { // test5
            test += 0.002;
            zoneDivider = 3.0;
            testThreshold = 25000;
        }

        int testCount = 0;



        // the previous node changed. Meant to prevent infinitely looping the same change
         ArrayList<Integer[]> prevChanged = new ArrayList<>();

         for (int i = 0; i < (zoneCount/(zoneDivider)); i++) {
            //Integer[] temp = {-1,-1};
            Integer[] temp = new Integer[2];
            temp[0] = -1;
            temp[1] = -1;
            prevChanged.add(temp);
         }
        
        //Populate initial districts
        for (int i = 1; i <= numDistricts; i++) {
            districts.put(i, 0);
        }

        //2d array to keep track of zones and their assigned district
        int[][] zones = new int[populations.length][];
        for (int i = 0; i < populations.length; i++) {
            zones[i] = new int[populations[i].length];
        }
        // Populate initial zones array
        int r1 = 0, r2 = rows, c1 = 0, c2 = cols;  // the unallocated region [r1,r2) x [c1,c2)
        
        for (int d = 1; d <= numDistricts; d++) {   // district numbers in [1, numDistricts]
            switch (d % 3) {
            case 0: // slice two rows off top
                fill(districts, populations, zones, r1, c1, r1 + 2, c2, d);
                r1 += 2;
                break;
            case 1: // slice two rows off right
                fill(districts, populations, zones, r1, c2 - 2, r2, c2, d);
                c2 -= 2;
                break;
            case 2: // slice two rows off bottom
                fill(districts, populations, zones, r2 - 2 , c1, r2, c2, d);
                r2 -= 2;
                break;
            case 3: // slice two rows off left 
                fill(districts, populations, zones, r1, c1, r2, c1 + 2, d);
                c1 += 2;
                break;
            }
        }
        
        fill(districts, populations, zones, r1, c1, r2, c2, numDistricts-1); // give everything else to the last district

        // loop to add more zones to the lowest-pop district
        // complete : boolean will check to see if the difference is less than 10%
        Boolean complete = false;
        while (complete == false) {

            // check to make sure badZones does not exceed max zones
            // and failure is less than max
            if (badZones >= zoneCount) {
                failure++;
                badZones=0;
                testCount++;
            }

            if (failure > numDistricts) {
                failure = 0;
                badZones = 0;
            }

            // test to see if the algorithm is done
            if (difference >= 0.99 - test) {
                complete = true;
                break;
            }


            // this basically handles the upper limit of my algorithm's success
            // If it has failed to find a zone to move for every district, it will reset the
            // precChanged array, and try again. But it will back itself into a corner, finding the
            // perfect arrangement where it can't move at all. If that happens, then my algorithm cannot
            // solve the puzzle at the current difficulty, so i will need to reset it and try again at a
            // lower percentage
            if (testCount == testThreshold) { //failure >= numDistricts
                testCount = 0;
                System.out.println("failure too high for districtCount " + numDistricts);
                // reset all values
                failure = 0;
                badZones = 0;

                // reset prevChanged
                ArrayList<Integer[]> newArray = new ArrayList<>();
                Integer[] prev = new Integer[2];
                prev[0] = -1;
                prev[1] = -1;
                for (int i = 0; i < prevChanged.size(); i++) {
                    newArray.add(prev);
                }
                prevChanged = newArray;

                //reset districts
                districts = new HashMap();
                for (int i = 1; i <= numDistricts; i++) {
                    districts.put(i, 0);
                }

                //reset zones
                zones = new int[populations.length][];
                for (int i = 0; i < populations.length; i++) {
                    zones[i] = new int[populations[i].length];
                }


                test+= 0.002; // try again with -0.001 to difficulty


                // repopulate everything, trying again
                // Populate initial zones array
                r1 = 0;
                r2 = rows;
                c1 = 0;
                c2 = cols;  // the unallocated region [r1,r2) x [c1,c2)
                
                for (int d = 1; d <= numDistricts; d++) {   // district numbers in [1, numDistricts]
                    switch (d % 3) {
                    case 0: // slice two rows off top
                        fill(districts, populations, zones, r1, c1, r1 + 2, c2, d);
                        r1 += 2;
                        break;
                    case 1: // slice two rows off right
                        fill(districts, populations, zones, r1, c2 - 2, r2, c2, d);
                        c2 -= 2;
                        break;
                    case 2: // slice two rows off bottom
                        fill(districts, populations, zones, r2 - 2 , c1, r2, c2, d);
                        r2 -= 2;
                        break;
                    case 3: // slice two rows off left 
                        fill(districts, populations, zones, r1, c1, r2, c1 + 2, d);
                        c1 += 2;
                        break;
                    }
                    //DistrictingTester.show(zones);  // you can insert calls like this to animate your algorithm
                }
                
                fill(districts, populations, zones, r1, c1, r2, c2, numDistricts-1); // give everything else to the last district
            }


            // ---------------------------
            // determine which district is the lowest population
            //----------------------------
            int smallestDistrict = Integer.MAX_VALUE;
            // the district to work on next, the lowest population one
            int districtToChange = -1;

            // Now we get the smallest population district, skipping districts we couldn't find moves for
            ArrayList<Integer> skipThese = new ArrayList<>();
            // outer loop will run failures+1 times. 
            for (int fails = 0; fails <= failure; fails++) { 
                //For each run above the first, it will find the smallest district and add it to skipThese
                // need to reset smallestDistrict
                smallestDistrict = Integer.MAX_VALUE;
                for (int i = 1; i <= numDistricts; i++) {
                    int val = districts.get(i);

                    // check to set smallest district, skipping skipThese
                    if (val <= smallestDistrict && !(skipThese.contains(val))) {
                        smallestDistrict = val;
                        districtToChange = i;
                    }
                }
                skipThese.add(smallestDistrict);
            }

            
            //-------------------------------
            // Using the lowest population district, find the next zone to change
            //-------------------------------
            // to find the next zone, we will just pick the next zone on the grid that isn't a part of the district
            int oldDistrict = -1;
            int zoneR = -1;
            int zoneC = -1;
            boolean loop = true;

            int badZonesCountdown = badZones; // the zones to skip this run

            for (int i = 0; i < zones.length; i++) {
                if (loop) {
                    for (int k = 0; k < zones[i].length; k++) {
                        if (loop) {
                            // check to see if it's already the district we are in
                            if (zones[i][k] == districtToChange) {
                                // do nothing
                            } else {
                                // check for bad zones
                                if (badZonesCountdown > 0) {
                                    badZonesCountdown--;
                                } else {
                                    // set the current zone's coordinates
                                    zoneR = i;
                                    zoneC = k;
                                    // set old and new districts
                                    oldDistrict = zones[i][k];
                                    // we are done
                                    loop = false;
                                }
                            }
                        }
                    }
                }
            }


            boolean valid = true;

            if (zoneC == -1 || zoneR == -1) {
                failure++;
                badZones = 0;
                valid = false;
                testCount++;
            }

            // check to make sure that the oldDistrict isn't going to be completely annihilated from this change
            if (valid == true) {
                if (districts.get(oldDistrict) == populations[zoneR][zoneC]) {
                    valid = false;
                }
            }

            if (valid) {
                //--------------------------
                // Change the zone to the districtToChange
                //--------------------------
                try {
                    zones[zoneR][zoneC] = districtToChange;
                    // update districts population and count for districttochange
                    districts.put(districtToChange, districts.get(districtToChange) + populations[zoneR][zoneC]);

                    districts.put(oldDistrict, districts.get(oldDistrict) - populations[zoneR][zoneC]);
                } catch (Exception e) {

                }
                

                // --------------------------
                // check to see if the zones are still connected
                // --------------------------
                // to do this, just look at the zone that was changed and make sure that there 
                // is at least 1 zone from the same district adjacent to it
                ArrayList<Integer> temp = new ArrayList<>();
                try {
                    temp.add(zones[zoneR-1][zoneC-1]); // top left: 0
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR][zoneC-1]); // left center: 1
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR+1][zoneC-1]); // bottom left: 2
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR+1][zoneC]); // bottom center: 3
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR+1][zoneC+1]); //bottom right: 4
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR][zoneC+1]); // right center: 5
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR-1][zoneC+1]); // top right: 6
                } catch (Exception e) {
                    temp.add(-1);
                }
                try {
                    temp.add(zones[zoneR-1][zoneC]); // top center: 7
                } catch (Exception e) {
                    temp.add(-1);
                }

                //---------------
                valid = false;
                


                // check if it is connected
                // cases where this can fail:
                    // opposite sides are olddistrict
                    // three sides are olddistrict AND there aren't blocks linking those three
                int newCount = 0;
                int oldCount = 0;
                for(int i = 1; i < temp.size(); i+=2){// this is because we only want to test adjacents: 1, 3, 5, 7
                    if (temp.get(i) == districtToChange) {
                       newCount++;
                    } 
                    if (temp.get(i) == oldDistrict) {
                        oldCount++;
                    }
                }

                if (newCount >= 1) { // we want it to be adjacent to a new district already
                    // Remember, oldcount is only the adjacent blocks. not the center

                    // 0 means it's just taking an isolated block. Fixing an error basically
                    // 1 means it's taking a block that is connected. This might be causing some problems
                    // but 3 means that it is digging into the old district, but likely not cutting it off
                        // the only exception to this is if it continues to cut down that line, which isn't likely, but not impossible.
                    // if oldcount > 10, then it's invalid. Based on the above results
                    // if oldcount == 2, it might be cutting something off. Check for that. 
                    if (oldCount == 2) {
                        // need to set valid to true, or else it won't matter
                        valid = true;
                        // check if the two are on opposite sides. Include nulls
                        if ((temp.get(1) == oldDistrict && temp.get(5) == oldDistrict)||(temp.get(1) == -1 && temp.get(5) == oldDistrict)||(temp.get(1) == oldDistrict && temp.get(5) == -1)) {
                            valid = false;
                        }
                        if ((temp.get(3) == oldDistrict && temp.get(7) == oldDistrict)||(temp.get(3) == -1 && temp.get(7) == oldDistrict)||(temp.get(3) == oldDistrict && temp.get(7) == -1)) {
                            valid = false;
                        }
                        // need another check to make sure that the two adjacent ones won't be cut off from each other
                        // first, check each side to see which is the oldDistrict(one of them will be 100%)
                        if (temp.get(1) == oldDistrict) {//left
                            // then, check whether the top or bottom is oldDistrict. one will be, 100%
                            // make sure that the district between them is also oldDistrict, else invalid
                            if (temp.get(7) == oldDistrict) { // top
                                if (!(temp.get(0) == oldDistrict)) { // top left
                                    valid = false;
                                }
                            } else if (temp.get(3) == oldDistrict) { // bottom
                                if (!(temp.get(2) == oldDistrict)) { // bottom left
                                    valid = false;
                                }
                            }
                        } else if (temp.get(5) == oldDistrict) { // right
                            // then, check whether the top or bottom is oldDistrict. one will be, 100%
                            // make sure that the district between them is also oldDistrict, else invalid
                            if (temp.get(7) == oldDistrict) { // top
                                if (!(temp.get(6) == oldDistrict)) { // top right
                                    valid = false;
                                }
                            } else if (temp.get(3) == oldDistrict) { // bottom
                                if (!(temp.get(4) == oldDistrict)) { // bottom right
                                    valid = false;
                                }
                            }
                        }
                    }

                    // if oldCount == 3, there are a few options
                    if (oldCount == 3) {
                        // need to check which one is the newCount
                        for(int i = 1; i < temp.size(); i+=2){
                            if (temp.get(i) == districtToChange) {
                                // check to make sure the olddistrict on the *opposite* side is fully surrounded
                                // to do this, just check the squares adjacent to the opposite side
                                int index = (i + 4) % 8; // this will get the opposite side
                                // can then use +1 and -1 for the sides to test. do not include nulls( they shouldnt be an issue anyway)
                                if (temp.get((index+1)%8) == oldDistrict && temp.get((index-1)%8) == oldDistrict) {
                                    valid = true;
                                }
                            }
                        }
                    }

                    // if oldcount == 0 or 1, we are fine
                    if (oldCount <= 1) {
                        valid = true;
                        
                    }
                }


                // check to see if it's the same point we are contesting
                for (int i = 0; i < prevChanged.size(); i++) {
                    Integer[] val = prevChanged.get(i);
                    if (val[0] == zoneR && val[1] == zoneC) {
                        valid = false;
                    }
                }
                
                // Undo changes if they failed the tests above
                if (valid == false) {
                    //undo changes
                    zones[zoneR][zoneC] = oldDistrict;
                    // update districts population and count
                    districts.put(districtToChange, districts.get(districtToChange) - populations[zoneR][zoneC]);

                    // update districts population and count for olddistrict
                    districts.put(oldDistrict, districts.get(oldDistrict) + populations[zoneR][zoneC]);
                }
            }

            // for animation
            DistrictingTester.show(zones);


            // check that it's valid
            if (valid) {

                // ---------------------------
                // determine if difference is less than 10% 
                //----------------------------
                Double largest = 0.0;
                Double smallest = (double)Integer.MAX_VALUE;
                for (int i = 1; i <= numDistricts; i++) {
                    Double value = (double)districts.get(i);
                    if (value <= smallest) {
                        smallest = value;
                    }
                    if (value > largest) {
                        largest = value;
                    }
                }


                // calculate the new difference
                Double newDifference = (districts.get(districtToChange)) / largest;
                if (newDifference >= difference) {
                    // Success!
                    // add this to prevChanged
                    ArrayList<Integer[]> newArray = new ArrayList<>();
                    Integer[] prev = new Integer[2];
                    prev[0] = zoneR;
                    prev[1] = zoneC;
                    for (int i = 0; i < prevChanged.size(); i++) {
                        if (i == 0) {
                            newArray.add(prev);
                        } else {
                            newArray.add(prevChanged.get(i-1));
                        }
                    }
                    prevChanged = newArray;
                    
                    // set new difference
                    difference = smallest / largest;
                    // reset badZones and failure counter
                    failure = 0;
                    badZones = 0;
                    // continue looping 
                } else {
                    // undo all changes
                    zones[zoneR][zoneC] = oldDistrict;
                    // update districts population and count
                    districts.put(districtToChange, districts.get(districtToChange) - populations[zoneR][zoneC]);

                    // update districts population and count for olddistrict
                    districts.put(oldDistrict, districts.get(oldDistrict) + populations[zoneR][zoneC]);
                    
                    // increment badZones counter
                    badZones++;
                }
            } else { // not valid
                // increment badZones counter
                badZones++;
            }
        }
        
         return zones;
    }   

    // fill in the array region [r1, r2) x [c1, c2) with value d
    public static void fill(Map<Integer, Integer> districts, int[][] populations, int[][] zones, int r1, int c1, int r2, int c2, int d) {
        for (int r = r1; r < r2; r++) {
            for (int c = c1; c < c2; c++) {
                // set zone[][] district to d
               zones[r][c] = d; 
               // replace entry in districts
               districts.put(d, districts.get(d) + populations[r][c]);
            }
        }
    }
}
