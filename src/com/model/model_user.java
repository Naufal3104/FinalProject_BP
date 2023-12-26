/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.model;

import com.controller.controller_user;
import com.koneksi.connect;
import com.view.Users;
import java.sql.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.swing.JOptionPane;

/**
 *
 * @author LENOVO
 */
public class model_user implements controller_user {

    @Override
    public void Save(Users user) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "INSERT INTO users (Name, UserEmail, username, password, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.txtName.getText());
            pstmt.setString(2, user.txtEmail.getText());
            pstmt.setString(3, user.txtUsername.getText());

            // Memasukkan password tanpa enkripsi
            pstmt.setString(4, user.txtPassword.getText());

            pstmt.setInt(5, user.comboRole.getSelectedIndex() + 1);
            if (user.txtName.getText() == null || user.txtEmail.getText() == null || user.txtUsername.getText() == null || user.txtPassword.getText() == null) {
                JOptionPane.showMessageDialog(null, "Please input an valid value");
            } else {
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Added a new user");
                New(user);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Show(user);
            user.setLebarKolom();
        }
    }

    @Override
    public void Edit(Users user) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "UPDATE users SET Name = ?, UserEmail = ?, username = ?, password = ?, role = ? WHERE UserID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.txtName.getText());
            pstmt.setString(2, user.txtEmail.getText());
            pstmt.setString(3, user.txtUsername.getText());
            pstmt.setString(4, user.txtPassword.getText());
            pstmt.setInt(5, user.comboRole.getSelectedIndex() + 1);
            pstmt.setString(6, user.txtID.getText());

            if (user.txtName.getText() == null || user.txtEmail.getText() == null || user.txtUsername.getText() == null || user.txtPassword.getText() == null) {
                JOptionPane.showMessageDialog(null, "Please input a valid value");
            } else {
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "User has been updated");
                New(user);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Show(user);
            user.setLebarKolom();
        }
    }

    @Override
    public void Delete(Users user) throws SQLException {
        try {
            Connection con = connect.getcon();
            String sql = "DELETE FROM users WHERE UserID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.txtID.getText());

            if (user.txtID.getText() == null) {
                JOptionPane.showMessageDialog(null, "Please input a valid UserID");
            } else {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure to delete this cart?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "User has been deleted");
                    New(user);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Show(user);
            user.setLebarKolom();
        }
    }

    @Override
    public void Show(Users user) throws SQLException {
        user.tblmodel.getDataVector().removeAllElements();
        user.tblmodel.fireTableDataChanged();
        try {
            Connection con = connect.getcon();
            Statement stt = con.createStatement();
            String sql = "SELECT UserID, Name, UserEmail, username, password, role FROM users ORDER BY role ASC";
            ResultSet res = stt.executeQuery(sql);
            int i = 1;
            String key = "rahasia123456789"; // Ganti dengan kunci rahasia Anda
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            while (res.next()) {
                Object[] ob = new Object[7];
                ob[0] = i++;
                ob[1] = res.getString(1);
                ob[2] = res.getString(2);
                ob[3] = res.getString(3);
                ob[4] = res.getString(4);
                // Menggunakan AES untuk enkripsi password
                byte[] encrypted = cipher.doFinal(res.getString(5).getBytes(StandardCharsets.UTF_8));
                ob[5] = Base64.getEncoder().encodeToString(encrypted);
                ob[6] = res.getString(6);
                user.tblmodel.addRow(ob);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void New(Users user) throws SQLException {
        user.txtEmail.setText("");
        user.txtName.setText("");
        user.txtUsername.setText("");
        user.txtPassword.setText("");
        user.comboRole.setSelectedIndex(0);
    }

    @Override
    public void ClickTable(Users user) throws SQLException {
        try {
            int pilih = user.userTable.getSelectedRow();
            if (pilih == -1) {
                return;
            }
            user.txtID.setText(user.tblmodel.getValueAt(pilih, 1).toString());
            user.txtName.setText(user.tblmodel.getValueAt(pilih, 2).toString());
            user.txtEmail.setText(user.tblmodel.getValueAt(pilih, 3).toString());
            user.txtUsername.setText(user.tblmodel.getValueAt(pilih, 4).toString());

            // Mengatur nilai txtPassword
            String encryptedPassword = user.tblmodel.getValueAt(pilih, 5).toString();
            String key = "rahasia123456789"; // Ganti dengan kunci rahasia Anda
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            String decryptedPassword = new String(decrypted, StandardCharsets.UTF_8);
            user.txtPassword.setText(decryptedPassword);

            // Mengatur nilai comboRole
            String role = user.tblmodel.getValueAt(pilih, 6).toString();
            switch (role) {
                case "1":
                    user.comboRole.setSelectedItem("Admin");
                    break;
                case "2":
                    user.comboRole.setSelectedItem("Cashier");
                    break;
                case "3":
                    user.comboRole.setSelectedItem("Manager");
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
