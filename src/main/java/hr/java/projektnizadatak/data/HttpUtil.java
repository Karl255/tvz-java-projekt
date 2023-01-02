package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import hr.java.projektnizadatak.shared.exceptions.UnexpectedInterruptException;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class HttpUtil {
	private static final HttpClient client = HttpClient.newHttpClient();

	public static String fetchFromEndpoint(URI endpoint) {
		var request = HttpRequest.newBuilder(endpoint)
			.header("accept", "application/json")
			.build();

		try {
			return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (IOException e) {
			throw new ReadOrWriteErrorException("Fetching endpoint: " + endpoint, e);
		} catch (InterruptedException e) {
			throw new UnexpectedInterruptException("Fetching endpoint: " + endpoint, e);
		}
	}

	@SafeVarargs
	public static URI buildUriWithParams(String endpoint, Pair<String, String>... parameters) {
		var sb = new StringBuilder(endpoint);
		boolean first = true;

		for (var parameter : parameters) {
			sb.append(first ? '?' : '&');
			first = false;

			sb.append(urlEncode(parameter.getKey()));

			if (parameter.getValue() != null) {
				sb.append('=')
					.append(urlEncode(parameter.getValue()));
			}
		}

		return URI.create(sb.toString());
	}

	private static String urlEncode(String s) {
		return URLEncoder.encode(s, Charset.defaultCharset());
	}
}
