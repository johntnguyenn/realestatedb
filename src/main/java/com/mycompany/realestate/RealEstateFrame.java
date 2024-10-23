
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.realestate;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author john
 */
public class realestateview extends JFrame {
    private JPanel contentPane;
    private JTable table;
    private JTextField textField;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
        public void run() {
            try {
                realestateview frame = new realestateview();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    }
    
    public realestateview() {
        setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
        setTitle("Real Estate Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 682, 361);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(182, 204, 182));
        // contentPane.setBorder(new EmptyBorder(5, 5, 5, 5,));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 70, 520, 217);
        contentPane.add(scrollPane);
        
        table = new JTable();
        table.setModel(new DefaultTableModel(
        new Object[][] {
        },
                new String[] {
                    "Property ID", "Address", "City", "State", "ZIP", "Property Type", "Bed Count", "Bath Count", "Sell Amount"
                }
        ));
        scrollPane.setViewportView(table);
        
        JButton btnNewButton = new JButton("Search");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = textField.getText();
                String searchQuery = "SELECT p.propertyID, p.address, p.city, p.state, p.zip, "
                   + "p.bedcount, p.bathcount, t.sellamount "
                   + "FROM Agent a "
                   + "JOIN Property p ON a.agentID = p.agentID "
                   + "JOIN PropertyType pt ON p.propertyTypeID = pt.propertyTypeID "
                   + "JOIN Transaction t ON p.propertyID = t.propertyID";
                try (Connection con = RealEstateView.getConnection()) {    
                    PreparedStatement pst = con.prepareStatement(searchQuery);
                    ResultSet rs = pst.executeQuery();
                        while(rs.next()) {
                            System.out.println("Data found");
                            String ID = String.valueOf(rs.getInt("propertyID"));
                            String address = rs.getString("address");
                            String city = rs.getString("city");
                            String state = rs.getString("state");
                            String zip = rs.getString("zip");
                            String bedCount = rs.getString("bedcount");
                            String bathCount = rs.getString("bathcount");
                            String sellAmount = rs.getString("sellamount");
                            
                            String tbData[] = {ID, address, city, state, zip, bedCount, bathCount, sellAmount};
                            DefaultTableModel tblModel = (DefaultTableModel)table.getModel();
                            tblModel.addRow(tbData);
                        }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
