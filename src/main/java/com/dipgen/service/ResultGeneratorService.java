package com.dipgen.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipgen.entity.Diploma;
import com.dipgen.entity.GeneratorString;

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
			GeneratorString textarea = generatorService.findEnabledTextarea(id);
			List<GeneratorString> textfields = generatorService.findEnabledTextfields(id);
			if (textarea != null) {
				int textareaId = textarea.getGeneratorId();
				String[] strings = parameters.get("html-input-" + textareaId)[0].split("\n");
				List<File> outputPdfFiles = new ArrayList<File>();
				for (String string : strings) {
					String svg = diploma.getSvg();
					svg = svg.replaceAll(textarea.getString(), string);
					for (GeneratorString generatorString : textfields) {
						String text = parameters.get("html-input-" + generatorString.getGeneratorId())[0];
						svg = svg.replaceAll(generatorString.getString(), text);
					}
					File tmpFile = File.createTempFile("pdf-single", "pdf");
					DiplomaUtil.generatePdf(svg, tmpFile);
					outputPdfFiles.add(tmpFile);
				}
				if (singlePdf) {
					DiplomaUtil.mergePdfs(outputPdfFiles, outputFile);
				} else {
					DiplomaUtil.generateZip(outputPdfFiles, outputFile);
				}
				// remove temporary files
				for (File file : outputPdfFiles) {
					file.delete();
				}
			} else {
				String svg = diploma.getSvg();
				for (GeneratorString generatorString : textfields) {
					String text = parameters.get("html-input-" + generatorString.getGeneratorId())[0];
					svg = svg.replaceAll(generatorString.getString(), text);
				}
				File tmpFile = File.createTempFile("pdf-single", "pdf");
				DiplomaUtil.generatePdf(svg, tmpFile);
				if (singlePdf) {
					outputFile.delete(); // delete tmp file, will be replaced by
											// actual file on next line
					outputFile = tmpFile;
				} else {
					List<File> pdfFiles = new ArrayList<File>();
					pdfFiles.add(tmpFile);
					DiplomaUtil.generateZip(pdfFiles, outputFile);
					tmpFile.delete();
				}
			}
			return outputFile;
		} catch (IOException e) {
			throw new DiplomaUtil.SvgConversionException(e);
		}
	}
}
