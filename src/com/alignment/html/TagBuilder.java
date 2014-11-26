package com.alignment.html;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Builder class for HTML tags. This class should be used instead of directly
 * instantiating the tag classes because it provides a DSL specifically for
 * creating the tags. The implementation of the tag is also hidden from the
 * user.
 * 
 */
public class TagBuilder {
	
	/**
	 * The type of the tag.
	 */
	private TagType tagType;
	
	/**
	 * The id of the tag.
	 */
	private double tagId;
	
	/**
	 * The text to set on the html tag.
	 */
	private String tagText = "";
	
	/**
	 * The attributes to set on the tag.
	 */
	private Map<String, String> attributes = Maps.newHashMap();
	
	/**
	 * The children that will need to be added to the html tag.
	 */
	private final LinkedList<HtmlTag> children = new LinkedList<>();
	
	/**
	 * Private constructor to force the use of the static factory methods.
	 */
	private TagBuilder(){
		//TODO need logic here to generate the unique identifier for the tag.
	}

	/**
	 * Create a default HTML tag. This creation method should be
	 * used for tags that have text embedded in them but are not
	 * as suitable for tags that do not have text.
	 * 
	 * @return A 
	 */
	public static TagBuilder create() {
		return new TagBuilder();
	}

	/**
	 * Set the type of the html tag. 
	 * 
	 * @param p The type to set the tag to. This cannot be null.
	 * 
	 * @return The builder.
	 * 
	 * @throws NullPointerException if the provided type is {@code null}.
	 */
	public TagBuilder ofType(TagType p) {
		tagType = checkNotNull(p, "Must have a non-null tag type.");
		return this;
	}
	
	/**
	 * Add a child that will be linked to the created tag.
	 * 
	 * @param tag The child tag to add.
	 */
	public void andChild(HtmlTag tag) {
		children.add(checkNotNull(tag, "Cannot have a null child."));
	}

	/**
	 * Set the text content of the tag.
	 * 
	 * @param string The text content of the tag. {@code null} is an acceptable
	 * value for the text.
	 * 
	 * @return The builder.
	 */
	public TagBuilder withText(String string) {
		tagText = string;
		return this;
	}

	/**
	 * Set the map of attribute names to values. This map cannot be null be can
	 * be empty if there are no attributes.
	 * 
	 * @param newHashMap
	 * @return
	 */
	public TagBuilder withAttributes(Map<String, String> newHashMap) {
		attributes = checkNotNull(newHashMap, "Must have a non-null hash map of attributes.");
		return this;
	}

	/**
	 * Create a new HTML tag object, setting the values as provided by the builder.
	 * If the required values are not set, then an illegal state exception is thrown
	 * indicating that the builder needs to have more values set on it.
	 * 
	 * @return A new HTML tag.
	 * 
	 * @throws IllegalStateException if there are missing values on the builder.
	 */
	public HtmlTag build() {
		StringBuilder errorMessage = new StringBuilder();
		if(tagText == null)
		{
			errorMessage.append("Missing the Tag Text for the new HTML tag.");
		}
		if(tagType == null)
		{
			if(errorMessage.length() > 0)
			{
				errorMessage.append("\n");
			}
			errorMessage.append("Missing the Tag Type for the new HTML tag.");
		}
		if(errorMessage.length() > 0)
		{
			throw new IllegalStateException(errorMessage.toString());
		}
		HtmlTag tag = new DefaultHtmlTextTag(tagType, tagId, tagText, attributes);
		if(!children.isEmpty())
		{
			for(HtmlTag child : children)
			{
				tag.addChild(child);
			}
		}
		return tag;
	}
	
	/**
	 * Default implementation of an html text tag.
	 */
	private static class DefaultHtmlTextTag extends AbstractHtmlTag
	{
		private final String tagText;
		
		/**
		 * Constructor accepting all required fields for the text tag. It
		 * is assumed that all fields are checked for null prior to setting
		 * them.
		 * 
		 * @param tagType
		 * @param tagId
		 * @param tagText
		 * @param attributes
		 */
		private DefaultHtmlTextTag(TagType tagType, double tagId,
				String tagText, Map<String, String> attributes) {
			super(tagType, tagId, attributes);
			this.tagText = tagText;
		}
		
		/**
		 * Get the text that the tag wraps. As an example the html snippet 
		 * {@code
		 *  <p>Hello sir how are you today?</p>
		 * }
		 * has a text value of {@code "Hello sir how are you today?"}.
		 * 
		 * @return The text value of the html tag. This could be {@code null}
		 * if the html tag does not have any text content.
		 */
		@Override
		public String getText() {
			return tagText;
		}	
	}
}
