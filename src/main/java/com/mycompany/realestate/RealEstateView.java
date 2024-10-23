package com.mycompany.realestate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RealEstateView extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    public RealEstateView() {
        setTitle("Real Estate Database Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 400);
        setLayout(new BorderLayout());

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JPanel topPanel = new JPanel();
        topPanel.add(searchField);
        topPanel.add(searchButton);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(searchField.getText());
            }
        });

        initializeTable();
    }

    private void initializeTable() {
        tableModel.addColumn("Property ID");
        tableModel.addColumn("Address");
        tableModel.addColumn("City");
        tableModel.addColumn("State");
        tableModel.addColumn("ZIP");
        tableModel.addColumn("Property Type");
        tableModel.addColumn("Bed Count");
        tableModel.addColumn("Bath Count");
        tableModel.addColumn("Sell Amount");
    }

    private void performSearch(String searchQuery) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/realestatedb", "root", "fcgdbadmin");
             PreparedStatement pst = conn.prepareStatement(
                     "SELECT * FROM agent WHERE first_name LIKE ?")) {
            pst.setString(1, "%" + searchQuery + "%");
            ResultSet rs = pst.executeQuery();

            // Clear existing data
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("propertyID"),
                    rs.getString("address"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("zip"),
                    rs.getString("propertyType"),
                    rs.getInt("bedCount"),
                    rs.getInt("bathCount"),
                    rs.getFloat("sellAmount")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            RealEstateView frame = new RealEstateView();
            frame.setVisible(true);
        });
    }
}
