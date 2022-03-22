package com.android.systemui.statusbar.notification.row;

import com.android.systemui.flags.Flags;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifBindPipelineInitializer_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider pipelineProvider;
    public final Provider stageProvider;

    public /* synthetic */ NotifBindPipelineInitializer_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.pipelineProvider = provider;
        this.stageProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Optional optional;
        switch (this.$r8$classId) {
            case 0:
                return new NotifBindPipelineInitializer((NotifBindPipeline) this.pipelineProvider.mo144get(), (RowContentBindStage) this.stageProvider.mo144get());
            default:
                MediaTttFlags mediaTttFlags = (MediaTttFlags) this.pipelineProvider.mo144get();
                Lazy lazy = DoubleCheck.lazy(this.stageProvider);
                Objects.requireNonNull(mediaTttFlags);
                if (!mediaTttFlags.featureFlags.isEnabled(Flags.MEDIA_TAP_TO_TRANSFER)) {
                    optional = Optional.empty();
                } else {
                    optional = Optional.of((MediaTttChipControllerReceiver) lazy.get());
                }
                Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
                return optional;
        }
    }
}
