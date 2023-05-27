module mp3player {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires jid3lib;
    requires javafx.media;

    exports mp3player.main to javafx.graphics;
    opens mp3player.controller to javafx.fxml;
    opens mp3player.mp3 to javafx.base;
}