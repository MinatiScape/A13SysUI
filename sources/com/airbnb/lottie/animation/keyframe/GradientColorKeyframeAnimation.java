package com.airbnb.lottie.animation.keyframe;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.leanback.R$style;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GradientColorKeyframeAnimation extends KeyframeAnimation<GradientColor> {
    public final GradientColor gradientColor;

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public final Object getValue(Keyframe keyframe, float f) {
        GradientColor gradientColor = this.gradientColor;
        GradientColor gradientColor2 = (GradientColor) keyframe.startValue;
        GradientColor gradientColor3 = (GradientColor) keyframe.endValue;
        Objects.requireNonNull(gradientColor);
        if (gradientColor2.colors.length == gradientColor3.colors.length) {
            int i = 0;
            while (true) {
                int[] iArr = gradientColor2.colors;
                if (i >= iArr.length) {
                    return this.gradientColor;
                }
                float[] fArr = gradientColor.positions;
                float f2 = gradientColor2.positions[i];
                float f3 = gradientColor3.positions[i];
                PointF pointF = MiscUtils.pathFromDataCurrentPoint;
                fArr[i] = MotionController$$ExternalSyntheticOutline0.m(f3, f2, f, f2);
                gradientColor.colors[i] = R$style.evaluate(f, iArr[i], gradientColor3.colors[i]);
                i++;
            }
        } else {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot interpolate between gradients. Lengths vary (");
            m.append(gradientColor2.colors.length);
            m.append(" vs ");
            m.append(gradientColor3.colors.length);
            m.append(")");
            throw new IllegalArgumentException(m.toString());
        }
    }

    public GradientColorKeyframeAnimation(List<Keyframe<GradientColor>> list) {
        super(list);
        int i = 0;
        GradientColor gradientColor = list.get(0).startValue;
        i = gradientColor != null ? gradientColor.colors.length : i;
        this.gradientColor = new GradientColor(new float[i], new int[i]);
    }
}
