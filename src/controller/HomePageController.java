package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/**
 * This is the HomePage ... This can take you into a seperate album, create/delete an album, or go to the search page!
 * 
 * @author Tomer Levy
 */
public class HomePageController {
	@FXML Button CreateAlbumButton, LogoutButton, SearchPhotos;
	@FXML TextField NewAlbum, RenameCurrentAlbum, DeleteAlbumField;
	@FXML ListView<Album> AlbumListView;

	private ObservableList<Album> objList;
	private List<Album> albums = new ArrayList<Album>();
	private User user;
	private UserList users;
	/**
	 * Start the homepage up!
	 * 
	 * @param mainStage		
	 */
	public void start(Stage mainStage) {
		albums = user.getAlbums();
		objList = FXCollections.observableArrayList(albums);
		AlbumListView.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>(){
			@Override public ListCell<Album> call(ListView<Album> p) {
				return new betterAlbum();
			}
		});	
		AlbumListView.setItems(objList);
	}
	/**
	 * Setter for what user is currently using the application
	 * 
	 * @param user the user		
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * Setter for all users in userList
	 * 
	 * @param userList the userList
	 */
	public void setUserList(UserList userList) {
		users = userList;
	}

	 /**
	  * Delete the selected album from the users memory!!!
	  * @param event the action event
	  * @throws IOException throws an IOexception
	  */
	 public void DeleteSelectedAlbum(ActionEvent event) throws IOException{
		 if (objList.size() != 0) {
			 int num = AlbumListView.getSelectionModel().getSelectedIndex();
			 if(num==-1){
				//nothing is selected
				 //dumb but important error
				 return;
			 }
			 Album selectedAlbum = AlbumListView.getSelectionModel().getSelectedItem();
			 DeleteAlbumField.setText(selectedAlbum.getName());
			 boolean deletingAlbum = Alerts.deleteAlbum();
			 if(deletingAlbum == true){
				 objList.remove(selectedAlbum);
				 albums.remove(selectedAlbum);
				 UserList.write(users);
					if (objList.size() == 1) {
						AlbumListView.getSelectionModel().select(0);
					}
					else{
						num = 0;
						for(Album album: albums){
							if(album == selectedAlbum){
								AlbumListView.getSelectionModel().select(num);
								break;
							}  
							num++;
						}
					}
			 }
		 }
		 else{
			 Alerts.emptyAlbumList();
		 }
		 DeleteAlbumField.clear();
	 }
	/**
	 * Create a new album and add it to the User's memory!!!
	 * 
	 * @param event
	 */
	@FXML private void CreateAlbum(ActionEvent event) throws IOException {
		ArrayList<Album> allAlbums = new ArrayList<Album>();
		allAlbums = (ArrayList<Album>) user.getAlbums();
		int index = AlbumListView.getSelectionModel().getSelectedIndex();
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Album Input");
		dialog.setHeaderText("What would you like to call your new album?");
		dialog.setContentText("I would like to call my album:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String albumName = result.get();
			for(int i=0; i< allAlbums.size(); i++){
				if(allAlbums.get(i).getName().equals(albumName)){
					Alerts.duplicateAlbum();
					return;
				}
			}
			Album newAlbum = new Album(albumName);
			user.createAlbum(newAlbum);
			objList.add(newAlbum);
			UserList.write(users);
			if (objList.size() == 1) {
				AlbumListView.getSelectionModel().select(0);
			} else{
				index = 0;
				for(Album a: albums){
					if(a == newAlbum){
						AlbumListView.getSelectionModel().select(index);
						break;
					}
					index++;
				}
			}	   
		}
		
	}
	
	/**
	 * Head on over to the search photo page
	 * 
	 * @param event
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@FXML void SearchAllPhotos(ActionEvent event) throws IOException, ClassNotFoundException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SearchPage.fxml"));
        Parent parent = (Parent) loader.load();
        SearchPageController goToSearchPage = loader.<SearchPageController>getController();
        goToSearchPage.setUser(user);
        goToSearchPage.setUserList(users);
        Scene scene = new Scene(parent);
        Stage stageApp = (Stage) ((Node) event.getSource()).getScene().getWindow();	
        goToSearchPage.start(stageApp);
		stageApp.setScene(scene);
		stageApp.show();
	}
	
	/**
	 * The good ol' logout button
	 * 
	 * @param event
	 * @throws ClassNotFoundException
	 */
	@FXML void Logout(ActionEvent event) throws ClassNotFoundException{
		Parent parent; 
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginPage.fxml"));
			parent = (Parent) loader.load();
			Scene scene = new Scene(parent);		
			Stage logout = (Stage) ((Node) event.getSource()).getScene().getWindow();			             
			logout.setScene(scene);
			logout.show();  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Replace the listcell to make albums look nicer, showing first picture in them
	 * 
	 * @author Tomer Levy
	 */
	private class betterAlbum extends ListCell<Album> {
		AnchorPane anchorPane = new AnchorPane();
		StackPane stackPane = new StackPane();
		ImageView imageView = new ImageView();
		Label albumNameLabel = new Label();
		Button viewButton = new Button("View");
		Button renameButton = new Button("Rename");	
		public betterAlbum(){
			super();			
			imageView.setFitWidth(100.0);
			imageView.setFitHeight(100.0);
			imageView.setPreserveRatio(true);
			StackPane.setAlignment(imageView, Pos.CENTER);
			stackPane.getChildren().add(imageView);
			stackPane.setPrefHeight(110.0);
			stackPane.setPrefWidth(100.0);			
			AnchorPane.setLeftAnchor(stackPane, 0.0);
			AnchorPane.setLeftAnchor(albumNameLabel, 100.0);
			AnchorPane.setTopAnchor(albumNameLabel, 0.0);			
			viewButton.setVisible(false);
			AnchorPane.setRightAnchor(viewButton, 0.0);
			AnchorPane.setTopAnchor(viewButton, 0.0);			
			renameButton.setVisible(false);
			AnchorPane.setRightAnchor(renameButton, 0.0);
			AnchorPane.setBottomAnchor(renameButton, 0.0);			
			anchorPane.getChildren().addAll(stackPane, albumNameLabel, viewButton, renameButton);
			anchorPane.setPrefHeight(110.0);
			albumNameLabel.setMaxWidth(250.0);
			setGraphic(anchorPane);
		}
			
		@Override public void updateItem(Album album, boolean empty){
			super.updateItem(album, empty);
			setText(null);
			if(album == null){
				imageView.setImage(null);
				albumNameLabel.setText("");
				viewButton.setVisible(false);
				renameButton.setVisible(false);
			}
			else{
				imageView.setImage(album.getAlbumPhoto());
				albumNameLabel.setText("         ALBUM: " + album.getName());
				viewButton.setVisible(true);
				renameButton.setVisible(true);
				viewButton.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e){
						try{
							goToAlbumContent(e, album);
						} catch (ClassNotFoundException | IOException e1){
							e1.printStackTrace();
						} 
					}
				});				
				renameButton.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e){
						renameAlbum(e, album);
					}
				});
			}
		}
		
		/**
		 * Go inside of the album to see what lurks inside
		 * 
		 * @param e		
		 * @param album		
		 * @throws IOException		
		 * @throws ClassNotFoundException		
		 */
		public void goToAlbumContent(ActionEvent e, Album album) throws IOException, ClassNotFoundException{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Album.fxml"));
	        Parent parent = (Parent) loader.load();
	        AlbumController goToSelectedAlbum = loader.<AlbumController>getController();
	        goToSelectedAlbum.setAlbum(album);
	        goToSelectedAlbum.setUser(user);
	        goToSelectedAlbum.setUserList(users);
	        Scene scene = new Scene(parent);
	        Stage stageApp = (Stage) ((Node) e.getSource()).getScene().getWindow();
			goToSelectedAlbum.start(stageApp);
			stageApp.setScene(scene);
			stageApp.show();
		}
		
		/**
		 * rename the album by pressing the button
		 * 
		 * @param e			
		 * @param album	
		 */
		public void renameAlbum(ActionEvent e, Album album) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Album Input");
			dialog.setHeaderText("What would you like to rename your album to?");
			dialog.setContentText("I would like to call my album:");
			Optional<String> result = dialog.showAndWait();  
		    if(result.get().trim().isEmpty()){
	 			Alerts.emptyAlbumnName();
				return;
			}
			else if(user.AlbumExists(result.get())){
				Alerts.duplicateAlbum();
				return;
			}
		    if(result.isPresent()){
				album.setAlbumName(result.get().trim());
				updateItem(album, true);
				try{
					UserList.write(users);
				}
				catch(Exception i){
					i.printStackTrace();
				}
		    }
		}
	}
}
