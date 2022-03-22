package com.android.systemui.shared.system.smartspace;

import android.os.IInterface;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ILauncherUnlockAnimationController extends IInterface {
    void playUnlockAnimation() throws RemoteException;

    void prepareForUnlock(boolean z, int i) throws RemoteException;

    void setSmartspaceVisibility(int i) throws RemoteException;

    void setUnlockAmount(float f) throws RemoteException;
}
