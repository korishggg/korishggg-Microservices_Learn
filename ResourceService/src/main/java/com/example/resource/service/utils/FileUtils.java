package com.example.resource.service.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

	public static File convertInputStreamToFile(InputStream inputStream, String fileName) throws IOException {
		File file = new File(fileName);
		try (OutputStream outputStream = new FileOutputStream(file)) {
			byte[] buffer = new byte[4096];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		}
		return file;
	}
}
