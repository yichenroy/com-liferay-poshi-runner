/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.poshi.runner.util;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Michael Hashimoto
 */
public class JSONCurlUtil {

	public static String get(String requestString)
		throws IOException, TimeoutException {

		Request request = new Request(requestString, "GET");

		return request.send();
	}

	public static String get(String requestString, String jsonPath)
		throws IOException, TimeoutException {

		Request request = new Request(requestString, "GET");

		return _getParsedResponse(request, jsonPath);
	}

	public static String post(String requestString)
		throws IOException, TimeoutException {

		Request request = new Request(requestString, "POST");

		return request.send();
	}

	public static String post(String requestString, String jsonPath)
		throws IOException, TimeoutException {

		Request request = new Request(requestString, "POST");

		return _getParsedResponse(request, jsonPath);
	}

	protected Request getRequest(String requestString, String requestMethod) {
		return new Request(requestString, requestMethod);
	}

	private static String _getParsedResponse(Request request, String jsonPath)
		throws IOException, TimeoutException {

		String response = request.send();

		DocumentContext documentContext = JsonPath.parse(response);

		Object object = documentContext.read(jsonPath);

		if (object == null) {
			throw new IOException(
				"Invalid JSON path " + jsonPath + " in " + response);
		}

		return object.toString();
	}

	private static class Request {

		public Request(String requestString, String requestMethod) {
			_requestMethod = requestMethod;

			requestString = requestString.replaceAll("\\s+\\\\?\\s*\\n", "\n");

			requestString = _encodeCurlData(requestString);

			List<String> tokens = _tokenize(requestString.trim());

			_requestURL = _getRequestURL(tokens);

			tokens.remove(_requestURL);

			_setRequestOptions(tokens);
		}

		public String send() throws IOException, TimeoutException {
			StringBuilder sb = new StringBuilder();

			sb.append("curl -X ");
			sb.append(_requestMethod);
			sb.append(" ");
			sb.append(_getRequestOptionsString());
			sb.append(" ");
			sb.append(_requestURL);

			Process process = ExecUtil.executeCommands(sb.toString());

			InputStream inputStream = process.getInputStream();

			inputStream.mark(Integer.MAX_VALUE);

			String response = ExecUtil.readInputStream(inputStream);

			System.out.println("Response: " + response);

			inputStream.reset();

			if (process.exitValue() != 0) {
				inputStream = process.getErrorStream();

				inputStream.mark(Integer.MAX_VALUE);

				System.out.println(
					"Error stream: " + ExecUtil.readInputStream(inputStream));

				inputStream.reset();

				throw new RuntimeException(
					"Command finished with exit value: " + process.exitValue());
			}

			return response;
		}

		private String _encodeCurlData(String requestString) {
			Matcher matcher = _escapePattern.matcher(requestString);

			String encodedRequestString = requestString;

			while (matcher.find()) {
				String key = "$CURLDATA:" + StringUtil.randomString("10");

				_curlDataMap.put(key, matcher.group(1));

				encodedRequestString = encodedRequestString.replace(
					matcher.group(0), key);
			}

			return encodedRequestString;
		}

		private String _escapeToken(String token) {
			if (token == null) {
				return null;
			}

			return token.replaceAll("\\\\\"", "\"");
		}

		private String _formatToken(String token) {
			if ((token.startsWith("'") && token.endsWith("'")) ||
				(token.startsWith("\"") && token.endsWith("\""))) {

				token = token.substring(1, token.length() - 1);
			}

			return _escapeToken(token);
		}

		private String _getRequestOptionsString() {
			StringBuilder sb = new StringBuilder();

			for (Map.Entry<String, List<String>> requestOption :
					_requestOptions.entrySet()) {

				List<String> optionValues = requestOption.getValue();

				if (optionValues.isEmpty()) {
					sb.append(requestOption.getKey());
				}
				else {
					for (String optionValue : optionValues) {
						sb.append(requestOption.getKey());
						sb.append(" \"");
						sb.append(_unEscapeToken(optionValue));
						sb.append("\" ");
					}
				}
			}

			return sb.toString();
		}

