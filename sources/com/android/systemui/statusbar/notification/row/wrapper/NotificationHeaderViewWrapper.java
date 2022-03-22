package com.android.systemui.statusbar.notification.row.wrapper;

import android.app.Notification;
import android.util.ArraySet;
import android.util.Pools;
import android.view.NotificationHeaderView;
import android.view.NotificationTopLineView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.NotificationExpandButton;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.CustomInterpolatorTransformation;
import com.android.systemui.statusbar.notification.FeedbackIcon;
import com.android.systemui.statusbar.notification.ImageTransformState;
import com.android.systemui.statusbar.notification.TransformState;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Objects;
import java.util.Stack;
/* loaded from: classes.dex */
public class NotificationHeaderViewWrapper extends NotificationViewWrapper {
    public static final PathInterpolator LOW_PRIORITY_HEADER_CLOSE = new PathInterpolator(0.4f, 0.0f, 0.7f, 1.0f);
    public View mAltExpandTarget;
    public View mAudiblyAlertedIcon;
    public NotificationExpandButton mExpandButton;
    public View mFeedbackIcon;
    public TextView mHeaderText;
    public CachingIconView mIcon;
    public View mIconContainer;
    public boolean mIsLowPriority;
    public NotificationHeaderView mNotificationHeader;
    public NotificationTopLineView mNotificationTopLine;
    public boolean mTransformLowPriorityTitle;
    public final ViewTransformationHelper mTransformationHelper;
    public ImageView mWorkProfileImage;

