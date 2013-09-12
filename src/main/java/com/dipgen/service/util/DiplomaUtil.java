package com.dipgen.service.util;


public class DiplomaUtil {

	public static final int THUMBNAIL_HEIGHT_SMALL = 100;
	public static final int THUMBNAIL_HEIGHT_BIG = 300;

	public static class SvgConversionException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public SvgConversionException(String message) {
			super(message);
		}

		public SvgConversionException(Throwable cause) {
			super(cause);
		}

	}

}
