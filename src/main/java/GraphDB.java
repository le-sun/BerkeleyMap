import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 *
 * @author Alan Yao
 */
public class GraphDB {
    /**
     * Example constructor shows how to create and start an XML parser.
     *
     * @param path Path to the XML file to be parsed.
     */
    private HashMap graph = new HashMap();
    private HashMap graphNodes = new HashMap();
    private LinkedList<GraphNode> tempNodes = new LinkedList<GraphNode>();

    public GraphDB(String path) {
        try {
            File inputFile = new File(path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            MapDBHandler maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        //130462 Nodes before cleaning, and 28654 after
        LinkedList<String> ids = new LinkedList<String>();
        for (Object o : graphNodes.values()) {
            GraphNode n = (GraphNode) o;
            String id = n.getId();
            if (!n.hasConnections()) {
                ids.add(id);
            }
        }
        for (String s : ids) {
            GraphNode removed = (GraphNode) graphNodes.remove(s);

        }
        ids.clear();
        System.out.println(graphNodes.size());
    }

    public HashMap getGraph() {
        return this.graph;
    }

    public HashMap getGraphNodes() {
        return this.graphNodes;
    }

    public LinkedList<GraphNode> getTempNodes() {
        return this.tempNodes;
    }
}
