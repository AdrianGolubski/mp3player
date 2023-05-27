package mp3player.controller;

import javafx.fxml.FXML;

public class MainController {
    @FXML
    private ContentPaneController contentPaneController;

    @FXML
    private ControlPaneController controlPaneController;

    @FXML
    MenuPaneController menuPaneController;

    public MainController() {
    }

    public void initialize() {
        System.out.println("Main controller created");
        System.out.println(controlPaneController);
        System.out.println(contentPaneController);
        System.out.println(menuPaneController);
    }
}