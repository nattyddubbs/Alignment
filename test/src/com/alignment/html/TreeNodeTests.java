package com.alignment.html;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;

public class TreeNodeTests {

	@Test
	public void testCreateTreeNode() {
		// Test that the creation of a tree node is checked.
		TreeNode<String> node = new TreeNode<>("Man");

		assertTrue(node.getValue().equals("Man"));

		try {
			node = new TreeNode<>(null);
			fail("Should not be able to construct with null.");
		} catch (Exception e) {
			// Success case.
		}
	}

	@Test
	public void getChildren() {
		TreeNode<String> node = new TreeNode<>("Man");

		TreeNode<String> child = TreeNode.create("Child");
		
		// Add some children to the node.
		node.addChild(child);

		assertTrue(node.getChildren().contains(child));
		assertFalse(node.getChildren().contains(TreeNode.create("Man")));
		
		//Try to add a null child. this shouldn't work.
		try
		{
			node.addChild(null);
			fail("Should not be able to add a null child.");
		}
		catch(Exception e)
		{
			//Success case.
		}
		
		//Make sure that we cannot modify the collection of children.
		Set<TreeNode<? extends String>> children = node.getChildren();
		try
		{
			children.add(TreeNode.create("Baby"));
			fail("Should not be able to modify returned set of children.");
		}
		catch (Exception e)
		{
			//Success case.
		}
	}
	
	@Test
	public void getParent()
	{
		TreeNode<String> node = TreeNode.create("Man");
		
		try
		{
			node.setParent(null);
		}
		catch(Exception e)
		{
			//failure case.
			fail("Should be able to set a null parent for a node. This"
					+ " means that it is the root.");
		}
		
		node.setParent(TreeNode.create("Father"));
		
		assertTrue(node.getParent().getValue().equals("Father"));
	}
}
