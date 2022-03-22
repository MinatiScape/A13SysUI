package androidx.constraintlayout.motion.utils;
/* loaded from: classes.dex */
public final class VelocityMatrix {
    public float mDRotate;
    public float mDScaleX;
    public float mDScaleY;
    public float mDTranslateX;
    public float mDTranslateY;
    public float mRotate;

    public final void applyTransform(float f, float f2, int i, int i2, float[] fArr) {
        float f3;
        float f4 = fArr[0];
        float f5 = fArr[1];
        float f6 = (f2 - 0.5f) * 2.0f;
        float f7 = f4 + this.mDTranslateX;
        float f8 = f5 + this.mDTranslateY;
        float f9 = (this.mDScaleX * (f - 0.5f) * 2.0f) + f7;
        float f10 = (this.mDScaleY * f6) + f8;
        float radians = (float) Math.toRadians(this.mRotate);
        float radians2 = (float) Math.toRadians(this.mDRotate);
        double d = radians;
        double sin = Math.sin(d);
        double d2 = i2 * f6;
        double cos = Math.cos(d);
        fArr[0] = (((float) ((sin * ((-i) * f3)) - (Math.cos(d) * d2))) * radians2) + f9;
        fArr[1] = (radians2 * ((float) ((cos * (i * f3)) - (Math.sin(d) * d2)))) + f10;
    }
}
