package controller;

import model.*;

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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Photo Page, where you are looking into the details of a photo, you can also move and copy this photo to a different album if you so choose.
 * Further, you can add tags and captions to your respective photo, as well as do a little slideshow!
 * @author Tomer Levy
 */
public class PhotoPageController {
	@FXML Button ToAlbumReturn, LogoutPhotos, PreviousButton, NextButton, CreateTagButton, MoveButton, CopyButton;
	@FXML ListView<Tags> TagListView;
	@FXML TextField DateTaken, Caption, TagNameID, TagNameID1, MovePhoto, CopyPhoto;
	@FXML ImageView PhotosImageView;
	@FXML TextArea CaptionTextField;
	
	private ObservableList<Tags> objList;
	private int photoIndex;
	private Album album;
	private User user;
	private List<Photo> photos;
	private UserList users;
	
	/**
	 * Setter for what album were currently on
	 * 
	 * @param album the album that we are currently on
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	/**
	 * Setter for what User is using the application at the current time
	 * @param user the user to be set	
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Setter for which photo is being selected
	 * @param num the index of the photo		
	 */
	public void setPhotoIndex(int num) {
		photoIndex = num;
	}
	
	/**
	 * setter for the list of users -- for writing serializibility
	 * 
	 * @param userList is the userList	
	 */
	public void setUserList(UserList userList) {
		users = userList;
	}
	
	/**
	 * Setter for the list of photos the user has
	 * 
	 * @param photos list of photos
	 */
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Start this whole thing up
	 * @param mainStage	the main Stage to be started
	 */
	public void start(Stage mainStage) {
		photos = album.getPhotos();
		updatePhotoDetails();
	}
	
	/**
	 * Fill in all the fields that photo page has using the photo details that are associated with each photo.
	 */
	public void updatePhotoDetails(){
		PhotosImageView.setImage(photos.get(photoIndex).getImage());
		Caption.setText(photos.get(photoIndex).getCaption());
		DateTaken.setText(photos.get(photoIndex).getDate());
		objList = FXCollections.observableArrayList(photos.get(photoIndex).getTags());
		TagListView.setCellFactory(new Callback<ListView<Tags>, ListCell<Tags>>(){
			@Override public ListCell<Tags> call(ListView<Tags> t){
				return new TagCell();
			}
		});	
		TagListView.setItems(objList);
		PreviousButton.setDisable(photoIndex == 0);
		NextButton.setDisable(photoIndex == photos.size()-1);
	}
	
	/**
	 * Back to AlbumPage
	 * @param event		
	 * @throws ClassNotFoundException		
	 */
	@FXML void ReturnToAlbum(ActionEvent event) throws ClassNotFoundException{
		Parent parent;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Album.fxml"));
	        parent = (Parent) loader.load();
	        AlbumController albumLoader = loader.<AlbumController>getController();
	        albumLoader.setUser(user);
	        albumLoader.setAlbum(album);
	        albumLoader.setUserList(users);
	        Scene scene = new Scene(parent);
	        Stage backtoAlbum = (Stage) ((Node) event.getSource()).getScene().getWindow();	
			albumLoader.start(backtoAlbum);
			backtoAlbum.setScene(scene);
			backtoAlbum.show();
		} catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	/**
	 * Display the photo previous to the one currently viewing.
	 * 
	 * @param event	
	 */
	@FXML void SlideShowPrevious(ActionEvent event) {
		photoIndex--;
		updatePhotoDetails();
	}
	
	/**
	 * Display the photo next to the one currently viewing.
	 * 
	 * @param event		
	 */
	@FXML void SlideShowNext(ActionEvent event) {
		photoIndex++;
		updatePhotoDetails();
	}
	
