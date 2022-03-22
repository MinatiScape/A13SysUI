package com.google.android.systemui.columbus.gates;

import android.app.IActivityManager;
import android.app.TaskStackListener;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.gates.Gate;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* compiled from: CameraVisibility.kt */
/* loaded from: classes.dex */
public final class CameraVisibility extends Gate {
    public final IActivityManager activityManager;
    public boolean cameraShowing;
    public boolean exceptionActive;
    public final List<Action> exceptions;
    public final KeyguardVisibility keyguardGate;
    public final PackageManager packageManager;
    public final PowerState powerState;
    public final Handler updateHandler;
    public final CameraVisibility$taskStackListener$1 taskStackListener = new TaskStackListener() { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$taskStackListener$1
        public final void onTaskStackChanged() {
            final CameraVisibility cameraVisibility = CameraVisibility.this;
            cameraVisibility.updateHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$taskStackListener$1$onTaskStackChanged$1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    CameraVisibility cameraVisibility2 = CameraVisibility.this;
                    Objects.requireNonNull(cameraVisibility2);
                    boolean isCameraShowing = cameraVisibility2.isCameraShowing();
                    cameraVisibility2.cameraShowing = isCameraShowing;
                    if (cameraVisibility2.exceptionActive || !isCameraShowing) {
                        z = false;
                    } else {
                        z = true;
                    }
                    cameraVisibility2.setBlocking(z);
                }
            });
        }
    };
    public final CameraVisibility$gateListener$1 gateListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$gateListener$1
        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
            final CameraVisibility cameraVisibility = CameraVisibility.this;
            cameraVisibility.updateHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$gateListener$1$onGateChanged$1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    CameraVisibility cameraVisibility2 = CameraVisibility.this;
                    Objects.requireNonNull(cameraVisibility2);
                    boolean isCameraShowing = cameraVisibility2.isCameraShowing();
                    cameraVisibility2.cameraShowing = isCameraShowing;
                    if (cameraVisibility2.exceptionActive || !isCameraShowing) {
                        z = false;
                    } else {
                        z = true;
                    }
                    cameraVisibility2.setBlocking(z);
                }
            });
        }
    };
    public final CameraVisibility$actionListener$1 actionListener = new Action.Listener() { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$actionListener$1
        @Override // com.google.android.systemui.columbus.actions.Action.Listener
        public final void onActionAvailabilityChanged(Action action) {
            Object obj;
            boolean z;
            CameraVisibility cameraVisibility = CameraVisibility.this;
            Iterator<T> it = cameraVisibility.exceptions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                Action action2 = (Action) obj;
                Objects.requireNonNull(action2);
                if (action2.isAvailable) {
                    break;
                }
            }
            boolean z2 = true;
            if (obj != null) {
                z = true;
            } else {
                z = false;
            }
            cameraVisibility.exceptionActive = z;
            CameraVisibility cameraVisibility2 = CameraVisibility.this;
            Objects.requireNonNull(cameraVisibility2);
            if (cameraVisibility2.exceptionActive || !cameraVisibility2.cameraShowing) {
                z2 = false;
            }
            cameraVisibility2.setBlocking(z2);
        }
    };

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onActivate() {
        boolean z = false;
        this.exceptionActive = false;
        for (Action action : this.exceptions) {
            CameraVisibility$actionListener$1 cameraVisibility$actionListener$1 = this.actionListener;
            Objects.requireNonNull(action);
            action.listeners.add(cameraVisibility$actionListener$1);
            this.exceptionActive = action.isAvailable | this.exceptionActive;
        }
        this.cameraShowing = isCameraShowing();
        this.keyguardGate.registerListener(this.gateListener);
        this.powerState.registerListener(this.gateListener);
        try {
            this.activityManager.registerTaskStackListener(this.taskStackListener);
        } catch (RemoteException e) {
            Log.e("Columbus/CameraVis", "Could not register task stack listener", e);
        }
        if (!this.exceptionActive && this.cameraShowing) {
            z = true;
        }
        setBlocking(z);
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0036 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0072 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isCameraShowing() {
        /*
            r10 = this;
            java.lang.String r0 = "com.google.android.GoogleCamera"
            java.lang.String r1 = "Columbus/CameraVis"
            r2 = 0
            r3 = 0
            r4 = 1
            android.app.IActivityManager r5 = r10.activityManager     // Catch: RemoteException -> 0x002c
            java.util.List r5 = r5.getTasks(r4)     // Catch: RemoteException -> 0x002c
            boolean r6 = r5.isEmpty()     // Catch: RemoteException -> 0x002c
            if (r6 == 0) goto L_0x0014
            goto L_0x0033
        L_0x0014:
            java.lang.Object r5 = r5.get(r2)     // Catch: RemoteException -> 0x002c
            android.app.ActivityManager$RunningTaskInfo r5 = (android.app.ActivityManager.RunningTaskInfo) r5     // Catch: RemoteException -> 0x002c
            android.content.ComponentName r5 = r5.topActivity     // Catch: RemoteException -> 0x002c
            if (r5 != 0) goto L_0x0020
            r5 = r3
            goto L_0x0024
        L_0x0020:
            java.lang.String r5 = r5.getPackageName()     // Catch: RemoteException -> 0x002c
        L_0x0024:
            if (r5 != 0) goto L_0x0027
            goto L_0x0033
        L_0x0027:
            boolean r5 = r5.equalsIgnoreCase(r0)     // Catch: RemoteException -> 0x002c
            goto L_0x0034
        L_0x002c:
            r5 = move-exception
            java.lang.String r6 = "unable to check task stack"
            android.util.Log.e(r1, r6, r5)
        L_0x0033:
            r5 = r2
        L_0x0034:
            if (r5 == 0) goto L_0x0095
            android.content.pm.PackageManager r5 = r10.packageManager     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            android.app.IActivityManager r6 = r10.activityManager     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            android.content.pm.UserInfo r6 = r6.getCurrentUser()     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            int r6 = r6.id     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            android.content.pm.ApplicationInfo r5 = r5.getApplicationInfoAsUser(r0, r2, r6)     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            int r5 = r5.uid     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            android.app.IActivityManager r6 = r10.activityManager     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            java.util.List r6 = r6.getRunningAppProcesses()     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            java.util.Iterator r6 = r6.iterator()     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
        L_0x0050:
            boolean r7 = r6.hasNext()     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            if (r7 == 0) goto L_0x0073
            java.lang.Object r7 = r6.next()     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            r8 = r7
            android.app.ActivityManager$RunningAppProcessInfo r8 = (android.app.ActivityManager.RunningAppProcessInfo) r8     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            int r9 = r8.uid     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            if (r9 != r5) goto L_0x006f
            java.lang.String r8 = r8.processName     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            if (r8 != 0) goto L_0x0067
            r8 = r2
            goto L_0x006b
        L_0x0067:
            boolean r8 = r8.equalsIgnoreCase(r0)     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
        L_0x006b:
            if (r8 == 0) goto L_0x006f
            r8 = r4
            goto L_0x0070
        L_0x006f:
            r8 = r2
        L_0x0070:
            if (r8 == 0) goto L_0x0050
            goto L_0x0074
        L_0x0073:
            r7 = r3
        L_0x0074:
            android.app.ActivityManager$RunningAppProcessInfo r7 = (android.app.ActivityManager.RunningAppProcessInfo) r7     // Catch: RemoteException -> 0x0078, NameNotFoundException -> 0x007e
            r3 = r7
            goto L_0x007e
        L_0x0078:
            r0 = move-exception
            java.lang.String r5 = "Could not check camera foreground status"
            android.util.Log.e(r1, r5, r0)
        L_0x007e:
            if (r3 != 0) goto L_0x0081
            goto L_0x0089
        L_0x0081:
            int r0 = r3.importance
            r1 = 100
            if (r0 != r1) goto L_0x0089
            r0 = r4
            goto L_0x008a
        L_0x0089:
            r0 = r2
        L_0x008a:
            if (r0 == 0) goto L_0x0095
            com.google.android.systemui.columbus.gates.PowerState r10 = r10.powerState
            boolean r10 = r10.isBlocking()
            if (r10 != 0) goto L_0x0095
            r2 = r4
        L_0x0095:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.gates.CameraVisibility.isCameraShowing():boolean");
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final void onDeactivate() {
        this.keyguardGate.unregisterListener(this.gateListener);
        this.powerState.unregisterListener(this.gateListener);
        for (Action action : this.exceptions) {
            CameraVisibility$actionListener$1 cameraVisibility$actionListener$1 = this.actionListener;
            Objects.requireNonNull(action);
            action.listeners.remove(cameraVisibility$actionListener$1);
        }
        try {
            this.activityManager.unregisterTaskStackListener(this.taskStackListener);
        } catch (RemoteException e) {
            Log.e("Columbus/CameraVis", "Could not unregister task stack listener", e);
        }
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [cameraShowing -> ");
        sb.append(this.cameraShowing);
        sb.append("; exceptionActive -> ");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.exceptionActive, ']');
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.systemui.columbus.gates.CameraVisibility$gateListener$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.google.android.systemui.columbus.gates.CameraVisibility$actionListener$1] */
    public CameraVisibility(Context context, List<Action> list, KeyguardVisibility keyguardVisibility, PowerState powerState, IActivityManager iActivityManager, Handler handler) {
        super(context);
        this.exceptions = list;
        this.keyguardGate = keyguardVisibility;
        this.powerState = powerState;
        this.activityManager = iActivityManager;
        this.updateHandler = handler;
        this.packageManager = context.getPackageManager();
    }
}
