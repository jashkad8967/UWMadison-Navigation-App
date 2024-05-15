import java.util.PriorityQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new HashtableMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    // Initialize priority queue to store search nodes
    PriorityQueue<SearchNode> queue = new PriorityQueue<>();
    // Initialize map to store visited nodes and their corresponding search nodes
    MapADT<NodeType, SearchNode> visited = new HashtableMap<>();
    // Create the initial search node for the start node
    SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
    queue.add(startNode);
    // Continue searching until the priority queue is empty
    while (!queue.isEmpty()) {
      // Remove the search node with the lowest cost from the priority queue
      SearchNode current = queue.poll();
      // Check if we have reached the end node
      if (current.node.data.equals(end)) {
        return current; // Return the search node representing the end of the shortest path
      }
      // Check if the current node has already been visited
      if (visited.containsKey(current.node.data)) {
        continue; // Skip this node if it has already been visited
      }
      // Mark the current node as visited
      visited.put(current.node.data, current);
      // Explore neighboring nodes (successors of the current node)
      for (Edge edge : current.node.edgesLeaving) {
        Node successor = edge.successor;
        // Calculate the cost of reaching the successor node via the current path
        double cost = current.cost + edge.data.doubleValue();
        // Create a new search node for the successor node
        SearchNode successorNode = new SearchNode(successor, cost, current);
        // Add the successor node to the priority queue
        queue.add(successorNode);
      }
    }
    // If we reach this point, no path from start to end exists
    throw new NoSuchElementException("Invalid Path");
  }


  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // Compute the shortest path using Dijkstra's algorithm
    SearchNode endNode = null;
    try {
      endNode = computeShortestPath(start, end);
    } catch (NoSuchElementException e) {
      // If the end node is null, it means there's no path from start to end
      throw new NoSuchElementException("Invalid Path");
    }
    // Initialize a list to store the node data along the shortest path
    List<NodeType> path = new LinkedList<>();
    // Traverse backwards from the end node to the start node, adding each node's data to the list
    while (endNode != null) {
      path.add(0, endNode.node.data);
      endNode = endNode.predecessor;
    }
    return path;
  }


  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    // Compute the shortest path using Dijkstra's algorithm
    SearchNode endNode = null;
    try {
      endNode = computeShortestPath(start, end);
    } catch (NoSuchElementException e) {
      // If the end node is null, it means there's no path from start to end
      return -1;
    }
    // Return the cost of the shortest path, which is stored in the end node
    return endNode.cost;
  }

  // TODO: implement 3+ tests in step 4.1
  /**
   * Tests an example that was traced through in lecture, and confirms that the results of the
   * implementation match what was previously computed by hand.
   */
  @Test
  public void testCostAndSequence() {
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>();
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("C");
    test.insertNode("D");
    test.insertNode("E");
    test.insertEdge("A", "B", 15);
    test.insertEdge("A", "C", 1);
    test.insertEdge("A", "D", 4);
    test.insertEdge("B", "A", 15);
    test.insertEdge("B", "D", 2);
    test.insertEdge("B", "E", 1);
    test.insertEdge("C", "A", 1);
    test.insertEdge("C", "E", 10);
    test.insertEdge("D", "A", 4);
    test.insertEdge("D", "B", 2);
    test.insertEdge("D", "E", 10);
    test.insertEdge("E", "B", 1);
    test.insertEdge("E", "C", 10);
    test.insertEdge("E", "D", 10);
    Assertions.assertEquals(test.shortestPathData("A", "E").toString(), ("[A, D, B, E]"));
    Assertions.assertEquals(test.shortestPathCost("A", "E"), 7);
  }

  /**
   * Tests an example that was traced through in lecture, but check the cost and sequence of data
   * along the shortest path between a different start and end node.
   */
  @Test
  public void testDifferentCostAndSequence() {
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>();
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("C");
    test.insertNode("D");
    test.insertNode("E");
    test.insertEdge("A", "B", 15);
    test.insertEdge("A", "C", 1);
    test.insertEdge("A", "D", 4);
    test.insertEdge("B", "A", 15);
    test.insertEdge("B", "D", 2);
    test.insertEdge("B", "E", 1);
    test.insertEdge("C", "A", 1);
    test.insertEdge("C", "E", 10);
    test.insertEdge("D", "A", 4);
    test.insertEdge("D", "B", 2);
    test.insertEdge("D", "E", 10);
    test.insertEdge("E", "B", 1);
    test.insertEdge("E", "C", 10);
    test.insertEdge("E", "D", 10);
    Assertions.assertEquals(test.shortestPathData("B", "C").toString(), "[B, D, A, C]");
    Assertions.assertEquals(test.shortestPathCost("B", "C"), 7);
  }

  /**
   * Tests the behavior of the implementation when the nodes that being searched for a path between
   * existing nodes in the graph, but there is no sequence of directed edges that connects them from
   * the start to the end.
   */
  @Test
  public void testInvalidPath() {
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>();
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("C");
    test.insertNode("D");
    test.insertNode("E");
    test.insertEdge("A", "B", 15);
    test.insertEdge("A", "C", 1);
    test.insertEdge("A", "D", 4);
    test.insertEdge("B", "A", 15);
    test.insertEdge("B", "D", 2);
    test.insertEdge("C", "A", 1);
    test.insertEdge("D", "A", 4);
    test.insertEdge("D", "B", 2);
    boolean expected = false;
    try {
      test.shortestPathData("A", "E").toString();
    } catch (NoSuchElementException e) {
      expected = true;
    }
    Assertions.assertTrue(expected);
    Assertions.assertEquals(test.shortestPathCost("A", "E"), -1);
  }
}
