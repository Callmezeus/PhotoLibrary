package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Album class, holds all the needed stuff for albums which hold the photos object
 * @author Tomer Levy
 */

public class Album implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1856965090006378782L;
	//Name of Album, List of photos, keep reference on oldest and newest photo's
	private String Album_Name;	
	private List<Photo> PhotoLibrary;
	private Photo LatestPhoto, OldestPhoto;
	
	/**
	 * Album constructor
	 * @param Album_Name string for the new album name
	 */
	public Album(String Album_Name) {
		this.Album_Name = Album_Name;
		PhotoLibrary = new ArrayList<Photo>();
		OldestPhoto = null;
		LatestPhoto = null;
	}
	/**
	 * setter for album name
	 * @param newName set new album name
	 */
	public void setAlbumName(String newName){
		this.Album_Name = newName;
	}
	/**
	 * getter for album name
	 * @return Album_name return album name
	 */
	public String getName(){
		return Album_Name;
	}

	
	/**
	 * getter for photo
	 * @param num this is the index of the photo
	 * @return return the Photo at the given index
	 */
	public Photo getPhoto(int num){
		return PhotoLibrary.get(num);
	}
	
	
	
	/**
	 * method to add photo to the album
	 * @param photo this is the photo to be added
	 */
	public void addPhoto(Photo photo){
		PhotoLibrary.add(photo);
		setNewest();
		setOldest();
	}
	/**
	 * getter for list of photos in a List<> Format
	 * @return returns photos in album
	 */
	public List<Photo> getPhotos(){
		return PhotoLibrary;
	}
	
	/**
	 * remove photo at the given index
	 * then Update dates
	 * @param num the index of the photo, given by selection
	 */
	public void removePhoto(int num){
		PhotoLibrary.remove(num);
		setNewest();
		setOldest();
		
	}
	/**
	 *  getter for size of album
	 * @return return size of album
	 */
	public int getSize(){
		return PhotoLibrary.size();
	}
	/**
	 * Finds the index of the photo requested
	 * @param photo The actual photo 
	 * @return returns the index of the photo
	 */
	public int findPhotoIndex(Photo photo){
		int i = 0;
		while(i < PhotoLibrary.size()){
			if(PhotoLibrary.get(i).equals(photo)){
				return i;
			}
			i++;
		}
		return -1;
	}
	/**
	 * this is used for selecting the first photo in the album to be used for display
	 * @return Image the first photo or null if the album is empty
	 */
	public Image getAlbumPhoto(){
		if(!PhotoLibrary.isEmpty()){
			return PhotoLibrary.get(0).getImage();
		}
		else{
			return null;
		}
	}
	
	
	/**
	 * setter for latest photo
	 */
	public void setNewest(){
		if(PhotoLibrary.size() != 0){
			Photo temp = PhotoLibrary.get(0);
			for(Photo photo : PhotoLibrary){
				if(photo.getDate().compareTo(temp.getDate()) > 0){
					temp = photo;
				}
			}
			LatestPhoto = temp;		
		}
		else{
			return;
		}
	}
	/**
	 * getter for date of newest photo
	 * @return returns the newest Photo
	 */
	public String getNewest(){
		if(LatestPhoto != null){
			return LatestPhoto.getDate();
		}
		else{
			return "None";
		}
	}
	
	/**
	 * find the oldest photo and set it as the oldest in album
	 */
	public void setOldest(){
		if(PhotoLibrary.size() != 0){
			Photo temp = PhotoLibrary.get(0);
			for(Photo photo : PhotoLibrary){
				if(photo.getDate().compareTo(temp.getDate()) < 0){
					temp = photo;
				}
			}
			OldestPhoto = temp;		
		}
		else{
			return;
		}
	}
	/**
	 *  getter for oldest photo
	 * @return returns the oldest photo 
	 */
	public String getOldest(){
		if(OldestPhoto != null){
			return OldestPhoto.getDate();
		}
		else{
			return "None";
		}
	}
	
}
