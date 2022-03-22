package com.android.systemui.toast;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import com.android.systemui.plugins.ToastPlugin;
/* loaded from: classes.dex */
public final class SystemUIToast implements ToastPlugin.Toast {
    public final Context mContext;
    public int mDefaultGravity;
    public int mDefaultY;
    public final Animator mInAnimator;
    public final Animator mOutAnimator;
    public final String mPackageName;
    public final ToastPlugin.Toast mPluginToast;
    public final CharSequence mText;
    public final View mToastView;
    public final int mUserId;

    /* JADX WARN: Removed duplicated region for block: B:39:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00fe  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SystemUIToast(android.view.LayoutInflater r17, android.content.Context r18, java.lang.CharSequence r19, com.android.systemui.plugins.ToastPlugin.Toast r20, java.lang.String r21, int r22, int r23) {
        /*
            Method dump skipped, instructions count: 806
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.toast.SystemUIToast.<init>(android.view.LayoutInflater, android.content.Context, java.lang.CharSequence, com.android.systemui.plugins.ToastPlugin$Toast, java.lang.String, int, int):void");
    }

    public final boolean isPluginToast() {
        if (this.mPluginToast != null) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final void onOrientationChange(int i) {
        ToastPlugin.Toast toast = this.mPluginToast;
        if (toast != null) {
            toast.onOrientationChange(i);
        }
        this.mDefaultY = this.mContext.getResources().getDimensionPixelSize(17105612);
        this.mDefaultGravity = this.mContext.getResources().getInteger(17694947);
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Integer getGravity() {
        if (!isPluginToast() || this.mPluginToast.getGravity() == null) {
            return Integer.valueOf(this.mDefaultGravity);
        }
        return this.mPluginToast.getGravity();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Integer getHorizontalMargin() {
        if (!isPluginToast() || this.mPluginToast.getHorizontalMargin() == null) {
            return 0;
        }
        return this.mPluginToast.getHorizontalMargin();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Integer getVerticalMargin() {
        if (!isPluginToast() || this.mPluginToast.getVerticalMargin() == null) {
            return 0;
        }
        return this.mPluginToast.getVerticalMargin();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Integer getXOffset() {
        if (!isPluginToast() || this.mPluginToast.getXOffset() == null) {
            return 0;
        }
        return this.mPluginToast.getXOffset();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Integer getYOffset() {
        if (!isPluginToast() || this.mPluginToast.getYOffset() == null) {
            return Integer.valueOf(this.mDefaultY);
        }
        return this.mPluginToast.getYOffset();
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Animator getInAnimation() {
        return this.mInAnimator;
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final Animator getOutAnimation() {
        return this.mOutAnimator;
    }

    @Override // com.android.systemui.plugins.ToastPlugin.Toast
    public final View getView() {
        return this.mToastView;
    }
}
