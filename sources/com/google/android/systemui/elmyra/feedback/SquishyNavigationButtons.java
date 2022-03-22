package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import android.view.View;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SquishyNavigationButtons extends NavigationBarEffect {
    public final KeyguardViewMediator mKeyguardViewMediator;
    public final SquishyViewController mViewController;

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    public final List<FeedbackEffect> findFeedbackEffects(NavigationBarView navigationBarView) {
        this.mViewController.clearViews();
        ButtonDispatcher backButton = navigationBarView.getBackButton();
        Objects.requireNonNull(backButton);
        ArrayList<View> arrayList = backButton.mViews;
        for (int i = 0; i < arrayList.size(); i++) {
            SquishyViewController squishyViewController = this.mViewController;
            Objects.requireNonNull(squishyViewController);
            squishyViewController.mLeftViews.add(arrayList.get(i));
        }
        ButtonDispatcher recentsButton = navigationBarView.getRecentsButton();
        Objects.requireNonNull(recentsButton);
        ArrayList<View> arrayList2 = recentsButton.mViews;
        for (int i2 = 0; i2 < arrayList2.size(); i2++) {
            SquishyViewController squishyViewController2 = this.mViewController;
            Objects.requireNonNull(squishyViewController2);
            squishyViewController2.mRightViews.add(arrayList2.get(i2));
        }
        return Arrays.asList(this.mViewController);
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    public final boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect) {
        boolean z;
        KeyguardViewMediator keyguardViewMediator = this.mKeyguardViewMediator;
        Objects.requireNonNull(keyguardViewMediator);
        if (!keyguardViewMediator.mShowing || keyguardViewMediator.mOccluded) {
            z = false;
        } else {
            z = true;
        }
        return !z;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    public final boolean validateFeedbackEffects(ArrayList arrayList) {
        SquishyViewController squishyViewController = this.mViewController;
        Objects.requireNonNull(squishyViewController);
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= squishyViewController.mLeftViews.size()) {
                int i2 = 0;
                while (true) {
                    if (i2 >= squishyViewController.mRightViews.size()) {
                        z = true;
                        break;
                    } else if (!((View) squishyViewController.mRightViews.get(i2)).isAttachedToWindow()) {
                        break;
                    } else {
                        i2++;
                    }
                }
            } else if (!((View) squishyViewController.mLeftViews.get(i)).isAttachedToWindow()) {
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            this.mViewController.clearViews();
        }
        return z;
    }

    public SquishyNavigationButtons(Context context, KeyguardViewMediator keyguardViewMediator, StatusBar statusBar, NavigationModeController navigationModeController) {
        super(statusBar, navigationModeController);
        this.mViewController = new SquishyViewController(context);
        this.mKeyguardViewMediator = keyguardViewMediator;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    public final void reset() {
        super.reset();
        this.mViewController.clearViews();
    }
}
