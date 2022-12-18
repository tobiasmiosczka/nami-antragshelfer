package com.github.tobiasmiosczka.nami.util;

import nami.connector.namitypes.NamiGeschlecht;
import nami.connector.namitypes.NamiMitglied;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.tobiasmiosczka.nami.util.TimeUtil.calcAge;

public class FilterUtil {
    @SafeVarargs
    public static <T> long sum(List<T> list, Predicate<? super T>...predicates) {
        Stream<T> stream = list.stream();
        for (Predicate<? super T> predicate : predicates) {
            stream = stream.filter(predicate);
        }
        return stream.count();
    }

    @SafeVarargs
    public static <T> List<T> filter(List<T> list, Predicate<? super T>...predicates) {
        Stream<T> stream = list.stream();
        for (Predicate<? super T> predicate : predicates) {
            stream = stream.filter(predicate);
        }
        return stream.toList();
    }

    public static Predicate<? super NamiMitglied> isOlderThan(int age, LocalDate atDate) {
        return e -> calcAge(e.getGeburtsDatum(), atDate) > age;
    }

    public static Predicate<? super NamiMitglied> isYoungerThan(int age, LocalDate atDate) {
        return e -> calcAge(e.getGeburtsDatum(), atDate) < age;
    }

    public static Predicate<? super NamiMitglied> is(NamiGeschlecht gender) {
        return (Predicate<NamiMitglied>) e -> e.getGeschlecht() == gender;
    }

    public static Predicate<? super NamiMitglied> ageIsBetween(int ageFrom, int ageTo, LocalDate atDate) {
        return e -> {
            int number = calcAge(e.getGeburtsDatum(), atDate);
            return (number >= ageFrom) && (number <= ageTo);
        };
    }
}
