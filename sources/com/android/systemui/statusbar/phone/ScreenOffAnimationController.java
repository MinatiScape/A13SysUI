package com.android.systemui.statusbar.phone;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import kotlin.collections.ArraysKt___ArraysKt;
/* compiled from: ScreenOffAnimationController.kt */
/* loaded from: classes.dex */
public final class ScreenOffAnimationController implements WakefulnessLifecycle.Observer {
    public final ArrayList animations;
    public final WakefulnessLifecycle wakefulnessLifecycle;

    public final boolean isKeyguardShowDelayed() {
        ArrayList arrayList = this.animations;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).isKeyguardShowDelayed()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public final void onStartedGoingToSleep() {
        Iterator it = this.animations.iterator();
        while (it.hasNext() && !((ScreenOffAnimation) it.next()).startAnimation()) {
        }
    }

    public final boolean overrideNotificationsFullyDozingOnKeyguard() {
        ArrayList arrayList = this.animations;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).overrideNotificationsDozeAmount()) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean shouldExpandNotifications() {
        ArrayList arrayList = this.animations;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).isAnimationPlaying()) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean shouldIgnoreKeyguardTouches() {
        ArrayList arrayList = this.animations;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).isAnimationPlaying()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ScreenOffAnimationController(Optional<SysUIUnfoldComponent> optional, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, WakefulnessLifecycle wakefulnessLifecycle) {
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        FoldAodAnimationController foldAodAnimationController = null;
        SysUIUnfoldComponent orElse = optional.orElse(null);
        this.animations = ArraysKt___ArraysKt.filterNotNull(new ScreenOffAnimation[]{orElse != null ? orElse.getFoldAodAnimationController() : foldAodAnimationController, unlockedScreenOffAnimationController});
    }
}
