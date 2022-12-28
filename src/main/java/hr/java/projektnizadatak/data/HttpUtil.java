package hr.java.projektnizadatak.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtil {
	private static final HttpClient client = HttpClient.newHttpClient();
	
	public static String fetchFromEndpoint(URI endpoint) {
		var request = HttpRequest.newBuilder(endpoint)
			.header("accept", "application/json")
			.build();

		try {
			return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (IOException | InterruptedException e) {
			// TODO?
			throw new RuntimeException(e);
		}
	}
}
