package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.Log;
/* compiled from: VrMode.kt */
/* loaded from: classes.dex */
public final class VrMode extends Gate {
    public boolean inVrMode;
    public final IVrManager vrManager = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));
    public final VrMode$vrStateCallbacks$1 vrStateCallbacks = new IVrStateCallbacks.Stub() { // from class: com.google.android.systemui.columbus.gates.VrMode$vrStateCallbacks$1
        public final void onVrStateChanged(boolean z) {
            VrMode vrMode = VrMode.this;
            vrMode.inVrMode = z;
            vrMode.setBlocking(z);
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        IVrManager iVrManager = this.vrManager;
        if (iVrManager != null) {
            try {
                boolean z = true;
                if (!iVrManager.getVrModeState()) {
                    z = false;
                }
                this.inVrMode = z;
                iVrManager.registerListener(this.vrStateCallbacks);
            } catch (RemoteException e) {
                Log.e("Columbus/VrMode", "Could not register IVrManager listener", e);
                this.inVrMode = false;
            }
        }
        setBlocking(this.inVrMode);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        try {
            IVrManager iVrManager = this.vrManager;
            if (iVrManager != null) {
                iVrManager.unregisterListener(this.vrStateCallbacks);
            }
        } catch (RemoteException e) {
            Log.e("Columbus/VrMode", "Could not unregister IVrManager listener", e);
        }
        this.inVrMode = false;
    }

    public VrMode(Context context) {
        super(context);
    }
}
