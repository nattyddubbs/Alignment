package com.alignment.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alignment.store.EmailLinkStore;
import com.google.appengine.api.datastore.EntityNotFoundException;

@SuppressWarnings("serial")
public class AlignmentServlet extends HttpServlet {

	private final EmailLinkStore store = new EmailLinkStore();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		logHitAndRedirect(req, resp);
	}

	private void logHitAndRedirect(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// Get a parameter.
		String param = req.getParameter("id");
		if (param != null) {
			handleRedirect(resp, param);
		} else {
			handleUnknown(resp);
		}
	}

	private void handleUnknown(HttpServletResponse resp) throws IOException {
		// If there isn't an id, then we have to notify.
		resp.sendError(404, "No redirect identifier specified");
	}

	private void handleRedirect(HttpServletResponse resp, String param)
			throws IOException {
		System.out.println(param);
		// Look up the id as known in the database.
		try {
			resp.sendRedirect(getRedirect(param));
		} catch (EntityNotFoundException e) {
			resp.sendError(404, "Unknown redirect.");
		}
	}

	private String getRedirect(String param) throws EntityNotFoundException {
		return store.getRedirect(param);
	}
}
