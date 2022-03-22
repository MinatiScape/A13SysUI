package com.google.android.systemui.elmyra.feedback;

import androidx.leanback.R$color;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class NavigationBarEffect implements FeedbackEffect, NavigationModeController.ModeChangedListener {
    public final ArrayList mFeedbackEffects = new ArrayList();
    public int mNavMode;
    public final StatusBar mStatusBar;

    public abstract List<FeedbackEffect> findFeedbackEffects(NavigationBarView navigationBarView);

    public abstract boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect);

    public abstract boolean validateFeedbackEffects(ArrayList arrayList);

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavMode = i;
        refreshFeedbackEffects();
    }

    public final void refreshFeedbackEffects() {
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        if (navigationBarView == null || !(!R$color.isGesturalMode(this.mNavMode))) {
            reset();
            return;
        }
        if (!validateFeedbackEffects(this.mFeedbackEffects)) {
            this.mFeedbackEffects.clear();
        }
        if (this.mFeedbackEffects.isEmpty()) {
            this.mFeedbackEffects.addAll(findFeedbackEffects(navigationBarView));
        }
    }

    public void reset() {
        this.mFeedbackEffects.clear();
    }

    public NavigationBarEffect(StatusBar statusBar, NavigationModeController navigationModeController) {
        this.mStatusBar = statusBar;
        this.mNavMode = navigationModeController.addListener(this);
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        refreshFeedbackEffects();
        for (int i2 = 0; i2 < this.mFeedbackEffects.size(); i2++) {
            FeedbackEffect feedbackEffect = (FeedbackEffect) this.mFeedbackEffects.get(i2);
            if (isActiveFeedbackEffect(feedbackEffect)) {
                feedbackEffect.onProgress(f, i);
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
        refreshFeedbackEffects();
        for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
            ((FeedbackEffect) this.mFeedbackEffects.get(i)).onRelease();
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        refreshFeedbackEffects();
        for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
            ((FeedbackEffect) this.mFeedbackEffects.get(i)).onResolve(detectionProperties);
        }
    }
}
