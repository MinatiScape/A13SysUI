package com.android.systemui.sensorprivacy.television;

import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda2;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda5;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.tv.TvBottomSheetActivity;
import java.util.Objects;
/* loaded from: classes.dex */
public class TvUnblockSensorActivity extends TvBottomSheetActivity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mSensor = -1;
    public TvUnblockSensorActivity$$ExternalSyntheticLambda0 mSensorPrivacyCallback;
    public final IndividualSensorPrivacyController mSensorPrivacyController;

    @Override // android.app.Activity
    public final void onPause() {
        this.mSensorPrivacyController.removeCallback(this.mSensorPrivacyCallback);
        super.onPause();
    }

    public TvUnblockSensorActivity(IndividualSensorPrivacyController individualSensorPrivacyController) {
        this.mSensorPrivacyController = individualSensorPrivacyController;
    }

    /* JADX WARN: Type inference failed for: r12v5, types: [com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity$$ExternalSyntheticLambda0] */
    @Override // com.android.systemui.tv.TvBottomSheetActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        if (getIntent().getBooleanExtra(SensorPrivacyManager.EXTRA_ALL_SENSORS, false)) {
            this.mSensor = Integer.MAX_VALUE;
        } else {
            this.mSensor = getIntent().getIntExtra(SensorPrivacyManager.EXTRA_SENSOR, -1);
        }
        if (this.mSensor == -1) {
            Log.v("TvUnblockSensorActivity", "Invalid extras");
            finish();
            return;
        }
        this.mSensorPrivacyCallback = new IndividualSensorPrivacyController.Callback() { // from class: com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController.Callback
            public final void onSensorBlockedChanged(int i, boolean z) {
                TvUnblockSensorActivity tvUnblockSensorActivity = TvUnblockSensorActivity.this;
                int i2 = TvUnblockSensorActivity.$r8$clinit;
                Objects.requireNonNull(tvUnblockSensorActivity);
                int i3 = tvUnblockSensorActivity.mSensor;
                if (i3 == Integer.MAX_VALUE) {
                    if (!tvUnblockSensorActivity.mSensorPrivacyController.isSensorBlocked(2) && !tvUnblockSensorActivity.mSensorPrivacyController.isSensorBlocked(1)) {
                        tvUnblockSensorActivity.finish();
                    }
                } else if (i3 == i && !z) {
                    tvUnblockSensorActivity.finish();
                }
            }
        };
        TextView textView = (TextView) findViewById(2131427600);
        TextView textView2 = (TextView) findViewById(2131427595);
        ImageView imageView = (ImageView) findViewById(2131427596);
        ImageView imageView2 = (ImageView) findViewById(2131427599);
        Button button = (Button) findViewById(2131427598);
        Button button2 = (Button) findViewById(2131427597);
        int i = this.mSensor;
        if (i == 1) {
            textView.setText(2131953261);
            textView2.setText(2131953260);
            imageView.setImageResource(17303155);
            imageView2.setVisibility(8);
        } else if (i != 2) {
            textView.setText(2131953259);
            textView2.setText(2131953258);
            imageView.setImageResource(17303150);
            imageView2.setImageResource(17303155);
        } else {
            textView.setText(2131953257);
            textView2.setText(2131953256);
            imageView.setImageResource(17303150);
            imageView2.setVisibility(8);
        }
        button.setText(17041455);
        button.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda5(this, 1));
        button2.setText(17039360);
        button2.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda2(this, 1));
    }

    @Override // android.app.Activity
    public final void onResume() {
        super.onResume();
        this.mSensorPrivacyController.addCallback(this.mSensorPrivacyCallback);
    }
}
