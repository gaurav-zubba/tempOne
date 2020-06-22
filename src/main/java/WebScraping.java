import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraping {

    public static void main(String[] args) {

        Document doc;

        String title;

        try {

            doc = Jsoup.connect("https://docs.aws.amazon.com/AmazonECR/latest/userguide/docker-push-multi-architecture-image.html").get();

            Elements paragraph =  doc.getElementsByTag("p");

           for(Element e : paragraph){
               System.out.println(e.text());
           }
        } catch (IOException e) {

        }
    }
}
