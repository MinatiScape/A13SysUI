package com.android.wm.shell.dagger;

import android.content.Context;
import android.hardware.SensorManager;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.qs.QSFooterViewController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLogger;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvPipModule_ProvideTvPipBoundsStateFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ TvPipModule_ProvideTvPipBoundsStateFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new TvPipBoundsState((Context) this.contextProvider.mo144get());
            case 1:
                SensorManager sensorManager = (SensorManager) ((Context) this.contextProvider.mo144get()).getSystemService(SensorManager.class);
                Objects.requireNonNull(sensorManager, "Cannot return null from a non-@Nullable @Provides method");
                return sensorManager;
            case 2:
                Context context = (Context) this.contextProvider.mo144get();
                if (DreamBackend.sInstance == null) {
                    DreamBackend.sInstance = new DreamBackend(context);
                }
                DreamBackend dreamBackend = DreamBackend.sInstance;
                Objects.requireNonNull(dreamBackend, "Cannot return null from a non-@Nullable @Provides method");
                return dreamBackend;
            case 3:
                QSFooterViewController qSFooterViewController = (QSFooterViewController) this.contextProvider.mo144get();
                qSFooterViewController.init();
                return qSFooterViewController;
            default:
                return new NotificationStackScrollLogger((LogBuffer) this.contextProvider.mo144get());
        }
    }
}
