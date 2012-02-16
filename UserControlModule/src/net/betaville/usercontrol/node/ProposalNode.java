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
