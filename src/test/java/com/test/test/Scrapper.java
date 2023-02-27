package com.test.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.json.JSONArray;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

public class Scrapper {
	String url;
	@Test
	public void invalidWikiUrl() throws Exception {
		
		// invalidWikiPedia url
		url = "https://en.wikipedia.org/wiki/EloMusk";
		HashSet<String> wikiLinks = getWikiLinks(url, 20);
	}

	@Test
	public void validWikiUrl() throws Exception { 
		// WikiPedia url
		url = "https://en.wikipedia.org/wiki/Elon_Musk";
		HashSet<String> wikiLinks = getWikiLinks(url, 20);
		JSONArray wikiPages = new JSONArray();
		JSONObject obj = new JSONObject();

		int totalLinksCountOrUniqueCount = wikiLinks.size();
		System.out.println(totalLinksCountOrUniqueCount);
		obj.put("totalLinksCountOrUniqueCount", totalLinksCountOrUniqueCount);

		for (String wikiLink : wikiLinks) {
			wikiPages.put(wikiLink);
		}

		obj.put("Wiki Links", wikiPages);

		System.out.println(obj);

		FileWriter file = new FileWriter(
				System.getProperty("user.dir") + "/src/test/java/com/test/test/WikiLinks.json");
		file.write(obj.toString());
		file.flush();
		file.close();

	}

	public static HashSet<String> getWikiLinks(String url, int number) throws Exception {

		HashSet<String> wikiLinks = new HashSet();

		try {
			// Connect to the URL and retrieve the page HTML
			Document doc = Jsoup.connect(url).get();

			// Select all the links on the page
			Elements links = doc.select("a[href]");

			// Iterate through the links and print out the Wikipedia pages
			for (Element link : links) {
				String href = link.attr("href");
				if (wikiLinks.size() < number) {
					if (href.startsWith("/wiki/")) {
						String WikiLink = "https://en.wikipedia.org" + href;
						wikiLinks.add(WikiLink);
					}
				}
			}
		} catch (IOException e) {
			throw new Exception("Not a valid wiki link");
		}

		return wikiLinks;
	}
}