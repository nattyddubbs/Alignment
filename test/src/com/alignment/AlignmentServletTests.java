package com.alignment;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.alignment.servlets.AlignmentServlet;
import com.alignment.store.EmailLinkStore;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class AlignmentServletTests extends Mockito {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(),
			new LocalMemcacheServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testAlignmentPut() throws Exception {
		EmailLinkStore store = new EmailLinkStore();

		// Lets put some redirect stuff in there.
		// Bob likes google.
		store.createUrlRedirect("BOB", "http://www.google.com/");

		// Now ask the alignment service to get the stuff.
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getParameter("id")).thenReturn("BOB");

		HttpServletResponse response = mock(HttpServletResponse.class);
		final Map<String, Object> out = new HashMap<>();
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				out.put("REDIRECT", invocation.getArguments()[0]);
				return null;
			}

		}).when(response).sendRedirect(anyString());

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				out.put("ERROR", invocation.getArguments()[1]);
				return null;
			}

		}).when(response).sendError(anyInt(), anyString());

		new AlignmentServlet().doGet(request, response);

		System.out.println(out);

		assertNotNull(out.get("REDIRECT"));
	}

}
