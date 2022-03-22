package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
/* loaded from: classes.dex */
public class Subscriber {
    public final Object target;

    /* loaded from: classes.dex */
    public static final class SynchronizedSubscriber extends Subscriber {
        @Override // com.google.common.eventbus.Subscriber
        public final void invokeSubscriberMethod(Object obj) throws InvocationTargetException {
            synchronized (this) {
                Subscriber.super.invokeSubscriberMethod(obj);
            }
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Subscriber) || this.target != ((Subscriber) obj).target) {
            return false;
        }
        throw null;
    }

    public void invokeSubscriberMethod(Object obj) throws InvocationTargetException {
        try {
            Objects.requireNonNull(obj);
            throw null;
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + obj, e);
        } catch (IllegalArgumentException e2) {
            throw new Error("Method rejected target/argument: " + obj, e2);
        } catch (InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw ((Error) e3.getCause());
            }
            throw e3;
        }
    }

    public final int hashCode() {
        throw null;
    }
}
