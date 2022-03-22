package com.android.systemui;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public class RegionInterceptingFrameLayout extends FrameLayout {
    public final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsListener;

    /* loaded from: classes.dex */
    public interface RegionInterceptableView {
        Region getInterceptRegion();

        default boolean shouldInterceptTouch() {
            return false;
        }
    }

    public RegionInterceptingFrameLayout(Context context) {
        super(context);
        this.mInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.RegionInterceptingFrameLayout$$ExternalSyntheticLambda1
            public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
                RegionInterceptingFrameLayout.$r8$lambda$etD9bSGE3_mjVzLmmjglFoqooLY(RegionInterceptingFrameLayout.this, internalInsetsInfo);
            }
        };
    }

    public static /* synthetic */ void $r8$lambda$etD9bSGE3_mjVzLmmjglFoqooLY(RegionInterceptingFrameLayout regionInterceptingFrameLayout, ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        Region interceptRegion;
        Objects.requireNonNull(regionInterceptingFrameLayout);
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.setEmpty();
        for (int i = 0; i < regionInterceptingFrameLayout.getChildCount(); i++) {
            View childAt = regionInterceptingFrameLayout.getChildAt(i);
            if (childAt instanceof RegionInterceptableView) {
                RegionInterceptableView regionInterceptableView = (RegionInterceptableView) childAt;
                if (regionInterceptableView.shouldInterceptTouch() && (interceptRegion = regionInterceptableView.getInterceptRegion()) != null) {
                    internalInsetsInfo.touchableRegion.op(interceptRegion, Region.Op.UNION);
                }
            }
        }
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.RegionInterceptingFrameLayout$$ExternalSyntheticLambda1
            public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
                RegionInterceptingFrameLayout.$r8$lambda$etD9bSGE3_mjVzLmmjglFoqooLY(RegionInterceptingFrameLayout.this, internalInsetsInfo);
            }
        };
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsListener);
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.RegionInterceptingFrameLayout$$ExternalSyntheticLambda1
            public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
                RegionInterceptingFrameLayout.$r8$lambda$etD9bSGE3_mjVzLmmjglFoqooLY(RegionInterceptingFrameLayout.this, internalInsetsInfo);
            }
        };
    }

    public RegionInterceptingFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mInsetsListener = new RegionInterceptingFrameLayout$$ExternalSyntheticLambda0(this, 0);
    }
}
