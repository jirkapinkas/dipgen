package com.dipgen.service.util;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import eu.medsea.util.MimeUtil;

public final class ImageUtilTest {

	@Test
	public void testGetDemoImage() throws IOException {
		InputStream demoImageSmile = ImageUtil.getDemoImage("smile.jpg");
		File fileSmile = new File("target/smile.jpg");
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileSmile));
		IOUtils.copy(demoImageSmile, outputStream);
		outputStream.close();
		assertEquals("image/jpeg", MimeUtil.getMagicMimeType(fileSmile));

		InputStream errorImage = ImageUtil.getDemoImage("../META-INF/persistence.xml");
		assertNull(errorImage);
	}

	private NodeList getImagesFromSvg(String inputSvg) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(IOUtils.toInputStream(inputSvg));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = xpath.compile("//image");
		NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return nodeList;
	}

	@Test
	public void testEmbeedImages() throws Exception {
		InputStream inputStream = getClass().getResourceAsStream("/diploma1.svg");
		String inputSvg = IOUtils.toString(inputStream);
		assertEquals(2, getImagesFromSvg(inputSvg).getLength());
		inputSvg = inputSvg.replace("{IMAGES-URL}", "classpath:/images/");
		String svgEmbeddedImages = ImageUtil.embeddImages(IOUtils.toInputStream(inputSvg));
		NodeList imagesFromSvg = getImagesFromSvg(svgEmbeddedImages);
		assertEquals(2, imagesFromSvg.getLength());
		for (int i = 0; i < imagesFromSvg.getLength(); i++) {
			NamedNodeMap attributes = imagesFromSvg.item(i).getAttributes();
			String base64ImageString = attributes.getNamedItem("xlink:href").getTextContent();
			byte[] image = Base64.decodeBase64(base64ImageString.replace("data:image/png;base64,", ""));

			File outputFile = new File("target/diploma1-" + i + ".image");
			FileUtils.writeByteArrayToFile(outputFile, image);
			assertEquals("image/png", MimeUtil.getMagicMimeType(outputFile));
		}
	}

	@Test
	public void testGenerateJpegThumbnail() throws IOException {
		byte[] jpegThumbnail = ImageUtil.generateJpegThumbnail(getClass().getResourceAsStream("/test-texts.svg"), 100);
		File outputFile = new File("target/test-texts.jpeg");
		FileUtils.writeByteArrayToFile(outputFile, jpegThumbnail);
		assertEquals("image/jpeg", MimeUtil.getMagicMimeType(outputFile));
	}

}
