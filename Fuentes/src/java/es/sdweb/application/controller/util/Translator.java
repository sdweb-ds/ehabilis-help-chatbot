package es.sdweb.application.controller.util;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

public final class Translator {
	private Translator() {}

	private static MessageResources translator = null;

	private static MessageResources getTranslator() {
		if(translator == null) {
			translator = MessageResources.getMessageResources("i18n");
		}

		return translator;
	}

	public static String getTranslation(String key) {
		String result = getTranslator().getMessage(key);

		if(result == null) {
			result = "???" + key + "???";
		}

		return result;
	}

	public static String getTranslation(Locale locale, String key) {
		String result = getTranslator().getMessage(locale, key);

		if(result == null) {
			result = "???" + key + "???";
		}

		return result;
	}

	public static String getTranslation(String key, Object... params) {
		String result = getTranslator().getMessage(key, params);

		if(result == null) {
			result = "???" + key + "???";
		}

		return result;
	}

	public static String getTranslation(Locale locale, String key, Object... params) {
		String result = getTranslator().getMessage(locale, key, params);

		if(result == null) {
			result = "???" + key + "???";
		}

		return result;
	}
}
