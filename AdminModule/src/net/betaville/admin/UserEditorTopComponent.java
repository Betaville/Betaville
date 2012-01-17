/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.admin;

import edu.poly.bxmc.betaville.model.IUser.UserType;
import edu.poly.bxmc.betaville.net.SecureClientManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.betaville.admin//UserEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "UserEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.betaville.admin.UserEditorTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_UserEditorAction",
preferredID = "UserEditorTopComponent")
@Messages({
    "CTL_UserEditorAction=UserEditor",
    "CTL_UserEditorTopComponent=UserEditor Window",
    "HINT_UserEditorTopComponent=This is a UserEditor window"
})
public final class UserEditorTopComponent extends TopComponent {
    
    private String userToEdit = "sbook";
    private String server = "localhost";
    
    private UserType originalUserType = UserType.MODERATOR;
    private String originalEmail = "skye.book@gmail.com";
    private String originalWebsite = "http://skyebook.net";
    private String originalBio = "My Biography.";
    
    private UserType newUserType;
    private String newEmail;
    private String newWebsite;
    private String newBio;
    
    
    
    private SecureClientManager scm;

    public UserEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_UserEditorTopComponent());
        setToolTipText(Bundle.HINT_UserEditorTopComponent());
        
        userTypeComboBox.removeAll();
        for(UserType type : UserType.values()){
            userTypeComboBox.addItem(type);
        }
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        topPanel = new javax.swing.JPanel();
        userNameLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        bottomPanel = new javax.swing.JPanel();
        saveChangesButton = new javax.swing.JButton();
        revertChangesButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        changePasswordLabel = new javax.swing.JLabel();
        changePasswordSubmitButton = new javax.swing.JButton();
        userTypeComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        updateEmailLabel = new javax.swing.JLabel();
        updateWebsiteLabel = new javax.swing.JLabel();
        updateEmailField = new javax.swing.JTextField();
        updateWebsiteField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        updateBioArea = new javax.swing.JTextArea();
        newPasswordEntryField1 = new javax.swing.JPasswordField();
        newPasswordEntryField2 = new javax.swing.JPasswordField();

        jTextField1.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.jTextField1.text")); // NOI18N

        jTextField2.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.jTextField2.text")); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(userNameLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.userNameLabel.text")); // NOI18N

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addComponent(userNameLabel)
                        .addGap(0, 692, Short.MAX_VALUE))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(topPanel, java.awt.BorderLayout.PAGE_START);

        org.openide.awt.Mnemonics.setLocalizedText(saveChangesButton, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.saveChangesButton.text")); // NOI18N
        saveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(revertChangesButton, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.revertChangesButton.text")); // NOI18N
        revertChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertChangesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveChangesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 562, Short.MAX_VALUE)
                .addComponent(revertChangesButton)
                .addContainerGap())
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(revertChangesButton)
                    .addComponent(saveChangesButton))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        org.openide.awt.Mnemonics.setLocalizedText(changePasswordLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.changePasswordLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(changePasswordSubmitButton, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.changePasswordSubmitButton.text")); // NOI18N
        changePasswordSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordSubmitButtonActionPerformed(evt);
            }
        });

        userTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        userTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userTypeComboBoxActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(updateEmailLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.updateEmailLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(updateWebsiteLabel, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.updateWebsiteLabel.text")); // NOI18N

        updateEmailField.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.updateEmailField.text")); // NOI18N
        updateEmailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateEmailFieldActionPerformed(evt);
            }
        });

        updateWebsiteField.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.updateWebsiteField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.jLabel3.text")); // NOI18N

        updateBioArea.setRows(5);
        jScrollPane1.setViewportView(updateBioArea);

        newPasswordEntryField1.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.newPasswordEntryField1.text")); // NOI18N

        newPasswordEntryField2.setText(org.openide.util.NbBundle.getMessage(UserEditorTopComponent.class, "UserEditorTopComponent.newPasswordEntryField2.text")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(changePasswordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(newPasswordEntryField1)
                            .addComponent(newPasswordEntryField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(userTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator1)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(changePasswordSubmitButton)
                            .addComponent(jLabel2)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(updateEmailLabel)
                                    .addComponent(updateWebsiteLabel)
                                    .addComponent(jLabel3))
                                .addGap(47, 47, 47)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(updateWebsiteField, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                                    .addComponent(updateEmailField)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 487, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changePasswordLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newPasswordEntryField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPasswordEntryField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changePasswordSubmitButton)
                .addGap(72, 72, 72)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateEmailLabel)
                    .addComponent(updateEmailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateWebsiteLabel)
                    .addComponent(updateWebsiteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void changePasswordSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordSubmitButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_changePasswordSubmitButtonActionPerformed

    private void userTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userTypeComboBoxActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void updateEmailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateEmailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateEmailFieldActionPerformed

    private void revertChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revertChangesButtonActionPerformed
        updateEmailField.setText(originalEmail);
        updateWebsiteField.setText(originalWebsite);
        updateBioArea.setText(originalBio);
        userTypeComboBox.setSelectedItem(originalUserType);
    }//GEN-LAST:event_revertChangesButtonActionPerformed

    private void saveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesButtonActionPerformed
        newEmail = updateEmailField.getText();
        newWebsite = updateWebsiteField.getText();
        newBio = updateBioArea.getText();
        newUserType = (UserType)userTypeComboBox.getSelectedItem();
    }//GEN-LAST:event_saveChangesButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel changePasswordLabel;
    private javax.swing.JButton changePasswordSubmitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField newPasswordEntryField1;
    private javax.swing.JPasswordField newPasswordEntryField2;
    private javax.swing.JButton revertChangesButton;
    private javax.swing.JButton saveChangesButton;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextArea updateBioArea;
    private javax.swing.JTextField updateEmailField;
    private javax.swing.JLabel updateEmailLabel;
    private javax.swing.JTextField updateWebsiteField;
    private javax.swing.JLabel updateWebsiteLabel;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JComboBox userTypeComboBox;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        System.setProperty("betaville.server", server);
        scm = new SecureClientManager(null, true);
        
        updateEmailField.setText(originalEmail);
        updateWebsiteField.setText(originalWebsite);
        updateBioArea.setText(originalBio);
        userTypeComboBox.setSelectedItem(originalUserType);
        
    }

    @Override
    public void componentClosed() {
        if(scm.isAlive()){
            scm.close();
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
