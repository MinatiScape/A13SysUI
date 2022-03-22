package androidx.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.leanback.R$styleable;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class VerticalGridView extends BaseGridView {
    public VerticalGridView(Context context) {
        this(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VerticalGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLayoutManager.setOrientation(1);
        initBaseGridViewAttributes(context, attributeSet);
        int[] iArr = R$styleable.lbVerticalGridView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        if (obtainStyledAttributes.peekValue(0) != null) {
            this.mLayoutManager.setRowHeight(obtainStyledAttributes.getLayoutDimension(0, 0));
            requestLayout();
        }
        int i2 = obtainStyledAttributes.getInt(1, 1);
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        if (i2 >= 0) {
            gridLayoutManager.mNumRowsRequested = i2;
            requestLayout();
            obtainStyledAttributes.recycle();
            return;
        }
        Objects.requireNonNull(gridLayoutManager);
        throw new IllegalArgumentException();
    }
}
