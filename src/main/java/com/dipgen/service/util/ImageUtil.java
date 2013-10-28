package com.dipgen.service.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dipgen.service.util.DiplomaUtil.SvgConversionException;

public class ImageUtil {

	public static double SCALE_RATIO = 1.5;

	/**
	 * Read demo images from classpath:/images. Won't allow reading files from
	 * other directories.
	 * 
	 * @param name
	 *            Image name
	 * @return Image Input Stream
	 */
	public static InputStream getDemoImage(String name) {
		InputStream inputStream = ImageUtil.class.getResourceAsStream("/images/" + name.replace("/", "").replace("\\", ""));
		return inputStream;
	}

	/**
	 * Scale image to width and height
	 * 
	 * @param image
	 *            Input image
	 * @param width
	 *            scaled image's width
	 * @param height
	 *            scaled image's height
	 * @return Output scaled image
	 */
	private static BufferedImage getScaledImage(BufferedImage image, int width, int height) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
	}

	/**
	 * Scale image to width and height
	 * 
	 * @param inputStream
	 *            Input Stream with image
	 * @param width
	 *            scaled image's width
	 * @param height
	 *            scaled image's height
	 * @return Output scaled image in png format
	 * @throws IOException
	 */
	private static byte[] scaleImage(InputStream inputStream, int width, int height) throws IOException {
		BufferedImage originalImage = ImageIO.read(inputStream);
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();
		BufferedImage scaledImage = null;
		if (originalWidth < (width * SCALE_RATIO) || originalHeight < (height * SCALE_RATIO)) {
			// return original image, don't scale it, it would be waste of
			// resources and image quality
			scaledImage = originalImage;
		} else {
			scaledImage = getScaledImage(originalImage, (int) (width * SCALE_RATIO), (int) (height * SCALE_RATIO));
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		boolean written = ImageIO.write(scaledImage, "png", outputStream);
		if (!written) {
			throw new SvgConversionException("could not scale image");
		}
		return outputStream.toByteArray();
	}

	/**
	 * Embeed images to SVG
	 * 
	 * @param inputStream
	 *            Input stream containing SVG file. Images URL with prefix
	 *            classpath: or http: wil be embedded inside CSV using Base64
	 * @return SVG file containing embedded images
	 */
	public static String embeddImages(InputStream inputStream) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputStream);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//image");
			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap attributes = nodeList.item(i).getAttributes();
				Node href = attributes.getNamedItem("xlink:href");
				Node width = attributes.getNamedItem("width");
				Node height = attributes.getNamedItem("height");
				InputStream streamImage = null;
				boolean scaleImage = true;
				if (href.getNodeValue().startsWith("classpath:")) {
					streamImage = ImageUtil.class.getResourceAsStream(href.getNodeValue().replace("classpath:", ""));
				} else if (href.getNodeValue().startsWith("http://")) {
					byte[] imageBytes = HttpClientUtil.get(href.getNodeValue());
					streamImage = new ByteArrayInputStream(imageBytes);
				} else if (href.getNodeValue().startsWith("data:image")) {
					// already encoded, skip
					scaleImage = false;
				}
				if (scaleImage) {
					if (streamImage != null) {
						byte[] scaledImage = scaleImage(streamImage, (int) Double.parseDouble(width.getTextContent()), (int) Double.parseDouble(height.getTextContent()));
						String base64Image = Base64.encodeBase64String(scaledImage);
						href.setNodeValue("data:image/png;base64," + base64Image);
						streamImage.close();
					} else {
						throw new SvgConversionException("Could not load image's input stream for embedding images, maybe not supported protocol?");
					}
				}
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.toString();
		} catch (Exception ex) {
			throw new SvgConversionException(ex);
		}
	}

	/**
	 * Generate JPEG thumbnail of SVG file
	 * 
	 * @param svgInputStream
	 *            SVG input stream
	 * @param height
	 *            images's height, width will be calculated automatically
	 * @return JPEG thumbnail
	 */
	public static byte[] generateJpegThumbnail(InputStream svgInputStream, int height) {
		try {
			List<String> lines = IOUtils.readLines(svgInputStream);
			return generateJpegThumbnail(SvgUtil.normalizeSvg(lines), height);
		} catch (IOException e) {
			throw new SvgConversionException(e);
		}
	}

	/**
	 * Generate JPEG thumbnail of SVG file
	 * 
	 * @param svgString
	 *            SVG String
	 * @param height
	 *            images's height, width will be calculated automatically
	 * @return JPEG thumbnail
	 */
	public static byte[] generateJpegThumbnail(String svgString, int height) {
		File tmpFile1 = null;
		File tmpFile2 = null;
		try {
			tmpFile1 = File.createTempFile("diploma-svg", "tmp");
			tmpFile2 = File.createTempFile("diploma-jpg", "tmp");
			FileUtils.writeStringToFile(tmpFile1, svgString, "UTF-8");
			SVGConverter converter = new SVGConverter();
			converter.setSources(new String[] { tmpFile1.toString() });
			converter.setDestinationType(DestinationType.JPEG);
			converter.setQuality(0.9F);
			converter.setMaxHeight(height);
			converter.setDst(tmpFile2);
			converter.execute();
			return FileUtils.readFileToByteArray(tmpFile2);
		} catch (Exception e) {
			throw new SvgConversionException(e);
		}

		finally {
			if (tmpFile1 != null) {
				if (!tmpFile1.delete()) {
					throw new SvgConversionException("could not delete temporary file!");
				}
			}
			if (tmpFile2 != null) {
				if (!tmpFile2.delete()) {
					throw new SvgConversionException("could not delete temporary file!");
				}
			}
		}
	}

}
