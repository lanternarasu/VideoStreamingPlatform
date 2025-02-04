package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.UploadVideoImpl;

@RestController
@RequestMapping("/upload")
public class UploadController {
	@Autowired
	UploadVideoImpl uploadVideoImpl;

	@PostMapping("/uploadVideo")
	public String uploadVideo(@RequestParam("file") MultipartFile multipartFile,
			@RequestParam String videoId,
			@RequestParam String videoName,
			@RequestParam String videoDescription) {
		if (!multipartFile.isEmpty()) {
			try {
				byte[] bytes = multipartFile.getBytes();
				String doesDirectoryCreated = uploadVideoImpl.createDirectory(videoId);
				String filePathUCV = uploadVideoImpl.getUncompressedVideo(bytes);
				uploadVideoImpl.doesVideoEncodingTranscodingAndSegments(filePathUCV,videoId);
				uploadVideoImpl.createMasterPlaylist(videoId);
				return "File and metadata uploaded successfully!";
			} catch (Exception e) {
				return "Failed to upload file and metadata: " + e.getMessage();
			}
		} else {
			return "File is empty!";
		}
	}
	
	@DeleteMapping("deleteVideo")
	public void deleteVideo(@RequestParam String videoId) throws IOException {
		uploadVideoImpl.deleteVideo(videoId);
	}
	
	@PostMapping("createMasterPlaylist")
	public void createMasterPlaylist(@RequestParam String videoId) throws IOException {
		uploadVideoImpl.createMasterPlaylist(videoId);
	}
}
