package com.android.keyguard;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.internal.widget.LockPatternView;
import com.android.keyguard.KeyguardInputView;
import com.android.settingslib.animation.AppearAnimationCreator;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.qs.QSFooterView$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardPatternView extends KeyguardInputView implements AppearAnimationCreator<LockPatternView.CellState> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AppearAnimationUtils mAppearAnimationUtils;
    public ConstraintLayout mContainer;
    public final DisappearAnimationUtils mDisappearAnimationUtils;
    public final DisappearAnimationUtils mDisappearAnimationUtilsLocked;
    public View mEcaView;
    public long mLastPokeTime;
    public final Rect mLockPatternScreenBounds;
    public LockPatternView mLockPatternView;
    public KeyguardMessageArea mSecurityMessageDisplay;
    public final Rect mTempRect;
    public final int[] mTmpPosition;

    public KeyguardPatternView(Context context) {
        this(context, null);
    }

    @Override // com.android.settingslib.animation.AppearAnimationCreator
    public final void createAnimation(LockPatternView.CellState cellState, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable) {
        float f2;
        float f3;
        float f4;
        float f5;
        LockPatternView.CellState cellState2 = cellState;
        LockPatternView lockPatternView = this.mLockPatternView;
        if (z) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        if (z) {
            f3 = f;
        } else {
            f3 = 0.0f;
        }
        if (z) {
            f4 = 0.0f;
        } else {
            f4 = f;
        }
        if (z) {
            f5 = 0.0f;
        } else {
            f5 = 1.0f;
        }
        lockPatternView.startCellStateAnimation(cellState2, 1.0f, f2, f3, f4, f5, 1.0f, j, j2, interpolator, runnable);
        if (runnable != null) {
            AppearAnimationUtils appearAnimationUtils = this.mAppearAnimationUtils;
            View view = this.mEcaView;
            Objects.requireNonNull(appearAnimationUtils);
            AppearAnimationUtils.createAnimation2(view, j, j2, f, z, interpolator, (Runnable) null);
        }
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final void startAppearAnimation() {
        enableClipping(false);
        setAlpha(1.0f);
        AppearAnimationUtils appearAnimationUtils = this.mAppearAnimationUtils;
        Objects.requireNonNull(appearAnimationUtils);
        setTranslationY(appearAnimationUtils.mStartTranslation);
        AppearAnimationUtils appearAnimationUtils2 = this.mAppearAnimationUtils;
        Objects.requireNonNull(appearAnimationUtils2);
        AppearAnimationUtils.startTranslationYAnimation(this, 0L, 500L, 0.0f, appearAnimationUtils2.mInterpolator, new KeyguardInputView.AnonymousClass1(18));
        this.mAppearAnimationUtils.startAnimation2d(this.mLockPatternView.getCellStates(), new QSFooterView$$ExternalSyntheticLambda0(this, 1), this);
        if (!TextUtils.isEmpty(this.mSecurityMessageDisplay.getText())) {
            AppearAnimationUtils appearAnimationUtils3 = this.mAppearAnimationUtils;
            KeyguardMessageArea keyguardMessageArea = this.mSecurityMessageDisplay;
            Objects.requireNonNull(appearAnimationUtils3);
            float f = appearAnimationUtils3.mStartTranslation;
            AppearAnimationUtils appearAnimationUtils4 = this.mAppearAnimationUtils;
            Objects.requireNonNull(appearAnimationUtils4);
            AppearAnimationUtils.createAnimation2((View) keyguardMessageArea, 0L, 220L, f, true, appearAnimationUtils4.mInterpolator, (Runnable) null);
        }
    }

    public KeyguardPatternView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTmpPosition = new int[2];
        this.mTempRect = new Rect();
        this.mLockPatternScreenBounds = new Rect();
        this.mLastPokeTime = -7000L;
        this.mAppearAnimationUtils = new AppearAnimationUtils(context, 220L, 1.5f, 2.0f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563662));
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 125L, 1.2f, 0.6f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
        this.mDisappearAnimationUtilsLocked = new DisappearAnimationUtils(context, 187L, 1.2f, 0.6f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final boolean disallowInterceptTouch(MotionEvent motionEvent) {
        if (!this.mLockPatternView.isEmpty() || this.mLockPatternScreenBounds.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
            return true;
        }
        return false;
    }

    public final void onDevicePostureChanged(int i) {
        float f = ((LinearLayout) this).mContext.getResources().getFloat(2131165793);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mContainer);
        if (i != 2) {
            f = 0.0f;
        }
        constraintSet.get(2131428575).layout.guidePercent = f;
        constraintSet.get(2131428575).layout.guideEnd = -1;
        constraintSet.get(2131428575).layout.guideBegin = -1;
        constraintSet.applyTo(this.mContainer);
    }

    public final void enableClipping(boolean z) {
        setClipChildren(z);
        this.mContainer.setClipToPadding(z);
        this.mContainer.setClipChildren(z);
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final String getTitle() {
        return getResources().getString(17040503);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mSecurityMessageDisplay = KeyguardMessageArea.findSecurityMessageDisplay(this);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternView = findViewById(2131428273);
        this.mEcaView = findViewById(2131428189);
        this.mContainer = (ConstraintLayout) findViewById(2131428574);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mLockPatternView.getLocationOnScreen(this.mTmpPosition);
        Rect rect = this.mLockPatternScreenBounds;
        int[] iArr = this.mTmpPosition;
        rect.set(iArr[0] - 40, iArr[1] - 40, this.mLockPatternView.getWidth() + iArr[0] + 40, this.mLockPatternView.getHeight() + this.mTmpPosition[1] + 40);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastPokeTime;
        if (onTouchEvent && elapsedRealtime > 6900) {
            this.mLastPokeTime = SystemClock.elapsedRealtime();
        }
        boolean z = false;
        this.mTempRect.set(0, 0, 0, 0);
        offsetRectIntoDescendantCoords(this.mLockPatternView, this.mTempRect);
        Rect rect = this.mTempRect;
        motionEvent.offsetLocation(rect.left, rect.top);
        if (this.mLockPatternView.dispatchTouchEvent(motionEvent) || onTouchEvent) {
            z = true;
        }
        Rect rect2 = this.mTempRect;
        motionEvent.offsetLocation(-rect2.left, -rect2.top);
        return z;
    }

    static {
        boolean z = KeyguardConstants.DEBUG;
    }
}
