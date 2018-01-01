package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/**
 * Main Page of the album you are in, where you can see all the photos that are in it, as well as delete them and make new ones!!!!!
 * 
 * @author Tomer Levy
 */
public class AlbumController {
	@FXML Button AlbumsReturn, CreateNewPhoto, DeletePhoto, LogoutCurrentAlbum;
	@FXML TextField AlbumName, NumberOfPhotos, OldestPhoto, LatestPhoto;
	@FXML ListView<Photo> PhotoListView;
	
	private Album album;
	private User user;
	private UserList users;
	private ObservableList<Photo> objList;
	private List<Photo> photoList;
	
	/**
	 * setter for album
	 * 
	 * @param album the album to be set	
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	/**
	 * setter for user
	 * 
	 * @param user sets the user who is viewing album		
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * setter for UserList
	 * 
	 * @param UserList the userlist
	 */
	public void setUserList(UserList UserList) {
		users = UserList;
	}
	
	/**
	 * Starts this album page up!
	 * 
	 * @param mainStage	the main stage for album
	 */
	public void start(Stage mainStage) {
		AlbumName.setText(album.getName());
		updateAlbumDetails();
		photoList = album.getPhotos();
		objList = FXCollections.observableArrayList(photoList);
		PhotoListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			@Override public ListCell<Photo> call(ListView<Photo> p) {
				return new betterPhoto();
			}
		});	
		PhotoListView.setItems(objList);
	}
	
	/**
	 * updates the cute picture and all details associated with the album
	 */
	public void updateAlbumDetails() {
		//AlbumDate.setText(album.doDateRange());
		OldestPhoto.setText(album.getOldest());
		NumberOfPhotos.setText("There are currently: " + album.getSize() + " photos in this album");
		LatestPhoto.setText(album.getNewest());
	}
	
	/**
	 * Go back to home page
	 * 
	 * @param event		
	 * @throws ClassNotFoundException 	
	 */
	@FXML protected void ReturnToAlbums(ActionEvent event) throws ClassNotFoundException{
		Parent parent;
   	
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/HomePage.fxml"));
	        parent = (Parent) loader.load();
	        HomePageController homeLoader = loader.<HomePageController>getController();
	        homeLoader.setUser(user);
	        homeLoader.setUserList(users);
	        Scene scene = new Scene(parent);
	        
	        Stage stageapp = (Stage) ((Node) event.getSource()).getScene().getWindow();	
	        homeLoader.start(stageapp);
			stageapp.setScene(scene);
			stageapp.show();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
	
	/**
	 * Finds which photo you selected and deletes it
	 * 
	 * @param event
	 */
	@FXML void DeletePhoto(ActionEvent event){
		int index = PhotoListView.getSelectionModel().getSelectedIndex();
		if(index==-1){
			//nothing is selected
				return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
 		alert.setTitle("Deleting Photo");
 		alert.setContentText("Are you sure you want to delete this Photo?");
 		Optional<ButtonType> userResponse = alert.showAndWait();
 		if(userResponse.isPresent()){
 			album.removePhoto(index);
 			objList.remove(index);
 			try{
 			   UserList.write(users);
 			   updateAlbumDetails();
 			} catch(Exception e){
 				e.printStackTrace();
 			}
 		}
	}
	
	/**
	 * create a photo when the button is clicked
	 * 
	 * @param event
	 */
	@FXML void CreatePhoto(ActionEvent event) throws IOException{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
		fileChooser.setTitle("Upload Photo");
		
		Stage stageApp = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File file = fileChooser.showOpenDialog(stageApp);
		if (file == null){
			return;
		}
		
        FileInputStream InputStream = new FileInputStream(file.getAbsolutePath());
		Image image = new Image(InputStream);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
        SerializeImage tempImage = new SerializeImage();
        tempImage.setImage(image);
        for (Photo p: album.getPhotos()){
        	if(tempImage.equals(p.getSerializeImage())){
        		Alerts.duplicatePhoto();
        	}
        }
        Photo photoToAdd = null;
        boolean photoFound = false;
        for(Album albumToParse: user.getAlbums()){
        	for(Photo p: albumToParse.getPhotos()){
        		if(tempImage.equals(p.getSerializeImage())){
        			photoToAdd = p;
        			photoFound = true;
        			break;
        		}
        		if(photoFound){
        			break;
        		}
        	}
        }
        if (!photoFound){
        	photoToAdd = new Photo(image);
        }
        photoToAdd.setDate(sdf.format(file.lastModified()));
        album.addPhoto(photoToAdd);
        objList.add(photoToAdd);
		UserList.write(users);
		updateAlbumDetails();
	}
	
	/**
	 * Logout!!!!!!!!!!
	 * 
	 * @param event
	 */
	@FXML void Logout(ActionEvent event){
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
	 * betterPhoto is used to override listcell where it just makes the photo visible to the user in the actual cell list
	 * 
	 * @author Tomer Levy
	 */
	private class betterPhoto extends ListCell<Photo> {	
		AnchorPane anchorPane = new AnchorPane();
		StackPane stackPane = new StackPane();
		ImageView imageView = new ImageView();
		Label captionOfPhoto = new Label();
		Button viewSelectedPhoto = new Button("View");
		public betterPhoto() {
			super();
			imageView.setFitWidth(52.0);
			imageView.setFitHeight(52.0);
			imageView.setPreserveRatio(true);
			StackPane.setAlignment(imageView, Pos.CENTER);
			stackPane.getChildren().add(imageView);
			stackPane.setPrefHeight(55.0);
			stackPane.setPrefWidth(45.0);
			AnchorPane.setLeftAnchor(stackPane, 0.0);
			AnchorPane.setLeftAnchor(captionOfPhoto, 55.0);
			AnchorPane.setTopAnchor(captionOfPhoto, 0.0);
			viewSelectedPhoto.setVisible(false);
			AnchorPane.setRightAnchor(viewSelectedPhoto, 0.0);
			AnchorPane.setBottomAnchor(viewSelectedPhoto, 0.0);
			anchorPane.getChildren().addAll(stackPane, captionOfPhoto, viewSelectedPhoto);
			anchorPane.setPrefHeight(55.0);
			captionOfPhoto.setMaxWidth(300.0);
			setGraphic(anchorPane);
		}
			
		@Override public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			setText(null);
			if(photo == null){
				imageView.setImage(null);
				captionOfPhoto.setText("");
				viewSelectedPhoto.setVisible(false);
			}
			if(photo != null){
				imageView.setImage(photo.getImage());
				if(photo.getCaption().equals("")){
					captionOfPhoto.setText("         No caption given");
				}else{
					captionOfPhoto.setText("         " + photo.getCaption());
				}
				viewSelectedPhoto.setVisible(true);
				viewSelectedPhoto.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						try {
							viewSelectedPhoto(e, photo);
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						} 
					}
				});
			}	
		}
		
		
		/**
		 * view the photo go to PhotoPage!
		 * 
		 * @param e		
		 * @param photo		
		 * @throws IOException	
		 * @throws ClassNotFoundException	
		 */
		public void viewSelectedPhoto(ActionEvent e, Photo photo) throws IOException, ClassNotFoundException {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PhotoPage.fxml"));
	        Parent parent = (Parent) loader.load();
	        PhotoPageController photoPageLoader = loader.<PhotoPageController>getController();
	        photoPageLoader.setPhotoIndex(album.findPhotoIndex(photo));
	        photoPageLoader.setAlbum(album);
	        photoPageLoader.setUser(user);
	        photoPageLoader.setUserList(users);
	        Scene scene = new Scene(parent);  
	        Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();	
			photoPageLoader.start(app_stage);
			app_stage.setScene(scene);
			app_stage.show();
		}
	}
}
