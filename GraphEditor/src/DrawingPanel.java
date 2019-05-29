import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
	private boolean moving, dragging;
	public boolean addNodePanelShowing, addLinkPanelShowing, deleteNodePanelShowing, longestPathPanelShowing,
			nodeModificationPanelShowing;
	public boolean clicked = false;
	private Point point1, point2;
	private int index;
	public Vector<Rectangle> nodes;
	public Vector<Integer> nodesData;
	public Vector<Links> links;
	public Vector<Integer> duration;
	public Vector<Integer> pathNodes;
	public Vector<Links> pathLinks;
	public String path;
	private Rectangle rectangle;

	public DrawingPanel() {
		nodes = new Vector<Rectangle>();
		nodesData = new Vector<Integer>();
		links = new Vector<Links>();
		duration = new Vector<Integer>();

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				GraphEditor.flag = true;
				if (!clicked && e.isMetaDown()) {
					if (!addLinkPanelShowing && !addNodePanelShowing && !deleteNodePanelShowing
							&& !longestPathPanelShowing) {
						clicked = true; /*
										 * to prevent two object of this class
										 * to be created at the same time.
										 */
						for (int i = 0; i < nodes.size(); i++) {
							if (e.getX() >= nodes.get(i).getMinX() - 25 && e.getY() >= nodes.get(i).getMinY() - 25
									&& e.getX() <= nodes.get(i).getMaxX() && e.getY() <= nodes.get(i).getMaxY()) {

								GraphEditor.addNode.setEnabled(false);
								GraphEditor.addLink.setEnabled(false);
								GraphEditor.deleteNode.setEnabled(false);
								GraphEditor.clear.setEnabled(false);
								GraphEditor.longestPath.setEnabled(false);

								NodeModificationPanel nodeModificationPanel = new NodeModificationPanel(i);
								GraphEditor.frame.add(nodeModificationPanel, BorderLayout.NORTH);
								GraphEditor.frame.validate();

							}
						}
					}
				}
			}

			public void mousePressed(MouseEvent e) {
				if (GraphEditor.createNode == true) {

					try {
						AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream(GraphEditor.class.getResource("Button_Sound.wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
					} catch (UnsupportedAudioFileException a) {
					} catch (IOException a) {
					} catch (LineUnavailableException a) {
					}

					point1 = new Point(e.getX(), e.getY());
					rectangle = new Rectangle(point1.x, point1.y, 50, 50);
					nodes.add(rectangle);
					nodesData.add(GraphEditor.nodeValue);
					duration.add(GraphEditor.nodeDuration);
					moving = false;

					AddNodePanel.dataTF.setEditable(true);
					AddNodePanel.durationTF.setEditable(true);
					repaint();
					GraphEditor.createNode = false;
				}
			}

			public void mouseReleased(MouseEvent e) {
				dragging = false;
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (!dragging && !e.isMetaDown()) {
					for (int i = 0; i < nodes.size(); i++) {
						if (e.getX() >= nodes.get(i).getMinX() - 25 && e.getY() >= nodes.get(i).getMinY() - 25
								&& e.getX() <= nodes.get(i).getMaxX() && e.getY() <= nodes.get(i).getMaxY()) {
							index = i;
							dragging = true;
							break;
						}
					}
				}
				if (dragging) {
					point2 = new Point(e.getX(), e.getY());
					nodes.get(index).setLocation(point2);
					repaint();
				}
			}

			public void mouseMoved(MouseEvent e) {
				if (GraphEditor.createNode) {
					point1 = new Point(e.getX(), e.getY());
					moving = true;
					repaint();
				}
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle rect;
		Point P1 = new Point(), P2 = new Point();

		for (int j = 0; j < links.size(); j++) {
			for (int k = 0; k < nodesData.size(); k++) {
				if (links.get(j).getValue1() == nodesData.get(k)) {
					P1 = nodes.get(k).getLocation();
				}
				if (links.get(j).getValue2() == nodesData.get(k)) {
					P2 = nodes.get(k).getLocation();
				}
			}
			g.setColor(Color.BLACK);
			g.drawLine(P1.x, P1.y, P2.x, P2.y);
		}
		if (GraphEditor.longestPathFlag) {
			for (int j = 0; j < pathLinks.size(); j++) {
				for (int k = 0; k < nodesData.size(); k++) {
					if (pathLinks.get(j).getValue1() == nodesData.get(k)) {
						P1 = nodes.get(k).getLocation();
					}
					if (pathLinks.get(j).getValue2() == nodesData.get(k)) {
						P2 = nodes.get(k).getLocation();
					}
				}
				g.setColor(Color.RED);
				g.drawLine(P1.x, P1.y, P2.x, P2.y);
			}
		}
		for (int i = 0; i < nodes.size(); i++) {

			g.setColor(Color.RED);
			rect = nodes.get(i);
			g.fillOval(rect.x - 25, rect.y - 25, 50, 50);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Times New Roman", Font.BOLD, 16));
			g.drawString(String.valueOf(nodesData.get(i)), rect.x - 5, rect.y + 5);
			g.setColor(Color.black);
			g.drawString(duration.get(i) == 1 ? duration.get(i) + " week" : duration.get(i) + " weeks", rect.x - 25,
					rect.y - 25);
		}
		if (GraphEditor.longestPathFlag) {
			for (int j = 0; j < pathNodes.size(); j++) {
				for (int k = 0; k < nodesData.size(); k++) {
					if (pathNodes.get(j) == nodesData.get(k)) {
						g.setColor(Color.GREEN);
						g.fillOval(nodes.get(k).x - 25, nodes.get(k).y - 25, 50, 50);
						g.setColor(Color.WHITE);
						g.setFont(new Font("Times New Roman", Font.BOLD, 16));
						g.drawString(String.valueOf(nodesData.get(k)), nodes.get(k).x - 5, nodes.get(k).y + 5);
						g.setColor(Color.black);
						g.drawString(duration.get(k) == 1 ? duration.get(k) + " week" : duration.get(k) + " weeks",
								nodes.get(k).x - 25, nodes.get(k).y - 25);
					}
				}
			}
			g.setColor(Color.black);
			g.setFont(new Font("Times New Roman", Font.BOLD, 20));
			g.drawString("The Longest Path is: " + path, 10, 50);
		}
		if (moving) {
			g.setColor(Color.black);
			g.fillOval(point1.x - 25, point1.y - 25, 50, 50);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Times New Roman", Font.BOLD, 16));
			g.drawString(String.valueOf(GraphEditor.nodeValue), point1.x - 5, point1.y + 5);
			g.setColor(Color.black);
			g.drawString(GraphEditor.nodeDuration == 1 ? GraphEditor.nodeDuration + " week"
					: GraphEditor.nodeDuration + " weeks", point1.x - 25, point1.y - 25);
			moving = false;
		}
	}
}
