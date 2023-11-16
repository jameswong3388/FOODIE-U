package com.oodwj_assignment;

import com.oodwj_assignment.dao.base.DaoFactory;
import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Tasks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class runRevenueController {

    @FXML private Label revenueLabel;
    @FXML private LineChart<String, Double> revenueLineChart;
    private LocalDate startDate;
    private LocalDate endDate;

    public void initialize(){
        startDate = LocalDate.now().minusDays(7);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    // Update data for the last 24 hours
    public void dailyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusDays(1);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    // Update data for the last 7 days
    public void weeklyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusDays(7);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    // Update data for the last 30 days
    public void monthlyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusMonths(1);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    // Update data for the last 365 days
    public void yearlyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusYears(1);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    private void loadAndDisplayData() {
        List<UUID> runnerIds = runMainController.getTaskId();
        List<Tasks> filteredTasks = new ArrayList<>();

        if (!runnerIds.isEmpty()) {
            for (UUID taskId : runnerIds) {
                Map<String, Object> query = Map.of("Id", taskId);
                Response<ArrayList<Tasks>> response = DaoFactory.getTaskDao().read(query);

                if (response.isSuccess()) {
                    Tasks task = response.getData().get(0);

                    if (task.getStatus() == Tasks.taskStatus.Delivered &&
                            !task.getCreatedAt().toLocalDate().isBefore(startDate) &&
                            !task.getCreatedAt().toLocalDate().isAfter(endDate)) {
                        filteredTasks.add(task);
                    }
                } else {
                    System.out.println("Failed to retrieve task with taskId " + taskId + ": " + response.getMessage());
                }
            }

            double totalRevenue = calculateTotalRevenue(filteredTasks);

            updateLineChart(filteredTasks);
            revenueLabel.setText(String.format("RM %.2f", totalRevenue));
        } else {
            System.out.println("No tasks found for the specified runnerIds");
        }
    }

    private double calculateTotalRevenue(List<Tasks> Tasks) {
        double totalRevenue = 0;
        for (Tasks tasks : Tasks) {
            if (tasks.getDeliveryFee() != null) {  // Check if delivery fee is not null
                totalRevenue += tasks.getDeliveryFee();
            }
        }
        return totalRevenue;
    }
    

    private void updateLineChart(List<Tasks> Tasks) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Revenue");

        for (Tasks tasks : Tasks) {
            LocalDate taskDate = tasks.getCreatedAt().toLocalDate();
            String formattedDate = taskDate.toString();
            series.getData().add(new XYChart.Data<>(formattedDate, tasks.getDeliveryFee()));
        }

        revenueLineChart.getData().clear();
        revenueLineChart.getData().add(series);
    }
}
