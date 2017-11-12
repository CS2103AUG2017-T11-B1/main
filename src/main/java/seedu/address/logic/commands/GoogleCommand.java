package seedu.address.logic.commands;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

//@@author philemontan
/**
 * This class inherits from the Oauth2Command class and is the parent class for all commands making calls to
 * Google's APIs.
 */
public abstract class GoogleCommand extends Oauth2Command {
    public static final String SERVICE_SOURCE = "GOOGLE";
    private static final String CLIENT_ID =
            "591065149112-69ikmid17q2trahg28gip4o8srmo47pv.apps.googleusercontent.com";
    protected String accessScope;
    protected TokenResponse authToken;
    protected GoogleCredential credential;
    protected HttpTransport httpTransport;
    protected JacksonFactory jsonFactory;

    protected GoogleCommand(String googleCommandType, String inputAccessScope)  {
        super(SERVICE_SOURCE + "_" + googleCommandType);
        accessScope = inputAccessScope;
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
    }

    /**Instantiates the GoogleCredential for OAuth2 requests.
     * This is the final step in the OAuth2 protocol for Google APIs
     * @param authCode is obtained from service provider, after successful authentication
     */
    protected void setupCredentials(String authCode) {
        authToken = new TokenResponse();
        authToken.setAccessToken(authCode);
        credential = new GoogleCredential().setFromTokenResponse(authToken);
    }

    protected static String getClientId() {
        return CLIENT_ID;
    }

}
