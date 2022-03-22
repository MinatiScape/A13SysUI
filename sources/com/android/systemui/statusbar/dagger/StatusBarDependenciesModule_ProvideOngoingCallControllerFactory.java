package com.android.systemui.statusbar.dagger;

import android.app.IActivityManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.BooleanFlag;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideOngoingCallControllerFactory implements Factory<OngoingCallController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<OngoingCallLogger> loggerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<OngoingCallFlags> ongoingCallFlagsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;
    public final Provider<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandlerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public static StatusBarDependenciesModule_ProvideOngoingCallControllerFactory create(Provider<CommonNotifCollection> provider, Provider<SystemClock> provider2, Provider<ActivityStarter> provider3, Provider<Executor> provider4, Provider<IActivityManager> provider5, Provider<OngoingCallLogger> provider6, Provider<DumpManager> provider7, Provider<StatusBarWindowController> provider8, Provider<SwipeStatusBarAwayGestureHandler> provider9, Provider<StatusBarStateController> provider10, Provider<OngoingCallFlags> provider11) {
        return new StatusBarDependenciesModule_ProvideOngoingCallControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        boolean z;
        Optional optional;
        Optional optional2;
        CommonNotifCollection commonNotifCollection = this.notifCollectionProvider.mo144get();
        SystemClock systemClock = this.systemClockProvider.mo144get();
        ActivityStarter activityStarter = this.activityStarterProvider.mo144get();
        Executor executor = this.mainExecutorProvider.mo144get();
        IActivityManager iActivityManager = this.iActivityManagerProvider.mo144get();
        OngoingCallLogger ongoingCallLogger = this.loggerProvider.mo144get();
        DumpManager dumpManager = this.dumpManagerProvider.mo144get();
        StatusBarWindowController statusBarWindowController = this.statusBarWindowControllerProvider.mo144get();
        SwipeStatusBarAwayGestureHandler swipeStatusBarAwayGestureHandler = this.swipeStatusBarAwayGestureHandlerProvider.mo144get();
        StatusBarStateController statusBarStateController = this.statusBarStateControllerProvider.mo144get();
        OngoingCallFlags ongoingCallFlags = this.ongoingCallFlagsProvider.mo144get();
        Objects.requireNonNull(ongoingCallFlags);
        FeatureFlags featureFlags = ongoingCallFlags.featureFlags;
        BooleanFlag booleanFlag = Flags.ONGOING_CALL_STATUS_BAR_CHIP;
        if (!featureFlags.isEnabled(booleanFlag) || !ongoingCallFlags.featureFlags.isEnabled(Flags.ONGOING_CALL_IN_IMMERSIVE)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            optional = Optional.of(statusBarWindowController);
        } else {
            optional = Optional.empty();
        }
        if (z) {
            optional2 = Optional.of(swipeStatusBarAwayGestureHandler);
        } else {
            optional2 = Optional.empty();
        }
        OngoingCallController ongoingCallController = new OngoingCallController(commonNotifCollection, ongoingCallFlags, systemClock, activityStarter, executor, iActivityManager, ongoingCallLogger, dumpManager, optional, optional2, statusBarStateController);
        dumpManager.registerDumpable(ongoingCallController);
        if (ongoingCallFlags.featureFlags.isEnabled(booleanFlag)) {
            commonNotifCollection.addCollectionListener(ongoingCallController.notifListener);
            statusBarStateController.addCallback(ongoingCallController.statusBarStateListener);
        }
        return ongoingCallController;
    }

    public StatusBarDependenciesModule_ProvideOngoingCallControllerFactory(Provider<CommonNotifCollection> provider, Provider<SystemClock> provider2, Provider<ActivityStarter> provider3, Provider<Executor> provider4, Provider<IActivityManager> provider5, Provider<OngoingCallLogger> provider6, Provider<DumpManager> provider7, Provider<StatusBarWindowController> provider8, Provider<SwipeStatusBarAwayGestureHandler> provider9, Provider<StatusBarStateController> provider10, Provider<OngoingCallFlags> provider11) {
        this.notifCollectionProvider = provider;
        this.systemClockProvider = provider2;
        this.activityStarterProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.iActivityManagerProvider = provider5;
        this.loggerProvider = provider6;
        this.dumpManagerProvider = provider7;
        this.statusBarWindowControllerProvider = provider8;
        this.swipeStatusBarAwayGestureHandlerProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.ongoingCallFlagsProvider = provider11;
    }
}
