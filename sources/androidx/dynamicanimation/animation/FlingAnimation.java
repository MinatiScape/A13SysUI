package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
/* loaded from: classes.dex */
public final class FlingAnimation extends DynamicAnimation<FlingAnimation> {
    public final DragForce mFlingForce;

    /* loaded from: classes.dex */
    public static final class DragForce {
        public float mFriction = -4.2f;
        public final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
        public float mVelocityThreshold;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0078 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0079 A[RETURN] */
    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean updateValueAndVelocity(long r7) {
        /*
            r6 = this;
            androidx.dynamicanimation.animation.FlingAnimation$DragForce r0 = r6.mFlingForce
            float r1 = r6.mValue
            float r2 = r6.mVelocity
            java.util.Objects.requireNonNull(r0)
            androidx.dynamicanimation.animation.DynamicAnimation$MassState r3 = r0.mMassState
            double r4 = (double) r2
            float r7 = (float) r7
            r8 = 1148846080(0x447a0000, float:1000.0)
            float r7 = r7 / r8
            float r8 = r0.mFriction
            float r7 = r7 * r8
            double r7 = (double) r7
            double r7 = java.lang.Math.exp(r7)
            double r7 = r7 * r4
            float r7 = (float) r7
            r3.mVelocity = r7
            androidx.dynamicanimation.animation.DynamicAnimation$MassState r7 = r0.mMassState
            float r8 = r7.mVelocity
            float r2 = r8 - r2
            float r3 = r0.mFriction
            float r2 = r2 / r3
            float r2 = r2 + r1
            r7.mValue = r2
            float r7 = java.lang.Math.abs(r8)
            float r8 = r0.mVelocityThreshold
            int r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            r8 = 1
            r1 = 0
            if (r7 >= 0) goto L_0x0036
            r7 = r8
            goto L_0x0037
        L_0x0036:
            r7 = r1
        L_0x0037:
            if (r7 == 0) goto L_0x003e
            androidx.dynamicanimation.animation.DynamicAnimation$MassState r7 = r0.mMassState
            r2 = 0
            r7.mVelocity = r2
        L_0x003e:
            androidx.dynamicanimation.animation.DynamicAnimation$MassState r7 = r0.mMassState
            float r0 = r7.mValue
            r6.mValue = r0
            float r7 = r7.mVelocity
            r6.mVelocity = r7
            float r2 = r6.mMinValue
            int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r3 >= 0) goto L_0x0051
            r6.mValue = r2
            return r8
        L_0x0051:
            float r2 = r6.mMaxValue
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x005a
            r6.mValue = r2
            return r8
        L_0x005a:
            if (r0 >= 0) goto L_0x0075
            if (r3 <= 0) goto L_0x0075
            androidx.dynamicanimation.animation.FlingAnimation$DragForce r6 = r6.mFlingForce
            java.util.Objects.requireNonNull(r6)
            float r7 = java.lang.Math.abs(r7)
            float r6 = r6.mVelocityThreshold
            int r6 = (r7 > r6 ? 1 : (r7 == r6 ? 0 : -1))
            if (r6 >= 0) goto L_0x006f
            r6 = r8
            goto L_0x0070
        L_0x006f:
            r6 = r1
        L_0x0070:
            if (r6 == 0) goto L_0x0073
            goto L_0x0075
        L_0x0073:
            r6 = r1
            goto L_0x0076
        L_0x0075:
            r6 = r8
        L_0x0076:
            if (r6 == 0) goto L_0x0079
            return r8
        L_0x0079:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.dynamicanimation.animation.FlingAnimation.updateValueAndVelocity(long):boolean");
    }

    public <K> FlingAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k, floatPropertyCompat);
        DragForce dragForce = new DragForce();
        this.mFlingForce = dragForce;
        dragForce.mVelocityThreshold = this.mMinVisibleChange * 0.75f * 62.5f;
    }
}
