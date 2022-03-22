package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.util.MathUtils;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationRoundnessManager {
    public HashSet<ExpandableView> mAnimatedChildren;
    public float mAppearFraction;
    public boolean mExpanded;
    public final ExpandableView[] mFirstInSectionViews;
    public boolean mIsClearAllInProgress;
    public final ExpandableView[] mLastInSectionViews;
    public boolean mRoundForPulsingViews;
    public Runnable mRoundingChangedCallback;
    public final ExpandableView[] mTmpFirstInSectionViews;
    public final ExpandableView[] mTmpLastInSectionViews;
    public ExpandableNotificationRow mTrackedHeadsUp;
    public ExpandableView mSwipedView = null;
    public ExpandableView mViewBeforeSwipedView = null;
    public ExpandableView mViewAfterSwipedView = null;

    public final boolean handleAddedNewViews(NotificationSection[] notificationSectionArr, ExpandableView[] expandableViewArr, boolean z) {
        ExpandableView expandableView;
        boolean z2;
        boolean z3;
        boolean z4 = false;
        for (NotificationSection notificationSection : notificationSectionArr) {
            Objects.requireNonNull(notificationSection);
            if (z) {
                expandableView = notificationSection.mFirstVisibleChild;
            } else {
                expandableView = notificationSection.mLastVisibleChild;
            }
            if (expandableView != null) {
                int length = expandableViewArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z2 = false;
                        break;
                    } else if (expandableViewArr[i] == expandableView) {
                        z2 = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!z2) {
                    if (!expandableView.isShown() || this.mAnimatedChildren.contains(expandableView)) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    updateViewWithoutCallback(expandableView, z3);
                    z4 = true;
                }
            }
        }
        return z4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x003a, code lost:
        r7 = r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean handleRemovedOldViews(com.android.systemui.statusbar.notification.stack.NotificationSection[] r10, com.android.systemui.statusbar.notification.row.ExpandableView[] r11, boolean r12) {
        /*
            r9 = this;
            int r0 = r11.length
            r1 = 0
            r2 = r1
            r3 = r2
        L_0x0004:
            if (r2 >= r0) goto L_0x0050
            r4 = r11[r2]
            r5 = 1
            if (r4 == 0) goto L_0x004d
            int r6 = r10.length
            r7 = r1
        L_0x000d:
            if (r7 >= r6) goto L_0x0039
            r8 = r10[r7]
            if (r12 == 0) goto L_0x0019
            java.util.Objects.requireNonNull(r8)
            com.android.systemui.statusbar.notification.row.ExpandableView r8 = r8.mFirstVisibleChild
            goto L_0x001e
        L_0x0019:
            java.util.Objects.requireNonNull(r8)
            com.android.systemui.statusbar.notification.row.ExpandableView r8 = r8.mLastVisibleChild
        L_0x001e:
            if (r8 != r4) goto L_0x0036
            boolean r6 = r4.mFirstInSection
            boolean r7 = r9.isFirstInSection(r4)
            if (r6 != r7) goto L_0x0034
            boolean r6 = r4.mLastInSection
            boolean r7 = r9.isLastInSection(r4)
            if (r6 == r7) goto L_0x0031
            goto L_0x0034
        L_0x0031:
            r7 = r1
            r6 = r5
            goto L_0x003b
        L_0x0034:
            r6 = r5
            goto L_0x003a
        L_0x0036:
            int r7 = r7 + 1
            goto L_0x000d
        L_0x0039:
            r6 = r1
        L_0x003a:
            r7 = r6
        L_0x003b:
            if (r6 == 0) goto L_0x003f
            if (r7 == 0) goto L_0x004d
        L_0x003f:
            boolean r3 = r4.isRemoved()
            if (r3 != 0) goto L_0x004c
            boolean r3 = r4.isShown()
            r9.updateViewWithoutCallback(r4, r3)
        L_0x004c:
            r3 = r5
        L_0x004d:
            int r2 = r2 + 1
            goto L_0x0004
        L_0x0050:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager.handleRemovedOldViews(com.android.systemui.statusbar.notification.stack.NotificationSection[], com.android.systemui.statusbar.notification.row.ExpandableView[], boolean):boolean");
    }

    public final boolean isFirstInSection(ExpandableView expandableView) {
        int i = 0;
        while (true) {
            ExpandableView[] expandableViewArr = this.mFirstInSectionViews;
            if (i >= expandableViewArr.length) {
                return false;
            }
            if (expandableView == expandableViewArr[i]) {
                return true;
            }
            i++;
        }
    }

    public final boolean updateViewWithoutCallback(ExpandableView expandableView, boolean z) {
        if (expandableView == null || expandableView == this.mViewBeforeSwipedView || expandableView == this.mViewAfterSwipedView) {
            return false;
        }
        float roundnessFraction = getRoundnessFraction(expandableView, true);
        float roundnessFraction2 = getRoundnessFraction(expandableView, false);
        boolean topRoundness = expandableView.setTopRoundness(roundnessFraction, z);
        boolean bottomRoundness = expandableView.setBottomRoundness(roundnessFraction2, z);
        boolean isFirstInSection = isFirstInSection(expandableView);
        boolean isLastInSection = isLastInSection(expandableView);
        expandableView.mFirstInSection = isFirstInSection;
        expandableView.mLastInSection = isLastInSection;
        if (isFirstInSection || isLastInSection) {
            return topRoundness || bottomRoundness;
        }
        return false;
    }

    public final float getRoundnessFraction(ExpandableView expandableView, boolean z) {
        boolean z2;
        if (expandableView == null) {
            return 0.0f;
        }
        if (!(expandableView == this.mViewBeforeSwipedView || expandableView == this.mSwipedView || expandableView == this.mViewAfterSwipedView)) {
            if (expandableView instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                if (!expandableNotificationRow.mEntry.isClearable() || (expandableNotificationRow.shouldShowPublic() && expandableNotificationRow.mSensitiveHiddenInGeneral)) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (z2 && this.mIsClearAllInProgress) {
                    return 1.0f;
                }
            }
            if (!expandableView.isPinned() && (!expandableView.isHeadsUpAnimatingAway() || this.mExpanded)) {
                if (isFirstInSection(expandableView) && z) {
                    return 1.0f;
                }
                if (isLastInSection(expandableView) && !z) {
                    return 1.0f;
                }
                if (expandableView == this.mTrackedHeadsUp) {
                    return MathUtils.saturate(1.0f - this.mAppearFraction);
                }
                if (expandableView.showingPulsing() && this.mRoundForPulsingViews) {
                    return 1.0f;
                }
                Resources resources = expandableView.getResources();
                return resources.getDimension(2131166626) / resources.getDimension(2131166625);
            }
        }
        return 1.0f;
    }

    public final boolean isLastInSection(ExpandableView expandableView) {
        for (int length = this.mLastInSectionViews.length - 1; length >= 0; length--) {
            if (expandableView == this.mLastInSectionViews[length]) {
                return true;
            }
        }
        return false;
    }

    public final void setViewsAffectedBySwipe(ExpandableView expandableView, ExpandableView expandableView2, ExpandableView expandableView3) {
        ExpandableView expandableView4 = this.mViewBeforeSwipedView;
        this.mViewBeforeSwipedView = expandableView;
        if (expandableView4 != null) {
            expandableView4.setBottomRoundness(getRoundnessFraction(expandableView4, false), true);
        }
        if (expandableView != null) {
            expandableView.setBottomRoundness(1.0f, true);
        }
        ExpandableView expandableView5 = this.mSwipedView;
        this.mSwipedView = expandableView2;
        if (expandableView5 != null) {
            float roundnessFraction = getRoundnessFraction(expandableView5, false);
            expandableView5.setTopRoundness(getRoundnessFraction(expandableView5, true), true);
            expandableView5.setBottomRoundness(roundnessFraction, true);
        }
        if (expandableView2 != null) {
            expandableView2.setTopRoundness(1.0f, true);
            expandableView2.setBottomRoundness(1.0f, true);
        }
        ExpandableView expandableView6 = this.mViewAfterSwipedView;
        this.mViewAfterSwipedView = expandableView3;
        if (expandableView6 != null) {
            expandableView6.setTopRoundness(getRoundnessFraction(expandableView6, true), true);
        }
        if (expandableView3 != null) {
            expandableView3.setTopRoundness(1.0f, true);
        }
    }

    public NotificationRoundnessManager(NotificationSectionsFeatureManager notificationSectionsFeatureManager) {
        Objects.requireNonNull(notificationSectionsFeatureManager);
        int length = notificationSectionsFeatureManager.getNotificationBuckets().length;
        this.mFirstInSectionViews = new ExpandableView[length];
        this.mLastInSectionViews = new ExpandableView[length];
        this.mTmpFirstInSectionViews = new ExpandableView[length];
        this.mTmpLastInSectionViews = new ExpandableView[length];
    }

    public final void updateView(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        if (updateViewWithoutCallback(expandableNotificationRow, z)) {
            this.mRoundingChangedCallback.run();
        }
    }
}
