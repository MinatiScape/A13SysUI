package com.android.wm.shell.common.split;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.IBinder;
import android.view.IWindow;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.WindowlessWindowManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SplitWindowManager extends WindowlessWindowManager {
    public Context mContext;
    public DividerView mDividerView;
    public SurfaceControl mLeash;
    public final ParentContainerCallbacks mParentContainerCallbacks;
    public SurfaceControlViewHost mViewHost;
    public final String mWindowName;

    /* loaded from: classes.dex */
    public interface ParentContainerCallbacks {
        void attachToParentSurface(SurfaceControl.Builder builder);

        void onLeashReady(SurfaceControl surfaceControl);
    }

    public SplitWindowManager(String str, Context context, Configuration configuration, ParentContainerCallbacks parentContainerCallbacks) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        this.mContext = context.createConfigurationContext(configuration);
        this.mParentContainerCallbacks = parentContainerCallbacks;
        this.mWindowName = str;
    }

    public final void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("SplitWindowManager").setHidden(true).setCallsite("SplitWindowManager#attachToParentSurface");
        this.mParentContainerCallbacks.attachToParentSurface(callsite);
        SurfaceControl build = callsite.build();
        this.mLeash = build;
        this.mParentContainerCallbacks.onLeashReady(build);
        builder.setParent(this.mLeash);
    }

    public final void setInteractive(boolean z) {
        int i;
        DividerView dividerView = this.mDividerView;
        if (dividerView != null) {
            Objects.requireNonNull(dividerView);
            if (z != dividerView.mInteractive) {
                dividerView.mInteractive = z;
                dividerView.releaseTouching();
                DividerHandleView dividerHandleView = dividerView.mHandle;
                if (dividerView.mInteractive) {
                    i = 0;
                } else {
                    i = 4;
                }
                dividerHandleView.setVisibility(i);
            }
        }
    }

    public final void setTouchRegion(Rect rect) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            setTouchRegion(surfaceControlViewHost.getWindowToken().asBinder(), new Region(rect));
        }
    }

    public final SurfaceControl getSurfaceControl(IWindow iWindow) {
        return SplitWindowManager.super.getSurfaceControl(iWindow);
    }

    public final void setConfiguration(Configuration configuration) {
        SplitWindowManager.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }
}
