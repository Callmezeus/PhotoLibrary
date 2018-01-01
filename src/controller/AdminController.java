package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TablePosition;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/**
 * Creates the AdminController Which is based on Admin.fxml
 * 
 * @author Tomer Levy
 *
 */
public class AdminController {
@FXML TableView<User> table;
@FXML TableColumn<User,String> usernameColumn;
@FXML TextField NewUsername, DeleteUserName;
@FXML Button CreateNewUsername, DeleteUserButton, Logout;

private ObservableList<User> objList;
private List<User> user = new ArrayList<User>();
private UserList userList;

/**
 * Admin page, where you can do all your admin duties such as add a user and delete a user.
 * 
 * @param mainStage the main stage
 * @throws ClassNotFoundException throws class exception
 * @throws IOException throws IO exception
 */
	public void start(Stage mainStage) throws ClassNotFoundException, IOException {
		userList = UserList.read();
		user = userList.getUserList();
		objList = FXCollections.observableArrayList(user);
		usernameColumn.setCellValueFactory(new Callback<CellDataFeatures<User, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<User, String> s) {
		         return new SimpleStringProperty(s.getValue().getUsername());
		     }
		  });
		table.setItems(objList);		
	      if (!objList.isEmpty()){
	    	  table.getSelectionModel().select(0);
	      }
	}
	/**
	 *  Creates a new user with the inputted name
	 *  
	 * @param event an action event
	 */
	 public void CreateNewUser(ActionEvent event) {
		 	int index = table.getSelectionModel().getSelectedIndex();
		    String inputtedUser = NewUsername.getText();
		    boolean test = Alerts.addUser();
		    if(test == true){
		    	if(inputtedUser.trim().isEmpty()){
		    		Alerts.emptyUser();
		    	}
		    	else if(inputtedUser.equals("admin")){
		    		Alerts.newAdmin();
		    	}
		    	else if(userList.containsUser(inputtedUser)){
		    		Alerts.duplicateUser();
		    	}
		    	else {
		    		User NewUser = new User(inputtedUser);
		    		objList.add(NewUser);
		    		userList.addUser(NewUser);
		    		try {
		    			UserList.write(userList);
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
		    		if (objList.size() == 1) {
						   table.getSelectionModel().select(0);
					   }
					   else
					   {
						   index = 0;
						   for(User user: userList.getUserList())
						   {
							   
							   if(user == NewUser)
							   {
								  table.getSelectionModel().select(index);
								  break;
							   }
							   
							   index++;
						   }
					   }
			    }
		    }
		    NewUsername.clear();
	 }
	 /**
	  * Logout of where you currently are and go to the login page
	  * 
	  * @param e an action event
	  * @throws ClassNotFoundException throws a class not found exception
	  */
	 public void Logout(ActionEvent e)throws ClassNotFoundException{
	    	Parent parent; 
			try{	
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginPage.fxml"));
				parent = (Parent)loader.load();
				Scene scene = new Scene(parent);
				Stage logout = (Stage) ((Node) e.getSource()).getScene().getWindow();			             
				logout.setScene(scene);
				logout.show();  
			} catch (IOException e1) {
				e1.printStackTrace();
			}         
	 }
	 
	/**
	 * Deletes a user
	 * 
	 * @param e an action event
	 */
	 public void DeleteSelectedUser(ActionEvent e){
		 if (objList.size() != 0) {
			 @SuppressWarnings("rawtypes")
			TablePosition tableOfUsers = table.getSelectionModel().getSelectedCells().get(0);
			 int row = tableOfUsers.getRow();
			 User item = table.getItems().get(row);
			 DeleteUserName.setText(item.getUsername());
			boolean test = Alerts.deleteUser();
			 if(test == true){
				 if(item.getUsername().equals("admin")){
					 Alerts.deleteAdmin();
				 }
				 else{
					 objList.remove(item);
					 user.remove(item);
					 try {
							UserList.write(userList);
						} catch (Exception e1) {
							e1.printStackTrace();
						}	 
				 }
			 }		 
		 }
		 else{
			 Alerts.emptyTableList();
		 }
		 DeleteUserName.clear();
	 }
}