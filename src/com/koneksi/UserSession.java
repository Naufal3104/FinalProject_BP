/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.koneksi;

/**
 *
 * @author LENOVO
 */
public class UserSession {

    private static String username;
    private static String cashierId;
    private static int userRole; // tambahkan ini

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static String getCashierId() {
        return cashierId;
    }

    public static void setCashierId(String cashierId) {
        UserSession.cashierId = cashierId;
    }

    public static int getUserRole() { // tambahkan ini
        return userRole;
    }

    public static void setUserRole(int userRole) { // tambahkan ini
        UserSession.userRole = userRole;
    }
    
    public static void clearUserSession() {
        username = null;
        cashierId = null;
        userRole = 0;
    }
}
