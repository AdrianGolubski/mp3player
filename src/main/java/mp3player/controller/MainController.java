/*
    contains the main logic of the application, or rather acts as an intermediary between the application logic and the view.
 */
package mp3player.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
//import javafx.scene.control.Slider;
//import javafx.scene.control.TableView;
//import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import mp3player.mp3.Mp3Parser;
import mp3player.mp3.Mp3Song;
import mp3player.player.Mp3Player;

import java.io.File;
import java.io.IOException;

public class MainController {
    @FXML
    private ContentPaneController contentPaneController;
    @FXML
    private ControlPaneController controlPaneController;
    @FXML
    private MenuPaneController menuPaneController;

    private Mp3Player player;

    public void initialize() {
        createPlayer();
        configureTableClick();
        configureButtons();
        configureMenu();
    }

    //First, we retrieve a reference to the object of the list associated with the table in the contentPane.
    // Then, we create an Mp3Player object, passing the mentioned list to the constructor.
    private void createPlayer() {
        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
        player = new Mp3Player(items);
    }

    //Configure an event that involves double-clicking on any item in the table
    private void configureTableClick() {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        contentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                //returns the index of the row that someone clicked on as a result
                int selectedIndex = contentTable.getSelectionModel().getSelectedIndex();
                playSelectedSong(selectedIndex);
            }
        });
    }

    private void playSelectedSong(int selectedIndex) {
        player.loadSong(selectedIndex);
        configureProgressBar();
        configureVolume();
        controlPaneController.getPlayButton().setSelected(true);
    }

    private void configureProgressBar() {
        Slider progressSlider = controlPaneController.getProgressSlider();
        //Setting the length of a progress bar/slider
        player.getMediaPlayer().setOnReady(() -> progressSlider.setMax(player.getLoadedSongLength()));
        //Changing the time in the player will automatically update the slider
        player.getMediaPlayer().currentTimeProperty().addListener((arg, oldVal, newVal) ->
                progressSlider.setValue(newVal.toSeconds()));
        //Moving the slider will cause the song to be scrolled to the indicated position
        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(progressSlider.isValueChanging()) {
                player.getMediaPlayer().seek(Duration.seconds(newValue.doubleValue()));
            }

        });
    }

    private void configureVolume() {
        Slider volumeSlider = controlPaneController.getVolumeSlider();
        volumeSlider.valueProperty().unbind();
        volumeSlider.setMax(1.0);
        volumeSlider.valueProperty().bindBidirectional(player.getMediaPlayer().volumeProperty());
    }

    private void configureButtons() {
        TableView<Mp3Song> contentTable = contentPaneController.getContentTable();
        ToggleButton playButton = controlPaneController.getPlayButton();
        Button prevButton = controlPaneController.getPreviousButton();
        Button nextButton = controlPaneController.getNextButton();

        playButton.setOnAction(event -> {
            if (playButton.isSelected()) {
                player.play();
            } else {
                player.stop();
            }
        });

        nextButton.setOnAction(event -> {
            contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() + 1);
            playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
        });

        prevButton.setOnAction(event -> {
            contentTable.getSelectionModel().select(contentTable.getSelectionModel().getSelectedIndex() - 1);
            playSelectedSong(contentTable.getSelectionModel().getSelectedIndex());
        });
    }

    private void configureMenu() {
        MenuItem openFile = menuPaneController.getFileMenuItem();
        MenuItem openDir = menuPaneController.getDirMenuItem();

        openFile.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Mp3", "*.mp3"));
            File file = fc.showOpenDialog(new Stage());
            try {
                contentPaneController.getContentTable().getItems().add(Mp3Parser.createMp3Song(file));
            } catch (Exception e) {
                e.printStackTrace(); //ignore
            }
        });

        openDir.setOnAction(event -> {
            DirectoryChooser dc = new DirectoryChooser();
            File dir = dc.showDialog(new Stage());
            try {
                contentPaneController.getContentTable().getItems().addAll(Mp3Parser.createMp3List(dir));
            } catch (Exception e) {
                e.printStackTrace(); //ignore
            }
        });
    }

//    private void addTestMp3() {
//        ObservableList<Mp3Song> items = contentPaneController.getContentTable().getItems();
//        Mp3Song mp3SongFromPath = createMp3SongFromPath("test.mp3");
//        items.add(mp3SongFromPath);
//        items.add(mp3SongFromPath);
//        items.add(mp3SongFromPath);
//    }

//    private Mp3Song createMp3SongFromPath(String filePath) {
//        File file = new File(filePath);
//        try {
//            MP3File mp3File = new MP3File(file);
//            String absolutePath = file.getAbsolutePath();
//            String title = mp3File.getID3v2Tag().getSongTitle();
//            String author = mp3File.getID3v2Tag().getLeadArtist();
//            String album = mp3File.getID3v2Tag().getAlbumTitle();
//            return new Mp3Song(title, author, album, absolutePath);
//        } catch (IOException | TagException e) {
//            e.printStackTrace();
//            return null; //ignore
//        }
//    }

}