package com.example.timetrackerv2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private EditText mEditText;
    private Button mSaveButton;

    private DatabaseHelper myDb;

    // Méthode pour créer une nouvelle instance de AddNewTask avec la date sélectionnée
    public static AddNewTask newInstance(String selectedDate) {
        AddNewTask fragment = new AddNewTask();
        Bundle args = new Bundle();
        args.putString("selectedDate", selectedDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflater le layout pour le fragment
        View v = inflater.inflate(R.layout.task_add , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des vues et de la base de données
        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);
        myDb = new DatabaseHelper(getActivity());

        // Vérification si le fragment est en mode mise à jour
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);
            // Désactiver le bouton de sauvegarde si le champ de texte est déjà rempli
            if (task != null && task.length() > 0) {
                mSaveButton.setEnabled(false);
            }
        }

        // Gestionnaire de texte pour vérifier si le champ est vide
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    // Désactiver le bouton de sauvegarde et changer la couleur de fond en gris si le champ est vide
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    // Activer le bouton de sauvegarde et changer la couleur de fond en pourpre si le champ est rempli
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        // Gestionnaire de clics pour le bouton de sauvegarde
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();

                if (!text.isEmpty()) {
                    // Récupérer la date sélectionnée depuis les arguments
                    Bundle bundle = getArguments();
                    String selectedDate = bundle.getString("selectedDate");

                    // Insérer la tâche dans la base de données avec la date sélectionnée
                    AppModel item = new AppModel();
                    item.setTask(text);
                    item.setStatus(0); // Par défaut
                    item.setDate(selectedDate);
                    myDb.insertTask(item);

                    // Fermer le fragment avant de lancer l'activité ChooseTime
                    dismiss();

                    // Lancer l'activité ChooseTime pour choisir l'heure
                    Intent intent = new Intent(getActivity(), ChooseTime.class);
                    startActivity(intent);
                } else {
                    // Afficher un message d'erreur si le champ est vide
                    Toast.makeText(getActivity(), "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Informer l'activité parente que le dialogue est fermé
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}