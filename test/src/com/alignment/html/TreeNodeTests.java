package com.alignment.html;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeNodeTests {

	@Test
	public void testCreateTreeNode() {
		// Test that the creation of a tree node is checked.
		TreeNode<String> node = new TreeNode<>("Man");
		
		try
		{
			node = new TreeNode<>(null);
			fail("Should not be able to construct with null.");
		}
	}
}
