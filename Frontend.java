import java.util.List;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Frontend extends Application {

	private static BackendInterface back = new BackendPlaceholder(new GraphPlaceholder());

	private String startText = "press to select";
	private String endText = "press to select";
	private String currentText = "press to select";
	private String timesText = "press to select";

	private List<String> pathList = new ArrayList<>();
	private List<Double> timeList = new ArrayList<>();
	private List<String> reachList = new ArrayList<>();
	private List<String> locList;

	private boolean checked = false;
	private boolean clickedStart = false;
	private boolean clickedEnd = false;
	private boolean clickedCurrent = false;
	private boolean clickedTime = false;

	private ListView<Button> startSelectorList = null;
	private ListView<Button> endSelectorList = null;
	private ListView<Button> locSelectorList = null;
	private ListView<Button> timeSelectorList = null;

	private Button startSelector = new Button(startText);
	private Button endSelector = new Button(endText);
	private Button locSelector = new Button(currentText);
	private Button timeSelector = new Button(timesText);

	private Label pathLabel = null;
	private Label reachableLabel = null;

	private String reachableText = "Please select location and time.";
	private String pathLabelText = "Please select the start and end location.";
	
	 public static void setBackend(BackendImplementation back) {
		    Frontend.back = back;
		  }

	public void start(Stage stage) {
		try{back.loadGraphData("./campus.dot");}
		catch(Exception e){}
		locList = back.getListOfAllLocations();
		Pane root = new Pane();
		root.setId("root");
		createAllControls(root);
		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
		stage.setTitle("P2: Prototype");
		stage.show();
	}

	@Override
	public void createAllControls(Pane parent) {
		createShortestPathControls(parent);
		createPathListDisplay(parent);
		createAdditionalFeatureControls(parent);
		createAboutAndQuitControls(parent);
	}

	@Override
	public void createShortestPathControls(Pane parent) {
		// create new label for path start and path end
		Label src = new Label("Path Start Selector:  ");
		src.setLayoutX(32);
		src.setLayoutY(16);
		parent.getChildren().add(src);
		Label dst = new Label("Path End Selector: ");
		dst.setLayoutX(32);
		dst.setLayoutY(48);
		parent.getChildren().add(dst);

		// create a ObservableList of Button, each button is one of the building in the
		// map
		ObservableList<Button> startLocationlist = FXCollections.observableArrayList();
		for (int i = 0; i < locList.size(); i++) {
			final String locs = locList.get(i);
			Button loc = new Button(locList.get(i));
			String id = locList.get(i).replaceAll(" ", "")+ "1";
			loc.setId(id);
			// if the button is clicked, the start point set to
			// location and the selector button's text set to the location
			// after clicked the button, close the ListView
			loc.setOnAction(e -> {
				startText = locs;
				startSelector.setText(locs);
				clickedStart = true;
				remove(parent);
			});
			// add the button
			startLocationlist.add(loc);
		}

		startSelector.setLayoutX(160);
		startSelector.setLayoutY(12);
		startSelector.setId("startSelector");
		// open the list by click the select button
		startSelector.setOnAction(e -> {
			startSelectorList = new ListView<>(startLocationlist);
			startSelectorList.setLayoutX(160);
			startSelectorList.setLayoutY(12);
			startSelectorList.setId("startSelectorList");
			parent.getChildren().add(startSelectorList);
		});
		// add the selector
		parent.getChildren().add(startSelector);

		ObservableList<Button> endLocation = FXCollections.observableArrayList();
		for (int i = 0; i < locList.size(); i++) {
			final String locs = locList.get(i);
			Button loc = new Button(locList.get(i));
			String id = locList.get(i).replaceAll(" ", "")+ "2";
			loc.setId(id);
			// if the button is clicked, the end point set to
			// location and the selector button's text set to the location.
			// after clicked the button, close the ListView
			loc.setOnAction(e -> {
				endText = locs;
				endSelector.setText(locs);
				clickedEnd = true;
				remove(parent);
			});
			endLocation.add(loc);
		}

		// set the button and add action on it
		endSelector.setLayoutX(153);
		endSelector.setLayoutY(45);
		endSelector.setId("endSelector");
		endSelector.setOnAction(e -> {
			// open the ListView after click the button
			endSelectorList = new ListView<>(endLocation);
			endSelectorList.setLayoutX(160);
			endSelectorList.setLayoutY(40);
			endSelectorList.setId("endSelectorList");
			parent.getChildren().add(endSelectorList);
		});
		parent.getChildren().add(endSelector);

		// create the find button
		Button find = new Button("Submit/Find Button");
		find.setLayoutX(32);
		find.setLayoutY(80);
		find.setId("find");
		find.setOnAction(e -> {
			// if the start and end location is been set to a building
			// call on the Backend method which find the shortest path and cost of time
			if (locList.contains(startText) && locList.contains(endText)) {
				pathList = back.findShortestPath(startText, endText);
				timeList = back.getTravelTimesOnPath(startText, endText);
			}
			// renew the path Label
			createPathListDisplay(parent);
		});
		parent.getChildren().add(find);

		// Initialize the path list label and add the pane
		pathLabel = new Label(pathLabelText);
		pathLabel.setId("pathLabel");
		pathLabel.setLayoutX(32);
		pathLabel.setLayoutY(112);
		parent.getChildren().add(pathLabel);
	}

	/**
	 * helper method for remove the LiewList after clicked the button in it
	 *
	 * @param parent
	 */
	private void remove(Pane parent) {
		if (clickedStart) {
			parent.getChildren().remove(startSelectorList);
			clickedStart = false;
		}
		if (clickedEnd) {
			parent.getChildren().remove(endSelectorList);
			clickedStart = false;
		}
		if (clickedCurrent) {
			parent.getChildren().remove(locSelectorList);
			clickedCurrent = false;
		}
		if (clickedTime) {
			parent.getChildren().remove(timeSelectorList);
			clickedTime = false;
		}
	}

	@Override
	public void createPathListDisplay(Pane parent) {
		String paths = "";
		// if start or end is not selected print instruction message
		if (startText.equals("press to select") || endText.equals("press to select")) {
			paths = "Please select the start and end location.";}
		// if the path is empty, show massage that give instruction
		// selective different start or end location
		else if (startText.equals(endText)) {
			paths = "The end location can't be same as the start location.\n Please select a different end location.";}
		// if the path is not empty and the time CheckBox is clicked
		// show result list with time included
		else if (!pathList.isEmpty() && checked) {
			paths = "Results List (with travel times):";
			for (int i = 0; i < pathList.size(); i++) {
				paths += "\n\t";
				if (i != 0)
					paths = paths + "-(" + timeList.get(i - 1) + "sec)->";
				paths += pathList.get(i);}
		}
		// show the regular result list
		else {
			paths = "Results List:";
			for (int i = 0; i < pathList.size(); i++) {
				paths += "\n\t";
				paths += pathList.get(i);
			}
		}
		// Initialize the Label and add to the pane
		pathLabel.setText(paths);

	}

	@Override
	public void createAdditionalFeatureControls(Pane parent) {
		createTravelTimesBox(parent);
		createFindReachableControls(parent);

	}

	@Override
	public void createTravelTimesBox(Pane parent) {
		// set a CheckBox if the checked set boolean to true false otherwise
		CheckBox showTimesBox = new CheckBox("Show Walking Times");
		showTimesBox.setLayoutX(200);
		showTimesBox.setLayoutY(83);
		showTimesBox.setId("showTimesBox");
		showTimesBox.setOnAction(e -> {
			if (showTimesBox.isSelected()) {
				checked = true;
			} else {
				checked = false;
			}
			// renew the pathLabel when the box is clicked
			createPathListDisplay(parent);
		});
		parent.getChildren().add(showTimesBox);

	}

	@Override
	public void createFindReachableControls(Pane parent) {
		// Initialize the location and time label
		Label lsr = new Label("Location Selector:");
		lsr.setLayoutX(455);
		lsr.setLayoutY(16);
		parent.getChildren().add(lsr);
		Label tsr = new Label("Time Selector:");
		tsr.setLayoutX(455);
		tsr.setLayoutY(48);
		parent.getChildren().add(tsr);

		// create a ObservableList of Button, each button is
		// one of the building in the map
		ObservableList<Button> selectLocation = FXCollections.observableArrayList();
		for (int i = 0; i < locList.size(); i++) {
			final String locs = locList.get(i);
			Button loc = new Button(locs);
			String id = locList.get(i).replaceAll(" ", "")+ "3";
			loc.setId(id);
			// if the button is clicked, the clicked button data point set to
			// current location and the selector button's text set to the location
			// after clicked the button, close the ListView
			loc.setOnAction(e -> {
				currentText = locs;
				locSelector.setText(locs);
				clickedCurrent = true;
				remove(parent);
			});
			selectLocation.add(loc);
		}

		locSelector.setLayoutX(573);
		locSelector.setLayoutY(12);
		locSelector.setId("locSelector");
		locSelector.setOnAction(e -> {
			// if the selector button clicked, open the data list
			locSelectorList = new ListView<>(selectLocation);
			locSelectorList.setLayoutX(570);
			locSelectorList.setLayoutY(12);
			locSelectorList.setId("locSelectorList");
			parent.getChildren().add(locSelectorList);
		});
		parent.getChildren().add(locSelector);

		// create a time button ObservableList if the button is clicked
		// set the button 's data to timeText and set the button's text to selector
		// and remove the ListView
		ObservableList<Button> timeloc = FXCollections.observableArrayList();
		for (int i = 10; i <= 400; i += 10) {
			final String secs = i + " secs";
			Button loc = new Button(secs);
			loc.setId("s" + String.valueOf(i));
			loc.setOnAction(e -> {
				timesText = secs;
				timeSelector.setText(secs);
				clickedTime = true;
				remove(parent);
			});
			timeloc.add(loc);
		}
		// open the list view if the selector button is clicked
		timeSelector.setLayoutX(550);
		timeSelector.setLayoutY(45);
		timeSelector.setId("timeSelector");
		timeSelector.setOnAction(e -> {
			timeSelectorList = new ListView<>(timeloc);
			timeSelectorList.setLayoutX(560);
			timeSelectorList.setLayoutY(45);
			timeSelectorList.setId("timeSelectorList");
			parent.getChildren().add(timeSelectorList);
		});
		parent.getChildren().add(timeSelector);

		Button findLocations = new Button("Find Locations");
		findLocations.setLayoutX(455);
		findLocations.setLayoutY(80);
		findLocations.setId("findLocations");
		findLocations.setOnAction(s -> {
			// if the time and current location is modified, renew the reachable list
			if (!currentText.equals("press to select") && !timesText.equals("press to select")) {
				reachList = back.getReachableLocations(currentText, Double.parseDouble(timesText.replace("secs", "")));
				if (reachList.isEmpty()) {
					reachableText = "No location found in " + timesText + " walking distance";
				} else {
					reachableText = "Location in " + timesText + " walking distance:";
					for (String i : reachList) {
						reachableText += "\n\t";
						reachableText += i;
					}
				}
			}
			reachableLabel.setText(reachableText);
		});

		reachableLabel = new Label(reachableText);
		reachableLabel.setLayoutX(455);
		reachableLabel.setLayoutY(112);
		reachableLabel.setId("reachableLabel");
		parent.getChildren().add(reachableLabel);
		parent.getChildren().add(findLocations);
	}

	@Override
	public void createAboutAndQuitControls(Pane parent) {
		// set the about menu context
		final String aboutContext = "This is a map applet on the University of Wisconsin-Madison \n"
				+ "campus. The main function is to obtain the shortest distance \n"
				+ "route between any two buildings in the school by selecting the \n"
				+ "starting point and destination. Other functions include \n"
				+ "displaying the walking time of the shortest route and the \n"
				+ "buildings that can be reached within the selected walking time. \n\n"
				+ "\t\t\t(*^_^*) Thanks for using (*^_^*)";

		// set the about button, if clicked, remove all the object from pane
		// in the same time set label and add them to the pane
		Button about = new Button("About");
		about.setLayoutX(32);
		about.setLayoutY(600);
		about.setId("about");
		about.setOnAction(e -> {
			parent.getChildren().clear();
			Label title = new Label("About the APP");
			title.setStyle("-fx-font-size: 22px;");
			title.setFont(new Font(22));
			title.setLayoutX(375);
			title.setLayoutY(100);
			title.setId("title");
			parent.getChildren().add(title);

			Label aboutLabel = new Label(aboutContext);
			aboutLabel.setStyle("-fx-font-size: 18px;");
			aboutLabel.setFont(new Font(18));
			aboutLabel.setLayoutX(160);
			aboutLabel.setLayoutY(220);
			aboutLabel.setId("aboutLabel");
			parent.getChildren().add(aboutLabel);

			// set quit button, if clicked, clean all the object in pane
			// add all other controls back
			Button exit = new Button("Exit");
			exit.setLayoutX(96);
			exit.setLayoutY(600);
			exit.setId("exit");
			exit.setOnAction(s -> {
				parent.getChildren().clear();
				createAllControls(parent);
			});
			parent.getChildren().add(exit);
		});
		parent.getChildren().add(about);

		// set quit button, if clicked, end the program
		Button quit = new Button("Quit");
		quit.setLayoutX(96);
		quit.setLayoutY(600);
		quit.setId("quit");
		quit.setOnAction(e -> Platform.exit());
		quit.requestFocus();
		parent.getChildren().add(quit);

	}
}
