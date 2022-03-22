package com.android.systemui.statusbar.notification.row;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda4;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda9;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class ExpandableOutlineView extends ExpandableView {
    public static final AnimatableProperty.AnonymousClass6 BOTTOM_ROUNDNESS = new AnimatableProperty.AnonymousClass6(2131427591, 2131427592, 2131427593, new AnimatableProperty.AnonymousClass5("bottomRoundness", WifiPickerTracker$$ExternalSyntheticLambda9.INSTANCE$1, ExpandableOutlineView$$ExternalSyntheticLambda1.INSTANCE));
    public static final Path EMPTY_PATH = new Path();
    public static final AnimationProperties ROUNDNESS_PROPERTIES;
    public static final AnimatableProperty.AnonymousClass6 TOP_ROUNDNESS;
    public boolean mAlwaysRoundBothCorners;
    public float mBottomRoundness;
    public float mCurrentBottomRoundness;
    public float mCurrentTopRoundness;
    public boolean mCustomOutline;
    public float mOutlineRadius;
    public final AnonymousClass1 mProvider;
    public float mTopRoundness;
    public final Rect mOutlineRect = new Rect();
    public float mOutlineAlpha = -1.0f;
    public Path mTmpPath = new Path();
    public boolean mDismissUsingRowTranslationX = true;
    public float[] mTmpCornerRadii = new float[8];

    public boolean childNeedsClipping(View view) {
        return false;
    }

    public Path getCustomClipPath(View view) {
        return null;
    }

    public final void setOutlineRect(float f, float f2, float f3, float f4) {
        this.mCustomOutline = true;
        this.mOutlineRect.set((int) f, (int) f2, (int) f3, (int) f4);
        Rect rect = this.mOutlineRect;
        rect.bottom = (int) Math.max(f2, rect.bottom);
        Rect rect2 = this.mOutlineRect;
        rect2.right = (int) Math.max(f, rect2.right);
        applyRoundness();
    }

    static {
        ExpandableOutlineView$$ExternalSyntheticLambda0 expandableOutlineView$$ExternalSyntheticLambda0 = ExpandableOutlineView$$ExternalSyntheticLambda0.INSTANCE;
        PeopleSpaceUtils$$ExternalSyntheticLambda4 peopleSpaceUtils$$ExternalSyntheticLambda4 = PeopleSpaceUtils$$ExternalSyntheticLambda4.INSTANCE$2;
        AnimatableProperty.AnonymousClass7 r2 = AnimatableProperty.Y;
        TOP_ROUNDNESS = new AnimatableProperty.AnonymousClass6(2131429077, 2131429078, 2131429079, new AnimatableProperty.AnonymousClass5("topRoundness", peopleSpaceUtils$$ExternalSyntheticLambda4, expandableOutlineView$$ExternalSyntheticLambda0));
        AnimationProperties animationProperties = new AnimationProperties();
        animationProperties.duration = 200L;
        ROUNDNESS_PROPERTIES = animationProperties;
    }

    public final Path getClipPath(boolean z) {
        float f;
        int i;
        int i2;
        int i3;
        int i4;
        float f2;
        int i5;
        if (this.mAlwaysRoundBothCorners) {
            f = this.mOutlineRadius;
        } else {
            f = getCurrentBackgroundRadiusTop();
        }
        if (!this.mCustomOutline) {
            if (this.mDismissUsingRowTranslationX || z) {
                i5 = 0;
            } else {
                i5 = (int) getTranslation();
            }
            int i6 = (int) (this.mExtraWidthForClipping / 2.0f);
            i2 = Math.max(i5, 0) - i6;
            i = this.mClipTopAmount + 0;
            i4 = Math.min(i5, 0) + getWidth() + i6;
            i3 = Math.max(this.mMinimumHeightForClipping, Math.max(this.mActualHeight - this.mClipBottomAmount, (int) (i + f)));
        } else {
            Rect rect = this.mOutlineRect;
            i2 = rect.left;
            i = rect.top;
            int i7 = rect.right;
            i3 = rect.bottom;
            i4 = i7;
        }
        int i8 = i3 - i;
        if (i8 == 0) {
            return EMPTY_PATH;
        }
        if (this.mAlwaysRoundBothCorners) {
            f2 = this.mOutlineRadius;
        } else {
            f2 = getCurrentBackgroundRadiusBottom();
        }
        float f3 = f + f2;
        float f4 = i8;
        if (f3 > f4) {
            float f5 = f3 - f4;
            float f6 = this.mCurrentTopRoundness;
            float f7 = this.mCurrentBottomRoundness;
            float f8 = f5 * f6;
            float f9 = f6 + f7;
            f -= f8 / f9;
            f2 -= (f5 * f7) / f9;
        }
        Path path = this.mTmpPath;
        path.reset();
        float[] fArr = this.mTmpCornerRadii;
        fArr[0] = f;
        fArr[1] = f;
        fArr[2] = f;
        fArr[3] = f;
        fArr[4] = f2;
        fArr[5] = f2;
        fArr[6] = f2;
        fArr[7] = f2;
        path.addRoundRect(i2, i, i4, i3, fArr, Path.Direction.CW);
        return this.mTmpPath;
    }

    public float getCurrentBackgroundRadiusBottom() {
        return this.mCurrentBottomRoundness * this.mOutlineRadius;
    }

    public float getCurrentBackgroundRadiusTop() {
        return this.mCurrentTopRoundness * this.mOutlineRadius;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final int getOutlineTranslation() {
        if (this.mCustomOutline) {
            return this.mOutlineRect.left;
        }
        if (this.mDismissUsingRowTranslationX) {
            return 0;
        }
        return (int) getTranslation();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setActualHeight(int i, boolean z) {
        int i2 = this.mActualHeight;
        super.setActualHeight(i, z);
        if (i2 != i) {
            applyRoundness();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean setBottomRoundness(float f, boolean z) {
        float f2 = this.mBottomRoundness;
        boolean z2 = false;
        if (f2 == f) {
            return false;
        }
        float abs = Math.abs(f - f2);
        this.mBottomRoundness = f;
        AnimatableProperty.AnonymousClass6 r1 = BOTTOM_ROUNDNESS;
        Objects.requireNonNull(r1);
        if (getTag(r1.val$animatorTag) != null) {
            z2 = true;
        }
        if (z2 && abs > 0.5f) {
            z = true;
        }
        PropertyAnimator.setProperty(this, r1, f, ROUNDNESS_PROPERTIES, z);
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipBottomAmount(int i) {
        int i2 = this.mClipBottomAmount;
        super.setClipBottomAmount(i);
        if (i2 != i) {
            applyRoundness();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipTopAmount(int i) {
        int i2 = this.mClipTopAmount;
        super.setClipTopAmount(i);
        if (i2 != i) {
            applyRoundness();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final boolean setTopRoundness(float f, boolean z) {
        float f2 = this.mTopRoundness;
        boolean z2 = false;
        if (f2 == f) {
            return false;
        }
        float abs = Math.abs(f - f2);
        this.mTopRoundness = f;
        AnimatableProperty.AnonymousClass6 r1 = TOP_ROUNDNESS;
        Objects.requireNonNull(r1);
        if (getTag(r1.val$animatorTag) != null) {
            z2 = true;
        }
        if (z2 && abs > 0.5f) {
            z = true;
        }
        PropertyAnimator.setProperty(this, r1, f, ROUNDNESS_PROPERTIES, z);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.statusbar.notification.row.ExpandableOutlineView$1, android.view.ViewOutlineProvider] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ExpandableOutlineView(android.content.Context r1, android.util.AttributeSet r2) {
        /*
            r0 = this;
            r0.<init>(r1, r2)
            android.graphics.Rect r1 = new android.graphics.Rect
            r1.<init>()
            r0.mOutlineRect = r1
            android.graphics.Path r1 = new android.graphics.Path
            r1.<init>()
            r1 = -1082130432(0xffffffffbf800000, float:-1.0)
            r0.mOutlineAlpha = r1
            android.graphics.Path r1 = new android.graphics.Path
            r1.<init>()
            r0.mTmpPath = r1
            r1 = 1
            r0.mDismissUsingRowTranslationX = r1
            r1 = 8
            float[] r1 = new float[r1]
            r0.mTmpCornerRadii = r1
            com.android.systemui.statusbar.notification.row.ExpandableOutlineView$1 r1 = new com.android.systemui.statusbar.notification.row.ExpandableOutlineView$1
            r1.<init>()
            r0.mProvider = r1
            r0.setOutlineProvider(r1)
            r0.initDimens()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ExpandableOutlineView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    private void initDimens() {
        Resources resources = getResources();
        this.mOutlineRadius = resources.getDimension(2131166675);
        boolean z = resources.getBoolean(2131034120);
        this.mAlwaysRoundBothCorners = z;
        if (!z) {
            this.mOutlineRadius = resources.getDimensionPixelSize(2131166625);
        }
        setClipToOutline(this.mAlwaysRoundBothCorners);
    }

    public void applyRoundness() {
        invalidateOutline();
        invalidate();
    }

    @Override // android.view.ViewGroup
    public final boolean drawChild(Canvas canvas, View view, long j) {
        canvas.save();
        if (childNeedsClipping(view)) {
            Path customClipPath = getCustomClipPath(view);
            if (customClipPath == null) {
                customClipPath = getClipPath(false);
            }
            if (customClipPath != null) {
                canvas.clipPath(customClipPath);
            }
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        canvas.restore();
        return drawChild;
    }

    public final boolean isClippingNeeded() {
        boolean z;
        if (getTranslation() == 0.0f || this.mDismissUsingRowTranslationX) {
            z = false;
        } else {
            z = true;
        }
        if (this.mAlwaysRoundBothCorners || this.mCustomOutline || z) {
            return true;
        }
        return false;
    }

    public boolean needsOutline() {
        if (isChildInGroup()) {
            if (!isGroupExpanded() || isGroupExpansionChanging()) {
                return false;
            }
            return true;
        } else if (!isSummaryWithChildren()) {
            return true;
        } else {
            if (!isGroupExpanded() || isGroupExpansionChanging()) {
                return true;
            }
            return false;
        }
    }

    public void onDensityOrFontScaleChanged() {
        initDimens();
        applyRoundness();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final float getOutlineAlpha() {
        return this.mOutlineAlpha;
    }
}
