package com.ceri.projet;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideBny {

    public static void loadFromWeb(Activity act, String url, ImageView iv, GlideBny.Center center) {
        if(center == Center.CROP)
            Glide.with(act).load(url).centerCrop().into(iv);
        else if(center == Center.FIT)
            Glide.with(act).load(url).fitCenter().into(iv);
    }

    public static void loadFromWeb(View view, String url, ImageView iv, GlideBny.Center center) {
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
                    .into(iv);
        }
        else if(center == Center.FIT) {
            Glide.with(act)
                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .load(url)
                    .fitCenter()
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

//    check si image en cache

    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public enum Center {
        CROP,
        FIT
    }

}
