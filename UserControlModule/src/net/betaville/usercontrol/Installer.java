/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.usercontrol;

import net.betaville.usercontrol.lookup.CentralLookup;
import net.betaville.usercontrol.lookup.UserStateManager;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        CentralLookup.getDefault().add(new UserStateManager());
    }
}
