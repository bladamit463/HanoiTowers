/*
 *An attempt at an implementation of the Hanoi Towers recursive 
 *solving algorithm in a JavaFX application.
 *
 */
package hanoitowers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Alexandre Perreault
 */
public class FXMLDocumentController implements Initializable {

    //Current running status of the solving algorithm
    private boolean isRunning = false;
    private boolean isReady = false;

    //Set disk size and the max. amount of disks allowed on-screen
    public final int DISK_SIZE = 10;
    public final int MAX_DISK = 100;

    //References to the 3 pegs
    public static enum location {
	SOURCE, AUX, DEST
    }

    //Contains HanoiDisks and diskMoves 
    private final ArrayList<HanoiDisk> diskArray = new ArrayList<>();
    private final ArrayList<diskMove> diskMoveArray = new ArrayList<>();

    //X values of each peg and Y value of base
    private double sourceX;
    private double auxX;
    private double destX;
    private double baseY;

    //Size of each peg
    private int sourceStack = 0, auxStack = 0, destStack = 0;

    //Solving and moving threads
    private Thread moving;
    private Thread solving;

    @FXML
    private final Group grpDisk = new Group();
    @FXML
    private Button btnSolve;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnStop;
    @FXML
    private TextField txfDiskCount;
    @FXML
    private Line lineSource;
    @FXML
    private Line lineAux;
    @FXML
    private Line lineDest;
    @FXML
    private Line lineBase;
    @FXML
    private AnchorPane pane;

    //Thread used to move each disk
    private class moveDiskThread implements Runnable {

	@Override
	public void run() {

	    while (!diskMoveArray.isEmpty() && isRunning) {
		try {
		    Thread.sleep(50);
		} catch (InterruptedException e) {
		}

		diskMove move = diskMoveArray.get(0);
		move.moveDisk(move.x, move.y);
		diskMoveArray.remove(0);
	    }
	    isRunning = false;
	    isReady = false;
	}
    }

    //Thread used to run the solving algorithm 
    private class HanoiSolveThread implements Runnable {

	@Override
	public void run() {
	    HanoiSolve(diskArray.get(0), location.SOURCE, location.DEST, location.AUX);
	}
    }

    /**
     * Creates a Hanoi Tower based on txfDiskCount's input and resets any
     * previous instances.
     *
     * @param event
     */
    @FXML
    private void btnCreateHanoi(ActionEvent event) {

	System.out.println("Starting up Hanoi tower creation");
	int diskNumber = 0;

	try {
	    diskNumber = Integer.parseInt(txfDiskCount.getText());
	} catch (NumberFormatException e) {
	    System.out.println("Input error");
	}

	if ((diskNumber > 0) && (diskNumber <= MAX_DISK)) {
	    System.out.println("Input validated, clearing previous instance");

	    diskArray.clear();
	    diskMoveArray.clear();
	    grpDisk.getChildren().clear();

	    sourceStack = diskNumber;
	    auxStack = 0;
	    destStack = 0;

	    for (int i = diskNumber; i > 0; i--) {
		HanoiDisk hanoiDisk = new HanoiDisk(i, createHanoiDisk(i, diskNumber - i + 1));
		diskArray.add(hanoiDisk);
		grpDisk.getChildren().add(hanoiDisk.disk);
	    }
	}
	
	isReady = true;
    }

    /**
     * Begins the recursive Hanoi solving algorithm
     *
     * @param event
     */
    @FXML
    private void btnStartSolve(ActionEvent event) throws InterruptedException {

	if (isReady && !isRunning) {
	    System.out.println("Here we go!");
	    isRunning = true;

	    moving = null;
	    solving = null;

	    solving = new Thread(new HanoiSolveThread());
	    solving.start();
	}
    }

    /**
     * Interrupts the solving processes and clears the scene
     *
     * @param event
     * @throws InterruptedException
     */
    @FXML
    private void btnStopSolve(ActionEvent event) throws InterruptedException {

	if (isRunning) {
	    System.out.println("IT'S TIME TO STOP!");
	    isRunning = false;

	    moving.join();
	    solving.join();
	    
	    diskArray.clear();
	    diskMoveArray.clear();
	    grpDisk.getChildren().clear();

	    isReady = false;
	}
    }

    /**
     * Creates a Rectangle object according to specified size and position
     *
     * @param size
     * @param position
     * @return Rectangle object
     */
    private Rectangle createHanoiDisk(int size, int position) {

	double height = DISK_SIZE;
	double width = DISK_SIZE * size;
	double x = sourceX - width / 2;
	double y = (baseY - position * height) - 2;

	Rectangle displayedDisk = new Rectangle(x, y, width, height);
	displayedDisk.setFill(Color.BLUE);
	displayedDisk.setStroke(Color.BLACK);
	return displayedDisk;
    }

    /**
     * Moves a HanoiDisk from start location to end location
     *
     * @param hanoiDisk
     * @param start
     * @param end
     */
    private void moveHanoiDisk(HanoiDisk hanoiDisk, location start, location end) {

	if (!diskArray.isEmpty() && start != end) {

	    double x;
	    double y;

	    switch (start) {

		case SOURCE:
		    sourceStack -= 1;
		    break;
		case AUX:
		    auxStack -= 1;
		    break;
		case DEST:
		    destStack -= 1;
		    break;
		default:
		    break;
	    }

	    switch (end) {

		case SOURCE:
		    sourceStack += 1;
		    x = DISK_SIZE * hanoiDisk.size / 2;
		    y = DISK_SIZE * sourceStack + 2;
		    diskMoveArray.add(new diskMove(hanoiDisk.disk, sourceX - x, baseY - y));
		    break;

		case AUX:
		    auxStack += 1;
		    x = DISK_SIZE * hanoiDisk.size / 2;
		    y = DISK_SIZE * auxStack + 2;
		    diskMoveArray.add(new diskMove(hanoiDisk.disk, auxX - x, baseY - y));
		    break;

		case DEST:
		    destStack += 1;
		    x = DISK_SIZE * hanoiDisk.size / 2;
		    y = DISK_SIZE * destStack + 2;
		    diskMoveArray.add(new diskMove(hanoiDisk.disk, destX - x, baseY - y));
		    break;
		default:
		    break;
	    }

	    if (moving == null) {
		moving = new Thread(new moveDiskThread());
		moving.start();
	    }
	}
    }

    /**
     * Recursive solving algorithm for the Hanoi puzzle
     *
     * @param disk
     * @param start
     * @param end
     * @param sec
     */
    private boolean HanoiSolve(HanoiDisk disk, location start, location end, location sec) {

	if (isRunning) {
	    if (diskArray.indexOf(disk) == diskArray.size() - 1) {
		moveHanoiDisk(disk, start, end);
	    } else {
		HanoiSolve(diskArray.get(diskArray.indexOf(disk) + 1), start, sec, end);

		moveHanoiDisk(disk, start, end);

		HanoiSolve(diskArray.get(diskArray.indexOf(disk) + 1), sec, end, start);
	    }
	}
	return isRunning;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	sourceX = lineSource.getLayoutX();
	auxX = lineAux.getLayoutX();
	destX = lineDest.getLayoutX();
	baseY = lineBase.getLayoutY();
	
	pane.getChildren().add(grpDisk);
    }

}
