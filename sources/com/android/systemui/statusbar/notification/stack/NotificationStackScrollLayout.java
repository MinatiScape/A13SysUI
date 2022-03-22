package com.android.systemui.statusbar.notification.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.OverScroller;
import android.widget.ScrollView;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import androidx.fragment.R$id;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.ExpandHelper;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.EmptyShadeView;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.ExpandAnimationParameters;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.FooterView;
import com.android.systemui.statusbar.notification.row.ForegroundServiceDungeonView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpTouchHelper;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ScrollAdapter;
import com.android.systemui.util.Assert;
import com.android.systemui.util.Utils;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.wm.shell.transition.DefaultTransitionHandler$$ExternalSyntheticLambda1;
import com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda1;
import com.google.android.systemui.gamedashboard.GameMenuActivity$$ExternalSyntheticLambda2;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NotificationStackScrollLayout extends ViewGroup implements Dumpable {
    public static final boolean SPEW = Log.isLoggable("StackScroller", 2);
    public boolean mActivateNeedsAnimation;
    public final AmbientState mAmbientState;
    public boolean mAnimateBottomOnLayout;
    public boolean mAnimateNextBackgroundBottom;
    public boolean mAnimateNextBackgroundTop;
    public boolean mAnimateNextSectionBoundsChange;
    public boolean mAnimateNextTopPaddingChange;
    public boolean mAnimationRunning;
    public boolean mAnimationsEnabled;
    public boolean mBackwardScrollable;
    public int mBgColor;
    public int mBottomPadding;
    public int mCachedBackgroundColor;
    public boolean mChangePositionInProgress;
    public boolean mCheckForLeavebehind;
    public boolean mChildTransferInProgress;
    public boolean mChildrenUpdateRequested;
    public ClearAllAnimationListener mClearAllAnimationListener;
    public boolean mClearAllEnabled;
    public boolean mClearAllInProgress;
    public ClearAllListener mClearAllListener;
    public int mContentHeight;
    public boolean mContinuousBackgroundUpdate;
    public boolean mContinuousShadowUpdate;
    public NotificationStackScrollLayoutController mController;
    public int mCornerRadius;
    public final boolean mDebugLines;
    public Paint mDebugPaint;
    public final boolean mDebugRemoveAnimation;
    public HashSet mDebugTextUsedYPositions;
    public float mDimAmount;
    public ValueAnimator mDimAnimator;
    public boolean mDimmedNeedsAnimation;
    public boolean mDisallowDismissInThisMotion;
    public boolean mDisallowScrollingInThisMotion;
    public boolean mDontClampNextScroll;
    public boolean mDontReportNextOverScroll;
    public int mDownX;
    public EmptyShadeView mEmptyShadeView;
    public boolean mEverythingNeedsAnimation;
    public ExpandHelper mExpandHelper;
    public ExpandableNotificationRow mExpandedGroupView;
    public float mExpandedHeight;
    public boolean mExpandedInThisMotion;
    public boolean mExpandingNotification;
    public ExpandableNotificationRow mExpandingNotificationRow;
    public float mExtraTopInsetForFullShadeTransition;
    public ForegroundServiceDungeonView mFgsSectionView;
    public Runnable mFinishScrollingCallback;
    public boolean mFlingAfterUpEvent;
    public FooterClearAllListener mFooterClearAllListener;
    public FooterView mFooterView;
    public boolean mForceNoOverlappingRendering;
    public View mForcedScroll;
    public boolean mForwardScrollable;
    public int mGapHeight;
    public boolean mGenerateChildOrderChangedEvent;
    public long mGoToFullShadeDelay;
    public boolean mGoToFullShadeNeedsAnimation;
    public GroupExpansionManager mGroupExpansionManager;
    public GroupMembershipManager mGroupMembershipManager;
    public boolean mHeadsUpAnimatingAway;
    public HeadsUpAppearanceController mHeadsUpAppearanceController;
    public int mHeadsUpInset;
    public boolean mHideSensitiveNeedsAnimation;
    public boolean mHighPriorityBeforeSpeedBump;
    public boolean mInHeadsUpPinnedMode;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public int mIntrinsicContentHeight;
    public int mIntrinsicPadding;
    public boolean mIsBeingDragged;
    public boolean mIsClipped;
    public boolean mIsCurrentUserSetup;
    public boolean mIsExpansionChanging;
    public boolean mIsFlinging;
    public boolean mIsRemoteInputActive;
    public boolean mKeyguardBypassEnabled;
    public int mLastMotionY;
    public float mLastSentAppear;
    public float mLastSentExpandedHeight;
    public ExpandAnimationParameters mLaunchAnimationParams;
    public boolean mLaunchingNotification;
    public boolean mLaunchingNotificationNeedsToBeClipped;
    public NotificationLogger.OnChildLocationsChangedListener mListener;
    public NotificationStackScrollLogger mLogger;
    public View.OnClickListener mManageButtonClickListener;
    public int mMaxLayoutHeight;
    public float mMaxOverScroll;
    public int mMaxScrollAfterExpand;
    public int mMaxTopPadding;
    public int mMaximumVelocity;
    public int mMinInteractionHeight;
    public float mMinTopOverScrollToEscape;
    public int mMinimumPaddings;
    public int mMinimumVelocity;
    public boolean mNeedViewResizeAnimation;
    public boolean mNeedsAnimation;
    public long mNumHeadsUp;
    public OnEmptySpaceClickListener mOnEmptySpaceClickListener;
    public ExpandableView.OnHeightChangedListener mOnHeightChangedListener;
    public Consumer<Boolean> mOnStackYChanged;
    public boolean mOnlyScrollingInThisMotion;
    public float mOverScrolledBottomPixels;
    public float mOverScrolledTopPixels;
    public int mOverflingDistance;
    public OnOverscrollTopChangedListener mOverscrollTopChangedListener;
    public int mOwnScrollY;
    public int mPaddingBetweenElements;
    public boolean mPanelTracking;
    public boolean mPulsing;
    public ViewGroup mQsContainer;
    public boolean mQsExpanded;
    public float mQsExpansionFraction;
    public int mQsScrollBoundaryPosition;
    public int mQsTilePadding;
    public Rect mRequestedClipBounds;
    public int mRoundedRectClippingBottom;
    public int mRoundedRectClippingLeft;
    public int mRoundedRectClippingRight;
    public int mRoundedRectClippingTop;
    public Consumer<Integer> mScrollListener;
    public boolean mScrollable;
    public boolean mScrolledToTopOnFirstDown;
    public OverScroller mScroller;
    public boolean mScrollingEnabled;
    public NotificationSection[] mSections;
    public final NotificationSectionsManager mSectionsManager;
    public ShadeController mShadeController;
    public NotificationShelf mShelf;
    public final boolean mShouldDrawNotificationBackground;
    public boolean mShouldShowShelfOnly;
    public boolean mShouldUseSplitNotificationShade;
    public int mSidePaddings;
    public boolean mSkinnyNotifsInLandscape;
    public float mSlopMultiplier;
    public final int mSplitShadeMinContentHeight;
    public final StackScrollAlgorithm mStackScrollAlgorithm;
    public float mStackTranslation;
    public StatusBar mStatusBar;
    @VisibleForTesting
    public int mStatusBarHeight;
    public int mStatusBarState;
    public NotificationSwipeHelper mSwipeHelper;
    public NotificationEntry mTopHeadsUpEntry;
    public int mTopPadding;
    public boolean mTopPaddingNeedsAnimation;
    public float mTopPaddingOverflow;
    public NotificationStackScrollLayoutController.TouchHandler mTouchHandler;
    public boolean mTouchIsClick;
    public int mTouchSlop;
    public VelocityTracker mVelocityTracker;
    public int mWaterfallTopInset;
    public boolean mWillExpand;
    public boolean mShadeNeedsToClose = false;
    public int mCurrentStackHeight = Integer.MAX_VALUE;
    public final Paint mBackgroundPaint = new Paint();
    public int mActivePointerId = -1;
    public int mBottomInset = 0;
    public HashSet<ExpandableView> mChildrenToAddAnimated = new HashSet<>();
    public ArrayList<View> mAddedHeadsUpChildren = new ArrayList<>();
    public ArrayList<ExpandableView> mChildrenToRemoveAnimated = new ArrayList<>();
    public ArrayList<ExpandableView> mChildrenChangingPositions = new ArrayList<>();
    public HashSet<View> mFromMoreCardAdditions = new HashSet<>();
    public ArrayList<AnimationEvent> mAnimationEvents = new ArrayList<>();
    public ArrayList<View> mSwipedOutViews = new ArrayList<>();
    public final StackStateAnimator mStateAnimator = new StackStateAnimator(this);
    public int mSpeedBumpIndex = -1;
    public boolean mSpeedBumpIndexDirty = true;
    public boolean mIsExpanded = true;
    public AnonymousClass1 mChildrenUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.1
        /* JADX WARN: Code restructure failed: missing block: B:152:0x02b6, code lost:
            if (r4 == false) goto L_0x02ba;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:501:0x0974  */
        /* JADX WARN: Removed duplicated region for block: B:582:0x09a4 A[SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r12v32, types: [boolean] */
        /* JADX WARN: Type inference failed for: r12v33 */
        /* JADX WARN: Type inference failed for: r12v34 */
        /* JADX WARN: Type inference failed for: r7v12, types: [android.widget.TextView] */
        /* JADX WARN: Type inference failed for: r7v8, types: [android.widget.TextView, android.view.View] */
        /* JADX WARN: Unknown variable types count: 2 */
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean onPreDraw() {
            /*
                Method dump skipped, instructions count: 2516
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.AnonymousClass1.onPreDraw():boolean");
        }
    };
    public int[] mTempInt2 = new int[2];
    public HashSet<Runnable> mAnimationFinishedRunnables = new HashSet<>();
    public HashSet<ExpandableView> mClearTransientViewsWhenFinished = new HashSet<>();
    public HashSet<Pair<ExpandableNotificationRow, Boolean>> mHeadsUpChangeAnimations = new HashSet<>();
    public final ArrayList<Pair<ExpandableNotificationRow, Boolean>> mTmpList = new ArrayList<>();
    public AnonymousClass2 mRunningAnimationUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.2
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            boolean z = NotificationStackScrollLayout.SPEW;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mShelf.updateAppearance();
            if (notificationStackScrollLayout.mNeedsAnimation || notificationStackScrollLayout.mChildrenUpdateRequested) {
                return true;
            }
            notificationStackScrollLayout.updateBackground();
            return true;
        }
    };
    public ArrayList<ExpandableView> mTmpSortedChildren = new ArrayList<>();
    public final AnonymousClass3 mDimEndListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            NotificationStackScrollLayout.this.mDimAnimator = null;
        }
    };
    public AnonymousClass4 mDimUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.4
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            boolean z = NotificationStackScrollLayout.SPEW;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mDimAmount = floatValue;
            notificationStackScrollLayout.updateBackgroundDimming();
        }
    };
    public NotificationStackScrollLayout$$ExternalSyntheticLambda0 mShadowUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda0
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            boolean z = NotificationStackScrollLayout.SPEW;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.updateViewShadows();
            return true;
        }
    };
    public NotificationStackScrollLayout$$ExternalSyntheticLambda1 mBackgroundUpdater = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda1
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            boolean z = NotificationStackScrollLayout.SPEW;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.updateBackground();
            return true;
        }
    };
    public NotificationStackScrollLayout$$ExternalSyntheticLambda3 mViewPositionComparator = NotificationStackScrollLayout$$ExternalSyntheticLambda3.INSTANCE;
    public final AnonymousClass5 mOutlineProvider = new ViewOutlineProvider() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.5
        @Override // android.view.ViewOutlineProvider
        public final void getOutline(View view, Outline outline) {
            if (NotificationStackScrollLayout.this.mAmbientState.isHiddenAtAll()) {
                NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
                float interpolation = notificationStackScrollLayout.mHideXInterpolator.getInterpolation((1.0f - notificationStackScrollLayout.mLinearHideAmount) * notificationStackScrollLayout.mBackgroundXFactor);
                NotificationStackScrollLayout notificationStackScrollLayout2 = NotificationStackScrollLayout.this;
                Rect rect = notificationStackScrollLayout2.mBackgroundAnimationRect;
                float f = notificationStackScrollLayout2.mCornerRadius;
                outline.setRoundRect(rect, MathUtils.lerp(f / 2.0f, f, interpolation));
                AmbientState ambientState = NotificationStackScrollLayout.this.mAmbientState;
                Objects.requireNonNull(ambientState);
                outline.setAlpha(1.0f - ambientState.mHideAmount);
                return;
            }
            ViewOutlineProvider.BACKGROUND.getOutline(view, outline);
        }
    };
    public float mInterpolatedHideAmount = 0.0f;
    public float mLinearHideAmount = 0.0f;
    public float mBackgroundXFactor = 1.0f;
    public int mMaxDisplayedNotifications = -1;
    public float mKeyguardBottomPadding = -1.0f;
    public final Rect mClipRect = new Rect();
    public boolean mHeadsUpGoingAwayAnimationsAllowed = true;
    public WMShell$7$$ExternalSyntheticLambda2 mReflingAndAnimateScroll = new WMShell$7$$ExternalSyntheticLambda2(this, 6);
    public final Rect mBackgroundAnimationRect = new Rect();
    public ArrayList<BiConsumer<Float, Float>> mExpandedHeightListeners = new ArrayList<>();
    public final Rect mTmpRect = new Rect();
    public PathInterpolator mHideXInterpolator = Interpolators.FAST_OUT_SLOW_IN;
    public final Path mRoundedClipPath = new Path();
    public final Path mLaunchedNotificationClipPath = new Path();
    public boolean mShouldUseRoundedRectClipping = false;
    public float[] mBgCornerRadii = new float[8];
    public boolean mAnimateStackYForContentHeightChange = false;
    public float[] mLaunchedNotificationRadii = new float[8];
    public boolean mDismissUsingRowTranslationX = true;
    public final AnonymousClass6 mOnChildHeightChangedListener = new ExpandableView.OnHeightChangedListener() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.6
        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public final void onHeightChanged(ExpandableView expandableView, boolean z) {
            NotificationStackScrollLayout.this.onChildHeightChanged(expandableView, z);
        }

        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public final void onReset(ExpandableNotificationRow expandableNotificationRow) {
            boolean z;
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            Objects.requireNonNull(notificationStackScrollLayout);
            if ((notificationStackScrollLayout.mAnimationsEnabled || notificationStackScrollLayout.mPulsing) && (notificationStackScrollLayout.mIsExpanded || NotificationStackScrollLayout.isPinnedHeadsUp(expandableNotificationRow))) {
                z = true;
            } else {
                z = false;
            }
            boolean z2 = expandableNotificationRow instanceof ExpandableNotificationRow;
            if (z2) {
                expandableNotificationRow.setIconAnimationRunning(z);
            }
            if (z2) {
                expandableNotificationRow.setChronometerRunning(notificationStackScrollLayout.mIsExpanded);
            }
        }
    };
    public final AnonymousClass7 mScrollAdapter = new AnonymousClass7();
    public AnonymousClass8 mReclamp = new Runnable() { // from class: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.8
        @Override // java.lang.Runnable
        public final void run() {
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            boolean z = NotificationStackScrollLayout.SPEW;
            int scrollRange = notificationStackScrollLayout.getScrollRange();
            NotificationStackScrollLayout notificationStackScrollLayout2 = NotificationStackScrollLayout.this;
            OverScroller overScroller = notificationStackScrollLayout2.mScroller;
            int i = ((ViewGroup) notificationStackScrollLayout2).mScrollX;
            int i2 = NotificationStackScrollLayout.this.mOwnScrollY;
            overScroller.startScroll(i, i2, 0, scrollRange - i2);
            NotificationStackScrollLayout notificationStackScrollLayout3 = NotificationStackScrollLayout.this;
            notificationStackScrollLayout3.mDontReportNextOverScroll = true;
            notificationStackScrollLayout3.mDontClampNextScroll = true;
            notificationStackScrollLayout3.animateScroll();
        }
    };
    public final AnonymousClass9 mHeadsUpCallback = new AnonymousClass9();
    public AnonymousClass11 mExpandHelperCallback = new AnonymousClass11();
    public final ScreenOffAnimationController mScreenOffAnimationController = (ScreenOffAnimationController) Dependency.get(ScreenOffAnimationController.class);

    /* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$11  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass11 implements ExpandHelper.Callback {
        public AnonymousClass11() {
        }

        public final boolean canChildBeExpanded(View view) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (expandableNotificationRow.isExpandable() && !expandableNotificationRow.areGutsExposed()) {
                    if (!NotificationStackScrollLayout.this.mIsExpanded) {
                        Objects.requireNonNull(expandableNotificationRow);
                        if (!expandableNotificationRow.mIsPinned) {
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        public final void expansionStateChanged(boolean z) {
            NotificationStackScrollLayout notificationStackScrollLayout = NotificationStackScrollLayout.this;
            notificationStackScrollLayout.mExpandingNotification = z;
            if (!notificationStackScrollLayout.mExpandedInThisMotion) {
                notificationStackScrollLayout.mMaxScrollAfterExpand = notificationStackScrollLayout.mOwnScrollY;
                notificationStackScrollLayout.mExpandedInThisMotion = true;
            }
        }

        public final void setUserExpandedChild(View view, boolean z) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (!z || !NotificationStackScrollLayout.this.onKeyguard()) {
                    expandableNotificationRow.setUserExpanded(z, true);
                    expandableNotificationRow.onExpandedByGesture(z);
                    return;
                }
                expandableNotificationRow.setUserLocked(false);
                NotificationStackScrollLayout.this.updateContentHeight();
                NotificationStackScrollLayout.this.notifyHeightChangeListener(expandableNotificationRow);
            }
        }

        public final void setUserLockedChild(View view, boolean z) {
            if (view instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) view).setUserLocked(z);
            }
            NotificationStackScrollLayout.this.cancelLongPress();
            NotificationStackScrollLayout.this.requestDisallowInterceptTouchEvent(true);
        }
    }

    /* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements ScrollAdapter {
        public AnonymousClass7() {
        }
    }

    /* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$9  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass9 implements HeadsUpTouchHelper.Callback {
        public AnonymousClass9() {
        }
    }

    /* loaded from: classes.dex */
    public static class AnimationEvent {
        public static AnimationFilter[] FILTERS;
        public static int[] LENGTHS = {464, 464, 360, 360, 220, 220, 360, 448, 360, 360, 360, 400, 400, 400, 360, 360};
        public final int animationType;
        public final AnimationFilter filter;
        public boolean headsUpFromBottom;
        public final long length;
        public final ExpandableView mChangingView;
        public View viewAfterChangingView;

        public AnimationEvent(ExpandableView expandableView, int i) {
            this(expandableView, i, LENGTHS[i]);
        }

        static {
            AnimationFilter animationFilter = new AnimationFilter();
            animationFilter.animateAlpha = true;
            animationFilter.animateHeight = true;
            animationFilter.animateTopInset = true;
            animationFilter.animateY = true;
            animationFilter.animateZ = true;
            animationFilter.hasDelays = true;
            AnimationFilter animationFilter2 = new AnimationFilter();
            animationFilter2.animateAlpha = true;
            animationFilter2.animateHeight = true;
            animationFilter2.animateTopInset = true;
            animationFilter2.animateY = true;
            animationFilter2.animateZ = true;
            animationFilter2.hasDelays = true;
            AnimationFilter animationFilter3 = new AnimationFilter();
            animationFilter3.animateHeight = true;
            animationFilter3.animateTopInset = true;
            animationFilter3.animateY = true;
            animationFilter3.animateZ = true;
            animationFilter3.hasDelays = true;
            AnimationFilter animationFilter4 = new AnimationFilter();
            animationFilter4.animateHeight = true;
            animationFilter4.animateTopInset = true;
            animationFilter4.animateY = true;
            animationFilter4.animateDimmed = true;
            animationFilter4.animateZ = true;
            AnimationFilter animationFilter5 = new AnimationFilter();
            animationFilter5.animateZ = true;
            AnimationFilter animationFilter6 = new AnimationFilter();
            animationFilter6.animateDimmed = true;
            AnimationFilter animationFilter7 = new AnimationFilter();
            animationFilter7.animateAlpha = true;
            animationFilter7.animateHeight = true;
            animationFilter7.animateTopInset = true;
            animationFilter7.animateY = true;
            animationFilter7.animateZ = true;
            AnimationFilter animationFilter8 = new AnimationFilter();
            animationFilter8.animateHeight = true;
            animationFilter8.animateTopInset = true;
            animationFilter8.animateY = true;
            animationFilter8.animateDimmed = true;
            animationFilter8.animateZ = true;
            animationFilter8.hasDelays = true;
            AnimationFilter animationFilter9 = new AnimationFilter();
            animationFilter9.animateHideSensitive = true;
            AnimationFilter animationFilter10 = new AnimationFilter();
            animationFilter10.animateHeight = true;
            animationFilter10.animateTopInset = true;
            animationFilter10.animateY = true;
            animationFilter10.animateZ = true;
            AnimationFilter animationFilter11 = new AnimationFilter();
            animationFilter11.animateAlpha = true;
            animationFilter11.animateHeight = true;
            animationFilter11.animateTopInset = true;
            animationFilter11.animateY = true;
            animationFilter11.animateZ = true;
            AnimationFilter animationFilter12 = new AnimationFilter();
            animationFilter12.animateHeight = true;
            animationFilter12.animateTopInset = true;
            animationFilter12.animateY = true;
            animationFilter12.animateZ = true;
            AnimationFilter animationFilter13 = new AnimationFilter();
            animationFilter13.animateHeight = true;
            animationFilter13.animateTopInset = true;
            animationFilter13.animateY = true;
            animationFilter13.animateZ = true;
            animationFilter13.hasDelays = true;
            AnimationFilter animationFilter14 = new AnimationFilter();
            animationFilter14.animateHeight = true;
            animationFilter14.animateTopInset = true;
            animationFilter14.animateY = true;
            animationFilter14.animateZ = true;
            animationFilter14.hasDelays = true;
            AnimationFilter animationFilter15 = new AnimationFilter();
            animationFilter15.animateHeight = true;
            animationFilter15.animateTopInset = true;
            animationFilter15.animateY = true;
            animationFilter15.animateZ = true;
            AnimationFilter animationFilter16 = new AnimationFilter();
            animationFilter16.animateAlpha = true;
            animationFilter16.animateDimmed = true;
            animationFilter16.animateHideSensitive = true;
            animationFilter16.animateHeight = true;
            animationFilter16.animateTopInset = true;
            animationFilter16.animateY = true;
            animationFilter16.animateZ = true;
            FILTERS = new AnimationFilter[]{animationFilter, animationFilter2, animationFilter3, animationFilter4, animationFilter5, animationFilter6, animationFilter7, animationFilter8, animationFilter9, animationFilter10, animationFilter11, animationFilter12, animationFilter13, animationFilter14, animationFilter15, animationFilter16};
        }

        public AnimationEvent(ExpandableView expandableView, int i, long j) {
            AnimationFilter animationFilter = FILTERS[i];
            AnimationUtils.currentAnimationTimeMillis();
            this.mChangingView = expandableView;
            this.animationType = i;
            this.length = j;
            this.filter = animationFilter;
        }
    }

    /* loaded from: classes.dex */
    public interface ClearAllAnimationListener {
    }

    /* loaded from: classes.dex */
    public interface ClearAllListener {
    }

    /* loaded from: classes.dex */
    public interface FooterClearAllListener {
    }

    /* loaded from: classes.dex */
    public interface OnEmptySpaceClickListener {
    }

    /* loaded from: classes.dex */
    public interface OnOverscrollTopChangedListener {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Type inference failed for: r12v15, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$6] */
    /* JADX WARN: Type inference failed for: r12v17, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$8] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$1] */
    /* JADX WARN: Type inference failed for: r2v10, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$4] */
    /* JADX WARN: Type inference failed for: r2v11, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r2v12, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda1] */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$5] */
    /* JADX WARN: Type inference failed for: r2v7, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$2] */
    /* JADX WARN: Type inference failed for: r2v9, types: [com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$3] */
    public NotificationStackScrollLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0, 0);
        boolean z = false;
        Resources resources = getResources();
        FeatureFlags featureFlags = (FeatureFlags) Dependency.get(FeatureFlags.class);
        this.mDebugLines = featureFlags.isEnabled(Flags.NSSL_DEBUG_LINES);
        this.mDebugRemoveAnimation = featureFlags.isEnabled(Flags.NSSL_DEBUG_REMOVE_ANIMATION);
        NotificationSectionsManager notificationSectionsManager = (NotificationSectionsManager) Dependency.get(NotificationSectionsManager.class);
        this.mSectionsManager = notificationSectionsManager;
        boolean shouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(getResources());
        if (shouldUseSplitNotificationShade != this.mShouldUseSplitNotificationShade) {
            this.mShouldUseSplitNotificationShade = shouldUseSplitNotificationShade;
            updateDismissBehavior();
            updateUseRoundedRectClipping();
        }
        Objects.requireNonNull(notificationSectionsManager);
        if (!notificationSectionsManager.initialized) {
            notificationSectionsManager.initialized = true;
            notificationSectionsManager.parent = this;
            notificationSectionsManager.reinflateViews();
            notificationSectionsManager.configurationController.addCallback(notificationSectionsManager.configurationListener);
            int[] notificationBuckets = notificationSectionsManager.sectionsFeatureManager.getNotificationBuckets();
            ArrayList arrayList = new ArrayList(notificationBuckets.length);
            int length = notificationBuckets.length;
            int i = 0;
            while (i < length) {
                int i2 = notificationBuckets[i];
                i++;
                NotificationStackScrollLayout notificationStackScrollLayout = notificationSectionsManager.parent;
                if (notificationStackScrollLayout == null) {
                    notificationStackScrollLayout = null;
                }
                arrayList.add(new NotificationSection(notificationStackScrollLayout, i2));
            }
            Object[] array = arrayList.toArray(new NotificationSection[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            this.mSections = (NotificationSection[]) array;
            this.mAmbientState = (AmbientState) Dependency.get(AmbientState.class);
            this.mBgColor = com.android.settingslib.Utils.getColorAttr(((ViewGroup) this).mContext, 16844002).getDefaultColor();
            int dimensionPixelSize = resources.getDimensionPixelSize(2131166657);
            resources.getDimensionPixelSize(2131166652);
            this.mSplitShadeMinContentHeight = resources.getDimensionPixelSize(2131166684);
            ExpandHelper expandHelper = new ExpandHelper(getContext(), this.mExpandHelperCallback, dimensionPixelSize);
            this.mExpandHelper = expandHelper;
            expandHelper.mEventSource = this;
            AnonymousClass7 r2 = this.mScrollAdapter;
            Objects.requireNonNull(expandHelper);
            expandHelper.mScrollAdapter = r2;
            this.mStackScrollAlgorithm = new StackScrollAlgorithm(context, this);
            boolean z2 = resources.getBoolean(2131034124);
            this.mShouldDrawNotificationBackground = z2;
            setOutlineProvider(this.mOutlineProvider);
            setWillNotDraw(!((z2 || this.mDebugLines) ? true : z));
            this.mBackgroundPaint.setAntiAlias(true);
            if (this.mDebugLines) {
                Paint paint = new Paint();
                this.mDebugPaint = paint;
                paint.setColor(-65536);
                this.mDebugPaint.setStrokeWidth(2.0f);
                this.mDebugPaint.setStyle(Paint.Style.STROKE);
                this.mDebugPaint.setTextSize(25.0f);
            }
            this.mClearAllEnabled = resources.getBoolean(2131034132);
            this.mGroupMembershipManager = (GroupMembershipManager) Dependency.get(GroupMembershipManager.class);
            this.mGroupExpansionManager = (GroupExpansionManager) Dependency.get(GroupExpansionManager.class);
            setImportantForAccessibility(1);
            return;
        }
        throw new IllegalStateException("NotificationSectionsManager already initialized".toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0017  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0069  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void customOverScrollBy(int r3, int r4, int r5, int r6) {
        /*
            r2 = this;
            int r4 = r4 + r3
            int r3 = -r6
            int r6 = r6 + r5
            r5 = 0
            r0 = 1
            if (r4 <= r6) goto L_0x000a
            r4 = r6
        L_0x0008:
            r3 = r0
            goto L_0x000f
        L_0x000a:
            if (r4 >= r3) goto L_0x000e
            r4 = r3
            goto L_0x0008
        L_0x000e:
            r3 = r5
        L_0x000f:
            android.widget.OverScroller r6 = r2.mScroller
            boolean r6 = r6.isFinished()
            if (r6 != 0) goto L_0x0069
            r2.setOwnScrollY(r4, r5)
            if (r3 == 0) goto L_0x004f
            int r3 = r2.getScrollRange()
            int r4 = r2.mOwnScrollY
            if (r4 > 0) goto L_0x0026
            r6 = r0
            goto L_0x0027
        L_0x0026:
            r6 = r5
        L_0x0027:
            if (r4 < r3) goto L_0x002b
            r1 = r0
            goto L_0x002c
        L_0x002b:
            r1 = r5
        L_0x002c:
            if (r6 != 0) goto L_0x0030
            if (r1 == 0) goto L_0x006c
        L_0x0030:
            if (r6 == 0) goto L_0x003b
            int r3 = -r4
            float r3 = (float) r3
            r2.setOwnScrollY(r5, r5)
            r2.mDontReportNextOverScroll = r0
            r4 = r0
            goto L_0x0042
        L_0x003b:
            int r4 = r4 - r3
            float r4 = (float) r4
            r2.setOwnScrollY(r3, r5)
            r3 = r4
            r4 = r5
        L_0x0042:
            r2.setOverScrollAmount(r3, r4, r5, r0)
            r3 = 0
            r2.setOverScrollAmount(r3, r4, r0, r0)
            android.widget.OverScroller r2 = r2.mScroller
            r2.forceFinished(r0)
            goto L_0x006c
        L_0x004f:
            float r3 = r2.getCurrentOverScrollAmount(r0)
            int r4 = r2.mOwnScrollY
            if (r4 >= 0) goto L_0x0061
            int r3 = -r4
            float r3 = (float) r3
            boolean r4 = r2.isRubberbanded(r0)
            r2.notifyOverscrollTopListener(r3, r4)
            goto L_0x006c
        L_0x0061:
            boolean r4 = r2.isRubberbanded(r0)
            r2.notifyOverscrollTopListener(r3, r4)
            goto L_0x006c
        L_0x0069:
            r2.setOwnScrollY(r4, r5)
        L_0x006c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.customOverScrollBy(int, int, int, int):void");
    }

    public final void endDrag() {
        setIsBeingDragged(false);
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        if (getCurrentOverScrollAmount(true) > 0.0f) {
            setOverScrollAmount(0.0f, true, true, true);
        }
        if (getCurrentOverScrollAmount(false) > 0.0f) {
            setOverScrollAmount(0.0f, false, true, true);
        }
    }

    public final void notifyHeightChangeListener(ExpandableView expandableView) {
        notifyHeightChangeListener(expandableView, false);
    }

    public final boolean scrollTo(View view) {
        ExpandableView expandableView = (ExpandableView) view;
        int positionInLinearLayout = getPositionInLinearLayout(view);
        int targetScrollForView = targetScrollForView(expandableView, positionInLinearLayout);
        int intrinsicHeight = expandableView.getIntrinsicHeight() + positionInLinearLayout;
        int i = this.mOwnScrollY;
        if (i >= targetScrollForView && intrinsicHeight >= i) {
            return false;
        }
        this.mScroller.startScroll(((ViewGroup) this).mScrollX, i, 0, targetScrollForView - i);
        this.mDontReportNextOverScroll = true;
        animateScroll();
        return true;
    }

    public final void setOverScrollAmount(float f, boolean z, boolean z2, boolean z3) {
        setOverScrollAmount(f, z, z2, z3, isRubberbanded(z));
    }

    @Override // android.view.ViewGroup
    public final boolean shouldDelayChildPressedState() {
        return true;
    }

    public final void updateViewShadows() {
        float f;
        for (int i = 0; i < getChildCount(); i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                this.mTmpSortedChildren.add(expandableView);
            }
        }
        Collections.sort(this.mTmpSortedChildren, this.mViewPositionComparator);
        ExpandableView expandableView2 = null;
        int i2 = 0;
        while (i2 < this.mTmpSortedChildren.size()) {
            ExpandableView expandableView3 = this.mTmpSortedChildren.get(i2);
            float translationZ = expandableView3.getTranslationZ();
            if (expandableView2 == null) {
                f = translationZ;
            } else {
                f = expandableView2.getTranslationZ();
            }
            float f2 = f - translationZ;
            if (f2 <= 0.0f || f2 >= 0.1f) {
                expandableView3.setFakeShadowIntensity(0.0f, 0.0f, 0, 0);
            } else {
                expandableView2.getExtraBottomPadding();
                expandableView3.setFakeShadowIntensity(f2 / 0.1f, expandableView2.getOutlineAlpha(), (int) (((expandableView2.getTranslationY() + expandableView2.mActualHeight) - expandableView3.getTranslationY()) - 0), (int) (expandableView2.getTranslation() + expandableView2.getOutlineTranslation()));
            }
            i2++;
            expandableView2 = expandableView3;
        }
        this.mTmpSortedChildren.clear();
    }

    public static boolean canChildBeCleared(View view) {
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            if (expandableNotificationRow.areGutsExposed() || !expandableNotificationRow.mEntry.hasFinishedInitialization() || !expandableNotificationRow.mEntry.isClearable()) {
                return false;
            }
            if (!expandableNotificationRow.shouldShowPublic() || !expandableNotificationRow.mSensitiveHiddenInGeneral) {
                return true;
            }
            return false;
        }
        if (view instanceof PeopleHubView) {
            Objects.requireNonNull((PeopleHubView) view);
        }
        return false;
    }

    public static void clearTemporaryViewsInGroup(ViewGroup viewGroup) {
        while (viewGroup != null && viewGroup.getTransientViewCount() != 0) {
            View transientView = viewGroup.getTransientView(0);
            viewGroup.removeTransientView(transientView);
            if (transientView instanceof ExpandableView) {
                ExpandableView expandableView = (ExpandableView) transientView;
                Objects.requireNonNull(expandableView);
                expandableView.mTransientContainer = null;
            }
        }
    }

    public static boolean isPinnedHeadsUp(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        Objects.requireNonNull(expandableNotificationRow);
        if (!expandableNotificationRow.mIsHeadsUp || !expandableNotificationRow.mIsPinned) {
            return false;
        }
        return true;
    }

    public final void abortBackgroundAnimators() {
        NotificationSection[] notificationSectionArr;
        for (NotificationSection notificationSection : this.mSections) {
            Objects.requireNonNull(notificationSection);
            ObjectAnimator objectAnimator = notificationSection.mBottomAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator objectAnimator2 = notificationSection.mTopAnimator;
            if (objectAnimator2 != null) {
                objectAnimator2.cancel();
            }
        }
    }

    public final void animateScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int i = this.mOwnScrollY;
            int currY = this.mScroller.getCurrY();
            if (i != currY) {
                int scrollRange = getScrollRange();
                if ((currY < 0 && i >= 0) || (currY > scrollRange && i <= scrollRange)) {
                    float currVelocity = this.mScroller.getCurrVelocity();
                    if (currVelocity >= this.mMinimumVelocity) {
                        this.mMaxOverScroll = (Math.abs(currVelocity) / 1000.0f) * this.mOverflingDistance;
                    }
                }
                if (this.mDontClampNextScroll) {
                    scrollRange = Math.max(scrollRange, i);
                }
                customOverScrollBy(currY - i, i, scrollRange, (int) this.mMaxOverScroll);
            }
            postOnAnimation(this.mReflingAndAnimateScroll);
            return;
        }
        this.mDontClampNextScroll = false;
        Runnable runnable = this.mFinishScrollingCallback;
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // android.view.View
    public final void cancelLongPress() {
        this.mSwipeHelper.cancelLongPress();
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0036, code lost:
        if (includeChildInClearAll(r8, r20) != false) goto L_0x0038;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x007e A[SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void clearNotifications(final int r20, boolean r21) {
        /*
            Method dump skipped, instructions count: 295
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.clearNotifications(int, boolean):void");
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void dispatchDraw(Canvas canvas) {
        if (this.mShouldUseRoundedRectClipping && !this.mLaunchingNotification) {
            canvas.clipPath(this.mRoundedClipPath);
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        Path path;
        if (!this.mShouldUseRoundedRectClipping || !this.mLaunchingNotification) {
            return super.drawChild(canvas, view, j);
        }
        canvas.save();
        ExpandableView expandableView = (ExpandableView) view;
        if (expandableView.isExpandAnimationRunning() || expandableView.hasExpandingChild()) {
            path = null;
        } else {
            path = this.mRoundedClipPath;
        }
        if (path != null) {
            canvas.clipPath(path);
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public final void drawDebugInfo(Canvas canvas, int i, int i2, String str) {
        this.mDebugPaint.setColor(i2);
        float f = i;
        canvas.drawLine(0.0f, f, getWidth(), f, this.mDebugPaint);
        while (this.mDebugTextUsedYPositions.contains(Integer.valueOf(i))) {
            i = (int) (this.mDebugPaint.getTextSize() + i);
        }
        this.mDebugTextUsedYPositions.add(Integer.valueOf(i));
        canvas.drawText(str, 0.0f, i, this.mDebugPaint);
    }

    public final void generateAddAnimation(ExpandableView expandableView, boolean z) {
        boolean z2;
        if (this.mIsExpanded && this.mAnimationsEnabled && !this.mChangePositionInProgress && !this.mAmbientState.isFullyHidden()) {
            this.mChildrenToAddAnimated.add(expandableView);
            if (z) {
                this.mFromMoreCardAdditions.add(expandableView);
            }
            this.mNeedsAnimation = true;
        }
        if (expandableView instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
            Objects.requireNonNull(expandableNotificationRow);
            z2 = expandableNotificationRow.mIsHeadsUp;
        } else {
            z2 = false;
        }
        if (z2 && this.mAnimationsEnabled && !this.mChangePositionInProgress && !this.mAmbientState.isFullyHidden()) {
            this.mAddedHeadsUpChildren.add(expandableView);
            this.mChildrenToAddAnimated.remove(expandableView);
        }
    }

    public final void generateHeadsUpAnimation(ExpandableNotificationRow expandableNotificationRow, boolean z) {
        boolean z2;
        if (!this.mAnimationsEnabled || (!z && !this.mHeadsUpGoingAwayAnimationsAllowed)) {
            z2 = false;
        } else {
            z2 = true;
        }
        boolean z3 = SPEW;
        if (z3) {
            StringBuilder sb = new StringBuilder();
            sb.append("generateHeadsUpAnimation: willAdd=");
            sb.append(z2);
            sb.append(" isHeadsUp=");
            sb.append(z);
            sb.append(" row=");
            Objects.requireNonNull(expandableNotificationRow);
            NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
            Objects.requireNonNull(notificationEntry);
            sb.append(notificationEntry.mKey);
            Log.v("StackScroller", sb.toString());
        }
        if (!z2) {
            return;
        }
        if (z || !this.mHeadsUpChangeAnimations.remove(new Pair(expandableNotificationRow, Boolean.TRUE))) {
            this.mHeadsUpChangeAnimations.add(new Pair<>(expandableNotificationRow, Boolean.valueOf(z)));
            this.mNeedsAnimation = true;
            if (!this.mIsExpanded && !this.mWillExpand && !z) {
                expandableNotificationRow.setHeadsUpAnimatingAway(true);
            }
            requestChildrenUpdate();
            return;
        }
        if (z3) {
            Log.v("StackScroller", "generateHeadsUpAnimation: previous hun appear animation cancelled");
        }
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry notificationEntry2 = expandableNotificationRow.mEntry;
        Objects.requireNonNull(notificationEntry2);
        logHunAnimationSkipped(notificationEntry2.mKey, "previous hun appear animation cancelled");
    }

    public final boolean generateRemoveAnimation(ExpandableView expandableView) {
        boolean z;
        boolean z2;
        String str = "";
        if (this.mDebugRemoveAnimation) {
            if (expandableView instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                Objects.requireNonNull(expandableNotificationRow);
                NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
                Objects.requireNonNull(notificationEntry);
                str = notificationEntry.mKey;
            }
            DialogFragment$$ExternalSyntheticOutline0.m("generateRemoveAnimation ", str, "StackScroller");
        }
        Iterator<Pair<ExpandableNotificationRow, Boolean>> it = this.mHeadsUpChangeAnimations.iterator();
        boolean z3 = false;
        while (it.hasNext()) {
            Pair<ExpandableNotificationRow, Boolean> next = it.next();
            ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) next.first;
            boolean booleanValue = ((Boolean) next.second).booleanValue();
            if (expandableView == expandableNotificationRow2) {
                this.mTmpList.add(next);
                z3 |= booleanValue;
            }
        }
        if (z3) {
            this.mHeadsUpChangeAnimations.removeAll(this.mTmpList);
            ((ExpandableNotificationRow) expandableView).setHeadsUpAnimatingAway(false);
        }
        this.mTmpList.clear();
        if (!z3 || !this.mAddedHeadsUpChildren.contains(expandableView)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            if (this.mDebugRemoveAnimation) {
                DialogFragment$$ExternalSyntheticOutline0.m("removedBecauseOfHeadsUp ", str, "StackScroller");
            }
            this.mAddedHeadsUpChildren.remove(expandableView);
            return false;
        }
        Boolean bool = (Boolean) expandableView.getTag(2131428138);
        if (bool == null || !bool.booleanValue()) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            this.mClearTransientViewsWhenFinished.add(expandableView);
            return true;
        }
        if (this.mDebugRemoveAnimation) {
            StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("generateRemove ", str, "\nmIsExpanded ");
            m.append(this.mIsExpanded);
            m.append("\nmAnimationsEnabled ");
            m.append(this.mAnimationsEnabled);
            m.append("\n!invisible group ");
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, !isChildInInvisibleGroup(expandableView), "StackScroller");
        }
        if (this.mIsExpanded && this.mAnimationsEnabled && !isChildInInvisibleGroup(expandableView)) {
            if (!this.mChildrenToAddAnimated.contains(expandableView)) {
                if (this.mDebugRemoveAnimation) {
                    DialogFragment$$ExternalSyntheticOutline0.m("needsAnimation = true ", str, "StackScroller");
                }
                this.mChildrenToRemoveAnimated.add(expandableView);
                this.mNeedsAnimation = true;
                return true;
            }
            this.mChildrenToAddAnimated.remove(expandableView);
            this.mFromMoreCardAdditions.remove(expandableView);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0024, code lost:
        if (r1.mDozing == false) goto L_0x003a;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float getAppearEndPosition() {
        /*
            r4 = this;
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r4.mController
            int r0 = r0.getVisibleNotificationCount()
            com.android.systemui.statusbar.EmptyShadeView r1 = r4.mEmptyShadeView
            int r1 = r1.getVisibility()
            r2 = 8
            r3 = 0
            if (r1 != r2) goto L_0x0063
            if (r0 <= 0) goto L_0x0063
            boolean r1 = r4.isHeadsUpTransition()
            if (r1 != 0) goto L_0x003a
            boolean r1 = r4.mInHeadsUpPinnedMode
            if (r1 == 0) goto L_0x0027
            com.android.systemui.statusbar.notification.stack.AmbientState r1 = r4.mAmbientState
            java.util.Objects.requireNonNull(r1)
            boolean r1 = r1.mDozing
            if (r1 != 0) goto L_0x0027
            goto L_0x003a
        L_0x0027:
            com.android.systemui.statusbar.NotificationShelf r0 = r4.mShelf
            int r0 = r0.getVisibility()
            if (r0 == r2) goto L_0x0069
            com.android.systemui.statusbar.NotificationShelf r0 = r4.mShelf
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.getHeight()
            int r3 = r3 + r0
            goto L_0x0069
        L_0x003a:
            com.android.systemui.statusbar.NotificationShelf r1 = r4.mShelf
            int r1 = r1.getVisibility()
            if (r1 == r2) goto L_0x0052
            r1 = 1
            if (r0 <= r1) goto L_0x0052
            com.android.systemui.statusbar.NotificationShelf r0 = r4.mShelf
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.getHeight()
            int r1 = r4.mPaddingBetweenElements
            int r0 = r0 + r1
            int r3 = r3 + r0
        L_0x0052:
            int r0 = r4.getTopHeadsUpPinnedHeight()
            com.android.systemui.statusbar.notification.stack.AmbientState r1 = r4.mAmbientState
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = r1.getTrackedHeadsUpRow()
            int r1 = r4.getPositionInLinearLayout(r1)
            int r1 = r1 + r0
            int r3 = r3 + r1
            goto L_0x0069
        L_0x0063:
            com.android.systemui.statusbar.EmptyShadeView r0 = r4.mEmptyShadeView
            int r3 = r0.getHeight()
        L_0x0069:
            boolean r0 = r4.onKeyguard()
            if (r0 == 0) goto L_0x0072
            int r4 = r4.mTopPadding
            goto L_0x0074
        L_0x0072:
            int r4 = r4.mIntrinsicPadding
        L_0x0074:
            int r3 = r3 + r4
            float r4 = (float) r3
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.getAppearEndPosition():float");
    }

    public final ExpandableView getChildAtRawPosition(float f, float f2) {
        getLocationOnScreen(this.mTempInt2);
        int[] iArr = this.mTempInt2;
        return getChildAtPosition(f - iArr[0], f2 - iArr[1], true, true);
    }

    public final ArrayList getChildrenWithBackground() {
        ArrayList arrayList = new ArrayList();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (!(expandableView.getVisibility() == 8 || (expandableView instanceof StackScrollerDecorView) || expandableView == this.mShelf)) {
                arrayList.add(expandableView);
            }
        }
        return arrayList;
    }

    public final float getCurrentOverScrollAmount(boolean z) {
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (z) {
            return ambientState.mOverScrollTopAmount;
        }
        return ambientState.mOverScrollBottomAmount;
    }

    public final int getEmptyBottomMargin() {
        int i;
        if (this.mShouldUseSplitNotificationShade) {
            i = Math.max(this.mSplitShadeMinContentHeight, this.mContentHeight);
        } else {
            i = this.mContentHeight;
        }
        return Math.max(this.mMaxLayoutHeight - i, 0);
    }

    public final NotificationSection getFirstVisibleSection() {
        NotificationSection[] notificationSectionArr;
        for (NotificationSection notificationSection : this.mSections) {
            Objects.requireNonNull(notificationSection);
            if (notificationSection.mFirstVisibleChild != null) {
                return notificationSection;
            }
        }
        return null;
    }

    public final NotificationSection getLastVisibleSection() {
        for (int length = this.mSections.length - 1; length >= 0; length--) {
            NotificationSection notificationSection = this.mSections[length];
            Objects.requireNonNull(notificationSection);
            if (notificationSection.mLastVisibleChild != null) {
                return notificationSection;
            }
        }
        return null;
    }

    public final int getMinExpansionHeight() {
        NotificationShelf notificationShelf = this.mShelf;
        Objects.requireNonNull(notificationShelf);
        int height = notificationShelf.getHeight();
        NotificationShelf notificationShelf2 = this.mShelf;
        Objects.requireNonNull(notificationShelf2);
        return (height - Math.max(0, ((notificationShelf2.getHeight() - this.mStatusBarHeight) + this.mWaterfallTopInset) / 2)) + this.mWaterfallTopInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x001c  */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int getPositionInLinearLayout(android.view.View r11) {
        /*
            r10 = this;
            boolean r0 = r11 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0018
            com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager r0 = r10.mGroupMembershipManager
            r3 = r11
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r3
            java.util.Objects.requireNonNull(r3)
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r3.mEntry
            boolean r0 = r0.isChildInGroup(r3)
            if (r0 == 0) goto L_0x0018
            r0 = r1
            goto L_0x0019
        L_0x0018:
            r0 = r2
        L_0x0019:
            r3 = 0
            if (r0 == 0) goto L_0x0027
            r3 = r11
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r3
            java.util.Objects.requireNonNull(r3)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r11 = r3.mNotificationParent
            r0 = r3
            r3 = r11
            goto L_0x0028
        L_0x0027:
            r0 = r3
        L_0x0028:
            r4 = r2
            r5 = r4
        L_0x002a:
            int r6 = r10.getChildCount()
            if (r4 >= r6) goto L_0x009c
            android.view.View r6 = r10.getChildAt(r4)
            com.android.systemui.statusbar.notification.row.ExpandableView r6 = (com.android.systemui.statusbar.notification.row.ExpandableView) r6
            int r7 = r6.getVisibility()
            r8 = 8
            if (r7 == r8) goto L_0x0040
            r7 = r1
            goto L_0x0041
        L_0x0040:
            r7 = r2
        L_0x0041:
            if (r7 == 0) goto L_0x004c
            boolean r9 = r6 instanceof com.android.systemui.statusbar.NotificationShelf
            if (r9 != 0) goto L_0x004c
            if (r5 == 0) goto L_0x004c
            int r9 = r10.mPaddingBetweenElements
            int r5 = r5 + r9
        L_0x004c:
            if (r6 != r11) goto L_0x0091
            if (r3 == 0) goto L_0x0090
            boolean r10 = r3.mIsSummaryWithChildren
            if (r10 == 0) goto L_0x008f
            com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer r10 = r3.mChildrenContainer
            java.util.Objects.requireNonNull(r10)
            int r11 = r10.mNotificationHeaderMargin
            int r3 = r10.mCurrentHeaderTranslation
            int r11 = r11 + r3
            int r3 = r10.mNotificationTopPadding
            int r11 = r11 + r3
            r3 = r2
        L_0x0062:
            java.util.ArrayList r4 = r10.mAttachedChildren
            int r4 = r4.size()
            if (r3 >= r4) goto L_0x008f
            java.util.ArrayList r4 = r10.mAttachedChildren
            java.lang.Object r4 = r4.get(r3)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r4
            int r6 = r4.getVisibility()
            if (r6 == r8) goto L_0x007a
            r6 = r1
            goto L_0x007b
        L_0x007a:
            r6 = r2
        L_0x007b:
            if (r6 == 0) goto L_0x0080
            int r7 = r10.mDividerHeight
            int r11 = r11 + r7
        L_0x0080:
            if (r4 != r0) goto L_0x0084
            r2 = r11
            goto L_0x008f
        L_0x0084:
            if (r6 == 0) goto L_0x008c
            int r4 = r4.getIntrinsicHeight()
            int r4 = r4 + r11
            r11 = r4
        L_0x008c:
            int r3 = r3 + 1
            goto L_0x0062
        L_0x008f:
            int r5 = r5 + r2
        L_0x0090:
            return r5
        L_0x0091:
            if (r7 == 0) goto L_0x0099
            int r6 = r6.getIntrinsicHeight()
            int r6 = r6 + r5
            r5 = r6
        L_0x0099:
            int r4 = r4 + 1
            goto L_0x002a
        L_0x009c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.getPositionInLinearLayout(android.view.View):int");
    }

    public final int getScrollRange() {
        int i;
        int i2 = this.mContentHeight;
        if (!this.mIsExpanded && this.mInHeadsUpPinnedMode) {
            i2 = this.mHeadsUpInset + getTopHeadsUpPinnedHeight();
        }
        int max = Math.max(0, i2 - this.mMaxLayoutHeight);
        int max2 = Math.max(0, this.mBottomInset - (getRootView().getHeight() - getHeight()));
        int min = Math.min(max2, Math.max(0, i2 - (getHeight() - max2))) + max;
        if (min <= 0) {
            return min;
        }
        if (this.mShouldUseSplitNotificationShade) {
            i = this.mSidePaddings;
        } else {
            i = this.mTopPadding - this.mQsScrollBoundaryPosition;
        }
        return Math.max(i, min);
    }

    public final int getSpeedBumpIndex() {
        if (this.mSpeedBumpIndexDirty) {
            this.mSpeedBumpIndexDirty = false;
            int childCount = getChildCount();
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() != 8 && (childAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    i2++;
                    boolean z = true;
                    if (this.mHighPriorityBeforeSpeedBump) {
                        NotificationEntry notificationEntry = expandableNotificationRow.mEntry;
                        Objects.requireNonNull(notificationEntry);
                        if (notificationEntry.mBucket >= 6) {
                            z = false;
                        }
                    } else {
                        z = true ^ expandableNotificationRow.mEntry.isAmbient();
                    }
                    if (z) {
                        i = i2;
                    }
                }
            }
            this.mSpeedBumpIndex = i;
        }
        return this.mSpeedBumpIndex;
    }

    public final int getTopHeadsUpPinnedHeight() {
        NotificationEntry groupSummary;
        NotificationEntry notificationEntry = this.mTopHeadsUpEntry;
        if (notificationEntry == null) {
            return 0;
        }
        Objects.requireNonNull(notificationEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        if (expandableNotificationRow.isChildInGroup() && (groupSummary = this.mGroupMembershipManager.getGroupSummary(expandableNotificationRow.mEntry)) != null) {
            expandableNotificationRow = groupSummary.row;
        }
        Objects.requireNonNull(expandableNotificationRow);
        return expandableNotificationRow.getPinnedHeadsUpHeight(true);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        if (this.mForceNoOverlappingRendering || !super.hasOverlappingRendering()) {
            return false;
        }
        return true;
    }

    public final void inflateEmptyShadeView() {
        int i;
        EmptyShadeView emptyShadeView = (EmptyShadeView) LayoutInflater.from(((ViewGroup) this).mContext).inflate(2131624523, (ViewGroup) this, false);
        Objects.requireNonNull(emptyShadeView);
        emptyShadeView.mText = 2131952312;
        emptyShadeView.mEmptyText.setText(2131952312);
        emptyShadeView.setOnClickListener(new GameMenuActivity$$ExternalSyntheticLambda1(this, 1));
        View view = this.mEmptyShadeView;
        if (view != null) {
            i = indexOfChild(view);
            removeView(this.mEmptyShadeView);
        } else {
            i = -1;
        }
        this.mEmptyShadeView = emptyShadeView;
        addView(emptyShadeView, i);
    }

    @VisibleForTesting
    public void inflateFooterView() {
        int i;
        FooterView footerView = (FooterView) LayoutInflater.from(((ViewGroup) this).mContext).inflate(2131624524, (ViewGroup) this, false);
        GameMenuActivity$$ExternalSyntheticLambda2 gameMenuActivity$$ExternalSyntheticLambda2 = new GameMenuActivity$$ExternalSyntheticLambda2(this, footerView, 1);
        Objects.requireNonNull(footerView);
        footerView.mClearAllButton.setOnClickListener(gameMenuActivity$$ExternalSyntheticLambda2);
        View view = this.mFooterView;
        if (view != null) {
            i = indexOfChild(view);
            removeView(this.mFooterView);
        } else {
            i = -1;
        }
        this.mFooterView = footerView;
        addView(footerView, i);
        View.OnClickListener onClickListener = this.mManageButtonClickListener;
        if (onClickListener != null) {
            FooterView footerView2 = this.mFooterView;
            Objects.requireNonNull(footerView2);
            footerView2.mManageButton.setOnClickListener(onClickListener);
        }
    }

    public final void initView(Context context, NotificationSwipeHelper notificationSwipeHelper) {
        this.mScroller = new OverScroller(getContext());
        this.mSwipeHelper = notificationSwipeHelper;
        setDescendantFocusability(262144);
        setClipChildren(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mOverflingDistance = viewConfiguration.getScaledOverflingDistance();
        Resources resources = context.getResources();
        resources.getDimensionPixelSize(2131166657);
        this.mGapHeight = resources.getDimensionPixelSize(2131166672);
        this.mStackScrollAlgorithm.initView(context);
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mZDistanceBetweenElements = Math.max(1, context.getResources().getDimensionPixelSize(2131167339));
        this.mPaddingBetweenElements = Math.max(1, resources.getDimensionPixelSize(2131166628));
        this.mMinTopOverScrollToEscape = resources.getDimensionPixelSize(2131166367);
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(((ViewGroup) this).mContext);
        this.mBottomPadding = resources.getDimensionPixelSize(2131166667);
        this.mMinimumPaddings = resources.getDimensionPixelSize(2131166677);
        this.mQsTilePadding = resources.getDimensionPixelOffset(2131166912);
        this.mSkinnyNotifsInLandscape = resources.getBoolean(2131034172);
        this.mSidePaddings = this.mMinimumPaddings;
        this.mMinInteractionHeight = resources.getDimensionPixelSize(2131166663);
        this.mCornerRadius = resources.getDimensionPixelSize(2131166625);
        this.mHeadsUpInset = resources.getDimensionPixelSize(2131165797) + this.mStatusBarHeight;
        this.mQsScrollBoundaryPosition = SystemBarUtils.getQuickQsOffsetHeight(((ViewGroup) this).mContext);
    }

    public final boolean isChildInInvisibleGroup(View view) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        GroupMembershipManager groupMembershipManager = this.mGroupMembershipManager;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationEntry groupSummary = groupMembershipManager.getGroupSummary(expandableNotificationRow.mEntry);
        if (groupSummary == null || groupSummary.row == expandableNotificationRow || expandableNotificationRow.getVisibility() != 4) {
            return false;
        }
        return true;
    }

    @VisibleForTesting
    public boolean isDimmed() {
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (!ambientState.mDimmed || (ambientState.isPulseExpanding() && ambientState.mDozeAmount == 1.0f)) {
            return false;
        }
        return true;
    }

    public final boolean isHeadsUpTransition() {
        if (this.mAmbientState.getTrackedHeadsUpRow() != null) {
            return true;
        }
        return false;
    }

    public final boolean isRubberbanded(boolean z) {
        if (!z || this.mExpandedInThisMotion || this.mIsExpansionChanging || this.mPanelTracking || !this.mScrolledToTopOnFirstDown) {
            return true;
        }
        return false;
    }

    public final boolean isVisible(ExpandableNotificationRow expandableNotificationRow) {
        boolean clipBounds = expandableNotificationRow.getClipBounds(this.mTmpRect);
        if (expandableNotificationRow.getVisibility() != 0 || (clipBounds && this.mTmpRect.height() <= 0)) {
            return false;
        }
        return true;
    }

    public final void logHunAnimationSkipped(String str, String str2) {
        NotificationStackScrollLogger notificationStackScrollLogger = this.mLogger;
        if (notificationStackScrollLogger != null) {
            LogBuffer logBuffer = notificationStackScrollLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            NotificationStackScrollLogger$hunAnimationSkipped$2 notificationStackScrollLogger$hunAnimationSkipped$2 = NotificationStackScrollLogger$hunAnimationSkipped$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("NotificationStackScroll", logLevel, notificationStackScrollLogger$hunAnimationSkipped$2);
                obtain.str1 = str;
                obtain.str2 = str2;
                logBuffer.push(obtain);
            }
        }
    }

    public final void notifyAppearChangedListeners() {
        float f;
        float f2;
        if (!this.mKeyguardBypassEnabled || !onKeyguard()) {
            float f3 = this.mExpandedHeight;
            float appearEndPosition = getAppearEndPosition();
            float appearStartPosition = getAppearStartPosition();
            f2 = MathUtils.saturate((f3 - appearStartPosition) / (appearEndPosition - appearStartPosition));
            f = this.mExpandedHeight;
        } else {
            AmbientState ambientState = this.mAmbientState;
            Objects.requireNonNull(ambientState);
            float f4 = ambientState.mPulseHeight;
            f = 0.0f;
            if (f4 == 100000.0f) {
                f4 = 0.0f;
            }
            f2 = MathUtils.smoothStep(0.0f, this.mIntrinsicPadding, f4);
            AmbientState ambientState2 = this.mAmbientState;
            Objects.requireNonNull(ambientState2);
            float f5 = ambientState2.mPulseHeight;
            if (f5 != 100000.0f) {
                f = f5;
            }
        }
        if (!(f2 == this.mLastSentAppear && f == this.mLastSentExpandedHeight)) {
            this.mLastSentAppear = f2;
            this.mLastSentExpandedHeight = f;
            for (int i = 0; i < this.mExpandedHeightListeners.size(); i++) {
                this.mExpandedHeightListeners.get(i).accept(Float.valueOf(f), Float.valueOf(f2));
            }
        }
    }

    public final void notifyHeightChangeListener(ExpandableView expandableView, boolean z) {
        ExpandableView.OnHeightChangedListener onHeightChangedListener = this.mOnHeightChangedListener;
        if (onHeightChangedListener != null) {
            onHeightChangedListener.onHeightChanged(expandableView, z);
        }
    }

    public final void notifyOverscrollTopListener(float f, boolean z) {
        boolean z2;
        boolean z3;
        NotificationPanelViewController notificationPanelViewController;
        ExpandHelper expandHelper = this.mExpandHelper;
        boolean z4 = true;
        if (f > 1.0f) {
            z2 = true;
        } else {
            z2 = false;
        }
        Objects.requireNonNull(expandHelper);
        expandHelper.mOnlyMovements = z2;
        if (this.mDontReportNextOverScroll) {
            this.mDontReportNextOverScroll = false;
            return;
        }
        OnOverscrollTopChangedListener onOverscrollTopChangedListener = this.mOverscrollTopChangedListener;
        if (onOverscrollTopChangedListener != null) {
            NotificationPanelViewController.OnOverscrollTopChangedListener onOverscrollTopChangedListener2 = (NotificationPanelViewController.OnOverscrollTopChangedListener) onOverscrollTopChangedListener;
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            if (!notificationPanelViewController2.mShouldUseSplitNotificationShade) {
                ValueAnimator valueAnimator = notificationPanelViewController2.mQsExpansionAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                if (!NotificationPanelViewController.this.isQsExpansionEnabled()) {
                    f = 0.0f;
                }
                if (f < 1.0f) {
                    f = 0.0f;
                }
                NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
                int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i == 0 || !z) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                Objects.requireNonNull(notificationPanelViewController3);
                notificationPanelViewController3.mStackScrollerOverscrolling = z3;
                QS qs = notificationPanelViewController3.mQs;
                if (qs != null) {
                    qs.setOverscrolling(z3);
                }
                NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
                if (i == 0) {
                    z4 = false;
                }
                notificationPanelViewController4.mQsExpansionFromOverscroll = z4;
                notificationPanelViewController4.mLastOverscroll = f;
                notificationPanelViewController4.updateQsState();
                NotificationPanelViewController.this.setQsExpansion(notificationPanelViewController.mQsMinExpansionHeight + f);
            }
        }
    }

    public final void onChildHeightChanged(ExpandableView expandableView, boolean z) {
        ExpandableNotificationRow expandableNotificationRow;
        ExpandableView expandableView2;
        boolean z2 = this.mAnimateStackYForContentHeightChange;
        if (z) {
            this.mAnimateStackYForContentHeightChange = true;
        }
        updateContentHeight();
        boolean z3 = expandableView instanceof ExpandableNotificationRow;
        ExpandableView expandableView3 = null;
        if (z3 && !onKeyguard()) {
            ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) expandableView;
            Objects.requireNonNull(expandableNotificationRow2);
            if (expandableNotificationRow2.mUserLocked && expandableNotificationRow2 != getFirstChildNotGone() && !expandableNotificationRow2.mIsSummaryWithChildren) {
                float translationY = expandableNotificationRow2.getTranslationY() + expandableNotificationRow2.mActualHeight;
                if (expandableNotificationRow2.isChildInGroup()) {
                    translationY += expandableNotificationRow2.mNotificationParent.getTranslationY();
                }
                int i = this.mMaxLayoutHeight + ((int) this.mStackTranslation);
                NotificationSection lastVisibleSection = getLastVisibleSection();
                if (lastVisibleSection == null) {
                    expandableView2 = null;
                } else {
                    expandableView2 = lastVisibleSection.mLastVisibleChild;
                }
                if (!(expandableNotificationRow2 == expandableView2 || this.mShelf.getVisibility() == 8)) {
                    NotificationShelf notificationShelf = this.mShelf;
                    Objects.requireNonNull(notificationShelf);
                    i -= notificationShelf.getHeight() + this.mPaddingBetweenElements;
                }
                float f = i;
                if (translationY > f) {
                    setOwnScrollY((int) ((this.mOwnScrollY + translationY) - f), false);
                    this.mDisallowScrollingInThisMotion = true;
                }
            }
        }
        clampScrollPosition();
        notifyHeightChangeListener(expandableView, z);
        if (z3) {
            expandableNotificationRow = (ExpandableNotificationRow) expandableView;
        } else {
            expandableNotificationRow = null;
        }
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        if (firstVisibleSection != null) {
            expandableView3 = firstVisibleSection.mFirstVisibleChild;
        }
        if (expandableNotificationRow != null && (expandableNotificationRow == expandableView3 || expandableNotificationRow.mNotificationParent == expandableView3)) {
            updateAlgorithmLayoutMinHeight();
        }
        if (z && this.mAnimationsEnabled && (this.mIsExpanded || (expandableNotificationRow != null && expandableNotificationRow.mIsPinned))) {
            this.mNeedViewResizeAnimation = true;
            this.mNeedsAnimation = true;
        }
        requestChildrenUpdate();
        this.mAnimateStackYForContentHeightChange = z2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x002b, code lost:
        if (r1.mDozing != false) goto L_0x002d;
     */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:98:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onDraw(android.graphics.Canvas r23) {
        /*
            Method dump skipped, instructions count: 613
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onDraw(android.graphics.Canvas):void");
    }

    @Override // android.view.View
    public final boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (this.mScrollingEnabled && this.mIsExpanded) {
            NotificationSwipeHelper notificationSwipeHelper = this.mSwipeHelper;
            Objects.requireNonNull(notificationSwipeHelper);
            if (!notificationSwipeHelper.mIsSwiping && !this.mExpandingNotification && !this.mDisallowScrollingInThisMotion) {
                if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() == 8 && !this.mIsBeingDragged) {
                    float axisValue = motionEvent.getAxisValue(9);
                    if (axisValue != 0.0f) {
                        int scrollRange = getScrollRange();
                        int i = this.mOwnScrollY;
                        int verticalScrollFactor = i - ((int) (axisValue * getVerticalScrollFactor()));
                        if (verticalScrollFactor < 0) {
                            scrollRange = 0;
                        } else if (verticalScrollFactor <= scrollRange) {
                            scrollRange = verticalScrollFactor;
                        }
                        if (scrollRange != i) {
                            setOwnScrollY(scrollRange, false);
                            return true;
                        }
                    }
                }
                return super.onGenericMotionEvent(motionEvent);
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        NotificationStackScrollLayoutController.TouchHandler touchHandler = this.mTouchHandler;
        if (touchHandler == null || !touchHandler.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    public final boolean onInterceptTouchEventScroll(MotionEvent motionEvent) {
        boolean z;
        if (!this.mScrollingEnabled) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        int i = action & 255;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    int i2 = this.mActivePointerId;
                    if (i2 != -1) {
                        int findPointerIndex = motionEvent.findPointerIndex(i2);
                        if (findPointerIndex == -1) {
                            Log.e("StackScroller", "Invalid pointerId=" + i2 + " in onInterceptTouchEvent");
                        } else {
                            int y = (int) motionEvent.getY(findPointerIndex);
                            int x = (int) motionEvent.getX(findPointerIndex);
                            int abs = Math.abs(y - this.mLastMotionY);
                            int abs2 = Math.abs(x - this.mDownX);
                            if (abs > getTouchSlop(motionEvent) && abs > abs2) {
                                setIsBeingDragged(true);
                                this.mLastMotionY = y;
                                this.mDownX = x;
                                if (this.mVelocityTracker == null) {
                                    this.mVelocityTracker = VelocityTracker.obtain();
                                }
                                this.mVelocityTracker.addMovement(motionEvent);
                            }
                        }
                    }
                } else if (i != 3) {
                    if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            }
            setIsBeingDragged(false);
            this.mActivePointerId = -1;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            if (this.mScroller.springBack(((ViewGroup) this).mScrollX, this.mOwnScrollY, 0, 0, 0, getScrollRange())) {
                animateScroll();
            }
        } else {
            int y2 = (int) motionEvent.getY();
            AnonymousClass7 r2 = this.mScrollAdapter;
            Objects.requireNonNull(r2);
            if (NotificationStackScrollLayout.this.mOwnScrollY == 0) {
                z = true;
            } else {
                z = false;
            }
            this.mScrolledToTopOnFirstDown = z;
            if (getChildAtPosition(motionEvent.getX(), y2, false, false) == null) {
                setIsBeingDragged(false);
                VelocityTracker velocityTracker2 = this.mVelocityTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.recycle();
                    this.mVelocityTracker = null;
                }
            } else {
                this.mLastMotionY = y2;
                this.mDownX = (int) motionEvent.getX();
                this.mActivePointerId = motionEvent.getPointerId(0);
                VelocityTracker velocityTracker3 = this.mVelocityTracker;
                if (velocityTracker3 == null) {
                    this.mVelocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker3.clear();
                }
                this.mVelocityTracker.addMovement(motionEvent);
                setIsBeingDragged(!this.mScroller.isFinished());
            }
        }
        return this.mIsBeingDragged;
    }

    public final boolean onKeyguard() {
        if (this.mStatusBarState == 1) {
            return true;
        }
        return false;
    }

    public final void onOverScrollFling(boolean z, int i) {
        int i2;
        OnOverscrollTopChangedListener onOverscrollTopChangedListener = this.mOverscrollTopChangedListener;
        if (onOverscrollTopChangedListener != null) {
            float f = i;
            NotificationPanelViewController.OnOverscrollTopChangedListener onOverscrollTopChangedListener2 = (NotificationPanelViewController.OnOverscrollTopChangedListener) onOverscrollTopChangedListener;
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (!notificationPanelViewController.mShouldUseSplitNotificationShade || !notificationPanelViewController.touchXOutsideOfQs(notificationPanelViewController.mInitialTouchX)) {
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                notificationPanelViewController2.mLastOverscroll = 0.0f;
                notificationPanelViewController2.mQsExpansionFromOverscroll = false;
                if (z) {
                    notificationPanelViewController2.mStackScrollerOverscrolling = false;
                    QS qs = notificationPanelViewController2.mQs;
                    if (qs != null) {
                        qs.setOverscrolling(false);
                    }
                }
                NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
                notificationPanelViewController3.setQsExpansion(notificationPanelViewController3.mQsExpansionHeight);
                boolean isQsExpansionEnabled = NotificationPanelViewController.this.isQsExpansionEnabled();
                NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
                if (!isQsExpansionEnabled && z) {
                    f = 0.0f;
                }
                if (!z || !isQsExpansionEnabled) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                notificationPanelViewController4.flingSettings(f, i2, new WMShell$7$$ExternalSyntheticLambda1(onOverscrollTopChangedListener2, 5), false);
            }
        }
        this.mDontReportNextOverScroll = true;
        setOverScrollAmount(0.0f, true, false, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x01ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onScrollTouch(android.view.MotionEvent r29) {
        /*
            Method dump skipped, instructions count: 796
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onScrollTouch(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        NotificationStackScrollLayoutController.TouchHandler touchHandler = this.mTouchHandler;
        if (touchHandler == null || !touchHandler.onTouchEvent(motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public final void onUpdateRowStates() {
        ForegroundServiceDungeonView foregroundServiceDungeonView = this.mFgsSectionView;
        int i = 1;
        if (foregroundServiceDungeonView != null) {
            changeViewPosition(foregroundServiceDungeonView, getChildCount() - 1);
            i = 2;
        }
        int i2 = i + 1;
        changeViewPosition(this.mFooterView, getChildCount() - i);
        changeViewPosition(this.mEmptyShadeView, getChildCount() - i2);
        changeViewPosition(this.mShelf, getChildCount() - (i2 + 1));
    }

    public final void onViewAddedInternal(ExpandableView expandableView) {
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        expandableView.setHideSensitiveForIntrinsicHeight(ambientState.mHideSensitive);
        expandableView.mOnHeightChangedListener = this.mOnChildHeightChangedListener;
        boolean z = false;
        generateAddAnimation(expandableView, false);
        if ((this.mAnimationsEnabled || this.mPulsing) && (this.mIsExpanded || isPinnedHeadsUp(expandableView))) {
            z = true;
        }
        boolean z2 = expandableView instanceof ExpandableNotificationRow;
        if (z2) {
            ((ExpandableNotificationRow) expandableView).setIconAnimationRunning(z);
        }
        if (z2) {
            ((ExpandableNotificationRow) expandableView).setChronometerRunning(this.mIsExpanded);
        }
        if (z2) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
            boolean z3 = this.mDismissUsingRowTranslationX;
            if (z3 != expandableNotificationRow.mDismissUsingRowTranslationX) {
                float translation = expandableNotificationRow.getTranslation();
                int i = (translation > 0.0f ? 1 : (translation == 0.0f ? 0 : -1));
                if (i != 0) {
                    expandableNotificationRow.setTranslation(0.0f);
                }
                expandableNotificationRow.mDismissUsingRowTranslationX = z3;
                if (i != 0) {
                    expandableNotificationRow.setTranslation(translation);
                }
            }
        }
    }

    public final void onViewRemovedInternal(ExpandableView expandableView, ViewGroup viewGroup) {
        int i;
        float f;
        if (!this.mChangePositionInProgress) {
            Objects.requireNonNull(expandableView);
            expandableView.mOnHeightChangedListener = null;
            int positionInLinearLayout = getPositionInLinearLayout(expandableView);
            int intrinsicHeight = expandableView.getIntrinsicHeight() + this.mPaddingBetweenElements;
            int i2 = positionInLinearLayout + intrinsicHeight;
            if (this.mShouldUseSplitNotificationShade) {
                i = this.mSidePaddings;
            } else {
                i = this.mTopPadding - this.mQsScrollBoundaryPosition;
            }
            this.mAnimateStackYForContentHeightChange = true;
            int i3 = this.mOwnScrollY;
            int i4 = i3 - i;
            boolean z = false;
            if (i2 <= i4) {
                setOwnScrollY(i3 - intrinsicHeight, false);
            } else if (positionInLinearLayout < i4) {
                setOwnScrollY(positionInLinearLayout + i, false);
            }
            if (!generateRemoveAnimation(expandableView)) {
                this.mSwipedOutViews.remove(expandableView);
            } else if (!this.mSwipedOutViews.contains(expandableView) || !isFullySwipedOut(expandableView)) {
                viewGroup.addTransientView(expandableView, 0);
                expandableView.mTransientContainer = viewGroup;
            }
            boolean z2 = expandableView instanceof ExpandableNotificationRow;
            if (z2) {
                ((ExpandableNotificationRow) expandableView).setIconAnimationRunning(false);
            }
            if (z2) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                if (expandableNotificationRow.mRefocusOnDismiss || expandableNotificationRow.isAccessibilityFocused()) {
                    z = true;
                }
                if (z) {
                    View view = expandableNotificationRow.mChildAfterViewWhenDismissed;
                    if (view == null) {
                        ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow.mGroupParentWhenDismissed;
                        if (expandableNotificationRow2 != null) {
                            f = expandableNotificationRow2.getTranslationY();
                        } else {
                            f = expandableView.getTranslationY();
                        }
                        view = getFirstChildBelowTranlsationY(f, true);
                    }
                    if (view != null) {
                        view.requestAccessibilityFocus();
                    }
                }
            }
        }
    }

    public final void requestChildrenUpdate() {
        if (!this.mChildrenUpdateRequested) {
            getViewTreeObserver().addOnPreDrawListener(this.mChildrenUpdater);
            this.mChildrenUpdateRequested = true;
            invalidate();
        }
    }

    public final void setAnimationRunning(boolean z) {
        if (z != this.mAnimationRunning) {
            if (z) {
                getViewTreeObserver().addOnPreDrawListener(this.mRunningAnimationUpdater);
            } else {
                getViewTreeObserver().removeOnPreDrawListener(this.mRunningAnimationUpdater);
            }
            this.mAnimationRunning = z;
            updateContinuousShadowDrawing();
        }
    }

    public final void setClearAllInProgress(boolean z) {
        this.mClearAllInProgress = z;
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mClearAllInProgress = z;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationRoundnessManager notificationRoundnessManager = notificationStackScrollLayoutController.mNotificationRoundnessManager;
        Objects.requireNonNull(notificationRoundnessManager);
        notificationRoundnessManager.mIsClearAllInProgress = z;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (expandableView.getVisibility() != 8) {
                canChildBeCleared(expandableView);
            }
        }
    }

    @VisibleForTesting
    public void setIsBeingDragged(boolean z) {
        this.mIsBeingDragged = z;
        if (z) {
            requestDisallowInterceptTouchEvent(true);
            cancelLongPress();
            this.mSwipeHelper.resetExposedMenuView(true, true);
        }
    }

    public final void setOverScrollAmount(float f, final boolean z, boolean z2, boolean z3, final boolean z4) {
        if (z3) {
            StackStateAnimator stackStateAnimator = this.mStateAnimator;
            Objects.requireNonNull(stackStateAnimator);
            ValueAnimator valueAnimator = z ? stackStateAnimator.mTopOverScrollAnimator : stackStateAnimator.mBottomOverScrollAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }
        float max = Math.max(0.0f, f);
        if (z2) {
            final StackStateAnimator stackStateAnimator2 = this.mStateAnimator;
            Objects.requireNonNull(stackStateAnimator2);
            float currentOverScrollAmount = stackStateAnimator2.mHostLayout.getCurrentOverScrollAmount(z);
            if (max != currentOverScrollAmount) {
                ValueAnimator valueAnimator2 = z ? stackStateAnimator2.mTopOverScrollAnimator : stackStateAnimator2.mBottomOverScrollAnimator;
                if (valueAnimator2 != null) {
                    valueAnimator2.cancel();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(currentOverScrollAmount, max);
                ofFloat.setDuration(360L);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator.3
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        stackStateAnimator2.mHostLayout.setOverScrollAmount(((Float) valueAnimator3.getAnimatedValue()).floatValue(), z, false, false, z4);
                    }
                });
                ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.stack.StackStateAnimator.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        if (z) {
                            stackStateAnimator2.mTopOverScrollAnimator = null;
                        } else {
                            stackStateAnimator2.mBottomOverScrollAnimator = null;
                        }
                    }
                });
                ofFloat.start();
                if (z) {
                    stackStateAnimator2.mTopOverScrollAnimator = ofFloat;
                } else {
                    stackStateAnimator2.mBottomOverScrollAnimator = ofFloat;
                }
            }
        } else {
            float rubberBandFactor = max / getRubberBandFactor(z);
            if (z) {
                this.mOverScrolledTopPixels = rubberBandFactor;
            } else {
                this.mOverScrolledBottomPixels = rubberBandFactor;
            }
            AmbientState ambientState = this.mAmbientState;
            Objects.requireNonNull(ambientState);
            if (z) {
                ambientState.mOverScrollTopAmount = max;
            } else {
                ambientState.mOverScrollBottomAmount = max;
            }
            if (z) {
                notifyOverscrollTopListener(max, z4);
            }
            updateStackPosition(false);
            requestChildrenUpdate();
        }
    }

    public final void setOwnScrollY(int i, boolean z) {
        int i2 = this.mOwnScrollY;
        if (i != i2) {
            int i3 = ((ViewGroup) this).mScrollX;
            onScrollChanged(i3, i, i3, i2);
            this.mOwnScrollY = i;
            AmbientState ambientState = this.mAmbientState;
            Objects.requireNonNull(ambientState);
            ambientState.mScrollY = Math.max(i, 0);
            Consumer<Integer> consumer = this.mScrollListener;
            if (consumer != null) {
                consumer.accept(Integer.valueOf(this.mOwnScrollY));
            }
            updateForwardAndBackwardScrollability();
            requestChildrenUpdate();
            updateStackPosition(z);
        }
    }

    public final float setPulseHeight(float f) {
        float f2;
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (f != ambientState.mPulseHeight) {
            ambientState.mPulseHeight = f;
            Runnable runnable = ambientState.mOnPulseHeightChangedListener;
            if (runnable != null) {
                runnable.run();
            }
        }
        if (this.mKeyguardBypassEnabled) {
            notifyAppearChangedListeners();
            f2 = Math.max(0.0f, f - this.mIntrinsicPadding);
        } else {
            f2 = Math.max(0.0f, f - this.mAmbientState.getInnerHeight(true));
        }
        requestChildrenUpdate();
        return f2;
    }

    @VisibleForTesting
    public void setStatusBarState(int i) {
        this.mStatusBarState = i;
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mStatusBarState = i;
        this.mSpeedBumpIndexDirty = true;
        updateDismissBehavior();
    }

    public final boolean shouldSkipHeightUpdate() {
        if (this.mAmbientState.isOnKeyguard()) {
            AmbientState ambientState = this.mAmbientState;
            Objects.requireNonNull(ambientState);
            if (!ambientState.mUnlockHintRunning) {
                AmbientState ambientState2 = this.mAmbientState;
                Objects.requireNonNull(ambientState2);
                if (ambientState2.mIsSwipingUp || this.mIsFlinging) {
                }
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:124:0x0272  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0311  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x05ae  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x0602  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x06be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void startAnimationToState$1() {
        /*
            Method dump skipped, instructions count: 2274
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.startAnimationToState$1():void");
    }

    public final void updateAlgorithmHeightAndPadding() {
        AmbientState ambientState = this.mAmbientState;
        int min = Math.min(this.mMaxLayoutHeight, this.mCurrentStackHeight);
        Objects.requireNonNull(ambientState);
        ambientState.mLayoutHeight = min;
        AmbientState ambientState2 = this.mAmbientState;
        int i = this.mMaxLayoutHeight;
        Objects.requireNonNull(ambientState2);
        ambientState2.mLayoutMaxHeight = i;
        updateAlgorithmLayoutMinHeight();
        AmbientState ambientState3 = this.mAmbientState;
        int i2 = this.mTopPadding;
        Objects.requireNonNull(ambientState3);
        ambientState3.mTopPadding = i2;
    }

    public final void updateAlgorithmLayoutMinHeight() {
        int i;
        AmbientState ambientState = this.mAmbientState;
        if (this.mQsExpanded || isHeadsUpTransition()) {
            i = getLayoutMinHeight();
        } else {
            i = 0;
        }
        Objects.requireNonNull(ambientState);
        ambientState.mLayoutMinHeight = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00fc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateBackground() {
        /*
            Method dump skipped, instructions count: 584
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.updateBackground():void");
    }

    public final void updateBackgroundDimming() {
        if (this.mShouldDrawNotificationBackground) {
            int blendARGB = ColorUtils.blendARGB(this.mBgColor, -1, MathUtils.smoothStep(0.4f, 1.0f, this.mLinearHideAmount));
            if (this.mCachedBackgroundColor != blendARGB) {
                this.mCachedBackgroundColor = blendARGB;
                this.mBackgroundPaint.setColor(blendARGB);
                invalidate();
            }
        }
    }

    public final void updateBgColor() {
        this.mBgColor = com.android.settingslib.Utils.getColorAttr(((ViewGroup) this).mContext, 16844002).getDefaultColor();
        updateBackgroundDimming();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ActivatableNotificationView) {
                ((ActivatableNotificationView) childAt).updateBackgroundColors();
            }
        }
    }

    public final void updateClipping() {
        boolean z;
        if (this.mRequestedClipBounds == null || this.mInHeadsUpPinnedMode || this.mHeadsUpAnimatingAway) {
            z = false;
        } else {
            z = true;
        }
        if (this.mIsClipped != z) {
            this.mIsClipped = z;
        }
        if (this.mAmbientState.isHiddenAtAll()) {
            invalidateOutline();
            if (this.mAmbientState.isFullyHidden()) {
                setClipBounds(null);
            }
        } else if (z) {
            setClipBounds(this.mRequestedClipBounds);
        } else {
            setClipBounds(null);
        }
        setClipToOutline(false);
    }

    public final void updateContentHeight() {
        float f;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        float f2;
        float f3;
        if (this.mAmbientState.isOnKeyguard()) {
            f = 0.0f;
        } else {
            f = this.mMinimumPaddings;
        }
        int i = (int) f;
        int i2 = this.mMaxDisplayedNotifications;
        ExpandableView expandableView = null;
        int i3 = 0;
        int i4 = 0;
        boolean z5 = false;
        while (true) {
            z = true;
            if (i3 >= getChildCount()) {
                break;
            }
            ExpandableView expandableView2 = (ExpandableView) getChildAt(i3);
            if (expandableView2 != this.mFooterView || !onKeyguard()) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (expandableView2.getVisibility() != 8 && !(expandableView2 instanceof NotificationShelf) && !z2) {
                if (i2 == -1 || i4 < i2) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                if (z3) {
                    NotificationShelf notificationShelf = this.mShelf;
                    Objects.requireNonNull(notificationShelf);
                    f2 = notificationShelf.getHeight();
                    z4 = true;
                } else {
                    z4 = z5;
                    f2 = expandableView2.getIntrinsicHeight();
                }
                if (i != 0) {
                    i += this.mPaddingBetweenElements;
                }
                StackScrollAlgorithm stackScrollAlgorithm = this.mStackScrollAlgorithm;
                NotificationSectionsManager notificationSectionsManager = this.mSectionsManager;
                Objects.requireNonNull(stackScrollAlgorithm);
                if (StackScrollAlgorithm.childNeedsGapHeight(notificationSectionsManager, i4, expandableView2, expandableView)) {
                    f3 = stackScrollAlgorithm.mGapHeight;
                } else {
                    f3 = 0.0f;
                }
                i = (int) (((int) (i + f3)) + f2);
                if (f2 > 0.0f || !(expandableView2 instanceof MediaContainerView)) {
                    i4++;
                }
                if (z4) {
                    break;
                }
                expandableView = expandableView2;
                z5 = z4;
            }
            i3++;
        }
        this.mIntrinsicContentHeight = i;
        this.mContentHeight = Math.max(this.mIntrinsicPadding, this.mTopPadding) + i + this.mBottomPadding;
        if (this.mQsExpanded || getScrollRange() <= 0) {
            z = false;
        }
        if (z != this.mScrollable) {
            this.mScrollable = z;
            setFocusable(z);
            updateForwardAndBackwardScrollability();
        }
        clampScrollPosition();
        updateStackPosition(false);
        AmbientState ambientState = this.mAmbientState;
        int i5 = this.mContentHeight;
        Objects.requireNonNull(ambientState);
        ambientState.mContentHeight = i5;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001a, code lost:
        if (r0.mIsSwiping != false) goto L_0x001e;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateContinuousBackgroundDrawing() {
        /*
            r3 = this;
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r3.mAmbientState
            java.util.Objects.requireNonNull(r0)
            float r0 = r0.mDozeAmount
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x0010
            r0 = r1
            goto L_0x0011
        L_0x0010:
            r0 = r2
        L_0x0011:
            if (r0 != 0) goto L_0x001d
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper r0 = r3.mSwipeHelper
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mIsSwiping
            if (r0 == 0) goto L_0x001d
            goto L_0x001e
        L_0x001d:
            r1 = r2
        L_0x001e:
            boolean r0 = r3.mContinuousBackgroundUpdate
            if (r1 == r0) goto L_0x0039
            r3.mContinuousBackgroundUpdate = r1
            if (r1 == 0) goto L_0x0030
            android.view.ViewTreeObserver r0 = r3.getViewTreeObserver()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda1 r3 = r3.mBackgroundUpdater
            r0.addOnPreDrawListener(r3)
            goto L_0x0039
        L_0x0030:
            android.view.ViewTreeObserver r0 = r3.getViewTreeObserver()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda1 r3 = r3.mBackgroundUpdater
            r0.removeOnPreDrawListener(r3)
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.updateContinuousBackgroundDrawing():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0015  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateContinuousShadowDrawing() {
        /*
            r3 = this;
            boolean r0 = r3.mAnimationRunning
            if (r0 != 0) goto L_0x0010
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper r0 = r3.mSwipeHelper
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mIsSwiping
            if (r0 == 0) goto L_0x000e
            goto L_0x0010
        L_0x000e:
            r0 = 0
            goto L_0x0011
        L_0x0010:
            r0 = 1
        L_0x0011:
            boolean r1 = r3.mContinuousShadowUpdate
            if (r0 == r1) goto L_0x002c
            if (r0 == 0) goto L_0x0021
            android.view.ViewTreeObserver r1 = r3.getViewTreeObserver()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda0 r2 = r3.mShadowUpdater
            r1.addOnPreDrawListener(r2)
            goto L_0x002a
        L_0x0021:
            android.view.ViewTreeObserver r1 = r3.getViewTreeObserver()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$$ExternalSyntheticLambda0 r2 = r3.mShadowUpdater
            r1.removeOnPreDrawListener(r2)
        L_0x002a:
            r3.mContinuousShadowUpdate = r0
        L_0x002c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.updateContinuousShadowDrawing():void");
    }

    public final void updateDecorViews() {
        int colorAttrDefaultColor = com.android.settingslib.Utils.getColorAttrDefaultColor(((ViewGroup) this).mContext, 16842806);
        NotificationSectionsManager notificationSectionsManager = this.mSectionsManager;
        Objects.requireNonNull(notificationSectionsManager);
        SectionHeaderView peopleHeaderView = notificationSectionsManager.getPeopleHeaderView();
        if (peopleHeaderView != null) {
            peopleHeaderView.mLabelView.setTextColor(colorAttrDefaultColor);
            peopleHeaderView.mClearAllButton.setImageTintList(ColorStateList.valueOf(colorAttrDefaultColor));
        }
        SectionHeaderView silentHeaderView = notificationSectionsManager.getSilentHeaderView();
        if (silentHeaderView != null) {
            silentHeaderView.mLabelView.setTextColor(colorAttrDefaultColor);
            silentHeaderView.mClearAllButton.setImageTintList(ColorStateList.valueOf(colorAttrDefaultColor));
        }
        SectionHeaderView alertingHeaderView = notificationSectionsManager.getAlertingHeaderView();
        if (alertingHeaderView != null) {
            alertingHeaderView.mLabelView.setTextColor(colorAttrDefaultColor);
            alertingHeaderView.mClearAllButton.setImageTintList(ColorStateList.valueOf(colorAttrDefaultColor));
        }
        this.mFooterView.updateColors();
        EmptyShadeView emptyShadeView = this.mEmptyShadeView;
        Objects.requireNonNull(emptyShadeView);
        emptyShadeView.mEmptyText.setTextColor(colorAttrDefaultColor);
    }

    public final void updateDismissBehavior() {
        boolean z = true;
        if (this.mShouldUseSplitNotificationShade && (this.mStatusBarState == 1 || !this.mIsExpanded)) {
            z = false;
        }
        if (this.mDismissUsingRowTranslationX != z) {
            this.mDismissUsingRowTranslationX = z;
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    Objects.requireNonNull(expandableNotificationRow);
                    if (z != expandableNotificationRow.mDismissUsingRowTranslationX) {
                        float translation = expandableNotificationRow.getTranslation();
                        int i2 = (translation > 0.0f ? 1 : (translation == 0.0f ? 0 : -1));
                        if (i2 != 0) {
                            expandableNotificationRow.setTranslation(0.0f);
                        }
                        expandableNotificationRow.mDismissUsingRowTranslationX = z;
                        if (i2 != 0) {
                            expandableNotificationRow.setTranslation(translation);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateFooter() {
        /*
            r7 = this;
            com.android.systemui.statusbar.notification.row.FooterView r0 = r7.mFooterView
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            boolean r0 = r7.mClearAllEnabled
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0018
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r7.mController
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.hasNotifications(r1, r2)
            if (r0 == 0) goto L_0x0018
            r0 = r2
            goto L_0x0019
        L_0x0018:
            r0 = r1
        L_0x0019:
            if (r0 != 0) goto L_0x0023
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r3 = r7.mController
            int r3 = r3.getVisibleNotificationCount()
            if (r3 <= 0) goto L_0x0066
        L_0x0023:
            boolean r3 = r7.mIsCurrentUserSetup
            if (r3 == 0) goto L_0x0066
            int r3 = r7.mStatusBarState
            if (r3 == r2) goto L_0x0066
            float r3 = r7.mQsExpansionFraction
            r4 = 1065353216(0x3f800000, float:1.0)
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 == 0) goto L_0x0066
            com.android.systemui.statusbar.phone.ScreenOffAnimationController r3 = r7.mScreenOffAnimationController
            java.util.Objects.requireNonNull(r3)
            java.util.ArrayList r3 = r3.animations
            boolean r4 = r3 instanceof java.util.Collection
            if (r4 == 0) goto L_0x0045
            boolean r4 = r3.isEmpty()
            if (r4 == 0) goto L_0x0045
            goto L_0x005d
        L_0x0045:
            java.util.Iterator r3 = r3.iterator()
        L_0x0049:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x005d
            java.lang.Object r4 = r3.next()
            com.android.systemui.statusbar.phone.ScreenOffAnimation r4 = (com.android.systemui.statusbar.phone.ScreenOffAnimation) r4
            boolean r4 = r4.isAnimationPlaying()
            if (r4 == 0) goto L_0x0049
            r3 = r2
            goto L_0x005e
        L_0x005d:
            r3 = r1
        L_0x005e:
            if (r3 != 0) goto L_0x0066
            boolean r3 = r7.mIsRemoteInputActive
            if (r3 != 0) goto L_0x0066
            r3 = r2
            goto L_0x0067
        L_0x0066:
            r3 = r1
        L_0x0067:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r4 = r7.mController
            boolean r4 = r4.isHistoryEnabled()
            com.android.systemui.statusbar.notification.row.FooterView r5 = r7.mFooterView
            if (r5 != 0) goto L_0x0072
            goto L_0x0092
        L_0x0072:
            boolean r6 = r7.mIsExpanded
            if (r6 == 0) goto L_0x007b
            boolean r6 = r7.mAnimationsEnabled
            if (r6 == 0) goto L_0x007b
            r1 = r2
        L_0x007b:
            r5.setVisible(r3, r1)
            com.android.systemui.statusbar.notification.row.FooterView r2 = r7.mFooterView
            r2.setSecondaryVisible(r0, r1)
            com.android.systemui.statusbar.notification.row.FooterView r7 = r7.mFooterView
            java.util.Objects.requireNonNull(r7)
            boolean r0 = r7.mShowHistory
            if (r0 != r4) goto L_0x008d
            goto L_0x0092
        L_0x008d:
            r7.mShowHistory = r4
            r7.updateText()
        L_0x0092:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.updateFooter():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateForwardAndBackwardScrollability() {
        /*
            r5 = this;
            boolean r0 = r5.mScrollable
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x001c
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$7 r0 = r5.mScrollAdapter
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.this
            int r3 = r0.mOwnScrollY
            int r0 = r0.getScrollRange()
            if (r3 < r0) goto L_0x0017
            r0 = r2
            goto L_0x0018
        L_0x0017:
            r0 = r1
        L_0x0018:
            if (r0 != 0) goto L_0x001c
            r0 = r2
            goto L_0x001d
        L_0x001c:
            r0 = r1
        L_0x001d:
            boolean r3 = r5.mScrollable
            if (r3 == 0) goto L_0x0033
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout$7 r3 = r5.mScrollAdapter
            java.util.Objects.requireNonNull(r3)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r3 = com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.this
            int r3 = r3.mOwnScrollY
            if (r3 != 0) goto L_0x002e
            r3 = r2
            goto L_0x002f
        L_0x002e:
            r3 = r1
        L_0x002f:
            if (r3 != 0) goto L_0x0033
            r3 = r2
            goto L_0x0034
        L_0x0033:
            r3 = r1
        L_0x0034:
            boolean r4 = r5.mForwardScrollable
            if (r0 != r4) goto L_0x003c
            boolean r4 = r5.mBackwardScrollable
            if (r3 == r4) goto L_0x003d
        L_0x003c:
            r1 = r2
        L_0x003d:
            r5.mForwardScrollable = r0
            r5.mBackwardScrollable = r3
            if (r1 == 0) goto L_0x0048
            r0 = 2048(0x800, float:2.87E-42)
            r5.sendAccessibilityEvent(r0)
        L_0x0048:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.updateForwardAndBackwardScrollability():void");
    }

    public final void updateLaunchedNotificationClipPath() {
        if (this.mLaunchingNotificationNeedsToBeClipped && this.mLaunchingNotification && this.mExpandingNotificationRow != null) {
            ExpandAnimationParameters expandAnimationParameters = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters);
            int min = Math.min(expandAnimationParameters.left, this.mRoundedRectClippingLeft);
            ExpandAnimationParameters expandAnimationParameters2 = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters2);
            int max = Math.max(expandAnimationParameters2.right, this.mRoundedRectClippingRight);
            ExpandAnimationParameters expandAnimationParameters3 = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters3);
            int max2 = Math.max(expandAnimationParameters3.bottom, this.mRoundedRectClippingBottom);
            PathInterpolator pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
            ExpandAnimationParameters expandAnimationParameters4 = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters4);
            PorterDuffXfermode porterDuffXfermode = LaunchAnimator.SRC_MODE;
            float interpolation = pathInterpolator.getInterpolation(LaunchAnimator.Companion.getProgress(ActivityLaunchAnimator.TIMINGS, expandAnimationParameters4.linearProgress, 0L, 100L));
            int i = this.mRoundedRectClippingTop;
            ExpandAnimationParameters expandAnimationParameters5 = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters5);
            ExpandAnimationParameters expandAnimationParameters6 = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters6);
            float f = expandAnimationParameters6.topCornerRadius;
            ExpandAnimationParameters expandAnimationParameters7 = this.mLaunchAnimationParams;
            Objects.requireNonNull(expandAnimationParameters7);
            float f2 = expandAnimationParameters7.bottomCornerRadius;
            float[] fArr = this.mLaunchedNotificationRadii;
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
            fArr[4] = f2;
            fArr[5] = f2;
            fArr[6] = f2;
            fArr[7] = f2;
            this.mLaunchedNotificationClipPath.reset();
            this.mLaunchedNotificationClipPath.addRoundRect(min, (int) Math.min(MathUtils.lerp(i, expandAnimationParameters5.top, interpolation), this.mRoundedRectClippingTop), max, max2, this.mLaunchedNotificationRadii, Path.Direction.CW);
            ExpandableNotificationRow expandableNotificationRow = this.mExpandingNotificationRow;
            Objects.requireNonNull(expandableNotificationRow);
            ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow.mNotificationParent;
            if (expandableNotificationRow2 != null) {
                expandableNotificationRow = expandableNotificationRow2;
            }
            this.mLaunchedNotificationClipPath.offset((-expandableNotificationRow.getLeft()) - expandableNotificationRow.getTranslationX(), (-expandableNotificationRow.getTop()) - expandableNotificationRow.getTranslationY());
            expandableNotificationRow.mExpandingClipPath = this.mLaunchedNotificationClipPath;
            expandableNotificationRow.invalidate();
            if (this.mShouldUseRoundedRectClipping) {
                invalidate();
            }
        }
    }

    public final void updateNotificationAnimationStates() {
        boolean z;
        boolean z2;
        if (this.mAnimationsEnabled || this.mPulsing) {
            z = true;
        } else {
            z = false;
        }
        NotificationShelf notificationShelf = this.mShelf;
        Objects.requireNonNull(notificationShelf);
        notificationShelf.mAnimationsEnabled = z;
        if (!z) {
            notificationShelf.mShelfIcons.setAnimationsEnabled(false);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (this.mIsExpanded || isPinnedHeadsUp(childAt)) {
                z2 = true;
            } else {
                z2 = false;
            }
            z &= z2;
            if (childAt instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) childAt).setIconAnimationRunning(z);
            }
        }
    }

    public final void updateOwnTranslationZ() {
        float f;
        ExpandableView firstChildNotGone;
        if (!this.mKeyguardBypassEnabled || !this.mAmbientState.isHiddenAtAll() || (firstChildNotGone = getFirstChildNotGone()) == null || !firstChildNotGone.showingPulsing()) {
            f = 0.0f;
        } else {
            f = firstChildNotGone.getTranslationZ();
        }
        setTranslationZ(f);
    }

    public final void updateSensitiveness(boolean z, boolean z2) {
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (z2 != ambientState.mHideSensitive) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                ((ExpandableView) getChildAt(i)).setHideSensitiveForIntrinsicHeight(z2);
            }
            AmbientState ambientState2 = this.mAmbientState;
            Objects.requireNonNull(ambientState2);
            ambientState2.mHideSensitive = z2;
            if (z && this.mAnimationsEnabled) {
                this.mHideSensitiveNeedsAnimation = true;
                this.mNeedsAnimation = true;
            }
            updateContentHeight();
            requestChildrenUpdate();
        }
    }

    public final void updateStackPosition(boolean z) {
        float f = this.mTopPadding + this.mExtraTopInsetForFullShadeTransition;
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        float currentOverScrollAmount = (f + ambientState.mOverExpansion) - getCurrentOverScrollAmount(false);
        AmbientState ambientState2 = this.mAmbientState;
        Objects.requireNonNull(ambientState2);
        float f2 = ambientState2.mExpansionFraction;
        float lerp = MathUtils.lerp(0.0f, currentOverScrollAmount, f2);
        AmbientState ambientState3 = this.mAmbientState;
        Objects.requireNonNull(ambientState3);
        ambientState3.mStackY = lerp;
        Consumer<Boolean> consumer = this.mOnStackYChanged;
        if (consumer != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        if (this.mQsExpansionFraction <= 0.0f && !shouldSkipHeightUpdate()) {
            float max = Math.max(0.0f, (getHeight() - getEmptyBottomMargin()) - this.mTopPadding);
            AmbientState ambientState4 = this.mAmbientState;
            Objects.requireNonNull(ambientState4);
            ambientState4.mStackEndHeight = max;
            AmbientState ambientState5 = this.mAmbientState;
            Objects.requireNonNull(ambientState5);
            float f3 = ambientState5.mDozeAmount;
            if (0.0f < f3 && f3 < 1.0f) {
                f2 = 1.0f - f3;
            }
            AmbientState ambientState6 = this.mAmbientState;
            float lerp2 = MathUtils.lerp(0.5f * max, max, f2);
            Objects.requireNonNull(ambientState6);
            ambientState6.mStackHeight = lerp2;
        }
    }

    public final void updateUseRoundedRectClipping() {
        boolean z;
        boolean z2 = false;
        if (this.mQsExpansionFraction < 0.5f || this.mShouldUseSplitNotificationShade) {
            z = true;
        } else {
            z = false;
        }
        if (this.mIsExpanded && z) {
            z2 = true;
        }
        if (z2 != this.mShouldUseRoundedRectClipping) {
            this.mShouldUseRoundedRectClipping = z2;
            invalidate();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001a, code lost:
        if (r4.mBucket == 6) goto L_0x0038;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0033, code lost:
        if (r4.mBucket < 6) goto L_0x0038;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:
        r4 = false;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean includeChildInClearAll(com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4, int r5) {
        /*
            boolean r0 = canChildBeCleared(r4)
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x003c
            if (r5 == 0) goto L_0x0038
            r0 = 6
            if (r5 == r1) goto L_0x0029
            r3 = 2
            if (r5 != r3) goto L_0x001d
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = r4.mEntry
            java.util.Objects.requireNonNull(r4)
            int r4 = r4.mBucket
            if (r4 != r0) goto L_0x0036
            goto L_0x0038
        L_0x001d:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "Unknown selection: "
            java.lang.String r5 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0.m(r0, r5)
            r4.<init>(r5)
            throw r4
        L_0x0029:
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = r4.mEntry
            java.util.Objects.requireNonNull(r4)
            int r4 = r4.mBucket
            if (r4 >= r0) goto L_0x0036
            goto L_0x0038
        L_0x0036:
            r4 = r2
            goto L_0x0039
        L_0x0038:
            r4 = r1
        L_0x0039:
            if (r4 == 0) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r1 = r2
        L_0x003d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.includeChildInClearAll(com.android.systemui.statusbar.notification.row.ExpandableNotificationRow, int):boolean");
    }

    public final void applyCurrentState() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            Objects.requireNonNull(expandableView);
            ExpandableViewState expandableViewState = expandableView.mViewState;
            if (!expandableViewState.gone) {
                expandableViewState.applyToView(expandableView);
            }
        }
        NotificationLogger.OnChildLocationsChangedListener onChildLocationsChangedListener = this.mListener;
        if (onChildLocationsChangedListener != null) {
            NotificationLogger.AnonymousClass1 r0 = (NotificationLogger.AnonymousClass1) onChildLocationsChangedListener;
            NotificationLogger notificationLogger = NotificationLogger.this;
            if (!notificationLogger.mHandler.hasCallbacks(notificationLogger.mVisibilityReporter)) {
                NotificationLogger notificationLogger2 = NotificationLogger.this;
                notificationLogger2.mHandler.postAtTime(notificationLogger2.mVisibilityReporter, notificationLogger2.mLastVisibilityReportUptimeMs + 500);
            }
        }
        Iterator<Runnable> it = this.mAnimationFinishedRunnables.iterator();
        while (it.hasNext()) {
            it.next().run();
        }
        this.mAnimationFinishedRunnables.clear();
        setAnimationRunning(false);
        updateBackground();
        updateViewShadows();
    }

    public final void changeViewPosition(ExpandableView expandableView, int i) {
        String str;
        Assert.isMainThread();
        if (!this.mChangePositionInProgress) {
            int indexOfChild = indexOfChild(expandableView);
            boolean z = false;
            if (indexOfChild == -1) {
                if (expandableView instanceof ExpandableNotificationRow) {
                    Objects.requireNonNull(expandableView);
                    if (expandableView.mTransientContainer != null) {
                        z = true;
                    }
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Attempting to re-position ");
                if (z) {
                    str = "transient";
                } else {
                    str = "";
                }
                m.append(str);
                m.append(" view {");
                m.append(expandableView);
                m.append("}");
                Log.e("StackScroller", m.toString());
            } else if (expandableView != null && expandableView.getParent() == this && indexOfChild != i) {
                this.mChangePositionInProgress = true;
                expandableView.mChangingPosition = true;
                removeView(expandableView);
                addView(expandableView, i);
                expandableView.mChangingPosition = false;
                this.mChangePositionInProgress = false;
                if (this.mIsExpanded && this.mAnimationsEnabled && expandableView.getVisibility() != 8) {
                    this.mChildrenChangingPositions.add(expandableView);
                    this.mNeedsAnimation = true;
                }
            }
        } else {
            throw new IllegalStateException("Reentrant call to changeViewPosition");
        }
    }

    public final void clampScrollPosition() {
        int i;
        int scrollRange = getScrollRange();
        if (scrollRange < this.mOwnScrollY) {
            AmbientState ambientState = this.mAmbientState;
            Objects.requireNonNull(ambientState);
            if (!ambientState.mClearAllInProgress) {
                boolean z = false;
                if (this.mShouldUseSplitNotificationShade) {
                    i = this.mSidePaddings;
                } else {
                    i = this.mTopPadding - this.mQsScrollBoundaryPosition;
                }
                if (scrollRange < i && this.mAnimateStackYForContentHeightChange) {
                    z = true;
                }
                setOwnScrollY(scrollRange, z);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void clearChildFocus(View view) {
        super.clearChildFocus(view);
        if (this.mForcedScroll == view) {
            this.mForcedScroll = null;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        IndentingPrintWriter asIndenting = R$id.asIndenting(printWriter);
        StringBuilder sb = new StringBuilder("[");
        sb.append(getClass().getSimpleName());
        sb.append(":");
        sb.append(" pulsing=");
        String str6 = "T";
        if (this.mPulsing) {
            str = str6;
        } else {
            str = "f";
        }
        sb.append(str);
        sb.append(" expanded=");
        if (this.mIsExpanded) {
            str2 = str6;
        } else {
            str2 = "f";
        }
        sb.append(str2);
        sb.append(" headsUpPinned=");
        if (this.mInHeadsUpPinnedMode) {
            str3 = str6;
        } else {
            str3 = "f";
        }
        sb.append(str3);
        sb.append(" qsClipping=");
        if (this.mShouldUseRoundedRectClipping) {
            str4 = str6;
        } else {
            str4 = "f";
        }
        sb.append(str4);
        sb.append(" qsClipDismiss=");
        if (this.mDismissUsingRowTranslationX) {
            str5 = str6;
        } else {
            str5 = "f";
        }
        sb.append(str5);
        sb.append(" visibility=");
        sb.append(R$id.visibilityString(getVisibility()));
        sb.append(" alpha=");
        sb.append(getAlpha());
        sb.append(" scrollY=");
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        sb.append(ambientState.mScrollY);
        sb.append(" maxTopPadding=");
        sb.append(this.mMaxTopPadding);
        sb.append(" showShelfOnly=");
        if (!this.mShouldShowShelfOnly) {
            str6 = "f";
        }
        sb.append(str6);
        sb.append(" qsExpandFraction=");
        sb.append(this.mQsExpansionFraction);
        sb.append(" isCurrentUserSetup=");
        sb.append(this.mIsCurrentUserSetup);
        sb.append(" hideAmount=");
        AmbientState ambientState2 = this.mAmbientState;
        Objects.requireNonNull(ambientState2);
        sb.append(ambientState2.mHideAmount);
        sb.append("]");
        asIndenting.println(sb.toString());
        R$id.withIncreasedIndent(asIndenting, new DefaultTransitionHandler$$ExternalSyntheticLambda1(this, asIndenting, fileDescriptor, strArr, 1));
    }

    public final float getAppearStartPosition() {
        int minExpansionHeight;
        int i;
        if (isHeadsUpTransition()) {
            NotificationSection firstVisibleSection = getFirstVisibleSection();
            if (firstVisibleSection != null) {
                i = firstVisibleSection.mFirstVisibleChild.getPinnedHeadsUpHeight();
            } else {
                i = 0;
            }
            minExpansionHeight = this.mHeadsUpInset + i;
        } else {
            minExpansionHeight = getMinExpansionHeight();
        }
        return minExpansionHeight;
    }

    public final ExpandableView getChildAtPosition(float f, float f2, boolean z, boolean z2) {
        ExpandableNotificationRow expandableNotificationRow;
        float translationY;
        ExpandableNotificationRow expandableNotificationRow2;
        int childCount = getChildCount();
        int i = 0;
        int i2 = 0;
        while (true) {
            expandableNotificationRow = null;
            if (i2 >= childCount) {
                return null;
            }
            ExpandableView expandableView = (ExpandableView) getChildAt(i2);
            if (expandableView.getVisibility() == 0 && (!z2 || !(expandableView instanceof StackScrollerDecorView))) {
                translationY = expandableView.getTranslationY();
                float f3 = expandableView.mClipTopAmount + translationY;
                float f4 = (expandableView.mActualHeight + translationY) - expandableView.mClipBottomAmount;
                int width = getWidth();
                if ((f4 - f3 >= this.mMinInteractionHeight || !z) && f2 >= f3 && f2 <= f4 && f >= 0 && f <= width) {
                    if (expandableView instanceof ExpandableNotificationRow) {
                        expandableNotificationRow2 = (ExpandableNotificationRow) expandableView;
                        NotificationEntry notificationEntry = expandableNotificationRow2.mEntry;
                        if (this.mIsExpanded || !expandableNotificationRow2.mIsHeadsUp || !expandableNotificationRow2.mIsPinned) {
                            break;
                        }
                        NotificationEntry notificationEntry2 = this.mTopHeadsUpEntry;
                        Objects.requireNonNull(notificationEntry2);
                        if (notificationEntry2.row == expandableNotificationRow2 || this.mGroupMembershipManager.getGroupSummary(this.mTopHeadsUpEntry) == notificationEntry) {
                            break;
                        }
                    } else {
                        return expandableView;
                    }
                }
            }
            i2++;
        }
        float f5 = f2 - translationY;
        if (!expandableNotificationRow2.mIsSummaryWithChildren || !expandableNotificationRow2.mChildrenExpanded) {
            return expandableNotificationRow2;
        }
        NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow2.mChildrenContainer;
        Objects.requireNonNull(notificationChildrenContainer);
        int size = notificationChildrenContainer.mAttachedChildren.size();
        while (true) {
            if (i >= size) {
                break;
            }
            ExpandableNotificationRow expandableNotificationRow3 = (ExpandableNotificationRow) notificationChildrenContainer.mAttachedChildren.get(i);
            float translationY2 = expandableNotificationRow3.getTranslationY();
            float f6 = expandableNotificationRow3.mClipTopAmount + translationY2;
            float f7 = translationY2 + expandableNotificationRow3.mActualHeight;
            if (f5 >= f6 && f5 <= f7) {
                expandableNotificationRow = expandableNotificationRow3;
                break;
            }
            i++;
        }
        if (expandableNotificationRow == null) {
            return expandableNotificationRow2;
        }
        return expandableNotificationRow;
    }

    public final View getFirstChildBelowTranlsationY(float f, boolean z) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                float translationY = childAt.getTranslationY();
                if (translationY >= f) {
                    return childAt;
                }
                if (!z && (childAt instanceof ExpandableNotificationRow)) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) childAt;
                    if (expandableNotificationRow.mIsSummaryWithChildren && expandableNotificationRow.mChildrenExpanded) {
                        ArrayList attachedChildren = expandableNotificationRow.getAttachedChildren();
                        for (int i2 = 0; i2 < attachedChildren.size(); i2++) {
                            ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) attachedChildren.get(i2);
                            if (expandableNotificationRow2.getTranslationY() + translationY >= f) {
                                return expandableNotificationRow2;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public final ExpandableView getFirstChildNotGone() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (!(childAt.getVisibility() == 8 || childAt == this.mShelf)) {
                return (ExpandableView) childAt;
            }
        }
        return null;
    }

    public final ExpandableView getFirstChildWithBackground() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ExpandableView expandableView = (ExpandableView) getChildAt(i);
            if (!(expandableView.getVisibility() == 8 || (expandableView instanceof StackScrollerDecorView) || expandableView == this.mShelf)) {
                return expandableView;
            }
        }
        return null;
    }

    public final int getLayoutMinHeight() {
        if (isHeadsUpTransition()) {
            ExpandableNotificationRow trackedHeadsUpRow = this.mAmbientState.getTrackedHeadsUpRow();
            if (!trackedHeadsUpRow.isAboveShelf()) {
                return getTopHeadsUpPinnedHeight();
            }
            int positionInLinearLayout = getPositionInLinearLayout(trackedHeadsUpRow);
            AmbientState ambientState = this.mAmbientState;
            Objects.requireNonNull(ambientState);
            return getTopHeadsUpPinnedHeight() + ((int) MathUtils.lerp(0, positionInLinearLayout, ambientState.mAppearFraction));
        } else if (this.mShelf.getVisibility() == 8) {
            return 0;
        } else {
            NotificationShelf notificationShelf = this.mShelf;
            Objects.requireNonNull(notificationShelf);
            return notificationShelf.getHeight();
        }
    }

    public final float getRubberBandFactor(boolean z) {
        if (!z) {
            return 0.35f;
        }
        if (this.mExpandedInThisMotion) {
            return 0.15f;
        }
        if (this.mIsExpansionChanging || this.mPanelTracking) {
            return 0.21f;
        }
        return this.mScrolledToTopOnFirstDown ? 1.0f : 0.35f;
    }

    public final float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return this.mTouchSlop * this.mSlopMultiplier;
        }
        return this.mTouchSlop;
    }

    public final void handleEmptySpaceClick(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                float touchSlop = getTouchSlop(motionEvent);
                if (!this.mTouchIsClick) {
                    return;
                }
                if (Math.abs(motionEvent.getY() - this.mInitialTouchY) > touchSlop || Math.abs(motionEvent.getX() - this.mInitialTouchX) > touchSlop) {
                    this.mTouchIsClick = false;
                }
            }
        } else if (this.mStatusBarState != 1 && this.mTouchIsClick && isBelowLastNotification(this.mInitialTouchX, this.mInitialTouchY)) {
            NotificationPanelViewController.OnEmptySpaceClickListener onEmptySpaceClickListener = (NotificationPanelViewController.OnEmptySpaceClickListener) this.mOnEmptySpaceClickListener;
            Objects.requireNonNull(onEmptySpaceClickListener);
            NotificationPanelViewController.this.onEmptySpaceClick();
        }
    }

    public final boolean isBelowLastNotification(float f, float f2) {
        boolean z;
        boolean z2;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ExpandableView expandableView = (ExpandableView) getChildAt(childCount);
            if (expandableView.getVisibility() != 8) {
                float y = expandableView.getY();
                if (y > f2) {
                    return false;
                }
                if (f2 > (expandableView.mActualHeight + y) - expandableView.mClipBottomAmount) {
                    z = true;
                } else {
                    z = false;
                }
                FooterView footerView = this.mFooterView;
                if (expandableView == footerView) {
                    if (!z) {
                        float x = f - footerView.getX();
                        float f3 = f2 - y;
                        Objects.requireNonNull(footerView);
                        if (x < footerView.mContent.getX() || x > footerView.mContent.getX() + footerView.mContent.getWidth() || f3 < footerView.mContent.getY() || f3 > footerView.mContent.getY() + footerView.mContent.getHeight()) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (!z2) {
                            return false;
                        }
                    } else {
                        continue;
                    }
                } else if (expandableView == this.mEmptyShadeView) {
                    return true;
                } else {
                    if (!z) {
                        return false;
                    }
                }
            }
        }
        if (f2 > this.mTopPadding + this.mStackTranslation) {
            return true;
        }
        return false;
    }

    public final boolean isFullySwipedOut(ExpandableView expandableView) {
        float f;
        float abs = Math.abs(expandableView.getTranslation());
        if (!this.mDismissUsingRowTranslationX) {
            f = expandableView.getMeasuredWidth();
        } else {
            float measuredWidth = getMeasuredWidth();
            f = measuredWidth - ((measuredWidth - expandableView.getMeasuredWidth()) / 2.0f);
        }
        if (abs >= Math.abs(f)) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mBottomInset = windowInsets.getSystemWindowInsetBottom();
        this.mWaterfallTopInset = 0;
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        if (displayCutout != null) {
            this.mWaterfallTopInset = displayCutout.getWaterfallInsets().top;
        }
        if (this.mOwnScrollY > getScrollRange()) {
            removeCallbacks(this.mReclamp);
            postDelayed(this.mReclamp, 50L);
        } else {
            View view = this.mForcedScroll;
            if (view != null) {
                scrollTo(view);
            }
        }
        return windowInsets;
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Resources resources = getResources();
        boolean shouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(getResources());
        if (shouldUseSplitNotificationShade != this.mShouldUseSplitNotificationShade) {
            this.mShouldUseSplitNotificationShade = shouldUseSplitNotificationShade;
            updateDismissBehavior();
            updateUseRoundedRectClipping();
        }
        this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(((ViewGroup) this).mContext);
        float f = resources.getDisplayMetrics().density;
        NotificationSwipeHelper notificationSwipeHelper = this.mSwipeHelper;
        Objects.requireNonNull(notificationSwipeHelper);
        notificationSwipeHelper.mDensityScale = f;
        NotificationSwipeHelper notificationSwipeHelper2 = this.mSwipeHelper;
        Objects.requireNonNull(notificationSwipeHelper2);
        notificationSwipeHelper2.mPagingTouchSlop = ViewConfiguration.get(getContext()).getScaledPagingTouchSlop();
        initView(getContext(), this.mSwipeHelper);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x005a, code lost:
        if (r6.getTranslation(r7) != 0.0f) goto L_0x005c;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onEntryUpdated(com.android.systemui.statusbar.notification.collection.NotificationEntry r7) {
        /*
            r6 = this;
            boolean r0 = r7.rowExists()
            if (r0 == 0) goto L_0x0077
            android.service.notification.StatusBarNotification r0 = r7.mSbn
            boolean r0 = r0.isClearable()
            if (r0 != 0) goto L_0x0077
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r7 = r7.row
            boolean r0 = r6.mIsExpanded
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x001f
            boolean r0 = isPinnedHeadsUp(r7)
            if (r0 == 0) goto L_0x001d
            goto L_0x001f
        L_0x001d:
            r0 = r1
            goto L_0x0020
        L_0x001f:
            r0 = r2
        L_0x0020:
            java.util.Objects.requireNonNull(r7)
            com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin r3 = r7.mMenuRow
            if (r3 == 0) goto L_0x0077
            boolean r3 = r3.isMenuVisible()
            r4 = 0
            if (r3 == 0) goto L_0x0033
            float r3 = r7.getTranslation()
            goto L_0x0034
        L_0x0033:
            r3 = r4
        L_0x0034:
            com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper r6 = r6.mSwipeHelper
            java.util.Objects.requireNonNull(r6)
            boolean r5 = r6.mIsSwiping
            if (r5 == 0) goto L_0x0041
            com.android.systemui.statusbar.notification.row.ExpandableView r5 = r6.mTouchedView
            if (r5 == r7) goto L_0x0077
        L_0x0041:
            boolean r5 = r6.mSnappingChild
            if (r5 == 0) goto L_0x0046
            goto L_0x0077
        L_0x0046:
            android.util.ArrayMap<android.view.View, android.animation.Animator> r5 = r6.mDismissPendingMap
            java.lang.Object r5 = r5.get(r7)
            android.animation.Animator r5 = (android.animation.Animator) r5
            if (r5 == 0) goto L_0x0054
            r5.cancel()
            goto L_0x005c
        L_0x0054:
            float r5 = r6.getTranslation(r7)
            int r5 = (r5 > r4 ? 1 : (r5 == r4 ? 0 : -1))
            if (r5 == 0) goto L_0x005d
        L_0x005c:
            r1 = r2
        L_0x005d:
            if (r1 == 0) goto L_0x0077
            if (r0 == 0) goto L_0x0065
            r6.snapChild(r7, r3, r4)
            goto L_0x0077
        L_0x0065:
            com.android.systemui.SwipeHelper$Callback r0 = r6.mCallback
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController$7 r0 = (com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController.AnonymousClass7) r0
            boolean r0 = r0.canChildBeDismissed(r7)
            r7.setTranslation(r4)
            float r1 = r6.getTranslation(r7)
            r6.updateSwipeProgressFromOffset(r7, r0, r1)
        L_0x0077:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.onEntryUpdated(com.android.systemui.statusbar.notification.collection.NotificationEntry):void");
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        inflateEmptyShadeView();
        inflateFooterView();
    }

    public final void onInitializeAccessibilityEventInternal(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEventInternal(accessibilityEvent);
        accessibilityEvent.setScrollable(this.mScrollable);
        accessibilityEvent.setMaxScrollX(((ViewGroup) this).mScrollX);
        accessibilityEvent.setScrollY(this.mOwnScrollY);
        accessibilityEvent.setMaxScrollY(getScrollRange());
    }

    public final void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfoInternal(accessibilityNodeInfo);
        if (this.mScrollable) {
            accessibilityNodeInfo.setScrollable(true);
            if (this.mBackwardScrollable) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
            }
            if (this.mForwardScrollable) {
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
            }
        }
        accessibilityNodeInfo.setClassName(ScrollView.class.getName());
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View childAt;
        float width = getWidth() / 2.0f;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            float measuredWidth = childAt.getMeasuredWidth() / 2.0f;
            getChildAt(i5).layout((int) (width - measuredWidth), 0, (int) (measuredWidth + width), childAt.getMeasuredHeight());
        }
        this.mMaxLayoutHeight = getHeight();
        updateAlgorithmHeightAndPadding();
        updateContentHeight();
        clampScrollPosition();
        requestChildrenUpdate();
        updateFirstAndLastBackgroundViews();
        updateAlgorithmLayoutMinHeight();
        updateOwnTranslationZ();
        this.mAnimateStackYForContentHeightChange = false;
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        if (size == 0 || !this.mSkinnyNotifsInLandscape) {
            this.mSidePaddings = this.mMinimumPaddings;
        } else if (getResources().getConfiguration().orientation == 1) {
            this.mSidePaddings = this.mMinimumPaddings;
        } else {
            int i3 = this.mMinimumPaddings;
            int i4 = this.mQsTilePadding;
            this.mSidePaddings = (((size - (i3 * 2)) - (i4 * 3)) / 4) + i3 + i4;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size - (this.mSidePaddings * 2), View.MeasureSpec.getMode(i));
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 0);
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            measureChild(getChildAt(i5), makeMeasureSpec, makeMeasureSpec2);
        }
    }

    public final void onSecondaryPointerUp(MotionEvent motionEvent) {
        int i;
        int action = (motionEvent.getAction() & 65280) >> 8;
        if (motionEvent.getPointerId(action) == this.mActivePointerId) {
            if (action == 0) {
                i = 1;
            } else {
                i = 0;
            }
            this.mLastMotionY = (int) motionEvent.getY(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    @Override // android.view.ViewGroup
    public final void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof ExpandableView) {
            onViewAddedInternal((ExpandableView) view);
        }
    }

    @Override // android.view.ViewGroup
    public final void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (!this.mChildTransferInProgress) {
            onViewRemovedInternal((ExpandableView) view, this);
        }
    }

    @Override // android.view.View
    public final void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z) {
            cancelLongPress();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0021, code lost:
        if (r5 != 16908346) goto L_0x005c;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean performAccessibilityActionInternal(int r5, android.os.Bundle r6) {
        /*
            r4 = this;
            boolean r6 = super.performAccessibilityActionInternal(r5, r6)
            r0 = 1
            if (r6 == 0) goto L_0x0008
            return r0
        L_0x0008:
            boolean r6 = r4.isEnabled()
            r1 = 0
            if (r6 != 0) goto L_0x0010
            return r1
        L_0x0010:
            r6 = -1
            r2 = 4096(0x1000, float:5.74E-42)
            if (r5 == r2) goto L_0x0024
            r2 = 8192(0x2000, float:1.14794E-41)
            if (r5 == r2) goto L_0x0025
            r2 = 16908344(0x1020038, float:2.3877386E-38)
            if (r5 == r2) goto L_0x0025
            r6 = 16908346(0x102003a, float:2.3877392E-38)
            if (r5 == r6) goto L_0x0024
            goto L_0x005c
        L_0x0024:
            r6 = r0
        L_0x0025:
            int r5 = r4.getHeight()
            int r2 = r4.mPaddingBottom
            int r5 = r5 - r2
            int r2 = r4.mTopPadding
            int r5 = r5 - r2
            int r2 = r4.mPaddingTop
            int r5 = r5 - r2
            com.android.systemui.statusbar.NotificationShelf r2 = r4.mShelf
            java.util.Objects.requireNonNull(r2)
            int r2 = r2.getHeight()
            int r5 = r5 - r2
            int r2 = r4.mOwnScrollY
            int r6 = r6 * r5
            int r6 = r6 + r2
            int r5 = r4.getScrollRange()
            int r5 = java.lang.Math.min(r6, r5)
            int r5 = java.lang.Math.max(r1, r5)
            int r6 = r4.mOwnScrollY
            if (r5 == r6) goto L_0x005c
            android.widget.OverScroller r2 = r4.mScroller
            int r3 = r4.mScrollX
            int r5 = r5 - r6
            r2.startScroll(r3, r6, r1, r5)
            r4.animateScroll()
            return r0
        L_0x005c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.performAccessibilityActionInternal(int, android.os.Bundle):boolean");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
        if (z) {
            cancelLongPress();
        }
    }

    public final void setDimmed(boolean z, boolean z2) {
        boolean onKeyguard = z & onKeyguard();
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mDimmed = onKeyguard;
        float f = 1.0f;
        if (!z2 || !this.mAnimationsEnabled) {
            if (!onKeyguard) {
                f = 0.0f;
            }
            this.mDimAmount = f;
            updateBackgroundDimming();
        } else {
            this.mDimmedNeedsAnimation = true;
            this.mNeedsAnimation = true;
            ValueAnimator valueAnimator = this.mDimAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (!onKeyguard) {
                f = 0.0f;
            }
            float f2 = this.mDimAmount;
            if (f != f2) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(f2, f);
                this.mDimAnimator = ofFloat;
                ofFloat.setDuration(220L);
                this.mDimAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                this.mDimAnimator.addListener(this.mDimEndListener);
                this.mDimAnimator.addUpdateListener(this.mDimUpdateListener);
                this.mDimAnimator.start();
            }
        }
        requestChildrenUpdate();
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x0187  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setExpandedHeight(float r10) {
        /*
            Method dump skipped, instructions count: 407
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout.setExpandedHeight(float):void");
    }

    public final int targetScrollForView(ExpandableView expandableView, int i) {
        int i2;
        int max = (Math.max(0, this.mBottomInset - (getRootView().getHeight() - getHeight())) + (expandableView.getIntrinsicHeight() + i)) - getHeight();
        if (this.mIsExpanded || !isPinnedHeadsUp(expandableView)) {
            i2 = this.mTopPadding;
        } else {
            i2 = this.mHeadsUpInset;
        }
        return max + i2;
    }

    public final void updateFirstAndLastBackgroundViews() {
        ExpandableView expandableView;
        ExpandableView expandableView2;
        boolean z;
        boolean z2;
        NotificationSection firstVisibleSection = getFirstVisibleSection();
        NotificationSection lastVisibleSection = getLastVisibleSection();
        ExpandableView expandableView3 = null;
        if (firstVisibleSection == null) {
            expandableView = null;
        } else {
            expandableView = firstVisibleSection.mFirstVisibleChild;
        }
        if (lastVisibleSection == null) {
            expandableView2 = null;
        } else {
            expandableView2 = lastVisibleSection.mLastVisibleChild;
        }
        ExpandableView firstChildWithBackground = getFirstChildWithBackground();
        int childCount = getChildCount();
        while (true) {
            childCount--;
            if (childCount < 0) {
                break;
            }
            ExpandableView expandableView4 = (ExpandableView) getChildAt(childCount);
            if (!(expandableView4.getVisibility() == 8 || (expandableView4 instanceof StackScrollerDecorView) || expandableView4 == this.mShelf)) {
                expandableView3 = expandableView4;
                break;
            }
        }
        boolean updateFirstAndLastViewsForAllSections = this.mSectionsManager.updateFirstAndLastViewsForAllSections(this.mSections, getChildrenWithBackground());
        if (!this.mAnimationsEnabled || !this.mIsExpanded) {
            this.mAnimateNextBackgroundTop = false;
            this.mAnimateNextBackgroundBottom = false;
            this.mAnimateNextSectionBoundsChange = false;
        } else {
            if (firstChildWithBackground != expandableView) {
                z = true;
            } else {
                z = false;
            }
            this.mAnimateNextBackgroundTop = z;
            if (expandableView3 != expandableView2 || this.mAnimateBottomOnLayout) {
                z2 = true;
            } else {
                z2 = false;
            }
            this.mAnimateNextBackgroundBottom = z2;
            this.mAnimateNextSectionBoundsChange = updateFirstAndLastViewsForAllSections;
        }
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mLastVisibleBackgroundChild = expandableView3;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationRoundnessManager notificationRoundnessManager = notificationStackScrollLayoutController.mNotificationRoundnessManager;
        NotificationSection[] notificationSectionArr = this.mSections;
        Objects.requireNonNull(notificationRoundnessManager);
        for (int i = 0; i < notificationSectionArr.length; i++) {
            ExpandableView[] expandableViewArr = notificationRoundnessManager.mTmpFirstInSectionViews;
            ExpandableView[] expandableViewArr2 = notificationRoundnessManager.mFirstInSectionViews;
            expandableViewArr[i] = expandableViewArr2[i];
            notificationRoundnessManager.mTmpLastInSectionViews[i] = notificationRoundnessManager.mLastInSectionViews[i];
            NotificationSection notificationSection = notificationSectionArr[i];
            Objects.requireNonNull(notificationSection);
            expandableViewArr2[i] = notificationSection.mFirstVisibleChild;
            ExpandableView[] expandableViewArr3 = notificationRoundnessManager.mLastInSectionViews;
            NotificationSection notificationSection2 = notificationSectionArr[i];
            Objects.requireNonNull(notificationSection2);
            expandableViewArr3[i] = notificationSection2.mLastVisibleChild;
        }
        if (notificationRoundnessManager.handleAddedNewViews(notificationSectionArr, notificationRoundnessManager.mTmpLastInSectionViews, false) || (((notificationRoundnessManager.handleRemovedOldViews(notificationSectionArr, notificationRoundnessManager.mTmpFirstInSectionViews, true) | false) | notificationRoundnessManager.handleRemovedOldViews(notificationSectionArr, notificationRoundnessManager.mTmpLastInSectionViews, false)) || notificationRoundnessManager.handleAddedNewViews(notificationSectionArr, notificationRoundnessManager.mTmpFirstInSectionViews, true))) {
            notificationRoundnessManager.mRoundingChangedCallback.run();
        }
        this.mAnimateBottomOnLayout = false;
        invalidate();
    }
}
