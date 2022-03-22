package com.android.systemui.qs.external;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.freeform.FreeformTaskListener;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PackageManagerAdapter_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ PackageManagerAdapter_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new PackageManagerAdapter((Context) this.contextProvider.mo144get());
            case 1:
                AudioManager audioManager = (AudioManager) ((Context) this.contextProvider.mo144get()).getSystemService(AudioManager.class);
                Objects.requireNonNull(audioManager, "Cannot return null from a non-@Nullable @Provides method");
                return audioManager;
            case 2:
                return new PrivacyDotDecorProviderFactory((Resources) this.contextProvider.mo144get());
            default:
                return new FreeformTaskListener((SyncTransactionQueue) this.contextProvider.mo144get());
        }
    }
}
