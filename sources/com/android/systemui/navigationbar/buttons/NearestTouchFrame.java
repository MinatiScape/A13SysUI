package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.volume.VolumeDialogComponent$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.SavedNetworkTracker$$ExternalSyntheticLambda3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public class NearestTouchFrame extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ArrayList mAttachedChildren;
    public final NearestTouchFrame$$ExternalSyntheticLambda0 mChildRegionComparator;
    public final ArrayList mClickableChildren;
    public final boolean mIsActive;
    public boolean mIsVertical;
    public final int[] mOffset;
    public final int[] mTmpInt;
    public final HashMap mTouchableRegions;
    public View mTouchingChild;

    public NearestTouchFrame(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, context.getResources().getConfiguration());
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.navigationbar.buttons.NearestTouchFrame$$ExternalSyntheticLambda0] */
    public NearestTouchFrame(Context context, AttributeSet attributeSet, Configuration configuration) {
        super(context, attributeSet);
        this.mClickableChildren = new ArrayList();
        this.mAttachedChildren = new ArrayList();
        this.mTmpInt = new int[2];
        this.mOffset = new int[2];
        this.mTouchableRegions = new HashMap();
        this.mChildRegionComparator = new Comparator() { // from class: com.android.systemui.navigationbar.buttons.NearestTouchFrame$$ExternalSyntheticLambda0
            /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
            /* JADX WARN: Unknown variable types count: 1 */
            @Override // java.util.Comparator
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final int compare(java.lang.Object r4, java.lang.Object r5) {
                /*
                    r3 = this;
                    com.android.systemui.navigationbar.buttons.NearestTouchFrame r3 = com.android.systemui.navigationbar.buttons.NearestTouchFrame.this
                    android.view.View r4 = (android.view.View) r4
                    android.view.View r5 = (android.view.View) r5
                    int r0 = com.android.systemui.navigationbar.buttons.NearestTouchFrame.$r8$clinit
                    java.util.Objects.requireNonNull(r3)
                    boolean r0 = r3.mIsVertical
                    int[] r1 = r3.mTmpInt
                    r4.getLocationInWindow(r1)
                    int[] r4 = r3.mTmpInt
                    r1 = r4[r0]
                    int[] r2 = r3.mOffset
                    r2 = r2[r0]
                    int r1 = r1 - r2
                    r5.getLocationInWindow(r4)
                    int[] r4 = r3.mTmpInt
                    r4 = r4[r0]
                    int[] r3 = r3.mOffset
                    r3 = r3[r0]
                    int r4 = r4 - r3
                    int r1 = r1 - r4
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.buttons.NearestTouchFrame$$ExternalSyntheticLambda0.compare(java.lang.Object, java.lang.Object):int");
            }
        };
        this.mIsActive = configuration.smallestScreenWidthDp < 600;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{2130969249});
        this.mIsVertical = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mIsActive) {
            final int x = (int) motionEvent.getX();
            final int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                this.mTouchingChild = (View) this.mClickableChildren.stream().filter(NearestTouchFrame$$ExternalSyntheticLambda2.INSTANCE).filter(new Predicate() { // from class: com.android.systemui.navigationbar.buttons.NearestTouchFrame$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        NearestTouchFrame nearestTouchFrame = NearestTouchFrame.this;
                        int i = x;
                        int i2 = y;
                        int i3 = NearestTouchFrame.$r8$clinit;
                        Objects.requireNonNull(nearestTouchFrame);
                        return ((Rect) nearestTouchFrame.mTouchableRegions.get((View) obj)).contains(i, i2);
                    }
                }).findFirst().orElse(null);
            }
            View view = this.mTouchingChild;
            if (view != null) {
                motionEvent.offsetLocation((view.getWidth() / 2) - x, (this.mTouchingChild.getHeight() / 2) - y);
                if (this.mTouchingChild.getVisibility() != 0 || !this.mTouchingChild.dispatchTouchEvent(motionEvent)) {
                    return false;
                }
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public final void addClickableChildren(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.isClickable()) {
                this.mClickableChildren.add(childAt);
            } else if (childAt instanceof ViewGroup) {
                addClickableChildren((ViewGroup) childAt);
            }
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        super.onLayout(z, i, i2, i3, i4);
        this.mClickableChildren.clear();
        this.mAttachedChildren.clear();
        this.mTouchableRegions.clear();
        addClickableChildren(this);
        getLocationInWindow(this.mOffset);
        if (!(getWidth() == 0 || getHeight() == 0)) {
            this.mClickableChildren.sort(this.mChildRegionComparator);
            Stream filter = this.mClickableChildren.stream().filter(SavedNetworkTracker$$ExternalSyntheticLambda3.INSTANCE$1);
            ArrayList arrayList = this.mAttachedChildren;
            Objects.requireNonNull(arrayList);
            filter.forEachOrdered(new VolumeDialogComponent$$ExternalSyntheticLambda0(arrayList, 1));
            for (int i7 = 0; i7 < this.mAttachedChildren.size(); i7++) {
                View view = (View) this.mAttachedChildren.get(i7);
                if (view.isAttachedToWindow()) {
                    view.getLocationInWindow(this.mTmpInt);
                    int[] iArr = this.mTmpInt;
                    int i8 = iArr[0];
                    int[] iArr2 = this.mOffset;
                    int i9 = i8 - iArr2[0];
                    int i10 = iArr[1] - iArr2[1];
                    Rect rect = new Rect(i9, i10, view.getWidth() + i9, view.getHeight() + i10);
                    if (i7 == 0) {
                        if (this.mIsVertical) {
                            rect.top = 0;
                        } else {
                            rect.left = 0;
                        }
                        this.mTouchableRegions.put(view, rect);
                    } else {
                        Rect rect2 = (Rect) this.mTouchableRegions.get((View) this.mAttachedChildren.get(i7 - 1));
                        if (this.mIsVertical) {
                            int i11 = rect.top;
                            int i12 = rect2.bottom;
                            int i13 = i11 - i12;
                            int i14 = i13 / 2;
                            rect.top = i11 - i14;
                            if (i13 % 2 == 0) {
                                i6 = 1;
                            } else {
                                i6 = 0;
                            }
                            rect2.bottom = (i14 - i6) + i12;
                        } else {
                            int i15 = rect.left;
                            int i16 = rect2.right;
                            int i17 = i15 - i16;
                            int i18 = i17 / 2;
                            rect.left = i15 - i18;
                            if (i17 % 2 == 0) {
                                i5 = 1;
                            } else {
                                i5 = 0;
                            }
                            rect2.right = (i18 - i5) + i16;
                        }
                        if (i7 == this.mClickableChildren.size() - 1) {
                            if (this.mIsVertical) {
                                rect.bottom = getHeight();
                            } else {
                                rect.right = getWidth();
                            }
                        }
                        this.mTouchableRegions.put(view, rect);
                    }
                }
            }
        }
    }

    public void setIsVertical(boolean z) {
        this.mIsVertical = z;
    }
}
