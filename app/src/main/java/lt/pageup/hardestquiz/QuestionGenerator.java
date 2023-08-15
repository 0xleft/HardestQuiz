package lt.pageup.hardestquiz;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuestionGenerator {

    public static Question generateQuestion() {
        String apiUrl = "https://pageup.lt/api/ai/generate_random_quiz";

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            JSONObject jsonObject = new JSONObject(response.toString());

            String question = jsonObject.getString("question");
            String[] answers = new String[4];
            answers[0] = jsonObject.getString("answer1");
            answers[1] = jsonObject.getString("answer2");
            answers[2] = jsonObject.getString("answer3");
            answers[3] = jsonObject.getString("answer4");
            int correctAnswerIndex = jsonObject.getInt("correct_answer");
            connection.disconnect();

            return new Question(question, answers, correctAnswerIndex);
        } catch (IOException | JSONException e) {
            Log.w("SHEIZE", e);
            return null;
        }
    }
}
