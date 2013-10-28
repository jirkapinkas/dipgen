package com.dipgen.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dipgen.service.util.ImageUtil;

@Controller
public class DemoImagesController {

	@RequestMapping(value = "/demo/image", params = "name")
	@ResponseBody
	public void get(@RequestParam String name, HttpServletResponse response) throws IOException {
		InputStream inputStream = ImageUtil.getDemoImage(name);
		ServletOutputStream outputStream = response.getOutputStream();
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();
		if (inputStream != null) {
			inputStream.close();
		}
	}

}
