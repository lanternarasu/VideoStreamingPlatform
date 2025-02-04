package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Service
public class UploadVideoImpl implements UploadVideo {

	@Override
	public String getUncompressedVideo(byte[] video) throws IOException {
		
		String filePath = writeVideoIntoRaw(video);
		
		String filePath2 = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\handleraw\\handler.mp4";
		String ffmpegPath = "C:\\ffmpeg\\ffmpeg.exe";

		FFmpeg fFmpeg = new FFmpeg(ffmpegPath);

		FFmpegBuilder builder = new FFmpegBuilder();
		
		builder.addInput(filePath).addOutput(filePath2);
		
		FFmpegExecutor executor = new FFmpegExecutor(fFmpeg);
		    
		executor.createJob(builder).run();
		
		File file = new File(filePath);
		file.delete();
		
		return filePath2;
	}

	@Override
	public String writeVideoIntoRaw(byte[] video) throws IOException {
		String filepath = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\handleraw\\handler.raw";
		try (FileOutputStream fileOutputStream = new FileOutputStream(filepath)) {
			fileOutputStream.write(video);
		}
		return filepath;		
	}

	@Override
	public String uploadVideoToDB(String filePath) {
		// TODO Auto-generated method stub
		return null;
	}

	//this func creates a base folder with videoId and 5 folders for different resolution in it
	@Override
	public String createDirectory(String videoId) {
		// TODO try to use threads here
		String basePath = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\server\\" + videoId;
		
		boolean isBaseDirCreated = new File(basePath).mkdir();
		if(isBaseDirCreated){
			
			String[] output_versions_folder =new String[5];
			
			output_versions_folder[0] = "//240p_output";
			output_versions_folder[1] = "//360p_output";
			output_versions_folder[2] = "//480p_output";
			output_versions_folder[3] = "//720p_output";
			output_versions_folder[4] = "//1080p_output";
			
			for(int i=0;i<output_versions_folder.length;i++) {
				new File(basePath+output_versions_folder[i]).mkdir();
			}
			
			return "Directory created success";

		}
		else {
			return "Directory not created!! Upload Failed!!!";
		}
	}

	//${funcName} and deletes the uncompressed video
	@Override
	public void doesVideoEncodingTranscodingAndSegments(String filePathUCV,String videoId) throws IOException, InterruptedException {
		// TODO try using threads
		
		System.out.println("in doesVideoEncodingTranscodingAndSegments");
		
		String ffmpeg = "C:\\ffmpeg\\ffmpeg.exe";
        String input = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\handleraw\\handler.mp4";
        String[] resolution = {"\"scale=426:240\"","\"scale=640:360\"","\"scale=854:480\"","\"scale=1280:720\"","\"scale=1920:1080\""};
        String videoEncoder = "libx264";
        String audioEncoder = "aac";
        String compatibilityCheck = "-2";
        String segmentStartingNumber = "0";
        String segmentDuration = "10";
        String includeAllSegments = "0";
        String outputFormat = "hls";
        String baseOutputFile = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\server\\";
        String[] outputFile = {
        					   baseOutputFile + videoId + "\\240p_output\\240p_output.m3u8",
        					   baseOutputFile + videoId + "\\360p_output\\360p_output.m3u8",
        					   baseOutputFile + videoId + "\\480p_output\\480p_output.m3u8",
        					   baseOutputFile + videoId + "\\720p_output\\720p_output.m3u8",
        					   baseOutputFile + videoId + "\\1080p_output\\1080p_output.m3u8"
        					   };

        List<List<String>> command = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) 
        {
            command.add(Arrays.asList(ffmpeg,"-i",input,"-vf",resolution[i],"-codec:v",videoEncoder,"-codec:a",audioEncoder,"-strict",compatibilityCheck,"-start_number",segmentStartingNumber,"-hls_time",segmentDuration,"-hls_list_size",includeAllSegments,"-f",outputFormat,outputFile[i]));
		}
   
        for(int i=0;i<command.size();i++) {
    		System.out.println(command.get(i));
        }
	
        for(int i=0;i<command.size();i++) {
        	ProcessBuilder builder = new ProcessBuilder(command.get(i));
            builder.redirectErrorStream(true); // Merge error stream with standard output
            Process process = builder.start();
    		
    		// Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Read the error stream (important for debugging)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("FFmpeg prcoess "+ i + " exited with code: " + exitCode);
            
            if(i==4) {
                
            File file = new File(filePathUCV);
      		file.delete();
            }
        }
	}

	@Override
	public void deleteVideo(String videoId) throws IOException {
		// TODO Auto-generated method stub
		String destination = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\server\\";
		FileUtils.forceDelete(new File(destination+videoId));
		
	}

	@Override
	public void createMasterPlaylist(String videoId) throws IOException {
		// TODO Auto-generated method stub
		String basePath = "C:\\Users\\2380256\\git\\repository2\\VideoStreamingPlatform\\src\\main\\resources\\videos\\server\\";
		FileWriter writer = new FileWriter(basePath+videoId+"\\master.m3u8");
		writer.write("#EXTM3U\r\n"
				+ "#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=426x240\r\n"
				+ "240p_output/240p_output.m3u8\r\n"
				+ "#EXT-X-STREAM-INF:BANDWIDTH=1400000,RESOLUTION=640x360\r\n"
				+ "360p_output/360p_output.m3u8\r\n"
				+ "#EXT-X-STREAM-INF:BANDWIDTH=2000000,RESOLUTION=854x480\r\n"
				+ "480p_output/480p_output.m3u8\r\n"
				+ "#EXT-X-STREAM-INF:BANDWIDTH=2800000,RESOLUTION=1280x720\r\n"
				+ "720p_output/720p_output.m3u8\r\n"
				+ "#EXT-X-STREAM-INF:BANDWIDTH=5000000,RESOLUTION=1920x1080\r\n"
				+ "1080p_output/1080p_output.m3u8\r\n"
				);
		writer.close();
	}
}
