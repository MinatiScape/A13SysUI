package com.android.systemui;

import android.content.Context;
import android.view.IRemoteAnimationRunner;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.Clock;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SystemUIApplication$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                SystemUIApplication systemUIApplication = (SystemUIApplication) this.f$0;
                String str = (String) this.f$1;
                int i = SystemUIApplication.$r8$clinit;
                Objects.requireNonNull(systemUIApplication);
                CoreStartable[] coreStartableArr = systemUIApplication.mServices;
                int length = coreStartableArr.length - 1;
                try {
                    CoreStartable coreStartable = (CoreStartable) Class.forName(str).getConstructor(Context.class).newInstance(systemUIApplication);
                    coreStartable.start();
                    coreStartableArr[length] = coreStartable;
                    return;
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            case 1:
                DreamOverlayStateController dreamOverlayStateController = (DreamOverlayStateController) this.f$0;
                DreamOverlayStateController.Callback callback = (DreamOverlayStateController.Callback) this.f$1;
                int i2 = DreamOverlayStateController.$r8$clinit;
                Objects.requireNonNull(dreamOverlayStateController);
                Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
                dreamOverlayStateController.mCallbacks.remove(callback);
                return;
            case 2:
                StatusBar.AnonymousClass23 r0 = (StatusBar.AnonymousClass23) this.f$0;
                IRemoteAnimationRunner iRemoteAnimationRunner = (IRemoteAnimationRunner) this.f$1;
                Objects.requireNonNull(r0);
                KeyguardViewMediator keyguardViewMediator = StatusBar.this.mKeyguardViewMediator;
                Objects.requireNonNull(keyguardViewMediator);
                if (keyguardViewMediator.mKeyguardDonePending) {
                    keyguardViewMediator.mKeyguardExitAnimationRunner = iRemoteAnimationRunner;
                    keyguardViewMediator.mViewMediatorCallback.readyForKeyguardDone();
                    return;
                }
                return;
            default:
                Clock.AnonymousClass2 r02 = (Clock.AnonymousClass2) this.f$0;
                int i3 = Clock.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r02);
                Clock.this.mCalendar = Calendar.getInstance(TimeZone.getTimeZone((String) this.f$1));
                Clock clock = Clock.this;
                SimpleDateFormat simpleDateFormat = clock.mClockFormat;
                if (simpleDateFormat != null) {
                    simpleDateFormat.setTimeZone(clock.mCalendar.getTimeZone());
                    return;
                }
                return;
        }
    }
}
