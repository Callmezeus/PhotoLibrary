package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is for user.
 * Each user only has a Username
 *  
 *  @author Tomer Levy
 */

public class User implements Serializable{

	private String Username;
	private List<Album> AlbumList;
	
	/**
	 * Contructor
	 * 
	 * @param Username	
	 * creates a new User
	 */
	
	public User(String Username){
		this.Username = Username;
		AlbumList = new ArrayList<Album>();
	}
	/**
	 * 
	 * Returns the Username
	 * @return username of user
	 */
	public String getUsername(){
		return Username;
	}
	/**
	 * Returns the Albums List
	 * @return List returns the album list
	 */
	public List<Album> getAlbums(){
		return AlbumList;
	}
	/**
	 * gets the album name if its in album
	 * 
	 * @param name - searches for this name in the album
	 * @return Return the album name by searching for a name of album
	 */
	public Album getAlbumName(String name) {
		for(Album albums : AlbumList)
		{
			if(albums.getName().equals(name))
				return albums;
		}
		return null;
	}
	/**
	 * finds the location of the album if it exists
	 * 
	 * @param album - The album which location must be found
	 * @return location where the album is if it exists
	 */
	public int getAlbumLocation(Album album) {
		int i = 0;
		while(i < AlbumList.size()){
			if (AlbumList.get(i).getName().equals(album.getName())){
				return i;
			}
			i++;
		}
		return -1;
	}
	/**
	 *
	 * adds a photo to an album
	 * @param newPhoto the photo to be added
	 * @param AlbumLocation - The Location of the photo
	 */
	public void addPhotoToAlbum(Photo newPhoto, int AlbumLocation) {
		AlbumList.get(AlbumLocation).addPhoto(newPhoto);
	}
	
	
	/**
	 * 
	 * Creates a new album named Album_Name
	 * 
	 * @param Album_Name - creates a new album with this name
	 */
	public void newAlbum(String Album_Name){
		AlbumList.add(new Album(Album_Name));
	}
	/**
	 * Adds an album to AlbumList
	 * 
	 * @param New_Album the new album
	 */
	public void createAlbum(Album New_Album) {
		AlbumList.add(New_Album);
	}
	/**
	 * deletes album from List
	 * 
	 * @param Album_Name the album to be deleted
	 */
	 
	public void deleteAlbum(Album Album_Name){
		AlbumList.remove(Album_Name);
	}
	/**
	 * Checks whether an Album exists
	 * 
	 * @param Album_Name album to be seached for
	 * @return true or false
	 */
	public boolean AlbumExists(String Album_Name) {
		for (Album album: AlbumList){
			if (album.getName().toUpperCase().equals(Album_Name.trim().toUpperCase()))
				return true;
		}
		return false;
	}
	
	
	
}
