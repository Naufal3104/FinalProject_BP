/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.controller;
import com.view.productCRUD;
import java.sql.*;
import java.util.logging.*;
/**
 *
 * @author LENOVO
 */
public interface controller_product {
    public void Save(productCRUD product) throws SQLException;
    public void Edit(productCRUD product) throws SQLException;
    public void Delete(productCRUD product) throws SQLException;
    public void Show(productCRUD product) throws SQLException;
    public void New(productCRUD product) throws SQLException;
    public void ClickTable(productCRUD product) throws SQLException;
}
