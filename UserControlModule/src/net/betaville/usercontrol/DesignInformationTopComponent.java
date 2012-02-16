/* Copyright (c) 2008-2012, Brooklyn eXperimental Media Center
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Brooklyn eXperimental Media Center nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Brooklyn eXperimental Media Center BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.betaville.usercontrol;

import edu.poly.bxmc.betaville.CentralLookup;
import edu.poly.bxmc.betaville.model.Design;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.betaville.usercontrol//DesignInformationTopComponent//EN",
autostore = false)
@TopComponent.Description(preferredID = "DesignInformationTopComponentTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "net.betaville.usercontrol.DesignInformationTopComponentTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_DesignInformationTopComponentAction",
preferredID = "DesignInformationTopComponentTopComponent")
@Messages({
    "CTL_DesignInformationTopComponentAction=Object Information",
    "CTL_DesignInformationTopComponentTopComponent=DesignInformationTopComponent Window",
    "HINT_DesignInformationTopComponentTopComponent=Shows an object's information"
})
public final class DesignInformationTopComponent extends TopComponent implements LookupListener {

    private Lookup.Result<Design> result = null;

    public DesignInformationTopComponent() {
        initComponents();
        setName(Bundle.CTL_DesignInformationTopComponentTopComponent());
        setToolTipText(Bundle.HINT_DesignInformationTopComponentTopComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        descriptionArea = new javax.swing.JTextField();
        addressLabel = new javax.swing.JLabel();
        addressIdentifier = new javax.swing.JLabel();
        urlIdentifier = new javax.swing.JLabel();
        urlLabel = new javax.swing.JLabel();
        userIdentifier = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.nameLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(dateLabel, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.dateLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(userLabel, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.userLabel.text")); // NOI18N

        descriptionArea.setText(org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.descriptionArea.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(addressLabel, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.addressLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(addressIdentifier, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.addressIdentifier.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(urlIdentifier, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.urlIdentifier.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(urlLabel, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.urlLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(userIdentifier, org.openide.util.NbBundle.getMessage(DesignInformationTopComponent.class, "DesignInformationTopComponent.userIdentifier.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionArea)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addressIdentifier)
                                    .addComponent(urlIdentifier))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(urlLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(userIdentifier)
                                .addGap(18, 18, 18)
                                .addComponent(userLabel)))
                        .addGap(0, 32, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(userIdentifier))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descriptionArea, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressIdentifier))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(urlIdentifier)
                    .addComponent(urlLabel))
                .addContainerGap(85, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressIdentifier;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JTextField descriptionArea;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel urlIdentifier;
    private javax.swing.JLabel urlLabel;
    private javax.swing.JLabel userIdentifier;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        result = CentralLookup.getDefault().lookupResult(Design.class);
        result.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
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

    @Override
    public void resultChanged(LookupEvent ev) {
        Design design = CentralLookup.getDefault().lookup(Design.class);
        if (design != null) {
            nameLabel.setText(design.getName());
            dateLabel.setText(design.getDateAdded());
            descriptionArea.setText(design.getDescription());
            addressLabel.setText(design.getAddress());
            urlLabel.setText(design.getURL());
            userLabel.setText(design.getUser());
        }
    }
}
