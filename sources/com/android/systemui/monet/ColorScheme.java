package com.android.systemui.monet;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.internal.graphics.cam.Cam;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
/* compiled from: ColorScheme.kt */
/* loaded from: classes.dex */
public final class ColorScheme {
    public static final Companion Companion = new Companion();
    public final ArrayList accent1;
    public final ArrayList accent2;
    public final ArrayList accent3;
    public final ArrayList neutral1;
    public final ArrayList neutral2;
    public final Style style;

    /* compiled from: ColorScheme.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static final String access$humanReadable(ArrayList arrayList) {
            return CollectionsKt___CollectionsKt.joinToString$default(arrayList, null, null, null, ColorScheme$Companion$humanReadable$1.INSTANCE, 31);
        }

        /* JADX WARN: Code restructure failed: missing block: B:107:0x0381, code lost:
            if (r3 != 15) goto L_0x0391;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static java.util.List getSeedColors(android.app.WallpaperColors r18) {
            /*
                Method dump skipped, instructions count: 924
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.monet.ColorScheme.Companion.getSeedColors(android.app.WallpaperColors):java.util.List");
        }
    }

    public ColorScheme(int i, Style style) {
        this.style = style;
        Cam fromInt = Cam.fromInt((i == 0 || Cam.fromInt(i).getChroma() < 5.0f) ? -14979341 : i);
        CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
        Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet);
        this.accent1 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet.a1.shades(fromInt);
        CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet2 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
        Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet2);
        this.accent2 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet2.a2.shades(fromInt);
        CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet3 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
        Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet3);
        this.accent3 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet3.a3.shades(fromInt);
        CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet4 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
        Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet4);
        this.neutral1 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet4.n1.shades(fromInt);
        CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet5 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
        Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet5);
        this.neutral2 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet5.n2.shades(fromInt);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ColorScheme {\n  neutral1: ");
        m.append(Companion.access$humanReadable(this.neutral1));
        m.append("\n  neutral2: ");
        m.append(Companion.access$humanReadable(this.neutral2));
        m.append("\n  accent1: ");
        m.append(Companion.access$humanReadable(this.accent1));
        m.append("\n  accent2: ");
        m.append(Companion.access$humanReadable(this.accent2));
        m.append("\n  accent3: ");
        m.append(Companion.access$humanReadable(this.accent3));
        m.append("\n  style: ");
        m.append(this.style);
        m.append("\n}");
        return m.toString();
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ColorScheme(android.app.WallpaperColors r2) {
        /*
            r1 = this;
            java.util.List r2 = com.android.systemui.monet.ColorScheme.Companion.getSeedColors(r2)
            boolean r0 = r2.isEmpty()
            if (r0 != 0) goto L_0x001b
            r0 = 0
            java.lang.Object r2 = r2.get(r0)
            java.lang.Number r2 = (java.lang.Number) r2
            int r2 = r2.intValue()
            com.android.systemui.monet.Style r0 = com.android.systemui.monet.Style.TONAL_SPOT
            r1.<init>(r2, r0)
            return
        L_0x001b:
            java.util.NoSuchElementException r1 = new java.util.NoSuchElementException
            java.lang.String r2 = "List is empty."
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.monet.ColorScheme.<init>(android.app.WallpaperColors):void");
    }
}
