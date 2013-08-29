package com.puma.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.puma.android.builder.ApkType;
import com.puma.android.builder.ProjectBuilder;
import com.puma.android.builder.ProjectConfig;
import com.puma.book.epub.EpubConfig;
import com.puma.book.epub.EpubConverterManager;
import com.puma.book.epub.exception.BookNotExistException;
import com.puma.common.BusinessHandler;
import com.puma.common.ResponseMessage;
import com.puma.common.SystemConfigBean;
import com.puma.core.base.BaseController;
import com.puma.core.security.SecurityUtils;
import com.puma.core.service.MemberService;
import com.puma.test.epub.EpubGenerateTest;
import com.puma.util.FileUtils;
import com.puma.util.ImageUtils;
import com.puma.util.QRUtils;
import com.puma.util.QrConfig;

@Controller
public class AdminController extends BaseController{
	
	// Spring security 最后登录异常Session名称
	//public static final String SPRING_SECURITY_LAST_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/main.html")
	public ModelAndView main(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("main");
		mv.addObject("member",SecurityUtils.getAuthedMember());
		return mv;
	}
	
	@RequestMapping("/login.html")
	public ModelAndView login(final HttpServletRequest req, @RequestParam(value="status", required=true, defaultValue="0") int status){
		HttpSession s = req.getSession();
		String sid = s.getId();
		System.out.println(sid);
		ModelAndView mv=new ModelAndView();
		mv.setViewName("login");
		mv.addObject("status",status);
		return mv;
	}
	
