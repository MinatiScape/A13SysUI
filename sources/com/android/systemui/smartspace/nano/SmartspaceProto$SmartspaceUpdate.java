package com.android.systemui.smartspace.nano;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.systemui.plugins.FalsingManager;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class SmartspaceProto$SmartspaceUpdate extends MessageNano {
    public SmartspaceCard[] card;

    /* loaded from: classes.dex */
    public static final class SmartspaceCard extends MessageNano {
        public static volatile SmartspaceCard[] _emptyArray;
        public boolean shouldDiscard = false;
        public int cardPriority = 0;
        public int cardId = 0;
        public Message preEvent = null;
        public Message duringEvent = null;
        public Message postEvent = null;
        public Image icon = null;
        public int cardType = 0;
        public TapAction tapAction = null;
        public long updateTimeMillis = 0;
        public long eventTimeMillis = 0;
        public long eventDurationMillis = 0;
        public ExpiryCriteria expiryCriteria = null;
        public boolean isSensitive = false;
        public boolean isWorkProfile = false;

        /* loaded from: classes.dex */
        public static final class ExpiryCriteria extends MessageNano {
            public long expirationTimeMillis = 0;
            public int maxImpressions = 0;

            @Override // com.google.protobuf.nano.MessageNano
            public final int computeSerializedSize() {
                long j = this.expirationTimeMillis;
                int i = 0;
                if (j != 0) {
                    i = 0 + CodedOutputByteBufferNano.computeInt64Size(1, j);
                }
                int i2 = this.maxImpressions;
                if (i2 != 0) {
                    return i + CodedOutputByteBufferNano.computeInt32Size(2, i2);
                }
                return i;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                long j = this.expirationTimeMillis;
                if (j != 0) {
                    codedOutputByteBufferNano.writeInt64(1, j);
                }
                int i = this.maxImpressions;
                if (i != 0) {
                    codedOutputByteBufferNano.writeInt32(2, i);
                }
            }

            public ExpiryCriteria() {
                this.cachedSize = -1;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        break;
                    } else if (readTag == 8) {
                        this.expirationTimeMillis = codedInputByteBufferNano.readRawVarint64();
                    } else if (readTag == 16) {
                        this.maxImpressions = codedInputByteBufferNano.readRawVarint32();
                    } else if (!codedInputByteBufferNano.skipField(readTag)) {
                        break;
                    }
                }
                return this;
            }
        }

        /* loaded from: classes.dex */
        public static final class Image extends MessageNano {
            public String key = "";
            public String gsaResourceName = "";
            public String uri = "";

            @Override // com.google.protobuf.nano.MessageNano
            public final int computeSerializedSize() {
                int i = 0;
                if (!this.key.equals("")) {
                    i = 0 + CodedOutputByteBufferNano.computeStringSize(1, this.key);
                }
                if (!this.gsaResourceName.equals("")) {
                    i += CodedOutputByteBufferNano.computeStringSize(2, this.gsaResourceName);
                }
                if (!this.uri.equals("")) {
                    return i + CodedOutputByteBufferNano.computeStringSize(3, this.uri);
                }
                return i;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                if (!this.key.equals("")) {
                    codedOutputByteBufferNano.writeString(1, this.key);
                }
                if (!this.gsaResourceName.equals("")) {
                    codedOutputByteBufferNano.writeString(2, this.gsaResourceName);
                }
                if (!this.uri.equals("")) {
                    codedOutputByteBufferNano.writeString(3, this.uri);
                }
            }

            public Image() {
                this.cachedSize = -1;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        break;
                    } else if (readTag == 10) {
                        this.key = codedInputByteBufferNano.readString();
                    } else if (readTag == 18) {
                        this.gsaResourceName = codedInputByteBufferNano.readString();
                    } else if (readTag == 26) {
                        this.uri = codedInputByteBufferNano.readString();
                    } else if (!codedInputByteBufferNano.skipField(readTag)) {
                        break;
                    }
                }
                return this;
            }
        }

        /* loaded from: classes.dex */
        public static final class Message extends MessageNano {
            public FormattedText title = null;
            public FormattedText subtitle = null;

            /* loaded from: classes.dex */
            public static final class FormattedText extends MessageNano {
                public FormatParam[] formatParam;
                public String text = "";
                public int truncateLocation = 0;

                /* loaded from: classes.dex */
                public static final class FormatParam extends MessageNano {
                    public static volatile FormatParam[] _emptyArray;
                    public String text = "";
                    public int truncateLocation = 0;
                    public int formatParamArgs = 0;
                    public boolean updateTimeLocally = false;

                    @Override // com.google.protobuf.nano.MessageNano
                    public final int computeSerializedSize() {
                        int i = 0;
                        if (!this.text.equals("")) {
                            i = 0 + CodedOutputByteBufferNano.computeStringSize(1, this.text);
                        }
                        int i2 = this.truncateLocation;
                        if (i2 != 0) {
                            i += CodedOutputByteBufferNano.computeInt32Size(2, i2);
                        }
                        int i3 = this.formatParamArgs;
                        if (i3 != 0) {
                            i += CodedOutputByteBufferNano.computeInt32Size(3, i3);
                        }
                        if (this.updateTimeLocally) {
                            return i + CodedOutputByteBufferNano.computeBoolSize(4);
                        }
                        return i;
                    }

                    @Override // com.google.protobuf.nano.MessageNano
                    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                        if (!this.text.equals("")) {
                            codedOutputByteBufferNano.writeString(1, this.text);
                        }
                        int i = this.truncateLocation;
                        if (i != 0) {
                            codedOutputByteBufferNano.writeInt32(2, i);
                        }
                        int i2 = this.formatParamArgs;
                        if (i2 != 0) {
                            codedOutputByteBufferNano.writeInt32(3, i2);
                        }
                        boolean z = this.updateTimeLocally;
                        if (z) {
                            codedOutputByteBufferNano.writeBool(4, z);
                        }
                    }

                    public FormatParam() {
                        this.cachedSize = -1;
                    }

                    @Override // com.google.protobuf.nano.MessageNano
                    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                        while (true) {
                            int readTag = codedInputByteBufferNano.readTag();
                            if (readTag == 0) {
                                break;
                            } else if (readTag == 10) {
                                this.text = codedInputByteBufferNano.readString();
                            } else if (readTag == 16) {
                                int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                                if (readRawVarint32 == 0 || readRawVarint32 == 1 || readRawVarint32 == 2 || readRawVarint32 == 3) {
                                    this.truncateLocation = readRawVarint32;
                                }
                            } else if (readTag == 24) {
                                int readRawVarint322 = codedInputByteBufferNano.readRawVarint32();
                                if (readRawVarint322 == 0 || readRawVarint322 == 1 || readRawVarint322 == 2 || readRawVarint322 == 3) {
                                    this.formatParamArgs = readRawVarint322;
                                }
                            } else if (readTag == 32) {
                                this.updateTimeLocally = codedInputByteBufferNano.readBool();
                            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                                break;
                            }
                        }
                        return this;
                    }
                }

                @Override // com.google.protobuf.nano.MessageNano
                public final int computeSerializedSize() {
                    int i;
                    int i2 = 0;
                    if (!this.text.equals("")) {
                        i = CodedOutputByteBufferNano.computeStringSize(1, this.text) + 0;
                    } else {
                        i = 0;
                    }
                    int i3 = this.truncateLocation;
                    if (i3 != 0) {
                        i += CodedOutputByteBufferNano.computeInt32Size(2, i3);
                    }
                    FormatParam[] formatParamArr = this.formatParam;
                    if (formatParamArr != null && formatParamArr.length > 0) {
                        while (true) {
                            FormatParam[] formatParamArr2 = this.formatParam;
                            if (i2 >= formatParamArr2.length) {
                                break;
                            }
                            FormatParam formatParam = formatParamArr2[i2];
                            if (formatParam != null) {
                                i += CodedOutputByteBufferNano.computeMessageSize(3, formatParam);
                            }
                            i2++;
                        }
                    }
                    return i;
                }

                @Override // com.google.protobuf.nano.MessageNano
                public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                    if (!this.text.equals("")) {
                        codedOutputByteBufferNano.writeString(1, this.text);
                    }
                    int i = this.truncateLocation;
                    if (i != 0) {
                        codedOutputByteBufferNano.writeInt32(2, i);
                    }
                    FormatParam[] formatParamArr = this.formatParam;
                    if (formatParamArr != null && formatParamArr.length > 0) {
                        int i2 = 0;
                        while (true) {
                            FormatParam[] formatParamArr2 = this.formatParam;
                            if (i2 < formatParamArr2.length) {
                                FormatParam formatParam = formatParamArr2[i2];
                                if (formatParam != null) {
                                    codedOutputByteBufferNano.writeMessage(3, formatParam);
                                }
                                i2++;
                            } else {
                                return;
                            }
                        }
                    }
                }

                public FormattedText() {
                    if (FormatParam._emptyArray == null) {
                        synchronized (InternalNano.LAZY_INIT_LOCK) {
                            if (FormatParam._emptyArray == null) {
                                FormatParam._emptyArray = new FormatParam[0];
                            }
                        }
                    }
                    this.formatParam = FormatParam._emptyArray;
                    this.cachedSize = -1;
                }

                @Override // com.google.protobuf.nano.MessageNano
                public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                    int i;
                    while (true) {
                        int readTag = codedInputByteBufferNano.readTag();
                        if (readTag == 0) {
                            break;
                        } else if (readTag == 10) {
                            this.text = codedInputByteBufferNano.readString();
                        } else if (readTag == 16) {
                            int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                            if (readRawVarint32 == 0 || readRawVarint32 == 1 || readRawVarint32 == 2 || readRawVarint32 == 3) {
                                this.truncateLocation = readRawVarint32;
                            }
                        } else if (readTag == 26) {
                            int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 26);
                            FormatParam[] formatParamArr = this.formatParam;
                            if (formatParamArr == null) {
                                i = 0;
                            } else {
                                i = formatParamArr.length;
                            }
                            int i2 = repeatedFieldArrayLength + i;
                            FormatParam[] formatParamArr2 = new FormatParam[i2];
                            if (i != 0) {
                                System.arraycopy(formatParamArr, 0, formatParamArr2, 0, i);
                            }
                            while (i < i2 - 1) {
                                formatParamArr2[i] = new FormatParam();
                                codedInputByteBufferNano.readMessage(formatParamArr2[i]);
                                codedInputByteBufferNano.readTag();
                                i++;
                            }
                            formatParamArr2[i] = new FormatParam();
                            codedInputByteBufferNano.readMessage(formatParamArr2[i]);
                            this.formatParam = formatParamArr2;
                        } else if (!codedInputByteBufferNano.skipField(readTag)) {
                            break;
                        }
                    }
                    return this;
                }
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final int computeSerializedSize() {
                FormattedText formattedText = this.title;
                int i = 0;
                if (formattedText != null) {
                    i = 0 + CodedOutputByteBufferNano.computeMessageSize(1, formattedText);
                }
                FormattedText formattedText2 = this.subtitle;
                if (formattedText2 != null) {
                    return i + CodedOutputByteBufferNano.computeMessageSize(2, formattedText2);
                }
                return i;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                FormattedText formattedText = this.title;
                if (formattedText != null) {
                    codedOutputByteBufferNano.writeMessage(1, formattedText);
                }
                FormattedText formattedText2 = this.subtitle;
                if (formattedText2 != null) {
                    codedOutputByteBufferNano.writeMessage(2, formattedText2);
                }
            }

            public Message() {
                this.cachedSize = -1;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        break;
                    } else if (readTag == 10) {
                        if (this.title == null) {
                            this.title = new FormattedText();
                        }
                        codedInputByteBufferNano.readMessage(this.title);
                    } else if (readTag == 18) {
                        if (this.subtitle == null) {
                            this.subtitle = new FormattedText();
                        }
                        codedInputByteBufferNano.readMessage(this.subtitle);
                    } else if (!codedInputByteBufferNano.skipField(readTag)) {
                        break;
                    }
                }
                return this;
            }
        }

        /* loaded from: classes.dex */
        public static final class TapAction extends MessageNano {
            public int actionType = 0;
            public String intent = "";

            @Override // com.google.protobuf.nano.MessageNano
            public final int computeSerializedSize() {
                int i = this.actionType;
                int i2 = 0;
                if (i != 0) {
                    i2 = 0 + CodedOutputByteBufferNano.computeInt32Size(1, i);
                }
                if (!this.intent.equals("")) {
                    return i2 + CodedOutputByteBufferNano.computeStringSize(2, this.intent);
                }
                return i2;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                int i = this.actionType;
                if (i != 0) {
                    codedOutputByteBufferNano.writeInt32(1, i);
                }
                if (!this.intent.equals("")) {
                    codedOutputByteBufferNano.writeString(2, this.intent);
                }
            }

            public TapAction() {
                this.cachedSize = -1;
            }

            @Override // com.google.protobuf.nano.MessageNano
            public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
                while (true) {
                    int readTag = codedInputByteBufferNano.readTag();
                    if (readTag == 0) {
                        break;
                    } else if (readTag == 8) {
                        int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                        if (readRawVarint32 == 0 || readRawVarint32 == 1 || readRawVarint32 == 2) {
                            this.actionType = readRawVarint32;
                        }
                    } else if (readTag == 18) {
                        this.intent = codedInputByteBufferNano.readString();
                    } else if (!codedInputByteBufferNano.skipField(readTag)) {
                        break;
                    }
                }
                return this;
            }
        }

        @Override // com.google.protobuf.nano.MessageNano
        public final int computeSerializedSize() {
            int i = 0;
            if (this.shouldDiscard) {
                i = 0 + CodedOutputByteBufferNano.computeBoolSize(1);
            }
            int i2 = this.cardId;
            if (i2 != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(2, i2);
            }
            Message message = this.preEvent;
            if (message != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(3, message);
            }
            Message message2 = this.duringEvent;
            if (message2 != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(4, message2);
            }
            Message message3 = this.postEvent;
            if (message3 != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(5, message3);
            }
            Image image = this.icon;
            if (image != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(6, image);
            }
            int i3 = this.cardType;
            if (i3 != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(7, i3);
            }
            TapAction tapAction = this.tapAction;
            if (tapAction != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(8, tapAction);
            }
            long j = this.updateTimeMillis;
            if (j != 0) {
                i += CodedOutputByteBufferNano.computeInt64Size(9, j);
            }
            long j2 = this.eventTimeMillis;
            if (j2 != 0) {
                i += CodedOutputByteBufferNano.computeInt64Size(10, j2);
            }
            long j3 = this.eventDurationMillis;
            if (j3 != 0) {
                i += CodedOutputByteBufferNano.computeInt64Size(11, j3);
            }
            ExpiryCriteria expiryCriteria = this.expiryCriteria;
            if (expiryCriteria != null) {
                i += CodedOutputByteBufferNano.computeMessageSize(12, expiryCriteria);
            }
            int i4 = this.cardPriority;
            if (i4 != 0) {
                i += CodedOutputByteBufferNano.computeInt32Size(13, i4);
            }
            if (this.isSensitive) {
                i += CodedOutputByteBufferNano.computeBoolSize(17);
            }
            if (this.isWorkProfile) {
                return i + CodedOutputByteBufferNano.computeBoolSize(18);
            }
            return i;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
            boolean z = this.shouldDiscard;
            if (z) {
                codedOutputByteBufferNano.writeBool(1, z);
            }
            int i = this.cardId;
            if (i != 0) {
                codedOutputByteBufferNano.writeInt32(2, i);
            }
            Message message = this.preEvent;
            if (message != null) {
                codedOutputByteBufferNano.writeMessage(3, message);
            }
            Message message2 = this.duringEvent;
            if (message2 != null) {
                codedOutputByteBufferNano.writeMessage(4, message2);
            }
            Message message3 = this.postEvent;
            if (message3 != null) {
                codedOutputByteBufferNano.writeMessage(5, message3);
            }
            Image image = this.icon;
            if (image != null) {
                codedOutputByteBufferNano.writeMessage(6, image);
            }
            int i2 = this.cardType;
            if (i2 != 0) {
                codedOutputByteBufferNano.writeInt32(7, i2);
            }
            TapAction tapAction = this.tapAction;
            if (tapAction != null) {
                codedOutputByteBufferNano.writeMessage(8, tapAction);
            }
            long j = this.updateTimeMillis;
            if (j != 0) {
                codedOutputByteBufferNano.writeInt64(9, j);
            }
            long j2 = this.eventTimeMillis;
            if (j2 != 0) {
                codedOutputByteBufferNano.writeInt64(10, j2);
            }
            long j3 = this.eventDurationMillis;
            if (j3 != 0) {
                codedOutputByteBufferNano.writeInt64(11, j3);
            }
            ExpiryCriteria expiryCriteria = this.expiryCriteria;
            if (expiryCriteria != null) {
                codedOutputByteBufferNano.writeMessage(12, expiryCriteria);
            }
            int i3 = this.cardPriority;
            if (i3 != 0) {
                codedOutputByteBufferNano.writeInt32(13, i3);
            }
            boolean z2 = this.isSensitive;
            if (z2) {
                codedOutputByteBufferNano.writeBool(17, z2);
            }
            boolean z3 = this.isWorkProfile;
            if (z3) {
                codedOutputByteBufferNano.writeBool(18, z3);
            }
        }

        public SmartspaceCard() {
            this.cachedSize = -1;
        }

        @Override // com.google.protobuf.nano.MessageNano
        public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
            while (true) {
                int readTag = codedInputByteBufferNano.readTag();
                switch (readTag) {
                    case 0:
                        break;
                    case 8:
                        this.shouldDiscard = codedInputByteBufferNano.readBool();
                        break;
                    case 16:
                        this.cardId = codedInputByteBufferNano.readRawVarint32();
                        break;
                    case 26:
                        if (this.preEvent == null) {
                            this.preEvent = new Message();
                        }
                        codedInputByteBufferNano.readMessage(this.preEvent);
                        break;
                    case 34:
                        if (this.duringEvent == null) {
                            this.duringEvent = new Message();
                        }
                        codedInputByteBufferNano.readMessage(this.duringEvent);
                        break;
                    case 42:
                        if (this.postEvent == null) {
                            this.postEvent = new Message();
                        }
                        codedInputByteBufferNano.readMessage(this.postEvent);
                        break;
                    case CustomEvent.MAX_STR_LENGTH /* 50 */:
                        if (this.icon == null) {
                            this.icon = new Image();
                        }
                        codedInputByteBufferNano.readMessage(this.icon);
                        break;
                    case SwipeRefreshLayout.CIRCLE_DIAMETER_LARGE /* 56 */:
                        int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                        switch (readRawVarint32) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case FalsingManager.VERSION /* 6 */:
                                this.cardType = readRawVarint32;
                                continue;
                        }
                    case 66:
                        if (this.tapAction == null) {
                            this.tapAction = new TapAction();
                        }
                        codedInputByteBufferNano.readMessage(this.tapAction);
                        break;
                    case 72:
                        this.updateTimeMillis = codedInputByteBufferNano.readRawVarint64();
                        break;
                    case 80:
                        this.eventTimeMillis = codedInputByteBufferNano.readRawVarint64();
                        break;
                    case 88:
                        this.eventDurationMillis = codedInputByteBufferNano.readRawVarint64();
                        break;
                    case 98:
                        if (this.expiryCriteria == null) {
                            this.expiryCriteria = new ExpiryCriteria();
                        }
                        codedInputByteBufferNano.readMessage(this.expiryCriteria);
                        break;
                    case 104:
                        int readRawVarint322 = codedInputByteBufferNano.readRawVarint32();
                        if (readRawVarint322 != 0 && readRawVarint322 != 1 && readRawVarint322 != 2) {
                            break;
                        } else {
                            this.cardPriority = readRawVarint322;
                            break;
                        }
                    case 136:
                        this.isSensitive = codedInputByteBufferNano.readBool();
                        break;
                    case 144:
                        this.isWorkProfile = codedInputByteBufferNano.readBool();
                        break;
                    default:
                        if (codedInputByteBufferNano.skipField(readTag)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        SmartspaceCard[] smartspaceCardArr = this.card;
        int i = 0;
        if (smartspaceCardArr == null || smartspaceCardArr.length <= 0) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            SmartspaceCard[] smartspaceCardArr2 = this.card;
            if (i >= smartspaceCardArr2.length) {
                return i2;
            }
            SmartspaceCard smartspaceCard = smartspaceCardArr2[i];
            if (smartspaceCard != null) {
                i2 += CodedOutputByteBufferNano.computeMessageSize(1, smartspaceCard);
            }
            i++;
        }
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        SmartspaceCard[] smartspaceCardArr = this.card;
        if (smartspaceCardArr != null && smartspaceCardArr.length > 0) {
            int i = 0;
            while (true) {
                SmartspaceCard[] smartspaceCardArr2 = this.card;
                if (i < smartspaceCardArr2.length) {
                    SmartspaceCard smartspaceCard = smartspaceCardArr2[i];
                    if (smartspaceCard != null) {
                        codedOutputByteBufferNano.writeMessage(1, smartspaceCard);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public SmartspaceProto$SmartspaceUpdate() {
        if (SmartspaceCard._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (SmartspaceCard._emptyArray == null) {
                    SmartspaceCard._emptyArray = new SmartspaceCard[0];
                }
            }
        }
        this.card = SmartspaceCard._emptyArray;
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                SmartspaceCard[] smartspaceCardArr = this.card;
                if (smartspaceCardArr == null) {
                    i = 0;
                } else {
                    i = smartspaceCardArr.length;
                }
                int i2 = repeatedFieldArrayLength + i;
                SmartspaceCard[] smartspaceCardArr2 = new SmartspaceCard[i2];
                if (i != 0) {
                    System.arraycopy(smartspaceCardArr, 0, smartspaceCardArr2, 0, i);
                }
                while (i < i2 - 1) {
                    smartspaceCardArr2[i] = new SmartspaceCard();
                    codedInputByteBufferNano.readMessage(smartspaceCardArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                smartspaceCardArr2[i] = new SmartspaceCard();
                codedInputByteBufferNano.readMessage(smartspaceCardArr2[i]);
                this.card = smartspaceCardArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
