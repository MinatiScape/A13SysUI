package com.android.systemui.dagger;

import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.FoldStateLogger;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldLatencyTracker;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.transition.ShellTransitions;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public interface SysUIComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        SysUIComponent build();

        /* renamed from: setAppPairs */
        Builder mo120setAppPairs(Optional<Object> optional);

        /* renamed from: setBackAnimation */
        Builder mo121setBackAnimation(Optional<BackAnimation> optional);

        /* renamed from: setBubbles */
        Builder mo122setBubbles(Optional<Bubbles> optional);

        /* renamed from: setCompatUI */
        Builder mo123setCompatUI(Optional<CompatUI> optional);

        /* renamed from: setDisplayAreaHelper */
        Builder mo124setDisplayAreaHelper(Optional<DisplayAreaHelper> optional);

        /* renamed from: setDragAndDrop */
        Builder mo125setDragAndDrop(Optional<DragAndDrop> optional);

        /* renamed from: setHideDisplayCutout */
        Builder mo126setHideDisplayCutout(Optional<HideDisplayCutout> optional);

        /* renamed from: setLegacySplitScreen */
        Builder mo127setLegacySplitScreen(Optional<LegacySplitScreen> optional);

        /* renamed from: setOneHanded */
        Builder mo128setOneHanded(Optional<OneHanded> optional);

        /* renamed from: setPip */
        Builder mo129setPip(Optional<Pip> optional);

        /* renamed from: setRecentTasks */
        Builder mo130setRecentTasks(Optional<RecentTasks> optional);

        /* renamed from: setShellCommandHandler */
        Builder mo131setShellCommandHandler(Optional<ShellCommandHandler> optional);

        /* renamed from: setSplitScreen */
        Builder mo132setSplitScreen(Optional<SplitScreen> optional);

        /* renamed from: setStartingSurface */
        Builder mo133setStartingSurface(Optional<StartingSurface> optional);

        /* renamed from: setTaskSurfaceHelper */
        Builder mo134setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional);

        /* renamed from: setTaskViewFactory */
        Builder mo135setTaskViewFactory(Optional<TaskViewFactory> optional);

        /* renamed from: setTransitions */
        Builder mo136setTransitions(ShellTransitions shellTransitions);
    }

    Dependency createDependency();

    DumpManager createDumpManager();

    ConfigurationController getConfigurationController();

    ContextComponentHelper getContextComponentHelper();

    Optional<FoldStateLogger> getFoldStateLogger();

    Optional<FoldStateLoggingProvider> getFoldStateLoggingProvider();

    InitController getInitController();

    Optional<MediaMuteAwaitConnectionCli> getMediaMuteAwaitConnectionCli();

    Optional<MediaTttChipControllerReceiver> getMediaTttChipControllerReceiver();

    Optional<MediaTttChipControllerSender> getMediaTttChipControllerSender();

    Optional<MediaTttCommandLineHelper> getMediaTttCommandLineHelper();

    Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider();

    Optional<NearbyMediaDevicesManager> getNearbyMediaDevicesManager();

    Map<Class<?>, Provider<CoreStartable>> getPerUserStartables();

    Map<Class<?>, Provider<CoreStartable>> getStartables();

    Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent();

    UnfoldLatencyTracker getUnfoldLatencyTracker();

    void inject(SystemUIAppComponentFactory systemUIAppComponentFactory);

    BootCompleteCacheImpl provideBootCacheImpl();

    default void init() {
        boolean z;
        getSysUIUnfoldComponent().ifPresent(SysUIComponent$$ExternalSyntheticLambda1.INSTANCE);
        getNaturalRotationUnfoldProgressProvider().ifPresent(SysUIComponent$$ExternalSyntheticLambda2.INSTANCE);
        getMediaTttChipControllerSender();
        getMediaTttChipControllerReceiver();
        getMediaTttCommandLineHelper();
        getMediaMuteAwaitConnectionCli();
        getNearbyMediaDevicesManager();
        UnfoldLatencyTracker unfoldLatencyTracker = getUnfoldLatencyTracker();
        Objects.requireNonNull(unfoldLatencyTracker);
        if (unfoldLatencyTracker.context.getResources().getIntArray(17236065).length == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            unfoldLatencyTracker.deviceStateManager.registerCallback(unfoldLatencyTracker.uiBgExecutor, unfoldLatencyTracker.foldStateListener);
            ScreenLifecycle screenLifecycle = unfoldLatencyTracker.screenLifecycle;
            Objects.requireNonNull(screenLifecycle);
            screenLifecycle.mObservers.add(unfoldLatencyTracker);
        }
        getFoldStateLoggingProvider().ifPresent(SysUIComponent$$ExternalSyntheticLambda0.INSTANCE);
        getFoldStateLogger().ifPresent(SysUIComponent$$ExternalSyntheticLambda3.INSTANCE);
    }
}
