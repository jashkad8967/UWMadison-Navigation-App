import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendImplementation {

  private DijkstraGraph<String, Double> graph;
  private List<String> allNodes; // Keep track of all nodes

  public BackendImplementation(DijkstraGraph<String, Double> dijkstraGraph) {
    this.graph = dijkstraGraph;
    this.allNodes = new ArrayList<>();
  }

  @Override
  public void loadGraphData(String filename) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("->")) {
          int sourceStart = line.indexOf('"') + 1;
          int sourceEnd = line.indexOf('"', sourceStart);
          String source = line.substring(sourceStart, sourceEnd);
          int destStart = line.indexOf('"', sourceEnd + 1) + 1;
          int destEnd = line.indexOf('"', destStart);
          String destination = line.substring(destStart, destEnd);
          int weightStart = line.indexOf("seconds=") + "seconds=".length();
          int weightEnd = line.indexOf("]", weightStart);
          double weight = Double.parseDouble(line.substring(weightStart, weightEnd));
          if (!graph.containsNode(source)) {
            graph.insertNode(source);
            allNodes.add(source);
          }
          if (!graph.containsNode(destination)) {
            graph.insertNode(destination);
            allNodes.add(destination);
          }
          graph.insertEdge(source, destination, weight);
        }
      }
    }
  }

  @Override
  public List<String> getListOfAllLocations() {
    return new ArrayList<>(allNodes);
  }

  @Override
  public List<String> findShortestPath(String startLocation, String endLocation) {
    return graph.shortestPathData(startLocation, endLocation);
  }

  @Override
  public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
    List<String> shortestPath = findShortestPath(startLocation, endLocation);
    List<Double> travelTimes = new ArrayList<>();
    for (int i = 0; i < shortestPath.size() - 1; i++) {
      String source = shortestPath.get(i);
      String destination = shortestPath.get(i + 1);
      try {
        double weight = graph.getEdge(source, destination);
        travelTimes.add(weight);
      } catch (NoSuchElementException e) {
        e.printStackTrace();
      }
    }
    return travelTimes;
  }

  @Override
  public List<String> getReachableLocations(String startLocation, double timesInSec) {
    List<String> reachableLocations = new ArrayList<>();
    for (String location : allNodes) {
      Double travelTime = null;
      if (!(location.equals(startLocation))) {
        try {
          travelTime = graph.getEdge(startLocation, location);
        } catch (NoSuchElementException e) {
          continue;
        }
        if (travelTime <= timesInSec) {
          reachableLocations.add(location);
        }
      }
    }
    return reachableLocations;
  }
}
