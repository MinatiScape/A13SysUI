package com.android.keyguard;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
/* loaded from: classes.dex */
public final class KeyguardClockAccessibilityDelegate extends View.AccessibilityDelegate {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final String mFancyColon;

    @Override // android.view.View.AccessibilityDelegate
    public final void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        if (TextUtils.isEmpty(this.mFancyColon)) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            return;
        }
        CharSequence text = ((TextView) view).getText();
        if (!TextUtils.isEmpty(text)) {
            accessibilityEvent.getText().add(replaceFancyColon(text));
        }
    }

    public final CharSequence replaceFancyColon(CharSequence charSequence) {
        if (TextUtils.isEmpty(this.mFancyColon)) {
            return charSequence;
        }
        return charSequence.toString().replace(this.mFancyColon, ":");
    }

    public KeyguardClockAccessibilityDelegate(Context context) {
        this.mFancyColon = context.getString(2131952536);
    }

    @Override // android.view.View.AccessibilityDelegate
    public final void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        if (!TextUtils.isEmpty(this.mFancyColon)) {
            CharSequence contentDescription = accessibilityEvent.getContentDescription();
            if (!TextUtils.isEmpty(contentDescription)) {
                accessibilityEvent.setContentDescription(replaceFancyColon(contentDescription));
            }
        }
    }

    @Override // android.view.View.AccessibilityDelegate
    public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
        if (!TextUtils.isEmpty(this.mFancyColon)) {
            if (!TextUtils.isEmpty(accessibilityNodeInfo.getText())) {
                accessibilityNodeInfo.setText(replaceFancyColon(accessibilityNodeInfo.getText()));
            }
            if (!TextUtils.isEmpty(accessibilityNodeInfo.getContentDescription())) {
                accessibilityNodeInfo.setContentDescription(replaceFancyColon(accessibilityNodeInfo.getContentDescription()));
            }
        }
    }
}
