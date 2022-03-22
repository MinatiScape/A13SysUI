package com.android.systemui.hdmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.systemui.tv.TvBottomSheetActivity;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public class HdmiCecSetMenuLanguageActivity extends TvBottomSheetActivity implements View.OnClickListener {
    public final HdmiCecSetMenuLanguageHelper mHdmiCecSetMenuLanguageHelper;

    public HdmiCecSetMenuLanguageActivity(HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper) {
        this.mHdmiCecSetMenuLanguageHelper = hdmiCecSetMenuLanguageHelper;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (view.getId() == 2131427598) {
            HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper = this.mHdmiCecSetMenuLanguageHelper;
            Objects.requireNonNull(hdmiCecSetMenuLanguageHelper);
            hdmiCecSetMenuLanguageHelper.mBackgroundExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda2(hdmiCecSetMenuLanguageHelper, 2));
        } else {
            HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper2 = this.mHdmiCecSetMenuLanguageHelper;
            Objects.requireNonNull(hdmiCecSetMenuLanguageHelper2);
            hdmiCecSetMenuLanguageHelper2.mDenylist.add(hdmiCecSetMenuLanguageHelper2.mLocale.toLanguageTag());
            hdmiCecSetMenuLanguageHelper2.mSecureSettings.putString("hdmi_cec_set_menu_language_denylist", String.join(",", hdmiCecSetMenuLanguageHelper2.mDenylist));
        }
        finish();
    }

    @Override // com.android.systemui.tv.TvBottomSheetActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addPrivateFlags(524288);
        String stringExtra = getIntent().getStringExtra("android.hardware.hdmi.extra.LOCALE");
        HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper = this.mHdmiCecSetMenuLanguageHelper;
        Objects.requireNonNull(hdmiCecSetMenuLanguageHelper);
        hdmiCecSetMenuLanguageHelper.mLocale = Locale.forLanguageTag(stringExtra);
        HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper2 = this.mHdmiCecSetMenuLanguageHelper;
        Objects.requireNonNull(hdmiCecSetMenuLanguageHelper2);
        if (hdmiCecSetMenuLanguageHelper2.mDenylist.contains(hdmiCecSetMenuLanguageHelper2.mLocale.toLanguageTag())) {
            finish();
        }
    }

    @Override // android.app.Activity
    public final void onResume() {
        super.onResume();
        HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper = this.mHdmiCecSetMenuLanguageHelper;
        Objects.requireNonNull(hdmiCecSetMenuLanguageHelper);
        String string = getString(2131952431, hdmiCecSetMenuLanguageHelper.mLocale.getDisplayLanguage());
        String string2 = getString(2131952430);
        Button button = (Button) findViewById(2131427598);
        Button button2 = (Button) findViewById(2131427597);
        ((TextView) findViewById(2131427600)).setText(string);
        ((TextView) findViewById(2131427595)).setText(string2);
        ((ImageView) findViewById(2131427596)).setImageResource(17302842);
        ((ImageView) findViewById(2131427599)).setVisibility(8);
        button.setText(2131952428);
        button.setOnClickListener(this);
        button2.setText(2131952429);
        button2.setOnClickListener(this);
        button2.requestFocus();
    }
}
