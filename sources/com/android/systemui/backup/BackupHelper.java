package com.android.systemui.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.people.widget.PeopleBackupHelper;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* compiled from: BackupHelper.kt */
/* loaded from: classes.dex */
public class BackupHelper extends BackupAgentHelper {
    public static final Object controlsDataLock = new Object();

    /* compiled from: BackupHelper.kt */
    /* loaded from: classes.dex */
    public static final class NoOverwriteFileBackupHelper extends FileBackupHelper {
        public final Context context;
        public final Map<String, Function0<Unit>> fileNamesAndPostProcess;
        public final Object lock;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public NoOverwriteFileBackupHelper(android.content.Context r4, java.util.Map r5) {
            /*
                r3 = this;
                java.lang.Object r0 = com.android.systemui.backup.BackupHelper.controlsDataLock
                java.util.Set r1 = r5.keySet()
                r2 = 0
                java.lang.String[] r2 = new java.lang.String[r2]
                java.lang.Object[] r1 = r1.toArray(r2)
                java.lang.String r2 = "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>"
                java.util.Objects.requireNonNull(r1, r2)
                java.lang.String[] r1 = (java.lang.String[]) r1
                int r2 = r1.length
                java.lang.Object[] r1 = java.util.Arrays.copyOf(r1, r2)
                java.lang.String[] r1 = (java.lang.String[]) r1
                r3.<init>(r4, r1)
                r3.lock = r0
                r3.context = r4
                r3.fileNamesAndPostProcess = r5
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.backup.BackupHelper.NoOverwriteFileBackupHelper.<init>(android.content.Context, java.util.Map):void");
        }

        @Override // android.app.backup.FileBackupHelper, android.app.backup.BackupHelper
        public final void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
            synchronized (this.lock) {
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
            }
        }

        @Override // android.app.backup.FileBackupHelper, android.app.backup.BackupHelper
        public final void restoreEntity(BackupDataInputStream backupDataInputStream) {
            if (Environment.buildPath(this.context.getFilesDir(), new String[]{backupDataInputStream.getKey()}).exists()) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("File ");
                m.append((Object) backupDataInputStream.getKey());
                m.append(" already exists. Skipping restore.");
                Log.w("BackupHelper", m.toString());
                return;
            }
            synchronized (this.lock) {
                super.restoreEntity(backupDataInputStream);
                Function0<Unit> function0 = this.fileNamesAndPostProcess.get(backupDataInputStream.getKey());
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }
    }

    public void onCreate(UserHandle userHandle, int i) {
        super.onCreate();
        Pair pair = new Pair("controls_favorites.xml", new BackupHelperKt$getPPControlsFile$1(this));
        addHelper("systemui.files_no_overwrite", new NoOverwriteFileBackupHelper(this, Collections.singletonMap(pair.getFirst(), pair.getSecond())));
        if (userHandle.isSystem()) {
            int i2 = PeopleBackupHelper.$r8$clinit;
            Object[] array = Collections.singletonList("shared_backup").toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
            addHelper("systemui.people.shared_preferences", new PeopleBackupHelper(this, userHandle, (String[]) array));
        }
    }

    @Override // android.app.backup.BackupAgent
    public final void onRestoreFinished() {
        super.onRestoreFinished();
        Intent intent = new Intent("com.android.systemui.backup.RESTORE_FINISHED");
        intent.setPackage(getPackageName());
        intent.putExtra("android.intent.extra.USER_ID", getUserId());
        intent.setFlags(1073741824);
        sendBroadcastAsUser(intent, UserHandle.SYSTEM, "com.android.systemui.permission.SELF");
    }
}
