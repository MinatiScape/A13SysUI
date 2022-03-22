package com.android.systemui.statusbar;

import android.content.Context;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.systemui.statusbar.OperatorNameViewController;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class OperatorNameView extends TextView {
    public boolean mDemoMode;

    public OperatorNameView(Context context) {
        this(context, null);
    }

    public final void update(boolean z, boolean z2, ArrayList arrayList) {
        int i;
        boolean z3 = false;
        if (z) {
            i = 0;
        } else {
            i = 8;
        }
        setVisibility(i);
        if (Settings.Global.getInt(((TextView) this).mContext.getContentResolver(), "airplane_mode_on", 0) != 0) {
            z3 = true;
        }
        if (!z2 || z3) {
            setText((CharSequence) null);
            setVisibility(8);
        } else if (!this.mDemoMode) {
            updateText(arrayList);
        }
    }

    public OperatorNameView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OperatorNameView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public final void updateText(ArrayList arrayList) {
        CharSequence charSequence;
        boolean z;
        int size = arrayList.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                charSequence = null;
                break;
            }
            OperatorNameViewController.SubInfo subInfo = (OperatorNameViewController.SubInfo) arrayList.get(i);
            OperatorNameViewController.SubInfo subInfo2 = (OperatorNameViewController.SubInfo) arrayList.get(i);
            Objects.requireNonNull(subInfo2);
            if (!TextUtils.isEmpty(subInfo2.mCarrierName)) {
                Objects.requireNonNull(subInfo);
                boolean z2 = true;
                if (subInfo.mSimState == 5) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    continue;
                } else {
                    ServiceState serviceState = subInfo.mServiceState;
                    if (serviceState == null || serviceState.getState() != 0) {
                        z2 = false;
                    }
                    if (z2) {
                        charSequence = subInfo.mCarrierName;
                        break;
                    }
                }
            }
            i++;
        }
        setText(charSequence);
    }
}
