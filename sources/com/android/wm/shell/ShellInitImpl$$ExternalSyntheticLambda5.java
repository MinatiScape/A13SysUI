package com.android.wm.shell;

import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.RemoteException;
import android.os.UserHandle;
import android.window.WindowContainerTransaction;
import com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda8;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda1;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellInitImpl$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda5 INSTANCE$1 = new ShellInitImpl$$ExternalSyntheticLambda5(1);
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda5 INSTANCE = new ShellInitImpl$$ExternalSyntheticLambda5(0);
    public static final /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda5 INSTANCE$2 = new ShellInitImpl$$ExternalSyntheticLambda5(2);

    public /* synthetic */ ShellInitImpl$$ExternalSyntheticLambda5(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                final BubbleController bubbleController = (BubbleController) obj;
                Objects.requireNonNull(bubbleController);
                BubbleData bubbleData = bubbleController.mBubbleData;
                BubbleController.AnonymousClass5 r0 = bubbleController.mBubbleDataListener;
                Objects.requireNonNull(bubbleData);
                bubbleData.mListener = r0;
                BubbleData bubbleData2 = bubbleController.mBubbleData;
                PipController$$ExternalSyntheticLambda1 pipController$$ExternalSyntheticLambda1 = new PipController$$ExternalSyntheticLambda1(bubbleController);
                Objects.requireNonNull(bubbleData2);
                bubbleData2.mSuppressionListener = pipController$$ExternalSyntheticLambda1;
                BubbleData bubbleData3 = bubbleController.mBubbleData;
                ImageLoader$$ExternalSyntheticLambda0 imageLoader$$ExternalSyntheticLambda0 = new ImageLoader$$ExternalSyntheticLambda0(bubbleController);
                Objects.requireNonNull(bubbleData3);
                bubbleData3.mCancelledListener = imageLoader$$ExternalSyntheticLambda0;
                try {
                    bubbleController.mWindowManagerShellWrapper.addPinnedStackListener(new BubbleController.BubblesImeListener());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                BubbleData bubbleData4 = bubbleController.mBubbleData;
                int i = bubbleController.mCurrentUserId;
                Objects.requireNonNull(bubbleData4);
                bubbleData4.mCurrentUserId = i;
                ShellTaskOrganizer shellTaskOrganizer = bubbleController.mTaskOrganizer;
                ShellTaskOrganizer.LocusIdListener bubbleController$$ExternalSyntheticLambda1 = new ShellTaskOrganizer.LocusIdListener() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda1
                    /* JADX WARN: Code restructure failed: missing block: B:14:0x0037, code lost:
                        if (r2.getTaskId() != r5) goto L_0x0039;
                     */
                    /* JADX WARN: Removed duplicated region for block: B:22:0x005b  */
                    /* JADX WARN: Removed duplicated region for block: B:23:0x005d  */
                    /* JADX WARN: Removed duplicated region for block: B:28:0x0068  */
                    /* JADX WARN: Removed duplicated region for block: B:34:0x007f  */
                    /* JADX WARN: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
                    @Override // com.android.wm.shell.ShellTaskOrganizer.LocusIdListener
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final void onVisibilityChanged(int r5, android.content.LocusId r6, boolean r7) {
                        /*
                            r4 = this;
                            com.android.wm.shell.bubbles.BubbleController r4 = com.android.wm.shell.bubbles.BubbleController.this
                            java.util.Objects.requireNonNull(r4)
                            com.android.wm.shell.bubbles.BubbleData r4 = r4.mBubbleData
                            r0 = 0
                            if (r6 != 0) goto L_0x000b
                            goto L_0x002b
                        L_0x000b:
                            r1 = r0
                        L_0x000c:
                            java.util.ArrayList r2 = r4.mBubbles
                            int r2 = r2.size()
                            if (r1 >= r2) goto L_0x002b
                            java.util.ArrayList r2 = r4.mBubbles
                            java.lang.Object r2 = r2.get(r1)
                            com.android.wm.shell.bubbles.Bubble r2 = (com.android.wm.shell.bubbles.Bubble) r2
                            java.util.Objects.requireNonNull(r2)
                            android.content.LocusId r3 = r2.mLocusId
                            boolean r3 = r6.equals(r3)
                            if (r3 == 0) goto L_0x0028
                            goto L_0x002c
                        L_0x0028:
                            int r1 = r1 + 1
                            goto L_0x000c
                        L_0x002b:
                            r2 = 0
                        L_0x002c:
                            if (r7 == 0) goto L_0x003f
                            if (r2 == 0) goto L_0x0039
                            java.util.Objects.requireNonNull(r4)
                            int r1 = r2.getTaskId()
                            if (r1 == r5) goto L_0x003f
                        L_0x0039:
                            android.util.ArraySet<android.content.LocusId> r1 = r4.mVisibleLocusIds
                            r1.add(r6)
                            goto L_0x0044
                        L_0x003f:
                            android.util.ArraySet<android.content.LocusId> r1 = r4.mVisibleLocusIds
                            r1.remove(r6)
                        L_0x0044:
                            if (r2 != 0) goto L_0x0052
                            android.util.ArrayMap<android.content.LocusId, com.android.wm.shell.bubbles.Bubble> r1 = r4.mSuppressedBubbles
                            java.lang.Object r1 = r1.get(r6)
                            r2 = r1
                            com.android.wm.shell.bubbles.Bubble r2 = (com.android.wm.shell.bubbles.Bubble) r2
                            if (r2 != 0) goto L_0x0052
                            goto L_0x008f
                        L_0x0052:
                            android.util.ArrayMap<android.content.LocusId, com.android.wm.shell.bubbles.Bubble> r1 = r4.mSuppressedBubbles
                            java.lang.Object r1 = r1.get(r6)
                            r3 = 1
                            if (r1 == 0) goto L_0x005d
                            r1 = r3
                            goto L_0x005e
                        L_0x005d:
                            r1 = r0
                        L_0x005e:
                            if (r7 == 0) goto L_0x007d
                            if (r1 != 0) goto L_0x007d
                            int r1 = r2.mFlags
                            r1 = r1 & 4
                            if (r1 == 0) goto L_0x0069
                            r0 = r3
                        L_0x0069:
                            if (r0 == 0) goto L_0x007d
                            int r0 = r2.getTaskId()
                            if (r5 == r0) goto L_0x007d
                            android.util.ArrayMap<android.content.LocusId, com.android.wm.shell.bubbles.Bubble> r5 = r4.mSuppressedBubbles
                            r5.put(r6, r2)
                            r4.doSuppress(r2)
                            r4.dispatchPendingChanges()
                            goto L_0x008f
                        L_0x007d:
                            if (r7 != 0) goto L_0x008f
                            android.util.ArrayMap<android.content.LocusId, com.android.wm.shell.bubbles.Bubble> r5 = r4.mSuppressedBubbles
                            java.lang.Object r5 = r5.remove(r6)
                            com.android.wm.shell.bubbles.Bubble r5 = (com.android.wm.shell.bubbles.Bubble) r5
                            if (r5 == 0) goto L_0x008c
                            r4.doUnsuppress(r5)
                        L_0x008c:
                            r4.dispatchPendingChanges()
                        L_0x008f:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda1.onVisibilityChanged(int, android.content.LocusId, boolean):void");
                    }
                };
                Objects.requireNonNull(shellTaskOrganizer);
                synchronized (shellTaskOrganizer.mLock) {
                    shellTaskOrganizer.mLocusIdListeners.add(bubbleController$$ExternalSyntheticLambda1);
                    for (int i2 = 0; i2 < shellTaskOrganizer.mVisibleTasksWithLocusId.size(); i2++) {
                        bubbleController$$ExternalSyntheticLambda1.onVisibilityChanged(shellTaskOrganizer.mVisibleTasksWithLocusId.keyAt(i2), shellTaskOrganizer.mVisibleTasksWithLocusId.valueAt(i2), true);
                    }
                }
                bubbleController.mLauncherApps.registerCallback(new LauncherApps.Callback() { // from class: com.android.wm.shell.bubbles.BubbleController.2
                    @Override // android.content.pm.LauncherApps.Callback
                    public final void onPackageAdded(String str, UserHandle userHandle) {
                    }

                    @Override // android.content.pm.LauncherApps.Callback
                    public final void onPackageChanged(String str, UserHandle userHandle) {
                    }

                    @Override // android.content.pm.LauncherApps.Callback
                    public final void onPackagesAvailable(String[] strArr, UserHandle userHandle, boolean z) {
                    }

                    @Override // android.content.pm.LauncherApps.Callback
                    public final void onPackagesUnavailable(String[] strArr, UserHandle userHandle, boolean z) {
                        for (String str : strArr) {
                            BubbleData bubbleData5 = BubbleController.this.mBubbleData;
                            Objects.requireNonNull(bubbleData5);
                            BubbleData$$ExternalSyntheticLambda7 bubbleData$$ExternalSyntheticLambda7 = new BubbleData$$ExternalSyntheticLambda7(str, 0);
                            BubbleData$$ExternalSyntheticLambda2 bubbleData$$ExternalSyntheticLambda2 = new BubbleData$$ExternalSyntheticLambda2(bubbleData5);
                            BubbleData.performActionOnBubblesMatching(bubbleData5.getBubbles(), bubbleData$$ExternalSyntheticLambda7, bubbleData$$ExternalSyntheticLambda2);
                            BubbleData.performActionOnBubblesMatching(bubbleData5.getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda7, bubbleData$$ExternalSyntheticLambda2);
                        }
                    }

                    @Override // android.content.pm.LauncherApps.Callback
                    public final void onPackageRemoved(String str, UserHandle userHandle) {
                        BubbleData bubbleData5 = BubbleController.this.mBubbleData;
                        Objects.requireNonNull(bubbleData5);
                        BubbleData$$ExternalSyntheticLambda7 bubbleData$$ExternalSyntheticLambda7 = new BubbleData$$ExternalSyntheticLambda7(str, 0);
                        BubbleData$$ExternalSyntheticLambda2 bubbleData$$ExternalSyntheticLambda2 = new BubbleData$$ExternalSyntheticLambda2(bubbleData5);
                        BubbleData.performActionOnBubblesMatching(bubbleData5.getBubbles(), bubbleData$$ExternalSyntheticLambda7, bubbleData$$ExternalSyntheticLambda2);
                        BubbleData.performActionOnBubblesMatching(bubbleData5.getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda7, bubbleData$$ExternalSyntheticLambda2);
                    }

                    @Override // android.content.pm.LauncherApps.Callback
                    public final void onShortcutsChanged(final String str, List<ShortcutInfo> list, UserHandle userHandle) {
                        super.onShortcutsChanged(str, list, userHandle);
                        final BubbleData bubbleData5 = BubbleController.this.mBubbleData;
                        Objects.requireNonNull(bubbleData5);
                        final HashSet hashSet = new HashSet();
                        for (ShortcutInfo shortcutInfo : list) {
                            hashSet.add(shortcutInfo.getId());
                        }
                        Predicate bubbleData$$ExternalSyntheticLambda8 = new Predicate() { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda8
                            @Override // java.util.function.Predicate
                            public final boolean test(Object obj2) {
                                boolean z;
                                ShortcutInfo shortcutInfo2;
                                String str2 = str;
                                Set set = hashSet;
                                Bubble bubble = (Bubble) obj2;
                                Objects.requireNonNull(bubble);
                                boolean equals = str2.equals(bubble.mPackageName);
                                boolean hasMetadataShortcutId = bubble.hasMetadataShortcutId();
                                if (!equals || !hasMetadataShortcutId) {
                                    return false;
                                }
                                if (!bubble.hasMetadataShortcutId() || (shortcutInfo2 = bubble.mShortcutInfo) == null || !shortcutInfo2.isEnabled() || !set.contains(bubble.mShortcutInfo.getId())) {
                                    z = false;
                                } else {
                                    z = true;
                                }
                                if (!equals || z) {
                                    return false;
                                }
                                return true;
                            }
                        };
                        Consumer bubbleData$$ExternalSyntheticLambda1 = new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleData$$ExternalSyntheticLambda1
                            public final /* synthetic */ int f$1 = 12;

                            @Override // java.util.function.Consumer
                            public final void accept(Object obj2) {
                                BubbleData bubbleData6 = BubbleData.this;
                                int i3 = this.f$1;
                                Bubble bubble = (Bubble) obj2;
                                Objects.requireNonNull(bubbleData6);
                                Objects.requireNonNull(bubble);
                                bubbleData6.dismissBubbleWithKey(bubble.mKey, i3);
                            }
                        };
                        BubbleData.performActionOnBubblesMatching(bubbleData5.getBubbles(), bubbleData$$ExternalSyntheticLambda8, bubbleData$$ExternalSyntheticLambda1);
                        BubbleData.performActionOnBubblesMatching(bubbleData5.getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda8, bubbleData$$ExternalSyntheticLambda1);
                    }
                }, bubbleController.mMainHandler);
                bubbleController.mTaskStackListener.addListener(new BubbleController.AnonymousClass3());
                bubbleController.mDisplayController.addDisplayChangingController(new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.bubbles.BubbleController.4
                    @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
                    public final void onRotateDisplay(int i3, int i4, int i5, WindowContainerTransaction windowContainerTransaction) {
                        BubbleStackView bubbleStackView;
                        if (i4 != i5 && (bubbleStackView = BubbleController.this.mStackView) != null) {
                            bubbleStackView.mRelativeStackPositionBeforeRotation = new BubbleStackView.RelativeStackPosition(bubbleStackView.mPositioner.getRestingPosition(), bubbleStackView.mStackAnimationController.getAllowableStackPositionRegion());
                            bubbleStackView.addOnLayoutChangeListener(bubbleStackView.mOrientationChangedListener);
                            bubbleStackView.hideFlyoutImmediate();
                        }
                    }
                });
                bubbleController.mOneHandedOptional.ifPresent(new BubbleController$$ExternalSyntheticLambda8(bubbleController, 0));
                return;
            case 1:
                int i3 = ZenModeControllerImpl.$r8$clinit;
                Objects.requireNonNull((ZenModeController.Callback) obj);
                return;
            default:
                StartingWindowController startingWindowController = (StartingWindowController) obj;
                int i4 = StartingWindowController.IStartingWindowImpl.$r8$clinit;
                Objects.requireNonNull(startingWindowController);
                startingWindowController.mTaskLaunchingCallback = null;
                return;
        }
    }
}
