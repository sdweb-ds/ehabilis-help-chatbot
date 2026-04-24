
package es.sdweb.application.vista.util;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Antonio Carro Mariño
 */
public class SessionCounter implements HttpSessionListener {
    private List sessions = new ArrayList();

    public SessionCounter() {
    }

    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.add(session.getId());

        session.setAttribute("counter", this);
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.remove(session.getId());

        session.setAttribute("counter", this);
    }

    public int getActiveSessionNumber() {
        return sessions.size();
    }
}

