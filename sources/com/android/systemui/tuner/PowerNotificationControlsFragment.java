package com.android.systemui.tuner;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.android.internal.logging.MetricsLogger;
/* loaded from: classes.dex */
public class PowerNotificationControlsFragment extends Fragment {
    public static final /* synthetic */ int $r8$clinit = 0;

    public final boolean isEnabled() {
        if (Settings.Secure.getInt(getContext().getContentResolver(), "show_importance_slider", 0) == 1) {
            return true;
        }
        return false;
    }

    @Override // android.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(2131624382, viewGroup, false);
    }

    @Override // android.app.Fragment
    public final void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 392, false);
    }

    @Override // android.app.Fragment
    public final void onResume() {
        super.onResume();
        MetricsLogger.visibility(getContext(), 392, true);
    }

    @Override // android.app.Fragment
    public final void onViewCreated(View view, Bundle bundle) {
        String str;
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(2131428998);
        final Switch r3 = (Switch) findViewById.findViewById(16908352);
        final TextView textView = (TextView) findViewById.findViewById(2131429000);
        r3.setChecked(isEnabled());
        if (isEnabled()) {
            str = getString(2131953346);
        } else {
            str = getString(2131953345);
        }
        textView.setText(str);
        r3.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.tuner.PowerNotificationControlsFragment.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                String str2;
                PowerNotificationControlsFragment powerNotificationControlsFragment = PowerNotificationControlsFragment.this;
                int i = PowerNotificationControlsFragment.$r8$clinit;
                boolean z = !powerNotificationControlsFragment.isEnabled();
                MetricsLogger.action(PowerNotificationControlsFragment.this.getContext(), 393, z);
                Settings.Secure.putInt(PowerNotificationControlsFragment.this.getContext().getContentResolver(), "show_importance_slider", z ? 1 : 0);
                r3.setChecked(z);
                TextView textView2 = textView;
                if (z) {
                    str2 = PowerNotificationControlsFragment.this.getString(2131953346);
                } else {
                    str2 = PowerNotificationControlsFragment.this.getString(2131953345);
                }
                textView2.setText(str2);
            }
        });
    }

    @Override // android.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}
