package com.android.systemui.statusbar.events;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Process;
import android.provider.DeviceConfig;
import com.android.systemui.privacy.PrivacyApplication;
import com.android.systemui.privacy.PrivacyChipBuilder;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemEventCoordinator.kt */
/* loaded from: classes.dex */
public final class SystemEventCoordinator {
    public final BatteryController batteryController;
    public final Context context;
    public final PrivacyItemController privacyController;
    public final SystemEventCoordinator$privacyStateListener$1 privacyStateListener = new PrivacyItemController.Callback() { // from class: com.android.systemui.statusbar.events.SystemEventCoordinator$privacyStateListener$1
        public List<PrivacyItem> currentPrivacyItems;
        public List<PrivacyItem> previousPrivacyItems;
        public long timeLastEmpty;

        {
            EmptyList emptyList = EmptyList.INSTANCE;
            this.currentPrivacyItems = emptyList;
            this.previousPrivacyItems = emptyList;
            this.timeLastEmpty = SystemEventCoordinator.this.systemClock.elapsedRealtime();
        }

        public static boolean uniqueItemsMatch(List list, List list2) {
            ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10));
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PrivacyItem privacyItem = (PrivacyItem) it.next();
                Objects.requireNonNull(privacyItem);
                PrivacyApplication privacyApplication = privacyItem.application;
                Objects.requireNonNull(privacyApplication);
                arrayList.add(new Pair(Integer.valueOf(privacyApplication.uid), privacyItem.privacyType.getPermGroupName()));
            }
            Set set = CollectionsKt___CollectionsKt.toSet(arrayList);
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list2, 10));
            Iterator it2 = list2.iterator();
            while (it2.hasNext()) {
                PrivacyItem privacyItem2 = (PrivacyItem) it2.next();
                Objects.requireNonNull(privacyItem2);
                PrivacyApplication privacyApplication2 = privacyItem2.application;
                Objects.requireNonNull(privacyApplication2);
                arrayList2.add(new Pair(Integer.valueOf(privacyApplication2.uid), privacyItem2.privacyType.getPermGroupName()));
            }
            return Intrinsics.areEqual(set, CollectionsKt___CollectionsKt.toSet(arrayList2));
        }

        @Override // com.android.systemui.privacy.PrivacyItemController.Callback
        public final void onPrivacyItemsChanged(List<PrivacyItem> list) {
            boolean z;
            boolean z2;
            int i;
            int i2;
            if (!uniqueItemsMatch(list, this.currentPrivacyItems)) {
                if (list.isEmpty()) {
                    this.previousPrivacyItems = this.currentPrivacyItems;
                    this.timeLastEmpty = SystemEventCoordinator.this.systemClock.elapsedRealtime();
                }
                this.currentPrivacyItems = list;
                boolean z3 = false;
                SystemStatusAnimationScheduler systemStatusAnimationScheduler = null;
                if (list.isEmpty()) {
                    SystemEventCoordinator systemEventCoordinator = SystemEventCoordinator.this;
                    Objects.requireNonNull(systemEventCoordinator);
                    SystemStatusAnimationScheduler systemStatusAnimationScheduler2 = systemEventCoordinator.scheduler;
                    if (systemStatusAnimationScheduler2 != null) {
                        systemStatusAnimationScheduler = systemStatusAnimationScheduler2;
                    }
                    Objects.requireNonNull(systemStatusAnimationScheduler);
                    if (systemStatusAnimationScheduler.hasPersistentDot && DeviceConfig.getBoolean("privacy", "enable_immersive_indicator", true)) {
                        systemStatusAnimationScheduler.hasPersistentDot = false;
                        LinkedHashSet<SystemStatusAnimationCallback> linkedHashSet = systemStatusAnimationScheduler.listeners;
                        ArrayList arrayList = new ArrayList();
                        for (SystemStatusAnimationCallback systemStatusAnimationCallback : linkedHashSet) {
                            systemStatusAnimationCallback.onHidePersistentDot();
                        }
                        if (systemStatusAnimationScheduler.animationState == 4) {
                            systemStatusAnimationScheduler.animationState = 0;
                        }
                        if (!arrayList.isEmpty()) {
                            new AnimatorSet().playTogether(arrayList);
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (!DeviceConfig.getBoolean("privacy", "privacy_chip_animation_enabled", true) || (uniqueItemsMatch(this.currentPrivacyItems, this.previousPrivacyItems) && SystemEventCoordinator.this.systemClock.elapsedRealtime() - this.timeLastEmpty < 3000)) {
                    z = false;
                } else {
                    z = true;
                }
                SystemEventCoordinator systemEventCoordinator2 = SystemEventCoordinator.this;
                Objects.requireNonNull(systemEventCoordinator2);
                PrivacyEvent privacyEvent = new PrivacyEvent(z);
                SystemEventCoordinator$privacyStateListener$1 systemEventCoordinator$privacyStateListener$1 = systemEventCoordinator2.privacyStateListener;
                Objects.requireNonNull(systemEventCoordinator$privacyStateListener$1);
                List<PrivacyItem> list2 = systemEventCoordinator$privacyStateListener$1.currentPrivacyItems;
                privacyEvent.privacyItems = list2;
                privacyEvent.contentDescription = systemEventCoordinator2.context.getString(2131952940, new PrivacyChipBuilder(systemEventCoordinator2.context, list2).joinTypes());
                final SystemStatusAnimationScheduler systemStatusAnimationScheduler3 = systemEventCoordinator2.scheduler;
                if (systemStatusAnimationScheduler3 == null) {
                    systemStatusAnimationScheduler3 = null;
                }
                Objects.requireNonNull(systemStatusAnimationScheduler3);
                if (systemStatusAnimationScheduler3.systemClock.uptimeMillis() - Process.getStartUptimeMillis() < 5000) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2 && DeviceConfig.getBoolean("privacy", "enable_immersive_indicator", true)) {
                    Assert.isMainThread();
                    int priority = privacyEvent.getPriority();
                    StatusEvent statusEvent = systemStatusAnimationScheduler3.scheduledEvent;
                    if (statusEvent == null) {
                        i = -1;
                    } else {
                        i = statusEvent.getPriority();
                    }
                    if (priority <= i || (i2 = systemStatusAnimationScheduler3.animationState) == 3 || i2 == 4 || !privacyEvent.getForceVisible()) {
                        StatusEvent statusEvent2 = systemStatusAnimationScheduler3.scheduledEvent;
                        if (statusEvent2 != null && statusEvent2.shouldUpdateFromEvent(privacyEvent)) {
                            z3 = true;
                        }
                        if (z3) {
                            StatusEvent statusEvent3 = systemStatusAnimationScheduler3.scheduledEvent;
                            if (statusEvent3 != null) {
                                statusEvent3.updateFromEvent(privacyEvent);
                            }
                            if (privacyEvent.getForceVisible()) {
                                systemStatusAnimationScheduler3.hasPersistentDot = true;
                                systemStatusAnimationScheduler3.notifyTransitionToPersistentDot();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    systemStatusAnimationScheduler3.scheduledEvent = privacyEvent;
                    if (privacyEvent.getForceVisible()) {
                        systemStatusAnimationScheduler3.hasPersistentDot = true;
                    }
                    if (privacyEvent.getShowAnimation() || !privacyEvent.getForceVisible()) {
                        systemStatusAnimationScheduler3.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$scheduleEvent$1
                            @Override // java.lang.Runnable
                            public final void run() {
                                Objects.requireNonNull(SystemStatusAnimationScheduler.this);
                                SystemStatusAnimationScheduler systemStatusAnimationScheduler4 = SystemStatusAnimationScheduler.this;
                                systemStatusAnimationScheduler4.animationState = 1;
                                StatusBarWindowController statusBarWindowController = systemStatusAnimationScheduler4.statusBarWindowController;
                                Objects.requireNonNull(statusBarWindowController);
                                StatusBarWindowController.State state = statusBarWindowController.mCurrentState;
                                state.mForceStatusBarVisible = true;
                                statusBarWindowController.apply(state);
                                ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                                ofFloat.setDuration(250L);
                                ofFloat.addListener(SystemStatusAnimationScheduler.this.systemAnimatorAdapter);
                                ofFloat.addUpdateListener(SystemStatusAnimationScheduler.this.systemUpdateListener);
                                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                ofFloat2.setDuration(250L);
                                SystemStatusAnimationScheduler systemStatusAnimationScheduler5 = SystemStatusAnimationScheduler.this;
                                StatusEvent statusEvent4 = systemStatusAnimationScheduler5.scheduledEvent;
                                Intrinsics.checkNotNull(statusEvent4);
                                ofFloat2.addListener(new SystemStatusAnimationScheduler.ChipAnimatorAdapter(2, statusEvent4.getViewCreator()));
                                ofFloat2.addUpdateListener(SystemStatusAnimationScheduler.this.chipUpdateListener);
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.playSequentially(ofFloat, ofFloat2);
                                animatorSet.start();
                                final SystemStatusAnimationScheduler systemStatusAnimationScheduler6 = SystemStatusAnimationScheduler.this;
                                systemStatusAnimationScheduler6.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.SystemStatusAnimationScheduler$scheduleEvent$1.1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        int i3;
                                        AnimatorSet notifyTransitionToPersistentDot;
                                        SystemStatusAnimationScheduler.this.animationState = 3;
                                        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
                                        ofFloat3.setDuration(250L);
                                        ofFloat3.addListener(SystemStatusAnimationScheduler.this.systemAnimatorAdapter);
                                        ofFloat3.addUpdateListener(SystemStatusAnimationScheduler.this.systemUpdateListener);
                                        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(1.0f, 0.0f);
                                        ofFloat4.setDuration(250L);
                                        SystemStatusAnimationScheduler systemStatusAnimationScheduler7 = SystemStatusAnimationScheduler.this;
                                        Objects.requireNonNull(systemStatusAnimationScheduler7);
                                        if (systemStatusAnimationScheduler7.hasPersistentDot) {
                                            i3 = 4;
                                        } else {
                                            i3 = 0;
                                        }
                                        SystemStatusAnimationScheduler systemStatusAnimationScheduler8 = SystemStatusAnimationScheduler.this;
                                        StatusEvent statusEvent5 = systemStatusAnimationScheduler8.scheduledEvent;
                                        Intrinsics.checkNotNull(statusEvent5);
                                        ofFloat4.addListener(new SystemStatusAnimationScheduler.ChipAnimatorAdapter(i3, statusEvent5.getViewCreator()));
                                        ofFloat4.addUpdateListener(SystemStatusAnimationScheduler.this.chipUpdateListener);
                                        AnimatorSet animatorSet2 = new AnimatorSet();
                                        animatorSet2.play(ofFloat4).before(ofFloat3);
                                        SystemStatusAnimationScheduler systemStatusAnimationScheduler9 = SystemStatusAnimationScheduler.this;
                                        Objects.requireNonNull(systemStatusAnimationScheduler9);
                                        if (systemStatusAnimationScheduler9.hasPersistentDot && (notifyTransitionToPersistentDot = SystemStatusAnimationScheduler.this.notifyTransitionToPersistentDot()) != null) {
                                            animatorSet2.playTogether(ofFloat3, notifyTransitionToPersistentDot);
                                        }
                                        animatorSet2.start();
                                        StatusBarWindowController statusBarWindowController2 = SystemStatusAnimationScheduler.this.statusBarWindowController;
                                        Objects.requireNonNull(statusBarWindowController2);
                                        StatusBarWindowController.State state2 = statusBarWindowController2.mCurrentState;
                                        state2.mForceStatusBarVisible = false;
                                        statusBarWindowController2.apply(state2);
                                        SystemStatusAnimationScheduler.this.scheduledEvent = null;
                                    }
                                }, 1500L);
                            }
                        }, 0L);
                        return;
                    }
                    systemStatusAnimationScheduler3.notifyTransitionToPersistentDot();
                    systemStatusAnimationScheduler3.scheduledEvent = null;
                }
            }
        }
    };
    public SystemStatusAnimationScheduler scheduler;
    public final SystemClock systemClock;

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.events.SystemEventCoordinator$privacyStateListener$1] */
    public SystemEventCoordinator(SystemClock systemClock, BatteryController batteryController, PrivacyItemController privacyItemController, Context context) {
        this.systemClock = systemClock;
        this.batteryController = batteryController;
        this.privacyController = privacyItemController;
        this.context = context;
    }
}
