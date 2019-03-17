package gtmetrixtest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

class GTmetrixClientTest extends AbstractGTmetrixClientTest {


	@BeforeAll
	static void setup() {
		GTmetrixClient.Builder builder = new GTmetrixClient.Builder();
		builder.apiKey(API_KEY);
		builder.username(USERNAME);
		builder.url(URL);
		client = builder.build();
	}

	@Test
	void testServiceResult() {
		JsonNode result = null;
		try {
			result = client.execute();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		assertNotNull(result);
		
		Object creditsLeft = result.getObject().get(JSON_KEY_CREDITS_LEFT);
		Object pollStateUrl = result.getObject().get(JSON_KEY_POLL_STATE_URL);
		Object testId = result.getObject().get(JSON_KEY_TEST_ID);
		
		assertNotNull(creditsLeft);
		assertNotNull(pollStateUrl);
		assertNotNull(testId);
		
		assertTrue(creditsLeft.toString().trim().length()>0);
		assertTrue(pollStateUrl.toString().trim().length()>0);
		assertTrue(testId.toString().trim().length()>0);
		
		System.out.println(result.toString());
		
	}

	@Test
	void testBuilder() {
		assertEquals(API_KEY, client.getApiKey());
		assertEquals(USERNAME, client.getUsername());
		assertEquals(URL, client.getUrl());
	}
	
}
