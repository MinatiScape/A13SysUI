package com.android.wm.shell.recents;

import android.os.IInterface;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IRecentTasksListener extends IInterface {
    void onRecentTasksChanged() throws RemoteException;
}
