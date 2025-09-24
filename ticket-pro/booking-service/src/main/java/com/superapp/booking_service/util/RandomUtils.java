package com.superapp.booking_service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static BigDecimal randomBigDecimal(BigDecimal min, BigDecimal max, int scale) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be <= max");
        }
        BigDecimal range = max.subtract(min);

        // random double between 0 and 1
        double randomFraction = ThreadLocalRandom.current().nextDouble();

        // scale it into the range
        BigDecimal randomValue = min.add(range.multiply(BigDecimal.valueOf(randomFraction)));

        return randomValue.setScale(scale, RoundingMode.HALF_UP);
    }
}
