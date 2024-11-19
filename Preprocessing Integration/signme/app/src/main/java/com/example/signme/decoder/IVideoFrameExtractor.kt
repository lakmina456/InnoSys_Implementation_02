package com.example.signme.decoder

/**
 * Created by Duc Ky Ngo on 9/13/2021.
 * duckyngo1705@gmail.com
 */
interface IVideoFrameExtractor {
    fun onCurrentFrameExtracted(currentFrame: Frame)
    fun onAllFrameExtracted(processedFrameCount: Int, processedTimeMs: Long)
}