package com.google.android.setupdesign.items;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import com.google.android.setupdesign.items.ItemInflater;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public abstract class SimpleInflater<T> {
    public final Resources resources;

    public abstract T onCreateItem(String str, AttributeSet attributeSet);

    public SimpleInflater(Resources resources) {
        this.resources = resources;
    }

    public final T createItemFromTag(String str, AttributeSet attributeSet) {
        try {
            return onCreateItem(str, attributeSet);
        } catch (InflateException e) {
            throw e;
        } catch (Exception e2) {
            throw new InflateException(attributeSet.getPositionDescription() + ": Error inflating class " + str, e2);
        }
    }

    public final Object inflate(XmlResourceParser xmlResourceParser) {
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
        if (next == 2) {
            T createItemFromTag = createItemFromTag(xmlResourceParser.getName(), asAttributeSet);
            rInflate(xmlResourceParser, createItemFromTag, asAttributeSet);
            return createItemFromTag;
        }
        throw new InflateException(xmlResourceParser.getPositionDescription() + ": No start tag found!");
    }

    public final void rInflate(XmlResourceParser xmlResourceParser, Object obj, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if ((next == 3 && xmlResourceParser.getDepth() <= depth) || next == 1) {
                return;
            }
            if (next == 2) {
                T createItemFromTag = createItemFromTag(xmlResourceParser.getName(), attributeSet);
                ItemHierarchy itemHierarchy = (ItemHierarchy) obj;
                ItemHierarchy itemHierarchy2 = (ItemHierarchy) createItemFromTag;
                if (itemHierarchy instanceof ItemInflater.ItemParent) {
                    ((ItemInflater.ItemParent) itemHierarchy).addChild(itemHierarchy2);
                    rInflate(xmlResourceParser, createItemFromTag, attributeSet);
                } else {
                    throw new IllegalArgumentException("Cannot add child item to " + itemHierarchy);
                }
            }
        }
    }
}
