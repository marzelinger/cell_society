package CellSociety;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * This class handles parsing XML files and returning a completed object.
 *
 * @author Rhondu Smithwick
 * @author Robert C. Duvall
 * @author Russell Llave (ral30)
 *
 */
public class XMLParser {
    public static final String SIM_FILE_TAG = "sim";
    public static final String TYPE_KEY = "type";
    public static final String THRESHOLD_KEY = "Threshold";
    public static final String EMPTY_PROBABILITY_KEY = "EmptyProbability";
    public static final String PROBABILITY_KEY = "Probability";
    public static final String ROWS_KEY = "rows";
    public static final String COLS_KEY = "cols";
    public static final String GRID_SHAPE_KEY = "GridShape";
    public static final String GRID_NEIGHBORS_KEY = "GridNeighbors";
    public static final String GRID_EDGE_KEY = "GridEdge";
    public static final String GRID_OUTLINED_KEY = "GridOutlined";
    public static final String ACTOR_COLOR1_KEY = "ActorColor1";
    public static final String ACTOR_COLOR2_KEY = "ActorColor2";
    public static final String ACTOR_COLOR3_KEY = "ActorColor3";
    public static final String EMPTY_COLOR_KEY = "EmptyColor";
    public static final String INIT_TYPE_KEY = "InitType";
    public static final String DESCRIPTION_KEY = "Description";
    public static final String RADIUS_NEIGHBORHOOD_KEY = "RadiusNeighbors";
    public static final String INIT_ARRAY_KEY = "InitArray";
    public static final String INIT_ACTOR1_KEY = "InitNumStates1";
    public static final String INIT_ACTOR2_KEY = "InitNumStates2";
    public static final String INIT_ACTOR3_KEY = "InitNumStates3";



    // Readable error message that can be displayed by the GUI
    public static final String ERROR_MESSAGE = "XML file does not represent %s";
    // name of root attribute that notes the type of file expecting to parse
    private final String TYPE_ATTRIBUTE;
    // keep only one documentBuilder because it is expensive to make and can reset it before parsing
    private final DocumentBuilder DOCUMENT_BUILDER;

    public static final String DATA_TYPE = "sim_test";
    public static final List<String> DATA_FIELDS = List.of(
            TYPE_KEY,THRESHOLD_KEY,EMPTY_PROBABILITY_KEY,PROBABILITY_KEY,EMPTY_PROBABILITY_KEY,
            ROWS_KEY,COLS_KEY,GRID_SHAPE_KEY,GRID_EDGE_KEY,GRID_NEIGHBORS_KEY,GRID_OUTLINED_KEY,
            ACTOR_COLOR1_KEY,ACTOR_COLOR2_KEY,ACTOR_COLOR3_KEY,EMPTY_COLOR_KEY,INIT_TYPE_KEY, DESCRIPTION_KEY,
            RADIUS_NEIGHBORHOOD_KEY,INIT_ARRAY_KEY,INIT_ACTOR1_KEY,INIT_ACTOR2_KEY,INIT_ACTOR3_KEY
    );
    
    /**
     * Create a parser for XML files of given type.
     */
    public XMLParser (String type) {
        DOCUMENT_BUILDER = getDocumentBuilder();
        TYPE_ATTRIBUTE = type;
    }

    /**
     * Get the data contained in this XML file as an object
     */
    public HashMap<String, String> getGame(File dataFile) {
        var root = getRootElement(dataFile);
        if (! isValidFile(root, DATA_TYPE)) {
            throw new XMLException(ERROR_MESSAGE, DATA_TYPE);
        }
        // read data associated with the fields given by the object
        var results = new HashMap<String, String>();
        for (var field : DATA_FIELDS) {
            results.put(field, getTextValue(root, field));
        }
        return results;
    }

    // Get root element of an XML file
    private Element getRootElement (File xmlFile) {
        try {
            DOCUMENT_BUILDER.reset();
            var xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
            return xmlDocument.getDocumentElement();
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }

    // Returns if this is a valid XML file for the specified object type
    private boolean isValidFile (Element root, String type) {
        return getAttribute(root, TYPE_ATTRIBUTE).equals(type);
    }

    // Get value of Element's attribute
    private String getAttribute (Element e, String attributeName) {
        return e.getAttribute(attributeName);
    }

    // Get value of Element's text
    private String getTextValue (Element e, String tagName) {
        var nodeList = e.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        else {
            // FIXME: empty string or null, is it an error to not find the text value?
            return "";
        }
    }

    // Boilerplate code needed to make a documentBuilder
    private DocumentBuilder getDocumentBuilder () {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }
}
