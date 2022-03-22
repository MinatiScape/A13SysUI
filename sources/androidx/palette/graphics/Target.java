package androidx.palette.graphics;
/* loaded from: classes.dex */
public final class Target {
    public static final Target DARK_MUTED;
    public static final Target DARK_VIBRANT;
    public static final Target LIGHT_MUTED;
    public static final Target LIGHT_VIBRANT;
    public static final Target MUTED;
    public static final Target VIBRANT;
    public final float[] mLightnessTargets;
    public final float[] mSaturationTargets;
    public final float[] mWeights;

    static {
        Target target = new Target();
        LIGHT_VIBRANT = target;
        float[] fArr = target.mLightnessTargets;
        fArr[0] = 0.55f;
        fArr[1] = 0.74f;
        float[] fArr2 = target.mSaturationTargets;
        fArr2[0] = 0.35f;
        fArr2[1] = 1.0f;
        Target target2 = new Target();
        VIBRANT = target2;
        float[] fArr3 = target2.mLightnessTargets;
        fArr3[0] = 0.3f;
        fArr3[1] = 0.5f;
        fArr3[2] = 0.7f;
        float[] fArr4 = target2.mSaturationTargets;
        fArr4[0] = 0.35f;
        fArr4[1] = 1.0f;
        Target target3 = new Target();
        DARK_VIBRANT = target3;
        float[] fArr5 = target3.mLightnessTargets;
        fArr5[1] = 0.26f;
        fArr5[2] = 0.45f;
        float[] fArr6 = target3.mSaturationTargets;
        fArr6[0] = 0.35f;
        fArr6[1] = 1.0f;
        Target target4 = new Target();
        LIGHT_MUTED = target4;
        float[] fArr7 = target4.mLightnessTargets;
        fArr7[0] = 0.55f;
        fArr7[1] = 0.74f;
        float[] fArr8 = target4.mSaturationTargets;
        fArr8[1] = 0.3f;
        fArr8[2] = 0.4f;
        Target target5 = new Target();
        MUTED = target5;
        float[] fArr9 = target5.mLightnessTargets;
        fArr9[0] = 0.3f;
        fArr9[1] = 0.5f;
        fArr9[2] = 0.7f;
        float[] fArr10 = target5.mSaturationTargets;
        fArr10[1] = 0.3f;
        fArr10[2] = 0.4f;
        Target target6 = new Target();
        DARK_MUTED = target6;
        float[] fArr11 = target6.mLightnessTargets;
        fArr11[1] = 0.26f;
        fArr11[2] = 0.45f;
        float[] fArr12 = target6.mSaturationTargets;
        fArr12[1] = 0.3f;
        fArr12[2] = 0.4f;
    }

    public Target() {
        this.mSaturationTargets = r1;
        this.mLightnessTargets = r2;
        this.mWeights = r0;
        float[] fArr = {0.0f, 0.5f, 1.0f};
        float[] fArr2 = {0.0f, 0.5f, 1.0f};
        float[] fArr3 = {0.24f, 0.52f, 0.24f};
    }
}
