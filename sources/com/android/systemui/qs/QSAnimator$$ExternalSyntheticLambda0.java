package com.android.systemui.qs;

import android.os.RemoteException;
import android.util.Log;
import android.view.animation.PathInterpolator;
import com.android.keyguard.KeyguardHostViewController;
import com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda1;
import com.google.android.systemui.assist.uihints.GlowView;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSAnimator$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSAnimator$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                QSAnimator qSAnimator = (QSAnimator) this.f$0;
                Objects.requireNonNull(qSAnimator);
                qSAnimator.updateAnimators();
                qSAnimator.setPosition(qSAnimator.mLastPosition);
                return;
            case 1:
                NavigationBarEdgePanel navigationBarEdgePanel = (NavigationBarEdgePanel) this.f$0;
                PathInterpolator pathInterpolator = NavigationBarEdgePanel.RUBBER_BAND_INTERPOLATOR;
                Objects.requireNonNull(navigationBarEdgePanel);
                navigationBarEdgePanel.setVisibility(8);
                return;
            case 2:
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$0;
                Objects.requireNonNull(expandableNotificationRow);
                expandableNotificationRow.mJustClicked = false;
                return;
            case 3:
                NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) this.f$0;
                boolean z = NotificationStackScrollLayout.SPEW;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.mShadeController.animateCollapsePanels(0);
                return;
            case 4:
                KeyguardBouncer keyguardBouncer = (KeyguardBouncer) this.f$0;
                Objects.requireNonNull(keyguardBouncer);
                KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
                if (keyguardHostViewController != null) {
                    keyguardHostViewController.resetSecurityContainer();
                    Iterator<KeyguardBouncer.KeyguardResetCallback> it = keyguardBouncer.mResetCallbacks.iterator();
                    while (it.hasNext()) {
                        it.next().onKeyguardReset();
                    }
                    return;
                }
                return;
            case 5:
                NotificationShadeWindowControllerImpl notificationShadeWindowControllerImpl = (NotificationShadeWindowControllerImpl) this.f$0;
                Objects.requireNonNull(notificationShadeWindowControllerImpl);
                try {
                    notificationShadeWindowControllerImpl.mActivityManager.setHasTopUi(notificationShadeWindowControllerImpl.mHasTopUiChanged);
                } catch (RemoteException e) {
                    Log.e("NotificationShadeWindowController", "Failed to call setHasTopUi", e);
                }
                notificationShadeWindowControllerImpl.mHasTopUi = notificationShadeWindowControllerImpl.mHasTopUiChanged;
                return;
            case FalsingManager.VERSION /* 6 */:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.mIsExpansionAnimating = false;
                bubbleStackView.updateExpandedView();
                bubbleStackView.requestUpdate();
                bubbleStackView.showManageMenu(bubbleStackView.mShowingManage);
                return;
            default:
                GlowView glowView = (GlowView) this.f$0;
                int i2 = GlowView.$r8$clinit;
                Objects.requireNonNull(glowView);
                glowView.updateGlowSizes();
                glowView.post(new OneHandedController$$ExternalSyntheticLambda1(glowView, 7));
                return;
        }
    }
}
