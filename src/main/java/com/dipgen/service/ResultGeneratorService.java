package com.dipgen.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipgen.entity.Diploma;
import com.dipgen.entity.GeneratorString;
import com.dipgen.service.security.RoleService;
import com.dipgen.service.security.UserService;
import com.dipgen.service.util.DiplomaUtil;
import com.dipgen.service.util.ImageUtil;
import com.dipgen.service.util.PdfUtil;
import com.dipgen.service.util.TextUtil;

@Service
public class ResultGeneratorService {

	@Autowired
	private DiplomaService diplomaService;

	@Autowired
	private GeneratorService generatorService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	public static class PremiumRestrictionException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public PremiumRestrictionException(String message) {
			super(message);
		}
	}

	private List<GeneratedFile> generateDiplomasWithTextArea(String svgTemplate, Map<String, String[]> parameters, GeneratorString textarea, List<GeneratorString> textfields, String username)
			throws IOException {
		int textareaId = textarea.getGeneratorId();
		String[] strings = parameters.get("html-input-" + textareaId)[0].split("\n");
		if (strings.length > 5) {
			// restrict free users to ease server load
			if (!userService.isPremium(username)) {
				throw new PremiumRestrictionException("I'm sorry, but free users can generate max. 5 diplomas at one time. Premium users have no such limit.");
			}
		}
		List<GeneratedFile> outputPdfFiles = new ArrayList<GeneratedFile>();
		for (String string : strings) {
			string = string.trim();
			String svg = svgTemplate;
			svg = svg.replaceAll(textarea.getString(), string);
			File tmpFile = generateDiplomaWithoutTextArea(svg, parameters, textfields);
			outputPdfFiles.add(new GeneratedFile(tmpFile, TextUtil.normalizeText(string)));
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

	private File generateOutputFile(boolean singlePdf, List<GeneratedFile> outputPdfFiles) throws IOException {
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
		List<GeneratedFile> outputPdfFiles = new ArrayList<GeneratedFile>();
		try {
			Diploma diploma = diplomaService.findOne(id, username);
			String svgTemplate = diploma.getSvg();
			// embedd images
			InputStream stream = IOUtils.toInputStream(svgTemplate);
			svgTemplate = ImageUtil.embeddImages(stream);
			stream.close();
			GeneratorString textarea = generatorService.findEnabledTextarea(id);
			List<GeneratorString> textfields = generatorService.findEnabledTextfields(id);
			if (textarea != null) {
				outputPdfFiles.addAll(generateDiplomasWithTextArea(svgTemplate, parameters, textarea, textfields, username));
			} else {
				File file = generateDiplomaWithoutTextArea(svgTemplate, parameters, textfields);
				outputPdfFiles.add(new GeneratedFile(file, ""));
			}
			return generateOutputFile(singlePdf, outputPdfFiles);
		} catch (IOException e) {
			throw new DiplomaUtil.SvgConversionException(e);
		} finally {
			for (GeneratedFile generatedFile : outputPdfFiles) {
				if (!generatedFile.getFile().delete()) {
					System.err.println("Could not delete file: " + generatedFile.getFile());
				}
			}
		}
	}

	public static class GeneratedFile {
		private File file;
		private String name;

		public GeneratedFile(File file, String name) {
			this.file = file;
			this.name = name;
		}

		public File getFile() {
			return file;
		}

		public String getName() {
			return name;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
