package com.airbnb.lottie.model;

import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class FontCharacter {
    public final char character;
    public final String fontFamily;
    public final List<ShapeGroup> shapes;
    public final String style;
    public final double width;

    public static int hashFor(char c, String str, String str2) {
        int hashCode = str.hashCode();
        return str2.hashCode() + ((hashCode + ((0 + c) * 31)) * 31);
    }

    public final int hashCode() {
        return hashFor(this.character, this.fontFamily, this.style);
    }

    public FontCharacter(ArrayList arrayList, char c, double d, String str, String str2) {
        this.shapes = arrayList;
        this.character = c;
        this.width = d;
        this.style = str;
        this.fontFamily = str2;
    }
}
