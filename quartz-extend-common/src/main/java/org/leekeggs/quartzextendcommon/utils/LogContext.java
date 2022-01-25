package org.leekeggs.quartzextendcommon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.util.Assert;

/**
 * 日志工具.
 *
 * @author redcoder54
 * @since 1.0.0
 */
public class LogContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogContext.class);

    private static final ThreadLocal<LogContext> LOCAL = new ThreadLocal<>();

    /**
     * 是否自动换行，如果为true，在调用{@link #append(Object...)}后，自动调用{@link #wrap()}
     */
    private final boolean autoWrap;

    /**
     * 消息容器，存储待输出的消息
     */
    private final StringBuilder messageContainer;

    private Level level;

    /**
     * 构建LogContext实例
     *
     * @param autoWrap 是否自动换行，如果为true，在调用{@link #append(Object...)}后，自动调用{@link #wrap()}
     * @param level    日志输出级别
     */
    private LogContext(boolean autoWrap, Level level) {
        this.autoWrap = autoWrap;
        this.level = level;
        messageContainer = new StringBuilder();
    }

    /**
     * 获取一个新的LogContext实例，默认配置：<code>autoWrap=true, level=Level.INFO</code>
     *
     * @return 新创建的LogContext实例
     */
    public static LogContext createInstance() {
        return createInstance(true, Level.INFO);
    }

    /**
     * 获取一个新的LogContext实例
     *
     * @param autoWrap 是否自动换行，如果为true，在调用{@link #append(Object...)}后，自动调用{@link #wrap()}
     * @param level    日志输出级别
     * @return 新创建的LogContext实例
     */
    public static LogContext createInstance(boolean autoWrap, Level level) {
        return new LogContext(autoWrap, level);
    }

    /**
     * 获取与当前线程绑定的LogContext实例，如果当前线程未绑定LogContext实例，抛出异常{@link IllegalStateException}
     *
     * @throws IllegalStateException 当前线程未绑定LogContext
     * @since 1.2.0
     */
    public static LogContext getThreadBoundInstance() {
        LogContext logContext = LOCAL.get();
        if (logContext == null) {
            throw new IllegalStateException("当前线程未绑定LogContext");
        }
        return logContext;
    }

    /**
     * 将LogContext实例与当前线程绑定
     *
     * @param logContext 待绑定的LogContext实例
     * @throws IllegalStateException 如果当前线程已绑定LocalContext，抛出异常
     * @since 1.2.0
     * @deprecated 使用 {@link #bindCurrentThread()} 替代该方法
     */
    @Deprecated
    public static void bindInstance(LogContext logContext) {
        Assert.notNull(logContext, "'logContext' must not be null");
        if (LOCAL.get() != null) {
            throw new IllegalStateException("当前线程已绑定LocalContext，禁止重复绑定");
        }
        LOCAL.set(logContext);
    }

    /**
     * 将LogContext实例与当前线程绑定
     *
     * @throws IllegalStateException 如果当前线程已绑定LocalContext，抛出异常
     * @since 1.4.2
     */
    public LogContext bindCurrentThread() {
        if (LOCAL.get() != null) {
            throw new IllegalStateException("当前线程已绑定LocalContext，禁止重复绑定");
        }
        LOCAL.set(this);
        return this;
    }

    /**
     * 添加要输出的消息，不改变日志级别
     *
     * @param messages 消息
     * @return 当前LogContext
     */
    public synchronized LogContext append(Object... messages) {
        for (Object msg : messages) {
            messageContainer.append(msg);
        }
        return autoWrap ? wrap() : this;
    }

    /**
     * 添加要输出的消息，改变日志级别
     *
     * @param level    日志级别
     * @param messages 消息
     * @return 当前LogContext
     * @since 1.2.0
     */
    public synchronized LogContext append(Level level, Object... messages) {
        Assert.notNull(level, "'level' must not be null");
        this.level = level;
        return append(messages);
    }

    /**
     * 更改日志级别
     *
     * @param level 日志级别
     * @return 当前LogContext
     * @since 1.4.2
     */
    public LogContext level(Level level) {
        this.level = level;
        return this;
    }

    /**
     * 添加要输出的消息，支持{}占位符
     *
     * @param format    消息
     * @param arguments 参数
     * @return 当前LogContext
     * @since 1.4.2
     */
    public synchronized LogContext appendF(String format, Object... arguments) {
        if (arguments.length == 0) {
            messageContainer.append(format);
            return autoWrap ? wrap() : this;
        }
        String msg = String.format(format.replaceAll("\\{}", "%s"), arguments);
        messageContainer.append(msg);
        return autoWrap ? wrap() : this;
    }

    /**
     * 换行
     */
    public synchronized LogContext wrap() {
        messageContainer.append("\n");
        return this;
    }

    /**
     * 输出日志。等于调用print(true)
     */
    public synchronized void print() {
        print(true);
    }

    /**
     * 输出日志
     *
     * @param remove true-调用{@link ThreadLocal#remove()}
     */
    public synchronized void print(boolean remove) {
        try {
            String msg = messageContainer.toString();
            if (level.toInt() == Level.ERROR.toInt()) {
                LOGGER.error(msg);
            } else if (level.toInt() == Level.WARN.toInt()) {
                LOGGER.warn(msg);
            } else {
                LOGGER.info(msg);
            }
        } finally {
            if (remove) {
                remove();
            }
        }
    }

    /**
     * 获取日志内容
     *
     * @param remove true-调用{@link ThreadLocal#remove()}
     * @return 日志内容
     */
    public String getLogContent(boolean remove) {
        try {
            return messageContainer.toString();
        } finally {
            if (remove) {
                remove();
            }
        }
    }

    public Level getLogLevel() {
        return level;
    }

    /**
     * Removes the current thread's value for this thread-local
     * variable.
     */
    public synchronized void remove() {
        LOCAL.remove();
    }
}
