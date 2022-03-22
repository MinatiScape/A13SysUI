package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;
/* loaded from: classes.dex */
public final class StackStateAnimator {
    public ValueAnimator mBottomOverScrollAnimator;
    public long mCurrentAdditionalDelay;
    public long mCurrentLength;
    public final int mGoToFullShadeAppearingTranslation;
    public int mHeadsUpAppearHeightBottom;
    public NotificationStackScrollLayout mHostLayout;
    public StackStateLogger mLogger;
    public boolean mShadeExpanded;
    public NotificationShelf mShelf;
    public ValueAnimator mTopOverScrollAnimator;
    public final ExpandableViewState mTmpState = new ExpandableViewState();
    public ArrayList<NotificationStackScrollLayout.AnimationEvent> mNewEvents = new ArrayList<>();
    public ArrayList<View> mNewAddChildren = new ArrayList<>();
    public HashSet<View> mHeadsUpAppearChildren = new HashSet<>();
    public HashSet<View> mHeadsUpDisappearChildren = new HashSet<>();
    public HashSet<Animator> mAnimatorSet = new HashSet<>();
    public Stack<AnimatorListenerAdapter> mAnimationListenerPool = new Stack<>();
    public AnimationFilter mAnimationFilter = new AnimationFilter();
    public ArrayList<ExpandableView> mTransientViewsToRemove = new ArrayList<>();
    public int[] mTmpLocation = new int[2];
    public final AnonymousClass1 mAnimationProperties = new AnonymousClass1();

    /* renamed from: com.android.systemui.statusbar.notification.stack.StackStateAnimator$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimationProperties {
        public AnonymousClass1() {
            StackStateAnimator.this = r1;
        }

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public final AnimationFilter getAnimationFilter() {
            return StackStateAnimator.this.mAnimationFilter;
        }

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public final AnimatorListenerAdapter getAnimationFinishListener(Property property) {
            StackStateAnimator stackStateAnimator = StackStateAnimator.this;
            Objects.requireNonNull(stackStateAnimator);
            if (!stackStateAnimator.mAnimationListenerPool.empty()) {
                return stackStateAnimator.mAnimationListenerPool.pop();
            }
            return new AnonymousClass2();
        }

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public final boolean wasAdded(View view) {
            return StackStateAnimator.this.mNewAddChildren.contains(view);
        }
    }

    /* renamed from: com.android.systemui.statusbar.notification.stack.StackStateAnimator$2 */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 extends AnimatorListenerAdapter {
        public boolean mWasCancelled;

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            this.mWasCancelled = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationStart(Animator animator) {
            this.mWasCancelled = false;
            StackStateAnimator.this.mAnimatorSet.add(animator);
        }

        public AnonymousClass2() {
            StackStateAnimator.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            StackStateAnimator.this.mAnimatorSet.remove(animator);
            if (StackStateAnimator.this.mAnimatorSet.isEmpty() && !this.mWasCancelled) {
                StackStateAnimator.this.onAnimationFinished();
            }
            StackStateAnimator.this.mAnimationListenerPool.push(this);
        }
    }

    public final void onAnimationFinished() {
        NotificationStackScrollLayout notificationStackScrollLayout = this.mHostLayout;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.setAnimationRunning(false);
        notificationStackScrollLayout.requestChildrenUpdate();
        Iterator<Runnable> it = notificationStackScrollLayout.mAnimationFinishedRunnables.iterator();
        while (it.hasNext()) {
            it.next().run();
        }
        notificationStackScrollLayout.mAnimationFinishedRunnables.clear();
        Iterator<ExpandableView> it2 = notificationStackScrollLayout.mClearTransientViewsWhenFinished.iterator();
        while (it2.hasNext()) {
            it2.next().removeFromTransientContainer();
        }
        notificationStackScrollLayout.mClearTransientViewsWhenFinished.clear();
        for (int i = 0; i < notificationStackScrollLayout.getChildCount(); i++) {
            View childAt = notificationStackScrollLayout.getChildAt(i);
            if (childAt instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                expandableNotificationRow.setHeadsUpAnimatingAway(false);
                if (expandableNotificationRow.mIsSummaryWithChildren) {
                    for (ExpandableNotificationRow expandableNotificationRow2 : expandableNotificationRow.getAttachedChildren()) {
                        expandableNotificationRow2.setHeadsUpAnimatingAway(false);
                    }
                }
            }
        }
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (ambientState.mClearAllInProgress) {
            notificationStackScrollLayout.setClearAllInProgress(false);
            if (notificationStackScrollLayout.mShadeNeedsToClose) {
                notificationStackScrollLayout.mShadeNeedsToClose = false;
                notificationStackScrollLayout.postDelayed(new QSAnimator$$ExternalSyntheticLambda0(notificationStackScrollLayout, 3), 200L);
            }
        }
        Iterator<ExpandableView> it3 = this.mTransientViewsToRemove.iterator();
        while (it3.hasNext()) {
            it3.next().removeFromTransientContainer();
        }
        this.mTransientViewsToRemove.clear();
    }

    public static void $r8$lambda$61WUm2lxj80T6Ev5pK6kC2FMdCY(StackStateAnimator stackStateAnimator, String str) {
        Objects.requireNonNull(stackStateAnimator);
        StackStateLogger stackStateLogger = stackStateAnimator.mLogger;
        Objects.requireNonNull(stackStateLogger);
        LogBuffer logBuffer = stackStateLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        StackStateLogger$appearAnimationEnded$2 stackStateLogger$appearAnimationEnded$2 = StackStateLogger$appearAnimationEnded$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("StackScroll", logLevel, stackStateLogger$appearAnimationEnded$2);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public StackStateAnimator(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mHostLayout = notificationStackScrollLayout;
        this.mGoToFullShadeAppearingTranslation = notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(2131165790);
        notificationStackScrollLayout.getContext().getResources().getDimensionPixelSize(2131166836);
    }
}
