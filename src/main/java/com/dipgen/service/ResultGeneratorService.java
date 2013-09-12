package com.dipgen.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipgen.entity.Diploma;
import com.dipgen.entity.GeneratorString;
import com.dipgen.service.util.DiplomaUtil;
import com.dipgen.service.util.ImageUtil;
import com.dipgen.service.util.PdfUtil;

@Service
public class ResultGeneratorService {

	@Autowired
	private DiplomaService diplomaService;

	@Autowired
	private GeneratorService generatorService;

	public File generate(int id, Map<String, String[]> parameters, boolean singlePdf, String username) {
		// TODO REFACTOR AND TEST THIS!!!!
		try {
			File outputFile = File.createTempFile("pdf-zip-result", "tmp");
			Diploma diploma = diplomaService.findOne(id, username);
			String svg = diploma.getSvg();
			// embedd images
			svg = ImageUtil.embeddImages(IOUtils.toInputStream(svg));
			GeneratorString textarea = generatorService.findEnabledTextarea(id);
			List<GeneratorString> textfields = generatorService.findEnabledTextfields(id);
			if (textarea != null) {
				int textareaId = textarea.getGeneratorId();
				String[] strings = parameters.get("html-input-" + textareaId)[0].split("\n");
				List<File> outputPdfFiles = new ArrayList<File>();
				for (String string : strings) {
					string = string.trim();
					svg = svg.replaceAll(textarea.getString(), string);
					for (GeneratorString generatorString : textfields) {
						String text = parameters.get("html-input-" + generatorString.getGeneratorId())[0].trim();
						svg = svg.replaceAll(generatorString.getString(), text);
					}
					File tmpFile = File.createTempFile("pdf-single", "pdf");
					PdfUtil.generatePdf(svg, tmpFile);
					outputPdfFiles.add(tmpFile);
				}
				if (singlePdf) {
					PdfUtil.mergePdfs(outputPdfFiles, outputFile);
				} else {
					PdfUtil.generateZip(outputPdfFiles, outputFile);
				}
				// remove temporary files
				for (File file : outputPdfFiles) {
					if (!file.delete()) {
						System.err.println("Could not delete file: " + file);
					}
				}
			} else {
				for (GeneratorString generatorString : textfields) {
					String text = parameters.get("html-input-" + generatorString.getGeneratorId())[0].trim();
					svg = svg.replaceAll(generatorString.getString(), text);
				}
				File tmpFile = File.createTempFile("pdf-single", "pdf");
				PdfUtil.generatePdf(svg, tmpFile);
				if (singlePdf) {
					// delete tmp file, will be replaced by
					// actual file on next line
					if (!outputFile.delete()) {
						System.err.println("Could not delete file: " + outputFile);
					}
					outputFile = tmpFile;
				} else {
					List<File> pdfFiles = new ArrayList<File>();
					pdfFiles.add(tmpFile);
					PdfUtil.generateZip(pdfFiles, outputFile);
					if (!tmpFile.delete()) {
						System.err.println("Could not delete file: " + outputFile);
					}
				}
			}
			return outputFile;
		} catch (IOException e) {
			throw new DiplomaUtil.SvgConversionException(e);
		}
	}
}
