package com.project;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class OrderFood extends Application {
    private static ObservableList<HBox> orderItemsDisplay = FXCollections.observableArrayList();
    private static HashMap<String, Integer> orderItems = new HashMap<>();
    private static String savedComment = "";
    private ListView<HBox> orderListView;
    private TextArea commentArea;

    private VBox createMenuItem(String imagePath, String foodName, String description, double price) {
        InputStream imageStream = getClass().getResourceAsStream(imagePath);

        ImageView imageView = new ImageView(new Image(imageStream));
        imageView.setFitWidth(115);
        imageView.setFitHeight(115);
        imageView.setPreserveRatio(true);

        Tooltip tooltip = new Tooltip(foodName + "\n" + description );
        Tooltip.install(imageView, tooltip);

        Label nameLabel = new Label(foodName);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 5px;");

        String formattedPrice = price % 1 == 0 ? String.format("%.0f ฿", price) : String.format("%.2f ฿", price);
        Label priceLabel = new Label("💰 " + formattedPrice);
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722;");

        VBox itemBox = new VBox(5, imageView, nameLabel, priceLabel);
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setStyle("""
            -fx-border-color: #ddd;
            -fx-border-radius: 10px;
            -fx-padding: 10px;
            -fx-background-color: #fff;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 3);
        """);

        itemBox.setOnMouseClicked(e -> addToOrder(foodName));

        return itemBox;
    }

    private void addToOrder(String foodName) {
        orderItems.put(foodName, orderItems.getOrDefault(foodName, 0) + 1);
        updateOrderList();
    }

    private void removeFromOrder(String foodName) {
        if (orderItems.containsKey(foodName)) {
            int count = orderItems.get(foodName);
            if (count > 1) {
                orderItems.put(foodName, count - 1);
            } else {
                orderItems.remove(foodName);
            }
            updateOrderList();
        }
    }

    private void updateOrderList() {
        orderItemsDisplay.clear();
        for (String item : orderItems.keySet()) {
            Label itemLabel = new Label(item + " x" + orderItems.get(item));
            itemLabel.setStyle("-fx-font-size: 14px;");
    
            Button removeButton = new Button("❌");
            removeButton.setStyle("-fx-background-color: #FF4444; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 3px 7px; -fx-background-radius: 5px;");
            removeButton.setOnAction(e -> removeFromOrder(item));
    
            HBox itemBox = new HBox(10, itemLabel, new Pane(), removeButton);
            HBox.setHgrow(itemBox.getChildren().get(1), Priority.ALWAYS);
            itemBox.setAlignment(Pos.CENTER);
            orderItemsDisplay.add(itemBox);
        }
    }
    
    @Override
    public void start(Stage orderStage) {
        loadOrderData(); //หลดข้อมูลรายการที่เคยสั่งไว้
        
        VBox[] menuItems = {
            createMenuItem("/images/Steak.jpg", "Steak", ": Australian beef ribeye steak", 499),
            createMenuItem("/images/Pizza.jpg", "Pizza", ": Hawaiian Pizza", 219),
            createMenuItem("/images/Burger.jpg", "Burger", ": Ground Beef Burger with Cheese", 189),
            createMenuItem("/images/Spaghetti.jpg", "Spaghetti", ": Spaghetti Carbonara", 129),
            createMenuItem("/images/Frenchfries.jpg", "French Fries",": French fries with cheese",109),
            createMenuItem("/images/Salad.jpg", "Salad",": Caesar salad", 89),
        };

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(20);
        menuGrid.setVgap(20);
        menuGrid.setAlignment(Pos.CENTER);
        menuGrid.setPadding(new Insets(15));

        for (int i = 0; i < menuItems.length; i++) {
            menuGrid.add(menuItems[i], i % 3, i / 3);
        }

        orderListView = new ListView<>(orderItemsDisplay);
        orderListView.setMinHeight(90);
        orderListView.setMaxHeight(Double.MAX_VALUE);
        updateOrderList();

        commentArea = new TextArea(savedComment);
        commentArea.setPromptText("Add or Reduce");
        commentArea.setPrefHeight(150);
        commentArea.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 10px;
            -fx-border-color: #ccc;
            -fx-border-radius: 10px;
            -fx-background-color: white;
        """);

        Button cartButton = new Button("Shopping Cart");
        saveOrderData();
        cartButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #212121, #616161);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        cartButton.setPrefWidth(160);
        
            cartButton.setOnAction(e -> {
                saveOrderData();
                ObservableList<String> cartItems = FXCollections.observableArrayList();
                for (HBox itemBox : orderItemsDisplay) {
                    if (!itemBox.getChildren().isEmpty() && itemBox.getChildren().get(0) instanceof Label label) {
                        cartItems.add(label.getText());
                    }
                }
                CartScreen cartScreen = new CartScreen(orderStage, cartItems, savedComment); // ✅ ส่งคอมเมนต์ไปด้วย
                cartScreen.start();
            });

        Button backButton = new Button("Back to Main");
        backButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #F44336, #D32F2F);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        backButton.setPrefWidth(160);
        
        backButton.setOnAction(e -> {
            saveOrderData();
            try {
                MainApp mainApp = new MainApp();
                Stage mainStage = new Stage();
                mainApp.start(mainStage);
                orderStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Label MENU = new Label("MENU");
        MENU.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label orderItems = new Label("Order Items");
        orderItems.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label notes = new Label("Notes");
        notes.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox layout = new VBox(20,
            MENU, menuGrid,
            orderItems, orderListView,
            notes, commentArea,
            cartButton,
            backButton
        );
 
        Scene scene = new Scene(layout, 700, 850);
            scene.setOnMouseClicked(event -> {
            if (!commentArea.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                layout.requestFocus();
            }
        });

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #eeeeee;");

        orderStage.setScene(scene);
        orderStage.show();
    }

    private void saveOrderData() {
    savedComment = commentArea.getText();
    
    // ดึงรายการอาหารจาก HashMap มาเป็น List
    List<String> orderList = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
        for (int i = 0; i < entry.getValue(); i++) {
            orderList.add(entry.getKey());
        }
    }
    
    OrderHistory.saveOrder(UserName.getUserName(), orderList);
}

    private void loadOrderData() {
    String currentUser = UserName.getUserName();
    List<String> pastOrders = OrderHistory.getOrder(currentUser);
    orderItems.clear();
    
    // แปลงรายการอาหารเป็น HashMap
    for (String item : pastOrders) {
        orderItems.put(item, orderItems.getOrDefault(item, 0) + 1);
    }
    updateOrderList();
}

    public static void main(String[] args) {
        launch(args);
    }
}
