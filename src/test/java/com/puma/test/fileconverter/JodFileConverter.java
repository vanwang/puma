package com.puma.test.fileconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.document.DocumentFormatRegistry;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.puma.util.FileUtils;


public class JodFileConverter {

	static OfficeManager officeManager;
	
    public static void main(String[] args) {
    	startService();
    	//run();
    	
    	//stopService();
    	File file = new File("D:\\ctest\\test.pdf");
    	File convertedFile = convert(file, "doc");
    	/*List<String> list = getSupportedOutputFormat(file);
    	if(list.contains("doc")){
    		list.add("docx");
    	}
    	for(String s:list){
    		System.out.println(s);
    		if(s.equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))){
    			continue;
    		}
    		File convertedFile = convert(file, s);
    		if(s.equalsIgnoreCase("docx")){
    			File docxFile = new File(convertedFile.getParent()+File.separator+FilenameUtils.getBaseName(convertedFile.getName())+s);
					//FileUtils.copyFile(convertedFile, docxFile);
				new EpubConverterManager().convert(docxFile.getAbsolutePath(),docxFile.getParent());
    		}
    		System.out.println(s);
    	}*/
    	
    	stopService();
	}
    
    /*static void run(){
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("111");
				new JodConverter().convert(new File("D:\\ctest\\test.docx"),0);
				
			}
		}).start();
    	
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("222");
				new JodConverter().convert(new File("D:\\ctest\\test2.docx"),3);
				
			}
		}).start();
    }*/
    
    public static void startService(){
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
          System.out.println("准备启动服务....");
            /*configuration.setOfficeHome(OFFICE_HOME);//设置OpenOffice.org安装目录
            configuration.setPortNumbers(port); //设置转换端口，默认为8100*/
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);//设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);//设置任务队列超时为24小时
         
            officeManager = configuration.buildOfficeManager();
            officeManager.start();    //启动服务
            System.out.println("office转换服务启动成功!");
        } catch (Exception ce) {
            System.out.println("office转换服务启动失败!详细信息:" + ce);
        }
    }
    
    public static void stopService(){
        System.out.println("关闭office转换服务....");
          if (officeManager != null) {
              officeManager.stop();
          }
          System.out.println("关闭office转换成功!");
  }
    
    public static List<String> getSupportedOutputFormat(File inputFile){
    	List<String> formatList = new ArrayList<String>();
    	OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        DocumentFormatRegistry formatRegistry = converter.getFormatRegistry();
        
    	String inputExtension = FilenameUtils.getExtension(inputFile.getName());
    	DocumentFormat inputFormat = formatRegistry.getFormatByExtension(inputExtension);
    	Set<DocumentFormat> outputFormats = formatRegistry.getOutputFormats(inputFormat.getInputFamily());
    	
    	
        for (DocumentFormat outputFormat : outputFormats) {
        	formatList.add(outputFormat.getExtension());
    	}
        return formatList;
    }
    
    public static File convert(final File inputFile, String extension){
					
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        	
            String outputFileName = FilenameUtils.getBaseName(inputFile.getName());
            String outputFileString = inputFile.getParent()+File.separator+outputFileName+"."+extension;
            
    		File outputFile = new File(outputFileString);
			try {
				converter.convert(inputFile, outputFile);
			} catch (OfficeException e) {
				e.printStackTrace();
			}
			return outputFile;
			
	}
        /*for (DocumentFormat outputFormat : outputFormats) {
        	System.out.println(outputFormat.getExtension());
				File outputFile = new File("D:\\ctest\\test.pdf");
				//outputFile.deleteOnExit();
				if(outputFormat.getExtension().equalsIgnoreCase("pdf")){
					officeManager.start();
					converter.convert(inputFile, outputFile, outputFormat);
					officeManager.stop();
				}
				
        	System.out.println(outputFormat.getMediaType());
        	System.out.println(outputFormat.getName());
            //File outputFile = File.createTempFile("test", "." + outputFormat.getExtension());
            File outputFile = File.createTempFile("test", "." + outputFormat.getExtension(), new File("D:\\testout"));
            outputFile.deleteOnExit();
            System.out.printf("-- converting %s to %s... ", inputFormat.getExtension(), outputFormat.getExtension());
            converter.convert(inputFile, outputFile, outputFormat);
            System.out.printf("done.\n");
            assertTrue(outputFile.isFile() && outputFile.length() > 0);
            //TODO use file detection to make sure outputFile is in the expected format
        }*/
}
