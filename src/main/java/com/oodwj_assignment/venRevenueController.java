package com.oodwj_assignment;

import com.oodwj_assignment.helpers.Response;
import com.oodwj_assignment.models.Orders;
import com.oodwj_assignment.dao.base.DaoFactory;
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
import java.util.stream.Collectors;

public class venRevenueController {

    @FXML private Label quantityLabel;
    @FXML private Label revenueLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private LineChart<String, Double> revenueLineChart;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID userId = UUID.fromString("8a7ed604-d77a-476f-87c5-8c7e71940756");

    public void initialize(){
        startDate = LocalDate.now().minusDays(7);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    public void dailyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusDays(1);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    public void weeklyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusDays(7);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    public void monthlyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusMonths(1);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    public void yearlyToggleClicked(ActionEvent event) {
        startDate = LocalDate.now().minusYears(1);
        endDate = LocalDate.now();
        loadAndDisplayData();
    }

    private void loadAndDisplayData() {
        Map<String, Object> query = Map.of("userId", userId);
        Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

        if (response.isSuccess()) {
            ArrayList<Orders> orders = response.getData();

            List<Orders> filteredOrders = orders.stream()
                    .filter(order -> !order.getCreatedAt().toLocalDate().isBefore(startDate) && !order.getCreatedAt().toLocalDate().isAfter(endDate))
                    .collect(Collectors.toList());

            double totalRevenue = calculateTotalRevenue(filteredOrders);
            int totalQuantity = calculateTotalQuantity(filteredOrders);
            int totalOrders = filteredOrders.size();

            updateLineChart(filteredOrders);
            revenueLabel.setText(String.format("RM %.2f", totalRevenue));
            quantityLabel.setText(String.valueOf(totalQuantity));
            totalOrdersLabel.setText(String.valueOf(totalOrders));
        } else {
            System.out.println("Failed to retrieve orders: " + response.getMessage());
        }
    }

    private double calculateTotalRevenue(List<Orders> orders) {
        double totalRevenue = 0;
        for (Orders order : orders) {
            totalRevenue += order.getTotalPrice();
        }
        return totalRevenue;
    }

    private int calculateTotalQuantity(List<Orders> orders) {
        int totalQuantity = 0;
        for (Orders order : orders) {
            totalQuantity += order.getTotalQuantity();
        }
        return totalQuantity;
    }

    private void updateLineChart(List<Orders> orders) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Revenue");

        for (Orders order : orders) {
            LocalDate orderDate = order.getCreatedAt().toLocalDate();
            String formattedDate = orderDate.toString();
            series.getData().add(new XYChart.Data<>(formattedDate, order.getTotalPrice()));
        }

        revenueLineChart.getData().clear();
        revenueLineChart.getData().add(series);
    }
}
