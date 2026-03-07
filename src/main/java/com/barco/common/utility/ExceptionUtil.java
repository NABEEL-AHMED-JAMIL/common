package com.barco.common.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
public final class ExceptionUtil {

    /**
     * Returns the root cause of the given throwable. The root cause is the last Throwable in the cause chain.
     * If the input throwable has no cause, or if the cause chain is cyclic, the original throwable is returned.
     *
     * @param throwable the Throwable to analyze
     * @return the root cause of the Throwable, or the original Throwable if no root cause is found
     */
    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        Throwable rootCause = list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
        if (BarcoUtil.isNull(rootCause)) {
            return throwable;
        }
        return rootCause;
    }

    /**
     * Returns the message of the root cause of the given throwable.
     *
     * @param throwable the Throwable to analyze
     * @return the message of the root cause, or the message of the original throwable if no root cause is found
     */
    public static String getRootCauseMessage(final Throwable throwable) {
        Throwable root = getRootCause(throwable);
        return root.toString();
    }

    /**
     * Returns a list of Throwable objects starting from the given throwable and following the cause chain.
     * The list will contain each unique Throwable in the chain, stopping if a cycle is detected.
     *
     * @param throwable the starting Throwable
     * @return a List of Throwable objects in the cause chain
     */
    private static List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<Throwable>();
        while (!BarcoUtil.isNull(throwable) && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }

}