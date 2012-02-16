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
	
	System.out.println(proposalRoots.size() + " proposal roots retrieved");
	
	List<ProposalChain> chains = new ArrayList<ProposalChain>();
	
	for(Design proposalRoot : proposalRoots){
	    // Get the versions of the proposal
	    List<Design> versions = icm.getVersionsOfProposal(proposalRoot.getID());
	    
	    // Create and populate a proposal chain
	    ProposalChain chain = new ProposalChain(proposalRoot);
	    for(Design version : versions){
		chain.addVersion(version);
	    }
	    
	    if(chain.getAllVersions().size()>0){
		System.out.println(chain.getProposalRoot().getName() + " has " + chain.getAllVersions().size() + " versions");
	    }
	    
	    chains.add(chain);
	}
	
	return chains;
    }
}
