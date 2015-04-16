package com.example.jumper.manager.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.jumper.MainActivity;
import com.example.jumper.manager.interfaces.FileManager;

public class FileManagerImpl implements FileManager {
	private static String m_currentDirectory = MainActivity.getAppContext().getFilesDir().getAbsolutePath();

	@Override
	public String read(String fileName, String fileDescription) throws IOException {
		File file = new File(m_currentDirectory + "/" + fileName);
		int length = (int) file.length();

		byte[] bytes = new byte[length];

		FileInputStream in = new FileInputStream(file);

		try
		{
			in.read(bytes);
		}
		finally
		{
			in.close();
		}

		return new String(bytes);
	}

	@Override
	public void write(String fileName, String contents, boolean append) throws IOException {
		File file = new File(m_currentDirectory + "/" + fileName);

		FileOutputStream fileStream = new FileOutputStream(file, append);

		try
		{
			if (append)
			{
				fileStream.write(System.getProperty("line.separator").getBytes());
			}

			fileStream.write(contents.getBytes());
		}
		finally
		{
			fileStream.close();
		}
	}
}
