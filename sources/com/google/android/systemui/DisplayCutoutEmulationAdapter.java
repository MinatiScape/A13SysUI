package com.google.android.systemui;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.List;
/* loaded from: classes.dex */
public final class DisplayCutoutEmulationAdapter {
    public final Context mContext;
    public final AnonymousClass1 mObserver;
    public final IOverlayManager mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));

    public final void update() {
        String stringForUser = Settings.Global.getStringForUser(this.mContext.getContentResolver(), "com.google.android.systemui.display_cutout_emulation", 0);
        if (stringForUser != null) {
            String[] split = stringForUser.split(":", 2);
            try {
                int parseInt = Integer.parseInt(split[0]);
                String str = split[1];
                if (parseInt > PreferenceManager.getDefaultSharedPreferences(this.mContext).getInt("com.google.android.systemui.display_cutout_emulation.VERSION", -1)) {
                    if (str.isEmpty() || str.startsWith("com.android.internal.display.cutout.emulation")) {
                        try {
                            List overlayInfosForTarget = this.mOverlayManager.getOverlayInfosForTarget(ThemeOverlayApplier.ANDROID_PACKAGE, 0);
                            int size = overlayInfosForTarget.size();
                            while (true) {
                                size--;
                                if (size < 0) {
                                    break;
                                } else if (!((OverlayInfo) overlayInfosForTarget.get(size)).packageName.startsWith("com.android.internal.display.cutout.emulation")) {
                                    overlayInfosForTarget.remove(size);
                                }
                            }
                            OverlayInfo[] overlayInfoArr = (OverlayInfo[]) overlayInfosForTarget.toArray(new OverlayInfo[overlayInfosForTarget.size()]);
                            String str2 = null;
                            for (OverlayInfo overlayInfo : overlayInfoArr) {
                                if (overlayInfo.isEnabled()) {
                                    str2 = overlayInfo.packageName;
                                }
                            }
                            if ((!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) && !TextUtils.equals(str, str2)) {
                                for (OverlayInfo overlayInfo2 : overlayInfoArr) {
                                    boolean isEnabled = overlayInfo2.isEnabled();
                                    boolean equals = TextUtils.equals(overlayInfo2.packageName, str);
                                    if (isEnabled != equals) {
                                        try {
                                            this.mOverlayManager.setEnabled(overlayInfo2.packageName, equals, 0);
                                        } catch (RemoteException e) {
                                            throw e.rethrowFromSystemServer();
                                        }
                                    }
                                }
                            }
                            PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putInt("com.google.android.systemui.display_cutout_emulation.VERSION", parseInt).apply();
                        } catch (RemoteException e2) {
                            throw e2.rethrowFromSystemServer();
                        }
                    } else {
                        Log.e("CutoutEmulationAdapter", "Invalid overlay prefix: " + stringForUser);
                    }
                }
            } catch (IndexOutOfBoundsException | NumberFormatException e3) {
                Log.e("CutoutEmulationAdapter", "Invalid configuration: " + stringForUser, e3);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.DisplayCutoutEmulationAdapter$1, android.database.ContentObserver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DisplayCutoutEmulationAdapter(android.content.Context r4) {
        /*
            r3 = this;
            r3.<init>()
            com.google.android.systemui.DisplayCutoutEmulationAdapter$1 r0 = new com.google.android.systemui.DisplayCutoutEmulationAdapter$1
            android.os.Handler r1 = android.os.Handler.getMain()
            r0.<init>(r1)
            r3.mObserver = r0
            r3.mContext = r4
            java.lang.String r1 = "overlay"
            android.os.IBinder r1 = android.os.ServiceManager.getService(r1)
            android.content.om.IOverlayManager r1 = android.content.om.IOverlayManager.Stub.asInterface(r1)
            r3.mOverlayManager = r1
            android.content.ContentResolver r4 = r4.getContentResolver()
            java.lang.String r1 = "com.google.android.systemui.display_cutout_emulation"
            android.net.Uri r1 = android.provider.Settings.Global.getUriFor(r1)
            r2 = 0
            r4.registerContentObserver(r1, r2, r0, r2)
            r3.update()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.DisplayCutoutEmulationAdapter.<init>(android.content.Context):void");
    }
}
