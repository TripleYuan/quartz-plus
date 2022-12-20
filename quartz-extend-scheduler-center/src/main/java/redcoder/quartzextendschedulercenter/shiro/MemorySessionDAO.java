package redcoder.quartzextendschedulercenter.shiro;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 本地存储session
 *
 * @author redcoder
 * @since 2022-12-20
 */
@Service
public class MemorySessionDAO extends AbstractSessionDAO implements InitializingBean {

    @Value("${shiro.session.expire-in-days}")
    private int expireInDays;

    private Cache<Serializable, Session> cache;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        cache.put(sessionId, session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return cache.getIfPresent(sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        Serializable sessionId = session.getId();
        if (cache.getIfPresent(sessionId) == null) {
            throw new UnknownSessionException("session不存在，session: " + session);
        }
        cache.put(sessionId, session);
    }

    @Override
    public void delete(Session session) {
        cache.invalidate(session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return cache.asMap().values();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = CacheBuilder.newBuilder()
                .maximumSize(Integer.MAX_VALUE)
                .expireAfterWrite(expireInDays, TimeUnit.DAYS)
                .build();
    }
}
