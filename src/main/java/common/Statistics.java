package common;

import java.util.Arrays;
import java.util.List;

public class Statistics {

    /**
     * Calculates the median of the given list of values.
     *
     * @param values the values
     * @return the median of the given values
     */
    public static long median(List<Long> values) {
        int numValues = values.size();
        long[] array = new long[numValues];
        for (int i = 0; i < numValues; i++) {
            array[i] = values.get(i);
        }
        return median(array);
    }

    /**
     * Calculates the median of the given values.
     *
     * @param values the values
     * @return the median of the given values
     */
    public static long median(long[] values) {
        Arrays.sort(values);
        int length = values.length;
        int middle = length / 2;
        if (length % 2 == 0) return (values[middle] + values[middle - 1]) / 2;
        else return values[middle];
    }


/**
 * Calculates the average of the given list of values.
 *
 * @param values the values
 * @return the average of the given values
 */
public static long average(List<Long> values) {
    int numValues = values.size();
    long sum=0;
    long[] array = new long[numValues];
    for (int i = 0; i < numValues; i++) {
        sum = sum + values.get(i);
    }
    return (sum/numValues);
}

}
