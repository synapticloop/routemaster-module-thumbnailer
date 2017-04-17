package synapticloop.routemaster.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD.CookieHandler;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Method;
import fi.iki.elonen.NanoHTTPD.ResponseException;

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

public class DummySession implements IHTTPSession {
	Map<String, List<String>> parameterMap = new HashMap<String, List<String>>();

	public void addParameter(String key, String... values) {
		parameterMap.put(key, Arrays.asList(values));
	}

	public void resetParameters() {
		parameterMap.clear();
	}

	public DummySession() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public CookieHandler getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getParms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getParameters() {
		return(parameterMap);
	}

	@Override
	public String getQueryParameterString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parseBody(Map<String, String> files) throws IOException, ResponseException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRemoteIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHostName() {
		// TODO Auto-generated method stub
		return null;
	}

}
