package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.NotificationHeaderView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.fragment.R$id;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.widget.CallLayout;
import com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda1;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.R$array;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController$$ExternalSyntheticLambda1;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.screenshot.ImageLoader$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.NotificationGroupingUtil;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.AboveShelfChangedListener;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import com.android.systemui.statusbar.notification.FeedbackIcon;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.notification.stack.SwipeableView;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.SmartReplyView;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda1;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ExpandableNotificationRow extends ActivatableNotificationView implements PluginListener<NotificationMenuRowPlugin>, SwipeableView, NotificationFadeAware.FadeOptimizedNotification {
    public static final long RECENTLY_ALERTED_THRESHOLD_MS = TimeUnit.SECONDS.toMillis(30);
    public static final AnonymousClass2 TRANSLATE_CONTENT = new FloatProperty<ExpandableNotificationRow>() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.2
        @Override // android.util.Property
        public final Float get(Object obj) {
            return Float.valueOf(((ExpandableNotificationRow) obj).getTranslation());
        }

        @Override // android.util.FloatProperty
        public final void setValue(ExpandableNotificationRow expandableNotificationRow, float f) {
            expandableNotificationRow.setTranslation(f);
        }
    };
    public boolean mAboveShelf;
    public AboveShelfChangedListener mAboveShelfChangedListener;
    public String mAppName;
    public Optional<BubblesManager> mBubblesManagerOptional;
    public KeyguardBypassController mBypassController;
    public View mChildAfterViewWhenDismissed;
    public boolean mChildIsExpanding;
    public NotificationChildrenContainer mChildrenContainer;
    public ViewStub mChildrenContainerStub;
    public boolean mChildrenExpanded;
    public ExpandableNotificationRowDragController mDragController;
    public boolean mEnableNonGroupedNotificationExpand;
    public NotificationEntry mEntry;
    public boolean mExpandAnimationRunning;
    public boolean mExpandable;
    public boolean mExpandedWhenPinned;
    public Path mExpandingClipPath;
    public OnExpansionChangedListener mExpansionChangedListener;
    public FalsingCollector mFalsingCollector;
    public FalsingManager mFalsingManager;
    public boolean mGroupExpansionChanging;
    public GroupExpansionManager mGroupExpansionManager;
    public GroupMembershipManager mGroupMembershipManager;
    public ExpandableNotificationRow mGroupParentWhenDismissed;
    public NotificationGuts mGuts;
    public ViewStub mGutsStub;
    public boolean mHasUserChangedExpansion;
    public Consumer<Boolean> mHeadsUpAnimatingAwayListener;
    public HeadsUpManager mHeadsUpManager;
    public boolean mHeadsupDisappearRunning;
    public boolean mHideSensitiveForIntrinsicHeight;
    public boolean mIconAnimationRunning;
    public int mIconTransformContentShift;
    public NotificationInlineImageResolver mImageResolver;
    public boolean mIsFaded;
    public boolean mIsHeadsUp;
    public boolean mIsLowPriority;
    public boolean mIsPinned;
    public boolean mIsSnoozed;
    public boolean mIsSummaryWithChildren;
    public boolean mIsSystemChildExpanded;
    public boolean mIsSystemExpanded;
    public boolean mJustClicked;
    public boolean mKeepInParent;
    public LayoutListener mLayoutListener;
    public NotificationContentView[] mLayouts;
    public ExpansionLogger mLogger;
    public String mLoggingKey;
    public LongPressListener mLongPressListener;
    public int mMaxExpandedHeight;
    public int mMaxHeadsUpHeight;
    public int mMaxHeadsUpHeightBeforeN;
    public int mMaxHeadsUpHeightBeforeP;
    public int mMaxHeadsUpHeightBeforeS;
    public int mMaxHeadsUpHeightIncreased;
    public int mMaxSmallHeight;
    public int mMaxSmallHeightBeforeN;
    public int mMaxSmallHeightBeforeP;
    public int mMaxSmallHeightBeforeS;
    public int mMaxSmallHeightLarge;
    public NotificationMenuRowPlugin mMenuRow;
    public boolean mMustStayOnScreen;
    public boolean mNeedsRedaction;
    public int mNotificationColor;
    public NotificationGutsManager mNotificationGutsManager;
    public int mNotificationLaunchHeight;
    public ExpandableNotificationRow mNotificationParent;
    public View.OnClickListener mOnClickListener;
    public OnDragSuccessListener mOnDragSuccessListener;
    public OnExpandClickListener mOnExpandClickListener;
    public ClipboardOverlayController$$ExternalSyntheticLambda1 mOnFeedbackClickListener;
    public Runnable mOnIntrinsicHeightReachedRunnable;
    public boolean mOnKeyguard;
    public OnUserInteractionCallback mOnUserInteractionCallback;
    public PeopleNotificationIdentifier mPeopleNotificationIdentifier;
    public NotificationContentView mPrivateLayout;
    public NotificationContentView mPublicLayout;
    public boolean mRemoved;
    public RowContentBindStage mRowContentBindStage;
    public BooleanSupplier mSecureStateProvider;
    public boolean mSensitive;
    public boolean mSensitiveHiddenInGeneral;
    public boolean mShowGroupBackgroundWhenExpanded;
    public boolean mShowNoBackground;
    public boolean mShowingPublic;
    public boolean mShowingPublicInitialized;
    public StatusBarStateController mStatusBarStateController;
    public Animator mTranslateAnim;
    public ArrayList<View> mTranslateableViews;
    public float mTranslationWhenRemoved;
    public boolean mUpdateBackgroundOnUpdate;
    public boolean mUseIncreasedCollapsedHeight;
    public boolean mUseIncreasedHeadsUpHeight;
    public boolean mUserExpanded;
    public boolean mUserLocked;
    public boolean mWasChildInGroupWhenRemoved;
    public float mHeaderVisibleAmount = 1.0f;
    public boolean mLastChronometerRunning = true;
    public AnonymousClass1 mExpandClickListener = new AnonymousClass1();
    public SystemNotificationAsyncTask mSystemNotificationAsyncTask = new SystemNotificationAsyncTask();
    public final WMShell$8$$ExternalSyntheticLambda0 mExpireRecentlyAlertedFlag = new WMShell$8$$ExternalSyntheticLambda0(this, 3);

    /* renamed from: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        public AnonymousClass1() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:7:0x0019, code lost:
            if (r0.isExpanded(false) != false) goto L_0x001b;
         */
        @Override // android.view.View.OnClickListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onClick(android.view.View r6) {
            /*
                r5 = this;
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$2 r1 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.TRANSLATE_CONTENT
                boolean r0 = r0.shouldShowPublic()
                r1 = 0
                r2 = 1
                if (r0 != 0) goto L_0x0057
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                boolean r3 = r0.mIsLowPriority
                if (r3 == 0) goto L_0x001b
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.isExpanded(r1)
                if (r0 == 0) goto L_0x0057
            L_0x001b:
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r3 = r0.mGroupMembershipManager
                com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r0.mEntry
                boolean r0 = r3.isGroupSummary(r0)
                if (r0 == 0) goto L_0x0057
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                r0.mGroupExpansionChanging = r2
                com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager r1 = r0.mGroupExpansionManager
                com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r0.mEntry
                boolean r0 = r1.isGroupExpanded(r0)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager r3 = r1.mGroupExpansionManager
                com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r1.mEntry
                boolean r1 = r3.toggleGroupExpansion(r1)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$OnExpandClickListener r4 = r3.mOnExpandClickListener
                com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r3.mEntry
                r4.onExpandClicked(r3, r6, r1)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r6 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                android.content.Context r6 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.access$000(r6)
                r3 = 408(0x198, float:5.72E-43)
                com.android.internal.logging.MetricsLogger.action(r6, r3, r1)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r5 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                r5.onExpansionChanged(r2, r0)
                goto L_0x00b0
            L_0x0057:
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                boolean r0 = r0.mEnableNonGroupedNotificationExpand
                if (r0 == 0) goto L_0x00b0
                boolean r0 = r6.isAccessibilityFocused()
                if (r0 == 0) goto L_0x006c
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                com.android.systemui.statusbar.notification.row.NotificationContentView r0 = r0.mPrivateLayout
                java.util.Objects.requireNonNull(r0)
                r0.mFocusOnVisibilityChange = r2
            L_0x006c:
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.mIsPinned
                if (r0 == 0) goto L_0x0084
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                boolean r1 = r0.mExpandedWhenPinned
                r1 = r1 ^ r2
                r0.mExpandedWhenPinned = r1
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$OnExpansionChangedListener r0 = r0.mExpansionChangedListener
                if (r0 == 0) goto L_0x0097
                r0.onExpansionChanged(r1)
                goto L_0x0097
            L_0x0084:
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.isExpanded(r1)
                r0 = r0 ^ r2
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                java.util.Objects.requireNonNull(r3)
                r3.setUserExpanded(r0, r1)
                r1 = r0
            L_0x0097:
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                r0.notifyHeightChanged(r2)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$OnExpandClickListener r2 = r0.mOnExpandClickListener
                com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = r0.mEntry
                r2.onExpandClicked(r0, r6, r1)
                com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r5 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.this
                android.content.Context r5 = com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.access$100(r5)
                r6 = 407(0x197, float:5.7E-43)
                com.android.internal.logging.MetricsLogger.action(r5, r6, r1)
            L_0x00b0:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.AnonymousClass1.onClick(android.view.View):void");
        }
    }

    /* loaded from: classes.dex */
    public interface CoordinateOnClickListener {
    }

    /* loaded from: classes.dex */
    public interface ExpansionLogger {
    }

    /* loaded from: classes.dex */
    public interface LayoutListener {
    }

    /* loaded from: classes.dex */
    public interface LongPressListener {
    }

    /* loaded from: classes.dex */
    public static class NotificationViewState extends ExpandableViewState {
        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public final void animateTo(View view, AnimationProperties animationProperties) {
            boolean z;
            boolean z2;
            float f;
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                Objects.requireNonNull(expandableNotificationRow);
                if (!expandableNotificationRow.mExpandAnimationRunning) {
                    if (expandableNotificationRow.mChildIsExpanding) {
                        this.zTranslation = expandableNotificationRow.getTranslationZ();
                        this.clipTopAmount = expandableNotificationRow.mClipTopAmount;
                    }
                    super.animateTo(view, animationProperties);
                    if (expandableNotificationRow.mIsSummaryWithChildren) {
                        NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow.mChildrenContainer;
                        Objects.requireNonNull(notificationChildrenContainer);
                        int size = notificationChildrenContainer.mAttachedChildren.size();
                        ViewState viewState = new ViewState();
                        float groupExpandFraction = notificationChildrenContainer.getGroupExpandFraction();
                        if (notificationChildrenContainer.showingAsLowPriority() || (!notificationChildrenContainer.mUserLocked && !notificationChildrenContainer.mContainingNotification.isGroupExpansionChanging())) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if ((!notificationChildrenContainer.mChildrenExpanded || !notificationChildrenContainer.mShowDividersWhenExpanded) && (!z || notificationChildrenContainer.mHideDividersDuringExpand)) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        for (int i = size - 1; i >= 0; i--) {
                            ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) notificationChildrenContainer.mAttachedChildren.get(i);
                            Objects.requireNonNull(expandableNotificationRow2);
                            ExpandableViewState expandableViewState = expandableNotificationRow2.mViewState;
                            expandableViewState.animateTo(expandableNotificationRow2, animationProperties);
                            View view2 = (View) notificationChildrenContainer.mDividers.get(i);
                            viewState.initFrom(view2);
                            viewState.yTranslation = expandableViewState.yTranslation - notificationChildrenContainer.mDividerHeight;
                            if (!notificationChildrenContainer.mChildrenExpanded || expandableViewState.alpha == 0.0f) {
                                f = 0.0f;
                            } else {
                                f = notificationChildrenContainer.mDividerAlpha;
                            }
                            if (notificationChildrenContainer.mUserLocked && !notificationChildrenContainer.showingAsLowPriority()) {
                                float f2 = expandableViewState.alpha;
                                if (f2 != 0.0f) {
                                    f = R$array.interpolate(0.0f, notificationChildrenContainer.mDividerAlpha, Math.min(f2, groupExpandFraction));
                                }
                            }
                            viewState.hidden = !z2;
                            viewState.alpha = f;
                            viewState.animateTo(view2, animationProperties);
                            expandableNotificationRow2.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
                        }
                        TextView textView = notificationChildrenContainer.mOverflowNumber;
                        if (textView != null) {
                            if (notificationChildrenContainer.mNeverAppliedGroupState) {
                                ViewState viewState2 = notificationChildrenContainer.mGroupOverFlowState;
                                float f3 = viewState2.alpha;
                                viewState2.alpha = 0.0f;
                                viewState2.applyToView(textView);
                                notificationChildrenContainer.mGroupOverFlowState.alpha = f3;
                                notificationChildrenContainer.mNeverAppliedGroupState = false;
                            }
                            notificationChildrenContainer.mGroupOverFlowState.animateTo(notificationChildrenContainer.mOverflowNumber, animationProperties);
                        }
                        View view3 = notificationChildrenContainer.mNotificationHeader;
                        if (view3 != null) {
                            notificationChildrenContainer.mHeaderViewState.applyToView(view3);
                        }
                        notificationChildrenContainer.updateChildrenClipping();
                    }
                }
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public final void applyToView(View view) {
            float f;
            boolean z;
            boolean z2;
            float f2;
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                Objects.requireNonNull(expandableNotificationRow);
                if (!expandableNotificationRow.mExpandAnimationRunning) {
                    if (expandableNotificationRow.mChildIsExpanding) {
                        this.zTranslation = expandableNotificationRow.getTranslationZ();
                        this.clipTopAmount = expandableNotificationRow.mClipTopAmount;
                    }
                    super.applyToView(view);
                    if (expandableNotificationRow.mIsSummaryWithChildren) {
                        NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow.mChildrenContainer;
                        Objects.requireNonNull(notificationChildrenContainer);
                        int size = notificationChildrenContainer.mAttachedChildren.size();
                        ViewState viewState = new ViewState();
                        if (notificationChildrenContainer.mUserLocked) {
                            f = notificationChildrenContainer.getGroupExpandFraction();
                        } else {
                            f = 0.0f;
                        }
                        if (notificationChildrenContainer.showingAsLowPriority() || (!notificationChildrenContainer.mUserLocked && !notificationChildrenContainer.mContainingNotification.isGroupExpansionChanging())) {
                            z = false;
                        } else {
                            z = true;
                        }
                        if ((!notificationChildrenContainer.mChildrenExpanded || !notificationChildrenContainer.mShowDividersWhenExpanded) && (!z || notificationChildrenContainer.mHideDividersDuringExpand)) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        for (int i = 0; i < size; i++) {
                            ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) notificationChildrenContainer.mAttachedChildren.get(i);
                            Objects.requireNonNull(expandableNotificationRow2);
                            ExpandableViewState expandableViewState = expandableNotificationRow2.mViewState;
                            expandableViewState.applyToView(expandableNotificationRow2);
                            View view2 = (View) notificationChildrenContainer.mDividers.get(i);
                            viewState.initFrom(view2);
                            viewState.yTranslation = expandableViewState.yTranslation - notificationChildrenContainer.mDividerHeight;
                            if (!notificationChildrenContainer.mChildrenExpanded || expandableViewState.alpha == 0.0f) {
                                f2 = 0.0f;
                            } else {
                                f2 = notificationChildrenContainer.mDividerAlpha;
                            }
                            if (notificationChildrenContainer.mUserLocked && !notificationChildrenContainer.showingAsLowPriority()) {
                                float f3 = expandableViewState.alpha;
                                if (f3 != 0.0f) {
                                    f2 = R$array.interpolate(0.0f, notificationChildrenContainer.mDividerAlpha, Math.min(f3, f));
                                }
                            }
                            viewState.hidden = !z2;
                            viewState.alpha = f2;
                            viewState.applyToView(view2);
                            expandableNotificationRow2.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
                        }
                        ViewState viewState2 = notificationChildrenContainer.mGroupOverFlowState;
                        if (viewState2 != null) {
                            viewState2.applyToView(notificationChildrenContainer.mOverflowNumber);
                            notificationChildrenContainer.mNeverAppliedGroupState = false;
                        }
                        ViewState viewState3 = notificationChildrenContainer.mHeaderViewState;
                        if (viewState3 != null) {
                            viewState3.applyToView(notificationChildrenContainer.mNotificationHeader);
                        }
                        notificationChildrenContainer.updateChildrenClipping();
                    }
                }
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        public final void onYTranslationAnimationFinished(View view) {
            super.onYTranslationAnimationFinished(view);
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                Objects.requireNonNull(expandableNotificationRow);
                if (expandableNotificationRow.mHeadsupDisappearRunning) {
                    expandableNotificationRow.setHeadsUpAnimatingAway(false);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnDragSuccessListener {
    }

    /* loaded from: classes.dex */
    public interface OnExpandClickListener {
        void onExpandClicked(NotificationEntry notificationEntry, View view, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnExpansionChangedListener {
        void onExpansionChanged(boolean z);
    }

    /* loaded from: classes.dex */
    public class SystemNotificationAsyncTask extends AsyncTask<Void, Void, Boolean> {
        public SystemNotificationAsyncTask() {
        }

        @Override // android.os.AsyncTask
        public final Boolean doInBackground(Void[] voidArr) {
            Context context = ((FrameLayout) ExpandableNotificationRow.this).mContext;
            NotificationEntry notificationEntry = ExpandableNotificationRow.this.mEntry;
            Objects.requireNonNull(notificationEntry);
            return ExpandableNotificationRow.isSystemNotification(context, notificationEntry.mSbn);
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Boolean bool) {
            Boolean bool2 = bool;
            NotificationEntry notificationEntry = ExpandableNotificationRow.this.mEntry;
            if (notificationEntry != null) {
                notificationEntry.mIsSystemNotification = bool2;
            }
        }
    }

    public final void doLongClickCallback(int i, int i2) {
        createMenu();
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        NotificationMenuRowPlugin.MenuItem menuItem = null;
        if (notificationMenuRowPlugin != null) {
            menuItem = notificationMenuRowPlugin.getLongpressMenuItem(((FrameLayout) this).mContext);
        }
        doLongClickCallback(i, i2, menuItem);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getPinnedHeadsUpHeight() {
        return getPinnedHeadsUpHeight(true);
    }

    public final void initialize(NotificationEntry notificationEntry, RemoteInputViewSubcomponent.Factory factory, String str, String str2, PipController$$ExternalSyntheticLambda1 pipController$$ExternalSyntheticLambda1, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, HeadsUpManager headsUpManager, RowContentBindStage rowContentBindStage, OnExpandClickListener onExpandClickListener, ImageLoader$$ExternalSyntheticLambda0 imageLoader$$ExternalSyntheticLambda0, FalsingManager falsingManager, FalsingCollector falsingCollector, StatusBarStateController statusBarStateController, PeopleNotificationIdentifier peopleNotificationIdentifier, OnUserInteractionCallback onUserInteractionCallback, Optional optional, NotificationGutsManager notificationGutsManager) {
        NotificationContentView[] notificationContentViewArr;
        this.mEntry = notificationEntry;
        this.mAppName = str;
        if (this.mMenuRow == null) {
            this.mMenuRow = new NotificationMenuRow(((FrameLayout) this).mContext, peopleNotificationIdentifier);
        }
        if (this.mMenuRow.getMenuView() != null) {
            this.mMenuRow.setAppName(this.mAppName);
        }
        this.mLogger = pipController$$ExternalSyntheticLambda1;
        this.mLoggingKey = str2;
        this.mBypassController = keyguardBypassController;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mGroupExpansionManager = groupExpansionManager;
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        notificationContentView.mGroupMembershipManager = groupMembershipManager;
        this.mHeadsUpManager = headsUpManager;
        this.mRowContentBindStage = rowContentBindStage;
        this.mOnExpandClickListener = onExpandClickListener;
        this.mOnFeedbackClickListener = new ClipboardOverlayController$$ExternalSyntheticLambda1(this, imageLoader$$ExternalSyntheticLambda0, 1);
        this.mFalsingManager = falsingManager;
        this.mFalsingCollector = falsingCollector;
        this.mStatusBarStateController = statusBarStateController;
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        for (NotificationContentView notificationContentView2 : this.mLayouts) {
            PeopleNotificationIdentifier peopleNotificationIdentifier2 = this.mPeopleNotificationIdentifier;
            Objects.requireNonNull(notificationContentView2);
            notificationContentView2.mPeopleIdentifier = peopleNotificationIdentifier2;
            notificationContentView2.mRemoteInputSubcomponentFactory = factory;
        }
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mBubblesManagerOptional = optional;
        this.mNotificationGutsManager = notificationGutsManager;
        NotificationEntry notificationEntry2 = this.mEntry;
        if (notificationEntry2 != null && notificationEntry2.mIsSystemNotification == null && this.mSystemNotificationAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
            this.mSystemNotificationAsyncTask.execute(new Void[0]);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final void onAppearAnimationFinished(boolean z) {
        if (z) {
            resetAllContentAlphas();
            setNotificationFaded(false);
            return;
        }
        setHeadsUpAnimatingAway(false);
    }

    public final void onExpansionChanged(boolean z, boolean z2) {
        boolean isExpanded = isExpanded(false);
        if (this.mIsSummaryWithChildren && (!this.mIsLowPriority || z2)) {
            isExpanded = this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
        }
        if (isExpanded != z2) {
            updateShelfIconColor();
            ExpansionLogger expansionLogger = this.mLogger;
            if (expansionLogger != null) {
                String str = this.mLoggingKey;
                ExpandableNotificationRowController expandableNotificationRowController = (ExpandableNotificationRowController) ((PipController$$ExternalSyntheticLambda1) expansionLogger).f$0;
                Objects.requireNonNull(expandableNotificationRowController);
                NotificationLogger notificationLogger = expandableNotificationRowController.mNotificationLogger;
                Objects.requireNonNull(notificationLogger);
                notificationLogger.mExpansionStateLogger.onExpansionChanged(str, z, isExpanded, notificationLogger.mVisibilityProvider.getLocation(str));
            }
            if (this.mIsSummaryWithChildren) {
                NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
                Objects.requireNonNull(notificationChildrenContainer);
                if (notificationChildrenContainer.mIsLowPriority) {
                    boolean z3 = notificationChildrenContainer.mUserLocked;
                    if (z3) {
                        notificationChildrenContainer.setUserLocked(z3);
                    }
                    notificationChildrenContainer.updateHeaderVisibility(true);
                }
            }
            OnExpansionChangedListener onExpansionChangedListener = this.mExpansionChangedListener;
            if (onExpansionChangedListener != null) {
                onExpansionChangedListener.onExpansionChanged(isExpanded);
            }
        }
    }

    public final void onUiModeChanged() {
        this.mUpdateBackgroundOnUpdate = true;
        reInflateViews();
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            Iterator it = notificationChildrenContainer.mAttachedChildren.iterator();
            while (it.hasNext()) {
                ((ExpandableNotificationRow) it.next()).onUiModeChanged();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public final long performRemoveAnimation(final long j, long j2, final float f, final boolean z, final float f2, final Runnable runnable, final AnimatorListenerAdapter animatorListenerAdapter) {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            super.performRemoveAnimation(j, 0L, f, z, f2, runnable, animatorListenerAdapter);
            return 0L;
        }
        ObjectAnimator translateViewAnimator = getTranslateViewAnimator(0.0f, null);
        translateViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.4
            public final /* synthetic */ long val$delay = 0;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                ExpandableNotificationRow.super.performRemoveAnimation(j, this.val$delay, f, z, f2, runnable, animatorListenerAdapter);
            }
        });
        translateViewAnimator.start();
        return translateViewAnimator.getDuration();
    }

    public final void reset() {
        this.mShowingPublicInitialized = false;
        this.mDismissed = false;
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null || !notificationMenuRowPlugin.isMenuVisible()) {
            resetTranslation();
        }
        ExpandableView.OnHeightChangedListener onHeightChangedListener = this.mOnHeightChangedListener;
        if (onHeightChangedListener != null) {
            onHeightChangedListener.onReset(this);
        }
        requestLayout();
        this.mTargetPoint = null;
    }

    public final void setClipToActualHeight(boolean z) {
        boolean z2;
        boolean z3 = false;
        if (z || this.mUserLocked) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mClipToActualHeight = z2;
        updateClipping();
        NotificationContentView showingLayout = getShowingLayout();
        if (z || this.mUserLocked) {
            z3 = true;
        }
        Objects.requireNonNull(showingLayout);
        showingLayout.mClipToActualHeight = z3;
        showingLayout.updateClipping();
    }

    public final void setExpandAnimationRunning(boolean z) {
        if (z) {
            setAboveShelf(true);
            this.mExpandAnimationRunning = true;
            this.mViewState.cancelAnimations(this);
            this.mNotificationLaunchHeight = Math.max(1, getContext().getResources().getDimensionPixelSize(2131167339)) * 4;
        } else {
            this.mExpandAnimationRunning = false;
            setAboveShelf(isAboveShelf());
            setVisibility(0);
            NotificationGuts notificationGuts = this.mGuts;
            if (notificationGuts != null) {
                notificationGuts.setAlpha(1.0f);
            }
            resetAllContentAlphas();
            this.mExtraWidthForClipping = 0.0f;
            updateClipping();
            invalidate();
            ExpandableNotificationRow expandableNotificationRow = this.mNotificationParent;
            if (expandableNotificationRow != null) {
                expandableNotificationRow.mExtraWidthForClipping = 0.0f;
                expandableNotificationRow.updateClipping();
                expandableNotificationRow.invalidate();
                ExpandableNotificationRow expandableNotificationRow2 = this.mNotificationParent;
                Objects.requireNonNull(expandableNotificationRow2);
                expandableNotificationRow2.mMinimumHeightForClipping = 0;
                expandableNotificationRow2.updateClipping();
                expandableNotificationRow2.invalidate();
            }
        }
        ExpandableNotificationRow expandableNotificationRow3 = this.mNotificationParent;
        if (expandableNotificationRow3 != null) {
            expandableNotificationRow3.mChildIsExpanding = this.mExpandAnimationRunning;
            expandableNotificationRow3.updateClipping();
            expandableNotificationRow3.invalidate();
        }
        updateChildrenVisibility();
        updateClipping();
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView);
        notificationBackgroundView.mExpandAnimationRunning = z;
        Drawable drawable = notificationBackgroundView.mBackground;
        if (drawable instanceof LayerDrawable) {
            ((GradientDrawable) ((LayerDrawable) drawable).getDrawable(0)).setAntiAlias(!z);
        }
        boolean z2 = notificationBackgroundView.mExpandAnimationRunning;
        if (!z2) {
            int i = notificationBackgroundView.mDrawableAlpha;
            notificationBackgroundView.mDrawableAlpha = i;
            if (!z2) {
                notificationBackgroundView.mBackground.setAlpha(i);
            }
        }
        notificationBackgroundView.invalidate();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setHeadsUpIsVisible() {
        this.mMustStayOnScreen = false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final void updateBackgroundTint() {
        updateBackgroundTint(false);
        updateBackgroundForGroupState();
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            ArrayList arrayList = notificationChildrenContainer.mAttachedChildren;
            for (int i = 0; i < arrayList.size(); i++) {
                ((ExpandableNotificationRow) arrayList.get(i)).updateBackgroundForGroupState();
            }
        }
    }

    public static void setChronometerRunningForChild(boolean z, View view) {
        if (view != null) {
            View findViewById = view.findViewById(16908874);
            if (findViewById instanceof Chronometer) {
                ((Chronometer) findViewById).setStarted(z);
            }
        }
    }

    public static void setIconAnimationRunningForChild(boolean z, View view) {
        if (view != null) {
            setIconRunning((ImageView) view.findViewById(16908294), z);
            setIconRunning((ImageView) view.findViewById(16909420), z);
        }
    }

    public static void setIconRunning(ImageView imageView, boolean z) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                if (z) {
                    animationDrawable.start();
                } else {
                    animationDrawable.stop();
                }
            } else if (drawable instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                if (z) {
                    animatedVectorDrawable.start();
                } else {
                    animatedVectorDrawable.stop();
                }
            }
        }
    }

    public final void addChildNotification(ExpandableNotificationRow expandableNotificationRow, int i) {
        boolean z;
        if (this.mChildrenContainer == null) {
            this.mChildrenContainerStub.inflate();
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        Objects.requireNonNull(notificationChildrenContainer);
        if (expandableNotificationRow.getParent() != null) {
            expandableNotificationRow.removeFromTransientContainerForAdditionTo(notificationChildrenContainer);
        }
        if (i < 0) {
            i = notificationChildrenContainer.mAttachedChildren.size();
        }
        notificationChildrenContainer.mAttachedChildren.add(i, expandableNotificationRow);
        notificationChildrenContainer.addView(expandableNotificationRow);
        expandableNotificationRow.setUserLocked(notificationChildrenContainer.mUserLocked);
        View inflateDivider = notificationChildrenContainer.inflateDivider();
        notificationChildrenContainer.addView(inflateDivider);
        notificationChildrenContainer.mDividers.add(i, inflateDivider);
        boolean z2 = expandableNotificationRow.mIsLastChild;
        if (expandableNotificationRow.mContentTransformationAmount != 0.0f) {
            z = true;
        } else {
            z = false;
        }
        boolean z3 = z2 | z;
        expandableNotificationRow.mIsLastChild = false;
        expandableNotificationRow.mContentTransformationAmount = 0.0f;
        if (z3) {
            expandableNotificationRow.updateContentTransformation();
        }
        expandableNotificationRow.setNotificationFaded(notificationChildrenContainer.mContainingNotificationIsFaded);
        ExpandableViewState expandableViewState = expandableNotificationRow.mViewState;
        if (expandableViewState != null) {
            expandableViewState.cancelAnimations(expandableNotificationRow);
            ValueAnimator valueAnimator = expandableNotificationRow.mAppearAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                expandableNotificationRow.mAppearAnimator = null;
            }
            expandableNotificationRow.enableAppearDrawing(false);
        }
        onAttachedChildrenCountChanged();
        expandableNotificationRow.setIsChildInGroup(true, this);
    }

    public final void animateResetTranslation() {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        ObjectAnimator translateViewAnimator = getTranslateViewAnimator(0.0f, null);
        this.mTranslateAnim = translateViewAnimator;
        translateViewAnimator.start();
    }

    public final void applyAudiblyAlertedRecently(boolean z) {
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            NotificationViewWrapper notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapper;
            if (notificationViewWrapper != null) {
                notificationViewWrapper.setRecentlyAudiblyAlerted(z);
            }
            NotificationViewWrapper notificationViewWrapper2 = notificationChildrenContainer.mNotificationHeaderWrapperLowPriority;
            if (notificationViewWrapper2 != null) {
                notificationViewWrapper2.setRecentlyAudiblyAlerted(z);
            }
        }
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        if (notificationContentView.mContractedChild != null) {
            notificationContentView.mContractedWrapper.setRecentlyAudiblyAlerted(z);
        }
        if (notificationContentView.mExpandedChild != null) {
            notificationContentView.mExpandedWrapper.setRecentlyAudiblyAlerted(z);
        }
        if (notificationContentView.mHeadsUpChild != null) {
            notificationContentView.mHeadsUpWrapper.setRecentlyAudiblyAlerted(z);
        }
        NotificationContentView notificationContentView2 = this.mPublicLayout;
        Objects.requireNonNull(notificationContentView2);
        if (notificationContentView2.mContractedChild != null) {
            notificationContentView2.mContractedWrapper.setRecentlyAudiblyAlerted(z);
        }
        if (notificationContentView2.mExpandedChild != null) {
            notificationContentView2.mExpandedWrapper.setRecentlyAudiblyAlerted(z);
        }
        if (notificationContentView2.mHeadsUpChild != null) {
            notificationContentView2.mHeadsUpWrapper.setRecentlyAudiblyAlerted(z);
        }
    }

    public final void applyChildrenRoundness() {
        float f;
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            float f2 = this.mCurrentBottomRoundness;
            Objects.requireNonNull(notificationChildrenContainer);
            boolean z = true;
            for (int size = notificationChildrenContainer.mAttachedChildren.size() - 1; size >= 0; size--) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) notificationChildrenContainer.mAttachedChildren.get(size);
                if (expandableNotificationRow.getVisibility() != 8) {
                    if (z) {
                        f = f2;
                    } else {
                        f = 0.0f;
                    }
                    expandableNotificationRow.setBottomRoundness(f, notificationChildrenContainer.isShown());
                    z = false;
                }
            }
        }
    }

    public final void applyContentTransformation(float f, float f2) {
        NotificationContentView[] notificationContentViewArr;
        if (!this.mIsLastChild) {
            f = 1.0f;
        }
        for (NotificationContentView notificationContentView : this.mLayouts) {
            notificationContentView.setAlpha(f);
            notificationContentView.setTranslationY(f2);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setAlpha(f);
            this.mChildrenContainer.setTranslationY(f2);
        }
    }

    public final boolean areGutsExposed() {
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            Objects.requireNonNull(notificationGuts);
            if (notificationGuts.mExposed) {
                return true;
            }
        }
        return false;
    }

    public final boolean canShowHeadsUp() {
        boolean z;
        boolean z2;
        if (this.mOnKeyguard) {
            StatusBarStateController statusBarStateController = this.mStatusBarStateController;
            if (statusBarStateController == null || !statusBarStateController.isDozing()) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                KeyguardBypassController keyguardBypassController = this.mBypassController;
                if (keyguardBypassController == null || keyguardBypassController.getBypassEnabled()) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    return false;
                }
            }
        }
        return true;
    }

    public final boolean canViewBeDismissed() {
        if (!this.mEntry.isDismissable() || (shouldShowPublic() && this.mSensitiveHiddenInGeneral)) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final boolean childNeedsClipping(View view) {
        boolean z;
        boolean z2;
        if (view instanceof NotificationContentView) {
            NotificationContentView notificationContentView = (NotificationContentView) view;
            if (isClippingNeeded()) {
                return true;
            }
            if (!hasNoRounding()) {
                boolean z3 = false;
                if (this.mCurrentBottomRoundness != 0.0f) {
                    z = true;
                } else {
                    z = false;
                }
                Objects.requireNonNull(notificationContentView);
                NotificationViewWrapper visibleWrapper = notificationContentView.getVisibleWrapper(notificationContentView.mVisibleType);
                if (visibleWrapper == null) {
                    z2 = false;
                } else {
                    z2 = visibleWrapper.shouldClipToRounding(z);
                }
                if (notificationContentView.mUserExpanding) {
                    NotificationViewWrapper visibleWrapper2 = notificationContentView.getVisibleWrapper(notificationContentView.mTransformationStartVisibleType);
                    if (visibleWrapper2 != null) {
                        z3 = visibleWrapper2.shouldClipToRounding(z);
                    }
                    z2 |= z3;
                }
                if (z2) {
                    return true;
                }
            }
        } else if (view == this.mChildrenContainer) {
            if (isClippingNeeded() || !hasNoRounding()) {
                return true;
            }
        } else if (view instanceof NotificationGuts) {
            return !hasNoRounding();
        }
        return super.childNeedsClipping(view);
    }

    public final boolean childrenRequireOverlappingRendering() {
        boolean z;
        RemoteInputView remoteInputView;
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        if (notificationEntry.mSbn.getNotification().isColorized()) {
            return true;
        }
        NotificationContentView showingLayout = getShowingLayout();
        if (showingLayout != null) {
            RemoteInputView remoteInputView2 = showingLayout.mHeadsUpRemoteInput;
            if ((remoteInputView2 == null || !remoteInputView2.isActive()) && ((remoteInputView = showingLayout.mExpandedRemoteInput) == null || !remoteInputView.isActive())) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final void closeRemoteInput() {
        NotificationContentView[] notificationContentViewArr;
        for (NotificationContentView notificationContentView : this.mLayouts) {
            Objects.requireNonNull(notificationContentView);
            RemoteInputView remoteInputView = notificationContentView.mHeadsUpRemoteInput;
            if (remoteInputView != null) {
                RemoteInputView.RemoteEditText remoteEditText = remoteInputView.mEditText;
                int i = RemoteInputView.RemoteEditText.$r8$clinit;
                remoteEditText.defocusIfNeeded(false);
            }
            RemoteInputView remoteInputView2 = notificationContentView.mExpandedRemoteInput;
            if (remoteInputView2 != null) {
                RemoteInputView.RemoteEditText remoteEditText2 = remoteInputView2.mEditText;
                int i2 = RemoteInputView.RemoteEditText.$r8$clinit;
                remoteEditText2.defocusIfNeeded(false);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final ExpandableViewState createExpandableViewState() {
        return new NotificationViewState();
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public final NotificationMenuRowPlugin createMenu() {
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null) {
            return null;
        }
        if (notificationMenuRowPlugin.getMenuView() == null) {
            NotificationMenuRowPlugin notificationMenuRowPlugin2 = this.mMenuRow;
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            notificationMenuRowPlugin2.createMenu(this, notificationEntry.mSbn);
            this.mMenuRow.setAppName(this.mAppName);
            addView(this.mMenuRow.getMenuView(), 0, new FrameLayout.LayoutParams(-1, -1));
        }
        return this.mMenuRow;
    }

    public final ArrayList getAttachedChildren() {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer == null) {
            return null;
        }
        return notificationChildrenContainer.mAttachedChildren;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getCollapsedHeight() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getMinHeight(false);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        Objects.requireNonNull(notificationChildrenContainer);
        return notificationChildrenContainer.getMinHeight(notificationChildrenContainer.getMaxAllowedVisibleChildren(true), false, notificationChildrenContainer.mCurrentHeaderTranslation);
    }

    public final float getContentTransformationShift() {
        return this.mIconTransformContentShift;
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final View getContentView() {
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout();
        }
        return this.mChildrenContainer;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final Path getCustomClipPath(View view) {
        if (view instanceof NotificationGuts) {
            return getClipPath(true);
        }
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void getExtraBottomPadding() {
        if (this.mIsSummaryWithChildren) {
            isGroupExpanded();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getIntrinsicHeight() {
        boolean z;
        if (this.mUserLocked) {
            return this.mActualHeight;
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null && notificationGuts.mExposed) {
            Objects.requireNonNull(notificationGuts);
            NotificationGuts.GutsContent gutsContent = notificationGuts.mGutsContent;
            if (gutsContent == null || !notificationGuts.mExposed) {
                return notificationGuts.getHeight();
            }
            return gutsContent.getActualHeight();
        } else if (isChildInGroup() && !isGroupExpanded()) {
            NotificationContentView notificationContentView = this.mPrivateLayout;
            Objects.requireNonNull(notificationContentView);
            return notificationContentView.getMinHeight(false);
        } else if (this.mSensitive && this.mHideSensitiveForIntrinsicHeight) {
            return getMinHeight(false);
        } else {
            if (this.mIsSummaryWithChildren) {
                return this.mChildrenContainer.getIntrinsicHeight();
            }
            if (canShowHeadsUp()) {
                if (this.mIsHeadsUp || this.mHeadsupDisappearRunning) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    if (this.mIsPinned || this.mHeadsupDisappearRunning) {
                        return getPinnedHeadsUpHeight(true);
                    }
                    if (isExpanded(false)) {
                        return Math.max(getMaxExpandHeight(), getHeadsUpHeight());
                    }
                    return Math.max(getCollapsedHeight(), getHeadsUpHeight());
                }
            }
            if (isExpanded(false)) {
                return getMaxExpandHeight();
            }
            return getCollapsedHeight();
        }
    }

    public final boolean getIsNonblockable() {
        boolean z;
        NotificationEntry notificationEntry;
        Boolean bool;
        NotificationEntry notificationEntry2 = this.mEntry;
        if (notificationEntry2 != null && notificationEntry2.mIsSystemNotification == null) {
            this.mSystemNotificationAsyncTask.cancel(true);
            NotificationEntry notificationEntry3 = this.mEntry;
            Context context = ((FrameLayout) this).mContext;
            Objects.requireNonNull(notificationEntry3);
            notificationEntry3.mIsSystemNotification = isSystemNotification(context, notificationEntry3.mSbn);
        }
        if (this.mEntry.getChannel().isImportanceLockedByOEM() || this.mEntry.getChannel().isImportanceLockedByCriticalDeviceFunction()) {
            z = true;
        } else {
            z = false;
        }
        if (z || (notificationEntry = this.mEntry) == null || (bool = notificationEntry.mIsSystemNotification) == null || !bool.booleanValue() || this.mEntry.getChannel() == null || this.mEntry.getChannel().isBlockable()) {
            return z;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getMaxContentHeight() {
        int i;
        int i2;
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return this.mChildrenContainer.getMaxContentHeight();
        }
        NotificationContentView showingLayout = getShowingLayout();
        Objects.requireNonNull(showingLayout);
        if (showingLayout.mExpandedChild != null) {
            i2 = showingLayout.getViewHeight(1, false);
            i = showingLayout.getExtraRemoteInputHeight(showingLayout.mExpandedRemoteInput);
        } else if (showingLayout.mIsHeadsUp && showingLayout.mHeadsUpChild != null && showingLayout.mContainingNotification.canShowHeadsUp()) {
            i2 = showingLayout.getViewHeight(2, false);
            i = showingLayout.getExtraRemoteInputHeight(showingLayout.mHeadsUpRemoteInput);
        } else if (showingLayout.mContractedChild != null) {
            return showingLayout.getViewHeight(0, false);
        } else {
            return showingLayout.mNotificationMaxHeight;
        }
        return i + i2;
    }

    public final int getMaxExpandHeight() {
        int i;
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        if (notificationContentView.mExpandedChild != null) {
            i = 1;
        } else if (notificationContentView.mContractedChild == null) {
            return notificationContentView.getMinHeight(false);
        } else {
            i = 0;
        }
        return notificationContentView.getExtraRemoteInputHeight(notificationContentView.mExpandedRemoteInput) + notificationContentView.getViewHeight(i, false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getMinHeight(boolean z) {
        NotificationGuts notificationGuts;
        if (!z && (notificationGuts = this.mGuts) != null && notificationGuts.mExposed) {
            Objects.requireNonNull(notificationGuts);
            NotificationGuts.GutsContent gutsContent = notificationGuts.mGutsContent;
            if (gutsContent == null || !notificationGuts.mExposed) {
                return notificationGuts.getHeight();
            }
            return gutsContent.getActualHeight();
        } else if (!z && canShowHeadsUp() && this.mIsHeadsUp && this.mHeadsUpManager.isTrackingHeadsUp()) {
            return getPinnedHeadsUpHeight(false);
        } else {
            if (this.mIsSummaryWithChildren && !isGroupExpanded() && !shouldShowPublic()) {
                NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
                Objects.requireNonNull(notificationChildrenContainer);
                return notificationChildrenContainer.getMinHeight(2, false, notificationChildrenContainer.mCurrentHeaderTranslation);
            } else if (!z && canShowHeadsUp() && this.mIsHeadsUp) {
                return getHeadsUpHeight();
            } else {
                NotificationContentView showingLayout = getShowingLayout();
                Objects.requireNonNull(showingLayout);
                return showingLayout.getMinHeight(false);
            }
        }
    }

    public final NotificationViewWrapper getNotificationViewWrapper() {
        NotificationViewWrapper notificationViewWrapper;
        NotificationViewWrapper notificationViewWrapper2;
        NotificationViewWrapper notificationViewWrapper3;
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            return notificationChildrenContainer.mNotificationHeaderWrapper;
        }
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        if (notificationContentView.mContractedChild != null && (notificationViewWrapper3 = notificationContentView.mContractedWrapper) != null) {
            return notificationViewWrapper3;
        }
        if (notificationContentView.mExpandedChild != null && (notificationViewWrapper2 = notificationContentView.mExpandedWrapper) != null) {
            return notificationViewWrapper2;
        }
        if (notificationContentView.mHeadsUpChild == null || (notificationViewWrapper = notificationContentView.mHeadsUpWrapper) == null) {
            return null;
        }
        return notificationViewWrapper;
    }

    public final int getOriginalIconColor() {
        int i;
        boolean z;
        int i2;
        NotificationViewWrapper notificationViewWrapper;
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            NotificationContentView showingLayout = getShowingLayout();
            Objects.requireNonNull(showingLayout);
            NotificationViewWrapper visibleWrapper = showingLayout.getVisibleWrapper(showingLayout.mVisibleType);
            if (visibleWrapper != null) {
                i = visibleWrapper.getOriginalIconColor();
            } else {
                i = 1;
            }
            if (i != 1) {
                return i;
            }
            NotificationEntry notificationEntry = this.mEntry;
            Context context = ((FrameLayout) this).mContext;
            int i3 = 0;
            if (!this.mIsLowPriority || isExpanded(false)) {
                z = false;
            } else {
                z = true;
            }
            int calculateBgColor = calculateBgColor(false, false);
            Objects.requireNonNull(notificationEntry);
            if (!z) {
                i3 = notificationEntry.mSbn.getNotification().color;
            }
            if (notificationEntry.mCachedContrastColorIsFor == i3 && (i2 = notificationEntry.mCachedContrastColor) != 1) {
                return i2;
            }
            int resolveContrastColor = ContrastColorUtil.resolveContrastColor(context, i3, calculateBgColor);
            notificationEntry.mCachedContrastColorIsFor = i3;
            notificationEntry.mCachedContrastColor = resolveContrastColor;
            return resolveContrastColor;
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        Objects.requireNonNull(notificationChildrenContainer);
        if (notificationChildrenContainer.showingAsLowPriority()) {
            notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapperLowPriority;
        } else {
            notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapper;
        }
        return notificationViewWrapper.getOriginalIconColor();
    }

    public final int getPinnedHeadsUpHeight(boolean z) {
        if (this.mIsSummaryWithChildren) {
            return this.mChildrenContainer.getIntrinsicHeight();
        }
        if (this.mExpandedWhenPinned) {
            return Math.max(getMaxExpandHeight(), getHeadsUpHeight());
        }
        if (z) {
            return Math.max(getCollapsedHeight(), getHeadsUpHeight());
        }
        return getHeadsUpHeight();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final StatusBarIconView getShelfIcon() {
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        return iconPack.mShelfIcon;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final View getShelfTransformationTarget() {
        NotificationViewWrapper notificationViewWrapper;
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            NotificationContentView showingLayout = getShowingLayout();
            Objects.requireNonNull(showingLayout);
            NotificationViewWrapper visibleWrapper = showingLayout.getVisibleWrapper(showingLayout.mVisibleType);
            if (visibleWrapper != null) {
                return visibleWrapper.getShelfTransformationTarget();
            }
            return null;
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        Objects.requireNonNull(notificationChildrenContainer);
        if (notificationChildrenContainer.showingAsLowPriority()) {
            notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapperLowPriority;
        } else {
            notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapper;
        }
        return notificationViewWrapper.getShelfTransformationTarget();
    }

    public final ObjectAnimator getTranslateViewAnimator(final float f, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, TRANSLATE_CONTENT, f);
        if (animatorUpdateListener != null) {
            ofFloat.addUpdateListener(animatorUpdateListener);
        }
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.3
            public boolean cancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator2) {
                this.cancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator2) {
                ExpandableNotificationRow expandableNotificationRow = ExpandableNotificationRow.this;
                AnonymousClass2 r0 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                if (!this.cancelled && f == 0.0f) {
                    NotificationMenuRowPlugin notificationMenuRowPlugin = ExpandableNotificationRow.this.mMenuRow;
                    if (notificationMenuRowPlugin != null) {
                        notificationMenuRowPlugin.resetMenu();
                    }
                    ExpandableNotificationRow.this.mTranslateAnim = null;
                }
            }
        });
        this.mTranslateAnim = ofFloat;
        return ofFloat;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public final float getTranslation() {
        if (this.mDismissUsingRowTranslationX) {
            return getTranslationX();
        }
        areGutsExposed();
        ArrayList<View> arrayList = this.mTranslateableViews;
        if (arrayList == null || arrayList.size() <= 0) {
            return 0.0f;
        }
        return this.mTranslateableViews.get(0).getTranslationX();
    }

    public final ArraySet<NotificationChannel> getUniqueChannels() {
        ArraySet<NotificationChannel> arraySet = new ArraySet<>();
        arraySet.add(this.mEntry.getChannel());
        if (this.mIsSummaryWithChildren) {
            ArrayList attachedChildren = getAttachedChildren();
            int size = attachedChildren.size();
            for (int i = 0; i < size; i++) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) attachedChildren.get(i);
                Objects.requireNonNull(expandableNotificationRow);
                NotificationChannel channel = expandableNotificationRow.mEntry.getChannel();
                NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                UserHandle user = statusBarNotification.getUser();
                NotificationEntry notificationEntry2 = this.mEntry;
                Objects.requireNonNull(notificationEntry2);
                if (user.equals(notificationEntry2.mSbn.getUser())) {
                    String packageName = statusBarNotification.getPackageName();
                    NotificationEntry notificationEntry3 = this.mEntry;
                    Objects.requireNonNull(notificationEntry3);
                    if (packageName.equals(notificationEntry3.mSbn.getPackageName())) {
                        arraySet.add(channel);
                    }
                }
            }
        }
        return arraySet;
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public final boolean hasFinishedInitialization() {
        return this.mEntry.hasFinishedInitialization();
    }

    public final boolean hasNoRounding() {
        if (this.mCurrentBottomRoundness == 0.0f && this.mCurrentTopRoundness == 0.0f) {
            return true;
        }
        return false;
    }

    public final boolean hideBackground() {
        if (!this.mShowNoBackground) {
            return false;
        }
        return true;
    }

    public final void initDimens() {
        this.mMaxSmallHeightBeforeN = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166661);
        this.mMaxSmallHeightBeforeP = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166658);
        this.mMaxSmallHeightBeforeS = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166659);
        this.mMaxSmallHeight = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166657);
        this.mMaxSmallHeightLarge = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166660);
        R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166662);
        this.mMaxExpandedHeight = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166652);
        this.mMaxHeadsUpHeightBeforeN = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166651);
        this.mMaxHeadsUpHeightBeforeP = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166648);
        this.mMaxHeadsUpHeightBeforeS = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166649);
        this.mMaxHeadsUpHeight = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166647);
        this.mMaxHeadsUpHeightIncreased = R$array.getFontScaledHeight(((FrameLayout) this).mContext, 2131166650);
        Resources resources = getResources();
        this.mEnableNonGroupedNotificationExpand = resources.getBoolean(2131034130);
        this.mShowGroupBackgroundWhenExpanded = resources.getBoolean(2131034162);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isChildInGroup() {
        if (this.mNotificationParent != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isContentExpandable() {
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return true;
        }
        NotificationContentView showingLayout = getShowingLayout();
        Objects.requireNonNull(showingLayout);
        return showingLayout.mIsContentExpandable;
    }

    public final boolean isConversation$1() {
        if (this.mPeopleNotificationIdentifier.getPeopleNotificationType(this.mEntry) != 0) {
            return true;
        }
        return false;
    }

    public final boolean isExpandable() {
        if (this.mIsSummaryWithChildren && !shouldShowPublic()) {
            return !this.mChildrenExpanded;
        }
        if (!this.mEnableNonGroupedNotificationExpand || !this.mExpandable) {
            return false;
        }
        return true;
    }

    public final boolean isExpanded(boolean z) {
        if ((!this.mOnKeyguard || z) && ((!this.mHasUserChangedExpansion && (this.mIsSystemExpanded || this.mIsSystemChildExpanded)) || this.mUserExpanded)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isGroupExpanded() {
        return this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
    }

    @Override // android.view.View
    public final boolean isSoundEffectsEnabled() {
        boolean z;
        BooleanSupplier booleanSupplier;
        StatusBarStateController statusBarStateController = this.mStatusBarStateController;
        if (statusBarStateController == null || !statusBarStateController.isDozing() || (booleanSupplier = this.mSecureStateProvider) == null || booleanSupplier.getAsBoolean()) {
            z = false;
        } else {
            z = true;
        }
        if (z || !super.isSoundEffectsEnabled()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean mustStayOnScreen() {
        if (!this.mIsHeadsUp || !this.mMustStayOnScreen) {
            return false;
        }
        return true;
    }

    public final void onAttachedChildrenCountChanged() {
        boolean z;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer == null || notificationChildrenContainer.mAttachedChildren.size() <= 0) {
            z = false;
        } else {
            z = true;
        }
        this.mIsSummaryWithChildren = z;
        if (z) {
            NotificationChildrenContainer notificationChildrenContainer2 = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer2);
            NotificationViewWrapper notificationViewWrapper = notificationChildrenContainer2.mNotificationHeaderWrapper;
            if (notificationViewWrapper == null || notificationViewWrapper.getNotificationHeader() == null) {
                this.mChildrenContainer.recreateNotificationHeader(this.mExpandClickListener, isConversation$1());
            }
        }
        getShowingLayout().updateBackgroundColor(false);
        NotificationContentView notificationContentView = this.mPrivateLayout;
        boolean isExpandable = isExpandable();
        Objects.requireNonNull(notificationContentView);
        notificationContentView.updateExpandButtonsDuringLayout(isExpandable, false);
        if (this.mIsSummaryWithChildren) {
            this.mChildrenContainer.updateChildrenAppearance();
        }
        updateChildrenVisibility();
        applyChildrenRoundness();
    }

    public final void onExpandedByGesture(boolean z) {
        int i;
        if (this.mGroupMembershipManager.isGroupSummary(this.mEntry)) {
            i = 410;
        } else {
            i = 409;
        }
        MetricsLogger.action(((FrameLayout) this).mContext, i, z);
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        NotificationViewWrapper notificationViewWrapper;
        View view;
        Trace.beginSection(appendTraceStyleTag("ExpNotRow#onLayout"));
        int intrinsicHeight = getIntrinsicHeight();
        super.onLayout(z, i, i2, i3, i4);
        if (intrinsicHeight != getIntrinsicHeight() && (intrinsicHeight != 0 || this.mActualHeight > 0)) {
            notifyHeightChanged(true);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onParentHeightUpdate();
        }
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            NotificationContentView showingLayout = getShowingLayout();
            Objects.requireNonNull(showingLayout);
            notificationViewWrapper = showingLayout.getVisibleWrapper(showingLayout.mVisibleType);
        } else {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            if (notificationChildrenContainer.showingAsLowPriority()) {
                notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapperLowPriority;
            } else {
                notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapper;
            }
        }
        if (notificationViewWrapper == null) {
            view = null;
        } else {
            view = notificationViewWrapper.getIcon();
        }
        if (view != null) {
            this.mIconTransformContentShift = view.getHeight() + getRelativeTopPadding(view);
        } else {
            this.mIconTransformContentShift = this.mContentShift;
        }
        LayoutListener layoutListener = this.mLayoutListener;
        if (layoutListener != null) {
            NotificationMenuRow notificationMenuRow = (NotificationMenuRow) layoutListener;
            notificationMenuRow.mIconsPlaced = false;
            notificationMenuRow.setMenuLocation();
            ExpandableNotificationRow expandableNotificationRow = notificationMenuRow.mParent;
            Objects.requireNonNull(expandableNotificationRow);
            expandableNotificationRow.mLayoutListener = null;
        }
        Trace.endSection();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        Trace.beginSection(appendTraceStyleTag("ExpNotRow#onMeasure"));
        super.onMeasure(i, i2);
        Trace.endSection();
    }

    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v9, types: [com.android.systemui.statusbar.policy.RemoteInputViewController, com.android.systemui.statusbar.policy.RemoteInputView] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onNotificationUpdated() {
        /*
            Method dump skipped, instructions count: 565
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.onNotificationUpdated():void");
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(NotificationMenuRowPlugin notificationMenuRowPlugin, Context context) {
        boolean z;
        NotificationMenuRowPlugin notificationMenuRowPlugin2 = notificationMenuRowPlugin;
        NotificationMenuRowPlugin notificationMenuRowPlugin3 = this.mMenuRow;
        if (notificationMenuRowPlugin3 == null || notificationMenuRowPlugin3.getMenuView() == null) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            removeView(this.mMenuRow.getMenuView());
        }
        if (notificationMenuRowPlugin2 != null) {
            this.mMenuRow = notificationMenuRowPlugin2;
            if (notificationMenuRowPlugin2.shouldUseDefaultMenuItems()) {
                ArrayList<NotificationMenuRowPlugin.MenuItem> arrayList = new ArrayList<>();
                arrayList.add(NotificationMenuRow.createConversationItem(((FrameLayout) this).mContext));
                arrayList.add(NotificationMenuRow.createPartialConversationItem(((FrameLayout) this).mContext));
                arrayList.add(NotificationMenuRow.createInfoItem(((FrameLayout) this).mContext));
                arrayList.add(NotificationMenuRow.createSnoozeItem(((FrameLayout) this).mContext));
                this.mMenuRow.setMenuItems(arrayList);
            }
            if (z) {
                createMenu();
            }
        }
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(NotificationMenuRowPlugin notificationMenuRowPlugin) {
        boolean z;
        if (this.mMenuRow.getMenuView() != null) {
            z = true;
        } else {
            z = false;
        }
        this.mMenuRow = new NotificationMenuRow(((FrameLayout) this).mContext, this.mPeopleNotificationIdentifier);
        if (z) {
            createMenu();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final void onTap() {
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        if (notificationEntry.mSbn.getNotification().contentIntent != null) {
            NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
            Objects.requireNonNull(notificationBackgroundView);
            notificationBackgroundView.mIsPressedAllowed = false;
        }
    }

    public final void performDismiss(boolean z) {
        OnUserInteractionCallback onUserInteractionCallback;
        ArrayList attachedChildren;
        int indexOf;
        ((MetricsLogger) Dependency.get(MetricsLogger.class)).count("notification_dismissed", 1);
        this.mDismissed = true;
        this.mRefocusOnDismiss = z;
        this.mLongPressListener = null;
        this.mDragController = null;
        this.mGroupParentWhenDismissed = this.mNotificationParent;
        this.mChildAfterViewWhenDismissed = null;
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        StatusBarIconView statusBarIconView = iconPack.mStatusBarIcon;
        Objects.requireNonNull(statusBarIconView);
        Runnable runnable = statusBarIconView.mOnDismissListener;
        if (runnable != null) {
            runnable.run();
        }
        if (isChildInGroup() && (indexOf = (attachedChildren = this.mNotificationParent.getAttachedChildren()).indexOf(this)) != -1 && indexOf < attachedChildren.size() - 1) {
            this.mChildAfterViewWhenDismissed = (View) attachedChildren.get(indexOf + 1);
        }
        if (this.mEntry.isDismissable() && (onUserInteractionCallback = this.mOnUserInteractionCallback) != null) {
            NotificationEntry notificationEntry2 = this.mEntry;
            onUserInteractionCallback.onDismiss(notificationEntry2, 2, onUserInteractionCallback.getGroupSummaryToDismiss(notificationEntry2));
        }
    }

    public final void reInflateViews() {
        View view;
        NotificationContentView[] notificationContentViewArr;
        HybridNotificationView hybridNotificationView;
        int i;
        Trace.beginSection("ExpandableNotificationRow#reInflateViews");
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            AnonymousClass1 r3 = this.mExpandClickListener;
            Objects.requireNonNull(this.mEntry);
            NotificationHeaderView notificationHeaderView = notificationChildrenContainer.mNotificationHeader;
            if (notificationHeaderView != null) {
                notificationChildrenContainer.removeView(notificationHeaderView);
                notificationChildrenContainer.mNotificationHeader = null;
            }
            NotificationHeaderView notificationHeaderView2 = notificationChildrenContainer.mNotificationHeaderLowPriority;
            if (notificationHeaderView2 != null) {
                notificationChildrenContainer.removeView(notificationHeaderView2);
                notificationChildrenContainer.mNotificationHeaderLowPriority = null;
            }
            notificationChildrenContainer.recreateNotificationHeader(r3, notificationChildrenContainer.mIsConversation);
            notificationChildrenContainer.initDimens();
            for (int i2 = 0; i2 < notificationChildrenContainer.mDividers.size(); i2++) {
                View view2 = (View) notificationChildrenContainer.mDividers.get(i2);
                int indexOfChild = notificationChildrenContainer.indexOfChild(view2);
                notificationChildrenContainer.removeView(view2);
                View inflateDivider = notificationChildrenContainer.inflateDivider();
                notificationChildrenContainer.addView(inflateDivider, indexOfChild);
                notificationChildrenContainer.mDividers.set(i2, inflateDivider);
            }
            notificationChildrenContainer.removeView(notificationChildrenContainer.mOverflowNumber);
            notificationChildrenContainer.mOverflowNumber = null;
            notificationChildrenContainer.mGroupOverFlowState = null;
            notificationChildrenContainer.updateGroupOverflow();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            int indexOfChild2 = indexOfChild(notificationGuts);
            removeView(notificationGuts);
            NotificationGuts notificationGuts2 = (NotificationGuts) LayoutInflater.from(((FrameLayout) this).mContext).inflate(2131624328, (ViewGroup) this, false);
            this.mGuts = notificationGuts2;
            if (notificationGuts.mExposed) {
                i = 0;
            } else {
                i = 8;
            }
            notificationGuts2.setVisibility(i);
            addView(this.mGuts, indexOfChild2);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin == null) {
            view = null;
        } else {
            view = notificationMenuRowPlugin.getMenuView();
        }
        if (view != null) {
            int indexOfChild3 = indexOfChild(view);
            removeView(view);
            NotificationMenuRowPlugin notificationMenuRowPlugin2 = this.mMenuRow;
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            notificationMenuRowPlugin2.createMenu(this, notificationEntry.mSbn);
            this.mMenuRow.setAppName(this.mAppName);
            addView(this.mMenuRow.getMenuView(), indexOfChild3);
        }
        for (NotificationContentView notificationContentView : this.mLayouts) {
            Objects.requireNonNull(notificationContentView);
            notificationContentView.mMinContractedHeight = notificationContentView.getResources().getDimensionPixelSize(2131166365);
            notificationContentView.getResources().getDimensionPixelSize(17105388);
            if (notificationContentView.mIsChildInGroup && (hybridNotificationView = notificationContentView.mSingleLineView) != null) {
                notificationContentView.removeView(hybridNotificationView);
                notificationContentView.mSingleLineView = null;
                notificationContentView.updateAllSingleLineViews();
            }
        }
        NotificationEntry notificationEntry2 = this.mEntry;
        Objects.requireNonNull(notificationEntry2);
        notificationEntry2.mSbn.clearPackageContext();
        RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(this.mEntry);
        stageParams.mViewsNeedReinflation = true;
        stageParams.mDirtyContentViews = stageParams.mContentViews | stageParams.mDirtyContentViews;
        this.mRowContentBindStage.requestRebind(this.mEntry, null);
        Trace.endSection();
    }

    public final void removeChildNotification(ExpandableNotificationRow expandableNotificationRow) {
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.removeNotification(expandableNotificationRow);
        }
        onAttachedChildrenCountChanged();
        expandableNotificationRow.setIsChildInGroup(false, null);
        expandableNotificationRow.setBottomRoundness(0.0f, false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final void resetAllContentAlphas() {
        this.mPrivateLayout.setAlpha(1.0f);
        this.mPrivateLayout.setLayerType(0, null);
        this.mPublicLayout.setAlpha(1.0f);
        this.mPublicLayout.setLayerType(0, null);
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setAlpha(1.0f);
            this.mChildrenContainer.setLayerType(0, null);
        }
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public final void resetTranslation() {
        Animator animator = this.mTranslateAnim;
        if (animator != null) {
            animator.cancel();
        }
        if (this.mDismissUsingRowTranslationX) {
            setTranslationX(0.0f);
        } else if (this.mTranslateableViews != null) {
            for (int i = 0; i < this.mTranslateableViews.size(); i++) {
                this.mTranslateableViews.get(i).setTranslationX(0.0f);
            }
            invalidateOutline();
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            IconPack iconPack = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack);
            iconPack.mShelfIcon.setScrollX(0);
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            notificationMenuRowPlugin.resetMenu();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setActualHeight(int i, boolean z) {
        boolean z2;
        NotificationContentView[] notificationContentViewArr;
        int i2;
        ViewGroup viewGroup;
        if (i != this.mActualHeight) {
            z2 = true;
        } else {
            z2 = false;
        }
        super.setActualHeight(i, z);
        if (z2 && this.mRemoved && (viewGroup = (ViewGroup) getParent()) != null) {
            viewGroup.invalidate();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts == null || !notificationGuts.mExposed) {
            int max = Math.max(getMinHeight(false), i);
            for (NotificationContentView notificationContentView : this.mLayouts) {
                Objects.requireNonNull(notificationContentView);
                notificationContentView.mUnrestrictedContentHeight = Math.max(max, notificationContentView.getMinHeight(false));
                notificationContentView.mContentHeight = Math.min(notificationContentView.mUnrestrictedContentHeight, (notificationContentView.mContainingNotification.getIntrinsicHeight() - notificationContentView.getExtraRemoteInputHeight(notificationContentView.mExpandedRemoteInput)) - notificationContentView.getExtraRemoteInputHeight(notificationContentView.mHeadsUpRemoteInput));
                notificationContentView.selectLayout(notificationContentView.mAnimate, false);
                if (notificationContentView.mContractedChild != null) {
                    int minContentHeightHint = notificationContentView.getMinContentHeightHint();
                    NotificationViewWrapper visibleWrapper = notificationContentView.getVisibleWrapper(notificationContentView.mVisibleType);
                    if (visibleWrapper != null) {
                        visibleWrapper.setContentHeight(notificationContentView.mUnrestrictedContentHeight, minContentHeightHint);
                    }
                    NotificationViewWrapper visibleWrapper2 = notificationContentView.getVisibleWrapper(notificationContentView.mTransformationStartVisibleType);
                    if (visibleWrapper2 != null) {
                        visibleWrapper2.setContentHeight(notificationContentView.mUnrestrictedContentHeight, minContentHeightHint);
                    }
                    notificationContentView.updateClipping();
                    notificationContentView.invalidateOutline();
                }
            }
            if (this.mIsSummaryWithChildren) {
                NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
                Objects.requireNonNull(notificationChildrenContainer);
                if (notificationChildrenContainer.mUserLocked) {
                    notificationChildrenContainer.mActualHeight = i;
                    float groupExpandFraction = notificationChildrenContainer.getGroupExpandFraction();
                    boolean showingAsLowPriority = notificationChildrenContainer.showingAsLowPriority();
                    if (notificationChildrenContainer.mUserLocked && notificationChildrenContainer.showingAsLowPriority()) {
                        float groupExpandFraction2 = notificationChildrenContainer.getGroupExpandFraction();
                        notificationChildrenContainer.mNotificationHeaderWrapper.transformFrom(notificationChildrenContainer.mNotificationHeaderWrapperLowPriority, groupExpandFraction2);
                        notificationChildrenContainer.mNotificationHeader.setVisibility(0);
                        notificationChildrenContainer.mNotificationHeaderWrapperLowPriority.transformTo(notificationChildrenContainer.mNotificationHeaderWrapper, groupExpandFraction2);
                    }
                    int maxAllowedVisibleChildren = notificationChildrenContainer.getMaxAllowedVisibleChildren(true);
                    int size = notificationChildrenContainer.mAttachedChildren.size();
                    for (int i3 = 0; i3 < size; i3++) {
                        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) notificationChildrenContainer.mAttachedChildren.get(i3);
                        if (showingAsLowPriority) {
                            i2 = expandableNotificationRow.getShowingLayout().getMinHeight(false);
                        } else if (expandableNotificationRow.isExpanded(true)) {
                            i2 = expandableNotificationRow.getMaxExpandHeight();
                        } else {
                            i2 = expandableNotificationRow.getShowingLayout().getMinHeight(true);
                        }
                        float f = i2;
                        if (i3 < maxAllowedVisibleChildren) {
                            expandableNotificationRow.setActualHeight((int) R$array.interpolate(expandableNotificationRow.getShowingLayout().getMinHeight(false), f, groupExpandFraction), false);
                        } else {
                            expandableNotificationRow.setActualHeight((int) f, false);
                        }
                    }
                }
            }
            NotificationGuts notificationGuts2 = this.mGuts;
            if (notificationGuts2 != null) {
                notificationGuts2.mActualHeight = i;
                notificationGuts2.invalidate();
            }
            NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
            if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
                this.mMenuRow.onParentHeightUpdate();
            }
            if (this.mOnIntrinsicHeightReachedRunnable != null && this.mActualHeight == getIntrinsicHeight()) {
                this.mOnIntrinsicHeightReachedRunnable.run();
                this.mOnIntrinsicHeightReachedRunnable = null;
                return;
            }
            return;
        }
        Objects.requireNonNull(notificationGuts);
        notificationGuts.mActualHeight = i;
        notificationGuts.invalidate();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setActualHeightAnimating(boolean z) {
        NotificationContentView notificationContentView = this.mPrivateLayout;
        if (notificationContentView == null) {
            return;
        }
        if (!z) {
            notificationContentView.mContentHeightAtAnimationStart = -1;
        } else {
            Objects.requireNonNull(notificationContentView);
        }
    }

    public final void setChildrenExpanded(boolean z) {
        this.mChildrenExpanded = z;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setChildrenExpanded$1(z);
        }
        updateBackgroundForGroupState();
        updateClickAndFocus();
    }

    public final void setChronometerRunning(boolean z) {
        boolean z2;
        this.mLastChronometerRunning = z;
        NotificationContentView notificationContentView = this.mPrivateLayout;
        boolean z3 = true;
        if (notificationContentView != null) {
            if (z || this.mIsPinned) {
                z2 = true;
            } else {
                z2 = false;
            }
            View view = notificationContentView.mContractedChild;
            View view2 = notificationContentView.mExpandedChild;
            View view3 = notificationContentView.mHeadsUpChild;
            setChronometerRunningForChild(z2, view);
            setChronometerRunningForChild(z2, view2);
            setChronometerRunningForChild(z2, view3);
        }
        NotificationContentView notificationContentView2 = this.mPublicLayout;
        if (notificationContentView2 != null) {
            if (!z && !this.mIsPinned) {
                z3 = false;
            }
            View view4 = notificationContentView2.mContractedChild;
            View view5 = notificationContentView2.mExpandedChild;
            View view6 = notificationContentView2.mHeadsUpChild;
            setChronometerRunningForChild(z3, view4);
            setChronometerRunningForChild(z3, view5);
            setChronometerRunningForChild(z3, view6);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            ArrayList arrayList = notificationChildrenContainer.mAttachedChildren;
            for (int i = 0; i < arrayList.size(); i++) {
                ((ExpandableNotificationRow) arrayList.get(i)).setChronometerRunning(z);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setClipBottomAmount(int i) {
        NotificationContentView[] notificationContentViewArr;
        if (!this.mExpandAnimationRunning) {
            if (i != this.mClipBottomAmount) {
                super.setClipBottomAmount(i);
                for (NotificationContentView notificationContentView : this.mLayouts) {
                    Objects.requireNonNull(notificationContentView);
                    notificationContentView.mClipBottomAmount = i;
                    notificationContentView.updateClipping();
                }
                NotificationGuts notificationGuts = this.mGuts;
                if (notificationGuts != null) {
                    Objects.requireNonNull(notificationGuts);
                    notificationGuts.mClipBottomAmount = i;
                    notificationGuts.invalidate();
                }
            }
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (!(notificationChildrenContainer == null || this.mChildIsExpanding)) {
                Objects.requireNonNull(notificationChildrenContainer);
                notificationChildrenContainer.mClipBottomAmount = i;
                notificationChildrenContainer.updateChildrenClipping();
            }
        }
    }

    public final void setFeedbackIcon(FeedbackIcon feedbackIcon) {
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            NotificationViewWrapper notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapper;
            if (notificationViewWrapper != null) {
                notificationViewWrapper.setFeedbackIcon(feedbackIcon);
            }
            NotificationViewWrapper notificationViewWrapper2 = notificationChildrenContainer.mNotificationHeaderWrapperLowPriority;
            if (notificationViewWrapper2 != null) {
                notificationViewWrapper2.setFeedbackIcon(feedbackIcon);
            }
        }
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        if (notificationContentView.mContractedChild != null) {
            notificationContentView.mContractedWrapper.setFeedbackIcon(feedbackIcon);
        }
        if (notificationContentView.mExpandedChild != null) {
            notificationContentView.mExpandedWrapper.setFeedbackIcon(feedbackIcon);
        }
        if (notificationContentView.mHeadsUpChild != null) {
            notificationContentView.mHeadsUpWrapper.setFeedbackIcon(feedbackIcon);
        }
        NotificationContentView notificationContentView2 = this.mPublicLayout;
        Objects.requireNonNull(notificationContentView2);
        if (notificationContentView2.mContractedChild != null) {
            notificationContentView2.mContractedWrapper.setFeedbackIcon(feedbackIcon);
        }
        if (notificationContentView2.mExpandedChild != null) {
            notificationContentView2.mExpandedWrapper.setFeedbackIcon(feedbackIcon);
        }
        if (notificationContentView2.mHeadsUpChild != null) {
            notificationContentView2.mHeadsUpWrapper.setFeedbackIcon(feedbackIcon);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setHideSensitiveForIntrinsicHeight(boolean z) {
        this.mHideSensitiveForIntrinsicHeight = z;
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            ArrayList arrayList = notificationChildrenContainer.mAttachedChildren;
            for (int i = 0; i < arrayList.size(); i++) {
                ((ExpandableNotificationRow) arrayList.get(i)).setHideSensitiveForIntrinsicHeight(z);
            }
        }
    }

    public final void setIconAnimationRunning(boolean z) {
        NotificationContentView[] notificationContentViewArr;
        for (NotificationContentView notificationContentView : this.mLayouts) {
            if (notificationContentView != null) {
                View view = notificationContentView.mContractedChild;
                View view2 = notificationContentView.mExpandedChild;
                View view3 = notificationContentView.mHeadsUpChild;
                setIconAnimationRunningForChild(z, view);
                setIconAnimationRunningForChild(z, view2);
                setIconAnimationRunningForChild(z, view3);
            }
        }
        if (this.mIsSummaryWithChildren) {
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer);
            NotificationViewWrapper notificationViewWrapper = notificationChildrenContainer.mNotificationHeaderWrapper;
            if (notificationViewWrapper != null) {
                setIconAnimationRunningForChild(z, notificationViewWrapper.getIcon());
            }
            NotificationChildrenContainer notificationChildrenContainer2 = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer2);
            NotificationViewWrapper notificationViewWrapper2 = notificationChildrenContainer2.mNotificationHeaderWrapperLowPriority;
            if (notificationViewWrapper2 != null) {
                setIconAnimationRunningForChild(z, notificationViewWrapper2.getIcon());
            }
            NotificationChildrenContainer notificationChildrenContainer3 = this.mChildrenContainer;
            Objects.requireNonNull(notificationChildrenContainer3);
            ArrayList arrayList = notificationChildrenContainer3.mAttachedChildren;
            for (int i = 0; i < arrayList.size(); i++) {
                ((ExpandableNotificationRow) arrayList.get(i)).setIconAnimationRunning(z);
            }
        }
        this.mIconAnimationRunning = z;
    }

    public final void setIsChildInGroup(boolean z, ExpandableNotificationRow expandableNotificationRow) {
        ExpandableNotificationRow expandableNotificationRow2;
        if (this.mExpandAnimationRunning && !z && (expandableNotificationRow2 = this.mNotificationParent) != null) {
            expandableNotificationRow2.mChildIsExpanding = false;
            expandableNotificationRow2.updateClipping();
            expandableNotificationRow2.invalidate();
            ExpandableNotificationRow expandableNotificationRow3 = this.mNotificationParent;
            Objects.requireNonNull(expandableNotificationRow3);
            expandableNotificationRow3.mExpandingClipPath = null;
            expandableNotificationRow3.invalidate();
            ExpandableNotificationRow expandableNotificationRow4 = this.mNotificationParent;
            Objects.requireNonNull(expandableNotificationRow4);
            expandableNotificationRow4.mExtraWidthForClipping = 0.0f;
            expandableNotificationRow4.updateClipping();
            expandableNotificationRow4.invalidate();
            ExpandableNotificationRow expandableNotificationRow5 = this.mNotificationParent;
            Objects.requireNonNull(expandableNotificationRow5);
            expandableNotificationRow5.mMinimumHeightForClipping = 0;
            expandableNotificationRow5.updateClipping();
            expandableNotificationRow5.invalidate();
        }
        if (!z) {
            expandableNotificationRow = null;
        }
        this.mNotificationParent = expandableNotificationRow;
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        notificationContentView.mIsChildInGroup = z;
        if (notificationContentView.mContractedChild != null) {
            notificationContentView.mContractedWrapper.setIsChildInGroup(z);
        }
        if (notificationContentView.mExpandedChild != null) {
            notificationContentView.mExpandedWrapper.setIsChildInGroup(notificationContentView.mIsChildInGroup);
        }
        if (notificationContentView.mHeadsUpChild != null) {
            notificationContentView.mHeadsUpWrapper.setIsChildInGroup(notificationContentView.mIsChildInGroup);
        }
        notificationContentView.updateAllSingleLineViews();
        updateBackgroundForGroupState();
        updateClickAndFocus();
        if (this.mNotificationParent != null) {
            setOverrideTintColor(0, 0.0f);
            this.mNotificationParent.updateBackgroundForGroupState();
        }
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        boolean z2 = !isChildInGroup();
        Objects.requireNonNull(notificationBackgroundView);
        if (z2 != notificationBackgroundView.mBottomAmountClips) {
            notificationBackgroundView.mBottomAmountClips = z2;
            notificationBackgroundView.invalidate();
        }
    }

    public final void setNeedsRedaction(boolean z) {
        if (this.mNeedsRedaction != z) {
            this.mNeedsRedaction = z;
            if (!this.mRemoved) {
                RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(this.mEntry);
                if (z) {
                    stageParams.requireContentViews(8);
                } else {
                    stageParams.markContentViewsFreeable(8);
                }
                this.mRowContentBindStage.requestRebind(this.mEntry, null);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.NotificationFadeAware
    public final void setNotificationFaded(boolean z) {
        NotificationContentView[] notificationContentViewArr;
        NotificationContentView[] notificationContentViewArr2;
        this.mIsFaded = z;
        if (childrenRequireOverlappingRendering()) {
            NotificationFadeAware.setLayerTypeForFaded(this, z);
            NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
            if (notificationChildrenContainer instanceof NotificationFadeAware) {
                notificationChildrenContainer.setNotificationFaded(false);
            } else {
                NotificationFadeAware.setLayerTypeForFaded(notificationChildrenContainer, false);
            }
            for (NotificationContentView notificationContentView : this.mLayouts) {
                if (notificationContentView instanceof NotificationFadeAware) {
                    notificationContentView.setNotificationFaded(false);
                } else {
                    NotificationFadeAware.setLayerTypeForFaded(notificationContentView, false);
                }
            }
            return;
        }
        NotificationFadeAware.setLayerTypeForFaded(this, false);
        NotificationChildrenContainer notificationChildrenContainer2 = this.mChildrenContainer;
        if (notificationChildrenContainer2 instanceof NotificationFadeAware) {
            notificationChildrenContainer2.setNotificationFaded(z);
        } else {
            NotificationFadeAware.setLayerTypeForFaded(notificationChildrenContainer2, z);
        }
        for (NotificationContentView notificationContentView2 : this.mLayouts) {
            if (notificationContentView2 instanceof NotificationFadeAware) {
                notificationContentView2.setNotificationFaded(z);
            } else {
                NotificationFadeAware.setLayerTypeForFaded(notificationContentView2, z);
            }
        }
    }

    public final void setOnKeyguard(boolean z) {
        if (z != this.mOnKeyguard) {
            boolean isAboveShelf = isAboveShelf();
            boolean isExpanded = isExpanded(false);
            this.mOnKeyguard = z;
            onExpansionChanged(false, isExpanded);
            if (isExpanded != isExpanded(false)) {
                if (this.mIsSummaryWithChildren) {
                    this.mChildrenContainer.updateGroupOverflow();
                }
                notifyHeightChanged(false);
            }
            if (isAboveShelf() != isAboveShelf) {
                ((AboveShelfObserver) this.mAboveShelfChangedListener).onAboveShelfStateChanged(!isAboveShelf);
            }
        }
        updateRippleAllowed();
    }

    public final void setSystemExpanded(boolean z) {
        if (z != this.mIsSystemExpanded) {
            boolean isExpanded = isExpanded(false);
            this.mIsSystemExpanded = z;
            notifyHeightChanged(false);
            onExpansionChanged(false, isExpanded);
            if (this.mIsSummaryWithChildren) {
                this.mChildrenContainer.updateGroupOverflow();
                if (this.mIsSummaryWithChildren) {
                    this.mChildrenContainer.updateExpansionStates();
                }
            }
        }
    }

    public final void setUserExpanded(boolean z, boolean z2) {
        this.mFalsingCollector.setNotificationExpanded();
        if (this.mIsSummaryWithChildren && !shouldShowPublic() && z2 && !this.mChildrenContainer.showingAsLowPriority()) {
            boolean isGroupExpanded = this.mGroupExpansionManager.isGroupExpanded(this.mEntry);
            this.mGroupExpansionManager.setGroupExpanded(this.mEntry, z);
            onExpansionChanged(true, isGroupExpanded);
        } else if (!z || this.mExpandable) {
            boolean isExpanded = isExpanded(false);
            this.mHasUserChangedExpansion = true;
            this.mUserExpanded = z;
            onExpansionChanged(true, isExpanded);
            if (!isExpanded && isExpanded(false) && this.mActualHeight != getIntrinsicHeight()) {
                notifyHeightChanged(true);
            }
        }
    }

    public final void setUserLocked(boolean z) {
        this.mUserLocked = z;
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        notificationContentView.mUserExpanding = z;
        if (z) {
            notificationContentView.mTransformationStartVisibleType = notificationContentView.mVisibleType;
        } else {
            notificationContentView.mTransformationStartVisibleType = -1;
            int calculateVisibleType = notificationContentView.calculateVisibleType();
            notificationContentView.mVisibleType = calculateVisibleType;
            notificationContentView.updateViewVisibilities(calculateVisibleType);
            notificationContentView.updateBackgroundColor(false);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            notificationChildrenContainer.setUserLocked(z);
            if (!this.mIsSummaryWithChildren) {
                return;
            }
            if (z || !isGroupExpanded()) {
                updateBackgroundForGroupState();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean shouldClipToActualHeight() {
        if (!this.mExpandAnimationRunning) {
            return true;
        }
        return false;
    }

    public final boolean shouldShowPublic() {
        if (!this.mSensitive || !this.mHideSensitiveForIntrinsicHeight) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean showingPulsing() {
        boolean z;
        boolean z2;
        boolean z3;
        if (this.mIsHeadsUp || this.mHeadsupDisappearRunning) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return false;
        }
        StatusBarStateController statusBarStateController = this.mStatusBarStateController;
        if (statusBarStateController == null || !statusBarStateController.isDozing()) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (!z2) {
            if (!this.mOnKeyguard) {
                return false;
            }
            KeyguardBypassController keyguardBypassController = this.mBypassController;
            if (keyguardBypassController == null || keyguardBypassController.getBypassEnabled()) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (!z3) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x009c, code lost:
        if (r4.mUserLocked != false) goto L_0x009e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x009e, code lost:
        if (r0 != 0) goto L_0x00a3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a1, code lost:
        r0 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:33:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateBackgroundForGroupState() {
        /*
            r6 = this;
            boolean r0 = r6.mIsSummaryWithChildren
            r1 = 0
            r2 = 1
            r3 = 0
            if (r0 == 0) goto L_0x005f
            boolean r0 = r6.mShowGroupBackgroundWhenExpanded
            if (r0 != 0) goto L_0x001d
            boolean r0 = r6.isGroupExpanded()
            if (r0 == 0) goto L_0x001d
            boolean r0 = r6.isGroupExpansionChanging()
            if (r0 != 0) goto L_0x001d
            boolean r0 = r6.mUserLocked
            if (r0 != 0) goto L_0x001d
            r0 = r2
            goto L_0x001e
        L_0x001d:
            r0 = r3
        L_0x001e:
            r6.mShowNoBackground = r0
            com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer r4 = r6.mChildrenContainer
            java.util.Objects.requireNonNull(r4)
            android.view.NotificationHeaderView r5 = r4.mNotificationHeader
            if (r5 == 0) goto L_0x0045
            if (r0 == 0) goto L_0x0042
            android.graphics.drawable.ColorDrawable r0 = new android.graphics.drawable.ColorDrawable
            r0.<init>()
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r5 = r4.mContainingNotification
            java.util.Objects.requireNonNull(r5)
            int r2 = r5.calculateBgColor(r2, r2)
            r0.setColor(r2)
            android.view.NotificationHeaderView r2 = r4.mNotificationHeader
            r2.setHeaderBackgroundDrawable(r0)
            goto L_0x0045
        L_0x0042:
            r5.setHeaderBackgroundDrawable(r1)
        L_0x0045:
            com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer r0 = r6.mChildrenContainer
            java.util.Objects.requireNonNull(r0)
            java.util.ArrayList r0 = r0.mAttachedChildren
            r2 = r3
        L_0x004d:
            int r4 = r0.size()
            if (r2 >= r4) goto L_0x00aa
            java.lang.Object r4 = r0.get(r2)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r4
            r4.updateBackgroundForGroupState()
            int r2 = r2 + 1
            goto L_0x004d
        L_0x005f:
            boolean r0 = r6.isChildInGroup()
            if (r0 == 0) goto L_0x00a8
            com.android.systemui.statusbar.notification.row.NotificationContentView r0 = r6.getShowingLayout()
            java.util.Objects.requireNonNull(r0)
            boolean r4 = r0.isGroupExpanded()
            if (r4 != 0) goto L_0x007f
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = r0.mContainingNotification
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.mUserLocked
            if (r4 == 0) goto L_0x007c
            goto L_0x007f
        L_0x007c:
            int r4 = r0.mVisibleType
            goto L_0x0083
        L_0x007f:
            int r4 = r0.calculateVisibleType()
        L_0x0083:
            int r0 = r0.getBackgroundColor(r4)
            boolean r4 = r6.isGroupExpanded()
            if (r4 != 0) goto L_0x00a3
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = r6.mNotificationParent
            boolean r4 = r4.isGroupExpansionChanging()
            if (r4 != 0) goto L_0x009e
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = r6.mNotificationParent
            java.util.Objects.requireNonNull(r4)
            boolean r4 = r4.mUserLocked
            if (r4 == 0) goto L_0x00a1
        L_0x009e:
            if (r0 == 0) goto L_0x00a1
            goto L_0x00a3
        L_0x00a1:
            r0 = r3
            goto L_0x00a4
        L_0x00a3:
            r0 = r2
        L_0x00a4:
            r0 = r0 ^ r2
            r6.mShowNoBackground = r0
            goto L_0x00aa
        L_0x00a8:
            r6.mShowNoBackground = r3
        L_0x00aa:
            boolean r0 = r6.mCustomOutline
            if (r0 == 0) goto L_0x00af
            goto L_0x00ba
        L_0x00af:
            boolean r0 = r6.needsOutline()
            if (r0 == 0) goto L_0x00b7
            com.android.systemui.statusbar.notification.row.ExpandableOutlineView$1 r1 = r6.mProvider
        L_0x00b7:
            r6.setOutlineProvider(r1)
        L_0x00ba:
            com.android.systemui.statusbar.notification.row.NotificationBackgroundView r0 = r6.mBackgroundNormal
            boolean r6 = r6.hideBackground()
            if (r6 == 0) goto L_0x00c3
            r3 = 4
        L_0x00c3:
            r0.setVisibility(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.updateBackgroundForGroupState():void");
    }

    public final void updateBubbleButton() {
        NotificationContentView[] notificationContentViewArr;
        for (NotificationContentView notificationContentView : this.mLayouts) {
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationContentView);
            notificationContentView.applyBubbleAction(notificationContentView.mExpandedChild, notificationEntry);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateChildrenVisibility() {
        /*
            r5 = this;
            boolean r0 = r5.mExpandAnimationRunning
            r1 = 0
            if (r0 == 0) goto L_0x0012
            com.android.systemui.statusbar.notification.row.NotificationGuts r0 = r5.mGuts
            if (r0 == 0) goto L_0x0012
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mExposed
            if (r0 == 0) goto L_0x0012
            r0 = 1
            goto L_0x0013
        L_0x0012:
            r0 = r1
        L_0x0013:
            com.android.systemui.statusbar.notification.row.NotificationContentView r2 = r5.mPrivateLayout
            boolean r3 = r5.mShowingPublic
            r4 = 4
            if (r3 != 0) goto L_0x0022
            boolean r3 = r5.mIsSummaryWithChildren
            if (r3 != 0) goto L_0x0022
            if (r0 != 0) goto L_0x0022
            r3 = r1
            goto L_0x0023
        L_0x0022:
            r3 = r4
        L_0x0023:
            r2.setVisibility(r3)
            com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer r2 = r5.mChildrenContainer
            if (r2 == 0) goto L_0x0039
            boolean r3 = r5.mShowingPublic
            if (r3 != 0) goto L_0x0035
            boolean r3 = r5.mIsSummaryWithChildren
            if (r3 == 0) goto L_0x0035
            if (r0 != 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r1 = r4
        L_0x0036:
            r2.setVisibility(r1)
        L_0x0039:
            r5.updateLimits()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.updateChildrenVisibility():void");
    }

    public final void updateContentAccessibilityImportanceForGuts(boolean z) {
        int i;
        int i2;
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        if (notificationChildrenContainer != null) {
            if (z) {
                i2 = 0;
            } else {
                i2 = 4;
            }
            notificationChildrenContainer.setImportantForAccessibility(i2);
        }
        NotificationContentView[] notificationContentViewArr = this.mLayouts;
        if (notificationContentViewArr != null) {
            for (NotificationContentView notificationContentView : notificationContentViewArr) {
                if (z) {
                    i = 0;
                } else {
                    i = 4;
                }
                notificationContentView.setImportantForAccessibility(i);
            }
        }
        if (z) {
            requestAccessibilityFocus();
        }
    }

    public final void updateContentTransformation() {
        if (!this.mExpandAnimationRunning) {
            float contentTransformationShift = getContentTransformationShift() * (-this.mContentTransformationAmount);
            float interpolation = Interpolators.ALPHA_OUT.getInterpolation(Math.min((1.0f - this.mContentTransformationAmount) / 0.5f, 1.0f));
            if (this.mIsLastChild) {
                contentTransformationShift *= 0.4f;
            }
            this.mContentTranslation = contentTransformationShift;
            applyContentTransformation(interpolation, contentTransformationShift);
        }
    }

    public final void updateLimits() {
        NotificationContentView[] notificationContentViewArr;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        int i;
        int i2;
        for (NotificationContentView notificationContentView : this.mLayouts) {
            Objects.requireNonNull(notificationContentView);
            View view = notificationContentView.mContractedChild;
            boolean z5 = true;
            if (view == null || view.getId() == 16909536) {
                z = false;
            } else {
                z = true;
            }
            int i3 = this.mEntry.targetSdk;
            if (i3 < 24) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (i3 < 28) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (i3 < 31) {
                z4 = true;
            } else {
                z4 = false;
            }
            boolean z6 = view instanceof CallLayout;
            if (!z || !z4 || this.mIsSummaryWithChildren) {
                if (z6) {
                    i = this.mMaxExpandedHeight;
                } else if (!this.mUseIncreasedCollapsedHeight || notificationContentView != this.mPrivateLayout) {
                    i = this.mMaxSmallHeight;
                } else {
                    i = this.mMaxSmallHeightLarge;
                }
            } else if (z2) {
                i = this.mMaxSmallHeightBeforeN;
            } else if (z3) {
                i = this.mMaxSmallHeightBeforeP;
            } else {
                i = this.mMaxSmallHeightBeforeS;
            }
            View view2 = notificationContentView.mHeadsUpChild;
            if (view2 == null || view2.getId() == 16909536) {
                z5 = false;
            }
            if (!z5 || !z4) {
                if (!this.mUseIncreasedHeadsUpHeight || notificationContentView != this.mPrivateLayout) {
                    i2 = this.mMaxHeadsUpHeight;
                } else {
                    i2 = this.mMaxHeadsUpHeightIncreased;
                }
            } else if (z2) {
                i2 = this.mMaxHeadsUpHeightBeforeN;
            } else if (z3) {
                i2 = this.mMaxHeadsUpHeightBeforeP;
            } else {
                i2 = this.mMaxHeadsUpHeightBeforeS;
            }
            NotificationViewWrapper visibleWrapper = notificationContentView.getVisibleWrapper(2);
            if (visibleWrapper != null) {
                i2 = Math.max(i2, visibleWrapper.getMinLayoutHeight());
            }
            int i4 = this.mMaxExpandedHeight;
            notificationContentView.mSmallHeight = i;
            notificationContentView.mHeadsUpHeight = i2;
            notificationContentView.mNotificationMaxHeight = i4;
        }
    }

    public final void updateRippleAllowed() {
        boolean z;
        if (!this.mOnKeyguard) {
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            if (notificationEntry.mSbn.getNotification().contentIntent != null) {
                z = false;
                NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
                Objects.requireNonNull(notificationBackgroundView);
                notificationBackgroundView.mIsPressedAllowed = z;
            }
        }
        z = true;
        NotificationBackgroundView notificationBackgroundView2 = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView2);
        notificationBackgroundView2.mIsPressedAllowed = z;
    }

    @VisibleForTesting
    public void updateShelfIconColor() {
        boolean z;
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        StatusBarIconView statusBarIconView = iconPack.mShelfIcon;
        int i = 0;
        if (!Boolean.TRUE.equals(statusBarIconView.getTag(2131428107)) || R$array.isGrayscale(statusBarIconView, ContrastColorUtil.getInstance(((FrameLayout) this).mContext))) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i = getOriginalIconColor();
        }
        statusBarIconView.setStaticDrawableColor(i);
    }

    /* renamed from: $r8$lambda$r5fBwOTn-3_BH7IpjuK_7B090Dc  reason: not valid java name */
    public static void m96$r8$lambda$r5fBwOTn3_BH7IpjuK_7B090Dc(ExpandableNotificationRow expandableNotificationRow, CoordinateOnClickListener coordinateOnClickListener, View view) {
        NotificationMenuRowPlugin.MenuItem feedbackMenuItem;
        Objects.requireNonNull(expandableNotificationRow);
        expandableNotificationRow.createMenu();
        NotificationMenuRowPlugin notificationMenuRowPlugin = expandableNotificationRow.mMenuRow;
        if (notificationMenuRowPlugin != null && (feedbackMenuItem = notificationMenuRowPlugin.getFeedbackMenuItem(((FrameLayout) expandableNotificationRow).mContext)) != null) {
            ImageLoader$$ExternalSyntheticLambda0 imageLoader$$ExternalSyntheticLambda0 = (ImageLoader$$ExternalSyntheticLambda0) coordinateOnClickListener;
            Objects.requireNonNull(imageLoader$$ExternalSyntheticLambda0);
            ((NotificationGutsManager) imageLoader$$ExternalSyntheticLambda0.f$0).openGuts(expandableNotificationRow, view.getWidth() / 2, view.getHeight() / 2, feedbackMenuItem);
        }
    }

    public ExpandableNotificationRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mImageResolver = new NotificationInlineImageResolver(context, new NotificationInlineImageCache());
        initDimens();
    }

    public static Boolean isSystemNotification(Context context, StatusBarNotification statusBarNotification) {
        if (Settings.Secure.getIntForUser(context.getContentResolver(), "notification_permission_enabled", 0, 0) == 1) {
            try {
                return Boolean.valueOf(INotificationManager.Stub.asInterface(ServiceManager.getService("notification")).isPermissionFixed(statusBarNotification.getPackageName(), statusBarNotification.getUserId()));
            } catch (RemoteException unused) {
                Log.e("ExpandableNotifRow", "cannot reach NMS");
                return Boolean.FALSE;
            }
        } else {
            PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(context, statusBarNotification.getUser().getIdentifier());
            try {
                return Boolean.valueOf(Utils.isSystemPackage(context.getResources(), packageManagerForUser, packageManagerForUser.getPackageInfo(statusBarNotification.getPackageName(), 64)));
            } catch (PackageManager.NameNotFoundException unused2) {
                Log.e("ExpandableNotifRow", "cacheIsSystemNotification: Could not find package info");
                return null;
            }
        }
    }

    public final String appendTraceStyleTag(String str) {
        if (!Trace.isEnabled()) {
            return str;
        }
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        Class notificationStyle = notificationEntry.mSbn.getNotification().getNotificationStyle();
        if (notificationStyle == null) {
            return SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "(nostyle)");
        }
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, "(");
        m.append(notificationStyle.getSimpleName());
        m.append(")");
        return m.toString();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final void applyRoundness() {
        super.applyRoundness();
        applyChildrenRoundness();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        canvas.save();
        Path path = this.mExpandingClipPath;
        if (path != null && (this.mExpandAnimationRunning || this.mChildIsExpanding)) {
            canvas.clipPath(path);
        }
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public final void doSmartActionClick(int i, int i2) {
        NotificationMenuRowPlugin.MenuItem menuItem;
        createMenu();
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null) {
            menuItem = notificationMenuRowPlugin.getLongpressMenuItem(((FrameLayout) this).mContext);
        } else {
            menuItem = null;
        }
        if (menuItem.getGutsView() instanceof NotificationConversationInfo) {
            ((NotificationConversationInfo) menuItem.getGutsView()).setSelectedAction(2);
        }
        doLongClickCallback(i, i2, menuItem);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.Dumpable
    public final void dump(final FileDescriptor fileDescriptor, PrintWriter printWriter, final String[] strArr) {
        final IndentingPrintWriter asIndenting = R$id.asIndenting(printWriter);
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Notification: ");
        NotificationEntry notificationEntry = this.mEntry;
        Objects.requireNonNull(notificationEntry);
        m.append(notificationEntry.mKey);
        asIndenting.println(m.toString());
        R$id.withIncreasedIndent(asIndenting, new Runnable() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                ExpandableNotificationRow expandableNotificationRow = ExpandableNotificationRow.this;
                PrintWriter printWriter2 = asIndenting;
                FileDescriptor fileDescriptor2 = fileDescriptor;
                String[] strArr2 = strArr;
                ExpandableNotificationRow.AnonymousClass2 r3 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                printWriter2.print("visibility: " + expandableNotificationRow.getVisibility());
                printWriter2.print(", alpha: " + expandableNotificationRow.getAlpha());
                printWriter2.print(", translation: " + expandableNotificationRow.getTranslation());
                printWriter2.print(", removed: " + expandableNotificationRow.mRemoved);
                printWriter2.print(", expandAnimationRunning: " + expandableNotificationRow.mExpandAnimationRunning);
                NotificationContentView showingLayout = expandableNotificationRow.getShowingLayout();
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(", privateShowing: ");
                if (showingLayout == expandableNotificationRow.mPrivateLayout) {
                    z = true;
                } else {
                    z = false;
                }
                m2.append(z);
                printWriter2.print(m2.toString());
                printWriter2.println();
                Objects.requireNonNull(showingLayout);
                printWriter2.print("contentView visibility: " + showingLayout.getVisibility());
                printWriter2.print(", alpha: " + showingLayout.getAlpha());
                printWriter2.print(", clipBounds: " + showingLayout.getClipBounds());
                printWriter2.print(", contentHeight: " + showingLayout.mContentHeight);
                printWriter2.print(", visibleType: " + showingLayout.mVisibleType);
                View viewForVisibleType = showingLayout.getViewForVisibleType(showingLayout.mVisibleType);
                printWriter2.print(", visibleView ");
                if (viewForVisibleType != null) {
                    StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m(" visibility: ");
                    m3.append(viewForVisibleType.getVisibility());
                    printWriter2.print(m3.toString());
                    printWriter2.print(", alpha: " + viewForVisibleType.getAlpha());
                    printWriter2.print(", clipBounds: " + viewForVisibleType.getClipBounds());
                } else {
                    printWriter2.print("null");
                }
                printWriter2.println();
                ExpandableViewState expandableViewState = expandableNotificationRow.mViewState;
                if (expandableViewState != null) {
                    expandableViewState.dump(fileDescriptor2, printWriter2, strArr2);
                    printWriter2.println();
                } else {
                    printWriter2.println("no viewState!!!");
                }
                if (expandableNotificationRow.mIsSummaryWithChildren) {
                    printWriter2.println();
                    printWriter2.print("ChildrenContainer");
                    printWriter2.print(" visibility: " + expandableNotificationRow.mChildrenContainer.getVisibility());
                    printWriter2.print(", alpha: " + expandableNotificationRow.mChildrenContainer.getAlpha());
                    printWriter2.print(", translationY: " + expandableNotificationRow.mChildrenContainer.getTranslationY());
                    printWriter2.println();
                    ArrayList<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                    StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("Children: ");
                    m4.append(attachedChildren.size());
                    printWriter2.println(m4.toString());
                    printWriter2.print("{");
                    printWriter2.increaseIndent();
                    for (ExpandableNotificationRow expandableNotificationRow2 : attachedChildren) {
                        printWriter2.println();
                        expandableNotificationRow2.dump(fileDescriptor2, printWriter2, strArr2);
                    }
                    printWriter2.decreaseIndent();
                    printWriter2.println("}");
                    return;
                }
                NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
                if (notificationContentView != null) {
                    if (notificationContentView.mHeadsUpSmartReplyView != null) {
                        printWriter2.println("HeadsUp SmartReplyView:");
                        printWriter2.increaseIndent();
                        notificationContentView.mHeadsUpSmartReplyView.dump(printWriter2);
                        printWriter2.decreaseIndent();
                    }
                    if (notificationContentView.mExpandedSmartReplyView != null) {
                        printWriter2.println("Expanded SmartReplyView:");
                        printWriter2.increaseIndent();
                        notificationContentView.mExpandedSmartReplyView.dump(printWriter2);
                        printWriter2.decreaseIndent();
                    }
                }
            }
        });
    }

    public final int getHeadsUpHeight() {
        return getShowingLayout().getHeadsUpHeight(false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getHeadsUpHeightWithoutHeader() {
        if (!canShowHeadsUp() || !this.mIsHeadsUp) {
            return getCollapsedHeight();
        }
        if (!this.mIsSummaryWithChildren || shouldShowPublic()) {
            return getShowingLayout().getHeadsUpHeight(true);
        }
        NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
        Objects.requireNonNull(notificationChildrenContainer);
        return notificationChildrenContainer.getMinHeight(notificationChildrenContainer.getMaxAllowedVisibleChildren(true), false, 0);
    }

    public final NotificationContentView getShowingLayout() {
        if (shouldShowPublic()) {
            return this.mPublicLayout;
        }
        return this.mPrivateLayout;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final boolean hasOverlappingRendering() {
        if (!super.hasOverlappingRendering() || !childrenRequireOverlappingRendering()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isAboveShelf() {
        if (!canShowHeadsUp() || (!this.mIsPinned && !this.mHeadsupDisappearRunning && ((!this.mIsHeadsUp || !this.mAboveShelf) && !this.mExpandAnimationRunning && !this.mChildIsExpanding))) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isGroupExpansionChanging() {
        if (isChildInGroup()) {
            return this.mNotificationParent.isGroupExpansionChanging();
        }
        return this.mGroupExpansionChanging;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void notifyHeightChanged(boolean z) {
        boolean z2;
        super.notifyHeightChanged(z);
        NotificationContentView showingLayout = getShowingLayout();
        if (z || this.mUserLocked) {
            z2 = true;
        } else {
            z2 = false;
        }
        Objects.requireNonNull(showingLayout);
        showingLayout.selectLayout(z2, false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onConfigurationChanged();
        }
        NotificationInlineImageResolver notificationInlineImageResolver = this.mImageResolver;
        if (notificationInlineImageResolver != null) {
            notificationInlineImageResolver.mMaxImageWidth = notificationInlineImageResolver.getMaxImageWidth();
            notificationInlineImageResolver.mMaxImageHeight = notificationInlineImageResolver.getMaxImageHeight();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final void onDensityOrFontScaleChanged() {
        super.onDensityOrFontScaleChanged();
        initDimens();
        this.mBackgroundNormal.setCustomBackground$1();
        reInflateViews();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mPublicLayout = (NotificationContentView) findViewById(2131427948);
        NotificationContentView notificationContentView = (NotificationContentView) findViewById(2131427947);
        this.mPrivateLayout = notificationContentView;
        NotificationContentView[] notificationContentViewArr = {notificationContentView, this.mPublicLayout};
        this.mLayouts = notificationContentViewArr;
        for (int i = 0; i < 2; i++) {
            NotificationContentView notificationContentView2 = notificationContentViewArr[i];
            AnonymousClass1 r5 = this.mExpandClickListener;
            Objects.requireNonNull(notificationContentView2);
            notificationContentView2.mExpandClickListener = r5;
            notificationContentView2.mContainingNotification = this;
        }
        ViewStub viewStub = (ViewStub) findViewById(2131428511);
        this.mGutsStub = viewStub;
        viewStub.setOnInflateListener(new ViewStub.OnInflateListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda0
            @Override // android.view.ViewStub.OnInflateListener
            public final void onInflate(ViewStub viewStub2, View view) {
                ExpandableNotificationRow expandableNotificationRow = ExpandableNotificationRow.this;
                ExpandableNotificationRow.AnonymousClass2 r1 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                NotificationGuts notificationGuts = (NotificationGuts) view;
                expandableNotificationRow.mGuts = notificationGuts;
                int i2 = expandableNotificationRow.mClipTopAmount;
                Objects.requireNonNull(notificationGuts);
                notificationGuts.mClipTopAmount = i2;
                notificationGuts.invalidate();
                NotificationGuts notificationGuts2 = expandableNotificationRow.mGuts;
                int i3 = expandableNotificationRow.mActualHeight;
                Objects.requireNonNull(notificationGuts2);
                notificationGuts2.mActualHeight = i3;
                notificationGuts2.invalidate();
                expandableNotificationRow.mGutsStub = null;
            }
        });
        ViewStub viewStub2 = (ViewStub) findViewById(2131427692);
        this.mChildrenContainerStub = viewStub2;
        viewStub2.setOnInflateListener(new ViewStub.OnInflateListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRow$$ExternalSyntheticLambda1
            @Override // android.view.ViewStub.OnInflateListener
            public final void onInflate(ViewStub viewStub3, View view) {
                ExpandableNotificationRow expandableNotificationRow = ExpandableNotificationRow.this;
                ExpandableNotificationRow.AnonymousClass2 r1 = ExpandableNotificationRow.TRANSLATE_CONTENT;
                Objects.requireNonNull(expandableNotificationRow);
                NotificationChildrenContainer notificationChildrenContainer = (NotificationChildrenContainer) view;
                expandableNotificationRow.mChildrenContainer = notificationChildrenContainer;
                boolean z = expandableNotificationRow.mIsLowPriority;
                Objects.requireNonNull(notificationChildrenContainer);
                notificationChildrenContainer.mIsLowPriority = z;
                if (notificationChildrenContainer.mContainingNotification != null) {
                    notificationChildrenContainer.recreateLowPriorityHeader(null);
                    notificationChildrenContainer.updateHeaderVisibility(false);
                }
                boolean z2 = notificationChildrenContainer.mUserLocked;
                if (z2) {
                    notificationChildrenContainer.setUserLocked(z2);
                }
                NotificationChildrenContainer notificationChildrenContainer2 = expandableNotificationRow.mChildrenContainer;
                Objects.requireNonNull(notificationChildrenContainer2);
                notificationChildrenContainer2.mContainingNotification = expandableNotificationRow;
                notificationChildrenContainer2.mGroupingUtil = new NotificationGroupingUtil(expandableNotificationRow);
                expandableNotificationRow.mChildrenContainer.onNotificationUpdated();
                expandableNotificationRow.mTranslateableViews.add(expandableNotificationRow.mChildrenContainer);
            }
        });
        this.mTranslateableViews = new ArrayList<>();
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            this.mTranslateableViews.add(getChildAt(i2));
        }
        this.mTranslateableViews.remove(this.mChildrenContainerStub);
        this.mTranslateableViews.remove(this.mGutsStub);
    }

    public final void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
        if (canViewBeDismissed() && !this.mIsSnoozed) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        }
        boolean shouldShowPublic = shouldShowPublic();
        boolean z = false;
        if (!shouldShowPublic) {
            if (this.mIsSummaryWithChildren) {
                shouldShowPublic = true;
                if (!this.mIsLowPriority || isExpanded(false)) {
                    z = isGroupExpanded();
                }
            } else {
                NotificationContentView notificationContentView = this.mPrivateLayout;
                Objects.requireNonNull(notificationContentView);
                shouldShowPublic = notificationContentView.mIsContentExpandable;
                z = isExpanded(false);
            }
        }
        if (shouldShowPublic && !this.mIsSnoozed) {
            if (z) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
            } else {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            }
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (notificationMenuRowPlugin != null && notificationMenuRowPlugin.getSnoozeMenuItem(getContext()) != null) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427446, getContext().getResources().getString(2131952914)));
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0) {
            this.mFalsingManager.isFalseTap(2);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return super.onKeyDown(i, keyEvent);
        }
        keyEvent.startTracking();
        return true;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return false;
        }
        doLongClickCallback(getWidth() / 2, getHeight() / 2);
        return true;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public final boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!KeyEvent.isConfirmKey(i)) {
            return super.onKeyUp(i, keyEvent);
        }
        if (keyEvent.isCanceled()) {
            return true;
        }
        performClick();
        return true;
    }

    public final boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 0 || !isChildInGroup() || isGroupExpanded()) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    public final boolean performAccessibilityActionInternal(int i, Bundle bundle) {
        NotificationMenuRowPlugin notificationMenuRowPlugin;
        if (super.performAccessibilityActionInternal(i, bundle)) {
            return true;
        }
        if (i == 32) {
            doLongClickCallback(getWidth() / 2, getHeight() / 2);
            return true;
        } else if (i == 262144 || i == 524288) {
            this.mExpandClickListener.onClick(this);
            return true;
        } else if (i == 1048576) {
            performDismiss(true);
            return true;
        } else if (i != 2131427446 || (notificationMenuRowPlugin = this.mMenuRow) == null) {
            return false;
        } else {
            NotificationMenuRowPlugin.MenuItem snoozeMenuItem = notificationMenuRowPlugin.getSnoozeMenuItem(getContext());
            if (snoozeMenuItem != null) {
                doLongClickCallback(getWidth() / 2, getHeight() / 2, snoozeMenuItem);
            }
            return true;
        }
    }

    @Override // android.view.View
    public final boolean performClick() {
        updateRippleAllowed();
        return super.performClick();
    }

    public final void setAboveShelf(boolean z) {
        boolean isAboveShelf = isAboveShelf();
        this.mAboveShelf = z;
        if (isAboveShelf() != isAboveShelf) {
            ((AboveShelfObserver) this.mAboveShelfChangedListener).onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final void setBackgroundTintColor(int i) {
        super.setBackgroundTintColor(i);
        NotificationContentView showingLayout = getShowingLayout();
        if (showingLayout != null) {
            NotificationEntry notificationEntry = showingLayout.mNotificationEntry;
            Objects.requireNonNull(notificationEntry);
            boolean isColorized = notificationEntry.mSbn.getNotification().isColorized();
            SmartReplyView smartReplyView = showingLayout.mExpandedSmartReplyView;
            if (smartReplyView != null) {
                smartReplyView.setBackgroundTintColor(i, isColorized);
            }
            SmartReplyView smartReplyView2 = showingLayout.mHeadsUpSmartReplyView;
            if (smartReplyView2 != null) {
                smartReplyView2.setBackgroundTintColor(i, isColorized);
            }
            RemoteInputView remoteInputView = showingLayout.mExpandedRemoteInput;
            if (remoteInputView != null) {
                remoteInputView.setBackgroundTintColor(i, isColorized);
            }
            RemoteInputView remoteInputView2 = showingLayout.mHeadsUpRemoteInput;
            if (remoteInputView2 != null) {
                remoteInputView2.setBackgroundTintColor(i, isColorized);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setClipTopAmount(int i) {
        NotificationContentView[] notificationContentViewArr;
        super.setClipTopAmount(i);
        for (NotificationContentView notificationContentView : this.mLayouts) {
            Objects.requireNonNull(notificationContentView);
            notificationContentView.mClipTopAmount = i;
            notificationContentView.updateClipping();
        }
        NotificationGuts notificationGuts = this.mGuts;
        if (notificationGuts != null) {
            notificationGuts.mClipTopAmount = i;
            notificationGuts.invalidate();
        }
    }

    public final void setHeadsUpAnimatingAway(boolean z) {
        boolean z2;
        Consumer<Boolean> consumer;
        boolean isAboveShelf = isAboveShelf();
        if (z != this.mHeadsupDisappearRunning) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mHeadsupDisappearRunning = z;
        NotificationContentView notificationContentView = this.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        notificationContentView.mHeadsUpAnimatingAway = z;
        notificationContentView.selectLayout(false, true);
        if (z2 && (consumer = this.mHeadsUpAnimatingAwayListener) != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        if (isAboveShelf() != isAboveShelf) {
            ((AboveShelfObserver) this.mAboveShelfChangedListener).onAboveShelfStateChanged(!isAboveShelf);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setHideSensitive(boolean z, boolean z2, long j, long j2) {
        boolean z3;
        View[] viewArr;
        if (getVisibility() != 8) {
            boolean z4 = this.mShowingPublic;
            if (!this.mSensitive || !z) {
                z3 = false;
            } else {
                z3 = true;
            }
            this.mShowingPublic = z3;
            if ((!this.mShowingPublicInitialized || z3 != z4) && this.mPublicLayout.getChildCount() != 0) {
                int i = 4;
                if (!z2) {
                    this.mPublicLayout.animate().cancel();
                    this.mPrivateLayout.animate().cancel();
                    NotificationChildrenContainer notificationChildrenContainer = this.mChildrenContainer;
                    if (notificationChildrenContainer != null) {
                        notificationChildrenContainer.animate().cancel();
                    }
                    resetAllContentAlphas();
                    NotificationContentView notificationContentView = this.mPublicLayout;
                    if (this.mShowingPublic) {
                        i = 0;
                    }
                    notificationContentView.setVisibility(i);
                    updateChildrenVisibility();
                } else {
                    boolean z5 = this.mShowingPublic;
                    View[] viewArr2 = this.mIsSummaryWithChildren ? new View[]{this.mChildrenContainer} : new View[]{this.mPrivateLayout};
                    View[] viewArr3 = {this.mPublicLayout};
                    if (z5) {
                        viewArr = viewArr2;
                    } else {
                        viewArr = viewArr3;
                    }
                    if (z5) {
                        viewArr2 = viewArr3;
                    }
                    for (View view : viewArr) {
                        view.setVisibility(0);
                        view.animate().cancel();
                        view.animate().alpha(0.0f).setStartDelay(j).setDuration(j2).withEndAction(new ClockManager$$ExternalSyntheticLambda1(this, view, 4));
                    }
                    for (View view2 : viewArr2) {
                        view2.setVisibility(0);
                        view2.setAlpha(0.0f);
                        view2.animate().cancel();
                        view2.animate().alpha(1.0f).setStartDelay(j).setDuration(j2);
                    }
                }
                getShowingLayout().updateBackgroundColor(z2);
                NotificationContentView notificationContentView2 = this.mPrivateLayout;
                boolean isExpandable = isExpandable();
                Objects.requireNonNull(notificationContentView2);
                notificationContentView2.updateExpandButtonsDuringLayout(isExpandable, false);
                updateShelfIconColor();
                this.mShowingPublicInitialized = true;
            }
        }
    }

    public final void setLastAudiblyAlertedMs(long j) {
        boolean z;
        long currentTimeMillis = System.currentTimeMillis() - j;
        long j2 = RECENTLY_ALERTED_THRESHOLD_MS;
        if (currentTimeMillis < j2) {
            z = true;
        } else {
            z = false;
        }
        applyAudiblyAlertedRecently(z);
        removeCallbacks(this.mExpireRecentlyAlertedFlag);
        if (z) {
            postDelayed(this.mExpireRecentlyAlertedFlag, j2 - currentTimeMillis);
        }
    }

    @Override // android.view.View
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.mOnClickListener = onClickListener;
        updateClickAndFocus();
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public final void setTranslation(float f) {
        invalidate();
        if (this.mDismissUsingRowTranslationX) {
            setTranslationX(f);
        } else if (this.mTranslateableViews != null) {
            for (int i = 0; i < this.mTranslateableViews.size(); i++) {
                if (this.mTranslateableViews.get(i) != null) {
                    this.mTranslateableViews.get(i).setTranslationX(f);
                }
            }
            invalidateOutline();
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            IconPack iconPack = notificationEntry.mIcons;
            Objects.requireNonNull(iconPack);
            iconPack.mShelfIcon.setScrollX((int) (-f));
        }
        NotificationMenuRowPlugin notificationMenuRowPlugin = this.mMenuRow;
        if (!(notificationMenuRowPlugin == null || notificationMenuRowPlugin.getMenuView() == null)) {
            this.mMenuRow.onParentTranslationUpdate(f);
        }
    }

    public final void updateClickAndFocus() {
        boolean z;
        boolean z2 = false;
        if (!isChildInGroup() || isGroupExpanded()) {
            z = true;
        } else {
            z = false;
        }
        if (this.mOnClickListener != null && z) {
            z2 = true;
        }
        if (isFocusable() != z) {
            setFocusable(z);
        }
        if (isClickable() != z2) {
            setClickable(z2);
        }
    }

    public final void doLongClickCallback(int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        LongPressListener longPressListener = this.mLongPressListener;
        if (longPressListener != null && menuItem != null) {
            ExpandableNotificationRowController expandableNotificationRowController = (ExpandableNotificationRowController) ((ExpandableNotificationRowController$$ExternalSyntheticLambda0) longPressListener).f$0;
            Objects.requireNonNull(expandableNotificationRowController);
            ExpandableNotificationRow expandableNotificationRow = expandableNotificationRowController.mView;
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mIsSummaryWithChildren) {
                ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRowController.mView;
                Objects.requireNonNull(expandableNotificationRow2);
                expandableNotificationRow2.mExpandClickListener.onClick(expandableNotificationRow2);
                return;
            }
            expandableNotificationRowController.mNotificationGutsManager.openGuts(this, i, i2, menuItem);
        }
    }

    @VisibleForTesting
    public void setChildrenContainer(NotificationChildrenContainer notificationChildrenContainer) {
        this.mChildrenContainer = notificationChildrenContainer;
    }

    @VisibleForTesting
    public void setPrivateLayout(NotificationContentView notificationContentView) {
        this.mPrivateLayout = notificationContentView;
    }

    @VisibleForTesting
    public void setPublicLayout(NotificationContentView notificationContentView) {
        this.mPublicLayout = notificationContentView;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean areChildrenExpanded() {
        return this.mChildrenExpanded;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final float getHeaderVisibleAmount() {
        return this.mHeaderVisibleAmount;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean hasExpandingChild() {
        return this.mChildIsExpanding;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isExpandAnimationRunning() {
        return this.mExpandAnimationRunning;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isHeadsUpAnimatingAway() {
        return this.mHeadsupDisappearRunning;
    }

    @Override // com.android.systemui.statusbar.notification.NotificationFadeAware.FadeOptimizedNotification
    public final boolean isNotificationFaded() {
        return this.mIsFaded;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isPinned() {
        return this.mIsPinned;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public final boolean isRemoved() {
        return this.mRemoved;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean isSummaryWithChildren() {
        return this.mIsSummaryWithChildren;
    }
}
