package com.alignment.html;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface HtmlTag extends Serializable{

	/**
	 * Get the type that this tag is. As an example, the {@code </p>} tag
	 * has a type of {@link TagType.P}.
	 * 
	 * @return The type that the tag is.
	 */
	TagType getTagType();

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
	String getText();

	/**
	 * Get the attributes associated with an HTML tag. The returned map could
	 * be empty but cannot be null. As an example the html snippet: 
	 * {@code 
	 * <a href="http://www.w3schools.com">This is a link</a>
	 * }
	 * would have the following attribute
	 * {@code 
	 * 	"href"=>"http://www.w3schools.com"
	 * }
	 * 
	 * @return A possibly empty map of the attributes of the HTML tag. Implementations
	 * should return views of the underlying attributes however should not allow the
	 * attributes to be modified through interactions with the map.
	 */
	Map<String, String> getAttributes();

	/**
	 * Get the identifier of the html tag. This identifier is internal and should be generated
	 * uniquely by some external source.
	 * 
	 * @return The generated identifier for the html tag.
	 */
	double getId();

	/**
	 * Get the parent of the HTML tag. If the tag is the document root, or has the type
	 * of {@link TagType.HTML} then it will have a {@code null} tag.
	 * 
	 * @return The parent tag of this html tag. If this tag is the root or the {@link TagType.HTML}
	 * type then this method will return {@code null}.
	 */
	HtmlTag getParent();

	/**
	 * Get all of the one generation children of the HTML tag. This does not include
	 * grandchildren. If the tag does not have any children then it is a leaf.
	 * 
	 * @return A non-null set of children. This set is not modifiable.
	 */
	Set<HtmlTag> getChildren();

	/**
	 * Set the parent of the HTML tag. This operation should be implemented as threadsafe.
	 * The father can be {@code null} if the tag is the root.
	 * 
	 * @param father The father to set. This can be null. 
	 */
	void setParent(HtmlTag father);

	/**
	 * Add a child to the HTML tag. This operation should be threadsafe so as not to cause exceptions
	 * with the asynchronous calling of {@link #getChildren()}.
	 * 
	 * @param abstractHtmlTag The child to add. This cannot be {@code null} as a null child is not
	 * valid. 
	 */
	void addChild(HtmlTag abstractHtmlTag);

}
