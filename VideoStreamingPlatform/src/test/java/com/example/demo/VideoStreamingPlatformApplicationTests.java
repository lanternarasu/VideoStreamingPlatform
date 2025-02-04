package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.*;

@SpringBootTest
class VideoStreamingPlatformApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void checkProcessesIfExecute() throws IOException {
		Runtime.getRuntime().exec("notepad.exe");
	}
	
	@Test
	void checkIfFfmpegWorks() throws IOException {
		
		String ffmpegPath = "C:\\ffmpeg\\ffmpeg.exe";
		String inputFilePath = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\ZZZ2_720p.mp4"; // Path to your input video file
		String outputFilePath = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\output.mp4"; // Path to your output video file
		
		FFmpeg fFmpeg = new FFmpeg(ffmpegPath);
		
		FFmpegBuilder builder = new FFmpegBuilder();
	    builder.addInput(inputFilePath).overrideOutputFiles(true).addOutput(outputFilePath);
	     
	    FFmpegExecutor executor = new FFmpegExecutor(fFmpeg);
	    
	    executor.createJob(builder).run();
	}
	
	@Test
	void createDirectory() {
		String videoId = "ZZZ2_720p";
		String basePath = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\server\\" + videoId;
		
		boolean file = new File(basePath).mkdir();
	}
}
