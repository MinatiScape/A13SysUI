package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.collection.ArrayMap;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.settingslib.Utils;
import com.android.systemui.statusbar.AlphaOptimizedFrameLayout;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.stack.AnimationFilter;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationIconContainer extends AlphaOptimizedFrameLayout {
    public static final AnonymousClass4 ADD_ICON_PROPERTIES;
    public static final AnonymousClass1 DOT_ANIMATION_PROPERTIES;
    public static final AnonymousClass2 ICON_ANIMATION_PROPERTIES;
    public static final AnonymousClass6 UNISOLATION_PROPERTY;
    public static final AnonymousClass5 UNISOLATION_PROPERTY_OTHERS;
    public static final AnonymousClass3 sTempProperties = new AnonymousClass3();
    public boolean mChangingViewPositions;
    public boolean mDisallowNextAnimation;
    public int mDotPadding;
    public boolean mDozing;
    public IconState mFirstVisibleIconState;
    public int mIconSize;
    public boolean mInNotificationIconShelf;
    public StatusBarIconView mIsolatedIcon;
    public StatusBarIconView mIsolatedIconForAnimation;
    public Rect mIsolatedIconLocation;
    public int mNumDots;
    public boolean mOnLockScreen;
    public int mOverflowWidth;
    public ArrayMap<String, ArrayList<StatusBarIcon>> mReplacingIcons;
    public int mStaticDotDiameter;
    public int mThemedTextColorPrimary;
    public float mVisualOverflowStart;
    public boolean mIsStaticLayout = true;
    public final HashMap<View, IconState> mIconStates = new HashMap<>();
    public int mActualLayoutWidth = Integer.MIN_VALUE;
    public float mActualPaddingEnd = -2.14748365E9f;
    public float mActualPaddingStart = -2.14748365E9f;
    public int mAddAnimationStartIndex = -1;
    public int mCannedAnimationStartIndex = -1;
    public int mSpeedBumpIndex = -1;
    public boolean mAnimationsEnabled = true;
    public int[] mAbsolutePosition = new int[2];

    /* loaded from: classes.dex */
    public class IconState extends ViewState {
        public static final /* synthetic */ int $r8$clinit = 0;
        public boolean justReplaced;
        public final View mView;
        public boolean needsCannedAnimation;
        public boolean noAnimations;
        public int visibleState;
        public float iconAppearAmount = 1.0f;
        public float clampedAppearAmount = 1.0f;
        public boolean justAdded = true;
        public int iconColor = 0;
        public final ShellTaskOrganizer$$ExternalSyntheticLambda0 mCannedAnimationEndListener = new ShellTaskOrganizer$$ExternalSyntheticLambda0(this, 3);

        public IconState(View view) {
            this.mView = view;
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x001e, code lost:
            if (r0.mVisibleState == 2) goto L_0x0020;
         */
        /* JADX WARN: Code restructure failed: missing block: B:12:0x0020, code lost:
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:7:0x0013, code lost:
            if (r0.mVisibleState != 1) goto L_0x0015;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r13v0, types: [com.android.systemui.statusbar.notification.stack.ViewState, com.android.systemui.statusbar.phone.NotificationIconContainer$IconState] */
        /* JADX WARN: Type inference failed for: r2v10 */
        /* JADX WARN: Type inference failed for: r2v11 */
        /* JADX WARN: Type inference failed for: r2v14, types: [com.android.systemui.statusbar.phone.NotificationIconContainer$3, java.lang.Object, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
        /* JADX WARN: Type inference failed for: r2v37 */
        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void applyToView(android.view.View r14) {
            /*
                Method dump skipped, instructions count: 421
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationIconContainer.IconState.applyToView(android.view.View):void");
        }

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        public final void initFrom(View view) {
            super.initFrom(view);
            if (view instanceof StatusBarIconView) {
                this.iconColor = ((StatusBarIconView) view).mDrawableColor;
            }
        }
    }

    public final void applyIconStates() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            IconState iconState = this.mIconStates.get(childAt);
            if (iconState != null) {
                iconState.applyToView(childAt);
            }
        }
        this.mAddAnimationStartIndex = -1;
        this.mCannedAnimationStartIndex = -1;
        this.mDisallowNextAnimation = false;
        this.mIsolatedIconForAnimation = null;
    }

    public final void resetViewStates() {
        float f;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            IconState iconState = this.mIconStates.get(childAt);
            iconState.initFrom(childAt);
            StatusBarIconView statusBarIconView = this.mIsolatedIcon;
            if (statusBarIconView == null || childAt == statusBarIconView) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            iconState.alpha = f;
            iconState.hidden = false;
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.NotificationIconContainer$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends AnimationProperties {
        public AnimationFilter mAnimationFilter;

        public AnonymousClass2() {
            AnimationFilter animationFilter = new AnimationFilter();
            animationFilter.animateX = true;
            animationFilter.animateY = true;
            animationFilter.animateAlpha = true;
            animationFilter.mAnimatedProperties.add(View.SCALE_X);
            animationFilter.mAnimatedProperties.add(View.SCALE_Y);
            this.mAnimationFilter = animationFilter;
        }

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public final AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.phone.NotificationIconContainer$1, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.phone.NotificationIconContainer$4, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.statusbar.phone.NotificationIconContainer$5, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.statusbar.phone.NotificationIconContainer$6, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
    /* JADX WARN: Unknown variable types count: 4 */
    static {
        /*
            com.android.systemui.statusbar.phone.NotificationIconContainer$1 r0 = new com.android.systemui.statusbar.phone.NotificationIconContainer$1
            r0.<init>()
            r1 = 200(0xc8, double:9.9E-322)
            r0.duration = r1
            com.android.systemui.statusbar.phone.NotificationIconContainer.DOT_ANIMATION_PROPERTIES = r0
            com.android.systemui.statusbar.phone.NotificationIconContainer$2 r0 = new com.android.systemui.statusbar.phone.NotificationIconContainer$2
            r0.<init>()
            r3 = 100
            r0.duration = r3
            com.android.systemui.statusbar.phone.NotificationIconContainer.ICON_ANIMATION_PROPERTIES = r0
            com.android.systemui.statusbar.phone.NotificationIconContainer$3 r0 = new com.android.systemui.statusbar.phone.NotificationIconContainer$3
            r0.<init>()
            com.android.systemui.statusbar.phone.NotificationIconContainer.sTempProperties = r0
            com.android.systemui.statusbar.phone.NotificationIconContainer$4 r0 = new com.android.systemui.statusbar.phone.NotificationIconContainer$4
            r0.<init>()
            r0.duration = r1
            r1 = 50
            r0.delay = r1
            com.android.systemui.statusbar.phone.NotificationIconContainer.ADD_ICON_PROPERTIES = r0
            com.android.systemui.statusbar.phone.NotificationIconContainer$5 r0 = new com.android.systemui.statusbar.phone.NotificationIconContainer$5
            r0.<init>()
            r1 = 110(0x6e, double:5.43E-322)
            r0.duration = r1
            com.android.systemui.statusbar.phone.NotificationIconContainer.UNISOLATION_PROPERTY_OTHERS = r0
            com.android.systemui.statusbar.phone.NotificationIconContainer$6 r0 = new com.android.systemui.statusbar.phone.NotificationIconContainer$6
            r0.<init>()
            r0.duration = r1
            com.android.systemui.statusbar.phone.NotificationIconContainer.UNISOLATION_PROPERTY = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationIconContainer.<clinit>():void");
    }

    public final void calculateIconTranslations() {
        int i;
        IconState iconState;
        View childAt;
        boolean z;
        boolean z2;
        float f;
        int i2;
        float f2;
        boolean z3;
        StatusBarIconView statusBarIconView;
        float actualPaddingStart = getActualPaddingStart();
        int childCount = getChildCount();
        if (this.mOnLockScreen) {
            i = 3;
        } else if (this.mIsStaticLayout) {
            i = 4;
        } else {
            i = childCount;
        }
        float layoutEnd = getLayoutEnd();
        float layoutEnd2 = getLayoutEnd() - this.mOverflowWidth;
        float f3 = 0.0f;
        this.mVisualOverflowStart = 0.0f;
        this.mFirstVisibleIconState = null;
        int i3 = -1;
        int i4 = 0;
        while (i4 < childCount) {
            View childAt2 = getChildAt(i4);
            IconState iconState2 = this.mIconStates.get(childAt2);
            float f4 = iconState2.iconAppearAmount;
            if (f4 == 1.0f) {
                iconState2.xTranslation = actualPaddingStart;
            }
            if (this.mFirstVisibleIconState == null) {
                this.mFirstVisibleIconState = iconState2;
            }
            int i5 = this.mSpeedBumpIndex;
            if ((i5 == -1 || i4 < i5 || f4 <= f3) && i4 < i) {
                z = false;
            } else {
                z = true;
            }
            if (i4 == childCount - 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!this.mOnLockScreen || !(childAt2 instanceof StatusBarIconView)) {
                f = 1.0f;
            } else {
                Objects.requireNonNull((StatusBarIconView) childAt2);
                f = statusBarIconView.mStatusBarIconDrawingSizeIncreased / statusBarIconView.mStatusBarIconDrawingSize;
            }
            if (iconState2.hidden) {
                i2 = 2;
            } else {
                i2 = 0;
            }
            iconState2.visibleState = i2;
            if (z2) {
                f2 = layoutEnd - this.mIconSize;
            } else {
                f2 = layoutEnd2 - this.mIconSize;
            }
            if (actualPaddingStart > f2) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (i3 == -1 && (z || z3)) {
                i3 = (!z2 || z) ? i4 : i4 - 1;
                float f5 = layoutEnd - this.mOverflowWidth;
                this.mVisualOverflowStart = f5;
                if (z || this.mIsStaticLayout) {
                    this.mVisualOverflowStart = Math.min(actualPaddingStart, f5);
                }
            }
            actualPaddingStart += iconState2.iconAppearAmount * childAt2.getWidth() * f;
            i4++;
            f3 = 0.0f;
        }
        this.mNumDots = 0;
        if (i3 != -1) {
            float f6 = this.mVisualOverflowStart;
            while (i3 < childCount) {
                IconState iconState3 = this.mIconStates.get(getChildAt(i3));
                int i6 = this.mStaticDotDiameter + this.mDotPadding;
                iconState3.xTranslation = f6;
                int i7 = this.mNumDots;
                if (i7 < 1) {
                    if (i7 != 0 || iconState3.iconAppearAmount >= 0.8f) {
                        iconState3.visibleState = 1;
                        this.mNumDots = i7 + 1;
                    } else {
                        iconState3.visibleState = 0;
                    }
                    if (this.mNumDots == 1) {
                        i6 *= 1;
                    }
                    f6 = (i6 * iconState3.iconAppearAmount) + f6;
                } else {
                    iconState3.visibleState = 2;
                }
                i3++;
            }
        } else if (childCount > 0) {
            this.mIconStates.get(getChildAt(childCount - 1));
            this.mFirstVisibleIconState = this.mIconStates.get(getChildAt(0));
        }
        if (isLayoutRtl()) {
            for (int i8 = 0; i8 < childCount; i8++) {
                IconState iconState4 = this.mIconStates.get(getChildAt(i8));
                iconState4.xTranslation = (getWidth() - iconState4.xTranslation) - childAt.getWidth();
            }
        }
        StatusBarIconView statusBarIconView2 = this.mIsolatedIcon;
        if (!(statusBarIconView2 == null || (iconState = this.mIconStates.get(statusBarIconView2)) == null)) {
            StatusBarIconView statusBarIconView3 = this.mIsolatedIcon;
            Objects.requireNonNull(statusBarIconView3);
            iconState.xTranslation = (this.mIsolatedIconLocation.left - this.mAbsolutePosition[0]) - (((1.0f - statusBarIconView3.mIconScale) * this.mIsolatedIcon.getWidth()) / 2.0f);
            iconState.visibleState = 0;
        }
    }

    public final float getActualPaddingStart() {
        float f = this.mActualPaddingStart;
        if (f == -2.14748365E9f) {
            return getPaddingStart();
        }
        return f;
    }

    public final float getLayoutEnd() {
        int i = this.mActualLayoutWidth;
        if (i == Integer.MIN_VALUE) {
            i = getWidth();
        }
        float f = i;
        float f2 = this.mActualPaddingEnd;
        if (f2 == -2.14748365E9f) {
            f2 = getPaddingEnd();
        }
        return f - f2;
    }

    public final boolean isReplacingIcon(View view) {
        if (this.mReplacingIcons == null || !(view instanceof StatusBarIconView)) {
            return false;
        }
        StatusBarIconView statusBarIconView = (StatusBarIconView) view;
        Objects.requireNonNull(statusBarIconView);
        Icon icon = statusBarIconView.mIcon.icon;
        String groupKey = statusBarIconView.mNotification.getGroupKey();
        ArrayMap<String, ArrayList<StatusBarIcon>> arrayMap = this.mReplacingIcons;
        Objects.requireNonNull(arrayMap);
        ArrayList<StatusBarIcon> orDefault = arrayMap.getOrDefault(groupKey, null);
        if (orDefault == null || !icon.sameAs(orDefault.get(0).icon)) {
            return false;
        }
        return true;
    }

    public final void setAnimationsEnabled(boolean z) {
        if (!z && this.mAnimationsEnabled) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                IconState iconState = this.mIconStates.get(childAt);
                if (iconState != null) {
                    iconState.cancelAnimations(childAt);
                    iconState.applyToView(childAt);
                }
            }
        }
        this.mAnimationsEnabled = z;
    }

    public NotificationIconContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initDimens();
        setWillNotDraw(true);
    }

    public final void initDimens() {
        this.mDotPadding = getResources().getDimensionPixelSize(2131166727);
        this.mStaticDotDiameter = getResources().getDimensionPixelSize(2131166726) * 2;
        this.mThemedTextColorPrimary = Utils.getColorAttr(new ContextThemeWrapper(getContext(), 16974563), 16842806).getDefaultColor();
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        initDimens();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(getActualPaddingStart(), 0.0f, getLayoutEnd(), getHeight(), paint);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float height = getHeight() / 2.0f;
        this.mIconSize = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i6 = (int) (height - (measuredHeight / 2.0f));
            childAt.layout(0, i6, measuredWidth, measuredHeight + i6);
            if (i5 == 0) {
                int width = childAt.getWidth();
                this.mIconSize = width;
                this.mOverflowWidth = ((this.mStaticDotDiameter + this.mDotPadding) * 0) + width;
            }
        }
        getLocationOnScreen(this.mAbsolutePosition);
        if (this.mIsStaticLayout) {
            resetViewStates();
            calculateIconTranslations();
            applyIconStates();
        }
    }

    @Override // android.view.ViewGroup
    public final void onViewAdded(View view) {
        super.onViewAdded(view);
        boolean isReplacingIcon = isReplacingIcon(view);
        if (!this.mChangingViewPositions) {
            IconState iconState = new IconState(view);
            if (isReplacingIcon) {
                iconState.justAdded = false;
                iconState.justReplaced = true;
            }
            this.mIconStates.put(view, iconState);
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild < getChildCount() - 1 && !isReplacingIcon && this.mIconStates.get(getChildAt(indexOfChild + 1)).iconAppearAmount > 0.0f) {
            int i = this.mAddAnimationStartIndex;
            if (i < 0) {
                this.mAddAnimationStartIndex = indexOfChild;
            } else {
                this.mAddAnimationStartIndex = Math.min(i, indexOfChild);
            }
        }
        if (view instanceof StatusBarIconView) {
            ((StatusBarIconView) view).setDozing(this.mDozing, false);
        }
    }

    @Override // android.view.ViewGroup
    public final void onViewRemoved(View view) {
        boolean z;
        boolean z2;
        long j;
        super.onViewRemoved(view);
        if (view instanceof StatusBarIconView) {
            boolean isReplacingIcon = isReplacingIcon(view);
            StatusBarIconView statusBarIconView = (StatusBarIconView) view;
            boolean z3 = false;
            if (this.mAnimationsEnabled || statusBarIconView == this.mIsolatedIcon) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                Objects.requireNonNull(statusBarIconView);
                if (statusBarIconView.mVisibleState != 2 && view.getVisibility() == 0 && isReplacingIcon) {
                    float translationX = statusBarIconView.getTranslationX();
                    int i = 0;
                    while (true) {
                        if (i >= getChildCount()) {
                            i = getChildCount();
                            break;
                        } else if (getChildAt(i).getTranslationX() > translationX) {
                            break;
                        } else {
                            i++;
                        }
                    }
                    int i2 = this.mAddAnimationStartIndex;
                    if (i2 < 0) {
                        this.mAddAnimationStartIndex = i;
                    } else {
                        this.mAddAnimationStartIndex = Math.min(i2, i);
                    }
                }
            }
            if (!this.mChangingViewPositions) {
                this.mIconStates.remove(view);
                if (this.mAnimationsEnabled || statusBarIconView == this.mIsolatedIcon) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2 && !isReplacingIcon) {
                    addTransientView(statusBarIconView, 0);
                    if (view == this.mIsolatedIcon) {
                        z3 = true;
                    }
                    PipTaskOrganizer$$ExternalSyntheticLambda4 pipTaskOrganizer$$ExternalSyntheticLambda4 = new PipTaskOrganizer$$ExternalSyntheticLambda4(this, statusBarIconView, 3);
                    if (z3) {
                        j = 110;
                    } else {
                        j = 0;
                    }
                    statusBarIconView.setVisibleState(2, true, pipTaskOrganizer$$ExternalSyntheticLambda4, j);
                }
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.NotificationIconContainer$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends AnimationProperties {
        public AnimationFilter mAnimationFilter = new AnimationFilter();

        @Override // com.android.systemui.statusbar.notification.stack.AnimationProperties
        public final AnimationFilter getAnimationFilter() {
            return this.mAnimationFilter;
        }
    }
}
