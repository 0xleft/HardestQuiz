package lt.pageup.hardestquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Button> buttons = new ArrayList<>();
    private TextView questionTextView;
    private Question question;
    private TextView streakTextView;
    private int streak = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncHelper.runAsync(this::setQuestion);
    }

    private void checkAnswer(@NotNull String answer, @NotNull Question question) {
        boolean correct = answer.equals(question.getAnswers()[question.getCorrectAnswerIndex() - 1]);
        if (correct) {
            streak++;
        } else {
            streak = 0;
        }

        streakTextView.setText("Streak: " + streak);

        for (Button button : buttons) {
            button.setClickable(false);
        }

        buttons.get(question.getCorrectAnswerIndex() - 1).setBackgroundColor(getResources().getColor(R.color.green));

        for (int i = 0; i < buttons.size(); i++) {
            if (i != question.getCorrectAnswerIndex() - 1) {
                buttons.get(i).setBackgroundColor(getResources().getColor(R.color.red));
            }

            buttons.get(i).setTextColor(getResources().getColor(R.color.black));
        }

        AsyncHelper.runAsync(() -> {
            try {
                Thread.sleep(2000);
                setQuestion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void resetButtonColor() {
        for (Button button : buttons) {
            button.setBackgroundColor(getResources().getColor(R.color.blue, getTheme()));
            button.setTextColor(getResources().getColor(R.color.white, getTheme()));
            button.setClickable(true);
        }
    }

    private void setQuestion(@NotNull Question question) {
        Log.e("SHIZE", "setQuestion");
        this.question = question;
        buttons = new ArrayList<>();
        buttons.add(findViewById(R.id.first_choice));
        buttons.add(findViewById(R.id.second_choice));
        buttons.add(findViewById(R.id.third_choice));
        buttons.add(findViewById(R.id.fourth_choice));

        resetButtonColor();

        questionTextView = findViewById(R.id.question);
        streakTextView = findViewById(R.id.streak);
        questionTextView.setText(question.getQuestion());

        for (int i = 0; i < question.getAnswers().length; i++) {
            buttons.get(i).setText(question.getAnswers()[i]);
        }

        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                Button clickedButton = (Button) v;
                String answer = clickedButton.getText().toString();

                checkAnswer(answer, this.question);
            });
        }
    }

    private void setQuestion() {
        runOnUiThread(() -> {
            setContentView(R.layout.loading);
        });
        Log.e("SHEIZE", "setQuestion");
        Question question = QuestionGenerator.generateQuestion();
        if (question == null) {
            setQuestion();
            return;
        }

        runOnUiThread(() -> {
            setContentView(R.layout.activity_main);
            setQuestion(question);
        });
    }
}