	@RequestMapping("/menumanagement.html")
	public ModelAndView menuManagementView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("menumanagement");
		return mv;
	}
	
	@RequestMapping("/menumanegement-addcustommenu.html")
	public ModelAndView menuManagementAddCustomView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("menumanegement-addcustommenu");
		return mv;
	}
	
	@RequestMapping("/resourcemanagement.html")
	public ModelAndView resourceManagementView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("resourcemanagement");
		return mv;
	}
	
	@RequestMapping("/rolemanagement.html")
	public ModelAndView roleManagementView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("rolemanagement");
		return mv;
	}
	
	@RequestMapping("/usermanagement.html")
	public ModelAndView userManagementView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("usermanagement");
		return mv;
	}
	
	@RequestMapping("/changeskin.html")
	public ModelAndView changeSkinView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("changeskin");
		return mv;
	}
	
	@RequestMapping("/userlog.html")
	public ModelAndView userlogView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("userlogmanagement");
		return mv;
	}
	
	@RequestMapping("/qrcode.html")
	public ModelAndView qrcodeView(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("qrcode/qrcode");
		return mv;
	}
	
	@RequestMapping("regverify.html")
	public ModelAndView registerVerify(@RequestParam(value = "vstr") final String activeCode){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("info");
		boolean regVerifyResult = memberService.regVerify(activeCode);
		String result = "";
		if(regVerifyResult){
			mv.addObject("title", "激活成功");
			result = "您的账号已经成功激活！ "
					+ "<br> <br> 请<a href=\"login.html\">登录</a>。";
		}else{
			mv.addObject("title", "激活失败");
			result = "对不起，此链接已失效。可能您已完成激活，或者超过有效激活时间。 "
					+ "<br> <br> 你可以选择<a href=\"login.html\">登录</a>，或<a href=\"register.html\">重新注册</a>。";
		}
		mv.addObject("info",result);
		return mv;
	}
	
	@RequestMapping("wizard.html")
	public ModelAndView wizardPage(final HttpServletRequest req){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("tools/wizard");
		mv.addObject("sessionId",req.getSession().getId());
		return mv;
	}
	
	@RequestMapping("generatebook.do")
	public @ResponseBody ResponseMessage generateBook(final HttpServletRequest req, 
			@RequestParam(value = "outputtype") final String outputType, 
			@RequestParam(value = "filename") final String fName,
			@RequestParam("imagename") final String  imageName,
			final EpubConfig epubConfig) {
		ResponseMessage response = ResponseMessage.handleException(new BusinessHandler(){
			@Override
			public Object handle() throws Exception {
				final String fileName = FilenameUtils.getName(fName);
				String sessionId = req.getSession().getId();
				String path = req.getSession().getServletContext().getRealPath(File.separator+"upload"+File.separator);
				path += File.separator+sessionId+File.separator;
				String downloadFilePath = SystemConfigBean.getServerUrl()+"upload/"+sessionId+"/";
				File uploadedFile = new File(path+fileName);
				if(uploadedFile.exists()){
					Map<String, String> m = new HashMap<String, String>(); 
					File destFile = null;
					String fileBaseName = FilenameUtils.getBaseName(fileName);
					String fileExtension = FilenameUtils.getExtension(fileName);
					File coverImg = new File(req.getSession().getServletContext().getRealPath(File.separator+"resources"+File.separator+"book"+File.separator+"cover.png"));
					
					if(outputType.equalsIgnoreCase("apk")){
						String srcDir = path+"tempsrc"+File.separator+"tmp.txt";
						File srcDirector = FileUtils.checkExist(srcDir);
						
						File destEpubFile = null;
						if(fileExtension.equalsIgnoreCase("docx")){
							destEpubFile = convertDocxToEpub(uploadedFile.getAbsolutePath(), uploadedFile.getParent());
						}else if(fileExtension.equalsIgnoreCase("epub")){
							destEpubFile = uploadedFile;
						}else{
							throw new BookNotExistException("目前只支持从docx,epub文件转换到apk或者epub！");
						}
						if(epubConfig != null){
							if(imageName != null && imageName.length()>0){
								//image name with chinese will not show
								File uploadedImage = new File(path+"/"+imageName);
								String prefix = UUID.randomUUID().toString().replace("-", "");
								String suffix = FilenameUtils.getExtension(imageName);
								coverImg = File.createTempFile(prefix, "."+suffix);
								FileUtils.copyFile(uploadedImage, coverImg);
								
								uploadedImage.delete();
								coverImg.deleteOnExit();
								if(coverImg.exists()){
									/*epubConfig.setCoverimg(coverImg);
									
									String coverSourceFilePath = req.getSession().getServletContext().getRealPath(File.separator+"templates"+File.separator+"cover.html.template");
									File coverDestFile = FileUtils.checkExist(path+"cover.html");
									final HashMap<String, String> keywords = new HashMap<String, String>();
									keywords.put("COVER_IMAGE_NAME", FilenameUtils.getName(coverImg.getAbsolutePath()));
									FileUtils.installFullPathTemplate(coverSourceFilePath, coverDestFile, keywords);
									
									epubConfig.setCoverpage(coverDestFile);*/
								}
								
							}
							
							epubConfig.setCoverimg(coverImg);
							
							String coverSourceFilePath = req.getSession().getServletContext().getRealPath(File.separator+"templates"+File.separator+"cover.html.template");
							File coverDestFile = FileUtils.checkExist(path+"cover.html");
							final HashMap<String, String> keywords = new HashMap<String, String>();
							keywords.put("COVER_IMAGE_NAME", FilenameUtils.getName(coverImg.getAbsolutePath()));
							FileUtils.installFullPathTemplate(coverSourceFilePath, coverDestFile, keywords);
							
							epubConfig.setCoverpage(coverDestFile);
							destEpubFile = configEpub(destEpubFile, epubConfig);
						}
						ProjectConfig config = new ProjectConfig();
				    	config.setApkType(ApkType.EBOOK);
				        config.setSrcDir(srcDirector.getParent());
				        config.setProjectName(fileBaseName);
				        List<File> fList = new ArrayList<File>();
				        fList.add(destEpubFile);
				        config.setFileList(fList);
				        
						destFile = buildApk(config, epubConfig);
						
					}else if(outputType.equalsIgnoreCase("epub")){
						if(fileExtension.equalsIgnoreCase("docx")){
							destFile = convertDocxToEpub(uploadedFile.getAbsolutePath(), uploadedFile.getParent());
						}else if(fileExtension.equalsIgnoreCase("epub")){
							destFile = uploadedFile;
						}else{
							throw new BookNotExistException("目前只支持从docx,epub文件转换到epub！");
						}
						if(epubConfig != null){
							if(imageName != null && imageName.length()>0){
								//image name with chinese will not show
								File uploadedImage = new File(path+"/"+imageName);
								String prefix = UUID.randomUUID().toString().replace("-", "");
								String suffix = FilenameUtils.getExtension(imageName);
								coverImg = File.createTempFile(prefix, "."+suffix);
								FileUtils.copyFile(uploadedImage, coverImg);
								uploadedImage.delete();
								coverImg.deleteOnExit();
								if(coverImg.exists()){
									epubConfig.setCoverimg(coverImg);
									
									String coverSourceFilePath = req.getSession().getServletContext().getRealPath(File.separator+"templates"+File.separator+"cover.html.template");
									File coverDestFile = FileUtils.checkExist(path+"cover.html");
									final HashMap<String, String> keywords = new HashMap<String, String>();
									keywords.put("COVER_IMAGE_NAME", FilenameUtils.getName(coverImg.getAbsolutePath()));
									FileUtils.installFullPathTemplate(coverSourceFilePath, coverDestFile, keywords);
									
									epubConfig.setCoverpage(coverDestFile);
									/*
									String prefix = FilenameUtils.getBaseName(coverImg.getAbsolutePath());
									String suffix = FilenameUtils.getExtension(coverImg.getAbsolutePath());
									File tempCoverImg = File.createTempFile(prefix, "."+suffix);
									tempCoverImg.deleteOnExit();
									
									ImageUtils.resize(250, tempCoverImg.getAbsolutePath(), coverImg);
											
									epubConfig.setCoverimg(tempCoverImg);
									
									String coverSourceFilePath = req.getSession().getServletContext().getRealPath(File.separator+"templates"+File.separator+"cover.html.template");
									File coverDestFile = FileUtils.checkExist(path+"cover.html");
									final HashMap<String, String> keywords = new HashMap<String, String>();
									keywords.put("COVER_IMAGE_NAME", FilenameUtils.getName(tempCoverImg.getAbsolutePath()));
									FileUtils.installFullPathTemplate(coverSourceFilePath, coverDestFile, keywords);
									
									epubConfig.setCoverpage(coverDestFile);
								*/}
								
							}
							destFile = configEpub(destFile, epubConfig);
						}
					}else{
						m.put("imgsrc", "resources/themes/img/generate.gif");
						m.put("downloadurl", "http://www.pumaframework.com");
						m.put("fileext", outputType);
					}
					
					downloadFilePath += FilenameUtils.getName(destFile.getName());
					String imgsrc = generateQrCode(downloadFilePath, destFile);
					
					m.put("imgsrc", "upload/"+sessionId+"/"+FilenameUtils.getName(imgsrc));
					m.put("downloadurl", downloadFilePath);
					m.put("fileext", FilenameUtils.getExtension(downloadFilePath));
					
					return m;
				}else{
					throw new BookNotExistException("您要转换的文件"+fileName+"不存在！");
				}
			}
		}, logger);
		return response;
	}
	
	private File configEpub(File epubFile, EpubConfig epubConfig) throws Exception{
		Book book = new EpubReader().readEpub(new FileInputStream(epubFile));
		
		String title = epubConfig.getTitle();
		if(title != null){
			book.getMetadata().addTitle(title);
		}
		//book.getMetadata().addIdentifier(new Identifier(Identifier.Scheme.ISBN, "987654321"));
		
		String author = epubConfig.getAuthor();
		if(author != null){
			//book.getMetadata().addAuthor(new Author(author, ""));
			List<Author> list = new ArrayList<Author>();
			list.add(new Author(author));
			book.getMetadata().setAuthors(list);
		}
		
		String description = epubConfig.getDescription();
		if(description != null){
			book.getMetadata().addDescription(description);
		}
		
		Date date = epubConfig.getPublishdate();
		if(date != null){
			//book.getMetadata().addDate(new nl.siegmann.epublib.domain.Date(date));
			List<nl.siegmann.epublib.domain.Date> list = new ArrayList<nl.siegmann.epublib.domain.Date>();
			list.add(new nl.siegmann.epublib.domain.Date(date));
			book.getMetadata().setDates(list);
		}
		
		String publisher = epubConfig.getPublisher();
		if(publisher != null){
			book.getMetadata().addPublisher(publisher);
		}
		
		String tag = epubConfig.getTag();
		if(tag != null){
			List<String> subjects = new ArrayList<String>();
			subjects.add(tag);//标签
			book.getMetadata().setSubjects(subjects);
		}
		
		String locale = epubConfig.getLocale();
		if(locale != null){
			//book.getMetadata().setLanguage(Locale.SIMPLIFIED_CHINESE.toString().replace("_", "-").toLowerCase());
			book.getMetadata().setLanguage(locale.replace("_", "-").toLowerCase());
		}
		
		File coverImg = epubConfig.getCoverimg();
		if(coverImg != null){
			book.setCoverImage(new Resource(new FileInputStream(epubConfig.getCoverimg()), FilenameUtils.getName(epubConfig.getCoverimg().getName())));
			
			book.setCoverPage(new Resource(new FileInputStream(epubConfig.getCoverpage()), FilenameUtils.getName(epubConfig.getCoverpage().getName())));
		}
		
		EpubWriter epubWriter = new EpubWriter();
		
		String filePath = epubFile.getParent();
		if(!filePath.endsWith(File.separator)){
			filePath += File.separator;
		}
		String fileBaseName = FilenameUtils.getBaseName(epubFile.getAbsolutePath());
		//String fileExtension = FilenameUtils.getExtension(epubFile.getAbsolutePath());
		String configedEpubFilePath = filePath + fileBaseName+"-configed.epub";
		File configedEpub = FileUtils.checkExist(configedEpubFilePath);
		// Write the Book as Epub
		epubWriter.write(book, new FileOutputStream(configedEpub));
		
		return configedEpub;
	}
	
	private File convertToEpub(File uploadedFile){
		File destEpubFile = convertDocxToEpub(uploadedFile.getAbsolutePath(), uploadedFile.getParent());
		return destEpubFile;
	}
	
	private File convertDocxToEpub(String docxFilePath, String outputfilePath){
		return new EpubConverterManager().convert(docxFilePath,outputfilePath);
	}
	
	private String generateQrCode(String path, File destFile){
		QrConfig qc = new QrConfig();
		qc.setData(path);
		
		String outputPath = destFile.getParent();
		if(!outputPath.endsWith(File.separator)){
			outputPath += File.separator;
		}
		String type = "jpg";
		if(qc.getOutput().equalsIgnoreCase("jpg")) {
			type = "jpg";
		}else if(qc.getOutput().equalsIgnoreCase("gif")) {
			type="gif";
		}else if(qc.getOutput().equalsIgnoreCase("png")) {
			type="png";
		}
		String fileName = FilenameUtils.getBaseName(destFile.getName());
		String fileExtension = FilenameUtils.getExtension(destFile.getName());
		String outputFileName = fileName +"-"+fileExtension+"."+type;
		String outputFilePath = outputPath + outputFileName;
		
		try {
			new QRUtils().generate(qc, outputFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputFilePath;
	}
	
	private File buildApk(ProjectConfig config, EpubConfig epubConfig) throws IOException, Exception{
		File file = new ProjectBuilder().build(config, epubConfig);
        return file;
	}
	
	@RequestMapping("singlefileupload.do")  
	public void singleFileUpload(HttpServletRequest req, HttpServletResponse res, @RequestParam(value="file", required=false) MultipartFile file) {
		//path = path + File.separator + filename; // 获取本地存储路径
		String sessionId = req.getSession().getId();
		String path = req.getSession().getServletContext().getRealPath(File.separator+"upload"+File.separator);
		path += File.separator+sessionId+File.separator;
		
		PrintWriter writer = null;
	    InputStream is = null;
	    FileOutputStream fos = null;

	      try {
	            writer = res.getWriter();
	        } catch (IOException ex) {
	        }
	        
	      
		if(file != null){

			CommonsMultipartFile mf = (CommonsMultipartFile) file;
			/*int index = mf.getOriginalFilename().lastIndexOf(".");
			String filename = mf.getOriginalFilename().substring(0, index);
			String extendname = mf.getOriginalFilename().substring(index);*/
			try {
	            is = mf.getInputStream();
	            fos = new FileOutputStream(FileUtils.checkExist(path +File.separator + mf.getOriginalFilename()));
	            IOUtils.copy(is, fos);
	            res.setStatus(HttpServletResponse.SC_OK);
	            writer.print("{success: true}");
	        } catch (FileNotFoundException ex) {
	            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
	        }catch (IOException ex) {
	        	res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
	        } catch (Exception e) {
	        	res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
			}  finally {
	            try {
	                fos.close();
	                is.close();
	            } catch (IOException ignored) {
	            }
	        }
			/*try {
				mf.getFileItem().write(file);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		 writer.flush();
	     writer.close();
	}  
	
	@RequestMapping("singlefileuploadflash.do")  
	public void singleFileUploadFlash(HttpServletRequest req, HttpServletResponse res) {
		//path = path + File.separator + filename; // 获取本地存储路径
		String sessionId = req.getSession().getId();
		String path = req.getSession().getServletContext().getRealPath(File.separator+"upload"+File.separator);
		path += File.separator+sessionId+File.separator;
		
		MultipartHttpServletRequest mul = (MultipartHttpServletRequest) req;
		Iterator<String> it = mul.getFileNames();
		while(it.hasNext()){
			String name = it.next();
			List<MultipartFile> files = mul.getFiles(name);
			try {
				for (int i = 0; i < files.size(); i++) {
					CommonsMultipartFile mf = (CommonsMultipartFile) files.get(i);
					String fileName = mf.getOriginalFilename();
					File file = FileUtils.checkExist(path + fileName);
					mf.getFileItem().write(file);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}  
	
	@SuppressWarnings("unused")
	@RequestMapping("multiupload.do")  
	public void multiUpload(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value="qqfile", required=true) String filename, @RequestParam(value="path", required=true) String path) {
		//path = path + File.separator + filename; // 获取本地存储路径
		
		PrintWriter writer = null;
	    InputStream is = null;
	    FileOutputStream fos = null;

	      try {
	            writer = res.getWriter();
	        } catch (IOException ex) {
	            //log(OctetStreamReader.class.getName() + "has thrown an exception: " + ex.getMessage());
	        }
	        
	        int index = filename.lastIndexOf(".");
			String prefix = filename.substring(0, index);
			String ext = filename.substring(index);
			
	      try {
	            is = req.getInputStream();
	            fos = new FileOutputStream(FileUtils.checkExist(path +File.separator + filename));
	            IOUtils.copy(is, fos);
	            res.setStatus(HttpServletResponse.SC_OK);
	            writer.print("{success: true}");
	        } catch (FileNotFoundException ex) {
	            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
	        }catch (IOException ex) {
	        	res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
	        } catch (Exception e) {
	        	res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
			}  finally {
	            try {
	                fos.close();
	                is.close();
	            } catch (IOException ignored) {
	            }
	        }
	      writer.flush();
	      writer.close();
	}  
	
    
}
