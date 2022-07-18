package org.steve.hockeyware.util.hwid;

import org.steve.hockeyware.HockeyWare;

public class NoStackTraceThrowable extends RuntimeException {

    public NoStackTraceThrowable(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override
    public String toString() {
        return "" + HockeyWare.VERSION;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
