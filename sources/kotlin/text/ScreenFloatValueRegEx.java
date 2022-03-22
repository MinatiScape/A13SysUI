package kotlin.text;

import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StringNumberConversionsJVM.kt */
/* loaded from: classes.dex */
public final class ScreenFloatValueRegEx {
    public static final Regex value;

    static {
        String stringPlus = Intrinsics.stringPlus("[eE][+-]?", "(\\p{Digit}+)");
        value = new Regex(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("[\\x00-\\x20]*[+-]?(NaN|Infinity|((", "((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)(" + stringPlus + ")?)|(\\.((\\p{Digit}+))(" + stringPlus + ")?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+))", ")[fFdD]?))[\\x00-\\x20]*"));
    }
}
