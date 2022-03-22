package com.airbnb.lottie.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.utils.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ImageAssetManager {
    public static final Object bitmapHashLock = new Object();
    public final Context context;
    public ImageAssetDelegate delegate;
    public final Map<String, LottieImageAsset> imageAssets;
    public String imagesFolder;

    public final Bitmap putBitmap(String str, Bitmap bitmap) {
        synchronized (bitmapHashLock) {
            LottieImageAsset lottieImageAsset = this.imageAssets.get(str);
            Objects.requireNonNull(lottieImageAsset);
            lottieImageAsset.bitmap = bitmap;
        }
        return bitmap;
    }

    public ImageAssetManager(Drawable.Callback callback, String str, ImageAssetDelegate imageAssetDelegate, Map<String, LottieImageAsset> map) {
        String str2;
        this.imagesFolder = str;
        if (!TextUtils.isEmpty(str)) {
            if (this.imagesFolder.charAt(str2.length() - 1) != '/') {
                this.imagesFolder += '/';
            }
        }
        if (!(callback instanceof View)) {
            Logger.warning("LottieDrawable must be inside of a view for images to work.");
            this.imageAssets = new HashMap();
            this.context = null;
            return;
        }
        this.context = ((View) callback).getContext();
        this.imageAssets = map;
        this.delegate = imageAssetDelegate;
    }
}
