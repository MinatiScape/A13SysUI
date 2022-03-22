package com.android.systemui.statusbar.notification.row;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.ConversationLayout;
import com.android.settingslib.inputmethod.InputMethodPreference$$ExternalSyntheticLambda5;
import com.android.systemui.Dependency;
import com.android.systemui.R$array;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.NotificationMenuRow;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationCustomViewWrapper;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda33;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt;
import com.android.systemui.statusbar.policy.SmartReplyView;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda6;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import kotlin.collections.EmptyList;
/* loaded from: classes.dex */
public class NotificationContentView extends FrameLayout implements NotificationFadeAware {
    public boolean mAnimate;
    public boolean mBeforeN;
    public RemoteInputView mCachedExpandedRemoteInput;
    public RemoteInputViewController mCachedExpandedRemoteInputViewController;
    public RemoteInputView mCachedHeadsUpRemoteInput;
    public RemoteInputViewController mCachedHeadsUpRemoteInputViewController;
    public int mClipBottomAmount;
    public int mClipTopAmount;
    public ExpandableNotificationRow mContainingNotification;
    public int mContentHeight;
    public View mContractedChild;
    public NotificationViewWrapper mContractedWrapper;
    public InflatedSmartReplyState mCurrentSmartReplyState;
    public View.OnClickListener mExpandClickListener;
    public boolean mExpandable;
    public View mExpandedChild;
    public InflatedSmartReplyViewHolder mExpandedInflatedSmartReplies;
    public RemoteInputView mExpandedRemoteInput;
    public RemoteInputViewController mExpandedRemoteInputController;
    public SmartReplyView mExpandedSmartReplyView;
    public Runnable mExpandedVisibleListener;
    public NotificationViewWrapper mExpandedWrapper;
    public boolean mFocusOnVisibilityChange;
    public GroupMembershipManager mGroupMembershipManager;
    public boolean mHeadsUpAnimatingAway;
    public View mHeadsUpChild;
    public int mHeadsUpHeight;
    public InflatedSmartReplyViewHolder mHeadsUpInflatedSmartReplies;
    public RemoteInputView mHeadsUpRemoteInput;
    public RemoteInputViewController mHeadsUpRemoteInputController;
    public SmartReplyView mHeadsUpSmartReplyView;
    public NotificationViewWrapper mHeadsUpWrapper;
    public boolean mIsChildInGroup;
    public boolean mIsContentExpandable;
    public boolean mIsHeadsUp;
    public boolean mLegacy;
    public NotificationEntry mNotificationEntry;
    public int mNotificationMaxHeight;
    public PeopleNotificationIdentifier mPeopleIdentifier;
    public PendingIntent mPreviousExpandedRemoteInputIntent;
    public PendingIntent mPreviousHeadsUpRemoteInputIntent;
    public RemoteInputController mRemoteInputController;
    public RemoteInputViewSubcomponent.Factory mRemoteInputSubcomponentFactory;
    public boolean mRemoteInputVisible;
    public HybridNotificationView mSingleLineView;
    public int mSingleLineWidthIndention;
    public int mSmallHeight;
    public int mTransformationStartVisibleType;
    public int mUnrestrictedContentHeight;
    public boolean mUserExpanding;
    public final Rect mClipBounds = new Rect();
    public int mVisibleType = -1;
    public final ArrayMap<View, Runnable> mOnContentViewInactiveListeners = new ArrayMap<>();
    public final AnonymousClass1 mEnableAnimationPredrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentView.1
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            NotificationContentView.this.post(new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentView.1.1
                @Override // java.lang.Runnable
                public final void run() {
                    NotificationContentView.this.mAnimate = true;
                }
            });
            NotificationContentView.this.getViewTreeObserver().removeOnPreDrawListener(this);
            return true;
        }
    };
    public boolean mClipToActualHeight = true;
    public int mAnimationStartVisibleType = -1;
    public boolean mForceSelectNextLayout = true;
    public int mContentHeightAtAnimationStart = -1;
    public HybridGroupManager mHybridGroupManager = new HybridGroupManager(getContext());
    public SmartReplyConstants mSmartReplyConstants = (SmartReplyConstants) Dependency.get(SmartReplyConstants.class);
    public SmartReplyController mSmartReplyController = (SmartReplyController) Dependency.get(SmartReplyController.class);
    public int mMinContractedHeight = getResources().getDimensionPixelSize(2131166365);

    /* loaded from: classes.dex */
    public static class RemoteInputViewData {
        public RemoteInputViewController mController;
        public RemoteInputView mView;
    }

    public static void applyExternalSmartReplyState(View view, InflatedSmartReplyState inflatedSmartReplyState) {
        boolean z;
        List list;
        boolean z2;
        int i;
        int i2;
        if (inflatedSmartReplyState == null || !inflatedSmartReplyState.hasPhishingAction) {
            z = false;
        } else {
            z = true;
        }
        View findViewById = view.findViewById(16909338);
        if (findViewById != null) {
            if (z) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            findViewById.setVisibility(i2);
        }
        if (inflatedSmartReplyState != null) {
            InflatedSmartReplyState.SuppressedActions suppressedActions = inflatedSmartReplyState.suppressedActions;
            if (suppressedActions == null) {
                list = null;
            } else {
                list = suppressedActions.suppressedActionIndices;
            }
            if (list == null) {
                list = EmptyList.INSTANCE;
            }
        } else {
            list = Collections.emptyList();
        }
        ViewGroup viewGroup = (ViewGroup) view.findViewById(16908741);
        if (viewGroup != null) {
            for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                View childAt = viewGroup.getChildAt(i3);
                Object tag = childAt.getTag(16909269);
                if (!(tag instanceof Integer) || !list.contains(tag)) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (z2) {
                    i = 8;
                } else {
                    i = 0;
                }
                childAt.setVisibility(i);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0029, code lost:
        if (r5 != false) goto L_0x002d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002d, code lost:
        if (r2 != false) goto L_0x002f;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int getExtraRemoteInputHeight(com.android.systemui.statusbar.policy.RemoteInputView r5) {
        /*
            r4 = this;
            r0 = 0
            if (r5 == 0) goto L_0x003b
            boolean r1 = r5.isActive()
            if (r1 != 0) goto L_0x002f
            int r1 = r5.getVisibility()
            r2 = 1
            if (r1 != 0) goto L_0x002c
            com.android.systemui.statusbar.RemoteInputController r1 = r5.mController
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r5.mEntry
            java.util.Objects.requireNonNull(r3)
            java.lang.String r3 = r3.mKey
            java.lang.Object r5 = r5.mToken
            java.util.Objects.requireNonNull(r1)
            android.util.ArrayMap<java.lang.String, java.lang.Object> r1 = r1.mSpinning
            java.lang.Object r1 = r1.get(r3)
            if (r1 != r5) goto L_0x0028
            r5 = r2
            goto L_0x0029
        L_0x0028:
            r5 = r0
        L_0x0029:
            if (r5 == 0) goto L_0x002c
            goto L_0x002d
        L_0x002c:
            r2 = r0
        L_0x002d:
            if (r2 == 0) goto L_0x003b
        L_0x002f:
            android.content.res.Resources r4 = r4.getResources()
            r5 = 17105387(0x10501eb, float:2.4429618E-38)
            int r4 = r4.getDimensionPixelSize(r5)
            return r4
        L_0x003b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.NotificationContentView.getExtraRemoteInputHeight(com.android.systemui.statusbar.policy.RemoteInputView):int");
    }

    public final TransformableView getTransformableViewForVisibleType(int i) {
        if (i == 1) {
            return this.mExpandedWrapper;
        }
        if (i == 2) {
            return this.mHeadsUpWrapper;
        }
        if (i != 3) {
            return this.mContractedWrapper;
        }
        return this.mSingleLineView;
    }

    public final View getViewForVisibleType(int i) {
        if (i == 1) {
            return this.mExpandedChild;
        }
        if (i == 2) {
            return this.mHeadsUpChild;
        }
        if (i != 3) {
            return this.mContractedChild;
        }
        return this.mSingleLineView;
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        boolean z;
        boolean z2;
        int i3;
        int i4;
        int i5;
        boolean z3;
        boolean z4;
        int i6;
        boolean z5;
        int i7;
        int mode = View.MeasureSpec.getMode(i2);
        boolean z6 = true;
        if (mode == 1073741824) {
            z = true;
        } else {
            z = false;
        }
        if (mode == Integer.MIN_VALUE) {
            z2 = true;
        } else {
            z2 = false;
        }
        int i8 = 1073741823;
        int size = View.MeasureSpec.getSize(i);
        if (z || z2) {
            i8 = View.MeasureSpec.getSize(i2);
        }
        if (this.mExpandedChild != null) {
            int i9 = this.mNotificationMaxHeight;
            SmartReplyView smartReplyView = this.mExpandedSmartReplyView;
            if (smartReplyView != null) {
                Objects.requireNonNull(smartReplyView);
                i9 += smartReplyView.mHeightUpperLimit;
            }
            int extraMeasureHeight = this.mExpandedWrapper.getExtraMeasureHeight() + i9;
            int i10 = this.mExpandedChild.getLayoutParams().height;
            if (i10 >= 0) {
                extraMeasureHeight = Math.min(extraMeasureHeight, i10);
                z5 = true;
            } else {
                z5 = false;
            }
            if (z5) {
                i7 = 1073741824;
            } else {
                i7 = Integer.MIN_VALUE;
            }
            measureChildWithMargins(this.mExpandedChild, i, 0, View.MeasureSpec.makeMeasureSpec(extraMeasureHeight, i7), 0);
            i3 = Math.max(0, this.mExpandedChild.getMeasuredHeight());
        } else {
            i3 = 0;
        }
        View view = this.mContractedChild;
        if (view != null) {
            int i11 = this.mSmallHeight;
            int i12 = view.getLayoutParams().height;
            if (i12 >= 0) {
                i11 = Math.min(i11, i12);
                z3 = true;
            } else {
                z3 = false;
            }
            if (!this.mBeforeN || !(this.mContractedWrapper instanceof NotificationCustomViewWrapper)) {
                z4 = false;
            } else {
                z4 = true;
            }
            if (z4 || z3) {
                i6 = View.MeasureSpec.makeMeasureSpec(i11, 1073741824);
            } else {
                i6 = View.MeasureSpec.makeMeasureSpec(i11, Integer.MIN_VALUE);
            }
            measureChildWithMargins(this.mContractedChild, i, 0, i6, 0);
            int measuredHeight = this.mContractedChild.getMeasuredHeight();
            int i13 = this.mMinContractedHeight;
            if (measuredHeight < i13) {
                measureChildWithMargins(this.mContractedChild, i, 0, View.MeasureSpec.makeMeasureSpec(i13, 1073741824), 0);
            }
            i3 = Math.max(i3, measuredHeight);
            if (this.mExpandedChild != null && this.mContractedChild.getMeasuredHeight() > this.mExpandedChild.getMeasuredHeight()) {
                measureChildWithMargins(this.mExpandedChild, i, 0, View.MeasureSpec.makeMeasureSpec(this.mContractedChild.getMeasuredHeight(), 1073741824), 0);
            }
        }
        if (this.mHeadsUpChild != null) {
            int i14 = this.mHeadsUpHeight;
            SmartReplyView smartReplyView2 = this.mHeadsUpSmartReplyView;
            if (smartReplyView2 != null) {
                Objects.requireNonNull(smartReplyView2);
                i14 += smartReplyView2.mHeightUpperLimit;
            }
            int extraMeasureHeight2 = this.mHeadsUpWrapper.getExtraMeasureHeight() + i14;
            int i15 = this.mHeadsUpChild.getLayoutParams().height;
            if (i15 >= 0) {
                extraMeasureHeight2 = Math.min(extraMeasureHeight2, i15);
            } else {
                z6 = false;
            }
            View view2 = this.mHeadsUpChild;
            if (z6) {
                i5 = 1073741824;
            } else {
                i5 = Integer.MIN_VALUE;
            }
            measureChildWithMargins(view2, i, 0, View.MeasureSpec.makeMeasureSpec(extraMeasureHeight2, i5), 0);
            i3 = Math.max(i3, this.mHeadsUpChild.getMeasuredHeight());
        }
        if (this.mSingleLineView != null) {
            if (this.mSingleLineWidthIndention == 0 || View.MeasureSpec.getMode(i) == 0) {
                i4 = i;
            } else {
                i4 = View.MeasureSpec.makeMeasureSpec(this.mSingleLineView.getPaddingEnd() + (size - this.mSingleLineWidthIndention), 1073741824);
            }
            this.mSingleLineView.measure(i4, View.MeasureSpec.makeMeasureSpec(this.mNotificationMaxHeight, Integer.MIN_VALUE));
            i3 = Math.max(i3, this.mSingleLineView.getMeasuredHeight());
        }
        setMeasuredDimension(size, Math.min(i3, i8));
    }

    public static void updateViewVisibility(int i, int i2, View view, TransformableView transformableView) {
        boolean z;
        if (view != null) {
            if (i == i2) {
                z = true;
            } else {
                z = false;
            }
            transformableView.setVisible(z);
        }
    }

    public final void applyBubbleAction(View view, NotificationEntry notificationEntry) {
        boolean z;
        int i;
        int i2;
        if (view != null && this.mContainingNotification != null && this.mPeopleIdentifier != null) {
            ImageView imageView = (ImageView) view.findViewById(16908829);
            View findViewById = view.findViewById(16908742);
            if (imageView != null && findViewById != null) {
                boolean z2 = true;
                if (this.mPeopleIdentifier.getPeopleNotificationType(notificationEntry) >= 2) {
                    z = true;
                } else {
                    z = false;
                }
                Context context = ((FrameLayout) this).mContext;
                Objects.requireNonNull(notificationEntry);
                if (!BubblesManager.areBubblesEnabled(context, notificationEntry.mSbn.getUser()) || !z || notificationEntry.mBubbleMetadata == null) {
                    z2 = false;
                }
                if (z2) {
                    Context context2 = ((FrameLayout) this).mContext;
                    if (notificationEntry.isBubble()) {
                        i = 2131231637;
                    } else {
                        i = 2131231633;
                    }
                    Drawable drawable = context2.getDrawable(i);
                    Resources resources = ((FrameLayout) this).mContext.getResources();
                    if (notificationEntry.isBubble()) {
                        i2 = 2131952908;
                    } else {
                        i2 = 2131952906;
                    }
                    imageView.setContentDescription(resources.getString(i2));
                    imageView.setImageDrawable(drawable);
                    ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
                    Objects.requireNonNull(expandableNotificationRow);
                    imageView.setOnClickListener(new BubbleStackView$$ExternalSyntheticLambda6(expandableNotificationRow, 2));
                    imageView.setVisibility(0);
                    findViewById.setVisibility(0);
                    return;
                }
                imageView.setVisibility(8);
            }
        }
    }

    public final RemoteInputViewData applyRemoteInput(View view, NotificationEntry notificationEntry, boolean z, PendingIntent pendingIntent, RemoteInputView remoteInputView, RemoteInputViewController remoteInputViewController, NotificationViewWrapper notificationViewWrapper) {
        RemoteInputViewData remoteInputViewData = new RemoteInputViewData();
        View findViewById = view.findViewById(16908742);
        if (findViewById instanceof FrameLayout) {
            Object obj = RemoteInputView.VIEW_TAG;
            RemoteInputView remoteInputView2 = (RemoteInputView) view.findViewWithTag(obj);
            remoteInputViewData.mView = remoteInputView2;
            if (remoteInputView2 != null) {
                remoteInputView2.onNotificationUpdateOrReset();
                RemoteInputView remoteInputView3 = remoteInputViewData.mView;
                Objects.requireNonNull(remoteInputView3);
                remoteInputViewData.mController = remoteInputView3.mViewController;
            }
            if (remoteInputViewData.mView == null && z) {
                FrameLayout frameLayout = (FrameLayout) findViewById;
                if (remoteInputView == null) {
                    Context context = ((FrameLayout) this).mContext;
                    RemoteInputController remoteInputController = this.mRemoteInputController;
                    RemoteInputView remoteInputView4 = (RemoteInputView) LayoutInflater.from(context).inflate(2131624445, (ViewGroup) frameLayout, false);
                    remoteInputView4.mController = remoteInputController;
                    remoteInputView4.mEntry = notificationEntry;
                    Objects.requireNonNull(notificationEntry);
                    UserHandle user = notificationEntry.mSbn.getUser();
                    if (UserHandle.ALL.equals(user)) {
                        user = UserHandle.of(ActivityManager.getCurrentUser());
                    }
                    RemoteInputView.RemoteEditText remoteEditText = remoteInputView4.mEditText;
                    remoteEditText.mUser = user;
                    remoteEditText.setTextOperationUser(user);
                    remoteInputView4.setTag(obj);
                    remoteInputView4.setVisibility(8);
                    frameLayout.addView(remoteInputView4, new FrameLayout.LayoutParams(-1, -1));
                    remoteInputViewData.mView = remoteInputView4;
                    RemoteInputViewController controller = this.mRemoteInputSubcomponentFactory.create(remoteInputView4, this.mRemoteInputController).getController();
                    remoteInputViewData.mController = controller;
                    RemoteInputView remoteInputView5 = remoteInputViewData.mView;
                    Objects.requireNonNull(remoteInputView5);
                    remoteInputView5.mViewController = controller;
                } else {
                    frameLayout.addView(remoteInputView);
                    remoteInputView.dispatchFinishTemporaryDetach();
                    remoteInputView.requestFocus();
                    remoteInputViewData.mView = remoteInputView;
                    remoteInputViewData.mController = remoteInputViewController;
                }
            }
            if (z) {
                RemoteInputView remoteInputView6 = remoteInputViewData.mView;
                Objects.requireNonNull(remoteInputView6);
                remoteInputView6.mWrapper = notificationViewWrapper;
                RemoteInputView remoteInputView7 = remoteInputViewData.mView;
                StatusBar$$ExternalSyntheticLambda33 statusBar$$ExternalSyntheticLambda33 = new StatusBar$$ExternalSyntheticLambda33(this, 1);
                Objects.requireNonNull(remoteInputView7);
                remoteInputView7.mOnVisibilityChangedListeners.add(statusBar$$ExternalSyntheticLambda33);
                if (pendingIntent != null || remoteInputViewData.mView.isActive()) {
                    Objects.requireNonNull(notificationEntry);
                    Notification.Action[] actionArr = notificationEntry.mSbn.getNotification().actions;
                    if (pendingIntent != null) {
                        RemoteInputView remoteInputView8 = remoteInputViewData.mView;
                        Objects.requireNonNull(remoteInputView8);
                        remoteInputView8.mPendingIntent = pendingIntent;
                        remoteInputViewData.mController.setPendingIntent(pendingIntent);
                    }
                    if (remoteInputViewData.mController.updatePendingIntentFromActions(actionArr)) {
                        if (!remoteInputViewData.mView.isActive()) {
                            remoteInputViewData.mView.focus();
                        }
                    } else if (remoteInputViewData.mView.isActive()) {
                        RemoteInputView remoteInputView9 = remoteInputViewData.mView;
                        Objects.requireNonNull(remoteInputView9);
                        RemoteInputView.RemoteEditText remoteEditText2 = remoteInputView9.mEditText;
                        int i = RemoteInputView.RemoteEditText.$r8$clinit;
                        remoteEditText2.defocusIfNeeded(false);
                    }
                }
            }
            if (remoteInputViewData.mView != null) {
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                Objects.requireNonNull(expandableNotificationRow);
                remoteInputViewData.mView.setBackgroundTintColor(expandableNotificationRow.mCurrentBackgroundTint, notificationEntry.mSbn.getNotification().isColorized());
            }
        }
        return remoteInputViewData;
    }

    public final void applySystemActions(View view, NotificationEntry notificationEntry) {
        boolean z;
        if (!(view == null || this.mContainingNotification == null)) {
            ImageView imageView = (ImageView) view.findViewById(16909508);
            View findViewById = view.findViewById(16908742);
            if (!(imageView == null || findViewById == null)) {
                if (Settings.Secure.getInt(((FrameLayout) this).mContext.getContentResolver(), "show_notification_snooze", 0) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                boolean z2 = !imageView.isEnabled();
                if (!z || z2) {
                    imageView.setVisibility(8);
                } else {
                    imageView.setImageDrawable(((FrameLayout) this).mContext.getDrawable(2131232272));
                    NotificationMenuRow.NotificationMenuItem notificationMenuItem = new NotificationMenuRow.NotificationMenuItem(((FrameLayout) this).mContext, ((FrameLayout) this).mContext.getString(2131952915), (NotificationSnooze) LayoutInflater.from(((FrameLayout) this).mContext).inflate(2131624333, (ViewGroup) null, false), 2131232272);
                    imageView.setContentDescription(((FrameLayout) this).mContext.getResources().getString(2131952915));
                    ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
                    Objects.requireNonNull(expandableNotificationRow);
                    imageView.setOnClickListener(new InputMethodPreference$$ExternalSyntheticLambda5(expandableNotificationRow, notificationMenuItem, 1));
                    imageView.setVisibility(0);
                    findViewById.setVisibility(0);
                }
            }
        }
        applyBubbleAction(view, notificationEntry);
    }

    public final int calculateVisibleType() {
        int i;
        int i2;
        if (this.mUserExpanding) {
            if (!this.mIsChildInGroup || isGroupExpanded() || this.mContainingNotification.isExpanded(true)) {
                i = this.mContainingNotification.getMaxContentHeight();
            } else {
                NotificationContentView showingLayout = this.mContainingNotification.getShowingLayout();
                Objects.requireNonNull(showingLayout);
                i = showingLayout.getMinHeight(false);
            }
            if (i == 0) {
                i = this.mContentHeight;
            }
            int visualTypeForHeight = getVisualTypeForHeight(i);
            if (!this.mIsChildInGroup || isGroupExpanded()) {
                i2 = getVisualTypeForHeight(this.mContainingNotification.getCollapsedHeight());
            } else {
                i2 = 3;
            }
            if (this.mTransformationStartVisibleType == i2) {
                return visualTypeForHeight;
            }
            return i2;
        }
        int intrinsicHeight = this.mContainingNotification.getIntrinsicHeight();
        int i3 = this.mContentHeight;
        if (intrinsicHeight != 0) {
            i3 = Math.min(i3, intrinsicHeight);
        }
        return getVisualTypeForHeight(i3);
    }

    public final void fireExpandedVisibleListenerIfVisible() {
        if (this.mExpandedVisibleListener != null && this.mExpandedChild != null && isShown() && this.mExpandedChild.getVisibility() == 0) {
            Runnable runnable = this.mExpandedVisibleListener;
            this.mExpandedVisibleListener = null;
            runnable.run();
        }
    }

    public final void forceUpdateVisibility(int i, View view, TransformableView transformableView) {
        boolean z;
        if (view != null) {
            if (this.mVisibleType == i || this.mTransformationStartVisibleType == i) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                view.setVisibility(4);
            } else {
                transformableView.setVisible(true);
            }
        }
    }

    public final int getHeadsUpHeight(boolean z) {
        int i = 0;
        if (this.mHeadsUpChild != null) {
            i = 2;
        } else if (this.mContractedChild == null) {
            return getMinHeight(false);
        }
        return getExtraRemoteInputHeight(this.mExpandedRemoteInput) + getExtraRemoteInputHeight(this.mHeadsUpRemoteInput) + getViewHeight(i, z);
    }

    public final int getMinContentHeightHint() {
        int i;
        int i2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (this.mIsChildInGroup && isVisibleOrTransitioning(3)) {
            return ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(17105374);
        }
        if (!(this.mHeadsUpChild == null || this.mExpandedChild == null)) {
            int i3 = this.mTransformationStartVisibleType;
            if ((i3 == 2 || this.mAnimationStartVisibleType == 2) && this.mVisibleType == 1) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                if ((i3 == 1 || this.mAnimationStartVisibleType == 1) && this.mVisibleType == 2) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (!z4) {
                    z2 = false;
                    if (!isVisibleOrTransitioning(0) || ((!this.mIsHeadsUp && !this.mHeadsUpAnimatingAway) || !this.mContainingNotification.canShowHeadsUp())) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (!z2 || z3) {
                        return Math.min(getViewHeight(2, false), getViewHeight(1, false));
                    }
                }
            }
            z2 = true;
            if (!isVisibleOrTransitioning(0)) {
            }
            z3 = false;
            if (!z2) {
            }
            return Math.min(getViewHeight(2, false), getViewHeight(1, false));
        }
        if (this.mVisibleType == 1 && (i2 = this.mContentHeightAtAnimationStart) != -1 && this.mExpandedChild != null) {
            return Math.min(i2, getViewHeight(1, false));
        }
        if (this.mHeadsUpChild != null && isVisibleOrTransitioning(2)) {
            i = getViewHeight(2, false);
        } else if (this.mExpandedChild != null) {
            i = getViewHeight(1, false);
        } else if (this.mContractedChild != null) {
            i = getViewHeight(0, false) + ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(17105374);
        } else {
            i = getMinHeight(false);
        }
        if (this.mExpandedChild == null || !isVisibleOrTransitioning(1)) {
            return i;
        }
        return Math.min(i, getViewHeight(1, false));
    }

    public final int getMinHeight(boolean z) {
        if (!z && this.mIsChildInGroup && !isGroupExpanded()) {
            return this.mSingleLineView.getHeight();
        }
        if (this.mContractedChild != null) {
            return getViewHeight(0, false);
        }
        return this.mMinContractedHeight;
    }

    public final NotificationViewWrapper getVisibleWrapper(int i) {
        if (i == 0) {
            return this.mContractedWrapper;
        }
        if (i == 1) {
            return this.mExpandedWrapper;
        }
        if (i != 2) {
            return null;
        }
        return this.mHeadsUpWrapper;
    }

    public final int getVisualTypeForHeight(float f) {
        boolean z;
        if (this.mExpandedChild == null) {
            z = true;
        } else {
            z = false;
        }
        if (!z && f == getViewHeight(1, false)) {
            return 1;
        }
        if (!this.mUserExpanding && this.mIsChildInGroup && !isGroupExpanded()) {
            return 3;
        }
        if ((this.mIsHeadsUp || this.mHeadsUpAnimatingAway) && this.mHeadsUpChild != null && this.mContainingNotification.canShowHeadsUp()) {
            if (f <= getViewHeight(2, false) || z) {
                return 2;
            }
            return 1;
        } else if (z || (this.mContractedChild != null && f <= getViewHeight(0, false) && (!this.mIsChildInGroup || isGroupExpanded() || !this.mContainingNotification.isExpanded(true)))) {
            return 0;
        } else {
            if (!z) {
                return 1;
            }
            return -1;
        }
    }

    @VisibleForTesting
    public boolean isAnimatingVisibleType() {
        if (this.mAnimationStartVisibleType != -1) {
            return true;
        }
        return false;
    }

    public final boolean isGroupExpanded() {
        return this.mContainingNotification.isGroupExpanded();
    }

    public final boolean isVisibleOrTransitioning(int i) {
        if (this.mVisibleType == i || this.mTransformationStartVisibleType == i || this.mAnimationStartVisibleType == i) {
            return true;
        }
        return false;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        View view = this.mExpandedChild;
        if (view != null) {
            i5 = view.getHeight();
        } else {
            i5 = 0;
        }
        super.onLayout(z, i, i2, i3, i4);
        if (!(i5 == 0 || this.mExpandedChild.getHeight() == i5)) {
            this.mContentHeightAtAnimationStart = i5;
        }
        updateClipping();
        invalidateOutline();
        selectLayout(false, this.mForceSelectNextLayout);
        this.mForceSelectNextLayout = false;
        updateExpandButtonsDuringLayout(this.mExpandable, true);
    }

    public final boolean pointInView(float f, float f2, float f3) {
        float f4 = this.mClipTopAmount;
        float f5 = this.mUnrestrictedContentHeight;
        if (f < (-f3) || f2 < f4 - f3 || f >= (((FrameLayout) this).mRight - ((FrameLayout) this).mLeft) + f3 || f2 >= f5 + f3) {
            return false;
        }
        return true;
    }

    public final void selectLayout(boolean z, boolean z2) {
        boolean z3;
        View expandButton;
        RemoteInputView remoteInputView;
        RemoteInputView remoteInputView2;
        if (this.mContractedChild != null) {
            if (this.mUserExpanding) {
                int calculateVisibleType = calculateVisibleType();
                if (getTransformableViewForVisibleType(this.mVisibleType) == null) {
                    this.mVisibleType = calculateVisibleType;
                    updateViewVisibilities(calculateVisibleType);
                    updateBackgroundColor(false);
                    return;
                }
                int i = this.mVisibleType;
                if (calculateVisibleType != i) {
                    this.mTransformationStartVisibleType = i;
                    TransformableView transformableViewForVisibleType = getTransformableViewForVisibleType(calculateVisibleType);
                    TransformableView transformableViewForVisibleType2 = getTransformableViewForVisibleType(this.mTransformationStartVisibleType);
                    transformableViewForVisibleType.transformFrom(transformableViewForVisibleType2, 0.0f);
                    getViewForVisibleType(calculateVisibleType).setVisibility(0);
                    transformableViewForVisibleType2.transformTo(transformableViewForVisibleType, 0.0f);
                    this.mVisibleType = calculateVisibleType;
                    updateBackgroundColor(true);
                }
                if (this.mForceSelectNextLayout) {
                    forceUpdateVisibility(0, this.mContractedChild, this.mContractedWrapper);
                    forceUpdateVisibility(1, this.mExpandedChild, this.mExpandedWrapper);
                    forceUpdateVisibility(2, this.mHeadsUpChild, this.mHeadsUpWrapper);
                    HybridNotificationView hybridNotificationView = this.mSingleLineView;
                    forceUpdateVisibility(3, hybridNotificationView, hybridNotificationView);
                    fireExpandedVisibleListenerIfVisible();
                    this.mAnimationStartVisibleType = -1;
                }
                int i2 = this.mTransformationStartVisibleType;
                if (i2 == -1 || this.mVisibleType == i2 || getViewForVisibleType(i2) == null) {
                    updateViewVisibilities(calculateVisibleType);
                    updateBackgroundColor(false);
                    return;
                }
                TransformableView transformableViewForVisibleType3 = getTransformableViewForVisibleType(this.mVisibleType);
                TransformableView transformableViewForVisibleType4 = getTransformableViewForVisibleType(this.mTransformationStartVisibleType);
                int viewHeight = getViewHeight(this.mTransformationStartVisibleType, false);
                int viewHeight2 = getViewHeight(this.mVisibleType, false);
                int abs = Math.abs(this.mContentHeight - viewHeight);
                int abs2 = Math.abs(viewHeight2 - viewHeight);
                float f = 1.0f;
                if (abs2 == 0) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("the total transformation distance is 0\n StartType: ");
                    m.append(this.mTransformationStartVisibleType);
                    m.append(" height: ");
                    m.append(viewHeight);
                    m.append("\n VisibleType: ");
                    m.append(this.mVisibleType);
                    m.append(" height: ");
                    m.append(viewHeight2);
                    m.append("\n mContentHeight: ");
                    m.append(this.mContentHeight);
                    Log.wtf("NotificationContentView", m.toString());
                } else {
                    f = Math.min(1.0f, abs / abs2);
                }
                transformableViewForVisibleType3.transformFrom(transformableViewForVisibleType4, f);
                transformableViewForVisibleType4.transformTo(transformableViewForVisibleType3, f);
                int backgroundColor = getBackgroundColor(this.mVisibleType);
                int backgroundColor2 = getBackgroundColor(this.mTransformationStartVisibleType);
                if (backgroundColor != backgroundColor2) {
                    if (backgroundColor2 == 0) {
                        ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
                        Objects.requireNonNull(expandableNotificationRow);
                        backgroundColor2 = expandableNotificationRow.calculateBgColor(false, false);
                    }
                    if (backgroundColor == 0) {
                        ExpandableNotificationRow expandableNotificationRow2 = this.mContainingNotification;
                        Objects.requireNonNull(expandableNotificationRow2);
                        backgroundColor = expandableNotificationRow2.calculateBgColor(false, false);
                    }
                    backgroundColor = R$array.interpolateColors(backgroundColor2, backgroundColor, f);
                }
                ExpandableNotificationRow expandableNotificationRow3 = this.mContainingNotification;
                Objects.requireNonNull(expandableNotificationRow3);
                if (expandableNotificationRow3.getShowingLayout() == this && backgroundColor != expandableNotificationRow3.mBgTint) {
                    expandableNotificationRow3.mBgTint = backgroundColor;
                    expandableNotificationRow3.updateBackgroundTint(false);
                    return;
                }
                return;
            }
            int calculateVisibleType2 = calculateVisibleType();
            if (calculateVisibleType2 != this.mVisibleType) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3 || z2) {
                View viewForVisibleType = getViewForVisibleType(calculateVisibleType2);
                if (viewForVisibleType != null) {
                    viewForVisibleType.setVisibility(0);
                    if (calculateVisibleType2 == 2 && this.mHeadsUpRemoteInput != null && (remoteInputView2 = this.mExpandedRemoteInput) != null && remoteInputView2.isActive()) {
                        this.mHeadsUpRemoteInput.stealFocusFrom(this.mExpandedRemoteInput);
                    }
                    if (calculateVisibleType2 == 1 && this.mExpandedRemoteInput != null && (remoteInputView = this.mHeadsUpRemoteInput) != null && remoteInputView.isActive()) {
                        this.mExpandedRemoteInput.stealFocusFrom(this.mHeadsUpRemoteInput);
                    }
                }
                if (!z || ((calculateVisibleType2 != 1 || this.mExpandedChild == null) && ((calculateVisibleType2 != 2 || this.mHeadsUpChild == null) && ((calculateVisibleType2 != 3 || this.mSingleLineView == null) && calculateVisibleType2 != 0)))) {
                    updateViewVisibilities(calculateVisibleType2);
                } else {
                    TransformableView transformableViewForVisibleType5 = getTransformableViewForVisibleType(calculateVisibleType2);
                    final TransformableView transformableViewForVisibleType6 = getTransformableViewForVisibleType(this.mVisibleType);
                    if (transformableViewForVisibleType5 == transformableViewForVisibleType6 || transformableViewForVisibleType6 == null) {
                        transformableViewForVisibleType5.setVisible(true);
                    } else {
                        this.mAnimationStartVisibleType = this.mVisibleType;
                        transformableViewForVisibleType5.transformFrom(transformableViewForVisibleType6);
                        getViewForVisibleType(calculateVisibleType2).setVisibility(0);
                        transformableViewForVisibleType6.transformTo(transformableViewForVisibleType5, new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationContentView.2
                            @Override // java.lang.Runnable
                            public final void run() {
                                TransformableView transformableView = transformableViewForVisibleType6;
                                NotificationContentView notificationContentView = NotificationContentView.this;
                                if (transformableView != notificationContentView.getTransformableViewForVisibleType(notificationContentView.mVisibleType)) {
                                    transformableViewForVisibleType6.setVisible(false);
                                }
                                NotificationContentView.this.mAnimationStartVisibleType = -1;
                            }
                        });
                        fireExpandedVisibleListenerIfVisible();
                    }
                }
                this.mVisibleType = calculateVisibleType2;
                if (z3 && this.mFocusOnVisibilityChange) {
                    NotificationViewWrapper visibleWrapper = getVisibleWrapper(calculateVisibleType2);
                    if (!(visibleWrapper == null || (expandButton = visibleWrapper.getExpandButton()) == null)) {
                        expandButton.requestAccessibilityFocus();
                    }
                    this.mFocusOnVisibilityChange = false;
                }
                NotificationViewWrapper visibleWrapper2 = getVisibleWrapper(calculateVisibleType2);
                if (visibleWrapper2 != null) {
                    visibleWrapper2.setContentHeight(this.mUnrestrictedContentHeight, getMinContentHeightHint());
                }
                updateBackgroundColor(z);
            }
        }
    }

    @Override // android.view.ViewGroup
    public final void setClipChildren(boolean z) {
        boolean z2;
        if (!z || this.mRemoteInputVisible) {
            z2 = false;
        } else {
            z2 = true;
        }
        super.setClipChildren(z2);
    }

    public final void setContractedChild(View view) {
        View view2 = this.mContractedChild;
        if (view2 != null) {
            this.mOnContentViewInactiveListeners.remove(view2);
            this.mContractedChild.animate().cancel();
            removeView(this.mContractedChild);
        }
        if (view == null) {
            this.mContractedChild = null;
            this.mContractedWrapper = null;
            if (this.mTransformationStartVisibleType == 0) {
                this.mTransformationStartVisibleType = -1;
                return;
            }
            return;
        }
        addView(view);
        this.mContractedChild = view;
        this.mContractedWrapper = NotificationViewWrapper.wrap(getContext(), view, this.mContainingNotification);
    }

    public final void setExpandedChild(View view) {
        if (this.mExpandedChild != null) {
            this.mPreviousExpandedRemoteInputIntent = null;
            RemoteInputView remoteInputView = this.mExpandedRemoteInput;
            if (remoteInputView != null) {
                remoteInputView.onNotificationUpdateOrReset();
                if (this.mExpandedRemoteInput.isActive()) {
                    RemoteInputView remoteInputView2 = this.mExpandedRemoteInput;
                    Objects.requireNonNull(remoteInputView2);
                    this.mPreviousExpandedRemoteInputIntent = remoteInputView2.mPendingIntent;
                    RemoteInputView remoteInputView3 = this.mExpandedRemoteInput;
                    this.mCachedExpandedRemoteInput = remoteInputView3;
                    this.mCachedExpandedRemoteInputViewController = this.mExpandedRemoteInputController;
                    remoteInputView3.dispatchStartTemporaryDetach();
                    ((ViewGroup) this.mExpandedRemoteInput.getParent()).removeView(this.mExpandedRemoteInput);
                }
            }
            this.mOnContentViewInactiveListeners.remove(this.mExpandedChild);
            this.mExpandedChild.animate().cancel();
            removeView(this.mExpandedChild);
            this.mExpandedRemoteInput = null;
            RemoteInputViewController remoteInputViewController = this.mExpandedRemoteInputController;
            if (remoteInputViewController != null) {
                remoteInputViewController.unbind();
            }
            this.mExpandedRemoteInputController = null;
        }
        if (view == null) {
            this.mExpandedChild = null;
            this.mExpandedWrapper = null;
            if (this.mTransformationStartVisibleType == 1) {
                this.mTransformationStartVisibleType = -1;
            }
            if (this.mVisibleType == 1) {
                selectLayout(false, true);
                return;
            }
            return;
        }
        addView(view);
        this.mExpandedChild = view;
        this.mExpandedWrapper = NotificationViewWrapper.wrap(getContext(), view, this.mContainingNotification);
        ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
        if (expandableNotificationRow != null) {
            applySystemActions(this.mExpandedChild, expandableNotificationRow.mEntry);
        }
    }

    public final void setHeadsUpChild(View view) {
        if (this.mHeadsUpChild != null) {
            this.mPreviousHeadsUpRemoteInputIntent = null;
            RemoteInputView remoteInputView = this.mHeadsUpRemoteInput;
            if (remoteInputView != null) {
                remoteInputView.onNotificationUpdateOrReset();
                if (this.mHeadsUpRemoteInput.isActive()) {
                    RemoteInputView remoteInputView2 = this.mHeadsUpRemoteInput;
                    Objects.requireNonNull(remoteInputView2);
                    this.mPreviousHeadsUpRemoteInputIntent = remoteInputView2.mPendingIntent;
                    RemoteInputView remoteInputView3 = this.mHeadsUpRemoteInput;
                    this.mCachedHeadsUpRemoteInput = remoteInputView3;
                    this.mCachedHeadsUpRemoteInputViewController = this.mHeadsUpRemoteInputController;
                    remoteInputView3.dispatchStartTemporaryDetach();
                    ((ViewGroup) this.mHeadsUpRemoteInput.getParent()).removeView(this.mHeadsUpRemoteInput);
                }
            }
            this.mOnContentViewInactiveListeners.remove(this.mHeadsUpChild);
            this.mHeadsUpChild.animate().cancel();
            removeView(this.mHeadsUpChild);
            this.mHeadsUpRemoteInput = null;
            RemoteInputViewController remoteInputViewController = this.mHeadsUpRemoteInputController;
            if (remoteInputViewController != null) {
                remoteInputViewController.unbind();
            }
            this.mHeadsUpRemoteInputController = null;
        }
        if (view == null) {
            this.mHeadsUpChild = null;
            this.mHeadsUpWrapper = null;
            if (this.mTransformationStartVisibleType == 2) {
                this.mTransformationStartVisibleType = -1;
            }
            if (this.mVisibleType == 2) {
                selectLayout(false, true);
                return;
            }
            return;
        }
        addView(view);
        this.mHeadsUpChild = view;
        this.mHeadsUpWrapper = NotificationViewWrapper.wrap(getContext(), view, this.mContainingNotification);
        ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
        if (expandableNotificationRow != null) {
            applySystemActions(this.mHeadsUpChild, expandableNotificationRow.mEntry);
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationFadeAware
    public final void setNotificationFaded(boolean z) {
        NotificationViewWrapper notificationViewWrapper = this.mContractedWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setNotificationFaded(z);
        }
        NotificationViewWrapper notificationViewWrapper2 = this.mHeadsUpWrapper;
        if (notificationViewWrapper2 != null) {
            notificationViewWrapper2.setNotificationFaded(z);
        }
        NotificationViewWrapper notificationViewWrapper3 = this.mExpandedWrapper;
        if (notificationViewWrapper3 != null) {
            notificationViewWrapper3.setNotificationFaded(z);
        }
        HybridNotificationView hybridNotificationView = this.mSingleLineView;
        if (hybridNotificationView != null) {
            hybridNotificationView.setNotificationFaded(z);
        }
    }

    public final void updateAllSingleLineViews() {
        boolean z;
        int i;
        if (this.mIsChildInGroup) {
            HybridNotificationView hybridNotificationView = this.mSingleLineView;
            if (hybridNotificationView == null) {
                z = true;
            } else {
                z = false;
            }
            HybridGroupManager hybridGroupManager = this.mHybridGroupManager;
            View view = this.mContractedChild;
            NotificationEntry notificationEntry = this.mNotificationEntry;
            Objects.requireNonNull(notificationEntry);
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            Objects.requireNonNull(hybridGroupManager);
            if (hybridNotificationView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) new ContextThemeWrapper(hybridGroupManager.mContext, 2132017499).getSystemService(LayoutInflater.class);
                if (view instanceof ConversationLayout) {
                    i = 2131624132;
                } else {
                    i = 2131624133;
                }
                hybridNotificationView = (HybridNotificationView) layoutInflater.inflate(i, (ViewGroup) this, false);
                addView(hybridNotificationView);
            }
            Notification notification = statusBarNotification.getNotification();
            CharSequence charSequence = notification.extras.getCharSequence("android.title");
            if (charSequence == null) {
                charSequence = notification.extras.getCharSequence("android.title.big");
            }
            Notification notification2 = statusBarNotification.getNotification();
            CharSequence charSequence2 = notification2.extras.getCharSequence("android.text");
            if (charSequence2 == null) {
                charSequence2 = notification2.extras.getCharSequence("android.bigText");
            }
            hybridNotificationView.bind(charSequence, charSequence2, view);
            this.mSingleLineView = hybridNotificationView;
            if (z) {
                updateViewVisibility(this.mVisibleType, 3, hybridNotificationView, hybridNotificationView);
                return;
            }
            return;
        }
        View view2 = this.mSingleLineView;
        if (view2 != null) {
            removeView(view2);
            this.mSingleLineView = null;
        }
    }

    public final void updateBackgroundColor(boolean z) {
        int backgroundColor = getBackgroundColor(this.mVisibleType);
        ExpandableNotificationRow expandableNotificationRow = this.mContainingNotification;
        Objects.requireNonNull(expandableNotificationRow);
        if (expandableNotificationRow.getShowingLayout() == this && backgroundColor != expandableNotificationRow.mBgTint) {
            expandableNotificationRow.mBgTint = backgroundColor;
            expandableNotificationRow.updateBackgroundTint(z);
        }
    }

    public final void updateClipping() {
        if (this.mClipToActualHeight) {
            int translationY = (int) (this.mClipTopAmount - getTranslationY());
            this.mClipBounds.set(0, translationY, getWidth(), Math.max(translationY, (int) ((this.mUnrestrictedContentHeight - this.mClipBottomAmount) - getTranslationY())));
            setClipBounds(this.mClipBounds);
            return;
        }
        setClipBounds(null);
    }

    public final void updateExpandButtonsDuringLayout(boolean z, boolean z2) {
        this.mExpandable = z;
        View view = this.mExpandedChild;
        boolean z3 = false;
        if (!(view == null || view.getHeight() == 0 || ((this.mIsHeadsUp || this.mHeadsUpAnimatingAway) && this.mHeadsUpChild != null && this.mContainingNotification.canShowHeadsUp() ? this.mExpandedChild.getHeight() > this.mHeadsUpChild.getHeight() : this.mContractedChild != null && this.mExpandedChild.getHeight() > this.mContractedChild.getHeight()))) {
            z = false;
        }
        if (z2 && this.mIsContentExpandable != z) {
            z3 = true;
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.updateExpandability(z, this.mExpandClickListener, z3);
        }
        if (this.mContractedChild != null) {
            this.mContractedWrapper.updateExpandability(z, this.mExpandClickListener, z3);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.updateExpandability(z, this.mExpandClickListener, z3);
        }
        this.mIsContentExpandable = z;
    }

    public final void updateLegacy() {
        if (this.mContractedChild != null) {
            this.mContractedWrapper.setLegacy(this.mLegacy);
        }
        if (this.mExpandedChild != null) {
            this.mExpandedWrapper.setLegacy(this.mLegacy);
        }
        if (this.mHeadsUpChild != null) {
            this.mHeadsUpWrapper.setLegacy(this.mLegacy);
        }
    }

    public final void updateViewVisibilities(int i) {
        updateViewVisibility(i, 0, this.mContractedChild, this.mContractedWrapper);
        updateViewVisibility(i, 1, this.mExpandedChild, this.mExpandedWrapper);
        updateViewVisibility(i, 2, this.mHeadsUpChild, this.mHeadsUpWrapper);
        HybridNotificationView hybridNotificationView = this.mSingleLineView;
        updateViewVisibility(i, 3, hybridNotificationView, hybridNotificationView);
        fireExpandedVisibleListenerIfVisible();
        this.mAnimationStartVisibleType = -1;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.statusbar.notification.row.NotificationContentView$1] */
    public NotificationContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getResources().getDimensionPixelSize(17105388);
    }

    public static SmartReplyView applySmartReplyView(View view, InflatedSmartReplyState inflatedSmartReplyState, NotificationEntry notificationEntry, InflatedSmartReplyViewHolder inflatedSmartReplyViewHolder) {
        SmartReplyView smartReplyView;
        View findViewById = view.findViewById(16909500);
        SmartReplyView smartReplyView2 = null;
        if (!(findViewById instanceof LinearLayout)) {
            return null;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById;
        if (!SmartReplyStateInflaterKt.shouldShowSmartReplyView(notificationEntry, inflatedSmartReplyState)) {
            linearLayout.setVisibility(8);
            return null;
        }
        int childCount = linearLayout.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt.getId() == 2131428878 && (childAt instanceof SmartReplyView)) {
                break;
            }
            i++;
        }
        if (i < childCount) {
            linearLayout.removeViewAt(i);
        }
        if (!(inflatedSmartReplyViewHolder == null || (smartReplyView = inflatedSmartReplyViewHolder.smartReplyView) == null)) {
            linearLayout.addView(smartReplyView, i);
            smartReplyView2 = smartReplyView;
        }
        if (smartReplyView2 != null) {
            smartReplyView2.mSmartReplyContainer = linearLayout;
            smartReplyView2.removeAllViews();
            smartReplyView2.setBackgroundTintColor(smartReplyView2.mDefaultBackgroundColor, false);
            Objects.requireNonNull(inflatedSmartReplyViewHolder);
            for (Button button : inflatedSmartReplyViewHolder.smartSuggestionButtons) {
                smartReplyView2.addView(button);
                smartReplyView2.setButtonColors(button);
            }
            smartReplyView2.mCandidateButtonQueueForSqueezing = new PriorityQueue<>(Math.max(smartReplyView2.getChildCount(), 1), SmartReplyView.DECREASING_MEASURED_WIDTH_WITHOUT_PADDING_COMPARATOR);
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            Objects.requireNonNull(expandableNotificationRow);
            smartReplyView2.setBackgroundTintColor(expandableNotificationRow.mCurrentBackgroundTint, notificationEntry.mSbn.getNotification().isColorized());
            linearLayout.setVisibility(0);
        }
        return smartReplyView2;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        RemoteInputView remoteInputView;
        float y = motionEvent.getY();
        View viewForVisibleType = getViewForVisibleType(this.mVisibleType);
        if (viewForVisibleType == this.mExpandedChild) {
            remoteInputView = this.mExpandedRemoteInput;
        } else if (viewForVisibleType == this.mHeadsUpChild) {
            remoteInputView = this.mHeadsUpRemoteInput;
        } else {
            remoteInputView = null;
        }
        if (remoteInputView != null && remoteInputView.getVisibility() == 0) {
            int height = this.mUnrestrictedContentHeight - remoteInputView.getHeight();
            if (y <= this.mUnrestrictedContentHeight && y >= height) {
                motionEvent.offsetLocation(0.0f, -height);
                return remoteInputView.dispatchTouchEvent(motionEvent);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public final int getBackgroundColor(int i) {
        NotificationViewWrapper visibleWrapper = getVisibleWrapper(i);
        if (visibleWrapper != null) {
            return visibleWrapper.getCustomBackgroundColor();
        }
        return 0;
    }

    public final int getViewHeight(int i, boolean z) {
        NotificationViewWrapper notificationViewWrapper;
        View viewForVisibleType = getViewForVisibleType(i);
        int height = viewForVisibleType.getHeight();
        if (viewForVisibleType == this.mContractedChild) {
            notificationViewWrapper = this.mContractedWrapper;
        } else if (viewForVisibleType == this.mExpandedChild) {
            notificationViewWrapper = this.mExpandedWrapper;
        } else if (viewForVisibleType == this.mHeadsUpChild) {
            notificationViewWrapper = this.mHeadsUpWrapper;
        } else {
            notificationViewWrapper = null;
        }
        if (notificationViewWrapper != null) {
            return height + notificationViewWrapper.getHeaderTranslation(z);
        }
        return height;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateVisibility();
    }

    public final void onChildVisibilityChanged(View view, int i, int i2) {
        Runnable remove;
        super.onChildVisibilityChanged(view, i, i2);
        boolean z = true;
        if (view != null && isShown() && (view.getVisibility() == 0 || getViewForVisibleType(this.mVisibleType) == view)) {
            z = false;
        }
        if (z && (remove = this.mOnContentViewInactiveListeners.remove(view)) != null) {
            remove.run();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this.mEnableAnimationPredrawListener);
    }

    @Override // android.view.ViewGroup
    public final void onViewAdded(View view) {
        super.onViewAdded(view);
        view.setTag(2131428725, this.mContainingNotification);
    }

    @Override // android.view.View
    public final void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        if (z) {
            fireExpandedVisibleListenerIfVisible();
        }
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        updateVisibility();
        if (i != 0 && !this.mOnContentViewInactiveListeners.isEmpty()) {
            Iterator it = new ArrayList(this.mOnContentViewInactiveListeners.values()).iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
            this.mOnContentViewInactiveListeners.clear();
        }
    }

    public final void performWhenContentInactive(int i, Runnable runnable) {
        View viewForVisibleType = getViewForVisibleType(i);
        if (viewForVisibleType != null) {
            View viewForVisibleType2 = getViewForVisibleType(i);
            boolean z = true;
            if (viewForVisibleType2 != null && isShown() && (viewForVisibleType2.getVisibility() == 0 || getViewForVisibleType(this.mVisibleType) == viewForVisibleType2)) {
                z = false;
            }
            if (!z) {
                this.mOnContentViewInactiveListeners.put(viewForVisibleType, runnable);
                return;
            }
        }
        runnable.run();
    }

    public final void removeContentInactiveRunnable(int i) {
        View viewForVisibleType = getViewForVisibleType(i);
        if (viewForVisibleType != null) {
            this.mOnContentViewInactiveListeners.remove(viewForVisibleType);
        }
    }

    @Override // android.view.View
    public final void setTranslationY(float f) {
        super.setTranslationY(f);
        updateClipping();
    }

    public final void updateVisibility() {
        if (isShown()) {
            getViewTreeObserver().removeOnPreDrawListener(this.mEnableAnimationPredrawListener);
            getViewTreeObserver().addOnPreDrawListener(this.mEnableAnimationPredrawListener);
            return;
        }
        getViewTreeObserver().removeOnPreDrawListener(this.mEnableAnimationPredrawListener);
        this.mAnimate = false;
    }
}
