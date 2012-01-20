/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.lookandfeel;

import com.jtattoo.plaf.noire.NoireLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "View",
id = "net.betaville.lookandfeel.ChangeLookAndFeelToJTattoo")
@ActionRegistration(displayName = "#CTL_ChangeLookAndFeelToJTattoo")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 0, separatorAfter = 50)
})
@Messages("CTL_ChangeLookAndFeelToJTattoo=Change Look and Feel to JTattoo")
public final class ChangeLookAndFeelToJTattoo implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(NoireLookAndFeel.class.getName());
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
