package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.R$array;
import com.android.systemui.R$styleable;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
/* loaded from: classes.dex */
public class SmartReplyView extends ViewGroup {
    public final BreakIterator mBreakIterator;
    public PriorityQueue<Button> mCandidateButtonQueueForSqueezing;
    public int mCurrentBackgroundColor;
    public boolean mCurrentColorized;
    public int mCurrentRippleColor;
    public int mCurrentStrokeColor;
    public int mCurrentTextColor;
    public final int mDefaultBackgroundColor;
    public final int mDefaultStrokeColor;
    public boolean mDidHideSystemReplies;
    public long mLastDispatchDrawTime;
    public long mLastDrawChildTime;
    public long mLastMeasureTime;
    public int mMaxNumActions;
    public int mMaxSqueezeRemeasureAttempts;
    public int mMinNumSystemGeneratedReplies;
    public final double mMinStrokeContrast;
    public final int mRippleColor;
    public final int mRippleColorDarkBg;
    public View mSmartReplyContainer;
    public final int mSpacing;
    public final int mStrokeWidth;
    public int mTotalSqueezeRemeasureAttempts;
    public static final int MEASURE_SPEC_ANY_LENGTH = View.MeasureSpec.makeMeasureSpec(0, 0);
    public static final SmartReplyView$$ExternalSyntheticLambda0 DECREASING_MEASURED_WIDTH_WITHOUT_PADDING_COMPARATOR = SmartReplyView$$ExternalSyntheticLambda0.INSTANCE;
    public boolean mSmartRepliesGeneratedByAssistant = false;
    public final int mHeightUpperLimit = R$array.getFontScaledHeight(((ViewGroup) this).mContext, 2131167026);
    public final int mDefaultTextColor = ((ViewGroup) this).mContext.getColor(2131100627);
    public final int mDefaultTextColorDarkBg = ((ViewGroup) this).mContext.getColor(2131100628);

    /* loaded from: classes.dex */
    public enum SmartButtonType {
        REPLY,
        ACTION
    }

    /* loaded from: classes.dex */
    public static class SmartSuggestionMeasures {
        public int mMaxChildHeight;
        public int mMeasuredWidth;

        public final Object clone() throws CloneNotSupportedException {
            return new SmartSuggestionMeasures(this.mMeasuredWidth, this.mMaxChildHeight);
        }

        public SmartSuggestionMeasures(int i, int i2) {
            this.mMeasuredWidth = i;
            this.mMaxChildHeight = i2;
        }
    }

    /* loaded from: classes.dex */
    public static class SmartActions {
        public final List<Notification.Action> actions;
        public final boolean fromAssistant;

        public SmartActions(List<Notification.Action> list, boolean z) {
            this.actions = list;
            this.fromAssistant = z;
        }
    }

    /* loaded from: classes.dex */
    public static class SmartReplies {
        public final List<CharSequence> choices;
        public final boolean fromAssistant;
        public final PendingIntent pendingIntent;
        public final RemoteInput remoteInput;

        public SmartReplies(List<CharSequence> list, RemoteInput remoteInput, PendingIntent pendingIntent, boolean z) {
            this.choices = list;
            this.remoteInput = remoteInput;
            this.pendingIntent = pendingIntent;
            this.fromAssistant = z;
        }
    }

