
package com.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;

public class PaymentScreen {
    private Stage paymentStage;
    private double totalAmount;

    public PaymentScreen(Stage stage, double total) {
        this.paymentStage = new Stage();    
        this.totalAmount = total;
    }
        
    public void start() {
        Label titleLabel = new Label("Payment");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // โหลด QR Code
        Image image = new Image(getClass().getResourceAsStream("/QR.jpg"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        // แสดงยอดรวมที่ต้องจ่าย
        Label totalLabel = new Label("Total: " + totalAmount + " Baht");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill:rgb(21, 92, 24); -fx-font-weight: bold;");

        Label qrText = new Label("Scan QR Code to pay");
        qrText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #555;");

        // ช่องเลือกเวลามารับอาหาร
        Label timeLabel = new Label("Select Pick-up Time:");
        timeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

        ComboBox<LocalTime> timePicker = new ComboBox<>();
        timePicker.setStyle("""
            -fx-font-size: 14px;
            -fx-padding: 8px;
            -fx-border-color: #cccccc;
            -fx-background-radius: 10px;
        """);
        
        for (int hour = 12 ; hour <= 20; hour++) {
            timePicker.getItems().add(LocalTime.of(hour, 0));
        }
        timePicker.setPromptText("Select Time");

        // ปุ่มยืนยันการชำระเงิน
        Button confirmPaymentButton = new Button("✅ Confirm Payment");
        confirmPaymentButton.setStyle("""
            -fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32);
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 10px 20px;
            -fx-background-radius: 30px;
        """);

        confirmPaymentButton.setOnAction(e -> {
            if (timePicker.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "⚠ Please select a pick-up time !", ButtonType.OK);
                alert.setTitle("Warning");
                alert.setHeaderText("");
                alert.showAndWait();
            } else {

                String tableNumber = TableReservation.getSelectedTable();
                LocalDate bookingDate = TableReservation.getSelectedDate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Payment");
                alert.setHeaderText("🎉 Thank you for your order !");
                alert.setContentText(
                            "👤 Name : " + UserName.getUserName() +
                            "\n💰 Total : " + totalAmount + " Baht" +
                            "\n🕒 Pick-up Time : " + timePicker.getValue() +
                            "\n🪑 Table Number : " + tableNumber +
                            "\n📅 Booking Date : " + bookingDate
        );

        alert.showAndWait();
        paymentStage.close();
        
            }
        });

        // เพิ่ม Shadow Effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(8);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(150, 150, 150, 0.5));
        confirmPaymentButton.setEffect(shadow);

        // Layout
        VBox layout = new VBox(15, titleLabel, imageView, qrText, totalLabel, timeLabel, timePicker, confirmPaymentButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #eeeeee;-fx-border-radius: 10px; -fx-padding: 20px;");
        layout.setEffect(shadow);

        // Scene
        Scene paymentScene = new Scene(layout, 420, 550);
        paymentStage.setScene(paymentScene);
        paymentStage.setTitle("");
        paymentStage.show();
    }
}