package androidx.emoji2.text;

import android.graphics.Typeface;
import android.util.SparseArray;
import androidx.emoji2.text.flatbuffer.MetadataItem;
import androidx.emoji2.text.flatbuffer.MetadataList;
import androidx.mediarouter.R$bool;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MetadataRepo {
    public final char[] mEmojiCharArray;
    public final MetadataList mMetadataList;
    public final Node mRootNode = new Node(1024);
    public final Typeface mTypeface;

    /* loaded from: classes.dex */
    public static class Node {
        public final SparseArray<Node> mChildren;
        public EmojiMetadata mData;

        public Node() {
            this(1);
        }

        public Node(int i) {
            this.mChildren = new SparseArray<>(i);
        }

        public final void put(EmojiMetadata emojiMetadata, int i, int i2) {
            Node node;
            int codepointAt = emojiMetadata.getCodepointAt(i);
            SparseArray<Node> sparseArray = this.mChildren;
            if (sparseArray == null) {
                node = null;
            } else {
                node = sparseArray.get(codepointAt);
            }
            if (node == null) {
                node = new Node(1);
                this.mChildren.put(emojiMetadata.getCodepointAt(i), node);
            }
            if (i2 > i) {
                node.put(emojiMetadata, i + 1, i2);
            } else {
                node.mData = emojiMetadata;
            }
        }
    }

    public void put(EmojiMetadata emojiMetadata) {
        boolean z;
        Objects.requireNonNull(emojiMetadata, "emoji metadata cannot be null");
        if (emojiMetadata.getCodepointsLength() > 0) {
            z = true;
        } else {
            z = false;
        }
        R$bool.checkArgument(z, "invalid metadata codepoint length");
        this.mRootNode.put(emojiMetadata, 0, emojiMetadata.getCodepointsLength() - 1);
    }

    public MetadataRepo(Typeface typeface, MetadataList metadataList) {
        int i;
        int i2;
        int i3;
        this.mTypeface = typeface;
        this.mMetadataList = metadataList;
        int __offset = metadataList.__offset(6);
        if (__offset != 0) {
            int i4 = __offset + metadataList.bb_pos;
            i = metadataList.bb.getInt(metadataList.bb.getInt(i4) + i4);
        } else {
            i = 0;
        }
        this.mEmojiCharArray = new char[i * 2];
        int __offset2 = metadataList.__offset(6);
        if (__offset2 != 0) {
            int i5 = __offset2 + metadataList.bb_pos;
            i2 = metadataList.bb.getInt(metadataList.bb.getInt(i5) + i5);
        } else {
            i2 = 0;
        }
        for (int i6 = 0; i6 < i2; i6++) {
            EmojiMetadata emojiMetadata = new EmojiMetadata(this, i6);
            MetadataItem metadataItem = emojiMetadata.getMetadataItem();
            int __offset3 = metadataItem.__offset(4);
            if (__offset3 != 0) {
                i3 = metadataItem.bb.getInt(__offset3 + metadataItem.bb_pos);
            } else {
                i3 = 0;
            }
            Character.toChars(i3, this.mEmojiCharArray, i6 * 2);
            put(emojiMetadata);
        }
    }
}
