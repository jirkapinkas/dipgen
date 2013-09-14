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

	private List<File> generateDiplomasWithTextArea(String svgTemplate, Map<String, String[]> parameters, GeneratorString textarea, List<GeneratorString> textfields) throws IOException {
		int textareaId = textarea.getGeneratorId();
		String[] strings = parameters.get("html-input-" + textareaId)[0].split("\n");
		List<File> outputPdfFiles = new ArrayList<File>();
		for (String string : strings) {
			string = string.trim();
			String svg = svgTemplate;
			svg = svg.replaceAll(textarea.getString(), string);
			File tmpFile = generateDiplomaWithoutTextArea(svg, parameters, textfields);
			outputPdfFiles.add(tmpFile);
		}
		return outputPdfFiles;
	}

	private File generateDiplomaWithoutTextArea(String svg, Map<String, String[]> parameters, List<GeneratorString> textfields) throws IOException {
		for (GeneratorString generatorString : textfields) {
			String text = parameters.get("html-input-" + generatorString.getGeneratorId())[0].trim();
			svg = svg.replaceAll(generatorString.getString(), text);
		}
		File outputFile = File.createTempFile("pdf-single", "pdf");
		PdfUtil.generatePdf(svg, outputFile);
		return outputFile;
	}

	private File generateOutputFile(boolean singlePdf, List<File> outputPdfFiles) throws IOException {
		File outputFile = File.createTempFile("pdf-zip-result", "tmp");
		if (singlePdf) {
			PdfUtil.mergePdfs(outputPdfFiles, outputFile);
		} else {
			PdfUtil.generateZip(outputPdfFiles, outputFile);
		}
		return outputFile;
	}

	// TODO TEST THIS!!!!
	public File generate(int id, Map<String, String[]> parameters, boolean singlePdf, String username) {
		List<File> outputPdfFiles = new ArrayList<File>();
		try {
			Diploma diploma = diplomaService.findOne(id, username);
			String svgTemplate = diploma.getSvg();
			// embedd images
			svgTemplate = ImageUtil.embeddImages(IOUtils.toInputStream(svgTemplate));
			GeneratorString textarea = generatorService.findEnabledTextarea(id);
			List<GeneratorString> textfields = generatorService.findEnabledTextfields(id);
			if (textarea != null) {
				outputPdfFiles.addAll(generateDiplomasWithTextArea(svgTemplate, parameters, textarea, textfields));
			} else {
				outputPdfFiles.add(generateDiplomaWithoutTextArea(svgTemplate, parameters, textfields));
			}
			return generateOutputFile(singlePdf, outputPdfFiles);
		} catch (IOException e) {
			throw new DiplomaUtil.SvgConversionException(e);
		} finally {
			for (File file : outputPdfFiles) {
				if (!file.delete()) {
					System.err.println("Could not delete file: " + file);
				}
			}
		}
	}
}
