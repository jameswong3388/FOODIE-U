module com.oodwj_assigment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.oodwj_assignment to javafx.fxml;
    exports com.oodwj_assignment;
}