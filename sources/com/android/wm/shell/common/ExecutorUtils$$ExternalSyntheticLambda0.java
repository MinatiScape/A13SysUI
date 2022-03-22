package com.android.wm.shell.common;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLDisplay;
import android.opengl.GLUtils;
import android.os.Trace;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Button;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.glwallpaper.ImageWallpaperRenderer;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleStackView;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ExecutorUtils$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ExecutorUtils$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ((Consumer) this.f$0).accept((RemoteCallable) this.f$1);
                return;
            case 1:
                ImageWallpaper.GLEngine gLEngine = (ImageWallpaper.GLEngine) this.f$0;
                SurfaceHolder surfaceHolder = (SurfaceHolder) this.f$1;
                int i = ImageWallpaper.GLEngine.MIN_SURFACE_WIDTH;
                Objects.requireNonNull(gLEngine);
                Trace.beginSection("ImageWallpaper#onSurfaceCreated");
                EglHelper eglHelper = gLEngine.mEglHelper;
                ImageWallpaperRenderer imageWallpaperRenderer = gLEngine.mRenderer;
                Objects.requireNonNull(imageWallpaperRenderer);
                ImageWallpaperRenderer.WallpaperTexture wallpaperTexture = imageWallpaperRenderer.mTexture;
                Objects.requireNonNull(wallpaperTexture);
                boolean z = wallpaperTexture.mWcgContent;
                Objects.requireNonNull(eglHelper);
                if (eglHelper.hasEglDisplay() || eglHelper.connectDisplay()) {
                    EGLDisplay eGLDisplay = eglHelper.mEglDisplay;
                    int[] iArr = eglHelper.mEglVersion;
                    if (!EGL14.eglInitialize(eGLDisplay, iArr, 0, iArr, 1)) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("eglInitialize failed: ");
                        m.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
                        Log.w("EglHelper", m.toString());
                    } else {
                        int[] iArr2 = new int[1];
                        EGLConfig[] eGLConfigArr = new EGLConfig[1];
                        EGLConfig eGLConfig = null;
                        if (!EGL14.eglChooseConfig(eglHelper.mEglDisplay, EglHelper.getConfig(), 0, eGLConfigArr, 0, 1, iArr2, 0)) {
                            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("eglChooseConfig failed: ");
                            m2.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
                            Log.w("EglHelper", m2.toString());
                        } else if (iArr2[0] <= 0) {
                            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("eglChooseConfig failed, invalid configs count: ");
                            m3.append(iArr2[0]);
                            Log.w("EglHelper", m3.toString());
                        } else {
                            eGLConfig = eGLConfigArr[0];
                        }
                        eglHelper.mEglConfig = eGLConfig;
                        if (eGLConfig == null) {
                            Log.w("EglHelper", "eglConfig not initialized!");
                        } else if (!eglHelper.createEglContext()) {
                            Log.w("EglHelper", "Can't create EGLContext!");
                        } else if (!eglHelper.createEglSurface(surfaceHolder, z)) {
                            Log.w("EglHelper", "Can't create EGLSurface!");
                        } else {
                            eglHelper.mEglReady = true;
                        }
                    }
                } else {
                    Log.w("EglHelper", "Can not connect display, abort!");
                }
                gLEngine.mRenderer.onSurfaceCreated();
                Trace.endSection();
                return;
            case 2:
                PeopleSpaceWidgetManager.AnonymousClass2 r1 = (PeopleSpaceWidgetManager.AnonymousClass2) this.f$0;
                int i2 = PeopleSpaceWidgetManager.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r1);
                PeopleSpaceWidgetManager.this.updateWidgetsFromBroadcastInBackground(((Intent) this.f$1).getAction());
                return;
            case 3:
                NotificationTemplateViewWrapper notificationTemplateViewWrapper = (NotificationTemplateViewWrapper) this.f$0;
                Button button = (Button) this.f$1;
                int i3 = NotificationTemplateViewWrapper.$r8$clinit;
                Objects.requireNonNull(notificationTemplateViewWrapper);
                if (button.isEnabled()) {
                    button.setEnabled(false);
                    ColorStateList textColors = button.getTextColors();
                    int[] colors = textColors.getColors();
                    int[] iArr3 = new int[colors.length];
                    float f = notificationTemplateViewWrapper.mView.getResources().getFloat(17105372);
                    for (int i4 = 0; i4 < colors.length; i4++) {
                        int i5 = colors[i4];
                        iArr3[i4] = ContrastColorUtil.compositeColors(Color.argb((int) (255.0f * f), Color.red(i5), Color.green(i5), Color.blue(i5)), notificationTemplateViewWrapper.resolveBackgroundColor());
                    }
                    button.setTextColor(new ColorStateList(textColors.getStates(), iArr3));
                    return;
                }
                return;
            default:
                BubbleStackView.$r8$lambda$DjZKjrr94LsLQZplD_wFyam4Xpc((BubbleStackView) this.f$0, (Bubble) this.f$1);
                return;
        }
    }
}
