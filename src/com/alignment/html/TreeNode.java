package com.alignment.html;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Iterator;

/**
 * A tree implementation for serializable objects. The value of the node cannot
 * be null. The child of the node will be null if we are at the root and the
 * parent of the node will be null if we are at the head.
 * 
 */
public class TreeNode<T extends Serializable> implements Iterable<TreeNode<T>> {

	/**
	 * The value of the node.
	 */
	private final T value;

	public TreeNode(T value) {
		this.value = checkNotNull(value,
				"Must have a non-null value for tree node.");
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
