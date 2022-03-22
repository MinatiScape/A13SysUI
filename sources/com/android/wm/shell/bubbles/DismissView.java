package com.android.wm.shell.bubbles;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import java.util.Objects;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
/* compiled from: DismissView.kt */
/* loaded from: classes.dex */
public final class DismissView extends FrameLayout {
    public final PhysicsAnimator<DismissCircleView> animator;
    public DismissCircleView circle;
    public boolean isShowing;
    public WindowManager wm;
    public final PhysicsAnimator.SpringConfig spring = new PhysicsAnimator.SpringConfig(200.0f, 0.75f);
    public final int DISMISS_SCRIM_FADE_MS = 200;

    public final void hide() {
        if (this.isShowing) {
            this.isShowing = false;
            Drawable background = getBackground();
            Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.TransitionDrawable");
            ((TransitionDrawable) background).reverseTransition(this.DISMISS_SCRIM_FADE_MS);
            PhysicsAnimator<DismissCircleView> physicsAnimator = this.animator;
            PhysicsAnimator.SpringConfig springConfig = this.spring;
            Objects.requireNonNull(physicsAnimator);
            physicsAnimator.spring(DynamicAnimation.TRANSLATION_Y, getHeight(), 0.0f, springConfig);
            physicsAnimator.endActions.addAll(ArraysKt___ArraysKt.filterNotNull(new Function0[]{new DismissView$hide$1(this)}));
            physicsAnimator.start();
        }
    }

    public final void updatePadding() {
        setPadding(0, 0, 0, getResources().getDimensionPixelSize(2131165726) + this.wm.getCurrentWindowMetrics().getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom);
    }

    public DismissView(Context context) {
        super(context);
        DismissCircleView dismissCircleView = new DismissCircleView(context);
        this.circle = dismissCircleView;
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        this.animator = PhysicsAnimator.Companion.getInstance(dismissCircleView);
        Object systemService = context.getSystemService("window");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.view.WindowManager");
        this.wm = (WindowManager) systemService;
        setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(2131165727), 80));
        updatePadding();
        setClipToPadding(false);
        setClipChildren(false);
        setVisibility(4);
        setBackgroundResource(2131231711);
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131165666);
        addView(this.circle, new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 81));
        this.circle.setTranslationY(getResources().getDimensionPixelSize(2131165727));
    }
}
