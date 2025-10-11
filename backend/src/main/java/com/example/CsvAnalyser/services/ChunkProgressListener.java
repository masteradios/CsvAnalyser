package com.example.CsvAnalyser.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ChunkProgressListener implements ChunkListener {

    private static final Logger log = LoggerFactory.getLogger(ChunkProgressListener.class);
    private int chunkCount = 0;

    @Override
    public void beforeChunk(ChunkContext context) { }

    @Override
    public void afterChunk(ChunkContext context) {
        chunkCount++;
        log.info("Chunk " + chunkCount + " completed successfully.");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.error("❌ Error occurred in chunk " + chunkCount);
    }
}

