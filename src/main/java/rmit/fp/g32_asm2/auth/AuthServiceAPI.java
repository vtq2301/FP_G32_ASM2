package rmit.fp.g32_asm2.auth;

import org.json.JSONObject;
import rmit.fp.g32_asm2.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class AuthServiceAPI {
    private static final String SUPABASE_URL = System.getenv("SUPABASE_URL");
    private static final String API_KEY = System.getenv("API_KEY");

    private User currentUser;


    public static AuthServiceAPI getInstance() {
        return new AuthServiceAPI();
    }

    public boolean signUp(String email, String password) {
        JSONObject res = sendAuthRequest("/auth/v1/signup", "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}");
        return res.get("status").equals("ok");
    }

    public String login(String email, String password) {
        JSONObject res = sendAuthRequest("/auth/v1/token?grant_type=password", "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}");
        return res.getString("id");
    }

    public String logout(String accessToken) {
        try {
            URI uri = new URI(SUPABASE_URL + "/auth/v1/logout");
            HttpURLConnection connection = setupConnection(uri, "POST", accessToken);
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_NO_CONTENT ? "Logged out successfully" : "Logout failed: " + responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public JSONObject getCurrentUser(String accessToken) {
        try {
            URI uri = new URI(SUPABASE_URL + "/auth/v1/user");
            HttpURLConnection connection = setupConnection(uri, "GET", accessToken);
            return readResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpURLConnection setupConnection(URI uri, String method, String accessToken) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("apikey", API_KEY);
        if (accessToken != null && !accessToken.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        }
        connection.setDoOutput(true);
        return connection;
    }

    private JSONObject sendAuthRequest(String path, String jsonInputString) {
        try {
            URI uri = new URI(SUPABASE_URL + path);
            HttpURLConnection connection = setupConnection(uri, "POST", null);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            return readResponse(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject readResponse(HttpURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
