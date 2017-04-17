package synapticloop.nanohttpd.handler;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import synapticloop.nanohttpd.utils.HttpUtils;

/*
 * Copyright (c) 2017 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

public class ThumbnailHandler extends Handler {
	private static final String THUMBNAIL_POSTFIX = ".thumbnail";
	private static final Map<String, String> MIME_EXTENSION = new HashMap<String, String>();
	static {
		MIME_EXTENSION.put("image/gif", "gif");
		MIME_EXTENSION.put("image/jpeg", "jpg");
		MIME_EXTENSION.put("image/png", "png");
	}

	@Override
	public boolean canServeUri(String uri) {
		return(uri.endsWith(THUMBNAIL_POSTFIX));
	}

	@Override
	public Response serveFile(File rootDir, String uri, Map<String, String> headers, IHTTPSession session) {
		String mimeType = getMimeType(uri);

		if(null == mimeType) {
			mimeType = "text/plain";
		}

		String filename = uri.substring(0, uri.lastIndexOf("."));
		String extension = MIME_EXTENSION.get(mimeType);

		File rootFile = new File(rootDir.getAbsolutePath() + filename);
		if(!rootFile.exists()) {
			return(HttpUtils.notFoundResponse());
		}

		if(!rootFile.canRead()) {
			return(HttpUtils.forbiddenResponse());
		}

		Mode scaleMode = getScaleMode(session);

		// at this point we have the image - we need to figure out what to do with it
		try {
			BufferedImage original = ImageIO.read(rootFile);
			int height = getPercentage(session, "ph", original.getHeight());
			int width =  getPercentage(session, "pw", original.getWidth());

			height = getSize(session, "h", height);
			width =  getSize(session, "w", width);

			BufferedImage resize = Scalr.resize(original, 
					Method.BALANCED, 
					scaleMode, 
					width, 
					height);
			original.flush();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resize, extension, baos);
			byte[] byteArray = baos.toByteArray();
			resize.flush();
			InputStream is = new ByteArrayInputStream(byteArray);
			return(HttpUtils.okResponse(mimeType, is, byteArray.length));
		} catch (IllegalArgumentException | ImagingOpException | IOException ex) {
			return(HttpUtils.internalServerErrorResponse(ex.getMessage()));
		}
	}

	private int getPercentage(IHTTPSession session, String key, int defaultSize) {
		String parameter = getParameter(session, key);
		if(null == parameter) {
			return(defaultSize);
		}

		try {
			return((int)((Integer.parseInt(parameter)/100.0f) * defaultSize));
		} catch(NumberFormatException ex) {
			return(defaultSize);
		}
	}

	private int getSize(IHTTPSession session, String key, int defaultSize) {
		String parameter = getParameter(session, key);
		if(null == parameter) {
			return(defaultSize);
		}

		try {
			return(Integer.parseInt(parameter));
		} catch(NumberFormatException ex) {
			return(defaultSize);
		}
	}

	private Mode getScaleMode(IHTTPSession session) {
		Mode scaleMode = Mode.AUTOMATIC;
		String modeParamater = getParameter(session, "mode");
		if(null != modeParamater) {
			switch (modeParamater) {
			case "width":
				scaleMode = Mode.FIT_TO_WIDTH;
				break;
			case "height":
				scaleMode = Mode.FIT_TO_HEIGHT;
				break;
			case "auto":
				scaleMode = Mode.AUTOMATIC;
				break;
			case "exact":
				scaleMode = Mode.FIT_EXACT;
				break;
			default:
				// do nothing
				break;
			}
		}
		return(scaleMode);
	}

	private String getParameter(IHTTPSession session, String key) {
		Map<String, List<String>> parameters = session.getParameters();
		if(null == parameters) {
			return(null);
		}

		List<String> list = parameters.get(key);
		if(null == list || null == list.get(0)) {
			return(null);
		}

		return(list.get(0));
	}
}
