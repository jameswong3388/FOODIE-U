package com.oodwj_assignment;

import com.oodwj_assignment.models.Tasks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.UUID;

public class runTaskPaneController {

    @FXML private Label taskId;
    @FXML private Label deliveryFee;
    @FXML private Label date;
    private runTaskController runTaskController;

    public void populateTaskData(Tasks tasks){
        taskId.setText(String.valueOf(tasks.getId()));
        deliveryFee.setText(String.format("RM %.2f", tasks.getDeliveryFee()));
        date.setText(String.valueOf(tasks.getCreatedAt().withNano(0)));
    }

    public void viewButtonClicked(ActionEvent event) throws IOException {
        UUID taskId = UUID.fromString(this.taskId.getText());
        if (runTaskController != null) {
            runTaskController.setTaskInfo(taskId);
        }
    }

    // Set the parent runTaskController for communication
    public void setRunTaskController(runTaskController controller) {
        this.runTaskController = controller;
    }

    public UUID getTaskId() {
        return UUID.fromString(taskId.getText());
    }
}
