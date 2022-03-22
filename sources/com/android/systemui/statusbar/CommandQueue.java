package com.android.systemui.statusbar;

import android.app.ITransientNotificationCallback;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.hardware.biometrics.IBiometricContextListener;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.media.INearbyMediaDevicesProvider;
import android.media.MediaRoute2Info;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.InsetsVisibilities;
import com.android.internal.os.SomeArgs;
import com.android.internal.statusbar.IAddTileResultCallback;
import com.android.internal.statusbar.IStatusBar;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.GcUtils;
import com.android.internal.view.AppearanceRegion;
import com.android.systemui.shared.tracing.FrameProtoTracer;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.google.protobuf.nano.MessageNano;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CommandQueue extends IStatusBar.Stub implements CallbackController<Callbacks>, DisplayManager.DisplayListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ProtoTracer mProtoTracer;
    public final CommandRegistry mRegistry;
    public final Object mLock = new Object();
    public ArrayList<Callbacks> mCallbacks = new ArrayList<>();
    public H mHandler = new H(Looper.getMainLooper());
    public SparseArray<Pair<Integer, Integer>> mDisplayDisabled = new SparseArray<>();
    public int mLastUpdatedImeDisplayId = -1;

    /* loaded from: classes.dex */
    public interface Callbacks {
        default void abortTransient(int i, int[] iArr) {
        }

        default void addQsTile(ComponentName componentName) {
        }

        default void animateCollapsePanels(int i, boolean z) {
        }

        default void animateExpandNotificationsPanel() {
        }

        default void animateExpandSettingsPanel(String str) {
        }

        default void appTransitionCancelled(int i) {
        }

        default void appTransitionFinished(int i) {
        }

        default void appTransitionPending(int i, boolean z) {
        }

        default void appTransitionStarting(int i, long j, long j2, boolean z) {
        }

        default void cancelPreloadRecentApps() {
        }

        default void cancelRequestAddTile(String str) {
        }

        default void clickTile(ComponentName componentName) {
        }

        default void disable(int i, int i2, int i3, boolean z) {
        }

        default void dismissInattentiveSleepWarning(boolean z) {
        }

        default void dismissKeyboardShortcutsMenu() {
        }

        default void handleShowGlobalActionsMenu() {
        }

        default void handleShowShutdownUi(boolean z, String str) {
        }

        default void handleSystemKey(int i) {
        }

        default void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        }

        default void hideAuthenticationDialog() {
        }

        default void hideRecentApps(boolean z, boolean z2) {
        }

        default void hideToast(String str, IBinder iBinder) {
        }

        default void onBiometricAuthenticated() {
        }

        default void onBiometricError(int i, int i2, int i3) {
        }

        default void onBiometricHelp(int i, String str) {
        }

        default void onCameraLaunchGestureDetected(int i) {
        }

        default void onDisplayReady(int i) {
        }

        default void onDisplayRemoved(int i) {
        }

        default void onEmergencyActionLaunchGestureDetected() {
        }

        default void onRecentsAnimationStateChanged(boolean z) {
        }

        default void onRotationProposal(int i, boolean z) {
        }

        default void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        }

        default void onTracingStateChanged(boolean z) {
        }

        default void preloadRecentApps() {
        }

        default void registerNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        }

        default void remQsTile(ComponentName componentName) {
        }

        default void removeIcon(String str) {
        }

        default void requestAddTile(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, IAddTileResultCallback iAddTileResultCallback) {
        }

        default void requestWindowMagnificationConnection(boolean z) {
        }

        default void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        }

        default void setIcon(String str, StatusBarIcon statusBarIcon) {
        }

        default void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        }

        default void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        }

        default void setTopAppHidesStatusBar(boolean z) {
        }

        default void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        }

        default void setWindowState(int i, int i2, int i3) {
        }

        default void showAssistDisclosure() {
        }

        default void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        }

        default void showInattentiveSleepWarning() {
        }

        default void showPictureInPictureMenu() {
        }

        default void showPinningEnterExitToast(boolean z) {
        }

        default void showPinningEscapeToast() {
        }

        default void showRecentApps(boolean z) {
        }

        default void showScreenPinningRequest(int i) {
        }

        default void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
        }

        default void showTransient(int i, int[] iArr, boolean z) {
        }

        default void showWirelessChargingAnimation(int i) {
        }

        default void startAssist(Bundle bundle) {
        }

        default void suppressAmbientDisplay(boolean z) {
        }

        default void toggleKeyboardShortcutsMenu(int i) {
        }

        default void togglePanel() {
        }

        default void toggleRecentApps() {
        }

        default void toggleSplitScreen() {
        }

        default void unregisterNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        }

        default void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        }

        default void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        }
    }

    /* loaded from: classes.dex */
    public final class H extends Handler {
        public H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            boolean z7;
            boolean z8;
            boolean z9;
            boolean z10;
            boolean z11;
            boolean z12;
            boolean z13;
            boolean z14 = true;
            int i = 0;
            switch (message.what & (-65536)) {
                case 65536:
                    int i2 = message.arg1;
                    if (i2 == 1) {
                        Pair pair = (Pair) message.obj;
                        while (i < CommandQueue.this.mCallbacks.size()) {
                            CommandQueue.this.mCallbacks.get(i).setIcon((String) pair.first, (StatusBarIcon) pair.second);
                            i++;
                        }
                        return;
                    } else if (i2 == 2) {
                        while (i < CommandQueue.this.mCallbacks.size()) {
                            CommandQueue.this.mCallbacks.get(i).removeIcon((String) message.obj);
                            i++;
                        }
                        return;
                    } else {
                        return;
                    }
                case 131072:
                    SomeArgs someArgs = (SomeArgs) message.obj;
                    for (int i3 = 0; i3 < CommandQueue.this.mCallbacks.size(); i3++) {
                        Callbacks callbacks = CommandQueue.this.mCallbacks.get(i3);
                        int i4 = someArgs.argi1;
                        int i5 = someArgs.argi2;
                        int i6 = someArgs.argi3;
                        if (someArgs.argi4 != 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                        callbacks.disable(i4, i5, i6, z);
                    }
                    return;
                case 196608:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).animateExpandNotificationsPanel();
                        i++;
                    }
                    return;
                case 262144:
                    for (int i7 = 0; i7 < CommandQueue.this.mCallbacks.size(); i7++) {
                        Callbacks callbacks2 = CommandQueue.this.mCallbacks.get(i7);
                        int i8 = message.arg1;
                        if (message.arg2 != 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        callbacks2.animateCollapsePanels(i8, z2);
                    }
                    return;
                case 327680:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).animateExpandSettingsPanel((String) message.obj);
                        i++;
                    }
                    return;
                case 393216:
                    SomeArgs someArgs2 = (SomeArgs) message.obj;
                    for (int i9 = 0; i9 < CommandQueue.this.mCallbacks.size(); i9++) {
                        Callbacks callbacks3 = CommandQueue.this.mCallbacks.get(i9);
                        int i10 = someArgs2.argi1;
                        int i11 = someArgs2.argi2;
                        AppearanceRegion[] appearanceRegionArr = (AppearanceRegion[]) someArgs2.arg1;
                        if (someArgs2.argi3 == 1) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        callbacks3.onSystemBarAttributesChanged(i10, i11, appearanceRegionArr, z3, someArgs2.argi4, (InsetsVisibilities) someArgs2.arg2, (String) someArgs2.arg3);
                    }
                    someArgs2.recycle();
                    return;
                case 458752:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onDisplayReady(message.arg1);
                        i++;
                    }
                    return;
                case 524288:
                    SomeArgs someArgs3 = (SomeArgs) message.obj;
                    CommandQueue commandQueue = CommandQueue.this;
                    int i12 = someArgs3.argi1;
                    IBinder iBinder = (IBinder) someArgs3.arg1;
                    int i13 = someArgs3.argi2;
                    int i14 = someArgs3.argi3;
                    if (someArgs3.argi4 == 0) {
                        z14 = false;
                    }
                    if (i12 == -1) {
                        Objects.requireNonNull(commandQueue);
                        return;
                    }
                    int i15 = commandQueue.mLastUpdatedImeDisplayId;
                    if (!(i15 == i12 || i15 == -1)) {
                        for (int i16 = 0; i16 < commandQueue.mCallbacks.size(); i16++) {
                            commandQueue.mCallbacks.get(i16).setImeWindowStatus(commandQueue.mLastUpdatedImeDisplayId, null, 4, 0, false);
                        }
                    }
                    while (i < commandQueue.mCallbacks.size()) {
                        commandQueue.mCallbacks.get(i).setImeWindowStatus(i12, iBinder, i13, i14, z14);
                        i++;
                    }
                    commandQueue.mLastUpdatedImeDisplayId = i12;
                    return;
                case 589824:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).toggleRecentApps();
                        i++;
                    }
                    return;
                case 655360:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).preloadRecentApps();
                        i++;
                    }
                    return;
                case 720896:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).cancelPreloadRecentApps();
                        i++;
                    }
                    return;
                case 786432:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).setWindowState(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                        i++;
                    }
                    return;
                case 851968:
                    for (int i17 = 0; i17 < CommandQueue.this.mCallbacks.size(); i17++) {
                        Callbacks callbacks4 = CommandQueue.this.mCallbacks.get(i17);
                        if (message.arg1 != 0) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                        callbacks4.showRecentApps(z4);
                    }
                    return;
                case 917504:
                    for (int i18 = 0; i18 < CommandQueue.this.mCallbacks.size(); i18++) {
                        Callbacks callbacks5 = CommandQueue.this.mCallbacks.get(i18);
                        if (message.arg1 != 0) {
                            z5 = true;
                        } else {
                            z5 = false;
                        }
                        if (message.arg2 != 0) {
                            z6 = true;
                        } else {
                            z6 = false;
                        }
                        callbacks5.hideRecentApps(z5, z6);
                    }
                    return;
                case 1179648:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showScreenPinningRequest(message.arg1);
                        i++;
                    }
                    return;
                case 1245184:
                    for (int i19 = 0; i19 < CommandQueue.this.mCallbacks.size(); i19++) {
                        Callbacks callbacks6 = CommandQueue.this.mCallbacks.get(i19);
                        int i20 = message.arg1;
                        if (message.arg2 != 0) {
                            z7 = true;
                        } else {
                            z7 = false;
                        }
                        callbacks6.appTransitionPending(i20, z7);
                    }
                    return;
                case 1310720:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).appTransitionCancelled(message.arg1);
                        i++;
                    }
                    return;
                case 1376256:
                    SomeArgs someArgs4 = (SomeArgs) message.obj;
                    for (int i21 = 0; i21 < CommandQueue.this.mCallbacks.size(); i21++) {
                        Callbacks callbacks7 = CommandQueue.this.mCallbacks.get(i21);
                        int i22 = someArgs4.argi1;
                        long longValue = ((Long) someArgs4.arg1).longValue();
                        long longValue2 = ((Long) someArgs4.arg2).longValue();
                        if (someArgs4.argi2 != 0) {
                            z8 = true;
                        } else {
                            z8 = false;
                        }
                        callbacks7.appTransitionStarting(i22, longValue, longValue2, z8);
                    }
                    return;
                case 1441792:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showAssistDisclosure();
                        i++;
                    }
                    return;
                case 1507328:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).startAssist((Bundle) message.obj);
                        i++;
                    }
                    return;
                case 1572864:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onCameraLaunchGestureDetected(message.arg1);
                        i++;
                    }
                    return;
                case 1638400:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).toggleKeyboardShortcutsMenu(message.arg1);
                        i++;
                    }
                    return;
                case 1703936:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showPictureInPictureMenu();
                        i++;
                    }
                    return;
                case 1769472:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).addQsTile((ComponentName) message.obj);
                        i++;
                    }
                    return;
                case 1835008:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).remQsTile((ComponentName) message.obj);
                        i++;
                    }
                    return;
                case 1900544:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).clickTile((ComponentName) message.obj);
                        i++;
                    }
                    return;
                case 1966080:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).toggleSplitScreen();
                        i++;
                    }
                    return;
                case 2031616:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).appTransitionFinished(message.arg1);
                        i++;
                    }
                    return;
                case 2097152:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).dismissKeyboardShortcutsMenu();
                        i++;
                    }
                    return;
                case 2162688:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).handleSystemKey(message.arg1);
                        i++;
                    }
                    return;
                case 2228224:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).handleShowGlobalActionsMenu();
                        i++;
                    }
                    return;
                case 2293760:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).togglePanel();
                        i++;
                    }
                    return;
                case 2359296:
                    for (int i23 = 0; i23 < CommandQueue.this.mCallbacks.size(); i23++) {
                        Callbacks callbacks8 = CommandQueue.this.mCallbacks.get(i23);
                        if (message.arg1 != 0) {
                            z9 = true;
                        } else {
                            z9 = false;
                        }
                        callbacks8.handleShowShutdownUi(z9, (String) message.obj);
                    }
                    return;
                case 2424832:
                    for (int i24 = 0; i24 < CommandQueue.this.mCallbacks.size(); i24++) {
                        Callbacks callbacks9 = CommandQueue.this.mCallbacks.get(i24);
                        if (message.arg1 != 0) {
                            z10 = true;
                        } else {
                            z10 = false;
                        }
                        callbacks9.setTopAppHidesStatusBar(z10);
                    }
                    return;
                case 2490368:
                    for (int i25 = 0; i25 < CommandQueue.this.mCallbacks.size(); i25++) {
                        Callbacks callbacks10 = CommandQueue.this.mCallbacks.get(i25);
                        int i26 = message.arg1;
                        if (message.arg2 != 0) {
                            z11 = true;
                        } else {
                            z11 = false;
                        }
                        callbacks10.onRotationProposal(i26, z11);
                    }
                    return;
                case 2555904:
                    CommandQueue.this.mHandler.removeMessages(2752512);
                    CommandQueue.this.mHandler.removeMessages(2686976);
                    CommandQueue.this.mHandler.removeMessages(2621440);
                    SomeArgs someArgs5 = (SomeArgs) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showAuthenticationDialog((PromptInfo) someArgs5.arg1, (IBiometricSysuiReceiver) someArgs5.arg2, (int[]) someArgs5.arg3, ((Boolean) someArgs5.arg4).booleanValue(), ((Boolean) someArgs5.arg5).booleanValue(), someArgs5.argi1, someArgs5.argl1, (String) someArgs5.arg6, someArgs5.argl2, someArgs5.argi2);
                        i++;
                    }
                    someArgs5.recycle();
                    return;
                case 2621440:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onBiometricAuthenticated();
                        i++;
                    }
                    return;
                case 2686976:
                    SomeArgs someArgs6 = (SomeArgs) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onBiometricHelp(someArgs6.argi1, (String) someArgs6.arg1);
                        i++;
                    }
                    someArgs6.recycle();
                    return;
                case 2752512:
                    SomeArgs someArgs7 = (SomeArgs) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onBiometricError(someArgs7.argi1, someArgs7.argi2, someArgs7.argi3);
                        i++;
                    }
                    someArgs7.recycle();
                    return;
                case 2818048:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).hideAuthenticationDialog();
                        i++;
                    }
                    return;
                case 2883584:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showWirelessChargingAnimation(message.arg1);
                        i++;
                    }
                    return;
                case 2949120:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showPinningEnterExitToast(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3014656:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showPinningEscapeToast();
                        i++;
                    }
                    return;
                case 3080192:
                    for (int i27 = 0; i27 < CommandQueue.this.mCallbacks.size(); i27++) {
                        Callbacks callbacks11 = CommandQueue.this.mCallbacks.get(i27);
                        if (message.arg1 > 0) {
                            z12 = true;
                        } else {
                            z12 = false;
                        }
                        callbacks11.onRecentsAnimationStateChanged(z12);
                    }
                    return;
                case 3145728:
                    int i28 = message.arg1;
                    int[] iArr = (int[]) message.obj;
                    if (message.arg2 == 0) {
                        z14 = false;
                    }
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showTransient(i28, iArr, z14);
                        i++;
                    }
                    return;
                case 3211264:
                    int i29 = message.arg1;
                    int[] iArr2 = (int[]) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).abortTransient(i29, iArr2);
                        i++;
                    }
                    return;
                case 3276800:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).showInattentiveSleepWarning();
                        i++;
                    }
                    return;
                case 3342336:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).dismissInattentiveSleepWarning(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3407872:
                    SomeArgs someArgs8 = (SomeArgs) message.obj;
                    String str = (String) someArgs8.arg1;
                    IBinder iBinder2 = (IBinder) someArgs8.arg2;
                    CharSequence charSequence = (CharSequence) someArgs8.arg3;
                    IBinder iBinder3 = (IBinder) someArgs8.arg4;
                    ITransientNotificationCallback iTransientNotificationCallback = (ITransientNotificationCallback) someArgs8.arg5;
                    int i30 = someArgs8.argi1;
                    int i31 = someArgs8.argi2;
                    Iterator<Callbacks> it = CommandQueue.this.mCallbacks.iterator();
                    while (it.hasNext()) {
                        it.next().showToast(i30, str, iBinder2, charSequence, iBinder3, i31, iTransientNotificationCallback);
                    }
                    return;
                case 3473408:
                    SomeArgs someArgs9 = (SomeArgs) message.obj;
                    String str2 = (String) someArgs9.arg1;
                    IBinder iBinder4 = (IBinder) someArgs9.arg2;
                    Iterator<Callbacks> it2 = CommandQueue.this.mCallbacks.iterator();
                    while (it2.hasNext()) {
                        it2.next().hideToast(str2, iBinder4);
                    }
                    return;
                case 3538944:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onTracingStateChanged(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3604480:
                    Iterator<Callbacks> it3 = CommandQueue.this.mCallbacks.iterator();
                    while (it3.hasNext()) {
                        it3.next().suppressAmbientDisplay(((Boolean) message.obj).booleanValue());
                    }
                    return;
                case 3670016:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).requestWindowMagnificationConnection(((Boolean) message.obj).booleanValue());
                        i++;
                    }
                    return;
                case 3735552:
                    SomeArgs someArgs10 = (SomeArgs) message.obj;
                    try {
                        ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) someArgs10.arg2;
                        while (i < CommandQueue.this.mCallbacks.size()) {
                            CommandQueue.this.mCallbacks.get(i).handleWindowManagerLoggingCommand((String[]) someArgs10.arg1, parcelFileDescriptor);
                            i++;
                        }
                        if (parcelFileDescriptor != null) {
                            parcelFileDescriptor.close();
                        }
                    } catch (IOException e) {
                        int i32 = CommandQueue.$r8$clinit;
                        Log.e("CommandQueue", "Failed to handle logging command", e);
                    }
                    someArgs10.recycle();
                    return;
                case 3801088:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).onEmergencyActionLaunchGestureDetected();
                        i++;
                    }
                    return;
                case 3866624:
                    for (int i33 = 0; i33 < CommandQueue.this.mCallbacks.size(); i33++) {
                        Callbacks callbacks12 = CommandQueue.this.mCallbacks.get(i33);
                        int i34 = message.arg1;
                        if (message.arg2 != 0) {
                            z13 = true;
                        } else {
                            z13 = false;
                        }
                        callbacks12.setNavigationBarLumaSamplingEnabled(i34, z13);
                    }
                    return;
                case 3932160:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).setUdfpsHbmListener((IUdfpsHbmListener) message.obj);
                        i++;
                    }
                    return;
                case 3997696:
                    SomeArgs someArgs11 = (SomeArgs) message.obj;
                    ComponentName componentName = (ComponentName) someArgs11.arg1;
                    CharSequence charSequence2 = (CharSequence) someArgs11.arg2;
                    CharSequence charSequence3 = (CharSequence) someArgs11.arg3;
                    Icon icon = (Icon) someArgs11.arg4;
                    IAddTileResultCallback iAddTileResultCallback = (IAddTileResultCallback) someArgs11.arg5;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).requestAddTile(componentName, charSequence2, charSequence3, icon, iAddTileResultCallback);
                        i++;
                    }
                    someArgs11.recycle();
                    return;
                case 4063232:
                    String str3 = (String) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).cancelRequestAddTile(str3);
                        i++;
                    }
                    return;
                case 4128768:
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).setBiometicContextListener((IBiometricContextListener) message.obj);
                        i++;
                    }
                    return;
                case 4194304:
                    SomeArgs someArgs12 = (SomeArgs) message.obj;
                    int intValue = ((Integer) someArgs12.arg1).intValue();
                    MediaRoute2Info mediaRoute2Info = (MediaRoute2Info) someArgs12.arg2;
                    IUndoMediaTransferCallback iUndoMediaTransferCallback = (IUndoMediaTransferCallback) someArgs12.arg3;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).updateMediaTapToTransferSenderDisplay(intValue, mediaRoute2Info, iUndoMediaTransferCallback);
                        i++;
                    }
                    someArgs12.recycle();
                    return;
                case 4259840:
                    SomeArgs someArgs13 = (SomeArgs) message.obj;
                    int intValue2 = ((Integer) someArgs13.arg1).intValue();
                    MediaRoute2Info mediaRoute2Info2 = (MediaRoute2Info) someArgs13.arg2;
                    Icon icon2 = (Icon) someArgs13.arg3;
                    CharSequence charSequence4 = (CharSequence) someArgs13.arg4;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).updateMediaTapToTransferReceiverDisplay(intValue2, mediaRoute2Info2, icon2, charSequence4);
                        i++;
                    }
                    someArgs13.recycle();
                    return;
                case 4325376:
                    INearbyMediaDevicesProvider iNearbyMediaDevicesProvider = (INearbyMediaDevicesProvider) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).registerNearbyMediaDevicesProvider(iNearbyMediaDevicesProvider);
                        i++;
                    }
                    return;
                case 4390912:
                    INearbyMediaDevicesProvider iNearbyMediaDevicesProvider2 = (INearbyMediaDevicesProvider) message.obj;
                    while (i < CommandQueue.this.mCallbacks.size()) {
                        CommandQueue.this.mCallbacks.get(i).unregisterNearbyMediaDevicesProvider(iNearbyMediaDevicesProvider2);
                        i++;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public final void animateCollapsePanels() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(262144);
            this.mHandler.obtainMessage(262144, 0, 0).sendToTarget();
        }
    }

    public final void appTransitionStarting(int i, long j, long j2) {
        appTransitionStarting(i, j, j2, false);
    }

    public final void disable(int i, int i2, int i3, boolean z) {
        synchronized (this.mLock) {
            this.mDisplayDisabled.put(i, new Pair<>(Integer.valueOf(i2), Integer.valueOf(i3)));
            this.mHandler.removeMessages(131072);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            obtain.argi4 = z ? 1 : 0;
            Message obtainMessage = this.mHandler.obtainMessage(131072, obtain);
            if (Looper.myLooper() == this.mHandler.getLooper()) {
                this.mHandler.handleMessage(obtainMessage);
                obtainMessage.recycle();
            } else {
                obtainMessage.sendToTarget();
            }
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayAdded(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayChanged(int i) {
    }

    public final boolean panelsEnabled() {
        return (((Integer) getDisabled(0).first).intValue() & 65536) == 0 && (((Integer) getDisabled(0).second).intValue() & 4) == 0 && !StatusBar.ONLY_CORE_APPS;
    }

    public final void abortTransient(int i, int[] iArr) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3211264, i, 0, iArr).sendToTarget();
        }
    }

    public final void addCallback(Callbacks callbacks) {
        this.mCallbacks.add(callbacks);
        for (int i = 0; i < this.mDisplayDisabled.size(); i++) {
            int keyAt = this.mDisplayDisabled.keyAt(i);
            callbacks.disable(keyAt, ((Integer) getDisabled(keyAt).first).intValue(), ((Integer) getDisabled(keyAt).second).intValue(), false);
        }
    }

    public final void addQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1769472, componentName).sendToTarget();
        }
    }

    public final void animateExpandNotificationsPanel() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(196608);
            this.mHandler.sendEmptyMessage(196608);
        }
    }

    public final void animateExpandSettingsPanel(String str) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(327680);
            this.mHandler.obtainMessage(327680, str).sendToTarget();
        }
    }

    public final void appTransitionCancelled(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1310720, i, 0).sendToTarget();
        }
    }

    public final void appTransitionFinished(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2031616, i, 0).sendToTarget();
        }
    }

    public final void appTransitionPending(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1245184, i, 0).sendToTarget();
        }
    }

    public final void appTransitionStarting(int i, long j, long j2, boolean z) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = z ? 1 : 0;
            obtain.arg1 = Long.valueOf(j);
            obtain.arg2 = Long.valueOf(j2);
            this.mHandler.obtainMessage(1376256, obtain).sendToTarget();
        }
    }

    public final void cancelPreloadRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(720896);
            this.mHandler.obtainMessage(720896, 0, 0, null).sendToTarget();
        }
    }

    public final void cancelRequestAddTile(String str) throws RemoteException {
        this.mHandler.obtainMessage(4063232, str).sendToTarget();
    }

    public final void clickQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1900544, componentName).sendToTarget();
        }
    }

    public final void dismissInattentiveSleepWarning(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3342336, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public final void dismissKeyboardShortcutsMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2097152);
            this.mHandler.obtainMessage(2097152).sendToTarget();
        }
    }

    public final Pair<Integer, Integer> getDisabled(int i) {
        Pair<Integer, Integer> pair = this.mDisplayDisabled.get(i);
        if (pair != null) {
            return pair;
        }
        Pair<Integer, Integer> pair2 = new Pair<>(0, 0);
        this.mDisplayDisabled.put(i, pair2);
        return pair2;
    }

    public final void handleSystemKey(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2162688, i, 0).sendToTarget();
        }
    }

    public final void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = strArr;
            obtain.arg2 = parcelFileDescriptor;
            this.mHandler.obtainMessage(3735552, obtain).sendToTarget();
        }
    }

    public final void hideAuthenticationDialog() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2818048).sendToTarget();
        }
    }

    public final void hideRecentApps(boolean z, boolean z2) {
        int i;
        synchronized (this.mLock) {
            this.mHandler.removeMessages(917504);
            H h = this.mHandler;
            if (z2) {
                i = 1;
            } else {
                i = 0;
            }
            h.obtainMessage(917504, z ? 1 : 0, i, null).sendToTarget();
        }
    }

    public final void hideToast(String str, IBinder iBinder) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = str;
            obtain.arg2 = iBinder;
            this.mHandler.obtainMessage(3473408, obtain).sendToTarget();
        }
    }

    public final void onBiometricAuthenticated() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2621440).sendToTarget();
        }
    }

    public final void onBiometricError(int i, int i2, int i3) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            this.mHandler.obtainMessage(2752512, obtain).sendToTarget();
        }
    }

    public final void onBiometricHelp(int i, String str) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.arg1 = str;
            this.mHandler.obtainMessage(2686976, obtain).sendToTarget();
        }
    }

    public final void onCameraLaunchGestureDetected(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1572864);
            this.mHandler.obtainMessage(1572864, i, 0).sendToTarget();
        }
    }

    public final void onDisplayReady(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(458752, i, 0).sendToTarget();
        }
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public final void onDisplayRemoved(int i) {
        synchronized (this.mLock) {
            this.mDisplayDisabled.remove(i);
        }
        for (int size = this.mCallbacks.size() - 1; size >= 0; size--) {
            this.mCallbacks.get(size).onDisplayRemoved(i);
        }
    }

    public final void onEmergencyActionLaunchGestureDetected() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(3801088);
            this.mHandler.obtainMessage(3801088).sendToTarget();
        }
    }

    public final void onProposedRotationChanged(int i, boolean z) {
        int i2;
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2490368);
            H h = this.mHandler;
            if (z) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            h.obtainMessage(2490368, i, i2, null).sendToTarget();
        }
    }

    public final void onRecentsAnimationStateChanged(boolean z) {
        int i;
        synchronized (this.mLock) {
            H h = this.mHandler;
            if (z) {
                i = 1;
            } else {
                i = 0;
            }
            h.obtainMessage(3080192, i, 0).sendToTarget();
        }
    }

    public final void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        int i4;
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            if (z) {
                i4 = 1;
            } else {
                i4 = 0;
            }
            obtain.argi3 = i4;
            obtain.arg1 = appearanceRegionArr;
            obtain.argi4 = i3;
            obtain.arg2 = insetsVisibilities;
            obtain.arg3 = str;
            this.mHandler.obtainMessage(393216, obtain).sendToTarget();
        }
    }

    public final void passThroughShellCommand(final String[] strArr, final ParcelFileDescriptor parcelFileDescriptor) {
        final PrintWriter printWriter = new PrintWriter(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
        new Thread() { // from class: com.android.systemui.statusbar.CommandQueue.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super("Sysui.passThroughShellCommand");
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                try {
                    CommandRegistry commandRegistry = CommandQueue.this.mRegistry;
                    if (commandRegistry == null) {
                    } else {
                        commandRegistry.onShellCommand(printWriter, strArr);
                        printWriter.flush();
                        try {
                            parcelFileDescriptor.close();
                        } catch (Exception unused) {
                        }
                    }
                } finally {
                    printWriter.flush();
                    try {
                        parcelFileDescriptor.close();
                    } catch (Exception unused2) {
                    }
                }
            }
        }.start();
    }

    public final void preloadRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(655360);
            this.mHandler.obtainMessage(655360, 0, 0, null).sendToTarget();
        }
    }

    public final void recomputeDisableFlags(int i, boolean z) {
        synchronized (this.mLock) {
            disable(i, ((Integer) getDisabled(i).first).intValue(), ((Integer) getDisabled(i).second).intValue(), z);
        }
    }

    public final void registerNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        this.mHandler.obtainMessage(4325376, iNearbyMediaDevicesProvider).sendToTarget();
    }

    public final void remQsTile(ComponentName componentName) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1835008, componentName).sendToTarget();
        }
    }

    public final void removeCallback(Callbacks callbacks) {
        this.mCallbacks.remove(callbacks);
    }

    public final void removeIcon(String str) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(65536, 2, 0, str).sendToTarget();
        }
    }

    public final void requestWindowMagnificationConnection(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3670016, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public final void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(4128768, iBiometricContextListener).sendToTarget();
        }
    }

    public final void setIcon(String str, StatusBarIcon statusBarIcon) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(65536, 1, 0, new Pair(str, statusBarIcon)).sendToTarget();
        }
    }

    public final void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        int i4;
        synchronized (this.mLock) {
            this.mHandler.removeMessages(524288);
            SomeArgs obtain = SomeArgs.obtain();
            obtain.argi1 = i;
            obtain.argi2 = i2;
            obtain.argi3 = i3;
            if (z) {
                i4 = 1;
            } else {
                i4 = 0;
            }
            obtain.argi4 = i4;
            obtain.arg1 = iBinder;
            this.mHandler.obtainMessage(524288, obtain).sendToTarget();
        }
    }

    public final void setNavigationBarLumaSamplingEnabled(int i, boolean z) {
        int i2;
        synchronized (this.mLock) {
            H h = this.mHandler;
            if (z) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            h.obtainMessage(3866624, i, i2).sendToTarget();
        }
    }

    public final void setTopAppHidesStatusBar(boolean z) {
        this.mHandler.removeMessages(2424832);
        this.mHandler.obtainMessage(2424832, z ? 1 : 0, 0).sendToTarget();
    }

    public final void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3932160, iUdfpsHbmListener).sendToTarget();
        }
    }

    public final void setWindowState(int i, int i2, int i3) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(786432, i, i2, Integer.valueOf(i3)).sendToTarget();
        }
    }

    public final void showAssistDisclosure() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1441792);
            this.mHandler.obtainMessage(1441792).sendToTarget();
        }
    }

    public final void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = promptInfo;
            obtain.arg2 = iBiometricSysuiReceiver;
            obtain.arg3 = iArr;
            obtain.arg4 = Boolean.valueOf(z);
            obtain.arg5 = Boolean.valueOf(z2);
            obtain.argi1 = i;
            obtain.arg6 = str;
            obtain.argl1 = j;
            obtain.argl2 = j2;
            obtain.argi2 = i2;
            this.mHandler.obtainMessage(2555904, obtain).sendToTarget();
        }
    }

    public final void showGlobalActionsMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2228224);
            this.mHandler.obtainMessage(2228224).sendToTarget();
        }
    }

    public final void showInattentiveSleepWarning() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3276800).sendToTarget();
        }
    }

    public final void showPictureInPictureMenu() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1703936);
            this.mHandler.obtainMessage(1703936).sendToTarget();
        }
    }

    public final void showPinningEnterExitToast(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(2949120, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public final void showPinningEscapeToast() {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3014656).sendToTarget();
        }
    }

    public final void showRecentApps(boolean z) {
        int i;
        synchronized (this.mLock) {
            this.mHandler.removeMessages(851968);
            H h = this.mHandler;
            if (z) {
                i = 1;
            } else {
                i = 0;
            }
            h.obtainMessage(851968, i, 0, null).sendToTarget();
        }
    }

    public final void showScreenPinningRequest(int i) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1179648, i, 0, null).sendToTarget();
        }
    }

    public final void showShutdownUi(boolean z, String str) {
        int i;
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2359296);
            H h = this.mHandler;
            if (z) {
                i = 1;
            } else {
                i = 0;
            }
            h.obtainMessage(2359296, i, 0, str).sendToTarget();
        }
    }

    public final void showToast(int i, String str, IBinder iBinder, CharSequence charSequence, IBinder iBinder2, int i2, ITransientNotificationCallback iTransientNotificationCallback) {
        synchronized (this.mLock) {
            SomeArgs obtain = SomeArgs.obtain();
            obtain.arg1 = str;
            obtain.arg2 = iBinder;
            obtain.arg3 = charSequence;
            obtain.arg4 = iBinder2;
            obtain.arg5 = iTransientNotificationCallback;
            obtain.argi1 = i;
            obtain.argi2 = i2;
            this.mHandler.obtainMessage(3407872, obtain).sendToTarget();
        }
    }

    public final void showTransient(int i, int[] iArr, boolean z) {
        int i2;
        synchronized (this.mLock) {
            H h = this.mHandler;
            if (z) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            h.obtainMessage(3145728, i, i2, iArr).sendToTarget();
        }
    }

    public final void showWirelessChargingAnimation(int i) {
        this.mHandler.removeMessages(2883584);
        this.mHandler.obtainMessage(2883584, i, 0).sendToTarget();
    }

    public final void startAssist(Bundle bundle) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1507328);
            this.mHandler.obtainMessage(1507328, bundle).sendToTarget();
        }
    }

    public final void startTracing() {
        synchronized (this.mLock) {
            ProtoTracer protoTracer = this.mProtoTracer;
            if (protoTracer != null) {
                FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer = protoTracer.mProtoTracer;
                Objects.requireNonNull(frameProtoTracer);
                synchronized (frameProtoTracer.mLock) {
                    if (!frameProtoTracer.mEnabled) {
                        frameProtoTracer.mBuffer.resetBuffer();
                        frameProtoTracer.mEnabled = true;
                        frameProtoTracer.logState();
                    }
                }
            }
            this.mHandler.obtainMessage(3538944, Boolean.TRUE).sendToTarget();
        }
    }

    public final void stopTracing() {
        synchronized (this.mLock) {
            ProtoTracer protoTracer = this.mProtoTracer;
            if (protoTracer != null) {
                protoTracer.stop();
            }
            this.mHandler.obtainMessage(3538944, Boolean.FALSE).sendToTarget();
        }
    }

    public final void suppressAmbientDisplay(boolean z) {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(3604480, Boolean.valueOf(z)).sendToTarget();
        }
    }

    public final void toggleKeyboardShortcutsMenu(int i) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1638400);
            this.mHandler.obtainMessage(1638400, i, 0).sendToTarget();
        }
    }

    public final void togglePanel() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(2293760);
            this.mHandler.obtainMessage(2293760, 0, 0).sendToTarget();
        }
    }

    public final void toggleRecentApps() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(589824);
            Message obtainMessage = this.mHandler.obtainMessage(589824, 0, 0, null);
            obtainMessage.setAsynchronous(true);
            obtainMessage.sendToTarget();
        }
    }

    public final void toggleSplitScreen() {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(1966080);
            this.mHandler.obtainMessage(1966080, 0, 0, null).sendToTarget();
        }
    }

    public final void unregisterNearbyMediaDevicesProvider(INearbyMediaDevicesProvider iNearbyMediaDevicesProvider) {
        this.mHandler.obtainMessage(4390912, iNearbyMediaDevicesProvider).sendToTarget();
    }

    public CommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        this.mProtoTracer = protoTracer;
        this.mRegistry = commandRegistry;
        ((DisplayManager) context.getSystemService(DisplayManager.class)).registerDisplayListener(this, this.mHandler);
        this.mDisplayDisabled.put(0, new Pair<>(0, 0));
    }

    public final void requestAddTile(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, IAddTileResultCallback iAddTileResultCallback) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = componentName;
        obtain.arg2 = charSequence;
        obtain.arg3 = charSequence2;
        obtain.arg4 = icon;
        obtain.arg5 = iAddTileResultCallback;
        this.mHandler.obtainMessage(3997696, obtain).sendToTarget();
    }

    public final void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = Integer.valueOf(i);
        obtain.arg2 = mediaRoute2Info;
        obtain.arg3 = icon;
        obtain.arg4 = charSequence;
        this.mHandler.obtainMessage(4259840, obtain).sendToTarget();
    }

    public final void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) throws RemoteException {
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = Integer.valueOf(i);
        obtain.arg2 = mediaRoute2Info;
        obtain.arg3 = iUndoMediaTransferCallback;
        this.mHandler.obtainMessage(4194304, obtain).sendToTarget();
    }

    public final void animateCollapsePanels(int i, boolean z) {
        synchronized (this.mLock) {
            this.mHandler.removeMessages(262144);
            this.mHandler.obtainMessage(262144, i, z ? 1 : 0).sendToTarget();
        }
    }

    public final void disable(int i, int i2, int i3) {
        disable(i, i2, i3, true);
    }

    public final void runGcForTest() {
        GcUtils.runGcAndFinalizersSync();
    }
}
