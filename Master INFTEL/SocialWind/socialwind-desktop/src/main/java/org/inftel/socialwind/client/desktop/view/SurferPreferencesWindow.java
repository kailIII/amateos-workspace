package org.inftel.socialwind.client.desktop.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.inftel.socialwind.client.desktop.model.SurferPreferences;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;

@SuppressWarnings("serial")
public class SurferPreferencesWindow extends JFrame {

    private BindingGroup m_bindingGroup;
    private JPanel m_contentPane;
    private SurferPreferences surferPreferencesModel;
    private JTextField displayNameJTextField;
    private JTextField fullNameJTextField;
    private JPasswordField passwordJPasswordField;
    private JCheckBox savePasswordJCheckBox;
    private JTextField userNameJTextField;
    private JButton btnConnect;
    private JLabel fullNameLabel;
    private JLabel lblStatus;
    private JLabel lblTitle;
    private JButton btnExit;
    private JPanel panel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SurferPreferencesWindow frame = new SurferPreferencesWindow(
                            new SurferPreferences());
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
    public SurferPreferencesWindow(SurferPreferences surferPreferencesModel) {
        setFont(null);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                SurferPreferencesWindow.class.getResource("socialwind-success.png")));
        setTitle(Messages.getString("appTitle")); //$NON-NLS-1$
        this.surferPreferencesModel = surferPreferencesModel;
        setBounds(100, 100, 320, 314);
        m_contentPane = new JPanel();
        m_contentPane.setAlignmentY(0.0f);
        m_contentPane.setAlignmentX(0.0f);
        setContentPane(m_contentPane);
        m_contentPane.setLayout(null);

        JLabel userNameLabel = new JLabel(Messages.getString("userName")); //$NON-NLS-1$
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        userNameLabel.setBounds(10, 53, 88, 14);
        m_contentPane.add(userNameLabel);

        userNameJTextField = new JTextField();
        userNameJTextField.setBounds(107, 49, 197, 20);
        m_contentPane.add(userNameJTextField);
        userNameJTextField.setColumns(10);

        JLabel passwordLabel = new JLabel(Messages.getString("password")); //$NON-NLS-1$
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordLabel.setBounds(10, 84, 88, 14);
        m_contentPane.add(passwordLabel);

        passwordJPasswordField = new JPasswordField();
        passwordJPasswordField.setBounds(107, 80, 197, 20);
        m_contentPane.add(passwordJPasswordField);

        JLabel savePasswordLabel = new JLabel(Messages.getString("savePassword")); //$NON-NLS-1$
        savePasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        savePasswordLabel.setBounds(10, 116, 88, 14);
        m_contentPane.add(savePasswordLabel);

        btnConnect = new JButton(Messages.getString("connect")); //$NON-NLS-1$
        btnConnect.setBounds(219, 109, 85, 23);
        m_contentPane.add(btnConnect);

        fullNameLabel = new JLabel(Messages.getString("fullName")); //$NON-NLS-1$
        fullNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        fullNameLabel.setBounds(10, 149, 88, 14);
        m_contentPane.add(fullNameLabel);

        fullNameJTextField = new JTextField();
        fullNameJTextField.setBounds(107, 143, 197, 20);
        m_contentPane.add(fullNameJTextField);

        JLabel displayNameLabel = new JLabel(Messages.getString("displayName")); //$NON-NLS-1$
        displayNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayNameLabel.setBounds(10, 180, 88, 14);
        m_contentPane.add(displayNameLabel);

        displayNameJTextField = new JTextField();
        displayNameJTextField.setBounds(107, 174, 197, 20);
        m_contentPane.add(displayNameJTextField);

        panel = new JPanel();
        panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
                Messages.getString("conectionStatus"), TitledBorder.LEADING, TitledBorder.TOP, null,
                new Color(0, 0, 0)));
        panel.setBounds(10, 205, 294, 38);
        m_contentPane.add(panel);
        panel.setLayout(null);

        lblStatus = new JLabel(Messages.getString("status")); //$NON-NLS-1$
        lblStatus.setVerticalAlignment(SwingConstants.BOTTOM);
        lblStatus.setBounds(10, 11, 274, 20);
        panel.add(lblStatus);

        lblTitle = new JLabel(Messages.getString("SurferPreferencesWindow.lblTitle.text")); //$NON-NLS-1$
        lblTitle.setAlignmentY(0.0f);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(Color.WHITE);
        lblTitle.setIcon(new ImageIcon(SurferPreferencesWindow.class.getResource("header.png")));
        lblTitle.setBounds(0, 0, 320, 38);
        m_contentPane.add(lblTitle);

        savePasswordJCheckBox = new JCheckBox();
        savePasswordJCheckBox.setBounds(107, 107, 21, 27);
        m_contentPane.add(savePasswordJCheckBox);

        btnExit = new JButton(Messages.getString("exit")); //$NON-NLS-1$
        btnExit.setBounds(215, 254, 89, 23);
        m_contentPane.add(btnExit);

        if (surferPreferencesModel != null) {
            m_bindingGroup = initDataBindings();
        }
    }

    public SurferPreferences getSurferPreferencesModel() {
        return surferPreferencesModel;
    }

    public void setSurferPreferencesModel(SurferPreferences newSurferPreferencesModel) {
        setSurferPreferencesModel(newSurferPreferencesModel, true);
    }

    public void setSurferPreferencesModel(SurferPreferences newSurferPreferencesModel,
            boolean update) {
        surferPreferencesModel = newSurferPreferencesModel;
        if (update) {
            if (m_bindingGroup != null) {
                m_bindingGroup.unbind();
                m_bindingGroup = null;
            }
            if (surferPreferencesModel != null) {
                m_bindingGroup = initDataBindings();
            }
        }
    }

    protected BindingGroup initDataBindings() {
        BeanProperty<SurferPreferences, String> displayNameProperty = BeanProperty
                .create("displayName");
        BeanProperty<JTextField, String> textProperty = BeanProperty.create("text");
        AutoBinding<SurferPreferences, String, JTextField, String> autoBinding = Bindings
                .createAutoBinding(UpdateStrategy.READ_WRITE, surferPreferencesModel,
                        displayNameProperty, displayNameJTextField, textProperty);
        autoBinding.bind();
        //
        BeanProperty<SurferPreferences, String> fullNameProperty = BeanProperty.create("fullName");
        BeanProperty<JTextField, String> textProperty_1 = BeanProperty.create("text");
        AutoBinding<SurferPreferences, String, JTextField, String> autoBinding_1 = Bindings
                .createAutoBinding(UpdateStrategy.READ_WRITE, surferPreferencesModel,
                        fullNameProperty, fullNameJTextField, textProperty_1);
        autoBinding_1.bind();
        //
        BeanProperty<SurferPreferences, String> passwordProperty = BeanProperty.create("password");
        BeanProperty<JPasswordField, String> textProperty_2 = BeanProperty.create("text");
        AutoBinding<SurferPreferences, String, JPasswordField, String> autoBinding_2 = Bindings
                .createAutoBinding(UpdateStrategy.READ_WRITE, surferPreferencesModel,
                        passwordProperty, passwordJPasswordField, textProperty_2);
        autoBinding_2.bind();
        //
        BeanProperty<SurferPreferences, Boolean> savePasswordProperty = BeanProperty
                .create("savePassword");
        BeanProperty<JCheckBox, Boolean> selectedProperty = BeanProperty.create("selected");
        AutoBinding<SurferPreferences, Boolean, JCheckBox, Boolean> autoBinding_3 = Bindings
                .createAutoBinding(UpdateStrategy.READ_WRITE, surferPreferencesModel,
                        savePasswordProperty, savePasswordJCheckBox, selectedProperty);
        autoBinding_3.bind();
        //
        BeanProperty<SurferPreferences, String> userNameProperty = BeanProperty.create("userName");
        BeanProperty<JTextField, String> textProperty_3 = BeanProperty.create("text");
        AutoBinding<SurferPreferences, String, JTextField, String> autoBinding_4 = Bindings
                .createAutoBinding(UpdateStrategy.READ_WRITE, surferPreferencesModel,
                        userNameProperty, userNameJTextField, textProperty_3);
        autoBinding_4.bind();
        //
        BeanProperty<SurferPreferences, Boolean> surferPreferencesModelBeanProperty = BeanProperty
                .create("connected");
        BeanProperty<JTextField, Boolean> jTextFieldBeanProperty = BeanProperty.create("enabled");
        AutoBinding<SurferPreferences, Boolean, JTextField, Boolean> autoBinding_5 = Bindings
                .createAutoBinding(UpdateStrategy.READ, surferPreferencesModel,
                        surferPreferencesModelBeanProperty, fullNameJTextField,
                        jTextFieldBeanProperty);
        autoBinding_5.bind();
        //
        AutoBinding<SurferPreferences, Boolean, JTextField, Boolean> autoBinding_6 = Bindings
                .createAutoBinding(UpdateStrategy.READ, surferPreferencesModel,
                        surferPreferencesModelBeanProperty, displayNameJTextField,
                        jTextFieldBeanProperty);
        autoBinding_6.bind();
        //
        BeanProperty<SurferPreferences, String> surferPreferencesBeanProperty = BeanProperty
                .create("status");
        BeanProperty<JLabel, String> jLabelBeanProperty = BeanProperty.create("text");
        AutoBinding<SurferPreferences, String, JLabel, String> autoBinding_7 = Bindings
                .createAutoBinding(UpdateStrategy.READ_WRITE, surferPreferencesModel,
                        surferPreferencesBeanProperty, lblStatus, jLabelBeanProperty);
        autoBinding_7.bind();
        //
        BindingGroup bindingGroup = new BindingGroup();
        //
        bindingGroup.addBinding(autoBinding);
        bindingGroup.addBinding(autoBinding_1);
        bindingGroup.addBinding(autoBinding_2);
        bindingGroup.addBinding(autoBinding_3);
        bindingGroup.addBinding(autoBinding_4);
        bindingGroup.addBinding(autoBinding_5);
        bindingGroup.addBinding(autoBinding_6);
        bindingGroup.addBinding(autoBinding_7);
        return bindingGroup;
    }

    public JButton getBtnConnect() {
        return btnConnect;
    }

    public JButton getBtnExit() {
        return btnExit;
    }
}
