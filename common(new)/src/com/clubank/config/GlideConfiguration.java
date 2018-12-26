package com.clubank.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by fengyq on 2016/11/14.
 */
public class GlideConfiguration  implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录，磁盘缓存给150M
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR, 150 * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

}