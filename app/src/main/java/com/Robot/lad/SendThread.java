package com.Robot.lad;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendThread extends Thread{
    public static DataOutputStream dos;
    //class takes an output stream and starts a thread that can be used to send through information.
    public SendThread(DataOutputStream dos) {
        this.dos = dos;

    }
    public static void StopThread()
    {
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    Thread.currentThread().interrupt();
    }
    public static void sendInt(int i)
    {
        try {
            dos.writeInt(i);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        super.run();
    }
}
