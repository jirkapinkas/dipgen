package com.dipgen.service.util;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public final class SvgUtilTest {

	@Test
	public void testParseTexts() {
		InputStream inputSvgStream = getClass().getResourceAsStream("/test-texts.svg");
		List<String> texts = SvgUtil.parseTexts(inputSvgStream);
		assertEquals(2, texts.size());
		assertEquals("hello world", texts.get(0));
		assertEquals("another hello", texts.get(1));
	}

	@Test
	public void testNormalizeSvgString() {
		String emptySvg = "<svg \r xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n xmlns:se=\"http://svg-edit.googlecode.com\" \r\n xmlns=\"http://www.w3.org/2000/svg\" overflow=\"visible\" y=\"480\" x=\"640\" height=\"480\" width=\"640\" id=\"svgcontent\">\n\n</svg>";
		String normalizedSvg = SvgUtil.normalizeSvg(emptySvg);
		assertEquals(
				"<svg   xmlns:xlink=\"http://www.w3.org/1999/xlink\"   xmlns:se=\"http://svg-edit.googlecode.com\"   xmlns=\"http://www.w3.org/2000/svg\" overflow=\"visible\" y=\"480\" x=\"640\" height=\"480\" width=\"640\" id=\"svgcontent\"> </svg>",
				normalizedSvg);
	}

	@Test
	public void testNormalizeSvgList() {
		List<String> emptySvg = new ArrayList<String>();
		emptySvg.add("<svg \r xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n ");
		emptySvg.add("xmlns:se=\"http://svg-edit.googlecode.com\" \r\n xmlns=\"http://www.w3.org/2000/svg\" ");
		emptySvg.add("overflow=\"visible\" y=\"480\" x=\"640\" height=\"480\" width=\"640\" id=\"svgcontent\">");
		emptySvg.add("\n\n</svg>");
		String normalizedSvg = SvgUtil.normalizeSvg(emptySvg);
		assertEquals(
				"<svg   xmlns:xlink=\"http://www.w3.org/1999/xlink\"    xmlns:se=\"http://svg-edit.googlecode.com\"   xmlns=\"http://www.w3.org/2000/svg\"  overflow=\"visible\" y=\"480\" x=\"640\" height=\"480\" width=\"640\" id=\"svgcontent\">  </svg> ",
				normalizedSvg);
	}

}
