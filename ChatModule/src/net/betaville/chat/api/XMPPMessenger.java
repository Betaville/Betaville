/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.chat.api;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

/**
 *
 * @author Skye Book
 */
public class XMPPMessenger {

    private Connection connection;
    private boolean loggedIn = false;

    public XMPPMessenger() {
    }

    public void login(String server, String user, String password) throws XMPPException {
        if (!isLoggedIn()) {
            ConnectionConfiguration config = new ConnectionConfiguration(server, 5222, "gmail.com");
            connection = new XMPPConnection(config);
            connection.connect();
            connection.login(user, password);
            loggedIn = true;
        }
    }

    public void logout() {
        if (isLoggedIn()) {
            connection.disconnect();
            loggedIn = false;
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void sendMessage(String recipient, String message, MessageListener messageListener) throws XMPPException {
        Chat chat = connection.getChatManager().createChat(recipient, messageListener);
        chat.sendMessage(message);
    }
}
