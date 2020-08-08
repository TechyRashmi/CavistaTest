package com.testleotech.cavistatest.Activity.activity.allClasses;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.testleotech.cavistatest.Activity.activity.database.DatabaseHelper;
import com.testleotech.cavistatest.R;


public class DisplayActivity extends AppCompatActivity {


    ImageView ivBack;
    ImageView ivImage;
    TextView tvHeader;
    Button btnSubmit;
    EditText etComment;

    //Database
    DatabaseHelper dba;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        i=getIntent();

//
//        ArrayList<String> list=dba.fetchAllDataSpecificColumn("comment",i.getStringExtra("id"));
//        Log.e("list",list.toString());

       //




        ivBack=findViewById(R.id.ivBack);
        ivImage=findViewById(R.id.ivImage);
        tvHeader=findViewById(R.id.tvHeader);
        btnSubmit=findViewById(R.id.btnSubmit);
        etComment=findViewById(R.id.etComment);


        //initialize database
        dba=new DatabaseHelper(this);



        try {

            String comment= dba.fetchAllDataSpecificColumn("comment",i.getStringExtra("id"));
            Log.e("commentE",comment);

            etComment.setText(comment);

        }
        catch (Exception e)
        {
            Log.e("Exception",e.toString());
        }

        //Set Values
        tvHeader.setText(i.getStringExtra("title"));

        Glide.with(this)
                .load(i.getStringExtra("image"))
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(ivImage);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etComment.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(DisplayActivity.this,"Comments can't be blank",Toast.LENGTH_LONG).show();
                }
                else
                {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.image_id, i.getStringExtra("id"));
                    values.put(DatabaseHelper.comment, etComment.getText().toString());
                    dba.insertUpdateData(values,DatabaseHelper.TABLE_COMMENT,DatabaseHelper.image_id,i.getStringExtra("id"));
                    Toast.makeText(DisplayActivity.this,"Comments added successfully",Toast.LENGTH_LONG).show();
                }




            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}