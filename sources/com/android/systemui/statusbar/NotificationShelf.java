package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.NotificationBackgroundView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationShelf extends ActivatableNotificationView implements View.OnLayoutChangeListener, StatusBarStateController.StateListener {
    public static final PathInterpolator ICON_ALPHA_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.6f, 0.0f);
    public AmbientState mAmbientState;
    public NotificationIconContainer mCollapsedIcons;
    public NotificationShelfController mController;
    public float mCornerAnimationDistance;
    public float mFirstElementRoundness;
    public float mFractionToShade;
    public boolean mHasItemsInStableShelf;
    public boolean mHideBackground;
    public NotificationStackScrollLayoutController mHostLayoutController;
    public boolean mInteractive;
    public int mNotGoneIndex;
    public int mPaddingBetweenElements;
    public int mScrollFastThreshold;
    public NotificationIconContainer mShelfIcons;
    public boolean mShowNotificationShelf;
    public int mStatusBarState;
    public int[] mTmp = new int[2];
    public boolean mAnimationsEnabled = true;
    public Rect mClipRect = new Rect();
    public int mIndexOfFirstViewInShelf = -1;
    public float mActualWidth = -1.0f;

    /* loaded from: classes.dex */
    public class ShelfState extends ExpandableViewState {
        public ExpandableView firstViewInShelf;
        public boolean hasItemsInStableShelf;

        public ShelfState() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public final void animateTo(View view, AnimationProperties animationProperties) {
            if (NotificationShelf.this.mShowNotificationShelf) {
                super.animateTo(view, animationProperties);
                NotificationShelf notificationShelf = NotificationShelf.this;
                ExpandableView expandableView = this.firstViewInShelf;
                Objects.requireNonNull(notificationShelf);
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationShelf.mHostLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationShelf.mIndexOfFirstViewInShelf = notificationStackScrollLayoutController.mView.indexOfChild(expandableView);
                NotificationShelf.this.updateAppearance();
                NotificationShelf notificationShelf2 = NotificationShelf.this;
                boolean z = this.hasItemsInStableShelf;
                Objects.requireNonNull(notificationShelf2);
                if (notificationShelf2.mHasItemsInStableShelf != z) {
                    notificationShelf2.mHasItemsInStableShelf = z;
                    notificationShelf2.updateInteractiveness();
                }
                NotificationShelf notificationShelf3 = NotificationShelf.this;
                notificationShelf3.mShelfIcons.setAnimationsEnabled(notificationShelf3.mAnimationsEnabled);
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public final void applyToView(View view) {
            if (NotificationShelf.this.mShowNotificationShelf) {
                super.applyToView(view);
                NotificationShelf notificationShelf = NotificationShelf.this;
                ExpandableView expandableView = this.firstViewInShelf;
                Objects.requireNonNull(notificationShelf);
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationShelf.mHostLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationShelf.mIndexOfFirstViewInShelf = notificationStackScrollLayoutController.mView.indexOfChild(expandableView);
                NotificationShelf.this.updateAppearance();
                NotificationShelf notificationShelf2 = NotificationShelf.this;
                boolean z = this.hasItemsInStableShelf;
                Objects.requireNonNull(notificationShelf2);
                if (notificationShelf2.mHasItemsInStableShelf != z) {
                    notificationShelf2.mHasItemsInStableShelf = z;
                    notificationShelf2.updateInteractiveness();
                }
                NotificationShelf notificationShelf3 = NotificationShelf.this;
                notificationShelf3.mShelfIcons.setAnimationsEnabled(notificationShelf3.mAnimationsEnabled);
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @VisibleForTesting
    public boolean isXInView(float f, float f2, float f3, float f4) {
        return f3 - f2 <= f && f < f4 + f2;
    }

    @VisibleForTesting
    public boolean isYInView(float f, float f2, float f3, float f4) {
        return f3 - f2 <= f && f < f4 + f2;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final ExpandableViewState createExpandableViewState() {
        return new ShelfState();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final boolean needsOutline() {
        if (this.mHideBackground || !super.needsOutline()) {
            return false;
        }
        return true;
    }

    @Override // android.view.View.OnLayoutChangeListener
    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        NotificationIconContainer notificationIconContainer = this.mCollapsedIcons;
        if (notificationIconContainer != null) {
            notificationIconContainer.getLocationOnScreen(this.mTmp);
        }
        getLocationOnScreen(this.mTmp);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        this.mStatusBarState = i;
        updateInteractiveness();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        if (!this.mHasItemsInStableShelf) {
            f = 0.0f;
        }
        super.setFakeShadowIntensity(f, f2, i, i2);
    }

    @VisibleForTesting
    public void updateActualWidth(float f, float f2) {
        float f3;
        if (this.mAmbientState.isOnKeyguard()) {
            f3 = MathUtils.lerp(f2, getWidth(), f);
        } else {
            f3 = getWidth();
        }
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        if (notificationBackgroundView != null) {
            Objects.requireNonNull(notificationBackgroundView);
            notificationBackgroundView.mActualWidth = (int) f3;
        }
        NotificationIconContainer notificationIconContainer = this.mShelfIcons;
        if (notificationIconContainer != null) {
            Objects.requireNonNull(notificationIconContainer);
            notificationIconContainer.mActualLayoutWidth = (int) f3;
        }
        this.mActualWidth = f3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:0x0220, code lost:
        if (r14 == false) goto L_0x0222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0294, code lost:
        if (r10 == false) goto L_0x0298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0051, code lost:
        if (java.lang.Math.abs(r7.mExpandingVelocity) > r32.mScrollFastThreshold) goto L_0x0056;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0159, code lost:
        if (r10 >= r2) goto L_0x01a3;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:100:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0204  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x02f2  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x030b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x033e  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x037e  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x03b6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x03bd  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x03fb  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0413  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x044c  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x0488  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x0544  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0569  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x0590  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x0592  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x05c1  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x05c3  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x05d9  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x0602  */
    /* JADX WARN: Removed duplicated region for block: B:358:0x064a  */
    /* JADX WARN: Removed duplicated region for block: B:359:0x064c  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x066f  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0688  */
    /* JADX WARN: Removed duplicated region for block: B:383:0x0560 A[EDGE_INSN: B:383:0x0560->B:312:0x0560 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:392:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01bb  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01fc  */
    /* JADX WARN: Type inference failed for: r7v7, types: [android.view.ViewTreeObserver$OnPreDrawListener, java.lang.Object, com.android.systemui.statusbar.NotificationShelf$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateAppearance() {
        /*
            Method dump skipped, instructions count: 1675
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.updateAppearance():void");
    }

    public final void updateInteractiveness() {
        boolean z;
        int i = 1;
        if (this.mStatusBarState != 1 || !this.mHasItemsInStableShelf) {
            z = false;
        } else {
            z = true;
        }
        this.mInteractive = z;
        setClickable(z);
        setFocusable(this.mInteractive);
        if (!this.mInteractive) {
            i = 4;
        }
        setImportantForAccessibility(i);
    }

    public NotificationShelf(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void initDimens() {
        Resources resources = getResources();
        SystemBarUtils.getStatusBarHeight(((FrameLayout) this).mContext);
        this.mPaddingBetweenElements = resources.getDimensionPixelSize(2131166628);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = resources.getDimensionPixelOffset(2131166676);
        setLayoutParams(layoutParams);
        int dimensionPixelOffset = resources.getDimensionPixelOffset(2131167012);
        this.mShelfIcons.setPadding(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        this.mScrollFastThreshold = resources.getDimensionPixelOffset(2131166972);
        this.mShowNotificationShelf = resources.getBoolean(2131034168);
        this.mCornerAnimationDistance = resources.getDimensionPixelSize(2131166624);
        NotificationIconContainer notificationIconContainer = this.mShelfIcons;
        Objects.requireNonNull(notificationIconContainer);
        notificationIconContainer.mInNotificationIconShelf = true;
        if (!this.mShowNotificationShelf) {
            setVisibility(8);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, android.view.View
    @VisibleForTesting
    public void onFinishInflate() {
        super.onFinishInflate();
        NotificationIconContainer notificationIconContainer = (NotificationIconContainer) findViewById(2131427738);
        this.mShelfIcons = notificationIconContainer;
        notificationIconContainer.setClipChildren(false);
        this.mShelfIcons.setClipToPadding(false);
        this.mClipToActualHeight = false;
        updateClipping();
        setClipChildren(false);
        setClipToPadding(false);
        NotificationIconContainer notificationIconContainer2 = this.mShelfIcons;
        Objects.requireNonNull(notificationIconContainer2);
        notificationIconContainer2.mIsStaticLayout = false;
        setBottomRoundness(1.0f, false);
        setTopRoundness(1.0f, false);
        this.mFirstInSection = true;
        initDimens();
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (this.mInteractive) {
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, getContext().getString(2131951767)));
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView, com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        NotificationIconContainer notificationIconContainer = this.mCollapsedIcons;
        if (notificationIconContainer != null) {
            notificationIconContainer.getLocationOnScreen(this.mTmp);
        }
        getLocationOnScreen(this.mTmp);
        int i5 = getResources().getDisplayMetrics().heightPixels;
        this.mClipRect.set(0, -i5, getWidth(), i5);
        NotificationIconContainer notificationIconContainer2 = this.mShelfIcons;
        if (notificationIconContainer2 != null) {
            notificationIconContainer2.setClipBounds(this.mClipRect);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean pointInView(float f, float f2, float f3) {
        int i;
        float f4;
        float width = getWidth();
        float f5 = this.mActualWidth;
        if (f5 > -1.0f) {
            i = (int) f5;
        } else {
            i = getWidth();
        }
        float f6 = i;
        if (isLayoutRtl()) {
            f4 = width - f6;
        } else {
            f4 = 0.0f;
        }
        if (!isLayoutRtl()) {
            width = f6;
        }
        float f7 = this.mClipTopAmount;
        float f8 = this.mActualHeight;
        if (!isXInView(f, f3, f4, width) || !isYInView(f2, f3, f7, f8)) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0065 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int updateNotificationClipHeight(com.android.systemui.statusbar.notification.row.ExpandableView r6, float r7, int r8) {
        /*
            r5 = this;
            float r0 = r6.getTranslationY()
            int r1 = r6.mActualHeight
            float r1 = (float) r1
            float r0 = r0 + r1
            boolean r1 = r6.isPinned()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0016
            boolean r1 = r6.isHeadsUpAnimatingAway()
            if (r1 == 0) goto L_0x0020
        L_0x0016:
            com.android.systemui.statusbar.notification.stack.AmbientState r1 = r5.mAmbientState
            boolean r1 = r1.isDozingAndNotPulsing(r6)
            if (r1 != 0) goto L_0x0020
            r1 = r2
            goto L_0x0021
        L_0x0020:
            r1 = r3
        L_0x0021:
            com.android.systemui.statusbar.notification.stack.AmbientState r4 = r5.mAmbientState
            boolean r4 = r4.isPulseExpanding()
            if (r4 == 0) goto L_0x002e
            if (r8 != 0) goto L_0x002c
            goto L_0x0032
        L_0x002c:
            r2 = r3
            goto L_0x0032
        L_0x002e:
            boolean r2 = r6.showingPulsing()
        L_0x0032:
            int r8 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r8 <= 0) goto L_0x0059
            if (r2 != 0) goto L_0x0059
            com.android.systemui.statusbar.notification.stack.AmbientState r8 = r5.mAmbientState
            java.util.Objects.requireNonNull(r8)
            boolean r8 = r8.mShadeExpanded
            if (r8 != 0) goto L_0x0043
            if (r1 != 0) goto L_0x0059
        L_0x0043:
            float r7 = r0 - r7
            int r7 = (int) r7
            if (r1 == 0) goto L_0x0055
            int r8 = r6.getIntrinsicHeight()
            int r1 = r6.getCollapsedHeight()
            int r8 = r8 - r1
            int r7 = java.lang.Math.min(r8, r7)
        L_0x0055:
            r6.setClipBottomAmount(r7)
            goto L_0x005c
        L_0x0059:
            r6.setClipBottomAmount(r3)
        L_0x005c:
            if (r2 == 0) goto L_0x0065
            float r5 = r5.getTranslationY()
            float r0 = r0 - r5
            int r5 = (int) r0
            return r5
        L_0x0065:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationShelf.updateNotificationClipHeight(com.android.systemui.statusbar.notification.row.ExpandableView, float, int):int");
    }

    @Override // com.android.systemui.statusbar.notification.row.ActivatableNotificationView
    public final View getContentView() {
        return this.mShelfIcons;
    }
}
