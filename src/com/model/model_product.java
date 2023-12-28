/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import java.sql.*;
import javax.swing.JOptionPane;
import com.controller.controller_product;
import com.koneksi.connect;
import com.view.productCRUD;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

/**
 *
 * @author LENOVO
 */
public class model_product implements controller_product {

    @Override
    public void Save(productCRUD product) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "Insert Into products (CategoryID, ProductName, Price, Stock) Values (?, ?, ?, ?)";
            PreparedStatement prepare = con.prepareStatement(sql);
            if (product.txtProductname.getText().isEmpty() || product.spinPrice.getValue().toString().isEmpty() || product.spinStock.getValue().toString().isEmpty() || product.comboCategoryID.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Make sure any value needed is not empty");
            } else {
                if (product.comboCategoryID.getSelectedItem() != null) {
                    int categoryID = Integer.parseInt(product.comboCategoryID.getSelectedItem().toString());
                    prepare.setInt(1, categoryID);
                }
                prepare.setString(2, product.txtProductname.getText());
                prepare.setInt(3, Integer.parseInt(product.spinPrice.getValue().toString()));
                prepare.setInt(4, Integer.parseInt(product.spinStock.getValue().toString()));
                prepare.executeUpdate();
                JOptionPane.showMessageDialog(null, "Product has been added");
                prepare.close();
                New(product);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please input an valid value");
        } finally {
            Show(product);
            product.setLebarKolom();
        }
    }

    @Override
    public void Edit(productCRUD product) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "UPDATE products SET CategoryID=?, ProductName=?, Price=?, Stock=? WHERE ProductID=?";
            PreparedStatement prepare = con.prepareStatement(sql);
            if (product.txtProductname.getText().isEmpty() || product.spinPrice.getValue().toString().isEmpty() || product.spinStock.getValue().toString().isEmpty() || product.comboCategoryID.getSelectedItem() == null || product.txtProductID.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Make sure any value needed is not empty");
            } else {
                if (product.comboCategoryID.getSelectedItem() != null) {
                    int categoryID = Integer.parseInt(product.comboCategoryID.getSelectedItem().toString());
                    prepare.setInt(1, categoryID);
                }
                prepare.setString(2, product.txtProductname.getText());
                prepare.setString(3, product.spinPrice.getValue().toString());
                prepare.setString(4, product.spinStock.getValue().toString());
                prepare.setString(5, product.txtProductID.getText());

                prepare.executeUpdate();
                JOptionPane.showMessageDialog(null, "Product has been edited");
                prepare.close();
                New(product);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please input an valid value");
        } finally {
            Show(product);
            product.setLebarKolom();
        }
    }

    @Override
    public void Delete(productCRUD product) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "DELETE FROM products WHERE ProductID =?";
            PreparedStatement prepare = con.prepareStatement(sql);
            if (product.txtProductID.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Select an product first");
            } else {
                prepare.setString(1, product.txtProductID.getText());
                prepare.executeUpdate();
                JOptionPane.showMessageDialog(null, "Product has been deleted");
                prepare.close();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please input an valid value");
        } finally {
            Show(product);
            product.setLebarKolom();
        }
    }

    @Override
    public void Show(productCRUD product) throws SQLException {
        product.tblmodel.getDataVector().removeAllElements();
        product.tblmodel.fireTableDataChanged();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT p.ProductID, c.CategoryType, p.ProductName, p.Price, p.Stock FROM products p INNER JOIN categories c ON p.CategoryID = c.CategoryID ORDER BY p.CategoryID ASC";
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
                product.tblmodel.addRow(ob);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void New(productCRUD product) throws SQLException {
        product.txtProductID.setText("");
        product.txtProductname.setText("");
        product.spinPrice.setValue(0);
        product.spinStock.setValue(0);
        product.comboCategory.setSelectedIndex(0);
    }

    @Override
    public void ClickTable(productCRUD product) throws SQLException {
        try {
            int pilih = product.table.getSelectedRow();
            if (pilih == -1) {
                return;
            }
            product.txtProductID.setText(product.tblmodel.getValueAt(pilih, 1).toString());
            product.comboCategory.setSelectedItem(product.tblmodel.getValueAt(pilih, 2).toString()); // This line has been changed
            product.txtProductname.setText(product.tblmodel.getValueAt(pilih, 3).toString());
            product.spinPrice.setValue(Integer.parseInt(product.tblmodel.getValueAt(pilih, 4).toString()));
            product.spinStock.setValue(Integer.parseInt(product.tblmodel.getValueAt(pilih, 5).toString()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fillComboCategory(productCRUD product) throws SQLException {
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT CategoryType FROM categories ORDER BY CategoryID ASC";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                product.comboCategory.addItem(res.getString("CategoryType"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fillComboCategoryID(productCRUD product) throws SQLException {
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT CategoryID FROM categories ORDER BY CategoryID ASC";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                product.comboCategoryID.addItem(res.getString("CategoryID"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void setComboItem(productCRUD product) throws SQLException {
        HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT CategoryID, CategoryType FROM categories ORDER BY CategoryType ASC";
            ResultSet res = stt.executeQuery(sql);
            while (res.next()) {
                categoryMap.put(res.getString("CategoryType"), res.getInt("CategoryID"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        product.comboCategory.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                String selectedCategoryType = (String) product.comboCategory.getSelectedItem();
                Integer selectedCategoryID = categoryMap.get(selectedCategoryType);
                if (selectedCategoryID != null) {
                    product.comboCategoryID.setSelectedItem(selectedCategoryID.toString());
                }
            }
        });
    }

}
