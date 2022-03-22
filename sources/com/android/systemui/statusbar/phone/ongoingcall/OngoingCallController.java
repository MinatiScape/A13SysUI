package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.IActivityManager;
import android.app.IUidObserver;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OngoingCallController.kt */
/* loaded from: classes.dex */
public final class OngoingCallController implements CallbackController<OngoingCallListener>, Dumpable {
    public final ActivityStarter activityStarter;
    public CallNotificationInfo callNotificationInfo;
    public View chipView;
    public final IActivityManager iActivityManager;
    public boolean isCallAppVisible;
    public boolean isFullscreen;
    public final OngoingCallLogger logger;
    public final Executor mainExecutor;
    public final CommonNotifCollection notifCollection;
    public final OngoingCallFlags ongoingCallFlags;
    public final StatusBarStateController statusBarStateController;
    public final Optional<StatusBarWindowController> statusBarWindowController;
    public final Optional<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandler;
    public final SystemClock systemClock;
    public OngoingCallController$setUpUidObserver$1 uidObserver;
    public final ArrayList mListeners = new ArrayList();
    public final OngoingCallController$notifListener$1 notifListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$notifListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            String str;
            String key = notificationEntry.mSbn.getKey();
            OngoingCallController.CallNotificationInfo callNotificationInfo = OngoingCallController.this.callNotificationInfo;
            if (callNotificationInfo == null) {
                str = null;
            } else {
                str = callNotificationInfo.key;
            }
            if (Intrinsics.areEqual(key, str)) {
                OngoingCallController.access$removeChip(OngoingCallController.this);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:5:0x0015, code lost:
            if (r11.mSbn.getNotification().isStyle(android.app.Notification.CallStyle.class) == false) goto L_0x0017;
         */
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onEntryUpdated(com.android.systemui.statusbar.notification.collection.NotificationEntry r11) {
            /*
                r10 = this;
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r0 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.this
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$CallNotificationInfo r0 = r0.callNotificationInfo
                r1 = 0
                if (r0 != 0) goto L_0x0017
                boolean r0 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallControllerKt.DEBUG
                android.service.notification.StatusBarNotification r0 = r11.mSbn
                android.app.Notification r0 = r0.getNotification()
                java.lang.Class<android.app.Notification$CallStyle> r2 = android.app.Notification.CallStyle.class
                boolean r0 = r0.isStyle(r2)
                if (r0 != 0) goto L_0x002d
            L_0x0017:
                android.service.notification.StatusBarNotification r0 = r11.mSbn
                java.lang.String r0 = r0.getKey()
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r2 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.this
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$CallNotificationInfo r2 = r2.callNotificationInfo
                if (r2 != 0) goto L_0x0025
                r2 = r1
                goto L_0x0027
            L_0x0025:
                java.lang.String r2 = r2.key
            L_0x0027:
                boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r2)
                if (r0 == 0) goto L_0x008c
            L_0x002d:
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$CallNotificationInfo r0 = new com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$CallNotificationInfo
                android.service.notification.StatusBarNotification r2 = r11.mSbn
                java.lang.String r3 = r2.getKey()
                android.service.notification.StatusBarNotification r2 = r11.mSbn
                android.app.Notification r2 = r2.getNotification()
                long r4 = r2.when
                android.service.notification.StatusBarNotification r2 = r11.mSbn
                android.app.Notification r2 = r2.getNotification()
                android.app.PendingIntent r2 = r2.contentIntent
                if (r2 != 0) goto L_0x0048
                goto L_0x004c
            L_0x0048:
                android.content.Intent r1 = r2.getIntent()
            L_0x004c:
                r6 = r1
                android.service.notification.StatusBarNotification r1 = r11.mSbn
                int r7 = r1.getUid()
                android.service.notification.StatusBarNotification r11 = r11.mSbn
                android.app.Notification r11 = r11.getNotification()
                android.os.Bundle r11 = r11.extras
                r1 = -1
                java.lang.String r2 = "android.callType"
                int r11 = r11.getInt(r2, r1)
                r1 = 2
                r2 = 0
                if (r11 != r1) goto L_0x0068
                r11 = 1
                goto L_0x0069
            L_0x0068:
                r11 = r2
            L_0x0069:
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r1 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.this
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$CallNotificationInfo r1 = r1.callNotificationInfo
                if (r1 != 0) goto L_0x0070
                goto L_0x0072
            L_0x0070:
                boolean r2 = r1.statusBarSwipedAway
            L_0x0072:
                r9 = r2
                r2 = r0
                r8 = r11
                r2.<init>(r3, r4, r6, r7, r8, r9)
                boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual(r0, r1)
                if (r1 == 0) goto L_0x007f
                return
            L_0x007f:
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r10 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.this
                r10.callNotificationInfo = r0
                if (r11 == 0) goto L_0x0089
                r10.updateChip()
                goto L_0x008c
            L_0x0089:
                com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.access$removeChip(r10)
            L_0x008c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$notifListener$1.onEntryUpdated(com.android.systemui.statusbar.notification.collection.NotificationEntry):void");
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryAdded(NotificationEntry notificationEntry) {
            onEntryUpdated(notificationEntry);
        }
    };
    public final OngoingCallController$statusBarStateListener$1 statusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$statusBarStateListener$1
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onFullscreenStateChanged(boolean z) {
            OngoingCallController ongoingCallController = OngoingCallController.this;
            ongoingCallController.isFullscreen = z;
            ongoingCallController.updateChipClickListener();
            OngoingCallController.this.updateGestureListening();
        }
    };

    /* compiled from: OngoingCallController.kt */
    /* loaded from: classes.dex */
    public static final class CallNotificationInfo {
        public final long callStartTime;
        public final Intent intent;
        public final boolean isOngoing;
        public final String key;
        public final boolean statusBarSwipedAway;
        public final int uid;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CallNotificationInfo)) {
                return false;
            }
            CallNotificationInfo callNotificationInfo = (CallNotificationInfo) obj;
            return Intrinsics.areEqual(this.key, callNotificationInfo.key) && this.callStartTime == callNotificationInfo.callStartTime && Intrinsics.areEqual(this.intent, callNotificationInfo.intent) && this.uid == callNotificationInfo.uid && this.isOngoing == callNotificationInfo.isOngoing && this.statusBarSwipedAway == callNotificationInfo.statusBarSwipedAway;
        }

