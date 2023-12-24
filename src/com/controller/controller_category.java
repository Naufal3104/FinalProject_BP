/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.controller;
import com.view.categoryCRUD;
import java.sql.*;

/**
 *
 * @author LENOVO
 */
public interface controller_category {
        public void Show(categoryCRUD Category) throws SQLException;
        public void Add(categoryCRUD Category) throws SQLException;
        public void Edit(categoryCRUD Category) throws SQLException;
        public void Delete(categoryCRUD Category) throws SQLException;
        public void Clear(categoryCRUD Category) throws SQLException;
        public void ClickTable(categoryCRUD Category) throws SQLException;
}
