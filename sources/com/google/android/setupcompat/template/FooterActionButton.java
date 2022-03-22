package com.google.android.setupcompat.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import java.util.Objects;
/* loaded from: classes.dex */
public class FooterActionButton extends Button {
    public FooterButton footerButton;
    public boolean isPrimaryButtonStyle = false;

    public FooterActionButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.TextView, android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        FooterButton footerButton;
        if (motionEvent.getAction() == 0 && (footerButton = this.footerButton) != null && !footerButton.enabled) {
            Objects.requireNonNull(footerButton);
            Objects.requireNonNull(this.footerButton);
        }
        return super.onTouchEvent(motionEvent);
    }
}
