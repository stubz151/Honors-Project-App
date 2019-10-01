package com.Robot.lad;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendThread extends Thread{
    private static DataOutputStream dos;

    public SendThread(DataOutputStream dos) {
        this.dos = dos;

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
