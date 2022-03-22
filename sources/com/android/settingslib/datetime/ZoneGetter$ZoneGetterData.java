package com.android.settingslib.datetime;

import com.android.i18n.timezone.CountryTimeZones;
import com.android.i18n.timezone.TimeZoneFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class ZoneGetter$ZoneGetterData {
    public List<String> lookupTimeZoneIdsByCountry(String str) {
        CountryTimeZones lookupCountryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(str);
        if (lookupCountryTimeZones == null) {
            return null;
        }
        List<CountryTimeZones.TimeZoneMapping> timeZoneMappings = lookupCountryTimeZones.getTimeZoneMappings();
        ArrayList arrayList = new ArrayList(timeZoneMappings.size());
        for (CountryTimeZones.TimeZoneMapping timeZoneMapping : timeZoneMappings) {
            arrayList.add(timeZoneMapping.getTimeZoneId());
        }
        return Collections.unmodifiableList(arrayList);
    }
}
