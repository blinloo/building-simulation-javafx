package gui.building;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;

@SuppressWarnings("Duplicates")

public class BuildingController extends Application
{
    private int canvasSize = 600;
    Building myBuilding = new Building("30 25;0 0 20 8 6 8;20 0 29 15 20 10;0 16 10 24 3 16;20 15 29 24 20 17");
    private int m = 18;//(int)Math.round((myBuilding.getXSize()+myBuilding.getYSize())/2.8); //multiplier for the building sizes to make it visible on the canvas
    private GraphicsContext gc;
    private Label labelBuildingInfo = new Label();
    private AnimationTimer animation;

    private Image brick = new Image(getClass().getResourceAsStream("Gray_Brick2.png"));
    private Image window = new Image(getClass().getResourceAsStream("Glass2.png"));
    private Image person = new Image(getClass().getResourceAsStream("Person.png"));
    private Image chair = new Image(getClass().getResourceAsStream("Wooden_Chair.png"));
    private Image goldenChair = new Image(getClass().getResourceAsStream("Golden_Chair.png"));
    private Image lightOn = new Image(getClass().getResourceAsStream("Topaz_Gemspark_Block.png"));
    private Image lightOff = new Image(getClass().getResourceAsStream("Diamond_Gemspark_Block.png"));
    private Image table = new Image(getClass().getResourceAsStream("Wooden_Table.png"));
    private Image furnace = new Image(getClass().getResourceAsStream("Furnace.png"));
    private Image toilet = new Image(getClass().getResourceAsStream("Toilet.png"));
    private Image goldenToilet = new Image(getClass().getResourceAsStream("Golden_Toilet.png"));
    private Image sink = new Image(getClass().getResourceAsStream("Sink.png"));
    private Image bed = new Image(getClass().getResourceAsStream("Bed.png"));
    private Image bed2 = new Image(getClass().getResourceAsStream("Bed2.png"));
    private Image bed3 = new Image(getClass().getResourceAsStream("Bed3.png"));
    private Image bed4 = new Image(getClass().getResourceAsStream("Bed4.png"));
    private Image bedsideTable = new Image(getClass().getResourceAsStream("Work_Bench.png"));


    /**
     * Function to set up the menu
     */
    MenuBar setMenu() {
        MenuBar menuBar = new MenuBar();		// create menu

        Menu mHelp = new Menu("Help");			// have entry for help
        // then add sub menus for About and Help
        // add the item and then the action to perform
        MenuItem mAbout = new MenuItem("About");
        mAbout.setOnAction(actionEvent -> {
            showAbout(); //show about message
        });

        MenuItem miHelp = new MenuItem("Help");
        miHelp.setOnAction(actionEvent -> {
            showHelp();                         //show help message
        });
        mHelp.getItems().addAll(mAbout, miHelp); 	// add submenus to Help

        // File menu, which has Exit, Save and Load functions
        Menu mFile = new Menu("File");
//        MenuItem mNew = new MenuItem("New Building");
//        mNew.setOnAction(t -> {
//            newBuilding();                      // allows user to create a new building
//        });
        MenuItem mSave = new MenuItem("Save to file");
        mSave.setOnAction(t -> {
            saveBuilding();						// saves the current building to file
        });
        MenuItem mLoad = new MenuItem("Load from file");
        mLoad.setOnAction(t -> {
            loadBuilding();						// loads a building from file
        });
        MenuItem mLoadPreset1 = new MenuItem("Load preset 1");
        mLoadPreset1.setOnAction(t -> {
            loadPreset(1);						// loads a building
        });
        MenuItem mLoadPreset2 = new MenuItem("Load preset 2");
        mLoadPreset2.setOnAction(t -> {
            loadPreset(2);						// loads a building
        });
        MenuItem mLoadPreset3 = new MenuItem("Load preset 3");
        mLoadPreset3.setOnAction(t -> {
            loadPreset(3);						// loads a building
        });
        MenuItem mExit = new MenuItem("Exit");
        mExit.setOnAction(t -> {
            System.exit(0);				// quit program
        });
        mFile.getItems().addAll(mSave, mLoad, mLoadPreset1, mLoadPreset2, mLoadPreset3, mExit); //mNew // adds submenus to File

        menuBar.getMenus().addAll(mFile, mHelp);	// menu has File and Help

        return menuBar;					// return the menu, so can be added
    }

