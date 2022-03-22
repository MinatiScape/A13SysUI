package android.hardware.google.pixel.vendor;

import com.android.systemui.plugins.FalsingManager;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RawMessageInfo;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PixelAtoms$ReverseDomainNames extends GeneratedMessageLite<PixelAtoms$ReverseDomainNames, Builder> implements MessageLiteOrBuilder {
    private static final PixelAtoms$ReverseDomainNames DEFAULT_INSTANCE;
    private static volatile Parser<PixelAtoms$ReverseDomainNames> PARSER = null;
    public static final int PIXEL_FIELD_NUMBER = 1;
    private int bitField0_;
    private String pixel_ = "com.google.pixel";

    /* loaded from: classes.dex */
    public static final class Builder extends GeneratedMessageLite.Builder<PixelAtoms$ReverseDomainNames, Builder> implements MessageLiteOrBuilder {
        public Builder() {
            super(PixelAtoms$ReverseDomainNames.DEFAULT_INSTANCE);
        }
    }

    static {
        PixelAtoms$ReverseDomainNames pixelAtoms$ReverseDomainNames = new PixelAtoms$ReverseDomainNames();
        DEFAULT_INSTANCE = pixelAtoms$ReverseDomainNames;
        GeneratedMessageLite.registerDefaultInstance(pixelAtoms$ReverseDomainNames);
    }

    public static Builder newBuilder() {
        PixelAtoms$ReverseDomainNames pixelAtoms$ReverseDomainNames = DEFAULT_INSTANCE;
        Objects.requireNonNull(pixelAtoms$ReverseDomainNames);
        return (Builder) ((GeneratedMessageLite.Builder) pixelAtoms$ReverseDomainNames.dynamicMethod(GeneratedMessageLite.MethodToInvoke.NEW_BUILDER));
    }

    @Override // com.google.protobuf.GeneratedMessageLite
    public final Object dynamicMethod(GeneratedMessageLite.MethodToInvoke methodToInvoke) {
        switch (methodToInvoke.ordinal()) {
            case 0:
                return (byte) 1;
            case 1:
                return null;
            case 2:
                return new RawMessageInfo(DEFAULT_INSTANCE, new Object[]{"bitField0_", "pixel_"});
            case 3:
                return new PixelAtoms$ReverseDomainNames();
            case 4:
                return new Builder();
            case 5:
                return DEFAULT_INSTANCE;
            case FalsingManager.VERSION /* 6 */:
                Parser<PixelAtoms$ReverseDomainNames> parser = PARSER;
                if (parser == null) {
                    synchronized (PixelAtoms$ReverseDomainNames.class) {
                        parser = PARSER;
                        if (parser == null) {
                            parser = new GeneratedMessageLite.DefaultInstanceBasedParser<>(DEFAULT_INSTANCE);
                            PARSER = parser;
                        }
                    }
                }
                return parser;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public final String getPixel() {
        return this.pixel_;
    }
}
