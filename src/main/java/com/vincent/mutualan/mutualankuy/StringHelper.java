package com.vincent.mutualan.mutualankuy;

import java.util.Objects;

public class StringHelper {

    public static Boolean isNotBlank(String string) {
        return Objects.nonNull(string) && string.trim().length() > 0;
    }
}
