package br.edu.infnet.folha.at;

import org.junit.jupiter.api.Test;
import io.javalin.Javalin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MensalistaTest {

    @Test
    void deveCriarEBuscarMensalista() throws Exception {
        Javalin app = Javalin.create();
        new MensalistaController(app);
        app.start(0);
        int port = app.port();

        URL postUrl = new URL("http://localhost:" + port + "/mensalista");
        HttpURLConnection postConnection = (HttpURLConnection) postUrl.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);

        String jsonBody = "{\"matricula\":123,\"nome\":\"Carlos Augusto\",\"cargo\":\"Pesquisador\",\"salario\":2500.0}";

        try (OutputStream os = postConnection.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        assertEquals(201, postConnection.getResponseCode());

        URL getUrl = new URL("http://localhost:" + port + "/mensalista/123");
        HttpURLConnection getConnection = (HttpURLConnection) getUrl.openConnection();
        getConnection.setRequestMethod("GET");

        assertEquals(200, getConnection.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
        String responseBody = in.readLine();
        in.close();

        assertEquals(true, responseBody.contains("Carlos Augusto"));

        app.stop();
    }
}
