package net.purelic.commons.profile.preferences;

import net.purelic.commons.profile.Preference;
import org.apache.commons.lang.WordUtils;

public enum HotbarItem {

    SWORD(Preference.HOTBAR_SWORD),
    BOW(Preference.HOTBAR_BOW),
    SHEARS(Preference.HOTBAR_SHEARS),
    PICKAXE(Preference.HOTBAR_PICKAXE),
    AXE(Preference.HOTBAR_AXE),
    WOOL(Preference.HOTBAR_WOOL),
    ;

    private final String name;
    private final Preference preference;

    HotbarItem(Preference preference) {
        this.name = WordUtils.capitalizeFully(this.name().replaceAll("_", " "));
        this.preference = preference;
    }

    public String getName() {
        return this.name;
    }

    public Preference getPreference() {
        return this.preference;
    }

}
