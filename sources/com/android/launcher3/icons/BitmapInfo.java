package com.android.launcher3.icons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.android.launcher3.icons.ClockDrawableWrapper;
/* loaded from: classes.dex */
public class BitmapInfo {
    public static final Bitmap LOW_RES_ICON = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
    public BitmapInfo badgeInfo;
    public final int color;
    public int flags;
    public final Bitmap icon;
    public Bitmap mMono;
    public Bitmap mWhiteShadowLayer;

    /* loaded from: classes.dex */
    public interface Extender {
        void drawForPersistence(Canvas canvas);

        ClockDrawableWrapper.ClockBitmapInfo getExtendedInfo(Bitmap bitmap, int i, BaseIconFactory baseIconFactory, float f);
    }

    public final void applyFlags(Context context, FastBitmapDrawable fastBitmapDrawable) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{2130968953});
        float f = obtainStyledAttributes.getFloat(0, 1.0f);
        obtainStyledAttributes.recycle();
        fastBitmapDrawable.mDisabledAlpha = f;
        BitmapInfo bitmapInfo = this.badgeInfo;
        if (bitmapInfo != null) {
            fastBitmapDrawable.setBadge(bitmapInfo.newIcon(context));
            return;
        }
        int i = this.flags;
        if ((i & 2) != 0) {
            fastBitmapDrawable.setBadge(context.getDrawable(2131232011));
        } else if ((i & 1) != 0) {
            fastBitmapDrawable.setBadge(context.getDrawable(2131232338));
        }
    }

    public BitmapInfo clone() {
        BitmapInfo bitmapInfo = new BitmapInfo(this.icon, this.color);
        bitmapInfo.mMono = this.mMono;
        bitmapInfo.mWhiteShadowLayer = this.mWhiteShadowLayer;
        bitmapInfo.flags = this.flags;
        bitmapInfo.badgeInfo = this.badgeInfo;
        return bitmapInfo;
    }

    public FastBitmapDrawable newIcon(Context context) {
        boolean z;
        FastBitmapDrawable fastBitmapDrawable;
        if (LOW_RES_ICON == this.icon) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            fastBitmapDrawable = new PlaceHolderIconDrawable(this, context);
        } else {
            fastBitmapDrawable = new FastBitmapDrawable(this.icon, this.color);
        }
        applyFlags(context, fastBitmapDrawable);
        return fastBitmapDrawable;
    }

    public BitmapInfo(Bitmap bitmap, int i) {
        this.icon = bitmap;
        this.color = i;
    }
}
