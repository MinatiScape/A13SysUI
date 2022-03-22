package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.qs.QSFooterViewController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.input.TouchActionRegion;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class IconController implements NgaMessageHandler.KeyboardInfoListener, NgaMessageHandler.ZerostateInfoListener, ConfigurationController.ConfigurationListener, TouchActionRegion {
    public boolean mHasAccurateLuma;
    public final KeyboardIconView mKeyboardIcon;
    public boolean mKeyboardIconRequested;
    public PendingIntent mOnKeyboardIconTap;
    public PendingIntent mOnZerostateIconTap;
    public final ZeroStateIconView mZeroStateIcon;
    public boolean mZerostateIconRequested;

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.KeyboardInfoListener
    public final void onHideKeyboard() {
        this.mKeyboardIconRequested = false;
        this.mOnKeyboardIconTap = null;
        maybeUpdateIconVisibility(this.mKeyboardIcon, false);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ZerostateInfoListener
    public final void onHideZerostate() {
        this.mZerostateIconRequested = false;
        this.mOnZerostateIconTap = null;
        maybeUpdateIconVisibility(this.mZeroStateIcon, false);
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchActionRegion
    public final Optional<Region> getTouchActionRegion() {
        Region region = new Region();
        if (this.mKeyboardIcon.getVisibility() == 0) {
            Rect rect = new Rect();
            this.mKeyboardIcon.getHitRect(rect);
            region.union(rect);
        }
        if (this.mZeroStateIcon.getVisibility() == 0) {
            Rect rect2 = new Rect();
            this.mZeroStateIcon.getHitRect(rect2);
            region.union(rect2);
        }
        if (region.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(region);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        KeyboardIconView keyboardIconView = this.mKeyboardIcon;
        Objects.requireNonNull(keyboardIconView);
        keyboardIconView.mKeyboardIcon.setImageDrawable(keyboardIconView.getContext().getDrawable(2131232013));
        ZeroStateIconView zeroStateIconView = this.mZeroStateIcon;
        Objects.requireNonNull(zeroStateIconView);
        zeroStateIconView.mZeroStateIcon.setImageDrawable(zeroStateIconView.getContext().getDrawable(2131231948));
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.KeyboardInfoListener
    public final void onShowKeyboard(PendingIntent pendingIntent) {
        boolean z;
        if (pendingIntent != null) {
            z = true;
        } else {
            z = false;
        }
        this.mKeyboardIconRequested = z;
        this.mOnKeyboardIconTap = pendingIntent;
        maybeUpdateIconVisibility(this.mKeyboardIcon, z);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ZerostateInfoListener
    public final void onShowZerostate(PendingIntent pendingIntent) {
        boolean z;
        if (pendingIntent != null) {
            z = true;
        } else {
            z = false;
        }
        this.mZerostateIconRequested = z;
        this.mOnZerostateIconTap = pendingIntent;
        maybeUpdateIconVisibility(this.mZeroStateIcon, z);
    }

    public IconController(ViewGroup viewGroup, ConfigurationController configurationController) {
        KeyboardIconView keyboardIconView = (KeyboardIconView) viewGroup.findViewById(2131428159);
        this.mKeyboardIcon = keyboardIconView;
        keyboardIconView.setOnClickListener(new IconController$$ExternalSyntheticLambda0(this, 0));
        ZeroStateIconView zeroStateIconView = (ZeroStateIconView) viewGroup.findViewById(2131429298);
        this.mZeroStateIcon = zeroStateIconView;
        zeroStateIconView.setOnClickListener(new QSFooterViewController$$ExternalSyntheticLambda0(this, 1));
        configurationController.addCallback(this);
    }

    public final void maybeUpdateIconVisibility(FrameLayout frameLayout, boolean z) {
        boolean z2;
        boolean z3 = true;
        int i = 0;
        if (frameLayout.getVisibility() == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z || !this.mHasAccurateLuma) {
            z3 = false;
        }
        if (z2 != z3) {
            if (!z3) {
                i = 8;
            }
            frameLayout.setVisibility(i);
        }
    }
}
