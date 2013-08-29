package com.puma.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import com.d_project.qrcode.ErrorCorrectLevel;
import com.d_project.qrcode.QRCode;
import com.d_project.qrcode.web.GIFImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRUtils {
	
	public static void main(String[] args) {
		QrConfig qc = new QrConfig();
		qc.setType(1);
		qc.setData("http://www.baidu.com/s?ie=utf-8&bs=java+QRCode&f=8&rsv_bp=1&wd=java+QR+d+project&rsv_sug3=7&rsv_sug1=7&rsv_sug4=123&inputT=4354");
		try {
			new QRUtils().generate(qc, "D:\\qrcode.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generate(QrConfig config, HttpServletRequest req, HttpServletResponse res)
			throws UnsupportedEncodingException, IOException, ServletException {
		res.setCharacterEncoding("UTF-8");
		ServletOutputStream out = res.getOutputStream();
		generate(config, out);
	}
	
	public void generate(QrConfig config, String outputFileString) throws Exception {
		FileOutputStream out = new FileOutputStream(FileUtils.checkExist(outputFileString));
        generate(config, out);
	}
	
	private void generate(QrConfig config, OutputStream out) throws IOException {
		int type = config.getType();
		if (type < 0 || 10 < type) {
			return;
		}

		int margin = config.getMargin();
		if (margin < 0 || 32 < margin) {
			return;
		}
		int cellSize = config.getCellSize();
		if (cellSize < 1 || 4 < cellSize) {
			return;
		}
		int errorCorrectLevel = 0;
		try {
			errorCorrectLevel = parseErrorCorrectLevel(config.getErrorCorrectLevel());
		} catch (Exception e) {
			return;
		}

		com.d_project.qrcode.QRCode qrcode = null;
		try {
			qrcode = getQRCode(config.getData(), type, errorCorrectLevel);
		} catch (Exception e) {
			return;
		}

		BufferedImage image = qrcode.createImage(cellSize, margin);
		if(config.getOutput().equalsIgnoreCase("jpg")){
			try {
				ImageIO.write(image, "jpg", out);
			} finally {
				out.close();
			}
		}else if(config.getOutput().equalsIgnoreCase("png")){
			try {
				ImageIO.write(image, "png", out);
			} finally {
				out.close();
			}
		}else if(config.getOutput().equalsIgnoreCase("gif")){
			try {
				ImageIO.write(image, "gif", out);
			} finally {
				out.close();
			}
		}
	}
	

	private static int parseErrorCorrectLevel(String ecl) {
		if ("L".equals(ecl)) {
			return ErrorCorrectLevel.L;
		} else if ("Q".equals(ecl)) {
			return ErrorCorrectLevel.Q;
		} else if ("M".equals(ecl)) {
			return ErrorCorrectLevel.M;
		} else if ("H".equals(ecl)) {
			return ErrorCorrectLevel.H;
		} else {
			return ErrorCorrectLevel.L;
			//throw rc.error("qr_error_correct_error");
		}

	}


	private static QRCode getQRCode(String text, int typeNumber,
			int errorCorrectLevel) {
		if (typeNumber == 0) {
			return QRCode.getMinimumQRCode(text, errorCorrectLevel);
		} else {
			QRCode qr = new QRCode();
			qr.setTypeNumber(typeNumber);
			qr.setErrorCorrectLevel(errorCorrectLevel);
			qr.addData(text);
			qr.make();
			return qr;
		}
	}

	/*private static GIFImage createGIFImage(QRCode qrcode, int cellSize,
			int margin) throws IOException {

		int imageSize = qrcode.getModuleCount() * cellSize + margin * 2;

		GIFImage image = new GIFImage(imageSize, imageSize);

		for (int y = 0; y < imageSize; y++) {

			for (int x = 0; x < imageSize; x++) {

				if (margin <= x && x < imageSize - margin && margin <= y
						&& y < imageSize - margin) {

					int col = (x - margin) / cellSize;
					int row = (y - margin) / cellSize;

					if (qrcode.isDark(row, col)) {
						image.setPixel(x, y, 0);
					} else {
						image.setPixel(x, y, 1);
					}

				} else {
					image.setPixel(x, y, 1);
				}
			}
		}

		return image;
	}*/
	
	/*public void generate(QrConfig config, HttpServletRequest req, HttpServletResponse res)
			throws UnsupportedEncodingException, IOException, ServletException {
		res.setCharacterEncoding("UTF-8");

		String text = config.getData();  
        String format = config.getOutput();  
        Hashtable hints= new Hashtable();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        hints.put(EncodeHintType.ERROR_CORRECTION,  config.getErrorCorrectLevel());  
        hints.put(EncodeHintType.MARGIN, config.getMargin());  
         BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, config.getImageSize(), config.getImageSize(), hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}  

		try {
			 MatrixToImageWriter.writeToStream(bitMatrix, format, res.getOutputStream());  
		} finally {
		}

	}*/
	
	/*public void generate(QrConfig config, String outputFileString)
			throws Exception {

		String text = config.getData();  
        String format = config.getOutput();  
        Hashtable hints= new Hashtable();  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        hints.put(EncodeHintType.ERROR_CORRECTION,  config.getErrorCorrectLevel());  
        hints.put(EncodeHintType.MARGIN, config.getMargin());  
         BitMatrix bitMatrix = null;
         
         FileOutputStream fos = null;
         fos = new FileOutputStream(FileUtils.checkExist(outputFileString));
		try {
			bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, config.getImageSize(), config.getImageSize(), hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}  

		try {
			 MatrixToImageWriter.writeToStream(bitMatrix, format, fos);  
		} finally {
			fos.close();
		}

	}*/

	/*public void decode(url) throws IOException {  
        //存在qrcode的网址  
                String url = rc.param("url", "");  
                //待解码的qrcdoe图像  
                File img = rc.file("qrcode");  
        if (StringUtils.isBlank(url) && img == null) {  
            throw rc.error("qr_upload_or_url_null");  
        }  
  
        List<Result> results = new ArrayList<Result>();  
        Config config = new Config();  
        Inputs inputs = new Inputs();  
  
        config.setHints(buildHints(config));  
  
        if (StringUtils.isNotBlank(url)) {  
            addArgumentToInputs(url, config, inputs);  
        }  
        if (img != null) {  
            inputs.addInput(img.getCanonicalPath());  
        }  
        while (true) {  
            String input = inputs.getNextInput();  
            if (input == null) {  
                break;  
            }  
            File inputFile = new File(input);  
            if (inputFile.exists()) {  
                try {  
                    Result result = decode(inputFile.toURI(), config,rc);  
                    results.add(result);  
                } catch (IOException e) {  
                }  
            } else {  
                try {  
                    Result result = decode(new URI(input), config,rc);  
                    results.add(result);  
                } catch (Exception e) {  
                }  
            }  
        }  
    }  
  
    private Result decode(URI uri,Config config,RequestContext rc)  
            throws IOException {  
        Map<DecodeHintType, ?> hints = config.getHints();  
        BufferedImage image;  
        try {  
            image = ImageIO.read(uri.toURL());  
        } catch (IllegalArgumentException iae) {  
            throw rc.error("qr_resource_not_found");  
        }  
        if (image == null) {  
            throw rc.error("qr_could_not_load_image");  
        }  
        try {  
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
            Result result = new MultiFormatReader().decode(bitmap, hints);  
            return result;  
        } catch (NotFoundException nfe) {  
            throw rc.error("qr_no_barcode_found");  
        }  
    }  
  
    private static Map<DecodeHintType, ?> buildHints(Config config) {  
        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(  
                DecodeHintType.class);  
        Collection<BarcodeFormat> vector = new ArrayList<BarcodeFormat>(8);  
        vector.add(BarcodeFormat.UPC_A);  
        vector.add(BarcodeFormat.UPC_E);  
        vector.add(BarcodeFormat.EAN_13);  
        vector.add(BarcodeFormat.EAN_8);  
        vector.add(BarcodeFormat.RSS_14);  
        vector.add(BarcodeFormat.RSS_EXPANDED);  
        if (!config.isProductsOnly()) {  
            vector.add(BarcodeFormat.CODE_39);  
            vector.add(BarcodeFormat.CODE_93);  
            vector.add(BarcodeFormat.CODE_128);  
            vector.add(BarcodeFormat.ITF);  
            vector.add(BarcodeFormat.QR_CODE);  
            vector.add(BarcodeFormat.DATA_MATRIX);  
            vector.add(BarcodeFormat.AZTEC);  
            vector.add(BarcodeFormat.PDF_417);  
            vector.add(BarcodeFormat.CODABAR);  
            vector.add(BarcodeFormat.MAXICODE);  
        }  
        hints.put(DecodeHintType.POSSIBLE_FORMATS, vector);  
        if (config.isTryHarder()) {  
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);  
        }  
        if (config.isPureBarcode()) {  
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);  
        }  
        return hints;  
    }  
  
    private static void addArgumentToInputs(String argument, Config config,  
            Inputs inputs) throws IOException {  
        File inputFile = new File(argument);  
        if (inputFile.exists()) {  
            inputs.addInput(inputFile.getCanonicalPath());  
        } else {  
            inputs.addInput(argument);  
        }  
    }  */
}
