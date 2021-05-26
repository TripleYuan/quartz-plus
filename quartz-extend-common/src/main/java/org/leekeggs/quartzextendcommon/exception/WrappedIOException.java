package org.leekeggs.quartzextendcommon.exception;

import java.io.IOException;

/**
 * 包装IOException，将其转化为unchecked exception
 *
 * @author leekeggs
 * @since 1.0.0
 */
public class WrappedIOException extends RuntimeException {

    public WrappedIOException(String message) {
        super(message);
    }

    public WrappedIOException(IOException e) {
        super(e);
    }

    public WrappedIOException(String message, IOException e) {
        super(message, e);
    }
}
