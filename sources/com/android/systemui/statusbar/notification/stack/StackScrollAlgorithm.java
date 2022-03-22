package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.FooterView;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StackScrollAlgorithm {
    public boolean mClipNotificationScrollToTop;
    public int mCollapsedSize;
    public int mGapHeight;
    public float mHeadsUpInset;
    public final ViewGroup mHostView;
    public boolean mIsExpanded;
    public int mMarginBottom;
    public float mNotificationScrimPadding;
    public int mPaddingBetweenElements;
    public int mPinnedZTranslationExtra;
    public StackScrollAlgorithmState mTempAlgorithmState = new StackScrollAlgorithmState();

    /* loaded from: classes.dex */
    public interface BypassController {
        boolean isBypassEnabled();
    }

    /* loaded from: classes.dex */
    public interface SectionProvider {
        boolean beginsSection(View view, View view2);
    }

    /* loaded from: classes.dex */
    public static class StackScrollAlgorithmState {
        public ExpandableView firstViewInShelf;
        public int mCurrentExpandedYPosition;
        public int mCurrentYPosition;
        public final ArrayList<ExpandableView> visibleChildren = new ArrayList<>();
    }

    public final int getMaxAllowedChildHeight(ExpandableView expandableView) {
        if (expandableView instanceof ExpandableView) {
            return expandableView.getIntrinsicHeight();
        }
        if (expandableView == null) {
            return this.mCollapsedSize;
        }
        return expandableView.getHeight();
    }

    public StackScrollAlgorithm(Context context, ViewGroup viewGroup) {
        this.mHostView = viewGroup;
        initView(context);
    }

    public static boolean childNeedsGapHeight(SectionProvider sectionProvider, int i, View view, View view2) {
        if (!sectionProvider.beginsSection(view, view2) || i <= 0 || (view2 instanceof SectionHeaderView) || (view instanceof FooterView)) {
            return false;
        }
        return true;
    }

    public final float getExpansionFractionWithoutShelf(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        boolean z;
        float f;
        float f2;
        Objects.requireNonNull(ambientState);
        NotificationShelf notificationShelf = ambientState.mShelf;
        if (notificationShelf == null || stackScrollAlgorithmState.firstViewInShelf == null) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            Objects.requireNonNull(notificationShelf);
            f = notificationShelf.getHeight();
        } else {
            f = 0.0f;
        }
        if (!ambientState.isOnKeyguard() || (ambientState.mBypassController.isBypassEnabled() && ambientState.isPulseExpanding())) {
            f2 = this.mNotificationScrimPadding;
        } else {
            f2 = 0.0f;
        }
        float f3 = (ambientState.mStackHeight - f) - f2;
        float f4 = (ambientState.mStackEndHeight - f) - f2;
        if (f4 == 0.0f) {
            return 0.0f;
        }
        return f3 / f4;
    }

    public final void initView(Context context) {
        Resources resources = context.getResources();
        this.mPaddingBetweenElements = resources.getDimensionPixelSize(2131166628);
        this.mCollapsedSize = resources.getDimensionPixelSize(2131166657);
        this.mClipNotificationScrollToTop = resources.getBoolean(2131034119);
        this.mHeadsUpInset = resources.getDimensionPixelSize(2131165797) + SystemBarUtils.getStatusBarHeight(context);
        this.mPinnedZTranslationExtra = resources.getDimensionPixelSize(2131165796);
        this.mGapHeight = resources.getDimensionPixelSize(2131166672);
        this.mNotificationScrimPadding = resources.getDimensionPixelSize(2131166677);
        this.mMarginBottom = resources.getDimensionPixelSize(2131166664);
    }
}
