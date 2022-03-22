package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.animation.Interpolators;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
/* compiled from: ManageEducationView.kt */
/* loaded from: classes.dex */
public final class ManageEducationView extends LinearLayout {
    public BubbleExpandedView bubbleExpandedView;
    public boolean isHiding;
    public final BubblePositioner positioner;
    public final long ANIMATE_DURATION = 200;
    public final Lazy manageView$delegate = LazyKt__LazyJVMKt.lazy(new ManageEducationView$manageView$2(this));
    public final Lazy manageButton$delegate = LazyKt__LazyJVMKt.lazy(new ManageEducationView$manageButton$2(this));
    public final Lazy gotItButton$delegate = LazyKt__LazyJVMKt.lazy(new ManageEducationView$gotItButton$2(this));
    public Rect realManageButtonRect = new Rect();

    public final Button getManageButton() {
        return (Button) this.manageButton$delegate.getValue();
    }

    public final ViewGroup getManageView() {
        return (ViewGroup) this.manageView$delegate.getValue();
    }

    public final void hide() {
        TaskView taskView;
        BubbleExpandedView bubbleExpandedView = this.bubbleExpandedView;
        if (!(bubbleExpandedView == null || (taskView = bubbleExpandedView.mTaskView) == null)) {
            taskView.mObscuredTouchRect = null;
        }
        if (getVisibility() == 0 && !this.isHiding) {
            animate().withStartAction(new Runnable() { // from class: com.android.wm.shell.bubbles.ManageEducationView$hide$1
                @Override // java.lang.Runnable
                public final void run() {
                    ManageEducationView.this.isHiding = true;
                }
            }).alpha(0.0f).setDuration(this.ANIMATE_DURATION).withEndAction(new Runnable() { // from class: com.android.wm.shell.bubbles.ManageEducationView$hide$2
                @Override // java.lang.Runnable
                public final void run() {
                    ManageEducationView manageEducationView = ManageEducationView.this;
                    manageEducationView.isHiding = false;
                    manageEducationView.setVisibility(8);
                }
            });
        }
    }

    public final void show(final BubbleExpandedView bubbleExpandedView) {
        int i;
        TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{17956900});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        getManageButton().setTextColor(((LinearLayout) this).mContext.getColor(17170472));
        getManageButton().setBackgroundDrawable(new ColorDrawable(color));
        ((Button) this.gotItButton$delegate.getValue()).setBackgroundDrawable(new ColorDrawable(color));
        if (getVisibility() != 0) {
            this.bubbleExpandedView = bubbleExpandedView;
            TaskView taskView = bubbleExpandedView.mTaskView;
            if (taskView != null) {
                BubblePositioner bubblePositioner = this.positioner;
                Objects.requireNonNull(bubblePositioner);
                taskView.mObscuredTouchRect = new Rect(bubblePositioner.mScreenRect);
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            BubblePositioner bubblePositioner2 = this.positioner;
            Objects.requireNonNull(bubblePositioner2);
            if (bubblePositioner2.mIsLargeScreen) {
                i = getContext().getResources().getDimensionPixelSize(2131165460);
            } else {
                i = -1;
            }
            layoutParams.width = i;
            setAlpha(0.0f);
            setVisibility(0);
            bubbleExpandedView.mManageButton.getBoundsOnScreen(this.realManageButtonRect);
            getManageView().setPadding(this.realManageButtonRect.left - ((LinearLayout.LayoutParams) bubbleExpandedView.mManageButton.getLayoutParams()).getMarginStart(), getManageView().getPaddingTop(), getManageView().getPaddingRight(), getManageView().getPaddingBottom());
            post(new Runnable() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1
                @Override // java.lang.Runnable
                public final void run() {
                    Button manageButton = ManageEducationView.this.getManageButton();
                    final ManageEducationView manageEducationView = ManageEducationView.this;
                    final BubbleExpandedView bubbleExpandedView2 = bubbleExpandedView;
                    manageButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ManageEducationView.this.hide();
                            bubbleExpandedView2.findViewById(2131428297).performClick();
                        }
                    });
                    ManageEducationView manageEducationView2 = ManageEducationView.this;
                    Objects.requireNonNull(manageEducationView2);
                    final ManageEducationView manageEducationView3 = ManageEducationView.this;
                    ((Button) manageEducationView2.gotItButton$delegate.getValue()).setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ManageEducationView.this.hide();
                        }
                    });
                    final ManageEducationView manageEducationView4 = ManageEducationView.this;
                    manageEducationView4.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ManageEducationView.this.hide();
                        }
                    });
                    Rect rect = new Rect();
                    ManageEducationView.this.getManageButton().getDrawingRect(rect);
                    ManageEducationView.this.getManageView().offsetDescendantRectToMyCoords(ManageEducationView.this.getManageButton(), rect);
                    ManageEducationView.this.setTranslationX(0.0f);
                    ManageEducationView manageEducationView5 = ManageEducationView.this;
                    manageEducationView5.setTranslationY(manageEducationView5.realManageButtonRect.top - rect.top);
                    ManageEducationView.this.bringToFront();
                    ManageEducationView.this.animate().setDuration(ManageEducationView.this.ANIMATE_DURATION).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
                }
            });
            getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesManageOnboarding", true).apply();
        }
    }

    public ManageEducationView(Context context, BubblePositioner bubblePositioner) {
        super(context);
        this.positioner = bubblePositioner;
        LayoutInflater.from(context).inflate(2131624027, this);
        setVisibility(8);
        setElevation(getResources().getDimensionPixelSize(2131165417));
        setLayoutDirection(3);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
    }

    @Override // android.view.View
    public final void setLayoutDirection(int i) {
        int i2;
        super.setLayoutDirection(i);
        ViewGroup manageView = getManageView();
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            i2 = 2131231643;
        } else {
            i2 = 2131231642;
        }
        manageView.setBackgroundResource(i2);
    }
}
