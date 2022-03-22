package com.android.systemui.media.dagger;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.Flags;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaModule_ProvidesMediaTttChipControllerSenderFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider controllerSenderLazyProvider;
    public final Provider mediaTttFlagsProvider;

    public /* synthetic */ MediaModule_ProvidesMediaTttChipControllerSenderFactory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.mediaTttFlagsProvider = provider;
        this.controllerSenderLazyProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Optional optional;
        switch (this.$r8$classId) {
            case 0:
                MediaTttFlags mediaTttFlags = (MediaTttFlags) this.mediaTttFlagsProvider.mo144get();
                Lazy lazy = DoubleCheck.lazy(this.controllerSenderLazyProvider);
                Objects.requireNonNull(mediaTttFlags);
                if (!mediaTttFlags.featureFlags.isEnabled(Flags.MEDIA_TAP_TO_TRANSFER)) {
                    optional = Optional.empty();
                } else {
                    optional = Optional.of((MediaTttChipControllerSender) lazy.get());
                }
                Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
                return optional;
            default:
                return new DebugModeFilterProvider((Context) this.mediaTttFlagsProvider.mo144get(), (DumpManager) this.controllerSenderLazyProvider.mo144get());
        }
    }
}
