package org.inftel.socialwind.client.desktop;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JLabel;

public class PopulateServerUtility extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PopulateServerUtility frame = new PopulateServerUtility();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public PopulateServerUtility() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 508, 499);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblName = new JLabel("name");
        lblName.setBounds(10, 11, 84, 14);
        contentPane.add(lblName);
        
        JLabel lblDescription = new JLabel("description");
        lblDescription.setBounds(10, 36, 84, 14);
        contentPane.add(lblDescription);
    }
}
