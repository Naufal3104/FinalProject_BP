/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.controller;
import com.view.sales;
import java.sql.*;
import java.util.logging.*;

/**
 *
 * @author LENOVO
 */
public interface controller_sales {
    public void addcart(sales Sales)throws SQLException;
    public void removecart(sales Sales)throws SQLException;
    public void editcart(sales Sales)throws SQLException;
    public void clear(sales Sales)throws SQLException;
    public void clearcart(sales Sales)throws SQLException;
    public void checkout(sales Sales)throws SQLException;
    public void showproduct(sales Sales)throws SQLException;
    public void showcart(sales Sales)throws SQLException;
    public void clickproduct(sales Sales)throws SQLException;
    public void clickcart(sales Sales)throws SQLException;
    public void Search(sales Sales)throws SQLException;
    public void ClearTable(sales Sales)throws SQLException;
}
