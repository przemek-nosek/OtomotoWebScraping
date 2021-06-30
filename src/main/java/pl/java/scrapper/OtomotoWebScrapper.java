package pl.java.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;


public class OtomotoWebScrapper {
    private final String url;
    private final Set<String> setOfUrls;


    public OtomotoWebScrapper(String url) {
        this.url = url;
        setOfUrls = new HashSet<>();
    }

    public void getLinksFromPage() throws IOException {
        Document doc = Jsoup.connect(url).get();
        for (Element element : doc.select("a.offer-title__link")) {
            String href = element.attr("href");
            if (!href.contains("otomoto.pl")) {
                continue;
            }
            setOfUrls.add(href);
        }
    }

    private boolean checkConnection(int statusCode) {
        if (statusCode >=400 && statusCode <= 499) {
            System.err.println("There was a client side error.");
            System.err.println("Status code: " + statusCode);
            return false;
        }
        else if (statusCode >=500 && statusCode <= 599) {
            System.err.println("There was a server side error.");
            System.err.println("Status code: " + statusCode);
            return false;
        }
        return true;
    }

    public void savePageContentToFile() throws IOException {
        for (String url : setOfUrls) {
            Connection con = Jsoup.connect(url).ignoreHttpErrors(true);
            Document doc = con.get();
            int statusCode = con.response().statusCode();

            if (!checkConnection(statusCode)) {
                continue;
            }

            if (!Files.exists(Path.of("links\\" + "page"))) {
                Files.createDirectory(Path.of("links\\" + "page"));
            }

            Path path = Path.of("links\\"+"page"+"\\"+doc.title()+".html");
            if (Files.exists(path)) {
                System.out.println("Duplicated entry");
                continue;
            }

            System.out.println("Saving... " + doc.title());
            Files.writeString(path, doc.outerHtml());
        }
    }


    public static int getMaxPages(String urlToCheck) throws IOException {
        Document doc = Jsoup.connect(urlToCheck).get();
        Elements select = doc.select("span.page");

        if (select.last() == null) {
            return 0;
        }

        return Integer.parseInt(select.last().text());
    }
}
