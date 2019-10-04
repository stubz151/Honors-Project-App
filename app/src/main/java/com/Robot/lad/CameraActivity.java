package com.Robot.lad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import TopCodes.TopCode;
import TopCodes.Scanner;
import java.util.ArrayList;



public class CameraActivity extends AppCompatActivity implements MyAdapter.ItemClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private ArrayList<TopCode> codes= new ArrayList<>();
    Scanner scanner = new Scanner();
    MyAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> moveNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        moveNames = new ArrayList<>();
        // set up the RecyclerView

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CameraActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new MyAdapter(this, moveNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Button btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i =codes.size()-1 ; i <= 0; i++)
                {
                    SendThread.sendInt(codes.get(i).getCode());
                }
                SendThread.sendInt(-1);
            }
        });
        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendThread.sendInt(567);
                SendThread.sendInt(-1);
                Intent myIntent = new Intent(CameraActivity.this, MainActivity.class);
                SendThread.StopThread();
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        codes.remove(position);
        setList();
        recyclerView.removeViewAt(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, codes.size());

    }

    private String getName(int code)
    {
        switch (code){
            case 55: return "Forward";
            case 87: return "Right";
            case 79: return "Left";
            case 47: return "Backward";
            case 155: return "While Not blocked move Forward";
            case 91: return "While Not blocked Backwards move Backward";
            default: return "Invalid scan option";
        }

    }
    private void setList()
    {
        moveNames.clear();
        for (int i =0;  i < codes.size();i++)
        {
            moveNames.add(getName(codes.get(i).getCode()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageBitmap = imageBitmap;
            codes.addAll(scanner.scan(mImageBitmap));
            setList();
        }
    }
}