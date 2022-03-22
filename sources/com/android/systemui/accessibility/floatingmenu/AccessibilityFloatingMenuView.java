package com.android.systemui.accessibility.floatingmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.internal.accessibility.dialog.AccessibilityTarget;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda1;
import com.android.wm.shell.common.ExecutorUtils$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class AccessibilityFloatingMenuView extends FrameLayout implements RecyclerView.OnItemTouchListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AccessibilityTargetAdapter mAdapter;
    public int mAlignment;
    @VisibleForTesting
    public final WindowManager.LayoutParams mCurrentLayoutParams;
    public int mDisplayHeight;
    public int mDisplayWidth;
    public int mDownX;
    public int mDownY;
    @VisibleForTesting
    public final ValueAnimator mDragAnimator;
    public final ValueAnimator mFadeOutAnimator;
    public float mFadeOutValue;
    public int mIconHeight;
    public int mIconWidth;
    public int mInset;
    public boolean mIsDownInEnlargedTouchArea;
    public boolean mIsFadeEffectEnabled;
    public boolean mIsShowing;
    public final Configuration mLastConfiguration;
    public final RecyclerView mListView;
    public int mMargin;
    public int mPadding;
    public final Position mPosition;
    public float mRadius;
    public int mRadiusType;
    public int mRelativeToPointerDownX;
    public int mRelativeToPointerDownY;
    public float mSquareScaledTouchSlop;
    public final ArrayList mTargets;
    public int mTemporaryShapeType;
    public final Handler mUiHandler;
    public final WindowManager mWindowManager;
    public boolean mIsDragging = false;
    public int mSizeType = 0;
    @VisibleForTesting
    public int mShapeType = 0;
    public final Rect mDisplayInsetsRect = new Rect();
    public final Rect mImeInsetsRect = new Rect();
    public Optional<OnDragEndListener> mOnDragEndListener = Optional.empty();

    /* renamed from: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            AccessibilityFloatingMenuView accessibilityFloatingMenuView;
            float f;
            AccessibilityFloatingMenuView accessibilityFloatingMenuView2;
            int i;
            Position position = AccessibilityFloatingMenuView.this.mPosition;
            if (accessibilityFloatingMenuView.mCurrentLayoutParams.x / accessibilityFloatingMenuView.getMaxWindowX() < 0.5d) {
                f = 0.0f;
            } else {
                f = 1.0f;
            }
            Objects.requireNonNull(AccessibilityFloatingMenuView.this);
            Objects.requireNonNull(position);
            position.mPercentageX = f;
            position.mPercentageY = accessibilityFloatingMenuView2.mCurrentLayoutParams.y / (accessibilityFloatingMenuView2.mDisplayHeight - accessibilityFloatingMenuView2.getWindowHeight());
            AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = AccessibilityFloatingMenuView.this;
            Position position2 = accessibilityFloatingMenuView3.mPosition;
            Objects.requireNonNull(position2);
            int i2 = 0;
            if (position2.mPercentageX < 0.5f) {
                i = 0;
            } else {
                i = 1;
            }
            accessibilityFloatingMenuView3.mAlignment = i;
            AccessibilityFloatingMenuView accessibilityFloatingMenuView4 = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView4.updateLocationWith(accessibilityFloatingMenuView4.mPosition);
            AccessibilityFloatingMenuView accessibilityFloatingMenuView5 = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView5.updateInsetWith(accessibilityFloatingMenuView5.getResources().getConfiguration().uiMode, AccessibilityFloatingMenuView.this.mAlignment);
            AccessibilityFloatingMenuView accessibilityFloatingMenuView6 = AccessibilityFloatingMenuView.this;
            if (accessibilityFloatingMenuView6.mAlignment != 1) {
                i2 = 2;
            }
            accessibilityFloatingMenuView6.mRadiusType = i2;
            accessibilityFloatingMenuView6.updateRadiusWith(accessibilityFloatingMenuView6.mSizeType, i2, accessibilityFloatingMenuView6.mTargets.size());
            AccessibilityFloatingMenuView.this.fadeOut();
            AccessibilityFloatingMenuView.this.mOnDragEndListener.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda1(this, 1));
        }
    }

    /* loaded from: classes.dex */
    public interface OnDragEndListener {
        void onDragEnd(Position position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public final void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override // android.view.View, androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public final void onTouchEvent(MotionEvent motionEvent) {
    }

    @VisibleForTesting
    public void fadeIn() {
        if (this.mIsFadeEffectEnabled) {
            this.mFadeOutAnimator.cancel();
            this.mUiHandler.removeCallbacksAndMessages(null);
            this.mUiHandler.post(new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 4));
        }
    }

    @VisibleForTesting
    public void fadeOut() {
        if (this.mIsFadeEffectEnabled) {
            this.mUiHandler.postDelayed(new ScrimView$$ExternalSyntheticLambda0(this, 3), 3000L);
        }
    }

    @VisibleForTesting
    public Rect getAvailableBounds() {
        return new Rect(0, 0, this.mDisplayWidth - (((this.mPadding * 2) + this.mIconWidth) + (getMarginStartEndWith(this.mLastConfiguration) * 2)), this.mDisplayHeight - getWindowHeight());
    }

    public final int getInterval() {
        Position position = this.mPosition;
        Objects.requireNonNull(position);
        int i = this.mDisplayHeight - this.mImeInsetsRect.bottom;
        int windowHeight = getWindowHeight() + ((int) (position.mPercentageY * (this.mDisplayHeight - getWindowHeight())));
        if (windowHeight > i) {
            return windowHeight - i;
        }
        return 0;
    }

    public final int getMarginStartEndWith(Configuration configuration) {
        if (configuration == null || configuration.orientation != 1) {
            return 0;
        }
        return this.mMargin;
    }

    public final int getMaxWindowX() {
        return (this.mDisplayWidth - getMarginStartEndWith(this.mLastConfiguration)) - ((this.mPadding * 2) + this.mIconWidth);
    }

    public final int getWindowHeight() {
        int i = this.mDisplayHeight;
        int i2 = this.mMargin;
        return Math.min(i, Math.min(i - (i2 * 2), (this.mTargets.size() * (this.mPadding + this.mIconHeight)) + this.mPadding) + (i2 * 2));
    }

    @VisibleForTesting
    public boolean hasExceededMaxLayoutHeight() {
        if ((this.mTargets.size() * (this.mPadding + this.mIconHeight)) + this.mPadding > this.mDisplayHeight - (this.mMargin * 2)) {
            return true;
        }
        return false;
    }

    public final void setRadius(float f, int i) {
        float[] fArr;
        GradientDrawable gradientDrawable = (GradientDrawable) ((InstantInsetLayerDrawable) this.mListView.getBackground()).getDrawable(0);
        if (i == 0) {
            fArr = new float[]{f, f, 0.0f, 0.0f, 0.0f, 0.0f, f, f};
        } else {
            fArr = i == 2 ? new float[]{0.0f, 0.0f, f, f, f, f, 0.0f, 0.0f} : new float[]{f, f, f, f, f, f, f, f};
        }
        gradientDrawable.setCornerRadii(fArr);
    }

    public final void setSystemGestureExclusion() {
        post(new ExecutorUtils$$ExternalSyntheticLambda1(this, new Rect(0, 0, (this.mPadding * 2) + this.mIconWidth + (getMarginStartEndWith(this.mLastConfiguration) * 2), getWindowHeight()), 1));
    }

    @VisibleForTesting
    public void snapToLocation(final int i, final int i2) {
        this.mDragAnimator.cancel();
        this.mDragAnimator.removeAllUpdateListeners();
        this.mDragAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenuView.this;
                int i3 = i;
                int i4 = i2;
                Objects.requireNonNull(accessibilityFloatingMenuView);
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                float f = 1.0f - floatValue;
                WindowManager.LayoutParams layoutParams = accessibilityFloatingMenuView.mCurrentLayoutParams;
                float f2 = i3 * floatValue;
                layoutParams.x = (int) (f2 + (layoutParams.x * f));
                layoutParams.y = (int) ((floatValue * i4) + (f * layoutParams.y));
                accessibilityFloatingMenuView.mWindowManager.updateViewLayout(accessibilityFloatingMenuView, layoutParams);
            }
        });
        this.mDragAnimator.start();
    }

    public final void updateInsetWith(int i, int i2) {
        boolean z;
        int i3;
        int i4;
        int i5;
        if ((i & 48) == 32) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i3 = this.mInset;
        } else {
            i3 = 0;
        }
        if (i2 == 0) {
            i4 = i3;
        } else {
            i4 = 0;
        }
        if (i2 == 1) {
            i5 = i3;
        } else {
            i5 = 0;
        }
        InstantInsetLayerDrawable instantInsetLayerDrawable = (InstantInsetLayerDrawable) this.mListView.getBackground();
        if (instantInsetLayerDrawable.getLayerInsetLeft(0) != i4 || instantInsetLayerDrawable.getLayerInsetRight(0) != i5) {
            instantInsetLayerDrawable.setLayerInset(0, i4, 0, i5, 0);
        }
    }

    public final void updateOpacityWith(boolean z, float f) {
        this.mIsFadeEffectEnabled = z;
        this.mFadeOutValue = f;
        this.mFadeOutAnimator.cancel();
        float f2 = 1.0f;
        this.mFadeOutAnimator.setFloatValues(1.0f, this.mFadeOutValue);
        if (this.mIsFadeEffectEnabled) {
            f2 = this.mFadeOutValue;
        }
        setAlpha(f2);
    }

    @VisibleForTesting
    public AccessibilityFloatingMenuView(Context context, Position position, RecyclerView recyclerView) {
        super(context);
        int i;
        int i2;
        int i3;
        ArrayList arrayList = new ArrayList();
        this.mTargets = arrayList;
        this.mListView = recyclerView;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        Configuration configuration = new Configuration(getResources().getConfiguration());
        this.mLastConfiguration = configuration;
        AccessibilityTargetAdapter accessibilityTargetAdapter = new AccessibilityTargetAdapter(arrayList);
        this.mAdapter = accessibilityTargetAdapter;
        Looper myLooper = Looper.myLooper();
        Objects.requireNonNull(myLooper, "looper must not be null");
        this.mUiHandler = new Handler(myLooper);
        this.mPosition = position;
        Objects.requireNonNull(position);
        if (position.mPercentageX < 0.5f) {
            i = 0;
        } else {
            i = 1;
        }
        this.mAlignment = i;
        if (i == 1) {
            i2 = 0;
        } else {
            i2 = 2;
        }
        this.mRadiusType = i2;
        updateDimensions();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2024, 520, -3);
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.privateFlags |= 2097152;
        layoutParams.windowAnimations = 16973827;
        layoutParams.gravity = 8388659;
        if (this.mAlignment == 1) {
            i3 = getMaxWindowX();
        } else {
            i3 = -getMarginStartEndWith(configuration);
        }
        layoutParams.x = i3;
        layoutParams.y = Math.max(0, ((int) (position.mPercentageY * (this.mDisplayHeight - getWindowHeight()))) - getInterval());
        layoutParams.accessibilityTitle = getResources().getString(17039577);
        this.mCurrentLayoutParams = layoutParams;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, this.mFadeOutValue);
        this.mFadeOutAnimator = ofFloat;
        ofFloat.setDuration(1000L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenuView.this;
                Objects.requireNonNull(accessibilityFloatingMenuView);
                accessibilityFloatingMenuView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mDragAnimator = ofFloat2;
        ofFloat2.setDuration(150L);
        ofFloat2.setInterpolator(new OvershootInterpolator());
        ofFloat2.addListener(new AnonymousClass1());
        Drawable drawable = getContext().getDrawable(2131231587);
        getContext();
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(1);
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        recyclerView.setBackground(new InstantInsetLayerDrawable(new Drawable[]{drawable}));
        recyclerView.setAdapter(accessibilityTargetAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.mOnItemTouchListeners.add(this);
        recyclerView.animate().setInterpolator(new OvershootInterpolator());
        RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate = new RecyclerViewAccessibilityDelegate(recyclerView) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.2
            @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
            public final AccessibilityDelegateCompat getItemDelegate() {
                return new ItemDelegateCompat(this, AccessibilityFloatingMenuView.this);
            }
        };
        recyclerView.mAccessibilityDelegate = recyclerViewAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(recyclerView, recyclerViewAccessibilityDelegate);
        updateListViewWith(configuration);
        addView(recyclerView);
        updateStrokeWith(getResources().getConfiguration().uiMode, this.mAlignment);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mLastConfiguration.setTo(configuration);
        if ((configuration.diff(this.mLastConfiguration) & 4) != 0) {
            this.mCurrentLayoutParams.accessibilityTitle = getResources().getString(17039577);
        }
        updateDimensions();
        updateListViewWith(configuration);
        updateItemViewDimensionsWith(this.mSizeType);
        AccessibilityTargetAdapter accessibilityTargetAdapter = this.mAdapter;
        int i = this.mPadding;
        Objects.requireNonNull(accessibilityTargetAdapter);
        accessibilityTargetAdapter.mItemPadding = i;
        AccessibilityTargetAdapter accessibilityTargetAdapter2 = this.mAdapter;
        int i2 = this.mIconWidth;
        Objects.requireNonNull(accessibilityTargetAdapter2);
        accessibilityTargetAdapter2.mIconWidthHeight = i2;
        this.mAdapter.notifyDataSetChanged();
        int i3 = 0;
        ((GradientDrawable) ((InstantInsetLayerDrawable) this.mListView.getBackground()).getDrawable(0)).setColor(getResources().getColor(2131099692));
        updateStrokeWith(configuration.uiMode, this.mAlignment);
        updateLocationWith(this.mPosition);
        updateRadiusWith(this.mSizeType, this.mRadiusType, this.mTargets.size());
        boolean hasExceededMaxLayoutHeight = hasExceededMaxLayoutHeight();
        RecyclerView recyclerView = this.mListView;
        if (!hasExceededMaxLayoutHeight) {
            i3 = 2;
        }
        recyclerView.setOverScrollMode(i3);
        setSystemGestureExclusion();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x003c, code lost:
        if (r12 != false) goto L_0x003e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r12 != 3) goto L_0x00fb;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent$1(android.view.MotionEvent r12) {
        /*
            Method dump skipped, instructions count: 252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.onInterceptTouchEvent$1(android.view.MotionEvent):boolean");
    }

    public final void onTargetsChanged(List<AccessibilityTarget> list) {
        int i;
        fadeIn();
        this.mTargets.clear();
        this.mTargets.addAll(list);
        this.mAdapter.notifyDataSetChanged();
        updateRadiusWith(this.mSizeType, this.mRadiusType, this.mTargets.size());
        boolean hasExceededMaxLayoutHeight = hasExceededMaxLayoutHeight();
        RecyclerView recyclerView = this.mListView;
        if (hasExceededMaxLayoutHeight) {
            i = 0;
        } else {
            i = 2;
        }
        recyclerView.setOverScrollMode(i);
        setSystemGestureExclusion();
        fadeOut();
    }

    public final void setShapeType(int i) {
        View.OnTouchListener onTouchListener;
        fadeIn();
        this.mShapeType = i;
        int i2 = this.mAlignment;
        float f = ((this.mPadding * 2) + this.mIconWidth) / 2.0f;
        if (i == 0) {
            f = 0.0f;
        }
        ViewPropertyAnimator animate = this.mListView.animate();
        if (i2 != 1) {
            f = -f;
        }
        animate.translationX(f);
        if (i == 0) {
            onTouchListener = null;
        } else {
            onTouchListener = new View.OnTouchListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda3
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenuView.this;
                    Objects.requireNonNull(accessibilityFloatingMenuView);
                    int action = motionEvent.getAction();
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    int marginStartEndWith = accessibilityFloatingMenuView.getMarginStartEndWith(accessibilityFloatingMenuView.mLastConfiguration);
                    int i3 = accessibilityFloatingMenuView.mMargin;
                    int i4 = accessibilityFloatingMenuView.mPadding;
                    Rect rect = new Rect(marginStartEndWith, i3, (i4 * 2) + accessibilityFloatingMenuView.mIconWidth + marginStartEndWith, Math.min(accessibilityFloatingMenuView.mDisplayHeight - (i3 * 2), (accessibilityFloatingMenuView.mTargets.size() * (i4 + accessibilityFloatingMenuView.mIconHeight)) + accessibilityFloatingMenuView.mPadding) + i3);
                    if (action == 0 && rect.contains(x, y)) {
                        accessibilityFloatingMenuView.mIsDownInEnlargedTouchArea = true;
                    }
                    if (!accessibilityFloatingMenuView.mIsDownInEnlargedTouchArea) {
                        return false;
                    }
                    if (action == 1 || action == 3) {
                        accessibilityFloatingMenuView.mIsDownInEnlargedTouchArea = false;
                    }
                    int i5 = accessibilityFloatingMenuView.mMargin;
                    motionEvent.setLocation(x - i5, y - i5);
                    return accessibilityFloatingMenuView.mListView.dispatchTouchEvent(motionEvent);
                }
            };
        }
        setOnTouchListener(onTouchListener);
        fadeOut();
    }

    public final void setSizeType(int i) {
        int i2;
        fadeIn();
        this.mSizeType = i;
        updateItemViewDimensionsWith(i);
        AccessibilityTargetAdapter accessibilityTargetAdapter = this.mAdapter;
        int i3 = this.mPadding;
        Objects.requireNonNull(accessibilityTargetAdapter);
        accessibilityTargetAdapter.mItemPadding = i3;
        AccessibilityTargetAdapter accessibilityTargetAdapter2 = this.mAdapter;
        int i4 = this.mIconWidth;
        Objects.requireNonNull(accessibilityTargetAdapter2);
        accessibilityTargetAdapter2.mIconWidthHeight = i4;
        this.mAdapter.notifyDataSetChanged();
        updateRadiusWith(i, this.mRadiusType, this.mTargets.size());
        updateLocationWith(this.mPosition);
        boolean hasExceededMaxLayoutHeight = hasExceededMaxLayoutHeight();
        RecyclerView recyclerView = this.mListView;
        if (hasExceededMaxLayoutHeight) {
            i2 = 0;
        } else {
            i2 = 2;
        }
        recyclerView.setOverScrollMode(i2);
        int i5 = this.mShapeType;
        int i6 = this.mAlignment;
        float f = ((this.mPadding * 2) + this.mIconWidth) / 2.0f;
        if (i5 == 0) {
            f = 0.0f;
        }
        ViewPropertyAnimator animate = this.mListView.animate();
        if (i6 != 1) {
            f = -f;
        }
        animate.translationX(f);
        setSystemGestureExclusion();
        fadeOut();
    }

    public final void updateDimensions() {
        Resources resources = getResources();
        updateDisplaySizeWith(this.mWindowManager.getCurrentWindowMetrics());
        this.mMargin = resources.getDimensionPixelSize(2131165302);
        this.mInset = resources.getDimensionPixelSize(2131165307);
        this.mSquareScaledTouchSlop = MathUtils.sq(ViewConfiguration.get(getContext()).getScaledTouchSlop());
        updateItemViewDimensionsWith(this.mSizeType);
    }

    public final void updateDisplaySizeWith(WindowMetrics windowMetrics) {
        Rect bounds = windowMetrics.getBounds();
        Insets insetsIgnoringVisibility = windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
        this.mDisplayInsetsRect.set(insetsIgnoringVisibility.toRect());
        bounds.inset(insetsIgnoringVisibility);
        this.mDisplayWidth = bounds.width();
        this.mDisplayHeight = bounds.height();
    }

    public final void updateItemViewDimensionsWith(int i) {
        int i2;
        int i3;
        Resources resources = getResources();
        if (i == 0) {
            i2 = 2131165304;
        } else {
            i2 = 2131165299;
        }
        this.mPadding = resources.getDimensionPixelSize(i2);
        if (i == 0) {
            i3 = 2131165306;
        } else {
            i3 = 2131165301;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(i3);
        this.mIconWidth = dimensionPixelSize;
        this.mIconHeight = dimensionPixelSize;
    }

    public final void updateListViewWith(Configuration configuration) {
        int marginStartEndWith = getMarginStartEndWith(configuration);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mListView.getLayoutParams();
        int i = this.mMargin;
        layoutParams.setMargins(marginStartEndWith, i, marginStartEndWith, i);
        this.mListView.setLayoutParams(layoutParams);
        this.mListView.setElevation(getResources().getDimensionPixelSize(2131165297));
    }

    public final void updateLocationWith(Position position) {
        boolean z;
        int i;
        Objects.requireNonNull(position);
        if (position.mPercentageX < 0.5f) {
            z = false;
        } else {
            z = true;
        }
        WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
        if (z) {
            i = getMaxWindowX();
        } else {
            i = -getMarginStartEndWith(this.mLastConfiguration);
        }
        layoutParams.x = i;
        this.mCurrentLayoutParams.y = Math.max(0, ((int) (position.mPercentageY * (this.mDisplayHeight - getWindowHeight()))) - getInterval());
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    public final void updateRadiusWith(int i, int i2, int i3) {
        int i4;
        Resources resources = getResources();
        if (i == 0) {
            if (i3 > 1) {
                i4 = 2131165303;
            } else {
                i4 = 2131165305;
            }
        } else if (i3 > 1) {
            i4 = 2131165298;
        } else {
            i4 = 2131165300;
        }
        float dimensionPixelSize = resources.getDimensionPixelSize(i4);
        this.mRadius = dimensionPixelSize;
        setRadius(dimensionPixelSize, i2);
    }

    public final void updateStrokeWith(int i, int i2) {
        boolean z;
        updateInsetWith(i, i2);
        if ((i & 48) == 32) {
            z = true;
        } else {
            z = false;
        }
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(2131165308);
        if (!z) {
            dimensionPixelSize = 0;
        }
        ((GradientDrawable) ((InstantInsetLayerDrawable) this.mListView.getBackground()).getDrawable(0)).setStroke(dimensionPixelSize, resources.getColor(2131099693));
    }
}
