package com.puma.test.fileconverter;

import java.nio.charset.Charset;

import com.aspose.words.Document;
import com.aspose.words.DocumentSplitCriteria;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.SaveFormat;

public class AposeToEpub {
	
	public static void main(String[] args) {
		String docFilePath = "D:\\ctest\\test.doc";
		String epubDir = "D:\\ctest\\test.docx";
		try {
			new AposeToEpub().convert(docFilePath, epubDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void convert(String docFilePath, String epubDir) throws Exception{
		Document doc = new Document(docFilePath);
		doc.save(epubDir);
	}

	public void convert2(String docFilePath, String epubDir) throws Exception{
		 // Open an existing document from disk.
		Document doc = new Document(docFilePath);

		// Create a new instance of HtmlSaveOptions. This object allows us to set options that control
		// how the output document is saved.
		HtmlSaveOptions saveOptions =
		        new HtmlSaveOptions();

		// Specify the desired encoding.
		saveOptions.setEncoding(Charset.forName("UTF-8"));

		// Specify at what elements to split the internal HTML at. This creates a new HTML within the EPUB
		// which allows you to limit the size of each HTML part. This is useful for readers which cannot read
		// HTML files greater than a certain size e.g 300kb.
		saveOptions.setDocumentSplitCriteria(DocumentSplitCriteria.HEADING_PARAGRAPH);

		// Specify that we want to export document properties.
		saveOptions.setExportDocumentProperties(true);

		// Specify that we want to save in EPUB format.
		saveOptions.setSaveFormat(SaveFormat.EPUB);

		// Export the document as an EPUB file.
		doc.save(epubDir, saveOptions);
	}
}
