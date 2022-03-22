package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class KeyguardMessageArea extends TextView {
    public static final Object ANNOUNCE_TOKEN = new Object();
    public boolean mAltBouncerShowing;
    public boolean mBouncerVisible;
    public ViewGroup mContainer;
    public ColorStateList mDefaultColorState;
    public CharSequence mMessage;
    public int mTopMargin;
    public ColorStateList mNextMessageColorState = ColorStateList.valueOf(-1);
    public final Handler mHandler = new Handler(Looper.myLooper());

    /* loaded from: classes.dex */
    public static class AnnounceRunnable implements Runnable {
        public final WeakReference<View> mHost;
        public final CharSequence mTextToAnnounce;

        @Override // java.lang.Runnable
        public final void run() {
            View view = this.mHost.get();
            if (view != null) {
                view.announceForAccessibility(this.mTextToAnnounce);
            }
        }

        public AnnounceRunnable(View view, CharSequence charSequence) {
            this.mHost = new WeakReference<>(view);
            this.mTextToAnnounce = charSequence;
        }
    }

    public final void onDensityOrFontScaleChanged() {
        TypedArray obtainStyledAttributes = ((TextView) this).mContext.obtainStyledAttributes(2132017508, new int[]{16842901});
        setTextSize(0, obtainStyledAttributes.getDimensionPixelSize(0, 0));
        obtainStyledAttributes.recycle();
    }

    public final void onThemeChanged() {
        TypedArray obtainStyledAttributes = ((TextView) this).mContext.obtainStyledAttributes(new int[]{16842806});
        ColorStateList valueOf = ColorStateList.valueOf(obtainStyledAttributes.getColor(0, -65536));
        obtainStyledAttributes.recycle();
        this.mDefaultColorState = valueOf;
        update();
    }

    public final void update() {
        int i;
        CharSequence charSequence = this.mMessage;
        if (TextUtils.isEmpty(charSequence) || (!this.mBouncerVisible && !this.mAltBouncerShowing)) {
            i = 4;
        } else {
            i = 0;
        }
        setVisibility(i);
        setText(charSequence);
        ColorStateList colorStateList = this.mDefaultColorState;
        if (this.mNextMessageColorState.getDefaultColor() != -1) {
            colorStateList = this.mNextMessageColorState;
            this.mNextMessageColorState = ColorStateList.valueOf(-1);
        }
        if (this.mAltBouncerShowing) {
            colorStateList = ColorStateList.valueOf(-1);
        }
        setTextColor(colorStateList);
    }

    public KeyguardMessageArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayerType(2, null);
        onThemeChanged();
    }

    public static KeyguardMessageArea findSecurityMessageDisplay(View view) {
        KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) view.findViewById(2131428181);
        if (keyguardMessageArea == null) {
            keyguardMessageArea = (KeyguardMessageArea) view.getRootView().findViewById(2131428181);
        }
        if (keyguardMessageArea != null) {
            return keyguardMessageArea;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Can't find keyguard_message_area in ");
        m.append(view.getClass());
        throw new RuntimeException(m.toString());
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContainer = (ViewGroup) getRootView().findViewById(2131428182);
    }

    public final void setMessage(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            this.mMessage = charSequence;
            update();
            Handler handler = this.mHandler;
            Object obj = ANNOUNCE_TOKEN;
            handler.removeCallbacksAndMessages(obj);
            this.mHandler.postAtTime(new AnnounceRunnable(this, getText()), obj, SystemClock.uptimeMillis() + 250);
            return;
        }
        this.mMessage = null;
        update();
    }
}
