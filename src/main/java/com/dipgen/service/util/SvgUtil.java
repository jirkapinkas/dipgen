package com.dipgen.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dipgen.service.util.DiplomaUtil.SvgConversionException;

public class SvgUtil {

	/**
	 * Normalize SVG - strip all EOL (End of Line) characters from input String
	 * (\r\n, \n, \r)
	 * 
	 * @param svg
	 *            Input SVG String
	 * @return Output SVG String
	 */
	public static String normalizeSvg(String svg) {
		return svg.replaceAll("[\r\n]+", " ").replaceAll("[\n]+", " ").replaceAll("[\r]+", " ");
	}

	/**
	 * Normalize SVG - convert String array to single String and strip all EOL
	 * characters using normalizeSvg(String) method.
	 * 
	 * @param svg
	 *            Input SVG array
	 * @return Output SVG String
	 */
	public static String normalizeSvg(String[] svg) {
		StringBuilder outputSvg = new StringBuilder();
		for (String line : svg) {
			outputSvg.append(normalizeSvg(line));
			outputSvg.append(" ");
		}
		return outputSvg.toString();
	}

	/**
	 * Normalize SVG - convert List of Strings to single String and strip all
	 * EOL characters using normalizeSvg(String) method.
	 * 
	 * @param svg
	 *            Input SVG (List of String)
	 * @return Output SVG String
	 */
	public static String normalizeSvg(List<String> svgList) {
		String[] array = svgList.toArray(new String[svgList.size()]);
		return normalizeSvg(array);
	}

	/**
	 * Parse input SVG and return all texts (contents of tag &lt;text&gt;)
	 * 
	 * @param svg
	 *            Input SVG String
	 * @return List of all texts
	 */
	public static List<String> parseTexts(String svg) {
		try {
			return parseTexts(IOUtils.toInputStream(svg, "UTF-8"));
		} catch (IOException ex) {
			throw new SvgConversionException(ex);
		}
	}

	/**
	 * Parse input SVG and return all texts (contents of tag &lt;text&gt;)
	 * 
	 * @param svg
	 *            Input SVG Input Stream
	 * @return List of all texts
	 */
	public static List<String> parseTexts(InputStream svg) {
		try {
			ArrayList<String> result = new ArrayList<String>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(svg);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//text");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				result.add(nodeList.item(i).getTextContent());
			}
			return result;
		} catch (SAXException e) {
			throw new SvgConversionException(e);
		} catch (IOException e) {
			throw new SvgConversionException(e);
		} catch (XPathExpressionException e) {
			throw new SvgConversionException(e);
		} catch (ParserConfigurationException e) {
			throw new SvgConversionException(e);
		}
	}

}
