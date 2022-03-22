package com.android.systemui;

import android.app.WallpaperColors;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.service.wallpaper.WallpaperService;
import android.util.ArraySet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda1;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda1;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.glwallpaper.ImageWallpaperRenderer;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda0;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class ImageWallpaper extends WallpaperService {
    public static final RectF LOCAL_COLOR_BOUNDS = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    public Bitmap mMiniBitmap;
    public HandlerThread mWorker;
    public final ArrayList<RectF> mLocalColorsToAdd = new ArrayList<>();
    public final ArraySet<RectF> mColorAreas = new ArraySet<>();
    public volatile int mPages = 1;

    /* loaded from: classes.dex */
    public class GLEngine extends WallpaperService.Engine implements DisplayManager.DisplayListener {
        @VisibleForTesting
        public static final int MIN_SURFACE_HEIGHT = 128;
        @VisibleForTesting
        public static final int MIN_SURFACE_WIDTH = 128;
        public int mDisplayHeight;
        public boolean mDisplaySizeValid;
        public int mDisplayWidth;
        public EglHelper mEglHelper;
        public final Runnable mFinishRenderingTask;
        public int mImgHeight;
        public int mImgWidth;
        public ImageWallpaperRenderer mRenderer;

        public GLEngine() {
            super(ImageWallpaper.this);
            this.mFinishRenderingTask = new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 3);
            this.mDisplaySizeValid = false;
            this.mDisplayWidth = 1;
            this.mDisplayHeight = 1;
            this.mImgWidth = 1;
            this.mImgHeight = 1;
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public final void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public final void onDisplayRemoved(int i) {
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            int i3 = 1;
            if (f3 > 0.0f && f3 <= 1.0f) {
                i3 = 1 + Math.round(1.0f / f3);
            }
            if (i3 != ImageWallpaper.this.mPages) {
                ImageWallpaper.this.mPages = i3;
                Bitmap bitmap = ImageWallpaper.this.mMiniBitmap;
                if (bitmap != null && !bitmap.isRecycled()) {
                    ImageWallpaper.this.mWorker.getThreadHandler().post(new TaskView$$ExternalSyntheticLambda4(this, 3));
                }
            }
        }

        public final boolean shouldWaitForEngineShown() {
            return true;
        }

        public final boolean shouldZoomOutWallpaper() {
            return true;
        }

        public final boolean supportsLocalColorExtraction() {
            return true;
        }

        public final void addLocalColorsAreas(List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new CarrierTextManager$$ExternalSyntheticLambda1(this, list, 1));
        }

        public final void computeAndNotifyLocalColors(List<RectF> list, Bitmap bitmap) {
            int i;
            int i2;
            float f;
            ArrayList arrayList = new ArrayList(list.size());
            for (int i3 = 0; i3 < list.size(); i3++) {
                RectF rectF = list.get(i3);
                if (!this.mDisplaySizeValid) {
                    Rect bounds = ((WindowManager) getDisplayContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
                    this.mDisplayWidth = bounds.width();
                    this.mDisplayHeight = bounds.height();
                    this.mDisplaySizeValid = true;
                }
                float f2 = 1.0f / ImageWallpaper.this.mPages;
                float f3 = (rectF.left % f2) / f2;
                float f4 = (rectF.right % f2) / f2;
                int floor = (int) Math.floor(rectF.centerX() / f2);
                RectF rectF2 = new RectF();
                if (this.mImgWidth != 0 && (i = this.mImgHeight) != 0 && this.mDisplayWidth > 0 && (i2 = this.mDisplayHeight) > 0) {
                    rectF2.bottom = rectF.bottom;
                    rectF2.top = rectF.top;
                    float min = this.mDisplayWidth * Math.min(i / i2, 1.0f);
                    int i4 = this.mImgWidth;
                    if (i4 > 0) {
                        f = min / i4;
                    } else {
                        f = 1.0f;
                    }
                    float min2 = Math.min(1.0f, f);
                    float f5 = floor * ((1.0f - min2) / (ImageWallpaper.this.mPages - 1));
                    rectF2.left = MathUtils.constrain((f3 * min2) + f5, 0.0f, 1.0f);
                    float constrain = MathUtils.constrain((f4 * min2) + f5, 0.0f, 1.0f);
                    rectF2.right = constrain;
                    if (rectF2.left > constrain) {
                        rectF2.left = 0.0f;
                        rectF2.right = 1.0f;
                    }
                }
                if (!ImageWallpaper.LOCAL_COLOR_BOUNDS.contains(rectF2)) {
                    arrayList.add(null);
                } else {
                    Rect rect = new Rect((int) Math.floor(rectF2.left * bitmap.getWidth()), (int) Math.floor(rectF2.top * bitmap.getHeight()), (int) Math.ceil(rectF2.right * bitmap.getWidth()), (int) Math.ceil(rectF2.bottom * bitmap.getHeight()));
                    if (rect.isEmpty()) {
                        arrayList.add(null);
                    } else {
                        arrayList.add(WallpaperColors.fromBitmap(Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())));
                    }
                }
            }
            ImageWallpaper.this.mColorAreas.addAll(list);
            try {
                notifyLocalColorsChanged(list, arrayList);
            } catch (RuntimeException e) {
                RectF rectF3 = ImageWallpaper.LOCAL_COLOR_BOUNDS;
                Log.e("ImageWallpaper", e.getMessage(), e);
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onCreate(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper.Engine#onCreate");
            this.mEglHelper = new EglHelper();
            this.mRenderer = new ImageWallpaperRenderer(getDisplayContext());
            setFixedSizeAllowed(true);
            Trace.beginSection("ImageWallpaper#updateSurfaceSize");
            SurfaceHolder surfaceHolder2 = getSurfaceHolder();
            ImageWallpaperRenderer imageWallpaperRenderer = this.mRenderer;
            Objects.requireNonNull(imageWallpaperRenderer);
            Rect rect = imageWallpaperRenderer.mSurfaceSize;
            ImageWallpaperRenderer.WallpaperTexture wallpaperTexture = imageWallpaperRenderer.mTexture;
            Objects.requireNonNull(wallpaperTexture);
            if (!wallpaperTexture.mTextureUsed) {
                wallpaperTexture.mDimensions.set(wallpaperTexture.mWallpaperManager.peekBitmapDimensions());
            }
            rect.set(wallpaperTexture.mDimensions);
            Size size = new Size(imageWallpaperRenderer.mSurfaceSize.width(), imageWallpaperRenderer.mSurfaceSize.height());
            surfaceHolder2.setFixedSize(Math.max(128, size.getWidth()), Math.max(128, size.getHeight()));
            Trace.endSection();
            setShowForAllUsers(true);
            ImageWallpaperRenderer imageWallpaperRenderer2 = this.mRenderer;
            ImageWallpaper$GLEngine$$ExternalSyntheticLambda2 imageWallpaper$GLEngine$$ExternalSyntheticLambda2 = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda2(this, 0);
            Objects.requireNonNull(imageWallpaperRenderer2);
            imageWallpaperRenderer2.mOnBitmapUpdated = imageWallpaper$GLEngine$$ExternalSyntheticLambda2;
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).registerDisplayListener(this, ImageWallpaper.this.mWorker.getThreadHandler());
            Trace.endSection();
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, final int i2, final int i3) {
            HandlerThread handlerThread = ImageWallpaper.this.mWorker;
            if (handlerThread != null) {
                handlerThread.getThreadHandler().post(new Runnable() { // from class: com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImageWallpaper.GLEngine gLEngine = ImageWallpaper.GLEngine.this;
                        int i4 = i2;
                        int i5 = i3;
                        Objects.requireNonNull(gLEngine);
                        Objects.requireNonNull(gLEngine.mRenderer);
                        GLES20.glViewport(0, 0, i4, i5);
                    }
                });
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            HandlerThread handlerThread = ImageWallpaper.this.mWorker;
            if (handlerThread != null) {
                handlerThread.getThreadHandler().post(new ExecutorUtils$$ExternalSyntheticLambda0(this, surfaceHolder, 1));
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onSurfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
            HandlerThread handlerThread = ImageWallpaper.this.mWorker;
            if (handlerThread != null) {
                handlerThread.getThreadHandler().post(new CarrierTextManager$$ExternalSyntheticLambda0(this, 1));
            }
        }

        public final void removeLocalColorsAreas(List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new Monitor$$ExternalSyntheticLambda0(this, list, 1));
        }

        public final void updateMiniBitmapAndNotify(Bitmap bitmap) {
            float f;
            if (bitmap != null) {
                int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
                if (min > 128) {
                    f = 128.0f / min;
                } else {
                    f = 1.0f;
                }
                this.mImgHeight = bitmap.getHeight();
                this.mImgWidth = bitmap.getWidth();
                ImageWallpaper.this.mMiniBitmap = Bitmap.createScaledBitmap(bitmap, (int) Math.max(bitmap.getWidth() * f, 1.0f), (int) Math.max(f * bitmap.getHeight(), 1.0f), false);
                ImageWallpaper imageWallpaper = ImageWallpaper.this;
                computeAndNotifyLocalColors(imageWallpaper.mLocalColorsToAdd, imageWallpaper.mMiniBitmap);
                ImageWallpaper.this.mLocalColorsToAdd.clear();
            }
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            Object obj;
            super.dump(str, fileDescriptor, printWriter, strArr);
            printWriter.print(str);
            printWriter.print("Engine=");
            printWriter.println(this);
            printWriter.print(str);
            printWriter.print("valid surface=");
            Object obj2 = "null";
            if (getSurfaceHolder() == null || getSurfaceHolder().getSurface() == null) {
                obj = obj2;
            } else {
                obj = Boolean.valueOf(getSurfaceHolder().getSurface().isValid());
            }
            printWriter.println(obj);
            printWriter.print(str);
            printWriter.print("surface frame=");
            if (getSurfaceHolder() != null) {
                obj2 = getSurfaceHolder().getSurfaceFrame();
            }
            printWriter.println(obj2);
            EglHelper eglHelper = this.mEglHelper;
            Objects.requireNonNull(eglHelper);
            StringBuilder sb = new StringBuilder();
            sb.append(eglHelper.mEglVersion[0]);
            sb.append(".");
            sb.append(eglHelper.mEglVersion[1]);
            String sb2 = sb.toString();
            printWriter.print(str);
            printWriter.print("EGL version=");
            printWriter.print(sb2);
            printWriter.print(", ");
            printWriter.print("EGL ready=");
            printWriter.print(eglHelper.mEglReady);
            printWriter.print(", ");
            printWriter.print("has EglContext=");
            printWriter.print(eglHelper.hasEglContext());
            printWriter.print(", ");
            printWriter.print("has EglSurface=");
            printWriter.println(eglHelper.hasEglSurface());
            int[] config = EglHelper.getConfig();
            StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('{');
            for (int i = 0; i < 17; i++) {
                int i2 = config[i];
                m.append("0x");
                m.append(Integer.toHexString(i2));
                m.append(",");
            }
            m.setCharAt(m.length() - 1, '}');
            printWriter.print(str);
            printWriter.print("EglConfig=");
            printWriter.println(m.toString());
            ImageWallpaperRenderer imageWallpaperRenderer = this.mRenderer;
            Objects.requireNonNull(imageWallpaperRenderer);
            printWriter.print(str);
            printWriter.print("mSurfaceSize=");
            printWriter.print(imageWallpaperRenderer.mSurfaceSize);
            printWriter.print(str);
            printWriter.print("mWcgContent=");
            ImageWallpaperRenderer.WallpaperTexture wallpaperTexture = imageWallpaperRenderer.mTexture;
            Objects.requireNonNull(wallpaperTexture);
            printWriter.print(wallpaperTexture.mWcgContent);
            Objects.requireNonNull(imageWallpaperRenderer.mWallpaper);
        }

        @Override // android.service.wallpaper.WallpaperService.Engine
        public final void onDestroy() {
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).unregisterDisplayListener(this);
            ImageWallpaper imageWallpaper = ImageWallpaper.this;
            imageWallpaper.mMiniBitmap = null;
            imageWallpaper.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 0));
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public final void onDisplayChanged(int i) {
            if (i == getDisplayContext().getDisplayId()) {
                this.mDisplaySizeValid = false;
            }
        }

        @VisibleForTesting
        public GLEngine(Handler handler) {
            super(ImageWallpaper.this, ImageWallpaper$GLEngine$$ExternalSyntheticLambda4.INSTANCE, handler);
            this.mFinishRenderingTask = new DozeUi$$ExternalSyntheticLambda1(this, 3);
            this.mDisplaySizeValid = false;
            this.mDisplayWidth = 1;
            this.mDisplayHeight = 1;
            this.mImgWidth = 1;
            this.mImgHeight = 1;
        }
    }

    @Override // android.service.wallpaper.WallpaperService
    public final WallpaperService.Engine onCreateEngine() {
        return new GLEngine();
    }

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public final void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("ImageWallpaper");
        this.mWorker = handlerThread;
        handlerThread.start();
    }

    @Override // android.service.wallpaper.WallpaperService, android.app.Service
    public final void onDestroy() {
        super.onDestroy();
        this.mWorker.quitSafely();
        this.mWorker = null;
        this.mMiniBitmap = null;
    }
}
