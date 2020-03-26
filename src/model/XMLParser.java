package model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A class for fetching XMl documents from Sveriges Radio and
 * reading its contents.
 */
public class XMLParser {
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder docBuild;
    private Document document;
    private DateTimeUtil dateTimeUtil;
    private ArrayList<Node> xmlNodes;
    private String primaryNode;

    /**
     * Builds a XML-document fom a HTTPS-source.
     * @param url = the url to the API (excluding the page number)
     * @param primaryNode = the node that we want to look at (channel, schedule, etc)
     * @throws IOException = in case there is an issue with the connection
     */
    public XMLParser(String url, String primaryNode) throws IOException {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            docBuild  = dbFactory.newDocumentBuilder();
            xmlNodes  = new ArrayList<>();
            dateTimeUtil = new DateTimeUtil();
            this.primaryNode = primaryNode;
            createListOfNodes(url, primaryNode);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    /**
     * For all of the XMl-pages, create one unified list
     * of all the nodes that are requested.
     * @param url = the url to the XML-file (excluding page nr)
     * @param node = the node that is wanted to be stored.
     * @throws IOException = could be several underlying IO issues.
     */
    private void createListOfNodes(String url, String node) throws IOException {
        try {
            if (dateTimeUtil.getCurrentHours() >= 12){
                document = docBuild.parse(createURLFromDate(
                        url, dateTimeUtil.getCurrentDate()).openStream()
                );
                convertNodeListToArray(getDocumentNodeList(node));

                if (primaryNode.equals("scheduledepisode")){
                    document = docBuild.parse(createURLFromDate(
                            url, dateTimeUtil.getOtherDate()).openStream()
                    );
                    convertNodeListToArray(getDocumentNodeList(node));
                }
            } else {
                if (primaryNode.equals("scheduledepisode")){
                    document = docBuild.parse(createURLFromDate(
                            url, dateTimeUtil.getOtherDate()).openStream()
                    );
                    convertNodeListToArray(getDocumentNodeList(node));
                }

                document = docBuild.parse(createURLFromDate(
                        url, dateTimeUtil.getCurrentDate()).openStream()
                );
                convertNodeListToArray(getDocumentNodeList(node));
            }
            filterNodesByTimeInterval(12);
        } catch (SAXException e){
            e.printStackTrace();
        }
    }


    /**
     * Filters so that only the nodes that fall
     * inside a given time interval +/- the
     * current time is present among the nodes.
     * @param interval = time interval in hours
     */
    private void filterNodesByTimeInterval(int interval){
        if (primaryNode.equals("scheduledepisode")) {
            Element element;
            String startTime, endTime;
            Iterator<Node> iterator = xmlNodes.iterator();
            while (iterator.hasNext()) {
                element   = (Element) iterator.next();
                startTime = element.getElementsByTagName("starttimeutc").item(0).getTextContent();
                endTime   = element.getElementsByTagName("endtimeutc").item(0).getTextContent();
                if (Math.abs(dateTimeUtil.durationBetweenTimes(
                        dateTimeUtil.getCurrentTimeAsZonedDateTime(),
                        dateTimeUtil.stringToZonedDateTime(startTime))) >= interval)
                {
                    if (Math.abs(dateTimeUtil.durationBetweenTimes(
                            dateTimeUtil.getCurrentTimeAsZonedDateTime(),
                            dateTimeUtil.stringToZonedDateTime(endTime))) >= interval) {
                        iterator.remove();
                    }
                }
            }
        }
    }


    /**
     * Gets a NodeList and appends all those nodes to
     * the complete list of all nodes across all XML-
     * files from the same API-function.
     * @param list = the NodeList.
     */
    private void convertNodeListToArray(NodeList list) {
        int length = list.getLength();
        Node[] copy = new Node[length];

        for (int n = 0; n < length; ++n)
            copy[n] = list.item(n);

        ArrayList<Node> temp = new ArrayList<>(Arrays.asList(copy));
        xmlNodes.addAll(temp);
    }


    /**
     * Get all the nodes from the XMl doc with the given element name.
     * @param xmlElement = the name
     * @return = node list object.
     */
    private NodeList getDocumentNodeList(String xmlElement){
        return document.getElementsByTagName(xmlElement);
    }


    /**
     * A static method that can be used to create a valid URL object to send
     * into the XMLReader constructor from a given string.
     * @param url  = a string representing the url (excluding the page)
     * @param date = the date of the xml that should be retrieved.
     * @return     = a URL object.
     * @throws MalformedURLException = in case the url has got any issues.
     */
    public URL createURLFromDate(String url, String date)
            throws MalformedURLException {
        url = url + "&pagination=false&size=1000&date=" + date;
        return new URL(url.replace(" ", "%20"));
    }


    /**
     * Get the ArrayList of all nodes that has been parsed.
     * @return = the nodes.
     */
    public ArrayList<Node> getXmlNodes() {
        return xmlNodes;
    }


    /**
     * Returns an ImageIcon from an XML or a replacement if no
     * image is found. If both would fail, returns a string.
     * @param element = the element that contains a URL to an image
     * @return = an ImageIcon, or worse case a string.
     */
    private Object linkToTableImage(Element element, String elementName){
        NodeList temp;
        try {
            temp = element.getElementsByTagName(elementName);
            if (temp.getLength() > 0){
                return new URL(temp.item(0).getTextContent());
            } else {
                return "/no_img.jpg";
            }
        } catch (MalformedURLException  e) {
            return "/no_img.jpg";
        }
    }


    /**
     * Get data information about all channels in a format suitable
     * for a JTable or internal table representation.
     * @param channels = all channels in a node list.
     * @return = the data for all the channels.
     */
    public Object[][] getChannelData(ArrayList<Node> channels){
        Object[][] data = new Object[channels.size()][5];
        NodeList temp;
        Node node;
        Element element;

        if (primaryNode.equals("channel")){
            for (int i = 0; i < channels.size(); ++i){
                node = channels.get(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    element = (Element) node;
                    data[i][0] = linkToTableImage(element, "image");
                    data[i][1] = node.getAttributes()
                            .getNamedItem("name")
                            .getTextContent();
                    data[i][2] = element.getElementsByTagName("tagline")
                            .item(0)
                            .getTextContent();
                    temp = element.getElementsByTagName("scheduleurl");
                    if (temp.getLength() > 0){
                        data[i][3] = temp.item(0).getTextContent();
                    } else data[i][3] = "Schedule not found";
                }
            }
        } else if (primaryNode.equals("scheduledepisode")){
            return getTableauData(channels);
        } else throw new IllegalArgumentException();
        return data;
    }


    /**
     * Get data information about the current tableau for a certain
     * channel, given its channels.
     * @param episodes = the episodes that will air on the channel
     * @return = information table about each episode.
     */
    public Object[][] getTableauData(ArrayList<Node> episodes){
        Object[][] data = new Object[episodes.size()][6];
        Node node;
        Element element;

        if (primaryNode.equals("scheduledepisode")){
            for (int i = 0; i < episodes.size(); ++i){
                node = episodes.get(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    element = (Element) node;
                    data[i][0] = element
                            .getElementsByTagName("title")
                            .item(0)
                            .getTextContent();
                    data[i][1] = dateTimeUtil
                            .stringToZonedDateTime(element
                            .getElementsByTagName("starttimeutc")
                            .item(0)
                            .getTextContent()
                    ).toString();
                    data[i][2] = dateTimeUtil
                            .stringToZonedDateTime(element
                            .getElementsByTagName("endtimeutc")
                            .item(0)
                            .getTextContent()
                    ).toString();
                    data[i][3] = dateTimeUtil.isBeforeNow(element
                                    .getElementsByTagName("starttimeutc")
                                    .item(0)
                                    .getTextContent(),
                            element
                                    .getElementsByTagName("endtimeutc")
                                    .item(0)
                                    .getTextContent()
                    );
                    data[i][4] = linkToTableImage(element, "imageurl");
                    data[i][5] = element
                            .getElementsByTagName("description")
                            .item(0)
                            .getTextContent();
                }
            }
        } else if (primaryNode.equals("channel")){
            return getChannelData(episodes);
        } else throw new IllegalArgumentException();
        return data;
    }


    /**
     * Prints an XML document. Used for debugging.
     */
    public void printXMLDocument (){
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf;
        try {
            tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.METHOD, "xml");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter writer = new StringWriter();
            tf.transform(new DOMSource(document), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("[\t\r]", "");
            System.out.println(output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}