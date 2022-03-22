package com.android.wm.shell.bubbles.storage;

import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.XmlUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.v1.XmlPullParser;
/* compiled from: BubbleXmlHelper.kt */
/* loaded from: classes.dex */
public final class BubbleXmlHelperKt {
    public static final SparseArray readXml(FileInputStream fileInputStream) {
        Integer num;
        SparseArray sparseArray = new SparseArray();
        XmlPullParser newPullParser = Xml.newPullParser();
        newPullParser.setInput(fileInputStream, StandardCharsets.UTF_8.name());
        XmlUtils.beginDocument(newPullParser, "bs");
        int depth = newPullParser.getDepth();
        String attributeWithName = getAttributeWithName(newPullParser, "v");
        if (attributeWithName == null) {
            num = null;
        } else {
            num = Integer.valueOf(Integer.parseInt(attributeWithName));
        }
        if (num == null) {
            return sparseArray;
        }
        int intValue = num.intValue();
        if (intValue == 1) {
            int depth2 = newPullParser.getDepth();
            ArrayList arrayList = new ArrayList();
            while (XmlUtils.nextElementWithin(newPullParser, depth2)) {
                BubbleEntity readXmlEntry = readXmlEntry(newPullParser);
                if (readXmlEntry != null && readXmlEntry.userId == 0) {
                    arrayList.add(readXmlEntry);
                }
            }
            if (!arrayList.isEmpty()) {
                sparseArray.put(0, CollectionsKt___CollectionsKt.toList(arrayList));
            }
        } else if (intValue == 2) {
            while (XmlUtils.nextElementWithin(newPullParser, depth)) {
                String attributeWithName2 = getAttributeWithName(newPullParser, "uid");
                if (attributeWithName2 != null) {
                    int depth3 = newPullParser.getDepth();
                    ArrayList arrayList2 = new ArrayList();
                    while (XmlUtils.nextElementWithin(newPullParser, depth3)) {
                        BubbleEntity readXmlEntry2 = readXmlEntry(newPullParser);
                        if (readXmlEntry2 != null) {
                            arrayList2.add(readXmlEntry2);
                        }
                    }
                    if (!arrayList2.isEmpty()) {
                        sparseArray.put(Integer.parseInt(attributeWithName2), CollectionsKt___CollectionsKt.toList(arrayList2));
                    }
                }
            }
        }
        return sparseArray;
    }

    public static final void writeXml(FileOutputStream fileOutputStream, SparseArray sparseArray) throws IOException {
        FastXmlSerializer fastXmlSerializer = new FastXmlSerializer();
        fastXmlSerializer.setOutput(fileOutputStream, StandardCharsets.UTF_8.name());
        fastXmlSerializer.startDocument((String) null, Boolean.TRUE);
        fastXmlSerializer.startTag((String) null, "bs");
        fastXmlSerializer.attribute((String) null, "v", "2");
        int size = sparseArray.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            int keyAt = sparseArray.keyAt(i);
            fastXmlSerializer.startTag((String) null, "bs");
            fastXmlSerializer.attribute((String) null, "uid", String.valueOf(keyAt));
            for (BubbleEntity bubbleEntity : (List) sparseArray.valueAt(i)) {
                try {
                    fastXmlSerializer.startTag((String) null, "bb");
                    Objects.requireNonNull(bubbleEntity);
                    fastXmlSerializer.attribute((String) null, "uid", String.valueOf(bubbleEntity.userId));
                    fastXmlSerializer.attribute((String) null, "pkg", bubbleEntity.packageName);
                    fastXmlSerializer.attribute((String) null, "sid", bubbleEntity.shortcutId);
                    fastXmlSerializer.attribute((String) null, "key", bubbleEntity.key);
                    fastXmlSerializer.attribute((String) null, "h", String.valueOf(bubbleEntity.desiredHeight));
                    fastXmlSerializer.attribute((String) null, "hid", String.valueOf(bubbleEntity.desiredHeightResId));
                    String str = bubbleEntity.title;
                    if (str != null) {
                        fastXmlSerializer.attribute((String) null, "t", str);
                    }
                    fastXmlSerializer.attribute((String) null, "tid", String.valueOf(bubbleEntity.taskId));
                    String str2 = bubbleEntity.locus;
                    if (str2 != null) {
                        fastXmlSerializer.attribute((String) null, "l", str2);
                    }
                    fastXmlSerializer.endTag((String) null, "bb");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            fastXmlSerializer.endTag((String) null, "bs");
            i = i2;
        }
        fastXmlSerializer.endTag((String) null, "bs");
        fastXmlSerializer.endDocument();
    }

    public static final String getAttributeWithName(XmlPullParser xmlPullParser, String str) {
        int attributeCount = xmlPullParser.getAttributeCount();
        int i = 0;
        while (i < attributeCount) {
            int i2 = i + 1;
            if (Intrinsics.areEqual(xmlPullParser.getAttributeName(i), str)) {
                return xmlPullParser.getAttributeValue(i);
            }
            i = i2;
        }
        return null;
    }

    public static final BubbleEntity readXmlEntry(XmlPullParser xmlPullParser) {
        Integer num;
        String attributeWithName;
        String attributeWithName2;
        Integer num2;
        Integer num3;
        int i;
        while (xmlPullParser.getEventType() != 2) {
            xmlPullParser.next();
        }
        String attributeWithName3 = getAttributeWithName(xmlPullParser, "uid");
        if (attributeWithName3 == null) {
            num = null;
        } else {
            num = Integer.valueOf(Integer.parseInt(attributeWithName3));
        }
        if (num == null) {
            return null;
        }
        int intValue = num.intValue();
        String attributeWithName4 = getAttributeWithName(xmlPullParser, "pkg");
        if (attributeWithName4 == null || (attributeWithName = getAttributeWithName(xmlPullParser, "sid")) == null || (attributeWithName2 = getAttributeWithName(xmlPullParser, "key")) == null) {
            return null;
        }
        String attributeWithName5 = getAttributeWithName(xmlPullParser, "h");
        if (attributeWithName5 == null) {
            num2 = null;
        } else {
            num2 = Integer.valueOf(Integer.parseInt(attributeWithName5));
        }
        if (num2 == null) {
            return null;
        }
        int intValue2 = num2.intValue();
        String attributeWithName6 = getAttributeWithName(xmlPullParser, "hid");
        if (attributeWithName6 == null) {
            num3 = null;
        } else {
            num3 = Integer.valueOf(Integer.parseInt(attributeWithName6));
        }
        if (num3 == null) {
            return null;
        }
        int intValue3 = num3.intValue();
        String attributeWithName7 = getAttributeWithName(xmlPullParser, "t");
        String attributeWithName8 = getAttributeWithName(xmlPullParser, "tid");
        if (attributeWithName8 == null) {
            i = -1;
        } else {
            i = Integer.parseInt(attributeWithName8);
        }
        return new BubbleEntity(intValue, attributeWithName4, attributeWithName, attributeWithName2, intValue2, intValue3, attributeWithName7, i, getAttributeWithName(xmlPullParser, "l"));
    }
}
