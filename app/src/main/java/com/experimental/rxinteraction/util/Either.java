package com.experimental.rxinteraction.util;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

/**
 * A Basic Implementation of the Either Monad
 * <p/>
 * An Either itself cannot be created (and aside from its inner classes should never be sub classed.
 * <p/>
 * To Create an Either, you need to create a LeftProjection, or a RightProjection.
 * <p/>
 * Example:
 *   Either<Integer, String> data = Either.left(18);
 *   or
 *   Either<Integer, String> data = Either.right("eighteen");
 *
 *   Then with your `data` object you can find out which type it is, and return the correct object accordingly
 *
 *   if (data.isLeft()) {
 *       Integer number = data.left();
 *   } else { // else if (data.isRight()) can be implied due to construction semantics
 *       String numberStr = data.right();
 *   }
 *
 * @param <Left> The type of object used for the LeftProjection side of this Either
 * @param <Right> The type of object used for the RightProjection side of this Either
 */
public abstract class Either<Left, Right> {
    protected abstract Optional<Left> getLeft();
    protected abstract Optional<Right> getRight();

    public final boolean isLeft() {
        return getLeft().isPresent();
    }

    public boolean isRight() {
        return getRight().isPresent();
    }

    public Left left() throws IllegalStateException {
        return getLeft().get();
    }

    public Right right() throws IllegalStateException {
        return getRight().get();
    }

    public static <L, R> Either<L, R> right(R r) {
        return new RightProjection<>(r);
    }

    public static <L, R> Either<L, R> left(L l) {
        return new LeftProjection<>(l);
    }

    /**
     * Create an Either of type <L, R> which will be strictly of type L
     */
    private static final class LeftProjection<L, R> extends Either<L, R> {

        private final L mItem;

        public LeftProjection(@NonNull L item) {
            this.mItem = item;
        }

        @Override
        protected Optional<L> getLeft() {
            return Optional.of(mItem);
        }

        @Override
        protected Optional<R> getRight() {
            return Optional.absent();
        }

        @Override
        public String toString() {
            return String.format("Left(%s)", mItem);
        }
    }

    /**
     * Create an Either of type <L, R> which will be strictly of type R
     */
    private static final class RightProjection<L, R> extends Either<L, R> {

        private final R mItem;

        public RightProjection(@NonNull R item) {
            this.mItem = item;
        }

        @Override
        protected Optional<L> getLeft() {
            return Optional.absent();
        }

        @Override
        protected Optional<R> getRight() {
            return Optional.of(mItem);
        }

        @Override
        public String toString() {
            return String.format("Right(%s)", mItem);
        }
    }
}