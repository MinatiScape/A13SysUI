package com.android.systemui.volume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import com.android.systemui.statusbar.phone.SystemUIDialog;
/* loaded from: classes.dex */
public abstract class SafetyWarningDialog extends SystemUIDialog implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener {
    public static final String TAG = Util.logTag(SafetyWarningDialog.class);
    public final AudioManager mAudioManager;
    public final Context mContext;
    public boolean mDisableOnVolumeUp;
    public boolean mNewVolumeUp;
    public final AnonymousClass1 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.volume.SafetyWarningDialog.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                if (D.BUG) {
                    Log.d(SafetyWarningDialog.TAG, "Received ACTION_CLOSE_SYSTEM_DIALOGS");
                }
                SafetyWarningDialog.this.cancel();
                SafetyWarningDialog.this.cleanUp();
            }
        }
    };
    public long mShowTime;

    public abstract void cleanUp();

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.mAudioManager.disableSafeMediaVolume();
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        try {
            this.mContext.unregisterReceiver(this.mReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        cleanUp();
    }

    @Override // android.app.AlertDialog, android.app.Dialog, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.mDisableOnVolumeUp && i == 24 && keyEvent.getRepeatCount() == 0) {
            this.mNewVolumeUp = true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.AlertDialog, android.app.Dialog, android.view.KeyEvent.Callback
    public final boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 24 && this.mNewVolumeUp && System.currentTimeMillis() - this.mShowTime > 1000) {
            if (D.BUG) {
                Log.d(TAG, "Confirmed warning via VOLUME_UP");
            }
            this.mAudioManager.disableSafeMediaVolume();
            dismiss();
        }
        return super.onKeyUp(i, keyEvent);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.volume.SafetyWarningDialog$1] */
    public SafetyWarningDialog(ContextThemeWrapper contextThemeWrapper, AudioManager audioManager) {
        super(contextThemeWrapper);
        this.mContext = contextThemeWrapper;
        this.mAudioManager = audioManager;
        try {
            this.mDisableOnVolumeUp = contextThemeWrapper.getResources().getBoolean(17891725);
        } catch (Resources.NotFoundException unused) {
            this.mDisableOnVolumeUp = true;
        }
        getWindow().setType(2010);
        SystemUIDialog.setShowForAllUsers(this);
        setMessage(this.mContext.getString(17041418));
        setButton(-1, this.mContext.getString(17039379), this);
        setButton(-2, this.mContext.getString(17039369), (DialogInterface.OnClickListener) null);
        setOnDismissListener(this);
        contextThemeWrapper.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), 2);
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public final void onStart() {
        super.onStart();
        this.mShowTime = System.currentTimeMillis();
    }
}
