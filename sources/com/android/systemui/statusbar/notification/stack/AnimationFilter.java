package com.android.systemui.statusbar.notification.stack;

import android.util.Property;
import android.view.View;
import androidx.collection.ArraySet;
/* loaded from: classes.dex */
public class AnimationFilter {
    public boolean animateAlpha;
    public boolean animateDimmed;
    public boolean animateHeight;
    public boolean animateHideSensitive;
    public boolean animateTopInset;
    public boolean animateX;
    public boolean animateY;
    public boolean animateZ;
    public long customDelay;
    public boolean hasDelays;
    public boolean hasGoToFullShadeEvent;
    public ArraySet<View> animateYViews = new ArraySet<>(0);
    public ArraySet<Property> mAnimatedProperties = new ArraySet<>(0);

    public final void reset() {
        this.animateAlpha = false;
        this.animateX = false;
        this.animateY = false;
        this.animateYViews.clear();
        this.animateZ = false;
        this.animateHeight = false;
        this.animateTopInset = false;
        this.animateDimmed = false;
        this.animateHideSensitive = false;
        this.hasDelays = false;
        this.hasGoToFullShadeEvent = false;
        this.customDelay = -1L;
        this.mAnimatedProperties.clear();
    }

    public final void combineFilter(AnimationFilter animationFilter) {
        this.animateAlpha |= animationFilter.animateAlpha;
        this.animateX |= animationFilter.animateX;
        this.animateY |= animationFilter.animateY;
        this.animateYViews.addAll((ArraySet<? extends View>) animationFilter.animateYViews);
        this.animateZ |= animationFilter.animateZ;
        this.animateHeight |= animationFilter.animateHeight;
        this.animateTopInset |= animationFilter.animateTopInset;
        this.animateDimmed |= animationFilter.animateDimmed;
        this.animateHideSensitive |= animationFilter.animateHideSensitive;
        this.hasDelays |= animationFilter.hasDelays;
        this.mAnimatedProperties.addAll((ArraySet<? extends Property>) animationFilter.mAnimatedProperties);
    }

    public boolean shouldAnimateProperty(Property property) {
        return this.mAnimatedProperties.contains(property);
    }
}
