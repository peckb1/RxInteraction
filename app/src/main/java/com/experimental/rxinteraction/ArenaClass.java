package com.experimental.rxinteraction;

/**
 * An enum currently pulling double duty of containing the selectable classes
 * as well as being the event sent across our Rx subjects when a class is chosen
 * <p/>
 * Ideally those would be split into two different objects to avoid extra data existing in one when the other
 * needs to be used.
 */
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

    /**
     * Returns a description of the hero power
     */
    public String getHeroPowerDescription() {
        return String.format("Description of %s's hero power", toString().toLowerCase());
    }

    /**
     * Returns the drawable resource for the given class
     */
    public int getDrawable() {
        return drawable;
    }
}
