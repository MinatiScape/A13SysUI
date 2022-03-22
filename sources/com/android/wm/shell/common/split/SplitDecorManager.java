package com.android.wm.shell.common.split;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.view.IWindow;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.WindowlessWindowManager;
import android.widget.ImageView;
import com.android.launcher3.icons.IconProvider;
/* loaded from: classes.dex */
public final class SplitDecorManager extends WindowlessWindowManager {
    public SurfaceControl mBackgroundLeash;
    public SurfaceControl mHostLeash;
    public Drawable mIcon;
    public SurfaceControl mIconLeash;
    public final IconProvider mIconProvider;
    public ImageView mResizingIconView;
    public final SurfaceSession mSurfaceSession;
    public SurfaceControlViewHost mViewHost;

    public SplitDecorManager(Configuration configuration, IconProvider iconProvider, SurfaceSession surfaceSession) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        this.mIconProvider = iconProvider;
        this.mSurfaceSession = surfaceSession;
    }

    public final void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("SplitDecorManager").setHidden(true).setParent(this.mHostLeash).setCallsite("SplitDecorManager#attachToParentSurface").build();
        this.mIconLeash = build;
        builder.setParent(build);
    }

    public final void release(SurfaceControl.Transaction transaction) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        SurfaceControl surfaceControl = this.mIconLeash;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            this.mIconLeash = null;
        }
        SurfaceControl surfaceControl2 = this.mBackgroundLeash;
        if (surfaceControl2 != null) {
            transaction.remove(surfaceControl2);
            this.mBackgroundLeash = null;
        }
        this.mHostLeash = null;
        this.mIcon = null;
        this.mResizingIconView = null;
    }
}
