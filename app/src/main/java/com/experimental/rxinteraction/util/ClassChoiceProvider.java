package com.experimental.rxinteraction.util;

import com.experimental.rxinteraction.ArenaClass;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A provider of ArenaClass(es) that the user can select from to start their arena draft
 */
public class ClassChoiceProvider {

    private static final Random RANDOM = new Random();

    private final List<ArenaClass> classChoices = Lists.newArrayList(EnumSet.allOf(ArenaClass.class));
    private final Set<ArenaClass> alreadySelectedChoices = Sets.newHashSet();

    public void reset() {
        alreadySelectedChoices.clear();
    }

    /**
     * Returns the next unique class to allow the user to choose from. If no more unique classes are available
     * to choose from than an absent Optional is returned.
     *
     * @return Optionally, a unique class to allow the user to choose for their net arena draft.
     */
    public synchronized Optional<ArenaClass> getNextClass() {
        if (alreadySelectedChoices.size() == classChoices.size()) {
            return Optional.absent();
        }

        ArenaClass arenaClass = classChoices.get(randomIndex());

        while(alreadySelectedChoices.contains(arenaClass)) {
            arenaClass = classChoices.get(randomIndex());
        }

        alreadySelectedChoices.add(arenaClass);
        return Optional.of(arenaClass);
    }

    private int randomIndex() {
        return RANDOM.nextInt(classChoices.size() - 1);
    }

}
