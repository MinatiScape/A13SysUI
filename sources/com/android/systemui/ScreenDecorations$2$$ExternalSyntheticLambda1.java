package com.android.systemui;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.decor.OverlayWindow;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$2$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ScreenDecorations$2$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations.AnonymousClass2 r0 = (ScreenDecorations.AnonymousClass2) this.f$0;
                View view = (View) this.f$1;
                for (int i = 0; i < 4; i++) {
                    OverlayWindow[] overlayWindowArr = ScreenDecorations.this.mOverlays;
                    if (overlayWindowArr[i] != null) {
                        OverlayWindow overlayWindow = overlayWindowArr[i];
                        Objects.requireNonNull(overlayWindow);
                        ViewGroup viewGroup = overlayWindow.rootView;
                        if (viewGroup.findViewById(view.getId()) != null) {
                            viewGroup.setVisibility(0);
                        }
                    }
                }
                Objects.requireNonNull(r0);
                return;
            default:
                CallbackHandler callbackHandler = (CallbackHandler) this.f$0;
                WifiIndicators wifiIndicators = (WifiIndicators) this.f$1;
                SimpleDateFormat simpleDateFormat = CallbackHandler.SSDF;
                Objects.requireNonNull(callbackHandler);
                Iterator<SignalCallback> it = callbackHandler.mSignalCallbacks.iterator();
                while (it.hasNext()) {
                    it.next().setWifiIndicators(wifiIndicators);
                }
                return;
        }
    }
}
