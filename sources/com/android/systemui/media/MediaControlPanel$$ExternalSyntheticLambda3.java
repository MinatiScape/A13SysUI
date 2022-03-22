package com.android.systemui.media;

import android.content.Intent;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.ManageEducationView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda3 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                MediaControlPanel mediaControlPanel = (MediaControlPanel) this.f$0;
                Objects.requireNonNull(mediaControlPanel);
                if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                    mediaControlPanel.closeGuts(false);
                    return;
                }
                return;
            case 1:
                KeyguardBottomAreaView keyguardBottomAreaView = (KeyguardBottomAreaView) this.f$0;
                Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
                Objects.requireNonNull(keyguardBottomAreaView);
                if (!keyguardBottomAreaView.mFalsingManager.isFalseTap(1)) {
                    keyguardBottomAreaView.mQuickAccessWalletController.startQuickAccessUiIntent(keyguardBottomAreaView.mActivityStarter, ActivityLaunchAnimator.Controller.fromView(view, null), keyguardBottomAreaView.mHasCard);
                    return;
                }
                return;
            default:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                if (bubbleStackView.mShowingManage) {
                    bubbleStackView.showManageMenu(false);
                    return;
                }
                ManageEducationView manageEducationView = bubbleStackView.mManageEduView;
                if (manageEducationView != null && manageEducationView.getVisibility() == 0) {
                    bubbleStackView.mManageEduView.hide();
                    return;
                } else if (bubbleStackView.isStackEduShowing()) {
                    bubbleStackView.mStackEduView.hide(false);
                    return;
                } else {
                    BubbleData bubbleData = bubbleStackView.mBubbleData;
                    Objects.requireNonNull(bubbleData);
                    if (bubbleData.mExpanded) {
                        bubbleStackView.mBubbleData.setExpanded(false);
                        return;
                    } else {
                        bubbleStackView.maybeShowStackEdu();
                        return;
                    }
                }
        }
    }
}
