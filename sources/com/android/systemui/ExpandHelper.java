package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.FloatProperty;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.policy.ScrollAdapter;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ExpandHelper implements Gefingerpoken {
    public static final AnonymousClass1 VIEW_SCALER_HEIGHT_PROPERTY = new FloatProperty<ViewScaler>() { // from class: com.android.systemui.ExpandHelper.1
        @Override // android.util.Property
        public final Float get(Object obj) {
            ViewScaler viewScaler = (ViewScaler) obj;
            Objects.requireNonNull(viewScaler);
            ExpandableView expandableView = viewScaler.mView;
            Objects.requireNonNull(expandableView);
            return Float.valueOf(expandableView.mActualHeight);
        }

        @Override // android.util.FloatProperty
        public final void setValue(ViewScaler viewScaler, float f) {
            viewScaler.setHeight(f);
        }
    };
    public Callback mCallback;
    public Context mContext;
    public float mCurrentHeight;
    public View mEventSource;
    public boolean mExpanding;
    public FlingAnimationUtils mFlingAnimationUtils;
    public boolean mHasPopped;
    public float mInitialTouchFocusY;
    public float mInitialTouchSpan;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public float mLastFocusY;
    public float mLastMotionY;
    public float mLastSpanY;
    public float mNaturalHeight;
    public float mOldHeight;
    public boolean mOnlyMovements;
    public float mPullGestureMinXSpan;
    public ExpandableView mResizedView;
    public ScaleGestureDetector mSGD;
    public ObjectAnimator mScaleAnimation;
    public ViewScaler mScaler;
    public ScrollAdapter mScrollAdapter;
    public int mSmallSize;
    public final int mTouchSlop;
    public VelocityTracker mVelocityTracker;
    public boolean mWatchingForPull;
    public int mExpansionStyle = 0;
    public boolean mEnabled = true;
    public AnonymousClass2 mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.android.systemui.ExpandHelper.2
        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public final boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public final void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public final boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ExpandHelper expandHelper = ExpandHelper.this;
            if (!expandHelper.mOnlyMovements) {
                expandHelper.startExpanding(expandHelper.mResizedView, 4);
            }
            return ExpandHelper.this.mExpanding;
        }
    };
    public int mGravity = 48;
    public final float mSlopMultiplier = ViewConfiguration.getAmbiguousGestureMultiplier();

    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* loaded from: classes.dex */
    public class ViewScaler {
        public ExpandableView mView;

        public ViewScaler() {
        }

        public final void setHeight(float f) {
            ExpandableView expandableView = this.mView;
            Objects.requireNonNull(expandableView);
            expandableView.setActualHeight((int) f, true);
            ExpandHelper.this.mCurrentHeight = f;
        }
    }

    @VisibleForTesting
    public void finishExpanding(boolean z, float f) {
        finishExpanding(z, f, true);
    }

    public final boolean isInside(NotificationStackScrollLayout notificationStackScrollLayout, float f, float f2) {
        int[] iArr;
        boolean z;
        boolean z2;
        int[] iArr2;
        if (notificationStackScrollLayout == null) {
            return false;
        }
        View view = this.mEventSource;
        if (view != null) {
            view.getLocationOnScreen(new int[2]);
            f += iArr2[0];
            f2 += iArr2[1];
        }
        notificationStackScrollLayout.getLocationOnScreen(new int[2]);
        float f3 = f - iArr[0];
        float f4 = f2 - iArr[1];
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return false;
        }
        if (f3 < notificationStackScrollLayout.getWidth()) {
            z = true;
        } else {
            z = false;
        }
        if (f4 < notificationStackScrollLayout.getHeight()) {
            z2 = true;
        } else {
            z2 = false;
        }
        return z & z2;
    }

    public final ExpandableView findView(float f, float f2) {
        View view = this.mEventSource;
        if (view != null) {
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            NotificationStackScrollLayout.AnonymousClass11 r3 = (NotificationStackScrollLayout.AnonymousClass11) this.mCallback;
            Objects.requireNonNull(r3);
            return NotificationStackScrollLayout.this.getChildAtRawPosition(f + iArr[0], f2 + iArr[1]);
        }
        NotificationStackScrollLayout.AnonymousClass11 r32 = (NotificationStackScrollLayout.AnonymousClass11) this.mCallback;
        Objects.requireNonNull(r32);
        NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
        boolean z = NotificationStackScrollLayout.SPEW;
        Objects.requireNonNull(notificationStackScrollLayout);
        return notificationStackScrollLayout.getChildAtPosition(f, f2, true, true);
    }

    public final void finishExpanding(boolean z, float f, boolean z2) {
        final boolean z3;
        if (this.mExpanding) {
            ViewScaler viewScaler = this.mScaler;
            Objects.requireNonNull(viewScaler);
            ExpandableView expandableView = viewScaler.mView;
            Objects.requireNonNull(expandableView);
            float f2 = expandableView.mActualHeight;
            float f3 = this.mOldHeight;
            float f4 = this.mSmallSize;
            boolean z4 = true;
            final boolean z5 = f3 == f4;
            if (!z) {
                int i = (f2 > f3 ? 1 : (f2 == f3 ? 0 : -1));
                z3 = (!z5 ? i >= 0 || f > 0.0f : i > 0 && f >= 0.0f) | (this.mNaturalHeight == f4);
            } else {
                z3 = !z5;
            }
            if (this.mScaleAnimation.isRunning()) {
                this.mScaleAnimation.cancel();
            }
            ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).expansionStateChanged(false);
            ViewScaler viewScaler2 = this.mScaler;
            Objects.requireNonNull(viewScaler2);
            Callback callback = ExpandHelper.this.mCallback;
            ExpandableView expandableView2 = viewScaler2.mView;
            Objects.requireNonNull((NotificationStackScrollLayout.AnonymousClass11) callback);
            int maxContentHeight = expandableView2.getMaxContentHeight();
            if (!z3) {
                maxContentHeight = this.mSmallSize;
            }
            float f5 = maxContentHeight;
            int i2 = (f5 > f2 ? 1 : (f5 == f2 ? 0 : -1));
            if (i2 == 0 || !this.mEnabled || !z2) {
                if (i2 != 0) {
                    this.mScaler.setHeight(f5);
                }
                ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).setUserExpandedChild(this.mResizedView, z3);
                ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).setUserLockedChild(this.mResizedView, false);
                ViewScaler viewScaler3 = this.mScaler;
                Objects.requireNonNull(viewScaler3);
                viewScaler3.mView = null;
                if (z5) {
                    InteractionJankMonitor.getInstance().end(3);
                }
            } else {
                this.mScaleAnimation.setFloatValues(f5);
                this.mScaleAnimation.setupStartValues();
                final ExpandableView expandableView3 = this.mResizedView;
                this.mScaleAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.ExpandHelper.3
                    public boolean mCancelled;

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationCancel(Animator animator) {
                        this.mCancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        if (!this.mCancelled) {
                            ((NotificationStackScrollLayout.AnonymousClass11) ExpandHelper.this.mCallback).setUserExpandedChild(expandableView3, z3);
                            ExpandHelper expandHelper = ExpandHelper.this;
                            if (!expandHelper.mExpanding) {
                                ViewScaler viewScaler4 = expandHelper.mScaler;
                                Objects.requireNonNull(viewScaler4);
                                viewScaler4.mView = null;
                            }
                        } else {
                            Callback callback2 = ExpandHelper.this.mCallback;
                            View view = expandableView3;
                            Objects.requireNonNull((NotificationStackScrollLayout.AnonymousClass11) callback2);
                            if (view instanceof ExpandableNotificationRow) {
                                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                                Objects.requireNonNull(expandableNotificationRow);
                                expandableNotificationRow.mGroupExpansionChanging = false;
                            }
                        }
                        ((NotificationStackScrollLayout.AnonymousClass11) ExpandHelper.this.mCallback).setUserLockedChild(expandableView3, false);
                        ExpandHelper.this.mScaleAnimation.removeListener(this);
                        if (z5) {
                            InteractionJankMonitor.getInstance().end(3);
                        }
                    }
                });
                if (f < 0.0f) {
                    z4 = false;
                }
                if (z3 != z4) {
                    f = 0.0f;
                }
                this.mFlingAnimationUtils.apply(this.mScaleAnimation, f2, f5, f);
                this.mScaleAnimation.start();
            }
            this.mExpanding = false;
            this.mExpansionStyle = 0;
        }
    }

    public final void maybeRecycleVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            return;
        }
        if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0053, code lost:
        if (r0 != 3) goto L_0x0145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x011c, code lost:
        if (r0 != false) goto L_0x0120;
     */
    @Override // com.android.systemui.Gefingerpoken
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r8) {
        /*
            Method dump skipped, instructions count: 337
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ExpandHelper.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // com.android.systemui.Gefingerpoken
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        float f;
        boolean z3;
        float f2;
        boolean z4;
        if (!this.mEnabled && !this.mExpanding) {
            return false;
        }
        trackVelocity(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        this.mSGD.onTouchEvent(motionEvent);
        int focusX = (int) this.mSGD.getFocusX();
        int focusY = (int) this.mSGD.getFocusY();
        if (this.mOnlyMovements) {
            this.mLastMotionY = motionEvent.getRawY();
            return false;
        }
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (this.mWatchingForPull) {
                        float rawY = motionEvent.getRawY() - this.mInitialTouchY;
                        float rawX = motionEvent.getRawX() - this.mInitialTouchX;
                        if (motionEvent.getClassification() == 1) {
                            f2 = this.mTouchSlop * this.mSlopMultiplier;
                        } else {
                            f2 = this.mTouchSlop;
                        }
                        if (rawY > f2 && rawY > Math.abs(rawX)) {
                            this.mWatchingForPull = false;
                            ExpandableView expandableView = this.mResizedView;
                            if (expandableView != null) {
                                if (expandableView.getIntrinsicHeight() != expandableView.getMaxContentHeight() || (expandableView.isSummaryWithChildren() && !expandableView.areChildrenExpanded())) {
                                    z4 = false;
                                } else {
                                    z4 = true;
                                }
                                if (!z4 && startExpanding(this.mResizedView, 1)) {
                                    this.mInitialTouchY = motionEvent.getRawY();
                                    this.mLastMotionY = motionEvent.getRawY();
                                    this.mHasPopped = false;
                                }
                            }
                        }
                    }
                    boolean z5 = this.mExpanding;
                    if (z5 && (this.mExpansionStyle & 1) != 0) {
                        float rawY2 = (motionEvent.getRawY() - this.mLastMotionY) + this.mCurrentHeight;
                        int i = this.mSmallSize;
                        float f3 = i;
                        if (rawY2 >= f3) {
                            f3 = rawY2;
                        }
                        float f4 = this.mNaturalHeight;
                        if (f3 > f4) {
                            f3 = f4;
                        }
                        if (rawY2 > f4) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        if (rawY2 < i) {
                            z3 = true;
                        }
                        if (!this.mHasPopped) {
                            View view = this.mEventSource;
                            if (view != null) {
                                view.performHapticFeedback(1);
                            }
                            this.mHasPopped = true;
                        }
                        this.mScaler.setHeight(f3);
                        this.mLastMotionY = motionEvent.getRawY();
                        if (z3) {
                            ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).expansionStateChanged(false);
                        } else {
                            ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).expansionStateChanged(true);
                        }
                        return true;
                    } else if (z5) {
                        updateExpansion();
                        this.mLastMotionY = motionEvent.getRawY();
                        return true;
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 5 || actionMasked == 6) {
                        this.mInitialTouchY = (this.mSGD.getFocusY() - this.mLastFocusY) + this.mInitialTouchY;
                        this.mInitialTouchSpan = (this.mSGD.getCurrentSpan() - this.mLastSpanY) + this.mInitialTouchSpan;
                    }
                }
            }
            if (!this.mEnabled || motionEvent.getActionMasked() == 3) {
                z2 = true;
            } else {
                z2 = false;
            }
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.computeCurrentVelocity(1000);
                f = this.mVelocityTracker.getYVelocity();
            } else {
                f = 0.0f;
            }
            finishExpanding(z2, f);
            this.mResizedView = null;
        } else {
            ScrollAdapter scrollAdapter = this.mScrollAdapter;
            if (scrollAdapter == null || !isInside(NotificationStackScrollLayout.this, focusX, focusY)) {
                z = false;
            } else {
                z = true;
            }
            this.mWatchingForPull = z;
            this.mResizedView = findView(focusX, focusY);
            this.mInitialTouchX = motionEvent.getRawX();
            this.mInitialTouchY = motionEvent.getRawY();
        }
        this.mLastMotionY = motionEvent.getRawY();
        maybeRecycleVelocityTracker(motionEvent);
        if (this.mResizedView != null) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean startExpanding(ExpandableView expandableView, int i) {
        if (!(expandableView instanceof ExpandableNotificationRow)) {
            return false;
        }
        this.mExpansionStyle = i;
        if (this.mExpanding && expandableView == this.mResizedView) {
            return true;
        }
        this.mExpanding = true;
        ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).expansionStateChanged(true);
        ((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).setUserLockedChild(expandableView, true);
        ViewScaler viewScaler = this.mScaler;
        Objects.requireNonNull(viewScaler);
        viewScaler.mView = expandableView;
        ViewScaler viewScaler2 = this.mScaler;
        Objects.requireNonNull(viewScaler2);
        ExpandableView expandableView2 = viewScaler2.mView;
        Objects.requireNonNull(expandableView2);
        float f = expandableView2.mActualHeight;
        this.mOldHeight = f;
        this.mCurrentHeight = f;
        if (((NotificationStackScrollLayout.AnonymousClass11) this.mCallback).canChildBeExpanded(expandableView)) {
            ViewScaler viewScaler3 = this.mScaler;
            Objects.requireNonNull(viewScaler3);
            Callback callback = ExpandHelper.this.mCallback;
            ExpandableView expandableView3 = viewScaler3.mView;
            Objects.requireNonNull((NotificationStackScrollLayout.AnonymousClass11) callback);
            this.mNaturalHeight = expandableView3.getMaxContentHeight();
            this.mSmallSize = expandableView.getCollapsedHeight();
        } else {
            this.mNaturalHeight = this.mOldHeight;
        }
        InteractionJankMonitor.getInstance().begin(expandableView, 3);
        return true;
    }

    @VisibleForTesting
    public void updateExpansion() {
        float f;
        float currentSpan = (this.mSGD.getCurrentSpan() - this.mInitialTouchSpan) * 1.0f;
        float focusY = (this.mSGD.getFocusY() - this.mInitialTouchFocusY) * 1.0f;
        if (this.mGravity == 80) {
            f = -1.0f;
        } else {
            f = 1.0f;
        }
        float f2 = focusY * f;
        float abs = Math.abs(currentSpan) + Math.abs(f2) + 1.0f;
        float abs2 = ((Math.abs(currentSpan) * currentSpan) / abs) + ((Math.abs(f2) * f2) / abs) + this.mOldHeight;
        float f3 = this.mSmallSize;
        if (abs2 < f3) {
            abs2 = f3;
        }
        float f4 = this.mNaturalHeight;
        if (abs2 > f4) {
            abs2 = f4;
        }
        this.mScaler.setHeight(abs2);
        this.mLastFocusY = this.mSGD.getFocusY();
        this.mLastSpanY = this.mSGD.getCurrentSpan();
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.ExpandHelper$2] */
    public ExpandHelper(Context context, Callback callback, int i) {
        this.mSmallSize = i;
        this.mContext = context;
        this.mCallback = callback;
        ViewScaler viewScaler = new ViewScaler();
        this.mScaler = viewScaler;
        this.mScaleAnimation = ObjectAnimator.ofFloat(viewScaler, VIEW_SCALER_HEIGHT_PROPERTY, 0.0f);
        this.mPullGestureMinXSpan = this.mContext.getResources().getDimension(2131166834);
        this.mTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
        this.mSGD = new ScaleGestureDetector(context, this.mScaleGestureListener);
        this.mFlingAnimationUtils = new FlingAnimationUtils(this.mContext.getResources().getDisplayMetrics(), 0.3f);
    }

    public final void trackVelocity(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                velocityTracker.clear();
            }
            this.mVelocityTracker.addMovement(motionEvent);
        } else if (actionMasked == 2) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
        }
    }

    @VisibleForTesting
    public ObjectAnimator getScaleAnimation() {
        return this.mScaleAnimation;
    }
}
