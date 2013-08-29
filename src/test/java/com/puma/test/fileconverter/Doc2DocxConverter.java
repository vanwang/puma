package com.puma.test.fileconverter;


import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.docx4j.convert.in.Doc;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

/**
 * @author jason
 *
 */
public class Doc2DocxConverter {

	private static Logger log = Logger.getLogger(Doc2DocxConverter.class);

	public static void main(String[] args) throws Exception {
				
		String localPath = "D:\\ctest\\test.doc";
				
		WordprocessingMLPackage out = Doc.convert(new FileInputStream(localPath));
		
		String outputfilepath = "D:\\ctest\\test.docx";		
		
		SaveToZipFile saver = new SaveToZipFile(out);
		saver.save(outputfilepath);
		
		//new EpubConverterManager().convert(outputfilepath,"D:\\");
		log.info("Done - saved docx as " + outputfilepath);
		
		
	}	
}
