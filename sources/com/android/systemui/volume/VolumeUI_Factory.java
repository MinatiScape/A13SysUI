package com.android.systemui.volume;

import android.content.Context;
import android.provider.Settings;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VolumeUI_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider volumeDialogComponentProvider;

    public /* synthetic */ VolumeUI_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.volumeDialogComponentProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new VolumeUI((Context) this.contextProvider.mo144get(), (VolumeDialogComponent) this.volumeDialogComponentProvider.mo144get());
            case 1:
                Context context = (Context) this.contextProvider.mo144get();
                return new NotifPipelineFlags((FeatureFlags) this.volumeDialogComponentProvider.mo144get());
            default:
                Optional optional = (Optional) this.contextProvider.mo144get();
                Context context2 = (Context) this.volumeDialogComponentProvider.mo144get();
                boolean z = false;
                if (context2.getPackageManager().hasSystemFeature("android.software.freeform_window_management") || Settings.Global.getInt(context2.getContentResolver(), "enable_freeform_support", 0) != 0) {
                    z = true;
                }
                if (!z) {
                    optional = Optional.empty();
                }
                Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
                return optional;
        }
    }
}
