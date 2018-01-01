package model;

import java.io.Serializable;
/**
 * @author Tomer Levy
 *
 */

public class Tags implements Serializable{
	private String tagType;
	private String tagValue;
	
	/**
	 * 
	 * @param tagType the type of the tag
	 * @param tagValue the value of the tag
	 */
	public Tags(String tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}

	/**
	 * Sets the tagType
	 * @param tagType returns the tagtype
	 */
	public void setType(String tagType) {
		this.tagType = tagType;
	}
	
	/**
	 * Sets the tagValue
	 * @param tagValue returns the tagvalue
	 */
	public void setValue(String tagValue) {
		this.tagValue = tagValue;
		
	}
	
	/**
	 * 
	 * @return the tagtype
	 */
	public String getType(){
		return tagType;
	}
	
	/**
	 * 
	 * @return the tagvalue
	 */
	public String getValue(){
		return tagValue;
	}
	
	/**
	 * returns tag in a nice format
	 */
	public String toString(){
		return getType() + " : " + getValue();
	}
	
	/**
	 * Overrides the equals method
	 */
	public boolean equals(Object o){
		if(o == null || !(o instanceof Tags)){
			return false;
		}
	Tags temp =(Tags)o;

	return temp.getType().equals(tagType) &&  temp.getValue().equals(tagValue);
	}
}
