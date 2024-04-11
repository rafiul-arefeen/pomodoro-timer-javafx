module org.example.pomodoro {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens org.example.pomodoro to javafx.fxml;
    exports org.example.pomodoro;
}