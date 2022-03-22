package com.google.android.systemui.backup;

import android.app.backup.BlobBackupHelper;
import android.content.ContentResolver;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.backup.BackupHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BackupHelperGoogle.kt */
/* loaded from: classes.dex */
public final class BackupHelperGoogle extends BackupHelper {
    public static final List<String> SECURE_SETTINGS_INT_KEYS = SetsKt__SetsKt.listOf("columbus_enabled", "columbus_low_sensitivity");
    public static final List<String> SECURE_SETTINGS_STRING_KEYS = SetsKt__SetsKt.listOf("columbus_action", "columbus_launch_app", "columbus_launch_app_shortcut");

    /* compiled from: BackupHelperGoogle.kt */
    /* loaded from: classes.dex */
    public static final class SecureSettingsBackupHelper extends BlobBackupHelper {
        public final ContentResolver contentResolver;
        public final UserHandle userHandle;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public SecureSettingsBackupHelper(android.content.ContentResolver r7, android.os.UserHandle r8) {
            /*
                r6 = this;
                java.util.ArrayList r0 = new java.util.ArrayList
                r1 = 2
                r0.<init>(r1)
                java.util.List<java.lang.String> r1 = com.google.android.systemui.backup.BackupHelperGoogle.SECURE_SETTINGS_INT_KEYS
                r2 = 0
                java.lang.String[] r3 = new java.lang.String[r2]
                java.lang.Object[] r1 = r1.toArray(r3)
                java.lang.String r3 = "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>"
                java.util.Objects.requireNonNull(r1, r3)
                int r4 = r1.length
                if (r4 <= 0) goto L_0x0023
                int r4 = r0.size()
                int r5 = r1.length
                int r4 = r4 + r5
                r0.ensureCapacity(r4)
                java.util.Collections.addAll(r0, r1)
            L_0x0023:
                java.util.List<java.lang.String> r1 = com.google.android.systemui.backup.BackupHelperGoogle.SECURE_SETTINGS_STRING_KEYS
                java.lang.String[] r2 = new java.lang.String[r2]
                java.lang.Object[] r1 = r1.toArray(r2)
                java.util.Objects.requireNonNull(r1, r3)
                int r2 = r1.length
                if (r2 <= 0) goto L_0x003d
                int r2 = r0.size()
                int r3 = r1.length
                int r2 = r2 + r3
                r0.ensureCapacity(r2)
                java.util.Collections.addAll(r0, r1)
            L_0x003d:
                int r1 = r0.size()
                java.lang.String[] r1 = new java.lang.String[r1]
                java.lang.Object[] r0 = r0.toArray(r1)
                java.lang.String[] r0 = (java.lang.String[]) r0
                r1 = 1
                r6.<init>(r1, r0)
                r6.contentResolver = r7
                r6.userHandle = r8
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.backup.BackupHelperGoogle.SecureSettingsBackupHelper.<init>(android.content.ContentResolver, android.os.UserHandle):void");
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final void applyRestoredPayload(String str, byte[] bArr) {
            boolean z;
            boolean z2;
            boolean z3 = false;
            if (CollectionsKt___CollectionsKt.contains(BackupHelperGoogle.SECURE_SETTINGS_INT_KEYS, str)) {
                if (str != null && bArr != null) {
                    if (str.length() == 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2) {
                        if (bArr.length == 0) {
                            z3 = true;
                        }
                        if (!z3) {
                            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
                            try {
                                int readInt = dataInputStream.readInt();
                                dataInputStream.close();
                                ContentResolver contentResolver = this.contentResolver;
                                Settings.Secure.putIntForUser(contentResolver, str, readInt, this.userHandle.getIdentifier());
                                dataInputStream = contentResolver;
                            } catch (IOException unused) {
                                Log.e("BackupHelper", Intrinsics.stringPlus("Failed to restore ", str));
                            } finally {
                            }
                        }
                    }
                }
            } else if (CollectionsKt___CollectionsKt.contains(BackupHelperGoogle.SECURE_SETTINGS_STRING_KEYS, str) && str != null && bArr != null) {
                if (str.length() == 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    if (bArr.length == 0) {
                        z3 = true;
                    }
                    if (!z3) {
                        DataInputStream dataInputStream2 = new DataInputStream(new ByteArrayInputStream(bArr));
                        try {
                            String readUTF = dataInputStream2.readUTF();
                            dataInputStream2.close();
                            Settings.Secure.putStringForUser(this.contentResolver, str, readUTF, this.userHandle.getIdentifier());
                        } catch (IOException unused2) {
                            Log.e("BackupHelper", Intrinsics.stringPlus("Failed to restore ", str));
                        } finally {
                        }
                    }
                }
            }
        }

        /* JADX WARN: Finally extract failed */
        public final byte[] getBackupPayload(String str) {
            DataOutputStream dataOutputStream;
            String stringForUser;
            byte[] bArr = null;
            if (CollectionsKt___CollectionsKt.contains(BackupHelperGoogle.SECURE_SETTINGS_INT_KEYS, str)) {
                try {
                    int intForUser = Settings.Secure.getIntForUser(this.contentResolver, str, this.userHandle.getIdentifier());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                    try {
                        try {
                            dataOutputStream.writeInt(intForUser);
                            bArr = byteArrayOutputStream.toByteArray();
                        } catch (IOException unused) {
                            dataOutputStream.close();
                            Log.e("BackupHelper", Intrinsics.stringPlus("Failed to backup ", str));
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                } catch (Settings.SettingNotFoundException unused2) {
                }
            } else if (CollectionsKt___CollectionsKt.contains(BackupHelperGoogle.SECURE_SETTINGS_STRING_KEYS, str) && (stringForUser = Settings.Secure.getStringForUser(this.contentResolver, str, this.userHandle.getIdentifier())) != null) {
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                dataOutputStream = new DataOutputStream(byteArrayOutputStream2);
                try {
                    try {
                        dataOutputStream.writeUTF(stringForUser);
                        bArr = byteArrayOutputStream2.toByteArray();
                    } catch (IOException unused3) {
                        Log.e("BackupHelper", Intrinsics.stringPlus("Failed to backup ", str));
                    }
                } finally {
                    dataOutputStream.close();
                }
            }
            return bArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [android.app.backup.BackupHelper, com.google.android.systemui.backup.BackupHelperGoogle$SecureSettingsBackupHelper] */
    @Override // com.android.systemui.backup.BackupHelper
    public final void onCreate(UserHandle userHandle, int i) {
        super.onCreate(userHandle, i);
        addHelper("systemui.google.secure_settings_backup", new SecureSettingsBackupHelper(getContentResolver(), userHandle));
    }
}
