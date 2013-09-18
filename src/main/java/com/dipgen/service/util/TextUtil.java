package com.dipgen.service.util;

public class TextUtil {

	public static String removeDiak(String retazec) {
		String retazecBD = "";
		retazec = retazec.replace("ž", "z");
		retazec = retazec.replace("Ž", "Z");
		retazec = retazec.replace("ý", "y");
		retazec = retazec.replace("Ý", "Y");
		retazec = retazec.replace("ú", "u");
		retazec = retazec.replace("Ú", "U");
		retazec = retazec.replace("ť", "t");
		retazec = retazec.replace("Ť", "T");
		retazec = retazec.replace("ň", "n");
		retazec = retazec.replace("Ň", "N");
		retazec = retazec.replace("ó", "o");
		retazec = retazec.replace("Ó", "O");
		String sdiak = "ůčšáäčďěéíĺžňóöôŕřšťúüýžźŮČŠÁÄČĎĚÉÍĹŇÓÖÔŔŘÚÜÝŽŐőÖöŰűÜü";
		String bdiak = "ucsaacdeeillnooorrstuuyzzUCSAACDEEILLNOOORRTUUYZOoOoUuUu";
		for (int l = 0; l < retazec.length(); l++) {
			if (sdiak.indexOf(retazec.charAt(l)) != -1)
				retazecBD += bdiak.charAt(sdiak.indexOf(retazec.charAt(l)));
			else
				retazecBD += retazec.charAt(l);
		}
		return retazecBD;
	}

	public static String normalizeText(String string) {
		string = removeDiak(string);
		string = string.replace(" ", "-");
		string = string.replace(".", "-");
		string = string.replace(",", "-");
		string = string.replace("/", "-");
		string = string.replace("\\", "-");
		string = string.replace("?", "-");
		string = string.replace("!", "-");
		string = string.replace("=", "-");
		string = string.replace("+", "-");
		string = string.replace("*", "-");
		string = string.replace(";", "-");
		string = string.replace(":", "-");
		string = string.replace("@", "-");
		string = string.replace("#", "-");
		string = string.replace("$", "-");
		string = string.replace("%", "-");
		string = string.replace("^", "-");
		string = string.replace("&", "-");
		string = string.replace("(", "-");
		string = string.replace(")", "-");
		string = string.replace("{", "-");
		string = string.replace("}", "-");
		string = string.replace("[", "-");
		string = string.replace("]", "-");
		string = string.replace("\"", "-");
		string = string.replace("'", "-");
		string = string.replace("|", "-");
		string = string.replace("<", "-");
		string = string.replace(">", "-");
		string = string.replace("~", "-");
		string = string.replace("-------", "-");
		string = string.replace("------", "-");
		string = string.replace("-----", "-");
		string = string.replace("----", "-");
		string = string.replace("---", "-");
		string = string.replace("--", "-");
		string = string.toLowerCase();
		if (string.endsWith("-")) {
			string = string.substring(0, string.length() - 1);
		}
		string = string.trim();
		return string;
	}

}
