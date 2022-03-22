package com.google.android.systemui.elmyra.feedback;

import android.view.View;
import android.view.ViewParent;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OpaHomeButton extends NavigationBarEffect {
    public final KeyguardViewMediator mKeyguardViewMediator;
    public StatusBar mStatusBar;

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    public final boolean validateFeedbackEffects(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!((View) arrayList.get(i)).isAttachedToWindow()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    public final List<FeedbackEffect> findFeedbackEffects(NavigationBarView navigationBarView) {
        ArrayList arrayList = new ArrayList();
        ButtonDispatcher homeButton = navigationBarView.getHomeButton();
        Objects.requireNonNull(homeButton);
        ArrayList<View> arrayList2 = homeButton.mViews;
        for (int i = 0; i < arrayList2.size(); i++) {
            View view = arrayList2.get(i);
            if (view instanceof FeedbackEffect) {
                arrayList.add((FeedbackEffect) view);
            }
        }
        return arrayList;
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
        if (z) {
            return false;
        }
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        Objects.requireNonNull(navigationBarView);
        View view = navigationBarView.mCurrentView;
        for (ViewParent parent = ((View) feedbackEffect).getParent(); parent != null; parent = parent.getParent()) {
            if (parent.equals(view)) {
                return true;
            }
        }
        return false;
    }

    public OpaHomeButton(KeyguardViewMediator keyguardViewMediator, StatusBar statusBar, NavigationModeController navigationModeController) {
        super(statusBar, navigationModeController);
        this.mStatusBar = statusBar;
        this.mKeyguardViewMediator = keyguardViewMediator;
    }
}
