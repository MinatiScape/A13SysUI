package androidx.transition;

import android.animation.TypeEvaluator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class FloatArrayEvaluator implements TypeEvaluator<float[]> {
    public float[] mArray;

    @Override // android.animation.TypeEvaluator
    public final float[] evaluate(float f, float[] fArr, float[] fArr2) {
        float[] fArr3 = fArr;
        float[] fArr4 = fArr2;
        float[] fArr5 = this.mArray;
        if (fArr5 == null) {
            fArr5 = new float[fArr3.length];
        }
        for (int i = 0; i < fArr5.length; i++) {
            float f2 = fArr3[i];
            fArr5[i] = MotionController$$ExternalSyntheticOutline0.m(fArr4[i], f2, f, f2);
        }
        return fArr5;
    }

    public FloatArrayEvaluator(float[] fArr) {
        this.mArray = fArr;
    }
}
