package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import java.util.Objects;
/* loaded from: classes.dex */
public class ExpandableViewState extends ViewState {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean belowSpeedBump;
    public int clipTopAmount;
    public boolean headsUpIsVisible;
    public int height;
    public boolean hideSensitive;
    public boolean inShelf;
    public int location;
    public int notGoneIndex;

    @Override // com.android.systemui.statusbar.notification.stack.ViewState
    public void animateTo(View view, AnimationProperties animationProperties) {
        super.animateTo(view, animationProperties);
        if (view instanceof ExpandableView) {
            final ExpandableView expandableView = (ExpandableView) view;
            StackStateAnimator.AnonymousClass1 r4 = (StackStateAnimator.AnonymousClass1) animationProperties;
            Objects.requireNonNull(r4);
            AnimationFilter animationFilter = StackStateAnimator.this.mAnimationFilter;
            if (this.height != expandableView.mActualHeight) {
                Integer num = (Integer) expandableView.getTag(2131428090);
                Integer num2 = (Integer) expandableView.getTag(2131428089);
                int i = this.height;
                if (num2 == null || num2.intValue() != i) {
                    ValueAnimator valueAnimator = (ValueAnimator) expandableView.getTag(2131428091);
                    if (StackStateAnimator.this.mAnimationFilter.animateHeight) {
                        ValueAnimator ofInt = ValueAnimator.ofInt(expandableView.mActualHeight, i);
                        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.stack.ExpandableViewState.1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                ExpandableView.this.setActualHeight(((Integer) valueAnimator2.getAnimatedValue()).intValue(), false);
                            }
                        });
                        ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                        ofInt.setDuration(ViewState.cancelAnimatorAndGetNewDuration(animationProperties.duration, valueAnimator));
                        if (animationProperties.delay > 0 && (valueAnimator == null || valueAnimator.getAnimatedFraction() == 0.0f)) {
                            ofInt.setStartDelay(animationProperties.delay);
                        }
                        AnimatorListenerAdapter animationFinishListener = animationProperties.getAnimationFinishListener(null);
                        if (animationFinishListener != null) {
                            ofInt.addListener(animationFinishListener);
                        }
                        ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.ExpandableViewState.2
                            public boolean mWasCancelled;

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationCancel(Animator animator) {
                                this.mWasCancelled = true;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationStart(Animator animator) {
                                this.mWasCancelled = false;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationEnd(Animator animator) {
                                ExpandableView expandableView2 = ExpandableView.this;
                                int i2 = ExpandableViewState.$r8$clinit;
                                expandableView2.setTag(2131428091, null);
                                ExpandableView.this.setTag(2131428090, null);
                                ExpandableView.this.setTag(2131428089, null);
                                ExpandableView.this.setActualHeightAnimating(false);
                                if (!this.mWasCancelled) {
                                    ExpandableView expandableView3 = ExpandableView.this;
                                    if (expandableView3 instanceof ExpandableNotificationRow) {
                                        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView3;
                                        Objects.requireNonNull(expandableNotificationRow);
                                        expandableNotificationRow.mGroupExpansionChanging = false;
                                    }
                                }
                            }
                        });
                        ViewState.startAnimator(ofInt, animationFinishListener);
                        expandableView.setTag(2131428091, ofInt);
                        expandableView.setTag(2131428090, Integer.valueOf(expandableView.mActualHeight));
                        expandableView.setTag(2131428089, Integer.valueOf(i));
                        expandableView.setActualHeightAnimating(true);
                    } else if (valueAnimator != null) {
                        PropertyValuesHolder[] values = valueAnimator.getValues();
                        int intValue = num.intValue() + (i - num2.intValue());
                        values[0].setIntValues(intValue, i);
                        expandableView.setTag(2131428090, Integer.valueOf(intValue));
                        expandableView.setTag(2131428089, Integer.valueOf(i));
                        valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
                    } else {
                        expandableView.setActualHeight(i, false);
                    }
                }
            } else {
                ViewState.abortAnimation(view, 2131428091);
            }
            if (this.clipTopAmount != expandableView.mClipTopAmount) {
                Integer num3 = (Integer) expandableView.getTag(2131429073);
                Integer num4 = (Integer) expandableView.getTag(2131429072);
                int i2 = this.clipTopAmount;
                if (num4 == null || num4.intValue() != i2) {
                    ValueAnimator valueAnimator2 = (ValueAnimator) expandableView.getTag(2131429074);
                    if (StackStateAnimator.this.mAnimationFilter.animateTopInset) {
                        ValueAnimator ofInt2 = ValueAnimator.ofInt(expandableView.mClipTopAmount, i2);
                        ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.stack.ExpandableViewState.3
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                                ExpandableView.this.setClipTopAmount(((Integer) valueAnimator3.getAnimatedValue()).intValue());
                            }
                        });
                        ofInt2.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                        ofInt2.setDuration(ViewState.cancelAnimatorAndGetNewDuration(animationProperties.duration, valueAnimator2));
                        if (animationProperties.delay > 0 && (valueAnimator2 == null || valueAnimator2.getAnimatedFraction() == 0.0f)) {
                            ofInt2.setStartDelay(animationProperties.delay);
                        }
                        AnimatorListenerAdapter animationFinishListener2 = animationProperties.getAnimationFinishListener(null);
                        if (animationFinishListener2 != null) {
                            ofInt2.addListener(animationFinishListener2);
                        }
                        ofInt2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.ExpandableViewState.4
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationEnd(Animator animator) {
                                ExpandableView expandableView2 = ExpandableView.this;
                                int i3 = ExpandableViewState.$r8$clinit;
                                expandableView2.setTag(2131429074, null);
                                ExpandableView.this.setTag(2131429073, null);
                                ExpandableView.this.setTag(2131429072, null);
                            }
                        });
                        ViewState.startAnimator(ofInt2, animationFinishListener2);
                        expandableView.setTag(2131429074, ofInt2);
                        expandableView.setTag(2131429073, Integer.valueOf(expandableView.mClipTopAmount));
                        expandableView.setTag(2131429072, Integer.valueOf(i2));
                    } else if (valueAnimator2 != null) {
                        PropertyValuesHolder[] values2 = valueAnimator2.getValues();
                        int intValue2 = num3.intValue() + (i2 - num4.intValue());
                        values2[0].setIntValues(intValue2, i2);
                        expandableView.setTag(2131429073, Integer.valueOf(intValue2));
                        expandableView.setTag(2131429072, Integer.valueOf(i2));
                        valueAnimator2.setCurrentPlayTime(valueAnimator2.getCurrentPlayTime());
                    } else {
                        expandableView.setClipTopAmount(i2);
                    }
                }
            } else {
                ViewState.abortAnimation(view, 2131429074);
            }
            boolean z = animationFilter.animateDimmed;
            expandableView.setBelowSpeedBump(this.belowSpeedBump);
            expandableView.setHideSensitive(this.hideSensitive, animationFilter.animateHideSensitive, animationProperties.delay, animationProperties.duration);
            if (animationProperties.wasAdded(view) && !this.hidden) {
                expandableView.performAddAnimation(animationProperties.delay, animationProperties.duration);
            }
            if (!expandableView.mInShelf && this.inShelf) {
                expandableView.mTransformingInShelf = true;
            }
            expandableView.mInShelf = this.inShelf;
            if (this.headsUpIsVisible) {
                expandableView.setHeadsUpIsVisible();
            }
        }
    }

    public static int getFinalActualHeight(ExpandableView expandableView) {
        if (((ValueAnimator) expandableView.getTag(2131428091)) == null) {
            return expandableView.mActualHeight;
        }
        return ((Integer) expandableView.getTag(2131428089)).intValue();
    }

    @Override // com.android.systemui.statusbar.notification.stack.ViewState
    public void applyToView(View view) {
        super.applyToView(view);
        if (view instanceof ExpandableView) {
            ExpandableView expandableView = (ExpandableView) view;
            Objects.requireNonNull(expandableView);
            int i = expandableView.mActualHeight;
            int i2 = this.height;
            if (i != i2) {
                expandableView.setActualHeight(i2, false);
            }
            expandableView.setHideSensitive(this.hideSensitive, false, 0L, 0L);
            expandableView.setBelowSpeedBump(this.belowSpeedBump);
            int i3 = this.clipTopAmount;
            if (expandableView.mClipTopAmount != i3) {
                expandableView.setClipTopAmount(i3);
            }
            expandableView.mTransformingInShelf = false;
            expandableView.mInShelf = this.inShelf;
            if (this.headsUpIsVisible) {
                expandableView.setHeadsUpIsVisible();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.stack.ViewState
    public final void cancelAnimations(View view) {
        super.cancelAnimations(view);
        Animator animator = (Animator) view.getTag(2131428091);
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = (Animator) view.getTag(2131429074);
        if (animator2 != null) {
            animator2.cancel();
        }
    }

    @Override // com.android.systemui.statusbar.notification.stack.ViewState
    public void copyFrom(ExpandableViewState expandableViewState) {
        super.copyFrom(expandableViewState);
        if (expandableViewState instanceof ExpandableViewState) {
            this.height = expandableViewState.height;
            this.hideSensitive = expandableViewState.hideSensitive;
            this.belowSpeedBump = expandableViewState.belowSpeedBump;
            this.clipTopAmount = expandableViewState.clipTopAmount;
            this.notGoneIndex = expandableViewState.notGoneIndex;
            this.location = expandableViewState.location;
            this.headsUpIsVisible = expandableViewState.headsUpIsVisible;
        }
    }
}
