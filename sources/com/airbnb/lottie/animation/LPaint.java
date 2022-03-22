package com.airbnb.lottie.animation;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.LocaleList;
/* loaded from: classes.dex */
public final class LPaint extends Paint {
    public LPaint() {
    }

    @Override // android.graphics.Paint
    public final void setTextLocales(LocaleList localeList) {
    }

    public LPaint(int i) {
        super(i);
    }

    public LPaint(PorterDuff.Mode mode) {
        setXfermode(new PorterDuffXfermode(mode));
    }

    public LPaint(PorterDuff.Mode mode, int i) {
        super(1);
        setXfermode(new PorterDuffXfermode(mode));
    }
}