    public final ArrayList filterActionsOrReplies(SmartButtonType smartButtonType) {
        ArrayList arrayList = new ArrayList();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (childAt.getVisibility() == 0 && (childAt instanceof Button) && layoutParams.mButtonType == smartButtonType) {
                arrayList.add(childAt);
            }
        }
        return arrayList;
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public final LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(((ViewGroup) this).mContext, attributeSet);
    }

    /* JADX WARN: Code restructure failed: missing block: B:79:0x01db, code lost:
        r0 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x0348  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0270 A[SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r30, int r31) {
        /*
            Method dump skipped, instructions count: 943
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyView.onMeasure(int, int):void");
    }

    public final void setBackgroundTintColor(int i, boolean z) {
        int i2;
        int i3;
        if (!(i == this.mCurrentBackgroundColor && z == this.mCurrentColorized)) {
            this.mCurrentBackgroundColor = i;
            this.mCurrentColorized = z;
            boolean isColorDark = Notification.Builder.isColorDark(i);
            if (isColorDark) {
                i2 = this.mDefaultTextColorDarkBg;
            } else {
                i2 = this.mDefaultTextColor;
            }
            int i4 = i | (-16777216);
            int ensureTextContrast = ContrastColorUtil.ensureTextContrast(i2, i4, isColorDark);
            this.mCurrentTextColor = ensureTextContrast;
            if (!z) {
                ensureTextContrast = ContrastColorUtil.ensureContrast(this.mDefaultStrokeColor, i4, isColorDark, this.mMinStrokeContrast);
            }
            this.mCurrentStrokeColor = ensureTextContrast;
            if (isColorDark) {
                i3 = this.mRippleColorDarkBg;
            } else {
                i3 = this.mRippleColor;
            }
            this.mCurrentRippleColor = i3;
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                setButtonColors((Button) getChildAt(i5));
            }
        }
    }

    public SmartReplyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int color = context.getColor(2131100625);
        this.mDefaultBackgroundColor = color;
        int color2 = ((ViewGroup) this).mContext.getColor(2131100626);
        this.mDefaultStrokeColor = color2;
        int color3 = ((ViewGroup) this).mContext.getColor(2131100488);
        this.mRippleColor = color3;
        this.mRippleColorDarkBg = Color.argb(Color.alpha(color3), 255, 255, 255);
        this.mMinStrokeContrast = ContrastColorUtil.calculateContrast(color2, color);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SmartReplyView, 0, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == 1) {
                i2 = obtainStyledAttributes.getDimensionPixelSize(i3, 0);
            } else if (index == 0) {
                i = obtainStyledAttributes.getDimensionPixelSize(i3, 0);
            }
        }
        obtainStyledAttributes.recycle();
        this.mStrokeWidth = i;
        this.mSpacing = i2;
        this.mBreakIterator = BreakIterator.getLineInstance();
        setBackgroundTintColor(this.mDefaultBackgroundColor, false);
        this.mCandidateButtonQueueForSqueezing = new PriorityQueue<>(Math.max(getChildCount(), 1), DECREASING_MEASURED_WIDTH_WITHOUT_PADDING_COMPARATOR);
    }

    public static void markButtonsWithPendingSqueezeStatusAs(int i, ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LayoutParams layoutParams = (LayoutParams) ((View) it.next()).getLayoutParams();
            if (layoutParams.squeezeStatus == 1) {
                layoutParams.squeezeStatus = i;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mLastDispatchDrawTime = SystemClock.elapsedRealtime();
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        if (!((LayoutParams) view.getLayoutParams()).show) {
            return false;
        }
        this.mLastDrawChildTime = SystemClock.elapsedRealtime();
        return super.drawChild(canvas, view, j);
    }

    public final void dump(IndentingPrintWriter indentingPrintWriter) {
        float f;
        float f2;
        indentingPrintWriter.println(this);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("mMaxSqueezeRemeasureAttempts=");
        indentingPrintWriter.println(this.mMaxSqueezeRemeasureAttempts);
        indentingPrintWriter.print("mTotalSqueezeRemeasureAttempts=");
        indentingPrintWriter.println(this.mTotalSqueezeRemeasureAttempts);
        indentingPrintWriter.print("mMaxNumActions=");
        indentingPrintWriter.println(this.mMaxNumActions);
        indentingPrintWriter.print("mSmartRepliesGeneratedByAssistant=");
        indentingPrintWriter.println(this.mSmartRepliesGeneratedByAssistant);
        indentingPrintWriter.print("mMinNumSystemGeneratedReplies=");
        indentingPrintWriter.println(this.mMinNumSystemGeneratedReplies);
        indentingPrintWriter.print("mHeightUpperLimit=");
        indentingPrintWriter.println(this.mHeightUpperLimit);
        indentingPrintWriter.print("mDidHideSystemReplies=");
        indentingPrintWriter.println(this.mDidHideSystemReplies);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        indentingPrintWriter.print("lastMeasureAge (s)=");
        long j = this.mLastMeasureTime;
        float f3 = Float.NaN;
        if (j == 0) {
            f = Float.NaN;
        } else {
            f = ((float) (elapsedRealtime - j)) / 1000.0f;
        }
        indentingPrintWriter.println(f);
        indentingPrintWriter.print("lastDrawChildAge (s)=");
        long j2 = this.mLastDrawChildTime;
        if (j2 == 0) {
            f2 = Float.NaN;
        } else {
            f2 = ((float) (elapsedRealtime - j2)) / 1000.0f;
        }
        indentingPrintWriter.println(f2);
        indentingPrintWriter.print("lastDispatchDrawAge (s)=");
        long j3 = this.mLastDispatchDrawTime;
        if (j3 != 0) {
            f3 = ((float) (elapsedRealtime - j3)) / 1000.0f;
        }
        indentingPrintWriter.println(f3);
        int childCount = getChildCount();
        indentingPrintWriter.print("children: num=");
        indentingPrintWriter.println(childCount);
        indentingPrintWriter.increaseIndent();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            indentingPrintWriter.print("[");
            indentingPrintWriter.print(i);
            indentingPrintWriter.print("] type=");
            indentingPrintWriter.print(layoutParams.mButtonType);
            indentingPrintWriter.print(" squeezeStatus=");
            indentingPrintWriter.print(layoutParams.squeezeStatus);
            indentingPrintWriter.print(" show=");
            indentingPrintWriter.print(layoutParams.show);
            indentingPrintWriter.print(" noShowReason=");
            indentingPrintWriter.print(layoutParams.mNoShowReason);
            indentingPrintWriter.print(" view=");
            indentingPrintWriter.println(childAt);
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    @Override // android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams.width, layoutParams.height);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        boolean z2 = true;
        if (getLayoutDirection() != 1) {
            z2 = false;
        }
        int i7 = i3 - i;
        if (z2) {
            i5 = i7 - ((ViewGroup) this).mPaddingRight;
        } else {
            i5 = ((ViewGroup) this).mPaddingLeft;
        }
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (((LayoutParams) childAt.getLayoutParams()).show) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (z2) {
                    i6 = i5 - measuredWidth;
                } else {
                    i6 = i5;
                }
                childAt.layout(i6, 0, i6 + measuredWidth, measuredHeight);
                int i9 = measuredWidth + this.mSpacing;
                if (z2) {
                    i5 -= i9;
                } else {
                    i5 += i9;
                }
            }
        }
    }

    public final void setButtonColors(Button button) {
        Drawable background = button.getBackground();
        if (background instanceof RippleDrawable) {
            Drawable mutate = background.mutate();
            RippleDrawable rippleDrawable = (RippleDrawable) mutate;
            rippleDrawable.setColor(ColorStateList.valueOf(this.mCurrentRippleColor));
            Drawable drawable = rippleDrawable.getDrawable(0);
            if (drawable instanceof InsetDrawable) {
                Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
                if (drawable2 instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) drawable2;
                    gradientDrawable.setColor(this.mCurrentBackgroundColor);
                    gradientDrawable.setStroke(this.mStrokeWidth, this.mCurrentStrokeColor);
                }
            }
            button.setBackground(mutate);
        }
        button.setTextColor(this.mCurrentTextColor);
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public boolean show = false;
        public int squeezeStatus = 0;
        public SmartButtonType mButtonType = SmartButtonType.REPLY;
        public String mNoShowReason = "new";

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        @VisibleForTesting
        public boolean isShown() {
            return this.show;
        }
    }
}
