package org.inftel.socialwind.client.desktop.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class PopupMessages extends JFrame {

    private JPanel contentPane;

    private Rectangle desktopBounds;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PopupMessages frame = new PopupMessages("Un buen titulo",
                            new Date().toString(), "Pues hay puede ir mucha info...");
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
    public PopupMessages(String title, String date, String description) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                PopupMessages.this.setVisible(false);
            }
        });
        setFocusableWindowState(false);
        setFocusable(false);
        setAlwaysOnTop(true);
        desktopBounds = getDesktopBounds();
        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 350, 116);
        setLocation(desktopBounds.width - this.getWidth() - 10,
                desktopBounds.height - this.getHeight() - 10);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblLogo = new JLabel("logo");
        lblLogo.setIcon(new ImageIcon(PopupMessages.class.getResource("logo.png")));
        lblLogo.setBounds(10, 11, 94, 94);
        contentPane.add(lblLogo);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(lblTitle.getFont().deriveFont(lblTitle.getFont().getStyle() | Font.BOLD));
        lblTitle.setBounds(114, 11, 226, 14);
        contentPane.add(lblTitle);

        JLabel lblDescription = new JLabel("<html>" + description);
        lblDescription.setVerticalTextPosition(SwingConstants.TOP);
        lblDescription.setVerticalAlignment(SwingConstants.TOP);
        lblDescription.setBounds(114, 61, 226, 44);
        contentPane.add(lblDescription);

        JLabel lblDate = new JLabel(date);
        lblDate.setBounds(114, 36, 226, 14);
        contentPane.add(lblDate);
    }

    Rectangle getDesktopBounds() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return env.getMaximumWindowBounds();
    }
}
