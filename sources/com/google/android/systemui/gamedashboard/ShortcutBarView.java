package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.animation.PhysicsAnimatorKt;
import com.google.android.systemui.gamedashboard.RevealButton;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public class ShortcutBarView extends FrameLayout implements ViewTreeObserver.OnComputeInternalInsetsListener, ConfigurationController.ConfigurationListener, RecordingController.RecordingStateChangeCallback {
    public static final int SHORTCUT_BAR_BACKGROUND_COLOR = Color.parseColor("#99000000");
    public ShortcutBarContainer mBar;
    public ConfigurationController mConfigurationController;
    public ShortcutBarButton mEntryPointButton;
    public Consumer<Rect> mExcludeBackRegionCallback;
    public TextView mFpsView;
    public Insets mInsets;
    public boolean mIsAttached;
    public boolean mIsEntryPointVisible;
    public boolean mIsFpsVisible;
    public boolean mIsRecordVisible;
    public boolean mIsScreenshotVisible;
    public ShortcutBarButton mRecordButton;
    public RevealButton mRevealButton;
    public ScreenRecordController mScreenRecordController;
    public ShortcutBarButton mScreenshotButton;
    public int mShiftForTransientBar;
    public GameDashboardUiEventLogger mUiEventLogger;
    public int mOrientation = -1;
    public boolean mIsDockingAnimationRunning = false;
    public boolean mIsDragging = false;
    public final int[] mTempInts = new int[2];
    public final Rect mTmpRect = new Rect();
    public final ShortcutBarView$$ExternalSyntheticLambda3 mSystemGestureExcludeUpdater = new ViewTreeObserver.OnDrawListener() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda3
        @Override // android.view.ViewTreeObserver.OnDrawListener
        public final void onDraw() {
            ShortcutBarView shortcutBarView = ShortcutBarView.this;
            int i = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
            Objects.requireNonNull(shortcutBarView);
            if (shortcutBarView.mExcludeBackRegionCallback != null) {
                if (shortcutBarView.mIsAttached) {
                    shortcutBarView.getTouchableRegion();
                } else {
                    shortcutBarView.mTmpRect.setEmpty();
                }
                shortcutBarView.mExcludeBackRegionCallback.accept(shortcutBarView.mTmpRect);
            }
        }
    };
    public final AnonymousClass1 mOnTouchListener = new ShortcutBarTouchController() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.1
        public final PhysicsAnimator.SpringConfig mSpringConfig = new PhysicsAnimator.SpringConfig(700.0f, 1.0f);

        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController
        public final View getView() {
            return ShortcutBarView.this.mBar;
        }

        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController
        public final void onMove(float f, float f2) {
            ShortcutBarView.this.mBar.setTranslationX(f);
            ShortcutBarView.this.mBar.setTranslationY(f2);
            if (ShortcutBarView.this.mBar.getX() < 0.0f) {
                ShortcutBarView shortcutBarView = ShortcutBarView.this;
                if (shortcutBarView.mInsets.left == 0) {
                    ShortcutBarView.m170$$Nest$mdock(shortcutBarView, false);
                    return;
                }
            }
            if (ShortcutBarView.this.mBar.getX() + ShortcutBarView.this.mBar.getWidth() > ShortcutBarView.this.getWidth()) {
                ShortcutBarView shortcutBarView2 = ShortcutBarView.this;
                if (shortcutBarView2.mInsets.right == 0) {
                    ShortcutBarView.m170$$Nest$mdock(shortcutBarView2, true);
                }
            }
        }

        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController
        public final void onUp(float f, float f2) {
            ShortcutBarView shortcutBarView;
            ShortcutBarView shortcutBarView2;
            ShortcutBarView shortcutBarView3 = ShortcutBarView.this;
            if (!shortcutBarView3.mIsDockingAnimationRunning) {
                ShortcutBarContainer shortcutBarContainer = shortcutBarView3.mBar;
                Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(shortcutBarContainer);
                DynamicAnimation.AnonymousClass1 r3 = DynamicAnimation.TRANSLATION_X;
                ShortcutBarView shortcutBarView4 = ShortcutBarView.this;
                int i = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                Objects.requireNonNull(shortcutBarView4);
                float f3 = shortcutBarView4.mInsets.left + shortcutBarView4.mBarMarginEnd;
                Objects.requireNonNull(ShortcutBarView.this);
                instance.flingThenSpring(r3, f, new PhysicsAnimator.FlingConfig(1.9f, f3, ((shortcutBarView.getWidth() - shortcutBarView.mBarMarginEnd) - shortcutBarView.mInsets.right) - shortcutBarView.mBar.getWidth(), 0.0f), this.mSpringConfig, true);
                DynamicAnimation.AnonymousClass2 r10 = DynamicAnimation.TRANSLATION_Y;
                Objects.requireNonNull(ShortcutBarView.this);
                ShortcutBarView shortcutBarView5 = ShortcutBarView.this;
                Objects.requireNonNull(shortcutBarView5);
                float height = (shortcutBarView5.getHeight() - shortcutBarView5.mInsets.bottom) - shortcutBarView5.mBar.getHeight();
                WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
                instance.flingConfigs.put(r10, new PhysicsAnimator.FlingConfig(1.9f, shortcutBarView2.mInsets.top, height, f2));
                instance.endListeners.add(new PhysicsAnimator.EndListener<View>() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.1.1
                    @Override // com.android.wm.shell.animation.PhysicsAnimator.EndListener
                    public final void onAnimationEnd(Object obj, FloatPropertyCompat floatPropertyCompat, boolean z, boolean z2, float f4, float f5) {
                        View view = (View) obj;
                        if (!z2 && floatPropertyCompat == DynamicAnimation.TRANSLATION_Y) {
                            ShortcutBarView shortcutBarView6 = ShortcutBarView.this;
                            int i2 = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                            shortcutBarView6.snapBarBackVertically().start();
                        }
                    }
                });
                instance.start();
            }
        }

        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController, android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            super.onTouch(view, motionEvent);
            int action = motionEvent.getAction();
            if (action == 0) {
                ShortcutBarView.this.mIsDragging = true;
            } else if (action == 1 || action == 3) {
                ShortcutBarView.this.mIsDragging = false;
            }
            return true;
        }
    };
    public final AnonymousClass2 mRevealButtonOnTouchListener = new ShortcutBarTouchController() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.2
        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController
        public final View getView() {
            return ShortcutBarView.this.mRevealButton;
        }

        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController
        public final void onMove(float f, float f2) {
            ShortcutBarView.this.mRevealButton.setTranslationY(f2);
        }

        @Override // com.google.android.systemui.gamedashboard.ShortcutBarView.ShortcutBarTouchController
        public final void onUp(float f, float f2) {
            ShortcutBarView shortcutBarView = ShortcutBarView.this;
            if (!shortcutBarView.mIsDockingAnimationRunning) {
                RevealButton revealButton = shortcutBarView.mRevealButton;
                Objects.requireNonNull(revealButton);
                if (!revealButton.mRightSide || f >= -500.0f) {
                    RevealButton revealButton2 = ShortcutBarView.this.mRevealButton;
                    Objects.requireNonNull(revealButton2);
                    if (revealButton2.mRightSide || f <= 500.0f) {
                        RevealButton revealButton3 = ShortcutBarView.this.mRevealButton;
                        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                        PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(revealButton3);
                        DynamicAnimation.AnonymousClass2 r0 = DynamicAnimation.TRANSLATION_Y;
                        ShortcutBarView shortcutBarView2 = ShortcutBarView.this;
                        int i = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
                        Objects.requireNonNull(shortcutBarView2);
                        ShortcutBarView shortcutBarView3 = ShortcutBarView.this;
                        Objects.requireNonNull(shortcutBarView3);
                        WeakHashMap<Object, PhysicsAnimator<?>> weakHashMap = PhysicsAnimatorKt.animators;
                        instance.flingConfigs.put(r0, new PhysicsAnimator.FlingConfig(1.9f, shortcutBarView2.mInsets.top, (shortcutBarView3.getHeight() - shortcutBarView3.mRevealButtonIconHeight) - shortcutBarView3.mInsets.bottom, f2));
                        instance.start();
                        return;
                    }
                }
                ShortcutBarView shortcutBarView4 = ShortcutBarView.this;
                Objects.requireNonNull(shortcutBarView4);
                shortcutBarView4.autoUndock(0.0f);
            }
        }
    };
    public final AnonymousClass3 mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.3
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            float f;
            float f2;
            int i9;
            int i10;
            float f3;
            ShortcutBarView shortcutBarView = ShortcutBarView.this;
            int i11 = ShortcutBarView.SHORTCUT_BAR_BACKGROUND_COLOR;
            Objects.requireNonNull(shortcutBarView);
            if (shortcutBarView.mRevealButton.getVisibility() == 0) {
                f = shortcutBarView.mRevealButton.getTranslationX();
            } else {
                f = shortcutBarView.mBar.getTranslationX();
            }
            ShortcutBarView shortcutBarView2 = ShortcutBarView.this;
            Objects.requireNonNull(shortcutBarView2);
            if (shortcutBarView2.mRevealButton.getVisibility() == 0) {
                f2 = shortcutBarView2.mRevealButton.getTranslationY();
            } else {
                f2 = shortcutBarView2.mBar.getTranslationY();
            }
            float f4 = i3 - i;
            float f5 = i7 - i5;
            float f6 = i4 - i2;
            float f7 = i8 - i6;
            ShortcutBarView shortcutBarView3 = ShortcutBarView.this;
            Objects.requireNonNull(shortcutBarView3);
            if (shortcutBarView3.mRevealButton.getVisibility() == 0) {
                i9 = shortcutBarView3.mRevealButton.getWidth();
            } else {
                i9 = shortcutBarView3.mBar.getWidth();
            }
            ShortcutBarView shortcutBarView4 = ShortcutBarView.this;
            Objects.requireNonNull(shortcutBarView4);
            if (shortcutBarView4.mRevealButton.getVisibility() == 0) {
                i10 = shortcutBarView4.mRevealButton.getHeight();
            } else {
                i10 = shortcutBarView4.mBar.getHeight();
            }
            if (ShortcutBarView.this.mRevealButton.getVisibility() == 0) {
                RevealButton revealButton = ShortcutBarView.this.mRevealButton;
                Objects.requireNonNull(revealButton);
                if (revealButton.mRightSide) {
                    f3 = f4 - ShortcutBarView.this.mRevealButtonIconWidth;
                } else {
                    f3 = 0.0f;
                }
            } else {
                float f8 = i9;
                f3 = ((f4 - f8) * f) / (f5 - f8);
            }
            float f9 = i10;
            float f10 = ((f6 - f9) * f2) / (f7 - f9);
            ShortcutBarView shortcutBarView5 = ShortcutBarView.this;
            Objects.requireNonNull(shortcutBarView5);
            if (shortcutBarView5.mRevealButton.getVisibility() == 0) {
                shortcutBarView5.mRevealButton.setTranslationX(f3);
                shortcutBarView5.mRevealButton.setTranslationY(f10);
            } else {
                shortcutBarView5.mBar.setTranslationX(f3);
                shortcutBarView5.mBar.setTranslationY(f10);
            }
            ShortcutBarView.this.snapBarBackIfNecessary();
            ShortcutBarView shortcutBarView6 = ShortcutBarView.this;
            shortcutBarView6.removeOnLayoutChangeListener(shortcutBarView6.mOnLayoutChangeListener);
        }
    };
    public final int mRevealButtonIconHeight = getResources().getDimensionPixelSize(2131165738);
    public final int mRevealButtonIconWidth = getResources().getDimensionPixelSize(2131165739) / 2;
    public final int mBarMarginEnd = getResources().getDimensionPixelSize(2131165737);
    public final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    /* loaded from: classes.dex */
    public abstract class ShortcutBarTouchController implements View.OnTouchListener {
        public final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
        public final PointF mViewPositionOnTouchDown = new PointF();
        public final PointF mTouchDown = new PointF();
        public boolean mHasMoved = false;
        public boolean mCancelled = false;

        public abstract View getView();

        public abstract void onMove(float f, float f2);

        public abstract void onUp(float f, float f2);

        public ShortcutBarTouchController() {
        }

        public final void cancelAnimation() {
            View view = getView();
            Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
            PhysicsAnimator.Companion.getInstance(view).cancel();
            this.mCancelled = true;
            this.mHasMoved = false;
            this.mVelocityTracker.clear();
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float rawX = motionEvent.getRawX() - motionEvent.getX();
            float rawY = motionEvent.getRawY() - motionEvent.getY();
            motionEvent.offsetLocation(rawX, rawY);
            this.mVelocityTracker.addMovement(motionEvent);
            motionEvent.offsetLocation(-rawX, -rawY);
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mTouchDown.set(motionEvent.getRawX(), motionEvent.getRawY());
                this.mViewPositionOnTouchDown.set(getView().getTranslationX(), getView().getTranslationY());
                this.mHasMoved = false;
                this.mCancelled = false;
            } else if (action != 1) {
                if (action != 2) {
                    if (action != 3 || this.mCancelled) {
                        return true;
                    }
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    onUp(this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity());
                    this.mHasMoved = false;
                    this.mVelocityTracker.clear();
                } else if (this.mCancelled) {
                    return true;
                } else {
                    onMove((motionEvent.getRawX() - this.mTouchDown.x) + this.mViewPositionOnTouchDown.x, (motionEvent.getRawY() - this.mTouchDown.y) + this.mViewPositionOnTouchDown.y);
                    if (Math.hypot(motionEvent.getRawX() - this.mTouchDown.x, motionEvent.getRawY() - this.mTouchDown.y) > ShortcutBarView.this.mTouchSlop) {
                        this.mHasMoved = true;
                    }
                }
            } else if (this.mCancelled) {
                return true;
            } else {
                if (!this.mHasMoved) {
                    view.performClick();
                } else {
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    onUp(this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity());
                }
                this.mHasMoved = false;
                this.mVelocityTracker.clear();
            }
            return true;
        }
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mShiftForTransientBar = 0;
        if (this.mOrientation == 1) {
            this.mInsets = windowInsets.getStableInsets();
        } else {
            Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
            if (insetsIgnoringVisibility.right > 0) {
                this.mInsets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars() | WindowInsets.Type.displayCutout());
                this.mShiftForTransientBar = insetsIgnoringVisibility.right;
            } else {
                this.mInsets = windowInsets.getStableInsets();
            }
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public final void autoUndock(float f) {
        boolean z;
        ShortcutBarContainer shortcutBarContainer;
        final float f2;
        if (this.mRevealButton.getVisibility() == 0) {
            if (this.mRevealButton.getTranslationX() > 0.0f) {
                z = true;
            } else {
                z = false;
            }
            if (!this.mIsDockingAnimationRunning) {
                cancelAnimation();
                cancelAnimation();
                this.mBar.setTranslationX(this.mRevealButton.getTranslationX());
                float translationY = this.mRevealButton.getTranslationY() + (this.mRevealButton.getHeight() / 2);
                this.mBar.setTranslationY(translationY - (shortcutBarContainer.getHeight() / 2));
                if (z) {
                    f2 = ((getWidth() - this.mBar.getWidth()) - this.mBarMarginEnd) - f;
                } else {
                    f2 = this.mBarMarginEnd + f;
                }
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mRevealButton, FrameLayout.TRANSLATION_X, f2);
                ObjectAnimator ofInt = ObjectAnimator.ofInt(this.mRevealButton, RevealButton.BACKGROUND_WIDTH, this.mBar.getWidth());
                ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this.mRevealButton, RevealButton.BACKGROUND_HEIGHT, this.mBar.getHeight());
                ObjectAnimator ofInt3 = ObjectAnimator.ofInt(this.mRevealButton, RevealButton.ICON_ALPHA, 255, 0);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mBar, FrameLayout.TRANSLATION_X, f2);
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mBar, FrameLayout.ALPHA, 0.0f, 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ofFloat, ofInt, ofInt2, ofInt3, ofFloat2, ofFloat3);
                animatorSet.setInterpolator(new FastOutSlowInInterpolator());
                animatorSet.setDuration(200L);
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        ShortcutBarView.this.mRevealButton.setVisibility(4);
                        RevealButton.AnonymousClass1 r3 = RevealButton.BACKGROUND_WIDTH;
                        ShortcutBarView shortcutBarView = ShortcutBarView.this;
                        r3.set((RevealButton.AnonymousClass1) shortcutBarView.mRevealButton, Integer.valueOf(shortcutBarView.mRevealButtonIconWidth * 2));
                        RevealButton.AnonymousClass2 r32 = RevealButton.BACKGROUND_HEIGHT;
                        ShortcutBarView shortcutBarView2 = ShortcutBarView.this;
                        r32.set((RevealButton.AnonymousClass2) shortcutBarView2.mRevealButton, Integer.valueOf(shortcutBarView2.mRevealButtonIconHeight));
                        RevealButton.ICON_ALPHA.set((RevealButton.AnonymousClass3) ShortcutBarView.this.mRevealButton, (Integer) 255);
                        ShortcutBarContainer shortcutBarContainer2 = ShortcutBarView.this.mBar;
                        Objects.requireNonNull(shortcutBarContainer2);
                        shortcutBarContainer2.mUseClearBackground = false;
                        shortcutBarContainer2.invalidate();
                        ShortcutBarView.this.mBar.setTranslationX(f2);
                        ShortcutBarView.this.snapBarBackVertically().start();
                        ShortcutBarView.this.mIsDockingAnimationRunning = false;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                        ShortcutBarView shortcutBarView = ShortcutBarView.this;
                        shortcutBarView.mIsDockingAnimationRunning = true;
                        shortcutBarView.mBar.setVisibility(0);
                        ShortcutBarContainer shortcutBarContainer2 = ShortcutBarView.this.mBar;
                        Objects.requireNonNull(shortcutBarContainer2);
                        shortcutBarContainer2.mUseClearBackground = true;
                        shortcutBarContainer2.invalidate();
                    }
                });
                animatorSet.start();
            }
        }
    }

    public final void getTouchableRegion() {
        int i;
        int i2;
        if (this.mRevealButton.getVisibility() == 0) {
            this.mRevealButton.getLocationInWindow(this.mTempInts);
        } else {
            this.mBar.getLocationInWindow(this.mTempInts);
        }
        Rect rect = this.mTmpRect;
        int[] iArr = this.mTempInts;
        int i3 = iArr[0];
        int i4 = iArr[1];
        int i5 = iArr[0];
        if (this.mRevealButton.getVisibility() == 0) {
            i = this.mRevealButton.getWidth();
        } else {
            i = this.mBar.getWidth();
        }
        int i6 = i + i5;
        int i7 = this.mTempInts[1];
        if (this.mRevealButton.getVisibility() == 0) {
            i2 = this.mRevealButton.getHeight();
        } else {
            i2 = this.mBar.getHeight();
        }
        rect.set(i3, i4, i6, i2 + i7);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onConfigChanged(Configuration configuration) {
        int i = configuration.orientation;
        if (i != this.mOrientation) {
            addOnLayoutChangeListener(this.mOnLayoutChangeListener);
            this.mOrientation = i;
        }
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onCountdown(long j) {
        int i;
        int floorDiv = (int) Math.floorDiv(j + 500, 1000);
        if (floorDiv == 1) {
            i = 2131231715;
        } else if (floorDiv == 2) {
            i = 2131231716;
        } else if (floorDiv != 3) {
            i = 2131232249;
        } else {
            i = 2131231717;
        }
        setScreenRecordButtonBackground(true);
        this.mRecordButton.setImageResource(i);
        this.mRecordButton.setContentDescription(((FrameLayout) this).mContext.getString(2131951745));
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onRecordingEnd() {
        this.mScreenshotButton.announceForAccessibility(((FrameLayout) this).mContext.getString(2131951738));
        onScreenRecordStop();
    }

    public final void onScreenRecordStop() {
        ScreenRecordController screenRecordController = this.mScreenRecordController;
        Objects.requireNonNull(screenRecordController);
        RecordingController recordingController = screenRecordController.mController;
        Objects.requireNonNull(recordingController);
        recordingController.mListeners.remove(this);
        setScreenRecordButtonBackground(false);
        setScreenRecordButtonDrawable();
        RevealButton revealButton = this.mRevealButton;
        Objects.requireNonNull(revealButton);
        revealButton.mIsRecording = false;
        revealButton.invalidate();
        this.mRecordButton.setContentDescription(((FrameLayout) this).mContext.getString(2131951744));
    }

    public final void setScreenRecordButtonBackground(boolean z) {
        int i;
        Drawable background = this.mRecordButton.getBackground();
        if (z) {
            i = ((FrameLayout) this).mContext.getColor(2131099868);
        } else {
            i = SHORTCUT_BAR_BACKGROUND_COLOR;
        }
        background.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC));
        this.mRecordButton.setBackground(background);
    }

    public final void setScreenRecordButtonDrawable() {
        Drawable mutate = ((FrameLayout) this).mContext.getDrawable(2131232249).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
        this.mRecordButton.setImageDrawable(mutate);
    }

    public final boolean shouldBeVisible() {
        if (this.mIsScreenshotVisible || this.mIsRecordVisible || this.mIsFpsVisible || this.mIsEntryPointVisible) {
            return true;
        }
        return false;
    }

    public final void slideIn() {
        float f;
        RevealButton revealButton = this.mRevealButton;
        Objects.requireNonNull(revealButton);
        int width = ((View) revealButton.getParent()).getWidth();
        if (!revealButton.mRightSide) {
            width = -revealButton.getWidth();
        }
        float width2 = revealButton.getWidth() / 2;
        Property property = View.TRANSLATION_X;
        float[] fArr = new float[2];
        float f2 = width;
        fArr[0] = f2;
        if (revealButton.mRightSide) {
            f = f2 - width2;
        } else {
            f = f2 + width2;
        }
        fArr[1] = f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(revealButton, property, fArr);
        ofFloat.setDuration(200L);
        ofFloat.start();
    }

    public final void snapBarBackIfNecessary() {
        boolean z;
        int i;
        if (this.mRevealButton.getVisibility() != 0) {
            if ((this.mBar.getWidth() / 2.0f) + this.mBar.getTranslationX() > getWidth() / 2.0f) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                i = ((getWidth() - this.mBarMarginEnd) - this.mInsets.right) - this.mBar.getWidth();
            } else {
                i = this.mInsets.left + this.mBarMarginEnd;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mBar, FrameLayout.TRANSLATION_X, i);
            ObjectAnimator snapBarBackVertically = snapBarBackVertically();
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, snapBarBackVertically);
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.setDuration(100L);
            animatorSet.start();
        }
    }

    public final ObjectAnimator snapBarBackVertically() {
        return ObjectAnimator.ofFloat(this.mBar, FrameLayout.TRANSLATION_Y, Math.min((getHeight() - this.mInsets.bottom) - this.mBar.getHeight(), Math.max(this.mInsets.top, this.mBar.getTranslationY())));
    }

    /* renamed from: -$$Nest$mdock  reason: not valid java name */
    public static void m170$$Nest$mdock(ShortcutBarView shortcutBarView, final boolean z) {
        RevealButton revealButton;
        int i;
        Objects.requireNonNull(shortcutBarView);
        if (!shortcutBarView.mIsDockingAnimationRunning) {
            shortcutBarView.mOnTouchListener.cancelAnimation();
            shortcutBarView.mRevealButtonOnTouchListener.cancelAnimation();
            shortcutBarView.mRevealButton.setTranslationX(shortcutBarView.mBar.getTranslationX());
            float translationY = shortcutBarView.mBar.getTranslationY() + (shortcutBarView.mBar.getHeight() / 2);
            shortcutBarView.mRevealButton.setTranslationY(translationY - (revealButton.getHeight() / 2));
            RevealButton.AnonymousClass1 r0 = RevealButton.BACKGROUND_WIDTH;
            r0.set((RevealButton.AnonymousClass1) shortcutBarView.mRevealButton, Integer.valueOf(shortcutBarView.mBar.getWidth()));
            RevealButton.AnonymousClass2 r1 = RevealButton.BACKGROUND_HEIGHT;
            r1.set((RevealButton.AnonymousClass2) shortcutBarView.mRevealButton, Integer.valueOf(shortcutBarView.mBar.getHeight()));
            if (z) {
                i = shortcutBarView.getWidth() - shortcutBarView.mRevealButtonIconWidth;
            } else {
                i = -shortcutBarView.mRevealButtonIconWidth;
            }
            final float f = i;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(shortcutBarView.mBar, FrameLayout.TRANSLATION_X, f);
            ObjectAnimator ofInt = ObjectAnimator.ofInt(shortcutBarView.mRevealButton, r0, shortcutBarView.mRevealButtonIconWidth * 2);
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(shortcutBarView.mRevealButton, r1, shortcutBarView.mRevealButtonIconHeight);
            ObjectAnimator ofInt3 = ObjectAnimator.ofInt(shortcutBarView.mRevealButton, RevealButton.ICON_ALPHA, 0, 255);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(shortcutBarView.mRevealButton, FrameLayout.TRANSLATION_X, f);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(shortcutBarView.mBar, FrameLayout.ALPHA, 1.0f, 0.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ofFloat, ofInt, ofInt2, ofInt3, ofFloat2, ofFloat3);
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.setDuration(200L);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.ShortcutBarView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    RevealButton revealButton2 = ShortcutBarView.this.mRevealButton;
                    boolean z2 = z;
                    Objects.requireNonNull(revealButton2);
                    revealButton2.mRightSide = z2;
                    revealButton2.invalidate();
                    ShortcutBarView.this.mRevealButton.setTranslationX(f);
                    RevealButton.ICON_ALPHA.set((RevealButton.AnonymousClass3) ShortcutBarView.this.mRevealButton, (Integer) 255);
                    ShortcutBarView.this.mBar.setVisibility(4);
                    ShortcutBarContainer shortcutBarContainer = ShortcutBarView.this.mBar;
                    Objects.requireNonNull(shortcutBarContainer);
                    shortcutBarContainer.mUseClearBackground = false;
                    shortcutBarContainer.invalidate();
                    ShortcutBarView.this.mIsDockingAnimationRunning = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    ShortcutBarView shortcutBarView2 = ShortcutBarView.this;
                    shortcutBarView2.mIsDockingAnimationRunning = true;
                    shortcutBarView2.mRevealButton.setVisibility(0);
                    ShortcutBarContainer shortcutBarContainer = ShortcutBarView.this.mBar;
                    Objects.requireNonNull(shortcutBarContainer);
                    shortcutBarContainer.mUseClearBackground = true;
                    shortcutBarContainer.invalidate();
                }
            });
            animatorSet.start();
        }
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [com.google.android.systemui.gamedashboard.ShortcutBarView$$ExternalSyntheticLambda3] */
    /* JADX WARN: Type inference failed for: r3v4, types: [com.google.android.systemui.gamedashboard.ShortcutBarView$1] */
    /* JADX WARN: Type inference failed for: r3v5, types: [com.google.android.systemui.gamedashboard.ShortcutBarView$2] */
    /* JADX WARN: Type inference failed for: r3v6, types: [com.google.android.systemui.gamedashboard.ShortcutBarView$3] */
    public ShortcutBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsAttached = true;
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        getViewTreeObserver().addOnDrawListener(this.mSystemGestureExcludeUpdater);
        this.mConfigurationController.addCallback(this);
        int i = ((FrameLayout) this).mContext.getResources().getConfiguration().orientation;
        int i2 = this.mOrientation;
        if (i2 == -1) {
            this.mOrientation = i;
            return;
        }
        if (i2 != i) {
            addOnLayoutChangeListener(this.mOnLayoutChangeListener);
            this.mOrientation = i;
        }
        if (this.mIsRecordVisible) {
            ScreenRecordController screenRecordController = this.mScreenRecordController;
            Objects.requireNonNull(screenRecordController);
            RecordingController recordingController = screenRecordController.mController;
            Objects.requireNonNull(recordingController);
            recordingController.mListeners.add(this);
            setScreenRecordButtonDrawable();
            ScreenRecordController screenRecordController2 = this.mScreenRecordController;
            Objects.requireNonNull(screenRecordController2);
            boolean isRecording = screenRecordController2.mController.isRecording();
            setScreenRecordButtonBackground(isRecording);
            RevealButton revealButton = this.mRevealButton;
            Objects.requireNonNull(revealButton);
            revealButton.mIsRecording = isRecording;
            revealButton.invalidate();
        }
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        getTouchableRegion();
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(this.mTmpRect);
    }

    @Override // com.android.systemui.screenrecord.RecordingController.RecordingStateChangeCallback
    public final void onCountdownEnd() {
        setScreenRecordButtonDrawable();
        ScreenRecordController screenRecordController = this.mScreenRecordController;
        Objects.requireNonNull(screenRecordController);
        if (!screenRecordController.mController.isRecording()) {
            ScreenRecordController screenRecordController2 = this.mScreenRecordController;
            Objects.requireNonNull(screenRecordController2);
            RecordingController recordingController = screenRecordController2.mController;
            Objects.requireNonNull(recordingController);
            if (!recordingController.mIsStarting) {
                this.mScreenshotButton.announceForAccessibility(((FrameLayout) this).mContext.getString(2131951737));
                onScreenRecordStop();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIsAttached = false;
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
        getViewTreeObserver().removeOnDrawListener(this.mSystemGestureExcludeUpdater);
        this.mConfigurationController.removeCallback(this);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mBar = (ShortcutBarContainer) findViewById(2131427949);
        this.mRevealButton = (RevealButton) findViewById(2131428698);
        this.mScreenshotButton = (ShortcutBarButton) this.mBar.findViewById(2131428765);
        this.mRecordButton = (ShortcutBarButton) this.mBar.findViewById(2131428675);
        this.mFpsView = (TextView) this.mBar.findViewById(2131427991);
        this.mEntryPointButton = (ShortcutBarButton) this.mBar.findViewById(2131427936);
    }
}
