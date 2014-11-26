package com.alignment.html;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;

/**
 * Abstract implementation of the HTML tag including all fields
 * and methods that are required for all HTML tags. Many of the
 * fields of an HTML tag are final as they will not change after
 * they have been created.
 */
public abstract class AbstractHtmlTag implements HtmlTag {
	
	/**
	 * Generated UUID 
	 */
	private static final long serialVersionUID = 7335428448951743288L;

	/**
	 * The type of the HTML tag.
	 */
	protected final TagType type;
	
	/**
	 * The unique identifier of the html tag.
	 */
	protected final double id;
	
	/**
	 * The attributes of the HTML tag.
	 */
	protected final ImmutableMap<String, String> attributes;
	
	/**
	 * The node that this html tag is within the node hierarchy.
	 * Favoring composition over inheritance.
	 */
	protected final TreeNode<HtmlTag> node;
	
	/**
	 * Create an abstract html tag with all of the required parameters. It
	 * is assumed that all input values are checked for validity prior to
	 * creating the object.
	 * 
	 * @param tagType A non-null tag type.
	 * 
	 * @param tagId   The unique tag id.
	 * 
	 * @param attributes The attributes that are associated with the tag.
	 */
	protected AbstractHtmlTag(
			TagType tagType,
			double tagId, 
			Map<String, String> attributes) {
		this.type = checkNotNull(tagType, "Must have a non-null tag type");
		this.id = checkNotNull(tagId, "Must have a non-null tag id.");
		Builder<String, String> builder = ImmutableMap.builder();
		this.attributes = builder.putAll(
				checkNotNull(attributes, "Must have a non-null attribute mapping."))
				.build();
		node = TreeNode.create((HtmlTag)this);
	}
	

	/**
	 * Get the type that this tag is. As an example, the {@code </p>} tag
	 * has a type of {@link TagType.P}.
	 * 
	 * @return The type that the tag is.
	 */
	@Override
	public TagType getTagType()
	{
		return type;
	}

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
	@Override
	public Map<String, String> getAttributes()
	{
		return attributes;
	}

	/**
	 * Get the identifier of the html tag. This identifier is internal and should be generated
	 * uniquely by some external source.
	 * 
	 * @return The generated identifier for the html tag.
	 */
	@Override
	public double getId()
	{
		return id;
	}

	/**
	 * Get the parent of the HTML tag. If the tag is the document root, or has the type
	 * of {@link TagType.HTML} then it will have a {@code null} tag.
	 * 
	 * @return The parent tag of this html tag. If this tag is the root or the {@link TagType.HTML}
	 * type then this method will return {@code null}.
	 */
	@Override
	public HtmlTag getParent(){
		if(node.getParent() == null)
		{
			return null;
		}
		return node.getParent().getValue();
	}
	
	/**
	 * Threadsafe set for the parent of the html tag.
	 */
	@Override
	public void setParent(HtmlTag parent){
		if(parent != null)
		{
			setParentInternal(TreeNode.create(parent));			
		}
		else
		{
			setParentInternal(null);
		}
		if(!parent.getChildren().contains(this))
		{
			parent.addChild(this);	
		}
	}
	
	/**
	 * Threadsafe addition of child to the html node. This
	 * child will also have its parent set if it is not already.
	 * 
	 * @throws IllegalStateException If the parent of the child is already
	 * set to something other than {@code this}. 
	 */
	@Override
	public void addChild(HtmlTag child){
		addChildInternal(TreeNode.create(
				checkNotNull(child, "Must have a non-null child.")));
		//Check to see if the child already has a parent that is not
		//equal to myself. If this is the case, then we are restructuring
		//the tree which is not allowed. Once the tree structure has been
		//created, then it is immutable. Having already set a parent implies
		//that the structure is already partially in place.
	    if(child.getParent() != null)
	    {
	    	if(!child.getParent().equals(this))
	    	{
		    	throw new IllegalStateException("Child has already been assigned"
		    			+ " a parent. Tree structure of HTML tags should not be"
		    			+ " altered indirectly. If the parent of the child tag "
		    			+ " needs to be changed, then it should be changed by "
		    			+ "the calling class.");	    		
	    	}
	    	else
	    	{
	    		//Don't need to set the parent.
	    	}
	    }
	    else
	    {
		    child.setParent(this);	    	
	    }
	}
	
	/**
	 * Protected method to set the child. This does not set the parent of the
	 * child equal to {@code this} however it does add the child to the set
	 * of children. It is assumed that the child is already checked for {@code null}.
	 * 
	 * @param child A non-null child.
	 */
	protected void addChildInternal(TreeNode<? extends HtmlTag> child)
	{
		node.addChild(child);
	}
	
	/**
	 * Protected method to set the parent. This does not add an additional
	 * child to the parent.
	 * 
	 * @param parent The parent to set. This could be {@code null}.
	 */
	protected void setParentInternal(TreeNode<? extends HtmlTag> parent)
	{
		node.setParent(parent);
	}
	
	/**
	 * Get all of the one generation children of the HTML tag. This does not include
	 * grandchildren. If the tag does not have any children then it is a leaf.
	 * 
	 * @return A non-null set of children. This set is not modifiable.
	 */
	@Override
	public Set<HtmlTag> getChildren()
	{
		ImmutableSet.Builder<HtmlTag> childrenBuilder = ImmutableSet.builder();
		for(TreeNode<? extends HtmlTag> child : node.getChildren())
		{
			childrenBuilder.add(child.getValue());
		}
		return childrenBuilder.build();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(type, id, attributes);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof AbstractHtmlTag)
		{
			AbstractHtmlTag tag = (AbstractHtmlTag) obj;
			return Objects.equals(tag.attributes, attributes)
					&& Objects.equals(tag.id, id)
					&& Objects.equals(tag.type, type);
		}
		return false;
	}
	
	/**
	 * ToString is expensive. Avoid using it except in debug situations.
	 */
	@Override
	public String toString()
	{
		//Builds the html looking string.
		StringBuilder builder = new StringBuilder();
		builder.append("<").append(type.toString().toLowerCase());
		for(Entry<String, String> attrEntry : attributes.entrySet())
		{
			builder.append(" ")
				.append(attrEntry.getKey())
				.append("=")
				.append(attrEntry.getValue());
		}
		builder.append(">");
		if(getText() != null)
		{
			builder.append("\n").append(getText()).append("\n");
		}
		//Append each of the children.
		for(HtmlTag child : getChildren())
		{
			builder.append(child.toString().replaceAll("(?m)^", "   "));
		}
		//Append the closing tag.
		builder.append("\n</").append(type.toString().toLowerCase()).append(">");
		return builder.toString();
	}
}
