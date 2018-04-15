package org.wso2.carbon.esb.connector.oauth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Json {
	// Encuentra valores simples "key":"value"
	private static final Pattern KEY_STRING_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*\"([^\"]*)\"");
	// Encuentra valores simples "key":123
	private static final Pattern KEY_NUMBER_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*(\\d+)");
	// Encuentra valores simples "key":["value1","value2"]
	private static final Pattern KEY_ARRAY_PATTERN = Pattern.compile("\"(\\w+)\"\\s*:\\s*\\[(.*)]", Pattern.DOTALL);
	private static final Pattern STRING_PATTERN = Pattern.compile("\"([^\"]*)\"");

	private final Map<String, Object> keyValues = new HashMap<String, Object>();
	private final String document;

	public Json(String document) {
		this.document = document;
		parse();
	}

	private void parse() {
		Matcher stringMatcher = KEY_STRING_PATTERN.matcher(document);
		while (stringMatcher.find()) {
			keyValues.put(stringMatcher.group(1), stringMatcher.group(2));
		}

		Matcher numberMatcher = KEY_NUMBER_PATTERN.matcher(document);
		while (numberMatcher.find()) {
			keyValues.put(numberMatcher.group(1), Integer.valueOf(numberMatcher.group(2)));
		}

		Matcher arrayMatcher = KEY_ARRAY_PATTERN.matcher(document);
		while (arrayMatcher.find()) {
			String key = arrayMatcher.group(1);
			String arrayValues = arrayMatcher.group(2);

			List<String> values = new ArrayList<String>();
			Matcher stringValueMatcher = STRING_PATTERN.matcher(arrayValues);
			while (stringValueMatcher.find()) {
				values.add(stringValueMatcher.group(1));
			}
			keyValues.put(key, values);
		}
	}

	public String getString(String key) throws JsonParseException {
		return (String) assertExpectedReturnType(key, String.class);
	}

	public Integer getInteger(String key) throws JsonParseException {
		return (Integer) assertExpectedReturnType(key, Integer.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> getArray(String key) throws JsonParseException {
		return (List<String>) assertExpectedReturnType(key, List.class);
	}

	@Override
	public String toString() {
		return document;
	}

	private Object assertExpectedReturnType(String key, Class<?> clas) throws JsonParseException {
		if (!keyValues.containsKey(key)) {
			throw new JsonParseException("No existe el campo: " + key);
		}

		Object value = keyValues.get(key);
		if (!(clas.isAssignableFrom(value.getClass()))) {
			throw new JsonParseException("El valor del campo: " + key + " no es del tipo esperado: " + clas);
		}

		return value;
	}
}
