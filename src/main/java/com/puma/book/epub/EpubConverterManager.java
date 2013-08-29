package com.puma.book.epub;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import junit.framework.Assert;

import com.adobe.dp.epub.conv.ConversionClient;
import com.adobe.dp.epub.conv.ConversionService;


public class EpubConverterManager implements ConversionClient{

	private String outputFolder = "D:\\";
	
	public static void main(String[] args) {
		new EpubConverterManager().convert("D:\\ctest\\test.docx","D:\\ctest");
	}
	
	public File convert(String srcFilePath, String outputFolderPath){
		//Assert.assertNotNull("You should specify the output path!",outputFolderPath);
		this.outputFolder = outputFolderPath;
		File srcFile = new File(srcFilePath);
		File destFile = null;
		Iterator it = ConversionService.registeredSerivces();
		
		while (it.hasNext()) {
			ConversionService service = (ConversionService) it.next();
			
			if(service.canConvert(srcFile)){
				StringWriter log = new StringWriter();
				PrintWriter plog = new PrintWriter(log);
				
				destFile = service.convert(srcFile, null, this, plog);
				System.out.println(destFile.getAbsolutePath());
			}
			
		}
		
		return destFile;
	}
	
	private File makeFile(File folder, String baseName){
		File file = new File(folder, baseName);
		if (file.exists()) {
			String baseStr;
			String extStr;
			int ext = baseName.indexOf('.');
			if (ext < 0) {
				baseStr = baseName;
				extStr = "";
			} else {
				baseStr = baseName.substring(0, ext);
				extStr = baseName.substring(ext);
			}
			int count = 1;
			while (true) {
				file = new File(folder, baseStr + "-" + count + extStr);
				if (!file.exists())
					break;
				count++;
			}
		}
		return file;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public void reportProgress(float progress) {
		// TODO Auto-generated method stub
		
	}

	public void reportIssue(String errorCode) {
		// TODO Auto-generated method stub
		
	}

	public File makeFile(String baseName) {
		File folder = new File(outputFolder);
		return makeFile(folder, baseName);
	}
}
