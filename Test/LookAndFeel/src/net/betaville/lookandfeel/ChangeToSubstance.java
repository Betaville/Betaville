/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.lookandfeel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

@ActionID(category = "View",
id = "net.betaville.lookandfeel.ChangeToSubstance")
@ActionRegistration(displayName = "#CTL_ChangeToSubstance")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0)
})
@Messages("CTL_ChangeToSubstance=Change to Substance")
public final class ChangeToSubstance implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
            //UIManager.setLookAndFeel(new SynthLookAndFeel());
            System.out.println("success");
        } catch (UnsupportedLookAndFeelException ex) {
            Exceptions.printStackTrace(ex);
            System.out.println("failure");
        }
    }
}
