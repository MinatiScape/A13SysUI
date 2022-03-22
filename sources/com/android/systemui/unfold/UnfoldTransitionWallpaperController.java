package com.android.systemui.unfold;

import android.app.WallpaperInfo;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.util.WallpaperController;
import java.util.Objects;
/* compiled from: UnfoldTransitionWallpaperController.kt */
/* loaded from: classes.dex */
public final class UnfoldTransitionWallpaperController {
    public final UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
    public final WallpaperController wallpaperController;

    /* compiled from: UnfoldTransitionWallpaperController.kt */
    /* loaded from: classes.dex */
    public final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
        public final void onTransitionStarted() {
        }

        public TransitionListener() {
        }

        @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
        public final void onTransitionFinished() {
            boolean z;
            WallpaperController wallpaperController = UnfoldTransitionWallpaperController.this.wallpaperController;
            Objects.requireNonNull(wallpaperController);
            WallpaperInfo wallpaperInfo = wallpaperController.wallpaperInfo;
            if (wallpaperInfo == null) {
                z = true;
            } else {
                z = wallpaperInfo.shouldUseDefaultUnfoldTransition();
            }
            if (z) {
                wallpaperController.unfoldTransitionZoomOut = 0.0f;
                wallpaperController.updateZoom();
            }
        }

        @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
        public final void onTransitionProgress(float f) {
            WallpaperController wallpaperController = UnfoldTransitionWallpaperController.this.wallpaperController;
            boolean z = true;
            float f2 = 1 - f;
            Objects.requireNonNull(wallpaperController);
            WallpaperInfo wallpaperInfo = wallpaperController.wallpaperInfo;
            if (wallpaperInfo != null) {
                z = wallpaperInfo.shouldUseDefaultUnfoldTransition();
            }
            if (z) {
                wallpaperController.unfoldTransitionZoomOut = f2;
                wallpaperController.updateZoom();
            }
        }
    }

    public UnfoldTransitionWallpaperController(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, WallpaperController wallpaperController) {
        this.unfoldTransitionProgressProvider = unfoldTransitionProgressProvider;
        this.wallpaperController = wallpaperController;
    }
}
