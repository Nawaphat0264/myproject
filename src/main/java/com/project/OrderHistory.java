package com.project;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.util.*;

public class OrderHistory {
    private static final String FILE_PATH = "order_history.csv";

    // โหลดประวัติการสั่งอาหารจาก CSV
    public static Map<String, List<String>> loadOrderHistory() {
        Map<String, List<String>> orderHistory = new HashMap<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) return orderHistory;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record.length > 1) {
                    String userName = record[0];
                    List<String> orders = Arrays.asList(Arrays.copyOfRange(record, 1, record.length));
                    orderHistory.put(userName, orders);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return orderHistory;
    }

    // บันทึกออเดอร์ของลูกค้าลง CSV
    public static void saveOrder(String userName, List<String> orders) {
        Map<String, List<String>> orderHistory = loadOrderHistory();
        orderHistory.put(userName, orders);

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, List<String>> entry : orderHistory.entrySet()) {
                List<String> row = new ArrayList<>();
                row.add(entry.getKey());
                row.addAll(entry.getValue());
                writer.writeNext(row.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ดึงออเดอร์ของลูกค้าตามชื่อ
    public static List<String> getOrder(String userName) {
        Map<String, List<String>> orderHistory = loadOrderHistory();
        return orderHistory.getOrDefault(userName, new ArrayList<>());
    }

    // ตรวจสอบว่าผู้ใช้มีประวัติการสั่งอาหารหรือไม่
    public static boolean hasOrderHistory(String userName) {
        Map<String, List<String>> orderHistory = loadOrderHistory();
        return orderHistory.containsKey(userName);
    }
}

