package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import androidx.fragment.app.FragmentContainer;
import com.google.android.material.resources.TextAppearance;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public final class TextDrawableHelper {
    public WeakReference<TextDrawableDelegate> delegate;
    public TextAppearance textAppearance;
    public float textWidth;
    public final TextPaint textPaint = new TextPaint(1);
    public final AnonymousClass1 fontCallback = new FragmentContainer() { // from class: com.google.android.material.internal.TextDrawableHelper.1
        @Override // androidx.fragment.app.FragmentContainer
        public final void onFontRetrievalFailed(int i) {
            TextDrawableHelper textDrawableHelper = TextDrawableHelper.this;
            textDrawableHelper.textWidthDirty = true;
            TextDrawableDelegate textDrawableDelegate = textDrawableHelper.delegate.get();
            if (textDrawableDelegate != null) {
                textDrawableDelegate.onTextSizeChange();
            }
        }

        @Override // androidx.fragment.app.FragmentContainer
        public final void onFontRetrieved(Typeface typeface, boolean z) {
            if (!z) {
                TextDrawableHelper textDrawableHelper = TextDrawableHelper.this;
                textDrawableHelper.textWidthDirty = true;
                TextDrawableDelegate textDrawableDelegate = textDrawableHelper.delegate.get();
                if (textDrawableDelegate != null) {
                    textDrawableDelegate.onTextSizeChange();
                }
            }
        }
    };
    public boolean textWidthDirty = true;

    /* loaded from: classes.dex */
    public interface TextDrawableDelegate {
        int[] getState();

        boolean onStateChange(int[] iArr);

        void onTextSizeChange();
    }

    public final float getTextWidth(String str) {
        float f;
        if (!this.textWidthDirty) {
            return this.textWidth;
        }
        if (str == null) {
            f = 0.0f;
        } else {
            f = this.textPaint.measureText((CharSequence) str, 0, str.length());
        }
        this.textWidth = f;
        this.textWidthDirty = false;
        return f;
    }

    public final void setTextAppearance(TextAppearance textAppearance, Context context) {
        if (this.textAppearance != textAppearance) {
            this.textAppearance = textAppearance;
            if (textAppearance != null) {
                textAppearance.updateMeasureState(context, this.textPaint, this.fontCallback);
                TextDrawableDelegate textDrawableDelegate = this.delegate.get();
                if (textDrawableDelegate != null) {
                    this.textPaint.drawableState = textDrawableDelegate.getState();
                }
                textAppearance.updateDrawState(context, this.textPaint, this.fontCallback);
                this.textWidthDirty = true;
            }
            TextDrawableDelegate textDrawableDelegate2 = this.delegate.get();
            if (textDrawableDelegate2 != null) {
                textDrawableDelegate2.onTextSizeChange();
                textDrawableDelegate2.onStateChange(textDrawableDelegate2.getState());
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.material.internal.TextDrawableHelper$1] */
    public TextDrawableHelper(TextDrawableDelegate textDrawableDelegate) {
        this.delegate = new WeakReference<>(null);
        this.delegate = new WeakReference<>(textDrawableDelegate);
    }
}
