package com.project;

public class UserName {
    private static String userName = "";
    private static boolean isNameSaved = false;

    public static String getUserName() {
        return userName; // ✅ จะคืนค่า "" (ว่างเปล่า) ทุกครั้งที่เริ่มแอปใหม่
    }

    public static void setUserName(String name) {
        userName = name;
        isNameSaved = true;
    }

    public static boolean isNameSaved() {
        return isNameSaved;
    }
}
