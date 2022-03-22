package com.android.systemui.media.dream;

import android.content.Context;
import android.view.IWindowManager;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.sensors.ProximitySensorImpl;
import com.android.systemui.util.sensors.ThresholdSensor;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDreamSentinel_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object complicationProvider;
    public final Provider contextProvider;
    public final Provider dreamOverlayStateControllerProvider;
    public final Provider mediaDataManagerProvider;

    public /* synthetic */ MediaDreamSentinel_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.mediaDataManagerProvider = provider2;
        this.dreamOverlayStateControllerProvider = provider3;
        this.complicationProvider = provider4;
    }

    public MediaDreamSentinel_Factory(UnfoldTransitionModule unfoldTransitionModule, Provider provider, Provider provider2, Provider provider3) {
        this.$r8$classId = 2;
        this.complicationProvider = unfoldTransitionModule;
        this.contextProvider = provider;
        this.mediaDataManagerProvider = provider2;
        this.dreamOverlayStateControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new MediaDreamSentinel((Context) this.contextProvider.mo144get(), (MediaDataManager) this.mediaDataManagerProvider.mo144get(), (DreamOverlayStateController) this.dreamOverlayStateControllerProvider.mo144get(), (MediaDreamComplication) ((Provider) this.complicationProvider).mo144get());
            case 1:
                return new ProximitySensorImpl((ThresholdSensor) this.contextProvider.mo144get(), (ThresholdSensor) this.mediaDataManagerProvider.mo144get(), (DelayableExecutor) this.dreamOverlayStateControllerProvider.mo144get(), (Execution) ((Provider) this.complicationProvider).mo144get());
            default:
                final Context context = (Context) this.contextProvider.mo144get();
                final IWindowManager iWindowManager = (IWindowManager) this.mediaDataManagerProvider.mo144get();
                Objects.requireNonNull((UnfoldTransitionModule) this.complicationProvider);
                return ((Optional) this.dreamOverlayStateControllerProvider.mo144get()).map(new Function() { // from class: com.android.systemui.unfold.UnfoldTransitionModule$provideNaturalRotationProgressProvider$1
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return new NaturalRotationUnfoldProgressProvider(context, iWindowManager, (UnfoldTransitionProgressProvider) obj);
                    }
                });
        }
    }
}
