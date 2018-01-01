package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;


/**
 * First thing a user sees when opening this application; you can either log into an existing user or just login to admin to do more stuff...
 *  @author Tomer Levy
 */
public class LoginPageController {
	@FXML private Button LoginButton;
	@FXML private TextField Username_Input;
	/**
	 * This handles all the logging in, if the user exists, go to that user, if they want admin, go to admin page
	 * @param event	Login button being clicked
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	public void Login(ActionEvent event) throws ClassNotFoundException, IOException{
		String CurrentUsername = Username_Input.getText();
	    Parent parent;
	    UserList allUserNames = null;
		try {
			allUserNames = UserList.read();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if(CurrentUsername.equals("admin")){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Admin.fxml"));
			parent = (Parent) loader.load();
			AdminController admin = loader.getController();
			Scene scene = new Scene(parent);			
			Stage gotoAdminPage = (Stage)((Node)event.getSource()).getScene().getWindow();	
			admin.start(gotoAdminPage);
			gotoAdminPage.setScene(scene);
			gotoAdminPage.show();  
		}
		else if(CurrentUsername != null && allUserNames.containsUser(CurrentUsername)){
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/HomePage.fxml"));
			parent = (Parent) loader.load();
			HomePageController goToUserHomePage  = loader.<HomePageController>getController();
			goToUserHomePage.setUserList(allUserNames);
			goToUserHomePage.setUser(allUserNames.getUserThroughUsername(CurrentUsername));
			Scene scene = new Scene(parent);    
			Stage setup = (Stage) ((Node) event.getSource()).getScene().getWindow();	        
			goToUserHomePage.start(setup);      
			setup.setScene(scene);
			setup.show();  
		}
		else{
			Alerts.UserDoesNotExist();
			Username_Input.clear();
		}	
	}
}

