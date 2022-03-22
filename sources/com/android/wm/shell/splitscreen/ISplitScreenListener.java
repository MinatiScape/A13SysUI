package com.android.wm.shell.splitscreen;

import android.os.IInterface;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ISplitScreenListener extends IInterface {
    void onStagePositionChanged(int i, int i2) throws RemoteException;

    void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException;
}
