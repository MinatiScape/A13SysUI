package com.android.systemui.media.taptotransfer.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.widget.CachingIconView;
import com.android.systemui.media.taptotransfer.common.MediaTttChipState;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaTttChipControllerCommon.kt */
/* loaded from: classes.dex */
public abstract class MediaTttChipControllerCommon<T extends MediaTttChipState> {
    public Runnable cancelChipViewTimeout;
    public final int chipLayoutRes;
    public ViewGroup chipView;
    public final Context context;
    public final DelayableExecutor mainExecutor;
    @SuppressLint({"WrongConstant"})
    public final WindowManager.LayoutParams windowLayoutParams;
    public final WindowManager windowManager;

    public abstract void updateChipView(T t, ViewGroup viewGroup);

    public final void displayChip(T t) {
        ViewGroup viewGroup = this.chipView;
        if (viewGroup == null) {
            View inflate = LayoutInflater.from(this.context).inflate(this.chipLayoutRes, (ViewGroup) null);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
            this.chipView = (ViewGroup) inflate;
        }
        ViewGroup viewGroup2 = this.chipView;
        Intrinsics.checkNotNull(viewGroup2);
        updateChipView(t, viewGroup2);
        if (viewGroup == null) {
            this.windowManager.addView(this.chipView, this.windowLayoutParams);
        }
        Runnable runnable = this.cancelChipViewTimeout;
        if (runnable != null) {
            runnable.run();
        }
        this.cancelChipViewTimeout = this.mainExecutor.executeDelayed(new Runnable(this) { // from class: com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon$displayChip$1
            public final /* synthetic */ MediaTttChipControllerCommon<T> $tmp0;

            {
                this.$tmp0 = this;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaTttChipControllerCommon<T> mediaTttChipControllerCommon = this.$tmp0;
                Objects.requireNonNull(mediaTttChipControllerCommon);
                ViewGroup viewGroup3 = mediaTttChipControllerCommon.chipView;
                if (viewGroup3 != null) {
                    mediaTttChipControllerCommon.windowManager.removeView(viewGroup3);
                    mediaTttChipControllerCommon.chipView = null;
                }
            }
        }, 3000L);
    }

    public MediaTttChipControllerCommon(Context context, WindowManager windowManager, DelayableExecutor delayableExecutor, int i) {
        this.context = context;
        this.windowManager = windowManager;
        this.mainExecutor = delayableExecutor;
        this.chipLayoutRes = i;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.gravity = 49;
        layoutParams.type = 2020;
        layoutParams.flags = 32;
        layoutParams.setTitle("Media Transfer Chip View");
        layoutParams.format = -3;
        layoutParams.setTrustedOverlay();
        this.windowLayoutParams = layoutParams;
    }

    public final void setIcon$frameworks__base__packages__SystemUI__android_common__SystemUI_core(T t, ViewGroup viewGroup) {
        int i;
        CachingIconView requireViewById = viewGroup.requireViewById(2131427500);
        requireViewById.setContentDescription(t.getAppName(this.context));
        Drawable appIcon = t.getAppIcon(this.context);
        if (appIcon != null) {
            i = 0;
        } else {
            i = 8;
        }
        requireViewById.setImageDrawable(appIcon);
        requireViewById.setVisibility(i);
    }
}
