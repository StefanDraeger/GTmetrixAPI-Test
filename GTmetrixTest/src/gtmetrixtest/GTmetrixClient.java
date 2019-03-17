package gtmetrixtest;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GTmetrixClient {

	private static final String API_URL = "https://gtmetrix.com/api/0.1/test";

	private String apiKey;
	private String username;

	private String url;

	private boolean useProxy = false;
	private String ipAddress;
	private int portNumber;

	private boolean useAuthentication = false;
	private String authUsername;
	private String authPassword;

	public GTmetrixClient(Builder builder) {
		this.apiKey = builder.apiKey;
		this.username = builder.username;
		this.url = builder.url;

		this.useProxy = builder.useProxy;
		this.ipAddress = builder.ipAddress;
		this.portNumber = builder.portNumber;

		this.useAuthentication = builder.useAuthentication;
		this.authUsername = builder.authUsername;
		this.authPassword = builder.authPassword;
	}

	public JsonNode execute() throws UnirestException {
		if (useProxy) {

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ipAddress, portNumber),
					new UsernamePasswordCredentials(authUsername, authPassword));

			HttpHost httpHost = new HttpHost(ipAddress, portNumber);

			Unirest.setProxy(httpHost);
		}

		HttpResponse<JsonNode> respond = Unirest.post(API_URL).basicAuth(username, apiKey).field("url", url).asJson();
		return respond.getBody();
	}

	public JsonNode pollTestReport(String pollStateUrl) throws UnirestException {
		if (useProxy) {

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(ipAddress, portNumber),
					new UsernamePasswordCredentials(authUsername, authPassword));

			HttpHost httpHost = new HttpHost(ipAddress, portNumber);

			Unirest.setProxy(httpHost);
		}		
		return Unirest.get(pollStateUrl).basicAuth(username, apiKey).asJson().getBody();
	}

	protected String getApiKey() {
		return apiKey;
	}

	protected String getUsername() {
		return username;
	}

	protected String getUrl() {
		return url;
	}

	public String getAuthUsername() {
		return authUsername;
	}

	public String getAuthPassword() {
		return authPassword;
	}

	public static class Builder {

		private String apiKey = "-undefined-";
		private String username = "jondoe";

		private String url = "https://example.com";

		private boolean useProxy = false;
		private String ipAddress;
		private int portNumber;

		private boolean useAuthentication = false;
		private String authUsername;
		private String authPassword;

		public GTmetrixClient build() {
			return new GTmetrixClient(this);
		}

		public Builder apiKey(String apiKey) {
			this.apiKey = apiKey;
			return this;
		}

		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder proxy(String ipAddress, int portNumber) {
			this.useProxy = true;
			this.ipAddress = ipAddress;
			this.portNumber = portNumber;
			return this;
		}

		public Builder authentication(String username, String password) {
			useAuthentication = true;
			this.authUsername = username;
			this.authPassword = password;
			return this;
		}

	}

}
