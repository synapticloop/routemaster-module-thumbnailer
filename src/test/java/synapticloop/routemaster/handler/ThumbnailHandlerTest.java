package synapticloop.routemaster.handler;

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

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import fi.iki.elonen.NanoHTTPD.Response;
import synapticloop.nanohttpd.handler.ThumbnailHandler;

public class ThumbnailHandlerTest {
	private ThumbnailHandler thumbnailHandler;
	private File rootDir = new File("src/test/resources/");

	@Before
	public void setup() {
		thumbnailHandler = new ThumbnailHandler();
	}

	@Test
	public void testCanServeUri() {
		assertTrue(thumbnailHandler.canServeUri("something.dot.thumbnail"));
		assertFalse(thumbnailHandler.canServeUri("something.dot.something-else"));
	}

	@Test
	public void testServeMissing() {
		Response response = thumbnailHandler.serveFile(rootDir, "/images/missing.jpg.thumbnail", new HashMap<String, String>(), null);
		assertEquals(404, response.getStatus().getRequestStatus());
	}

	@Test
	public void testServe() {
		DummySession dummySession = new DummySession();
		dummySession.addParameter("mode", "width");
		Response response = thumbnailHandler.serveFile(rootDir, "/images/fido.jpg.thumbnail", new HashMap<String, String>(), dummySession);
		assertEquals(200, response.getStatus().getRequestStatus());
	}
}
