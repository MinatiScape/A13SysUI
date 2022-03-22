package com.android.systemui.media.taptotransfer.receiver;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.systemui.media.taptotransfer.common.MediaTttChipState;
/* compiled from: ChipStateReceiver.kt */
/* loaded from: classes.dex */
public final class ChipStateReceiver extends MediaTttChipState {
    public final Drawable appIconDrawable;
    public final CharSequence appName;

    @Override // com.android.systemui.media.taptotransfer.common.MediaTttChipState
    public final Drawable getAppIcon(Context context) {
        Drawable drawable = this.appIconDrawable;
        if (drawable != null) {
            return drawable;
        }
        return super.getAppIcon(context);
    }

    @Override // com.android.systemui.media.taptotransfer.common.MediaTttChipState
    public final String getAppName(Context context) {
        CharSequence charSequence = this.appName;
        if (charSequence != null) {
            return charSequence.toString();
        }
        return super.getAppName(context);
    }

    public ChipStateReceiver(String str, Drawable drawable, CharSequence charSequence) {
        super(str);
        this.appIconDrawable = drawable;
        this.appName = charSequence;
    }
}
