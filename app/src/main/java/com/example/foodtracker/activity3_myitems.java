package com.example.foodtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.sort;

public class activity3_myitems extends AppCompatActivity {
    private ImageButton back2;
    private ImageButton home2;

    public static final String ITEM_KEY = "item";
    public static final String DATE_KEY = "date";
    public static final String TAG = "test";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Pair<String, String>> displayList = new ArrayList<>();
    ListView listView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3_myitems);
        back2 = (ImageButton) findViewById (R.id.imageButton5);

        listView = (ListView) findViewById(R.id.listview1);

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity5();
            }
        });

       /* home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity6();
            }
        }); */

        fetchItems();
    }
    public void openactivity5() {
        Intent intent = new Intent(this, activity2.class);
        startActivity(intent);
    }

    public void openactivity6() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void fetchItems() {
        db.collection("FoodCollection").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                //Add data from database into FoodList as custom object "FoodItem"
                ArrayList<FoodItem> FoodList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    FoodItem newData = doc.toObject(FoodItem.class);
                    FoodList.add(newData);
                }
                sort(FoodList);

                displayList = new ArrayList<>();
                for (FoodItem foodItem : FoodList) {
                    displayList.add(new Pair<>(foodItem.getItem(), foodItem.getStringDate()));
                }
                Log.d(TAG, Arrays.toString(displayList.toArray()));

                List<HashMap<String, String>> listItems = new ArrayList<>();
                SimpleAdapter simpleAdapter = new SimpleAdapter(context, listItems, R.layout.activity_items_list,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.foodItem, R.id.expirydate});

                for (Pair<String, String> pair : displayList){
                    HashMap<String, String> resultsMap = new HashMap<>();
                    resultsMap.put("First Line", pair.first);
                    resultsMap.put("Second Line", pair.second);
                    listItems.add(resultsMap);
                }
                ItemsList.MyCustomAdapter customAdapter = new ItemsList.MyCustomAdapter(displayList, context);
                listView.setAdapter(customAdapter);
            }
        });
    }
}
