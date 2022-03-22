package com.android.wm.shell.common;

import android.app.people.ConversationChannel;
import android.content.pm.ShortcutInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BadgedImageView;
import com.android.wm.shell.bubbles.Bubble;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda6;
import com.android.wm.shell.bubbles.BubbleFlyoutView;
import com.android.wm.shell.bubbles.BubblePositioner;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import com.android.wm.shell.bubbles.animation.StackAnimationController;
import com.google.android.systemui.dreamliner.DockObserver;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ExecutorUtils$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ExecutorUtils$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        List<Rect> list;
        switch (this.$r8$classId) {
            case 0:
                ((Consumer) this.f$0).accept((RemoteCallable) this.f$1);
                return;
            case 1:
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = (AccessibilityFloatingMenuView) this.f$0;
                Rect rect = (Rect) this.f$1;
                int i = AccessibilityFloatingMenuView.$r8$clinit;
                Objects.requireNonNull(accessibilityFloatingMenuView);
                if (accessibilityFloatingMenuView.mIsShowing) {
                    list = Collections.singletonList(rect);
                } else {
                    list = Collections.emptyList();
                }
                accessibilityFloatingMenuView.setSystemGestureExclusionRects(list);
                return;
            case 2:
                PeopleSpaceWidgetManager.TileConversationListener tileConversationListener = (PeopleSpaceWidgetManager.TileConversationListener) this.f$0;
                ConversationChannel conversationChannel = (ConversationChannel) this.f$1;
                Objects.requireNonNull(tileConversationListener);
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = PeopleSpaceWidgetManager.this;
                Objects.requireNonNull(peopleSpaceWidgetManager);
                ShortcutInfo shortcutInfo = conversationChannel.getShortcutInfo();
                synchronized (peopleSpaceWidgetManager.mLock) {
                    Iterator it = peopleSpaceWidgetManager.getMatchingKeyWidgetIds(new PeopleTileKey(shortcutInfo.getId(), shortcutInfo.getUserId(), shortcutInfo.getPackage())).iterator();
                    while (it.hasNext()) {
                        peopleSpaceWidgetManager.updateStorageAndViewWithConversationData(conversationChannel, Integer.parseInt((String) it.next()));
                    }
                }
                return;
            case 3:
                StackStateAnimator.$r8$lambda$61WUm2lxj80T6Ev5pK6kC2FMdCY((StackStateAnimator) this.f$0, (String) this.f$1);
                return;
            case 4:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Bubble bubble = (Bubble) this.f$1;
                int i2 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                if (!bubbleStackView.mIsExpanded) {
                    Objects.requireNonNull(bubble);
                    if (bubble.mIconView != null) {
                        WifiEntry$$ExternalSyntheticLambda0 wifiEntry$$ExternalSyntheticLambda0 = new WifiEntry$$ExternalSyntheticLambda0(bubbleStackView, 5);
                        if (bubbleStackView.mFlyout.getVisibility() == 0) {
                            final BubbleFlyoutView bubbleFlyoutView = bubbleStackView.mFlyout;
                            final Bubble.FlyoutMessage flyoutMessage = bubble.mFlyoutMessage;
                            StackAnimationController stackAnimationController = bubbleStackView.mStackAnimationController;
                            Objects.requireNonNull(stackAnimationController);
                            final PointF pointF = stackAnimationController.mStackPosition;
                            final boolean z = !bubble.showDot();
                            float[] dotCenter = bubble.mIconView.getDotCenter();
                            ExecutorUtils$$ExternalSyntheticLambda0 executorUtils$$ExternalSyntheticLambda0 = bubbleStackView.mAfterFlyoutHidden;
                            Objects.requireNonNull(bubbleFlyoutView);
                            bubbleFlyoutView.mOnHide = executorUtils$$ExternalSyntheticLambda0;
                            bubbleFlyoutView.mDotCenter = dotCenter;
                            bubbleFlyoutView.fade(false, pointF, z, new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleFlyoutView$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    final BubbleFlyoutView bubbleFlyoutView2 = BubbleFlyoutView.this;
                                    Bubble.FlyoutMessage flyoutMessage2 = flyoutMessage;
                                    final PointF pointF2 = pointF;
                                    final boolean z2 = z;
                                    Objects.requireNonNull(bubbleFlyoutView2);
                                    bubbleFlyoutView2.updateFlyoutMessage(flyoutMessage2);
                                    bubbleFlyoutView2.post(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleFlyoutView$$ExternalSyntheticLambda0
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            BubbleFlyoutView bubbleFlyoutView3 = BubbleFlyoutView.this;
                                            PointF pointF3 = pointF2;
                                            boolean z3 = z2;
                                            Objects.requireNonNull(bubbleFlyoutView3);
                                            bubbleFlyoutView3.fade(true, pointF3, z3, BubbleFlyoutView$$ExternalSyntheticLambda2.INSTANCE);
                                        }
                                    });
                                }
                            });
                        } else {
                            bubbleStackView.mFlyout.setVisibility(4);
                            BubbleFlyoutView bubbleFlyoutView2 = bubbleStackView.mFlyout;
                            Bubble.FlyoutMessage flyoutMessage2 = bubble.mFlyoutMessage;
                            StackAnimationController stackAnimationController2 = bubbleStackView.mStackAnimationController;
                            Objects.requireNonNull(stackAnimationController2);
                            PointF pointF2 = stackAnimationController2.mStackPosition;
                            boolean isStackOnLeftSide = bubbleStackView.mStackAnimationController.isStackOnLeftSide();
                            BadgedImageView badgedImageView = bubble.mIconView;
                            Objects.requireNonNull(badgedImageView);
                            int i3 = badgedImageView.mDotColor;
                            ExecutorUtils$$ExternalSyntheticLambda0 executorUtils$$ExternalSyntheticLambda02 = bubbleStackView.mAfterFlyoutHidden;
                            float[] dotCenter2 = bubble.mIconView.getDotCenter();
                            Objects.requireNonNull(bubbleFlyoutView2);
                            BubblePositioner bubblePositioner = bubbleFlyoutView2.mPositioner;
                            Objects.requireNonNull(bubblePositioner);
                            int i4 = bubblePositioner.mBubbleSize;
                            bubbleFlyoutView2.mBubbleSize = i4;
                            float f = i4 * 0.228f;
                            bubbleFlyoutView2.mOriginalDotSize = f;
                            float f2 = (f * 1.0f) / 2.0f;
                            bubbleFlyoutView2.mNewDotRadius = f2;
                            bubbleFlyoutView2.mNewDotSize = f2 * 2.0f;
                            bubbleFlyoutView2.updateFlyoutMessage(flyoutMessage2);
                            bubbleFlyoutView2.mArrowPointingLeft = isStackOnLeftSide;
                            bubbleFlyoutView2.mDotColor = i3;
                            bubbleFlyoutView2.mOnHide = executorUtils$$ExternalSyntheticLambda02;
                            bubbleFlyoutView2.mDotCenter = dotCenter2;
                            bubbleFlyoutView2.setCollapsePercent(1.0f);
                            bubbleFlyoutView2.post(new BubbleController$$ExternalSyntheticLambda6(bubbleFlyoutView2, pointF2, !bubble.showDot(), wifiEntry$$ExternalSyntheticLambda0, 1));
                        }
                        bubbleStackView.mFlyout.bringToFront();
                        return;
                    }
                    return;
                }
                return;
            case 5:
                PhysicsAnimationLayout physicsAnimationLayout = (PhysicsAnimationLayout) this.f$0;
                View view = (View) this.f$1;
                int i5 = PhysicsAnimationLayout.$r8$clinit;
                Objects.requireNonNull(physicsAnimationLayout);
                physicsAnimationLayout.cancelAnimationsOnView(view);
                physicsAnimationLayout.removeTransientView(view);
                return;
            default:
                DockObserver dockObserver = (DockObserver) this.f$0;
                Runnable runnable = (Runnable) this.f$1;
                String str = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                if (dockObserver.mWirelessCharger == null) {
                    Log.i("DLObserver", "hint is UNKNOWN for null wireless charger HAL");
                    dockObserver.mFanLevel = -1;
                } else {
                    long currentTimeMillis = System.currentTimeMillis();
                    dockObserver.mFanLevel = dockObserver.mWirelessCharger.getFanLevel();
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("command=2, l=");
                    m.append(dockObserver.mFanLevel);
                    m.append(", spending time=");
                    m.append(System.currentTimeMillis() - currentTimeMillis);
                    Log.d("DLObserver", m.toString());
                }
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
        }
    }
}
