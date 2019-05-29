import java.awt.Rectangle;
import java.util.Vector;

public class LongestPath {
	private boolean[] isVisited;
	private int[] timeDuration;
	private int[] parents;
	private Vector<Integer> children;
	private int nodeIndex;

	public void findLongestPath(Vector<Rectangle> nodes, int size, int start) {
		boolean found;
		int max, sum, index = 0, nodeDuration, value1, value2, startNodeData;

		// Collect the indices of children of each node that is considered as
		// the starting
		// node in each iteration.
		for (int i = 0; i < GraphEditor.drawingPanel.links.size(); i++) {
			value1 = GraphEditor.drawingPanel.links.get(i).getValue1();
			value2 = GraphEditor.drawingPanel.links.get(i).getValue2();
			startNodeData = GraphEditor.drawingPanel.nodesData.get(start);

			if (startNodeData == value1 || startNodeData == value2) {
				if (!isVisited[getNodeIndex(value1, false)] && !isVisited[getNodeIndex(value2, false)]) {
					if (value1 == startNodeData) {
						children.add(getNodeIndex(value2, false));
					} else {
						children.add(getNodeIndex(value1, false));
					}
				}
			}
		}
		isVisited[start] = true;

		// apply the longest path algorithm.
		for (int j = 0; j < children.size(); j++) {
			nodeDuration = GraphEditor.drawingPanel.duration.get(children.get(j));
			sum = timeDuration[start] + nodeDuration;

			if (sum > timeDuration[children.get(j)]) {
				// Updating
				timeDuration[children.get(j)] = sum;
				parents[children.get(j)] = start;
			}
		}
		// find a new starting node.
		found = false;
		max = -10000;
		for (int i = 0; i < GraphEditor.drawingPanel.nodes.size(); i++) {
			if (!isVisited[i]) {
				if (timeDuration[i] > max) {
					found = true;
					max = timeDuration[i];
					index = i;
				}
			}
		}
		if (found) {
			children.clear();
			findLongestPath(nodes, size, index);
		}
	}

	public void initialize(Vector<Rectangle> nodes, int size, int start) {
		isVisited = new boolean[size];
		timeDuration = new int[size];
		parents = new int[size];
		children = new Vector<Integer>();
		GraphEditor.drawingPanel.pathNodes = new Vector<Integer>();
		GraphEditor.drawingPanel.pathLinks = new Vector<Links>();

		for (int i = 0; i < size; i++) {
			isVisited[i] = false;
			timeDuration[i] = -10000;
			parents[i] = -1;
		}
		timeDuration[start] = GraphEditor.drawingPanel.duration.get(start);
		findLongestPath(nodes, size, start);
	}

	public void path(Vector<Rectangle> nodes, int start, int end) {
		if (start == end)
			return;

		if (parents[end] == start) {
			GraphEditor.drawingPanel.pathNodes.add(GraphEditor.drawingPanel.nodesData.get(start));
			GraphEditor.drawingPanel.path = String.valueOf(GraphEditor.drawingPanel.nodesData.get(start));
		} else {
			path(nodes, start, parents[end]);
		}
		GraphEditor.drawingPanel.pathNodes.add(GraphEditor.drawingPanel.nodesData.get(end));
		GraphEditor.drawingPanel.path += ("=>" + GraphEditor.drawingPanel.nodesData.get(end));
	}

	// this method is for determining which link contributes to the longest
	// path.
	public void gatherPathLinks() {
		for (int i = 0; i < GraphEditor.drawingPanel.pathNodes.size() - 1; i++) {
			int value1 = GraphEditor.drawingPanel.pathNodes.get(i);
			int value2 = GraphEditor.drawingPanel.pathNodes.get(i + 1);
			GraphEditor.drawingPanel.pathLinks.add(new Links(value1, value2));
		}
	}

	// this method is for determining the ending node needed to generate the
	// longest path.
	public int getMaxDuration() {
		int maxDuration = timeDuration[0];
		for (int i = 0; i < timeDuration.length; i++) {
			if (timeDuration[i] > maxDuration) {
				maxDuration = timeDuration[i];
			}
		}
		return maxDuration;
	}

	public int getNodeIndex(int data, boolean duration) {
		if (!duration) {
			for (int i = 0; i < GraphEditor.drawingPanel.nodesData.size(); i++) {
				if (data == GraphEditor.drawingPanel.nodesData.get(i)) {
					nodeIndex = i;
					break;
				}
			}
		} else {
			for (int i = 0; i < timeDuration.length; i++) {
				if (data == timeDuration[i]) {
					nodeIndex = i;
					break;
				}
			}
		}
		return nodeIndex;
	}
}
