package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.scene.image.Image;

/**
 * 
 * Photo class, holds all the needed stuff for Photos objects
 * 
 * @author Tomer Levy
 */
public class Photo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3117292720490753597L;
	private SerializeImage image;
	private String caption;
	private List<Tags> TagsList;
	private Calendar calendar;
	private String correctDate;
	
	/**
	 * Photo Constructor
	 */
	public Photo(){
		image = new SerializeImage();
		caption = "";
		TagsList = new ArrayList<Tags>();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Setter for photo caption
	 * @param caption  this is the photo caption given
	 */
	public void setCaption(String caption){
		this.caption = caption;
	}
	
	/**
	 * geter for photo caption
	 * @return this is the photo caption given
	 */
	public String getCaption(){
		return caption;
	}
	/**
	 * 
	 * @return the list of all the tags a photo has
	 */
	public List<Tags> getTags(){
		return TagsList;
	}
	
	/**
	 * add  a new tag to the tagslist
	 * @param tagType first parameter of tag
	 * @param tagValue second parameter of tag
	 */
	public void newTag(String tagType, String tagValue){
		TagsList.add(new Tags(tagType, tagValue));
	}
	/**
	 * getter for tag at a certain index
	 * @param num index given 
	 * @return returns the tag at the given index
	 */
	public Tags getTag(int num){
		Tags temp = TagsList.get(num);
		return temp;
	}
	
	/**
	 * 
	 * @param num this is the index at which you want to remove a tag
	 */
	public void deleteTag(int num){
		TagsList.remove(num);
	}
	
	/**
	 * 
	 * @return calendar date that the photo has
	 */
	public Calendar getCalender() {
		return calendar;
	}
	/**
	 *  Janky way of getting date, to be used later for search
	 * @return the date using the calender
	 */
	public void setDate(String formatted){
		this.correctDate = formatted;
	}
	public String getDate() {
		return this.correctDate;
	}
	
	/**
	 * 
	 * @param from date where photo is from
	 * @param to date where photo is to
	 * @return a boolean
	 */
	public boolean photoRange(LocalDate from, LocalDate to){
		LocalDate date = calendar.getTime().toInstant().atZone(
				ZoneId.systemDefault()).toLocalDate();
		boolean test = date.isBefore(to) && date.isAfter(from) ||
				date.equals(from) || date.equals(to);
		
		return test;
	}
	/**
	 * 
	 * @return the actual photo image
	 */
	public Image getImage() {
		return image.getImage();
	}
	
	/**
	 * gets the serialized image
	 * @return serialized image
	 */
	public SerializeImage getSerializeImage(){
		return image;
	}
	
	/**
	 * Constructor for photo
	 * @param newImage new photo
	 */
	public Photo(Image newImage){
		this();
		image.setImage(newImage);
	}
}
