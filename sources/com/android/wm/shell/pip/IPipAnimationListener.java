package com.android.wm.shell.pip;

import android.os.IInterface;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IPipAnimationListener extends IInterface {
    void onPipAnimationStarted() throws RemoteException;

    void onPipCornerRadiusChanged(int i) throws RemoteException;
}
