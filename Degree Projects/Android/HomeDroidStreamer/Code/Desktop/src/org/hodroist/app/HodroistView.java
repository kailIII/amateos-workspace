/*
 * HodroistView.java
 */

package org.hodroist.app;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 * The application's main frame.
 */
public class HodroistView extends FrameView {

    public static ArrayList<Track> mp3Collection;
    DefaultTableModel tableModel;

    public HodroistView(SingleFrameApplication app) {
        super(app);

        //Create table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Artist");
        tableModel.addColumn("Title");
        tableModel.addColumn("Album");
        tableModel.addColumn("Year");

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });



        //Obtain mp3 collection and show items on GUI
        mp3Collection = new Collection().scanFiles();
        showCollection();

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = HodroistApp.getApplication().getMainFrame();
            aboutBox = new HodroistAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        HodroistApp.getApplication().show(aboutBox);
    }

    @Action
    public void showConnectBox(){
        if (connectionBox == null) {
            JFrame mainFrame = HodroistApp.getApplication().getMainFrame();
            connectionBox = new HodroistConnectBox(mainFrame,statusMessageLabel,mp3Collection);
            connectionBox.setLocationRelativeTo(mainFrame);
        }
        HodroistApp.getApplication().show(connectionBox);
    }


    //Show mp3 files info on GUI
    public void showCollection(){

        for (int i=0; i<mp3Collection.size(); i++){
            Track track = (Track) mp3Collection.get(i);
            addRowToTable(track.artist,track.title,track.album,track.year);
        }

    }


    //Add a new mp3 file to table on GUI
    public void addRowToTable(String artist,String title, String album, String year){
        tableModel.insertRow(tableModel.getRowCount(), new Object[]{artist,title,album,year});
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tracksTableScrollPane = new javax.swing.JScrollPane();
        tracksTable = new javax.swing.JTable();
        viewLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        connectionMenu = new javax.swing.JMenu();
        connectionMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.hodroist.app.HodroistApp.class).getContext().getResourceMap(HodroistView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 161));

        tracksTableScrollPane.setName("tracksTableScrollPane"); // NOI18N

        tracksTable.setBackground(resourceMap.getColor("tracksTable.background")); // NOI18N
        tracksTable.setModel(tableModel);
        tracksTable.setName("tracksTable"); // NOI18N
        tracksTable.getTableHeader().setReorderingAllowed(false);
        tracksTableScrollPane.setViewportView(tracksTable);
        tracksTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tracksTable.columnModel.title0")); // NOI18N
        tracksTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tracksTable.columnModel.title1")); // NOI18N
        tracksTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tracksTable.columnModel.title2")); // NOI18N
        tracksTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("tracksTable.columnModel.title3")); // NOI18N

        viewLabel.setFont(resourceMap.getFont("viewLabel.font")); // NOI18N
        viewLabel.setForeground(resourceMap.getColor("viewLabel.foreground")); // NOI18N
        viewLabel.setText(resourceMap.getString("viewLabel.text")); // NOI18N
        viewLabel.setName("viewLabel"); // NOI18N

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(viewLabel)
                .addContainerGap(676, Short.MAX_VALUE))
            .add(tracksTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(viewLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tracksTableScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.hodroist.app.HodroistApp.class).getContext().getActionMap(HodroistView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        connectionMenu.setText(resourceMap.getString("connectionMenu.text")); // NOI18N
        connectionMenu.setName("connectionMenu"); // NOI18N

        connectionMenuItem.setAction(actionMap.get("showConnectBox")); // NOI18N
        connectionMenuItem.setText(resourceMap.getString("connectionMenuItem.text")); // NOI18N
        connectionMenuItem.setName("connectionMenuItem"); // NOI18N
        connectionMenu.add(connectionMenuItem);

        menuBar.add(connectionMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setBackground(resourceMap.getColor("statusPanel.background")); // NOI18N
        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(800, 60));

        statusPanelSeparator.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("statusPanelSeparator.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("statusPanelSeparator.border.titleFont"))); // NOI18N
        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setFont(resourceMap.getFont("statusMessageLabel.font")); // NOI18N
        statusMessageLabel.setForeground(resourceMap.getColor("statusMessageLabel.foreground")); // NOI18N
        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 655, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(statusAnimationLabel)
                .addContainerGap())
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(statusPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(statusMessageLabel)
                            .add(statusAnimationLabel))
                        .add(114, 114, 114))
                    .add(statusPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu connectionMenu;
    private javax.swing.JMenuItem connectionMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    public javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTable tracksTable;
    private javax.swing.JScrollPane tracksTableScrollPane;
    private javax.swing.JLabel viewLabel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private JDialog connectionBox;

}
