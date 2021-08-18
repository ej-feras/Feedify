package com.feedify.rest.service;

import org.springframework.stereotype.Service;

import com.feedify.rest.service.favicon.FaviconLoader;
import com.feedify.rest.service.favicon.FaviconLoader.FaviconCallback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FaviconExtractorService {

    public List<URL> findFaviconLinks(String websiteAdress) throws IOException {
        List<URL> links = new ArrayList<URL>();
        new FaviconLoader().getFavicons(websiteAdress, new FaviconCallback() {
            @Override
            public void onFaviconsLoaded(Set<URL> favicons) {
                for (URL fav : favicons) {
                    links.add(fav);
                }
            }
        });

        return links;
    }

}
