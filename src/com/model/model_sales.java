/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import java.sql.*;
import javax.swing.JOptionPane;
import com.controller.controller_sales;
import com.koneksi.UserSession;
import com.koneksi.connect;
import com.view.sales;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import javax.swing.JSpinner;
import com.koneksi.GetTransactionID;
import com.view.detailTransaction;

/**
 *
 * @author LENOVO
 */
public class model_sales implements controller_sales {

    @Override
    public void addcart(sales Sales) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "Insert Into carts (ProductID, Qty) Values (?, ?)";
            PreparedStatement prepare = con.prepareStatement(sql);
            String productID = Sales.comboProductID.getSelectedItem().toString();
            String qty = Sales.spinQtyproduct.getValue().toString();

            if (productID.matches("\\d+") && qty.matches("\\d+")) {
                prepare.setInt(1, Integer.parseInt(productID));
                prepare.setInt(2, Integer.parseInt(qty));
                prepare.executeUpdate();
                JOptionPane.showMessageDialog(null, "Added a new cart");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter only numbers.");
            }
            prepare.close();
            clear(Sales);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            showproduct(Sales);
            showcart(Sales);
            Sales.setLebarKolom1();
            Sales.setLebarKolom2();
        }
    }

    public void removecart(sales Sales) throws SQLException {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete this cart?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                String productId = Sales.txtProductID.getText();
                Connection con = connect.getcon();
                String sql = "DELETE FROM carts WHERE ProductID = ?";
                PreparedStatement prepare = con.prepareStatement(sql);
                prepare.setString(1, productId);
                prepare.executeUpdate();
                prepare.close();
                JOptionPane.showMessageDialog(null, "Cart has been deleted");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                showproduct(Sales);
                showcart(Sales);
                Sales.setLebarKolom1();
                Sales.setLebarKolom2();
            }
        }
    }

    @Override
    public void editcart(sales Sales) throws SQLException {
        try {
            String productId = Sales.txtProductID.getText();
            int newQty = Integer.parseInt(Sales.spinQtycart.getValue().toString());
            Connection con = connect.getcon();
            String sql = "UPDATE carts SET Qty = ? WHERE ProductID = ?";
            PreparedStatement prepare = con.prepareStatement(sql);
            prepare.setInt(1, newQty);
            prepare.setString(2, productId);
            prepare.executeUpdate();
            prepare.close();
            JOptionPane.showMessageDialog(null, "Cart has been updated");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            showproduct(Sales);
            showcart(Sales);
            Sales.setLebarKolom1();
            Sales.setLebarKolom2();
        }
    }

    @Override
    public void clear(sales Sales) throws SQLException {
        Sales.comboProductID.setSelectedIndex(0);
        Sales.comboProductname.setSelectedIndex(0);
        Sales.spinQtyproduct.setValue(1);
        Sales.spinQtycart.setValue(1);
        Sales.txtProductID.setText("");
        Sales.txtPaytotal.setText("");
        Sales.txtProductName.setText("");
        Sales.txtTotal.setText("");
    }

    @Override
    public void clearcart(sales Sales) throws SQLException {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to clear all carts?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                Connection con = connect.getcon();
                String sql = "DELETE FROM carts";
                PreparedStatement prepare = con.prepareStatement(sql);
                prepare.executeUpdate();
                clear(Sales);
                prepare.close();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Carts is empty");
            } finally {
                showproduct(Sales);
                showcart(Sales);
                Sales.setLebarKolom1();
                Sales.setLebarKolom2();
            }
        }
    }

    @Override
    public void checkout(sales Sales) throws SQLException {
        try {
            int paytotal = Integer.parseInt(Sales.txtPaytotal.getText());
            int total = Integer.parseInt(Sales.txtTotal.getText());
            String cashierId = UserSession.getCashierId();
            if (paytotal < total) {
                JOptionPane.showMessageDialog(null, "Pay Total must be greater than Total price");
            } else {
                Connection con = connect.getcon();
                java.util.Date date = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                String query = "INSERT INTO transactions (CashierID, Date, Total, PayTotal) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, cashierId);
                pstmt.setDate(2, sqlDate);
                pstmt.setInt(3, total);
                pstmt.setInt(4, paytotal);
                pstmt.executeUpdate();

                // Dapatkan ID transaksi yang baru saja dihasilkan
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int transactionId = generatedKeys.getInt(1);

                    // Set TransactionID to TransactionData
                    GetTransactionID.setTransactionID(String.valueOf(transactionId));

                    // Ambil semua item di keranjang
                    for (int i = 0; i < Sales.tableCart.getRowCount(); i++) {
                        String itemId = Sales.tableCart.getValueAt(i, 1).toString();
                        int qty = Integer.parseInt(Sales.tableCart.getValueAt(i, 4).toString());
                        int subtotal = Integer.parseInt(Sales.tableCart.getValueAt(i, 3).toString());

                        // Masukkan data ke tabel detailtransactions
                        String detailQuery = "INSERT INTO detailtransactions (TransactionID, ProductID, Qty, Subtotal) VALUES (?, ?, ?, ?)";
                        PreparedStatement detailPstmt = con.prepareStatement(detailQuery);
                        detailPstmt.setInt(1, transactionId);
                        detailPstmt.setString(2, itemId);
                        detailPstmt.setInt(3, qty);
                        detailPstmt.setInt(4, subtotal);
                        detailPstmt.executeUpdate();
                    }

                    // Kosongkan tabel carts
                    String truncateQuery = "TRUNCATE TABLE carts";
                    PreparedStatement truncatePstmt = con.prepareStatement(truncateQuery);
                    truncatePstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Checkout Successful");
                Sales.setVisible(false);
                new detailTransaction().setVisible(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Check the carts or Pay Total input is empty");
        } finally {
            showproduct(Sales);
            showcart(Sales);
            Sales.setLebarKolom1();
            Sales.setLebarKolom2();
        }
    }

    @Override
    public void showproduct(sales Sales) throws SQLException {
        Sales.tablemodel1.getDataVector().removeAllElements();
        Sales.tablemodel1.fireTableDataChanged();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT p.ProductID, c.CategoryType, p.ProductName, p.Price, p.Stock FROM products p "
                    + "INNER JOIN categories c ON p.CategoryID = c.CategoryID "
                    + "LEFT JOIN carts cart ON p.ProductID = cart.ProductID "
                    + "WHERE cart.ProductID IS NULL "
                    + "ORDER BY p.CategoryID ASC";
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
                Sales.tablemodel1.addRow(ob);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void showcart(sales Sales) throws SQLException {
        int total = 0;
        Sales.tablemodel2.getDataVector().removeAllElements();
        Sales.tablemodel2.fireTableDataChanged();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT p.ProductID, p.ProductName, p.Price * c.Qty as TotalPrice, c.Qty FROM products p INNER JOIN carts c ON p.ProductID = c.ProductID ORDER BY p.ProductID ASC";
            ResultSet res = stt.executeQuery(sql);
            int i = 1;
            while (res.next()) {
                Object[] ob = new Object[5];
                ob[0] = i++;
                ob[1] = res.getString(1); // ProductID
                ob[2] = res.getString(2); // ProductName
                ob[3] = res.getString(3); // Subtotal
                ob[4] = res.getString(4); // Qty
                Sales.tablemodel2.addRow(ob);
                total += Integer.parseInt(ob[3].toString());
            }
            Sales.txtTotal.setText(String.valueOf(total));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void clickproduct(sales Sales) throws SQLException {
        try {
            int pilih = Sales.tableProduct.getSelectedRow();
            if (pilih == -1) {
                return;
            }
            Sales.comboProductname.setSelectedItem(Sales.tablemodel1.getValueAt(pilih, 3).toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void clickcart(sales Sales) throws SQLException {
        try {
            int pilih = Sales.tableCart.getSelectedRow();
            if (pilih == -1) {
                return;
            }
            Sales.txtProductID.setText(Sales.tablemodel2.getValueAt(pilih, 1).toString());
            Sales.txtProductName.setText(Sales.tablemodel2.getValueAt(pilih, 2).toString());
            Sales.spinQtycart.setValue(Integer.parseInt(Sales.tablemodel2.getValueAt(pilih, 4).toString()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fillComboProduct(sales Sales) throws SQLException {
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT p.ProductName FROM products p LEFT JOIN carts c ON p.ProductID = c.ProductID WHERE c.ProductID IS NULL ORDER BY p.ProductID ASC";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                Sales.comboProductname.addItem(res.getString("ProductName"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fillComboProductID(sales Sales) throws SQLException {
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT p.ProductID FROM products p LEFT JOIN carts c ON p.ProductID = c.ProductID WHERE c.ProductID IS NULL ORDER BY p.ProductID ASC";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                Sales.comboProductID.addItem(res.getString("ProductID"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setComboItem(sales Sales) throws SQLException {
        HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT ProductID, ProductName FROM products ORDER BY ProductName ASC";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                categoryMap.put(res.getString("ProductName"), res.getInt("ProductID"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        Sales.comboProductname.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                String selectedCategoryType = (String) Sales.comboProductname.getSelectedItem();
                Integer selectedCategoryID = categoryMap.get(selectedCategoryType);
                if (selectedCategoryID != null) {
                    Sales.comboProductID.setSelectedItem(selectedCategoryID.toString());
                }
            }
        });
    }

    @Override
    public void Search(sales Sales) throws SQLException {
        String searchQuery = Sales.txtSearch.getText();
        String query = "SELECT p.ProductID, c.CategoryType, p.ProductName, p.Price, p.Stock "
                + "FROM products p "
                + "INNER JOIN categories c ON p.CategoryID = c.CategoryID "
                + "LEFT JOIN carts cart ON p.ProductID = cart.ProductID "
                + "WHERE (cart.ProductID IS NULL) AND "
                + "(p.ProductName LIKE ? OR c.CategoryType LIKE ? OR p.Price LIKE ? OR p.Stock LIKE ?) "
                + "ORDER BY p.CategoryID ASC";

        Connection con = connect.getcon();
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, "%" + searchQuery + "%");
        preparedStatement.setString(2, "%" + searchQuery + "%");
        preparedStatement.setString(3, "%" + searchQuery + "%");
        preparedStatement.setString(4, "%" + searchQuery + "%");

        ResultSet resultSet = preparedStatement.executeQuery();
        Sales.tablemodel1.getDataVector().removeAllElements();
        Sales.tablemodel1.fireTableDataChanged();
        int i = 1;

        while (resultSet.next()) {
            Object[] row = new Object[6];
            row[0] = i++;
            row[1] = resultSet.getString("ProductID");
            row[2] = resultSet.getString("CategoryType");
            row[3] = resultSet.getString("ProductName");
            row[4] = resultSet.getString("Price");
            row[5] = resultSet.getString("Stock");
            Sales.tablemodel1.addRow(row);
        }
    }

    @Override
    public void ClearTable(sales Sales) throws SQLException {
        try {
            Sales.tablemodel1.setRowCount(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
