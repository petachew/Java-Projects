/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections; edges are streets
 *
 */
public class MapGraph {
	// a hash map that maps the location on the map (latitude and
	// longitude) to the actual intersection (node)
	private HashMap<GeographicPoint,MapNode> pointNodeMap;
	
	// a hash set that contains all the streets / edges
	private HashSet<MapEdge> edges;
	
	// maintains both nodes and edges as you will need to
	// be able to look up nodes by lat/lon or by streets connecting them to other nodes
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		pointNodeMap = new HashMap<GeographicPoint,MapNode>();
		edges = new HashSet<MapEdge>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		// TODO: WRITE THE PROPER RETURN STATEMENT
		return pointNodeMap.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		// TODO: WRITE THE PROPER RETURN STATEMENT
		return pointNodeMap.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		// TODO: WRITE THE PROPER RETURN STATEMENT
		return edges.size();
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if (location == null || pointNodeMap.containsKey(location)) {
			return false;
		}
		
		pointNodeMap.put(location, new MapNode(location));
		// TODO ////////////////////////////////////////////////////////////////////////////
		// 1. CHECK IF THE LOCATION IS ALREADY ASSOCIATED WITH AN EXISTING NODE
		// 2. IF IT IS, RETURN FALSE
		// 3. IF IT IS NOT, CREATE THE NEW MAPNODE AT THAT LOCATION AND ADD IT TO POINTNODEMAP
		//		THEN RETURN TRUE
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		MapNode n1 = pointNodeMap.get(from);
		MapNode n2 = pointNodeMap.get(to);

		// check nodes are valid
		if (n1 == null)
			throw new NullPointerException("addEdge: pt1:"+from+"is not in graph");
		if (n2 == null)
			throw new NullPointerException("addEdge: pt2:"+to+"is not in graph");
		