    /* renamed from: com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 extends CustomInterpolatorTransformation {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
        public final Interpolator getCustomInterpolator(int i, boolean z) {
            boolean z2 = NotificationHeaderViewWrapper.this.mView instanceof NotificationHeaderView;
            if (i != 16) {
                return null;
            }
            if ((!z2 || z) && (z2 || !z)) {
                return NotificationHeaderViewWrapper.LOW_PRIORITY_HEADER_CLOSE;
            }
            return Interpolators.LINEAR_OUT_SLOW_IN;
        }
    }

    public final void addTransformedViews(View... viewArr) {
        for (View view : viewArr) {
            if (view != null) {
                this.mTransformationHelper.addTransformedView(view);
            }
        }
    }

    public final void addViewsTransformingToSimilar(View... viewArr) {
        for (View view : viewArr) {
            if (view != null) {
                this.mTransformationHelper.addViewTransformingToSimilar(view);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper, com.android.systemui.statusbar.TransformableView
    public final void transformFrom(TransformableView transformableView) {
        this.mTransformationHelper.transformFrom(transformableView);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper, com.android.systemui.statusbar.TransformableView
    public final void transformTo(TransformableView transformableView, Runnable runnable) {
        this.mTransformationHelper.transformTo(transformableView, runnable);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper, com.android.systemui.statusbar.TransformableView
    public final TransformState getCurrentState(int i) {
        return this.mTransformationHelper.getCurrentState(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final int getOriginalIconColor() {
        return this.mIcon.getOriginalIconColor();
    }

    public final void resolveHeaderViews() {
        this.mIcon = this.mView.findViewById(16908294);
        this.mHeaderText = (TextView) this.mView.findViewById(16909071);
        TextView textView = (TextView) this.mView.findViewById(16908782);
        this.mExpandButton = this.mView.findViewById(16908980);
        this.mAltExpandTarget = this.mView.findViewById(16908768);
        this.mIconContainer = this.mView.findViewById(16908925);
        this.mWorkProfileImage = (ImageView) this.mView.findViewById(16909365);
        this.mNotificationHeader = this.mView.findViewById(16909272);
        this.mNotificationTopLine = this.mView.findViewById(16909284);
        this.mAudiblyAlertedIcon = this.mView.findViewById(16908760);
        this.mFeedbackIcon = this.mView.findViewById(16908993);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void setExpanded(boolean z) {
        this.mExpandButton.setExpanded(z);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void setFeedbackIcon(FeedbackIcon feedbackIcon) {
        int i;
        View view = this.mFeedbackIcon;
        if (view != null) {
            if (feedbackIcon != null) {
                i = 0;
            } else {
                i = 8;
            }
            view.setVisibility(i);
            if (feedbackIcon != null) {
                View view2 = this.mFeedbackIcon;
                if (view2 instanceof ImageButton) {
                    ((ImageButton) view2).setImageResource(feedbackIcon.iconRes);
                }
                this.mFeedbackIcon.setContentDescription(this.mView.getContext().getString(feedbackIcon.contentDescRes));
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void setIsChildInGroup(boolean z) {
        this.mTransformLowPriorityTitle = !z;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void setRecentlyAudiblyAlerted(boolean z) {
        int i;
        View view = this.mAudiblyAlertedIcon;
        if (view != null) {
            if (z) {
                i = 0;
            } else {
                i = 8;
            }
            view.setVisibility(i);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper, com.android.systemui.statusbar.TransformableView
    public final void transformFrom(TransformableView transformableView, float f) {
        this.mTransformationHelper.transformFrom(transformableView, f);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper, com.android.systemui.statusbar.TransformableView
    public final void transformTo(TransformableView transformableView, float f) {
        this.mTransformationHelper.transformTo(transformableView, f);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void updateExpandability(boolean z, View.OnClickListener onClickListener, boolean z2) {
        int i;
        View.OnClickListener onClickListener2;
        View.OnClickListener onClickListener3;
        View.OnClickListener onClickListener4;
        NotificationExpandButton notificationExpandButton = this.mExpandButton;
        if (z) {
            i = 0;
        } else {
            i = 8;
        }
        notificationExpandButton.setVisibility(i);
        NotificationExpandButton notificationExpandButton2 = this.mExpandButton;
        if (z) {
            onClickListener2 = onClickListener;
        } else {
            onClickListener2 = null;
        }
        notificationExpandButton2.setOnClickListener(onClickListener2);
        View view = this.mAltExpandTarget;
        if (view != null) {
            if (z) {
                onClickListener4 = onClickListener;
            } else {
                onClickListener4 = null;
            }
            view.setOnClickListener(onClickListener4);
        }
        View view2 = this.mIconContainer;
        if (view2 != null) {
            if (z) {
                onClickListener3 = onClickListener;
            } else {
                onClickListener3 = null;
            }
            view2.setOnClickListener(onClickListener3);
        }
        NotificationHeaderView notificationHeaderView = this.mNotificationHeader;
        if (notificationHeaderView != null) {
            if (!z) {
                onClickListener = null;
            }
            notificationHeaderView.setOnClickListener(onClickListener);
        }
        if (z2) {
            this.mExpandButton.getParent().requestLayout();
        }
    }

    public void updateTransformedTypes() {
        TextView textView;
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        Objects.requireNonNull(viewTransformationHelper);
        viewTransformationHelper.mTransformedViews.clear();
        viewTransformationHelper.mKeysTransformingToSimilar.clear();
        this.mTransformationHelper.addTransformedView(0, this.mIcon);
        this.mTransformationHelper.addTransformedView(6, this.mExpandButton);
        if (this.mIsLowPriority && (textView = this.mHeaderText) != null) {
            this.mTransformationHelper.addTransformedView(1, textView);
        }
        addViewsTransformingToSimilar(this.mWorkProfileImage, this.mAudiblyAlertedIcon, this.mFeedbackIcon);
    }

    public NotificationHeaderViewWrapper(View view, ExpandableNotificationRow expandableNotificationRow) {
        super(view, expandableNotificationRow);
        ViewTransformationHelper viewTransformationHelper = new ViewTransformationHelper();
        this.mTransformationHelper = viewTransformationHelper;
        viewTransformationHelper.mCustomTransformations.put(1, new AnonymousClass1());
        resolveHeaderViews();
        Objects.requireNonNull(expandableNotificationRow);
        ClipboardOverlayController$$ExternalSyntheticLambda1 clipboardOverlayController$$ExternalSyntheticLambda1 = expandableNotificationRow.mOnFeedbackClickListener;
        NotificationTopLineView notificationTopLineView = this.mNotificationTopLine;
        if (notificationTopLineView != null) {
            notificationTopLineView.setFeedbackOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda1);
        }
        View view2 = this.mFeedbackIcon;
        if (view2 != null) {
            view2.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda1);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        boolean z;
        int id;
        Objects.requireNonNull(expandableNotificationRow);
        this.mIsLowPriority = expandableNotificationRow.mEntry.isAmbient();
        if (expandableNotificationRow.isChildInGroup() || expandableNotificationRow.mIsSummaryWithChildren) {
            z = false;
        } else {
            z = true;
        }
        this.mTransformLowPriorityTitle = z;
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        Objects.requireNonNull(viewTransformationHelper);
        ArraySet arraySet = new ArraySet(viewTransformationHelper.mTransformedViews.values());
        resolveHeaderViews();
        updateTransformedTypes();
        ViewTransformationHelper viewTransformationHelper2 = this.mTransformationHelper;
        View view = this.mView;
        Objects.requireNonNull(viewTransformationHelper2);
        int size = viewTransformationHelper2.mTransformedViews.size();
        for (int i = 0; i < size; i++) {
            Object valueAt = viewTransformationHelper2.mTransformedViews.valueAt(i);
            while (true) {
                View view2 = (View) valueAt;
                if (view2 != view.getParent()) {
                    view2.setTag(2131427737, Boolean.TRUE);
                    valueAt = view2.getParent();
                }
            }
        }
        Stack stack = new Stack();
        stack.push(view);
        while (!stack.isEmpty()) {
            View view3 = (View) stack.pop();
            if (((Boolean) view3.getTag(2131427737)) != null || (id = view3.getId()) == -1) {
                view3.setTag(2131427737, null);
                if ((view3 instanceof ViewGroup) && !viewTransformationHelper2.mTransformedViews.containsValue(view3)) {
                    ViewGroup viewGroup = (ViewGroup) view3;
                    for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
                        stack.push(viewGroup.getChildAt(i2));
                    }
                }
            } else {
                viewTransformationHelper2.addTransformedView(id, view3);
            }
        }
        Stack stack2 = new Stack();
        stack2.push(this.mView);
        while (!stack2.isEmpty()) {
            View view4 = (View) stack2.pop();
            if ((view4 instanceof ImageView) && view4.getId() != 16908924) {
                ((ImageView) view4).setCropToPadding(true);
            } else if (view4 instanceof ViewGroup) {
                ViewGroup viewGroup2 = (ViewGroup) view4;
                for (int i3 = 0; i3 < viewGroup2.getChildCount(); i3++) {
                    stack2.push(viewGroup2.getChildAt(i3));
                }
            }
        }
        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry);
        Notification notification = notificationEntry.mSbn.getNotification();
        CachingIconView cachingIconView = this.mIcon;
        Pools.SimplePool<ImageTransformState> simplePool = ImageTransformState.sInstancePool;
        cachingIconView.setTag(2131428116, notification.getSmallIcon());
        ViewTransformationHelper viewTransformationHelper3 = this.mTransformationHelper;
        Objects.requireNonNull(viewTransformationHelper3);
        ArraySet arraySet2 = new ArraySet(viewTransformationHelper3.mTransformedViews.values());
        for (int i4 = 0; i4 < arraySet.size(); i4++) {
            View view5 = (View) arraySet.valueAt(i4);
            if (!arraySet2.contains(view5)) {
                ViewTransformationHelper viewTransformationHelper4 = this.mTransformationHelper;
                Objects.requireNonNull(viewTransformationHelper4);
                TransformState createFrom = TransformState.createFrom(view5, viewTransformationHelper4);
                createFrom.setVisible(true, true);
                createFrom.recycle();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper, com.android.systemui.statusbar.TransformableView
    public final void setVisible(boolean z) {
        super.setVisible(z);
        this.mTransformationHelper.setVisible(z);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final View getExpandButton() {
        return this.mExpandButton;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final CachingIconView getIcon() {
        return this.mIcon;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final NotificationHeaderView getNotificationHeader() {
        return this.mNotificationHeader;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public View getShelfTransformationTarget() {
        return this.mIcon;
    }
}
