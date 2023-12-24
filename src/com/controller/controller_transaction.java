/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.controller;

import com.view.showTransaction;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
public interface controller_transaction {
    public void ShowTable(showTransaction Transaction)throws SQLException;
    public void ClickTable(showTransaction Transaction)throws SQLException;
    public void SearchbyDate(showTransaction Transaction)throws SQLException;
    public void ClearTable(showTransaction Transaction)throws SQLException;
}
