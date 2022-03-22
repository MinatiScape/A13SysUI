package com.android.systemui.media;

import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: MediaHost.kt */
/* loaded from: classes.dex */
public final class MediaHost$init$3 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ int $location;
    public final /* synthetic */ MediaHost this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MediaHost$init$3(MediaHost mediaHost, int i) {
        super(0);
        this.this$0 = mediaHost;
        this.$location = i;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        MediaHost mediaHost = this.this$0;
        MediaHostStatesManager mediaHostStatesManager = mediaHost.mediaHostStatesManager;
        int i = this.$location;
        MediaHost.MediaHostStateHolder mediaHostStateHolder = mediaHost.state;
        Objects.requireNonNull(mediaHostStatesManager);
        if (!mediaHostStateHolder.equals((MediaHostState) mediaHostStatesManager.mediaHostStates.get(Integer.valueOf(i)))) {
            MediaHost.MediaHostStateHolder copy = mediaHostStateHolder.copy();
            mediaHostStatesManager.mediaHostStates.put(Integer.valueOf(i), copy);
            mediaHostStatesManager.updateCarouselDimensions(i, mediaHostStateHolder);
            for (MediaViewController mediaViewController : mediaHostStatesManager.controllers) {
                Objects.requireNonNull(mediaViewController);
                mediaViewController.stateCallback.onHostStateChanged(i, copy);
            }
            for (MediaHostStatesManager.Callback callback : mediaHostStatesManager.callbacks) {
                callback.onHostStateChanged(i, copy);
            }
        }
        return Unit.INSTANCE;
    }
}
