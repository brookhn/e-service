package com.pp.server.thread;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class PipNotify {

    /**
     * 管道通信
     * @throws IOException
     */
    public static void PipNtfy() throws IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();

        writer.connect(reader);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        for (int i= 0 ; i < 10 ; i++) {
                            writer.write( i+"");
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int message;

                    try {
                        while (((message = reader.read()) != -1))
                        {
                            System.out.println("message "+ (char)message);
                        };
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

            }
        });
        t1.start();
        t2.start();
    }

    public static void main(String args[]) throws IOException {
        PipNotify.PipNtfy();
    }
}
