package com.recursivechaos.rcbot.plugins.persistence;

/**
 * DAO
 * Handles most of the generic hibernate calls
 * 
 * @author Andrew Bell
 */
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DAO {
	private static final Logger log = Logger.getAnonymousLogger();
	private static final ThreadLocal session = new ThreadLocal();
	@SuppressWarnings("deprecation")
	private static final SessionFactory sessionFactory = new Configuration()
			.configure(
					"/com/recursivechaos/rcbot/plugins/persistence/hibernate.cfg.xml")
			.buildSessionFactory();

	/**
	 * closes current hibernate session
	 */
	protected static void close() {
		getSession().close();
		DAO.session.set(null);
	}

	/**
	 * Returns the active session, or creates one
	 * 
	 * @return session from sessionFactory
	 */
	protected static Session getSession() {
		Session session = (Session) DAO.session.get();
		if (session == null) {
			session = sessionFactory.openSession();
			DAO.session.set(session);
		}
		return session;
	}

	protected DAO() {
	}

	/**
	 * Begins hibernate transaction
	 */
	protected void begin() {
		getSession().beginTransaction();
	}

	/**
	 * Commits hibernate transaction
	 */
	protected void commit() {
		getSession().getTransaction().commit();
	}

	/**
	 * commits, and closes transaction, canceling if caught error
	 */
	void rollback() {
		try {
			getSession().getTransaction().commit();
		} catch (HibernateException e) {
			log.log(Level.WARNING, "Cannot rollback", e);
		}

		try {
			getSession().close();
		} catch (HibernateException e) {
			log.log(Level.WARNING, "Cannot close", e);
		}
		DAO.session.set(null);
	}
}
