package com.qinxin.common.util

import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.qinxin.common.App

object CoilUtil {
    private var imageLoader: ImageLoader? = null
    fun getImageLoader(): ImageLoader {
        if (imageLoader == null) {
            imageLoader = ImageLoader.Builder(App.instance!!)
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                    add(VideoFrameDecoder.Factory())
                }
                .memoryCache {
                    MemoryCache.Builder(App.instance!!)
                        .maxSizePercent(0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(App.instance!!.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .build()
        }
        return imageLoader!!
    }
}