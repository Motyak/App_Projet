package com.ceri.projet;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideBny {

    static private Activity act;
    static private View view;
    static private String url;
    static private ImageView iv;
    static private Center center;

    public static void loadFromWeb(Activity act, String url, ImageView iv, GlideBny.Center center) {
        GlideBny.act = act;
        GlideBny.view = null;
        GlideBny.url = url;
        GlideBny.iv = iv;
        GlideBny.center = center;

        if(center == Center.CROP)
            Glide.with(act).load(url).centerCrop().into(iv);
        else if(center == Center.FIT)
            Glide.with(act).load(url).fitCenter().into(iv);
    }

    public static void loadFromWeb(View view, String url, ImageView iv, GlideBny.Center center) {
        GlideBny.act = null;
        GlideBny.view = view;
        GlideBny.url = url;
        GlideBny.iv = iv;
        GlideBny.center = center;

        if(center == Center.CROP)
            Glide.with(view).load(url).centerCrop().into(iv);
        else if(center == Center.FIT)
            Glide.with(view).load(url).fitCenter().into(iv);
    }

    public static void loadFromCache(Activity act, String url, ImageView iv, GlideBny.Center center) {
        if(center == Center.CROP) {
            Glide.with(act)
                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(url)
                    .centerCrop()
                    .listener(GlideBny.requestListener)
                    .into(iv);
        }
        else if(center == Center.FIT) {
            Glide.with(act)
                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(url)
                    .fitCenter()
                    .listener(GlideBny.requestListener)
                    .into(iv);
        }
    }

    public static void loadFromCache(View view, String url, ImageView iv, GlideBny.Center center) {
        if(center == Center.CROP) {
            Glide.with(view)
                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(url)
                    .centerCrop()
                    .into(iv);
        }
        else if(center == Center.FIT) {
            Glide.with(view)
                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(url)
                    .fitCenter()
                    .into(iv);
        }
    }

    public static void saveInCache(Activity act, String url) {
        Glide.with(act).load(url).submit();
    }

    public static void saveInCacheResource(Activity act, int resId) {
        Glide.with(act).load(resId).submit();
    }

    public static void saveInCache(View view, String url) {
        Glide.with(view).load(url).submit();
    }

    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            System.out.println("Error loading from cache, trying to download from web..");

            if(GlideBny.act == null) {
                GlideBny.saveInCache(GlideBny.view, GlideBny.url);
                GlideBny.loadFromCache(GlideBny.view, GlideBny.url, GlideBny.iv, GlideBny.center);
            }

            else if(GlideBny.view == null) {
                GlideBny.saveInCache(GlideBny.act, GlideBny.url);
                GlideBny.loadFromCache(GlideBny.act, GlideBny.url, GlideBny.iv, GlideBny.center);
            }

            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    };

    public enum Center {
        CROP,
        FIT
    }

}
