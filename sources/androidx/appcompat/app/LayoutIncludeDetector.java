package androidx.appcompat.app;

import android.graphics.Path;
import android.graphics.PathMeasure;
import com.airbnb.lottie.animation.content.TrimPathContent;
import com.airbnb.lottie.utils.Utils;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class LayoutIncludeDetector {
    public AbstractCollection mXmlParserStack = new ArrayList();

    public final void apply(Path path) {
        int size = ((List) this.mXmlParserStack).size();
        while (true) {
            size--;
            if (size >= 0) {
                TrimPathContent trimPathContent = (TrimPathContent) ((List) this.mXmlParserStack).get(size);
                PathMeasure pathMeasure = Utils.pathMeasure;
                if (trimPathContent != null && !trimPathContent.hidden) {
                    Utils.applyTrimPathIfNeeded(path, trimPathContent.startAnimation.getFloatValue() / 100.0f, trimPathContent.endAnimation.getFloatValue() / 100.0f, trimPathContent.offsetAnimation.getFloatValue() / 360.0f);
                }
            } else {
                return;
            }
        }
    }
}
