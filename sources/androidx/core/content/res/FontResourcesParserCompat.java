package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Base64;
import android.util.Xml;
import androidx.core.provider.FontRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okio.Okio__OkioKt;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class FontResourcesParserCompat {

    /* loaded from: classes.dex */
    public interface FamilyResourceEntry {
    }

    public static void skip(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        int i = 1;
        while (i > 0) {
            int next = xmlResourceParser.next();
            if (next == 2) {
                i++;
            } else if (next == 3) {
                i--;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry {
        public final FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(FontFileResourceEntry[] fontFileResourceEntryArr) {
            this.mEntries = fontFileResourceEntryArr;
        }
    }

    /* loaded from: classes.dex */
    public static final class FontFileResourceEntry {
        public final String mFileName;
        public boolean mItalic;
        public int mResourceId;
        public int mTtcIndex;
        public String mVariationSettings;
        public int mWeight;

        public FontFileResourceEntry(String str, int i, boolean z, String str2, int i2, int i3) {
            this.mFileName = str;
            this.mWeight = i;
            this.mItalic = z;
            this.mVariationSettings = str2;
            this.mTtcIndex = i2;
            this.mResourceId = i3;
        }
    }

    /* loaded from: classes.dex */
    public static final class ProviderResourceEntry implements FamilyResourceEntry {
        public final FontRequest mRequest;
        public final int mStrategy;
        public final String mSystemFontFamilyName;
        public final int mTimeoutMs;

        public ProviderResourceEntry(FontRequest fontRequest, int i, int i2, String str) {
            this.mRequest = fontRequest;
            this.mStrategy = i;
            this.mTimeoutMs = i2;
            this.mSystemFontFamilyName = str;
        }
    }

    public static FamilyResourceEntry parse(XmlResourceParser xmlResourceParser, Resources resources) throws XmlPullParserException, IOException {
        int next;
        int i;
        boolean z;
        int i2;
        do {
            next = xmlResourceParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            xmlResourceParser.require(2, null, "font-family");
            if (xmlResourceParser.getName().equals("font-family")) {
                TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlResourceParser), Okio__OkioKt.FontFamily);
                String string = obtainAttributes.getString(0);
                String string2 = obtainAttributes.getString(4);
                String string3 = obtainAttributes.getString(5);
                int resourceId = obtainAttributes.getResourceId(1, 0);
                int integer = obtainAttributes.getInteger(2, 1);
                int integer2 = obtainAttributes.getInteger(3, 500);
                String string4 = obtainAttributes.getString(6);
                obtainAttributes.recycle();
                if (string == null || string2 == null || string3 == null) {
                    ArrayList arrayList = new ArrayList();
                    while (xmlResourceParser.next() != 3) {
                        if (xmlResourceParser.getEventType() == 2) {
                            if (xmlResourceParser.getName().equals("font")) {
                                TypedArray obtainAttributes2 = resources.obtainAttributes(Xml.asAttributeSet(xmlResourceParser), Okio__OkioKt.FontFamilyFont);
                                int i3 = 8;
                                if (!obtainAttributes2.hasValue(8)) {
                                    i3 = 1;
                                }
                                int i4 = obtainAttributes2.getInt(i3, 400);
                                if (obtainAttributes2.hasValue(6)) {
                                    i = 6;
                                } else {
                                    i = 2;
                                }
                                if (1 == obtainAttributes2.getInt(i, 0)) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                int i5 = 9;
                                if (!obtainAttributes2.hasValue(9)) {
                                    i5 = 3;
                                }
                                int i6 = 7;
                                if (!obtainAttributes2.hasValue(7)) {
                                    i6 = 4;
                                }
                                String string5 = obtainAttributes2.getString(i6);
                                int i7 = obtainAttributes2.getInt(i5, 0);
                                if (obtainAttributes2.hasValue(5)) {
                                    i2 = 5;
                                } else {
                                    i2 = 0;
                                }
                                int resourceId2 = obtainAttributes2.getResourceId(i2, 0);
                                String string6 = obtainAttributes2.getString(i2);
                                obtainAttributes2.recycle();
                                while (xmlResourceParser.next() != 3) {
                                    skip(xmlResourceParser);
                                }
                                arrayList.add(new FontFileResourceEntry(string6, i4, z, string5, i7, resourceId2));
                            } else {
                                skip(xmlResourceParser);
                            }
                        }
                    }
                    if (!arrayList.isEmpty()) {
                        return new FontFamilyFilesResourceEntry((FontFileResourceEntry[]) arrayList.toArray(new FontFileResourceEntry[arrayList.size()]));
                    }
                } else {
                    while (xmlResourceParser.next() != 3) {
                        skip(xmlResourceParser);
                    }
                    return new ProviderResourceEntry(new FontRequest(string, string2, string3, readCerts(resources, resourceId)), integer, integer2, string4);
                }
            } else {
                skip(xmlResourceParser);
            }
            return null;
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static List<List<byte[]>> readCerts(Resources resources, int i) {
        if (i == 0) {
            return Collections.emptyList();
        }
        TypedArray obtainTypedArray = resources.obtainTypedArray(i);
        try {
            if (obtainTypedArray.length() == 0) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            if (obtainTypedArray.getType(0) == 1) {
                for (int i2 = 0; i2 < obtainTypedArray.length(); i2++) {
                    int resourceId = obtainTypedArray.getResourceId(i2, 0);
                    if (resourceId != 0) {
                        String[] stringArray = resources.getStringArray(resourceId);
                        ArrayList arrayList2 = new ArrayList();
                        for (String str : stringArray) {
                            arrayList2.add(Base64.decode(str, 0));
                        }
                        arrayList.add(arrayList2);
                    }
                }
            } else {
                String[] stringArray2 = resources.getStringArray(i);
                ArrayList arrayList3 = new ArrayList();
                for (String str2 : stringArray2) {
                    arrayList3.add(Base64.decode(str2, 0));
                }
                arrayList.add(arrayList3);
            }
            return arrayList;
        } finally {
            obtainTypedArray.recycle();
        }
    }
}
