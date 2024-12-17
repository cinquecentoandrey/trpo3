package com.cinquecento.weathermodel.util;

public class MembershipUtils {

    private MembershipUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates the degree of membership for a "low" fuzzy set.
     *
     * @param value the current value (temperature or humidity)
     * @param lowBound the minimum boundary for the low range
     * @param midPoint the midpoint where membership starts decreasing to zero
     * @return the degree of membership in the "low" fuzzy set (between 0 and 1)
     */
    public static double calculateLowMembership(double value, double lowBound, double midPoint) {
        return Math.max(0, Math.min(1, (midPoint - value) / (midPoint - lowBound)));
    }

    /**
     * Calculates the degree of membership for a "medium" fuzzy set.
     *
     * @param value the current value (temperature or humidity)
     * @param lowPoint the lower boundary of the medium range
     * @param highPoint the upper boundary of the medium range
     * @return the degree of membership in the "medium" fuzzy set (between 0 and 1)
     */
    public static double calculateMediumMembership(double value, double lowPoint, double highPoint) {
        return Math.max(0, Math.min(1, 1 - Math.abs(value - (lowPoint + highPoint) / 2) / ((highPoint - lowPoint) / 2)));
    }

    /**
     * Calculates the degree of membership for a "high" fuzzy set.
     *
     * @param value the current value (temperature or humidity)
     * @param midPoint the midpoint where membership starts increasing
     * @param highBound the maximum boundary for the high range
     * @return the degree of membership in the "high" fuzzy set (between 0 and 1)
     */
    public static double calculateHighMembership(double value, double midPoint, double highBound) {
        return Math.max(0, Math.min(1, (value - midPoint) / (highBound - midPoint)));
    }

}
