package com.mc.app.hotel.common.facealignment.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class FileMD5 {

	private static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static String getMD5(File file) {
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[2048];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			byte[] b = md.digest();
			return byteToHexString(b);
		} catch (Exception e) {
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
		return "";
	}

	public static String getMD5(byte[] source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			return byteToHexString(tmp);
		} catch (Exception e) {
		}
		return "";
	}

	public static String byteToHexString(byte[] tmp) {

		String s;

		// 用字节表示就是 16 个字节

		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，

		// 所以表示成 16 进制需要 32 个字符

		int k = 0; // 表示转换结果中对应的字符位置

		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节

			// 转换成 16 进制字符的转换

			byte byte0 = tmp[i]; // 取第 i 个字节

			str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,

			// >>> 为逻辑右移，将符号位一起右移

			str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换

		}

		s = new String(str); // 换后的结果转换为字符串

		return s;

	}

}
