package com.project;

import java.time.LocalDate;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TableReservation {
    private Stage mainStage;
    private Scene mainScene;
    private static String selectedTable = null;
    private static LocalDate selectedDate = null;

    public TableReservation(Stage stage, Scene mainScene) {
        this.mainStage = stage;
        this.mainScene = mainScene;
    }

    public void start() {
        // UI Layout
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f5f5, #ffffff);");

        // กล่องที่เก็บ Input ต่าง ๆ
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 15px;
            -fx-padding: 20px;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 3);
        """);

        // หัวข้อ
        Label reservationLabel = new Label("Reserve a dining table");
        reservationLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333;");

        //  ช่องใส่ชื่อ
        TextField nameField = new TextField(UserName.getUserName());
        nameField.setPromptText( "Fill in your name");
        nameField.setPrefWidth(300);
        nameField.setStyle(inputFieldStyle());

        // เลือกโต๊ะ
        ComboBox<String> tableComboBox = new ComboBox<>();
        tableComboBox.getItems().addAll("table 1", "table 2", "table 3", "table 4", "table 5", "table 6");
        tableComboBox.setPromptText("Choose the table you want.");
        tableComboBox.setPrefWidth(300);
        tableComboBox.setStyle(inputFieldStyle());

        if (selectedTable != null) {
            tableComboBox.setValue(selectedTable);
        }

        Platform.runLater(() -> tableComboBox.requestFocus());

        // ปฏิทินเลือกวันที่
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Choose a date");
        datePicker.setPrefWidth(300);
        datePicker.setStyle(calendarStyle());

        if (selectedDate != null) {
            datePicker.setValue(selectedDate);
        }

        datePicker.setDayCellFactory(createDateCellFactory());

        // ปุ่มจองโต๊ะ
        Button reserveButton = createButton("Reserve a table.", reserveButtonStyle(), e -> handleReservation(tableComboBox, datePicker));

        // ปุ่มกลับหน้าหลัก
        Button backButton = createButton("Back to main", backButtonStyle(), e -> mainStage.setScene(mainScene));

        // ใส่องค์ประกอบต่าง ๆ ลงใน Card UI
        card.getChildren().addAll(reservationLabel, nameField, tableComboBox, datePicker, reserveButton, backButton);
        root.getChildren().add(card);

        // Scene
        Scene reservationScene = new Scene(root, 520, 600);
        mainStage.setScene(reservationScene);
        mainStage.setTitle("");
    }

    // ฟังก์ชันสร้างปุ่มแบบง่าย ๆ
    private Button createButton(String text, String style, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setPrefWidth(280);
        button.setMinHeight(45);
        button.setStyle(style);
        button.setOnAction(action);
        return button;
    }

    //  Style ปุ่ม
    private String reserveButtonStyle() {
        return """
            -fx-font-size: 16px;
            -fx-padding: 10px;
            -fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32);
            -fx-text-fill: white;
            -fx-border-radius: 10px;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 3);
        """;
    }

    private String backButtonStyle() {
        return """
            -fx-font-size: 16px;
            -fx-padding: 10px;
            -fx-background-color: linear-gradient(to right, #F44336, #D32F2F);
            -fx-text-fill: white;
            -fx-border-radius: 10px;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 3);
        """;
    }

    // Style ช่องป้อนข้อมูล
    private String inputFieldStyle() {
        return """
            -fx-font-size: 14px;
            -fx-padding: 10px;
            -fx-border-radius: 8px;
            -fx-border-color: #ccc;
            -fx-background-color: white;
        """;
    }

    // Style ปฏิทิน
    private String calendarStyle() {
        return """
            -fx-font-size: 14px;
            -fx-padding: 10px;
            -fx-border-radius: 8px;
            -fx-border-color: #bbb;
            -fx-background-color: white;
        """;
    }

    // ป้องกันการเลือกวันที่ที่ผ่านไปแล้ว
    private Callback<DatePicker, DateCell> createDateCellFactory() {
        return param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #aaaaaa;");
                } else {
                    setStyle("-fx-background-color: white;");
                    setOnMouseEntered(e -> setStyle("-fx-background-color: #dcedc8;"));
                    setOnMouseExited(e -> setStyle("-fx-background-color: white;"));
                }
            }
        };
    }

    // ฟังก์ชันจัดการจองโต๊ะ
    private void handleReservation(ComboBox<String> tableComboBox, DatePicker datePicker) {
        if (tableComboBox.getValue() == null || datePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "⚠ Please reserve a table and date !", ButtonType.OK);
                alert.setTitle("Warning");
                alert.setHeaderText("");
                alert.showAndWait();
        } else{
            showAlert("Successfully reserved !\ntable : " + tableComboBox.getValue() + "\nDate : " + datePicker.getValue());
            
        }
        selectedTable = tableComboBox.getValue();
        selectedDate = datePicker.getValue();
    }

    // ฟังก์ชันแสดงข้อความแจ้งเตือน
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
              alert.setTitle("Table Reservation");
              alert.setHeaderText("");
              alert.showAndWait();
    }

    public static String getSelectedTable() {
        return selectedTable != null ? selectedTable : "Not reserved";
    }

    public static LocalDate getSelectedDate() {
        return selectedDate != null ? selectedDate : LocalDate.now();
    }
}

