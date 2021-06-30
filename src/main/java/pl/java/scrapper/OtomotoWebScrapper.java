package pl.java.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

public class OtomotoWebScrapper {
    private final String url;
    private final Set<String> setOfUrls;
    //    private static LongAdder counter = new LongAdder();
    private int page;


    public OtomotoWebScrapper(String url, int page) {
//        counter.add(1);
        this.url = url;
        this.page = page;
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

    public void savePageContentToFile() throws IOException {
        for (String url : setOfUrls) {
            var doc = Jsoup.connect(url).get();
            System.out.println("Saving... " + doc.title());

            if (!Files.exists(Path.of("links\\" + "page" + page))) {
                Files.createDirectory(Path.of("links\\" + "page" + page));
            }
            Files.writeString(Path.of("links\\" + "page" + page + "\\" + doc.title() + ".html"), doc.outerHtml());
        }
    }

    public int getMaxPages() throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements select = doc.select("span.page");

        int amount = Integer.parseInt(select.last().text());
        System.out.println(select.last().text());

        return amount;
    }

//    public static LongAdder getCounter() {
//        return counter;
//    }

}
