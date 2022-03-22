package com.android.systemui.tuner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ShortcutParser {
    public AttributeSet mAttrs;
    public final Context mContext;
    public final String mName;
    public final String mPkg;
    public final int mResId;
    public Resources mResources;

    /* loaded from: classes.dex */
    public static class Shortcut {
        public Icon icon;
        public String id;
        public Intent intent;
        public String label;
        public String name;
        public String pkg;

        public final String toString() {
            return this.pkg + "::" + this.name + "::" + this.id;
        }
    }

    public final ArrayList getShortcuts() {
        Shortcut parseShortcut;
        ArrayList arrayList = new ArrayList();
        if (this.mResId != 0) {
            try {
                Resources resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(this.mPkg);
                this.mResources = resourcesForApplication;
                XmlResourceParser xml = resourcesForApplication.getXml(this.mResId);
                this.mAttrs = Xml.asAttributeSet(xml);
                while (true) {
                    int next = xml.next();
                    if (next == 1) {
                        break;
                    } else if (next == 2 && xml.getName().equals("shortcut") && (parseShortcut = parseShortcut(xml)) != null) {
                        arrayList.add(parseShortcut);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public final Shortcut parseShortcut(XmlResourceParser xmlResourceParser) throws IOException, XmlPullParserException {
        TypedArray obtainAttributes = this.mResources.obtainAttributes(this.mAttrs, R.styleable.Shortcut);
        Shortcut shortcut = new Shortcut();
        if (!obtainAttributes.getBoolean(1, true)) {
            return null;
        }
        String string = obtainAttributes.getString(2);
        int resourceId = obtainAttributes.getResourceId(0, 0);
        int resourceId2 = obtainAttributes.getResourceId(3, 0);
        String str = this.mPkg;
        shortcut.pkg = str;
        shortcut.icon = Icon.createWithResource(str, resourceId);
        shortcut.id = string;
        shortcut.label = this.mResources.getString(resourceId2);
        shortcut.name = this.mName;
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 3) {
                break;
            } else if (next == 2 && xmlResourceParser.getName().equals("intent")) {
                shortcut.intent = Intent.parseIntent(this.mResources, xmlResourceParser, this.mAttrs);
            }
        }
        if (shortcut.intent != null) {
            return shortcut;
        }
        return null;
    }

    public ShortcutParser(Context context, ComponentName componentName) throws PackageManager.NameNotFoundException {
        int i;
        String packageName = componentName.getPackageName();
        String className = componentName.getClassName();
        ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(componentName, 128);
        Bundle bundle = activityInfo.metaData;
        if (bundle == null || !bundle.containsKey("android.app.shortcuts")) {
            i = 0;
        } else {
            i = activityInfo.metaData.getInt("android.app.shortcuts");
        }
        this.mContext = context;
        this.mPkg = packageName;
        this.mResId = i;
        this.mName = className;
    }
}
