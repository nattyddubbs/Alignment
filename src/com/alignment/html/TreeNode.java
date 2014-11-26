package com.alignment.html;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;


/**
 * A tree implementation for {@code Serializable} objects. The value of the node cannot
 * be null. The child of the node will be null if we are at the root and the
 * parent of the node will be null if we are at the head.
 * 
 * Mutator methods of this class are thread safe. Adding a child to the tree
 * node is thread safe as well as retrieving all children.
 * 
 */
public class TreeNode<T extends Serializable> implements Serializable {

	/**
	 * Unique serial UUID
	 */
	private static final long serialVersionUID = -653939266032198554L;
	/**
	 * The value of the node.
	 */
	private final T value;

	/**
	 * All of the children of the Tree node. These are first generation children
	 * only and do not include grand children.
	 */
	private final Set<TreeNode<? extends T>> children = Sets
			.newConcurrentHashSet();
	
	/**
	 * Parent of this tree node.
	 */
	private final AtomicReference<TreeNode<? extends T>> parent =
			new AtomicReference<>();

	/**
	 * Only constructor of the tree node. This constructor will fully populate
	 * the values necessary for this class.
	 * 
	 * @param val
	 *            The value of the node. These values are encouraged to be
	 *            immutable.
	 */
	public TreeNode(T val) {
		value = checkNotNull(val,
				"Must have a non-null value for tree node.");
	}

	/**
	 * Static factory method for creating a node.
	 * 
	 * @param value
	 *            The value to create the node with.
	 * 
	 * @return A newly created tree node.
	 */
	public static <E extends Serializable> TreeNode<E> create(E value) {
		return new TreeNode<E>(value);
	}

	/**
	 * Get the value associated with this tree node.
	 * 
	 * @return The non-null value associated with this tree node.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Get the children of the tree node. If the set of children is empty, then
	 * the tree node is a root.
	 * 
	 * @return The set of children associated with this tree node. This set it
	 *         unmodifiable. The set will not be null. The set is a view of the
	 *         live set and thus could change. Iteration over the set is thread
	 *         safe.
	 */
	public Set<TreeNode<? extends T>> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	/**
	 * Add a child to the tree node. This is a thread safe operation.
	 * 
	 * @param childNode
	 *            The tree node to add.
	 * 
	 * @throws NullPointerException
	 *             If a null node is attempted to be added to the node.
	 */
	public void addChild(TreeNode<? extends T> childNode) {
		children.add(checkNotNull(childNode,
				"Must have a non-null child to add to tree."));
	}

	/**
	 * Equality condition: 
	 * 1) Must be of type TreeNode 
	 * 2) Value must be equal 
	 * 3) Parent must be equal 
	 * 4) Children must be equal
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TreeNode) {
			TreeNode<?> node = (TreeNode<?>) obj;
			return node.value.equals(value)
					&& node.children.equals(children)
					&& node.parent.equals(parent);
		}
		return false;
	}

	/**
	 * Hashcode value of the node.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(value, children, parent);
	}
	
	/**
	 * Overridden toString method.
	 */
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(TreeNode.class)
				.add("Value", value)
				.add("Children", children)
				.toString();
	}

	/**
	 * Get the parent of the tree node. If the parent is null, then we are at
	 * the root of the tree.
	 * 
	 * @return The parent of the tree node. {@code null} if the tree node is the root.
	 */
	public TreeNode<? extends T> getParent() {
		return parent.get();
	}

	/**
	 * Set the parent of the tree node. 
	 * 
	 * @param create The parent of the tree node. This can be {@code null}
	 * if the tree node is the root.
	 */
	public void setParent(TreeNode<? extends T> create) {
		parent.set(create);
	}

}
