package ru.hse.pipo.utils;

import java.util.function.Consumer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PatchUtils {
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
