package com.android.systemui.volume;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.android.systemui.recents.TriangleShape;
/* loaded from: classes.dex */
public class VolumeToolTipView extends LinearLayout {
    public VolumeToolTipView(Context context) {
        super(context);
    }

    public VolumeToolTipView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VolumeToolTipView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(2131427514);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.createHorizontal(layoutParams.width, layoutParams.height, false));
        Paint paint = shapeDrawable.getPaint();
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(16843829, typedValue, true);
        Context context = getContext();
        int i = typedValue.resourceId;
        Object obj = ContextCompat.sLock;
        paint.setColor(context.getColor(i));
        paint.setPathEffect(new CornerPathEffect(getResources().getDimension(2131167310)));
        findViewById.setBackground(shapeDrawable);
    }

    public VolumeToolTipView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
