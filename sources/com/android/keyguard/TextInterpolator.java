package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.text.PositionedGlyphs;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextShaper;
import android.util.MathUtils;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import com.android.internal.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TextInterpolator.kt */
/* loaded from: classes.dex */
public final class TextInterpolator {
    public final TextPaint basePaint;
    public Layout layout;
    public float progress;
    public final TextPaint targetPaint;
    public List<Line> lines = EmptyList.INSTANCE;
    public final FontInterpolator fontInterpolator = new FontInterpolator();
    public final TextPaint tmpDrawPaint = new TextPaint();
    public float[] tmpPositionArray = new float[20];

    /* compiled from: TextInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class FontRun {
        public Font baseFont;
        public final int end;
        public final int start;
        public Font targetFont;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FontRun)) {
                return false;
            }
            FontRun fontRun = (FontRun) obj;
            return this.start == fontRun.start && this.end == fontRun.end && Intrinsics.areEqual(this.baseFont, fontRun.baseFont) && Intrinsics.areEqual(this.targetFont, fontRun.targetFont);
        }

        public final int hashCode() {
            int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.end, Integer.hashCode(this.start) * 31, 31);
            return this.targetFont.hashCode() + ((this.baseFont.hashCode() + m) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FontRun(start=");
            m.append(this.start);
            m.append(", end=");
            m.append(this.end);
            m.append(", baseFont=");
            m.append(this.baseFont);
            m.append(", targetFont=");
            m.append(this.targetFont);
            m.append(')');
            return m.toString();
        }

        public FontRun(int i, int i2, Font font, Font font2) {
            this.start = i;
            this.end = i2;
            this.baseFont = font;
            this.targetFont = font2;
        }
    }

    public final void shapeText(Layout layout) {
        ArrayList arrayList;
        float[] fArr;
        float[] fArr2;
        ArrayList shapeText = shapeText(layout, this.basePaint);
        ArrayList shapeText2 = shapeText(layout, this.targetPaint);
        if (shapeText.size() == shapeText2.size()) {
            Iterator it = shapeText.iterator();
            Iterator it2 = shapeText2.iterator();
            int i = 10;
            ArrayList arrayList2 = new ArrayList(Math.min(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(shapeText, 10), CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(shapeText2, 10)));
            int i2 = 0;
            TextInterpolator textInterpolator = this;
            while (it.hasNext() && it2.hasNext()) {
                Object next = it.next();
                List list = (List) it2.next();
                List list2 = (List) next;
                Iterator it3 = list2.iterator();
                Iterator it4 = list.iterator();
                ArrayList arrayList3 = new ArrayList(Math.min(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list2, i), CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, i)));
                while (it3.hasNext() && it4.hasNext()) {
                    Object next2 = it3.next();
                    PositionedGlyphs positionedGlyphs = (PositionedGlyphs) it4.next();
                    PositionedGlyphs positionedGlyphs2 = (PositionedGlyphs) next2;
                    if (positionedGlyphs2.glyphCount() == positionedGlyphs.glyphCount()) {
                        int glyphCount = positionedGlyphs2.glyphCount();
                        int[] iArr = new int[glyphCount];
                        for (int i3 = 0; i3 < glyphCount; i3++) {
                            int glyphId = positionedGlyphs2.getGlyphId(i3);
                            if (glyphId == positionedGlyphs.getGlyphId(i3)) {
                                iArr[i3] = glyphId;
                            } else {
                                StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("Inconsistent glyph ID at ", i3, " in line ");
                                m.append(textInterpolator.lines.size());
                                throw new IllegalArgumentException(m.toString().toString());
                            }
                        }
                        float[] fArr3 = new float[glyphCount];
                        for (int i4 = 0; i4 < glyphCount; i4++) {
                            fArr3[i4] = positionedGlyphs2.getGlyphX(i4);
                        }
                        float[] fArr4 = new float[glyphCount];
                        for (int i5 = 0; i5 < glyphCount; i5++) {
                            fArr4[i5] = positionedGlyphs2.getGlyphY(i5);
                        }
                        float[] fArr5 = new float[glyphCount];
                        for (int i6 = 0; i6 < glyphCount; i6++) {
                            fArr5[i6] = positionedGlyphs.getGlyphX(i6);
                        }
                        float[] fArr6 = new float[glyphCount];
                        for (int i7 = 0; i7 < glyphCount; i7++) {
                            fArr6[i7] = positionedGlyphs.getGlyphY(i7);
                        }
                        ArrayList arrayList4 = new ArrayList();
                        if (glyphCount != 0) {
                            it = it;
                            Font font = positionedGlyphs2.getFont(0);
                            Font font2 = positionedGlyphs.getFont(0);
                            it2 = it2;
                            it3 = it3;
                            it4 = it4;
                            fArr2 = fArr6;
                            if (font.getTtcIndex() == font2.getTtcIndex() && font.getSourceIdentifier() == font2.getSourceIdentifier()) {
                                int i8 = 0;
                                int i9 = 1;
                                arrayList2 = arrayList2;
                                arrayList = arrayList3;
                                Font font3 = font;
                                int i10 = i2;
                                while (i9 < glyphCount) {
                                    i9++;
                                    Font font4 = positionedGlyphs2.getFont(i9);
                                    Font font5 = positionedGlyphs.getFont(i9);
                                    if (font3 == font4) {
                                        positionedGlyphs = positionedGlyphs;
                                        if (!(font2 == font5)) {
                                            throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Base font has not changed at ", i9, " but target font has changed.").toString());
                                        }
                                    } else if (font2 != font5) {
                                        positionedGlyphs = positionedGlyphs;
                                        arrayList4.add(new FontRun(i8, i9, font3, font2));
                                        int max = Math.max(i10, i9 - i8);
                                        if (font4.getTtcIndex() == font5.getTtcIndex() && font4.getSourceIdentifier() == font5.getSourceIdentifier()) {
                                            i10 = max;
                                            font3 = font4;
                                            font2 = font5;
                                            i8 = i9;
                                        } else {
                                            throw new IllegalArgumentException(("Cannot interpolate font at " + i9 + " (" + font4 + " vs " + font5 + ')').toString());
                                        }
                                    } else {
                                        throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Base font has changed at ", i9, " but target font has not changed.").toString());
                                    }
                                    fArr5 = fArr5;
                                    positionedGlyphs2 = positionedGlyphs2;
                                }
                                fArr = fArr5;
                                arrayList4.add(new FontRun(i8, glyphCount, font3, font2));
                                i2 = Math.max(i10, glyphCount - i8);
                            } else {
                                throw new IllegalArgumentException(("Cannot interpolate font at 0 (" + font + " vs " + font2 + ')').toString());
                            }
                        } else {
                            fArr = fArr5;
                            it = it;
                            it2 = it2;
                            arrayList2 = arrayList2;
                            it3 = it3;
                            it4 = it4;
                            arrayList = arrayList3;
                            fArr2 = fArr6;
                        }
                        arrayList.add(new Run(iArr, fArr3, fArr4, fArr, fArr2, arrayList4));
                        textInterpolator = this;
                        arrayList3 = arrayList;
                    } else {
                        throw new IllegalArgumentException(Intrinsics.stringPlus("Inconsistent glyph count at line ", Integer.valueOf(textInterpolator.lines.size())).toString());
                    }
                }
                arrayList2.add(new Line(arrayList3));
                i = 10;
                i2 = i2;
                arrayList2 = arrayList2;
                it = it;
                it2 = it2;
            }
            textInterpolator.lines = arrayList2;
            int i11 = i2 * 2;
            if (textInterpolator.tmpPositionArray.length < i11) {
                textInterpolator.tmpPositionArray = new float[i11];
                return;
            }
            return;
        }
        throw new IllegalArgumentException("The new layout result has different line count.".toString());
    }

    /* compiled from: TextInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class Line {
        public final List<Run> runs;

        public Line(ArrayList arrayList) {
            this.runs = arrayList;
        }
    }

    /* compiled from: TextInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class Run {
        public final float[] baseX;
        public final float[] baseY;
        public final List<FontRun> fontRuns;
        public final int[] glyphIds;
        public final float[] targetX;
        public final float[] targetY;

        public Run(int[] iArr, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4, ArrayList arrayList) {
            this.glyphIds = iArr;
            this.baseX = fArr;
            this.baseY = fArr2;
            this.targetX = fArr3;
            this.targetY = fArr4;
            this.fontRuns = arrayList;
        }
    }

    public final void drawFontRun(Canvas canvas, Run run, FontRun fontRun, TextPaint textPaint) {
        Objects.requireNonNull(fontRun);
        int i = fontRun.start;
        int i2 = fontRun.end;
        int i3 = 0;
        while (i < i2) {
            int i4 = i3 + 1;
            this.tmpPositionArray[i3] = MathUtils.lerp(run.baseX[i], run.targetX[i], this.progress);
            this.tmpPositionArray[i4] = MathUtils.lerp(run.baseY[i], run.targetY[i], this.progress);
            i++;
            i3 = i4 + 1;
        }
        int[] iArr = run.glyphIds;
        int i5 = fontRun.start;
        canvas.drawGlyphs(iArr, i5, this.tmpPositionArray, 0, fontRun.end - i5, this.fontInterpolator.lerp(fontRun.baseFont, fontRun.targetFont, this.progress), textPaint);
    }

    public final void rebase() {
        boolean z;
        float f = this.progress;
        boolean z2 = true;
        if (f == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            if (f != 1.0f) {
                z2 = false;
            }
            if (z2) {
                this.basePaint.set(this.targetPaint);
            } else {
                lerp(this.basePaint, this.targetPaint, f, this.tmpDrawPaint);
                this.basePaint.set(this.tmpDrawPaint);
            }
            for (Line line : this.lines) {
                Objects.requireNonNull(line);
                for (Run run : line.runs) {
                    Objects.requireNonNull(run);
                    int length = run.baseX.length;
                    for (int i = 0; i < length; i++) {
                        float[] fArr = run.baseX;
                        fArr[i] = MathUtils.lerp(fArr[i], run.targetX[i], this.progress);
                        float[] fArr2 = run.baseY;
                        fArr2[i] = MathUtils.lerp(fArr2[i], run.targetY[i], this.progress);
                    }
                    for (FontRun fontRun : run.fontRuns) {
                        FontInterpolator fontInterpolator = this.fontInterpolator;
                        Objects.requireNonNull(fontRun);
                        fontRun.baseFont = fontInterpolator.lerp(fontRun.baseFont, fontRun.targetFont, this.progress);
                    }
                }
            }
            this.progress = 0.0f;
        }
    }

    public TextInterpolator(Layout layout) {
        this.basePaint = new TextPaint(layout.getPaint());
        this.targetPaint = new TextPaint(layout.getPaint());
        this.layout = layout;
        shapeText(layout);
    }

    public static void lerp(TextPaint textPaint, TextPaint textPaint2, float f, TextPaint textPaint3) {
        textPaint3.set((Paint) textPaint);
        textPaint3.setTextSize(MathUtils.lerp(textPaint.getTextSize(), textPaint2.getTextSize(), f));
        textPaint3.setColor(ColorUtils.blendARGB(textPaint.getColor(), textPaint2.getColor(), f));
    }

    public static ArrayList shapeText(Layout layout, TextPaint textPaint) {
        ArrayList arrayList = new ArrayList();
        int lineCount = layout.getLineCount();
        int i = 0;
        while (i < lineCount) {
            i++;
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i) - lineStart;
            final ArrayList arrayList2 = new ArrayList();
            TextShaper.shapeText(layout.getText(), lineStart, lineEnd, layout.getTextDirectionHeuristic(), textPaint, new TextShaper.GlyphsConsumer() { // from class: com.android.keyguard.TextInterpolator$shapeText$3
                public final void accept(int i2, int i3, PositionedGlyphs positionedGlyphs, TextPaint textPaint2) {
                    arrayList2.add(positionedGlyphs);
                }
            });
            arrayList.add(arrayList2);
        }
        return arrayList;
    }
}
