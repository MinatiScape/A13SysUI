package com.android.systemui.media.dream;

import android.widget.FrameLayout;
import com.android.systemui.media.MediaHost;
import com.android.systemui.util.ViewController;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public final class MediaComplicationViewController extends ViewController<FrameLayout> {
    public final MediaHost mMediaHost;

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mMediaHost.setExpansion(0.0f);
        MediaHost mediaHost = this.mMediaHost;
        Objects.requireNonNull(mediaHost);
        MediaHost.MediaHostStateHolder mediaHostStateHolder = mediaHost.state;
        Objects.requireNonNull(mediaHostStateHolder);
        if (!Boolean.TRUE.equals(Boolean.valueOf(mediaHostStateHolder.showsOnlyActiveMedia))) {
            mediaHostStateHolder.showsOnlyActiveMedia = true;
            Function0<Unit> function0 = mediaHostStateHolder.changedListener;
            if (function0 != null) {
                function0.invoke();
            }
        }
        MediaHost mediaHost2 = this.mMediaHost;
        Objects.requireNonNull(mediaHost2);
        MediaHost.MediaHostStateHolder mediaHostStateHolder2 = mediaHost2.state;
        Objects.requireNonNull(mediaHostStateHolder2);
        if (!mediaHostStateHolder2.falsingProtectionNeeded) {
            mediaHostStateHolder2.falsingProtectionNeeded = true;
            Function0<Unit> function02 = mediaHostStateHolder2.changedListener;
            if (function02 != null) {
                function02.invoke();
            }
        }
        this.mMediaHost.init(3);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mMediaHost.hostView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        ((FrameLayout) this.mView).addView(this.mMediaHost.hostView);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        ((FrameLayout) this.mView).removeView(this.mMediaHost.hostView);
    }

    public MediaComplicationViewController(FrameLayout frameLayout, MediaHost mediaHost) {
        super(frameLayout);
        this.mMediaHost = mediaHost;
    }
}