        public final int hashCode() {
            int i;
            int hashCode = (Long.hashCode(this.callStartTime) + (this.key.hashCode() * 31)) * 31;
            Intent intent = this.intent;
            if (intent == null) {
                i = 0;
            } else {
                i = intent.hashCode();
            }
            int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.uid, (hashCode + i) * 31, 31);
            boolean z = this.isOngoing;
            int i2 = 1;
            if (z) {
                z = true;
            }
            int i3 = z ? 1 : 0;
            int i4 = z ? 1 : 0;
            int i5 = (m + i3) * 31;
            boolean z2 = this.statusBarSwipedAway;
            if (!z2) {
                i2 = z2 ? 1 : 0;
            }
            return i5 + i2;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CallNotificationInfo(key=");
            m.append(this.key);
            m.append(", callStartTime=");
            m.append(this.callStartTime);
            m.append(", intent=");
            m.append(this.intent);
            m.append(", uid=");
            m.append(this.uid);
            m.append(", isOngoing=");
            m.append(this.isOngoing);
            m.append(", statusBarSwipedAway=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.statusBarSwipedAway, ')');
        }

        public CallNotificationInfo(String str, long j, Intent intent, int i, boolean z, boolean z2) {
            this.key = str;
            this.callStartTime = j;
            this.intent = intent;
            this.uid = i;
            this.isOngoing = z;
            this.statusBarSwipedAway = z2;
        }
    }

