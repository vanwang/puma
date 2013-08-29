package com.puma.android.builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.puma.common.SystemConfigBean;
import com.puma.util.FileUtils;

public class TemplateManager {
	
	private static final String TEMPLATE = "templates";
	
	private static final String ANDROID_SDK_HOME = "ANDROID_SDK_HOME";
	private static final String JDK_HOME = "JDK_HOME";
	private static final String ANDROID_APP_NAME = "ANDROID_APP_NAME";
	private static final String ANDROID_APP_PACKAGE_NAME = "ANDROID_APP_PACKAGE_NAME";
	private static final String PACKAGE_PREFIX ="com.puma.ebook.book";
	
	public static String getTemplatePath(ApkType apkType){
		switch(apkType){
		case EBOOK:
			return SystemConfigBean.getEbookTemplatePath();
		}
		return SystemConfigBean.getEbookTemplatePath();
	}
	
	public void installEBookTemplate(ProjectConfig config){
		final HashMap<String, String> keywords = new HashMap<String, String>();
		 keywords.put(ANDROID_SDK_HOME, SystemConfigBean.getSdkHome());
		 keywords.put(JDK_HOME, 		SystemConfigBean.getJdkHome());
		 keywords.put(ANDROID_APP_NAME,	config.getProjectName());
		 keywords.put(ANDROID_APP_PACKAGE_NAME,	PACKAGE_PREFIX+UUID.randomUUID().toString().replace("-", ""));
		 List<File> fList = config.getFileList();
		 File f = fList.get(0);
		 String fileName = "mybook.epub";
		 String srcDir = config.getSrcDir();
		 if(f != null && f.exists()){
			 //fileName = FilenameUtils.getName(f.getAbsolutePath());
			 String targetFilePath = srcDir + "assets" + File.separator + "data" + File.separator + "book" + File.separator + fileName;
			 try {
				FileUtils.copyFile(f, new File(targetFilePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 
		 
		 String sourceFilePath = getTemplatePath(ApkType.EBOOK)+File.separator+TEMPLATE+File.separator+"build.properties.template";
		 installFullPathTemplate(sourceFilePath,new File(config.getSrcDir(), "build.properties"), keywords);
		 
		 sourceFilePath = getTemplatePath(ApkType.EBOOK)+File.separator+TEMPLATE+File.separator+"build.xml.template";
		 installFullPathTemplate(sourceFilePath,new File(config.getSrcDir(), "build.xml"), keywords);
		 
		 sourceFilePath = getTemplatePath(ApkType.EBOOK)+File.separator+TEMPLATE+File.separator+"AndroidManifest.xml.template";
		 installFullPathTemplate(sourceFilePath,new File(config.getSrcDir(), "AndroidManifest.xml"), keywords);
		 
		/* sourceFilePath = getTemplatePath(ApkType.EBOOK)+File.separator+TEMPLATE+File.separator+"BookUtil.java.template";
		 //src\org\geometerplus\fbreader\book
		 String bookUtilFilePath = srcDir + "src" + File.separator + "org" + File.separator + "geometerplus" 
				 					+ File.separator + "fbreader" + File.separator + "book" + File.separator + "BookUtil.java";
		 installFullPathTemplate(sourceFilePath,new File(bookUtilFilePath), keywords);*/
		 
	}
	
	private void installFullPathTemplate(String sourceFilePath, File destFile,
            Map<String, String> placeholderMap) {

        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(destFile));
        	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile),"UTF-8"));
            //BufferedReader in = new BufferedReader(new FileReader(sourceFilePath));
        	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath),"UTF-8"));
            String line;

            while ((line = in.readLine()) != null) {
                if (placeholderMap != null) {
                    for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
                        line = line.replace(entry.getKey(), entry.getValue());
                    }
                }
                out.write(line);
                out.newLine();
            }

            out.close();
            in.close();
        } catch (Exception e) {
        }

    }
}
