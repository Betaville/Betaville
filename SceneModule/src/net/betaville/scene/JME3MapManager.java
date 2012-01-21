/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.scene;

import com.jme3.math.Vector3f;
import edu.poly.bxmc.betaville.jme.map.*;

/**
 *
 * @author skyebook
 */
public class JME3MapManager extends MapManager<Vector3f> implements VectorConverter<Vector3f>{
	
	public static final JME3MapManager instance = new JME3MapManager();
	
	@Override
	public Vector3f locationToBetaville(ILocation location) {
		return convertToNativeVector(locationToBetavilleInternal(location));
	}

	@Override
	public UTMCoordinate betavilleToUTM(Vector3f vec) {
		return betavilleToUTMInternal(convertToBetaville(vec));
	}

    @Override
	public BVVec3f convertToBetaville(Vector3f nativeVector) {
		return new BVVec3f(nativeVector.x, nativeVector.y, nativeVector.z);
	}
    
    @Override
	public Vector3f convertToNativeVector(BVVec3f betavilleVector) {
		return new Vector3f(betavilleVector.x, betavilleVector.y, betavilleVector.z);
	}

}
