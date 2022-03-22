package com.android.systemui.glwallpaper;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import java.util.Collections;
import java.util.HashSet;
/* loaded from: classes.dex */
public final class EglHelper {
    public EGLConfig mEglConfig;
    public EGLContext mEglContext;
    public EGLDisplay mEglDisplay;
    public boolean mEglReady;
    public EGLSurface mEglSurface;
    public final int[] mEglVersion = new int[2];
    public final HashSet mExts = new HashSet();

    public static int[] getConfig() {
        return new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12352, 4, 12327, 12344, 12344};
    }

    public final boolean connectDisplay() {
        this.mExts.clear();
        this.mEglDisplay = EGL14.eglGetDisplay(0);
        if (!hasEglDisplay()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("eglGetDisplay failed: ");
            m.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
            Log.w("EglHelper", m.toString());
            return false;
        }
        String eglQueryString = EGL14.eglQueryString(this.mEglDisplay, 12373);
        if (TextUtils.isEmpty(eglQueryString)) {
            return true;
        }
        Collections.addAll(this.mExts, eglQueryString.split(" "));
        return true;
    }

    public final boolean createEglContext() {
        Log.d("EglHelper", "createEglContext start");
        int[] iArr = new int[5];
        iArr[0] = 12440;
        char c = 2;
        iArr[1] = 2;
        if (this.mExts.contains("EGL_IMG_context_priority")) {
            iArr[2] = 12544;
            c = 4;
            iArr[3] = 12547;
        }
        iArr[c] = 12344;
        if (hasEglDisplay()) {
            this.mEglContext = EGL14.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL14.EGL_NO_CONTEXT, iArr, 0);
            if (!hasEglContext()) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("eglCreateContext failed: ");
                m.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
                Log.w("EglHelper", m.toString());
                return false;
            }
            Log.d("EglHelper", "createEglContext done");
            return true;
        }
        Log.w("EglHelper", "mEglDisplay is null");
        return false;
    }

    public final boolean createEglSurface(SurfaceHolder surfaceHolder, boolean z) {
        int i;
        Log.d("EglHelper", "createEglSurface start");
        if (!hasEglDisplay() || !surfaceHolder.getSurface().isValid()) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Create EglSurface failed: hasEglDisplay=");
            m.append(hasEglDisplay());
            m.append(", has valid surface=");
            m.append(surfaceHolder.getSurface().isValid());
            Log.w("EglHelper", m.toString());
            return false;
        }
        int[] iArr = null;
        if (this.mExts.contains("EGL_EXT_gl_colorspace_display_p3_passthrough")) {
            i = 13456;
        } else {
            i = 0;
        }
        if (z && this.mExts.contains("EGL_KHR_gl_colorspace") && i > 0) {
            iArr = new int[]{12445, i, 12344};
        }
        this.mEglSurface = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, surfaceHolder, iArr, 0);
        if (!hasEglSurface()) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("createWindowSurface failed: ");
            m2.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
            Log.w("EglHelper", m2.toString());
            return false;
        }
        EGLDisplay eGLDisplay = this.mEglDisplay;
        EGLSurface eGLSurface = this.mEglSurface;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEglContext)) {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("eglMakeCurrent failed: ");
            m3.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
            Log.w("EglHelper", m3.toString());
            return false;
        }
        Log.d("EglHelper", "createEglSurface done");
        return true;
    }

    public final boolean hasEglContext() {
        EGLContext eGLContext = this.mEglContext;
        if (eGLContext == null || eGLContext == EGL14.EGL_NO_CONTEXT) {
            return false;
        }
        return true;
    }

    public final boolean hasEglDisplay() {
        EGLDisplay eGLDisplay = this.mEglDisplay;
        if (eGLDisplay == null || eGLDisplay == EGL14.EGL_NO_DISPLAY) {
            return false;
        }
        return true;
    }

    public final boolean hasEglSurface() {
        EGLSurface eGLSurface = this.mEglSurface;
        if (eGLSurface == null || eGLSurface == EGL14.EGL_NO_SURFACE) {
            return false;
        }
        return true;
    }

    public EglHelper() {
        connectDisplay();
    }

    public final void destroyEglSurface() {
        if (hasEglSurface()) {
            EGLDisplay eGLDisplay = this.mEglDisplay;
            EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(this.mEglDisplay, this.mEglSurface);
            this.mEglSurface = EGL14.EGL_NO_SURFACE;
        }
    }
}
