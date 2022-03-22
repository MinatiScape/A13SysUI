package com.android.systemui.keyguard;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class KeyguardIndication {
    public final Drawable mBackground;
    public final Drawable mIcon;
    public final CharSequence mMessage;
    public final Long mMinVisibilityMillis;
    public final View.OnClickListener mOnClickListener;
    public final ColorStateList mTextColor;

    public final String toString() {
        String str = "KeyguardIndication{";
        if (!TextUtils.isEmpty(this.mMessage)) {
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, "mMessage=");
            m.append((Object) this.mMessage);
            str = m.toString();
        }
        if (this.mIcon != null) {
            StringBuilder m2 = DebugInfo$$ExternalSyntheticOutline0.m(str, " mIcon=");
            m2.append(this.mIcon);
            str = m2.toString();
        }
        if (this.mOnClickListener != null) {
            StringBuilder m3 = DebugInfo$$ExternalSyntheticOutline0.m(str, " mOnClickListener=");
            m3.append(this.mOnClickListener);
            str = m3.toString();
        }
        if (this.mBackground != null) {
            StringBuilder m4 = DebugInfo$$ExternalSyntheticOutline0.m(str, " mBackground=");
            m4.append(this.mBackground);
            str = m4.toString();
        }
        if (this.mMinVisibilityMillis != null) {
            StringBuilder m5 = DebugInfo$$ExternalSyntheticOutline0.m(str, " mMinVisibilityMillis=");
            m5.append(this.mMinVisibilityMillis);
            str = m5.toString();
        }
        return SupportMenuInflater$$ExternalSyntheticOutline0.m(str, "}");
    }

    public KeyguardIndication(CharSequence charSequence, ColorStateList colorStateList, Drawable drawable, View.OnClickListener onClickListener, Drawable drawable2, Long l) {
        this.mMessage = charSequence;
        this.mTextColor = colorStateList;
        this.mIcon = drawable;
        this.mOnClickListener = onClickListener;
        this.mBackground = drawable2;
        this.mMinVisibilityMillis = l;
    }
}
