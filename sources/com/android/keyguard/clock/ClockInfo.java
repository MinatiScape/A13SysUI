package com.android.keyguard.clock;

import android.graphics.Bitmap;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class ClockInfo {
    public final String mId;
    public final String mName;
    public final Supplier<Bitmap> mPreview;
    public final Supplier<Bitmap> mThumbnail;
    public final Supplier<String> mTitle;

    public ClockInfo() {
        throw null;
    }

    public ClockInfo(String str, Supplier supplier, String str2, Supplier supplier2, Supplier supplier3) {
        this.mName = str;
        this.mTitle = supplier;
        this.mId = str2;
        this.mThumbnail = supplier2;
        this.mPreview = supplier3;
    }
}
