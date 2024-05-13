package com.example.timetrackerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner {

    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;
    private DatabaseHelper myDB;
    private List<AppModel> mList;
    private DatabaseAdapter adapter;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupération des vues depuis le layout XML
        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);

        // Initialisation de la base de données et de la liste des tâches
        myDB = new DatabaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new DatabaseAdapter(myDB, MainActivity.this);

        // Configuration du RecyclerView
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        // Initialisation de selectedDate avec la date actuelle
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        selectedDate = dateFormat.format(calendar.getTime());

        // Mise à jour de l'interface utilisateur avec les tâches pour la date sélectionnée
        updateUI(selectedDate);

        // Affichage de la date actuelle dans un TextView
        TextView dateTextView = findViewById(R.id.date_indicator);
        dateTextView.setText(selectedDate);

        // Gestionnaire d'événements pour le changement de date dans le CalendarView
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Mise à jour de selectedDate lorsque l'utilisateur sélectionne une date
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                // Mise à jour de l'interface utilisateur avec les tâches pour la nouvelle date sélectionnée
                updateUI(selectedDate);
            }
        });

        // Gestionnaire d'événements pour le bouton flottant
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    // Passer la date sélectionnée à la méthode newInstance pour afficher une nouvelle tâche
                    AddNewTask.newInstance(selectedDate).show(getSupportFragmentManager(), AddNewTask.TAG);
                } else {
                    // Afficher un message à l'utilisateur s'il n'a pas sélectionné de date
                    Toast.makeText(MainActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Attachement de ItemTouchHelper pour gérer les mouvements et les suppressions dans le RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        Intent intent = new Intent(MainActivity.this, ChooseTime.class);
    }

    // Méthode pour mettre à jour l'interface utilisateur avec les tâches pour une date donnée
    private void updateUI(String selectedDate) {
        mList = myDB.getAllTasks(selectedDate);
        Collections.reverse(mList);
        adapter.setTasks(mList);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        // Mise à jour de l'interface utilisateur après la fermeture de la boîte de dialogue
        updateUI(selectedDate);
    }
}
