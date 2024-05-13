package com.example.timetrackerv2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseDifficulty extends AppCompatActivity {

    private NumberPicker difficultyNumber;
    private NumberPicker startHourPicker;
    private NumberPicker startMinutePicker;
    private NumberPicker endHourPicker;
    private NumberPicker endMinutePicker;
    private Button buttonFinishSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_settings);

        difficultyNumber = findViewById(R.id.difficultyNumber);
        startHourPicker = findViewById(R.id.hourStart);
        startMinutePicker = findViewById(R.id.minuteStart);
        endHourPicker = findViewById(R.id.hourEnd);
        endMinutePicker = findViewById(R.id.minuteEnd);
        buttonFinishSettings = findViewById(R.id.buttonFinish);

        // Configurer les valeurs minimales et maximales du NumberPicker
        difficultyNumber.setMinValue(0);
        difficultyNumber.setMaxValue(5);

        // Ajouter un écouteur d'événements sur le bouton "Finish Settings"
        buttonFinishSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs sélectionnées dans les NumberPickers
                int startHour = startHourPicker.getValue();
                int startMinute = startMinutePicker.getValue();
                int endHour = endHourPicker.getValue();
                int endMinute = endMinutePicker.getValue();

                // Enregistrer les valeurs dans la base de données
                saveDataToDatabase(startHour, startMinute, endHour, endMinute);
                finish();
            }
        });
    }

    // Méthode pour enregistrer les données dans la base de données
    private void saveDataToDatabase(int startHour, int startMinute, int endHour, int endMinute) {
        // Créer une instance de DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Appeler la méthode d'insertion de la base de données
        boolean isInserted = databaseHelper.insertData(startHour, startMinute, endHour, endMinute);

        // Vérifier si l'insertion a réussi
        if (isInserted) {
            // Afficher un message indiquant que les données ont été enregistrées avec succès
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Afficher un message d'erreur si l'insertion a échoué
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }
}
