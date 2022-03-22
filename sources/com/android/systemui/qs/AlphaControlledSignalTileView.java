package com.android.systemui.qs;

import android.content.Context;
import android.content.res.ColorStateList;
import com.android.systemui.qs.tileimpl.SlashImageView;
/* loaded from: classes.dex */
public final class AlphaControlledSignalTileView extends SignalTileView {
    @Override // com.android.systemui.qs.SignalTileView
    public final SlashImageView createSlashImageView(Context context) {
        return new AlphaControlledSlashImageView(context);
    }

    /* loaded from: classes.dex */
    public static class AlphaControlledSlashDrawable extends SlashDrawable {
        @Override // com.android.systemui.qs.SlashDrawable
        public final void setDrawableTintList(ColorStateList colorStateList) {
        }

        public final void setFinalTintList(ColorStateList colorStateList) {
            super.setDrawableTintList(colorStateList);
        }
    }

    /* loaded from: classes.dex */
    public static class AlphaControlledSlashImageView extends SlashImageView {
        public AlphaControlledSlashImageView(Context context) {
            super(context);
        }
    }

    public AlphaControlledSignalTileView(Context context) {
        super(context);
    }
}