	/**
	 * Move photo to a different album, deleting it from current one.
	 * 
	 * @param event
	 * @throws ClassNotFoundException 
	 */
	@FXML void MovePhoto(ActionEvent event) throws ClassNotFoundException{
		//System.out.println(MovePhoto.getText());
		Photo photoToMove = photos.get(photoIndex);
		ArrayList<Album> allAlbums = new ArrayList<Album>();
		allAlbums = (ArrayList<Album>) user.getAlbums();
		for(int i=0; i<allAlbums.size();i++){
			if(allAlbums.get(i).getName().equals(MovePhoto.getText())){
				//System.out.println("Found Album: "+ allAlbums.get(i).getName());
				//System.out.println("Moving photo: "+ photoToMove.getCaption());
				allAlbums.get(i).addPhoto(photoToMove);
				album.removePhoto(photoIndex);
				try{
					UserList.write(users);
				} catch(Exception i1){
					i1.printStackTrace();
				}
				ReturnToAlbum(event);
				return;
			}
		}
		Alerts.albumDoesNotExist();
		MovePhoto.clear();
	}
	/**
	 * Copy photo to a different album, NOT deleting it from current one.
	 * 
	 * @param event
	 * @throws ClassNotFoundException 
	 */
	@FXML void CopyPhoto(ActionEvent event) throws ClassNotFoundException{
		//System.out.println("Copy photo to album . . .Needs to be implemented");
		//System.out.println(CopyPhoto.getText());
		Photo photoToCopy = photos.get(photoIndex);
		Photo Copy = new Photo();
		Copy = photoToCopy;
		ArrayList<Album> allAlbums = new ArrayList<Album>();
		allAlbums = (ArrayList<Album>) user.getAlbums();
		for(int i=0; i<allAlbums.size();i++){
			if(allAlbums.get(i).getName().equals(CopyPhoto.getText())){
				//System.out.println("Found Album: "+ allAlbums.get(i).getName());
				//System.out.println("Moving photo: "+ photoToMove.getCaption());
				allAlbums.get(i).addPhoto(Copy);
				try{
					UserList.write(users);
				} catch(Exception i1){
					i1.printStackTrace();
				}
				ReturnToAlbum(event);
				return;
			}
		}
		Alerts.albumDoesNotExist();
		CopyPhoto.clear();
	}
	
	/**
	 * Logout of application
	 * 
	 * @param event
	 * @throws ClassNotFoundException
	 */
	@FXML void LogoutButton(ActionEvent event) throws ClassNotFoundException{
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
	 * Add a tag to the photo
	 * 
	 * @param event		
	 */
	@FXML void CreateTag(ActionEvent event){
		for(Tags t : photos.get(photoIndex).getTags()){
			if(t.getType().equals(TagNameID.getText()) && t.getValue().equals(TagNameID1.getText())){
				Alerts.duplicateTag();
				return;
			}
		}
		if(TagNameID.getText().equals("") || TagNameID1.getText().equals("")){
			Alerts.emptyTag();
			return;
		}
		Photo currentPhoto = photos.get(photoIndex);
		currentPhoto.newTag(TagNameID.getText(), TagNameID1.getText());			
		updatePhotoDetails();  
		try{
			UserList.write(users);
		} catch(Exception i){
			i.printStackTrace();
		}
	}
	
	
	
	/**
	 * Display the tags with delete button and make them look nice
	 * 
	 * @author Tomer Levy
	 */
	private class TagCell extends ListCell<Tags>{
		AnchorPane apane = new AnchorPane();
		Label tagLabel = new Label();
		Button deleteButton = new Button("Delete");			
		public TagCell(){
			super();
			AnchorPane.setLeftAnchor(tagLabel, 0.0);
			AnchorPane.setTopAnchor(tagLabel, 0.0);
			AnchorPane.setRightAnchor(deleteButton, 0.0);
			AnchorPane.setTopAnchor(deleteButton, 0.0);
			tagLabel.setMaxWidth(200.0);
			deleteButton.setVisible(false);
			apane.getChildren().addAll(tagLabel, deleteButton);
			setGraphic(apane);
		}
		@Override public void updateItem(Tags tag, boolean empty){
			super.updateItem(tag, empty);
			setText(null);
			if(tag != null){
				tagLabel.setText(tag.toString());
				deleteButton.setVisible(true);
				deleteButton.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e) {
						deleteTag(e, tag);
					}
				});
			}	
		}
	}
	/**
	 * Rename caption button; Used to rename a caption
	 * @param e an action event
	 */
	public void RenameCaption(ActionEvent e){
		Photo currentPhoto = photos.get(photoIndex);
		currentPhoto.setCaption(CaptionTextField.getText());
		updatePhotoDetails();
		CaptionTextField.clear();
		try{
			UserList.write(users);
		} catch(Exception i){
			i.printStackTrace();
		}
	}
		
	/**
	 * Delete tag of your choice
	 * @param e an actionevent
	 * @param tag the tag to be deleted
	 */
	public void deleteTag(ActionEvent e, Tags tag) {
		Alert alert = new Alert(AlertType.INFORMATION);
 		alert.setTitle("Delete Tag");
 		alert.setContentText("Do you want to delete this tag?");
 		Optional<ButtonType> result = alert.showAndWait();
 		if(result.isPresent()){
 			Photo currentPhoto = photos.get(photoIndex);
 			for(int t = 0; t < currentPhoto.getTags().size(); t++){
 				if(currentPhoto.getTag(t).equals(tag)){
 					currentPhoto.deleteTag(t);
 					updatePhotoDetails();
 					try{
 						UserList.write(users);
 					} catch(Exception i){
 						i.printStackTrace();
 					}
 				 }
 			 }	 		 		
 		}
	}
}

