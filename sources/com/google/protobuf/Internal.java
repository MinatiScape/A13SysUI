package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public final class Internal {
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /* loaded from: classes.dex */
    public interface EnumLite {
    }

    /* loaded from: classes.dex */
    public interface ProtobufList<E> extends List<E>, RandomAccess {
        boolean isModifiable();

        void makeImmutable();

        ProtobufList<E> mutableCopyWithCapacity(int i);
    }

    public static int hashLong(long j) {
        return (int) (j ^ (j >>> 32));
    }

    static {
        Charset.forName("ISO-8859-1");
        byte[] bArr = new byte[0];
        EMPTY_BYTE_ARRAY = bArr;
        ByteBuffer.wrap(bArr);
        if ((0 - 0) + 0 > Integer.MAX_VALUE) {
            try {
                throw InvalidProtocolBufferException.truncatedMessage();
            } catch (InvalidProtocolBufferException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static GeneratedMessageLite mergeMessage(Object obj, Object obj2) {
        GeneratedMessageLite.Builder builder$1 = ((MessageLite) obj).toBuilder$1();
        Objects.requireNonNull(builder$1);
        return builder$1.mergeFrom((MessageLite) obj2).buildPartial();
    }
}
