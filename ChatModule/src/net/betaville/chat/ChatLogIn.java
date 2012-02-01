/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.betaville.chat.api.XMPPMessenger;
import org.jivesoftware.smack.XMPPException;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(category = "Chat",
id = "net.betaville.chat.ChatLogIn")
@ActionRegistration(displayName = "#CTL_ChatLogIn")
@ActionReferences({
    @ActionReference(path = "Menu/Chat", position = 5300)
})
@Messages("CTL_ChatLogIn=Log In to Chat")
public final class ChatLogIn implements ActionListener{

    public void actionPerformed(ActionEvent e) {
        Lookup lookup = WindowManager.getDefault().findTopComponent("SimpleChatWindowTopComponent").getLookup();
        XMPPMessenger messenger = lookup.lookup(XMPPMessenger.class);
        if(!messenger.isLoggedIn()){
            try {
                messenger.login("talk.google.com", "user@server.com", "password");
            } catch (XMPPException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
