package com.android.systemui.media.dialog;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.MediaMetadata;
import android.media.MediaRoute2Info;
import android.media.RoutingSessionInfo;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.InfoMediaDevice;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3;
import com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda0;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class MediaOutputController implements LocalMediaManager.DeviceCallback {
    public static final boolean DEBUG = Log.isLoggable("MediaOutputController", 3);
    public final ActivityStarter mActivityStarter;
    public Callback mCallback;
    public int mColorActiveItem;
    public int mColorButtonBackground;
    public int mColorInactiveItem;
    public int mColorItemBackground;
    public int mColorSeekbarProgress;
    public final Context mContext;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public LocalMediaManager mLocalMediaManager;
    public MediaController mMediaController;
    public final MediaSessionManager mMediaSessionManager;
    public MediaOutputMetricLogger mMetricLogger;
    public final CommonNotifCollection mNotifCollection;
    public final String mPackageName;
    public final ShadeController mShadeController;
    public final boolean mVolumeAdjustmentForRemoteGroupSessions;
    public final List<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    public final AnonymousClass1 mCb = new MediaController.Callback() { // from class: com.android.systemui.media.dialog.MediaOutputController.1
        @Override // android.media.session.MediaController.Callback
        public final void onMetadataChanged(MediaMetadata mediaMetadata) {
            MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) MediaOutputController.this.mCallback;
            Objects.requireNonNull(mediaOutputBaseDialog);
            mediaOutputBaseDialog.mMainThreadHandler.post(new LockIconViewController$$ExternalSyntheticLambda2(mediaOutputBaseDialog, 1));
        }

        @Override // android.media.session.MediaController.Callback
        public final void onPlaybackStateChanged(PlaybackState playbackState) {
            int state = playbackState.getState();
            if (state == 1 || state == 2) {
                MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) MediaOutputController.this.mCallback;
                Objects.requireNonNull(mediaOutputBaseDialog);
                if (mediaOutputBaseDialog.isShowing()) {
                    mediaOutputBaseDialog.dismiss();
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public final ArrayList getSelectableMediaDevice() {
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        Objects.requireNonNull(localMediaManager);
        InfoMediaManager infoMediaManager = localMediaManager.mInfoMediaManager;
        Objects.requireNonNull(infoMediaManager);
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(infoMediaManager.mPackageName)) {
            Log.w("InfoMediaManager", "getSelectableMediaDevice() package name is null or empty!");
        } else {
            RoutingSessionInfo routingSessionInfo = infoMediaManager.getRoutingSessionInfo();
            if (routingSessionInfo != null) {
                for (MediaRoute2Info mediaRoute2Info : infoMediaManager.mRouterManager.getSelectableRoutes(routingSessionInfo)) {
                    arrayList.add(new InfoMediaDevice(infoMediaManager.mContext, infoMediaManager.mRouterManager, mediaRoute2Info, infoMediaManager.mPackageName));
                }
            } else {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("getSelectableMediaDevice() cannot found selectable MediaDevice from : ");
                m.append(infoMediaManager.mPackageName);
                Log.w("InfoMediaManager", m.toString());
            }
        }
        return arrayList;
    }

    public final ArrayList getSelectedMediaDevice() {
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        Objects.requireNonNull(localMediaManager);
        InfoMediaManager infoMediaManager = localMediaManager.mInfoMediaManager;
        Objects.requireNonNull(infoMediaManager);
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(infoMediaManager.mPackageName)) {
            Log.w("InfoMediaManager", "getSelectedMediaDevice() package name is null or empty!");
        } else {
            RoutingSessionInfo routingSessionInfo = infoMediaManager.getRoutingSessionInfo();
            if (routingSessionInfo != null) {
                for (MediaRoute2Info mediaRoute2Info : infoMediaManager.mRouterManager.getSelectedRoutes(routingSessionInfo)) {
                    arrayList.add(new InfoMediaDevice(infoMediaManager.mContext, infoMediaManager.mRouterManager, mediaRoute2Info, infoMediaManager.mPackageName));
                }
            } else {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("getSelectedMediaDevice() cannot found selectable MediaDevice from : ");
                m.append(infoMediaManager.mPackageName);
                Log.w("InfoMediaManager", m.toString());
            }
        }
        return arrayList;
    }

    public final boolean hasAdjustVolumeUserRestriction() {
        if (RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_adjust_volume", UserHandle.myUserId()) != null) {
            return true;
        }
        return ((UserManager) this.mContext.getSystemService(UserManager.class)).hasBaseUserRestriction("no_adjust_volume", UserHandle.of(UserHandle.myUserId()));
    }

    public final boolean isTransferring() {
        for (MediaDevice mediaDevice : this.mMediaDevices) {
            Objects.requireNonNull(mediaDevice);
            if (mediaDevice.mState == 1) {
                return true;
            }
        }
        return false;
    }

    public final boolean isZeroMode() {
        if (this.mMediaDevices.size() != 1) {
            return false;
        }
        MediaDevice next = this.mMediaDevices.iterator().next();
        Objects.requireNonNull(next);
        int i = next.mType;
        if (i == 7 || i == 2 || i == 1) {
            return true;
        }
        return false;
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public final void onDeviceAttributesChanged() {
        MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.mCallback;
        Objects.requireNonNull(mediaOutputBaseDialog);
        mediaOutputBaseDialog.mMainThreadHandler.post(new Action$$ExternalSyntheticLambda0(mediaOutputBaseDialog, 1));
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public final void onDeviceListUpdate(ArrayList arrayList) {
        if (this.mMediaDevices.isEmpty()) {
            LocalMediaManager localMediaManager = this.mLocalMediaManager;
            Objects.requireNonNull(localMediaManager);
            MediaDevice mediaDevice = localMediaManager.mCurrentConnectedDevice;
            if (mediaDevice == null) {
                if (DEBUG) {
                    Log.d("MediaOutputController", "No connected media device.");
                }
                this.mMediaDevices.addAll(arrayList);
            } else {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    MediaDevice mediaDevice2 = (MediaDevice) it.next();
                    if (TextUtils.equals(mediaDevice2.getId(), mediaDevice.getId())) {
                        this.mMediaDevices.add(0, mediaDevice2);
                    } else {
                        this.mMediaDevices.add(mediaDevice2);
                    }
                }
            }
        } else {
            ArrayList arrayList2 = new ArrayList();
            for (MediaDevice mediaDevice3 : this.mMediaDevices) {
                Iterator it2 = arrayList.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        MediaDevice mediaDevice4 = (MediaDevice) it2.next();
                        if (TextUtils.equals(mediaDevice3.getId(), mediaDevice4.getId())) {
                            arrayList2.add(mediaDevice4);
                            break;
                        }
                    }
                }
            }
            if (arrayList2.size() != arrayList.size()) {
                arrayList.removeAll(arrayList2);
                arrayList2.addAll(arrayList);
            }
            this.mMediaDevices.clear();
            this.mMediaDevices.addAll(arrayList2);
        }
        MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.mCallback;
        Objects.requireNonNull(mediaOutputBaseDialog);
        mediaOutputBaseDialog.mMainThreadHandler.post(new PipTaskOrganizer$$ExternalSyntheticLambda3(mediaOutputBaseDialog, 1));
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public final void onRequestFailed(int i) {
        int i2;
        MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.mCallback;
        Objects.requireNonNull(mediaOutputBaseDialog);
        mediaOutputBaseDialog.mMainThreadHandler.post(new Action$$ExternalSyntheticLambda0(mediaOutputBaseDialog, 1));
        MediaOutputMetricLogger mediaOutputMetricLogger = this.mMetricLogger;
        List<MediaDevice> list = this.mMediaDevices;
        if (MediaOutputMetricLogger.DEBUG) {
            Objects.requireNonNull(mediaOutputMetricLogger);
            Log.e("MediaOutputMetricLogger", "logRequestFailed - " + i);
        }
        mediaOutputMetricLogger.updateLoggingDeviceCount(list);
        int loggingDeviceType = MediaOutputMetricLogger.getLoggingDeviceType(mediaOutputMetricLogger.mSourceDevice);
        int loggingDeviceType2 = MediaOutputMetricLogger.getLoggingDeviceType(mediaOutputMetricLogger.mTargetDevice);
        int i3 = 2;
        if (i != 1) {
            if (i != 2) {
                i3 = 4;
                if (i != 3) {
                    if (i != 4) {
                        i2 = 0;
                    } else {
                        i3 = 5;
                    }
                }
            } else {
                i2 = 3;
            }
            SysUiStatsLog.write(loggingDeviceType, loggingDeviceType2, 0, i2, mediaOutputMetricLogger.getLoggingPackageName(), mediaOutputMetricLogger.mWiredDeviceCount, mediaOutputMetricLogger.mConnectedBluetoothDeviceCount, mediaOutputMetricLogger.mRemoteDeviceCount);
        }
        i2 = i3;
        SysUiStatsLog.write(loggingDeviceType, loggingDeviceType2, 0, i2, mediaOutputMetricLogger.getLoggingPackageName(), mediaOutputMetricLogger.mWiredDeviceCount, mediaOutputMetricLogger.mConnectedBluetoothDeviceCount, mediaOutputMetricLogger.mRemoteDeviceCount);
    }

    @Override // com.android.settingslib.media.LocalMediaManager.DeviceCallback
    public final void onSelectedDeviceStateChanged(MediaDevice mediaDevice) {
        MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.mCallback;
        Objects.requireNonNull(mediaOutputBaseDialog);
        mediaOutputBaseDialog.mMainThreadHandler.post(new Action$$ExternalSyntheticLambda0(mediaOutputBaseDialog, 1));
        MediaOutputMetricLogger mediaOutputMetricLogger = this.mMetricLogger;
        String obj = mediaDevice.toString();
        List<MediaDevice> list = this.mMediaDevices;
        if (MediaOutputMetricLogger.DEBUG) {
            Objects.requireNonNull(mediaOutputMetricLogger);
            Log.d("MediaOutputMetricLogger", "logOutputSuccess - selected device: " + obj);
        }
        mediaOutputMetricLogger.updateLoggingDeviceCount(list);
        SysUiStatsLog.write(MediaOutputMetricLogger.getLoggingDeviceType(mediaOutputMetricLogger.mSourceDevice), MediaOutputMetricLogger.getLoggingDeviceType(mediaOutputMetricLogger.mTargetDevice), 1, 1, mediaOutputMetricLogger.getLoggingPackageName(), mediaOutputMetricLogger.mWiredDeviceCount, mediaOutputMetricLogger.mConnectedBluetoothDeviceCount, mediaOutputMetricLogger.mRemoteDeviceCount);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.media.dialog.MediaOutputController$1] */
    public MediaOutputController(Context context, String str, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ShadeController shadeController, ActivityStarter activityStarter, CommonNotifCollection commonNotifCollection, DialogLaunchAnimator dialogLaunchAnimator) {
        new CopyOnWriteArrayList();
        this.mContext = context;
        this.mPackageName = str;
        this.mMediaSessionManager = mediaSessionManager;
        this.mShadeController = shadeController;
        this.mActivityStarter = activityStarter;
        this.mNotifCollection = commonNotifCollection;
        this.mLocalMediaManager = new LocalMediaManager(context, localBluetoothManager, new InfoMediaManager(context, str, localBluetoothManager), str);
        this.mMetricLogger = new MediaOutputMetricLogger(context, str);
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mVolumeAdjustmentForRemoteGroupSessions = context.getResources().getBoolean(17891813);
        this.mColorActiveItem = Utils.getColorStateListDefaultColor(context, 2131100380);
        this.mColorInactiveItem = Utils.getColorStateListDefaultColor(context, 2131100382);
        this.mColorSeekbarProgress = Utils.getColorStateListDefaultColor(context, 17170491);
        this.mColorButtonBackground = Utils.getColorStateListDefaultColor(context, 2131100383);
        this.mColorItemBackground = Utils.getColorStateListDefaultColor(context, 17170502);
    }

    public static boolean isActiveRemoteDevice(MediaDevice mediaDevice) {
        List<String> list;
        Objects.requireNonNull(mediaDevice);
        MediaRoute2Info mediaRoute2Info = mediaDevice.mRouteInfo;
        if (mediaRoute2Info == null) {
            Log.w("MediaDevice", "Unable to get features. RouteInfo is empty");
            list = new ArrayList<>();
        } else {
            list = mediaRoute2Info.getFeatures();
        }
        if (list.contains("android.media.route.feature.REMOTE_PLAYBACK") || list.contains("android.media.route.feature.REMOTE_AUDIO_PLAYBACK") || list.contains("android.media.route.feature.REMOTE_VIDEO_PLAYBACK") || list.contains("android.media.route.feature.REMOTE_GROUP_PLAYBACK")) {
            return true;
        }
        return false;
    }
}
