package com.weibo.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectIoUtil {
	public static Object readObject(String file_path) throws Exception {
		FileInputStream input = new FileInputStream(file_path);
		ObjectInputStream objectInput = new ObjectInputStream(input);
		Object bloomFilter = objectInput.readObject();
		objectInput.close();
		input.close();
		return bloomFilter;
	}

	public static boolean writeObject(String file_path, Object bloom)
			throws Exception {
		FileOutputStream output = new FileOutputStream(file_path);
		ObjectOutputStream objectOutput = new ObjectOutputStream(output);
		try {
			objectOutput.writeObject(bloom);
			objectOutput.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static byte[] b = new byte[1024 * 5];

	public static boolean fileBak(String sourceFile, String destinationFile)
			throws Exception {
		FileInputStream input = new FileInputStream(sourceFile);
		FileOutputStream output = new FileOutputStream(destinationFile);
		try {
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
