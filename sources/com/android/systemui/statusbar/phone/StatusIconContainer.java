package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.systemui.statusbar.StatusIconDisplayable;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.ViewState;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StatusIconContainer extends AlphaOptimizedLinearLayout {
    public static final AnonymousClass1 ADD_ICON_PROPERTIES;
    public static final AnonymousClass3 ANIMATE_ALL_PROPERTIES;
    public static final AnonymousClass2 X_ANIMATION_PROPERTIES;
    public int mDotPadding;
    public int mIconDotFrameWidth;
    public int mIconSpacing;
    public ArrayList<String> mIgnoredSlots;
    public ArrayList<StatusIconState> mLayoutStates;
    public ArrayList<View> mMeasureViews;
    public boolean mNeedsUnderflow;
    public boolean mShouldRestrictIcons;
    public int mStaticDotDiameter;
    public int mUnderflowStart;
    public int mUnderflowWidth;

    public StatusIconContainer(Context context) {
        this(context, null);
    }

    /* loaded from: classes.dex */
    public static class StatusIconState extends ViewState {
        public int visibleState = 0;
        public boolean justAdded = true;
        public float distanceToViewEnd = -1.0f;

        @Override // com.android.systemui.statusbar.notification.stack.ViewState
        public final void applyToView(View view) {
            float f;
            if (view.getParent() instanceof View) {
                f = ((View) view.getParent()).getWidth();
            } else {
                f = 0.0f;
            }
            float f2 = f - this.xTranslation;
            if (view instanceof StatusIconDisplayable) {
                StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) view;
                AnimationProperties animationProperties = null;
                boolean z = true;
                if (this.justAdded || (statusIconDisplayable.getVisibleState() == 2 && this.visibleState == 0)) {
                    super.applyToView(view);
                    view.setAlpha(0.0f);
                    statusIconDisplayable.setVisibleState(2);
                    animationProperties = StatusIconContainer.ADD_ICON_PROPERTIES;
                } else {
                    int visibleState = statusIconDisplayable.getVisibleState();
                    int i = this.visibleState;
                    if (visibleState != i) {
                        if (statusIconDisplayable.getVisibleState() == 0 && this.visibleState == 2) {
                            z = false;
                        } else {
                            animationProperties = StatusIconContainer.ANIMATE_ALL_PROPERTIES;
                        }
                    } else if (!(i == 2 || this.distanceToViewEnd == f2)) {
                        animationProperties = StatusIconContainer.X_ANIMATION_PROPERTIES;
                    }
                }
                statusIconDisplayable.setVisibleState(this.visibleState, z);
                if (animationProperties != null) {
                    animateTo(view, animationProperties);
                } else {
                    super.applyToView(view);
                }
                this.justAdded = false;
                this.distanceToViewEnd = f2;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.stack.AnimationProperties, com.android.systemui.statusbar.phone.StatusIconContainer$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.phone.StatusIconContainer$2, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.phone.StatusIconContainer$3, com.android.systemui.statusbar.notification.stack.AnimationProperties] */
    /* JADX WARN: Unknown variable types count: 3 */
    static {
        /*
            com.android.systemui.statusbar.phone.StatusIconContainer$1 r0 = new com.android.systemui.statusbar.phone.StatusIconContainer$1
            r0.<init>()
            r1 = 200(0xc8, double:9.9E-322)
            r0.duration = r1
            r3 = 50
            r0.delay = r3
            com.android.systemui.statusbar.phone.StatusIconContainer.ADD_ICON_PROPERTIES = r0
            com.android.systemui.statusbar.phone.StatusIconContainer$2 r0 = new com.android.systemui.statusbar.phone.StatusIconContainer$2
            r0.<init>()
            r0.duration = r1
            com.android.systemui.statusbar.phone.StatusIconContainer.X_ANIMATION_PROPERTIES = r0
            com.android.systemui.statusbar.phone.StatusIconContainer$3 r0 = new com.android.systemui.statusbar.phone.StatusIconContainer$3
            r0.<init>()
            r0.duration = r1
            com.android.systemui.statusbar.phone.StatusIconContainer.ANIMATE_ALL_PROPERTIES = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusIconContainer.<clinit>():void");
    }

    public StatusIconContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUnderflowStart = 0;
        this.mShouldRestrictIcons = true;
        this.mLayoutStates = new ArrayList<>();
        this.mMeasureViews = new ArrayList<>();
        this.mIgnoredSlots = new ArrayList<>();
        this.mIconDotFrameWidth = getResources().getDimensionPixelSize(17105554);
        this.mDotPadding = getResources().getDimensionPixelSize(2131166727);
        this.mIconSpacing = getResources().getDimensionPixelSize(2131167073);
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131166726) * 2;
        this.mStaticDotDiameter = dimensionPixelSize;
        this.mUnderflowWidth = ((dimensionPixelSize + this.mDotPadding) * 0) + this.mIconDotFrameWidth;
        setWillNotDraw(true);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        boolean z;
        int i4;
        int paddingStart;
        int paddingEnd;
        this.mMeasureViews.clear();
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) getChildAt(i5);
            if (statusIconDisplayable.isIconVisible() && !statusIconDisplayable.isIconBlocked() && !this.mIgnoredSlots.contains(statusIconDisplayable.getSlot())) {
                this.mMeasureViews.add((View) statusIconDisplayable);
            }
        }
        int size2 = this.mMeasureViews.size();
        if (size2 <= 7) {
            i3 = 7;
        } else {
            i3 = 6;
        }
        int i6 = ((LinearLayout) this).mPaddingLeft + ((LinearLayout) this).mPaddingRight;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 0);
        if (!this.mShouldRestrictIcons || size2 <= 7) {
            z = false;
        } else {
            z = true;
        }
        this.mNeedsUnderflow = z;
        boolean z2 = true;
        for (int i7 = 0; i7 < size2; i7++) {
            View view = this.mMeasureViews.get((size2 - i7) - 1);
            measureChild(view, makeMeasureSpec, i2);
            if (i7 == size2 - 1) {
                i4 = 0;
            } else {
                i4 = this.mIconSpacing;
            }
            if (!this.mShouldRestrictIcons) {
                paddingStart = view.getPaddingStart() + view.getMeasuredWidth();
                paddingEnd = view.getPaddingEnd();
            } else if (i7 >= i3 || !z2) {
                if (z2) {
                    i6 += this.mUnderflowWidth;
                    z2 = false;
                }
            } else {
                paddingStart = view.getPaddingStart() + view.getMeasuredWidth();
                paddingEnd = view.getPaddingEnd();
            }
            i6 = paddingEnd + paddingStart + i4 + i6;
        }
        if (mode == 1073741824) {
            if (!this.mNeedsUnderflow && i6 > size) {
                this.mNeedsUnderflow = true;
            }
            setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
            return;
        }
        if (mode != Integer.MIN_VALUE || i6 <= size) {
            size = i6;
        } else {
            this.mNeedsUnderflow = true;
        }
        setMeasuredDimension(size, View.MeasureSpec.getSize(i2));
    }

    public final void removeIgnoredSlot(String str) {
        if (this.mIgnoredSlots.remove(str)) {
            requestLayout();
        }
    }

    public final void addIgnoredSlots(List<String> list) {
        boolean z;
        boolean z2 = false;
        for (String str : list) {
            if (this.mIgnoredSlots.contains(str)) {
                z = false;
            } else {
                this.mIgnoredSlots.add(str);
                z = true;
            }
            z2 |= z;
        }
        if (z2) {
            requestLayout();
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View childAt;
        float height = getHeight() / 2.0f;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt2 = getChildAt(i5);
            int measuredWidth = childAt2.getMeasuredWidth();
            int measuredHeight = childAt2.getMeasuredHeight();
            int i6 = (int) (height - (measuredHeight / 2.0f));
            childAt2.layout(0, i6, measuredWidth, measuredHeight + i6);
        }
        for (int i7 = 0; i7 < getChildCount(); i7++) {
            View childAt3 = getChildAt(i7);
            StatusIconState statusIconState = (StatusIconState) childAt3.getTag(2131428929);
            if (statusIconState != null) {
                statusIconState.initFrom(childAt3);
                statusIconState.alpha = 1.0f;
                statusIconState.hidden = false;
            }
        }
        this.mLayoutStates.clear();
        float width = getWidth();
        float paddingEnd = width - getPaddingEnd();
        float paddingStart = getPaddingStart();
        int childCount = getChildCount();
        for (int i8 = childCount - 1; i8 >= 0; i8--) {
            View childAt4 = getChildAt(i8);
            StatusIconDisplayable statusIconDisplayable = (StatusIconDisplayable) childAt4;
            StatusIconState statusIconState2 = (StatusIconState) childAt4.getTag(2131428929);
            if (!statusIconDisplayable.isIconVisible() || statusIconDisplayable.isIconBlocked() || this.mIgnoredSlots.contains(statusIconDisplayable.getSlot())) {
                statusIconState2.visibleState = 2;
            } else {
                float paddingEnd2 = paddingEnd - (childAt4.getPaddingEnd() + (childAt4.getPaddingStart() + childAt4.getWidth()));
                statusIconState2.visibleState = 0;
                statusIconState2.xTranslation = paddingEnd2;
                this.mLayoutStates.add(0, statusIconState2);
                paddingEnd = paddingEnd2 - this.mIconSpacing;
            }
        }
        int size = this.mLayoutStates.size();
        int i9 = 7;
        if (size > 7) {
            i9 = 6;
        }
        this.mUnderflowStart = 0;
        int i10 = size - 1;
        int i11 = 0;
        while (true) {
            if (i10 < 0) {
                i10 = -1;
                break;
            }
            StatusIconState statusIconState3 = this.mLayoutStates.get(i10);
            if ((this.mNeedsUnderflow && statusIconState3.xTranslation < this.mUnderflowWidth + paddingStart) || (this.mShouldRestrictIcons && i11 >= i9)) {
                break;
            }
            this.mUnderflowStart = (int) Math.max(paddingStart, (statusIconState3.xTranslation - this.mUnderflowWidth) - this.mIconSpacing);
            i11++;
            i10--;
        }
        if (i10 != -1) {
            int i12 = this.mStaticDotDiameter + this.mDotPadding;
            int i13 = (this.mUnderflowStart + this.mUnderflowWidth) - this.mIconDotFrameWidth;
            int i14 = 0;
            while (i10 >= 0) {
                StatusIconState statusIconState4 = this.mLayoutStates.get(i10);
                if (i14 < 1) {
                    statusIconState4.xTranslation = i13;
                    statusIconState4.visibleState = 1;
                    i13 -= i12;
                    i14++;
                } else {
                    statusIconState4.visibleState = 2;
                }
                i10--;
            }
        }
        if (isLayoutRtl()) {
            for (int i15 = 0; i15 < childCount; i15++) {
                StatusIconState statusIconState5 = (StatusIconState) getChildAt(i15).getTag(2131428929);
                statusIconState5.xTranslation = (width - statusIconState5.xTranslation) - childAt.getWidth();
            }
        }
        for (int i16 = 0; i16 < getChildCount(); i16++) {
            View childAt5 = getChildAt(i16);
            StatusIconState statusIconState6 = (StatusIconState) childAt5.getTag(2131428929);
            if (statusIconState6 != null) {
                statusIconState6.applyToView(childAt5);
            }
        }
    }

    @Override // android.view.ViewGroup
    public final void onViewAdded(View view) {
        super.onViewAdded(view);
        StatusIconState statusIconState = new StatusIconState();
        statusIconState.justAdded = true;
        view.setTag(2131428929, statusIconState);
    }

    @Override // android.view.ViewGroup
    public final void onViewRemoved(View view) {
        super.onViewRemoved(view);
        view.setTag(2131428929, null);
    }

    public final void removeIgnoredSlots(List<String> list) {
        boolean z = false;
        for (String str : list) {
            z |= this.mIgnoredSlots.remove(str);
        }
        if (z) {
            requestLayout();
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
    }
}
