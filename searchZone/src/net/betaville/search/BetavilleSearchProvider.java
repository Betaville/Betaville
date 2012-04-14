/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betaville.search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.quicksearch.SearchProvider;
import org.netbeans.spi.quicksearch.SearchRequest;
import org.netbeans.spi.quicksearch.SearchResponse;
import org.openide.awt.HtmlBrowser;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

public class BetavilleSearchProvider implements SearchProvider {

    /**
     * Method is called by infrastructure when search operation was requested.
     * Implementors should evaluate given request and fill response object with
     * apropriate results
     *
     * @param request Search request object that contains information what to
     * search for
     * @param response Search response object that stores search results. Note
     * that it's important to react to return value of
     * SearchResponse.addResult(...) method and stop computation if false value
     * is returned.
     */
    public void evaluate(SearchRequest request, SearchResponse response) {
        try {
            //The URL we are providing a search for:
            URL url = new URL("http://betaville.net");
            //Stuff needed by Tidy:
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            tidy.setTidyMark(false);
            tidy.setShowWarnings(false);
            tidy.setQuiet(true);

            //Get the org.w3c.dom.Document from Tidy,
            //or use a different parser of your choice:
            org.w3c.dom.Document doc = tidy.parseDOM(url.openStream(), null);

            //Get all "a" elements:
            NodeList list = doc.getElementsByTagName("a");

            //Get the number of elements:
            int length = list.getLength();

            //Loop through all the "a" elements:
            for (int i = 0; i < length; i++) {

                String href = null;
                if (null != list.item(i).getAttributes().getNamedItem("href")) {
                    //Get the "href" attribute from the current "a" element:
                    href = list.item(i).getAttributes().getNamedItem("href").getNodeValue();
                }

                //Get the "title" attribute from the current "a" element:
                if (null != list.item(i).getAttributes().getNamedItem("title")) {
                    String title = list.item(i).getAttributes().getNamedItem("title").getNodeValue();
                    //If the title matches the requested text:
                    if (title.toLowerCase().indexOf(request.getText().toLowerCase()) != -1) {
                        //Add the runnable and the title to the response and return if nothin is added:
                        if (!response.addResult(new OpenFoundArticle(href), title)) {
                            return;
                        }
                    }
                }
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }



        //sample code
        //for (SearchedItem item : getAllItemsToSearchIn()) {
        //    if (isConditionSatisfied(item, request)) {
        //        if (!response.addResult(item.getRunnable(), item.getDisplayName(),
        //	      item.getShortcut(), item.getDisplayHint())) {
        //	      break;
        //	  }
        //    }
        //}
    }

    private static class OpenFoundArticle implements Runnable {

        private String article;

        public OpenFoundArticle(String article) {
            this.article = article;
        }

        @Override
        public void run() {
            try {
                URL url = new URL("http://betaville.net" + article);
                StatusDisplayer.getDefault().setStatusText(url.toString());
                HtmlBrowser.URLDisplayer.getDefault().showURL(url);
            } catch (MalformedURLException ex) {
                Logger.getLogger(BetavilleSearchProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