		private String _getRequestURL(List<String> tokens) {
			int requestURLIndex = -1;

			for (int i = 0; i < tokens.size(); i++) {
				String token = tokens.get(i);

				if (token.startsWith("http")) {
					if (requestURLIndex != -1) {
						StringBuilder sb = new StringBuilder();

						sb.append("Found 2 URLs when only 1 is allowed:\n");
						sb.append(tokens.get(requestURLIndex));
						sb.append("\n");
						sb.append(token);

						throw new IllegalArgumentException(sb.toString());
					}

					requestURLIndex = i;
				}
			}

			if (requestURLIndex == -1) {
				throw new IllegalArgumentException("No URL found in statement");
			}

			return tokens.get(requestURLIndex);
		}

		private void _setRequestOptions(List<String> tokens) {
			for (int i = 0; i < tokens.size(); i++) {
				String optionType = _formatToken(tokens.get(i));

				Matcher optionTypeMatcher = _requestPattern.matcher(optionType);

				if (optionTypeMatcher.matches()) {
					String optionValue = "";

					if (i < (tokens.size() - 1)) {
						String nextToken = tokens.get(i + 1);

						optionTypeMatcher = _requestPattern.matcher(nextToken);

						if (!optionTypeMatcher.matches()) {
							if (nextToken.matches("\\$CURLDATA:\\w{10}")) {
								nextToken = _curlDataMap.get(nextToken);
							}

							optionValue = _formatToken(nextToken);

							i++;
						}
					}

					_validateRequestOption(optionType, optionValue);

					if (_customOptionsMap.containsKey(optionType)) {
						optionType = _customOptionsMap.get(optionType);
					}

					List<String> optionValues = new ArrayList<>();

					if (_requestOptions.containsKey(optionType)) {
						optionValues = _requestOptions.get(optionType);
					}

					optionValues.add(optionValue);

					_requestOptions.put(optionType, optionValues);
				}
			}
		}

		private List<String> _tokenize(String requestString) {
			List<String> tokens = new ArrayList<>();

			Matcher matcher = _requestPattern.matcher(requestString);

			int end = -1;
			int start = -1;

			while (matcher.find()) {
				if (!tokens.isEmpty()) {
					end = matcher.start();

					if ((end - start) > 1) {
						String miscellaneousToken = requestString.substring(
							start, end);

						tokens.add(miscellaneousToken.trim());
					}
				}

				String token = matcher.group(1);

				tokens.add(token.trim());

				start = matcher.end();
			}

			if (start != requestString.length()) {
				tokens.add(requestString.substring(start));
			}

			return tokens;
		}

		private String _unEscapeToken(String token) {
			if (token == null) {
				return null;
			}

			token = token.replaceAll("(?<!\\\\)\"", "\\\\\"");

			return token;
		}

		private void _validateRequestOption(
			String optionType, String optionValue) {

			if (optionType.equals("--json-data")) {
				try {
					new JSONObject(optionValue);
				}
				catch (JSONException jsone) {
					throw new RuntimeException(
						"Invalid JSON: '" + optionValue + "'");
				}
			}
		}

		private static Map<String, String> _customOptionsMap = new HashMap<>();
		private static Pattern _escapePattern = Pattern.compile(
			"<CURL_DATA\\[(.*?)\\]CURL_DATA>");
		private static Pattern _requestPattern = Pattern.compile(
			"(-[\\w#:\\.]|--[\\w#:\\.-]{2,}|https?:[^\\s]+)(\\s+|\\Z)");

		static {
			_customOptionsMap.put("--json-data", "--data");
		}

		private Map<String, String> _curlDataMap = new HashMap<>();
		private final String _requestMethod;
		private Map<String, List<String>> _requestOptions = new HashMap<>();
		private final String _requestURL;

	}

}