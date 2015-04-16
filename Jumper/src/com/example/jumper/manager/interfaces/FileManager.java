package com.example.jumper.manager.interfaces;

import java.io.IOException;

public interface FileManager {
	String read(String fileName, String fileDescription) throws IOException;
	void write(String fileName, String contents, boolean append) throws IOException;
}
