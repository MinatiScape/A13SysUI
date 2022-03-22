package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontVariationAxis;
import androidx.fragment.R$styleable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FontInterpolator.kt */
/* loaded from: classes.dex */
public final class FontInterpolator {
    public static final FontVariationAxis[] EMPTY_AXES = new FontVariationAxis[0];
    public final HashMap<InterpKey, Font> interpCache = new HashMap<>();
    public final HashMap<VarFontKey, Font> verFontCache = new HashMap<>();
    public final InterpKey tmpInterpKey = new InterpKey(null, null, 0.0f);
    public final VarFontKey tmpVarFontKey = new VarFontKey(0, 0, new ArrayList());

    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class InterpKey {
        public Font l;
        public float progress;
        public Font r;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof InterpKey)) {
                return false;
            }
            InterpKey interpKey = (InterpKey) obj;
            return Intrinsics.areEqual(this.l, interpKey.l) && Intrinsics.areEqual(this.r, interpKey.r) && Intrinsics.areEqual(Float.valueOf(this.progress), Float.valueOf(interpKey.progress));
        }

        public final int hashCode() {
            Font font = this.l;
            int i = 0;
            int hashCode = (font == null ? 0 : font.hashCode()) * 31;
            Font font2 = this.r;
            if (font2 != null) {
                i = font2.hashCode();
            }
            return Float.hashCode(this.progress) + ((hashCode + i) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("InterpKey(l=");
            m.append(this.l);
            m.append(", r=");
            m.append(this.r);
            m.append(", progress=");
            m.append(this.progress);
            m.append(')');
            return m.toString();
        }

        public InterpKey(Font font, Font font2, float f) {
            this.l = font;
            this.r = font2;
            this.progress = f;
        }
    }

    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class VarFontKey {
        public int index;
        public final List<FontVariationAxis> sortedAxes;
        public int sourceId;

        public VarFontKey() {
            throw null;
        }

        public VarFontKey(int i, int i2, ArrayList arrayList) {
            this.sourceId = i;
            this.index = i2;
            this.sortedAxes = arrayList;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof VarFontKey)) {
                return false;
            }
            VarFontKey varFontKey = (VarFontKey) obj;
            return this.sourceId == varFontKey.sourceId && this.index == varFontKey.index && Intrinsics.areEqual(this.sortedAxes, varFontKey.sortedAxes);
        }

        public final int hashCode() {
            return this.sortedAxes.hashCode() + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.index, Integer.hashCode(this.sourceId) * 31, 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("VarFontKey(sourceId=");
            m.append(this.sourceId);
            m.append(", index=");
            m.append(this.index);
            m.append(", sortedAxes=");
            m.append(this.sortedAxes);
            m.append(')');
            return m.toString();
        }
    }

    public static float coerceInWithStep(float f, float f2, float f3) {
        if (0.0f <= f2) {
            if (f < 0.0f) {
                f = 0.0f;
            } else if (f > f2) {
                f = f2;
            }
            return ((int) (f / f3)) * f3;
        }
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + f2 + " is less than minimum 0.0.");
    }

    public final Font lerp(Font font, Font font2, float f) {
        boolean z;
        boolean z2;
        boolean z3;
        String str;
        String str2;
        int i;
        int i2;
        FontVariationAxis fontVariationAxis;
        boolean z4;
        if (f == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return font;
        }
        if (f == 1.0f) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            return font2;
        }
        FontVariationAxis[] axes = font.getAxes();
        if (axes == null) {
            axes = EMPTY_AXES;
        }
        FontVariationAxis[] axes2 = font2.getAxes();
        if (axes2 == null) {
            axes2 = EMPTY_AXES;
        }
        if (axes.length == 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z3) {
            if (axes2.length == 0) {
                z4 = true;
            } else {
                z4 = false;
            }
            if (z4) {
                return font;
            }
        }
        InterpKey interpKey = this.tmpInterpKey;
        Objects.requireNonNull(interpKey);
        interpKey.l = font;
        interpKey.r = font2;
        interpKey.progress = f;
        Font font3 = this.interpCache.get(this.tmpInterpKey);
        if (font3 != null) {
            return font3;
        }
        FontInterpolator$lerp$newAxes$1 fontInterpolator$lerp$newAxes$1 = new FontInterpolator$lerp$newAxes$1(this, f);
        if (axes.length > 1) {
            Comparator fontInterpolator$lerp$$inlined$sortBy$1 = new Comparator() { // from class: com.android.keyguard.FontInterpolator$lerp$$inlined$sortBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return R$styleable.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                }
            };
            if (axes.length > 1) {
                Arrays.sort(axes, fontInterpolator$lerp$$inlined$sortBy$1);
            }
        }
        if (axes2.length > 1) {
            Comparator fontInterpolator$lerp$$inlined$sortBy$2 = new Comparator() { // from class: com.android.keyguard.FontInterpolator$lerp$$inlined$sortBy$2
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return R$styleable.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                }
            };
            if (axes2.length > 1) {
                Arrays.sort(axes2, fontInterpolator$lerp$$inlined$sortBy$2);
            }
        }
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= axes.length && i4 >= axes2.length) {
                break;
            }
            if (i3 < axes.length) {
                str = axes[i3].getTag();
            } else {
                str = null;
            }
            if (i4 < axes2.length) {
                str2 = axes2[i4].getTag();
            } else {
                str2 = null;
            }
            if (str == null) {
                i = 1;
            } else if (str2 == null) {
                i = -1;
            } else {
                i = str.compareTo(str2);
            }
            if (i == 0) {
                Intrinsics.checkNotNull(str);
                int i5 = i4 + 1;
                fontVariationAxis = new FontVariationAxis(str, ((Number) fontInterpolator$lerp$newAxes$1.invoke(str, Float.valueOf(axes[i3].getStyleValue()), Float.valueOf(axes2[i4].getStyleValue()))).floatValue());
                i3++;
                i2 = i5;
            } else if (i < 0) {
                Intrinsics.checkNotNull(str);
                FontVariationAxis fontVariationAxis2 = new FontVariationAxis(str, ((Number) fontInterpolator$lerp$newAxes$1.invoke(str, Float.valueOf(axes[i3].getStyleValue()), null)).floatValue());
                i2 = i4;
                fontVariationAxis = fontVariationAxis2;
                i3++;
            } else {
                Intrinsics.checkNotNull(str2);
                i2 = i4 + 1;
                fontVariationAxis = new FontVariationAxis(str2, ((Number) fontInterpolator$lerp$newAxes$1.invoke(str2, null, Float.valueOf(axes2[i4].getStyleValue()))).floatValue());
            }
            arrayList.add(fontVariationAxis);
            i4 = i2;
        }
        VarFontKey varFontKey = this.tmpVarFontKey;
        Objects.requireNonNull(varFontKey);
        varFontKey.sourceId = font.getSourceIdentifier();
        varFontKey.index = font.getTtcIndex();
        varFontKey.sortedAxes.clear();
        varFontKey.sortedAxes.addAll(arrayList);
        List<FontVariationAxis> list = varFontKey.sortedAxes;
        if (list.size() > 1) {
            CollectionsKt__MutableCollectionsJVMKt.sortWith(list, new Comparator() { // from class: com.android.keyguard.FontInterpolator$VarFontKey$set$$inlined$sortBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return R$styleable.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                }
            });
        }
        Font font4 = this.verFontCache.get(this.tmpVarFontKey);
        if (font4 != null) {
            this.interpCache.put(new InterpKey(font, font2, f), font4);
            return font4;
        }
        Font.Builder builder = new Font.Builder(font);
        Object[] array = arrayList.toArray(new FontVariationAxis[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        Font build = builder.setFontVariationSettings((FontVariationAxis[]) array).build();
        this.interpCache.put(new InterpKey(font, font2, f), build);
        HashMap<VarFontKey, Font> hashMap = this.verFontCache;
        int sourceIdentifier = font.getSourceIdentifier();
        int ttcIndex = font.getTtcIndex();
        ArrayList arrayList2 = new ArrayList(arrayList);
        if (arrayList2.size() > 1) {
            CollectionsKt__MutableCollectionsJVMKt.sortWith(arrayList2, new Comparator() { // from class: com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return R$styleable.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                }
            });
        }
        hashMap.put(new VarFontKey(sourceIdentifier, ttcIndex, arrayList2), build);
        return build;
    }
}
