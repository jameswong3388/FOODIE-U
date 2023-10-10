module com.oodwj_assigment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.oodwj_assignment to javafx.fxml;
    exports com.oodwj_assignment;
    exports com.oodwj_assignment.Controllers;
    opens com.oodwj_assignment.Controllers to javafx.fxml;

}