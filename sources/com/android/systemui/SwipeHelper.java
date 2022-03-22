package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public class SwipeHelper implements Gefingerpoken {
    public final Callback mCallback;
    public boolean mCanCurrViewBeDimissed;
    public float mDensityScale;
    public final boolean mFadeDependingOnAmountSwiped;
    public final FalsingManager mFalsingManager;
    public final int mFalsingThreshold;
    public final FeatureFlags mFeatureFlags;
    public final FlingAnimationUtils mFlingAnimationUtils;
    public float mInitialTouchPos;
    public boolean mIsSwiping;
    public boolean mLongPressSent;
    public boolean mMenuRowIntercepting;
    public float mPagingTouchSlop;
    public float mPerpendicularInitialTouchPos;
    public final float mSlopMultiplier;
    public boolean mSnappingChild;
    public final int mSwipeDirection;
    public boolean mTouchAboveFalsingThreshold;
    public int mTouchSlop;
    public ExpandableView mTouchedView;
    public float mTranslation = 0.0f;
    public final float[] mDownLocation = new float[2];
    public final AnonymousClass1 mPerformLongPress = new AnonymousClass1();
    public final ArrayMap<View, Animator> mDismissPendingMap = new ArrayMap<>();
    public final Handler mHandler = new Handler();
    public final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    public float mTouchSlopMultiplier = ViewConfiguration.getAmbiguousGestureMultiplier();
    public final long mLongPressTimeout = ViewConfiguration.getLongPressTimeout() * 1.5f;

    /* renamed from: com.android.systemui.SwipeHelper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        public final int[] mViewOffset = new int[2];

        public AnonymousClass1() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0074, code lost:
            if (r0 == false) goto L_0x0078;
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                r7 = this;
                com.android.systemui.SwipeHelper r0 = com.android.systemui.SwipeHelper.this
                com.android.systemui.statusbar.notification.row.ExpandableView r1 = r0.mTouchedView
                if (r1 == 0) goto L_0x0089
                boolean r2 = r0.mLongPressSent
                if (r2 != 0) goto L_0x0089
                r2 = 1
                r0.mLongPressSent = r2
                boolean r0 = r1 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
                if (r0 == 0) goto L_0x0089
                int[] r0 = r7.mViewOffset
                r1.getLocationOnScreen(r0)
                com.android.systemui.SwipeHelper r0 = com.android.systemui.SwipeHelper.this
                float[] r1 = r0.mDownLocation
                r3 = 0
                r4 = r1[r3]
                int r4 = (int) r4
                int[] r5 = r7.mViewOffset
                r6 = r5[r3]
                int r4 = r4 - r6
                r1 = r1[r2]
                int r1 = (int) r1
                r5 = r5[r2]
                int r1 = r1 - r5
                com.android.systemui.statusbar.notification.row.ExpandableView r0 = r0.mTouchedView
                r5 = 2
                r0.sendAccessibilityEvent(r5)
                com.android.systemui.SwipeHelper r0 = com.android.systemui.SwipeHelper.this
                com.android.systemui.statusbar.notification.row.ExpandableView r0 = r0.mTouchedView
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r0
                r0.doLongClickCallback(r4, r1)
                com.android.systemui.SwipeHelper r0 = com.android.systemui.SwipeHelper.this
                com.android.systemui.statusbar.notification.row.ExpandableView r1 = r0.mTouchedView
                com.android.systemui.flags.FeatureFlags r0 = r0.mFeatureFlags
                com.android.systemui.flags.ResourceBooleanFlag r4 = com.android.systemui.flags.Flags.NOTIFICATION_DRAG_TO_CONTENTS
                boolean r0 = r0.isEnabled(r4)
                if (r0 == 0) goto L_0x0077
                boolean r0 = r1 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
                if (r0 == 0) goto L_0x0077
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r1
                java.util.Objects.requireNonNull(r1)
                com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r1.mEntry
                java.util.Objects.requireNonNull(r0)
                android.service.notification.NotificationListenerService$Ranking r0 = r0.mRanking
                boolean r0 = r0.canBubble()
                com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r1.mEntry
                java.util.Objects.requireNonNull(r1)
                android.service.notification.StatusBarNotification r1 = r1.mSbn
                android.app.Notification r1 = r1.getNotification()
                android.app.PendingIntent r4 = r1.contentIntent
                if (r4 == 0) goto L_0x006a
                goto L_0x006c
            L_0x006a:
                android.app.PendingIntent r4 = r1.fullScreenIntent
            L_0x006c:
                if (r4 == 0) goto L_0x0077
                boolean r1 = r4.isActivity()
                if (r1 == 0) goto L_0x0077
                if (r0 != 0) goto L_0x0077
                goto L_0x0078
            L_0x0077:
                r2 = r3
            L_0x0078:
                if (r2 == 0) goto L_0x0089
                com.android.systemui.SwipeHelper r7 = com.android.systemui.SwipeHelper.this
                com.android.systemui.SwipeHelper$Callback r0 = r7.mCallback
                com.android.systemui.statusbar.notification.row.ExpandableView r7 = r7.mTouchedView
                com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$7 r0 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.AnonymousClass7) r0
                java.util.Objects.requireNonNull(r0)
                com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.this
                r0.mLongPressedView = r7
            L_0x0089:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.AnonymousClass1.run():void");
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public final void dismissChild(final View view, float f, final NotificationStackScrollLayout$$ExternalSyntheticLambda2 notificationStackScrollLayout$$ExternalSyntheticLambda2, long j, boolean z, long j2, boolean z2) {
        boolean z3;
        boolean z4;
        boolean z5;
        float f2;
        long j3;
        Animator animator;
        float f3;
        final boolean canChildBeDismissed = ((NotificationStackScrollLayoutController.AnonymousClass7) this.mCallback).canChildBeDismissed(view);
        boolean z6 = false;
        if (view.getLayoutDirection() == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i != 0 || ((getTranslation(view) != 0.0f && !z2) || this.mSwipeDirection != 1)) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (i != 0 || ((getTranslation(view) != 0.0f && !z2) || !z3)) {
            z5 = false;
        } else {
            z5 = true;
        }
        if ((Math.abs(f) > getEscapeVelocity() && f < 0.0f) || (getTranslation(view) < 0.0f && !z2)) {
            z6 = true;
        }
        if (z6 || z5 || z4) {
            NotificationStackScrollLayoutController.AnonymousClass7 r2 = (NotificationStackScrollLayoutController.AnonymousClass7) ((NotificationSwipeHelper) this).mCallback;
            Objects.requireNonNull(r2);
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayoutController.this.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            if (!notificationStackScrollLayout.mDismissUsingRowTranslationX) {
                f3 = view.getMeasuredWidth();
            } else {
                float measuredWidth = notificationStackScrollLayout.getMeasuredWidth();
                f3 = measuredWidth - ((measuredWidth - view.getMeasuredWidth()) / 2.0f);
            }
            f2 = -f3;
        } else {
            NotificationStackScrollLayoutController.AnonymousClass7 r22 = (NotificationStackScrollLayoutController.AnonymousClass7) ((NotificationSwipeHelper) this).mCallback;
            Objects.requireNonNull(r22);
            NotificationStackScrollLayout notificationStackScrollLayout2 = NotificationStackScrollLayoutController.this.mView;
            Objects.requireNonNull(notificationStackScrollLayout2);
            if (!notificationStackScrollLayout2.mDismissUsingRowTranslationX) {
                f2 = view.getMeasuredWidth();
            } else {
                float measuredWidth2 = notificationStackScrollLayout2.getMeasuredWidth();
                f2 = measuredWidth2 - ((measuredWidth2 - view.getMeasuredWidth()) / 2.0f);
            }
        }
        if (j2 != 0) {
            j3 = j2;
        } else if (i != 0) {
            j3 = Math.min(400L, (int) ((Math.abs(f2 - getTranslation(view)) * 1000.0f) / Math.abs(f)));
        } else {
            j3 = 200;
        }
        view.setLayerType(2, null);
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.SwipeHelper.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeHelper swipeHelper = SwipeHelper.this;
                View view2 = view;
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                boolean z7 = canChildBeDismissed;
                Objects.requireNonNull(swipeHelper);
                swipeHelper.updateSwipeProgressFromOffset(view2, z7, floatValue);
            }
        };
        NotificationSwipeHelper notificationSwipeHelper = (NotificationSwipeHelper) this;
        if (view instanceof ExpandableNotificationRow) {
            animator = ((ExpandableNotificationRow) view).getTranslateViewAnimator(f2, animatorUpdateListener);
        } else {
            animator = notificationSwipeHelper.superGetViewTranslationAnimator(view, f2, animatorUpdateListener);
        }
        if (animator == null) {
            InteractionJankMonitor.getInstance().end(4);
            return;
        }
        if (z) {
            animator.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
            animator.setDuration(j3);
        } else {
            this.mFlingAnimationUtils.applyDismissing(animator, getTranslation(view), f2, f, getSize(view));
        }
        if (j > 0) {
            animator.setStartDelay(j);
        }
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.SwipeHelper.3
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator2) {
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator2) {
                boolean z7;
                SwipeHelper swipeHelper = SwipeHelper.this;
                View view2 = view;
                boolean z8 = canChildBeDismissed;
                Objects.requireNonNull(swipeHelper);
                swipeHelper.updateSwipeProgressFromOffset(view2, z8, swipeHelper.getTranslation(view2));
                SwipeHelper.this.mDismissPendingMap.remove(view);
                View view3 = view;
                if (view3 instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view3;
                    Objects.requireNonNull(expandableNotificationRow);
                    z7 = expandableNotificationRow.mRemoved;
                } else {
                    z7 = false;
                }
                if (!this.mCancelled || z7) {
                    Callback callback = SwipeHelper.this.mCallback;
                    View view4 = view;
                    NotificationStackScrollLayoutController.AnonymousClass7 r6 = (NotificationStackScrollLayoutController.AnonymousClass7) callback;
                    Objects.requireNonNull(r6);
                    if (view4 instanceof ActivatableNotificationView) {
                        ActivatableNotificationView activatableNotificationView = (ActivatableNotificationView) view4;
                        Objects.requireNonNull(activatableNotificationView);
                        if (!activatableNotificationView.mDismissed) {
                            r6.handleChildViewDismissed(view4);
                        }
                        activatableNotificationView.removeFromTransientContainer();
                    }
                    SwipeHelper swipeHelper2 = SwipeHelper.this;
                    Objects.requireNonNull(swipeHelper2);
                    swipeHelper2.mTouchedView = null;
                    swipeHelper2.mIsSwiping = false;
                }
                Runnable runnable = notificationStackScrollLayout$$ExternalSyntheticLambda2;
                if (runnable != null) {
                    runnable.run();
                }
                Objects.requireNonNull(SwipeHelper.this);
                view.setLayerType(0, null);
                Objects.requireNonNull((NotificationSwipeHelper) SwipeHelper.this);
                InteractionJankMonitor.getInstance().end(4);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator2) {
                super.onAnimationStart(animator2);
                ((NotificationStackScrollLayoutController.AnonymousClass7) SwipeHelper.this.mCallback).onBeginDrag(view);
            }
        });
        this.mDismissPendingMap.put(view, animator);
        animator.start();
    }

    public void dismissChild(View view, float f, boolean z) {
        throw null;
    }

    public float getEscapeVelocity() {
        throw null;
    }

    public float getTranslation(View view) {
        throw null;
    }

    @Override // com.android.systemui.Gefingerpoken
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        throw null;
    }

    public void snapChild(View view, float f, float f2) {
        throw null;
    }

    public boolean swipedFarEnough() {
        throw null;
    }

    public boolean swipedFastEnough() {
        throw null;
    }

    public final void cancelLongPress() {
        this.mHandler.removeCallbacks(this.mPerformLongPress);
    }

    public final float getPos(MotionEvent motionEvent) {
        if (this.mSwipeDirection == 0) {
            return motionEvent.getX();
        }
        return motionEvent.getY();
    }

    public final float getSize(View view) {
        int i;
        if (this.mSwipeDirection == 0) {
            i = view.getMeasuredWidth();
        } else {
            i = view.getMeasuredHeight();
        }
        return i;
    }

    public final boolean isDismissGesture(MotionEvent motionEvent) {
        getTranslation(this.mTouchedView);
        if (motionEvent.getActionMasked() == 1 && !this.mFalsingManager.isUnlockingDisabled() && !isFalseGesture() && (swipedFastEnough() || swipedFarEnough())) {
            Callback callback = this.mCallback;
            ExpandableView expandableView = this.mTouchedView;
            NotificationStackScrollLayoutController.AnonymousClass7 r2 = (NotificationStackScrollLayoutController.AnonymousClass7) callback;
            Objects.requireNonNull(r2);
            if (r2.canChildBeDismissed(expandableView)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isFalseGesture() {
        NotificationStackScrollLayoutController.AnonymousClass7 r0 = (NotificationStackScrollLayoutController.AnonymousClass7) this.mCallback;
        Objects.requireNonNull(r0);
        boolean onKeyguard = NotificationStackScrollLayoutController.this.mView.onKeyguard();
        if (this.mFalsingManager.isClassifierEnabled()) {
            if (!onKeyguard || !this.mFalsingManager.isFalseTouch(1)) {
                return false;
            }
        } else if (!onKeyguard || this.mTouchAboveFalsingThreshold) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003d, code lost:
        if (r0 != 4) goto L_0x019f;
     */
    @Override // com.android.systemui.Gefingerpoken
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouchEvent(android.view.MotionEvent r9) {
        /*
            Method dump skipped, instructions count: 416
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.SwipeHelper.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public SwipeHelper(int i, Callback callback, Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags) {
        this.mCallback = callback;
        this.mSwipeDirection = i;
        this.mPagingTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mDensityScale = resources.getDisplayMetrics().density;
        this.mFalsingThreshold = resources.getDimensionPixelSize(2131167219);
        this.mFadeDependingOnAmountSwiped = resources.getBoolean(2131034136);
        this.mFalsingManager = falsingManager;
        this.mFeatureFlags = featureFlags;
        this.mFlingAnimationUtils = new FlingAnimationUtils(resources.getDisplayMetrics(), ((float) 400) / 1000.0f);
    }

    public final void updateSwipeProgressFromOffset(View view, boolean z, float f) {
        float f2;
        float min = Math.min(Math.max(0.0f, Math.abs(f / getSize(view))), 1.0f);
        NotificationStackScrollLayoutController.AnonymousClass7 r2 = (NotificationStackScrollLayoutController.AnonymousClass7) this.mCallback;
        Objects.requireNonNull(r2);
        if (!(!NotificationStackScrollLayoutController.this.mFadeNotificationsOnDismiss) && z) {
            if (min == 0.0f || min == 1.0f) {
                view.setLayerType(0, null);
            } else {
                view.setLayerType(2, null);
            }
            if (this.mFadeDependingOnAmountSwiped) {
                f2 = Math.max(1.0f - min, 0.0f);
            } else {
                f2 = 1.0f - Math.max(0.0f, Math.min(1.0f, min / 0.5f));
            }
            view.setAlpha(f2);
        }
        RectF rectF = new RectF(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        while (view.getParent() != null && (view.getParent() instanceof View)) {
            view = (View) view.getParent();
            view.getMatrix().mapRect(rectF);
            view.invalidate((int) Math.floor(rectF.left), (int) Math.floor(rectF.top), (int) Math.ceil(rectF.right), (int) Math.ceil(rectF.bottom));
        }
    }
}
