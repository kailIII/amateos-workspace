/*
 * HodroistAboutBox.java
 */

package org.hodroist.app;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.jibble.simplewebserver.SimpleWebServer;


//Allow connect to MySQL using an SSL connection and, if connect, start the web server and the attention socket
public class HodroistConnectBox extends javax.swing.JDialog {

    public SSLConnection connection;
    public JLabel statusLabel;
    String username;
    String password;
    ArrayList<Track> mp3Collection;

    //Constructor
    public HodroistConnectBox(java.awt.Frame parent, JLabel _statusLabel, ArrayList<Track> _mp3Collection) {
        super(parent);
        initComponents();
        statusLabel = _statusLabel;
        mp3Collection = _mp3Collection;
        getRootPane().setDefaultButton(cancelButton);
    }

    @Action public void closeConnectBox() {
        dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancelButton = new javax.swing.JButton();
        usernameTextField = new javax.swing.JTextField();
        passwordTextField = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setName("aboutBox"); // NOI18N
        setResizable(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.hodroist.app.HodroistApp.class).getContext().getActionMap(HodroistConnectBox.class, this);
        cancelButton.setAction(actionMap.get("closeConnectBox")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.hodroist.app.HodroistApp.class).getContext().getResourceMap(HodroistConnectBox.class);
        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        usernameTextField.setColumns(8);
        usernameTextField.setText(resourceMap.getString("usernameTextField.text")); // NOI18N
        usernameTextField.setName("usernameTextField"); // NOI18N

        passwordTextField.setColumns(8);
        passwordTextField.setText(resourceMap.getString("passwordTextField.text")); // NOI18N
        passwordTextField.setName("passwordTextField"); // NOI18N

        usernameLabel.setText(resourceMap.getString("usernameLabel.text")); // NOI18N
        usernameLabel.setName("usernameLabel"); // NOI18N

        passwordLabel.setText(resourceMap.getString("passwordLabel.text")); // NOI18N
        passwordLabel.setName("passwordLabel"); // NOI18N

        connectButton.setText(resourceMap.getString("connectButton.text")); // NOI18N
        connectButton.setName("connectButton"); // NOI18N
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        disconnectButton.setText(resourceMap.getString("disconnectButton.text")); // NOI18N
        disconnectButton.setName("disconnectButton"); // NOI18N
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(layout.createSequentialGroup()
                        .add(connectButton)
                        .add(6, 6, 6)
                        .add(disconnectButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton))
                    .add(layout.createSequentialGroup()
                        .add(passwordLabel)
                        .add(28, 28, 28)
                        .add(passwordTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(usernameLabel)
                        .add(26, 26, 26)
                        .add(usernameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameLabel)
                    .add(usernameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel)
                    .add(passwordTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(connectButton)
                    .add(disconnectButton)
                    .add(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        // TODO add your handling code here:

        //Get introduced data from GUI
        username = usernameTextField.getText();
        password = passwordTextField.getText();

        //Create an SSL connection
        connection = new SSLConnection();
        String result = "No response obtained from server";

        //Connect to server and send IP to MySQL
        try {
            result = connection.connect(username, password);
            
        } catch (Exception ex) {
            Logger.getLogger(HodroistConnectBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(result.lastIndexOf("Connected")>=0){

                //Set status in green color
                statusLabel.setForeground(Color.getHSBColor((float)0.3, (float)0.63, (float)0.63)); //Green
                statusLabel.setText("Connected");

                //Init WebServer
                initWebServer();

                //Create socket to receive clients petitions
                new ClientConnectionServer(password,mp3Collection).start();

                closeConnectBox();

        }else{
            JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
        }


        
    }//GEN-LAST:event_connectButtonActionPerformed

    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed
        // TODO add your handling code here:
        String result="Can not disconnect";

        try {
            result = connection.disconnect(usernameTextField.getText(), passwordTextField.getText());
        } catch (URISyntaxException ex) {
            Logger.getLogger(HodroistConnectBox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(HodroistConnectBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(result.lastIndexOf("Disconnected")>=0){
            //set status in red color
            statusLabel.setForeground(Color.getHSBColor((float)0, (float)0.8, (float)0.5)); //Red
            statusLabel.setText(result);

            closeConnectBox();

        }else{
            JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_disconnectButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField passwordTextField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables


    //Starts running a web server at port 8088
    public void initWebServer(){

        try {
            SimpleWebServer server = new SimpleWebServer(new File("./Music"), 8088);
        } catch(IOException e) {
            System.out.println("Exception while starting WebServer");
        }
    }
}
