package com.android.systemui.screenrecord;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.users.AvatarPickerActivity$$ExternalSyntheticLambda0;
import com.android.settingslib.users.AvatarPickerActivity$$ExternalSyntheticLambda1;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScreenRecordDialog extends SystemUIDialog {
    public static final List<ScreenRecordingAudioSource> MODES = Arrays.asList(ScreenRecordingAudioSource.INTERNAL, ScreenRecordingAudioSource.MIC, ScreenRecordingAudioSource.MIC_AND_INTERNAL);
    public Switch mAudioSwitch;
    public final RecordingController mController;
    public final Runnable mOnStartRecordingClicked;
    public Spinner mOptions;
    public Switch mTapsSwitch;
    public final UserContextProvider mUserContextProvider;

    public ScreenRecordDialog(Context context, RecordingController recordingController, UserContextProvider userContextProvider, StandardWifiEntry$$ExternalSyntheticLambda0 standardWifiEntry$$ExternalSyntheticLambda0) {
        super(context);
        this.mController = recordingController;
        this.mUserContextProvider = userContextProvider;
        this.mOnStartRecordingClicked = standardWifiEntry$$ExternalSyntheticLambda0;
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.addPrivateFlags(16);
        window.setGravity(17);
        setTitle(2131953209);
        setContentView(2131624460);
        ((TextView) findViewById(2131427644)).setOnClickListener(new AvatarPickerActivity$$ExternalSyntheticLambda0(this, 1));
        ((TextView) findViewById(2131427648)).setOnClickListener(new AvatarPickerActivity$$ExternalSyntheticLambda1(this, 1));
        this.mAudioSwitch = (Switch) findViewById(2131428763);
        this.mTapsSwitch = (Switch) findViewById(2131428764);
        this.mOptions = (Spinner) findViewById(2131428762);
        ScreenRecordingAdapter screenRecordingAdapter = new ScreenRecordingAdapter(getContext().getApplicationContext(), MODES);
        screenRecordingAdapter.setDropDownViewResource(17367049);
        this.mOptions.setAdapter((SpinnerAdapter) screenRecordingAdapter);
        this.mOptions.setOnItemClickListenerInt(new AdapterView.OnItemClickListener() { // from class: com.android.systemui.screenrecord.ScreenRecordDialog$$ExternalSyntheticLambda0
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                ScreenRecordDialog screenRecordDialog = ScreenRecordDialog.this;
                Objects.requireNonNull(screenRecordDialog);
                screenRecordDialog.mAudioSwitch.setChecked(true);
            }
        });
    }
}
