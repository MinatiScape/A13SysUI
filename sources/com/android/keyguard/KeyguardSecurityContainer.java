package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardSecurityContainer extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mActivePointerId;
    public AlertDialog mAlertDialog;
    public int mCurrentMode;
    public boolean mDisappearAnimRunning;
    public FalsingManager mFalsingManager;
    public GlobalSettings mGlobalSettings;
    public boolean mIsDragging;
    public float mLastTouchY;
    public final ArrayList mMotionEventListeners;
    public KeyguardSecurityViewFlipper mSecurityViewFlipper;
    public final SpringAnimation mSpringAnimation;
    public float mStartTouchY;
    public SwipeListener mSwipeListener;
    public boolean mSwipeUpToRetry;
    public UserSwitcherController mUserSwitcherController;
    public final VelocityTracker mVelocityTracker;
    public final ViewConfiguration mViewConfiguration;
    public ViewMode mViewMode;
    public final AnonymousClass1 mWindowInsetsAnimationCallback;

    /* loaded from: classes.dex */
    public static class DefaultViewMode implements ViewMode {
        public KeyguardSecurityViewFlipper mViewFlipper;

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
            this.mViewFlipper = keyguardSecurityViewFlipper;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) keyguardSecurityViewFlipper.getLayoutParams();
            layoutParams.gravity = 1;
            this.mViewFlipper.setLayoutParams(layoutParams);
            this.mViewFlipper.setTranslationX(0.0f);
        }
    }

    /* loaded from: classes.dex */
    public static class OneHandedViewMode implements ViewMode {
        public GlobalSettings mGlobalSettings;
        public ValueAnimator mRunningOneHandedAnimator;
        public ViewGroup mView;
        public KeyguardSecurityViewFlipper mViewFlipper;

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void updateSecurityViewLocation() {
            updateSecurityViewLocation(isLeftAligned(), false);
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
            this.mView = viewGroup;
            this.mViewFlipper = keyguardSecurityViewFlipper;
            this.mGlobalSettings = globalSettings;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) keyguardSecurityViewFlipper.getLayoutParams();
            layoutParams.gravity = 83;
            this.mViewFlipper.setLayoutParams(layoutParams);
            updateSecurityViewLocation(isLeftAligned(), false);
        }

        public final boolean isLeftAligned() {
            if (this.mGlobalSettings.getInt("one_handed_keyguard_side", 0) == 0) {
                return true;
            }
            return false;
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void updatePositionByTouchX(float f) {
            boolean z;
            if (f <= this.mView.getWidth() / 2.0f) {
                z = true;
            } else {
                z = false;
            }
            updateSecurityViewLocation(z, false);
        }

        public final void updateSecurityViewLocation(final boolean z, boolean z2) {
            ValueAnimator valueAnimator = this.mRunningOneHandedAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.mRunningOneHandedAnimator = null;
            }
            final boolean z3 = false;
            final int measuredWidth = z ? 0 : this.mView.getMeasuredWidth() - this.mViewFlipper.getWidth();
            if (z2) {
                final Interpolator loadInterpolator = AnimationUtils.loadInterpolator(this.mView.getContext(), 17563674);
                final PathInterpolator pathInterpolator = Interpolators.FAST_OUT_LINEAR_IN;
                final PathInterpolator pathInterpolator2 = Interpolators.LINEAR_OUT_SLOW_IN;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.mRunningOneHandedAnimator = ofFloat;
                ofFloat.setDuration(500L);
                this.mRunningOneHandedAnimator.setInterpolator(Interpolators.LINEAR);
                final int translationX = (int) this.mViewFlipper.getTranslationX();
                final int dimension = (int) this.mView.getResources().getDimension(2131166695);
                if (this.mViewFlipper.hasOverlappingRendering() && this.mViewFlipper.getLayerType() != 2) {
                    z3 = true;
                }
                if (z3) {
                    this.mViewFlipper.setLayerType(2, null);
                }
                final float alpha = this.mViewFlipper.getAlpha();
                this.mRunningOneHandedAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardSecurityContainer.OneHandedViewMode.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        OneHandedViewMode.this.mRunningOneHandedAnimator = null;
                    }
                });
                this.mRunningOneHandedAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.KeyguardSecurityContainer$OneHandedViewMode$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        boolean z4;
                        KeyguardSecurityContainer.OneHandedViewMode oneHandedViewMode = KeyguardSecurityContainer.OneHandedViewMode.this;
                        Interpolator interpolator = loadInterpolator;
                        int i = dimension;
                        boolean z5 = z;
                        Interpolator interpolator2 = pathInterpolator;
                        float f = alpha;
                        int i2 = translationX;
                        Interpolator interpolator3 = pathInterpolator2;
                        int i3 = measuredWidth;
                        boolean z6 = z3;
                        Objects.requireNonNull(oneHandedViewMode);
                        if (valueAnimator2.getAnimatedFraction() < 0.2f) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                        int interpolation = (int) (interpolator.getInterpolation(valueAnimator2.getAnimatedFraction()) * i);
                        int i4 = i - interpolation;
                        if (z5) {
                            interpolation = -interpolation;
                            i4 = -i4;
                        }
                        if (z4) {
                            oneHandedViewMode.mViewFlipper.setAlpha(interpolator2.getInterpolation(MathUtils.constrainedMap(1.0f, 0.0f, 0.0f, 0.2f, valueAnimator2.getAnimatedFraction())) * f);
                            oneHandedViewMode.mViewFlipper.setTranslationX(i2 + interpolation);
                        } else {
                            oneHandedViewMode.mViewFlipper.setAlpha(interpolator3.getInterpolation(MathUtils.constrainedMap(0.0f, 1.0f, 0.2f, 1.0f, valueAnimator2.getAnimatedFraction())));
                            oneHandedViewMode.mViewFlipper.setTranslationX(i3 - i4);
                        }
                        if (valueAnimator2.getAnimatedFraction() == 1.0f && z6) {
                            oneHandedViewMode.mViewFlipper.setLayerType(0, null);
                        }
                    }
                });
                this.mRunningOneHandedAnimator.start();
                return;
            }
            this.mViewFlipper.setTranslationX(measuredWidth);
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final int getChildWidthMeasureSpec(int i) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) / 2, 1073741824);
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void handleTap(MotionEvent motionEvent) {
            int i;
            float x = motionEvent.getX();
            boolean isLeftAligned = isLeftAligned();
            if ((isLeftAligned && x > this.mView.getWidth() / 2.0f) || (!isLeftAligned && x < this.mView.getWidth() / 2.0f)) {
                boolean z = !isLeftAligned ? 1 : 0;
                this.mGlobalSettings.putInt("one_handed_keyguard_side", !z ? 1 : 0);
                if (z) {
                    i = 5;
                } else {
                    i = 6;
                }
                SysUiStatsLog.write(63, i);
                updateSecurityViewLocation(z, true);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface SecurityCallback {
    }

    /* loaded from: classes.dex */
    public interface SwipeListener {
    }

    /* loaded from: classes.dex */
    public static class UserSwitcherViewMode implements ViewMode {
        public FalsingManager mFalsingManager;
        public KeyguardUserSwitcherPopupMenu mPopup;
        public Resources mResources;
        public TextView mUserSwitcher;
        public UserSwitcherController mUserSwitcherController;
        public ViewGroup mUserSwitcherViewGroup;
        public ViewGroup mView;
        public KeyguardSecurityViewFlipper mViewFlipper;

        /* JADX WARN: Type inference failed for: r3v17, types: [com.android.systemui.statusbar.policy.UserSwitcherController$BaseUserAdapter, com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$1] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void init(android.view.ViewGroup r2, com.android.systemui.util.settings.GlobalSettings r3, com.android.keyguard.KeyguardSecurityViewFlipper r4, com.android.systemui.plugins.FalsingManager r5, com.android.systemui.statusbar.policy.UserSwitcherController r6) {
            /*
                r1 = this;
                r1.mView = r2
                r1.mViewFlipper = r4
                r1.mFalsingManager = r5
                r1.mUserSwitcherController = r6
                android.content.Context r3 = r2.getContext()
                android.content.res.Resources r3 = r3.getResources()
                r1.mResources = r3
                android.view.ViewGroup r3 = r1.mUserSwitcherViewGroup
                r4 = 1
                if (r3 != 0) goto L_0x0034
                android.content.Context r2 = r2.getContext()
                android.view.LayoutInflater r2 = android.view.LayoutInflater.from(r2)
                r3 = 2131624154(0x7f0e00da, float:1.887548E38)
                android.view.ViewGroup r5 = r1.mView
                r2.inflate(r3, r5, r4)
                android.view.ViewGroup r2 = r1.mView
                r3 = 2131428168(0x7f0b0348, float:1.8477973E38)
                android.view.View r2 = r2.findViewById(r3)
                android.view.ViewGroup r2 = (android.view.ViewGroup) r2
                r1.mUserSwitcherViewGroup = r2
            L_0x0034:
                int r2 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
                android.view.ViewGroup r3 = r1.mView
                android.content.Context r3 = r3.getContext()
                android.os.UserManager r3 = android.os.UserManager.get(r3)
                android.graphics.Bitmap r3 = r3.getUserIcon(r2)
                r5 = 0
                if (r3 == 0) goto L_0x004f
                android.graphics.drawable.BitmapDrawable r2 = new android.graphics.drawable.BitmapDrawable
                r2.<init>(r3)
                goto L_0x0055
            L_0x004f:
                android.content.res.Resources r3 = r1.mResources
                android.graphics.drawable.Drawable r2 = com.android.internal.util.UserIcons.getDefaultUserIcon(r3, r2, r5)
            L_0x0055:
                android.view.ViewGroup r3 = r1.mView
                r6 = 2131429168(0x7f0b0730, float:1.8480001E38)
                android.view.View r3 = r3.findViewById(r6)
                android.widget.ImageView r3 = (android.widget.ImageView) r3
                r3.setImageDrawable(r2)
                r1.updateSecurityViewLocation()
                android.view.ViewGroup r2 = r1.mView
                r3 = 2131429174(0x7f0b0736, float:1.8480013E38)
                android.view.View r2 = r2.findViewById(r3)
                android.widget.TextView r2 = (android.widget.TextView) r2
                r1.mUserSwitcher = r2
                com.android.systemui.statusbar.policy.UserSwitcherController r2 = r1.mUserSwitcherController
                java.util.Objects.requireNonNull(r2)
                r3 = r5
            L_0x0079:
                java.util.ArrayList<com.android.systemui.statusbar.policy.UserSwitcherController$UserRecord> r6 = r2.mUsers
                int r6 = r6.size()
                if (r3 >= r6) goto L_0x0091
                java.util.ArrayList<com.android.systemui.statusbar.policy.UserSwitcherController$UserRecord> r6 = r2.mUsers
                java.lang.Object r6 = r6.get(r3)
                com.android.systemui.statusbar.policy.UserSwitcherController$UserRecord r6 = (com.android.systemui.statusbar.policy.UserSwitcherController.UserRecord) r6
                boolean r0 = r6.isCurrent
                if (r0 == 0) goto L_0x008e
                goto L_0x0092
            L_0x008e:
                int r3 = r3 + 1
                goto L_0x0079
            L_0x0091:
                r6 = 0
            L_0x0092:
                android.widget.TextView r2 = r1.mUserSwitcher
                com.android.systemui.statusbar.policy.UserSwitcherController r3 = r1.mUserSwitcherController
                java.lang.String r3 = r3.getCurrentUserName()
                r2.setText(r3)
                android.view.ViewGroup r2 = r1.mView
                r3 = 2131429172(0x7f0b0734, float:1.848001E38)
                android.view.View r2 = r2.findViewById(r3)
                android.view.ViewGroup r2 = (android.view.ViewGroup) r2
                com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$1 r3 = new com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$1
                com.android.systemui.statusbar.policy.UserSwitcherController r0 = r1.mUserSwitcherController
                r3.<init>(r0)
                int r6 = r3.getCount()
                r0 = 2
                if (r6 >= r0) goto L_0x00c9
                android.widget.TextView r1 = r1.mUserSwitcher
                android.graphics.drawable.Drawable r1 = r1.getBackground()
                android.graphics.drawable.LayerDrawable r1 = (android.graphics.drawable.LayerDrawable) r1
                android.graphics.drawable.Drawable r1 = r1.getDrawable(r4)
                r1.setAlpha(r5)
                r2.setClickable(r5)
                goto L_0x00e2
            L_0x00c9:
                android.widget.TextView r5 = r1.mUserSwitcher
                android.graphics.drawable.Drawable r5 = r5.getBackground()
                android.graphics.drawable.LayerDrawable r5 = (android.graphics.drawable.LayerDrawable) r5
                android.graphics.drawable.Drawable r4 = r5.getDrawable(r4)
                r5 = 255(0xff, float:3.57E-43)
                r4.setAlpha(r5)
                com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda0 r4 = new com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda0
                r4.<init>()
                r2.setOnClickListener(r4)
            L_0x00e2:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainer.UserSwitcherViewMode.init(android.view.ViewGroup, com.android.systemui.util.settings.GlobalSettings, com.android.keyguard.KeyguardSecurityViewFlipper, com.android.systemui.plugins.FalsingManager, com.android.systemui.statusbar.policy.UserSwitcherController):void");
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void reset() {
            KeyguardUserSwitcherPopupMenu keyguardUserSwitcherPopupMenu = this.mPopup;
            if (keyguardUserSwitcherPopupMenu != null) {
                keyguardUserSwitcherPopupMenu.dismiss();
                this.mPopup = null;
            }
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void startAppearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
            if (securityMode != KeyguardSecurityModel.SecurityMode.Password) {
                this.mUserSwitcherViewGroup.setAlpha(0.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mUserSwitcherViewGroup, View.ALPHA, 1.0f);
                ofFloat.setInterpolator(Interpolators.ALPHA_IN);
                ofFloat.setDuration(500L);
                ofFloat.start();
            }
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void startDisappearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
            if (securityMode != KeyguardSecurityModel.SecurityMode.Password) {
                int dimensionPixelSize = this.mResources.getDimensionPixelSize(2131165665);
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mView, View.TRANSLATION_Y, dimensionPixelSize);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.mView, View.ALPHA, 0.0f);
                animatorSet.setInterpolator(Interpolators.STANDARD_ACCELERATE);
                animatorSet.playTogether(ofFloat2, ofFloat);
                animatorSet.start();
            }
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final void updateSecurityViewLocation() {
            if (this.mResources.getConfiguration().orientation == 1) {
                KeyguardSecurityViewFlipper keyguardSecurityViewFlipper = this.mViewFlipper;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) keyguardSecurityViewFlipper.getLayoutParams();
                layoutParams.gravity = 1;
                keyguardSecurityViewFlipper.setLayoutParams(layoutParams);
                ViewGroup viewGroup = this.mUserSwitcherViewGroup;
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                layoutParams2.gravity = 1;
                viewGroup.setLayoutParams(layoutParams2);
                this.mUserSwitcherViewGroup.setTranslationY(0.0f);
                return;
            }
            KeyguardSecurityViewFlipper keyguardSecurityViewFlipper2 = this.mViewFlipper;
            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) keyguardSecurityViewFlipper2.getLayoutParams();
            layoutParams3.gravity = 85;
            keyguardSecurityViewFlipper2.setLayoutParams(layoutParams3);
            ViewGroup viewGroup2 = this.mUserSwitcherViewGroup;
            FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) viewGroup2.getLayoutParams();
            layoutParams4.gravity = 19;
            viewGroup2.setLayoutParams(layoutParams4);
            this.mUserSwitcherViewGroup.setTranslationY(-this.mResources.getDimensionPixelSize(2131167060));
        }

        @Override // com.android.keyguard.KeyguardSecurityContainer.ViewMode
        public final int getChildWidthMeasureSpec(int i) {
            return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i) / 2, 1073741824);
        }
    }

    /* loaded from: classes.dex */
    public interface ViewMode {
        default int getChildWidthMeasureSpec(int i) {
            return i;
        }

        default void handleTap(MotionEvent motionEvent) {
        }

        default void init(ViewGroup viewGroup, GlobalSettings globalSettings, KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, FalsingManager falsingManager, UserSwitcherController userSwitcherController) {
        }

        default void reset() {
        }

        default void startAppearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        }

        default void startDisappearAnimation(KeyguardSecurityModel.SecurityMode securityMode) {
        }

        default void updatePositionByTouchX(float f) {
        }

        default void updateSecurityViewLocation() {
        }
    }

    public KeyguardSecurityContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                int childWidthMeasureSpec = this.mViewMode.getChildWidthMeasureSpec(i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                layoutParams.width = View.MeasureSpec.getSize(childWidthMeasureSpec);
                measureChildWithMargins(childAt, childWidthMeasureSpec, 0, i2, 0);
                i3 = Math.max(i3, childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                i4 = Math.max(i4, childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                i5 = View.combineMeasuredStates(i5, childAt.getMeasuredState());
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(getPaddingRight() + getPaddingLeft() + i3, getSuggestedMinimumWidth()), i, i5), View.resolveSizeAndState(Math.max(getPaddingBottom() + getPaddingTop() + i4, getSuggestedMinimumHeight()), i2, i5 << 16));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final boolean shouldDelayChildPressedState() {
        return true;
    }

    public final void showAlmostAtWipeDialog(int i, int i2, int i3) {
        String str;
        if (i3 == 1) {
            str = ((FrameLayout) this).mContext.getString(2131952575, Integer.valueOf(i), Integer.valueOf(i2));
        } else if (i3 == 2) {
            str = ((FrameLayout) this).mContext.getString(2131952572, Integer.valueOf(i), Integer.valueOf(i2));
        } else if (i3 != 3) {
            str = null;
        } else {
            str = ((FrameLayout) this).mContext.getString(2131952573, Integer.valueOf(i), Integer.valueOf(i2));
        }
        showDialog(str);
    }

    public final void showWipeDialog(int i, int i2) {
        String str;
        if (i2 == 1) {
            str = ((FrameLayout) this).mContext.getString(2131952578, Integer.valueOf(i));
        } else if (i2 == 2) {
            str = ((FrameLayout) this).mContext.getString(2131952576, Integer.valueOf(i));
        } else if (i2 != 3) {
            str = null;
        } else {
            str = ((FrameLayout) this).mContext.getString(2131952577, Integer.valueOf(i));
        }
        showDialog(str);
    }

    /* loaded from: classes.dex */
    public enum BouncerUiEvent implements UiEventLogger.UiEventEnum {
        UNKNOWN(0),
        BOUNCER_DISMISS_EXTENDED_ACCESS(413),
        BOUNCER_DISMISS_BIOMETRIC(414),
        BOUNCER_DISMISS_NONE_SECURITY(415),
        BOUNCER_DISMISS_PASSWORD(416),
        BOUNCER_DISMISS_SIM(417),
        BOUNCER_PASSWORD_SUCCESS(418),
        BOUNCER_PASSWORD_FAILURE(419);
        
        private final int mId;

        BouncerUiEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public KeyguardSecurityContainer(Context context) {
        this(context, null, 0);
    }

    public static String modeToString(int i) {
        if (i == 0) {
            return "Default";
        }
        if (i == 1) {
            return "OneHanded";
        }
        if (i == 2) {
            return "UserSwitcher";
        }
        throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("mode: ", i, " not supported"));
    }

    public final boolean isOneHandedModeLeftAligned() {
        if (this.mCurrentMode != 1 || !((OneHandedViewMode) this.mViewMode).isLeftAligned()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0029, code lost:
        if (r3 != 3) goto L_0x007c;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(final android.view.MotionEvent r6) {
        /*
            r5 = this;
            java.util.ArrayList r0 = r5.mMotionEventListeners
            java.util.stream.Stream r0 = r0.stream()
            com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda0 r1 = new com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda0
            r1.<init>()
            boolean r0 = r0.anyMatch(r1)
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x001c
            boolean r0 = super.onInterceptTouchEvent(r6)
            if (r0 == 0) goto L_0x001a
            goto L_0x001c
        L_0x001a:
            r0 = r1
            goto L_0x001d
        L_0x001c:
            r0 = r2
        L_0x001d:
            int r3 = r6.getActionMasked()
            if (r3 == 0) goto L_0x0067
            if (r3 == r2) goto L_0x0064
            r4 = 2
            if (r3 == r4) goto L_0x002c
            r6 = 3
            if (r3 == r6) goto L_0x0064
            goto L_0x007c
        L_0x002c:
            boolean r3 = r5.mIsDragging
            if (r3 == 0) goto L_0x0031
            return r2
        L_0x0031:
            boolean r3 = r5.mSwipeUpToRetry
            if (r3 != 0) goto L_0x0036
            return r1
        L_0x0036:
            com.android.keyguard.KeyguardSecurityViewFlipper r3 = r5.mSecurityViewFlipper
            com.android.keyguard.KeyguardInputView r3 = r3.getSecurityView()
            boolean r3 = r3.disallowInterceptTouch(r6)
            if (r3 == 0) goto L_0x0043
            return r1
        L_0x0043:
            int r1 = r5.mActivePointerId
            int r1 = r6.findPointerIndex(r1)
            android.view.ViewConfiguration r3 = r5.mViewConfiguration
            int r3 = r3.getScaledTouchSlop()
            float r3 = (float) r3
            r4 = 1082130432(0x40800000, float:4.0)
            float r3 = r3 * r4
            r4 = -1
            if (r1 == r4) goto L_0x007c
            float r4 = r5.mStartTouchY
            float r6 = r6.getY(r1)
            float r4 = r4 - r6
            int r6 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1))
            if (r6 <= 0) goto L_0x007c
            r5.mIsDragging = r2
            return r2
        L_0x0064:
            r5.mIsDragging = r1
            goto L_0x007c
        L_0x0067:
            int r1 = r6.getActionIndex()
            float r2 = r6.getY(r1)
            r5.mStartTouchY = r2
            int r6 = r6.getPointerId(r1)
            r5.mActivePointerId = r6
            android.view.VelocityTracker r5 = r5.mVelocityTracker
            r5.clear()
        L_0x007c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainer.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void showDialog(String str) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        AlertDialog create = new AlertDialog.Builder(((FrameLayout) this).mContext).setTitle((CharSequence) null).setMessage(str).setCancelable(false).setNeutralButton(2131952934, (DialogInterface.OnClickListener) null).create();
        this.mAlertDialog = create;
        if (!(((FrameLayout) this).mContext instanceof Activity)) {
            create.getWindow().setType(2009);
        }
        this.mAlertDialog.show();
    }

    public final void showTimeoutDialog(int i, int i2, LockPatternUtils lockPatternUtils, KeyguardSecurityModel.SecurityMode securityMode) {
        int i3;
        int i4 = i2 / 1000;
        int ordinal = securityMode.ordinal();
        if (ordinal == 2) {
            i3 = 2131952603;
        } else if (ordinal == 3) {
            i3 = 2131952602;
        } else if (ordinal != 4) {
            i3 = 0;
        } else {
            i3 = 2131952604;
        }
        if (i3 != 0) {
            showDialog(((FrameLayout) this).mContext.getString(i3, Integer.valueOf(lockPatternUtils.getCurrentFailedPasswordAttempts(i)), Integer.valueOf(i4)));
        }
    }

    /* JADX WARN: Type inference failed for: r2v6, types: [com.android.keyguard.KeyguardSecurityContainer$1] */
    public KeyguardSecurityContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mMotionEventListeners = new ArrayList();
        this.mLastTouchY = -1.0f;
        this.mActivePointerId = -1;
        this.mStartTouchY = -1.0f;
        this.mViewMode = new DefaultViewMode();
        this.mCurrentMode = 0;
        this.mWindowInsetsAnimationCallback = new WindowInsetsAnimation.Callback() { // from class: com.android.keyguard.KeyguardSecurityContainer.1
            public final Rect mInitialBounds = new Rect();
            public final Rect mFinalBounds = new Rect();

            @Override // android.view.WindowInsetsAnimation.Callback
            public final void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                if (!KeyguardSecurityContainer.this.mDisappearAnimRunning) {
                    InteractionJankMonitor.getInstance().end(17);
                    for (int i2 = 0; i2 < KeyguardSecurityContainer.this.getChildCount(); i2++) {
                        View childAt = KeyguardSecurityContainer.this.getChildAt(i2);
                        childAt.setTranslationY(0);
                        childAt.setAlpha(1.0f);
                    }
                    return;
                }
                InteractionJankMonitor.getInstance().end(20);
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public final void onPrepare(WindowInsetsAnimation windowInsetsAnimation) {
                KeyguardSecurityContainer.this.mSecurityViewFlipper.getBoundsOnScreen(this.mInitialBounds);
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public final WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                int i2;
                float f;
                float f2;
                boolean z = KeyguardSecurityContainer.this.mDisappearAnimRunning;
                if (z) {
                    i2 = -(this.mFinalBounds.bottom - this.mInitialBounds.bottom);
                } else {
                    i2 = this.mInitialBounds.bottom - this.mFinalBounds.bottom;
                }
                float f3 = i2;
                if (z) {
                    f = -((this.mFinalBounds.bottom - this.mInitialBounds.bottom) * 0.75f);
                } else {
                    f = 0.0f;
                }
                float f4 = 1.0f;
                int i3 = 0;
                for (WindowInsetsAnimation windowInsetsAnimation : list) {
                    if ((windowInsetsAnimation.getTypeMask() & WindowInsets.Type.ime()) != 0) {
                        f4 = windowInsetsAnimation.getInterpolatedFraction();
                        i3 += (int) MathUtils.lerp(f3, f, f4);
                    }
                }
                KeyguardSecurityContainer keyguardSecurityContainer = KeyguardSecurityContainer.this;
                if (keyguardSecurityContainer.mDisappearAnimRunning) {
                    f2 = 1.0f - f4;
                } else {
                    f2 = Math.max(f4, keyguardSecurityContainer.getAlpha());
                }
                for (int i4 = 0; i4 < KeyguardSecurityContainer.this.getChildCount(); i4++) {
                    View childAt = KeyguardSecurityContainer.this.getChildAt(i4);
                    childAt.setTranslationY(i3);
                    childAt.setAlpha(f2);
                }
                return windowInsets;
            }

            @Override // android.view.WindowInsetsAnimation.Callback
            public final WindowInsetsAnimation.Bounds onStart(WindowInsetsAnimation windowInsetsAnimation, WindowInsetsAnimation.Bounds bounds) {
                KeyguardSecurityContainer keyguardSecurityContainer = KeyguardSecurityContainer.this;
                if (!keyguardSecurityContainer.mDisappearAnimRunning) {
                    KeyguardInputView securityView = keyguardSecurityContainer.mSecurityViewFlipper.getSecurityView();
                    if (securityView != null) {
                        InteractionJankMonitor.getInstance().begin(securityView, 17);
                    }
                } else {
                    KeyguardInputView securityView2 = keyguardSecurityContainer.mSecurityViewFlipper.getSecurityView();
                    if (securityView2 != null) {
                        InteractionJankMonitor.getInstance().begin(securityView2, 20);
                    }
                }
                KeyguardSecurityContainer.this.mSecurityViewFlipper.getBoundsOnScreen(this.mFinalBounds);
                return bounds;
            }
        };
        this.mSpringAnimation = new SpringAnimation(this, DynamicAnimation.Y);
        this.mViewConfiguration = ViewConfiguration.get(context);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int max = Integer.max(windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).bottom, windowInsets.getInsets(WindowInsets.Type.ime()).bottom);
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), max);
        return windowInsets.inset(0, 0, 0, max);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mViewMode.updateSecurityViewLocation();
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mSecurityViewFlipper = (KeyguardSecurityViewFlipper) findViewById(2131429183);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mViewMode.updateSecurityViewLocation();
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0085  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouchEvent(final android.view.MotionEvent r8) {
        /*
            r7 = this;
            int r0 = r8.getActionMasked()
            java.util.ArrayList r1 = r7.mMotionEventListeners
            java.util.stream.Stream r1 = r1.stream()
            com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda1 r2 = new com.android.keyguard.KeyguardSecurityContainer$$ExternalSyntheticLambda1
            r2.<init>()
            boolean r1 = r1.anyMatch(r2)
            if (r1 != 0) goto L_0x0018
            super.onTouchEvent(r8)
        L_0x0018:
            r1 = -1082130432(0xffffffffbf800000, float:-1.0)
            r2 = 1
            r3 = 0
            if (r0 == r2) goto L_0x006b
            r4 = 2
            if (r0 == r4) goto L_0x0046
            r4 = 3
            if (r0 == r4) goto L_0x006b
            r1 = 6
            if (r0 == r1) goto L_0x0028
            goto L_0x0083
        L_0x0028:
            int r1 = r8.getActionIndex()
            int r4 = r8.getPointerId(r1)
            int r5 = r7.mActivePointerId
            if (r4 != r5) goto L_0x0083
            if (r1 != 0) goto L_0x0038
            r1 = r2
            goto L_0x0039
        L_0x0038:
            r1 = r3
        L_0x0039:
            float r4 = r8.getY(r1)
            r7.mLastTouchY = r4
            int r1 = r8.getPointerId(r1)
            r7.mActivePointerId = r1
            goto L_0x0083
        L_0x0046:
            android.view.VelocityTracker r4 = r7.mVelocityTracker
            r4.addMovement(r8)
            int r4 = r7.mActivePointerId
            int r4 = r8.findPointerIndex(r4)
            float r4 = r8.getY(r4)
            float r5 = r7.mLastTouchY
            int r1 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r1 == 0) goto L_0x0068
            float r1 = r4 - r5
            float r5 = r7.getTranslationY()
            r6 = 1048576000(0x3e800000, float:0.25)
            float r1 = r1 * r6
            float r1 = r1 + r5
            r7.setTranslationY(r1)
        L_0x0068:
            r7.mLastTouchY = r4
            goto L_0x0083
        L_0x006b:
            r4 = -1
            r7.mActivePointerId = r4
            r7.mLastTouchY = r1
            r7.mIsDragging = r3
            android.view.VelocityTracker r1 = r7.mVelocityTracker
            float r1 = r1.getYVelocity()
            androidx.dynamicanimation.animation.SpringAnimation r4 = r7.mSpringAnimation
            java.util.Objects.requireNonNull(r4)
            r4.mVelocity = r1
            r1 = 0
            r4.animateToFinalPosition(r1)
        L_0x0083:
            if (r0 != r2) goto L_0x00db
            float r0 = r7.getTranslationY()
            float r0 = -r0
            r1 = 1092616192(0x41200000, float:10.0)
            android.content.res.Resources r4 = r7.getResources()
            android.util.DisplayMetrics r4 = r4.getDisplayMetrics()
            float r1 = android.util.TypedValue.applyDimension(r2, r1, r4)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x00d2
            com.android.keyguard.KeyguardSecurityContainer$SwipeListener r7 = r7.mSwipeListener
            if (r7 == 0) goto L_0x00db
            com.android.keyguard.KeyguardSecurityContainerController$3 r7 = (com.android.keyguard.KeyguardSecurityContainerController.AnonymousClass3) r7
            com.android.keyguard.KeyguardSecurityContainerController r8 = com.android.keyguard.KeyguardSecurityContainerController.this
            com.android.keyguard.KeyguardUpdateMonitor r8 = r8.mUpdateMonitor
            java.util.Objects.requireNonNull(r8)
            int r8 = r8.mFaceRunningState
            if (r8 != r2) goto L_0x00ae
            r3 = r2
        L_0x00ae:
            if (r3 != 0) goto L_0x00db
            com.android.keyguard.KeyguardSecurityContainerController r8 = com.android.keyguard.KeyguardSecurityContainerController.this
            com.android.keyguard.KeyguardUpdateMonitor r8 = r8.mUpdateMonitor
            r8.requestFaceAuth(r2)
            com.android.keyguard.KeyguardSecurityContainerController r8 = com.android.keyguard.KeyguardSecurityContainerController.this
            com.android.keyguard.KeyguardSecurityContainerController$2 r8 = r8.mKeyguardSecurityCallback
            r8.userActivity()
            com.android.keyguard.KeyguardSecurityContainerController r7 = com.android.keyguard.KeyguardSecurityContainerController.this
            r8 = 0
            java.util.Objects.requireNonNull(r7)
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r0 = r7.mCurrentSecurityMode
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.None
            if (r0 == r1) goto L_0x00db
            com.android.keyguard.KeyguardInputViewController r7 = r7.getCurrentSecurityController()
            r7.showMessage(r8, r8)
            goto L_0x00db
        L_0x00d2:
            boolean r0 = r7.mIsDragging
            if (r0 != 0) goto L_0x00db
            com.android.keyguard.KeyguardSecurityContainer$ViewMode r7 = r7.mViewMode
            r7.handleTap(r8)
        L_0x00db:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainer.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
