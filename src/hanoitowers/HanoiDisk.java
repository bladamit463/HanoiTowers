/*
 * Custom class used in the solving process. Contains a disk and its size.
 * 
 */
package hanoitowers;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author Alexandre Perreault
 */
public class HanoiDisk{
        
    public int size;
    public Rectangle disk;
        
    public HanoiDisk(int size, Rectangle disk){
	this.size = size;
	this.disk = disk;
    }
}
