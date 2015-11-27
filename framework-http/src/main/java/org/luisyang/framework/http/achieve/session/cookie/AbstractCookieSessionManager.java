package org.luisyang.framework.http.achieve.session.cookie;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.http.achieve.session.AbstractSessionManager;
import org.luisyang.framework.http.servlet.Controller;
import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.util.StringHelper;
import org.luisyang.util.collection.ConcurrentLRUHashMap;
import org.luisyang.util.parser.LongParser;

public abstract class AbstractCookieSessionManager extends AbstractSessionManager {
	private static final ConcurrentLRUHashMap<Object, Object> USER_AGENT_DIGEST = new ConcurrentLRUHashMap(100000);

	public AbstractCookieSessionManager(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	protected String getTokenName() {
		return this.resourceProvider.getSystemDefine().getGUID();
	}

	public Session getSession(HttpServletRequest request, HttpServletResponse response, boolean create) {
		String tokenName = getTokenName();
		Cookie token = null;
		Cookie[] cookies = request.getCookies();
		if ((cookies != null) && (cookies.length > 0)) {
			for (Cookie cookie : cookies) {
				if (tokenName.equals(cookie.getName())) {
					token = cookie;
					break;
				}
			}
		}
		if (token == null) {
			if (!create) {
				return null;
			}
			token = new Cookie(tokenName, newToken());
			token.setHttpOnly(true);
			token.setSecure(false);
			token.setMaxAge(-1);
			token.setPath("/");
			response.addCookie(token);
		}
		return newSession(token, request, response);
	}

	protected String newToken() {
		return UUID.randomUUID().toString();
	}

	protected long getMaxIdleTime() {
		long ttl = LongParser.parse(((ConfigureProvider) this.resourceProvider.getResource(ConfigureProvider.class))
				.getProperty("SYSTEM.SESSION_MAX_IDLE_TIME"));
		if (0L == ttl) {
			return 1800000L;
		}
		return ttl;
	}

	protected abstract Session newSession(Cookie paramCookie, HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse);

	public void close() {
	}

	protected abstract class AbstractCookieSession extends AbstractSessionManager.AbstractSession implements Session {
		protected final long creationTime;
		protected final Cookie cookie;
		protected String userAgent;
		protected String ip;
		protected String userAgentDigest;

		public AbstractCookieSession(Cookie cookie, HttpServletRequest request, HttpServletResponse response) {
			super();
			this.cookie = cookie;
			this.creationTime = System.currentTimeMillis();
			Controller controller;
			try {
				controller = (Controller) AbstractCookieSessionManager.this.resourceProvider
						.getResource(Controller.class);
				this.userAgent = controller.getRemoteAgent(request);
				this.ip = controller.getRemoteAddr(request);
				if (null != this.userAgent) {
					this.userAgentDigest = ((String) AbstractCookieSessionManager.USER_AGENT_DIGEST
							.get(this.userAgent));
					if (this.userAgentDigest == null) {
						this.userAgentDigest = StringHelper.digest(this.userAgent);
						AbstractCookieSessionManager.USER_AGENT_DIGEST.put(this.userAgent, this.userAgentDigest);
					}
				}
			} catch (Throwable throwable) {
				AbstractCookieSessionManager.this.resourceProvider.log(throwable);
			}
		}

		public String getRemoteIP() {
			return this.ip;
		}

		public String getToken() {
			return this.cookie.getValue();
		}

		public void invalidate(HttpServletRequest request, HttpServletResponse response) {
			this.cookie.setMaxAge(0);
			response.addCookie(this.cookie);
		}
	}
}
