package br.com.softwareminas.quizzzz.util;

import android.os.Bundle;

import br.com.softwareminas.quizzzz.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView historyTextView = findViewById(R.id.history_text_view);
        // Certifique-se de usar o ID correto
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consultar o banco de dados
        String[] projection = {
                DatabaseHelper.COLUMN_SCORE,
                DatabaseHelper.COLUMN_DATE
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_HISTORY,
                projection,
                null,
                null,
                null,
                null,
                null
        );

// Verifica quantos registros foram encontrados
        Log.d("HistoryActivity", "Número de registros encontrados: " + cursor.getCount());

        StringBuilder historyBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            historyBuilder.append("Pontuação: ").append(score).append(" | Data: ").append(date).append("\n");
        }

        cursor.close();
        db.close();

        historyTextView.setText(historyBuilder.toString());

    }
}

