package com.android.systemui.volume;

import android.content.Context;
import android.content.Intent;
import android.media.VolumePolicy;
import android.os.Bundle;
import android.util.Log;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.Prefs;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.qs.tiles.DndTile;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class VolumeDialogComponent implements VolumeComponent, TunerService.Tunable, VolumeDialogControllerImpl.UserActivityListener {
    public final ActivityStarter mActivityStarter;
    public final Context mContext;
    public final VolumeDialogControllerImpl mController;
    public VolumeDialog mDialog;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public static final Intent ZEN_SETTINGS = new Intent("android.settings.ZEN_MODE_SETTINGS");
    public static final Intent ZEN_PRIORITY_SETTINGS = new Intent("android.settings.ZEN_MODE_PRIORITY_SETTINGS");
    public final InterestingConfigChanges mConfigChanges = new InterestingConfigChanges(-1073741308);
    public VolumePolicy mVolumePolicy = new VolumePolicy(false, false, false, 400);
    public final AnonymousClass1 mVolumeDialogCallback = new VolumeDialog.Callback() { // from class: com.android.systemui.volume.VolumeDialogComponent.1
        @Override // com.android.systemui.plugins.VolumeDialog.Callback
        public final void onZenPrioritySettingsClicked() {
            VolumeDialogComponent volumeDialogComponent = VolumeDialogComponent.this;
            Intent intent = VolumeDialogComponent.ZEN_PRIORITY_SETTINGS;
            Objects.requireNonNull(volumeDialogComponent);
            volumeDialogComponent.mActivityStarter.startActivity(intent, true, true);
        }

        @Override // com.android.systemui.plugins.VolumeDialog.Callback
        public final void onZenSettingsClicked() {
            VolumeDialogComponent volumeDialogComponent = VolumeDialogComponent.this;
            Intent intent = VolumeDialogComponent.ZEN_SETTINGS;
            Objects.requireNonNull(volumeDialogComponent);
            volumeDialogComponent.mActivityStarter.startActivity(intent, true, true);
        }
    };

    @Override // com.android.systemui.demomode.DemoModeCommandReceiver
    public final void dispatchDemoCommand(String str, Bundle bundle) {
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.volume.VolumeDialogComponent$1] */
    public VolumeDialogComponent(Context context, KeyguardViewMediator keyguardViewMediator, ActivityStarter activityStarter, VolumeDialogControllerImpl volumeDialogControllerImpl, DemoModeController demoModeController, PluginDependencyProvider pluginDependencyProvider, ExtensionController extensionController, TunerService tunerService, final VolumeDialog volumeDialog) {
        this.mContext = context;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mActivityStarter = activityStarter;
        this.mController = volumeDialogControllerImpl;
        Objects.requireNonNull(volumeDialogControllerImpl);
        synchronized (volumeDialogControllerImpl) {
            volumeDialogControllerImpl.mUserActivityListener = this;
        }
        pluginDependencyProvider.allowPluginDependency(VolumeDialogController.class);
        ExtensionControllerImpl.ExtensionBuilder newExtension = extensionController.newExtension();
        Objects.requireNonNull(newExtension);
        String action = PluginManager.Helper.getAction(VolumeDialog.class);
        ExtensionControllerImpl.ExtensionImpl<T> extensionImpl = newExtension.mExtension;
        Objects.requireNonNull(extensionImpl);
        extensionImpl.mProducers.add(new ExtensionControllerImpl.ExtensionImpl.PluginItem(action, VolumeDialog.class, null));
        newExtension.withDefault(new Supplier() { // from class: com.android.systemui.volume.VolumeDialogComponent$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return VolumeDialog.this;
            }
        });
        newExtension.mExtension.mCallbacks.add(new VolumeDialogComponent$$ExternalSyntheticLambda0(this, 0));
        newExtension.build();
        volumeDialogControllerImpl.setVolumePolicy(this.mVolumePolicy);
        if (D.BUG) {
            Log.d(VolumeDialogControllerImpl.TAG, "showDndTile");
        }
        Context context2 = volumeDialogControllerImpl.mContext;
        Intent intent = DndTile.ZEN_SETTINGS;
        Prefs.putBoolean(context2, "DndTileVisible", true);
        tunerService.addTunable(this, "sysui_volume_down_silent", "sysui_volume_up_silent", "sysui_do_not_disturb");
        demoModeController.addCallback((DemoMode) this);
    }

    @Override // com.android.systemui.demomode.DemoMode
    public final List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("volume");
        return arrayList;
    }

    @Override // com.android.systemui.volume.VolumeComponent
    public final void dismissNow() {
        VolumeDialogControllerImpl volumeDialogControllerImpl = this.mController;
        Objects.requireNonNull(volumeDialogControllerImpl);
        volumeDialogControllerImpl.mCallbacks.onDismissRequested(2);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        VolumePolicy volumePolicy = this.mVolumePolicy;
        boolean z = volumePolicy.volumeDownToEnterSilent;
        boolean z2 = volumePolicy.volumeUpToExitSilent;
        boolean z3 = volumePolicy.doNotDisturbWhenSilent;
        if ("sysui_volume_down_silent".equals(str)) {
            z = TunerService.parseIntegerSwitch(str2, false);
        } else if ("sysui_volume_up_silent".equals(str)) {
            z2 = TunerService.parseIntegerSwitch(str2, false);
        } else if ("sysui_do_not_disturb".equals(str)) {
            z3 = TunerService.parseIntegerSwitch(str2, false);
        }
        VolumePolicy volumePolicy2 = new VolumePolicy(z, z2, z3, this.mVolumePolicy.vibrateToSilentDebounce);
        this.mVolumePolicy = volumePolicy2;
        this.mController.setVolumePolicy(volumePolicy2);
    }
}
