package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Makes an image serializable so that it can be 
 * reloaded when the next person gets images
 * 
 * @author Tomer Levy
 */
public class SerializeImage implements Serializable {
	
	private int width, height;
	private int[][] pix;
	
	/**
	 * 
	 * @return int the size of width
	 */
	public int getWidth(){
		return width;
	}
	/**
	 * 
	 * @return int the height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * 
	 * @return the pixel 2d array
	 */
	public int[][] getPix(){
		return pix;
	}
	
	/**
	 * turns an image into a 2d pixel array so that we can serialize
	 * @param image The image that will be pixelated
	 */
	public void setImage(Image image){
		width = ((int) image.getWidth());
		height = ((int) image.getHeight());
		pix = new int[width][height];
		
		PixelReader read = image.getPixelReader();
		for(int i = 0; i < width; i++){
			for(int k = 0; k <height; k++){
				pix[i][k] = read.getArgb(i, k);
			}
		}
	}
	/**
	 * Converts a 2d array into an image
	 * @return Image returns the image that was depixelated
	 */
	public Image getImage(){
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter writer = image.getPixelWriter();
		for(int i = 0; i < width; i++){
			for(int k = 0; k <height; k++){
				writer.setArgb(i, k, pix[i][k]);
			}
		}
		
		return image;
	}
	/**
	 * This compares two images to see which are equivalent
	 * @param image to be compared with the other image
	 * @return A boolean value
	 */
	public boolean ImageEqual(SerializeImage image){
		if(height != image.getHeight()){
			return false;
		}
		if(width != image.getWidth()){
			return false;
		}
		for(int i = 0; i < width; i++){
			for(int k = 0; k<height; k++){
				if(pix[i][k] != image.getPix()[i][k]){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Default constructor for this class
	 */
	public SerializeImage(){}
}
