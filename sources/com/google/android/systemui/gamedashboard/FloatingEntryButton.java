package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class FloatingEntryButton {
    public final int mButtonHeight;
    public final Context mContext;
    public final ImageView mEntryPointButton;
    public final View mFloatingView;
    public final int mMargin;
    public Consumer<Boolean> mVisibilityChangedCallback;
    public final WindowManager mWindowManager;
    public boolean mIsShowing = false;
    public boolean mCanShow = true;

    public final boolean hide() {
        if (!this.mIsShowing) {
            return false;
        }
        this.mWindowManager.removeViewImmediate(this.mFloatingView);
        this.mIsShowing = false;
        Consumer<Boolean> consumer = this.mVisibilityChangedCallback;
        if (consumer == null) {
            return true;
        }
        consumer.accept(Boolean.FALSE);
        return true;
    }

    public FloatingEntryButton(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        View inflate = LayoutInflater.from(context).inflate(2131624105, (ViewGroup) null);
        this.mFloatingView = inflate;
        inflate.setVisibility(0);
        ImageView imageView = (ImageView) inflate.findViewById(2131427995);
        this.mEntryPointButton = imageView;
        imageView.setVisibility(0);
        Resources resources = context.getResources();
        this.mMargin = Math.max(resources.getDimensionPixelSize(2131165735), resources.getDimensionPixelSize(2131166936));
        this.mButtonHeight = resources.getDimensionPixelSize(2131165734);
    }
}
