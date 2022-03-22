package com.android.systemui.dreams;

import android.content.Context;
import android.media.session.MediaSessionManager;
import com.android.systemui.assist.PhoneStateMonitor$$ExternalSyntheticLambda1;
import com.google.android.systemui.columbus.feedback.HapticClick;
import com.google.android.systemui.communal.dock.conditions.DockCondition;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DreamOverlayStateController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider executorProvider;

    public /* synthetic */ DreamOverlayStateController_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.executorProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new DreamOverlayStateController((Executor) this.executorProvider.mo144get());
            case 1:
                MediaSessionManager mediaSessionManager = (MediaSessionManager) ((Context) this.executorProvider.mo144get()).getSystemService(MediaSessionManager.class);
                Objects.requireNonNull(mediaSessionManager, "Cannot return null from a non-@Nullable @Provides method");
                return mediaSessionManager;
            case 2:
                Optional map = ((Optional) this.executorProvider.mo144get()).map(PhoneStateMonitor$$ExternalSyntheticLambda1.INSTANCE$3);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
            case 3:
                return new HapticClick(DoubleCheck.lazy(this.executorProvider));
            default:
                HashSet hashSet = new HashSet();
                hashSet.add(new DockCondition((Context) this.executorProvider.mo144get()));
                return hashSet;
        }
    }
}
