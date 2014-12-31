package com.experimental.rxinteraction.util;

import android.support.annotation.NonNull;

import com.experimental.rxinteraction.ArenaApplication;
import com.experimental.rxinteraction.ArenaCard;
import com.experimental.rxinteraction.ArenaClass;
import com.experimental.rxinteraction.R;
import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.util.Random;

import static com.experimental.rxinteraction.ArenaCard.Quality;

public class CardChoiceProvider {

    private static final Random RANDOM = new Random();

    public CardChoiceProvider() {
        ArenaApplication.inject(this);
    }

    public synchronized Optional<ArenaCard> getNextCard(@NonNull final ArenaClass arenaClass) {
        return getRandomQuality().transform(new Function<Quality, ArenaCard>() {
            @Override
            public ArenaCard apply(Quality quality) {
                return new ArenaCard(
                        RANDOM.nextInt(12),
                        String.format("%s available card", arenaClass.toString()),
                        R.drawable.default_card_image,
                        quality
                );
            }
        });
    }

    private Optional<Quality> getRandomQuality() {
        int qualityIndex = RANDOM.nextInt(Quality.values().length);
        return Quality.from(qualityIndex);
    }
}
