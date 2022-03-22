package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.systemui.statusbar.connectivity.ConnectivityState;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public abstract class SignalController<T extends ConnectivityState, I extends SignalIcon$IconGroup> {
    public final CallbackHandler mCallbackHandler;
    public final Context mContext;
    public int mHistoryIndex;
    public final NetworkControllerImpl mNetworkController;
    public final String mTag;
    public final int mTransportType;
    public static final boolean DEBUG = NetworkControllerImpl.DEBUG;
    public static final boolean CHATTY = NetworkControllerImpl.CHATTY;
    public final T mCurrentState = cleanState();
    public final T mLastState = cleanState();
    public final ConnectivityState[] mHistory = new ConnectivityState[64];

    public abstract T cleanState();

    public abstract void notifyListeners(SignalCallback signalCallback);

    public void dump(PrintWriter printWriter) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  - ");
        m.append(this.mTag);
        m.append(" -----");
        printWriter.println(m.toString());
        printWriter.println("  Current State: " + this.mCurrentState);
        int i = 0;
        for (int i2 = 0; i2 < 64; i2++) {
            if (this.mHistory[i2].time != 0) {
                i++;
            }
        }
        int i3 = this.mHistoryIndex + 64;
        while (true) {
            i3--;
            if (i3 >= (this.mHistoryIndex + 64) - i) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Previous State(");
                m2.append((this.mHistoryIndex + 64) - i3);
                m2.append("): ");
                m2.append(this.mHistory[i3 & 63]);
                printWriter.println(m2.toString());
            } else {
                return;
            }
        }
    }

    public int getContentDescription() {
        T t = this.mCurrentState;
        if (t.connected) {
            return t.iconGroup.contentDesc[t.level];
        }
        return t.iconGroup.discContentDesc;
    }

    public int getCurrentIconId() {
        T t = this.mCurrentState;
        if (t.connected) {
            return t.iconGroup.sbIcons[t.inetCondition][t.level];
        }
        if (t.enabled) {
            return t.iconGroup.sbDiscState;
        }
        return t.iconGroup.sbNullState;
    }

    public final CharSequence getTextIfExists(int i) {
        if (i != 0) {
            return this.mContext.getText(i);
        }
        return "";
    }

    public final void notifyCallStateChange(IconState iconState, int i) {
        this.mCallbackHandler.setCallIndicator(iconState, i);
    }

    public final void notifyListeners() {
        notifyListeners(this.mCallbackHandler);
    }

    public final void notifyListenersIfNecessary() {
        boolean z;
        if (!this.mLastState.equals(this.mCurrentState)) {
            if (DEBUG) {
                String str = this.mTag;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Change in state from: ");
                m.append(this.mLastState);
                m.append("\n\tto: ");
                m.append(this.mCurrentState);
                Log.d(str, m.toString());
            }
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mHistory[this.mHistoryIndex].copyFrom(this.mLastState);
            this.mHistoryIndex = (this.mHistoryIndex + 1) % 64;
            this.mCurrentState.time = System.currentTimeMillis();
            this.mLastState.copyFrom(this.mCurrentState);
            notifyListeners();
        }
    }

    public SignalController(String str, Context context, int i, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl) {
        this.mTag = SupportMenuInflater$$ExternalSyntheticOutline0.m("NetworkController.", str);
        this.mNetworkController = networkControllerImpl;
        this.mTransportType = i;
        this.mContext = context;
        this.mCallbackHandler = callbackHandler;
        for (int i2 = 0; i2 < 64; i2++) {
            this.mHistory[i2] = cleanState();
        }
    }
}
