package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.StaticLayoutBuilderCompat;
import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.resources.TextAppearance;
import java.util.Objects;
import java.util.WeakHashMap;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
public final class CollapsingTextHelper {
    public boolean boundsChanged;
    public float collapsedDrawX;
    public float collapsedDrawY;
    public CancelableFontCallback collapsedFontCallback;
    public float collapsedLetterSpacing;
    public ColorStateList collapsedShadowColor;
    public float collapsedShadowDx;
    public float collapsedShadowDy;
    public float collapsedShadowRadius;
    public float collapsedTextBlend;
    public ColorStateList collapsedTextColor;
    public float collapsedTextWidth;
    public Typeface collapsedTypeface;
    public float currentDrawX;
    public float currentDrawY;
    public int currentOffsetY;
    public float currentTextSize;
    public Typeface currentTypeface;
    public boolean drawTitle;
    public float expandedDrawX;
    public float expandedDrawY;
    public float expandedFirstLineDrawX;
    public CancelableFontCallback expandedFontCallback;
    public float expandedFraction;
    public float expandedLetterSpacing;
    public int expandedLineCount;
    public ColorStateList expandedShadowColor;
    public float expandedShadowDx;
    public float expandedShadowDy;
    public float expandedShadowRadius;
    public float expandedTextBlend;
    public ColorStateList expandedTextColor;
    public Bitmap expandedTitleTexture;
    public Typeface expandedTypeface;
    public boolean fadeModeEnabled;
    public float fadeModeStartFraction;
    public float fadeModeThresholdFraction;
    public boolean isRtl;
    public TimeInterpolator positionInterpolator;
    public float scale;
    public int[] state;
    public CharSequence text;
    public StaticLayout textLayout;
    public final TextPaint textPaint;
    public TimeInterpolator textSizeInterpolator;
    public CharSequence textToDraw;
    public CharSequence textToDrawCollapsed;
    public final TextPaint tmpPaint;
    public final View view;
    public int expandedTextGravity = 16;
    public int collapsedTextGravity = 16;
    public float expandedTextSize = 15.0f;
    public float collapsedTextSize = 15.0f;
    public boolean isRtlTextDirectionHeuristicsEnabled = true;
    public int maxLines = 1;
    public float lineSpacingMultiplier = 1.0f;
    public final Rect collapsedBounds = new Rect();
    public final Rect expandedBounds = new Rect();
    public final RectF currentBounds = new RectF();

