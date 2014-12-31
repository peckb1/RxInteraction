package com.experimental.rxinteraction;

import com.google.common.base.Optional;

import java.util.EnumSet;

public class ArenaCard {

    public static enum Quality {
        COMMON(0),
        RARE(1),
        EPIC(2),
        LEGENDARY(3);

        private final int qualityNumber;

        Quality(int qualityNumber) {
            this.qualityNumber = qualityNumber;
        }

        public static Optional<Quality> from(int qualityNumber) {
            EnumSet<Quality> qualities = EnumSet.allOf(Quality.class);
            for (Quality quality : qualities) {
                if (quality.getQualityNumber() == qualityNumber) {
                    return Optional.of(quality);
                }
            }

            return Optional.absent();
        }

        private int getQualityNumber() {
            return qualityNumber;
        }
    }

    private final int cost;
    private final String name;
    private final int imageResource;
    private final Quality quality;

    public ArenaCard(int cost, String name, int imageResource, Quality quality) {
        this.cost = cost;
        this.name = name;
        this.imageResource = imageResource;
        this.quality = quality;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public Quality getQuality() {
        return quality;
    }
}
