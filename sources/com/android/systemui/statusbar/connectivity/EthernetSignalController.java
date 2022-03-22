package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import androidx.leanback.R$layout;
import com.android.settingslib.SignalIcon$IconGroup;
import com.android.systemui.flags.Flags;
import java.util.BitSet;
/* loaded from: classes.dex */
public final class EthernetSignalController extends SignalController<ConnectivityState, SignalIcon$IconGroup> {
    public EthernetSignalController(Context context, CallbackHandler callbackHandler, NetworkControllerImpl networkControllerImpl) {
        super("EthernetSignalController", context, 3, callbackHandler, networkControllerImpl);
        T t = this.mCurrentState;
        T t2 = this.mLastState;
        int[][] iArr = Flags.ETHERNET_ICONS;
        int[] iArr2 = R$layout.ETHERNET_CONNECTION_VALUES;
        SignalIcon$IconGroup signalIcon$IconGroup = new SignalIcon$IconGroup("Ethernet Icons", iArr, null, iArr2, 0, 0, 0, 0, iArr2[0]);
        t2.iconGroup = signalIcon$IconGroup;
        t.iconGroup = signalIcon$IconGroup;
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final ConnectivityState cleanState() {
        return new ConnectivityState();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final int getContentDescription() {
        T t = this.mCurrentState;
        if (t.connected) {
            return t.iconGroup.contentDesc[1];
        }
        return t.iconGroup.discContentDesc;
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalController
    public final void notifyListeners(SignalCallback signalCallback) {
        signalCallback.setEthernetIndicators(new IconState(this.mCurrentState.connected, getCurrentIconId(), getTextIfExists(getContentDescription()).toString()));
    }

    public final void updateConnectivity(BitSet bitSet, BitSet bitSet2) {
        this.mCurrentState.connected = bitSet.get(this.mTransportType);
        this.mCurrentState.inetCondition = bitSet2.get(this.mTransportType) ? 1 : 0;
        notifyListenersIfNecessary();
    }
}
