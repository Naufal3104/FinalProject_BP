/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import com.controller.controller_category;
import com.koneksi.connect;
import com.view.categoryCRUD;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author LENOVO
 */
public class model_category implements controller_category {

    @Override
    public void Show(categoryCRUD Category) throws SQLException {
        Category.tblmodel.getDataVector().removeAllElements();
        Category.tblmodel.fireTableDataChanged();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT CategoryID, CategoryType FROM categories ORDER BY CategoryID ASC";
            ResultSet res = stt.executeQuery(sql);
            int i = 1;
            while (res.next()) {
                Object[] ob = new Object[5];
                ob[0] = i++;
                ob[1] = res.getString(1);
                ob[2] = res.getString(2);
                Category.tblmodel.addRow(ob);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void Add(categoryCRUD Category) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "Insert Into categories (CategoryType) Values (?)";
            PreparedStatement prepare = con.prepareStatement(sql);
            String CategoryName = Category.txtCategoryName.getText();
            prepare.setString(1, CategoryName);
            if (CategoryName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please input an valid value");
            } else {
                prepare.executeUpdate();
                JOptionPane.showMessageDialog(null, "Added a new category");
                prepare.close();
                Clear(Category);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Show(Category);
            Category.setLebarKolom();
        }
    }

    @Override
    public void Edit(categoryCRUD Category) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "UPDATE categories SET CategoryType = ? WHERE CategoryID = ?";
            PreparedStatement prepare = con.prepareStatement(sql);
            prepare.setString(1, Category.txtCategoryName.getText());
            prepare.setString(2, Category.txtID.getText());
            if (Category.txtCategoryName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please input an valid value");
            } else {
                prepare.executeUpdate();
                prepare.close();
                JOptionPane.showMessageDialog(null, "Category has been updated");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Show(Category);
            Category.setLebarKolom();
        }
    }

    @Override
    public void Delete(categoryCRUD Category) throws SQLException {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete this category?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                Connection con = connect.getcon();
                String sql = "DELETE FROM categories WHERE CategoryID = ?";
                PreparedStatement prepare = con.prepareStatement(sql);
                prepare.setString(1, Category.txtID.getText());
                prepare.executeUpdate();
                prepare.close();
                JOptionPane.showMessageDialog(null, "Category has been deleted");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                Clear(Category);
                Show(Category);
                Category.setLebarKolom();
            }
        }
    }

    @Override
    public void Clear(categoryCRUD Category) throws SQLException {
        Category.txtCategoryName.setText("");
        Category.txtID.setText("");
    }

    @Override
    public void ClickTable(categoryCRUD Category) throws SQLException {
        try {
            int pilih = Category.tableCategory.getSelectedRow();
            if (pilih == -1) {
                return;
            }
            Category.txtID.setText(Category.tableCategory.getValueAt(pilih, 1).toString());
            Category.txtCategoryName.setText(Category.tableCategory.getValueAt(pilih, 2).toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
