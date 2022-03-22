package com.google.android.systemui.input;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.Display;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
import com.google.input.ContextPacket;
import com.google.input.ITouchContextService;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TouchContextService implements DisplayManager.DisplayListener {
    public static final String INTERFACE = MotionController$$ExternalSyntheticOutline1.m(new StringBuilder(), ITouchContextService.DESCRIPTOR, "/default");
    public final DisplayManager mDm;
    @GuardedBy({"mLock"})
    public ITouchContextService mService;
    public final Object mLock = new Object();
    public int mLastRotation = -1;

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayAdded(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayRemoved(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayChanged(int i) {
        Display display;
        int rotation;
        ITouchContextService iTouchContextService;
        if (i == 0 && (display = this.mDm.getDisplay(i)) != null && (rotation = display.getRotation()) != this.mLastRotation) {
            Display.Mode mode = display.getMode();
            ContextPacket contextPacket = new ContextPacket();
            byte b = (byte) rotation;
            if (mode.getPhysicalWidth() > mode.getPhysicalHeight()) {
                b = (byte) ((b + 1) % 4);
            }
            contextPacket.orientation = b;
            ITouchContextService iTouchContextService2 = this.mService;
            if (iTouchContextService2 == null) {
                final IBinder service = ServiceManager.getService(INTERFACE);
                if (service == null) {
                    Log.e("TouchContextService", "Failed to get ITouchContextService despite being declared.");
                } else {
                    try {
                        service.linkToDeath(new IBinder.DeathRecipient() { // from class: com.google.android.systemui.input.TouchContextService$$ExternalSyntheticLambda0
                            @Override // android.os.IBinder.DeathRecipient
                            public final void binderDied() {
                                TouchContextService touchContextService = TouchContextService.this;
                                IBinder iBinder = service;
                                Objects.requireNonNull(touchContextService);
                                synchronized (touchContextService.mLock) {
                                    if (touchContextService.mService.asBinder() == iBinder) {
                                        touchContextService.mService = null;
                                    }
                                }
                            }
                        }, 0);
                        int i2 = ITouchContextService.Stub.$r8$clinit;
                        IInterface queryLocalInterface = service.queryLocalInterface(ITouchContextService.DESCRIPTOR);
                        if (queryLocalInterface == null || !(queryLocalInterface instanceof ITouchContextService)) {
                            iTouchContextService = new ITouchContextService.Stub.Proxy(service);
                        } else {
                            iTouchContextService = (ITouchContextService) queryLocalInterface;
                        }
                        iTouchContextService2 = iTouchContextService;
                        this.mService = iTouchContextService2;
                    } catch (RemoteException e) {
                        Log.e("TouchContextService", "Failed to link to death on ITouchContextService. Binder is probably dead.", e);
                    }
                }
                iTouchContextService2 = null;
            }
            if (iTouchContextService2 == null) {
                Log.e("TouchContextService", "Failed to get touch context service, dropping context packet.");
                return;
            }
            try {
                iTouchContextService2.updateContext(contextPacket);
                this.mLastRotation = rotation;
            } catch (RemoteException e2) {
                Log.e("TouchContextService", "Failed to send input context packet.", e2);
            }
        }
    }

    public TouchContextService(Context context) {
        Handler handler = BackgroundThread.getHandler();
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDm = displayManager;
        if (!ServiceManager.isDeclared(INTERFACE)) {
            Log.d("TouchContextService", "No ITouchContextService declared in manifest, not sending input context");
            return;
        }
        displayManager.registerDisplayListener(this, handler);
        handler.post(new WifiEntry$$ExternalSyntheticLambda0(this, 8));
    }
}
