package com.puma.android.builder;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ProjectConfig {
	
	private String sdkToolsDir;
	
	private String srcDir;
	
	private String projectName;
	
	private ApkType apkType;
	
	private List<File> fileList;

	public ProjectConfig() {
		sdkToolsDir = "F:\\Tools\\android-sdk-windows\\tools";
		projectName = "PumaProject";
	}
	
	public String getSdkToolsDir() {
		return sdkToolsDir;
	}

	public void setSdkToolsDir(String sdkToolsDir) {
		this.sdkToolsDir = sdkToolsDir;
	}

	public String getSrcDir() {
		 if(srcDir != null && !srcDir.endsWith(File.separator)){
			 srcDir += File.separator;
		 }
		return srcDir;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public ApkType getApkType() {
		return apkType;
	}

	public void setApkType(ApkType apkType) {
		this.apkType = apkType;
	}

	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}

}
