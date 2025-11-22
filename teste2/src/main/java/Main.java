import br.vitrola.mindcare.model.ChatRequest;
import br.vitrola.mindcare.model.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;

public class Main {

    private static final String API_KEY = "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";
    private static final String MODEL = "gpt-4o-mini";


    public static void main(String[] args) throws IOException {


        Message message = new Message("user","O que é a linguagem java? Explique em uma frase.");
        ChatRequest chatRequest = new ChatRequest(MODEL, Collections.singletonList(message));

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        String json = gson.toJson(chatRequest);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()){

            if (response.isSuccessful()){

                String responseBody = response.body().string();
                String returnedMessage = gson.fromJson(responseBody, JsonObject.class)
                        .getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();

                System.out.println("Resposta da API");
                System.out.println(returnedMessage);

            } else {
                System.out.println(response.code() + " " + response.message());
            }
        }

    }
}
