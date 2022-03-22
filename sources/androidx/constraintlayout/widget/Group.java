package androidx.constraintlayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;
/* loaded from: classes.dex */
public class Group extends ConstraintHelper {
    @Override // android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        applyLayoutFeatures$1();
    }

    @Override // android.view.View
    public final void setElevation(float f) {
        super.setElevation(f);
        applyLayoutFeatures$1();
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        super.setVisibility(i);
        applyLayoutFeatures$1();
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void updatePostLayout() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
        layoutParams.widget.setWidth(0);
        layoutParams.widget.setHeight(0);
    }

    @Override // androidx.constraintlayout.widget.ConstraintHelper
    public final void init(AttributeSet attributeSet) {
        super.init(attributeSet);
    }

    public Group(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
