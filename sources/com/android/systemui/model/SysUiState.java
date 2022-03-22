package com.android.systemui.model;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.util.Log;
import androidx.leanback.R$color;
import com.android.systemui.Dumpable;
import com.android.systemui.model.SysUiState;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class SysUiState implements Dumpable {
    public int mFlags;
    public final ArrayList mCallbacks = new ArrayList();
    public int mFlagsToSet = 0;
    public int mFlagsToClear = 0;

    /* loaded from: classes.dex */
    public interface SysUiStateCallback {
        void onSystemUiStateChanged(int i);
    }

    public final void commitUpdate(int i) {
        if (i != 0) {
            Log.w("SysUiState", VendorAtomValue$$ExternalSyntheticOutline0.m("Ignoring flag update for display: ", i), new Throwable());
        } else {
            int i2 = this.mFlags;
            final int i3 = (this.mFlagsToSet | i2) & (~this.mFlagsToClear);
            if (i3 != i2) {
                this.mCallbacks.forEach(new Consumer() { // from class: com.android.systemui.model.SysUiState$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((SysUiState.SysUiStateCallback) obj).onSystemUiStateChanged(i3);
                    }
                });
                this.mFlags = i3;
            }
        }
        this.mFlagsToSet = 0;
        this.mFlagsToClear = 0;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        printWriter.println("SysUiState state:");
        printWriter.print("  mSysUiStateFlags=");
        printWriter.println(this.mFlags);
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        int i = this.mFlags;
        StringJoiner stringJoiner = new StringJoiner("|");
        String str25 = "";
        if ((i & 1) != 0) {
            str = "screen_pinned";
        } else {
            str = str25;
        }
        stringJoiner.add(str);
        if ((i & 128) != 0) {
            str2 = "overview_disabled";
        } else {
            str2 = str25;
        }
        stringJoiner.add(str2);
        if ((i & 256) != 0) {
            str3 = "home_disabled";
        } else {
            str3 = str25;
        }
        stringJoiner.add(str3);
        if ((i & 1024) != 0) {
            str4 = "search_disabled";
        } else {
            str4 = str25;
        }
        stringJoiner.add(str4);
        if ((i & 2) != 0) {
            str5 = "navbar_hidden";
        } else {
            str5 = str25;
        }
        stringJoiner.add(str5);
        if ((i & 4) != 0) {
            str6 = "notif_visible";
        } else {
            str6 = str25;
        }
        stringJoiner.add(str6);
        if ((i & 2048) != 0) {
            str7 = "qs_visible";
        } else {
            str7 = str25;
        }
        stringJoiner.add(str7);
        if ((i & 64) != 0) {
            str8 = "keygrd_visible";
        } else {
            str8 = str25;
        }
        stringJoiner.add(str8);
        if ((i & 512) != 0) {
            str9 = "keygrd_occluded";
        } else {
            str9 = str25;
        }
        stringJoiner.add(str9);
        if ((i & 8) != 0) {
            str10 = "bouncer_visible";
        } else {
            str10 = str25;
        }
        stringJoiner.add(str10);
        if ((32768 & i) != 0) {
            str11 = "global_actions";
        } else {
            str11 = str25;
        }
        stringJoiner.add(str11);
        if ((i & 16) != 0) {
            str12 = "a11y_click";
        } else {
            str12 = str25;
        }
        stringJoiner.add(str12);
        if ((i & 32) != 0) {
            str13 = "a11y_long_click";
        } else {
            str13 = str25;
        }
        stringJoiner.add(str13);
        if ((i & 4096) != 0) {
            str14 = "tracing";
        } else {
            str14 = str25;
        }
        stringJoiner.add(str14);
        if ((i & 8192) != 0) {
            str15 = "asst_gesture_constrain";
        } else {
            str15 = str25;
        }
        stringJoiner.add(str15);
        if ((i & 16384) != 0) {
            str16 = "bubbles_expanded";
        } else {
            str16 = str25;
        }
        stringJoiner.add(str16);
        if ((65536 & i) != 0) {
            str17 = "one_handed_active";
        } else {
            str17 = str25;
        }
        stringJoiner.add(str17);
        if ((i & 131072) != 0) {
            str18 = "allow_gesture";
        } else {
            str18 = str25;
        }
        stringJoiner.add(str18);
        if ((262144 & i) != 0) {
            str19 = "ime_visible";
        } else {
            str19 = str25;
        }
        stringJoiner.add(str19);
        if ((524288 & i) != 0) {
            str20 = "magnification_overlap";
        } else {
            str20 = str25;
        }
        stringJoiner.add(str20);
        if ((1048576 & i) != 0) {
            str21 = "ime_switcher_showing";
        } else {
            str21 = str25;
        }
        stringJoiner.add(str21);
        if ((2097152 & i) != 0) {
            str22 = "device_dozing";
        } else {
            str22 = str25;
        }
        stringJoiner.add(str22);
        if ((4194304 & i) != 0) {
            str23 = "back_disabled";
        } else {
            str23 = str25;
        }
        stringJoiner.add(str23);
        if ((8388608 & i) != 0) {
            str24 = "bubbles_mange_menu_expanded";
        } else {
            str24 = str25;
        }
        stringJoiner.add(str24);
        if ((i & 16777216) != 0) {
            str25 = "immersive_mode";
        }
        stringJoiner.add(str25);
        sb.append(stringJoiner.toString());
        printWriter.println(sb.toString());
        printWriter.print("    backGestureDisabled=");
        printWriter.println(R$color.isBackGestureDisabled(this.mFlags));
        printWriter.print("    assistantGestureDisabled=");
        int i2 = this.mFlags;
        if ((i2 & 131072) != 0) {
            i2 &= -3;
        }
        boolean z = true;
        if ((i2 & 3083) == 0 && ((i2 & 4) == 0 || (i2 & 64) != 0)) {
            z = false;
        }
        printWriter.println(z);
    }

    public final SysUiState setFlag(int i, boolean z) {
        if (z) {
            this.mFlagsToSet = i | this.mFlagsToSet;
        } else {
            this.mFlagsToClear = i | this.mFlagsToClear;
        }
        return this;
    }
}
