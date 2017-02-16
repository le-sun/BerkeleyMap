import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 * pathfinding, under some constraints.
 * See OSM documentation on
 * <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 * <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 * <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 * and the java
 * <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 * @author Alan Yao
 */
public class MapDBHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;
    private HashMap graph;
    private HashMap graphNodes;
    private LinkedList<GraphNode> tempNodes;
    private HashMap<String, String> tags = new HashMap<String, String>();

    public MapDBHandler(GraphDB g) {
        this.g = g;
        this.graph = g.getGraph();
        this.graphNodes = g.getGraphNodes();
        this.tempNodes = g.getTempNodes();
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     *
     * @param uri       The Ns URI, or the empty string if the element has no Namespace URI or
     *                  if Ns processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName     The qualified name (with prefix), or the empty string if qualified names are
     *                  not available. This tells us which element we're looking at.
     * @param attr      The attributes attached to the element. If there are no attributes, it
     *                  shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */
        if (qName.equals("node")) {
            activeState = "node";
            String id = attr.getValue("id");
            Double lon = Double.parseDouble(attr.getValue("lon"));
            Double lat = Double.parseDouble(attr.getValue("lat"));
            GraphNode newNode = new GraphNode(id, null, null, activeState, lon, lat);
            graphNodes.put(id, newNode);
        } else if (qName.equals("way")) {
            activeState = "way";
            String id = attr.getValue("id");
            //GraphNode wayNode = new GraphNode(id, null, null, activeState, 0, 0);
            //tempNodes.add(wayNode);
        } else if (qName.equals("nd")) {
            activeState = "way";
            String id = attr.getValue("ref");
            GraphNode node = (GraphNode) graphNodes.get(id);
            tempNodes.add(node);
        } else if (activeState.equals("node") && qName.equals("tag") && attr.getValue("k")
                .equals("name")) {

        } else if (activeState.equals("way") && qName.equals("tag")) {
            String k = attr.getValue("k");
            String v = attr.getValue("v");
            tags.put(k, v);
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     *
     * @param uri       The Ns URI, or the empty string if the element has no Namespace URI or
     *                  if Ns processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName     The qualified name (with prefix), or the empty string if qualified names are
     *                  not available.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            if (tempNodes.size() == 1) {
                tempNodes.clear();
                tags.clear();
                return;
            }

            if (tags.containsKey("highway")) {
                String typeOfHighway = tags.get("highway");
                if (!ALLOWED_HIGHWAY_TYPES.contains(typeOfHighway)) {
                    tempNodes.clear();
                    tags.clear();
                    return;
                }
            } else {
                tempNodes.clear();
                tags.clear();
                return;
            }

            GraphNode fakeNullNode = new GraphNode("null", null, null, "null", 0, 0);
            for (int i = 0; i < tempNodes.size(); i++) {
                GraphNode n1 = tempNodes.get(i);
                GraphNode n2 = fakeNullNode;
                GraphNode temp = fakeNullNode;

                if (i - 1 < 0) {
                    temp = n1;
                } else {
                    temp = tempNodes.get(i - 1);
                }
                if (i + 1 >= tempNodes.size()) {
                    temp = tempNodes.get(i - 1);
                    n1.connect(temp, fakeNullNode);
                    break;
                }
                n2 = tempNodes.get(i + 1);
                n1.connect(temp, n2);
            }
            tempNodes.clear();
            tags.clear();
        } else if (qName.equals("node")) {
            return;
        }
    }
}
