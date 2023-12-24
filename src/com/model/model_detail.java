/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import com.controller.controller_detail;
import com.koneksi.GetTransactionID;
import com.koneksi.connect;
import com.view.detailTransaction;
import java.sql.*;

/**
 *
 * @author LENOVO
 */
public class model_detail implements controller_detail {

    @Override
    public void ShowTable(detailTransaction Detail) throws SQLException {
        Detail.tablemodel.getDataVector().removeAllElements();
        Detail.tablemodel.fireTableDataChanged();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String transactionID = GetTransactionID.getTransactionID();
            String sql = "SELECT p.ProductName, d.Qty, d.Subtotal, c.CashierName, t.Date, t.Total, t.PayTotal "
                    + "FROM detailtransactions d "
                    + "INNER JOIN products p ON d.ProductID = p.ProductID "
                    + "INNER JOIN transactions t ON d.TransactionID = t.TransactionID "
                    + "INNER JOIN cashiers c ON t.CashierID = c.CashierID "
                    + "WHERE t.TransactionID = '" + transactionID + "'";
            ResultSet res = stt.executeQuery(sql);
            int i = 1;
            while (res.next()) {
                Object[] ob = new Object[4];
                ob[0] = i++;
                ob[1] = res.getString(1);
                ob[2] = res.getString(2);
                ob[3] = res.getString(3);
                Detail.tablemodel.addRow(ob);

                Detail.labelCashier.setText(res.getString(4));
                Detail.txtDate.setText(res.getString(5));
                Detail.txtTotal.setText(res.getString(6));
                Detail.txtPaytotal.setText(res.getString(7));
                int total = Integer.parseInt(Detail.txtTotal.getText());
                int payTotal = Integer.parseInt(Detail.txtPaytotal.getText());
                int change = payTotal - total;
                Detail.txtChange.setText(String.valueOf(change));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
