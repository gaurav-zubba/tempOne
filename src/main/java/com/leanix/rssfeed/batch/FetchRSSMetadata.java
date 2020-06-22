package com.leanix.rssfeed.batch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;

public class FetchRSSMetadata {
    Document doc;
    DBOperator dbo=new DBOperator();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    Stack s=new Stack();

    public void ScrapeRssLink(RSSLinkPOJO rs)
    {
        try {
            String childTag;
            int success=Intiliazedoc(rs.RSSLink);
            if(success==0){
                return;
            }
            if (rs.RSSLink.startsWith("https://cloud.google.com/feeds/")) {
                childTag="entry";
            }
            else{
                childTag="item";
            }
            FetchMetaData(childTag);
        }
        catch (Exception ex){
            System.out.println(rs.id);
            System.out.println(rs.RSSLink);
            System.out.println(ex);
            //ex.printStackTrace();
            //System.exit(0);
        }
    }
    public int Intiliazedoc(String link){
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            InputStream is = connection.getInputStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(is);
            System.out.println("Parsing for url : "+ link);
            return 1;
        }
        catch (Exception e) {
            System.out.println(e);
            //System.out.println(e.getMessage());
            return 0;
        }

    }
    public void FetchMetaData(String childTag){
        NodeList itemList = doc.getElementsByTagName(childTag);
        Element itemElt;
        RSSMetaPOJO pp=null;
        for (int k = 0; k < itemList.getLength(); k++) {
            Node itemNode = itemList.item(k);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                itemElt = (Element) itemNode;
                pp=new RSSMetaPOJO();
                pp.title=getAttribute(itemElt,"title");//0.get title
                pp.pubdate=getAttribute(itemElt,"pubdate");//1.get publish date
                pp.sublink=getAttribute(itemElt,"suburl");//2.get sub url
                s.push(pp);
            }
        }


    }
    public String getAttribute(Element e, String attrName){
        switch(attrName){
            case "title":
                //title tag
                return e.getElementsByTagName("title").item(0).getTextContent();
            case "pubdate":
                //updated or pubDate tag
                NodeList ndl=e.getElementsByTagName("pubDate");
                if (ndl.getLength()!=0){
                    return e.getElementsByTagName("pubDate").item(0).getTextContent();
                }
                else{
                    return e.getElementsByTagName("updated").item(0).getTextContent();
                }
            case "suburl":
                //link tag
                String url=e.getElementsByTagName("link").item(0).getTextContent();
                if(url.length()==0) {
                    return e.getElementsByTagName("link").item(0).getAttributes().getNamedItem("href").getNodeValue();
                }
                return url;
            default:
                return "None";
        }

    }
    public void GetRssLink(Stack s)
    {
        while(!s.empty())
        {
            RSSLinkPOJO rs=(RSSLinkPOJO)s.pop();
            ScrapeRssLink(rs);
        }
    }
    public void process()
    {
        System.out.println("Starting process");
        GetRssLink(dbo.readRSSLinks());
        dbo.WriteRSSMeta(s);
        System.out.println(s.size());
    }



    public static void main(String s[])
    {
        FetchRSSMetadata rssmeta=new FetchRSSMetadata();
        rssmeta.process();
        System.out.println("Completed process");
    }

}
