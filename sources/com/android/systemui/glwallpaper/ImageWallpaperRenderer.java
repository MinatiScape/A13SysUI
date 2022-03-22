package com.android.systemui.glwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.util.Log;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ImageWallpaperRenderer {
    public Consumer<Bitmap> mOnBitmapUpdated;
    public final ImageGLProgram mProgram;
    public final Rect mSurfaceSize = new Rect();
    public final WallpaperTexture mTexture;
    public final ImageGLWallpaper mWallpaper;

    /* loaded from: classes.dex */
    public static class WallpaperTexture {
        public Bitmap mBitmap;
        public boolean mTextureUsed;
        public final WallpaperManager mWallpaperManager;
        public boolean mWcgContent;
        public final AtomicInteger mRefCount = new AtomicInteger();
        public final Rect mDimensions = new Rect();

        public final void use(Consumer<Bitmap> consumer) {
            Bitmap bitmap;
            this.mRefCount.incrementAndGet();
            synchronized (this.mRefCount) {
                if (this.mBitmap == null) {
                    this.mBitmap = this.mWallpaperManager.getBitmapAsUser(-2, false);
                    this.mWcgContent = this.mWallpaperManager.wallpaperSupportsWcg(1);
                    this.mWallpaperManager.forgetLoadedWallpaper();
                    Bitmap bitmap2 = this.mBitmap;
                    if (bitmap2 != null) {
                        this.mDimensions.set(0, 0, bitmap2.getWidth(), this.mBitmap.getHeight());
                        this.mTextureUsed = true;
                    } else {
                        Log.w("ImageWallpaperRenderer", "Can't get bitmap");
                    }
                }
            }
            consumer.accept(this.mBitmap);
            synchronized (this.mRefCount) {
                if (this.mRefCount.decrementAndGet() == 0 && (bitmap = this.mBitmap) != null) {
                    bitmap.recycle();
                    this.mBitmap = null;
                }
            }
        }

        public WallpaperTexture(WallpaperManager wallpaperManager) {
            this.mWallpaperManager = wallpaperManager;
        }

        public final String toString() {
            String str;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("{");
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null) {
                str = Integer.toHexString(bitmap.hashCode());
            } else {
                str = "null";
            }
            m.append(str);
            m.append(", ");
            m.append(this.mRefCount.get());
            m.append("}");
            return m.toString();
        }
    }

    public final void onSurfaceCreated() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        ImageGLProgram imageGLProgram = this.mProgram;
        Objects.requireNonNull(imageGLProgram);
        String shaderResource = imageGLProgram.getShaderResource(2131886082);
        String shaderResource2 = imageGLProgram.getShaderResource(2131886081);
        int shaderHandle = ImageGLProgram.getShaderHandle(35633, shaderResource);
        int shaderHandle2 = ImageGLProgram.getShaderHandle(35632, shaderResource2);
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram == 0) {
            Log.d("ImageGLProgram", "Can not create OpenGL ES program");
            glCreateProgram = 0;
        } else {
            GLES20.glAttachShader(glCreateProgram, shaderHandle);
            GLES20.glAttachShader(glCreateProgram, shaderHandle2);
            GLES20.glLinkProgram(glCreateProgram);
        }
        imageGLProgram.mProgramHandle = glCreateProgram;
        GLES20.glUseProgram(glCreateProgram);
        this.mTexture.use(new ShellTaskOrganizer$$ExternalSyntheticLambda0(this, 1));
    }

    public ImageWallpaperRenderer(Context context) {
        WallpaperManager wallpaperManager = (WallpaperManager) context.getSystemService(WallpaperManager.class);
        if (wallpaperManager == null) {
            Log.w("ImageWallpaperRenderer", "WallpaperManager not available");
        }
        this.mTexture = new WallpaperTexture(wallpaperManager);
        ImageGLProgram imageGLProgram = new ImageGLProgram(context);
        this.mProgram = imageGLProgram;
        this.mWallpaper = new ImageGLWallpaper(imageGLProgram);
    }
}
