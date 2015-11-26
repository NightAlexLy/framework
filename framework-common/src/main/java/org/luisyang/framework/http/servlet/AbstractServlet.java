package org.luisyang.framework.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.luisyang.framework.config.ConfigureProvider;
import org.luisyang.framework.http.servlet.annotation.Right;
import org.luisyang.framework.http.session.Session;
import org.luisyang.framework.http.session.SessionManager;
import org.luisyang.framework.http.session.authentication.AuthenticationException;
import org.luisyang.framework.resource.PromptLevel;
import org.luisyang.framework.resource.ResourceProvider;
import org.luisyang.framework.resource.ResourceRegister;
import org.luisyang.framework.service.ServiceProvider;
import org.luisyang.framework.service.ServiceSession;


/**
 * <b>抽象控制器</b>
 * <ul>
 * 	  <li>提供基本处理方法。doGet/doPost</li>
 * 	  <li>processGet/processPost 提供基本流程处理方法。Get默认调用Post方法。</li>
 * 	  <li>forward/forwardXX跳转到不同的地址</li>
 * 	  <li>sendRedirect重定向到不同的地址</li>
 * </ul>
 * 
 * @author LuisYang
 */
public abstract class AbstractServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 3086524142001612055L;
	protected final Logger logger = Logger.getLogger(getClass());

	protected ResourceProvider getResourceProvider() {
		return ResourceRegister.getResourceProvider(getServletContext());
	}

	protected Controller getController() {
		return (Controller) ResourceRegister.getResourceProvider(getServletContext()).getResource(Controller.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ResourceProvider resourceProvider = getResourceProvider();
			ConfigureProvider configureProvider = (ConfigureProvider) resourceProvider
					.getResource(ConfigureProvider.class);
			if (configureProvider != null) {
				request.setCharacterEncoding(resourceProvider.getCharset());
				response.setCharacterEncoding(resourceProvider.getCharset());
			}
			Session session = ((SessionManager) resourceProvider.getResource(SessionManager.class)).getSession(request,
					response);
			if ((mustAuthenticated()) && ((session == null) || (!session.isAuthenticated()))) {
				throw new AuthenticationException();
			}
			Right resource = (Right) getClass().getAnnotation(Right.class);
			if ((resource != null) && (!session.isAccessableResource(resource))) {
				onAccesssDenied(request, response, session, resource);
				return;
			}
			ServiceSession serviceSession = ((ServiceProvider) resourceProvider.getResource(ServiceProvider.class))
					.createServiceSession(session, getController().getPrompter(request));
			Throwable localThrowable2 = null;
			try {
				processGet(request, response, serviceSession);
			} catch (Throwable localThrowable1) {
				localThrowable2 = localThrowable1;
				throw localThrowable1;
			} finally {
				if (serviceSession != null) {
					if (localThrowable2 != null) {
						try {
							serviceSession.close();
						} catch (Throwable x2) {
							localThrowable2.addSuppressed(x2);
						}
					} else {
						serviceSession.close();
					}
				}
			}
		} catch (Throwable throwable) {
			onThrowable(request, response, throwable);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ResourceProvider resourceProvider = getResourceProvider();
			ConfigureProvider configureProvider = (ConfigureProvider) resourceProvider
					.getResource(ConfigureProvider.class);
			if (configureProvider != null) {
				request.setCharacterEncoding(resourceProvider.getCharset());
				response.setCharacterEncoding(resourceProvider.getCharset());
			}
			Session session = ((SessionManager) resourceProvider.getResource(SessionManager.class)).getSession(request,
					response);
			if ((mustAuthenticated()) && ((session == null) || (!session.isAuthenticated()))) {
				throw new AuthenticationException();
			}
			Right resource = (Right) getClass().getAnnotation(Right.class);
			if ((resource != null) && (!session.isAccessableResource(resource))) {
				onAccesssDenied(request, response, session, resource);
				return;
			}
			ServiceSession serviceSession = ((ServiceProvider) resourceProvider.getResource(ServiceProvider.class))
					.createServiceSession(session, getController().getPrompter(request));
			Throwable localThrowable2 = null;
			try {
				processPost(request, response, serviceSession);
			} catch (Throwable localThrowable1) {
				localThrowable2 = localThrowable1;
				throw localThrowable1;
			} finally {
				if (serviceSession != null) {
					if (localThrowable2 != null) {
						try {
							serviceSession.close();
						} catch (Throwable x2) {
							localThrowable2.addSuppressed(x2);
						}
					} else {
						serviceSession.close();
					}
				}
			}
		} catch (Throwable throwable) {
			onThrowable(request, response, throwable);
		}
	}

	protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	protected abstract void processPost(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, ServiceSession paramServiceSession) throws Throwable;

	protected boolean mustAuthenticated() {
		return true;
	}

	protected void onAccesssDenied(HttpServletRequest request, HttpServletResponse response, Session session,
			Right resource) throws ServletException, IOException {
		Controller controller = (Controller) getResourceProvider().getResource(Controller.class);

		this.logger.error(String.format("User %s access resource %s from %s ,UserAgent:%s denied.",
				new Object[] {
						(session == null) || (!session.isAuthenticated()) ? ""
								: Integer.toString(session.getAccountId()),
						resource.id(), controller.getRemoteAddr(request), controller.getRemoteAgent(request) }));

		response.sendError(403, "拒绝访问");
	}

	protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		this.logger.error("AbstractServlet.onThrowable", throwable);
		if ((throwable instanceof AuthenticationException)) {
			throw ((AuthenticationException) throwable);
		}
		prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
	}

	public void prompt(HttpServletRequest request, HttpServletResponse response, PromptLevel level, String message) {
		getController().prompt(request, response, level, message);
	}

	public void clearPrompts(HttpServletRequest request, HttpServletResponse response, PromptLevel level) {
		getController().clearPrompts(request, response, level);
	}

	public void clearAll(HttpServletRequest request, HttpServletResponse response) {
		getController().clearAll(request, response);
	}

	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String location)
			throws ServletException, IOException {
		getController().sendRedirect(request, response, location);
	}

	public void forward(HttpServletRequest request, HttpServletResponse response, String path)
			throws ServletException, IOException {
		getController().forward(request, response, path);
	}

	public void forwardController(HttpServletRequest request, HttpServletResponse response,
			Class<? extends HttpServlet> controller) throws ServletException, IOException {
		getController().forwardController(request, response, controller);
	}

	public void forwardView(HttpServletRequest request, HttpServletResponse response,
			Class<? extends HttpServlet> controller) throws ServletException, IOException {
		getController().forwardView(request, response, controller);
	}
}