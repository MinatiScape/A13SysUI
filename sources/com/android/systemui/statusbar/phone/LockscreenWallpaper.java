package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.IWallpaperManager;
import android.app.IWallpaperManagerCallback;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public final class LockscreenWallpaper extends IWallpaperManagerCallback.Stub implements Runnable, Dumpable {
    public Bitmap mCache;
    public boolean mCached;
    public int mCurrentUserId = ActivityManager.getCurrentUser();
    public final Handler mH;
    public AsyncTask<Void, Void, LoaderResult> mLoader;
    public final NotificationMediaManager mMediaManager;
    public final WallpaperManager mWallpaperManager;

    /* loaded from: classes.dex */
    public static class WallpaperDrawable extends DrawableWrapper {
        public final ConstantState mState;
        public final Rect mTmpRect = new Rect();

        /* loaded from: classes.dex */
        public static class ConstantState extends Drawable.ConstantState {
            public final Bitmap mBackground;

            @Override // android.graphics.drawable.Drawable.ConstantState
            public final int getChangingConfigurations() {
                return 0;
            }

            @Override // android.graphics.drawable.Drawable.ConstantState
            public final Drawable newDrawable() {
                return new WallpaperDrawable(null, this);
            }

            @Override // android.graphics.drawable.Drawable.ConstantState
            public final Drawable newDrawable(Resources resources) {
                return new WallpaperDrawable(resources, this);
            }

            public ConstantState(Bitmap bitmap) {
                this.mBackground = bitmap;
            }
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public final int getIntrinsicHeight() {
            return -1;
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public final int getIntrinsicWidth() {
            return -1;
        }

        public WallpaperDrawable(Resources resources, ConstantState constantState) {
            super(new BitmapDrawable(resources, constantState.mBackground));
            this.mState = constantState;
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public final void onBoundsChange(Rect rect) {
            float f;
            float f2;
            int width = getBounds().width();
            int height = getBounds().height();
            int width2 = this.mState.mBackground.getWidth();
            int height2 = this.mState.mBackground.getHeight();
            if (width2 * height > width * height2) {
                f2 = height;
                f = height2;
            } else {
                f2 = width;
                f = width2;
            }
            float f3 = f2 / f;
            if (f3 <= 1.0f) {
                f3 = 1.0f;
            }
            float f4 = height2 * f3;
            float f5 = (height - f4) * 0.5f;
            this.mTmpRect.set(rect.left, Math.round(f5) + rect.top, Math.round(width2 * f3) + rect.left, Math.round(f4 + f5) + rect.top);
            super.onBoundsChange(this.mTmpRect);
        }

        public final void setXfermode(Xfermode xfermode) {
            getDrawable().setXfermode(xfermode);
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public final Drawable.ConstantState getConstantState() {
            return this.mState;
        }
    }

    public final void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
    }

    /* loaded from: classes.dex */
    public static class LoaderResult {
        public final Bitmap bitmap;
        public final boolean success;

        public LoaderResult(boolean z, Bitmap bitmap) {
            this.success = z;
            this.bitmap = bitmap;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("LockscreenWallpaper:");
        IndentingPrintWriter increaseIndent = new IndentingPrintWriter(printWriter, "  ").increaseIndent();
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mCached=");
        m.append(this.mCached);
        increaseIndent.println(m.toString());
        increaseIndent.println("mCache=" + this.mCache);
        increaseIndent.println("mCurrentUserId=" + this.mCurrentUserId);
        increaseIndent.println("mSelectedUser=null");
    }

    public final LoaderResult loadBitmap(int i) {
        if (!this.mWallpaperManager.isWallpaperSupported()) {
            return new LoaderResult(true, null);
        }
        ParcelFileDescriptor wallpaperFile = this.mWallpaperManager.getWallpaperFile(2, i);
        if (wallpaperFile == null) {
            return new LoaderResult(true, null);
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.HARDWARE;
            return new LoaderResult(true, BitmapFactory.decodeFileDescriptor(wallpaperFile.getFileDescriptor(), null, options));
        } catch (OutOfMemoryError e) {
            Log.w("LockscreenWallpaper", "Can't decode file", e);
            return new LoaderResult(false, null);
        } finally {
            IoUtils.closeQuietly(wallpaperFile);
        }
    }

    public final void onWallpaperChanged() {
        Handler handler = this.mH;
        if (handler == null) {
            Log.wtfStack("LockscreenWallpaper", "Trying to use LockscreenWallpaper before initialization.");
            return;
        }
        handler.removeCallbacks(this);
        this.mH.post(this);
    }

    @Override // java.lang.Runnable
    public final void run() {
        AsyncTask<Void, Void, LoaderResult> asyncTask = this.mLoader;
        if (asyncTask != null) {
            asyncTask.cancel(false);
        }
        final int i = this.mCurrentUserId;
        this.mLoader = new AsyncTask<Void, Void, LoaderResult>() { // from class: com.android.systemui.statusbar.phone.LockscreenWallpaper.1
            @Override // android.os.AsyncTask
            public final LoaderResult doInBackground(Void[] voidArr) {
                return LockscreenWallpaper.this.loadBitmap(i);
            }

            @Override // android.os.AsyncTask
            public final void onPostExecute(LoaderResult loaderResult) {
                LoaderResult loaderResult2 = loaderResult;
                super.onPostExecute(loaderResult2);
                if (!isCancelled()) {
                    if (loaderResult2.success) {
                        LockscreenWallpaper lockscreenWallpaper = LockscreenWallpaper.this;
                        lockscreenWallpaper.mCached = true;
                        lockscreenWallpaper.mCache = loaderResult2.bitmap;
                        lockscreenWallpaper.mMediaManager.updateMediaMetaData(true, true);
                    }
                    LockscreenWallpaper.this.mLoader = null;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public LockscreenWallpaper(WallpaperManager wallpaperManager, IWallpaperManager iWallpaperManager, DumpManager dumpManager, NotificationMediaManager notificationMediaManager, Handler handler) {
        dumpManager.registerDumpable("LockscreenWallpaper", this);
        this.mWallpaperManager = wallpaperManager;
        this.mMediaManager = notificationMediaManager;
        this.mH = handler;
        if (iWallpaperManager != null) {
            try {
                iWallpaperManager.setLockWallpaperCallback(this);
            } catch (RemoteException e) {
                Log.e("LockscreenWallpaper", "System dead?" + e);
            }
        }
    }
}
