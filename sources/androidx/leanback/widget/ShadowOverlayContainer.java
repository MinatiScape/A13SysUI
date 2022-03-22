package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class ShadowOverlayContainer extends FrameLayout {
    public ShadowOverlayContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        getResources().getDimension(2131166020);
        getResources().getDimension(2131166019);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    static {
        new Rect();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
