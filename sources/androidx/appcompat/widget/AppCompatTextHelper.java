package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.mediarouter.R$bool;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class AppCompatTextHelper {
    public boolean mAsyncFontPending;
    public final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    public TintInfo mDrawableBottomTint;
    public TintInfo mDrawableEndTint;
    public TintInfo mDrawableLeftTint;
    public TintInfo mDrawableRightTint;
    public TintInfo mDrawableStartTint;
    public TintInfo mDrawableTopTint;
    public Typeface mFontTypeface;
    public final TextView mView;
    public int mStyle = 0;
    public int mFontWeight = -1;

    public final void applyCompoundDrawableTint(Drawable drawable, TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }

    public final void applyCompoundDrawablesTints() {
        if (!(this.mDrawableLeftTint == null && this.mDrawableTopTint == null && this.mDrawableRightTint == null && this.mDrawableBottomTint == null)) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
            Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
        }
    }

    @SuppressLint({"NewApi"})
    public final void loadFromAttributes(AttributeSet attributeSet, int i) {
        String str;
        String str2;
        boolean z;
        boolean z2;
        float f;
        float f2;
        float f3;
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        Drawable drawable5;
        Drawable drawable6;
        int i2;
        int i3;
        int fontMetricsInt;
        int i4;
        int i5;
        int resourceId;
        int i6;
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        int[] iArr = R$styleable.AppCompatTextHelper;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, iArr, i);
        TextView textView = this.mView;
        Context context2 = textView.getContext();
        TypedArray typedArray = obtainStyledAttributes.mWrapped;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(textView, context2, iArr, attributeSet, typedArray, i, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(0, -1);
        if (obtainStyledAttributes.hasValue(3)) {
            this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(3, 0));
        }
        if (obtainStyledAttributes.hasValue(1)) {
            this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(1, 0));
        }
        if (obtainStyledAttributes.hasValue(4)) {
            this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(4, 0));
        }
        if (obtainStyledAttributes.hasValue(2)) {
            this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(2, 0));
        }
        if (obtainStyledAttributes.hasValue(5)) {
            this.mDrawableStartTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(5, 0));
        }
        if (obtainStyledAttributes.hasValue(6)) {
            this.mDrawableEndTint = createTintInfo(context, appCompatDrawableManager, obtainStyledAttributes.getResourceId(6, 0));
        }
        obtainStyledAttributes.recycle();
        boolean z3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        if (resourceId2 != -1) {
            TintTypedArray tintTypedArray = new TintTypedArray(context, context.obtainStyledAttributes(resourceId2, R$styleable.TextAppearance));
            if (z3 || !tintTypedArray.hasValue(14)) {
                z2 = false;
                z = false;
            } else {
                z2 = tintTypedArray.getBoolean(14, false);
                z = true;
            }
            updateTypefaceAndStyle(context, tintTypedArray);
            if (tintTypedArray.hasValue(15)) {
                str2 = tintTypedArray.getString(15);
                i6 = 13;
            } else {
                i6 = 13;
                str2 = null;
            }
            if (tintTypedArray.hasValue(i6)) {
                str = tintTypedArray.getString(i6);
            } else {
                str = null;
            }
            tintTypedArray.recycle();
        } else {
            z2 = false;
            z = false;
            str2 = null;
            str = null;
        }
        TintTypedArray tintTypedArray2 = new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, R$styleable.TextAppearance, i, 0));
        if (!z3 && tintTypedArray2.hasValue(14)) {
            z2 = tintTypedArray2.getBoolean(14, false);
            z = true;
        }
        if (tintTypedArray2.hasValue(15)) {
            str2 = tintTypedArray2.getString(15);
        }
        if (tintTypedArray2.hasValue(13)) {
            str = tintTypedArray2.getString(13);
        }
        if (tintTypedArray2.hasValue(0) && tintTypedArray2.getDimensionPixelSize(0, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        updateTypefaceAndStyle(context, tintTypedArray2);
        tintTypedArray2.recycle();
        if (!z3 && z) {
            this.mView.setAllCaps(z2);
        }
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            if (this.mFontWeight == -1) {
                this.mView.setTypeface(typeface, this.mStyle);
            } else {
                this.mView.setTypeface(typeface);
            }
        }
        if (str != null) {
            this.mView.setFontVariationSettings(str);
        }
        if (str2 != null) {
            this.mView.setTextLocales(LocaleList.forLanguageTags(str2));
        }
        AppCompatTextViewAutoSizeHelper appCompatTextViewAutoSizeHelper = this.mAutoSizeTextHelper;
        Objects.requireNonNull(appCompatTextViewAutoSizeHelper);
        Context context3 = appCompatTextViewAutoSizeHelper.mContext;
        int[] iArr2 = R$styleable.AppCompatTextView;
        TypedArray obtainStyledAttributes2 = context3.obtainStyledAttributes(attributeSet, iArr2, i, 0);
        TextView textView2 = appCompatTextViewAutoSizeHelper.mTextView;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(textView2, textView2.getContext(), iArr2, attributeSet, obtainStyledAttributes2, i, 0);
        if (obtainStyledAttributes2.hasValue(5)) {
            appCompatTextViewAutoSizeHelper.mAutoSizeTextType = obtainStyledAttributes2.getInt(5, 0);
        }
        if (obtainStyledAttributes2.hasValue(4)) {
            f = obtainStyledAttributes2.getDimension(4, -1.0f);
        } else {
            f = -1.0f;
        }
        if (obtainStyledAttributes2.hasValue(2)) {
            f2 = obtainStyledAttributes2.getDimension(2, -1.0f);
        } else {
            f2 = -1.0f;
        }
        if (obtainStyledAttributes2.hasValue(1)) {
            f3 = obtainStyledAttributes2.getDimension(1, -1.0f);
        } else {
            f3 = -1.0f;
        }
        if (obtainStyledAttributes2.hasValue(3) && (resourceId = obtainStyledAttributes2.getResourceId(3, 0)) > 0) {
            TypedArray obtainTypedArray = obtainStyledAttributes2.getResources().obtainTypedArray(resourceId);
            int length = obtainTypedArray.length();
            int[] iArr3 = new int[length];
            if (length > 0) {
                for (int i7 = 0; i7 < length; i7++) {
                    iArr3[i7] = obtainTypedArray.getDimensionPixelSize(i7, -1);
                }
                appCompatTextViewAutoSizeHelper.mAutoSizeTextSizesInPx = AppCompatTextViewAutoSizeHelper.cleanupAutoSizePresetSizes(iArr3);
                appCompatTextViewAutoSizeHelper.setupAutoSizeUniformPresetSizesConfiguration();
            }
            obtainTypedArray.recycle();
        }
        obtainStyledAttributes2.recycle();
        if (!appCompatTextViewAutoSizeHelper.supportsAutoSizeText()) {
            appCompatTextViewAutoSizeHelper.mAutoSizeTextType = 0;
        } else if (appCompatTextViewAutoSizeHelper.mAutoSizeTextType == 1) {
            if (!appCompatTextViewAutoSizeHelper.mHasPresetAutoSizeValues) {
                DisplayMetrics displayMetrics = appCompatTextViewAutoSizeHelper.mContext.getResources().getDisplayMetrics();
                if (f2 == -1.0f) {
                    i5 = 2;
                    f2 = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                } else {
                    i5 = 2;
                }
                if (f3 == -1.0f) {
                    f3 = TypedValue.applyDimension(i5, 112.0f, displayMetrics);
                }
                if (f == -1.0f) {
                    f = 1.0f;
                }
                appCompatTextViewAutoSizeHelper.validateAndSetAutoSizeTextTypeUniformConfiguration(f2, f3, f);
            }
            appCompatTextViewAutoSizeHelper.setupAutoSizeText();
        }
        AppCompatTextViewAutoSizeHelper appCompatTextViewAutoSizeHelper2 = this.mAutoSizeTextHelper;
        Objects.requireNonNull(appCompatTextViewAutoSizeHelper2);
        if (appCompatTextViewAutoSizeHelper2.mAutoSizeTextType != 0) {
            AppCompatTextViewAutoSizeHelper appCompatTextViewAutoSizeHelper3 = this.mAutoSizeTextHelper;
            Objects.requireNonNull(appCompatTextViewAutoSizeHelper3);
            int[] iArr4 = appCompatTextViewAutoSizeHelper3.mAutoSizeTextSizesInPx;
            if (iArr4.length > 0) {
                if (this.mView.getAutoSizeStepGranularity() != -1.0f) {
                    TextView textView3 = this.mView;
                    AppCompatTextViewAutoSizeHelper appCompatTextViewAutoSizeHelper4 = this.mAutoSizeTextHelper;
                    Objects.requireNonNull(appCompatTextViewAutoSizeHelper4);
                    int round = Math.round(appCompatTextViewAutoSizeHelper4.mAutoSizeMinTextSizeInPx);
                    AppCompatTextViewAutoSizeHelper appCompatTextViewAutoSizeHelper5 = this.mAutoSizeTextHelper;
                    Objects.requireNonNull(appCompatTextViewAutoSizeHelper5);
                    int round2 = Math.round(appCompatTextViewAutoSizeHelper5.mAutoSizeMaxTextSizeInPx);
                    AppCompatTextViewAutoSizeHelper appCompatTextViewAutoSizeHelper6 = this.mAutoSizeTextHelper;
                    Objects.requireNonNull(appCompatTextViewAutoSizeHelper6);
                    textView3.setAutoSizeTextTypeUniformWithConfiguration(round, round2, Math.round(appCompatTextViewAutoSizeHelper6.mAutoSizeStepGranularityInPx), 0);
                } else {
                    this.mView.setAutoSizeTextTypeUniformWithPresetSizes(iArr4, 0);
                }
            }
        }
        TintTypedArray tintTypedArray3 = new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, R$styleable.AppCompatTextView));
        int resourceId3 = tintTypedArray3.getResourceId(8, -1);
        if (resourceId3 != -1) {
            drawable = appCompatDrawableManager.getDrawable(context, resourceId3);
        } else {
            drawable = null;
        }
        int resourceId4 = tintTypedArray3.getResourceId(13, -1);
        if (resourceId4 != -1) {
            drawable2 = appCompatDrawableManager.getDrawable(context, resourceId4);
        } else {
            drawable2 = null;
        }
        int resourceId5 = tintTypedArray3.getResourceId(9, -1);
        if (resourceId5 != -1) {
            drawable3 = appCompatDrawableManager.getDrawable(context, resourceId5);
        } else {
            drawable3 = null;
        }
        int resourceId6 = tintTypedArray3.getResourceId(6, -1);
        if (resourceId6 != -1) {
            drawable4 = appCompatDrawableManager.getDrawable(context, resourceId6);
        } else {
            drawable4 = null;
        }
        int resourceId7 = tintTypedArray3.getResourceId(10, -1);
        if (resourceId7 != -1) {
            drawable5 = appCompatDrawableManager.getDrawable(context, resourceId7);
        } else {
            drawable5 = null;
        }
        int resourceId8 = tintTypedArray3.getResourceId(7, -1);
        if (resourceId8 != -1) {
            drawable6 = appCompatDrawableManager.getDrawable(context, resourceId8);
        } else {
            drawable6 = null;
        }
        if (drawable5 != null || drawable6 != null) {
            Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            TextView textView4 = this.mView;
            if (drawable5 == null) {
                drawable5 = compoundDrawablesRelative[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative[1];
            }
            if (drawable6 == null) {
                drawable6 = compoundDrawablesRelative[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative[3];
            }
            textView4.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable5, drawable2, drawable6, drawable4);
        } else if (!(drawable == null && drawable2 == null && drawable3 == null && drawable4 == null)) {
            Drawable[] compoundDrawablesRelative2 = this.mView.getCompoundDrawablesRelative();
            if (compoundDrawablesRelative2[0] == null && compoundDrawablesRelative2[2] == null) {
                Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
                TextView textView5 = this.mView;
                if (drawable == null) {
                    drawable = compoundDrawables[0];
                }
                if (drawable2 == null) {
                    drawable2 = compoundDrawables[1];
                }
                if (drawable3 == null) {
                    drawable3 = compoundDrawables[2];
                }
                if (drawable4 == null) {
                    drawable4 = compoundDrawables[3];
                }
                textView5.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
            } else {
                TextView textView6 = this.mView;
                Drawable drawable7 = compoundDrawablesRelative2[0];
                if (drawable2 == null) {
                    drawable2 = compoundDrawablesRelative2[1];
                }
                Drawable drawable8 = compoundDrawablesRelative2[2];
                if (drawable4 == null) {
                    drawable4 = compoundDrawablesRelative2[3];
                }
                textView6.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable7, drawable2, drawable8, drawable4);
            }
        }
        if (tintTypedArray3.hasValue(11)) {
            ColorStateList colorStateList = tintTypedArray3.getColorStateList(11);
            TextView textView7 = this.mView;
            Objects.requireNonNull(textView7);
            textView7.setCompoundDrawableTintList(colorStateList);
        }
        if (tintTypedArray3.hasValue(12)) {
            i2 = -1;
            PorterDuff.Mode parseTintMode = DrawableUtils.parseTintMode(tintTypedArray3.getInt(12, -1), null);
            TextView textView8 = this.mView;
            Objects.requireNonNull(textView8);
            textView8.setCompoundDrawableTintMode(parseTintMode);
        } else {
            i2 = -1;
        }
        int dimensionPixelSize = tintTypedArray3.getDimensionPixelSize(15, i2);
        int dimensionPixelSize2 = tintTypedArray3.getDimensionPixelSize(18, i2);
        int dimensionPixelSize3 = tintTypedArray3.getDimensionPixelSize(19, i2);
        tintTypedArray3.recycle();
        if (dimensionPixelSize != i2) {
            TextView textView9 = this.mView;
            R$bool.checkArgumentNonnegative(dimensionPixelSize);
            textView9.setFirstBaselineToTopHeight(dimensionPixelSize);
        }
        if (dimensionPixelSize2 != i2) {
            TextView textView10 = this.mView;
            R$bool.checkArgumentNonnegative(dimensionPixelSize2);
            Paint.FontMetricsInt fontMetricsInt2 = textView10.getPaint().getFontMetricsInt();
            if (textView10.getIncludeFontPadding()) {
                i4 = fontMetricsInt2.bottom;
            } else {
                i4 = fontMetricsInt2.descent;
            }
            if (dimensionPixelSize2 > Math.abs(i4)) {
                textView10.setPadding(textView10.getPaddingLeft(), textView10.getPaddingTop(), textView10.getPaddingRight(), dimensionPixelSize2 - i4);
            }
            i3 = -1;
        } else {
            i3 = i2;
        }
        if (dimensionPixelSize3 != i3) {
            TextView textView11 = this.mView;
            R$bool.checkArgumentNonnegative(dimensionPixelSize3);
            if (dimensionPixelSize3 != textView11.getPaint().getFontMetricsInt(null)) {
                textView11.setLineSpacing(dimensionPixelSize3 - fontMetricsInt, 1.0f);
            }
        }
    }

    public final void onSetTextAppearance(Context context, int i) {
        String string;
        TintTypedArray tintTypedArray = new TintTypedArray(context, context.obtainStyledAttributes(i, R$styleable.TextAppearance));
        if (tintTypedArray.hasValue(14)) {
            this.mView.setAllCaps(tintTypedArray.getBoolean(14, false));
        }
        if (tintTypedArray.hasValue(0) && tintTypedArray.getDimensionPixelSize(0, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        updateTypefaceAndStyle(context, tintTypedArray);
        if (tintTypedArray.hasValue(13) && (string = tintTypedArray.getString(13)) != null) {
            this.mView.setFontVariationSettings(string);
        }
        tintTypedArray.recycle();
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            this.mView.setTypeface(typeface, this.mStyle);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v1, types: [androidx.appcompat.widget.AppCompatTextHelper$1] */
    public final void updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        String string;
        boolean z;
        boolean z2;
        this.mStyle = tintTypedArray.getInt(2, this.mStyle);
        int i = tintTypedArray.getInt(11, -1);
        this.mFontWeight = i;
        boolean z3 = false;
        if (i != -1) {
            this.mStyle = (this.mStyle & 2) | 0;
        }
        int i2 = 10;
        if (tintTypedArray.hasValue(10) || tintTypedArray.hasValue(12)) {
            this.mFontTypeface = null;
            if (tintTypedArray.hasValue(12)) {
                i2 = 12;
            }
            final int i3 = this.mFontWeight;
            final int i4 = this.mStyle;
            if (!context.isRestricted()) {
                final WeakReference weakReference = new WeakReference(this.mView);
                try {
                    Typeface font = tintTypedArray.getFont(i2, this.mStyle, new ResourcesCompat.FontCallback() { // from class: androidx.appcompat.widget.AppCompatTextHelper.1
                        @Override // androidx.core.content.res.ResourcesCompat.FontCallback
                        public final void onFontRetrievalFailed(int i5) {
                        }

                        @Override // androidx.core.content.res.ResourcesCompat.FontCallback
                        public final void onFontRetrieved(final Typeface typeface) {
                            boolean z4;
                            int i5 = i3;
                            if (i5 != -1) {
                                if ((i4 & 2) != 0) {
                                    z4 = true;
                                } else {
                                    z4 = false;
                                }
                                typeface = Typeface.create(typeface, i5, z4);
                            }
                            AppCompatTextHelper appCompatTextHelper = AppCompatTextHelper.this;
                            WeakReference weakReference2 = weakReference;
                            Objects.requireNonNull(appCompatTextHelper);
                            if (appCompatTextHelper.mAsyncFontPending) {
                                appCompatTextHelper.mFontTypeface = typeface;
                                final TextView textView = (TextView) weakReference2.get();
                                if (textView != null) {
                                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                                    if (ViewCompat.Api19Impl.isAttachedToWindow(textView)) {
                                        final int i6 = appCompatTextHelper.mStyle;
                                        textView.post(new Runnable() { // from class: androidx.appcompat.widget.AppCompatTextHelper.2
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                textView.setTypeface(typeface, i6);
                                            }
                                        });
                                        return;
                                    }
                                    textView.setTypeface(typeface, appCompatTextHelper.mStyle);
                                }
                            }
                        }
                    });
                    if (font != null) {
                        if (this.mFontWeight != -1) {
                            Typeface create = Typeface.create(font, 0);
                            int i5 = this.mFontWeight;
                            if ((this.mStyle & 2) != 0) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            this.mFontTypeface = Typeface.create(create, i5, z2);
                        } else {
                            this.mFontTypeface = font;
                        }
                    }
                    if (this.mFontTypeface == null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    this.mAsyncFontPending = z;
                } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.mFontTypeface == null && (string = tintTypedArray.getString(i2)) != null) {
                if (this.mFontWeight != -1) {
                    Typeface create2 = Typeface.create(string, 0);
                    int i6 = this.mFontWeight;
                    if ((this.mStyle & 2) != 0) {
                        z3 = true;
                    }
                    this.mFontTypeface = Typeface.create(create2, i6, z3);
                    return;
                }
                this.mFontTypeface = Typeface.create(string, this.mStyle);
            }
        } else if (tintTypedArray.hasValue(1)) {
            this.mAsyncFontPending = false;
            int i7 = tintTypedArray.getInt(1, 1);
            if (i7 == 1) {
                this.mFontTypeface = Typeface.SANS_SERIF;
            } else if (i7 == 2) {
                this.mFontTypeface = Typeface.SERIF;
            } else if (i7 == 3) {
                this.mFontTypeface = Typeface.MONOSPACE;
            }
        }
    }

    public AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(textView);
    }

    public static TintInfo createTintInfo(Context context, AppCompatDrawableManager appCompatDrawableManager, int i) {
        ColorStateList tintList;
        Objects.requireNonNull(appCompatDrawableManager);
        synchronized (appCompatDrawableManager) {
            tintList = appCompatDrawableManager.mResourceManager.getTintList(context, i);
        }
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }
}
