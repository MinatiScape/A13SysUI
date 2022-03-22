package com.android.keyguard;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.animation.PathInterpolator;
import android.widget.TextView;
import com.android.keyguard.TextInterpolator;
import com.android.systemui.R$styleable;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: AnimatableClockView.kt */
/* loaded from: classes.dex */
public final class AnimatableClockView extends TextView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final int chargeAnimationDelay;
    public String descFormat;
    public int dozingColor;
    public final int dozingWeightInternal;
    public String format;
    public final boolean isSingleLineInternal;
    public int lockScreenColor;
    public final int lockScreenWeightInternal;
    public AnimatableClockView$setTextStyle$1 onTextAnimatorInitialized;
    public TextAnimator textAnimator;
    public final Calendar time;

    /* compiled from: AnimatableClockView.kt */
    /* loaded from: classes.dex */
    public interface DozeStateGetter {
    }

    /* compiled from: AnimatableClockView.kt */
    /* loaded from: classes.dex */
    public static final class Patterns {
        public static String sCacheKey;
        public static String sClockView12;
        public static String sClockView24;
    }

    public AnimatableClockView(Context context) {
        this(context, null, 0, 0, 14, null);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, null);
    }

    public /* synthetic */ AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0029, code lost:
        if (r0 != false) goto L_0x002b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002b, code lost:
        r1 = r1 + 100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0016, code lost:
        if (r0 != false) goto L_0x002b;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void animateDoze(boolean r12, boolean r13) {
        /*
            r11 = this;
            r0 = 1
            r1 = 0
            r2 = 100
            if (r12 == 0) goto L_0x0019
            android.content.res.Resources r3 = r11.getResources()
            android.content.res.Configuration r3 = r3.getConfiguration()
            int r3 = r3.fontWeightAdjustment
            if (r3 <= r2) goto L_0x0013
            goto L_0x0014
        L_0x0013:
            r0 = r1
        L_0x0014:
            int r1 = r11.dozingWeightInternal
            if (r0 == 0) goto L_0x002d
            goto L_0x002b
        L_0x0019:
            android.content.res.Resources r3 = r11.getResources()
            android.content.res.Configuration r3 = r3.getConfiguration()
            int r3 = r3.fontWeightAdjustment
            if (r3 <= r2) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r0 = r1
        L_0x0027:
            int r1 = r11.lockScreenWeightInternal
            if (r0 == 0) goto L_0x002d
        L_0x002b:
            int r1 = r1 + 100
        L_0x002d:
            r3 = r1
            if (r12 == 0) goto L_0x0033
            int r12 = r11.dozingColor
            goto L_0x0035
        L_0x0033:
            int r12 = r11.lockScreenColor
        L_0x0035:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r12)
            r6 = 300(0x12c, double:1.48E-321)
            r8 = 0
            r10 = 0
            r2 = r11
            r5 = r13
            r2.setTextStyle(r3, r4, r5, r6, r8, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.AnimatableClockView.animateDoze(boolean, boolean):void");
    }

    public final void setTextStyle(int i, Integer num, boolean z, PathInterpolator pathInterpolator, long j, long j2, AnimatableClockView$animateCharge$startAnimPhase2$1 animatableClockView$animateCharge$startAnimPhase2$1) {
        TextAnimator textAnimator = this.textAnimator;
        if (textAnimator != null) {
            textAnimator.setTextStyle(i, -1.0f, num, z, j, pathInterpolator, j2, animatableClockView$animateCharge$startAnimPhase2$1);
        } else {
            this.onTextAnimatorInitialized = new AnimatableClockView$setTextStyle$1(this, i, num, j, pathInterpolator, j2, animatableClockView$animateCharge$startAnimPhase2$1);
        }
    }

    /* JADX WARN: Finally extract failed */
    public AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.time = Calendar.getInstance();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AnimatableClockView, i, i2);
        try {
            this.dozingWeightInternal = obtainStyledAttributes.getInt(1, 100);
            this.lockScreenWeightInternal = obtainStyledAttributes.getInt(2, 300);
            this.chargeAnimationDelay = obtainStyledAttributes.getInt(0, 200);
            obtainStyledAttributes.recycle();
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.TextView, i, i2);
            try {
                boolean z = obtainStyledAttributes2.getBoolean(32, false);
                obtainStyledAttributes2.recycle();
                this.isSingleLineInternal = z;
                refreshFormat();
            } catch (Throwable th) {
                obtainStyledAttributes2.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            obtainStyledAttributes.recycle();
            throw th2;
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void onDraw(Canvas canvas) {
        float f;
        TextAnimator textAnimator = this.textAnimator;
        if (textAnimator != null) {
            TextInterpolator textInterpolator = textAnimator.textInterpolator;
            Objects.requireNonNull(textInterpolator);
            TextInterpolator.lerp(textInterpolator.basePaint, textInterpolator.targetPaint, textInterpolator.progress, textInterpolator.tmpDrawPaint);
            int i = 0;
            for (Object obj : textInterpolator.lines) {
                int i2 = i + 1;
                if (i >= 0) {
                    TextInterpolator.Line line = (TextInterpolator.Line) obj;
                    Objects.requireNonNull(line);
                    for (TextInterpolator.Run run : line.runs) {
                        canvas.save();
                        try {
                            Layout layout = textInterpolator.layout;
                            if (layout.getParagraphDirection(i) == 1) {
                                f = layout.getLineLeft(i);
                            } else {
                                f = layout.getLineRight(i);
                            }
                            canvas.translate(f, textInterpolator.layout.getLineBaseline(i));
                            Objects.requireNonNull(run);
                            for (TextInterpolator.FontRun fontRun : run.fontRuns) {
                                textInterpolator.drawFontRun(canvas, run, fontRun, textInterpolator.tmpDrawPaint);
                            }
                        } finally {
                            canvas.restore();
                        }
                    }
                    i = i2;
                } else {
                    SetsKt__SetsKt.throwIndexOverflow();
                    throw null;
                }
            }
        }
    }

    public final void refreshTime() {
        this.time.setTimeInMillis(System.currentTimeMillis());
        setText(DateFormat.format(this.format, this.time));
        setContentDescription(DateFormat.format(this.descFormat, this.time));
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    @Override // android.widget.TextView, android.view.View
    @SuppressLint({"DrawAllocation"})
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextAnimator textAnimator = this.textAnimator;
        if (textAnimator == null) {
            this.textAnimator = new TextAnimator(getLayout(), new AnimatableClockView$onMeasure$1(this));
            AnimatableClockView$setTextStyle$1 animatableClockView$setTextStyle$1 = this.onTextAnimatorInitialized;
            if (animatableClockView$setTextStyle$1 != null) {
                animatableClockView$setTextStyle$1.run();
            }
            this.onTextAnimatorInitialized = null;
            return;
        }
        Layout layout = getLayout();
        TextInterpolator textInterpolator = textAnimator.textInterpolator;
        Objects.requireNonNull(textInterpolator);
        textInterpolator.layout = layout;
        textInterpolator.shapeText(layout);
    }

    public final void refreshFormat() {
        String str;
        String str2;
        int i;
        boolean z;
        Context context = getContext();
        Locale locale = Locale.getDefault();
        Resources resources = context.getResources();
        String string = resources.getString(2131952119);
        String string2 = resources.getString(2131952120);
        String str3 = locale.toString() + ((Object) string) + ((Object) string2);
        if (!Intrinsics.areEqual(str3, Patterns.sCacheKey)) {
            String bestDateTimePattern = DateFormat.getBestDateTimePattern(locale, string);
            Patterns.sClockView12 = bestDateTimePattern;
            if (!StringsKt__StringsKt.contains$default(string, "a")) {
                String replace = new Regex("a").replace(bestDateTimePattern);
                int length = replace.length() - 1;
                int i2 = 0;
                boolean z2 = false;
                while (i2 <= length) {
                    if (!z2) {
                        i = i2;
                    } else {
                        i = length;
                    }
                    if (Intrinsics.compare(replace.charAt(i), 32) <= 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z2) {
                        if (!z) {
                            z2 = true;
                        } else {
                            i2++;
                        }
                    } else if (!z) {
                        break;
                    } else {
                        length--;
                    }
                }
                Patterns.sClockView12 = replace.subSequence(i2, length + 1).toString();
            }
            Patterns.sClockView24 = DateFormat.getBestDateTimePattern(locale, string2);
            Patterns.sCacheKey = str3;
        }
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        boolean z3 = this.isSingleLineInternal;
        if (z3 && is24HourFormat) {
            str = Patterns.sClockView24;
        } else if (!z3 && is24HourFormat) {
            str = "HH\nmm";
        } else if (!z3 || is24HourFormat) {
            str = "hh\nmm";
        } else {
            str = Patterns.sClockView12;
        }
        this.format = str;
        if (is24HourFormat) {
            str2 = Patterns.sClockView24;
        } else {
            str2 = Patterns.sClockView12;
        }
        this.descFormat = str2;
        refreshTime();
    }

    public final void setTextStyle(int i, Integer num, boolean z, long j, long j2, AnimatableClockView$animateCharge$startAnimPhase2$1 animatableClockView$animateCharge$startAnimPhase2$1) {
        setTextStyle(i, num, z, null, j, j2, animatableClockView$animateCharge$startAnimPhase2$1);
    }
}