    public final int getCurrentColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            return 0;
        }
        int[] iArr = this.state;
        if (iArr != null) {
            return colorStateList.getColorForState(iArr, 0);
        }
        return colorStateList.getDefaultColor();
    }

    public final void setExpansionFraction(float f) {
        float f2;
        float f3;
        Rect rect;
        float clamp = AtomicFU.clamp(f, 0.0f, 1.0f);
        if (clamp != this.expandedFraction) {
            this.expandedFraction = clamp;
            if (this.fadeModeEnabled) {
                RectF rectF = this.currentBounds;
                if (clamp < this.fadeModeThresholdFraction) {
                    rect = this.expandedBounds;
                } else {
                    rect = this.collapsedBounds;
                }
                rectF.set(rect);
            } else {
                this.currentBounds.left = lerp(this.expandedBounds.left, this.collapsedBounds.left, clamp, this.positionInterpolator);
                this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, clamp, this.positionInterpolator);
                this.currentBounds.right = lerp(this.expandedBounds.right, this.collapsedBounds.right, clamp, this.positionInterpolator);
                this.currentBounds.bottom = lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, clamp, this.positionInterpolator);
            }
            if (!this.fadeModeEnabled) {
                this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, clamp, this.positionInterpolator);
                this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, clamp, this.positionInterpolator);
                setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, clamp, this.textSizeInterpolator));
                f2 = clamp;
            } else if (clamp < this.fadeModeThresholdFraction) {
                this.currentDrawX = this.expandedDrawX;
                this.currentDrawY = this.expandedDrawY;
                setInterpolatedTextSize(this.expandedTextSize);
                f2 = 0.0f;
            } else {
                this.currentDrawX = this.collapsedDrawX;
                this.currentDrawY = this.collapsedDrawY - Math.max(0, this.currentOffsetY);
                setInterpolatedTextSize(this.collapsedTextSize);
                f2 = 1.0f;
            }
            FastOutSlowInInterpolator fastOutSlowInInterpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
            this.collapsedTextBlend = 1.0f - lerp(0.0f, 1.0f, 1.0f - clamp, fastOutSlowInInterpolator);
            View view = this.view;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(view);
            this.expandedTextBlend = lerp(1.0f, 0.0f, clamp, fastOutSlowInInterpolator);
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this.view);
            ColorStateList colorStateList = this.collapsedTextColor;
            ColorStateList colorStateList2 = this.expandedTextColor;
            if (colorStateList != colorStateList2) {
                this.textPaint.setColor(blendColors(getCurrentColor(colorStateList2), getCurrentColor(this.collapsedTextColor), f2));
            } else {
                this.textPaint.setColor(getCurrentColor(colorStateList));
            }
            float f4 = this.collapsedLetterSpacing;
            float f5 = this.expandedLetterSpacing;
            if (f4 != f5) {
                this.textPaint.setLetterSpacing(lerp(f5, f4, clamp, fastOutSlowInInterpolator));
            } else {
                this.textPaint.setLetterSpacing(f4);
            }
            this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, clamp, null), lerp(this.expandedShadowDx, this.collapsedShadowDx, clamp, null), lerp(this.expandedShadowDy, this.collapsedShadowDy, clamp, null), blendColors(getCurrentColor(this.expandedShadowColor), getCurrentColor(this.collapsedShadowColor), clamp));
            if (this.fadeModeEnabled) {
                int alpha = this.textPaint.getAlpha();
                float f6 = this.fadeModeThresholdFraction;
                if (clamp <= f6) {
                    f3 = AnimationUtils.lerp(1.0f, 0.0f, this.fadeModeStartFraction, f6, clamp);
                } else {
                    f3 = AnimationUtils.lerp(0.0f, 1.0f, f6, 1.0f, clamp);
                }
                this.textPaint.setAlpha((int) (f3 * alpha));
            }
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this.view);
        }
    }

    public final void setInterpolatedTextSize(float f) {
        calculateUsingTextSize(f, false);
        View view = this.view;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api16Impl.postInvalidateOnAnimation(view);
    }

    public static int blendColors(int i, int i2, float f) {
        float f2 = 1.0f - f;
        return Color.argb((int) ((Color.alpha(i2) * f) + (Color.alpha(i) * f2)), (int) ((Color.red(i2) * f) + (Color.red(i) * f2)), (int) ((Color.green(i2) * f) + (Color.green(i) * f2)), (int) ((Color.blue(i2) * f) + (Color.blue(i) * f2)));
    }

    public static float lerp(float f, float f2, float f3, TimeInterpolator timeInterpolator) {
        if (timeInterpolator != null) {
            f3 = timeInterpolator.getInterpolation(f3);
        }
        LinearInterpolator linearInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
        return MotionController$$ExternalSyntheticOutline0.m(f2, f, f3, f);
    }

    public final boolean calculateIsRtl(CharSequence charSequence) {
        TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal textDirectionHeuristicInternal;
        View view = this.view;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        boolean z = true;
        if (ViewCompat.Api17Impl.getLayoutDirection(view) != 1) {
            z = false;
        }
        if (!this.isRtlTextDirectionHeuristicsEnabled) {
            return z;
        }
        if (z) {
            textDirectionHeuristicInternal = TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL;
        } else {
            textDirectionHeuristicInternal = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        }
        return textDirectionHeuristicInternal.isRtl(charSequence, charSequence.length());
    }

    public final void calculateUsingTextSize(float f, boolean z) {
        boolean z2;
        float f2;
        boolean z3;
        boolean z4;
        boolean z5;
        StaticLayout staticLayout;
        boolean z6;
        if (this.text != null) {
            float width = this.collapsedBounds.width();
            float width2 = this.expandedBounds.width();
            if (Math.abs(f - this.collapsedTextSize) < 0.001f) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                f2 = this.collapsedTextSize;
                this.scale = 1.0f;
                Typeface typeface = this.currentTypeface;
                Typeface typeface2 = this.collapsedTypeface;
                if (typeface != typeface2) {
                    this.currentTypeface = typeface2;
                    z3 = true;
                } else {
                    z3 = false;
                }
            } else {
                float f3 = this.expandedTextSize;
                Typeface typeface3 = this.currentTypeface;
                Typeface typeface4 = this.expandedTypeface;
                if (typeface3 != typeface4) {
                    this.currentTypeface = typeface4;
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (Math.abs(f - f3) < 0.001f) {
                    z6 = true;
                } else {
                    z6 = false;
                }
                if (z6) {
                    this.scale = 1.0f;
                } else {
                    this.scale = f / this.expandedTextSize;
                }
                float f4 = this.collapsedTextSize / this.expandedTextSize;
                float f5 = width2 * f4;
                if (!z && f5 > width) {
                    width = Math.min(width / f4, width2);
                } else {
                    width = width2;
                }
                f2 = f3;
            }
            if (width > 0.0f) {
                if (this.currentTextSize != f2 || this.boundsChanged || z3) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                this.currentTextSize = f2;
                this.boundsChanged = false;
            }
            if (this.textToDraw == null || z3) {
                this.textPaint.setTextSize(this.currentTextSize);
                this.textPaint.setTypeface(this.currentTypeface);
                TextPaint textPaint = this.textPaint;
                if (this.scale != 1.0f) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                textPaint.setLinearText(z4);
                boolean calculateIsRtl = calculateIsRtl(this.text);
                this.isRtl = calculateIsRtl;
                int i = this.maxLines;
                if (i <= 1 || (calculateIsRtl && !this.fadeModeEnabled)) {
                    z5 = false;
                } else {
                    z5 = true;
                }
                if (!z5) {
                    i = 1;
                }
                try {
                    StaticLayoutBuilderCompat staticLayoutBuilderCompat = new StaticLayoutBuilderCompat(this.text, this.textPaint, (int) width);
                    staticLayoutBuilderCompat.ellipsize = TextUtils.TruncateAt.END;
                    staticLayoutBuilderCompat.isRtl = calculateIsRtl;
                    staticLayoutBuilderCompat.alignment = Layout.Alignment.ALIGN_NORMAL;
                    staticLayoutBuilderCompat.includePad = false;
                    staticLayoutBuilderCompat.maxLines = i;
                    float f6 = this.lineSpacingMultiplier;
                    staticLayoutBuilderCompat.lineSpacingAdd = 0.0f;
                    staticLayoutBuilderCompat.lineSpacingMultiplier = f6;
                    staticLayoutBuilderCompat.hyphenationFrequency = 1;
                    staticLayout = staticLayoutBuilderCompat.build();
                } catch (StaticLayoutBuilderCompat.StaticLayoutBuilderCompatException e) {
                    Log.e("CollapsingTextHelper", e.getCause().getMessage(), e);
                    staticLayout = null;
                }
                Objects.requireNonNull(staticLayout);
                this.textLayout = staticLayout;
                this.textToDraw = staticLayout.getText();
            }
        }
    }

    public final float getCollapsedTextHeight() {
        TextPaint textPaint = this.tmpPaint;
        textPaint.setTextSize(this.collapsedTextSize);
        textPaint.setTypeface(this.collapsedTypeface);
        textPaint.setLetterSpacing(this.collapsedLetterSpacing);
        return -this.tmpPaint.ascent();
    }

    public final void onBoundsChanged() {
        boolean z;
        if (this.collapsedBounds.width() <= 0 || this.collapsedBounds.height() <= 0 || this.expandedBounds.width() <= 0 || this.expandedBounds.height() <= 0) {
            z = false;
        } else {
            z = true;
        }
        this.drawTitle = z;
    }

    public final void recalculate(boolean z) {
        float f;
        int i;
        float f2;
        float f3;
        float f4;
        float f5;
        Rect rect;
        StaticLayout staticLayout;
        if ((this.view.getHeight() > 0 && this.view.getWidth() > 0) || z) {
            float f6 = this.currentTextSize;
            calculateUsingTextSize(this.collapsedTextSize, z);
            CharSequence charSequence = this.textToDraw;
            if (!(charSequence == null || (staticLayout = this.textLayout) == null)) {
                this.textToDrawCollapsed = TextUtils.ellipsize(charSequence, this.textPaint, staticLayout.getWidth(), TextUtils.TruncateAt.END);
            }
            if (this.textToDrawCollapsed != null) {
                TextPaint textPaint = new TextPaint(this.textPaint);
                textPaint.setLetterSpacing(this.collapsedLetterSpacing);
                CharSequence charSequence2 = this.textToDrawCollapsed;
                this.collapsedTextWidth = textPaint.measureText(charSequence2, 0, charSequence2.length());
            } else {
                this.collapsedTextWidth = 0.0f;
            }
            int absoluteGravity = Gravity.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl ? 1 : 0);
            int i2 = absoluteGravity & 112;
            if (i2 == 48) {
                this.collapsedDrawY = this.collapsedBounds.top;
            } else if (i2 != 80) {
                this.collapsedDrawY = this.collapsedBounds.centerY() - ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f);
            } else {
                this.collapsedDrawY = this.textPaint.ascent() + this.collapsedBounds.bottom;
            }
            int i3 = absoluteGravity & 8388615;
            if (i3 == 1) {
                this.collapsedDrawX = this.collapsedBounds.centerX() - (this.collapsedTextWidth / 2.0f);
            } else if (i3 != 5) {
                this.collapsedDrawX = this.collapsedBounds.left;
            } else {
                this.collapsedDrawX = this.collapsedBounds.right - this.collapsedTextWidth;
            }
            calculateUsingTextSize(this.expandedTextSize, z);
            StaticLayout staticLayout2 = this.textLayout;
            if (staticLayout2 != null) {
                f = staticLayout2.getHeight();
            } else {
                f = 0.0f;
            }
            StaticLayout staticLayout3 = this.textLayout;
            if (staticLayout3 != null) {
                i = staticLayout3.getLineCount();
            } else {
                i = 0;
            }
            this.expandedLineCount = i;
            CharSequence charSequence3 = this.textToDraw;
            if (charSequence3 != null) {
                f2 = this.textPaint.measureText(charSequence3, 0, charSequence3.length());
            } else {
                f2 = 0.0f;
            }
            StaticLayout staticLayout4 = this.textLayout;
            if (staticLayout4 != null && this.maxLines > 1) {
                f2 = staticLayout4.getWidth();
            }
            StaticLayout staticLayout5 = this.textLayout;
            if (staticLayout5 == null) {
                f3 = 0.0f;
            } else if (this.maxLines > 1) {
                f3 = staticLayout5.getLineStart(0);
            } else {
                f3 = staticLayout5.getLineLeft(0);
            }
            this.expandedFirstLineDrawX = f3;
            int absoluteGravity2 = Gravity.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0);
            int i4 = absoluteGravity2 & 112;
            if (i4 == 48) {
                this.expandedDrawY = this.expandedBounds.top;
            } else if (i4 != 80) {
                this.expandedDrawY = this.expandedBounds.centerY() - (f / 2.0f);
            } else {
                this.expandedDrawY = this.textPaint.descent() + (this.expandedBounds.bottom - f);
            }
            int i5 = absoluteGravity2 & 8388615;
            if (i5 == 1) {
                this.expandedDrawX = this.expandedBounds.centerX() - (f2 / 2.0f);
            } else if (i5 != 5) {
                this.expandedDrawX = this.expandedBounds.left;
            } else {
                this.expandedDrawX = this.expandedBounds.right - f2;
            }
            Bitmap bitmap = this.expandedTitleTexture;
            if (bitmap != null) {
                bitmap.recycle();
                this.expandedTitleTexture = null;
            }
            setInterpolatedTextSize(f6);
            float f7 = this.expandedFraction;
            if (this.fadeModeEnabled) {
                RectF rectF = this.currentBounds;
                if (f7 < this.fadeModeThresholdFraction) {
                    rect = this.expandedBounds;
                } else {
                    rect = this.collapsedBounds;
                }
                rectF.set(rect);
            } else {
                this.currentBounds.left = lerp(this.expandedBounds.left, this.collapsedBounds.left, f7, this.positionInterpolator);
                this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, f7, this.positionInterpolator);
                this.currentBounds.right = lerp(this.expandedBounds.right, this.collapsedBounds.right, f7, this.positionInterpolator);
                this.currentBounds.bottom = lerp(this.expandedBounds.bottom, this.collapsedBounds.bottom, f7, this.positionInterpolator);
            }
            if (!this.fadeModeEnabled) {
                this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, f7, this.positionInterpolator);
                this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, f7, this.positionInterpolator);
                setInterpolatedTextSize(lerp(this.expandedTextSize, this.collapsedTextSize, f7, this.textSizeInterpolator));
                f4 = f7;
            } else if (f7 < this.fadeModeThresholdFraction) {
                this.currentDrawX = this.expandedDrawX;
                this.currentDrawY = this.expandedDrawY;
                setInterpolatedTextSize(this.expandedTextSize);
                f4 = 0.0f;
            } else {
                this.currentDrawX = this.collapsedDrawX;
                this.currentDrawY = this.collapsedDrawY - Math.max(0, this.currentOffsetY);
                setInterpolatedTextSize(this.collapsedTextSize);
                f4 = 1.0f;
            }
            FastOutSlowInInterpolator fastOutSlowInInterpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
            this.collapsedTextBlend = 1.0f - lerp(0.0f, 1.0f, 1.0f - f7, fastOutSlowInInterpolator);
            View view = this.view;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api16Impl.postInvalidateOnAnimation(view);
            this.expandedTextBlend = lerp(1.0f, 0.0f, f7, fastOutSlowInInterpolator);
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this.view);
            ColorStateList colorStateList = this.collapsedTextColor;
            ColorStateList colorStateList2 = this.expandedTextColor;
            if (colorStateList != colorStateList2) {
                this.textPaint.setColor(blendColors(getCurrentColor(colorStateList2), getCurrentColor(this.collapsedTextColor), f4));
            } else {
                this.textPaint.setColor(getCurrentColor(colorStateList));
            }
            float f8 = this.collapsedLetterSpacing;
            float f9 = this.expandedLetterSpacing;
            if (f8 != f9) {
                this.textPaint.setLetterSpacing(lerp(f9, f8, f7, fastOutSlowInInterpolator));
            } else {
                this.textPaint.setLetterSpacing(f8);
            }
            this.textPaint.setShadowLayer(lerp(this.expandedShadowRadius, this.collapsedShadowRadius, f7, null), lerp(this.expandedShadowDx, this.collapsedShadowDx, f7, null), lerp(this.expandedShadowDy, this.collapsedShadowDy, f7, null), blendColors(getCurrentColor(this.expandedShadowColor), getCurrentColor(this.collapsedShadowColor), f7));
            if (this.fadeModeEnabled) {
                int alpha = this.textPaint.getAlpha();
                float f10 = this.fadeModeThresholdFraction;
                if (f7 <= f10) {
                    f5 = AnimationUtils.lerp(1.0f, 0.0f, this.fadeModeStartFraction, f10, f7);
                } else {
                    f5 = AnimationUtils.lerp(0.0f, 1.0f, f10, 1.0f, f7);
                }
                this.textPaint.setAlpha((int) (f5 * alpha));
            }
            ViewCompat.Api16Impl.postInvalidateOnAnimation(this.view);
        }
    }

    public final void setCollapsedTextAppearance(int i) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), i);
        ColorStateList colorStateList = textAppearance.textColor;
        if (colorStateList != null) {
            this.collapsedTextColor = colorStateList;
        }
        float f = textAppearance.textSize;
        if (f != 0.0f) {
            this.collapsedTextSize = f;
        }
        ColorStateList colorStateList2 = textAppearance.shadowColor;
        if (colorStateList2 != null) {
            this.collapsedShadowColor = colorStateList2;
        }
        this.collapsedShadowDx = textAppearance.shadowDx;
        this.collapsedShadowDy = textAppearance.shadowDy;
        this.collapsedShadowRadius = textAppearance.shadowRadius;
        this.collapsedLetterSpacing = textAppearance.letterSpacing;
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            Objects.requireNonNull(cancelableFontCallback);
            cancelableFontCallback.cancelled = true;
        }
        CancelableFontCallback.ApplyFont applyFont = new CancelableFontCallback.ApplyFont() { // from class: com.google.android.material.internal.CollapsingTextHelper.1
            @Override // com.google.android.material.resources.CancelableFontCallback.ApplyFont
            public final void apply(Typeface typeface) {
                CollapsingTextHelper collapsingTextHelper = CollapsingTextHelper.this;
                Objects.requireNonNull(collapsingTextHelper);
                CancelableFontCallback cancelableFontCallback2 = collapsingTextHelper.collapsedFontCallback;
                boolean z = true;
                if (cancelableFontCallback2 != null) {
                    cancelableFontCallback2.cancelled = true;
                }
                if (collapsingTextHelper.collapsedTypeface != typeface) {
                    collapsingTextHelper.collapsedTypeface = typeface;
                } else {
                    z = false;
                }
                if (z) {
                    collapsingTextHelper.recalculate(false);
                }
            }
        };
        textAppearance.createFallbackFont();
        this.collapsedFontCallback = new CancelableFontCallback(applyFont, textAppearance.font);
        textAppearance.getFontAsync(this.view.getContext(), this.collapsedFontCallback);
        recalculate(false);
    }

    public final void setCollapsedTextColor(ColorStateList colorStateList) {
        if (this.collapsedTextColor != colorStateList) {
            this.collapsedTextColor = colorStateList;
            recalculate(false);
        }
    }

    public final void setExpandedTextAppearance(int i) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), i);
        ColorStateList colorStateList = textAppearance.textColor;
        if (colorStateList != null) {
            this.expandedTextColor = colorStateList;
        }
        float f = textAppearance.textSize;
        if (f != 0.0f) {
            this.expandedTextSize = f;
        }
        ColorStateList colorStateList2 = textAppearance.shadowColor;
        if (colorStateList2 != null) {
            this.expandedShadowColor = colorStateList2;
        }
        this.expandedShadowDx = textAppearance.shadowDx;
        this.expandedShadowDy = textAppearance.shadowDy;
        this.expandedShadowRadius = textAppearance.shadowRadius;
        this.expandedLetterSpacing = textAppearance.letterSpacing;
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            Objects.requireNonNull(cancelableFontCallback);
            cancelableFontCallback.cancelled = true;
        }
        CancelableFontCallback.ApplyFont applyFont = new CancelableFontCallback.ApplyFont() { // from class: com.google.android.material.internal.CollapsingTextHelper.2
            @Override // com.google.android.material.resources.CancelableFontCallback.ApplyFont
            public final void apply(Typeface typeface) {
                CollapsingTextHelper collapsingTextHelper = CollapsingTextHelper.this;
                Objects.requireNonNull(collapsingTextHelper);
                CancelableFontCallback cancelableFontCallback2 = collapsingTextHelper.expandedFontCallback;
                boolean z = true;
                if (cancelableFontCallback2 != null) {
                    cancelableFontCallback2.cancelled = true;
                }
                if (collapsingTextHelper.expandedTypeface != typeface) {
                    collapsingTextHelper.expandedTypeface = typeface;
                } else {
                    z = false;
                }
                if (z) {
                    collapsingTextHelper.recalculate(false);
                }
            }
        };
        textAppearance.createFallbackFont();
        this.expandedFontCallback = new CancelableFontCallback(applyFont, textAppearance.font);
        textAppearance.getFontAsync(this.view.getContext(), this.expandedFontCallback);
        recalculate(false);
    }

    public final boolean setState(int[] iArr) {
        boolean z;
        ColorStateList colorStateList;
        this.state = iArr;
        ColorStateList colorStateList2 = this.collapsedTextColor;
        if ((colorStateList2 == null || !colorStateList2.isStateful()) && ((colorStateList = this.expandedTextColor) == null || !colorStateList.isStateful())) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        recalculate(false);
        return true;
    }

    public CollapsingTextHelper(View view) {
        this.view = view;
        TextPaint textPaint = new TextPaint(129);
        this.textPaint = textPaint;
        this.tmpPaint = new TextPaint(textPaint);
        float f = this.fadeModeStartFraction;
        this.fadeModeThresholdFraction = MotionController$$ExternalSyntheticOutline0.m(1.0f, f, 0.5f, f);
    }

    public final void draw(Canvas canvas) {
        float f;
        int save = canvas.save();
        if (this.textToDraw != null && this.drawTitle) {
            boolean z = true;
            if (this.maxLines > 1) {
                f = this.textLayout.getLineStart(0);
            } else {
                f = this.textLayout.getLineLeft(0);
            }
            float f2 = (this.currentDrawX + f) - (this.expandedFirstLineDrawX * 2.0f);
            this.textPaint.setTextSize(this.currentTextSize);
            float f3 = this.currentDrawX;
            float f4 = this.currentDrawY;
            float f5 = this.scale;
            if (f5 != 1.0f && !this.fadeModeEnabled) {
                canvas.scale(f5, f5, f3, f4);
            }
            if (this.maxLines <= 1 || (this.isRtl && !this.fadeModeEnabled)) {
                z = false;
            }
            if (!z || (this.fadeModeEnabled && this.expandedFraction <= this.fadeModeThresholdFraction)) {
                canvas.translate(f3, f4);
                this.textLayout.draw(canvas);
            } else {
                int alpha = this.textPaint.getAlpha();
                canvas.translate(f2, f4);
                float f6 = alpha;
                this.textPaint.setAlpha((int) (this.expandedTextBlend * f6));
                this.textLayout.draw(canvas);
                this.textPaint.setAlpha((int) (this.collapsedTextBlend * f6));
                int lineBaseline = this.textLayout.getLineBaseline(0);
                CharSequence charSequence = this.textToDrawCollapsed;
                float f7 = lineBaseline;
                canvas.drawText(charSequence, 0, charSequence.length(), 0.0f, f7, this.textPaint);
                if (!this.fadeModeEnabled) {
                    String trim = this.textToDrawCollapsed.toString().trim();
                    if (trim.endsWith("…")) {
                        trim = trim.substring(0, trim.length() - 1);
                    }
                    this.textPaint.setAlpha(alpha);
                    canvas.drawText(trim, 0, Math.min(this.textLayout.getLineEnd(0), trim.length()), 0.0f, f7, (Paint) this.textPaint);
                }
            }
            canvas.restoreToCount(save);
        }
    }
}
