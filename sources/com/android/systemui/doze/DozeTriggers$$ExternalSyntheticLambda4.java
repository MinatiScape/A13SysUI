package com.android.systemui.doze;

import android.net.Uri;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import android.util.Log;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.telephony.TelephonyCallback;
import com.android.systemui.tuner.SelectablePreference;
import com.android.systemui.tuner.ShortcutPicker;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        switch (this.$r8$classId) {
            case 0:
                DozeTriggers dozeTriggers = (DozeTriggers) this.f$0;
                boolean booleanValue = ((Boolean) obj).booleanValue();
                Objects.requireNonNull(dozeTriggers);
                if (dozeTriggers.mMachine.isExecutingTransition()) {
                    Log.w("DozeTriggers", "onProximityFar called during transition. Ignoring sensor response.");
                    return;
                }
                boolean z4 = !booleanValue;
                DozeMachine.State state = dozeTriggers.mMachine.getState();
                if (state == DozeMachine.State.DOZE_AOD_PAUSED) {
                    z = true;
                } else {
                    z = false;
                }
                DozeMachine.State state2 = DozeMachine.State.DOZE_AOD_PAUSING;
                if (state == state2) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                DozeMachine.State state3 = DozeMachine.State.DOZE_AOD;
                if (state != state3) {
                    z3 = false;
                }
                if (state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT) {
                    if (DozeTriggers.DEBUG) {
                        Log.i("DozeTriggers", "Prox changed, ignore touch = " + z4);
                    }
                    dozeTriggers.mDozeHost.onIgnoreTouchWhilePulsing(z4);
                }
                if (booleanValue && (z || z2)) {
                    if (DozeTriggers.DEBUG) {
                        Log.i("DozeTriggers", "Prox FAR, unpausing AOD");
                    }
                    dozeTriggers.mMachine.requestState(state3);
                    return;
                } else if (z4 && z3) {
                    if (DozeTriggers.DEBUG) {
                        Log.i("DozeTriggers", "Prox NEAR, pausing AOD");
                    }
                    dozeTriggers.mMachine.requestState(state2);
                    return;
                } else {
                    return;
                }
            case 1:
                Messenger messenger = (Messenger) this.f$0;
                Uri uri = (Uri) obj;
                int i = TakeScreenshotService.$r8$clinit;
                try {
                    messenger.send(Message.obtain(null, 1, uri));
                    return;
                } catch (RemoteException e) {
                    Log.d("Screenshot", "ignored remote exception", e);
                    return;
                }
            case 2:
                int i2 = TelephonyCallback.$r8$clinit;
                ((TelephonyCallback.ServiceStateListener) obj).onServiceStateChanged((ServiceState) this.f$0);
                return;
            default:
                SelectablePreference selectablePreference = (SelectablePreference) obj;
                int i3 = ShortcutPicker.$r8$clinit;
                selectablePreference.setChecked(((String) this.f$0).equals(selectablePreference.toString()));
                return;
        }
    }
}
