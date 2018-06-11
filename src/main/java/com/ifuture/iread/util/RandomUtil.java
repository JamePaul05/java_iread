package com.ifuture.iread.util;

import java.util.Random;

public class RandomUtil {
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String numberChar = "0123456789";

	public static String generateMixString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));

		}
		return sb.toString();
	}

	public static String generateNumberString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
		}
		return sb.toString();
	}

	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("generateMixString(6)");

		for (int i = 0; i < 6; i++) {
			System.out.println(generateMixString(6));
		}

		System.out.println("generateNumberString(6)");
		for (int i = 0; i < 6; i++) {
			System.out.println(generateNumberString(6));
		}
	}

}
