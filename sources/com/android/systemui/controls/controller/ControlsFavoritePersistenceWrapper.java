package com.android.systemui.controls.controller;

import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;
import androidx.core.graphics.drawable.IconCompat;
import com.android.systemui.backup.BackupHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
/* compiled from: ControlsFavoritePersistenceWrapper.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritePersistenceWrapper {
    public BackupManager backupManager;
    public final Executor executor;
    public File file;

    public static ArrayList parseXml(XmlPullParser xmlPullParser) {
        Integer num;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ComponentName componentName = null;
        String str = null;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return arrayList;
            }
            String name = xmlPullParser.getName();
            String str2 = "";
            if (name == null) {
                name = str2;
            }
            if (next == 2 && Intrinsics.areEqual(name, "structure")) {
                componentName = ComponentName.unflattenFromString(xmlPullParser.getAttributeValue(null, "component"));
                str = xmlPullParser.getAttributeValue(null, "structure");
                if (str == null) {
                    str = str2;
                }
            } else if (next == 2 && Intrinsics.areEqual(name, "control")) {
                String attributeValue = xmlPullParser.getAttributeValue(null, "id");
                String attributeValue2 = xmlPullParser.getAttributeValue(null, "title");
                String attributeValue3 = xmlPullParser.getAttributeValue(null, "subtitle");
                if (attributeValue3 != null) {
                    str2 = attributeValue3;
                }
                String attributeValue4 = xmlPullParser.getAttributeValue(null, IconCompat.EXTRA_TYPE);
                if (attributeValue4 == null) {
                    num = null;
                } else {
                    num = Integer.valueOf(Integer.parseInt(attributeValue4));
                }
                if (!(attributeValue == null || attributeValue2 == null || num == null)) {
                    arrayList2.add(new ControlInfo(attributeValue, attributeValue2, str2, num.intValue()));
                }
            } else if (next == 3 && Intrinsics.areEqual(name, "structure")) {
                Intrinsics.checkNotNull(componentName);
                Intrinsics.checkNotNull(str);
                arrayList.add(new StructureInfo(componentName, str, CollectionsKt___CollectionsKt.toList(arrayList2)));
                arrayList2.clear();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.io.BufferedInputStream, java.lang.AutoCloseable, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v6, types: [org.xmlpull.v1.XmlPullParser] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.android.systemui.controls.controller.StructureInfo> readFavorites() {
        /*
            r4 = this;
            java.io.File r0 = r4.file
            boolean r0 = r0.exists()
            if (r0 != 0) goto L_0x0012
            java.lang.String r4 = "ControlsFavoritePersistenceWrapper"
            java.lang.String r0 = "No favorites, returning empty list"
            android.util.Log.d(r4, r0)
            kotlin.collections.EmptyList r4 = kotlin.collections.EmptyList.INSTANCE
            return r4
        L_0x0012:
            java.io.BufferedInputStream r0 = new java.io.BufferedInputStream     // Catch: FileNotFoundException -> 0x0066
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: FileNotFoundException -> 0x0066
            java.io.File r2 = r4.file     // Catch: FileNotFoundException -> 0x0066
            r1.<init>(r2)     // Catch: FileNotFoundException -> 0x0066
            r0.<init>(r1)     // Catch: FileNotFoundException -> 0x0066
            java.lang.String r1 = "ControlsFavoritePersistenceWrapper"
            java.lang.String r2 = "Reading data from file: "
            java.io.File r3 = r4.file     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            java.lang.String r2 = kotlin.jvm.internal.Intrinsics.stringPlus(r2, r3)     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            android.util.Log.d(r1, r2)     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            java.lang.Object r1 = com.android.systemui.backup.BackupHelper.controlsDataLock     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            monitor-enter(r1)     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            org.xmlpull.v1.XmlPullParser r2 = android.util.Xml.newPullParser()     // Catch: all -> 0x003f
            r3 = 0
            r2.setInput(r0, r3)     // Catch: all -> 0x003f
            java.util.ArrayList r2 = parseXml(r2)     // Catch: all -> 0x003f
            monitor-exit(r1)     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            libcore.io.IoUtils.closeQuietly(r0)
            return r2
        L_0x003f:
            r2 = move-exception
            monitor-exit(r1)     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
            throw r2     // Catch: all -> 0x0042, IOException -> 0x0044, XmlPullParserException -> 0x0053
        L_0x0042:
            r4 = move-exception
            goto L_0x0062
        L_0x0044:
            r1 = move-exception
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: all -> 0x0042
            java.lang.String r3 = "Failed parsing favorites file: "
            java.io.File r4 = r4.file     // Catch: all -> 0x0042
            java.lang.String r4 = kotlin.jvm.internal.Intrinsics.stringPlus(r3, r4)     // Catch: all -> 0x0042
            r2.<init>(r4, r1)     // Catch: all -> 0x0042
            throw r2     // Catch: all -> 0x0042
        L_0x0053:
            r1 = move-exception
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: all -> 0x0042
            java.lang.String r3 = "Failed parsing favorites file: "
            java.io.File r4 = r4.file     // Catch: all -> 0x0042
            java.lang.String r4 = kotlin.jvm.internal.Intrinsics.stringPlus(r3, r4)     // Catch: all -> 0x0042
            r2.<init>(r4, r1)     // Catch: all -> 0x0042
            throw r2     // Catch: all -> 0x0042
        L_0x0062:
            libcore.io.IoUtils.closeQuietly(r0)
            throw r4
        L_0x0066:
            java.lang.String r4 = "ControlsFavoritePersistenceWrapper"
            java.lang.String r0 = "No file found"
            android.util.Log.i(r4, r0)
            kotlin.collections.EmptyList r4 = kotlin.collections.EmptyList.INSTANCE
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper.readFavorites():java.util.List");
    }

    public ControlsFavoritePersistenceWrapper(File file, Executor executor, BackupManager backupManager) {
        this.file = file;
        this.executor = executor;
        this.backupManager = backupManager;
    }

    public final void storeFavorites(final List<StructureInfo> list) {
        if (!list.isEmpty() || this.file.exists()) {
            this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper$storeFavorites$1
                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    BackupManager backupManager;
                    Log.d("ControlsFavoritePersistenceWrapper", Intrinsics.stringPlus("Saving data to file: ", ControlsFavoritePersistenceWrapper.this.file));
                    AtomicFile atomicFile = new AtomicFile(ControlsFavoritePersistenceWrapper.this.file);
                    Object obj = BackupHelper.controlsDataLock;
                    Object obj2 = BackupHelper.controlsDataLock;
                    List<StructureInfo> list2 = list;
                    synchronized (obj2) {
                        try {
                            try {
                                FileOutputStream startWrite = atomicFile.startWrite();
                                z = true;
                                try {
                                    XmlSerializer newSerializer = Xml.newSerializer();
                                    newSerializer.setOutput(startWrite, "utf-8");
                                    newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                                    newSerializer.startDocument(null, Boolean.TRUE);
                                    newSerializer.startTag(null, "version");
                                    newSerializer.text("1");
                                    newSerializer.endTag(null, "version");
                                    newSerializer.startTag(null, "structures");
                                    for (StructureInfo structureInfo : list2) {
                                        newSerializer.startTag(null, "structure");
                                        Objects.requireNonNull(structureInfo);
                                        newSerializer.attribute(null, "component", structureInfo.componentName.flattenToString());
                                        newSerializer.attribute(null, "structure", structureInfo.structure.toString());
                                        newSerializer.startTag(null, "controls");
                                        for (ControlInfo controlInfo : structureInfo.controls) {
                                            newSerializer.startTag(null, "control");
                                            Objects.requireNonNull(controlInfo);
                                            newSerializer.attribute(null, "id", controlInfo.controlId);
                                            newSerializer.attribute(null, "title", controlInfo.controlTitle.toString());
                                            newSerializer.attribute(null, "subtitle", controlInfo.controlSubtitle.toString());
                                            newSerializer.attribute(null, IconCompat.EXTRA_TYPE, String.valueOf(controlInfo.deviceType));
                                            newSerializer.endTag(null, "control");
                                        }
                                        newSerializer.endTag(null, "controls");
                                        newSerializer.endTag(null, "structure");
                                    }
                                    newSerializer.endTag(null, "structures");
                                    newSerializer.endDocument();
                                    atomicFile.finishWrite(startWrite);
                                } catch (Throwable unused) {
                                    Log.e("ControlsFavoritePersistenceWrapper", "Failed to write file, reverting to previous version");
                                    atomicFile.failWrite(startWrite);
                                    z = false;
                                }
                                IoUtils.closeQuietly(startWrite);
                            } catch (Throwable th) {
                                throw th;
                            }
                        } catch (IOException e) {
                            Log.e("ControlsFavoritePersistenceWrapper", "Failed to start write file", e);
                            return;
                        }
                    }
                    if (z && (backupManager = ControlsFavoritePersistenceWrapper.this.backupManager) != null) {
                        backupManager.dataChanged();
                    }
                }
            });
        }
    }
}
