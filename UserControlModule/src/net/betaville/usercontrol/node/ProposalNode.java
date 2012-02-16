/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.usercontrol.node;

import edu.poly.bxmc.betaville.model.ProposalChain;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Skye Book
 */
public class ProposalNode extends AbstractNode {

    private ProposalChain proposalChain;

    public ProposalNode(ProposalChain proposalChain) {
	super(Children.create(new DesignListFactory(proposalChain), true), Lookups.singleton(proposalChain));
	this.proposalChain = proposalChain;

	setDisplayName(proposalChain.getProposalRoot().getName());
    }

    public ProposalChain getProposalChain() {
	return proposalChain;
    }

    @Override
    public Action[] getActions(boolean popup) {
	return new Action[]{new InfoAction()};
    }

    public class InfoAction extends AbstractAction {

	public InfoAction() {
	    putValue(NAME, "Info");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    ProposalChain chain = getLookup().lookup(ProposalChain.class);
	    JOptionPane.showMessageDialog(null, chain.getProposalRoot().getName() + " with "
		    + chain.getAllVersions().size() + " children");
	}
    }
}
