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

/**
 * A simple class used to retrieved cards to select from for a given round of choices
 * <p/>
 * At the moment no logic exists to actually group cards in sets of three; each card currently
 * had just random-ish data for its quality/cost/name
 */
public class CardChoiceProvider {

    private static final Random RANDOM = new Random();

    public CardChoiceProvider() {
        ArenaApplication.inject(this);
    }

    /**
     * Optionally retrieve a card to allow the user to choose.
     * <p/>
     * The Optional is essentially extraneous here as there should always be a card to choose from
     * However the currently implementation of how quality is selected means we're starting an optional.
     * <p/>
     * Ideally this method should not return an Optional, or null.
     *
     * @param arenaClass The currently selected class; of which cards need to be available to play in that deck
     * @return An Optional containing a card to allow the user to choose
     */
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
