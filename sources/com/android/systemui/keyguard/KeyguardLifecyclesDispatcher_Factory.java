package com.android.systemui.keyguard;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.os.Handler;
import com.android.systemui.communal.conditions.CommunalSettingCondition;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.flags.Flags;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardLifecyclesDispatcher_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider screenLifecycleProvider;
    public final Object wakefulnessLifecycleProvider;

    public /* synthetic */ KeyguardLifecyclesDispatcher_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.screenLifecycleProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new KeyguardLifecyclesDispatcher((ScreenLifecycle) this.screenLifecycleProvider.mo144get(), (WakefulnessLifecycle) ((Provider) this.wakefulnessLifecycleProvider).mo144get());
            case 1:
                return new CommunalSettingCondition((Handler) this.screenLifecycleProvider.mo144get(), (SecureSettings) ((Provider) this.wakefulnessLifecycleProvider).mo144get());
            case 2:
                return mo144get();
            case 3:
                return new ConversationNotificationProcessor((LauncherApps) this.screenLifecycleProvider.mo144get(), (ConversationNotificationManager) ((Provider) this.wakefulnessLifecycleProvider).mo144get());
            case 4:
                return mo144get();
            default:
                Context context = (Context) this.screenLifecycleProvider.mo144get();
                Objects.requireNonNull((DependencyProvider) this.wakefulnessLifecycleProvider);
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
                Objects.requireNonNull(sharedPreferences, "Cannot return null from a non-@Nullable @Provides method");
                return sharedPreferences;
        }
    }

    public KeyguardLifecyclesDispatcher_Factory(DependencyProvider dependencyProvider, Provider provider) {
        this.$r8$classId = 5;
        this.wakefulnessLifecycleProvider = dependencyProvider;
        this.screenLifecycleProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public final Optional mo144get() {
        Optional optional;
        switch (this.$r8$classId) {
            case 2:
                MediaFlags mediaFlags = (MediaFlags) this.screenLifecycleProvider.mo144get();
                Lazy lazy = DoubleCheck.lazy((Provider) this.wakefulnessLifecycleProvider);
                Objects.requireNonNull(mediaFlags);
                if (!mediaFlags.featureFlags.isEnabled(Flags.MEDIA_MUTE_AWAIT)) {
                    optional = Optional.empty();
                } else {
                    optional = Optional.of((MediaMuteAwaitConnectionCli) lazy.get());
                }
                Objects.requireNonNull(optional, "Cannot return null from a non-@Nullable @Provides method");
                return optional;
            default:
                Optional optional2 = (Optional) this.screenLifecycleProvider.mo144get();
                if (!ActivityTaskManager.supportsSplitScreenMultiWindow((Context) ((Provider) this.wakefulnessLifecycleProvider).mo144get())) {
                    optional2 = Optional.empty();
                }
                Objects.requireNonNull(optional2, "Cannot return null from a non-@Nullable @Provides method");
                return optional2;
        }
    }
}
