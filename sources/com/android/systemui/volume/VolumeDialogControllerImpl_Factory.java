package com.android.systemui.volume;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.concurrency.ThreadFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VolumeDialogControllerImpl_Factory implements Factory<VolumeDialogControllerImpl> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<IAudioService> iAudioServiceProvider;
    public final Provider<NotificationManager> notificationManagerProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<RingerModeTracker> ringerModeTrackerProvider;
    public final Provider<ThreadFactory> theadFactoryProvider;
    public final Provider<VibratorHelper> vibratorProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public VolumeDialogControllerImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        ThreadFactoryImpl_Factory threadFactoryImpl_Factory = ThreadFactoryImpl_Factory.InstanceHolder.INSTANCE;
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.ringerModeTrackerProvider = provider3;
        this.theadFactoryProvider = threadFactoryImpl_Factory;
        this.audioManagerProvider = provider4;
        this.notificationManagerProvider = provider5;
        this.vibratorProvider = provider6;
        this.iAudioServiceProvider = provider7;
        this.accessibilityManagerProvider = provider8;
        this.packageManagerProvider = provider9;
        this.wakefulnessLifecycleProvider = provider10;
    }

    public static VolumeDialogControllerImpl_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10) {
        return new VolumeDialogControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new VolumeDialogControllerImpl(this.contextProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.ringerModeTrackerProvider.mo144get(), this.theadFactoryProvider.mo144get(), this.audioManagerProvider.mo144get(), this.notificationManagerProvider.mo144get(), this.vibratorProvider.mo144get(), this.iAudioServiceProvider.mo144get(), this.accessibilityManagerProvider.mo144get(), this.packageManagerProvider.mo144get(), this.wakefulnessLifecycleProvider.mo144get());
    }
}
