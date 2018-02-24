package dom;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class TreeViewer {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new DOMTreeFrame();
            frame.setTitle("TreeViewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class DOMTreeFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private DocumentBuilder builder;

    public DOMTreeFrame() throws HeadlessException {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(event -> openFile());
        fileMenu.add(openItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("dom"));
        chooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final File file = chooser.getSelectedFile();
        new SwingWorker<Document, Void>() {

            @Override
            protected Document doInBackground() throws Exception {
                if (builder == null) {
                    builder = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder();
                }
                return builder.parse(file);
            }

            @Override
            protected void done() {
                try {
                    Document doc = get();
                    JTree tree = new JTree(new DOMTreeModel(doc));
                    tree.setCellRenderer(new DOMTreeCellRenderer());

                    setContentPane(new JScrollPane(tree));
                    validate();
                } catch (InterruptedException | ExecutionException e) {
                    JOptionPane.showMessageDialog(DOMTreeFrame.this, e);
                }
            }

        }.execute();
    }

}

class DOMTreeModel implements TreeModel {

    private Document doc;

    public DOMTreeModel(Document doc) {
        this.doc = doc;
    }

    @Override
    public void addTreeModelListener(TreeModelListener arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public Object getChild(Object parent, int index) {
        Node node = (Node) parent;
        return node.getChildNodes().item(index);
    }

    @Override
    public int getChildCount(Object parent) {
        Node node = (Node) parent;
        return node.getChildNodes().getLength();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Node node = (Node) parent;
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (getChild(node, i) == child) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object getRoot() {
        return doc.getDocumentElement();
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    @Override
    public void removeTreeModelListener(TreeModelListener arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void valueForPathChanged(TreePath arg0, Object arg1) {
        // TODO Auto-generated method stub
    }

}

class DOMTreeCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        Node node = (Node) value;
        if (node instanceof Element) {
            return elementPanel((Element)node);
        }
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (node instanceof CharacterData) {
            setText(characterString((CharacterData) node));
        } else {
            setText(node.getClass() + ":" + node.toString());
        }
        return this;
    }
    
    public static JPanel elementPanel(Element e) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Elment:" + e.getTagName()));
        final NamedNodeMap map = e.getAttributes();
        panel.add(new JTable(new AbstractTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getValueAt(int r, int c) {
                
                return c == 0 ? map.item(r).getNodeName() : map.item(r).getNodeValue();
            }
            
            @Override
            public int getRowCount() {
                return map.getLength();
            }
            
            @Override
            public int getColumnCount() {
                return 2;
            }
        }));
        return panel;
    }

    private static String characterString(CharacterData node) {
        StringBuilder builder = new StringBuilder(node.getData());
        for (int i = 0; i<builder.length(); i++) {
            switch (builder.charAt(i)) {
            case '\r':
                builder.replace(i, i+1, "\\r");
                break;
            case '\n':
                builder.replace(i, i+1, "\\n");
                break;
            case '\t':
                builder.replace(i, i+1, "\\t");
                break;
            default:
                i--;
                break;
            }
            i++;
        }
        if (node instanceof CDATASection) {
            builder.insert(0, "CDATASection: ");
        } else if (node instanceof Text) {
            builder.insert(0, "Text: ");
        } else if (node instanceof Comment) {
            builder.insert(0, "Comment: ");
        }
        return builder.toString();
    }
}