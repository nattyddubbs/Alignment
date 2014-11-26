package com.alignment.html;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

/**
 * Tests for the HTML Tag class.
 *
 */
public class HtmlTagTests {

	
	@Test
	public void testGetters()
	{
		
		HtmlTag tag = TagBuilder.create().ofType(TagType.P).withText("Hello").withAttributes(new HashMap<String, String>()).build();
		
		//Check the state of the tag.
		assertTrue(tag.getTagType().equals(TagType.P));
		assertTrue(tag.getText().equals("Hello"));
		assertTrue(tag.getAttributes().isEmpty());	
		
		//Make sure that the tag does not have any children or a parent.
		assertNull(tag.getParent());
		assertNotNull(tag.getChildren());
		assertTrue(tag.getChildren().isEmpty());
	}
	
	@Test
	public void testSettingParent()
	{
		HtmlTag father = TagBuilder.create()
				.ofType(TagType.P)
				.withText("Father")
				.withAttributes(new HashMap<String, String>())
				.build();
		
		HtmlTag son = TagBuilder.create()
				.ofType(TagType.P)
				.withText("Son")
				.withAttributes(new HashMap<String, String>())
				.build();		
		
		assertNull(son.getParent());
		assertTrue(son.getChildren().isEmpty());
		assertTrue(father.getChildren().isEmpty());
		
		son.setParent(father);
		assertTrue(son.getParent().equals(father));
		assertTrue(father.getChildren().contains(son));
		assertTrue(father.getChildren().size() == 1);
	}
	
	@Test
	public void testSettingChild()
	{
		HtmlTag father = TagBuilder.create()
				.ofType(TagType.P)
				.withText("Father")
				.withAttributes(new HashMap<String, String>())
				.build();
		
		HtmlTag son = TagBuilder.create()
				.ofType(TagType.P)
				.withText("Son")
				.withAttributes(new HashMap<String, String>())
				.build();		
		
		assertNull(son.getParent());
		assertTrue(son.getChildren().isEmpty());
		assertTrue(father.getChildren().isEmpty());
		
		father.addChild(son);
		assertTrue(son.getParent().equals(father));
		assertTrue(father.getChildren().contains(son));
		assertTrue(father.getChildren().size() == 1);
		
		//Add the son again and make sure that the size of the children does
		//not change.
		father.addChild(son);
		assertTrue(son.getParent().equals(father));
		assertTrue(father.getChildren().contains(son));
		assertTrue(father.getChildren().size() == 1);
	}
	
	
	@Test
	public void testBuilder()
	{
		
	}
	
}
