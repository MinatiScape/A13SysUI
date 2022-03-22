package com.android.keyguard;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.R$styleable;
import java.util.Locale;
/* loaded from: classes.dex */
public class CarrierText extends TextView {
    public final boolean mShowAirplaneMode;
    public final boolean mShowMissingSim;

    public CarrierText(Context context) {
        this(context, null);
    }

    /* loaded from: classes.dex */
    public static class CarrierTextTransformationMethod extends SingleLineTransformationMethod {
        public final boolean mAllCaps;
        public final Locale mLocale;

        public CarrierTextTransformationMethod(Context context, boolean z) {
            this.mLocale = context.getResources().getConfiguration().locale;
            this.mAllCaps = z;
        }

        @Override // android.text.method.ReplacementTransformationMethod, android.text.method.TransformationMethod
        public final CharSequence getTransformation(CharSequence charSequence, View view) {
            CharSequence transformation = super.getTransformation(charSequence, view);
            if (!this.mAllCaps || transformation == null) {
                return transformation;
            }
            return transformation.toString().toUpperCase(this.mLocale);
        }
    }

    /* JADX WARN: Finally extract failed */
    public CarrierText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.CarrierText, 0, 0);
        try {
            boolean z = obtainStyledAttributes.getBoolean(0, false);
            this.mShowAirplaneMode = obtainStyledAttributes.getBoolean(1, false);
            this.mShowMissingSim = obtainStyledAttributes.getBoolean(2, false);
            obtainStyledAttributes.recycle();
            setTransformationMethod(new CarrierTextTransformationMethod(((TextView) this).mContext, z));
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        } else {
            setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}
