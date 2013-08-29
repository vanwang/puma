package com.puma.book.epub;

import java.io.File;
import java.util.Date;
import java.util.Locale;

public class EpubConfig {
	
	//书名
	private String title;
		
	//作者
	private String author;
	
	//书籍简介
	private String description;
	
	//出版社
	private String publisher;
	
	//出版时间
	private Date publishdate;
	
	//标签
	private String tag;
	
	//语言
	private String locale;
	
	//封面图片
	private File coverimg;
	
	//封面页面
	private File coverpage;
	
	public EpubConfig() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getPublishdate() {
		return publishdate;
	}

	public void setPublishdate(Date publishdate) {
		this.publishdate = publishdate;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public File getCoverimg() {
		return coverimg;
	}

	public void setCoverimg(File coverimg) {
		this.coverimg = coverimg;
	}

	public File getCoverpage() {
		return coverpage;
	}

	public void setCoverpage(File coverpage) {
		this.coverpage = coverpage;
	}
	
}
