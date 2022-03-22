package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.text.PositionedGlyphs;
import android.text.Layout;
import android.text.TextPaint;
import android.util.SparseArray;
import com.android.keyguard.TextInterpolator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TextAnimator.kt */
/* loaded from: classes.dex */
public final class TextAnimator {
    public ValueAnimator animator;
    public final Function0<Unit> invalidateCallback;
    public TextInterpolator textInterpolator;
    public final SparseArray<Typeface> typefaceCache = new SparseArray<>();

    /* JADX WARN: Multi-variable type inference failed */
    public final void setTextStyle(int i, float f, Integer num, boolean z, long j, TimeInterpolator timeInterpolator, long j2, final Runnable runnable) {
        boolean z2;
        long j3;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        if (z) {
            this.animator.cancel();
            this.textInterpolator.rebase();
        }
        if (f >= 0.0f) {
            TextInterpolator textInterpolator = this.textInterpolator;
            Objects.requireNonNull(textInterpolator);
            textInterpolator.targetPaint.setTextSize(f);
        }
        if (i >= 0) {
            TextInterpolator textInterpolator2 = this.textInterpolator;
            Objects.requireNonNull(textInterpolator2);
            TextPaint textPaint = textInterpolator2.targetPaint;
            SparseArray<Typeface> sparseArray = this.typefaceCache;
            TextAnimator$setTextStyle$1 textAnimator$setTextStyle$1 = new TextAnimator$setTextStyle$1(this, i);
            Object obj = sparseArray.get(i);
            if (obj == null) {
                obj = textAnimator$setTextStyle$1.invoke();
                sparseArray.put(i, obj);
            }
            textPaint.setTypeface((Typeface) obj);
        }
        if (num != null) {
            TextInterpolator textInterpolator3 = this.textInterpolator;
            Objects.requireNonNull(textInterpolator3);
            textInterpolator3.targetPaint.setColor(num.intValue());
        }
        TextInterpolator textInterpolator4 = this.textInterpolator;
        Objects.requireNonNull(textInterpolator4);
        ArrayList shapeText = TextInterpolator.shapeText(textInterpolator4.layout, textInterpolator4.targetPaint);
        if (shapeText.size() == textInterpolator4.lines.size()) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            List<TextInterpolator.Line> list = textInterpolator4.lines;
            Iterator it = list.iterator();
            Iterator it2 = shapeText.iterator();
            int i2 = 10;
            ArrayList arrayList = new ArrayList(Math.min(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list, 10), CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(shapeText, 10)));
            while (it.hasNext() && it2.hasNext()) {
                Object next = it.next();
                List list2 = (List) it2.next();
                TextInterpolator.Line line = (TextInterpolator.Line) next;
                Objects.requireNonNull(line);
                List<TextInterpolator.Run> list3 = line.runs;
                Iterator<T> it3 = list3.iterator();
                Iterator it4 = list2.iterator();
                ArrayList arrayList2 = new ArrayList(Math.min(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list3, i2), CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(list2, i2)));
                while (it3.hasNext() && it4.hasNext()) {
                    Object next2 = it3.next();
                    PositionedGlyphs positionedGlyphs = (PositionedGlyphs) it4.next();
                    TextInterpolator.Run run = (TextInterpolator.Run) next2;
                    int glyphCount = positionedGlyphs.glyphCount();
                    Objects.requireNonNull(run);
                    if (glyphCount == run.glyphIds.length) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3) {
                        for (TextInterpolator.FontRun fontRun : run.fontRuns) {
                            Objects.requireNonNull(fontRun);
                            Font font = positionedGlyphs.getFont(fontRun.start);
                            int i3 = fontRun.start;
                            for (int i4 = fontRun.end; i3 < i4; i4 = i4) {
                                int i5 = i3 + 1;
                                int glyphId = positionedGlyphs.getGlyphId(fontRun.start);
                                int[] iArr = run.glyphIds;
                                int i6 = fontRun.start;
                                if (glyphId == iArr[i6]) {
                                    z5 = true;
                                } else {
                                    z5 = false;
                                }
                                if (z5) {
                                    if (font == positionedGlyphs.getFont(i3)) {
                                        z6 = true;
                                    } else {
                                        z6 = false;
                                    }
                                    if (z6) {
                                        i3 = i5;
                                        it = it;
                                        it2 = it2;
                                    } else {
                                        throw new IllegalArgumentException(("The new layout has different font run. " + font + " vs " + positionedGlyphs.getFont(i3) + " at " + i3).toString());
                                    }
                                } else {
                                    throw new IllegalArgumentException(Intrinsics.stringPlus("The new layout has different glyph ID at ", Integer.valueOf(i6)).toString());
                                }
                            }
                            Font font2 = fontRun.baseFont;
                            if (font.getTtcIndex() == font2.getTtcIndex() && font.getSourceIdentifier() == font2.getSourceIdentifier()) {
                                z4 = true;
                            } else {
                                z4 = false;
                            }
                            if (z4) {
                                fontRun.targetFont = font;
                                it = it;
                                it2 = it2;
                            } else {
                                throw new IllegalArgumentException(("New font cannot be interpolated with existing font. " + font + ", " + fontRun.baseFont).toString());
                            }
                        }
                        int length = run.baseX.length;
                        for (int i7 = 0; i7 < length; i7++) {
                            run.targetX[i7] = positionedGlyphs.getGlyphX(i7);
                            run.targetY[i7] = positionedGlyphs.getGlyphY(i7);
                        }
                        arrayList2.add(Unit.INSTANCE);
                        it = it;
                        it2 = it2;
                    } else {
                        throw new IllegalArgumentException("The new layout has different glyph count.".toString());
                    }
                }
                arrayList.add(arrayList2);
                it = it;
                it2 = it2;
                i2 = 10;
            }
            if (z) {
                this.animator.setStartDelay(j2);
                ValueAnimator valueAnimator = this.animator;
                if (j == -1) {
                    j3 = 300;
                } else {
                    j3 = j;
                }
                valueAnimator.setDuration(j3);
                if (timeInterpolator != null) {
                    this.animator.setInterpolator(timeInterpolator);
                }
                if (runnable != null) {
                    this.animator.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.TextAnimator$setTextStyle$listener$1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationCancel(Animator animator) {
                            TextAnimator textAnimator = this;
                            Objects.requireNonNull(textAnimator);
                            textAnimator.animator.removeListener(this);
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            runnable.run();
                            TextAnimator textAnimator = this;
                            Objects.requireNonNull(textAnimator);
                            textAnimator.animator.removeListener(this);
                        }
                    });
                }
                this.animator.start();
                return;
            }
            TextInterpolator textInterpolator5 = this.textInterpolator;
            Objects.requireNonNull(textInterpolator5);
            textInterpolator5.progress = 1.0f;
            this.textInterpolator.rebase();
            this.invalidateCallback.invoke();
            return;
        }
        throw new IllegalStateException("The new layout result has different line count.".toString());
    }

    public TextAnimator(Layout layout, Function0<Unit> function0) {
        this.invalidateCallback = function0;
        this.textInterpolator = new TextInterpolator(layout);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f);
        ofFloat.setDuration(300L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.keyguard.TextAnimator$animator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                TextAnimator textAnimator = TextAnimator.this;
                Objects.requireNonNull(textAnimator);
                TextInterpolator textInterpolator = textAnimator.textInterpolator;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                float floatValue = ((Float) animatedValue).floatValue();
                Objects.requireNonNull(textInterpolator);
                textInterpolator.progress = floatValue;
                TextAnimator.this.invalidateCallback.invoke();
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.TextAnimator$animator$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                TextAnimator textAnimator = TextAnimator.this;
                Objects.requireNonNull(textAnimator);
                textAnimator.textInterpolator.rebase();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                TextAnimator textAnimator = TextAnimator.this;
                Objects.requireNonNull(textAnimator);
                textAnimator.textInterpolator.rebase();
            }
        });
        this.animator = ofFloat;
    }
}
