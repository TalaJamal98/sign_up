package com.example.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.signup.Model.model1;
import com.example.signup.adapter.Adapter;

public class CategoryActivity extends AppCompatActivity {


    RecyclerView rvList;
    Adapter adapter;
    String s="";
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        b=findViewById(R.id.b);
        rvList = findViewById(R.id.list);
        adapter = new Adapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);
        adapter.setData(getList());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s="";
                int size = ((Adapter) rvList.getAdapter()).getItemCount();
                for (int i = 0; i < size; i++) {
                    if (((Adapter) rvList.getAdapter()).getItem(i).isSelected()) {
                        // Get each selected item
                        if(!((Adapter) rvList.getAdapter()).getItem(i).models.isEmpty())
                            s+=((Adapter) rvList.getAdapter()).getItem(i).getName()+"/";

                        else
                            s+=((Adapter) rvList.getAdapter()).getItem(i).getName();

                        // Do something with the item like save it to a selected items array.
                    }
                }
                Intent data = new Intent();
                data.putExtra("myData1", s);
                Log.e("hhh", s);
                setResult(RESULT_OK, data);
                finish();


            }
        });

    }


    public ArrayList<model1> getList() {

        ArrayList<model1> models = new ArrayList<>();

        model1 model1_1 = new model1("Apparel", 1 );
        model1 model1_1_1 = new model1("Men's Clothes", 2);
        model1 model1_1_2 = new model1("Women's Clothes", 2);
        model1 model1_1_3 = new model1("Girl-Kids' Clothes", 2);
        model1 model1_1_4 = new model1("Men's shoes", 2);
        model1 model1_1_5 = new model1("Women's shoes", 2);
        model1 model1_1_6 = new model1("Girl-Kids' shoes", 2);
        model1 model1_1_7 = new model1("Men's Accessories", 2);
        model1 model1_1_8 = new model1("Women's Accessories", 2);
        model1 model1_1_9 = new model1("Girl-Kids' Accessories", 2);
        model1 model1_1_10 = new model1("Boy-Kids' Accessories", 2);
        model1 model1_1_11 = new model1("Boy-Kids' Clothes", 2);
        model1 model1_1_12 = new model1("Boy-Kids' Shoes", 2);


        model1 model1_1_1_1 = new model1("Shirts", 3);//
        model1 model1_1_1_2 = new model1("Coats & Jackets", 3);//
        model1 model1_1_1_3 = new model1("Tops", 3);
        model1 model1_1_1_4 = new model1("Sweaters", 3);//
        model1 model1_1_1_5 = new model1("Jeans", 3);//
        model1 model1_1_1_6 = new model1("Pants", 3);//
        model1 model1_1_1_7 = new model1("Activewear", 3);//
        model1 model1_1_1_8 = new model1("Dress Shirts", 3);//
        model1 model1_1_1_9 = new model1("Casual Shirts", 3);//




        model1 model1_1_2_1 = new model1("Dresses", 3);
        model1 model1_1_2_2 = new model1("Coats & Jackets", 3);
        model1 model1_1_2_4 = new model1("Sweaters", 3);
        model1 model1_1_2_5 = new model1("Jeans", 3);
        model1 model1_1_2_6 = new model1("Pants", 3);
        model1 model1_1_2_7 = new model1("Activewear", 3);
        model1 model1_1_2_8 = new model1("Jumpsuits", 3);
        model1 model1_1_2_9 = new model1("T-Shirts", 3);
        model1 model1_1_2_10 = new model1("Skirts", 3);


        model1 model1_1_3_1 = new model1("Dresses", 3);
        model1 model1_1_3_2 = new model1("Shorts", 3);
        model1 model1_1_3_3 = new model1("Outfits", 3);
        model1 model1_1_3_4 = new model1("Shirts", 3);
        model1 model1_1_3_5 = new model1("T-Shirts", 3);
        model1 model1_1_3_6 = new model1("Pants", 3);
        model1 model1_1_3_7 = new model1("Outerwear", 3);
        model1 model1_1_3_8 = new model1("Jumpsuits", 3);
        model1 model1_1_3_10 = new model1("Skirts", 3);


        model1 model1_1_4_1 = new model1("Athletic", 3);
        model1 model1_1_4_2 = new model1("Boots", 3);
        model1 model1_1_4_3 = new model1("Casual", 3);
        model1 model1_1_4_4 = new model1("Dress Shoes", 3);
        model1 model1_1_4_5 = new model1("Sandal", 3);

        model1 model1_1_5_1 = new model1("Flats", 3);
        model1 model1_1_5_2 = new model1("Boots", 3);
        model1 model1_1_5_3 = new model1("Heels", 3);
        model1 model1_1_5_4 = new model1("Sneakers", 3);
        model1 model1_1_5_5 = new model1("Sandals", 3);





        model1_1.models.add(model1_1_1);
        model1_1.models.add(model1_1_2);
        model1_1.models.add(model1_1_3);
        model1_1.models.add(model1_1_4);
        model1_1.models.add(model1_1_5);
        model1_1.models.add(model1_1_6);
        model1_1.models.add(model1_1_7);
        model1_1.models.add(model1_1_8);
        model1_1.models.add(model1_1_9);

        model1_1_2.models.add(model1_1_2_1);
        model1_1_2.models.add(model1_1_2_2);
        model1_1_2.models.add(model1_1_2_4);
        model1_1_2.models.add(model1_1_2_5);
        model1_1_2.models.add(model1_1_2_6);
        model1_1_2.models.add(model1_1_2_7);
        model1_1_2.models.add(model1_1_2_8);
        model1_1_2.models.add(model1_1_2_9);
        model1_1_2.models.add(model1_1_2_10);

        model1_1_1.models.add(model1_1_1_1);
        model1_1_1.models.add(model1_1_1_2);
        model1_1_1.models.add(model1_1_1_3);
        model1_1_1.models.add(model1_1_1_4);
        model1_1_1.models.add(model1_1_1_5);
        model1_1_1.models.add(model1_1_1_6);
        model1_1_1.models.add(model1_1_1_7);
        model1_1_1.models.add(model1_1_1_8);
        model1_1_1.models.add(model1_1_1_9);


        model1_1_3.models.add(model1_1_3_1);
        model1_1_3.models.add(model1_1_3_2);
        model1_1_3.models.add(model1_1_3_3);
        model1_1_3.models.add(model1_1_3_4);
        model1_1_3.models.add(model1_1_3_5);
        model1_1_3.models.add(model1_1_3_6);
        model1_1_3.models.add(model1_1_3_7);
        model1_1_3.models.add(model1_1_3_8);
        model1_1_3.models.add(model1_1_3_10);

        model1_1_4.models.add(model1_1_4_1);
        model1_1_4.models.add(model1_1_4_2);
        model1_1_4.models.add(model1_1_4_3);
        model1_1_4.models.add(model1_1_4_4);
        model1_1_4.models.add(model1_1_4_5);

        model1_1_5.models.add(model1_1_5_1);
        model1_1_5.models.add(model1_1_5_2);
        model1_1_5.models.add(model1_1_5_3);
        model1_1_5.models.add(model1_1_5_4);
        model1_1_5.models.add(model1_1_5_5);


        model1 model1_2 = new model1("Manuela Kass", 1);
        model1 model1_2_1 = new model1("Roseanna Branham", 2);
        model1 model1_2_1_1 = new model1("Dennise Lasso", 3);
        model1 model1_2_1_2 = new model1("Sabrina Shively", 3);
        model1 model1_2_1_3 = new model1("Jin Haecker", 3);
        model1 model1_2_1_4 = new model1("Season Parrett  ", 3);
        model1_2_1.models.add(model1_2_1_1);
        model1_2_1.models.add(model1_2_1_2);
        model1_2_1.models.add(model1_2_1_3);
        model1_2_1.models.add(model1_2_1_4);

        model1 model1_2_2 = new model1("Vicky Parkhurst", 2);
        model1 model1_2_3 = new model1("Taisha Dragoo", 2);
        model1 model1_2_4 = new model1("Abbey Ballance", 2);
        model1_2.models.add(model1_2_1);
//        model1_2.models.add(model1_2_2);
//        model1_2.models.add(model1_2_3);
//        model1_2.models.add(model1_2_4);

//        model1_2.models.add(model1_2_1);
//        model1_2.models.add(model1_2_2);
//        model1_1.models.add(model1_2_3);
//        model1_1.models.add(model1_2_4);


        model1 model1_3 = new model1("Arlinda Fogal", 1);

        model1 model1_4 = new model1("Stephen Cabe", 1);
        model1 model1_4_1 = new model1("Cherilyn Lehn", 2);
        model1 model1_4_2 = new model1("Lashay Baumer", 2);
        model1 model1_4_3 = new model1("Abbie Kilmer", 2);
        model1 model1_4_4 = new model1("Clinton Boyers", 2);
        model1_4.models.add(model1_4_1);
        model1_4.models.add(model1_4_2);
        model1_4.models.add(model1_4_3);
        model1_4.models.add(model1_4_4);

        models.add(model1_1);
        models.add(model1_2);
        models.add(model1_3);
        models.add(model1_4);


        return models;
    }

}