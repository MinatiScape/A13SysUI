package okio;
/* compiled from: Okio.kt */
/* loaded from: classes.dex */
public final /* synthetic */ class Okio__OkioKt {
    public static final int[] ColorStateListItem = {16843173, 16843551, 16844359, 2130968627, 2130969285};
    public static final int[] FontFamily = {2130969094, 2130969095, 2130969096, 2130969097, 2130969098, 2130969099, 2130969100};
    public static final int[] FontFamilyFont = {16844082, 16844083, 16844095, 16844143, 16844144, 2130969092, 2130969101, 2130969102, 2130969103, 2130970084};
    public static final int[] GradientColor = {16843165, 16843166, 16843169, 16843170, 16843171, 16843172, 16843265, 16843275, 16844048, 16844049, 16844050, 16844051};
    public static final int[] GradientColorItem = {16843173, 16844052};

    public static int smear(int i) {
        return (int) (Integer.rotateLeft((int) (i * (-862048943)), 15) * 461845907);
    }

    public static int smearedHash(Object obj) {
        int i;
        if (obj == null) {
            i = 0;
        } else {
            i = obj.hashCode();
        }
        return smear(i);
    }
}
