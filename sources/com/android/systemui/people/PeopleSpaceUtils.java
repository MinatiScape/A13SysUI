package com.android.systemui.people;

import android.app.backup.BackupManager;
import android.app.people.IPeopleManager;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public final class PeopleSpaceUtils {
    public static final PeopleTileKey EMPTY_KEY = new PeopleTileKey("", -1, "");

    /* loaded from: classes.dex */
    public enum NotificationAction {
        POSTED,
        REMOVED
    }

    /* loaded from: classes.dex */
    public enum PeopleSpaceWidgetEvent implements UiEventLogger.UiEventEnum {
        PEOPLE_SPACE_WIDGET_DELETED(666),
        PEOPLE_SPACE_WIDGET_ADDED(667),
        PEOPLE_SPACE_WIDGET_CLICKED(668);
        
        private final int mId;

        PeopleSpaceWidgetEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x006d, code lost:
        if (r1 == null) goto L_0x0070;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0070, code lost:
        return r0;
     */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<java.lang.String> getContactLookupKeysWithBirthdaysToday(android.content.Context r11) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = 1
            r0.<init>(r1)
            java.text.SimpleDateFormat r2 = new java.text.SimpleDateFormat
            java.lang.String r3 = "MM-dd"
            r2.<init>(r3)
            java.util.Date r3 = new java.util.Date
            r3.<init>()
            java.lang.String r2 = r2.format(r3)
            java.lang.String r3 = "lookup"
            java.lang.String r4 = "data1"
            java.lang.String[] r7 = new java.lang.String[]{r3, r4}
            java.lang.String r8 = "mimetype= ? AND data2=3 AND (substr(data1,6) = ? OR substr(data1,3) = ? )"
            r4 = 3
            java.lang.String[] r9 = new java.lang.String[r4]
            r4 = 0
            java.lang.String r5 = "vnd.android.cursor.item/contact_event"
            r9[r4] = r5
            r9[r1] = r2
            r1 = 2
            r9[r1] = r2
            r1 = 0
            android.content.ContentResolver r5 = r11.getContentResolver()     // Catch: all -> 0x0054, SQLException -> 0x0056
            android.net.Uri r6 = android.provider.ContactsContract.Data.CONTENT_URI     // Catch: all -> 0x0054, SQLException -> 0x0056
            r10 = 0
            android.database.Cursor r1 = r5.query(r6, r7, r8, r9, r10)     // Catch: all -> 0x0054, SQLException -> 0x0056
        L_0x003a:
            if (r1 == 0) goto L_0x004e
            boolean r11 = r1.moveToNext()     // Catch: all -> 0x0054, SQLException -> 0x0056
            if (r11 == 0) goto L_0x004e
            int r11 = r1.getColumnIndex(r3)     // Catch: all -> 0x0054, SQLException -> 0x0056
            java.lang.String r11 = r1.getString(r11)     // Catch: all -> 0x0054, SQLException -> 0x0056
            r0.add(r11)     // Catch: all -> 0x0054, SQLException -> 0x0056
            goto L_0x003a
        L_0x004e:
            if (r1 == 0) goto L_0x0070
        L_0x0050:
            r1.close()
            goto L_0x0070
        L_0x0054:
            r11 = move-exception
            goto L_0x0071
        L_0x0056:
            r11 = move-exception
            java.lang.String r2 = "PeopleSpaceUtils"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: all -> 0x0054
            r3.<init>()     // Catch: all -> 0x0054
            java.lang.String r4 = "Failed to query birthdays: "
            r3.append(r4)     // Catch: all -> 0x0054
            r3.append(r11)     // Catch: all -> 0x0054
            java.lang.String r11 = r3.toString()     // Catch: all -> 0x0054
            android.util.Log.e(r2, r11)     // Catch: all -> 0x0054
            if (r1 == 0) goto L_0x0070
            goto L_0x0050
        L_0x0070:
            return r0
        L_0x0071:
            if (r1 == 0) goto L_0x0076
            r1.close()
        L_0x0076:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.PeopleSpaceUtils.getContactLookupKeysWithBirthdaysToday(android.content.Context):java.util.List");
    }

    @VisibleForTesting
    public static void getDataFromContacts(Context context, PeopleSpaceWidgetManager peopleSpaceWidgetManager, Map<Integer, PeopleSpaceTile> map, int[] iArr) {
        Throwable th;
        Cursor cursor;
        SQLException e;
        float f;
        boolean z;
        if (iArr.length != 0) {
            List<String> contactLookupKeysWithBirthdaysToday = getContactLookupKeysWithBirthdaysToday(context);
            for (int i : iArr) {
                PeopleSpaceTile peopleSpaceTile = map.get(Integer.valueOf(i));
                if (peopleSpaceTile == null || peopleSpaceTile.getContactUri() == null) {
                    updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, 0.0f, null);
                } else {
                    Cursor cursor2 = null;
                    try {
                        try {
                            cursor = context.getContentResolver().query(peopleSpaceTile.getContactUri(), null, null, null, null);
                            while (cursor != null) {
                                try {
                                    try {
                                        if (!cursor.moveToNext()) {
                                            break;
                                        }
                                        String string = cursor.getString(cursor.getColumnIndex("lookup"));
                                        int columnIndex = cursor.getColumnIndex("starred");
                                        if (columnIndex >= 0) {
                                            if (cursor.getInt(columnIndex) != 0) {
                                                z = true;
                                            } else {
                                                z = false;
                                            }
                                            if (z) {
                                                f = Math.max(0.5f, 1.0f);
                                                if (!string.isEmpty() || !contactLookupKeysWithBirthdaysToday.contains(string)) {
                                                    updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, f, null);
                                                } else {
                                                    try {
                                                        updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, f, context.getString(2131951955));
                                                    } catch (SQLException e2) {
                                                        e = e2;
                                                        cursor2 = cursor;
                                                        Log.e("PeopleSpaceUtils", "Failed to query contact: " + e);
                                                        if (cursor2 != null) {
                                                            cursor = cursor2;
                                                            cursor.close();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        f = 0.5f;
                                        if (!string.isEmpty()) {
                                        }
                                        updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, f, null);
                                    } catch (SQLException e3) {
                                        e = e3;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    cursor2 = cursor;
                                    if (cursor2 != null) {
                                        cursor2.close();
                                    }
                                    throw th;
                                }
                            }
                            if (cursor == null) {
                            }
                        } catch (SQLException e4) {
                            e = e4;
                        }
                        cursor.close();
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
            }
        }
    }

    public static List<PeopleSpaceTile> getSortedTiles(IPeopleManager iPeopleManager, final LauncherApps launcherApps, final UserManager userManager, Stream<ShortcutInfo> stream) {
        return (List) stream.filter(PeopleSpaceUtils$$ExternalSyntheticLambda7.INSTANCE).filter(new Predicate() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return !userManager.isQuietModeEnabled(((ShortcutInfo) obj).getUserHandle());
            }
        }).map(new Function() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return new PeopleSpaceTile.Builder((ShortcutInfo) obj, launcherApps).build();
            }
        }).filter(PeopleSpaceUtils$$ExternalSyntheticLambda8.INSTANCE).map(new PeopleSpaceUtils$$ExternalSyntheticLambda2(iPeopleManager, 0)).sorted(PeopleSpaceUtils$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
    }

    public static PeopleSpaceTile removeNotificationFields(PeopleSpaceTile peopleSpaceTile) {
        PeopleSpaceTile.Builder notificationCategory = peopleSpaceTile.toBuilder().setNotificationKey((String) null).setNotificationContent((CharSequence) null).setNotificationSender((CharSequence) null).setNotificationDataUri((Uri) null).setMessagesCount(0).setNotificationCategory((String) null);
        if (!TextUtils.isEmpty(peopleSpaceTile.getNotificationKey())) {
            notificationCategory.setLastInteractionTimestamp(System.currentTimeMillis());
        }
        return notificationCategory.build();
    }

    public static void removeSharedPreferencesStorageForTile(Context context, PeopleTileKey peopleTileKey, int i, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(context), 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(String.valueOf(i));
        String peopleTileKey2 = peopleTileKey.toString();
        HashSet hashSet = new HashSet(sharedPreferences.getStringSet(peopleTileKey2, new HashSet()));
        hashSet.remove(String.valueOf(i));
        edit.putStringSet(peopleTileKey2, hashSet);
        HashSet hashSet2 = new HashSet(sharedPreferences.getStringSet(str, new HashSet()));
        hashSet2.remove(String.valueOf(i));
        edit.putStringSet(str, hashSet2);
        edit.apply();
        SharedPreferences.Editor edit2 = context.getSharedPreferences(String.valueOf(i), 0).edit();
        edit2.remove("package_name");
        edit2.remove("user_id");
        edit2.remove("shortcut_id");
        edit2.apply();
    }

    public static void setSharedPreferencesStorageForTile(Context context, PeopleTileKey peopleTileKey, int i, Uri uri, BackupManager backupManager) {
        String str;
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            Log.e("PeopleSpaceUtils", "Not storing for invalid key");
            return;
        }
        SharedPreferencesHelper.setPeopleTileKey(context.getSharedPreferences(String.valueOf(i), 0), peopleTileKey);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(context), 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (uri == null) {
            str = "";
        } else {
            str = uri.toString();
        }
        edit.putString(String.valueOf(i), str);
        String peopleTileKey2 = peopleTileKey.toString();
        HashSet hashSet = new HashSet(sharedPreferences.getStringSet(peopleTileKey2, new HashSet()));
        hashSet.add(String.valueOf(i));
        edit.putStringSet(peopleTileKey2, hashSet);
        if (!TextUtils.isEmpty(str)) {
            HashSet hashSet2 = new HashSet(sharedPreferences.getStringSet(str, new HashSet()));
            hashSet2.add(String.valueOf(i));
            edit.putStringSet(str, hashSet2);
        }
        edit.apply();
        backupManager.dataChanged();
    }

    public static void updateTileContactFields(PeopleSpaceWidgetManager peopleSpaceWidgetManager, Context context, PeopleSpaceTile peopleSpaceTile, int i, float f, String str) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5 = true;
        if (peopleSpaceTile.getBirthdayText() == null || !peopleSpaceTile.getBirthdayText().equals(context.getString(2131951955))) {
            z = false;
        } else {
            z = true;
        }
        if (!z || str != null) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (peopleSpaceTile.getBirthdayText() == null || !peopleSpaceTile.getBirthdayText().equals(context.getString(2131951955))) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (z3 || str == null) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (peopleSpaceTile.getContactAffinity() == f && !z2 && !z4) {
            z5 = false;
        }
        if (z5) {
            peopleSpaceWidgetManager.updateAppWidgetOptionsAndView(i, peopleSpaceTile.toBuilder().setBirthdayText(str).setContactAffinity(f).build());
        }
    }
}
