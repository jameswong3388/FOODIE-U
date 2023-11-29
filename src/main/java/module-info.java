module com.oodwj_assigment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.oodwj_assignment to javafx.fxml;
    exports com.oodwj_assignment;
    exports com.oodwj_assignment.dao;
    opens com.oodwj_assignment.dao to javafx.fxml;
    exports com.oodwj_assignment.dao.users;
    opens com.oodwj_assignment.dao.users to javafx.fxml;
    exports com.oodwj_assignment.helpers;
    opens com.oodwj_assignment.helpers to javafx.fxml;
}