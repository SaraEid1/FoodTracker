package com.example.foodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
    }
    public static class MyCustomAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<Pair<String,String>> list = new ArrayList<Pair<String,String>>();
        private Context context;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private String TAG = "test";
    

        public MyCustomAdapter(ArrayList<Pair<String,String>> list, Context context) {
            this.list = list;
            this.context = context;
        }



        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.activity_items_list, null);
            }

            TextView FoodItemText = (TextView)view.findViewById(R.id.foodItem);
            TextView DateItemText = (TextView)view.findViewById(R.id.expirydate);

            FoodItemText.setText(list.get(position).first);
            DateItemText.setText(list.get(position).second);

            ImageButton deleteButton = (ImageButton)view.findViewById(R.id.delete);
            ImageButton editButton = (ImageButton)view.findViewById(R.id.edit);

            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String itemToDel = list.get(position).first;
                    deleteDBDocument(itemToDel);
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            editButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    notifyDataSetChanged();
                }

            });

            return view;
        }
        public void deleteDBDocument(String item){
            db.collection("FoodCollection").document(item)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
    }

}
