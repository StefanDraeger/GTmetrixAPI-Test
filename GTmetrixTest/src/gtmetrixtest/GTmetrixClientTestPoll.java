package gtmetrixtest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

@FixMethodOrder(MethodSorters.DEFAULT)
public class GTmetrixClientTestPoll extends AbstractGTmetrixClientTest {

	private static final String JSON_KEY_STATE = "state";
	private static final String QUEUED = "queued";
	private static final String STARTED = "started";
	private static final String COMPLETE = "complete";

	private JsonNode reportMetaData;

	@BeforeAll
	static void setup() {
		GTmetrixClient.Builder builder = new GTmetrixClient.Builder();
		builder.apiKey(API_KEY);
		builder.username(USERNAME);
		builder.url(URL);
		client = builder.build();
	}

	@Test
	void generateReport() throws UnirestException, InterruptedException {
		assertNotNull(client);
		reportMetaData = client.execute();
		assertNotNull(reportMetaData);
		System.out.println(reportMetaData.toString());

		JSONObject object = reportMetaData.getObject();

		assertNotNull(object);

		String poll_state_url = (String) object.get(JSON_KEY_POLL_STATE_URL);

		assertNotNull(poll_state_url);
		assertTrue(poll_state_url.trim().length() > 0);

		String pollState;
		JSONObject jsonObject;

		do {
			Thread.sleep(1000);
			JsonNode pollTestReport = client.pollTestReport(poll_state_url);
			jsonObject = pollTestReport.getObject();
			assertNotNull(jsonObject);
			pollState = (String) jsonObject.get(JSON_KEY_STATE);
			System.out.println(jsonObject.toString());
		} while (pollState.equals(QUEUED) || pollState.equals(STARTED));

		assertNotNull(jsonObject);
		assertTrue(pollState.equals(COMPLETE));
		System.out.println(jsonObject.toString());
	}
}
