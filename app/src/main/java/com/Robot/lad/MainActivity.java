package com.Robot.lad;

import androidx.appcompat.app.AppCompatActivity;
import lejos.pc.comm.NXTCommException;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {
    private NXTCommAndroid nxtCommAndroid;
    private DataOutputStream dos;
    private SendThread sendThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nxtCommAndroid = new NXTCommAndroid();
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (!bluetoothAdapter.isEnabled())
                    {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                    }
                    if (bluetoothAdapter.isEnabled())
                    {

                        boolean connected = false;
                        try {
                            connected = nxtCommAndroid.open(nxtCommAndroid.search("NXT")[0]);
                        } catch (NXTCommException e) {
                            e.printStackTrace();
                        }
                        if (!connected)
                        {
                            Toast.makeText(getApplicationContext(),"Could not connect, Please make sure Bluetooth is on and robot is running Main.nxj", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            dos = new DataOutputStream(nxtCommAndroid.getOutputStream());
                            sendThread = new SendThread(dos);
                            sendThread.run();
                        }

                    }
                    }
        });

    }
}
