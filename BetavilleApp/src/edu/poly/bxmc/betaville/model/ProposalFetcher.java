/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.poly.bxmc.betaville.model;

import edu.poly.bxmc.betaville.CentralLookup;
import edu.poly.bxmc.betaville.net.InsecureClientManager;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Skye Book
 */
public class ProposalFetcher {
    
    public static List<ProposalChain> fetchProposals(int cityID) throws UnknownHostException, IOException{
	InsecureClientManager icm = new InsecureClientManager(null, CentralLookup.getDefault().lookup(ClientSession.class).getServer());
	List<Design> proposalRoots = icm.findAllProposalsInCity(cityID);
	
	List<ProposalChain> chains = new ArrayList<ProposalChain>();
	
	for(Design proposalRoot : proposalRoots){
	    // Get the versions of the proposal
	    List<Design> versions = icm.getVersionsOfProposal(proposalRoot.getCityID());
	    
	    // Create and populate a proposal chain
	    ProposalChain chain = new ProposalChain(proposalRoot);
	    for(Design version : versions){
		chain.addVersion(version);
	    }
	    
	    chains.add(chain);
	}
	
	return chains;
    }
}
