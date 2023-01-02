package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import hr.java.projektnizadatak.shared.exceptions.UnexpectedInterruptException;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class HttpUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	private static final HttpClient client = HttpClient.newHttpClient();

	public static String fetchFromEndpoint(URI endpoint) {
		var request = HttpRequest.newBuilder(endpoint)
			.header("accept", "application/json")
			.build();

		try {
			return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		} catch (IOException e) {
			String m = "Fetching endpoint: " + endpoint;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		} catch (InterruptedException e) {
			String m = "Fetching endpoint: " + endpoint;
			logger.error(m);

			throw new UnexpectedInterruptException(m, e);
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
