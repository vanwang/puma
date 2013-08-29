package com.puma.android.builder;

import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import com.puma.book.epub.EpubConfig;
import com.puma.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class ProjectBuilder {
	
    public static void main(String[] args) {
    	ProjectConfig config = new ProjectConfig();
    	config.setApkType(ApkType.EBOOK);
        config.setSrcDir("e:\\genpro1");
        config.setProjectName("我的书");
        
        /*try {
			new ProjectBuilder().build(config);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }

    public File build(ProjectConfig config, EpubConfig epubConfig) throws IOException, Exception {
    		
    		createPumaProject(config);
    		
    		File coverImage = epubConfig.getCoverimg();
    		String suffix = FilenameUtils.getExtension(coverImage.getName());
    		if(!suffix.equalsIgnoreCase("png")){
				File existFile = new File(config.getSrcDir()+File.separator+"res"+File.separator+"drawable"+File.separator+"label.png");
				existFile.delete();
			}
			File labelImg = new File(config.getSrcDir()+File.separator+"res"+File.separator+"drawable"+File.separator+"label."+suffix);
			FileUtils.copyFile(coverImage, labelImg);
			
    		installProjectTemplate(config);
    		
        	releaseProject(config.getSrcDir());
        	
        	String releaseSingedApkPathString = config.getSrcDir()+File.separator+"bin"+File.separator+config.getProjectName()+"-release-signed.apk";
        	String finalApkPathString = new File(config.getSrcDir()).getParent();
        	if(!finalApkPathString.endsWith(File.separator)){
        		finalApkPathString += File.separator;
        	}
        	finalApkPathString += config.getProjectName()+".apk";
        	File destFile = FileUtils.copyFile(releaseSingedApkPathString , finalApkPathString);
        	
        	FileUtils.deleteFolder(config.getSrcDir());
        	
        	return destFile;
    }
    
    private void installProjectTemplate(ProjectConfig config){
    	TemplateManager templateManager = new TemplateManager();
    	switch(config.getApkType()){
			case EBOOK:
				templateManager.installEBookTemplate(config);
				break;
			default:
				break;
		}
    }
    
    private void createPumaProject(ProjectConfig config) throws IOException {
    	FileUtils.copyDirectiory(TemplateManager.getTemplatePath(config.getApkType()), config.getSrcDir());
    }
    
    private void releaseProject(String projectSrcDir) throws Exception {     
    	Project project = new Project();     
        project.init();     
    
        DefaultLogger consoleLogger = new DefaultLogger();     
        consoleLogger.setErrorPrintStream(System.err);     
        consoleLogger.setOutputPrintStream(System.out);     
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);     
        project.addBuildListener(consoleLogger);
             
        String _buildFile = new String(projectSrcDir + File.separator + "build.xml");     
    
        //ProjectHelper.getProjectHelper().parse(project, new File(_buildFile));      
        ProjectHelper.configureProject(project, new File(_buildFile));     
        
        if (project == null)     
            throw new Exception("No target can be launched because the project has not been initialized. Please call the 'init' method first !");     
        String _target = "zipalign";
        project.executeTarget(_target);     
    }     
    
    /*public void genKey(ProjectConfig config) throws Exception{
        StringBuffer cmd = new StringBuffer();  
        //cmd.append("C:\\jdk1.7.0_06\\bin\\");  
        cmd.append("keytool -genkey -v -alias puma-alias -keyalg RSA -keysize 2048 -validity 10000 ");  
        cmd.append("-keystore C:\\pumakeystore\\my-release-key.keystore ");  
        cmd.append("-keypass 123456 -storepass 123456 ");  
        cmd.append("-dname \"CN=puma,OU=cn,O=cn,L=cn,ST=cn,C=cn\"");  
        Process ps = Runtime.getRuntime().exec(cmd.toString());  
    }
    
    public void signJar(ProjectConfig config) throws Exception{
        StringBuffer cmd = new StringBuffer();  
        //cmd.append("C:\\jdk1.7.0_06\\bin\\");  
        cmd.append("jarsigner -keypass 123456 -storepass 123456 -verbose -sigalg MD5withRSA -digestalg SHA1 -keystore ");
        cmd.append("C:\\pumakeystore\\my-release-key.keystore ");
        //cmd.append("E:\\genpro\\bin\\myandroidproject-release-unsigned.apk");
        cmd.append(config.getSrcDir()+File.separator+"bin"+File.separator+config.getProjectName()+"-release-unsigned.apk");
        cmd.append(" puma-alias");
        System.out.println(cmd);
        Process ps = Runtime.getRuntime().exec(cmd.toString());  
    }
    
    public void verifyApk(ProjectConfig config) throws Exception{
        StringBuffer cmd = new StringBuffer();  
        //cmd.append("jarsigner -verify -verbose E:\\genpro\\bin\\myandroidproject-release-unsigned.apk");  
        cmd.append("jarsigner -verify -verbose ");
        cmd.append(config.getSrcDir()+File.separator+"bin"+File.separator+config.getProjectName()+"-release-unsigned.apk");
        System.out.println(cmd);
        Process ps = Runtime.getRuntime().exec(cmd.toString());  
    }
    
    public void alignApk(ProjectConfig config) throws Exception{
        StringBuffer cmd = new StringBuffer();  
        cmd.append(config.getSdkToolsDir()+File.separator);  
        //cmd.append("zipalign -v 4 E:\\genpro\\bin\\myandroidproject-release-unsigned.apk E:\\genpro\\bin\\myandroidproject-release.apk");  
        cmd.append("zipalign -v 4 ");
        cmd.append(config.getSrcDir()+File.separator+"bin"+File.separator+config.getProjectName()+"-release-unsigned.apk ");
        cmd.append(config.getSrcDir()+File.separator+"bin"+File.separator+config.getProjectName()+"-release.apk ");
        System.out.println(cmd);
        Process ps = Runtime.getRuntime().exec(cmd.toString());  
    }*/
}
