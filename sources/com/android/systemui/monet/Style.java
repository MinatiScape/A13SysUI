package com.android.systemui.monet;
/* JADX WARN: Init of enum EF0 can be incorrect */
/* JADX WARN: Init of enum EF366 can be incorrect */
/* JADX WARN: Init of enum EF4 can be incorrect */
/* JADX WARN: Init of enum EF453 can be incorrect */
/* JADX WARN: Init of enum EF545 can be incorrect */
/* JADX WARN: Init of enum TONAL_SPOT can be incorrect */
/* compiled from: ColorScheme.kt */
/* loaded from: classes.dex */
public enum Style {
    /* JADX INFO: Fake field, exist only in values array */
    SPRITZ(new CoreSpec(new TonalSpec(new Chroma(r8, 12.0d)), new TonalSpec(new Chroma(r8, 8.0d)), new TonalSpec(new Chroma(r8, 16.0d)), new TonalSpec(new Chroma(r8, 4.0d)), new TonalSpec(new Chroma(r8, 8.0d)))),
    TONAL_SPOT(new CoreSpec(r4, r5, new TonalSpec(new Hue(r13, 60.0d), new Chroma(r8, 24.0d)), new TonalSpec(new Chroma(r8, 4.0d)), new TonalSpec(new Chroma(r8, 8.0d)))),
    /* JADX INFO: Fake field, exist only in values array */
    VIBRANT(new CoreSpec(new TonalSpec(new Chroma(r6, 48.0d)), new TonalSpec(new Hue(r13, 15.0d), new Chroma(r8, 24.0d)), new TonalSpec(new Hue(r13, 30.0d), new Chroma(r6, 32.0d)), new TonalSpec(new Chroma(r8, 8.0d)), new TonalSpec(new Chroma(r8, 16.0d)))),
    /* JADX INFO: Fake field, exist only in values array */
    EXPRESSIVE(new CoreSpec(new TonalSpec(new Hue(r7, 60.0d), new Chroma(r6, 64.0d)), new TonalSpec(new Hue(r7, 30.0d), new Chroma(r8, 24.0d)), new TonalSpec(new Chroma(r6, 48.0d)), new TonalSpec(new Chroma(r8, 12.0d)), new TonalSpec(new Chroma(r8, 16.0d)))),
    /* JADX INFO: Fake field, exist only in values array */
    RAINBOW(new CoreSpec(new TonalSpec(new Chroma(r6, 48.0d)), new TonalSpec(new Chroma(r8, 16.0d)), new TonalSpec(new Hue(r13, 60.0d), new Chroma(r8, 24.0d)), new TonalSpec(new Chroma(r8, 0.0d)), new TonalSpec(new Chroma(r8, 0.0d)))),
    /* JADX INFO: Fake field, exist only in values array */
    FRUIT_SALAD(new CoreSpec(new TonalSpec(new Hue(r7, 50.0d), new Chroma(r6, 48.0d)), new TonalSpec(new Hue(r7, 50.0d), new Chroma(r8, 36.0d)), new TonalSpec(new Chroma(r8, 36.0d)), new TonalSpec(new Chroma(r8, 10.0d)), new TonalSpec(new Chroma(r8, 16.0d))));
    
    private final CoreSpec coreSpec;

    static {
        ChromaStrategy chromaStrategy = ChromaStrategy.EQ;
        new TonalSpec(new Chroma(ChromaStrategy.GTE, 32.0d));
        new TonalSpec(new Chroma(chromaStrategy, 16.0d));
        HueStrategy hueStrategy = HueStrategy.ADD;
        HueStrategy hueStrategy2 = HueStrategy.SUBTRACT;
    }

    Style(CoreSpec coreSpec) {
        this.coreSpec = coreSpec;
    }

    public final CoreSpec getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet() {
        return this.coreSpec;
    }
}
