package com.android.wm.shell.onehanded;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.util.Slog;
import android.view.ContextThemeWrapper;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BackgroundWindowManager extends WindowlessWindowManager {
    public View mBackgroundView;
    public Context mContext;
    public int mCurrentState;
    public Rect mDisplayBounds;
    public SurfaceControl mLeash;
    public final ViewCompat$$ExternalSyntheticLambda0 mTransactionFactory = ViewCompat$$ExternalSyntheticLambda0.INSTANCE;
    public SurfaceControlViewHost mViewHost;

    public final void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setColorLayer().setBufferSize(this.mDisplayBounds.width(), this.mDisplayBounds.height()).setFormat(3).setOpaque(true).setName("BackgroundWindowManager").setCallsite("BackgroundWindowManager#attachToParentSurface").build();
        this.mLeash = build;
        builder.setParent(build);
    }

    public final int getThemeColorForBackground() {
        int color = new ContextThemeWrapper(this.mContext, 16974563).getColor(2131100492);
        return Color.argb(Color.alpha(color), Color.red(color) - 10, Color.green(color) - 10, Color.blue(color) - 10);
    }

    public final void showBackgroundLayer() {
        boolean z;
        if (this.mBackgroundView == null && this.mViewHost == null) {
            Context context = this.mContext;
            this.mViewHost = new SurfaceControlViewHost(context, context.getDisplay(), (WindowlessWindowManager) this);
            this.mBackgroundView = LayoutInflater.from(this.mContext).inflate(2131624013, (ViewGroup) null);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mDisplayBounds.width(), this.mDisplayBounds.height(), 0, 537133096, -3);
            layoutParams.token = new Binder();
            layoutParams.setTitle("background-panel");
            layoutParams.privateFlags |= 536870976;
            this.mBackgroundView.setBackgroundColor(getThemeColorForBackground());
            this.mViewHost.setView(this.mBackgroundView, layoutParams);
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            updateThemeOnly();
        } else if (this.mLeash == null) {
            Slog.w("BackgroundWindowManager", "SurfaceControl mLeash is null, can't show One-handed mode background panel!");
        } else {
            Objects.requireNonNull(this.mTransactionFactory);
            new SurfaceControl.Transaction().setAlpha(this.mLeash, 1.0f).setLayer(this.mLeash, -1).show(this.mLeash).apply();
        }
    }

    public final void updateThemeOnly() {
        View view = this.mBackgroundView;
        if (view == null || this.mViewHost == null || this.mLeash == null) {
            Slog.w("BackgroundWindowManager", "Background view or SurfaceControl does not exist when trying to update theme only!");
            return;
        }
        this.mBackgroundView.setBackgroundColor(getThemeColorForBackground());
        this.mViewHost.setView(this.mBackgroundView, (WindowManager.LayoutParams) view.getLayoutParams());
    }

    public BackgroundWindowManager(Context context) {
        super(context.getResources().getConfiguration(), (SurfaceControl) null, (IBinder) null);
        this.mContext = context;
    }

    public final SurfaceControl getSurfaceControl(IWindow iWindow) {
        return BackgroundWindowManager.super.getSurfaceControl(iWindow);
    }

    public final void setConfiguration(Configuration configuration) {
        BackgroundWindowManager.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }
}
