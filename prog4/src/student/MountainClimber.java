package student;

/* 
 * This class is meant to contain your algorithm.
 * You should implement the static method: findRoute
 * The input is:
 *   the number of rows and columns of
 *   a 2D array of integer heights
 *   a staring Point where row = start.y and col = start.x
 *   a goal Point
 * 
 * You should return an ArrayList of Points(x = col, y = row) for the least cost path
 * from the start to the goal.
 */
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MountainClimber
{

    public static ArrayList<Point> findRoute(int rows, int cols, int[][] grid, 
            Point start, Point goal) {

        // ---------------------------- Design Notes --------------------------//
        // I am going to try and implement Dijkstra's algorithm
        // To help me understand this better conceptually, vertex weight = distance
        // for each vertex V in the graph, set the distance to V to infinity, 

        // set the previous vertex to undefined

        // What is the best way to keep track of the vertices? 
        // They each need a distance value from source and a previous node value
        // this could be a map of points and vertices, 
        // and I could have lists of settled and unsettled points, not vertices

        // I could go by rows and columns, I could use a map that identifies by array {row, column}
        // need to initialize them with an infinite distance and unknown predecessor, except the source

        // unsettled vertices set starts empty, then I add the source to it so it gets picked first
        
        // then, pick the node with the lowest distance from the unsettled set

        // evaluate all adjacent vertices that are not in settled vertices

        // add edge weight to the evaluation node distance, then compare it to the destination's distance

        //then, move the node to the settled set, move the vertices that had their distance values changed to the unsettled vertices set

        // choose the node with the lowest distance and reiterate until all vertices are settled in the graph
        // ----------------------------------------------------------------//


        // to return
        ArrayList<Point> path = new ArrayList<Point>();

        // set up point lists
        List<Point> settled = new ArrayList<>();
        List<Point> unsettled = new ArrayList<>();

        // create a map of all points to vertices
        Map<Point, Vertex> vertices = new HashMap<>();
        for (int i = 0; i < cols; i++) {
            for (int k = 0; k < rows; k++) {
                Point idPoint = new Point(i, k); // X is column, Y is row
                Vertex vertex = new Vertex(idPoint);
                vertex.setWeight(Integer.MAX_VALUE);
                vertex.setPrevious(null);
                vertices.put(idPoint, vertex);
            }
        } 

        // set up the source vertex
        Vertex source = vertices.get(start);
        source.setWeight(0);

        // add start to unsettled
        unsettled.add(start);

        //the main execution loop
        while (unsettled.size() > 0) {
            Point currentPoint = new Point();
            double shortestDistance = Double.MAX_VALUE;

            // loop to find the shortest distance vertex in unsettled
            for (Point point : unsettled) {
                double distance = vertices.get(point).getWeight();
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    currentPoint = point;
                }
            }

            // remove current point from unsettled points
            unsettled.remove(currentPoint);

            // get a list of all adjacent vertices to the currentPoint
            ArrayList<Point> adjacentPoints = vertices.get(currentPoint).getAdjacentPoints(cols, rows);
            ArrayList<Integer> directions = vertices.get(currentPoint).getDirections();

            // evaluate vertices adjacent to the current one
            // for each point, 
            int direction = 0;

            for (Point point : adjacentPoints) {
                //get the vertex, 
                Vertex vertex = vertices.get(point);

                //calculate the distance to the vertex from the source
                // grid[y - 1][x] is North, grid[y][x - 1] is West
                // X is columns, Y is rows. grid[rows][columns] i guess
                int currHeight = grid[(int)currentPoint.getY()][(int)currentPoint.getX()];
                int newHeight = grid[(int)point.getY()][(int)point.getX()];


                // Cost Calculation
                // 0 if horizontal/vertical, 1 if diagonal
                double base = -100;
                double delta = Math.abs(currHeight-newHeight);
                double costToMove;
                if (directions.get(direction) == 0) {
                    base = 1;
                    costToMove = base + (delta*delta*delta);
                } else {
                    base = Math.sqrt(2);
                    costToMove = base + (delta*delta*delta)/2;
                }

                // calculate the weight
                double weight = vertices.get(currentPoint).getWeight() + costToMove;
                
                //compare it to the current distance to that vertex
                if (weight < vertex.getWeight()) {
                    // if greater,
                    // update vertex distance
                    vertex.setWeight(weight);
                    // update previous vertex to the current vertex we are evaluating for
                    vertex.setPrevious(vertices.get(currentPoint));
                    //update vertex
                    vertices.put(vertex.getIdPoint(), vertex);
                    // add vertex to unsettled list
                    unsettled.add(point);
                } else {
                    // nothing
                }
                direction++;
            }

            // add current point to settled
            settled.add(currentPoint);
        }

        // construct the path to return
        // Create an arraylist of vertices starting from the destination, back to the start
        // reverse them and convert them to points
        ArrayList<Vertex> vertexPath = new ArrayList<>();
        Vertex currentVertex = vertices.get(goal);
        Vertex prevVertex = currentVertex.getPrevious();

        // loop to create vertex arraylist
        while(prevVertex != null) {
            vertexPath.add(currentVertex);
            currentVertex = prevVertex;
            prevVertex = currentVertex.getPrevious();
        }
        // loop to convert them to points
        path.add(start);
        for (int i = vertexPath.size() - 1; i > 0 ; i--) {
            path.add(vertexPath.get(i).getIdPoint());
        }
        path.add(goal);

        return path;
    }
}

    class Vertex {
        private Point idPoint;
        private double weight;
        private Vertex previous;
        ArrayList<Point> adjacentPoints;
        ArrayList<Integer> directions;


        public Vertex(Point idPoint) {
            this.idPoint = idPoint;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }

        public void setPrevious(Vertex previous) {
            this.previous = previous;
        }

        public Vertex getPrevious() {
            return previous;
        }

        public Point getIdPoint() {
            return idPoint;
        }

        public ArrayList<Integer> getDirections() {
            return directions;
        }

        public ArrayList<Point> getAdjacentPoints(int cols, int rows) {
            
            if (adjacentPoints == null) {
                adjacentPoints = new ArrayList<Point>();
                directions = new ArrayList<Integer>();
                // find the adjacent Points
                
                // modifiers for the points, starting top-left going clockwise
                // X is columns, Y is rows
                int[][] mods = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

                int x = (int)idPoint.getX();
                int y = (int)idPoint.getY();

                for (int i = 0; i < 8; i++) {
                    // Check to see if the points fit within the grid, else discard
                    // cols and rows and sizes, not indexes. So needs to be -1
                    if ((x + mods[i][0] > cols-1 || x + mods[i][0] < 0) || (y + mods[i][1] > rows-1 || y + mods[i][1] < 0)) {
                        //do nothing, there is no adjacent vertex
                    } else {
                        //add the point to adjacent points
                        adjacentPoints.add(new Point(x+mods[i][0], y+mods[i][1]));
                        // 0 if horizontal, 1 if diagonal
                        switch (i) {
                            case 0:
                                directions.add(1);
                                break;
                            case 1:
                                directions.add(0);
                                break;
                            case 2:
                                directions.add(1);
                                break;
                            case 3:
                                directions.add(0);
                                break;
                            case 4:
                                directions.add(1);
                                break;
                            case 5:
                                directions.add(0);
                                break;
                            case 6:
                                directions.add(1);
                                break;
                            case 7:
                                directions.add(0);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            return adjacentPoints;
        }
    }


