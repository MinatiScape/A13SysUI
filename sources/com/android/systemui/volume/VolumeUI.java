package com.android.systemui.volume;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.Prefs;
import com.android.systemui.qs.tiles.DndTile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class VolumeUI extends CoreStartable {
    public static boolean LOGD = Log.isLoggable("VolumeUI", 3);
    public boolean mEnabled;
    public VolumeDialogComponent mVolumeComponent;

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("mEnabled=");
        printWriter.println(this.mEnabled);
        if (this.mEnabled) {
            Objects.requireNonNull(this.mVolumeComponent);
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void onConfigurationChanged(Configuration configuration) {
        if (this.mEnabled) {
            VolumeDialogComponent volumeDialogComponent = this.mVolumeComponent;
            Objects.requireNonNull(volumeDialogComponent);
            if (volumeDialogComponent.mConfigChanges.applyNewConfig(volumeDialogComponent.mContext.getResources())) {
                volumeDialogComponent.mController.mCallbacks.onConfigurationChanged();
            }
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        boolean z;
        boolean z2 = this.mContext.getResources().getBoolean(2131034192);
        boolean z3 = this.mContext.getResources().getBoolean(2131034191);
        if (z2 || z3) {
            z = true;
        } else {
            z = false;
        }
        this.mEnabled = z;
        if (z) {
            VolumeDialogComponent volumeDialogComponent = this.mVolumeComponent;
            Objects.requireNonNull(volumeDialogComponent);
            VolumeDialogControllerImpl volumeDialogControllerImpl = volumeDialogComponent.mController;
            Objects.requireNonNull(volumeDialogControllerImpl);
            volumeDialogControllerImpl.mShowVolumeDialog = z2;
            volumeDialogControllerImpl.mShowSafetyWarning = z3;
            Context context = this.mContext;
            Intent intent = DndTile.ZEN_SETTINGS;
            Prefs.putBoolean(context, "DndTileVisible", true);
            if (LOGD) {
                Log.d("VolumeUI", "Registering default volume controller");
            }
            VolumeDialogComponent volumeDialogComponent2 = this.mVolumeComponent;
            Objects.requireNonNull(volumeDialogComponent2);
            VolumeDialogControllerImpl volumeDialogControllerImpl2 = volumeDialogComponent2.mController;
            Objects.requireNonNull(volumeDialogControllerImpl2);
            try {
                volumeDialogControllerImpl2.mAudio.setVolumeController(volumeDialogControllerImpl2.mVolumeController);
            } catch (SecurityException e) {
                Log.w(VolumeDialogControllerImpl.TAG, "Unable to set the volume controller", e);
            }
            volumeDialogControllerImpl2.setVolumePolicy(volumeDialogControllerImpl2.mVolumePolicy);
            boolean z4 = volumeDialogControllerImpl2.mShowDndTile;
            if (D.BUG) {
                Log.d(VolumeDialogControllerImpl.TAG, "showDndTile");
            }
            Context context2 = volumeDialogControllerImpl2.mContext;
            Intent intent2 = DndTile.ZEN_SETTINGS;
            Prefs.putBoolean(context2, "DndTileVisible", z4);
            try {
                volumeDialogControllerImpl2.mMediaSessions.init();
            } catch (SecurityException e2) {
                Log.w(VolumeDialogControllerImpl.TAG, "No access to media sessions", e2);
            }
            Prefs.putBoolean(volumeDialogComponent2.mContext, "DndTileCombinedIcon", true);
        }
    }

    public VolumeUI(Context context, VolumeDialogComponent volumeDialogComponent) {
        super(context);
        new Handler();
        this.mVolumeComponent = volumeDialogComponent;
    }
}
