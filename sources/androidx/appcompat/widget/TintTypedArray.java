package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextHelper;
import androidx.core.content.res.ResourcesCompat;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TintTypedArray {
    public final Context mContext;
    public TypedValue mTypedValue;
    public final TypedArray mWrapped;

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, int[] iArr, int i) {
        return new TintTypedArray(context, context.obtainStyledAttributes(attributeSet, iArr, i, 0));
    }

    public final boolean getBoolean(int i, boolean z) {
        return this.mWrapped.getBoolean(i, z);
    }

    public final ColorStateList getColorStateList(int i) {
        int resourceId;
        ColorStateList colorStateList;
        if (!this.mWrapped.hasValue(i) || (resourceId = this.mWrapped.getResourceId(i, 0)) == 0 || (colorStateList = AppCompatResources.getColorStateList(this.mContext, resourceId)) == null) {
            return this.mWrapped.getColorStateList(i);
        }
        return colorStateList;
    }

    public final int getDimensionPixelOffset(int i, int i2) {
        return this.mWrapped.getDimensionPixelOffset(i, i2);
    }

    public final int getDimensionPixelSize(int i, int i2) {
        return this.mWrapped.getDimensionPixelSize(i, i2);
    }

    public final Drawable getDrawable(int i) {
        int resourceId;
        if (!this.mWrapped.hasValue(i) || (resourceId = this.mWrapped.getResourceId(i, 0)) == 0) {
            return this.mWrapped.getDrawable(i);
        }
        return AppCompatResources.getDrawable(this.mContext, resourceId);
    }

    public final Drawable getDrawableIfKnown(int i) {
        int resourceId;
        Drawable drawable;
        if (!this.mWrapped.hasValue(i) || (resourceId = this.mWrapped.getResourceId(i, 0)) == 0) {
            return null;
        }
        AppCompatDrawableManager appCompatDrawableManager = AppCompatDrawableManager.get();
        Context context = this.mContext;
        Objects.requireNonNull(appCompatDrawableManager);
        synchronized (appCompatDrawableManager) {
            drawable = appCompatDrawableManager.mResourceManager.getDrawable(context, resourceId, true);
        }
        return drawable;
    }

    public final Typeface getFont(int i, int i2, AppCompatTextHelper.AnonymousClass1 r12) {
        int resourceId = this.mWrapped.getResourceId(i, 0);
        if (resourceId == 0) {
            return null;
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        Context context = this.mContext;
        TypedValue typedValue = this.mTypedValue;
        ThreadLocal<TypedValue> threadLocal = ResourcesCompat.sTempTypedValue;
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, resourceId, typedValue, i2, r12, true, false);
    }

    public final int getInt(int i, int i2) {
        return this.mWrapped.getInt(i, i2);
    }

    public final int getResourceId(int i, int i2) {
        return this.mWrapped.getResourceId(i, i2);
    }

    public final String getString(int i) {
        return this.mWrapped.getString(i);
    }

    public final CharSequence getText(int i) {
        return this.mWrapped.getText(i);
    }

    public final boolean hasValue(int i) {
        return this.mWrapped.hasValue(i);
    }

    public final void recycle() {
        this.mWrapped.recycle();
    }

    public TintTypedArray(Context context, TypedArray typedArray) {
        this.mContext = context;
        this.mWrapped = typedArray;
    }
}
