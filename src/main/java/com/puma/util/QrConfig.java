package com.puma.util;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrConfig {

/*	public static String IMAGE_JPEG = "image/jpeg";
	public static String IMAGE_PNG = "image/png";
	public static String IMAGE_GIF = "image/gif";*/
	//输出数据
	String data = "PUMA Framework";
	
	//输出图片类型
	String output = "jpg";

	int type = 0;

	int margin = 5;

	int cellSize = 3;
	
	int imageSize = 150;

	String errorCorrectLevel = "L";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	public String getErrorCorrectLevel() {
		return errorCorrectLevel;
	}

	public void setErrorCorrectLevel(String errorCorrectLevel) {
		this.errorCorrectLevel = errorCorrectLevel;
	}

	public int getImageSize() {
		return imageSize;
	}

	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	
	
	
}
