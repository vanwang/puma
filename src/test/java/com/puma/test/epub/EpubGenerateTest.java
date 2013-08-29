package com.puma.test.epub;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Date;
import nl.siegmann.epublib.domain.Identifier;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

public class EpubGenerateTest {
	public static void main(String[] args) {
		File f = new File("D:\\ctest\\test.epub");
		try {
			readEpub(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*try {
			Book book = new Book();
			
			book.getMetadata().addTitle("书名");
			
			book.getMetadata().addIdentifier(new Identifier(Identifier.Scheme.ISBN, "987654321"));
			book.getMetadata().addAuthor(new Author("汪凡", "Tester"));
			book.getMetadata().addDescription("description文字说明");
			book.getMetadata().addDate(new Date(new java.util.Date()));
			book.getMetadata().addPublisher("中国人民出版社");
			List<String> subjects = new ArrayList<String>();
			subjects.add("subject文字说明");//标签
			book.getMetadata().setSubjects(subjects);
			
			book.getMetadata().setLanguage(Locale.SIMPLIFIED_CHINESE.toString().replace("_", "-").toLowerCase());
			book.addResource(new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/book1.css"), "book1.css"));
			book.setCoverPage(new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/cover.html"), "cover.html"));
			book.setCoverImage(new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/cover.png"), "cover.png"));
			book.addSection("Chapter 1", new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/chapter1.html"), "chapter1.html"));
			TOCReference chapter2 = book.addSection("Second chapter", new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/chapter2.html"), "chapter2.html"));
			book.addResource(new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/flowers_320x240.jpg"), "media/flowers.jpg"));
			book.addSection(chapter2, "Chapter 2 section 1", new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/chapter2_1.html"), "chapter2_1.html"));
			book.addSection("Chapter 3", new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/chapter3.html"), "chapter3.html"));

			// Create EpubWriter
			EpubWriter epubWriter = new EpubWriter();

			// Write the Book as Epub
			epubWriter.write(book, new FileOutputStream("D:\\aa.epub"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public static void readEpub(File epubFile) throws FileNotFoundException, IOException{
		Book book = new EpubReader().readEpub(new FileInputStream(epubFile));
		book.setCoverPage(new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/cover.html"), "cover.html"));
		book.setCoverImage(new Resource(EpubGenerateTest.class.getResourceAsStream("/book1/cover.png"), "cover.png"));
		EpubWriter epubWriter = new EpubWriter();
		// Write the Book as Epub
		epubWriter.write(book, new FileOutputStream("D:\\ctest\\recreated.epub"));
	}
}