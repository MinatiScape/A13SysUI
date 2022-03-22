package com.google.android.systemui.statusbar;

import android.os.IInterface;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface INotificationVoiceReplyServiceCallbacks extends IInterface {
    void onNotifAvailableForQuickPhraseChanged(boolean z) throws RemoteException;

    void onNotifAvailableForReplyChanged(boolean z) throws RemoteException;

    void onVoiceReplyHandled(int i, int i2) throws RemoteException;
}
