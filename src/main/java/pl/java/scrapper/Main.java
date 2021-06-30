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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



public class Main {
    public static void main(String[] args) {
        final String url =
                "https://www.otomoto.pl/osobowe/?search%5Bfilter_float_price%3Afrom%5D=500000&search%5Bfilter_float_price%3Ato%5D=1000000";

        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 8; i++) {
            executor.execute(() -> {
                try {
                    var scrapper = new OtomotoWebScrapper(url+"&page="+ atomicInteger.incrementAndGet(), atomicInteger.get());
                    scrapper.getMaxPages();
                    scrapper.getLinksFromPage();
                    scrapper.savePageContentToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


// https://jsoup.org/cookbook/extracting-data/attributes-text-html
// https://able.bio/DavidLandup/introduction-to-web-scraping-with-java-jsoup--641yfyl
// https://www.otomoto.pl/osobowe/?search%5Bfilter_float_price%3Afrom%5D=500000&search%5Bfilter_float_price%3Ato%5D=1000000
// https://jsoup.org/
// https://www.youtube.com/watch?v=tI1qGwhn_bs
// https://www.youtube.com/results?search_query=java+web+scraping