package androidx.core.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FontProvider {
    public static final AnonymousClass1 sByteArrayComparator = new Comparator<byte[]>() { // from class: androidx.core.provider.FontProvider.1
        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Comparator
        public final int compare(byte[] bArr, byte[] bArr2) {
            int i;
            int i2;
            byte[] bArr3 = bArr;
            byte[] bArr4 = bArr2;
            if (bArr3.length != bArr4.length) {
                i2 = bArr3.length;
                i = bArr4.length;
            } else {
                for (int i3 = 0; i3 < bArr3.length; i3++) {
                    if (bArr3[i3] != bArr4[i3]) {
                        i2 = bArr3[i3];
                        i = bArr4[i3];
                    }
                }
                return 0;
            }
            return i2 - i;
        }
    };

    public static FontsContractCompat$FontInfo[] query(Context context, FontRequest fontRequest, String str, CancellationSignal cancellationSignal) {
        int i;
        int i2;
        Uri uri;
        int i3;
        boolean z;
        ArrayList arrayList = new ArrayList();
        Uri build = new Uri.Builder().scheme("content").authority(str).build();
        Uri build2 = new Uri.Builder().scheme("content").authority(str).appendPath("file").build();
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Objects.requireNonNull(fontRequest);
            cursor = contentResolver.query(build, new String[]{"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"}, "query = ?", new String[]{fontRequest.mQuery}, null, cancellationSignal);
            if (cursor != null && cursor.getCount() > 0) {
                int columnIndex = cursor.getColumnIndex("result_code");
                ArrayList arrayList2 = new ArrayList();
                int columnIndex2 = cursor.getColumnIndex("_id");
                int columnIndex3 = cursor.getColumnIndex("file_id");
                int columnIndex4 = cursor.getColumnIndex("font_ttc_index");
                int columnIndex5 = cursor.getColumnIndex("font_weight");
                int columnIndex6 = cursor.getColumnIndex("font_italic");
                while (cursor.moveToNext()) {
                    if (columnIndex != -1) {
                        i = cursor.getInt(columnIndex);
                    } else {
                        i = 0;
                    }
                    if (columnIndex4 != -1) {
                        i2 = cursor.getInt(columnIndex4);
                    } else {
                        i2 = 0;
                    }
                    if (columnIndex3 == -1) {
                        uri = ContentUris.withAppendedId(build, cursor.getLong(columnIndex2));
                    } else {
                        uri = ContentUris.withAppendedId(build2, cursor.getLong(columnIndex3));
                    }
                    if (columnIndex5 != -1) {
                        i3 = cursor.getInt(columnIndex5);
                    } else {
                        i3 = 400;
                    }
                    if (columnIndex6 == -1 || cursor.getInt(columnIndex6) != 1) {
                        z = false;
                    } else {
                        z = true;
                    }
                    arrayList2.add(new FontsContractCompat$FontInfo(uri, i2, i3, z, i));
                }
                arrayList = arrayList2;
            }
            return (FontsContractCompat$FontInfo[]) arrayList.toArray(new FontsContractCompat$FontInfo[0]);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static FontsContractCompat$FontFamilyResult getFontFamilyResult(Context context, FontRequest fontRequest) throws PackageManager.NameNotFoundException {
        ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (provider == null) {
            return new FontsContractCompat$FontFamilyResult(1, null);
        }
        return new FontsContractCompat$FontFamilyResult(0, query(context, fontRequest, provider.authority, null));
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0087 A[LOOP:1: B:14:0x0044->B:28:0x0087, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0086 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static android.content.pm.ProviderInfo getProvider(android.content.pm.PackageManager r6, androidx.core.provider.FontRequest r7, android.content.res.Resources r8) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            java.util.Objects.requireNonNull(r7)
            java.lang.String r0 = r7.mProviderAuthority
            r1 = 0
            android.content.pm.ProviderInfo r2 = r6.resolveContentProvider(r0, r1)
            if (r2 == 0) goto L_0x00a3
            java.lang.String r3 = r2.packageName
            java.lang.String r4 = r7.mProviderPackage
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x008c
            java.lang.String r0 = r2.packageName
            r3 = 64
            android.content.pm.PackageInfo r6 = r6.getPackageInfo(r0, r3)
            android.content.pm.Signature[] r6 = r6.signatures
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r3 = r1
        L_0x0026:
            int r4 = r6.length
            if (r3 >= r4) goto L_0x0035
            r4 = r6[r3]
            byte[] r4 = r4.toByteArray()
            r0.add(r4)
            int r3 = r3 + 1
            goto L_0x0026
        L_0x0035:
            androidx.core.provider.FontProvider$1 r6 = androidx.core.provider.FontProvider.sByteArrayComparator
            java.util.Collections.sort(r0, r6)
            java.util.List<java.util.List<byte[]>> r6 = r7.mCertificates
            if (r6 == 0) goto L_0x003f
            goto L_0x0043
        L_0x003f:
            java.util.List r6 = androidx.core.content.res.FontResourcesParserCompat.readCerts(r8, r1)
        L_0x0043:
            r7 = r1
        L_0x0044:
            int r8 = r6.size()
            if (r7 >= r8) goto L_0x008a
            java.util.ArrayList r8 = new java.util.ArrayList
            java.lang.Object r3 = r6.get(r7)
            java.util.Collection r3 = (java.util.Collection) r3
            r8.<init>(r3)
            androidx.core.provider.FontProvider$1 r3 = androidx.core.provider.FontProvider.sByteArrayComparator
            java.util.Collections.sort(r8, r3)
            int r3 = r0.size()
            int r4 = r8.size()
            if (r3 == r4) goto L_0x0065
            goto L_0x007e
        L_0x0065:
            r3 = r1
        L_0x0066:
            int r4 = r0.size()
            if (r3 >= r4) goto L_0x0083
            java.lang.Object r4 = r0.get(r3)
            byte[] r4 = (byte[]) r4
            java.lang.Object r5 = r8.get(r3)
            byte[] r5 = (byte[]) r5
            boolean r4 = java.util.Arrays.equals(r4, r5)
            if (r4 != 0) goto L_0x0080
        L_0x007e:
            r8 = r1
            goto L_0x0084
        L_0x0080:
            int r3 = r3 + 1
            goto L_0x0066
        L_0x0083:
            r8 = 1
        L_0x0084:
            if (r8 == 0) goto L_0x0087
            return r2
        L_0x0087:
            int r7 = r7 + 1
            goto L_0x0044
        L_0x008a:
            r6 = 0
            return r6
        L_0x008c:
            android.content.pm.PackageManager$NameNotFoundException r6 = new android.content.pm.PackageManager$NameNotFoundException
            java.lang.String r8 = "Found content provider "
            java.lang.String r1 = ", but package was not "
            java.lang.StringBuilder r8 = androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0.m(r8, r0, r1)
            java.lang.String r7 = r7.mProviderPackage
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            r6.<init>(r7)
            throw r6
        L_0x00a3:
            android.content.pm.PackageManager$NameNotFoundException r6 = new android.content.pm.PackageManager$NameNotFoundException
            java.lang.String r7 = "No package found for authority: "
            java.lang.String r7 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r7, r0)
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.provider.FontProvider.getProvider(android.content.pm.PackageManager, androidx.core.provider.FontRequest, android.content.res.Resources):android.content.pm.ProviderInfo");
    }
}
