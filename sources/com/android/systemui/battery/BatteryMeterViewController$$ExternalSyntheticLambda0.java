package com.android.systemui.battery;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.view.IScrollCaptureConnection;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.concurrent.futures.ResolvableFuture;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.util.Assert;
import com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda1;
import com.google.android.systemui.elmyra.proto.nano.ContextHubMessages$SensitivityUpdate;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.protobuf.nano.MessageNano;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BatteryMeterViewController$$ExternalSyntheticLambda0 implements BatteryMeterView.BatteryEstimateFetcher, CallbackToFutureAdapter.Resolver, Pluggable.PluggableListener, GestureConfiguration.Listener {
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BatteryMeterViewController$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }

    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        ScrollCaptureClient.SessionWrapper sessionWrapper = (ScrollCaptureClient.SessionWrapper) this.f$0;
        int i = ScrollCaptureClient.SessionWrapper.$r8$clinit;
        Objects.requireNonNull(sessionWrapper);
        IScrollCaptureConnection iScrollCaptureConnection = sessionWrapper.mConnection;
        if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
            completer.setException(new DeadObjectException("Connection is closed!"));
            return "";
        }
        try {
            sessionWrapper.mTileRequestCompleter = completer;
            sessionWrapper.mCancellationSignal = sessionWrapper.mConnection.requestImage(sessionWrapper.mRequestRect);
            OneHandedController$$ExternalSyntheticLambda1 oneHandedController$$ExternalSyntheticLambda1 = new OneHandedController$$ExternalSyntheticLambda1(sessionWrapper, 4);
            SaveImageInBackgroundTask$$ExternalSyntheticLambda0 saveImageInBackgroundTask$$ExternalSyntheticLambda0 = SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE;
            ResolvableFuture<Void> resolvableFuture = completer.cancellationFuture;
            if (resolvableFuture != null) {
                resolvableFuture.addListener(oneHandedController$$ExternalSyntheticLambda1, saveImageInBackgroundTask$$ExternalSyntheticLambda0);
            }
        } catch (RemoteException e) {
            completer.setException(e);
        }
        return "IScrollCaptureCallbacks#onImageRequestCompleted";
    }

    @Override // com.google.android.systemui.elmyra.sensors.config.GestureConfiguration.Listener
    public final void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration) {
        CHREGestureSensor cHREGestureSensor = (CHREGestureSensor) this.f$0;
        Objects.requireNonNull(cHREGestureSensor);
        ContextHubMessages$SensitivityUpdate contextHubMessages$SensitivityUpdate = new ContextHubMessages$SensitivityUpdate();
        contextHubMessages$SensitivityUpdate.sensitivity = gestureConfiguration.getSensitivity();
        cHREGestureSensor.sendMessageToNanoApp(202, MessageNano.toByteArray(contextHubMessages$SensitivityUpdate));
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable.PluggableListener
    public final void onPluggableInvalidated(Object obj) {
        ShadeListBuilder shadeListBuilder = (ShadeListBuilder) this.f$0;
        NotifFilter notifFilter = (NotifFilter) obj;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        ShadeListBuilderLogger shadeListBuilderLogger = shadeListBuilder.mLogger;
        Objects.requireNonNull(notifFilter);
        String str = notifFilter.mName;
        PipelineState pipelineState = shadeListBuilder.mPipelineState;
        Objects.requireNonNull(pipelineState);
        shadeListBuilderLogger.logPreGroupFilterInvalidated(str, pipelineState.mState);
        shadeListBuilder.rebuildListIfBefore(3);
    }
}
