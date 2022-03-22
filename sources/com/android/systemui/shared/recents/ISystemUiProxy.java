package com.android.systemui.shared.recents;

import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda1;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda12;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda2;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda3;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda4;
import com.android.systemui.shared.recents.model.Task$TaskKey;
import com.android.systemui.statusbar.VibratorHelper$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda1;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda4;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public interface ISystemUiProxy extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISystemUiProxy {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.systemui.shared.recents.ISystemUiProxy");
            }
            if (i != 1598968902) {
                if (i == 2) {
                    int readInt = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    OverviewProxyService.AnonymousClass1 r11 = (OverviewProxyService.AnonymousClass1) this;
                    r11.verifyCallerAndClearCallingIdentityPostMain("startScreenPinning", new OverviewProxyService$1$$ExternalSyntheticLambda2(r11, readInt, 0));
                    parcel2.writeNoException();
                } else if (i == 10) {
                    parcel.enforceNoDataAvail();
                    OverviewProxyService.AnonymousClass1 r112 = (OverviewProxyService.AnonymousClass1) this;
                    final VibratorHelper$$ExternalSyntheticLambda0 vibratorHelper$$ExternalSyntheticLambda0 = new VibratorHelper$$ExternalSyntheticLambda0(r112, (MotionEvent) parcel.readTypedObject(MotionEvent.CREATOR), 1);
                    r112.verifyCallerAndClearCallingIdentity("onStatusBarMotionEvent", new Supplier() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda20
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            vibratorHelper$$ExternalSyntheticLambda0.run();
                            return null;
                        }
                    });
                    parcel2.writeNoException();
                } else if (i == 26) {
                    final int readInt2 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    final OverviewProxyService.AnonymousClass1 r113 = (OverviewProxyService.AnonymousClass1) this;
                    r113.verifyCallerAndClearCallingIdentityPostMain("notifyPrioritizedRotation", new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            OverviewProxyService.AnonymousClass1 r0 = OverviewProxyService.AnonymousClass1.this;
                            int i3 = readInt2;
                            Objects.requireNonNull(r0);
                            OverviewProxyService overviewProxyService = OverviewProxyService.this;
                            Objects.requireNonNull(overviewProxyService);
                            int size = overviewProxyService.mConnectionCallbacks.size();
                            while (true) {
                                size--;
                                if (size >= 0) {
                                    ((OverviewProxyService.OverviewProxyListener) overviewProxyService.mConnectionCallbacks.get(size)).onPrioritizedRotation(i3);
                                } else {
                                    return;
                                }
                            }
                        }
                    });
                    parcel2.writeNoException();
                } else if (i == 7) {
                    final boolean readBoolean = parcel.readBoolean();
                    parcel.enforceNoDataAvail();
                    final OverviewProxyService.AnonymousClass1 r114 = (OverviewProxyService.AnonymousClass1) this;
                    r114.verifyCallerAndClearCallingIdentityPostMain("onOverviewShown", new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda8
                        @Override // java.lang.Runnable
                        public final void run() {
                            OverviewProxyService.AnonymousClass1 r2 = OverviewProxyService.AnonymousClass1.this;
                            Objects.requireNonNull(r2);
                            int size = OverviewProxyService.this.mConnectionCallbacks.size();
                            while (true) {
                                size--;
                                if (size >= 0) {
                                    ((OverviewProxyService.OverviewProxyListener) OverviewProxyService.this.mConnectionCallbacks.get(size)).onOverviewShown();
                                } else {
                                    return;
                                }
                            }
                        }
                    });
                    parcel2.writeNoException();
                } else if (i == 8) {
                    final OverviewProxyService.AnonymousClass1 r115 = (OverviewProxyService.AnonymousClass1) this;
                    parcel2.writeNoException();
                    parcel2.writeTypedObject((Rect) r115.verifyCallerAndClearCallingIdentity("getNonMinimizedSplitScreenSecondaryBounds", new Supplier() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda18
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            OverviewProxyService.AnonymousClass1 r1 = OverviewProxyService.AnonymousClass1.this;
                            Objects.requireNonNull(r1);
                            return (Rect) OverviewProxyService.this.mLegacySplitScreenOptional.map(OverviewProxyService$1$$ExternalSyntheticLambda17.INSTANCE).orElse(null);
                        }
                    }), 1);
                } else if (i == 13) {
                    final float readFloat = parcel.readFloat();
                    parcel.enforceNoDataAvail();
                    final OverviewProxyService.AnonymousClass1 r116 = (OverviewProxyService.AnonymousClass1) this;
                    r116.verifyCallerAndClearCallingIdentityPostMain("onAssistantProgress", new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            OverviewProxyService.AnonymousClass1 r0 = OverviewProxyService.AnonymousClass1.this;
                            float f = readFloat;
                            Objects.requireNonNull(r0);
                            OverviewProxyService overviewProxyService = OverviewProxyService.this;
                            Objects.requireNonNull(overviewProxyService);
                            int size = overviewProxyService.mConnectionCallbacks.size();
                            while (true) {
                                size--;
                                if (size >= 0) {
                                    ((OverviewProxyService.OverviewProxyListener) overviewProxyService.mConnectionCallbacks.get(size)).onAssistantProgress(f);
                                } else {
                                    return;
                                }
                            }
                        }
                    });
                    parcel2.writeNoException();
                } else if (i == 14) {
                    parcel.enforceNoDataAvail();
                    OverviewProxyService.AnonymousClass1 r117 = (OverviewProxyService.AnonymousClass1) this;
                    r117.verifyCallerAndClearCallingIdentityPostMain("startAssistant", new PipTaskOrganizer$$ExternalSyntheticLambda4(r117, (Bundle) parcel.readTypedObject(Bundle.CREATOR), 1));
                    parcel2.writeNoException();
                } else if (i == 29) {
                    Bundle bundle = (Bundle) parcel.readTypedObject(Bundle.CREATOR);
                    Rect rect = (Rect) parcel.readTypedObject(Rect.CREATOR);
                    Insets insets = (Insets) parcel.readTypedObject(Insets.CREATOR);
                    Task$TaskKey task$TaskKey = (Task$TaskKey) parcel.readTypedObject(Task$TaskKey.CREATOR);
                    parcel.enforceNoDataAvail();
                    OverviewProxyService overviewProxyService = OverviewProxyService.this;
                    overviewProxyService.mScreenshotHelper.provideScreenshot(bundle, rect, insets, task$TaskKey.id, task$TaskKey.userId, task$TaskKey.sourceComponent, 3, overviewProxyService.mHandler, (Consumer) null);
                    parcel2.writeNoException();
                } else if (i != 30) {
                    switch (i) {
                        case 16:
                            int readInt3 = parcel.readInt();
                            parcel.enforceNoDataAvail();
                            OverviewProxyService.AnonymousClass1 r118 = (OverviewProxyService.AnonymousClass1) this;
                            final OverviewProxyService$1$$ExternalSyntheticLambda3 overviewProxyService$1$$ExternalSyntheticLambda3 = new OverviewProxyService$1$$ExternalSyntheticLambda3(r118, readInt3, 0);
                            r118.verifyCallerAndClearCallingIdentity("notifyAccessibilityButtonClicked", new Supplier() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda20
                                @Override // java.util.function.Supplier
                                public final Object get() {
                                    overviewProxyService$1$$ExternalSyntheticLambda3.run();
                                    return null;
                                }
                            });
                            parcel2.writeNoException();
                            break;
                        case 17:
                            OverviewProxyService.AnonymousClass1 r119 = (OverviewProxyService.AnonymousClass1) this;
                            final BubbleStackView$$ExternalSyntheticLambda17 bubbleStackView$$ExternalSyntheticLambda17 = new BubbleStackView$$ExternalSyntheticLambda17(r119, 4);
                            r119.verifyCallerAndClearCallingIdentity("notifyAccessibilityButtonLongClicked", new Supplier() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda20
                                @Override // java.util.function.Supplier
                                public final Object get() {
                                    bubbleStackView$$ExternalSyntheticLambda17.run();
                                    return null;
                                }
                            });
                            parcel2.writeNoException();
                            break;
                        case 18:
                            ((OverviewProxyService.AnonymousClass1) this).verifyCallerAndClearCallingIdentityPostMain("stopScreenPinning", OverviewProxyService$1$$ExternalSyntheticLambda12.INSTANCE);
                            parcel2.writeNoException();
                            break;
                        case 19:
                            float readFloat2 = parcel.readFloat();
                            parcel.enforceNoDataAvail();
                            OverviewProxyService.AnonymousClass1 r1110 = (OverviewProxyService.AnonymousClass1) this;
                            r1110.verifyCallerAndClearCallingIdentityPostMain("onAssistantGestureCompletion", new OverviewProxyService$1$$ExternalSyntheticLambda1(r1110, readFloat2, 0));
                            parcel2.writeNoException();
                            break;
                        case 20:
                            final float readFloat3 = parcel.readFloat();
                            final boolean readBoolean2 = parcel.readBoolean();
                            parcel.enforceNoDataAvail();
                            final OverviewProxyService.AnonymousClass1 r1111 = (OverviewProxyService.AnonymousClass1) this;
                            r1111.verifyCallerAndClearCallingIdentityPostMain("setNavBarButtonAlpha", new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    OverviewProxyService.AnonymousClass1 r0 = OverviewProxyService.AnonymousClass1.this;
                                    float f = readFloat3;
                                    boolean z = readBoolean2;
                                    Objects.requireNonNull(r0);
                                    OverviewProxyService overviewProxyService2 = OverviewProxyService.this;
                                    Objects.requireNonNull(overviewProxyService2);
                                    int size = overviewProxyService2.mConnectionCallbacks.size();
                                    while (true) {
                                        size--;
                                        if (size >= 0) {
                                            ((OverviewProxyService.OverviewProxyListener) overviewProxyService2.mConnectionCallbacks.get(size)).onNavBarButtonAlphaChanged(f, z);
                                        } else {
                                            return;
                                        }
                                    }
                                }
                            });
                            parcel2.writeNoException();
                            break;
                        default:
                            switch (i) {
                                case 22:
                                    Bitmap bitmap = (Bitmap) parcel.readTypedObject(Bitmap.CREATOR);
                                    Rect rect2 = (Rect) parcel.readTypedObject(Rect.CREATOR);
                                    Insets insets2 = (Insets) parcel.readTypedObject(Insets.CREATOR);
                                    parcel.readInt();
                                    parcel.enforceNoDataAvail();
                                    parcel2.writeNoException();
                                    break;
                                case 23:
                                    final boolean readBoolean3 = parcel.readBoolean();
                                    parcel.enforceNoDataAvail();
                                    OverviewProxyService.this.mLegacySplitScreenOptional.ifPresent(new Consumer() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda14
                                        @Override // java.util.function.Consumer
                                        public final void accept(Object obj) {
                                            ((LegacySplitScreen) obj).setMinimized(readBoolean3);
                                        }
                                    });
                                    parcel2.writeNoException();
                                    break;
                                case 24:
                                    OverviewProxyService.AnonymousClass1 r1112 = (OverviewProxyService.AnonymousClass1) this;
                                    final DozeScreenState$$ExternalSyntheticLambda0 dozeScreenState$$ExternalSyntheticLambda0 = new DozeScreenState$$ExternalSyntheticLambda0(r1112, 5);
                                    r1112.verifyCallerAndClearCallingIdentity("notifySwipeToHomeFinished", new Supplier() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda20
                                        @Override // java.util.function.Supplier
                                        public final Object get() {
                                            dozeScreenState$$ExternalSyntheticLambda0.run();
                                            return null;
                                        }
                                    });
                                    parcel2.writeNoException();
                                    break;
                                default:
                                    switch (i) {
                                        case 45:
                                            OverviewProxyService.AnonymousClass1 r1113 = (OverviewProxyService.AnonymousClass1) this;
                                            r1113.verifyCallerAndClearCallingIdentityPostMain("onBackPressed", new BaseWifiTracker$$ExternalSyntheticLambda0(r1113, 3));
                                            parcel2.writeNoException();
                                            break;
                                        case 46:
                                            boolean readBoolean4 = parcel.readBoolean();
                                            parcel.enforceNoDataAvail();
                                            OverviewProxyService.AnonymousClass1 r1114 = (OverviewProxyService.AnonymousClass1) this;
                                            r1114.verifyCallerAndClearCallingIdentityPostMain("setHomeRotationEnabled", new OverviewProxyService$1$$ExternalSyntheticLambda4(r1114, readBoolean4, 0));
                                            parcel2.writeNoException();
                                            break;
                                        case 47:
                                            OverviewProxyService.AnonymousClass1 r1115 = (OverviewProxyService.AnonymousClass1) this;
                                            r1115.verifyCallerAndClearCallingIdentityPostMain("notifySwipeUpGestureStarted", new OneHandedController$$ExternalSyntheticLambda1(r1115, 3));
                                            break;
                                        case 48:
                                            final boolean readBoolean5 = parcel.readBoolean();
                                            final boolean readBoolean6 = parcel.readBoolean();
                                            parcel.enforceNoDataAvail();
                                            final OverviewProxyService.AnonymousClass1 r1116 = (OverviewProxyService.AnonymousClass1) this;
                                            r1116.verifyCallerAndClearCallingIdentityPostMain("notifyTaskbarStatus", new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda11
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    OverviewProxyService.AnonymousClass1 r0 = OverviewProxyService.AnonymousClass1.this;
                                                    boolean z = readBoolean5;
                                                    boolean z2 = readBoolean6;
                                                    Objects.requireNonNull(r0);
                                                    OverviewProxyService overviewProxyService2 = OverviewProxyService.this;
                                                    Objects.requireNonNull(overviewProxyService2);
                                                    int size = overviewProxyService2.mConnectionCallbacks.size();
                                                    while (true) {
                                                        size--;
                                                        if (size >= 0) {
                                                            ((OverviewProxyService.OverviewProxyListener) overviewProxyService2.mConnectionCallbacks.get(size)).onTaskbarStatusUpdated(z, z2);
                                                        } else {
                                                            return;
                                                        }
                                                    }
                                                }
                                            });
                                            break;
                                        case 49:
                                            final boolean readBoolean7 = parcel.readBoolean();
                                            parcel.enforceNoDataAvail();
                                            final OverviewProxyService.AnonymousClass1 r1117 = (OverviewProxyService.AnonymousClass1) this;
                                            r1117.verifyCallerAndClearCallingIdentityPostMain("notifyTaskbarAutohideSuspend", new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda10
                                                @Override // java.lang.Runnable
                                                public final void run() {
                                                    OverviewProxyService.AnonymousClass1 r0 = OverviewProxyService.AnonymousClass1.this;
                                                    boolean z = readBoolean7;
                                                    Objects.requireNonNull(r0);
                                                    OverviewProxyService overviewProxyService2 = OverviewProxyService.this;
                                                    Objects.requireNonNull(overviewProxyService2);
                                                    int size = overviewProxyService2.mConnectionCallbacks.size();
                                                    while (true) {
                                                        size--;
                                                        if (size >= 0) {
                                                            ((OverviewProxyService.OverviewProxyListener) overviewProxyService2.mConnectionCallbacks.get(size)).onTaskbarAutohideSuspend(z);
                                                        } else {
                                                            return;
                                                        }
                                                    }
                                                }
                                            });
                                            break;
                                        case CustomEvent.MAX_STR_LENGTH /* 50 */:
                                            OverviewProxyService.AnonymousClass1 r1118 = (OverviewProxyService.AnonymousClass1) this;
                                            ((InputMethodManager) OverviewProxyService.this.mContext.getSystemService(InputMethodManager.class)).showInputMethodPickerFromSystem(true, 0);
                                            OverviewProxyService.this.mUiEventLogger.log(KeyButtonView.NavBarButtonEvent.NAVBAR_IME_SWITCHER_BUTTON_TAP);
                                            parcel2.writeNoException();
                                            break;
                                        case 51:
                                            OverviewProxyService.AnonymousClass1 r1119 = (OverviewProxyService.AnonymousClass1) this;
                                            r1119.verifyCallerAndClearCallingIdentityPostMain("toggleNotificationPanel", new Action$$ExternalSyntheticLambda0(r1119, 2));
                                            parcel2.writeNoException();
                                            break;
                                        default:
                                            return super.onTransact(i, parcel, parcel2, i2);
                                    }
                            }
                    }
                } else {
                    OverviewProxyService.AnonymousClass1 r1120 = (OverviewProxyService.AnonymousClass1) this;
                    final QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0 = new QSTileImpl$$ExternalSyntheticLambda0(r1120, 3);
                    r1120.verifyCallerAndClearCallingIdentity("expandNotificationPanel", new Supplier() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda20
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            qSTileImpl$$ExternalSyntheticLambda0.run();
                            return null;
                        }
                    });
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString("com.android.systemui.shared.recents.ISystemUiProxy");
            return true;
        }

        public Stub() {
            attachInterface(this, "com.android.systemui.shared.recents.ISystemUiProxy");
        }
    }
}
