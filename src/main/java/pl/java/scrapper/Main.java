package pl.java.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        final String url =
                "https://www.otomoto.pl/osobowe/?search%5Bfilter_float_price%3Afrom%5D=500000&search%5Bfilter_float_price%3Ato%5D=1000000";

//        var doc = Jsoup.connect(url).get();
//        Elements links = doc.select("a[href]");
//        for (Element link : links) {
//            System.out.println(link.attr("href"));
//        }

//        Document doc = Jsoup.connect(url).get();
//        Elements titles = doc.getElementsByClass("offer-title__link");
//        for (Element title : titles) {
//            System.out.println(title.text());
//        }


        Document doc = Jsoup.connect(url).get();
        var setOfLinks = new HashSet<String>();
        for (Element element : doc.select("a.offer-title__link")) {
            String href = element.attr("href");
            if (!href.contains("otomoto.pl")) {
                continue;
            }
            setOfLinks.add(element.attr("href"));
            System.out.println(element.attr("href"));
        }
        setOfLinks.forEach(System.out::println);
        System.out.println(setOfLinks.size());

        var increment = new AtomicInteger(0);
        setOfLinks.forEach(link -> {
            try {
                var document = Jsoup.connect(link).get();
                Files.writeString(Path.of("abc\\test" + increment.incrementAndGet() + ".html"), document.outerHtml());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // https://jsoup.org/cookbook/extracting-data/attributes-text-html
        // https://able.bio/DavidLandup/introduction-to-web-scraping-with-java-jsoup--641yfyl
        // https://www.otomoto.pl/osobowe/?search%5Bfilter_float_price%3Afrom%5D=500000&search%5Bfilter_float_price%3Ato%5D=1000000
        // https://jsoup.org/
        // https://www.youtube.com/watch?v=tI1qGwhn_bs
        // https://www.youtube.com/results?search_query=java+web+scraping
    }
}
