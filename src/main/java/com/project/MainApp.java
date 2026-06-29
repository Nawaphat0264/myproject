package com.project;

import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        VBox root = new VBox(15); // เพิ่ม spacing
        root.setPadding(new Insets(20, 25, 20, 25)); // เพิ่มระยะห่างด้านบน
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #eeeeee;");

        DropShadow shadow = new DropShadow(5, 2, 2, Color.GRAY);

        // โหลดโลโก้
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/Logo.jpg")));
        logoView.setFitWidth(455);
        logoView.setFitHeight(455);
        logoView.setPreserveRatio(true);

        // === กล่องเก็บ TextField และปุ่ม Save ===
        VBox nameContainer = new VBox(10);
        nameContainer.setAlignment(Pos.CENTER);

        // ช่องกรอกชื่อ
        TextField nameTextField = new TextField(UserName.getUserName());
        nameTextField.setPromptText("Enter your name");
        nameTextField.setStyle("""
            -fx-font-size: 16px;
            -fx-padding: 10px;
            -fx-border-radius: 10px;
            -fx-border-color: #bbb;
        """);
        nameTextField.setEffect(shadow);
        nameTextField.setMaxWidth(250); // กำหนดขนาดให้เหมาะสม

        Platform.runLater(() -> {
            nameTextField.positionCaret(nameTextField.getText().length());
            root.requestFocus();
        });

        // CheckBox "Saved"
        CheckBox saveCheckBox = new CheckBox("Saved");
        saveCheckBox.setDisable(true);
        saveCheckBox.setStyle("-fx-font-size: 12px; -fx-text-fill: #444;");

        if (UserName.isNameSaved()) {
            saveCheckBox.setSelected(true);
        }

        // ปุ่ม Save
        Button saveButton = new Button("Save");
        saveButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        saveButton.setPrefWidth(150);

        saveButton.setOnAction(e -> {
        String OrderName = nameTextField.getText().trim();
        if (!OrderName.isEmpty()) {
        UserName.setUserName(OrderName);
        saveCheckBox.setSelected(true);

        // เช็คว่ามีประวัติการสั่งอาหารไหม
        if (OrderHistory.hasOrderHistory(OrderName)) {
            List<String> pastOrders = OrderHistory.getOrder(OrderName);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order History ");
            alert.setHeaderText("Welcome back, " + OrderName + " !");
            alert.setContentText("Your past orders :\n" + String.join(", ", pastOrders));
            alert.showAndWait();
        }
    }
});

        // ใส่ช่องกรอกชื่อ + ปุ่ม Save ไว้ใน nameContainer
        nameContainer.getChildren().addAll(nameTextField, saveCheckBox, saveButton);

        // === ปุ่มต่าง ๆ ===
        Button orderFoodButton = new Button("Order Food");
        orderFoodButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #FF9800, #FF5722, #FFD700);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        orderFoodButton.setPrefWidth(160);

        orderFoodButton.setOnAction(e -> {
            try {
                new OrderFood().start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button reserveTableButton = new Button("Reserve Table");
        reserveTableButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #1E90FF, #6A5ACD);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        reserveTableButton.setPrefWidth(160);

        reserveTableButton.setOnAction(e -> {
            TableReservation reservationPage = new TableReservation(primaryStage, primaryStage.getScene());
            reservationPage.start();
        });

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(12);
        buttonGrid.setVgap(12);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.add(orderFoodButton, 0, 0);
        buttonGrid.add(reserveTableButton, 1, 0);

        Button exitButton = new Button("Exit");
        exitButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #F44336, #D32F2F);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
        """);
        exitButton.setPrefWidth(160);
        exitButton.setOnAction(e -> Platform.exit());

        VBox exitContainer = new VBox(exitButton);
        exitContainer.setAlignment(Pos.CENTER);
        exitContainer.setPadding(new Insets(5, 0, 0, 0));

        // === จัดเรียงองค์ประกอบใหม่ ===
        root.getChildren().addAll(logoView, nameContainer, buttonGrid, exitContainer);

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
