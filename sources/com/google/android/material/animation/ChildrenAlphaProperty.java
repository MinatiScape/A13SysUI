package com.google.android.material.animation;

import android.util.Property;
import android.view.ViewGroup;
/* loaded from: classes.dex */
public final class ChildrenAlphaProperty extends Property<ViewGroup, Float> {
    public static final ChildrenAlphaProperty CHILDREN_ALPHA = new ChildrenAlphaProperty();

    public ChildrenAlphaProperty() {
        super(Float.class, "childrenAlpha");
    }

    @Override // android.util.Property
    public final Float get(ViewGroup viewGroup) {
        Float f = (Float) viewGroup.getTag(2131428460);
        if (f != null) {
            return f;
        }
        return Float.valueOf(1.0f);
    }

    @Override // android.util.Property
    public final void set(ViewGroup viewGroup, Float f) {
        ViewGroup viewGroup2 = viewGroup;
        float floatValue = f.floatValue();
        viewGroup2.setTag(2131428460, Float.valueOf(floatValue));
        int childCount = viewGroup2.getChildCount();
        for (int i = 0; i < childCount; i++) {
            viewGroup2.getChildAt(i).setAlpha(floatValue);
        }
    }
}
