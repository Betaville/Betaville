/*
 * Copyright (c) 2011 JME2Loader
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'JME2Loader' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.skyebook.jme2loader;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.jme.math.Vector3f;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 * @author Skye Book
 *
 */
public class TextureConverter {

	private static final Logger logger = Logger.getLogger(TextureConverter.class.getName());

	private static ArrayList<com.jme.image.Image.Format> unsupportedImageFormat;

	static{
		unsupportedImageFormat = new ArrayList<com.jme.image.Image.Format>();
		for(com.jme.image.Image.Format format : com.jme.image.Image.Format.values()){
			try{
				if(Image.Format.valueOf(format.name())==null){
					unsupportedImageFormat.add(format);
				}
			}catch (IllegalArgumentException e) {
				unsupportedImageFormat.add(format);
			}
		}
	}
	
	public static boolean isImageFormatSupported(com.jme.image.Image.Format format){
		return !unsupportedImageFormat.contains(format);
	}

	public static Texture twoToThree(com.jme.image.Texture jme2Texture, AssetManager assetManager){
		
		Texture newTexture = null;

		// If the texture is stored in the file, we need to extract it manually
		if(jme2Texture.isStoreTexture()){
			try{
				if(jme2Texture.getImage()==null){
					logger.warning("WTF, null texture coming from jME2?");
					return null;
				}
			}catch(NullPointerException e){
				logger.warning("WTF, null texture coming from jME2?");
				return null;
			}
			Image jme3Image = twoToThree(jme2Texture.getImage());
			if(jme3Image==null){
				return null;
			}
			if(jme2Texture instanceof com.jme.image.Texture1D){
				System.out.println("1D texture");
				return new Texture2D(jme3Image);
			}
			else if(jme2Texture instanceof com.jme.image.Texture2D){
				return new Texture2D(jme3Image);
			}
			else if(jme2Texture instanceof com.jme.image.Texture3D){
				System.out.println("1D texture");
				newTexture = new Texture2D(jme3Image);
			}
			else{
				System.out.println("Conversion failed");
				return null;
			}
		}
		else{
			File imageLocation = new File(jme2Texture.getImageLocation());
			
			assetManager.registerLocator("binary/", ClasspathLocator.class);
			
			newTexture = assetManager.loadTexture(imageLocation.getName());
		}
		
		
		transferTextureCoordinates(jme2Texture, newTexture);
		return newTexture;
	}

	private static void transferTextureCoordinates(com.jme.image.Texture jme2Texture, Texture jme3Texture){
		Vector3f jme2TextureCoordinates = jme2Texture.getTranslation();
		
		// convert to a jME3 Vector3f, now what?
		com.jme3.math.Vector3f jme3TextureCoordintes = JME2Loader.twoToThree(jme2TextureCoordinates);
	}

	public static Image twoToThree(com.jme.image.Image image){
		//Image.Format.
		if(isImageFormatSupported(image.getFormat())){
			return new Image(Image.Format.valueOf(image.getFormat().name()), image.getWidth(), image.getHeight(), image.getDepth(), image.getData());
		}
		else{
			logger.warning(image.getFormat().name() + " is not supported in jME3!");
			return null;
		}
	}

}