    public final void addCallback(OngoingCallListener ongoingCallListener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(ongoingCallListener)) {
                this.mListeners.add(ongoingCallListener);
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("Active call notification: ", this.callNotificationInfo));
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.isCallAppVisible, "Call app visible: ", printWriter);
    }

    public final boolean hasOngoingCall() {
        boolean z;
        CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        if (callNotificationInfo != null && callNotificationInfo.isOngoing) {
            z = true;
        } else {
            z = false;
        }
        if (!z || this.isCallAppVisible) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(OngoingCallListener ongoingCallListener) {
        OngoingCallListener ongoingCallListener2 = ongoingCallListener;
        synchronized (this.mListeners) {
            this.mListeners.remove(ongoingCallListener2);
        }
    }

    public final Unit tearDownChipView() {
        OngoingCallChronometer ongoingCallChronometer;
        View view = this.chipView;
        if (view == null || (ongoingCallChronometer = (OngoingCallChronometer) view.findViewById(2131428539)) == null) {
            return null;
        }
        ongoingCallChronometer.stop();
        return Unit.INSTANCE;
    }

    public final void updateChip() {
        OngoingCallChronometer ongoingCallChronometer;
        boolean z;
        final CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        if (callNotificationInfo != null) {
            View view = this.chipView;
            if (view == null) {
                ongoingCallChronometer = null;
            } else {
                ongoingCallChronometer = (OngoingCallChronometer) view.findViewById(2131428539);
            }
            if (view == null || ongoingCallChronometer == null) {
                this.callNotificationInfo = null;
                if (OngoingCallControllerKt.DEBUG) {
                    Log.w("OngoingCallController", "Ongoing call chip view could not be found; Not displaying chip in status bar");
                    return;
                }
                return;
            }
            boolean z2 = false;
            if (callNotificationInfo.callStartTime > 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                ongoingCallChronometer.shouldHideText = false;
                ongoingCallChronometer.requestLayout();
                ongoingCallChronometer.setBase(this.systemClock.elapsedRealtime() + (callNotificationInfo.callStartTime - this.systemClock.currentTimeMillis()));
                ongoingCallChronometer.start();
            } else {
                ongoingCallChronometer.shouldHideText = true;
                ongoingCallChronometer.requestLayout();
                ongoingCallChronometer.stop();
            }
            updateChipClickListener();
            if (this.iActivityManager.getUidProcessState(callNotificationInfo.uid, (String) null) <= 2) {
                z2 = true;
            }
            this.isCallAppVisible = z2;
            OngoingCallController$setUpUidObserver$1 ongoingCallController$setUpUidObserver$1 = this.uidObserver;
            if (ongoingCallController$setUpUidObserver$1 != null) {
                this.iActivityManager.unregisterUidObserver(ongoingCallController$setUpUidObserver$1);
            }
            IUidObserver.Stub ongoingCallController$setUpUidObserver$12 = new IUidObserver.Stub() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1
                public final void onUidActive(int i) {
                }

                public final void onUidCachedChanged(int i, boolean z3) {
                }

                public final void onUidGone(int i, boolean z3) {
                }

                public final void onUidIdle(int i, boolean z3) {
                }

                public final void onUidStateChanged(int i, int i2, long j, int i3) {
                    boolean z3;
                    OngoingCallController.CallNotificationInfo callNotificationInfo2 = OngoingCallController.CallNotificationInfo.this;
                    Objects.requireNonNull(callNotificationInfo2);
                    if (i == callNotificationInfo2.uid) {
                        final OngoingCallController ongoingCallController = this;
                        boolean z4 = ongoingCallController.isCallAppVisible;
                        if (i2 <= 2) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        ongoingCallController.isCallAppVisible = z3;
                        if (z4 != z3) {
                            ongoingCallController.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Iterator it = OngoingCallController.this.mListeners.iterator();
                                    while (it.hasNext()) {
                                        ((OngoingCallListener) it.next()).onOngoingCallStateChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            };
            this.uidObserver = ongoingCallController$setUpUidObserver$12;
            this.iActivityManager.registerUidObserver(ongoingCallController$setUpUidObserver$12, 1, -1, (String) null);
            if (!callNotificationInfo.statusBarSwipedAway) {
                this.statusBarWindowController.ifPresent(OngoingCallController$updateChip$1.INSTANCE);
            }
            updateGestureListening();
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                ((OngoingCallListener) it.next()).onOngoingCallStateChanged();
            }
        }
    }

    public final void updateChipClickListener() {
        final View view;
        boolean z;
        if (this.callNotificationInfo != null) {
            final Intent intent = null;
            if (this.isFullscreen) {
                OngoingCallFlags ongoingCallFlags = this.ongoingCallFlags;
                Objects.requireNonNull(ongoingCallFlags);
                boolean z2 = true;
                if (!ongoingCallFlags.featureFlags.isEnabled(Flags.ONGOING_CALL_STATUS_BAR_CHIP) || !ongoingCallFlags.featureFlags.isEnabled(Flags.ONGOING_CALL_IN_IMMERSIVE)) {
                    z = false;
                } else {
                    z = true;
                }
                if (!z || !ongoingCallFlags.featureFlags.isEnabled(Flags.ONGOING_CALL_IN_IMMERSIVE_CHIP_TAP)) {
                    z2 = false;
                }
                if (!z2) {
                    View view2 = this.chipView;
                    if (view2 != null) {
                        view2.setOnClickListener(null);
                        return;
                    }
                    return;
                }
            }
            View view3 = this.chipView;
            if (view3 == null) {
                view = null;
            } else {
                view = view3.findViewById(2131428538);
            }
            CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
            if (callNotificationInfo != null) {
                intent = callNotificationInfo.intent;
            }
            if (view3 != null && view != null && intent != null) {
                view3.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$updateChipClickListener$1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view4) {
                        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController;
                        OngoingCallLogger ongoingCallLogger = OngoingCallController.this.logger;
                        Objects.requireNonNull(ongoingCallLogger);
                        ongoingCallLogger.logger.log(OngoingCallLogger.OngoingCallEvents.ONGOING_CALL_CLICKED);
                        ActivityStarter activityStarter = OngoingCallController.this.activityStarter;
                        Intent intent2 = intent;
                        View view5 = view;
                        if (!(view5.getParent() instanceof ViewGroup)) {
                            Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view5 + " is not attached to a ViewGroup", new Exception());
                            ghostedViewLaunchAnimatorController = null;
                        } else {
                            ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(view5, (Integer) 34, 4);
                        }
                        activityStarter.postStartActivityDismissingKeyguard(intent2, 0, ghostedViewLaunchAnimatorController);
                    }
                });
            }
        }
    }

    public final void updateGestureListening() {
        CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        if (callNotificationInfo != null) {
            boolean z = false;
            if (callNotificationInfo != null && callNotificationInfo.statusBarSwipedAway) {
                z = true;
            }
            if (!z && this.isFullscreen) {
                this.swipeStatusBarAwayGestureHandler.ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$updateGestureListening$2

                    /* compiled from: OngoingCallController.kt */
                    /* renamed from: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$updateGestureListening$2$1  reason: invalid class name */
                    /* loaded from: classes.dex */
                    final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Unit> {
                        public AnonymousClass1(OngoingCallController ongoingCallController) {
                            super(0, ongoingCallController, OngoingCallController.class, "onSwipeAwayGestureDetected", "onSwipeAwayGestureDetected()V", 0);
                        }

                        @Override // kotlin.jvm.functions.Function0
                        public final Unit invoke() {
                            OngoingCallController.CallNotificationInfo callNotificationInfo;
                            OngoingCallController ongoingCallController = (OngoingCallController) this.receiver;
                            Objects.requireNonNull(ongoingCallController);
                            if (OngoingCallControllerKt.DEBUG) {
                                Log.d("OngoingCallController", "Swipe away gesture detected");
                            }
                            OngoingCallController.CallNotificationInfo callNotificationInfo2 = ongoingCallController.callNotificationInfo;
                            if (callNotificationInfo2 == null) {
                                callNotificationInfo = null;
                            } else {
                                callNotificationInfo = new OngoingCallController.CallNotificationInfo(callNotificationInfo2.key, callNotificationInfo2.callStartTime, callNotificationInfo2.intent, callNotificationInfo2.uid, callNotificationInfo2.isOngoing, true);
                            }
                            ongoingCallController.callNotificationInfo = callNotificationInfo;
                            ongoingCallController.statusBarWindowController.ifPresent(OngoingCallController$onSwipeAwayGestureDetected$1.INSTANCE);
                            ongoingCallController.swipeStatusBarAwayGestureHandler.ifPresent(OngoingCallController$onSwipeAwayGestureDetected$2.INSTANCE);
                            return Unit.INSTANCE;
                        }
                    }

                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((SwipeStatusBarAwayGestureHandler) obj).addOnGestureDetectedCallback(new AnonymousClass1(OngoingCallController.this));
                    }
                });
                return;
            }
        }
        this.swipeStatusBarAwayGestureHandler.ifPresent(OngoingCallController$updateGestureListening$1.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$notifListener$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$statusBarStateListener$1] */
    public OngoingCallController(CommonNotifCollection commonNotifCollection, OngoingCallFlags ongoingCallFlags, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger, DumpManager dumpManager, Optional<StatusBarWindowController> optional, Optional<SwipeStatusBarAwayGestureHandler> optional2, StatusBarStateController statusBarStateController) {
        this.notifCollection = commonNotifCollection;
        this.ongoingCallFlags = ongoingCallFlags;
        this.systemClock = systemClock;
        this.activityStarter = activityStarter;
        this.mainExecutor = executor;
        this.iActivityManager = iActivityManager;
        this.logger = ongoingCallLogger;
        this.statusBarWindowController = optional;
        this.swipeStatusBarAwayGestureHandler = optional2;
        this.statusBarStateController = statusBarStateController;
    }

    public static final void access$removeChip(OngoingCallController ongoingCallController) {
        Objects.requireNonNull(ongoingCallController);
        ongoingCallController.callNotificationInfo = null;
        ongoingCallController.tearDownChipView();
        ongoingCallController.statusBarWindowController.ifPresent(OngoingCallController$removeChip$1.INSTANCE);
        ongoingCallController.swipeStatusBarAwayGestureHandler.ifPresent(OngoingCallController$removeChip$2.INSTANCE);
        Iterator it = ongoingCallController.mListeners.iterator();
        while (it.hasNext()) {
            ((OngoingCallListener) it.next()).onOngoingCallStateChanged();
        }
        OngoingCallController$setUpUidObserver$1 ongoingCallController$setUpUidObserver$1 = ongoingCallController.uidObserver;
        if (ongoingCallController$setUpUidObserver$1 != null) {
            ongoingCallController.iActivityManager.unregisterUidObserver(ongoingCallController$setUpUidObserver$1);
        }
    }
}
