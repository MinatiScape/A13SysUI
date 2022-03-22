package androidx.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.InflateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class PreferenceInflater {
    public final Context mContext;
    public PreferenceManager mPreferenceManager;
    public static final Class<?>[] CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    public static final HashMap<String, Constructor> CONSTRUCTOR_MAP = new HashMap<>();
    public final Object[] mConstructorArgs = new Object[2];
    public String[] mDefaultPackages = {Preference.class.getPackage().getName() + ".", SwitchPreference.class.getPackage().getName() + "."};

    public final Preference createItemFromTag(String str, AttributeSet attributeSet) {
        try {
            if (-1 == str.indexOf(46)) {
                return createItem(str, this.mDefaultPackages, attributeSet);
            }
            return createItem(str, null, attributeSet);
        } catch (InflateException e) {
            throw e;
        } catch (ClassNotFoundException e2) {
            InflateException inflateException = new InflateException(attributeSet.getPositionDescription() + ": Error inflating class (not found)" + str);
            inflateException.initCause(e2);
            throw inflateException;
        } catch (Exception e3) {
            InflateException inflateException2 = new InflateException(attributeSet.getPositionDescription() + ": Error inflating class " + str);
            inflateException2.initCause(e3);
            throw inflateException2;
        }
    }

    public final Preference createItem(String str, String[] strArr, AttributeSet attributeSet) throws ClassNotFoundException, InflateException {
        Class<?> cls;
        Constructor<?> constructor = CONSTRUCTOR_MAP.get(str);
        if (constructor == null) {
            try {
                try {
                    ClassLoader classLoader = this.mContext.getClassLoader();
                    if (!(strArr == null || strArr.length == 0)) {
                        cls = null;
                        ClassNotFoundException e = null;
                        for (String str2 : strArr) {
                            try {
                                cls = Class.forName(str2 + str, false, classLoader);
                                break;
                            } catch (ClassNotFoundException e2) {
                                e = e2;
                            }
                        }
                        if (cls == null) {
                            if (e == null) {
                                throw new InflateException(attributeSet.getPositionDescription() + ": Error inflating class " + str);
                            }
                            throw e;
                        }
                        constructor = cls.getConstructor(CONSTRUCTOR_SIGNATURE);
                        constructor.setAccessible(true);
                        CONSTRUCTOR_MAP.put(str, constructor);
                    }
                    cls = Class.forName(str, false, classLoader);
                    constructor = cls.getConstructor(CONSTRUCTOR_SIGNATURE);
                    constructor.setAccessible(true);
                    CONSTRUCTOR_MAP.put(str, constructor);
                } catch (ClassNotFoundException e3) {
                    throw e3;
                }
            } catch (Exception e4) {
                InflateException inflateException = new InflateException(attributeSet.getPositionDescription() + ": Error inflating class " + str);
                inflateException.initCause(e4);
                throw inflateException;
            }
        }
        Object[] objArr = this.mConstructorArgs;
        objArr[1] = attributeSet;
        return (Preference) constructor.newInstance(objArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4, types: [androidx.preference.PreferenceGroup, androidx.preference.Preference] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final androidx.preference.PreferenceGroup inflate(android.content.res.XmlResourceParser r6, androidx.preference.PreferenceScreen r7) {
        /*
            r5 = this;
            java.lang.Object[] r0 = r5.mConstructorArgs
            monitor-enter(r0)
            android.util.AttributeSet r1 = android.util.Xml.asAttributeSet(r6)     // Catch: all -> 0x0083
            java.lang.Object[] r2 = r5.mConstructorArgs     // Catch: all -> 0x0083
            r3 = 0
            android.content.Context r4 = r5.mContext     // Catch: all -> 0x0083
            r2[r3] = r4     // Catch: all -> 0x0083
        L_0x000e:
            int r2 = r6.next()     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            r3 = 2
            if (r2 == r3) goto L_0x0018
            r4 = 1
            if (r2 != r4) goto L_0x000e
        L_0x0018:
            if (r2 != r3) goto L_0x0033
            java.lang.String r2 = r6.getName()     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            androidx.preference.Preference r2 = r5.createItemFromTag(r2, r1)     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            androidx.preference.PreferenceGroup r2 = (androidx.preference.PreferenceGroup) r2     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            if (r7 != 0) goto L_0x002c
            androidx.preference.PreferenceManager r7 = r5.mPreferenceManager     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            r2.onAttachedToHierarchy(r7)     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            r7 = r2
        L_0x002c:
            r5.rInflate(r6, r7, r1)     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            monitor-exit(r0)     // Catch: all -> 0x0083
            return r7
        L_0x0031:
            r5 = move-exception
            goto L_0x004e
        L_0x0033:
            android.view.InflateException r5 = new android.view.InflateException     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            r7.<init>()     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            java.lang.String r1 = r6.getPositionDescription()     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            r7.append(r1)     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            java.lang.String r1 = ": No start tag found!"
            r7.append(r1)     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            java.lang.String r7 = r7.toString()     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            r5.<init>(r7)     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
            throw r5     // Catch: IOException -> 0x0031, XmlPullParserException -> 0x0073, InflateException -> 0x0081, all -> 0x0083
        L_0x004e:
            android.view.InflateException r7 = new android.view.InflateException     // Catch: all -> 0x0083
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: all -> 0x0083
            r1.<init>()     // Catch: all -> 0x0083
            java.lang.String r6 = r6.getPositionDescription()     // Catch: all -> 0x0083
            r1.append(r6)     // Catch: all -> 0x0083
            java.lang.String r6 = ": "
            r1.append(r6)     // Catch: all -> 0x0083
            java.lang.String r6 = r5.getMessage()     // Catch: all -> 0x0083
            r1.append(r6)     // Catch: all -> 0x0083
            java.lang.String r6 = r1.toString()     // Catch: all -> 0x0083
            r7.<init>(r6)     // Catch: all -> 0x0083
            r7.initCause(r5)     // Catch: all -> 0x0083
            throw r7     // Catch: all -> 0x0083
        L_0x0073:
            r5 = move-exception
            android.view.InflateException r6 = new android.view.InflateException     // Catch: all -> 0x0083
            java.lang.String r7 = r5.getMessage()     // Catch: all -> 0x0083
            r6.<init>(r7)     // Catch: all -> 0x0083
            r6.initCause(r5)     // Catch: all -> 0x0083
            throw r6     // Catch: all -> 0x0083
        L_0x0081:
            r5 = move-exception
            throw r5     // Catch: all -> 0x0083
        L_0x0083:
            r5 = move-exception
            monitor-exit(r0)     // Catch: all -> 0x0083
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.preference.PreferenceInflater.inflate(android.content.res.XmlResourceParser, androidx.preference.PreferenceScreen):androidx.preference.PreferenceGroup");
    }

    public PreferenceInflater(Context context, PreferenceManager preferenceManager) {
        this.mContext = context;
        this.mPreferenceManager = preferenceManager;
    }

    public final void rInflate(XmlResourceParser xmlResourceParser, Preference preference, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if ((next == 3 && xmlResourceParser.getDepth() <= depth) || next == 1) {
                return;
            }
            if (next == 2) {
                String name = xmlResourceParser.getName();
                if ("intent".equals(name)) {
                    try {
                        Intent parseIntent = Intent.parseIntent(this.mContext.getResources(), xmlResourceParser, attributeSet);
                        Objects.requireNonNull(preference);
                        preference.mIntent = parseIntent;
                    } catch (IOException e) {
                        XmlPullParserException xmlPullParserException = new XmlPullParserException("Error parsing preference");
                        xmlPullParserException.initCause(e);
                        throw xmlPullParserException;
                    }
                } else if ("extra".equals(name)) {
                    Resources resources = this.mContext.getResources();
                    Objects.requireNonNull(preference);
                    if (preference.mExtras == null) {
                        preference.mExtras = new Bundle();
                    }
                    resources.parseBundleExtra("extra", attributeSet, preference.mExtras);
                    try {
                        int depth2 = xmlResourceParser.getDepth();
                        while (true) {
                            int next2 = xmlResourceParser.next();
                            if (next2 != 1 && (next2 != 3 || xmlResourceParser.getDepth() > depth2)) {
                            }
                        }
                    } catch (IOException e2) {
                        XmlPullParserException xmlPullParserException2 = new XmlPullParserException("Error parsing preference");
                        xmlPullParserException2.initCause(e2);
                        throw xmlPullParserException2;
                    }
                } else {
                    Preference createItemFromTag = createItemFromTag(name, attributeSet);
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    Objects.requireNonNull(preferenceGroup);
                    preferenceGroup.addPreference(createItemFromTag);
                    rInflate(xmlResourceParser, createItemFromTag, attributeSet);
                }
            }
        }
    }
}
