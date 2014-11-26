package com.alignment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.alignment.html.HtmlParserTests;
import com.alignment.html.HtmlTagTests;
import com.alignment.html.TreeNodeTests;

@RunWith(Suite.class)
@SuiteClasses({ AlignmentServletTests.class,
	TreeNodeTests.class,
	HtmlTagTests.class,
	HtmlParserTests.class})
public class AllTests {
	
}
