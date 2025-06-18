package br.edu.infnet.folha.at;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private static Javalin app;
    private static int port;

    @BeforeAll
    static void beforeAll() {
        app = Javalin.create();
        new MensalistaController(app);
        app.start(0);
        port = app.port();
    }

    @AfterAll
    static void afterAll() {
        app.stop();
    }

    @Test
    void testarHello() throws Exception {
        URL url = new URL("http://localhost:" + port + "/hello");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        assertEquals(200, connection.getResponseCode());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseBody = in.readLine();
            assertEquals("Hello, Javalin!", responseBody);
        }
    }

    @Test
    void deveCriarMensalistaComPOST() throws Exception {
        URL url = new URL("http://localhost:" + port + "/mensalista");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonBody = "{\"matricula\":123,\"nome\":\"Carlos Augusto\",\"cargo\":\"Pesquisador\",\"salario\":2500.0}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        assertEquals(201, connection.getResponseCode());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseBody = in.readLine();
            assertNotNull(responseBody);
            assertTrue(responseBody.contains("\"nome\":\"Carlos Augusto\""));
            assertTrue(responseBody.contains("\"matricula\":123"));
        }
    }

    @Test
    void deveBuscarMensalistaPorMatriculaComGET() throws Exception {
        URL postUrl = new URL("http://localhost:" + port + "/mensalista");
        HttpURLConnection postConn = (HttpURLConnection) postUrl.openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type", "application/json");
        postConn.setDoOutput(true);

        String jsonBody = "{\"matricula\":456,\"nome\":\"Ana Paula\",\"cargo\":\"Diretora\",\"salario\":6000.0}";

        try (OutputStream os = postConn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(201, postConn.getResponseCode());

        URL getUrl = new URL("http://localhost:" + port + "/mensalista/456");
        HttpURLConnection getConn = (HttpURLConnection) getUrl.openConnection();
        getConn.setRequestMethod("GET");

        assertEquals(200, getConn.getResponseCode());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(getConn.getInputStream(), StandardCharsets.UTF_8))) {
            String response = in.readLine();
            assertNotNull(response);
            assertTrue(response.contains("\"nome\":\"Ana Paula\""));
            assertTrue(response.contains("\"matricula\":456"));
            assertTrue(response.contains("\"cargo\":\"Diretora\""));
            assertTrue(response.contains("\"salario\":6000.0"));
        }
    }

    @Test
    void deveListarTodosMensalistas() throws Exception {
        URL postUrl = new URL("http://localhost:" + port + "/mensalista");
        HttpURLConnection postConn = (HttpURLConnection) postUrl.openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type", "application/json");
        postConn.setDoOutput(true);

        String jsonBody = "{\"matricula\":789,\"nome\":\"João Silva\",\"cargo\":\"Monitor\",\"salario\":3000.0}";

        try (OutputStream os = postConn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(201, postConn.getResponseCode());

        URL getUrl = new URL("http://localhost:" + port + "/mensalistas");
        HttpURLConnection getConn = (HttpURLConnection) getUrl.openConnection();
        getConn.setRequestMethod("GET");

        assertEquals(200, getConn.getResponseCode());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(getConn.getInputStream(), StandardCharsets.UTF_8))) {
            String response = in.readLine();
            assertNotNull(response);
            assertTrue(response.startsWith("["));
            assertTrue(response.endsWith("]"));
            assertTrue(response.contains("\"matricula\":789"));
        }
    }
}
