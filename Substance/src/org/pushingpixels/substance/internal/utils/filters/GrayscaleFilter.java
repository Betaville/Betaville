/*
 * Copyright (c) 2005-2010 Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.substance.internal.utils.filters;

import java.awt.image.BufferedImage;

import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

/**
 * @author Kirill Grouchnikov
 */
public class GrayscaleFilter extends AbstractFilter {
	/**
	 * @throws IllegalArgumentException
	 *             if <code>scheme</code> is null
	 */
	public GrayscaleFilter() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dst) {
		if (dst == null) {
			dst = createCompatibleDestImage(src, null);
		}

		int width = src.getWidth();
		int height = src.getHeight();

		int[] pixels = new int[width * height];
		getPixels(src, 0, 0, width, height, pixels);
		grayScaleColor(pixels);
		setPixels(dst, 0, 0, width, height, pixels);

		return dst;
	}

	private void grayScaleColor(int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			int argb = pixels[i];
			int brightness = SubstanceColorUtilities.getColorBrightness(argb);
			pixels[i] = (argb & 0xFF000000) | brightness << 16
					| brightness << 8 | brightness;
		}
	}
}
