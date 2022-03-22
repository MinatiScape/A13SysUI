package com.google.protobuf.nano;

import java.io.IOException;
/* loaded from: classes.dex */
public final class CodedInputByteBufferNano {
    public final byte[] buffer;
    public int bufferSize;
    public int bufferSizeAfterLimit;
    public int lastTag;
    public int recursionDepth;
    public int currentLimit = Integer.MAX_VALUE;
    public int bufferStart = 0;
    public int bufferPos = 0;

    public final long readRawVarint64() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte readRawByte = readRawByte();
            j |= (readRawByte & Byte.MAX_VALUE) << i;
            if ((readRawByte & 128) == 0) {
                return j;
            }
        }
        throw new InvalidProtocolBufferNanoException("CodedInputStream encountered a malformed varint.");
    }

    public final int pushLimit(int i) throws InvalidProtocolBufferNanoException {
        if (i >= 0) {
            int i2 = i + this.bufferPos;
            int i3 = this.currentLimit;
            if (i2 <= i3) {
                this.currentLimit = i2;
                int i4 = this.bufferSize + this.bufferSizeAfterLimit;
                this.bufferSize = i4;
                if (i4 > i2) {
                    int i5 = i4 - i2;
                    this.bufferSizeAfterLimit = i5;
                    this.bufferSize = i4 - i5;
                } else {
                    this.bufferSizeAfterLimit = 0;
                }
                return i3;
            }
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        throw new InvalidProtocolBufferNanoException("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    public final byte readRawByte() throws IOException {
        int i = this.bufferPos;
        if (i != this.bufferSize) {
            byte[] bArr = this.buffer;
            this.bufferPos = i + 1;
            return bArr[i];
        }
        throw InvalidProtocolBufferNanoException.truncatedMessage();
    }

    public final byte[] readRawBytes(int i) throws IOException {
        if (i >= 0) {
            int i2 = this.bufferPos;
            int i3 = i2 + i;
            int i4 = this.currentLimit;
            if (i3 > i4) {
                skipRawBytes(i4 - i2);
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            } else if (i <= this.bufferSize - i2) {
                byte[] bArr = new byte[i];
                System.arraycopy(this.buffer, i2, bArr, 0, i);
                this.bufferPos += i;
                return bArr;
            } else {
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            }
        } else {
            throw new InvalidProtocolBufferNanoException("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
        }
    }

    public final int readTag() throws IOException {
        boolean z;
        if (this.bufferPos == this.bufferSize) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.lastTag = 0;
            return 0;
        }
        int readRawVarint32 = readRawVarint32();
        this.lastTag = readRawVarint32;
        if (readRawVarint32 != 0) {
            return readRawVarint32;
        }
        throw new InvalidProtocolBufferNanoException("Protocol message contained an invalid tag (zero).");
    }

    public final boolean skipField(int i) throws IOException {
        int readTag;
        int i2 = i & 7;
        if (i2 == 0) {
            readRawVarint32();
            return true;
        } else if (i2 == 1) {
            readRawLittleEndian64();
            return true;
        } else if (i2 == 2) {
            skipRawBytes(readRawVarint32());
            return true;
        } else if (i2 == 3) {
            do {
                readTag = readTag();
                if (readTag == 0) {
                    break;
                }
            } while (skipField(readTag));
            if (this.lastTag == (((i >>> 3) << 3) | 4)) {
                return true;
            }
            throw new InvalidProtocolBufferNanoException("Protocol message end-group tag did not match expected tag.");
        } else if (i2 == 4) {
            return false;
        } else {
            if (i2 == 5) {
                readRawLittleEndian32();
                return true;
            }
            throw new InvalidProtocolBufferNanoException("Protocol message tag had invalid wire type.");
        }
    }

    public final void skipRawBytes(int i) throws IOException {
        if (i >= 0) {
            int i2 = this.bufferPos;
            int i3 = i2 + i;
            int i4 = this.currentLimit;
            if (i3 > i4) {
                skipRawBytes(i4 - i2);
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            } else if (i <= this.bufferSize - i2) {
                this.bufferPos = i3;
            } else {
                throw InvalidProtocolBufferNanoException.truncatedMessage();
            }
        } else {
            throw new InvalidProtocolBufferNanoException("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
        }
    }

    public CodedInputByteBufferNano(byte[] bArr, int i) {
        this.buffer = bArr;
        this.bufferSize = i + 0;
    }

    public final boolean readBool() throws IOException {
        if (readRawVarint32() != 0) {
            return true;
        }
        return false;
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public final void readMessage(MessageNano messageNano) throws IOException {
        int readRawVarint32 = readRawVarint32();
        if (this.recursionDepth < 64) {
            int pushLimit = pushLimit(readRawVarint32);
            this.recursionDepth++;
            messageNano.mergeFrom(this);
            if (this.lastTag == 0) {
                this.recursionDepth--;
                this.currentLimit = pushLimit;
                int i = this.bufferSize + this.bufferSizeAfterLimit;
                this.bufferSize = i;
                if (i > pushLimit) {
                    int i2 = i - pushLimit;
                    this.bufferSizeAfterLimit = i2;
                    this.bufferSize = i - i2;
                    return;
                }
                this.bufferSizeAfterLimit = 0;
                return;
            }
            throw new InvalidProtocolBufferNanoException("Protocol message end-group tag did not match expected tag.");
        }
        throw new InvalidProtocolBufferNanoException("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    public final int readRawLittleEndian32() throws IOException {
        byte readRawByte = readRawByte();
        byte readRawByte2 = readRawByte();
        byte readRawByte3 = readRawByte();
        return ((readRawByte() & 255) << 24) | (readRawByte & 255) | ((readRawByte2 & 255) << 8) | ((readRawByte3 & 255) << 16);
    }

    public final long readRawLittleEndian64() throws IOException {
        byte readRawByte = readRawByte();
        byte readRawByte2 = readRawByte();
        return ((readRawByte2 & 255) << 8) | (readRawByte & 255) | ((readRawByte() & 255) << 16) | ((readRawByte() & 255) << 24) | ((readRawByte() & 255) << 32) | ((readRawByte() & 255) << 40) | ((readRawByte() & 255) << 48) | ((readRawByte() & 255) << 56);
    }

    public final int readRawVarint32() throws IOException {
        int i;
        byte readRawByte = readRawByte();
        if (readRawByte >= 0) {
            return readRawByte;
        }
        int i2 = readRawByte & Byte.MAX_VALUE;
        byte readRawByte2 = readRawByte();
        if (readRawByte2 >= 0) {
            i = readRawByte2 << 7;
        } else {
            i2 |= (readRawByte2 & Byte.MAX_VALUE) << 7;
            byte readRawByte3 = readRawByte();
            if (readRawByte3 >= 0) {
                i = readRawByte3 << 14;
            } else {
                i2 |= (readRawByte3 & Byte.MAX_VALUE) << 14;
                byte readRawByte4 = readRawByte();
                if (readRawByte4 >= 0) {
                    i = readRawByte4 << 21;
                } else {
                    int i3 = i2 | ((readRawByte4 & Byte.MAX_VALUE) << 21);
                    byte readRawByte5 = readRawByte();
                    int i4 = i3 | (readRawByte5 << 28);
                    if (readRawByte5 >= 0) {
                        return i4;
                    }
                    for (int i5 = 0; i5 < 5; i5++) {
                        if (readRawByte() >= 0) {
                            return i4;
                        }
                    }
                    throw new InvalidProtocolBufferNanoException("CodedInputStream encountered a malformed varint.");
                }
            }
        }
        return i | i2;
    }

    public final String readString() throws IOException {
        int readRawVarint32 = readRawVarint32();
        int i = this.bufferSize;
        int i2 = this.bufferPos;
        if (readRawVarint32 > i - i2 || readRawVarint32 <= 0) {
            return new String(readRawBytes(readRawVarint32), InternalNano.UTF_8);
        }
        String str = new String(this.buffer, i2, readRawVarint32, InternalNano.UTF_8);
        this.bufferPos += readRawVarint32;
        return str;
    }
}
