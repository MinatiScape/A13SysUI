package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/* loaded from: classes.dex */
public class UdfpsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public boolean mAwaitingSurfaceToStartIllumination;
    public GhbmIlluminationListener mGhbmIlluminationListener;
    public boolean mHasValidSurface;
    public final SurfaceHolder mHolder;
    public Runnable mOnIlluminatedRunnable;
    public final Paint mSensorPaint;

    /* loaded from: classes.dex */
    public interface GhbmIlluminationListener {
        void enableGhbm(Surface surface, Runnable runnable);
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHasValidSurface = true;
        if (this.mAwaitingSurfaceToStartIllumination) {
            Runnable runnable = this.mOnIlluminatedRunnable;
            GhbmIlluminationListener ghbmIlluminationListener = this.mGhbmIlluminationListener;
            if (ghbmIlluminationListener == null) {
                Log.e("UdfpsSurfaceView", "doIlluminate | mGhbmIlluminationListener is null");
            } else {
                ghbmIlluminationListener.enableGhbm(this.mHolder.getSurface(), runnable);
            }
            this.mOnIlluminatedRunnable = null;
            this.mAwaitingSurfaceToStartIllumination = false;
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mHasValidSurface = false;
    }

    public UdfpsSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        this.mHolder = holder;
        holder.addCallback(this);
        holder.setFormat(1);
        Paint paint = new Paint(0);
        this.mSensorPaint = paint;
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.FILL);
    }
}
