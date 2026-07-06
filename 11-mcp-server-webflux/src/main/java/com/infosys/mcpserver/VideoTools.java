package com.infosys.mcpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoTools {
    private static final Logger log = LoggerFactory.getLogger(VideoTools.class);

    private final VideoRepository videoRepository;

    public VideoTools(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Tool(name = "get_all_infosys_videos", description = "Get all videos from Infosys")
    public List<Video> getAllVideos() {
        log.info("Getting all videos");
        return videoRepository.findAll();
    }

    @Tool(name = "search_infosys_videos", description = "Search Infosys videos by title")
    public List<Video> searchVideos(String title) {
        log.info("Searching videos by title: {}", title);
        return videoRepository.findByTitleContainingIgnoreCase(title);
    }

    @Tool(name = "get_infosys_video_by_title", description = "Get a single video from Infosys by title")
    public Video getVideoByTitle(String title) {
        log.info("Getting video by title: {}", title);
        return videoRepository.findByTitle(title).orElse(null);
    }

}
