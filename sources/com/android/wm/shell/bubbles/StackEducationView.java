package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.animation.Interpolators;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
/* compiled from: StackEducationView.kt */
/* loaded from: classes.dex */
public final class StackEducationView extends LinearLayout {
    public final BubbleController controller;
    public boolean isHiding;
    public final BubblePositioner positioner;
    public final long ANIMATE_DURATION = 200;
    public final long ANIMATE_DURATION_SHORT = 40;
    public final Lazy view$delegate = LazyKt__LazyJVMKt.lazy(new StackEducationView$view$2(this));
    public final Lazy titleTextView$delegate = LazyKt__LazyJVMKt.lazy(new StackEducationView$titleTextView$2(this));
    public final Lazy descTextView$delegate = LazyKt__LazyJVMKt.lazy(new StackEducationView$descTextView$2(this));

    public final boolean show(final PointF pointF) {
        int i;
        this.isHiding = false;
        if (getVisibility() == 0) {
            return false;
        }
        this.controller.updateWindowFlagsForBackpress(true);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        BubblePositioner bubblePositioner = this.positioner;
        Objects.requireNonNull(bubblePositioner);
        if (bubblePositioner.mIsLargeScreen) {
            i = getContext().getResources().getDimensionPixelSize(2131165460);
        } else {
            i = -1;
        }
        layoutParams.width = i;
        setAlpha(0.0f);
        setVisibility(0);
        post(new Runnable() { // from class: com.android.wm.shell.bubbles.StackEducationView$show$1
            @Override // java.lang.Runnable
            public final void run() {
                StackEducationView.this.requestFocus();
                StackEducationView stackEducationView = StackEducationView.this;
                Objects.requireNonNull(stackEducationView);
                View view = (View) stackEducationView.view$delegate.getValue();
                StackEducationView stackEducationView2 = StackEducationView.this;
                PointF pointF2 = pointF;
                if (view.getResources().getConfiguration().getLayoutDirection() == 0) {
                    BubblePositioner bubblePositioner2 = stackEducationView2.positioner;
                    Objects.requireNonNull(bubblePositioner2);
                    view.setPadding(view.getPaddingRight() + bubblePositioner2.mBubbleSize, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                } else {
                    int paddingLeft = view.getPaddingLeft();
                    int paddingTop = view.getPaddingTop();
                    BubblePositioner bubblePositioner3 = stackEducationView2.positioner;
                    Objects.requireNonNull(bubblePositioner3);
                    view.setPadding(paddingLeft, paddingTop, view.getPaddingLeft() + bubblePositioner3.mBubbleSize, view.getPaddingBottom());
                }
                float f = pointF2.y;
                BubblePositioner bubblePositioner4 = stackEducationView2.positioner;
                Objects.requireNonNull(bubblePositioner4);
                view.setTranslationY((f + (bubblePositioner4.mBubbleSize / 2)) - (view.getHeight() / 2));
                StackEducationView.this.animate().setDuration(StackEducationView.this.ANIMATE_DURATION).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
            }
        });
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesOnboarding", true).apply();
        return true;
    }

    public StackEducationView(Context context, BubblePositioner bubblePositioner, BubbleController bubbleController) {
        super(context);
        this.positioner = bubblePositioner;
        this.controller = bubbleController;
        LayoutInflater.from(context).inflate(2131624025, this);
        setVisibility(8);
        setElevation(getResources().getDimensionPixelSize(2131165417));
        setLayoutDirection(3);
    }

    public final void hide(boolean z) {
        long j;
        if (getVisibility() == 0 && !this.isHiding) {
            this.isHiding = true;
            this.controller.updateWindowFlagsForBackpress(false);
            ViewPropertyAnimator alpha = animate().alpha(0.0f);
            if (z) {
                j = this.ANIMATE_DURATION_SHORT;
            } else {
                j = this.ANIMATE_DURATION;
            }
            alpha.setDuration(j).withEndAction(new Runnable() { // from class: com.android.wm.shell.bubbles.StackEducationView$hide$1
                @Override // java.lang.Runnable
                public final void run() {
                    StackEducationView.this.setVisibility(8);
                }
            });
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFocusableInTouchMode(true);
        setOnKeyListener(new View.OnKeyListener() { // from class: com.android.wm.shell.bubbles.StackEducationView$onAttachedToWindow$1
            @Override // android.view.View.OnKeyListener
            public final boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == 1 && i == 4) {
                    StackEducationView stackEducationView = StackEducationView.this;
                    if (!stackEducationView.isHiding) {
                        stackEducationView.hide(false);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnKeyListener(null);
        this.controller.updateWindowFlagsForBackpress(false);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{16843829, 16842809});
        int color = obtainStyledAttributes.getColor(0, -16777216);
        int color2 = obtainStyledAttributes.getColor(1, -1);
        obtainStyledAttributes.recycle();
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(color2, color, true);
        ((TextView) this.titleTextView$delegate.getValue()).setTextColor(ensureTextContrast);
        ((TextView) this.descTextView$delegate.getValue()).setTextColor(ensureTextContrast);
    }

    @Override // android.view.View
    public final void setLayoutDirection(int i) {
        int i2;
        super.setLayoutDirection(i);
        View view = (View) this.view$delegate.getValue();
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            i2 = 2131231642;
        } else {
            i2 = 2131231643;
        }
        view.setBackgroundResource(i2);
    }
}
