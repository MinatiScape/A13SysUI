package androidx.core.graphics;

import android.annotation.SuppressLint;
import androidx.core.content.res.FontResourcesParserCompat;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public class TypefaceCompatBaseImpl {
    @SuppressLint({"BanConcurrentHashMap"})
    public ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry> mFontFamilies = new ConcurrentHashMap<>();
}
