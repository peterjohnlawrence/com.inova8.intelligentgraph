package recursivePathParser_Deprecated;

import java.util.Iterator;

public class TreeNodeIterator<T> implements Iterator<TreeNode<T>> {

	enum ProcessStages {
		ProcessParent, ProcessChildCurNode, ProcessChildSubNode
	}

	private TreeNode<T> TreeNode;

	public TreeNodeIterator(TreeNode<T> TreeNode) {
		this.TreeNode = TreeNode;
		this.doNext = ProcessStages.ProcessParent;
		this.childrenCurNodeIter = TreeNode.children.iterator();
	}

	private ProcessStages doNext;
	private TreeNode<T> next;
	private Iterator<TreeNode<T>> childrenCurNodeIter;
	private Iterator<TreeNode<T>> childrenSubNodeIter;

	@Override
	public boolean hasNext() {

		if (this.doNext == ProcessStages.ProcessParent) {
			this.next = this.TreeNode;
			this.doNext = ProcessStages.ProcessChildCurNode;
			return true;
		}

		if (this.doNext == ProcessStages.ProcessChildCurNode) {
			if (childrenCurNodeIter.hasNext()) {
				TreeNode<T> childDirect = childrenCurNodeIter.next();
				childrenSubNodeIter = childDirect.iterator();
				this.doNext = ProcessStages.ProcessChildSubNode;
				return hasNext();
			}

			else {
				this.doNext = null;
				return false;
			}
		}
		
		if (this.doNext == ProcessStages.ProcessChildSubNode) {
			if (childrenSubNodeIter.hasNext()) {
				this.next = childrenSubNodeIter.next();
				return true;
			}
			else {
				this.next = null;
				this.doNext = ProcessStages.ProcessChildCurNode;
				return hasNext();
			}
		}

		return false;
	}

	@Override
	public TreeNode<T> next() {
		return this.next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
