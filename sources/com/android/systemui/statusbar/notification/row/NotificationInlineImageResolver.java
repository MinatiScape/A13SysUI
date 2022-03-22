package com.android.systemui.statusbar.notification.row;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ImageResolver;
import com.android.internal.widget.LocalImageResolver;
import com.android.systemui.statusbar.notification.row.NotificationInlineImageCache;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationInlineImageResolver implements ImageResolver {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Context mContext;
    public final ImageCache mImageCache;
    public HashSet mWantedUriSet;
    @VisibleForTesting
    public int mMaxImageWidth = getMaxImageWidth();
    @VisibleForTesting
    public int mMaxImageHeight = getMaxImageHeight();

    /* loaded from: classes.dex */
    public interface ImageCache {
    }

    @VisibleForTesting
    public int getMaxImageHeight() {
        int i;
        Resources resources = this.mContext.getResources();
        if (ActivityManager.isLowRamDeviceStatic()) {
            i = 17105393;
        } else {
            i = 17105392;
        }
        return resources.getDimensionPixelSize(i);
    }

    @VisibleForTesting
    public int getMaxImageWidth() {
        int i;
        Resources resources = this.mContext.getResources();
        if (ActivityManager.isLowRamDeviceStatic()) {
            i = 17105395;
        } else {
            i = 17105394;
        }
        return resources.getDimensionPixelSize(i);
    }

    public final Drawable loadImage(Uri uri) {
        boolean z;
        try {
            if (this.mImageCache == null || ActivityManager.isLowRamDeviceStatic()) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                return LocalImageResolver.resolveImage(uri, this.mContext, this.mMaxImageWidth, this.mMaxImageHeight);
            }
            NotificationInlineImageCache notificationInlineImageCache = (NotificationInlineImageCache) this.mImageCache;
            Objects.requireNonNull(notificationInlineImageCache);
            if (!notificationInlineImageCache.mCache.containsKey(uri)) {
                NotificationInlineImageCache notificationInlineImageCache2 = (NotificationInlineImageCache) this.mImageCache;
                Objects.requireNonNull(notificationInlineImageCache2);
                NotificationInlineImageCache.PreloadImageTask preloadImageTask = new NotificationInlineImageCache.PreloadImageTask(notificationInlineImageCache2.mResolver);
                preloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
                notificationInlineImageCache2.mCache.put(uri, preloadImageTask);
            }
            return ((NotificationInlineImageCache) this.mImageCache).get(uri);
        } catch (IOException | SecurityException e) {
            Log.d("NotificationInlineImageResolver", "loadImage: Can't load image from " + uri, e);
            return null;
        }
    }

    public NotificationInlineImageResolver(Context context, NotificationInlineImageCache notificationInlineImageCache) {
        this.mContext = context.getApplicationContext();
        this.mImageCache = notificationInlineImageCache;
        notificationInlineImageCache.mResolver = this;
    }
}
