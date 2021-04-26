/*
 * Salwa Abdalla 
 * ICS3U Assignment 2: 26/11/18
 * Jeff Radulovic
 * 
 * This program is a class meant to create geometric patterns using equidistant circles
 * around the circumference of a larger circle and lines to connect them using a standard formula.
 */

//all of the imports from the javaFX library needed to make this code run
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Assignment2 extends Application {
	
	//Creating default values that will be used throughout the different methods within my program
	private static int[] screenSize = {800, 800};
	private static int littleCircleradi = 3;
	private static int[] centerPoints = {400, 400};
	private static int radius = 300;
	private static int numberPoints = 10;
	private static int endnumberPoints = 100;
	private static double multiplier = 5;
	private static double multiplierIncrement = 0.1;
	private static double endMultiplier = 5;
	
	//Creating two boolean triggers which will be used to animate using the buttons
	private static boolean AnimateNtoEnd = false;
	private static boolean AnimateMtoEnd = false;
	
	//Initializing the big default circle and the group that holds it
	Group sceneObjects;
	Circle mainCircle;
	
	//Initializing the lists that will be used throughout methods
	private static double[] ycoordinates;
	private static double[] xcoordinates;
	private static Circle[] circleList;
	private static Line[] lineList;
	private static int[] connectTo;
	
	//launching the javaFX animations
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Creating the layout boxes to hold the two sections (side menu and circle)
		//creating the layout box to hold the different side menu items
		HBox root = new HBox();
		VBox buttonLayout = new VBox();
		sceneObjects = new Group();
		root.getChildren().addAll(buttonLayout, sceneObjects);
		Scene scene = new Scene(root, screenSize[0], screenSize[1]);
		
		//Calculating and drawing the Main circle template
		drawCircle();
		
		//Creating the four buttons, and setting a minimum width (for stylistic/resizing purposes)
		Button b1 = new Button("Draw N");
		Button b2 = new Button("Draw N to end");
		Button b3 = new Button("Draw to next M");
		Button b4 = new Button("Draw changing M");
		
		//Creating stylistic values
		int minbuttonsize = 150;
		b1.setMinWidth(minbuttonsize);
		b2.setMinWidth(minbuttonsize);
		b3.setMinWidth(minbuttonsize);
		b4.setMinWidth(minbuttonsize);
		
		//Creating all the labels that will appear in the side menu
		Label Nlabel = new Label(" N: ");
		Label EndNlabel = new Label(" End N: ");
		Label CurrentNlabel = new Label(" Current N: " + numberPoints);
		Label CurrentEndNLabel = new Label(" Current End N: " + endnumberPoints);
		Label Mlabel = new Label(" M: ");
		Label MIncrementlabel = new Label(" M Increment: ");
		Label EndMlabel = new Label(" End M: ");
		Label CurrentMlabel = new Label(" Current M: " + multiplier);
		Label CurrentEndMLabel = new Label(" Current End M: " + endMultiplier);
		Label CurrentMIncrementlabel = new Label(" Current M Increment: " + multiplierIncrement);
		
		//Creating all the text fields that will appear in the side menu
		TextField Ntextfield = new TextField();
		TextField EndNtextfield = new TextField();
		TextField Mtextfield = new TextField();
		TextField MIncrementtextfield = new TextField();
		TextField EndMtextfield = new TextField();
		
		//Adding the objects (in order) to the side menu
		buttonLayout.getChildren().addAll(Nlabel, Ntextfield, b1);
		buttonLayout.getChildren().addAll(EndNlabel, EndNtextfield, CurrentNlabel, CurrentEndNLabel, b2);
		buttonLayout.getChildren().addAll(Mlabel, Mtextfield, MIncrementlabel, MIncrementtextfield, b3);
		buttonLayout.getChildren().addAll(EndMlabel, EndMtextfield, b4, CurrentMlabel, 
				CurrentEndMLabel, CurrentMIncrementlabel);		
		
		//Creating, and setting values of the small circles and the lines to connect them
		changeLists();
		
		//Programming the button to draw the set value of N, with the set value of M
		b1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//Clearing the past objects (for times something has already been drawn)
				//Redrawing the main circle using a method
				//Calculating and drawing the small and the lines that have the set value of n & m
				sceneObjects.getChildren().clear();
				drawCircle();
				drawScreen(circleList, lineList, xcoordinates, ycoordinates, connectTo);
			}
		});

		//Programming the button to draw the set value of N to end N, with a set value of M
		b2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//using a boolean trigger which is within the animation update
				AnimateNtoEnd = true;
			}
		});

		//Programming the button to draw the set value of N, with the next value of M
		//the next value of M relies on the mIncrment
		b3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//Clearing the past objects (for times something has already been drawn)
				//Increasing the multiplier by the increment
				sceneObjects.getChildren().clear();
				multiplier += multiplierIncrement;
				//Redrawing the main circle using a method
				//Calculating and drawing the small and the lines that have the set value of n & m
				drawCircle();
				drawScreen(circleList, lineList, xcoordinates, ycoordinates, connectTo);
				//Updating the labels to show the correct values
				CurrentMlabel.setText(" Current M: " + multiplier);
				CurrentNlabel.setText(" Current N: " + numberPoints);
				
			}
		});

		//Programming the button to draw the set value of N, from set value of M to end M
		b4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//using a boolean trigger which is within the animation update
				AnimateMtoEnd = true;
			}
		});
		
		//This text field allows the user to change the value of N and updates corresponding label
		Ntextfield.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				numberPoints = Integer.valueOf(Ntextfield.getText());
				CurrentNlabel.setText(" Current N: " + numberPoints);
			}
		});
		
		//This text field allows the user to change the value of end N and updates label
		EndNtextfield.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				endnumberPoints = Integer.valueOf(EndNtextfield.getText());
				CurrentEndNLabel.setText(" Current End N: " + endnumberPoints);
			}
		});
		
		//This text field allows the user to change the value of M and updates corresponding label
		Mtextfield.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				multiplier = Double.valueOf(Mtextfield.getText());
				CurrentMlabel.setText(" Current M: " + multiplier);
			}
		});

		//This text field allows the user to change the value of M increment and updates label
		MIncrementtextfield.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				multiplierIncrement = Double.valueOf(MIncrementtextfield.getText());
				CurrentMIncrementlabel.setText(" Current M Increment: " + multiplierIncrement);
			}
		});
		
		//This text field allows the user to change the value of end M and updates label
		EndMtextfield.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				endMultiplier = Double.valueOf(EndMtextfield.getText());
				CurrentEndNLabel.setText(" Current End N: " + endnumberPoints);
			}
		});
		
		//The update time in nanoseconds
		int timeDiff = 100000000;
		
		//Creates the Animation thread
		AnimationTimer timer = new AnimationTimer() {
			//sets the first value of the "starting time"
			long oldTime = 0;
			@Override
			public void handle(long arg0) {
				//if the difference between current % old time is > 1/10th of a second, update screen
				if(arg0 - oldTime > timeDiff) { 
					oldTime = arg0;
					if (AnimateNtoEnd) //if Animate N to end N is true, call calculating/draw method
						upDateNumberPoints(endnumberPoints, CurrentNlabel);
					if (AnimateMtoEnd) //if Animate M to end M is true, call calculating/draw method
						upDateMultiplier(endMultiplier, CurrentMlabel);
				}
			}
		};
		timer.start(); //start the Animation timer
				
		primaryStage.setTitle("Assignment 2");
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	public void upDateNumberPoints(int endnumberPoints, Label CurrentNlabel){
		
		//If the current N is less than end N
		if (numberPoints < endnumberPoints) {
			numberPoints += 1; 										//increase N by one
			CurrentNlabel.setText(" Current N: " + numberPoints);	//update the N label
			sceneObjects.getChildren().clear();						//clearing the root
			drawCircle();											//draw the main circle
			changeLists();											//re-calculate the lists
			//draw the small circles and the lines that connect
			drawScreen(circleList, lineList, xcoordinates, ycoordinates, connectTo);
		}
		//If the current N is more than end N, same steps but goes backwards
		else if (numberPoints > endnumberPoints) {
			numberPoints -= 1;
			CurrentNlabel.setText(" Current N: " + numberPoints);
			sceneObjects.getChildren().clear();
			drawCircle();
			changeLists();
			drawScreen(circleList, lineList, xcoordinates, ycoordinates, connectTo);
		}
		else //If N is equal to end N, the boolean is false, stopping the animation
			AnimateNtoEnd = false;
		
	}
	
	public void upDateMultiplier(double endMultiplie, Label CurrentMlabel) {
		
		//If the current M is less than end M
		if (multiplier < endMultiplier) {				
			sceneObjects.getChildren().clear();						//clear the root
			multiplier += multiplierIncrement;						//add increment to M
			CurrentMlabel.setText(" Current M: " + multiplier);		//update the label
			drawCircle();											//draw the main circle
			changeLists();											//re-calculate the lists
			//draw the small circles and the lines that connect
			drawScreen(circleList, lineList, xcoordinates, ycoordinates, connectTo);
		}
		//If the current M is greater than end M, same steps but it goes backwards
		else if (multiplier > endMultiplier) {
			sceneObjects.getChildren().clear();
			multiplier -= multiplierIncrement;
			CurrentMlabel.setText(" Current M: " + multiplier);
			drawCircle();
			changeLists();
			drawScreen(circleList, lineList, xcoordinates, ycoordinates, connectTo);
		}
		else //If M is equal to end M, the boolean is false, stopping the animation
			AnimateMtoEnd = false;
	}
	
	public void drawCircle() {
		int borderThickness = 1;
		
		mainCircle = new Circle(centerPoints[0], centerPoints[1], radius); //creating the circle
		mainCircle.setFill(Color.WHITE); 								   //set colour to white
		mainCircle.setStroke(Color.BLACK);								   //set border to black
		mainCircle.setStrokeWidth(borderThickness);						   //set border thickness
		sceneObjects.getChildren().add(mainCircle);						   //add it to root
	}
	
	public void drawScreen(Circle[] circleList, Line[] lineList, double[] xcoordinates, 
			double[] ycoordinates, int[] connectTo) {
		
		changeLists(); //calculate the new positions for all circles and lines
		int lineThickness = 3; //creating line thickness for connecting lines
		
		for (int i = 0; i < numberPoints; i++) {
			//draw the circles and colour them red, add to root
			circleList[i] = new Circle(xcoordinates[i], ycoordinates[i], littleCircleradi);
			circleList[i].setFill(Color.RED);
			sceneObjects.getChildren().add(circleList[i]);

			//draw the connecting line and set thickness, add to root
			lineList[i] = new Line(xcoordinates[i], ycoordinates[i], 
					xcoordinates[(connectTo[i])], ycoordinates[(connectTo[i])]);
			lineList[i].setStrokeWidth(lineThickness);
			sceneObjects.getChildren().add(lineList[i]);
		}
	}
	
	public static void changeLists() {
		//Calculate the values of all the lists (small circles, connecting lines)
		ycoordinates = Coordinates(centerPoints, radius, numberPoints, "y");
		xcoordinates = Coordinates(centerPoints, radius, numberPoints, "x");
		circleList = new Circle[numberPoints];
		lineList = new Line[numberPoints];
		connectTo = connectTo(multiplier, numberPoints);
	}
	
	public static int[] connectTo(double multiplier, int numbPoints) {
		int[] connections = new int[numbPoints]; //create a list to connect the lines
		for (int i = 0; i < numbPoints; i++)
			connections[i] = (int)(i * multiplier) % numbPoints; //calculate the point to connect to
		return connections;
	}
	
	public static double[] Coordinates(int[] centerPoints, int radius, int numbPoints, String type){
		double totalAngle = 2 * Math.PI; //using radians (equivalent to 360)
		
		double[] coordinates = new double[numbPoints];
		double angle = totalAngle/numbPoints; //the angle in between each section
		
		if (type == "x") { //if calculating the x coordinate
			for (int i = 0; i < numbPoints; i++)
				coordinates[i] = -(radius * Math.cos(i * angle)) + centerPoints[0];
			//using the formula -(R*cos(i*angle) + center), to have points in same order as image
		}
		if (type == "y") { //if calculating the y coordinate
			for (int i = 0; i < numbPoints; i++)
				coordinates[i] = (radius * Math.sin(i * angle)) + centerPoints[1];
			//using the formula (R*sin(i*angle) + center), to have points in same order as image
		}
		return coordinates;
	}
}