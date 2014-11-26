package com.alignment.html.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alignment.html.HtmlTag;
import com.alignment.html.TagBuilder;
import com.alignment.html.TagType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * HTML parser for parsing out HTML into the custom objects.
 * @author Home
 *
 */
public class HtmlParser {

	/**
	 * Logging utility.
	 */
	private static Logger logger = Logger.getLogger(HtmlParser.class.getSimpleName());
	
	/**
	 * Parse the provided html {@code String} into our well known
	 * format.
	 * 
	 * @param str The string that we are parsing.
	 * 
	 * @return The html tag structure reflecting the provided html.
	 */
	public static HtmlTag parseSingleTag(String str) {
		HtmlTag root = null;
		long time = System.nanoTime();
		try {
			HtmlHandler handler = new HtmlHandler();
			SAXParserImpl.newInstance(null).parse(new ByteArrayInputStream(str.getBytes("UTF-8")), handler);
			root = handler.getRoot();
		} catch (SAXException | IOException e) {
			logger.log(Level.SEVERE, "Encountered an exception while parsing HTML.", e);
		}
		if(logger.getLevel() == Level.INFO)
		{
			logger.log(Level.INFO, "Parsing HTML took " + (System.nanoTime() - time) / 1E6 + "ms.");
		}
		return root;
	}
	
	/**
	 * Constructor for the default handler that will enable us to parse HTML.
	 * @author Home
	 *
	 */
	private static class HtmlHandler extends DefaultHandler
	{
		/**
		 * The stack of builders that we are using to construct the html document.
		 */
		private final Stack<TagBuilder> builderStack = new Stack<TagBuilder>();
		
		/**
		 * The root of the HTML document.
		 */
		private HtmlTag root = null;
		
		/**
		 * The start of the document.
		 */
	    @Override
		public void startDocument() throws SAXException {
	        System.out.println("start document   : ");
	    }

	    /**
	     * Get the root of the built HTML structure.
	     * @return
	     */
	    public HtmlTag getRoot() {
			return root;
		}

		@Override
		public void endDocument() throws SAXException {
	        System.out.println("end document     : ");
	    }

	    @Override
		public void startElement(String uri, String localName,
	        String qName, Attributes attributes)
	    throws SAXException {
	    	TagBuilder builder = TagBuilder.create();
	    	try
	    	{
	    		builder.ofType(TagType.valueOf(localName.toUpperCase()));	    		
	    	}
	    	catch(Exception e)
	    	{
	    		logger.log(Level.INFO, "Unable to find corresponding tag for tag type " 
	    					+ localName.toUpperCase(), e);
	    		builder.ofType(TagType.DEFAULT);
	    	}

	    	//Set the attributes if they exist.
	    	if(attributes.getLength() > 0)
	    	{
		    	Builder<String, String> immutableBuilder = ImmutableMap.builder();
	    		for(int i = 0; i < attributes.getLength() ; i++)
	    		{
	    			immutableBuilder.put(attributes.getLocalName(i), attributes.getValue(i));
	    		}
	    		builder.withAttributes(immutableBuilder.build());
	    	}
	    	builderStack.add(builder);
	    }

	    @Override
		public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	    	TagBuilder builder = builderStack.pop();
	    	HtmlTag tag = builder.build();
	    	
	    	//Set the parent - child relationship if applicable.
	    	if(!builderStack.isEmpty())
	    	{
	    		builderStack.peek().andChild(tag);
	    	}
	    	else
	    	{
	    		root = tag;
	    	}
	        System.out.println("end element      : " + localName);
	    }

	    @Override
		public void characters(char ch[], int start, int length)
	    throws SAXException {
	    	TagBuilder builder = builderStack.pop();
	    	builder.withText(new String(ch));	        
	    }
	}

}
