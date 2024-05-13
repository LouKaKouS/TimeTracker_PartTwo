package com.example.timetrackerv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseTime extends AppCompatActivity {

    private NumberPicker startHourSelected;
    private NumberPicker startMinuteSelected;
    private NumberPicker endHourSelected;
    private NumberPicker endMinuteSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_settings);

        // Initialiser les NumberPickers
        startHourSelected = findViewById(R.id.hourStart);
        startMinuteSelected = findViewById(R.id.minuteStart);
        endHourSelected = findViewById(R.id.hourEnd);
        endMinuteSelected = findViewById(R.id.minuteEnd);

        // Configurez les valeurs minimales et maximales pour les NumberPickers
        startHourSelected.setMinValue(0);
        startHourSelected.setMaxValue(23);
        startMinuteSelected.setMinValue(0);
        startMinuteSelected.setMaxValue(3);

        endHourSelected.setMinValue(0);
        endHourSelected.setMaxValue(23);
        endMinuteSelected.setMinValue(0);
        endMinuteSelected.setMaxValue(3);

        // Définir les valeurs acceptables pour les minutes (00, 15, 30, 45)
        String[] minuteValues = {"00", "15", "30", "45"};

        // Définir les valeurs pour le NumberPicker des minutes
        startMinuteSelected.setDisplayedValues(minuteValues);
        endMinuteSelected.setDisplayedValues(minuteValues);

        Intent intent = new Intent(ChooseTime.this, ChooseDifficulty.class);
    }

    // Méthode pour enregistrer les données dans la base de données
    private void saveDataToDatabase(String startTime, String endTime) {
        // Ici, vous pouvez utiliser votre instance de DatabaseHelper pour enregistrer les données dans la base de données
        // Par exemple :
        // myDb.insertStartTimeAndEndTime(startTime, endTime);
        // Assurez-vous d'avoir une instance de DatabaseHelper initialisée et prête à être utilisée
    }
}
