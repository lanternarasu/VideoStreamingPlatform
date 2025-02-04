package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface UploadVideo {
 public String writeVideoIntoRaw(byte[] video) throws FileNotFoundException, IOException;
 public String getUncompressedVideo(byte[] video) throws IOException;
 public String uploadVideoToDB(String filePath);
 public String createDirectory(String videoId);
 public void doesVideoEncodingTranscodingAndSegments(String filePathUCV,String videoId) throws IOException, InterruptedException;
 public void deleteVideo(String videoId) throws IOException;
 public void createMasterPlaylist(String videoId) throws IOException;
 
}
