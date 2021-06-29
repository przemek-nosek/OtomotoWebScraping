package pl.java.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

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

        for (Element element : doc.select("a.offer-title__link")) {
//            Elements select = element.select("a[href]");
            System.out.println(element.attr("href"));
        }

    }
}
