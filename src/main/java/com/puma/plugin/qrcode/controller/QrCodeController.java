package com.puma.plugin.qrcode.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.puma.core.base.BaseController;
import com.puma.util.QRUtils;
import com.puma.util.QrConfig;

@Controller
@RequestMapping("/qrcode")
public class QrCodeController extends BaseController{
	
	@RequestMapping("generateqrimage.do")  
	public void generateQrImage(HttpServletRequest req, HttpServletResponse res, QrConfig config) {
		try {
			new QRUtils().generate(config, req, res);
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} catch (ServletException e) {
		}
	}  
	
}
