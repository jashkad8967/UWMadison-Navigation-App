import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.control.Button;

public class BackendDeveloperTests extends ApplicationTest {

  /**
   * Test to verify the correctness of getListOfAllLocations method. This test checks if the list of
   * all locations returned matches the expected list.
   */
  @Test
  public void testGetListOfAllLocations() {
    BackendInterface backend = new BackendImplementation(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
      Assertions.assertTrue(true);
    } catch (IOException e) {
      Assertions.fail("IOException thrown: " + e.getMessage());
    }
    List<String> locations = backend.getListOfAllLocations();
    Assertions.assertTrue(locations.contains("Union South"));
    Assertions.assertTrue(locations.contains("Computer Sciences and Statistics"));
    Assertions.assertTrue(locations.contains("Atmospheric, Oceanic and Space Sciences"));
    Assertions.assertTrue(locations.contains("Skywalk"));
    Assertions.assertTrue(locations.contains("Fleet and Service Garage"));
    Assertions.assertTrue(locations.contains("Rust-Schreiner Hall"));
    Assertions.assertTrue(locations.contains("Meiklejohn House"));
    Assertions.assertTrue(locations.contains("Wisconsin Primate Center"));
    Assertions.assertTrue(locations.contains("Wisconsin National Primate Research Center"));
    Assertions.assertTrue(locations.contains("Medical Sciences"));
    Assertions.assertTrue(locations.contains("Plant Sciences"));
    Assertions.assertTrue(locations.contains("Adams Residence Hall"));
    Assertions.assertTrue(locations.contains("Tripp Residence Hall"));
    Assertions.assertTrue(locations.contains("206 Bernard Ct."));
    Assertions.assertTrue(locations.contains("University Club"));
    Assertions.assertTrue(locations.contains("University Avenue Ramp (Lot 20)"));
    Assertions.assertTrue(locations.contains("Athletic Operations Building"));
    Assertions.assertTrue(locations.contains("Field House"));
    Assertions.assertTrue(locations.contains("Kellner Hall"));
    Assertions.assertTrue(locations.contains("Meriter Laboratories"));
    Assertions.assertTrue(locations.contains("Wisconsin Energy Institute"));
    Assertions.assertTrue(locations.contains("7-Eleven"));
    Assertions.assertTrue(locations.contains("Spring Brook Row Apartments"));
    Assertions.assertTrue(locations.contains("The Regent Apartments"));
    Assertions.assertTrue(locations.contains("Lark at Randall"));
    Assertions.assertTrue(locations.contains("Mickies Dairy Bar"));
    Assertions.assertTrue(locations.contains("UW Credit Union"));
    Assertions.assertTrue(locations.contains("Vantage Point"));
    Assertions.assertTrue(locations.contains("Choles Floral"));
    Assertions.assertTrue(locations.contains("The Neighborhood House"));
    Assertions.assertTrue(locations.contains("Indie Coffee"));
    Assertions.assertTrue(locations.contains("Budget Bicycle Center - New Bicycles"));
    Assertions.assertTrue(locations.contains("Hotel Red"));
    Assertions.assertTrue(locations.contains("X01"));
    Assertions.assertTrue(locations.contains("Sconnie Bar"));
    Assertions.assertTrue(locations.contains("Memorial Arch"));
    Assertions.assertTrue(locations.contains("Phi Kappa Theta"));
    Assertions.assertTrue(locations.contains("DeLuca Biochemistry Building"));
    Assertions.assertTrue(locations.contains("Luther Memorial Church"));
    Assertions.assertTrue(locations.contains("Jenson Auto"));
    Assertions.assertTrue(locations.contains("Brat Stand"));
    Assertions.assertTrue(locations.contains("Oakland on Monroe"));
    Assertions.assertTrue(locations.contains("Kosharie"));
    Assertions.assertTrue(locations.contains("Civil War Stockade"));
    Assertions.assertTrue(locations.contains("Camp Randall Stadium"));
    Assertions.assertTrue(locations.contains("Weeks Hall for Geological Sciences"));
    Assertions.assertTrue(locations.contains("Mosse Humanities Building"));
    Assertions.assertTrue(locations.contains("Memorial Union"));
    Assertions.assertTrue(locations.contains("Science Hall"));
    Assertions.assertTrue(locations.contains("Brat Stand"));
    Assertions.assertTrue(locations.contains("Helen C White Hall"));
    Assertions.assertTrue(locations.contains("Radio Hall"));
    Assertions.assertTrue(locations.contains("Wisconsin State Historical Society"));
    Assertions.assertTrue(locations.contains("Van Hise Hall"));
    Assertions.assertTrue(locations.contains("Steenbock Memorial Library"));
    Assertions.assertTrue(locations.contains("Lot 36 - Observatory Drive Ramp"));
    Assertions.assertTrue(locations.contains("DeLuca Biochemistry Laboratories"));
    Assertions.assertTrue(locations.contains("Agricultural Dean's Residence"));
    Assertions.assertTrue(locations.contains("Slichter Residence Hall"));
    Assertions.assertTrue(locations.contains("Bascom Hall"));
    Assertions.assertTrue(locations.contains("Waters Residence Hall"));
    Assertions.assertTrue(locations.contains("North Hall"));
    Assertions.assertTrue(locations.contains("Education Building"));
  }

