package com.android.systemui.people.widget;

import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.app.people.IPeopleManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class PeopleBackupHelper extends SharedPreferencesBackupHelper {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AppWidgetManager mAppWidgetManager;
    public final Context mContext;
    public final IPeopleManager mIPeopleManager;
    public final PackageManager mPackageManager;
    public final UserHandle mUserHandle;

    /* loaded from: classes.dex */
    public enum SharedFileEntryType {
        UNKNOWN,
        WIDGET_ID,
        PEOPLE_TILE_KEY,
        CONTACT_URI
    }

    public PeopleBackupHelper(Context context, UserHandle userHandle, String[] strArr) {
        super(context, strArr);
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mPackageManager = context.getPackageManager();
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    public static SharedFileEntryType getEntryType(Map.Entry<String, ?> entry) {
        SharedFileEntryType sharedFileEntryType = SharedFileEntryType.UNKNOWN;
        String key = entry.getKey();
        if (key == null) {
            return sharedFileEntryType;
        }
        try {
            Integer.parseInt(key);
            try {
                try {
                    String str = (String) entry.getValue();
                    return SharedFileEntryType.WIDGET_ID;
                } catch (Exception unused) {
                    Log.w("PeopleBackupHelper", "Malformed value, skipping:" + entry.getValue());
                    return sharedFileEntryType;
                }
            } catch (Exception unused2) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Malformed value, skipping:");
                m.append(entry.getValue());
                Log.w("PeopleBackupHelper", m.toString());
                return sharedFileEntryType;
            }
        } catch (NumberFormatException unused3) {
            Set set = (Set) entry.getValue();
            if (PeopleTileKey.fromString(key) != null) {
                return SharedFileEntryType.PEOPLE_TILE_KEY;
            }
            try {
                Uri.parse(key);
                return SharedFileEntryType.CONTACT_URI;
            } catch (Exception unused4) {
                return sharedFileEntryType;
            }
        }
    }

    @Override // android.app.backup.SharedPreferencesBackupHelper, android.app.backup.BackupHelper
    public final void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!defaultSharedPreferences.getAll().isEmpty()) {
            final SharedPreferences.Editor edit = this.mContext.getSharedPreferences("shared_backup", 0).edit();
            edit.clear();
            int identifier = this.mUserHandle.getIdentifier();
            final ArrayList arrayList = new ArrayList();
            for (int i : this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class))) {
                String valueOf = String.valueOf(i);
                if (this.mContext.getSharedPreferences(valueOf, 0).getInt("user_id", -1) == identifier) {
                    arrayList.add(valueOf);
                }
            }
            if (!arrayList.isEmpty()) {
                defaultSharedPreferences.getAll().entrySet().forEach(new Consumer() { // from class: com.android.systemui.people.widget.PeopleBackupHelper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PeopleBackupHelper peopleBackupHelper = PeopleBackupHelper.this;
                        SharedPreferences.Editor editor = edit;
                        List list = arrayList;
                        Map.Entry entry = (Map.Entry) obj;
                        Objects.requireNonNull(peopleBackupHelper);
                        String str = (String) entry.getKey();
                        if (!TextUtils.isEmpty(str)) {
                            int ordinal = PeopleBackupHelper.getEntryType(entry).ordinal();
                            if (ordinal == 1) {
                                String valueOf2 = String.valueOf(entry.getValue());
                                if (list.contains(str)) {
                                    Uri parse = Uri.parse(valueOf2);
                                    if (ContentProvider.uriHasUserId(parse)) {
                                        int userIdFromUri = ContentProvider.getUserIdFromUri(parse);
                                        editor.putInt("add_user_id_to_uri_" + str, userIdFromUri);
                                        parse = ContentProvider.getUriWithoutUserId(parse);
                                    }
                                    editor.putString(str, parse.toString());
                                }
                            } else if (ordinal == 2) {
                                Set set = (Set) entry.getValue();
                                PeopleTileKey fromString = PeopleTileKey.fromString(str);
                                Objects.requireNonNull(fromString);
                                if (fromString.mUserId == peopleBackupHelper.mUserHandle.getIdentifier()) {
                                    Set<String> set2 = (Set) set.stream().filter(new PeopleBackupHelper$$ExternalSyntheticLambda1(list, 0)).collect(Collectors.toSet());
                                    if (!set2.isEmpty()) {
                                        fromString.mUserId = -1;
                                        editor.putStringSet(fromString.toString(), set2);
                                    }
                                }
                            } else if (ordinal != 3) {
                                MotionLayout$$ExternalSyntheticOutline0.m("Key not identified, skipping: ", str, "PeopleBackupHelper");
                            } else {
                                Set<String> set3 = (Set) entry.getValue();
                                Uri parse2 = Uri.parse(String.valueOf(str));
                                if (ContentProvider.uriHasUserId(parse2)) {
                                    int userIdFromUri2 = ContentProvider.getUserIdFromUri(parse2);
                                    if (userIdFromUri2 == peopleBackupHelper.mUserHandle.getIdentifier()) {
                                        Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(parse2);
                                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("add_user_id_to_uri_");
                                        m.append(uriWithoutUserId.toString());
                                        editor.putInt(m.toString(), userIdFromUri2);
                                        editor.putStringSet(uriWithoutUserId.toString(), set3);
                                    }
                                } else if (peopleBackupHelper.mUserHandle.isSystem()) {
                                    editor.putStringSet(parse2.toString(), set3);
                                }
                            }
                        }
                    }
                });
                edit.apply();
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
            }
        }
    }

    public static boolean isReadyForRestore(IPeopleManager iPeopleManager, PackageManager packageManager, PeopleTileKey peopleTileKey) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            return true;
        }
        try {
            Objects.requireNonNull(peopleTileKey);
            packageManager.getPackageInfoAsUser(peopleTileKey.mPackageName, 0, peopleTileKey.mUserId);
            return iPeopleManager.isConversation(peopleTileKey.mPackageName, peopleTileKey.mUserId, peopleTileKey.mShortcutId);
        } catch (PackageManager.NameNotFoundException | Exception unused) {
            return false;
        }
    }

    public static void updateWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PeopleSpaceWidgetProvider.class));
        if (appWidgetIds != null && appWidgetIds.length != 0) {
            Intent intent = new Intent(context, PeopleSpaceWidgetProvider.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra("appWidgetIds", appWidgetIds);
            context.sendBroadcast(intent);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x010c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0031 A[SYNTHETIC] */
    @Override // android.app.backup.SharedPreferencesBackupHelper, android.app.backup.BackupHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void restoreEntity(android.app.backup.BackupDataInputStream r13) {
        /*
            Method dump skipped, instructions count: 354
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleBackupHelper.restoreEntity(android.app.backup.BackupDataInputStream):void");
    }

    @VisibleForTesting
    public PeopleBackupHelper(Context context, UserHandle userHandle, String[] strArr, PackageManager packageManager, IPeopleManager iPeopleManager) {
        super(context, strArr);
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mPackageManager = packageManager;
        this.mIPeopleManager = iPeopleManager;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }
}
