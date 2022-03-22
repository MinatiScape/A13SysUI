package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.recents.TriangleShape;
import java.util.Objects;
/* loaded from: classes.dex */
public class BaseTooltipView extends FrameLayout {
    public final AccessibilityFloatingMenuView mAnchorView;
    public int mArrowCornerRadius;
    public int mArrowHeight;
    public int mArrowMargin;
    public int mArrowWidth;
    public final WindowManager.LayoutParams mCurrentLayoutParams;
    public int mFontSize;
    public boolean mIsShowing;
    public int mScreenWidth;
    public TextView mTextView;
    public int mTextViewCornerRadius;
    public int mTextViewMargin;
    public int mTextViewPadding;
    public final WindowManager mWindowManager;

    public void hide() {
        if (this.mIsShowing) {
            this.mIsShowing = false;
            this.mWindowManager.removeView(this);
        }
    }

    @Override // android.view.View
    public final boolean performAccessibilityAction(int i, Bundle bundle) {
        if (i != AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS.getId()) {
            return super.performAccessibilityAction(i, bundle);
        }
        hide();
        return true;
    }

    public BaseTooltipView(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(context);
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mAnchorView = accessibilityFloatingMenuView;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2024, 262152, -3);
        layoutParams.windowAnimations = 16973827;
        layoutParams.gravity = 8388659;
        this.mCurrentLayoutParams = layoutParams;
        View inflate = LayoutInflater.from(getContext()).inflate(2131623985, (ViewGroup) this, false);
        this.mTextView = (TextView) inflate.findViewById(2131429024);
        addView(inflate);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mAnchorView.onConfigurationChanged(configuration);
        updateTooltipView();
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 4) {
            hide();
        }
        return super.onTouchEvent(motionEvent);
    }

    public final void updateTooltipView() {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        Resources resources = getResources();
        this.mScreenWidth = resources.getDisplayMetrics().widthPixels;
        this.mArrowWidth = resources.getDimensionPixelSize(2131165312);
        this.mArrowHeight = resources.getDimensionPixelSize(2131165310);
        this.mArrowMargin = resources.getDimensionPixelSize(2131165311);
        this.mArrowCornerRadius = resources.getDimensionPixelSize(2131165309);
        this.mFontSize = resources.getDimensionPixelSize(2131165313);
        this.mTextViewMargin = resources.getDimensionPixelSize(2131165314);
        this.mTextViewPadding = resources.getDimensionPixelSize(2131165315);
        this.mTextViewCornerRadius = resources.getDimensionPixelSize(2131165316);
        this.mTextView.setTextSize(0, this.mFontSize);
        TextView textView = this.mTextView;
        int i5 = this.mTextViewPadding;
        textView.setPadding(i5, i5, i5, i5);
        GradientDrawable gradientDrawable = (GradientDrawable) this.mTextView.getBackground();
        gradientDrawable.setCornerRadius(this.mTextViewCornerRadius);
        gradientDrawable.setColor(Utils.getColorAttrDefaultColor(getContext(), 17956900));
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mAnchorView;
        Objects.requireNonNull(accessibilityFloatingMenuView);
        WindowManager.LayoutParams layoutParams = accessibilityFloatingMenuView.mCurrentLayoutParams;
        int i6 = layoutParams.x;
        int i7 = layoutParams.y;
        Rect rect = new Rect(i6, i7, (accessibilityFloatingMenuView.mPadding * 2) + accessibilityFloatingMenuView.mIconWidth + (accessibilityFloatingMenuView.getMarginStartEndWith(accessibilityFloatingMenuView.mLastConfiguration) * 2) + i6, accessibilityFloatingMenuView.getWindowHeight() + i7);
        boolean z2 = true;
        if (rect.left < this.mScreenWidth / 2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i = 2131427516;
        } else {
            i = 2131427517;
        }
        View findViewById = findViewById(i);
        findViewById.setVisibility(0);
        ViewGroup.LayoutParams layoutParams2 = findViewById.getLayoutParams();
        ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.createHorizontal(layoutParams2.width, layoutParams2.height, z));
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(Utils.getColorAttrDefaultColor(getContext(), 17956900));
        paint.setPathEffect(new CornerPathEffect(this.mArrowCornerRadius));
        findViewById.setBackground(shapeDrawable);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) findViewById.getLayoutParams();
        layoutParams3.width = this.mArrowWidth;
        layoutParams3.height = this.mArrowHeight;
        if (z) {
            i2 = 0;
        } else {
            i2 = this.mArrowMargin;
        }
        if (z) {
            i3 = this.mArrowMargin;
        } else {
            i3 = 0;
        }
        layoutParams3.setMargins(i2, 0, i3, 0);
        findViewById.setLayoutParams(layoutParams3);
        ViewGroup.LayoutParams layoutParams4 = this.mTextView.getLayoutParams();
        this.mTextView.measure(View.MeasureSpec.makeMeasureSpec((((this.mScreenWidth - rect.width()) - this.mArrowWidth) - this.mArrowMargin) - this.mTextViewMargin, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        layoutParams4.width = this.mTextView.getMeasuredWidth();
        this.mTextView.setLayoutParams(layoutParams4);
        WindowManager.LayoutParams layoutParams5 = this.mCurrentLayoutParams;
        int i8 = rect.left;
        int i9 = this.mScreenWidth;
        if (i8 >= i9 / 2) {
            z2 = false;
        }
        if (z2) {
            i4 = rect.width();
        } else {
            this.mTextView.measure(View.MeasureSpec.makeMeasureSpec((((i9 - rect.width()) - this.mArrowWidth) - this.mArrowMargin) - this.mTextViewMargin, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
            i4 = (i9 - ((this.mTextView.getMeasuredWidth() + this.mArrowWidth) + this.mArrowMargin)) - rect.width();
        }
        layoutParams5.x = i4;
        WindowManager.LayoutParams layoutParams6 = this.mCurrentLayoutParams;
        int centerY = rect.centerY();
        this.mTextView.measure(View.MeasureSpec.makeMeasureSpec((((this.mScreenWidth - rect.width()) - this.mArrowWidth) - this.mArrowMargin) - this.mTextViewMargin, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        layoutParams6.y = centerY - (this.mTextView.getMeasuredHeight() / 2);
    }
}
