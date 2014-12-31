package com.experimental.rxinteraction;

import com.experimental.rxinteraction.R;

public enum ArenaClass {
    DRUID  (R.drawable.ic_launcher),
    HUNTER (R.drawable.ic_launcher),
    MAGE   (R.drawable.ic_launcher),
    PALADIN(R.drawable.ic_launcher),
    PRIEST (R.drawable.ic_launcher),
    ROGUE  (R.drawable.ic_launcher),
    SHAMAN (R.drawable.ic_launcher),
    WARLOCK(R.drawable.ic_launcher),
    WARRIOR(R.drawable.ic_launcher),

    UN_CHOSEN(0);

    private final int drawable;

    ArenaClass(int drawable) {
        this.drawable = drawable;
    }

    @Override
    public String toString() {
        String s = super.toString();
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public String getHeroPowerDescription() {
        return String.format("Description of %s's hero power", toString().toLowerCase());
    }

    public int getDrawable() {
        return drawable;
    }
}
