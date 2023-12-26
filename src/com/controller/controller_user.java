/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.controller;
import com.view.Users;
import java.sql.*;
/**
 *
 * @author LENOVO
 */
public interface controller_user {
    public void Save(Users user) throws SQLException;
    public void Edit(Users user) throws SQLException;
    public void Delete(Users user) throws SQLException;
    public void Show(Users user) throws SQLException;
    public void New(Users user) throws SQLException;
    public void ClickTable(Users user) throws SQLException;
}
