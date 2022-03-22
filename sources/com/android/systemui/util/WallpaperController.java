package com.android.systemui.util;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: WallpaperController.kt */
/* loaded from: classes.dex */
public final class WallpaperController {
    public float notificationShadeZoomOut;
    public View rootView;
    public float unfoldTransitionZoomOut;
    public WallpaperInfo wallpaperInfo;
    public final WallpaperManager wallpaperManager;

    public final void updateZoom() {
        IBinder iBinder;
        float max = Math.max(this.notificationShadeZoomOut, this.unfoldTransitionZoomOut);
        try {
            View view = this.rootView;
            if (view != null) {
                if (!view.isAttachedToWindow() || view.getWindowToken() == null) {
                    Log.i("WallpaperController", Intrinsics.stringPlus("Won't set zoom. Window not attached ", view));
                } else {
                    this.wallpaperManager.setWallpaperZoomOut(view.getWindowToken(), max);
                }
            }
        } catch (IllegalArgumentException e) {
            View view2 = this.rootView;
            if (view2 == null) {
                iBinder = null;
            } else {
                iBinder = view2.getWindowToken();
            }
            Log.w("WallpaperController", Intrinsics.stringPlus("Can't set zoom. Window is gone: ", iBinder), e);
        }
    }

    public WallpaperController(WallpaperManager wallpaperManager) {
        this.wallpaperManager = wallpaperManager;
    }
}
