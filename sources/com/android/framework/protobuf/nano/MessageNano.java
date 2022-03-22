package com.android.framework.protobuf.nano;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public abstract class MessageNano {
    public void computeSerializedSize() {
    }

    public void writeTo() throws IOException {
    }

    public MessageNano clone() throws CloneNotSupportedException {
        return (MessageNano) super.clone();
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            MessageNanoPrinter.print(null, this, new StringBuffer(), stringBuffer);
            return stringBuffer.toString();
        } catch (IllegalAccessException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Error printing proto: ");
            m.append(e.getMessage());
            return m.toString();
        } catch (InvocationTargetException e2) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Error printing proto: ");
            m2.append(e2.getMessage());
            return m2.toString();
        }
    }
}
