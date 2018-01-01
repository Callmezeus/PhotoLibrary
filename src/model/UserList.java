package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * This class stores the UserList which basically holds all the Users that will be using this program.
 * 
 *  @author Tomer Levy
 *
 */
public class UserList implements Serializable {
	
	private List<User> Users;
	private static final long serialVersionUID = 1L;
	public static final String storeDir = "data";
	public static final String storeFile = "Users.SerData";
	
	/**
	 * UserList Constructor
	 * Creates a new UserList
	 */
	public UserList() {
		Users = new ArrayList<User>();
	}
	
	/**
	 * Gets the Users in the program
	 * 
	 * @return returns the List of users in the program
	 */
	public List<User> getUserList(){
		  return Users;
	}
	/**
	 * Gets the User based on the username
	 * @param username the username we are using to find user
	 * @return User with the same name
	 */
	public User getUserThroughUsername(String username){
		for(User user : Users){
			if(user.getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}
		
	/**
	 * adds User to the List
	 * @param user the new user
	 */
	public void addUser(User user){
		Users.add(user);
	}
	/**
	 * Removes a user from the list
	 * 
	 * @param user the user to be removed
	 */
	public void removeUser(User user){
		Users.remove(user);
	}
	/**
	 * Checks to see if the User is contained in the UserList
	 * 
	 * @param username the username which we need to compare with the UserList
	 * @return boolean depends on whether in the list
	 */
	public boolean containsUser(String username){
		boolean temp = false;
		for(User user : Users){
			if(user.getUsername().equals(username)){
				temp = true;
			}
		}
		return temp;
	}
	/**
	 * Overrides the toString method
	 * 
	 * @return String all the Users in the list based on Username
	 */
	public String toString(){
		String temp = "";
		if(Users != null){
			for(User user : Users){
				temp = temp + user.getUsername() + " ";
			}
		}
		else{
			temp = "empty";
		}
		return temp;
	}
	/**
	 * 
	 * @param UserList Serializes the userList
	 * @throws IOException Exception thrown if error
	 */
	public static void write (UserList UserList) throws IOException {
		ObjectOutputStream OutputStream = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		OutputStream.writeObject(UserList);
		OutputStream.close();
	}
	/**
	 * 
	 * @return UserList returns the userList
	 * @throws IOException throws an IO exception
	 * @throws ClassNotFoundException throws an ClassNotFoundException exception
	 */
	public static UserList read() throws IOException, ClassNotFoundException {
		   ObjectInputStream InputStream = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
		   UserList Users = (UserList)InputStream.readObject();
		   InputStream.close();
		   return Users;
	   }
	
}
