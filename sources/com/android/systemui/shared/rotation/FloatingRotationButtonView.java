package com.android.systemui.shared.rotation;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.systemui.navigationbar.buttons.KeyButtonRipple;
import java.util.Objects;
/* loaded from: classes.dex */
public class FloatingRotationButtonView extends ImageView {
    public final Configuration mLastConfiguration;
    public final Paint mOvalBgPaint;
    public KeyButtonRipple mRipple;

    public FloatingRotationButtonView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FloatingRotationButtonView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOvalBgPaint = new Paint(3);
        this.mLastConfiguration = getResources().getConfiguration();
        setClickable(true);
        setWillNotDraw(false);
        forceHasOverlappingRendering(false);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        KeyButtonRipple keyButtonRipple;
        int updateFrom = this.mLastConfiguration.updateFrom(configuration);
        if (((updateFrom & 1024) != 0 || (updateFrom & 4096) != 0) && (keyButtonRipple = this.mRipple) != null) {
            Objects.requireNonNull(keyButtonRipple);
            keyButtonRipple.mMaxWidth = keyButtonRipple.mTargetView.getContext().getResources().getDimensionPixelSize(keyButtonRipple.mMaxWidthResource);
            keyButtonRipple.invalidateSelf();
        }
    }

    @Override // android.view.View
    public final void draw(Canvas canvas) {
        float min = Math.min(getWidth(), getHeight());
        canvas.drawOval(0.0f, 0.0f, min, min, this.mOvalBgPaint);
        super.draw(canvas);
    }

    @Override // android.view.View
    public final void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i != 0) {
            jumpDrawablesToCurrentState();
        }
    }
}
