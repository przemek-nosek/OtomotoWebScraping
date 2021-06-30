package pl.java.scrapper;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        final String url =
                "https://www.otomoto.pl/osobowe/?search%5Bfilter_float_price%3Afrom%5D=500000&search%5Bfilter_float_price%3Ato%5D=1000000";

        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int maxPages = OtomotoWebScrapper.getMaxPages(url);

        if (maxPages > 10) { // Can read max 10 pages total.
            maxPages = 11;
        }

        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (int i = 1; i < maxPages; i++) {
            executor.execute(() -> {
                try {
                    var scrapper = new OtomotoWebScrapper(url+"&page="+ atomicInteger.getAndIncrement());
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