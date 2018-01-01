package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
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
import model.Album;
import model.Alerts;
import model.Photo;
import model.Tags;
import model.User;
import model.UserList;

/**
 * Search Page for all your searching needs, goes through all user photos, and searches by tag and date... Searching by a new date will refresh the tags.
 * However, searching by multiple tags with a date already applied is possible.
 * @author Tomer Levy
 */
public class SearchPageController {
	@FXML TextField SearchTagType, SearchTagValue;
	@FXML DatePicker StartCalender, EndCalender;
	@FXML ListView<Photo> PhotosListView;
	@FXML ListView<Tags> listOfTags;
	
	private ObservableList<Photo> photoObjList;
	private ObservableList<Photo> photoResults;
	private ObservableList<Photo> validDates;
	private ObservableList<Photo> newResults;
	private ObservableList<Tags> tagobjList;
	private User users;
	private UserList userList;
	private List<Photo> photos;
	private List<Tags> tags;
	/**
	 * Setter for user whose album this belongs to
	 * @param user its the user
	 */
	public void setUser(User user) {
		this.users = user;
		
	}

	/**
	 *  Setter for the userList, to be used for serializing
	 * @param usersList the userList
	 */
	public void setUserList(UserList usersList) {
		this.userList = usersList;
		
	}

	/**
	 * Stage setter for searchPage -- Startup
	 * @param stage startup stage
	 */
	public void start(Stage stage) {
		tags = new ArrayList<Tags>();
		tagobjList = FXCollections.observableArrayList(tags);
		photos = getUserPhotos();
		photoObjList = FXCollections.observableArrayList(photos);
		photoResults = FXCollections.observableArrayList();
		PhotosListView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			@Override
			public ListCell<Photo> call(ListView<Photo> p) {
				return new previewCell();
			}
		});	
		
		PhotosListView.setItems(photoObjList);
		
	}
	
	/**
	 * while starting, get all users photos by parsing through albums
	 * @return returns all photos the user has
	 */
	public List<Photo> getUserPhotos() {
		List<Photo> allUserPhotos = new ArrayList<Photo>();
		List<Album> allUserAlbums = users.getAlbums();
		for(int i = 0; i < allUserAlbums.size(); i++)
			for (Photo photo: allUserAlbums.get(i).getPhotos()){
				if (!allUserPhotos.contains(photo)){
					allUserPhotos.add(photo);
				}
			}
		return allUserPhotos;
	}
	/**
	 * Method to make the view of the images in search results look nice.
	 *
	 */
	private class previewCell extends ListCell<Photo> {
		AnchorPane anchorPane = new AnchorPane();
		StackPane stackPane = new StackPane();
		ImageView image = new ImageView();
		Label caption = new Label();		
		public previewCell() {
			super();
			image.setFitWidth(52.0);
			image.setFitHeight(52.0);
			image.setPreserveRatio(true);
			StackPane.setAlignment(image, Pos.CENTER);
			stackPane.getChildren().add(image);
			stackPane.setPrefHeight(50.0);
			stackPane.setPrefWidth(45.0);
			AnchorPane.setLeftAnchor(stackPane, 0.0);
			AnchorPane.setLeftAnchor(caption, 50.0);
			AnchorPane.setTopAnchor(caption, 0.0);
			anchorPane.getChildren().addAll(stackPane, caption);
			anchorPane.setPrefHeight(50.0);
			caption.setMaxWidth(2500.0);
			setGraphic(anchorPane);
		}
		@Override
		public void updateItem(Photo photo, boolean empty) {
			super.updateItem(photo, empty);
			setText(null);
			if(photo != null){
				image.setImage(photo.getImage());
				if(photo.getCaption().equals("")){
					caption.setText("         No caption given");
				}else{
					caption.setText("         " + photo.getCaption());
				}
			}else{
				image.setImage(null);
				caption.setText("");
			}
		}
	}
	/**
	 * Logout of where you currently are and go to the login page
	 * @param e an action event
	 * @throws ClassNotFoundException throws class not found exception
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
	 * Searches the Photos given tags and date range
	 * @param e action event
	 */
	public void SearchPhotos(ActionEvent e){
		//Set tag into table of tags
		if(SearchTagType.getText().length()!=0 && SearchTagValue.getText().length()!=0 && StartCalender.getValue()!=null && EndCalender.getValue()!=null){
			Alerts.searchByOne();
			return;
		}
		//Dear god this got messy
		if(SearchTagType.getText().equals("") && SearchTagValue.getText().equals("") && StartCalender.getValue()!=null && EndCalender.getValue()!=null){
			//Search by date
			String[] Start = StartCalender.getValue().toString().split("-");
			int yearS = Integer.parseInt(Start[0]);
			int monthS = Integer.parseInt(Start[1]);
			int dayS = Integer.parseInt(Start[2]);
			String[] End = EndCalender.getValue().toString().split("-");
			int yearE = Integer.parseInt(End[0]);
			int monthE = Integer.parseInt(End[1]);
			int dayE = Integer.parseInt(End[2]);
			tagobjList = FXCollections.observableArrayList();
			listOfTags.setItems(tagobjList);
			String tag = "Start Year: " + yearS + " Month: "+ Start[1] + " day: "+ Start[2];
			String value = "End Year: " + yearE + " Month: "+ End[1] + " day: "+ End[2];
			Tags newTag = new Tags(tag,value);
			tagobjList.add(newTag);
			listOfTags.setItems(tagobjList);
			photoResults = FXCollections.observableArrayList();
			for(int totalPhotos =0; totalPhotos < photos.size(); totalPhotos++){
				String photoDate = photos.get(totalPhotos).getDate();

				String[] fixedArray = new String[3];
				fixedArray[0] =photoDate.substring(6,10); //year
				fixedArray[1] =photoDate.substring(0,2); // month
				fixedArray[2] =photoDate.substring(3,5); // day
				int yearP = Integer.parseInt(fixedArray[0]);
				int monthP = Integer.parseInt(fixedArray[1]);
				int dayP = Integer.parseInt(fixedArray[2]);
				if(yearP == yearS){
					if(monthP == monthS){
						if(dayP>= dayS && dayP<= dayE){
							photoResults.add(photos.get(totalPhotos));
							continue;
						}
					}else{
						if(monthP> monthS){
							if(monthE >monthP){
								photoResults.add(photos.get(totalPhotos));
								continue;
							}else if(monthP<=monthE){
								if(dayP<= dayE){
									photoResults.add(photos.get(totalPhotos));
									continue;
								}
							}else{
								//out of bounds;
								continue;
							}
						}
					}
				}else if(yearP>yearS){
					if(yearP<yearE){
						photoResults.add(photos.get(totalPhotos));
						continue;
					}else if(yearP==yearE){
						if(monthP<monthE){
							photoResults.add(photos.get(totalPhotos));
							continue;
						}else if(monthP==monthE){
							if(dayP<= dayE){
								photoResults.add(photos.get(totalPhotos));
								continue;
							}
						}
					}
				}
			}
			PhotosListView.setItems(photoResults);
			SearchTagType.clear();
			SearchTagValue.clear();
			StartCalender.setValue(null);
			EndCalender.setValue(null);
			return;
			
		}else{
			//Search by tag
			String tag = SearchTagType.getText();
			String value = SearchTagValue.getText();
			if(tag.equals("") || value.equals("")){
				Alerts.emptyTag();
				return;
			}
			Tags newTag = new Tags(tag, value);
			for(int check=0; check<tagobjList.size();check++){
				if(tagobjList.get(check).equals(newTag)){
					Alerts.alreadySearching();
					return;
				}
			}
			tagobjList.add(newTag);
			listOfTags.setItems(tagobjList);
			photoResults = FXCollections.observableArrayList();
			validDates = FXCollections.observableArrayList();
			newResults = FXCollections.observableArrayList();
			ArrayList<Tags> currentPhotoTags = new ArrayList<Tags>();
			int counter=0;
			int testCounter=tagobjList.size();
			for(int totalPhotos =0; totalPhotos < photos.size(); totalPhotos++){
				counter=0;
				currentPhotoTags = (ArrayList<Tags>) photos.get(totalPhotos).getTags();
				for(int totalTags =0; totalTags < tagobjList.size(); totalTags++){
					String checkDateType = tagobjList.get(totalTags).getType();
					String checkDateValue = tagobjList.get(totalTags).getValue();
					int checkLength1=checkDateType.length();
					int checkLength2=checkDateValue.length();
					if(checkLength1>=5 && checkLength2 >=3){
						if(checkDateType.substring(0, 5).equals("Start") && checkDateValue.substring(0, 3).equals("End")){
							//checking date
							String[] startDate = new String[3];
							String[] endDate = new String[3];
							startDate[0] = checkDateType.substring(12,16); // Year
							startDate[1] = checkDateType.substring(24,26); // Month
							startDate[2] = checkDateType.substring(32,34); // Day
							int yearS = Integer.parseInt(startDate[0]);
							int monthS = Integer.parseInt(startDate[1]);
							int dayS = Integer.parseInt(startDate[2]);
									
							endDate[0] = checkDateValue.substring(10,14); // Year
							endDate[1] = checkDateValue.substring(22,24); // Month
							endDate[2] = checkDateValue.substring(30,32); // Day
							int yearE = Integer.parseInt(endDate[0]);
							int monthE = Integer.parseInt(endDate[1]);
							int dayE = Integer.parseInt(endDate[2]);
									
							String photoDate = photos.get(totalPhotos).getDate();

							String[] fixedArray = new String[3];
							fixedArray[0] =photoDate.substring(6,10); //year
							fixedArray[1] =photoDate.substring(0,2); // month
							fixedArray[2] =photoDate.substring(3,5); // day
							int yearP = Integer.parseInt(fixedArray[0]);
							int monthP = Integer.parseInt(fixedArray[1]);
							int dayP = Integer.parseInt(fixedArray[2]);
							if(yearP == yearS){
								if(monthP == monthS){
									if(dayP>= dayS && dayP<= dayE){
										validDates.add(photos.get(totalPhotos));
										counter++;
										continue;
									}
								}else{
									if(monthP> monthS){
										if(monthE >monthP){
											validDates.add(photos.get(totalPhotos));	
											counter++;
											continue;
										}else if(monthP<=monthE){
											if(dayP<= dayE){
												validDates.add(photos.get(totalPhotos));	
												counter++;
												continue;
											}
										}else{
											//out of bounds;
											continue;
										}
									}
								}
							}else if(yearP>yearS){
								if(yearP<yearE){
									validDates.add(photos.get(totalPhotos));	
									counter++;
									continue;
								}else if(yearP==yearE){
									if(monthP<monthE){
										validDates.add(photos.get(totalPhotos));	
										counter++;
										continue;
									}else if(monthP==monthE){
										if(dayP<= dayE){
											validDates.add(photos.get(totalPhotos));	
											counter++;
											continue;
										}
									}
								}
							}						
						}
					}
					//checking by tag
					for(int totalPhotoTags =0; totalPhotoTags < currentPhotoTags.size(); totalPhotoTags++){
						if(currentPhotoTags.get(totalPhotoTags).equals(tagobjList.get(totalTags))){
							counter++;
						}
					}
				}
				
				if(counter >= testCounter){
					photoResults.add(photos.get(totalPhotos));
				}
			}
		}
		if(validDates == null){
			
			PhotosListView.setItems(photoResults);
			SearchTagType.clear();
			SearchTagValue.clear();
			StartCalender.setValue(null);
			EndCalender.setValue(null);
		}else{
			if(validDates.size()!=0){
				for(int i=0; i<validDates.size();i++){
					for(int j=0; j<photoResults.size();j++){
						if(validDates.get(i).equals(photoResults.get(j))){
							newResults.add(photoResults.get(j));
						}
					}
				}
				photoResults = newResults;
				PhotosListView.setItems(photoResults);
				SearchTagType.clear();
				SearchTagValue.clear();
				StartCalender.setValue(null);
				EndCalender.setValue(null);
			}else{
				PhotosListView.setItems(photoResults);
				SearchTagType.clear();
				SearchTagValue.clear();
				StartCalender.setValue(null);
				EndCalender.setValue(null);
			}
		}
	}
	
	/**
	 * Creates album depending on what photos show up in search results
	 * @param e  an actionevent
	 */
	@FXML private void CreateAlbumFromSearch(ActionEvent e){
		//Implement create album from search results
		//shouldnt be hard, after done with search
		if(tagobjList.size()==0){
			Alerts.noSearchTags();
			return;
		}
		if(photoResults.size()==0){
			Alerts.noResults();
			return;
		}
		ArrayList<Album> allAlbums = new ArrayList<Album>();
		allAlbums = (ArrayList<Album>) users.getAlbums();
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Album Input");
		dialog.setHeaderText("What would you like to call your new album?");
		dialog.setContentText("I would like to call my album:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			String albumName = result.get();
			for(int i=0; i< allAlbums.size(); i++){
				if(allAlbums.get(i).getName().equals(albumName)){
					Alerts.duplicateAlbum();
					return;
				}
			}
			Album newAlbum = new Album(albumName);
			users.createAlbum(newAlbum);
			for(int j=0; j<photoResults.size();j++){
				newAlbum.addPhoto(photoResults.get(j));
			}
			try {
				UserList.write(userList);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else{
			return;
		}
	}
	/**
	 * Clears the tag box of all search tags, basically a refresh of search page
	 * @param e
	 */
	public void clearSearchTags(ActionEvent e){
		PhotosListView.setItems(photoObjList);
		tagobjList = FXCollections.observableArrayList();
		listOfTags.setItems(tagobjList);
		photoResults = FXCollections.observableArrayList();
	}
	/**
	 * returns to albums 
	 * @param event an action event
	 * @throws ClassNotFoundException throws a class not found exception
	 */
	public void ReturnToAlbums(ActionEvent event)throws ClassNotFoundException{
		Parent parent;
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/HomePage.fxml"));
	        parent = (Parent) loader.load();
	        HomePageController albumScene = loader.<HomePageController>getController();
	        albumScene.setUser(users);
	        albumScene.setUserList(userList);
	        Scene scene = new Scene(parent);
	        Stage stageapp = (Stage) ((Node) event.getSource()).getScene().getWindow();	
			albumScene.start(stageapp);
			stageapp.setScene(scene);
			stageapp.show();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
}