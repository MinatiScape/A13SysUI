package com.android.systemui.usb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.UserHandle;
import android.os.storage.DiskInfo;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.VolumeRecord;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.systemui.CoreStartable;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Objects;
/* loaded from: classes.dex */
public class StorageNotification extends CoreStartable {
    public NotificationManager mNotificationManager;
    public StorageManager mStorageManager;
    public final SparseArray<MoveInfo> mMoves = new SparseArray<>();
    public final AnonymousClass1 mListener = new StorageEventListener() { // from class: com.android.systemui.usb.StorageNotification.1
        public final void onDiskDestroyed(DiskInfo diskInfo) {
            StorageNotification storageNotification = StorageNotification.this;
            Objects.requireNonNull(storageNotification);
            storageNotification.mNotificationManager.cancelAsUser(diskInfo.getId(), 1396986699, UserHandle.ALL);
        }

        public final void onDiskScanned(DiskInfo diskInfo, int i) {
            StorageNotification.this.onDiskScannedInternal(diskInfo, i);
        }

        public final void onVolumeForgotten(String str) {
            StorageNotification.this.mNotificationManager.cancelAsUser(str, 1397772886, UserHandle.ALL);
        }

        public final void onVolumeRecordChanged(VolumeRecord volumeRecord) {
            VolumeInfo findVolumeByUuid = StorageNotification.this.mStorageManager.findVolumeByUuid(volumeRecord.getFsUuid());
            if (findVolumeByUuid != null && findVolumeByUuid.isMountedReadable()) {
                StorageNotification.this.onVolumeStateChangedInternal(findVolumeByUuid);
            }
        }

        public final void onVolumeStateChanged(VolumeInfo volumeInfo, int i, int i2) {
            StorageNotification.this.onVolumeStateChangedInternal(volumeInfo);
        }
    };
    public final AnonymousClass2 mSnoozeReceiver = new BroadcastReceiver() { // from class: com.android.systemui.usb.StorageNotification.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            StorageNotification.this.mStorageManager.setVolumeSnoozed(intent.getStringExtra("android.os.storage.extra.FS_UUID"), true);
        }
    };
    public final AnonymousClass3 mFinishReceiver = new BroadcastReceiver() { // from class: com.android.systemui.usb.StorageNotification.3
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            StorageNotification.this.mNotificationManager.cancelAsUser(null, 1397575510, UserHandle.ALL);
        }
    };
    public final AnonymousClass4 mMoveCallback = new PackageManager.MoveCallback() { // from class: com.android.systemui.usb.StorageNotification.4
        public final void onCreated(int i, Bundle bundle) {
            MoveInfo moveInfo = new MoveInfo(0);
            moveInfo.moveId = i;
            if (bundle != null) {
                moveInfo.packageName = bundle.getString("android.intent.extra.PACKAGE_NAME");
                moveInfo.label = bundle.getString("android.intent.extra.TITLE");
                moveInfo.volumeUuid = bundle.getString("android.os.storage.extra.FS_UUID");
            }
            StorageNotification.this.mMoves.put(i, moveInfo);
        }

        public final void onStatusChanged(int i, int i2, long j) {
            String str;
            CharSequence charSequence;
            PendingIntent pendingIntent;
            String str2;
            String str3;
            PendingIntent pendingIntent2;
            MoveInfo moveInfo = StorageNotification.this.mMoves.get(i);
            if (moveInfo == null) {
                GridLayoutManager$$ExternalSyntheticOutline1.m("Ignoring unknown move ", i, "StorageNotification");
            } else if (PackageManager.isMoveStatusFinished(i2)) {
                StorageNotification storageNotification = StorageNotification.this;
                Objects.requireNonNull(storageNotification);
                String str4 = moveInfo.packageName;
                if (str4 != null) {
                    storageNotification.mNotificationManager.cancelAsUser(str4, 1397575510, UserHandle.ALL);
                    return;
                }
                VolumeInfo primaryStorageCurrentVolume = storageNotification.mContext.getPackageManager().getPrimaryStorageCurrentVolume();
                String bestVolumeDescription = storageNotification.mStorageManager.getBestVolumeDescription(primaryStorageCurrentVolume);
                if (i2 == -100) {
                    str3 = storageNotification.mContext.getString(17040220);
                    str2 = storageNotification.mContext.getString(17040219, bestVolumeDescription);
                } else {
                    str3 = storageNotification.mContext.getString(17040217);
                    str2 = storageNotification.mContext.getString(17040216);
                }
                if (primaryStorageCurrentVolume == null || primaryStorageCurrentVolume.getDisk() == null) {
                    if (primaryStorageCurrentVolume != null) {
                        Intent intent = new Intent();
                        if (storageNotification.isTv()) {
                            intent.setPackage("com.android.tv.settings");
                            intent.setAction("android.settings.INTERNAL_STORAGE_SETTINGS");
                        } else if (!storageNotification.isAutomotive()) {
                            int type = primaryStorageCurrentVolume.getType();
                            if (type == 0) {
                                intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.Settings$PublicVolumeSettingsActivity");
                            } else if (type == 1) {
                                intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.Settings$PrivateVolumeSettingsActivity");
                            }
                        }
                        intent.putExtra("android.os.storage.extra.VOLUME_ID", primaryStorageCurrentVolume.getId());
                        pendingIntent2 = PendingIntent.getActivityAsUser(storageNotification.mContext, primaryStorageCurrentVolume.getId().hashCode(), intent, 335544320, null, UserHandle.CURRENT);
                        Notification.Builder autoCancel = new Notification.Builder(storageNotification.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification.mContext.getColor(17170460)).setContentTitle(str3).setContentText(str2).setContentIntent(pendingIntent2).setStyle(new Notification.BigTextStyle().bigText(str2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setAutoCancel(true);
                        SystemUIApplication.overrideNotificationAppName(storageNotification.mContext, autoCancel, false);
                        storageNotification.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, autoCancel.build(), UserHandle.ALL);
                    }
                    pendingIntent2 = null;
                    Notification.Builder autoCancel2 = new Notification.Builder(storageNotification.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification.mContext.getColor(17170460)).setContentTitle(str3).setContentText(str2).setContentIntent(pendingIntent2).setStyle(new Notification.BigTextStyle().bigText(str2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setAutoCancel(true);
                    SystemUIApplication.overrideNotificationAppName(storageNotification.mContext, autoCancel2, false);
                    storageNotification.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, autoCancel2.build(), UserHandle.ALL);
                }
                DiskInfo disk = primaryStorageCurrentVolume.getDisk();
                Intent intent2 = new Intent();
                if (storageNotification.isTv()) {
                    intent2.setPackage("com.android.tv.settings");
                    intent2.setAction("android.settings.INTERNAL_STORAGE_SETTINGS");
                } else {
                    if (!storageNotification.isAutomotive()) {
                        intent2.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardReady");
                    }
                    pendingIntent2 = null;
                    Notification.Builder autoCancel22 = new Notification.Builder(storageNotification.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification.mContext.getColor(17170460)).setContentTitle(str3).setContentText(str2).setContentIntent(pendingIntent2).setStyle(new Notification.BigTextStyle().bigText(str2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setAutoCancel(true);
                    SystemUIApplication.overrideNotificationAppName(storageNotification.mContext, autoCancel22, false);
                    storageNotification.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, autoCancel22.build(), UserHandle.ALL);
                }
                intent2.putExtra("android.os.storage.extra.DISK_ID", disk.getId());
                pendingIntent2 = PendingIntent.getActivityAsUser(storageNotification.mContext, disk.getId().hashCode(), intent2, 335544320, null, UserHandle.CURRENT);
                Notification.Builder autoCancel222 = new Notification.Builder(storageNotification.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification.mContext.getColor(17170460)).setContentTitle(str3).setContentText(str2).setContentIntent(pendingIntent2).setStyle(new Notification.BigTextStyle().bigText(str2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setAutoCancel(true);
                SystemUIApplication.overrideNotificationAppName(storageNotification.mContext, autoCancel222, false);
                storageNotification.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, autoCancel222.build(), UserHandle.ALL);
            } else {
                StorageNotification storageNotification2 = StorageNotification.this;
                Objects.requireNonNull(storageNotification2);
                if (!TextUtils.isEmpty(moveInfo.label)) {
                    str = storageNotification2.mContext.getString(17040218, moveInfo.label);
                } else {
                    str = storageNotification2.mContext.getString(17040221);
                }
                if (j < 0) {
                    charSequence = null;
                } else {
                    charSequence = DateUtils.formatDuration(j);
                }
                if (moveInfo.packageName != null) {
                    Intent intent3 = new Intent();
                    if (storageNotification2.isTv()) {
                        intent3.setPackage("com.android.tv.settings");
                        intent3.setAction("com.android.tv.settings.action.MOVE_APP");
                    } else {
                        if (!storageNotification2.isAutomotive()) {
                            intent3.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardMoveProgress");
                        }
                        pendingIntent = null;
                        Notification.Builder ongoing = new Notification.Builder(storageNotification2.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification2.mContext.getColor(17170460)).setContentTitle(str).setContentText(charSequence).setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(charSequence)).setVisibility(1).setLocalOnly(true).setCategory("progress").setProgress(100, i2, false).setOngoing(true);
                        SystemUIApplication.overrideNotificationAppName(storageNotification2.mContext, ongoing, false);
                        storageNotification2.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, ongoing.build(), UserHandle.ALL);
                    }
                    intent3.putExtra("android.content.pm.extra.MOVE_ID", moveInfo.moveId);
                    pendingIntent = PendingIntent.getActivityAsUser(storageNotification2.mContext, moveInfo.moveId, intent3, 335544320, null, UserHandle.CURRENT);
                    Notification.Builder ongoing2 = new Notification.Builder(storageNotification2.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification2.mContext.getColor(17170460)).setContentTitle(str).setContentText(charSequence).setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(charSequence)).setVisibility(1).setLocalOnly(true).setCategory("progress").setProgress(100, i2, false).setOngoing(true);
                    SystemUIApplication.overrideNotificationAppName(storageNotification2.mContext, ongoing2, false);
                    storageNotification2.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, ongoing2.build(), UserHandle.ALL);
                }
                Intent intent4 = new Intent();
                if (storageNotification2.isTv()) {
                    intent4.setPackage("com.android.tv.settings");
                    intent4.setAction("com.android.tv.settings.action.MIGRATE_STORAGE");
                } else {
                    if (!storageNotification2.isAutomotive()) {
                        intent4.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardMigrateProgress");
                    }
                    pendingIntent = null;
                    Notification.Builder ongoing22 = new Notification.Builder(storageNotification2.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification2.mContext.getColor(17170460)).setContentTitle(str).setContentText(charSequence).setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(charSequence)).setVisibility(1).setLocalOnly(true).setCategory("progress").setProgress(100, i2, false).setOngoing(true);
                    SystemUIApplication.overrideNotificationAppName(storageNotification2.mContext, ongoing22, false);
                    storageNotification2.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, ongoing22.build(), UserHandle.ALL);
                }
                intent4.putExtra("android.content.pm.extra.MOVE_ID", moveInfo.moveId);
                VolumeInfo findVolumeByQualifiedUuid = storageNotification2.mStorageManager.findVolumeByQualifiedUuid(moveInfo.volumeUuid);
                if (findVolumeByQualifiedUuid != null) {
                    intent4.putExtra("android.os.storage.extra.VOLUME_ID", findVolumeByQualifiedUuid.getId());
                }
                pendingIntent = PendingIntent.getActivityAsUser(storageNotification2.mContext, moveInfo.moveId, intent4, 335544320, null, UserHandle.CURRENT);
                Notification.Builder ongoing222 = new Notification.Builder(storageNotification2.mContext, "DSK").setSmallIcon(17302834).setColor(storageNotification2.mContext.getColor(17170460)).setContentTitle(str).setContentText(charSequence).setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(charSequence)).setVisibility(1).setLocalOnly(true).setCategory("progress").setProgress(100, i2, false).setOngoing(true);
                SystemUIApplication.overrideNotificationAppName(storageNotification2.mContext, ongoing222, false);
                storageNotification2.mNotificationManager.notifyAsUser(moveInfo.packageName, 1397575510, ongoing222.build(), UserHandle.ALL);
            }
        }
    };

    /* loaded from: classes.dex */
    public static class MoveInfo {
        public String label;
        public int moveId;
        public String packageName;
        public String volumeUuid;

        public MoveInfo() {
        }

        public MoveInfo(int i) {
        }
    }

    public final PendingIntent buildInitPendingIntent(VolumeInfo volumeInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.NEW_STORAGE");
        } else if (isAutomotive()) {
            return null;
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardInit");
        }
        intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
        return PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, null, UserHandle.CURRENT);
    }

    public final Notification.Builder buildNotificationBuilder(VolumeInfo volumeInfo, String str, String str2) {
        Notification.Builder builder = new Notification.Builder(this.mContext, "DSK");
        DiskInfo disk = volumeInfo.getDisk();
        volumeInfo.getState();
        int i = 17302834;
        if (!disk.isSd() && disk.isUsb()) {
            i = 17302876;
        }
        Notification.Builder extend = builder.setSmallIcon(i).setColor(this.mContext.getColor(17170460)).setContentTitle(str).setContentText(str2).setStyle(new Notification.BigTextStyle().bigText(str2)).setVisibility(1).setLocalOnly(true).extend(new Notification.TvExtender());
        SystemUIApplication.overrideNotificationAppName(this.mContext, extend, false);
        return extend;
    }

    public final PendingIntent buildSnoozeIntent(String str) {
        Intent intent = new Intent("com.android.systemui.action.SNOOZE_VOLUME");
        intent.putExtra("android.os.storage.extra.FS_UUID", str);
        return PendingIntent.getBroadcastAsUser(this.mContext, str.hashCode(), intent, 335544320, UserHandle.CURRENT);
    }

    public final PendingIntent buildUnmountPendingIntent(VolumeInfo volumeInfo) {
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.UNMOUNT_STORAGE");
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            return PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, null, UserHandle.CURRENT);
        } else if (isAutomotive()) {
            intent.setClassName("com.android.car.settings", "com.android.car.settings.storage.StorageUnmountReceiver");
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            return PendingIntent.getBroadcastAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, UserHandle.CURRENT);
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageUnmountReceiver");
            intent.putExtra("android.os.storage.extra.VOLUME_ID", volumeInfo.getId());
            return PendingIntent.getBroadcastAsUser(this.mContext, volumeInfo.getId().hashCode(), intent, 335544320, UserHandle.CURRENT);
        }
    }

    public final boolean isAutomotive() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    public final boolean isTv() {
        return this.mContext.getPackageManager().hasSystemFeature("android.software.leanback");
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        StorageManager storageManager = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        this.mStorageManager = storageManager;
        storageManager.registerListener(this.mListener);
        this.mContext.registerReceiver(this.mSnoozeReceiver, new IntentFilter("com.android.systemui.action.SNOOZE_VOLUME"), "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", null, 2);
        this.mContext.registerReceiver(this.mFinishReceiver, new IntentFilter("com.android.systemui.action.FINISH_WIZARD"), "android.permission.MOUNT_UNMOUNT_FILESYSTEMS", null, 2);
        for (DiskInfo diskInfo : this.mStorageManager.getDisks()) {
            onDiskScannedInternal(diskInfo, diskInfo.volumeCount);
        }
        for (VolumeInfo volumeInfo : this.mStorageManager.getVolumes()) {
            onVolumeStateChangedInternal(volumeInfo);
        }
        this.mContext.getPackageManager().registerMoveCallback(this.mMoveCallback, new Handler());
        updateMissingPrivateVolumes();
    }

    public final void updateMissingPrivateVolumes() {
        if (!(isTv() || isAutomotive())) {
            for (VolumeRecord volumeRecord : this.mStorageManager.getVolumeRecords()) {
                if (volumeRecord.getType() == 1) {
                    String fsUuid = volumeRecord.getFsUuid();
                    VolumeInfo findVolumeByUuid = this.mStorageManager.findVolumeByUuid(fsUuid);
                    if ((findVolumeByUuid == null || !findVolumeByUuid.isMountedWritable()) && !volumeRecord.isSnoozed()) {
                        String string = this.mContext.getString(17040215, volumeRecord.getNickname());
                        String string2 = this.mContext.getString(17040214);
                        Notification.Builder contentText = new Notification.Builder(this.mContext, "DSK").setSmallIcon(17302834).setColor(this.mContext.getColor(17170460)).setContentTitle(string).setContentText(string2);
                        Intent intent = new Intent();
                        intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.Settings$PrivateVolumeForgetActivity");
                        intent.putExtra("android.os.storage.extra.FS_UUID", volumeRecord.getFsUuid());
                        Notification.Builder extend = contentText.setContentIntent(PendingIntent.getActivityAsUser(this.mContext, volumeRecord.getFsUuid().hashCode(), intent, 335544320, null, UserHandle.CURRENT)).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1).setLocalOnly(true).setCategory("sys").setDeleteIntent(buildSnoozeIntent(fsUuid)).extend(new Notification.TvExtender());
                        SystemUIApplication.overrideNotificationAppName(this.mContext, extend, false);
                        this.mNotificationManager.notifyAsUser(fsUuid, 1397772886, extend.build(), UserHandle.ALL);
                    } else {
                        this.mNotificationManager.cancelAsUser(fsUuid, 1397772886, UserHandle.ALL);
                    }
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.usb.StorageNotification$2] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.usb.StorageNotification$3] */
    public StorageNotification(Context context) {
        super(context);
    }

    public final void onDiskScannedInternal(DiskInfo diskInfo, int i) {
        PendingIntent pendingIntent;
        if (i != 0 || diskInfo.size <= 0) {
            this.mNotificationManager.cancelAsUser(diskInfo.getId(), 1396986699, UserHandle.ALL);
            return;
        }
        String string = this.mContext.getString(17040245, diskInfo.getDescription());
        String string2 = this.mContext.getString(17040244, diskInfo.getDescription());
        Notification.Builder builder = new Notification.Builder(this.mContext, "DSK");
        int i2 = 17302834;
        if (!diskInfo.isSd() && diskInfo.isUsb()) {
            i2 = 17302876;
        }
        Notification.Builder contentText = builder.setSmallIcon(i2).setColor(this.mContext.getColor(17170460)).setContentTitle(string).setContentText(string2);
        Intent intent = new Intent();
        if (isTv()) {
            intent.setPackage("com.android.tv.settings");
            intent.setAction("com.android.tv.settings.action.NEW_STORAGE");
        } else if (isAutomotive()) {
            pendingIntent = null;
            Notification.Builder extend = contentText.setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1).setLocalOnly(true).setCategory("err").extend(new Notification.TvExtender());
            SystemUIApplication.overrideNotificationAppName(this.mContext, extend, false);
            this.mNotificationManager.notifyAsUser(diskInfo.getId(), 1396986699, extend.build(), UserHandle.ALL);
        } else {
            intent.setClassName(ThemeOverlayApplier.SETTINGS_PACKAGE, "com.android.settings.deviceinfo.StorageWizardInit");
        }
        intent.putExtra("android.os.storage.extra.DISK_ID", diskInfo.getId());
        pendingIntent = PendingIntent.getActivityAsUser(this.mContext, diskInfo.getId().hashCode(), intent, 335544320, null, UserHandle.CURRENT);
        Notification.Builder extend2 = contentText.setContentIntent(pendingIntent).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1).setLocalOnly(true).setCategory("err").extend(new Notification.TvExtender());
        SystemUIApplication.overrideNotificationAppName(this.mContext, extend2, false);
        this.mNotificationManager.notifyAsUser(diskInfo.getId(), 1396986699, extend2.build(), UserHandle.ALL);
    }

    /* JADX WARN: Finally extract failed */
    public final void onVolumeStateChangedInternal(VolumeInfo volumeInfo) {
        Notification build;
        PendingIntent pendingIntent;
        int type = volumeInfo.getType();
        if (type == 0) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Notifying about public volume: ");
            m.append(volumeInfo.toString());
            Log.d("StorageNotification", m.toString());
            if (volumeInfo.getMountUserId() == -10000) {
                Log.d("StorageNotification", "Ignore public volume state change event of removed user");
                return;
            }
            Notification notification = null;
            switch (volumeInfo.getState()) {
                case 1:
                    DiskInfo disk = volumeInfo.getDisk();
                    notification = buildNotificationBuilder(volumeInfo, this.mContext.getString(17040212, disk.getDescription()), this.mContext.getString(17040211, disk.getDescription())).setCategory("progress").setOngoing(true).build();
                    break;
                case 2:
                case 3:
                    VolumeRecord findRecordByUuid = this.mStorageManager.findRecordByUuid(volumeInfo.getFsUuid());
                    DiskInfo disk2 = volumeInfo.getDisk();
                    if (!findRecordByUuid.isSnoozed() || !disk2.isAdoptable()) {
                        if (!disk2.isAdoptable() || findRecordByUuid.isInited()) {
                            String description = disk2.getDescription();
                            String string = this.mContext.getString(17040226, disk2.getDescription());
                            StrictMode.VmPolicy allowVmViolations = StrictMode.allowVmViolations();
                            try {
                                PendingIntent activityAsUser = PendingIntent.getActivityAsUser(this.mContext, volumeInfo.getId().hashCode(), volumeInfo.buildBrowseIntentForUser(volumeInfo.getMountUserId()), 335544320, null, UserHandle.CURRENT);
                                StrictMode.setVmPolicy(allowVmViolations);
                                Notification.Builder category = buildNotificationBuilder(volumeInfo, description, string).addAction(new Notification.Action(17302456, this.mContext.getString(17040210), activityAsUser)).addAction(new Notification.Action(17302433, this.mContext.getString(17040239), buildUnmountPendingIntent(volumeInfo))).setContentIntent(activityAsUser).setCategory("sys");
                                if (disk2.isAdoptable()) {
                                    category.setDeleteIntent(buildSnoozeIntent(volumeInfo.getFsUuid()));
                                }
                                build = category.build();
                            } catch (Throwable th) {
                                StrictMode.setVmPolicy(allowVmViolations);
                                throw th;
                            }
                        } else {
                            String description2 = disk2.getDescription();
                            String string2 = this.mContext.getString(17040222, disk2.getDescription());
                            PendingIntent buildInitPendingIntent = buildInitPendingIntent(volumeInfo);
                            PendingIntent buildUnmountPendingIntent = buildUnmountPendingIntent(volumeInfo);
                            if (isAutomotive()) {
                                build = buildNotificationBuilder(volumeInfo, description2, string2).setContentIntent(buildUnmountPendingIntent).setDeleteIntent(buildSnoozeIntent(volumeInfo.getFsUuid())).build();
                            } else {
                                build = buildNotificationBuilder(volumeInfo, description2, string2).addAction(new Notification.Action(17302840, this.mContext.getString(17040213), buildInitPendingIntent)).addAction(new Notification.Action(17302433, this.mContext.getString(17040239), buildUnmountPendingIntent)).setContentIntent(buildInitPendingIntent).setDeleteIntent(buildSnoozeIntent(volumeInfo.getFsUuid())).build();
                            }
                        }
                        notification = build;
                        break;
                    }
                    break;
                case 5:
                    DiskInfo disk3 = volumeInfo.getDisk();
                    notification = buildNotificationBuilder(volumeInfo, this.mContext.getString(17040243, disk3.getDescription()), this.mContext.getString(17040242, disk3.getDescription())).setCategory("progress").setOngoing(true).build();
                    break;
                case FalsingManager.VERSION /* 6 */:
                    DiskInfo disk4 = volumeInfo.getDisk();
                    String string3 = this.mContext.getString(17040241, disk4.getDescription());
                    String string4 = this.mContext.getString(17040240, disk4.getDescription());
                    if (isAutomotive()) {
                        pendingIntent = buildUnmountPendingIntent(volumeInfo);
                    } else {
                        pendingIntent = buildInitPendingIntent(volumeInfo);
                    }
                    notification = buildNotificationBuilder(volumeInfo, string3, string4).setContentIntent(pendingIntent).setCategory("err").build();
                    break;
                case 7:
                    if (volumeInfo.isPrimary()) {
                        DiskInfo disk5 = volumeInfo.getDisk();
                        build = buildNotificationBuilder(volumeInfo, this.mContext.getString(17040225, disk5.getDescription()), this.mContext.getString(17040224, disk5.getDescription())).setCategory("err").build();
                        notification = build;
                        break;
                    }
                    break;
                case 8:
                    if (volumeInfo.isPrimary()) {
                        DiskInfo disk6 = volumeInfo.getDisk();
                        build = buildNotificationBuilder(volumeInfo, this.mContext.getString(17040209, disk6.getDescription()), this.mContext.getString(17040208, disk6.getDescription())).setCategory("err").build();
                        notification = build;
                        break;
                    }
                    break;
            }
            if (notification != null) {
                this.mNotificationManager.notifyAsUser(volumeInfo.getId(), 1397773634, notification, UserHandle.of(volumeInfo.getMountUserId()));
            } else {
                this.mNotificationManager.cancelAsUser(volumeInfo.getId(), 1397773634, UserHandle.of(volumeInfo.getMountUserId()));
            }
        } else if (type == 1) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Notifying about private volume: ");
            m2.append(volumeInfo.toString());
            Log.d("StorageNotification", m2.toString());
            updateMissingPrivateVolumes();
        }
    }
}
