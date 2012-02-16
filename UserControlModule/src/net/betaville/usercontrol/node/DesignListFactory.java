/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.usercontrol.node;

import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.ProposalChain;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Skye Book
 */
public class DesignListFactory extends ChildFactory<Design> {
    
    private ProposalChain proposalChain;
    
    public DesignListFactory(ProposalChain proposalChain){
	this.proposalChain = proposalChain;
    }

    @Override
    protected boolean createKeys(List<Design> toPopulate) {
	toPopulate.addAll(proposalChain.getAllVersions());
	return true;
    }

    @Override
    protected Node createNodeForKey(Design design) {
	Node result = new DesignNode(design);
	return result;
    }
}
