package com.dipgen.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import eu.medsea.util.MimeUtil;

public final class DiplomaUtilTest {

	@Test
	public void testParseTexts() {
		InputStream inputSvgStream = getClass().getResourceAsStream("/test-texts.svg");
		List<String> texts = DiplomaUtil.parseTexts(inputSvgStream);
		assertEquals(2, texts.size());
		assertEquals("hello world", texts.get(0));
		assertEquals("another hello", texts.get(1));
	}

	@Test
	public void testGeneratePdf() {
		InputStream inputSvgStream = getClass().getResourceAsStream("/test-texts.svg");
		File outputFile = new File("target/test-texts.pdf");
		DiplomaUtil.generatePdf(inputSvgStream, outputFile);
		assertEquals("application/pdf", MimeUtil.getMagicMimeType(outputFile));
	}

	@Test
	public void testGenerateJpegThumbnail() throws IOException {
		byte[] jpegThumbnail = DiplomaUtil.generateJpegThumbnail(getClass().getResourceAsStream("/test-texts.svg"), 100);
		File outputFile = new File("target/test-texts.jpeg");
		FileUtils.writeByteArrayToFile(outputFile, jpegThumbnail);
		assertEquals("image/jpeg", MimeUtil.getMagicMimeType(outputFile));
	}

	@Test
	public void testMergePdfs() throws IOException {
		InputStream inputCsvStream1 = getClass().getResourceAsStream("/test-texts.svg");
		File file1 = new File("target/test-texts-1.pdf");
		DiplomaUtil.generatePdf(inputCsvStream1, file1);
		InputStream inputCsvStream2 = getClass().getResourceAsStream("/test-texts.svg");
		File file2 = new File("target/test-texts-2.pdf");
		DiplomaUtil.generatePdf(inputCsvStream2, file2);
		ArrayList<File> list = new ArrayList<File>();
		list.add(file1);
		list.add(file2);
		File outputFile = new File("target/test-texts-merged.pdf");
		DiplomaUtil.mergePdfs(list, outputFile);
		assertEquals("application/pdf", MimeUtil.getMagicMimeType(outputFile));
		PDDocument doc = PDDocument.load(outputFile);
		assertEquals(2, doc.getNumberOfPages());
	}

	@Test
	public void testGenerateZip() throws ZipException, IOException {
		InputStream inputCsvStream1 = getClass().getResourceAsStream("/test-texts.svg");
		File file1 = new File("target/test-texts-1.pdf");
		DiplomaUtil.generatePdf(inputCsvStream1, file1);
		InputStream inputCsvStream2 = getClass().getResourceAsStream("/test-texts.svg");
		File file2 = new File("target/test-texts-2.pdf");
		DiplomaUtil.generatePdf(inputCsvStream2, file2);
		InputStream inputCsvStream3 = getClass().getResourceAsStream("/test-texts.svg");
		File file3 = new File("target/test-texts-3.pdf");
		DiplomaUtil.generatePdf(inputCsvStream3, file3);
		ArrayList<File> list = new ArrayList<File>();
		list.add(file1);
		list.add(file2);
		list.add(file3);
		File outputFile = new File("target/test-texts-zipped.zip");
		DiplomaUtil.generateZip(list, outputFile);
		ZipFile zipFile = new ZipFile(outputFile);
		assertEquals(3, zipFile.size());
		zipFile.close();
	}
}
