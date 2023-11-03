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

public class venRevenueController {

    @FXML private Label quantityLabel;
    @FXML private Label revenueLabel;
    @FXML private Label totalOrdersLabel;
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
        List<UUID> orderIds = venMainController.getOrderIds();
        List<Orders> filteredOrders = new ArrayList<>();

        if (!orderIds.isEmpty()) {
            for (UUID orderId : orderIds) {
                Map<String, Object> query = Map.of("Id", orderId);
                Response<ArrayList<Orders>> response = DaoFactory.getOrderDao().read(query);

                if (response.isSuccess()) {
                    Orders order = response.getData().get(0);

                    if (order.getStatus() == Orders.orderStatus.Completed &&
                            !order.getCreatedAt().toLocalDate().isBefore(startDate) &&
                            !order.getCreatedAt().toLocalDate().isAfter(endDate)) {
                        filteredOrders.add(order);
                    }
                } else {
                    System.out.println("Failed to retrieve order with orderId " + orderId + ": " + response.getMessage());
                }
            }

            double totalRevenue = calculateTotalRevenue(filteredOrders);
            int totalQuantity = calculateTotalQuantity(filteredOrders);
            int totalOrders = filteredOrders.size();

            updateLineChart(filteredOrders);
            revenueLabel.setText(String.format("RM %.2f", totalRevenue));
            quantityLabel.setText(String.valueOf(totalQuantity));
            totalOrdersLabel.setText(String.valueOf(totalOrders));
        } else {
            System.out.println("Failed to retrieve orders: Order list is empty");
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
