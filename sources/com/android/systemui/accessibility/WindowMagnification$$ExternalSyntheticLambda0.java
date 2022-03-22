package com.android.systemui.accessibility;

import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.util.Assert;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WindowMagnification$$ExternalSyntheticLambda0 implements MagnificationModeSwitch.SwitchListener, Pluggable.PluggableListener {
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WindowMagnification$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
    public final void onPluggableInvalidated(Object obj) {
        ShadeListBuilder shadeListBuilder = (ShadeListBuilder) this.f$0;
        NotifSectioner notifSectioner = (NotifSectioner) obj;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        ShadeListBuilderLogger shadeListBuilderLogger = shadeListBuilder.mLogger;
        Objects.requireNonNull(notifSectioner);
        String str = notifSectioner.mName;
        PipelineState pipelineState = shadeListBuilder.mPipelineState;
        Objects.requireNonNull(pipelineState);
        shadeListBuilderLogger.logNotifSectionInvalidated(str, pipelineState.mState);
        shadeListBuilder.rebuildListIfBefore(7);
    }

    @Override // com.android.systemui.accessibility.MagnificationModeSwitch.SwitchListener
    public final void onSwitch(int i, int i2) {
        WindowMagnificationConnectionImpl windowMagnificationConnectionImpl = (WindowMagnificationConnectionImpl) this.f$0;
        Objects.requireNonNull(windowMagnificationConnectionImpl);
        IWindowMagnificationConnectionCallback iWindowMagnificationConnectionCallback = windowMagnificationConnectionImpl.mConnectionCallback;
        if (iWindowMagnificationConnectionCallback != null) {
            try {
                iWindowMagnificationConnectionCallback.onChangeMagnificationMode(i, i2);
            } catch (RemoteException e) {
                Log.e("WindowMagnificationConnectionImpl", "Failed to inform changing magnification mode", e);
            }
        }
    }
}
