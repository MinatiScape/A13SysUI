package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
/* loaded from: classes.dex */
public abstract class ReflectionInflater<T> extends SimpleInflater<T> {
    public static final Class<?>[] CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    public static final HashMap<String, Constructor<?>> constructorMap = new HashMap<>();
    public final Context context;
    public String defaultPackage;
    public final Object[] tempConstructorArgs = new Object[2];

    @Override // com.google.android.setupdesign.items.SimpleInflater
    public final T onCreateItem(String str, AttributeSet attributeSet) {
        String str2;
        String str3 = this.defaultPackage;
        if (str3 == null || str.indexOf(46) != -1) {
            str2 = str;
        } else {
            str2 = str3.concat(str);
        }
        HashMap<String, Constructor<?>> hashMap = constructorMap;
        Constructor<?> constructor = hashMap.get(str2);
        if (constructor == null) {
            try {
                constructor = this.context.getClassLoader().loadClass(str2).getConstructor(CONSTRUCTOR_SIGNATURE);
                constructor.setAccessible(true);
                hashMap.put(str, constructor);
            } catch (Exception e) {
                throw new InflateException(attributeSet.getPositionDescription() + ": Error inflating class " + str2, e);
            }
        }
        Object[] objArr = this.tempConstructorArgs;
        objArr[0] = this.context;
        objArr[1] = attributeSet;
        T t = (T) constructor.newInstance(objArr);
        Object[] objArr2 = this.tempConstructorArgs;
        objArr2[0] = null;
        objArr2[1] = null;
        return t;
    }

    public ReflectionInflater(Context context) {
        super(context.getResources());
        this.context = context;
    }
}
