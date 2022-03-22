package com.google.android.systemui.fingerprint;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.google.hardware.pixel.display.IDisplay;
/* loaded from: classes.dex */
public final class UdfpsLhbmProvider implements IBinder.DeathRecipient {
    public volatile IDisplay mDisplayHal;

    @Override // android.os.IBinder.DeathRecipient
    public final void binderDied() {
        Log.e("UdfpsLhbmProvider", "binderDied | Display HAL died");
        this.mDisplayHal = null;
    }

    public final IDisplay getDisplayHal() {
        IDisplay iDisplay;
        IDisplay iDisplay2 = this.mDisplayHal;
        if (iDisplay2 != null) {
            return iDisplay2;
        }
        IBinder waitForDeclaredService = ServiceManager.waitForDeclaredService("com.google.hardware.pixel.display.IDisplay/default");
        if (waitForDeclaredService == null) {
            Log.e("UdfpsLhbmProvider", "getDisplayHal | Failed to find the Display HAL");
            return null;
        }
        try {
            waitForDeclaredService.linkToDeath(this, 0);
            int i = IDisplay.Stub.$r8$clinit;
            IInterface queryLocalInterface = waitForDeclaredService.queryLocalInterface(IDisplay.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDisplay)) {
                iDisplay = new IDisplay.Stub.Proxy(waitForDeclaredService);
            } else {
                iDisplay = (IDisplay) queryLocalInterface;
            }
            this.mDisplayHal = iDisplay;
            return this.mDisplayHal;
        } catch (RemoteException e) {
            Log.e("UdfpsLhbmProvider", "getDisplayHal | Failed to link to death", e);
            return null;
        }
    }

    public UdfpsLhbmProvider() {
        getDisplayHal();
    }
}
