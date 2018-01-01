package model;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * This model is used for all the error handling in the application as well as confirmation boxes
 *@author Tomer Levy
 *
 */
public class Alerts {
	
	/**
	 * Tells the user their inputted username does not exist in the system logs
	 */
	public static void UserDoesNotExist(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("The username you have entered does not exist in the system");
		alert.show();
	}
	
	/**
	 * Confirmation for adding a user to list
	 * @return boolean true or false
	 */
	public static boolean addUser() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation Dialog");
		alert.setContentText("Are you sure you want to add this user?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
		    return false;
		}
	}
	/**
	 * trying to make a new admin user
	 */
		public static void newAdmin() {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("ERROR");
			alert.setContentText("You cannot create a new admin.");
			alert.show();		
		}	
/**
 * Throw an error if the inputted username is empty
 */
	public static void emptyUser() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You must input an acutal Username!");
		alert.show();
	}


	/**
	 * Throw an error if the table is empty
	 */
	public static void emptyTableList() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("The Table is empty!");
		alert.show();
	}
	/**
	 * Will stop the user if they attempt to add a duplicate photo
	 */
	public static void duplicatePhoto(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("This photo already exists");
		alert.showAndWait();
		return;
	}
	/**
	 * Throw an error when trying to add a duplicate user
	 */
	public static void duplicateUser() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You've entered a duplicate user!");
		alert.show();
			
	}
	public static void duplicateTag(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("You've entered a tag that already exists");
		alert.showAndWait();
	}
	public static void alreadySearching(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("You've entered a tag that you are already searching by!");
		alert.showAndWait();
	}
	/**
	 * confirmation box for deleting a user
	 * @return whether user wants to delete user
	 */
	public static boolean deleteUser() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation Dialog");
		alert.setContentText("Are you sure you want to delete this user?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
		    return false;
		}
	}
	/**
	 * Will stop the user if they try to delete admin for some reason
	 */
	public static void deleteAdmin() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You cannot delete Admin.");
		alert.show();		
		
	}
	/**
	 * Will stop the user if the tags they enter are empty
	 */
	public static void emptyTag(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText("Both Tag fields need to have input");
		alert.showAndWait();
	}
	
	/**
	 * throw an error if the album name inserted is empty
	 */
	public static void emptyAlbumnName() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You did not put anything for the album name!");
		alert.show();	
	}
	
	/**
	 *  confirmation for creating an album
	 * @return yes/no on whether or not they want to make the album
	 */
	public static boolean createAlbum() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation Dialog");
		alert.setContentText("Are you sure you wish to add this album?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
		    return false;
		}
	}
	/**
	 * Throw an error if inputted album in move/copy does not exist
	 */
	public static void albumDoesNotExist() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("The album you have entered does not exist!");
		alert.show();	
	}
	
	/**
	 * Throw an error if a duplicate album name is entered
	 */
	public static void duplicateAlbum() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You have entered a duplicate album name!");
		alert.show();	
	}
	/**
	 * deletes selected Album
	 * @return yes/no on whether or not to delete album
	 */
	public static boolean deleteAlbum() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation Dialog");
		alert.setContentText("Are you sure you wish to delete this album?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
		    return false;
		}
	}
	/**
	 * Throw an error if you try to delete an album from an empty list
	 */
	public static void emptyAlbumList() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You cannot delete an album from an empty list!");
		alert.show();	
	}
	/**
	 * renames selected album
	 * @return whether user wants to rename
	 */
	public static boolean renameAlbum() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Confirmation Dialog");
		alert.setContentText("Are you sure you wish to rename this album?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
		    return false;
		}
	}
	/**
	 * checks to see if the field is empty
	 */
	public static void emptyRename() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You did not put anything into the rename field!");
		alert.show();	
	}

	public static void noResults() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You cannot make an album from zero search results!");
		alert.show();	
		
	}
	public static void noSearchTags(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You cannot make an album unless you are searching by at least one tag.");
		alert.show();	
	}

	public static void searchByOne() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("ERROR");
		alert.setContentText("You cannot search by both tag and date at once, please search one at a time.");
		alert.show();	
	}
	
	
}
	
	
	

