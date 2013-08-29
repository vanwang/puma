package com.puma.core.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.puma.util.SpringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service("html.staticservice")
@Singleton
public class HtmlService{

	public final static String ARTICLES_TEMPLATE_FILEPATH = "front/articles.ftl";
	public final static String ARTICLES_HTML_FILEPATH = "/site/articles/"; 
	public final static String ARTICLE_TEMPLATE_FILEPATH = "front/article.ftl";
	public final static String ARTICLE_HTML_FILEPATH = "/site/articles/single/"; 
	public final static String CATEGORY_TEMPLATE_FILEPATH = "front/categories.ftl";
	public final static String CATEGORY_HTML_FILEPATH = "/site/articles/category/"; 
	public final static String INDEX_TEMPLATE_FILEPATH = "front/index.ftl";
	public final static String INDEX_HTML_FILEPATH = "/site/"; 
	
	public final static String REGISTER_SUCC_MAIL_TEMPLATE = "regverifymail.ftl"; 
	
	@Autowired
	private ServletContext servletContext;
	
	public void buildHtml(String templateFilePath, String htmlFilePath, Map<String, Object> data) {
		try {
			FreeMarkerConfigurer freemarkerConfig = (FreeMarkerConfigurer)SpringUtils.getBean("freemarkerConfig");
			Configuration configuration = freemarkerConfig.getConfiguration();
			Template template = configuration.getTemplate(templateFilePath);
			File htmlFile = new File(servletContext.getRealPath(htmlFilePath));
			File htmlDirectory = htmlFile.getParentFile();
			if (!htmlDirectory.exists()) {
				htmlDirectory.mkdirs();
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
			template.process(data, out);
			out.flush();
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String templateToString(Map<String, Object> data) {
		StringBuffer result = new StringBuffer();
		try {
			FreeMarkerConfigurer freemarkerConfig = (FreeMarkerConfigurer)SpringUtils.getBean("freemarkerConfig");
			Configuration configuration = freemarkerConfig.getConfiguration();
			Template template = configuration.getTemplate(REGISTER_SUCC_MAIL_TEMPLATE);
			StringWriter out = new StringWriter();
			template.process(data, out);
			out.flush();
			out.close();
			
			result = out.getBuffer( ); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
}
