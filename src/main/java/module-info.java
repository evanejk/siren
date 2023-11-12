module com.happynicetime.siren {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.happynicetime.siren to javafx.fxml;
    exports com.happynicetime.siren;
}
