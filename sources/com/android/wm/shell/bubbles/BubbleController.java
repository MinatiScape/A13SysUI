package com.android.wm.shell.bubbles;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseSetArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.systemui.keyguard.KeyguardViewMediator$9$$ExternalSyntheticLambda0;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda2;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda3;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda7;
import com.android.systemui.wmshell.BubblesManager$8$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda7;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.ShellExecutor$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import com.android.wm.shell.pip.phone.PipController$PipImpl$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.phone.PipController$PipImpl$$ExternalSyntheticLambda1;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public final class BubbleController {
    public final IStatusBarService mBarService;
    public BubbleBadgeIconFactory mBubbleBadgeIconFactory;
    public BubbleData mBubbleData;
    public BubbleIconFactory mBubbleIconFactory;
    public BubblePositioner mBubblePositioner;
    public final Context mContext;
    public SparseArray<UserInfo> mCurrentProfiles;
    public int mCurrentUserId;
    public final BubbleDataRepository mDataRepository;
    public final DisplayController mDisplayController;
    public BubbleController$$ExternalSyntheticLambda4 mExpandListener;
    public final FloatingContentCoordinator mFloatingContentCoordinator;
    public boolean mInflateSynchronously;
    public final LauncherApps mLauncherApps;
    public BubbleLogger mLogger;
    public final ShellExecutor mMainExecutor;
    public final Handler mMainHandler;
    public BubbleEntry mNotifEntryToExpandOnShadeUnlock;
    public Optional<OneHandedController> mOneHandedOptional;
    public final SparseSetArray<String> mSavedBubbleKeysPerUser;
    public BubbleStackView mStackView;
    public BubbleStackView.SurfaceSynchronizer mSurfaceSynchronizer;
    public final SyncTransactionQueue mSyncQueue;
    public Bubbles.SysuiProxy mSysuiProxy;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TaskStackListenerImpl mTaskStackListener;
    public final TaskViewTransitions mTaskViewTransitions;
    public NotificationListenerService.Ranking mTmpRanking;
    public WindowInsets mWindowInsets;
    public final WindowManager mWindowManager;
    public final WindowManagerShellWrapper mWindowManagerShellWrapper;
    public WindowManager.LayoutParams mWmLayoutParams;
    public final BubblesImpl mImpl = new BubblesImpl();
    public BubbleData.Listener mOverflowListener = null;
    public boolean mOverflowDataLoadNeeded = true;
    public boolean mAddedToWindowManager = false;
    public int mDensityDpi = 0;
    public Rect mScreenBounds = new Rect();
    public float mFontScale = 0.0f;
    public int mLayoutDirection = -1;
    public boolean mIsStatusBarShade = true;
    public final AnonymousClass5 mBubbleDataListener = new AnonymousClass5();

    /* renamed from: com.android.wm.shell.bubbles.BubbleController$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 implements TaskStackListenerCallback {
        public AnonymousClass3() {
        }

        @Override // com.android.wm.shell.common.TaskStackListenerCallback
        public final void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2) {
            for (Bubble bubble : BubbleController.this.mBubbleData.getBubbles()) {
                if (runningTaskInfo.taskId == bubble.getTaskId()) {
                    BubbleController.this.mBubbleData.setSelectedBubble(bubble);
                    BubbleController.this.mBubbleData.setExpanded(true);
                    return;
                }
            }
            for (Bubble bubble2 : BubbleController.this.mBubbleData.getOverflowBubbles()) {
                if (runningTaskInfo.taskId == bubble2.getTaskId()) {
                    BubbleController.this.promoteBubbleFromOverflow(bubble2);
                    BubbleController.this.mBubbleData.setExpanded(true);
                    return;
                }
            }
        }

        @Override // com.android.wm.shell.common.TaskStackListenerCallback
        public final void onTaskMovedToFront(final int i) {
            Bubbles.SysuiProxy sysuiProxy = BubbleController.this.mSysuiProxy;
            if (sysuiProxy != null) {
                Consumer bubbleController$3$$ExternalSyntheticLambda1 = new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleController$3$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        BubbleController.AnonymousClass3 r0 = BubbleController.AnonymousClass3.this;
                        int i2 = i;
                        Objects.requireNonNull(r0);
                        BubbleController.this.mMainExecutor.execute(new BubbleController$3$$ExternalSyntheticLambda0(r0, (Boolean) obj, i2, 0));
                    }
                };
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) sysuiProxy;
                Objects.requireNonNull(r0);
                r0.val$sysuiMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda2(r0, bubbleController$3$$ExternalSyntheticLambda1, 0));
            }
        }
    }

    /* renamed from: com.android.wm.shell.bubbles.BubbleController$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements BubbleData.Listener {
        public AnonymousClass5() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:105:0x0261, code lost:
            if (r15.removeIf(new com.android.wm.shell.bubbles.storage.BubbleVolatileRepository$removeBubbles$1$1<>(r14)) == false) goto L_0x0235;
         */
        /* JADX WARN: Code restructure failed: missing block: B:106:0x0263, code lost:
            r11.add(r13);
         */
        @Override // com.android.wm.shell.bubbles.BubbleData.Listener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void applyUpdate(com.android.wm.shell.bubbles.BubbleData.Update r17) {
            /*
                Method dump skipped, instructions count: 1341
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController.AnonymousClass5.applyUpdate(com.android.wm.shell.bubbles.BubbleData$Update):void");
        }
    }

    /* loaded from: classes.dex */
    public class BubblesImeListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        public BubblesImeListener() {
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public final void onImeVisibilityChanged(boolean z, int i) {
            BubbleViewProvider bubbleViewProvider;
            float f;
            BubblePositioner bubblePositioner = BubbleController.this.mBubblePositioner;
            Objects.requireNonNull(bubblePositioner);
            bubblePositioner.mImeVisible = z;
            bubblePositioner.mImeHeight = i;
            BubbleStackView bubbleStackView = BubbleController.this.mStackView;
            if (bubbleStackView == null) {
                return;
            }
            if ((bubbleStackView.mIsExpansionAnimating || bubbleStackView.mIsBubbleSwitchAnimating) && bubbleStackView.mIsExpanded) {
                bubbleStackView.mExpandedAnimationController.expandFromStack(new ScreenDecorations$$ExternalSyntheticLambda2(bubbleStackView, 8));
            } else if (!bubbleStackView.mIsExpanded && bubbleStackView.getBubbleCount() > 0) {
                StackAnimationController stackAnimationController = bubbleStackView.mStackAnimationController;
                Objects.requireNonNull(stackAnimationController);
                float f2 = stackAnimationController.getAllowableStackPositionRegion().bottom;
                if (z) {
                    float f3 = stackAnimationController.mStackPosition.y;
                    if (f3 > f2 && stackAnimationController.mPreImeY == -1.4E-45f) {
                        stackAnimationController.mPreImeY = f3;
                        f = f2;
                    }
                    f = -1.4E-45f;
                } else {
                    f2 = stackAnimationController.mPreImeY;
                    if (f2 != -1.4E-45f) {
                        stackAnimationController.mPreImeY = -1.4E-45f;
                        f = f2;
                    }
                    f = -1.4E-45f;
                }
                int i2 = (f > (-1.4E-45f) ? 1 : (f == (-1.4E-45f) ? 0 : -1));
                if (i2 != 0) {
                    DynamicAnimation.AnonymousClass2 r2 = DynamicAnimation.TRANSLATION_Y;
                    SpringForce springForce = stackAnimationController.getSpringForce();
                    springForce.setStiffness(200.0f);
                    stackAnimationController.springFirstBubbleWithStackFollowing(r2, springForce, 0.0f, f, new Runnable[0]);
                    stackAnimationController.notifyFloatingCoordinatorStackAnimatingTo(stackAnimationController.mStackPosition.x, f);
                }
                if (i2 == 0) {
                    f = stackAnimationController.mStackPosition.y;
                }
                StackAnimationController stackAnimationController2 = bubbleStackView.mStackAnimationController;
                Objects.requireNonNull(stackAnimationController2);
                float f4 = f - stackAnimationController2.mStackPosition.y;
                if (bubbleStackView.mFlyout.getVisibility() == 0) {
                    BubbleFlyoutView bubbleFlyoutView = bubbleStackView.mFlyout;
                    Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                    PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(bubbleFlyoutView);
                    instance.spring(DynamicAnimation.TRANSLATION_Y, bubbleStackView.mFlyout.getTranslationY() + f4, 0.0f, BubbleStackView.FLYOUT_IME_ANIMATION_SPRING_CONFIG);
                    instance.start();
                }
            } else if (bubbleStackView.mPositioner.showBubblesVertically() && bubbleStackView.mIsExpanded && (bubbleViewProvider = bubbleStackView.mExpandedBubble) != null && bubbleViewProvider.getExpandedView() != null) {
                BubbleExpandedView expandedView = bubbleStackView.mExpandedBubble.getExpandedView();
                Objects.requireNonNull(expandedView);
                expandedView.mImeVisible = z;
                if (!z && expandedView.mNeedsNewHeight) {
                    expandedView.updateHeight();
                }
                ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < bubbleStackView.mBubbleContainer.getChildCount(); i3++) {
                    arrayList.add(ObjectAnimator.ofFloat(bubbleStackView.mBubbleContainer.getChildAt(i3), FrameLayout.TRANSLATION_Y, bubbleStackView.mPositioner.getExpandedBubbleXY(i3, bubbleStackView.getState()).y));
                }
                bubbleStackView.updatePointerPosition(true);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(arrayList);
                animatorSet.start();
            }
        }
    }

    /* loaded from: classes.dex */
    public class BubblesImpl implements Bubbles {
        public CachedState mCachedState = new CachedState();

        @VisibleForTesting
        /* loaded from: classes.dex */
        public class CachedState {
            public boolean mIsStackExpanded;
            public String mSelectedBubbleKey;
            public HashSet<String> mSuppressedBubbleKeys = new HashSet<>();
            public HashMap<String, String> mSuppressedGroupToNotifKeys = new HashMap<>();
            public HashMap<String, Bubble> mShortcutIdToBubble = new HashMap<>();
            public ArrayList<Bubble> mTmpBubbles = new ArrayList<>();

            public CachedState() {
            }
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void expandStackAndSelectBubble(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda3(this, bubbleEntry, 1));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onNotificationChannelModified(final String str, final UserHandle userHandle, final NotificationChannel notificationChannel, final int i) {
            if (i == 2 || i == 3) {
                BubbleController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        BubbleController.BubblesImpl bubblesImpl = BubbleController.BubblesImpl.this;
                        String str2 = str;
                        UserHandle userHandle2 = userHandle;
                        NotificationChannel notificationChannel2 = notificationChannel;
                        int i2 = i;
                        Objects.requireNonNull(bubblesImpl);
                        BubbleController.this.onNotificationChannelModified(str2, userHandle2, notificationChannel2, i2);
                    }
                });
            }
        }

        public BubblesImpl() {
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void collapseStack() {
            BubbleController.this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, 8));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            try {
                BubbleController.this.mMainExecutor.executeBlocking$1(new BubbleController$BubblesImpl$$ExternalSyntheticLambda2(this, fileDescriptor, printWriter, strArr));
            } catch (InterruptedException unused) {
                Slog.e("Bubbles", "Failed to dump BubbleController in 2s");
            }
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void expandStackAndSelectBubble(Bubble bubble) {
            BubbleController.this.mMainExecutor.execute(new TaskView$$ExternalSyntheticLambda7(this, bubble, 4));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final Bubble getBubbleWithShortcutId(String str) {
            Bubble bubble;
            CachedState cachedState = this.mCachedState;
            Objects.requireNonNull(cachedState);
            synchronized (cachedState) {
                bubble = cachedState.mShortcutIdToBubble.get(str);
            }
            return bubble;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda10] */
        /* JADX WARN: Type inference failed for: r5v1, types: [com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda11] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // com.android.wm.shell.bubbles.Bubbles
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean handleDismissalInterception(final com.android.wm.shell.bubbles.BubbleEntry r2, final java.util.ArrayList r3, final com.android.systemui.wmshell.BubblesManager$$ExternalSyntheticLambda1 r4, final java.util.concurrent.Executor r5) {
            /*
                r1 = this;
                com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda10 r0 = new com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda10
                r0.<init>()
                com.android.wm.shell.bubbles.BubbleController r4 = com.android.wm.shell.bubbles.BubbleController.this
                com.android.wm.shell.common.ShellExecutor r4 = r4.mMainExecutor
                com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda11 r5 = new com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda11
                r5.<init>()
                java.lang.Object r1 = r4.executeBlockingForResult(r5)
                java.lang.Boolean r1 = (java.lang.Boolean) r1
                boolean r1 = r1.booleanValue()
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController.BubblesImpl.handleDismissalInterception(com.android.wm.shell.bubbles.BubbleEntry, java.util.ArrayList, com.android.systemui.wmshell.BubblesManager$$ExternalSyntheticLambda1, java.util.concurrent.Executor):boolean");
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final boolean isBubbleExpanded(String str) {
            boolean z;
            CachedState cachedState = this.mCachedState;
            Objects.requireNonNull(cachedState);
            synchronized (cachedState) {
                if (cachedState.mIsStackExpanded) {
                    if (str.equals(cachedState.mSelectedBubbleKey)) {
                        z = true;
                    }
                }
                z = false;
            }
            return z;
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0020, code lost:
            if (r2.equals(r1.mSuppressedGroupToNotifKeys.get(r3)) != false) goto L_0x0025;
         */
        @Override // com.android.wm.shell.bubbles.Bubbles
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean isBubbleNotificationSuppressedFromShade(java.lang.String r2, java.lang.String r3) {
            /*
                r1 = this;
                com.android.wm.shell.bubbles.BubbleController$BubblesImpl$CachedState r1 = r1.mCachedState
                java.util.Objects.requireNonNull(r1)
                monitor-enter(r1)
                java.util.HashSet<java.lang.String> r0 = r1.mSuppressedBubbleKeys     // Catch: all -> 0x0028
                boolean r0 = r0.contains(r2)     // Catch: all -> 0x0028
                if (r0 != 0) goto L_0x0025
                java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.mSuppressedGroupToNotifKeys     // Catch: all -> 0x0028
                boolean r0 = r0.containsKey(r3)     // Catch: all -> 0x0028
                if (r0 == 0) goto L_0x0023
                java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.mSuppressedGroupToNotifKeys     // Catch: all -> 0x0028
                java.lang.Object r3 = r0.get(r3)     // Catch: all -> 0x0028
                boolean r2 = r2.equals(r3)     // Catch: all -> 0x0028
                if (r2 == 0) goto L_0x0023
                goto L_0x0025
            L_0x0023:
                r2 = 0
                goto L_0x0026
            L_0x0025:
                r2 = 1
            L_0x0026:
                monitor-exit(r1)
                return r2
            L_0x0028:
                r2 = move-exception
                monitor-exit(r1)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController.BubblesImpl.isBubbleNotificationSuppressedFromShade(java.lang.String, java.lang.String):boolean");
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final boolean isStackExpanded() {
            boolean z;
            CachedState cachedState = this.mCachedState;
            Objects.requireNonNull(cachedState);
            synchronized (cachedState) {
                z = cachedState.mIsStackExpanded;
            }
            return z;
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onConfigChanged(Configuration configuration) {
            BubbleController.this.mMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda1(this, configuration, 4));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
            BubbleController.this.mMainExecutor.execute(new ShellExecutor$$ExternalSyntheticLambda0(this, sparseArray, 4));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onEntryAdded(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda0(this, bubbleEntry, 0));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onEntryRemoved(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new KeyguardViewMediator$9$$ExternalSyntheticLambda0(this, bubbleEntry, 2));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onEntryUpdated(final BubbleEntry bubbleEntry, final boolean z) {
            BubbleController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    BubbleController.BubblesImpl bubblesImpl = BubbleController.BubblesImpl.this;
                    BubbleEntry bubbleEntry2 = bubbleEntry;
                    boolean z2 = z;
                    Objects.requireNonNull(bubblesImpl);
                    BubbleController.this.onEntryUpdated(bubbleEntry2, z2);
                }
            });
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda1(this, rankingMap, hashMap, 0));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onStatusBarStateChanged(final boolean z) {
            BubbleController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    BubbleController.BubblesImpl bubblesImpl = BubbleController.BubblesImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(bubblesImpl);
                    BubbleController bubbleController = BubbleController.this;
                    Objects.requireNonNull(bubbleController);
                    bubbleController.mIsStatusBarShade = z2;
                    if (!z2) {
                        bubbleController.collapseStack();
                    }
                    BubbleEntry bubbleEntry = bubbleController.mNotifEntryToExpandOnShadeUnlock;
                    if (bubbleEntry != null) {
                        bubbleController.expandStackAndSelectBubble(bubbleEntry);
                        bubbleController.mNotifEntryToExpandOnShadeUnlock = null;
                    }
                    bubbleController.updateStack();
                }
            });
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onStatusBarVisibilityChanged(final boolean z) {
            BubbleController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z2;
                    BubbleController.BubblesImpl bubblesImpl = BubbleController.BubblesImpl.this;
                    boolean z3 = z;
                    Objects.requireNonNull(bubblesImpl);
                    BubbleController bubbleController = BubbleController.this;
                    Objects.requireNonNull(bubbleController);
                    BubbleStackView bubbleStackView = bubbleController.mStackView;
                    if (bubbleStackView != null) {
                        if (z3 || bubbleController.isStackExpanded()) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        bubbleStackView.mTemporarilyInvisible = z2;
                        bubbleStackView.updateTemporarilyInvisibleAnimation(z2);
                    }
                }
            });
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onUserChanged(int i) {
            BubbleController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda1(this, i, 1));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void onZenStateChanged() {
            BubbleController.this.mMainExecutor.execute(new CreateUserActivity$$ExternalSyntheticLambda1(this, 7));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void removeSuppressedSummaryIfNecessary(final String str, final BubblesManager$8$$ExternalSyntheticLambda0 bubblesManager$8$$ExternalSyntheticLambda0, final Executor executor) {
            BubbleController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    Consumer consumer;
                    BubbleController.BubblesImpl bubblesImpl = BubbleController.BubblesImpl.this;
                    final Consumer consumer2 = bubblesManager$8$$ExternalSyntheticLambda0;
                    final Executor executor2 = executor;
                    String str2 = str;
                    if (consumer2 != null) {
                        Objects.requireNonNull(bubblesImpl);
                        consumer = new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda9
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                executor2.execute(new Bubble$$ExternalSyntheticLambda1(consumer2, (String) obj, 2));
                            }
                        };
                    } else {
                        consumer = null;
                    }
                    BubbleController bubbleController = BubbleController.this;
                    Objects.requireNonNull(bubbleController);
                    if (bubbleController.mBubbleData.isSummarySuppressed(str2)) {
                        BubbleData bubbleData = bubbleController.mBubbleData;
                        Objects.requireNonNull(bubbleData);
                        bubbleData.mSuppressedGroupKeys.remove(str2);
                        BubbleData.Update update = bubbleData.mStateChange;
                        update.suppressedSummaryChanged = true;
                        update.suppressedSummaryGroup = str2;
                        bubbleData.dispatchPendingChanges();
                        if (consumer != null) {
                            BubbleData bubbleData2 = bubbleController.mBubbleData;
                            Objects.requireNonNull(bubbleData2);
                            consumer.accept(bubbleData2.mSuppressedGroupKeys.get(str2));
                        }
                    }
                }
            });
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void setExpandListener(StatusBar$$ExternalSyntheticLambda2 statusBar$$ExternalSyntheticLambda2) {
            BubbleController.this.mMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda2(this, statusBar$$ExternalSyntheticLambda2, 2));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void setSysuiProxy(BubblesManager.AnonymousClass5 r4) {
            BubbleController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda0(this, r4, 2));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public final void updateForThemeChanges() {
            BubbleController.this.mMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda0(this, 4));
        }
    }

    @VisibleForTesting
    public BubbleController(Context context, BubbleData bubbleData, BubbleStackView.SurfaceSynchronizer surfaceSynchronizer, FloatingContentCoordinator floatingContentCoordinator, BubbleDataRepository bubbleDataRepository, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, LauncherApps launcherApps, BubbleLogger bubbleLogger, TaskStackListenerImpl taskStackListenerImpl, ShellTaskOrganizer shellTaskOrganizer, BubblePositioner bubblePositioner, DisplayController displayController, Optional<OneHandedController> optional, ShellExecutor shellExecutor, Handler handler, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        IStatusBarService iStatusBarService2;
        this.mContext = context;
        this.mLauncherApps = launcherApps;
        if (iStatusBarService == null) {
            iStatusBarService2 = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        } else {
            iStatusBarService2 = iStatusBarService;
        }
        this.mBarService = iStatusBarService2;
        this.mWindowManager = windowManager;
        this.mWindowManagerShellWrapper = windowManagerShellWrapper;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        this.mDataRepository = bubbleDataRepository;
        this.mLogger = bubbleLogger;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mTaskStackListener = taskStackListenerImpl;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSurfaceSynchronizer = surfaceSynchronizer;
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mBubblePositioner = bubblePositioner;
        this.mBubbleData = bubbleData;
        this.mSavedBubbleKeysPerUser = new SparseSetArray<>();
        this.mBubbleIconFactory = new BubbleIconFactory(context);
        this.mBubbleBadgeIconFactory = new BubbleBadgeIconFactory(context);
        this.mDisplayController = displayController;
        this.mTaskViewTransitions = taskViewTransitions;
        this.mOneHandedOptional = optional;
        this.mSyncQueue = syncTransactionQueue;
    }

    @VisibleForTesting
    public void updateBubble(BubbleEntry bubbleEntry) {
        updateBubble(bubbleEntry, false, true);
    }

    public static PackageManager getPackageManagerForUser(Context context, int i) {
        if (i >= 0) {
            try {
                context = context.createPackageContextAsUser(context.getPackageName(), 4, new UserHandle(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return context.getPackageManager();
    }

    @VisibleForTesting
    public void collapseStack() {
        this.mBubbleData.setExpanded(false);
    }

    public final void ensureStackViewCreated() {
        if (this.mStackView == null) {
            BubbleStackView bubbleStackView = new BubbleStackView(this.mContext, this, this.mBubbleData, this.mSurfaceSynchronizer, this.mFloatingContentCoordinator, this.mMainExecutor);
            this.mStackView = bubbleStackView;
            bubbleStackView.mRelativeStackPositionBeforeRotation = new BubbleStackView.RelativeStackPosition(bubbleStackView.mPositioner.getRestingPosition(), bubbleStackView.mStackAnimationController.getAllowableStackPositionRegion());
            bubbleStackView.addOnLayoutChangeListener(bubbleStackView.mOrientationChangedListener);
            bubbleStackView.hideFlyoutImmediate();
            BubbleController$$ExternalSyntheticLambda4 bubbleController$$ExternalSyntheticLambda4 = this.mExpandListener;
            if (bubbleController$$ExternalSyntheticLambda4 != null) {
                BubbleStackView bubbleStackView2 = this.mStackView;
                Objects.requireNonNull(bubbleStackView2);
                bubbleStackView2.mExpandListener = bubbleController$$ExternalSyntheticLambda4;
            }
            BubbleStackView bubbleStackView3 = this.mStackView;
            Bubbles.SysuiProxy sysuiProxy = this.mSysuiProxy;
            Objects.requireNonNull(sysuiProxy);
            BubbleController$$ExternalSyntheticLambda10 bubbleController$$ExternalSyntheticLambda10 = new BubbleController$$ExternalSyntheticLambda10(sysuiProxy, 0);
            Objects.requireNonNull(bubbleStackView3);
            bubbleStackView3.mUnbubbleConversationCallback = bubbleController$$ExternalSyntheticLambda10;
        }
        if (this.mStackView != null && !this.mAddedToWindowManager) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 16777256, -3);
            this.mWmLayoutParams = layoutParams;
            layoutParams.setTrustedOverlay();
            this.mWmLayoutParams.setFitInsetsTypes(0);
            WindowManager.LayoutParams layoutParams2 = this.mWmLayoutParams;
            layoutParams2.softInputMode = 16;
            layoutParams2.token = new Binder();
            this.mWmLayoutParams.setTitle("Bubbles!");
            this.mWmLayoutParams.packageName = this.mContext.getPackageName();
            WindowManager.LayoutParams layoutParams3 = this.mWmLayoutParams;
            layoutParams3.layoutInDisplayCutoutMode = 3;
            layoutParams3.privateFlags = 16 | layoutParams3.privateFlags;
            try {
                this.mAddedToWindowManager = true;
                BubbleData bubbleData = this.mBubbleData;
                Objects.requireNonNull(bubbleData);
                BubbleOverflow bubbleOverflow = bubbleData.mOverflow;
                Objects.requireNonNull(bubbleOverflow);
                View inflate = bubbleOverflow.inflater.inflate(2131624018, (ViewGroup) null, false);
                Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.wm.shell.bubbles.BubbleExpandedView");
                BubbleExpandedView bubbleExpandedView = (BubbleExpandedView) inflate;
                bubbleOverflow.expandedView = bubbleExpandedView;
                bubbleExpandedView.applyThemeAttrs();
                bubbleOverflow.updateResources();
                BubbleExpandedView bubbleExpandedView2 = bubbleOverflow.expandedView;
                if (bubbleExpandedView2 != null) {
                    bubbleExpandedView2.initialize(this, getStackView(), true);
                }
                this.mWindowManager.addView(this.mStackView, this.mWmLayoutParams);
                this.mStackView.setOnApplyWindowInsetsListener(new BubbleController$$ExternalSyntheticLambda0(this, 0));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public final void expandStackAndSelectBubble(BubbleEntry bubbleEntry) {
        if (this.mIsStatusBarShade) {
            this.mNotifEntryToExpandOnShadeUnlock = null;
            String key = bubbleEntry.getKey();
            Bubble bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(key);
            if (bubbleInStackWithKey != null) {
                this.mBubbleData.setSelectedBubble(bubbleInStackWithKey);
                this.mBubbleData.setExpanded(true);
                return;
            }
            Bubble overflowBubbleWithKey = this.mBubbleData.getOverflowBubbleWithKey(key);
            if (overflowBubbleWithKey != null) {
                promoteBubbleFromOverflow(overflowBubbleWithKey);
            } else if (bubbleEntry.mRanking.canBubble()) {
                bubbleEntry.setFlagBubble(true);
                try {
                    this.mBarService.onNotificationBubbleChanged(bubbleEntry.getKey(), true, 3);
                } catch (RemoteException unused) {
                }
            }
        } else {
            this.mNotifEntryToExpandOnShadeUnlock = bubbleEntry;
        }
    }

    public final ArrayList<Bubble> getBubblesInGroup(String str) {
        ArrayList<Bubble> arrayList = new ArrayList<>();
        if (str == null) {
            return arrayList;
        }
        BubbleData bubbleData = this.mBubbleData;
        Objects.requireNonNull(bubbleData);
        for (Bubble bubble : Collections.unmodifiableList(bubbleData.mBubbles)) {
            Objects.requireNonNull(bubble);
            String str2 = bubble.mGroupKey;
            if (str2 != null && str.equals(str2)) {
                arrayList.add(bubble);
            }
        }
        return arrayList;
    }

    @VisibleForTesting
    public BubblesImpl.CachedState getImplCachedState() {
        return this.mImpl.mCachedState;
    }

    @VisibleForTesting
    public boolean hasBubbles() {
        if (this.mStackView == null) {
            return false;
        }
        BubbleData bubbleData = this.mBubbleData;
        Objects.requireNonNull(bubbleData);
        if ((!bubbleData.mBubbles.isEmpty()) || this.mBubbleData.isShowingOverflow()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean isBubbleNotificationSuppressedFromShade(String str, String str2) {
        boolean z;
        if (!this.mBubbleData.hasAnyBubbleWithKey(str) || this.mBubbleData.getAnyBubbleWithkey(str).showInShade()) {
            z = false;
        } else {
            z = true;
        }
        boolean isSummarySuppressed = this.mBubbleData.isSummarySuppressed(str2);
        BubbleData bubbleData = this.mBubbleData;
        Objects.requireNonNull(bubbleData);
        if ((!str.equals(bubbleData.mSuppressedGroupKeys.get(str2)) || !isSummarySuppressed) && !z) {
            return false;
        }
        return true;
    }

    @VisibleForTesting
    public boolean isStackExpanded() {
        BubbleData bubbleData = this.mBubbleData;
        Objects.requireNonNull(bubbleData);
        return bubbleData.mExpanded;
    }

    @VisibleForTesting
    public void onBubbleNotificationSuppressionChanged(Bubble bubble) {
        boolean z;
        try {
            IStatusBarService iStatusBarService = this.mBarService;
            Objects.requireNonNull(bubble);
            String str = bubble.mKey;
            boolean z2 = true;
            if (!bubble.showInShade()) {
                z = true;
            } else {
                z = false;
            }
            if ((bubble.mFlags & 8) == 0) {
                z2 = false;
            }
            iStatusBarService.onBubbleNotificationSuppressionChanged(str, z, z2);
        } catch (RemoteException unused) {
        }
        BubblesImpl.CachedState cachedState = this.mImpl.mCachedState;
        Objects.requireNonNull(cachedState);
        synchronized (cachedState) {
            if (!bubble.showInShade()) {
                cachedState.mSuppressedBubbleKeys.add(bubble.mKey);
            } else {
                cachedState.mSuppressedBubbleKeys.remove(bubble.mKey);
            }
        }
    }

    public final void onEntryUpdated(BubbleEntry bubbleEntry, boolean z) {
        boolean z2;
        if (!z || !canLaunchInTaskView(this.mContext, bubbleEntry)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (!z2 && this.mBubbleData.hasAnyBubbleWithKey(bubbleEntry.getKey())) {
            removeBubble(bubbleEntry.getKey(), 7);
        } else if (z2 && bubbleEntry.isBubble()) {
            updateBubble(bubbleEntry);
        }
    }

    @VisibleForTesting
    public void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        String str2;
        ArrayList arrayList = new ArrayList(this.mBubbleData.getOverflowBubbles());
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            Bubble bubble = (Bubble) arrayList.get(i2);
            Objects.requireNonNull(bubble);
            ShortcutInfo shortcutInfo = bubble.mShortcutInfo;
            if (shortcutInfo != null) {
                str2 = shortcutInfo.getId();
            } else {
                str2 = bubble.mMetadataShortcutId;
            }
            if (Objects.equals(str2, notificationChannel.getConversationId()) && bubble.mPackageName.equals(str) && bubble.mUser.getIdentifier() == userHandle.getIdentifier() && (!notificationChannel.canBubble() || notificationChannel.isDeleted())) {
                this.mBubbleData.dismissBubbleWithKey(bubble.mKey, 7);
            }
        }
    }

    @VisibleForTesting
    public void onUserChanged(int i) {
        int i2 = this.mCurrentUserId;
        this.mSavedBubbleKeysPerUser.remove(i2);
        for (Bubble bubble : this.mBubbleData.getBubbles()) {
            SparseSetArray<String> sparseSetArray = this.mSavedBubbleKeysPerUser;
            Objects.requireNonNull(bubble);
            sparseSetArray.add(i2, bubble.mKey);
        }
        this.mCurrentUserId = i;
        this.mBubbleData.dismissAll(8);
        BubbleData bubbleData = this.mBubbleData;
        Objects.requireNonNull(bubbleData);
        while (!bubbleData.mOverflowBubbles.isEmpty()) {
            Bubble bubble2 = (Bubble) bubbleData.mOverflowBubbles.get(0);
            Objects.requireNonNull(bubble2);
            bubbleData.doRemove(bubble2.mKey, 8);
        }
        bubbleData.dispatchPendingChanges();
        this.mOverflowDataLoadNeeded = true;
        final ArraySet arraySet = this.mSavedBubbleKeysPerUser.get(i);
        if (arraySet != null) {
            Bubbles.SysuiProxy sysuiProxy = this.mSysuiProxy;
            final BubbleController$$ExternalSyntheticLambda9 bubbleController$$ExternalSyntheticLambda9 = new BubbleController$$ExternalSyntheticLambda9(this, 0);
            final BubblesManager.AnonymousClass5 r1 = (BubblesManager.AnonymousClass5) sysuiProxy;
            Objects.requireNonNull(r1);
            r1.val$sysuiMainExecutor.execute(new Runnable() { // from class: com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    BubblesManager.AnonymousClass5 r0 = BubblesManager.AnonymousClass5.this;
                    ArraySet arraySet2 = arraySet;
                    Consumer consumer = bubbleController$$ExternalSyntheticLambda9;
                    Objects.requireNonNull(r0);
                    ArrayList arrayList = new ArrayList();
                    for (NotificationEntry notificationEntry : BubblesManager.this.mCommonNotifCollection.getAllNotifs()) {
                        NotificationLockscreenUserManager notificationLockscreenUserManager = BubblesManager.this.mNotifUserManager;
                        Objects.requireNonNull(notificationEntry);
                        if (notificationLockscreenUserManager.isCurrentProfile(notificationEntry.mSbn.getUserId()) && arraySet2.contains(notificationEntry.mKey) && BubblesManager.this.mNotificationInterruptStateProvider.shouldBubbleUp(notificationEntry) && notificationEntry.isBubble()) {
                            arrayList.add(BubblesManager.notifToBubbleEntry(notificationEntry));
                        }
                    }
                    consumer.accept(arrayList);
                }
            });
            this.mSavedBubbleKeysPerUser.remove(i);
        }
        BubbleData bubbleData2 = this.mBubbleData;
        Objects.requireNonNull(bubbleData2);
        bubbleData2.mCurrentUserId = i;
    }

    public final void promoteBubbleFromOverflow(Bubble bubble) {
        this.mLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_BACK_TO_STACK);
        bubble.setInflateSynchronously(this.mInflateSynchronously);
        bubble.mFlags |= 1;
        bubble.mLastAccessed = System.currentTimeMillis();
        bubble.setSuppressNotification(true);
        bubble.setShowDot(false);
        bubble.mIsBubble = true;
        Bubbles.SysuiProxy sysuiProxy = this.mSysuiProxy;
        String str = bubble.mKey;
        BubbleController$$ExternalSyntheticLambda11 bubbleController$$ExternalSyntheticLambda11 = new BubbleController$$ExternalSyntheticLambda11(this, true, bubble);
        BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) sysuiProxy;
        Objects.requireNonNull(r0);
        r0.val$sysuiMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda7(r0, str, bubbleController$$ExternalSyntheticLambda11));
    }

    @VisibleForTesting
    public void removeBubble(String str, int i) {
        if (this.mBubbleData.hasAnyBubbleWithKey(str)) {
            this.mBubbleData.dismissBubbleWithKey(str, i);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.wm.shell.bubbles.Bubbles$BubbleExpandListener, com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda4] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setExpandListener(final com.android.wm.shell.bubbles.Bubbles.BubbleExpandListener r2) {
        /*
            r1 = this;
            com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda4 r0 = new com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda4
            r0.<init>()
            r1.mExpandListener = r0
            com.android.wm.shell.bubbles.BubbleStackView r1 = r1.mStackView
            if (r1 == 0) goto L_0x000d
            r1.mExpandListener = r0
        L_0x000d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController.setExpandListener(com.android.wm.shell.bubbles.Bubbles$BubbleExpandListener):void");
    }

    @VisibleForTesting
    public void updateBubble(BubbleEntry bubbleEntry, boolean z, boolean z2) {
        Bubbles.SysuiProxy sysuiProxy = this.mSysuiProxy;
        String key = bubbleEntry.getKey();
        BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) sysuiProxy;
        Objects.requireNonNull(r0);
        r0.val$sysuiMainExecutor.execute(new Monitor$$ExternalSyntheticLambda0(r0, key, 3));
        if (bubbleEntry.mRanking.isTextChanged() || bubbleEntry.getBubbleMetadata() == null || bubbleEntry.getBubbleMetadata().getAutoExpandBubble() || !this.mBubbleData.hasOverflowBubbleWithKey(bubbleEntry.getKey())) {
            BubbleData bubbleData = this.mBubbleData;
            LocusId locusId = bubbleEntry.mSbn.getNotification().getLocusId();
            Objects.requireNonNull(bubbleData);
            if (bubbleData.mSuppressedBubbles.get(locusId) != null) {
                Bubble suppressedBubbleWithKey = this.mBubbleData.getSuppressedBubbleWithKey(bubbleEntry.getKey());
                if (suppressedBubbleWithKey != null) {
                    suppressedBubbleWithKey.setEntry(bubbleEntry);
                    return;
                }
                return;
            }
            Bubble orCreateBubble = this.mBubbleData.getOrCreateBubble(bubbleEntry, null);
            ensureStackViewCreated();
            orCreateBubble.setInflateSynchronously(this.mInflateSynchronously);
            orCreateBubble.inflate(new BubbleController$$ExternalSyntheticLambda3(this, z, z2), this.mContext, this, this.mStackView, this.mBubbleIconFactory, this.mBubbleBadgeIconFactory, false);
            return;
        }
        this.mBubbleData.getOverflowBubbleWithKey(bubbleEntry.getKey()).setEntry(bubbleEntry);
    }

    public final void updateStack() {
        final BadgedImageView badgedImageView;
        Bubble bubble;
        int i;
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            int i2 = 0;
            if (!this.mIsStatusBarShade) {
                bubbleStackView.setVisibility(4);
            } else if (hasBubbles()) {
                this.mStackView.setVisibility(0);
            }
            BubbleStackView bubbleStackView2 = this.mStackView;
            Objects.requireNonNull(bubbleStackView2);
            if (!bubbleStackView2.mBubbleData.getBubbles().isEmpty()) {
                for (int i3 = 0; i3 < bubbleStackView2.mBubbleData.getBubbles().size(); i3++) {
                    Bubble bubble2 = bubbleStackView2.mBubbleData.getBubbles().get(i3);
                    Objects.requireNonNull(bubble2);
                    String str = bubble2.mAppName;
                    String str2 = bubble2.mTitle;
                    if (str2 == null) {
                        str2 = bubbleStackView2.getResources().getString(2131952883);
                    }
                    BadgedImageView badgedImageView2 = bubble2.mIconView;
                    if (badgedImageView2 != null) {
                        if (bubbleStackView2.mIsExpanded || i3 > 0) {
                            badgedImageView2.setContentDescription(bubbleStackView2.getResources().getString(2131952062, str2, str));
                        } else {
                            bubble2.mIconView.setContentDescription(bubbleStackView2.getResources().getString(2131952063, str2, str, Integer.valueOf(bubbleStackView2.mBubbleContainer.getChildCount() - 1)));
                        }
                    }
                }
            }
            BubbleStackView bubbleStackView3 = this.mStackView;
            Objects.requireNonNull(bubbleStackView3);
            while (true) {
                badgedImageView = null;
                if (i2 >= bubbleStackView3.mBubbleData.getBubbles().size()) {
                    break;
                }
                if (i2 > 0) {
                    bubble = bubbleStackView3.mBubbleData.getBubbles().get(i2 - 1);
                } else {
                    bubble = null;
                }
                Bubble bubble3 = bubbleStackView3.mBubbleData.getBubbles().get(i2);
                Objects.requireNonNull(bubble3);
                BadgedImageView badgedImageView3 = bubble3.mIconView;
                if (badgedImageView3 != null) {
                    if (bubbleStackView3.mIsExpanded) {
                        badgedImageView3.setImportantForAccessibility(1);
                        if (bubble != null) {
                            badgedImageView = bubble.mIconView;
                        }
                        if (badgedImageView != null) {
                            badgedImageView3.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.wm.shell.bubbles.BubbleStackView.14
                                @Override // android.view.View.AccessibilityDelegate
                                public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                                    accessibilityNodeInfo.setTraversalAfter(badgedImageView);
                                }
                            });
                        }
                    } else {
                        if (i2 == 0) {
                            i = 1;
                        } else {
                            i = 2;
                        }
                        badgedImageView3.setImportantForAccessibility(i);
                    }
                }
                i2++;
            }
            if (bubbleStackView3.mIsExpanded) {
                BubbleOverflow bubbleOverflow = bubbleStackView3.mBubbleOverflow;
                if (bubbleOverflow != null) {
                    badgedImageView = bubbleOverflow.getIconView$1();
                }
                if (!(badgedImageView == null || bubbleStackView3.mBubbleData.getBubbles().isEmpty())) {
                    Bubble bubble4 = bubbleStackView3.mBubbleData.getBubbles().get(bubbleStackView3.mBubbleData.getBubbles().size() - 1);
                    Objects.requireNonNull(bubble4);
                    final BadgedImageView badgedImageView4 = bubble4.mIconView;
                    if (badgedImageView4 != null) {
                        badgedImageView.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.wm.shell.bubbles.BubbleStackView.15
                            @Override // android.view.View.AccessibilityDelegate
                            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                                accessibilityNodeInfo.setTraversalAfter(badgedImageView4);
                            }
                        });
                    }
                }
            }
        }
    }

    public final void updateWindowFlagsForBackpress(boolean z) {
        int i;
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null && this.mAddedToWindowManager) {
            WindowManager.LayoutParams layoutParams = this.mWmLayoutParams;
            if (z) {
                i = 0;
            } else {
                i = 40;
            }
            layoutParams.flags = i | 16777216;
            this.mWindowManager.updateViewLayout(bubbleStackView, layoutParams);
        }
    }

    public static boolean canLaunchInTaskView(Context context, BubbleEntry bubbleEntry) {
        PendingIntent pendingIntent;
        if (bubbleEntry.getBubbleMetadata() != null) {
            pendingIntent = bubbleEntry.getBubbleMetadata().getIntent();
        } else {
            pendingIntent = null;
        }
        if (bubbleEntry.getBubbleMetadata() != null && bubbleEntry.getBubbleMetadata().getShortcutId() != null) {
            return true;
        }
        if (pendingIntent == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to create bubble -- no intent: ");
            m.append(bubbleEntry.getKey());
            Log.w("Bubbles", m.toString());
            return false;
        }
        ActivityInfo resolveActivityInfo = pendingIntent.getIntent().resolveActivityInfo(getPackageManagerForUser(context, bubbleEntry.mSbn.getUser().getIdentifier()), 0);
        if (resolveActivityInfo == null) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to send as bubble, ");
            m2.append(bubbleEntry.getKey());
            m2.append(" couldn't find activity info for intent: ");
            m2.append(pendingIntent);
            Log.w("Bubbles", m2.toString());
            return false;
        } else if (ActivityInfo.isResizeableMode(resolveActivityInfo.resizeMode)) {
            return true;
        } else {
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to send as bubble, ");
            m3.append(bubbleEntry.getKey());
            m3.append(" activity is not resizable for intent: ");
            m3.append(pendingIntent);
            Log.w("Bubbles", m3.toString());
            return false;
        }
    }

    public final boolean isSummaryOfBubbles(BubbleEntry bubbleEntry) {
        boolean z;
        Objects.requireNonNull(bubbleEntry);
        String groupKey = bubbleEntry.mSbn.getGroupKey();
        ArrayList<Bubble> bubblesInGroup = getBubblesInGroup(groupKey);
        if (this.mBubbleData.isSummarySuppressed(groupKey)) {
            BubbleData bubbleData = this.mBubbleData;
            Objects.requireNonNull(bubbleData);
            if (bubbleData.mSuppressedGroupKeys.get(groupKey).equals(bubbleEntry.getKey())) {
                z = true;
                boolean isGroupSummary = bubbleEntry.mSbn.getNotification().isGroupSummary();
                if ((!z || isGroupSummary) && !bubblesInGroup.isEmpty()) {
                    return true;
                }
                return false;
            }
        }
        z = false;
        boolean isGroupSummary2 = bubbleEntry.mSbn.getNotification().isGroupSummary();
        if (!z) {
        }
        return true;
    }

    @VisibleForTesting
    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }

    @VisibleForTesting
    public Bubbles asBubbles() {
        return this.mImpl;
    }

    @VisibleForTesting
    public BubblePositioner getPositioner() {
        return this.mBubblePositioner;
    }

    @VisibleForTesting
    public BubbleStackView getStackView() {
        return this.mStackView;
    }
}
