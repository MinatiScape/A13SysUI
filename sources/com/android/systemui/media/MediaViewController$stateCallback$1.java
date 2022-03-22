package com.android.systemui.media;

import com.android.systemui.media.MediaHostStatesManager;
import java.util.Objects;
/* compiled from: MediaViewController.kt */
/* loaded from: classes.dex */
public final class MediaViewController$stateCallback$1 implements MediaHostStatesManager.Callback {
    public final /* synthetic */ MediaViewController this$0;

    public MediaViewController$stateCallback$1(MediaViewController mediaViewController) {
        this.this$0 = mediaViewController;
    }

    @Override // com.android.systemui.media.MediaHostStatesManager.Callback
    public final void onHostStateChanged(int i, MediaHostState mediaHostState) {
        MediaViewController mediaViewController = this.this$0;
        Objects.requireNonNull(mediaViewController);
        if (i == mediaViewController.currentEndLocation || i == this.this$0.currentStartLocation) {
            MediaViewController mediaViewController2 = this.this$0;
            int i2 = mediaViewController2.currentStartLocation;
            Objects.requireNonNull(mediaViewController2);
            mediaViewController2.setCurrentState(i2, mediaViewController2.currentEndLocation, this.this$0.currentTransitionProgress, false);
        }
    }
}
