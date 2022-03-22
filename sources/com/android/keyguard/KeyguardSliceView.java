package com.android.keyguard;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.RowContent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.util.wakelock.KeepAwakeAnimationListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardSliceView extends LinearLayout {
    public Runnable mContentChangeListener;
    public float mDarkAmount = 0.0f;
    public boolean mHasHeader;
    public int mIconSize;
    public int mIconSizeWithHeader;
    public final LayoutTransition mLayoutTransition;
    public View.OnClickListener mOnClickListener;
    public Row mRow;
    public int mTextColor;
    @VisibleForTesting
    public TextView mTitle;

    /* loaded from: classes.dex */
    public static class Row extends LinearLayout {
        public float mDarkAmount;
        public final KeepAwakeAnimationListener mKeepAwakeListener;
        public HashSet mKeyguardSliceTextViewSet;
        public LayoutTransition mLayoutTransition;

        public Row(Context context) {
            this(context, null);
        }

        @Override // android.view.View
        public final boolean hasOverlappingRendering() {
            return false;
        }

        public Row(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        @Override // android.view.View
        public final void onFinishInflate() {
            LayoutTransition layoutTransition = new LayoutTransition();
            this.mLayoutTransition = layoutTransition;
            layoutTransition.setDuration(550L);
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(null, PropertyValuesHolder.ofInt("left", 0, 1), PropertyValuesHolder.ofInt("right", 0, 1));
            this.mLayoutTransition.setAnimator(0, ofPropertyValuesHolder);
            this.mLayoutTransition.setAnimator(1, ofPropertyValuesHolder);
            LayoutTransition layoutTransition2 = this.mLayoutTransition;
            AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = Interpolators.ACCELERATE_DECELERATE;
            layoutTransition2.setInterpolator(0, accelerateDecelerateInterpolator);
            this.mLayoutTransition.setInterpolator(1, accelerateDecelerateInterpolator);
            this.mLayoutTransition.setStartDelay(0, 550L);
            this.mLayoutTransition.setStartDelay(1, 550L);
            this.mLayoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, "alpha", 0.0f, 1.0f));
            this.mLayoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, "alpha", 1.0f, 0.0f);
            this.mLayoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
            this.mLayoutTransition.setDuration(3, 137L);
            this.mLayoutTransition.setAnimator(3, ofFloat);
            this.mLayoutTransition.setAnimateParentHierarchy(false);
        }

        public Row(Context context, AttributeSet attributeSet, int i) {
            this(context, attributeSet, i, 0);
        }

        @Override // android.view.ViewGroup
        public final void addView(View view, int i) {
            super.addView(view, i);
            if (view instanceof KeyguardSliceTextView) {
                this.mKeyguardSliceTextViewSet.add((KeyguardSliceTextView) view);
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        public final void onMeasure(int i, int i2) {
            View.MeasureSpec.getSize(i);
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof KeyguardSliceTextView) {
                    ((KeyguardSliceTextView) childAt).setMaxWidth(Integer.MAX_VALUE);
                }
            }
            super.onMeasure(i, i2);
        }

        @Override // android.view.View
        public final void onVisibilityAggregated(boolean z) {
            LayoutTransition layoutTransition;
            super.onVisibilityAggregated(z);
            if (z) {
                layoutTransition = this.mLayoutTransition;
            } else {
                layoutTransition = null;
            }
            setLayoutTransition(layoutTransition);
        }

        @Override // android.view.ViewGroup, android.view.ViewManager
        public final void removeView(View view) {
            super.removeView(view);
            if (view instanceof KeyguardSliceTextView) {
                this.mKeyguardSliceTextViewSet.remove((KeyguardSliceTextView) view);
            }
        }

        public Row(Context context, AttributeSet attributeSet, int i, int i2) {
            super(context, attributeSet, i, i2);
            this.mKeyguardSliceTextViewSet = new HashSet();
            this.mKeepAwakeListener = new KeepAwakeAnimationListener(((LinearLayout) this).mContext);
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class KeyguardSliceTextView extends TextView {
        public KeyguardSliceTextView(Context context) {
            super(context, null, 0, 2132017919);
            updatePadding();
            setEllipsize(TextUtils.TruncateAt.END);
        }

        @Override // android.widget.TextView
        public final void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            Drawable[] compoundDrawables;
            super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
            int currentTextColor = getCurrentTextColor();
            for (Drawable drawable5 : getCompoundDrawables()) {
                if (drawable5 != null) {
                    drawable5.setTint(currentTextColor);
                }
            }
            updatePadding();
        }

        @Override // android.widget.TextView
        public final void setText(CharSequence charSequence, TextView.BufferType bufferType) {
            super.setText(charSequence, bufferType);
            updatePadding();
        }

        @Override // android.widget.TextView
        public final void setTextColor(int i) {
            Drawable[] compoundDrawables;
            super.setTextColor(i);
            int currentTextColor = getCurrentTextColor();
            for (Drawable drawable : getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setTint(currentTextColor);
                }
            }
        }

        public final void updatePadding() {
            int i;
            boolean z = !TextUtils.isEmpty(getText());
            int dimension = ((int) getContext().getResources().getDimension(2131167325)) / 2;
            if (z) {
                i = dimension;
            } else {
                i = 0;
            }
            setPadding(0, dimension, 0, i);
            setCompoundDrawablePadding((int) ((TextView) this).mContext.getResources().getDimension(2131167326));
        }
    }

    @VisibleForTesting
    public int getTextColor() {
        return ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
    }

    public final void onDensityOrFontScaleChanged() {
        this.mIconSize = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(2131167327);
        this.mIconSizeWithHeader = (int) ((LinearLayout) this).mContext.getResources().getDimension(2131165794);
        for (int i = 0; i < this.mRow.getChildCount(); i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof KeyguardSliceTextView) {
                KeyguardSliceTextView keyguardSliceTextView = (KeyguardSliceTextView) childAt;
                Objects.requireNonNull(keyguardSliceTextView);
                keyguardSliceTextView.updatePadding();
            }
        }
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        this.mTitle.setOnClickListener(onClickListener);
    }

    @VisibleForTesting
    public void setTextColor(int i) {
        this.mTextColor = i;
        updateTextColors();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final HashMap showSlice(RowContent rowContent, List list) {
        boolean z;
        PendingIntent pendingIntent;
        CharSequence charSequence;
        CharSequence charSequence2;
        Drawable drawable;
        boolean z2;
        int i;
        CharSequence charSequence3;
        Trace.beginSection("KeyguardSliceView#showSlice");
        int i2 = 0;
        if (rowContent != null) {
            z = true;
        } else {
            z = false;
        }
        this.mHasHeader = z;
        HashMap hashMap = new HashMap();
        int i3 = 8;
        if (!this.mHasHeader) {
            this.mTitle.setVisibility(8);
        } else {
            this.mTitle.setVisibility(0);
            Objects.requireNonNull(rowContent);
            SliceItem sliceItem = rowContent.mTitleItem;
            if (sliceItem != null) {
                charSequence3 = (CharSequence) sliceItem.mObj;
            } else {
                charSequence3 = null;
            }
            this.mTitle.setText(charSequence3);
            SliceItem sliceItem2 = rowContent.mPrimaryAction;
            if (!(sliceItem2 == null || sliceItem2.getAction() == null)) {
                hashMap.put(this.mTitle, rowContent.mPrimaryAction.getAction());
            }
        }
        int size = list.size();
        int textColor = getTextColor();
        boolean z3 = this.mHasHeader;
        Row row = this.mRow;
        if (size > 0) {
            i3 = 0;
        }
        row.setVisibility(i3);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRow.getLayoutParams();
        layoutParams.gravity = 8388611;
        this.mRow.setLayoutParams(layoutParams);
        for (int i4 = z3; i4 < size; i4++) {
            RowContent rowContent2 = (RowContent) list.get(i4);
            Objects.requireNonNull(rowContent2);
            SliceItem sliceItem3 = rowContent2.mSliceItem;
            Uri uri = sliceItem3.getSlice().getUri();
            KeyguardSliceTextView keyguardSliceTextView = (KeyguardSliceTextView) this.mRow.findViewWithTag(uri);
            if (keyguardSliceTextView == null) {
                keyguardSliceTextView = new KeyguardSliceTextView(((LinearLayout) this).mContext);
                keyguardSliceTextView.setTextColor(textColor);
                keyguardSliceTextView.setTag(uri);
                this.mRow.addView(keyguardSliceTextView, i4 - (this.mHasHeader ? 1 : 0));
            }
            SliceItem sliceItem4 = rowContent2.mPrimaryAction;
            if (sliceItem4 != null) {
                pendingIntent = sliceItem4.getAction();
            } else {
                pendingIntent = null;
            }
            hashMap.put(keyguardSliceTextView, pendingIntent);
            SliceItem sliceItem5 = rowContent2.mTitleItem;
            if (sliceItem5 == null) {
                charSequence = null;
            } else {
                charSequence = (CharSequence) sliceItem5.mObj;
            }
            keyguardSliceTextView.setText(charSequence);
            SliceItem sliceItem6 = rowContent2.mContentDescr;
            if (sliceItem6 != null) {
                charSequence2 = (CharSequence) sliceItem6.mObj;
            } else {
                charSequence2 = null;
            }
            keyguardSliceTextView.setContentDescription(charSequence2);
            SliceItem find = SliceQuery.find(sliceItem3.getSlice(), "image", (String[]) null, (String[]) null);
            if (find != null) {
                if (this.mHasHeader) {
                    i = this.mIconSizeWithHeader;
                } else {
                    i = this.mIconSize;
                }
                drawable = ((IconCompat) find.mObj).loadDrawable(((LinearLayout) this).mContext);
                if (drawable != null) {
                    if (drawable instanceof InsetDrawable) {
                        drawable = ((InsetDrawable) drawable).getDrawable();
                    }
                    drawable.setBounds(0, 0, Math.max((int) ((drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight()) * i), 1), i);
                }
            } else {
                drawable = null;
            }
            keyguardSliceTextView.setCompoundDrawablesRelative(drawable, null, null, null);
            keyguardSliceTextView.setOnClickListener(this.mOnClickListener);
            if (pendingIntent != null) {
                z2 = true;
            } else {
                z2 = false;
            }
            keyguardSliceTextView.setClickable(z2);
        }
        while (i2 < this.mRow.getChildCount()) {
            View childAt = this.mRow.getChildAt(i2);
            if (!hashMap.containsKey(childAt)) {
                this.mRow.removeView(childAt);
                i2--;
            }
            i2++;
        }
        Runnable runnable = this.mContentChangeListener;
        if (runnable != null) {
            runnable.run();
        }
        Trace.endSection();
        return hashMap;
    }

    public KeyguardSliceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context.getResources();
        LayoutTransition layoutTransition = new LayoutTransition();
        this.mLayoutTransition = layoutTransition;
        layoutTransition.setStagger(0, 275L);
        layoutTransition.setDuration(2, 550L);
        layoutTransition.setDuration(3, 275L);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
        layoutTransition.setInterpolator(2, Interpolators.FAST_OUT_SLOW_IN);
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimateParentHierarchy(false);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(2131429057);
        this.mRow = (Row) findViewById(2131428719);
        this.mTextColor = Utils.getColorAttrDefaultColor(((LinearLayout) this).mContext, 2130970103);
        this.mIconSize = (int) ((LinearLayout) this).mContext.getResources().getDimension(2131167327);
        this.mIconSizeWithHeader = (int) ((LinearLayout) this).mContext.getResources().getDimension(2131165794);
        this.mTitle.setBreakStrategy(2);
    }

    @Override // android.view.View
    public final void onVisibilityAggregated(boolean z) {
        LayoutTransition layoutTransition;
        super.onVisibilityAggregated(z);
        if (z) {
            layoutTransition = this.mLayoutTransition;
        } else {
            layoutTransition = null;
        }
        setLayoutTransition(layoutTransition);
    }

    public final void updateTextColors() {
        int textColor = getTextColor();
        this.mTitle.setTextColor(textColor);
        int childCount = this.mRow.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(textColor);
            }
        }
    }
}
