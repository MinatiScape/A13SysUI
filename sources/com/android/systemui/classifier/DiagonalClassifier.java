package com.android.systemui.classifier;

import android.provider.DeviceConfig;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DiagonalClassifier extends FalsingClassifier {
    public final float mHorizontalAngleRange = DeviceConfig.getFloat("systemui", "brightline_falsing_diagonal_horizontal_angle_range", 0.08726646f);
    public final float mVerticalAngleRange = DeviceConfig.getFloat("systemui", "brightline_falsing_diagonal_horizontal_angle_range", 0.08726646f);

    public static boolean angleBetween(float f, float f2, float f3) {
        if (f2 < 0.0f) {
            f2 = (f2 % 6.2831855f) + 6.2831855f;
        } else if (f2 > 6.2831855f) {
            f2 %= 6.2831855f;
        }
        if (f3 < 0.0f) {
            f3 = (f3 % 6.2831855f) + 6.2831855f;
        } else if (f3 > 6.2831855f) {
            f3 %= 6.2831855f;
        }
        int i = (f2 > f3 ? 1 : (f2 == f3 ? 0 : -1));
        int i2 = (f > f2 ? 1 : (f == f2 ? 0 : -1));
        return i > 0 ? i2 >= 0 || f <= f3 : i2 >= 0 && f <= f3;
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    public final FalsingClassifier.Result calculateFalsingResult(int i) {
        FalsingDataProvider falsingDataProvider;
        boolean z;
        FalsingDataProvider falsingDataProvider2;
        FalsingDataProvider falsingDataProvider3 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider3);
        falsingDataProvider3.recalculateData();
        float f = falsingDataProvider3.mAngle;
        if (f == Float.MAX_VALUE) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (i == 5 || i == 6 || i == 14) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        float f2 = this.mHorizontalAngleRange;
        float f3 = 0.7853982f - f2;
        float f4 = f2 + 0.7853982f;
        Objects.requireNonNull(this.mDataProvider);
        if (!falsingDataProvider.isHorizontal()) {
            float f5 = this.mVerticalAngleRange;
            f3 = 0.7853982f - f5;
            f4 = f5 + 0.7853982f;
        }
        if (angleBetween(f, f3, f4) || angleBetween(f, f3 + 1.5707964f, f4 + 1.5707964f) || angleBetween(f, f3 - 1.5707964f, f4 - 1.5707964f) || angleBetween(f, f3 + 3.1415927f, f4 + 3.1415927f)) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            return FalsingClassifier.Result.passed(0.5d);
        }
        FalsingDataProvider falsingDataProvider4 = this.mDataProvider;
        Objects.requireNonNull(falsingDataProvider4);
        falsingDataProvider4.recalculateData();
        Objects.requireNonNull(this.mDataProvider);
        return falsed(0.5d, String.format(null, "{angle=%f, vertical=%s}", Float.valueOf(falsingDataProvider4.mAngle), Boolean.valueOf(!falsingDataProvider2.isHorizontal())));
    }

    public DiagonalClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        Objects.requireNonNull(deviceConfigProxy);
    }
}
