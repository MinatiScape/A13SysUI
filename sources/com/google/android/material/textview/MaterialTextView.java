package com.google.android.material.textview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.R$styleable;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
/* loaded from: classes.dex */
public class MaterialTextView extends AppCompatTextView {
    public MaterialTextView(Context context) {
        this(context, null);
    }

    public MaterialTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    public final void applyLineHeightFromViewAppearance(Resources.Theme theme, int i) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(i, R$styleable.MaterialTextAppearance);
        Context context = getContext();
        int[] iArr = {1, 2};
        int i2 = -1;
        for (int i3 = 0; i3 < 2 && i2 < 0; i3++) {
            i2 = MaterialResources.getDimensionPixelSize(context, obtainStyledAttributes, iArr[i3], -1);
        }
        obtainStyledAttributes.recycle();
        if (i2 >= 0) {
            setLineHeight(i2);
        }
    }

    public MaterialTextView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView
    public final void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        TypedValue resolve = MaterialAttributes.resolve(context, 2130969980);
        boolean z = true;
        if (resolve != null && resolve.type == 18 && resolve.data == 0) {
            z = false;
        }
        if (z) {
            applyLineHeightFromViewAppearance(context.getTheme(), i);
        }
    }

    public MaterialTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, i2), attributeSet, i);
        Context context2 = getContext();
        TypedValue resolve = MaterialAttributes.resolve(context2, 2130969980);
        boolean z = true;
        if ((resolve != null && resolve.type == 18 && resolve.data == 0) ? false : true) {
            Resources.Theme theme = context2.getTheme();
            int[] iArr = R$styleable.MaterialTextView;
            TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, iArr, i, i2);
            int[] iArr2 = {1, 2};
            int i3 = -1;
            for (int i4 = 0; i4 < 2 && i3 < 0; i4++) {
                i3 = MaterialResources.getDimensionPixelSize(context2, obtainStyledAttributes, iArr2[i4], -1);
            }
            obtainStyledAttributes.recycle();
            if (!(i3 == -1 ? false : z)) {
                TypedArray obtainStyledAttributes2 = theme.obtainStyledAttributes(attributeSet, iArr, i, i2);
                int resourceId = obtainStyledAttributes2.getResourceId(0, -1);
                obtainStyledAttributes2.recycle();
                if (resourceId != -1) {
                    applyLineHeightFromViewAppearance(theme, resourceId);
                }
            }
        }
    }
}
