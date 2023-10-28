module com.oodwj_assigment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.oodwj_assignment to javafx.fxml;
    exports com.oodwj_assignment;
    exports com.oodwj_assignment.dao;
    opens com.oodwj_assignment.dao to javafx.fxml;
    exports com.oodwj_assignment.mediaTesting;
    opens com.oodwj_assignment.mediaTesting to javafx.fxml;
    exports com.oodwj_assignment.dao.media;
    opens com.oodwj_assignment.dao.media to javafx.fxml;
}