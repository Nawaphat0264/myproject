package com.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class CartScreen {
    private Stage mainStage;
    private ObservableList<String> cartItems;
    private Map<String, Double> menuPrices;
    private ListView<String> cartListView;
    private Label totalLabel;
    private String customerComment;

    public CartScreen(Stage stage, ObservableList<String> orderItems, String comment) {
        this.mainStage = stage;
        this.cartItems = FXCollections.observableArrayList(orderItems);
        this.customerComment = comment;

        // กำหนดราคาของแต่ละเมนู
        menuPrices = new HashMap<>();
        menuPrices.put("Steak", 499.0);
        menuPrices.put("Pizza", 219.0);
        menuPrices.put("Burger", 189.0);
        menuPrices.put("Spaghetti", 129.0);
        menuPrices.put("French Fries", 109.0);
        menuPrices.put("Salad", 89.0);
    }

    private double calculateTotal() {
        double total = 0.0;
    
        for (String item : cartItems) {
            String[] parts = item.split(" x"); // แยกชื่ออาหารกับจำนวน
            String foodName = parts[0].trim(); // ชื่ออาหาร
            int quantity = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 1; // จำนวน
    
            total += menuPrices.getOrDefault(foodName, 0.0) * quantity; // คำนวณราคาต่อจำนวน
        }
    
        return total;
    }
    
    public void start() {
        Label cartLabel = new Label("Menu");
        cartLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        cartListView = new ListView<>(cartItems);
        cartListView.setStyle("-fx-border-color: #ccc; -fx-border-radius: 10px; -fx-padding: 5px;");
        cartListView.setMaxHeight(250);
        cartListView.setPrefWidth(350);


        Label commentLabel = new Label("Notes");
        commentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555; -fx-font-weight: bold;");

        TextArea commentDisplay = new TextArea(customerComment);
        commentDisplay.setPrefHeight(100);
        commentDisplay.setPrefWidth(350);
        commentDisplay.setEditable(false);
        commentDisplay.setWrapText(true);
        commentDisplay.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 10px;
            -fx-border-color: #ccc;
            -fx-border-radius: 10px;
            -fx-background-color: white;
        """);

        totalLabel = new Label("Total: " + calculateTotal() + " Baht");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill:rgb(21, 92, 24); -fx-font-weight: bold;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);

        Button backButton = new Button("Back to Order");
        backButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #F44336, #D32F2F);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        backButton.setPrefWidth(160);
 
        backButton.setOnAction(e -> {
            new OrderFood().start(new Stage());
                mainStage.close();
        });

         //เพิ่มปุ่ม "Pay Now" สำหรับเปิดหน้า PaymentScreen
         Button payButton = new Button("Pay Now");
         payButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        payButton.setPrefWidth(160);
 
        payButton.setOnAction(e -> {
             double totalAmount = calculateTotal(); // คำนวณยอดรวม
             if (totalAmount == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "⚠ Please choose a food menu !", ButtonType.OK);
                alert.setTitle("Warning");
                alert.setHeaderText("");
                alert.showAndWait();
             } else {
                 PaymentScreen paymentScreen = new PaymentScreen(mainStage, totalAmount);
                 paymentScreen.start();
             }
         });

        VBox layout = new VBox(10, cartLabel, cartListView, commentLabel,commentDisplay, totalLabel, payButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #eeeeee;");
        layout.setEffect(shadow);

        Scene cartScene = new Scene(layout, 420, 550);
        mainStage.setScene(cartScene);
        mainStage.centerOnScreen();
        mainStage.setTitle("");
        mainStage.show();
    }
}