		// TODO: //////////////////////////////////////////////////////////////////////////
		// 1. CREATE MAPEDGE OBJECT
		// 2. ADD TO EDGES
		// 3. ADD TO N1 NODE'S LIST OF OUTGOING EDGES
		MapEdge e = new MapEdge(roadName, roadType, n1, n2, length);
		edges.add(e);
		n1.getEdges().add(e);
	}
		
	/** 
	 * Get a set of neighbor nodes from a mapNode
	 * @param node  The node to get the neighbors from
	 * @return A set containing the MapNode objects that are the neighbors 
	 * 	of node
	 */
	private Set<MapNode> getNeighbors(MapNode node) {
		// TODO: WRITE THE PROPER RETURN STATEMENT //////////////////////////////////
		
		return node.getNeighbors();
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, 
			 					     Consumer<GeographicPoint> nodeSearched)
	{
				
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}

		// setup to begin BFS
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		Queue<MapNode> toExplore = new LinkedList<MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		toExplore.add(startNode);
		MapNode next = null;
		
		boolean found = false;
		
		while (!toExplore.isEmpty() && !found) {
			next = toExplore.remove();
			
			 // hook for visualization
			nodeSearched.accept(next.getLocation());
			
			// TODO:////////////////////////////////////////////////////////////////
			// WRITE THE REST OF THE BFS LOOP
			if(!next.equals(endNode)) {
				for(MapNode temp : getNeighbors(next)) {
					if(!visited.contains(temp) && temp != null) {
						visited.add(temp); //add n to visited set
						parentMap.put(temp, next); //add curr as n's parent in parent map
						toExplore.add(temp); //enqueue n onto the queue
					}
				}
			} else {
				found = true;
			}
			
			if(found) {
				break;
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path 
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	
	}
	


	/** Reconstruct a path from start to goal using the parentMap
	 *
	 * @param parentMap the HashNode map of children and their parents
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from
	 *   start to goal (including both start and goal).
	 */
	private List<GeographicPoint> reconstructPath(HashMap<MapNode,MapNode> parentMap,
					MapNode start, MapNode goal)
	{
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		MapNode current = goal;

		while (!current.equals(start)) {
			path.addFirst(current.getLocation());
			current = parentMap.get(current);
		}

		// add start
		path.addFirst(start.getLocation());
		return path;
	}


	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}
		
		// setup to begin BFS
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		
		PriorityQueue<distNode> toExplore = new PriorityQueue<distNode>();
		distNode distStart = new distNode(startNode, 0.0);
		toExplore.add(distStart);
		
		double distance = Double.MAX_VALUE;
		
		HashSet<MapNode> visited = new HashSet<MapNode>();
		MapNode next = null;	
		
		
		boolean found = false;
		
		while (!toExplore.isEmpty() && !found) {
			distNode distNext = toExplore.remove();
			next = distNext.node;
			double nextDist = distNext.dist;
			 // hook for visualization
			nodeSearched.accept(next.getLocation());
			
			// TODO:////////////////////////////////////////////////////////////////
			if(!visited.contains(next)) {
				visited.add(next);
				
				if(!next.equals(endNode)) {
					double best = Double.MAX_VALUE;
					
					for(MapNode temp : getNeighbors(next)) {
						double currToTemp = nextDist + temp.getLocation().distance(next.getLocation());
						if(nextDist < currToTemp && !visited.contains(temp)) {
							best = currToTemp;
							parentMap.put(temp, next);
							toExplore.add(new distNode(temp, currToTemp));
						}
					}
					
					/*
					for(MapNode temp : getNeighbors(next)) {
						
						double theD = distNext.dist + temp.getLocation().distance(next.getLocation());
						if(!visited.contains(temp) && temp != null && theD < best) {
							visited.add(temp);
							parentMap.put(temp, next);
							toExplore.add(new distNode(temp, theD));
							
							//visited.add(temp); //add n to visited set
							//parentMap.put(temp, next); //add curr as n's parent in parent map
							//toExplore.add(new distNode(temp, d)); //enqueue n onto the queue
						}
					}
					
					*/
				} else {
					found = true;
				}
			}
			
			if(found) {
				break;
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path 
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method ////////////////////////////////////////////
		
		// Setup - check validity of inputs
		if (start == null || goal == null)
			throw new NullPointerException("Cannot find route from or to null node");
		MapNode startNode = pointNodeMap.get(start);
		MapNode endNode = pointNodeMap.get(goal);
		if (startNode == null) {
			System.err.println("Start node " + start + " does not exist");
			return null;
		}
		if (endNode == null) {
			System.err.println("End node " + goal + " does not exist");
			return null;
		}
		
		// setup to begin BFS
		HashMap<MapNode,MapNode> parentMap = new HashMap<MapNode,MapNode>();
		
		PriorityQueue<distNode> toExplore = new PriorityQueue<distNode>();
		distNode distStart = new distNode(startNode, 0.0);
		toExplore.add(distStart);
		
		double distance = Double.MAX_VALUE;
		
		HashSet<MapNode> visited = new HashSet<MapNode>();
		MapNode next = null;	
		
		
		boolean found = false;
		
		while (!toExplore.isEmpty() && !found) {
			distNode distNext = toExplore.remove();
			next = distNext.node;
			double nextDist = distNext.dist;
			 // hook for visualization
			nodeSearched.accept(next.getLocation());
			
			// TODO:////////////////////////////////////////////////////////////////
			if(!visited.contains(distNext)) {
				visited.add(next);
				if(!next.equals(endNode)) {
					double best = Double.MAX_VALUE;
					double best2 = Double.MAX_VALUE;
					MapNode bestN = null;
					
					for(MapNode temp : getNeighbors(next)) {
						double tempToGoal = temp.getLocation().distance(goal);
						double theD = distNext.dist + tempToGoal;//temp.getLocation().distance(next.getLocation());
						if(!visited.contains(temp) && temp != null && tempToGoal < best2) { //CHECKS ALL NODES IF IT IS LESS THAN BEST2
							
							best2 = tempToGoal; //make best2 always be the node with the shortest distance
							
							bestN = temp;
						}
					}
					
					if(bestN != null) {
						visited.add(bestN);
						parentMap.put(bestN, next);
						toExplore.add(new distNode(bestN, best2));
					}
				} else {
					found = true;
				}
			}
			
			if(found) {
				break;
			}
		}
		if (!next.equals(endNode)) {
			System.out.println("No path found from " +start+ " to " + goal);
			return null;
		}
		// Reconstruct the parent path 
		List<GeographicPoint> path =
				reconstructPath(parentMap, startNode, endNode);

		return path;
	}
	
	/**
	 * distNode is a MapNode with the distance.
	 * @author Peter
	 *
	 */
	class distNode implements Comparable<Object>{
		/** The MapNode. */
		MapNode node;
		/** The distance of this node to another. */
		double dist;
		
		/**
		 * Constructor for this distNode.
		 * @param m the MapNode.
		 * @param d the distance.
		 */
		distNode(MapNode m, double d) {
			node = m;
			dist = d;
		}
		
		/**
		 * Compares the two distNodes distances.
		 * @param arg0: the distNode I want to compare with.
		 */
		@Override
		public int compareTo(Object arg0) { 
			if(!(arg0 instanceof distNode)) {
				throw new IllegalArgumentException();
			}
			distNode other = (distNode) arg0;
			if (dist + 0.001 > other.dist && dist - 0.001 < other.dist){
				return 0;
			} else if(dist > other.dist) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", theMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		/* Use this code in Week 3 End of Week Quiz
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/
		
	}
	
	
}
