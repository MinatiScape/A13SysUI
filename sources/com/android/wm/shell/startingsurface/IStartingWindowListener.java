package com.android.wm.shell.startingsurface;

import android.os.IInterface;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IStartingWindowListener extends IInterface {
    void onTaskLaunching(int i, int i2, int i3) throws RemoteException;
}
