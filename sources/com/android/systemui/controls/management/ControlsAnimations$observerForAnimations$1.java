package com.android.systemui.controls.management;

import android.content.Intent;
import android.view.ViewGroup;
import android.view.Window;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.mediarouter.R$bool;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
public final class ControlsAnimations$observerForAnimations$1 implements LifecycleObserver {
    public final /* synthetic */ ViewGroup $view;
    public final /* synthetic */ Window $window;
    public boolean showAnimation;

    public ControlsAnimations$observerForAnimations$1(Intent intent, ViewGroup viewGroup, Window window) {
        this.$view = viewGroup;
        this.$window = window;
        boolean z = false;
        this.showAnimation = intent.getBooleanExtra("extra_animate", false);
        viewGroup.setTransitionGroup(true);
        viewGroup.setTransitionAlpha(0.0f);
        if (R$bool.translationY == -1.0f ? true : z) {
            R$bool.translationY = viewGroup.getContext().getResources().getDimensionPixelSize(2131165762);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public final void enterAnimation() {
        if (this.showAnimation) {
            R$bool.enterAnimation(this.$view).start();
            this.showAnimation = false;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public final void resetAnimation() {
        this.$view.setTranslationY(0.0f);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public final void setup() {
        Window window = this.$window;
        ViewGroup viewGroup = this.$view;
        window.setAllowEnterTransitionOverlap(true);
        int id = viewGroup.getId();
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$enterWindowTransition$1.INSTANCE);
        windowTransition.addTarget(id);
        window.setEnterTransition(windowTransition);
        int id2 = viewGroup.getId();
        WindowTransition windowTransition2 = new WindowTransition(ControlsAnimations$exitWindowTransition$1.INSTANCE);
        windowTransition2.addTarget(id2);
        window.setExitTransition(windowTransition2);
        int id3 = viewGroup.getId();
        WindowTransition windowTransition3 = new WindowTransition(ControlsAnimations$enterWindowTransition$1.INSTANCE);
        windowTransition3.addTarget(id3);
        window.setReenterTransition(windowTransition3);
        int id4 = viewGroup.getId();
        WindowTransition windowTransition4 = new WindowTransition(ControlsAnimations$exitWindowTransition$1.INSTANCE);
        windowTransition4.addTarget(id4);
        window.setReturnTransition(windowTransition4);
    }
}
