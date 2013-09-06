package com.dipgen.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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

	public static String normalizeSvg(String svg) {
		return svg.replaceAll("[\r\n]+", " ");
	}

	public static String normalizeSvg(String[] svg) {
		String outputSvg = "";
		for (String line : svg) {
			outputSvg += line + " ";
		}
		return outputSvg;
	}

	public static String normalizeSvg(List<String> svgList) {
		String[] array = svgList.toArray(new String[svgList.size()]);
		return normalizeSvg(array);
	}

	public static List<String> parseTexts(String svg) {
		try {
			return parseTexts(IOUtils.toInputStream(svg, "UTF-8"));
		} catch (IOException ex) {
			throw new SvgConversionException(ex);
		}
	}

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
		} catch (Exception ex) {
			throw new SvgConversionException(ex);
		}
	}

	public static void generatePdf(String inputSvg, File outputPdfFile) {
		try {
			generatePdf(IOUtils.toInputStream(inputSvg, "UTF-8"), outputPdfFile);
		} catch (IOException e) {
			throw new SvgConversionException(e);
		}
	}

	public static void generatePdf(InputStream inputSvgStream, File outputPdfFile) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(outputPdfFile);
			Transcoder transcoder = new PDFTranscoder();
			TranscoderInput transcoderInput = new TranscoderInput(inputSvgStream);
			TranscoderOutput transcoderOutput = new TranscoderOutput(outputStream);
			transcoder.transcode(transcoderInput, transcoderOutput);
		} catch (Exception ex) {
			throw new SvgConversionException(ex);
		} finally {
			if (outputStream != null) {
				try {
					inputSvgStream.close();
					outputStream.close();
				} catch (IOException e) {
					throw new SvgConversionException(e);
				}
			}
		}
	}

	public static byte[] generateJpegThumbnail(InputStream svgInputStream, int height) {
		try {
			List<String> lines = IOUtils.readLines(svgInputStream);
			return generateJpegThumbnail(normalizeSvg(lines), height);
		} catch (IOException e) {
			throw new SvgConversionException(e);
		}
	}

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
		} finally {
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

	public static void mergePdfs(List<File> pdfFiles, File outputPdfFile) {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		for (File pdf : pdfFiles) {
			pdfMergerUtility.addSource(pdf);
		}
		pdfMergerUtility.setDestinationFileName(outputPdfFile.toString());
		try {
			pdfMergerUtility.mergeDocuments();
		} catch (Exception e) {
			throw new SvgConversionException(e);
		}
	}

	public static void generateZip(List<File> pdfFiles, File outputZipFile) {
		byte[] buffer = new byte[1024];
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(outputZipFile));
			int index = 1;
			for (File file : pdfFiles) {
				zos.putNextEntry(new ZipEntry("diploma-" + index + ".pdf"));
				FileInputStream in = new FileInputStream(file);
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				in.close();
				index++;
			}
		} catch (IOException ex) {
			throw new SvgConversionException(ex);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					throw new SvgConversionException(e);
				}
			}
		}
	}

}
