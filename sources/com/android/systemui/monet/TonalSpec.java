package com.android.systemui.monet;
/* compiled from: ColorScheme.kt */
/* loaded from: classes.dex */
public final class TonalSpec {
    public final Chroma chroma;
    public final Hue hue;

    public /* synthetic */ TonalSpec(Chroma chroma) {
        this(new Hue(HueStrategy.SOURCE, 0.0d), chroma);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002b, code lost:
        if (r1 >= 360.0d) goto L_0x0043;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0041, code lost:
        if (r1 >= 360.0d) goto L_0x0043;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0043, code lost:
        r1 = r1 % 360;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.ArrayList shades(com.android.internal.graphics.cam.Cam r14) {
        /*
            r13 = this;
            com.android.systemui.monet.Hue r0 = r13.hue
            float r1 = r14.getHue()
            double r1 = (double) r1
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.monet.HueStrategy r3 = r0.strategy
            int r3 = r3.ordinal()
            r4 = 1
            r5 = 2
            if (r3 == 0) goto L_0x0045
            r6 = 4645040803167600640(0x4076800000000000, double:360.0)
            r8 = 360(0x168, float:5.04E-43)
            r9 = 0
            if (r3 == r4) goto L_0x0034
            if (r3 != r5) goto L_0x002e
            double r11 = r0.value
            double r1 = r1 - r11
            int r0 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1))
            if (r0 >= 0) goto L_0x0029
            goto L_0x003b
        L_0x0029:
            int r0 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r0 < 0) goto L_0x0045
            goto L_0x0043
        L_0x002e:
            kotlin.NoWhenBranchMatchedException r13 = new kotlin.NoWhenBranchMatchedException
            r13.<init>()
            throw r13
        L_0x0034:
            double r11 = r0.value
            double r1 = r1 + r11
            int r0 = (r1 > r9 ? 1 : (r1 == r9 ? 0 : -1))
            if (r0 >= 0) goto L_0x003f
        L_0x003b:
            double r6 = (double) r8
            double r1 = r1 % r6
            double r1 = r1 + r6
            goto L_0x0045
        L_0x003f:
            int r0 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r0 < 0) goto L_0x0045
        L_0x0043:
            double r6 = (double) r8
            double r1 = r1 % r6
        L_0x0045:
            com.android.systemui.monet.Chroma r13 = r13.chroma
            float r14 = r14.getChroma()
            double r6 = (double) r14
            java.util.Objects.requireNonNull(r13)
            com.android.systemui.monet.ChromaStrategy r14 = r13.strategy
            int r14 = r14.ordinal()
            if (r14 == 0) goto L_0x0067
            if (r14 != r4) goto L_0x0061
            double r13 = r13.value
            int r0 = (r6 > r13 ? 1 : (r6 == r13 ? 0 : -1))
            if (r0 >= 0) goto L_0x0069
            r6 = r13
            goto L_0x0069
        L_0x0061:
            kotlin.NoWhenBranchMatchedException r13 = new kotlin.NoWhenBranchMatchedException
            r13.<init>()
            throw r13
        L_0x0067:
            double r6 = r13.value
        L_0x0069:
            float r13 = (float) r1
            float r14 = (float) r6
            r0 = 12
            int[] r1 = new int[r0]
            r2 = 1109393408(0x42200000, float:40.0)
            float r3 = java.lang.Math.min(r2, r14)
            r6 = 1120272384(0x42c60000, float:99.0)
            int r3 = com.android.internal.graphics.ColorUtils.CAMToColor(r13, r3, r6)
            r6 = 0
            r1[r6] = r3
            float r3 = java.lang.Math.min(r2, r14)
            r7 = 1119748096(0x42be0000, float:95.0)
            int r3 = com.android.internal.graphics.ColorUtils.CAMToColor(r13, r3, r7)
            r1[r4] = r3
        L_0x008a:
            if (r5 >= r0) goto L_0x00ad
            r3 = 6
            if (r5 != r3) goto L_0x0093
            r3 = 1111909990(0x42466666, float:49.6)
            goto L_0x009a
        L_0x0093:
            int r3 = r5 + (-1)
            int r3 = r3 * 10
            int r3 = 100 - r3
            float r3 = (float) r3
        L_0x009a:
            r4 = 1119092736(0x42b40000, float:90.0)
            int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x00a4
            float r14 = java.lang.Math.min(r2, r14)
        L_0x00a4:
            int r3 = com.android.internal.graphics.ColorUtils.CAMToColor(r13, r14, r3)
            r1[r5] = r3
            int r5 = r5 + 1
            goto L_0x008a
        L_0x00ad:
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>(r0)
        L_0x00b2:
            if (r6 >= r0) goto L_0x00c0
            r14 = r1[r6]
            int r6 = r6 + 1
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)
            r13.add(r14)
            goto L_0x00b2
        L_0x00c0:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.monet.TonalSpec.shades(com.android.internal.graphics.cam.Cam):java.util.ArrayList");
    }

    public TonalSpec(Hue hue, Chroma chroma) {
        this.hue = hue;
        this.chroma = chroma;
    }
}
