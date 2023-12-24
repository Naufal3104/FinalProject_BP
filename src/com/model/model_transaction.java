/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import com.controller.controller_transaction;
import com.koneksi.connect;
import com.view.showTransaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import com.koneksi.GetTransactionID;

/**
 *
 * @author LENOVO
 */
public class model_transaction implements controller_transaction {

    @Override
    public void ShowTable(showTransaction Transaction) throws SQLException {
        try {
            Transaction.tblmodel.getDataVector().removeAllElements();
            Transaction.tblmodel.fireTableDataChanged();
            try {
                Connection con = connect.getcon();
                Statement stt = con.createStatement();
                String sql = "SELECT t.TransactionID, c.CashierName, t.Date, t.Total, t.PayTotal FROM transactions t "
                        + "INNER JOIN cashiers c ON t.CashierID = c.CashierID";
                ResultSet res = stt.executeQuery(sql);
                int i = 1;
                while (res.next()) {
                    Object[] ob = new Object[6];
                    ob[0] = i++;
                    ob[1] = res.getString(1);
                    ob[2] = res.getString(2);
                    ob[3] = res.getString(3);
                    ob[4] = res.getString(4);
                    ob[5] = res.getString(5);
                    Transaction.tblmodel.addRow(ob);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void ClickTable(showTransaction Transaction) throws SQLException {
        int pilih = Transaction.tableTransaction.getSelectedRow();
        if (pilih == -1) {
            return;
        }
        String selectedTransactionID = Transaction.tableTransaction.getModel().getValueAt(pilih, 1).toString();
        System.out.println(selectedTransactionID);
        GetTransactionID.setTransactionID(selectedTransactionID);
    }

    @Override
    public void SearchbyDate(showTransaction Transaction) throws SQLException {
        ClearTable(Transaction);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(Transaction.jDate.getDate());
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT t.TransactionID, c.CashierName, t.Date, t.Total, t.PayTotal FROM transactions t "
                    + "INNER JOIN cashiers c ON t.CashierID = c.CashierID "
                    + "WHERE t.Date = '" + date + "'";
            ResultSet res = stt.executeQuery(sql);
            int i = 1;
            while (res.next()) {
                Object[] ob = new Object[6];
                ob[0] = i++;
                ob[1] = res.getString(1);
                ob[2] = res.getString(2);
                ob[3] = res.getString(3);
                ob[4] = res.getString(4);
                ob[5] = res.getString(5);
                Transaction.tblmodel.addRow(ob);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @Override
    public void ClearTable(showTransaction Transaction) throws SQLException {
        try {
            Transaction.tblmodel.setRowCount(0);
        } catch (Exception e) {
//            System.out.println(e);
        }
    }

}
