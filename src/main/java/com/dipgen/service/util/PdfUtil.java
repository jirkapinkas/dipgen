package com.dipgen.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.commons.io.IOUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.pdfbox.util.PDFMergerUtility;

import com.dipgen.service.ResultGeneratorService.GeneratedFile;
import com.dipgen.service.util.DiplomaUtil.SvgConversionException;

public class PdfUtil {

	/**
	 * Generate PDF from SVG file
	 * 
	 * @param inputSvg
	 *            Input SVG file
	 * @param outputPdfFile
	 *            Output PDF file will be saved here
	 */
	public static void generatePdf(String inputSvg, File outputPdfFile) {
		try {
			generatePdf(IOUtils.toInputStream(inputSvg, "UTF-8"), outputPdfFile);
		} catch (IOException e) {
			throw new SvgConversionException(e);
		}
	}

	/**
	 * Generate PDF from SVG file
	 * 
	 * @param inputSvgStream
	 *            Input Stream with SVG file
	 * @param outputPdfFile
	 *            Output PDF file will be saved here
	 */
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

	/**
	 * Merge PDF files to single output PDF file
	 * 
	 * @param pdfFiles
	 *            Input PDF files
	 * @param outputPdfFile
	 *            Single output PDF file
	 */
	public static void mergePdfs(List<GeneratedFile> pdfFiles, File outputPdfFile) {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		for (GeneratedFile pdf : pdfFiles) {
			pdfMergerUtility.addSource(pdf.getFile());
		}
		pdfMergerUtility.setDestinationFileName(outputPdfFile.toString());
		try {
			pdfMergerUtility.mergeDocuments();
		} catch (Exception e) {
			throw new SvgConversionException(e);
		}
	}

	public static void generateZip(List<GeneratedFile> pdfFiles, File outputZipFile) {
		byte[] buffer = new byte[1024];
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(outputZipFile));
			int index = 1;
			for (GeneratedFile file : pdfFiles) {
				FileInputStream in = null;
				try {
					zos.putNextEntry(new ZipEntry(index + "-" + file.getName() + ".pdf"));
					in = new FileInputStream(file.getFile());
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					zos.closeEntry();
					index++;
				} finally {
					if (in != null) {
						in.close();
					}
				}
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
