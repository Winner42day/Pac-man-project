module pacmanapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens pacmanapp to javafx.fxml;
    exports pacmanapp;
}