  /**
   * Test to verify the correctness of findShortestPath method. This test checks if the shortest
   * path between two locations is correctly returned.
   */
  @Test
  public void testFindShortestPath() {
    BackendInterface backend = new BackendImplementation(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
      Assertions.assertTrue(true);
    } catch (IOException e) {
      Assertions.fail("IOException thrown: " + e.getMessage());
    }
    List<String> expected = Arrays.asList(
        "Union South, Atmospheric, Oceanic and Space Sciences");
    List<String> shortestPath =
        backend.findShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
    Assertions.assertEquals(expected.toString(),
        shortestPath.toString());
  }

  /**
   * Test to verify the correctness of getTravelTimesOnPath method. This test checks if the list of
   * travel times on the shortest path between two locations is correctly returned.
   */
  @Test
  public void testGetTravelTimesOnPath() {
    BackendInterface backend = new BackendImplementation(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
      Assertions.assertTrue(true);
      // If no exception is thrown, loading was successful
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
    List<Double> travelTimes = backend.getTravelTimesOnPath("Union South", "Memorial Union");
    Assertions.assertEquals(Arrays.asList(176.0, 164.20000000000002, 128.9, 199.1, 192.3, 147.4,
        157.3, 202.29999999999998, 105.8), travelTimes);
  }


  /**
   * Test to verify the correctness of the getReachableLocations() method. This test checks if the
   * method returns the correct nodes.
   */
  @Test
  public void testGetReachableLocations() {
    // Create a graph placeholder with some sample data
    BackendImplementation backend = new BackendImplementation(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
      Assertions.assertTrue(true);
      // If no exception is thrown, loading was successful
    } catch (IOException e) {
      Assertions.fail(e.getMessage());
    }
    // Test reachable locations from "Union South" within 200 seconds
    List<String> expected = Arrays.asList(
        "Wendt Commons, Memorial Arch, 1410 Engineering Dr, Computer Sciences and Statistics, Atmospheric, Oceanic and Space Sciences");
    List<String> actual = backend.getReachableLocations("Union South", 200);
    Assertions.assertEquals(expected.toString(), actual.toString());

    // Test reachable locations from "Computer Sciences and Statistics" within 200 seconds
    expected = Arrays.asList("Atmospheric, Oceanic and Space Sciences, Rust-Schreiner Hall");
    actual = backend.getReachableLocations("Computer Sciences and Statistics", 150);
    Assertions.assertEquals(expected.toString(), actual.toString());

    // Test reachable locations from "Atmospheric, Oceanic and Space Sciences" within 150 seconds
    expected = Arrays.asList(
        "Computer Sciences and Statistics, Rust-Schreiner Hall, Weeks Hall for Geological Sciences, Vantage Point");
    actual = backend.getReachableLocations("Atmospheric, Oceanic and Space Sciences", 150);
    Assertions.assertEquals(expected.toString(), actual.toString());
  }

  @BeforeEach
  public void setup() throws Exception {
    BackendImplementation backend = new BackendImplementation(new DijkstraGraph<String, Double>());
    try {
      backend.loadGraphData("campus.dot");
      Frontend.setBackend(backend);
    } catch (IOException e) {
      Assertions.fail("IOException thrown: " + e.getMessage());
    }
    ApplicationTest.launch(Frontend.class);
  }

  @Test
  public void integrationTestShortestPath() {
    Button startSelector = lookup("#startSelector").query();
    Button endSelector = lookup("#endSelector").query();
    Label pathLabel = lookup("#pathLabel").query();

    // case 1: click find when no start and end location selected
    clickOn("#find");
    assertEquals("Please select the start and end location.", pathLabel.getText(),
        "test 1: case 1 failed");

    // case 2: select only one of the location
    clickOn("#startSelector");
    clickOn("#UnionSouth1");
    clickOn("#find");
    assertEquals("Union South", startSelector.getText(), "test 1: case 2 failed");
    assertEquals("Please select the start and end location.", pathLabel.getText(),
        "test 1: case 2 failed");

    // case 3: select both locations
    clickOn("#endSelector");
    clickOn("#ComputerSciencesandStatistics2");
    clickOn("#find");
    assertEquals("Computer Sciences and Statistics", endSelector.getText(),
        "test 1: case 3 failed");
    assertEquals(true, pathLabel.getText().contains("Results List:"), "test 1: case 3 failed");

    // case 4: select same locations
    clickOn("#endSelector");
    clickOn("#UnionSouth2");
    clickOn("#find");
    assertEquals("Union South", endSelector.getText(), "test 1: case 4 failed");
    assertEquals(
        "The end location can't be same as the start location.\n Please select a different end location.",
        pathLabel.getText(), "test 1: case 4 failed");

  }

  @Test
  public void integrationTestCreateFindReachableControls() {
    Label reachableLabel = lookup("#reachableLabel").query();

    // case 1: find the location when time and location is not selected
    clickOn("#findLocations");
    assertEquals("Please select location and time.", reachableLabel.getText(),
        "Itergration test 1: case 1 failed");

    // case 2: find the location in 110 sec
    clickOn("#locSelector");
    clickOn("#RadioHall3");
    clickOn("#timeSelector");
    clickOn("#s110");
    clickOn("#findLocations");
    assertEquals(reachableLabel.getText(), "Location in 110 secs walking distance:\n\tScience Hall",
        "Itergration test 1: case 2 failed");
  }

  @Test
  public void partnerTestShortestPath() {
    Label pathLabel = lookup("#pathLabel").query();

    // Case 1: click find when no start and end location selected
    clickOn("#find");
    Assertions.assertEquals("Please select the start and end location.", pathLabel.getText(),
        "Test: No locations selected");

    // Case 2: select only one of the locations
    Button startSelector = lookup("#startSelector").query();
    clickOn("#startSelector");
    clickOn("#UnionSouth1");
    clickOn("#find");
    Assertions.assertEquals("Union South", startSelector.getText(), "Test: One location selected");

    // Case 3: select both locations
    Button endSelector = lookup("#endSelector").query();
    clickOn("#endSelector");
    clickOn("#ComputerSciencesandStatistics2");
    clickOn("#find");
    Assertions.assertEquals("Computer Sciences and Statistics", endSelector.getText(),
        "Test: Both locations selected");
    Assertions.assertTrue(pathLabel.getText().contains("Results List:"),
        "Test: Both locations selected");

    // Case 4: select same locations
    clickOn("#endSelector");
    clickOn("#UnionSouth2");
    clickOn("#find");
    Assertions.assertEquals("Union South", endSelector.getText(), "Test: Same locations selected");
    Assertions.assertEquals(
        "The end location can't be same as the start location.\n Please select a different end location.",
        pathLabel.getText(), "Test: Same locations selected");
  }

  @Test
  public void partnerTestReachableLocations() {
    Label reachableLabel = lookup("#reachableLabel").query();

    // Case 1: find the location when time and location are not selected
    clickOn("#findLocations");
    Assertions.assertEquals("Please select location and time.", reachableLabel.getText(),
        "Test: No locations selected");

    // Case 2: select only location without time
    Button locSelector = lookup("#locSelector").query();
    clickOn("#locSelector");
    clickOn("#UnionSouth3");
    clickOn("#findLocations");
    Assertions.assertEquals("Union South", locSelector.getText(),
        "Test: Location selected without time");
    Assertions.assertEquals("Please select location and time.", reachableLabel.getText(),
        "Test: Location selected without time");

    // Case 3: select location and time
    Button timeSelector = lookup("#timeSelector").query();
    clickOn("#timeSelector");
    clickOn("#s100");
    clickOn("#findLocations");
    Assertions.assertEquals("100 secs", timeSelector.getText(), "Test: Location and time selected");
    Assertions.assertTrue(
        reachableLabel.getText().contains("Location in 100 secs walking distance:"),
        "Test: Location and time selected");
  }
}
