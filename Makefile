runTests: BackendImplementation.java App.java BackendInterface.java FrontendInterface.java GraphADT.java campus.dot BackendDeveloperTests.java BackendPlaceholder.java FrontendPlaceholder.java GraphPlaceholder.java HashtableMap.java MapADT.java BaseGraph.java DijkstraGraph.java Frontend.java
	javac BackendInterface.java
	javac GraphADT.java    
	javac BackendPlaceholder.java
	javac -cp .:../junit5.jar BackendImplementation.java
	javac GraphPlaceholder.java
	javac MapADT.java
	javac -cp .:../junit5.jar HashtableMap.java
	javac BaseGraph.java
	javac -cp .:../junit5.jar DijkstraGraph.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar BackendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c BackendDeveloperTests

runApp: App.java
	javac -cp .:../junit5.jar BackendImplementation.java
	javac GraphPlaceholder.java
	javac MapADT.java
	javac -cp .:../junit5.jar HashtableMap.java
	javac BaseGraph.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar Frontend.java
	javac --module-path ../javafx/lib --add-modules javafx.controls App.java
	java --module-path ../javafx/lib --add-modules javafx.controls App

clean:
	rm -f *.class

runFDTests:FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests
