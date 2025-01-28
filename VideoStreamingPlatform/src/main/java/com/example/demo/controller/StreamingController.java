package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

@RestController
@RequestMapping("/application")
public class StreamingController {

	@PostMapping("/upload")
	public String uploadVideo(@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("metadata") String metadata) {
		if (!multipartFile.isEmpty()) {
			try {
				byte[] bytes = multipartFile.getBytes();

				JSONObject jsonMetadata = new JSONObject("metadata");

				String videoId = jsonMetadata.getString("videoId");
				String videoName = jsonMetadata.getString("videoName");
				String videoDescription = jsonMetadata.getString("videoDescription");

				return "File and metadata uploaded successfully!";
			} catch (Exception e) {
				return "Failed to upload file and metadata: " + e.getMessage();
			}
		} else {
			return "File is empty!";
		}
	}
}