    /**
     * Function to set up the button box
     * Buttons:
     *      "Start" - starts the simulation
     *      "Pause" - pauses movement in the simulation
     *      "Building Info" - showing information about the current building
     *      "Add People" - allows the user to add an amount of people
     *      "Add Object(s)" - allows the user to add an amount of objects of a certain type to a specified room
     */
    private HBox setButtons()
    {
        HBox boxButtons = new HBox();
        Button btnStart = new Button("Start");
        btnStart.setOnAction(event -> {
            startSim();
        });
        Button btnPause = new Button("Pause");
        btnPause.setOnAction(event -> {
            // code for pausing the simulation
            animation.stop();
        });
        Button btnAddPeople = new Button("Add People");
        btnAddPeople.setOnAction(event -> addPeople());

        Button btnAddObject = new Button("Add Object(s)");
        btnAddObject.setOnAction(event -> addObject());

        boxButtons.getChildren().addAll(btnStart, btnPause, btnAddPeople, btnAddObject); // adds all buttons to the box
        boxButtons.setSpacing(10);
        boxButtons.setAlignment(Pos.CENTER); // aligns buttons to the center of the window
        return boxButtons;
    }

    private VBox setInfo()
    {
        VBox boxInfo = new VBox();
        //Label labelBuildingInfo = new Label();

        boxInfo.getChildren().addAll(labelBuildingInfo);
        boxInfo.setSpacing(10);
        return boxInfo;
    }

    private void changeInfo(String info)
    {
        labelBuildingInfo.setText(info);
    }

