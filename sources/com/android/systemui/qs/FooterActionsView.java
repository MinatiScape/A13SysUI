package com.android.systemui.qs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.Utils;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import com.android.systemui.statusbar.phone.SettingsButton;
import java.util.Objects;
/* compiled from: FooterActionsView.kt */
/* loaded from: classes.dex */
public final class FooterActionsView extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ImageView multiUserAvatar;
    public MultiUserSwitch multiUserSwitch;
    public boolean qsDisabled;
    public SettingsButton settingsButton;
    public View settingsContainer;

    public final void onUserInfoChanged(Drawable drawable, boolean z) {
        if (drawable != null && z && !(drawable instanceof UserIconDrawable)) {
            drawable = drawable.getConstantState().newDrawable(getResources()).mutate();
            drawable.setColorFilter(Utils.getColorAttrDefaultColor(((LinearLayout) this).mContext, 16842800), PorterDuff.Mode.SRC_IN);
        }
        ImageView imageView = this.multiUserAvatar;
        if (imageView == null) {
            imageView = null;
        }
        imageView.setImageDrawable(drawable);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.settingsButton = (SettingsButton) findViewById(2131428838);
        this.settingsContainer = findViewById(2131428839);
        MultiUserSwitch multiUserSwitch = (MultiUserSwitch) findViewById(2131428473);
        this.multiUserSwitch = multiUserSwitch;
        this.multiUserAvatar = (ImageView) multiUserSwitch.findViewById(2131428472);
        SettingsButton settingsButton = this.settingsButton;
        SettingsButton settingsButton2 = null;
        if (settingsButton == null) {
            settingsButton = null;
        }
        if (settingsButton.getBackground() instanceof RippleDrawable) {
            SettingsButton settingsButton3 = this.settingsButton;
            if (settingsButton3 != null) {
                settingsButton2 = settingsButton3;
            }
            Drawable background = settingsButton2.getBackground();
            Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            ((RippleDrawable) background).setForceSoftware(true);
        }
        setImportantForAccessibility(1);
    }

    public FooterActionsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
