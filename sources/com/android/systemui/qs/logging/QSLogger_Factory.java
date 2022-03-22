package com.android.systemui.qs.logging;

import android.content.Context;
import android.net.NetworkScoreManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.notification.NotificationClickerLogger;
import com.android.systemui.usb.StorageNotification;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSLogger_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bufferProvider;

    public /* synthetic */ QSLogger_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.bufferProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new QSLogger((LogBuffer) this.bufferProvider.mo144get());
            case 1:
                NetworkScoreManager networkScoreManager = (NetworkScoreManager) ((Context) this.bufferProvider.mo144get()).getSystemService(NetworkScoreManager.class);
                Objects.requireNonNull(networkScoreManager, "Cannot return null from a non-@Nullable @Provides method");
                return networkScoreManager;
            case 2:
                return new MediaFeatureFlag((Context) this.bufferProvider.mo144get());
            case 3:
                return new NotificationClickerLogger((LogBuffer) this.bufferProvider.mo144get());
            default:
                return new StorageNotification((Context) this.bufferProvider.mo144get());
        }
    }
}
