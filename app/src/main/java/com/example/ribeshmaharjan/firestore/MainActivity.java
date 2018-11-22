package com.example.ribeshmaharjan.firestore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    public static final String CLASS_KEY = "class";
    public static final String NAME_KEY = "name";
    public static final String TAG = "InspirationQuote";
    EditText name;
    EditText class_name;
    Button btn_save;
    Button btn_fetch;
    TextView mdisplay;
    Source source=Source.CACHE;
    private DocumentReference mDocRef= FirebaseFirestore.getInstance().document("sampleData/Inspiration");//collection/Document
    //DatainRealTime
    @Override
    protected void onStart() {

        super.onStart();

        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    String GetName=documentSnapshot.getString(NAME_KEY);
                    String GetClass=documentSnapshot.getString(CLASS_KEY);

                    mdisplay.setText("\""+ GetClass + "\"" +"--"+ GetName );
                }
                else if (e!=null)
                {
                    Log.w(TAG,"Got An Exception",e);
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.name_edittext);
        class_name=findViewById(R.id.class_editext);
        btn_save=findViewById(R.id.btn_save);
        btn_fetch=findViewById(R.id.btn_fetch);
        mdisplay=findViewById(R.id.display);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PersonName=name.getText().toString();
                String ClassName=class_name.getText().toString();

                if(PersonName.isEmpty()|| ClassName.isEmpty()){
                    return;
                }
                Map<String, Object> dataToSave= new HashMap<String, Object>();
                dataToSave.put(NAME_KEY,PersonName);
                dataToSave.put(CLASS_KEY,ClassName);
                mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document has been saved!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Document was not saved!!",e);
                    }
                });

            }
        });

        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            String GetName=documentSnapshot.getString(NAME_KEY);
                            String GetClass=documentSnapshot.getString(CLASS_KEY);

                            mdisplay.setText("\""+ GetClass + "\"" +"--"+ GetName );
                            //Other Ways https://www.youtube.com/watch?v=kDZYIhNkQoM&list=PLl-K7zZEsYLmxfvI4Ds2Atko79iVvxlaq
                            Map<String, Object> myData=documentSnapshot.getData();
                        }
                    }
                });
            }
        });

    }
}
