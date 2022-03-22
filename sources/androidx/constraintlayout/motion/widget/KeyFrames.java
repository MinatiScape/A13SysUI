package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Xml;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class KeyFrames {
    public static HashMap<String, Constructor<? extends Key>> sKeyMakers;
    public HashMap<Integer, ArrayList<Key>> mFramesMap = new HashMap<>();

    static {
        HashMap<String, Constructor<? extends Key>> hashMap = new HashMap<>();
        sKeyMakers = hashMap;
        try {
            hashMap.put("KeyAttribute", KeyAttributes.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyPosition", KeyPosition.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyCycle", KeyCycle.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyTimeCycle", KeyTimeCycle.class.getConstructor(new Class[0]));
            sKeyMakers.put("KeyTrigger", KeyTrigger.class.getConstructor(new Class[0]));
        } catch (NoSuchMethodException e) {
            Log.e("KeyFrames", "unable to load", e);
        }
    }

    public final void addFrames(MotionController motionController) {
        boolean z;
        ArrayList<Key> arrayList = this.mFramesMap.get(Integer.valueOf(motionController.mId));
        if (arrayList != null) {
            motionController.mKeyList.addAll(arrayList);
        }
        ArrayList<Key> arrayList2 = this.mFramesMap.get(-1);
        if (arrayList2 != null) {
            Iterator<Key> it = arrayList2.iterator();
            while (it.hasNext()) {
                Key next = it.next();
                String str = ((ConstraintLayout.LayoutParams) motionController.mView.getLayoutParams()).constraintTag;
                Objects.requireNonNull(next);
                String str2 = next.mTargetString;
                if (str2 == null || str == null) {
                    z = false;
                } else {
                    z = str.matches(str2);
                }
                if (z) {
                    motionController.mKeyList.add(next);
                }
            }
        }
    }

    public final void addKey(Key key) {
        if (!this.mFramesMap.containsKey(Integer.valueOf(key.mTargetId))) {
            this.mFramesMap.put(Integer.valueOf(key.mTargetId), new ArrayList<>());
        }
        this.mFramesMap.get(Integer.valueOf(key.mTargetId)).add(key);
    }

    public KeyFrames(Context context, XmlResourceParser xmlResourceParser) {
        Key key;
        Exception e;
        HashMap<String, ConstraintAttribute> hashMap;
        try {
            int eventType = xmlResourceParser.getEventType();
            Key key2 = null;
            while (eventType != 1) {
                if (eventType == 2) {
                    String name = xmlResourceParser.getName();
                    if (sKeyMakers.containsKey(name)) {
                        try {
                            key = (Key) sKeyMakers.get(name).newInstance(new Object[0]);
                        } catch (Exception e2) {
                            e = e2;
                            key = key2;
                        }
                        try {
                            key.load(context, Xml.asAttributeSet(xmlResourceParser));
                            addKey(key);
                        } catch (Exception e3) {
                            e = e3;
                            Log.e("KeyFrames", "unable to create ", e);
                            key2 = key;
                            eventType = xmlResourceParser.next();
                        }
                        key2 = key;
                    } else if (!(!name.equalsIgnoreCase("CustomAttribute") || key2 == null || (hashMap = key2.mCustomConstraints) == null)) {
                        ConstraintAttribute.parse(context, xmlResourceParser, hashMap);
                    }
                } else if (eventType == 3) {
                    if ("KeyFrameSet".equals(xmlResourceParser.getName())) {
                        return;
                    }
                }
                eventType = xmlResourceParser.next();
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        } catch (XmlPullParserException e5) {
            e5.printStackTrace();
        }
    }
}
