package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class FooterButtonInflater {
    public final Context context;

    public FooterButtonInflater(Context context) {
        this.context = context;
    }

    public final FooterButton inflate(XmlResourceParser xmlResourceParser) {
        int next;
        AttributeSet asAttributeSet = Xml.asAttributeSet(xmlResourceParser);
        while (true) {
            try {
                next = xmlResourceParser.next();
                if (next == 2 || next == 1) {
                    break;
                }
            } catch (IOException e) {
                throw new InflateException(xmlResourceParser.getPositionDescription() + ": " + e.getMessage(), e);
            } catch (XmlPullParserException e2) {
                throw new InflateException(e2.getMessage(), e2);
            }
        }
        if (next != 2) {
            throw new InflateException(xmlResourceParser.getPositionDescription() + ": No start tag found!");
        } else if (xmlResourceParser.getName().equals("FooterButton")) {
            return new FooterButton(this.context, asAttributeSet);
        } else {
            throw new InflateException(xmlResourceParser.getPositionDescription() + ": not a FooterButton");
        }
    }
}