    /**
     * Function to show a message,
     * @param TStr		title of message block
     * @param CStr		content of message
     */
    private void showMessage(String TStr, String CStr) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TStr);
        alert.setHeaderText(null);
        alert.setContentText(CStr);

        alert.showAndWait();
    }

    //Shows a message with information about the program
    private void showAbout()
    {
        showMessage("About", "Building Simulation created by Sam Webb");
    }

    //Shows a message with help for the program
    private void showHelp()
    {
        showMessage("Help", "Black - Wall, not attached to room \nGrey Bricks - Room Wall \nPale Green - Door \nGlass - Window \nRed Dot - Person");
    }

    private void addPeople() {
        try {
            int num = Integer.parseInt(JOptionPane.showInputDialog("Number of people to add: "));
            myBuilding.addPerson(num);
            drawBuilding();
        } catch (Exception e) {
            System.out.println("no values entered");
        }
    }

    private void loadPreset(int preset) {
        switch(preset) {
            case 1:
                newBuilding("30 25;0 0 20 8 6 8;20 0 29 15 20 10;0 16 10 24 3 16;20 15 29 24 20 17");
                break;
            case 2:
                newBuilding("20 20;0 0 10 12 10 4;0 12 19 19 14 12");
                break;
            case 3:
                newBuilding("30 30;0 0 10 6 10 3;0 6 10 12 10 9;0 12 10 18 10 15;0 18 10 24 10 21;0 24 10 29 10 26;19 0 29 6 19 3");
                break;
        }
    }

    private File getFile() //returns a file chosen by the user
    {
        File selectedFile = new File("blank.txt");
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(new JFrame());
        if (result == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = chooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
        return selectedFile;
    }

    private void saveBuilding()
    {
        BufferedWriter writer = null; //initialises the writer
        try {
            File saveFile = getFile();
            System.out.println(saveFile.getCanonicalPath()); //shows where file is saved

            String saveString = myBuilding.getCreateString();
            writer = new BufferedWriter(new FileWriter(saveFile));
            writer.write(saveString); //writes string for building to file
            System.out.println("Saved building: " + saveString);

            writer.newLine();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //Close the writer regardless of what happens
                writer.close();
            } catch (Exception e) {
                System.out.println("Saving failed");
            }
        }
    }

    private String loadBuilding()
    {
        BufferedReader reader = null; //initialises the reader
        String buildS = "";
        try {
            File loadFile = getFile();
            reader = new BufferedReader(new FileReader(loadFile));
            buildS = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //Close the reader regardless of what happens
                reader.close();
            } catch (Exception e) {
                System.out.println("Loading failed");
            }
        }
        return buildS;
    }

    private void newBuilding(String buildingInput)
    {
        myBuilding = new Building(buildingInput);

        drawBuilding();
        // get inputs from user to create a new building

    }

    /**
     * Creates a window to allow the user to input all the values for a new object
     * @return a string with type, amount and room number for creating the object
     */
    private String[] getObjectInput()
    {
        String[] s = {"", "", ""};
        JTextField typeF = new JTextField(5);
        JTextField amountF = new JTextField(5);
        JTextField roomNumF = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Type of object(table, chair, toilet, sink or bed):"));
        myPanel.add(typeF);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Amount to add:"));
        myPanel.add(amountF);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Room to put object in: "));
        myPanel.add(roomNumF);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Enter object details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            s[0] = typeF.getText();
            s[1] = amountF.getText();
            s[2] = roomNumF.getText();
        }
        if ((result == JOptionPane.CANCEL_OPTION) || (result == JOptionPane.CLOSED_OPTION)) {
            s[0] = "error";
        }
        return s;
    }

    /**
     * Code to add a new object to the building
     * Verifies user inputted values here too
     */
    private void addObject() {
        String[] s = getObjectInput();
        if (s[0] != "error"){
            String type = s[0]; //convert inputs to correct types
            try {
                int amount = Integer.parseInt(s[1]);
                int roomNum = Integer.parseInt(s[2]) - 1;
                if ((roomNum < myBuilding.getNoRooms()) && (roomNum > -1)) { //ensure room exists
                    System.out.println(type);
                    switch (type) { //ensure object type exists
                        case "sink":
                        case "toilet":
                        case "chair":
                            for (int i = 0; i < amount; i++) {
                                if (myBuilding.getRoom(roomNum).addThing(type, 1))
                                    System.out.println("added successfully");
                            }
                            break;
                        case "table":
                        case "bed":
                            for (int i = 0; i < amount; i++) {
                                if (myBuilding.getRoom(roomNum).addThing(type, 2))
                                    System.out.println("added successfully");
                            }
                            break;
                        default:
                            System.out.println("invalid object type");
                            break;
                    }

                } else
                    System.out.println("invalid room number");
            } catch (Exception e) {
                System.out.println("no values entered");
            }
        } else
            System.out.println("cancelled by user");
        drawBuilding();
    }

    /**
     * Draws a rectangle on the canvas using these defined values
     * @param x		starting x point
     * @param y		starting y point
     * @param w     width of rectangle
     * @param h     height of rectangle
     * @param outlineColour     colour to be used for the outline
     */
    private void drawHRect(double x, double y, int w, int h, Color outlineColour)
    {
        x=x*m; y=y*m; w=w*m; h=h*m;
        gc.setFill(outlineColour);
        gc.fillRect(x, y, w, h);
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(x + m, y + m, w-(2*m), h-(2*m));
    }

    private void drawRect(int x, int y, int w, int h, Color fillColour) {
        x = x*m; y = y*m; w *= m; h *= m;
        gc.setFill(fillColour);
        gc.fillRect(x, y, w, h);
    }

    private void drawPoint(Point2D p, Color colour) {
        double x = p.getX() * m; double y = p.getY() * m;
        gc.setFill(colour);
        gc.fillRect(x, y, m, m);
    }

    private void drawIt (Image i, Point2D p, double szw, double szh) {
        // to draw centred at x,y, give top left position and x,y size
        double x = p.getX() * m; double y = p.getY() * m;
        gc.drawImage(i, x, y, szw, szh);
    }

    private void clearSpace(Point2D p, double w, double h) {
        double x = p.getX() * m; double y = p.getY() * m;
        gc.clearRect(x, y, w, h);
    }

    private void drawPeople() {
        for (int pct = 0; pct < myBuilding.getPersonCount(); pct++) {
            Person p = myBuilding.getPerson(pct);
            drawIt(person, p.getPos(), m/2, m/2);
        }
    }

    /**
     * draws the building on the canvas with room, objects and people
     */
    private void drawBuilding() {
        gc.clearRect(0, 0, canvasSize, canvasSize);

        drawHRect(0, 0, myBuilding.getXSize(), myBuilding.getYSize(), Color.BLACK);
        int chance = 0;

        for (int rct = 0; rct < myBuilding.getNoRooms(); rct++) //steps through each room in the building
        {
            Room r = myBuilding.getRoom(rct);
            //drawHRect(r.getP1().getX(), r.getP1().getY(), r.getWidth(), r.getHeight(), Color.DARKGREEN);
            for (double xct = r.getP1().getX(); xct <= r.getP2().getX(); xct++) {
                drawIt(brick, new Point2D(xct, r.getP1().getY()), m, m);
                drawIt(brick, new Point2D(xct, r.getP2().getY()), m, m);
            }
            for (double yct = r.getP1().getY(); yct <= r.getP2().getY(); yct++) {
                drawIt(brick, new Point2D(r.getP1().getX(), yct), m, m);
                drawIt(brick, new Point2D(r.getP2().getX(), yct), m, m);
            }

            drawPoint(r.getDoor(), Color.PALEGREEN); //draw door



            for (Thing t : r.getThingsInRoom()) {
                switch (t.getType()) {
                    case "table": drawIt(table, t.getTopCorner(), 2*m, m); break;
                    case "chair": drawIt(chair, t.getTopCorner(), m, m); break;
                    case "bedsideTable": drawIt(bedsideTable, t.getTopCorner(), m, m); break;
                    case "window": drawIt(window, t.getTopCorner(), m, m); break;
                    case "light":
                        if (t.getState()) {
                            drawIt(lightOn, t.getTopCorner(), m, m);
                        } else {
                            drawIt(lightOff, t.getTopCorner(), m, m);
                        }
                        break;
                    case "furnace": drawIt(furnace, t.getTopCorner(), m, m); break;
                    case "toilet":
                        drawIt(toilet, t.getTopCorner(), m, m);
                        break;
                    case "sink": drawIt(sink, t.getTopCorner(), m, m); break;
                    case "bed": drawIt(bed, t.getTopCorner(), 2*m, m); break;
//                        chance = ThreadLocalRandom.current().nextInt(0, 5);
//                        if (chance == 1)
//                            drawIt(bed2, t.getTopCorner(), 2*m, m);
//                        if (chance == 3)
//                            drawIt(bed3, t.getTopCorner(), 2*m, m);
//                        if (chance == 4)
//                            drawIt(bed4, t.getTopCorner(), 2*m, m);
//                        break;
                    //default:
                }
            }
        }
        drawPeople();
        changeInfo(myBuilding.toString());
        //drawPoint(myBuilding.getBodyPos(), Color.MAGENTA);
    }

    /**
     * updates the building and information for the animation
     */
    private void updateBuilding(){
        drawBuilding();
        myBuilding.updateLights();
    }

    private void startSim() {
        //start simulation
        for (int i = 0; i < myBuilding.getPersonCount(); i++) {
            myBuilding.addDest(i);
        }
        animation.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Building Simulation by Sam Webb");

        BorderPane bp = new BorderPane();			// create border pane

        bp.setTop(setMenu());						// create menu, add to top

        Group root = new Group();					// create group
        Canvas canvas = new Canvas( canvasSize*1.5, canvasSize);
        // and canvas to draw in
        root.getChildren().add( canvas );			// and add canvas to group
        gc = canvas.getGraphicsContext2D();			// context for drawing
        bp.setCenter(root);							// put group in centre pane

        bp.setBottom(setButtons());					/// add buttons to bottom

        bp.setRight(setInfo());
        changeInfo(myBuilding.toString());

        Scene scene = new Scene(bp, canvasSize*2.5, canvasSize*1.4);
        // create scene so bigger than canvas,

        animation = new AnimationTimer() {
            private long lastUpdateT = 0;
            public void handle(long currentT) {
                if (currentT - lastUpdateT >= 60_000_000) {
                    updateBuilding();
                    for (int i = 0; i < myBuilding.getPersonCount(); i++) {
                        if (!myBuilding.moveP(i))
                            myBuilding.addDest(i);
                    }
                    lastUpdateT = currentT;
                }
            }
        };

        primaryStage.setScene(scene);
        primaryStage.show();
        drawBuilding();
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
