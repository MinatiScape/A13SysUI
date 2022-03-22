package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.SwipeHelper;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.StatusBar;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationSwipeHelper extends SwipeHelper implements NotificationSwipeActionHelper {
    @VisibleForTesting
    public static final long COVER_MENU_DELAY = 4000;
    public final NotificationCallback mCallback;
    public WeakReference<NotificationMenuRowPlugin> mCurrMenuRowRef;
    public final KeyguardUpdateMonitor$$ExternalSyntheticLambda6 mFalsingCheck = new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 6);
    public boolean mIsExpanded;
    public View mMenuExposedView;
    public final NotificationMenuRowPlugin.OnMenuEventListener mMenuListener;
    public boolean mPulsing;
    public View mTranslatingParentView;

    /* loaded from: classes.dex */
    public interface NotificationCallback extends SwipeHelper.Callback {
    }

    public NotificationSwipeHelper(Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags, int i, NotificationCallback notificationCallback, NotificationMenuRowPlugin.OnMenuEventListener onMenuEventListener) {
        super(i, notificationCallback, resources, viewConfiguration, falsingManager, featureFlags);
        this.mMenuListener = onMenuEventListener;
        this.mCallback = notificationCallback;
    }

    public static boolean isTouchInView(MotionEvent motionEvent, View view) {
        int i;
        if (view == null) {
            return false;
        }
        if (view instanceof ExpandableView) {
            i = ((ExpandableView) view).mActualHeight;
        } else {
            i = view.getHeight();
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i2 = iArr[0];
        int i3 = iArr[1];
        return new Rect(i2, i3, view.getWidth() + i2, i + i3).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
    }

    @VisibleForTesting
    public void snapClosed(View view, float f) {
        snapChild(view, 0.0f, f);
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper
    public final void snapOpen(View view, int i, float f) {
        snapChild(view, i, f);
    }

    @VisibleForTesting
    public void superDismissChild(View view, float f, boolean z) {
        dismissChild(view, f, null, 0L, z, 0L, false);
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper
    public final boolean swipedFarEnough(float f, float f2) {
        return swipedFarEnough();
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper
    public final boolean swipedFastEnough(float f, float f2) {
        return swipedFastEnough();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public final FalsingManager mFalsingManager;
        public final FeatureFlags mFeatureFlags;
        public NotificationMenuRowPlugin.OnMenuEventListener mOnMenuEventListener;
        public final Resources mResources;
        public final ViewConfiguration mViewConfiguration;

        public Builder(Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager, FeatureFlags featureFlags) {
            this.mResources = resources;
            this.mViewConfiguration = viewConfiguration;
            this.mFalsingManager = falsingManager;
            this.mFeatureFlags = featureFlags;
        }
    }

    public final NotificationMenuRowPlugin getCurrentMenuRow() {
        WeakReference<NotificationMenuRowPlugin> weakReference = this.mCurrMenuRowRef;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    @Override // com.android.systemui.SwipeHelper
    @VisibleForTesting
    public float getEscapeVelocity() {
        return this.mDensityScale * 500.0f;
    }

    @Override // com.android.systemui.SwipeHelper
    public final float getTranslation(View view) {
        if (view instanceof SwipeableView) {
            return ((SwipeableView) view).getTranslation();
        }
        return 0.0f;
    }

    @VisibleForTesting
    public void handleMenuCoveredOrDismissed() {
        View view = this.mMenuExposedView;
        if (view != null && view == this.mTranslatingParentView) {
            this.mMenuExposedView = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0028, code lost:
        if (r1 != 3) goto L_0x016c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00d5, code lost:
        if (r10 != false) goto L_0x017a;
     */
    @Override // com.android.systemui.SwipeHelper, com.android.systemui.Gefingerpoken
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r10) {
        /*
            Method dump skipped, instructions count: 397
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @VisibleForTesting
    public void setCurrentMenuRow(NotificationMenuRowPlugin notificationMenuRowPlugin) {
        WeakReference<NotificationMenuRowPlugin> weakReference;
        if (notificationMenuRowPlugin != null) {
            weakReference = new WeakReference<>(notificationMenuRowPlugin);
        } else {
            weakReference = null;
        }
        this.mCurrMenuRowRef = weakReference;
    }

    @VisibleForTesting
    public boolean shouldResetMenu(boolean z) {
        View view = this.mMenuExposedView;
        if (view == null) {
            return false;
        }
        if (z || view != this.mTranslatingParentView) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper
    public final void snooze(StatusBarNotification statusBarNotification, NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        NotificationStackScrollLayoutController.AnonymousClass7 r0 = (NotificationStackScrollLayoutController.AnonymousClass7) this.mCallback;
        Objects.requireNonNull(r0);
        StatusBar statusBar = NotificationStackScrollLayoutController.this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mNotificationsController.setNotificationSnoozed(statusBarNotification, snoozeOption);
    }

    @VisibleForTesting
    public Animator superGetViewTranslationAnimator(View view, float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        Property property;
        if (this.mSwipeDirection == 0) {
            property = View.TRANSLATION_X;
        } else {
            property = View.TRANSLATION_Y;
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, f);
        if (animatorUpdateListener != null) {
            ofFloat.addUpdateListener(animatorUpdateListener);
        }
        return ofFloat;
    }

    @VisibleForTesting
    public void superSnapChild(final View view, float f, float f2) {
        Animator animator;
        final boolean canChildBeDismissed = ((NotificationStackScrollLayoutController.AnonymousClass7) super.mCallback).canChildBeDismissed(view);
        ValueAnimator.AnimatorUpdateListener swipeHelper$$ExternalSyntheticLambda0 = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.SwipeHelper$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwipeHelper swipeHelper = SwipeHelper.this;
                View view2 = view;
                boolean z = canChildBeDismissed;
                Objects.requireNonNull(swipeHelper);
                swipeHelper.updateSwipeProgressFromOffset(view2, z, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        };
        boolean z = view instanceof ExpandableNotificationRow;
        if (z) {
            animator = ((ExpandableNotificationRow) view).getTranslateViewAnimator(f, swipeHelper$$ExternalSyntheticLambda0);
        } else {
            animator = superGetViewTranslationAnimator(view, f, swipeHelper$$ExternalSyntheticLambda0);
        }
        if (animator == null) {
            InteractionJankMonitor.getInstance().end(4);
            return;
        }
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.SwipeHelper.4
            public boolean wasCancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator2) {
                this.wasCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator2) {
                SwipeHelper swipeHelper = SwipeHelper.this;
                swipeHelper.mSnappingChild = false;
                if (!this.wasCancelled) {
                    View view2 = view;
                    swipeHelper.updateSwipeProgressFromOffset(view2, canChildBeDismissed, swipeHelper.getTranslation(view2));
                    SwipeHelper swipeHelper2 = SwipeHelper.this;
                    Objects.requireNonNull(swipeHelper2);
                    swipeHelper2.mTouchedView = null;
                    swipeHelper2.mIsSwiping = false;
                }
                Objects.requireNonNull((NotificationSwipeHelper) SwipeHelper.this);
                InteractionJankMonitor.getInstance().end(4);
            }
        });
        this.mSnappingChild = true;
        this.mFlingAnimationUtils.apply(animator, getTranslation(view), f, f2, Math.abs(f - getTranslation(view)));
        animator.start();
        NotificationStackScrollLayoutController.AnonymousClass7 r9 = (NotificationStackScrollLayoutController.AnonymousClass7) super.mCallback;
        Objects.requireNonNull(r9);
        NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayoutController.this.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.updateFirstAndLastBackgroundViews();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationStackScrollLayout.mController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        notificationStackScrollLayoutController.mNotificationRoundnessManager.setViewsAffectedBySwipe(null, null, null);
        notificationStackScrollLayout.mShelf.updateAppearance();
        if (z) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mIsPinned && !r9.canChildBeDismissed(expandableNotificationRow)) {
                NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
                Objects.requireNonNull(notificationEntry);
                if (notificationEntry.mSbn.getNotification().fullScreenIntent == null) {
                    HeadsUpManagerPhone headsUpManagerPhone = NotificationStackScrollLayoutController.this.mHeadsUpManager;
                    NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
                    Objects.requireNonNull(notificationEntry2);
                    headsUpManagerPhone.removeNotification(notificationEntry2.mSbn.getKey(), true);
                }
            }
        }
    }

    @Override // com.android.systemui.SwipeHelper
    @VisibleForTesting
    public boolean swipedFarEnough() {
        return Math.abs(getTranslation(this.mTouchedView)) > getSize(this.mTouchedView) * 0.6f;
    }

    @Override // com.android.systemui.SwipeHelper
    @VisibleForTesting
    public boolean swipedFastEnough() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        float xVelocity = this.mSwipeDirection == 0 ? velocityTracker.getXVelocity() : velocityTracker.getYVelocity();
        float translation = getTranslation(this.mTouchedView);
        if (Math.abs(xVelocity) > getEscapeVelocity()) {
            if ((xVelocity > 0.0f) == (translation > 0.0f)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper
    public final void dismiss(View view, float f) {
        dismissChild(view, f, !swipedFastEnough());
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0034  */
    @Override // com.android.systemui.SwipeHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void dismissChild(android.view.View r3, float r4, boolean r5) {
        /*
            r2 = this;
            r2.superDismissChild(r3, r4, r5)
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper$NotificationCallback r4 = r2.mCallback
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$7 r4 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.AnonymousClass7) r4
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r5 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.this
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r5 = r5.mView
            java.util.Objects.requireNonNull(r5)
            boolean r5 = r5.mIsExpanded
            r0 = 1
            r1 = 0
            if (r5 == 0) goto L_0x0031
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r4 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.this
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r4 = r4.mView
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.notification.stack.AmbientState r4 = r4.mAmbientState
            java.util.Objects.requireNonNull(r4)
            float r4 = r4.mDozeAmount
            r5 = 0
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 != 0) goto L_0x002c
            r4 = r0
            goto L_0x002d
        L_0x002c:
            r4 = r1
        L_0x002d:
            if (r4 == 0) goto L_0x0031
            r4 = r0
            goto L_0x0032
        L_0x0031:
            r4 = r1
        L_0x0032:
            if (r4 == 0) goto L_0x003b
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper$NotificationCallback r4 = r2.mCallback
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$7 r4 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.AnonymousClass7) r4
            r4.handleChildViewDismissed(r3)
        L_0x003b:
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper$NotificationCallback r3 = r2.mCallback
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$7 r3 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.AnonymousClass7) r3
            java.util.Objects.requireNonNull(r3)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r3 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.this
            com.android.systemui.statusbar.notification.row.NotificationGutsManager r3 = r3.mNotificationGutsManager
            r3.closeAndSaveGuts(r0, r1, r1, r1)
            r2.handleMenuCoveredOrDismissed()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper.dismissChild(android.view.View, float, boolean):void");
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper
    public final float getMinDismissVelocity() {
        return getEscapeVelocity();
    }

    @VisibleForTesting
    public void handleMenuRowSwipe(MotionEvent motionEvent, View view, float f, NotificationMenuRowPlugin notificationMenuRowPlugin) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        if (!notificationMenuRowPlugin.shouldShowMenu()) {
            if (isDismissGesture(motionEvent)) {
                dismiss(view, f);
                return;
            }
            snapClosed(view, f);
            notificationMenuRowPlugin.onSnapClosed();
        } else if (notificationMenuRowPlugin.isSnappedAndOnSameSide()) {
            boolean isDismissGesture = isDismissGesture(motionEvent);
            if (notificationMenuRowPlugin.isWithinSnapMenuThreshold() && !isDismissGesture) {
                notificationMenuRowPlugin.onSnapOpen();
                snapChild(view, notificationMenuRowPlugin.getMenuSnapTarget(), f);
            } else if (!isDismissGesture || notificationMenuRowPlugin.shouldSnapBack()) {
                snapClosed(view, f);
                notificationMenuRowPlugin.onSnapClosed();
            } else {
                dismiss(view, f);
                notificationMenuRowPlugin.onDismiss();
            }
        } else {
            boolean isDismissGesture2 = isDismissGesture(motionEvent);
            boolean isTowardsMenu = notificationMenuRowPlugin.isTowardsMenu(f);
            boolean z10 = true;
            if (getEscapeVelocity() <= Math.abs(f)) {
                z = true;
            } else {
                z = false;
            }
            double eventTime = motionEvent.getEventTime() - motionEvent.getDownTime();
            if (notificationMenuRowPlugin.canBeDismissed() || eventTime < 200.0d) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (!isTowardsMenu || isDismissGesture2) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (!z || z2) {
                z4 = true;
            } else {
                z4 = false;
            }
            if (swipedFarEnough() || !notificationMenuRowPlugin.isSwipedEnoughToShowMenu()) {
                z5 = false;
            } else {
                z5 = true;
            }
            if (!z5 || !z4) {
                z6 = false;
            } else {
                z6 = true;
            }
            if (!z || isTowardsMenu || isDismissGesture2) {
                z7 = false;
            } else {
                z7 = true;
            }
            if (notificationMenuRowPlugin.shouldShowGutsOnSnapOpen() || (this.mIsExpanded && !this.mPulsing)) {
                z8 = true;
            } else {
                z8 = false;
            }
            if (z6 || (z7 && z8)) {
                z9 = true;
            } else {
                z9 = false;
            }
            int menuSnapTarget = notificationMenuRowPlugin.getMenuSnapTarget();
            if (isFalseGesture() || !z9) {
                z10 = false;
            }
            if ((z3 || z10) && menuSnapTarget != 0) {
                snapChild(view, menuSnapTarget, f);
                notificationMenuRowPlugin.onSnapOpen();
            } else if (!isDismissGesture(motionEvent) || isTowardsMenu) {
                snapClosed(view, f);
                notificationMenuRowPlugin.onSnapClosed();
            } else {
                dismiss(view, f);
                notificationMenuRowPlugin.onDismiss();
            }
        }
    }

    @VisibleForTesting
    public void initializeRow(SwipeableView swipeableView) {
        if (swipeableView.hasFinishedInitialization()) {
            NotificationMenuRowPlugin createMenu = swipeableView.createMenu();
            setCurrentMenuRow(createMenu);
            if (createMenu != null) {
                createMenu.setMenuClickListener(this.mMenuListener);
                createMenu.onTouchStart();
            }
        }
    }

    public final void resetExposedMenuView(boolean z, boolean z2) {
        Animator animator;
        if (shouldResetMenu(z2)) {
            View view = this.mMenuExposedView;
            if (z) {
                if (view instanceof ExpandableNotificationRow) {
                    animator = ((ExpandableNotificationRow) view).getTranslateViewAnimator(0.0f, null);
                } else {
                    animator = superGetViewTranslationAnimator(view, 0.0f, null);
                }
                if (animator != null) {
                    animator.start();
                }
            } else if (view instanceof SwipeableView) {
                SwipeableView swipeableView = (SwipeableView) view;
                if (!swipeableView.isRemoved()) {
                    swipeableView.resetTranslation();
                }
            }
            this.mMenuExposedView = null;
        }
    }

    @Override // com.android.systemui.SwipeHelper
    public final void snapChild(View view, float f, float f2) {
        superSnapChild(view, f, f2);
        NotificationStackScrollLayoutController.AnonymousClass7 r1 = (NotificationStackScrollLayoutController.AnonymousClass7) this.mCallback;
        Objects.requireNonNull(r1);
        NotificationStackScrollLayoutController.this.mFalsingCollector.onNotificationStopDismissing();
        if (f == 0.0f) {
            handleMenuCoveredOrDismissed();
        }
    }

    @VisibleForTesting
    public void setTranslatingParentView(View view) {
        this.mTranslatingParentView = view;
    }

    @VisibleForTesting
    public Runnable getFalsingCheck() {
        return this.mFalsingCheck;
    }

    @VisibleForTesting
    public Handler getHandler() {
        return this.mHandler;
    }
}
