package com.alignment.html;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.alignment.html.parser.HtmlParser;

/**
 * Tests for parsing HTML. There are a lot of tests in here as this is the core functionality.
 */
public class HtmlParserTests {

	final String ATTRIBUTE_TEST = "<link rel=\"stylesheet\" type=\"text/css\" href=\"//cdn.sstatic.net/stackoverflow/all.css?v=fd40bcfb3c2e\">";
	
	static
	{
		Logger logger = Logger.getLogger(HtmlParser.class.getSimpleName());
		logger.setLevel(Level.INFO);
	}
	
	@Test
	public void testAttributeParsing()
	{
		
		HtmlTag tag = HtmlParser.parseSingleTag(ATTRIBUTE_TEST);
		assertNotNull(tag);
		assertTrue(tag.getTagType().equals(TagType.HTML));
		
		//Make sure the tag has one child.
		assertTrue(tag.getChildren().size() == 1);
		HtmlTag child = tag.getChildren().iterator().next();
		assertNotNull(child);
		assertTrue(child.getTagType().equals(TagType.HEAD));
		
		assertTrue(child.getChildren().size() == 1);
		child = child.getChildren().iterator().next();
		assertTrue(child.getTagType().equals(TagType.LINK));
		
		//Make sure the attributes are set correctly.
		assertTrue(child.getAttributes().get("rel").equals("stylesheet"));
		assertTrue(child.getAttributes().get("type").equals("text/css"));
		assertTrue(child.getAttributes().get("href").equals("//cdn.sstatic.net/stackoverflow/all.css?v=fd40bcfb3c2e"));
	}
	
}
