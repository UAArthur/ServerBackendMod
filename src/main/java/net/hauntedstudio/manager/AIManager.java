package net.hauntedstudio.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.hauntedstudio.AppBackend;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIManager {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Queue<MessageTask> messageQueue = new LinkedList<>();
    private boolean isProcessing = false;

    public void sendMessageToAI(String uuid, String username, String message) {
        System.out.println("Message To AI: "+ message);
        messageQueue.offer(new MessageTask(uuid, username, message));
        processQueue();
    }

    private synchronized void processQueue() {
        if (!isProcessing && !messageQueue.isEmpty()) {
            isProcessing = true;
            MessageTask task = messageQueue.poll();
            executor.submit(() -> {
                try {
                    String aiResponse = generateAIResponse(task.getMessage());
                    System.out.println(aiResponse);
                    AppBackend.webhookSender.sendMessagetoWebhook(aiResponse, "Shiroo [AI]", "48a7773acdac4014ac3faec0c1c2b4dd");
                    AppBackend.server.getPlayerManager().broadcast(Text.literal("§d§l[AI] §fShiroo: §7" + AppBackend.webhookSender.escapeSpecialCharacters(aiResponse)), false);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    isProcessing = false;
                    processQueue(); // Process next task in the queue
                }
            });
        }
    }

    private String generateAIResponse(String message) throws IOException {
        // Create URL
        URL url = new URL("http://localhost:11434/api/generate");

        // Open connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method
        connection.setRequestMethod("POST");

        // Set request headers
        connection.setRequestProperty("Content-Type", "application/json");

        // Enable output and disable caching
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        // Create JSON data to send
        String jsonData = "{ \"model\": \"neural-chat\", \"prompt\": \"" + message + "\", \"stream\": false }";

        // Write JSON data to connection output stream with UTF-8 encoding
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(jsonData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        // Get response code
        int responseCode = connection.getResponseCode();

        // Read response with UTF-8 encoding
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        // Parse JSON response using GSON with UTF-8 encoding
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

        // Extract "response" field
        return jsonResponse.get("response").getAsString();
    }
    // Cleanup resources
    public void shutdown() {
        executor.shutdown();
    }

    private static class MessageTask {
        private final String uuid;
        private final String username;
        private final String message;

        public MessageTask(String uuid,String username, String message) {
            this.uuid = uuid;
            this.username = username;
            this.message = message;
        }

        public String getUuid() {
            return uuid;
        }

        public String getUsername() {
            return username;
        }

        public String getMessage() {
            return message;
        }
    }
}
