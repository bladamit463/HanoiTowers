/*
 * Custom class used in the rendering process. Contains a disk and its
 * coordinates on the scene, as well as a moveDisk() method to move it.
 * 
 */
package hanoitowers;

import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Alexandre Perreault
 */
public class diskMove {
    
    public Rectangle disk;
    public double x;
    public double y;
    
    public diskMove(Rectangle disk, double x, double y){
	this.disk = disk;
	this.x = x;
	this.y = y;
    }
    
    public void moveDisk(double x, double y){	
	disk.setX(x);
	disk.setY(y);
    }
}
