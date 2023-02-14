package com.example.project4_davide_porello;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.Random;


public class MyThread extends HandlerThread {

    private Handler handler, UIHandler;
    public MyThread(String name, Handler UIHandler) {
        super(name);
        this.UIHandler = UIHandler;
    }

    protected void onLooperPrepared() {
        //define the handler running on the worker thread
        handler = new Handler(getLooper()) {

            int count1 = 0, count2 = 0;
            int rand_int1, rand_int2, prev_int1 = -1, prev_int2 = -1;
            Random rand = new Random();
            Message ui;

            public void handleMessage(Message msg) {

                switch(msg.what) {
                    //player1 plays randomly
                    case MainActivity.PLAYER_1:

                        //wait 1 second
                        try { Thread.sleep(1000); }
                        catch (InterruptedException e) { System.out.println("Thread interrupted!"); }

                        //if it has already places 3 pieces on the board decide which one remove
                        if(count1 >= 3) {
                            do {
                                rand_int1 = rand.nextInt(3);
                                rand_int2 = rand.nextInt(3);
                            } while(MainActivity.game[rand_int1][rand_int2] != 1);

                            count1--;
                            MainActivity.game[rand_int1][rand_int2] = 0;

                            //get a message instance with target set to UI thread's message queue
                            //arg1 and arg2 are the indices of the piece to remove
                            ui = UIHandler.obtainMessage(MainActivity.REMOVE);
                            ui.arg1 = rand_int1;
                            ui.arg2 = rand_int2;
                            UIHandler.sendMessage(ui);

                            prev_int1 = rand_int1;
                            prev_int2 = rand_int2;
                        }

                        //figure out the next move
                        do {
                            rand_int1 = rand.nextInt(3);
                            rand_int2 = rand.nextInt(3);
                        } while(MainActivity.game[rand_int1][rand_int2] != 0 || (rand_int1 == prev_int1 && rand_int2 == prev_int2));

                        count1++;
                        MainActivity.game[rand_int1][rand_int2] = 1;

                        //get a message instance with target set to UI thread's message queue
                        //arg1 and arg2 are the indices of the piece to add
                        ui = UIHandler.obtainMessage(MainActivity.PLAYER_1);
                        ui.arg1 = rand_int1;
                        ui.arg2 = rand_int2;
                        UIHandler.sendMessage(ui);

                        break;

                    //player2 scans the board and place a piece in the first free spot
                    case MainActivity.PLAYER_2:

                        //wait 1 second
                        try { Thread.sleep(1000); }
                        catch (InterruptedException e) { System.out.println("Thread interrupted!"); }

                        //if it has already places 3 pieces on the board decide which one remove
                        boolean found = false;
                        if(count2 >= 3) {
                            for(int i=0;i<3 && !found;i++)
                                for(int j=0;j<3 && !found;j++)
                                    if(MainActivity.game[i][j] == 2) {
                                        count2--;
                                        MainActivity.game[i][j] = 0;

                                        //get a message instance with target set to UI thread's message queue
                                        //arg1 and arg2 are the indices of the piece to remove
                                        ui = UIHandler.obtainMessage(MainActivity.REMOVE);
                                        ui.arg1 = i;
                                        ui.arg2 = j;
                                        UIHandler.sendMessage(ui);

                                        prev_int1 = i;
                                        prev_int2 = j;
                                        found = true;
                                    }
                        }

                        //figure out the next move
                        found = false;
                        for(int i=0;i<3 && !found;i++)
                            for(int j=0;j<3 && !found;j++)
                                if(MainActivity.game[i][j] == 0 && (i != prev_int1 || j != prev_int2)) {
                                    count2++;
                                    MainActivity.game[i][j] = 2;

                                    //get a message instance with target set to UI thread's message queue
                                    //arg1 and arg2 are the indices of the piece to add
                                    ui = UIHandler.obtainMessage(MainActivity.PLAYER_2);
                                    ui.arg1 = i;
                                    ui.arg2 = j;
                                    UIHandler.sendMessage(ui);

                                    found = true;
                                }
                        break;

                    //reset the thread when the previous game is not finished yet
                    case MainActivity.RESET:
                        count1 = 0;
                        count2 = 0;
                        prev_int1 = -1;
                        prev_int2 = -1;

                        //get a message instance with target set to UI thread's message queue
                        //communicate to the UI thread that this thread is ready to play again
                        ui = UIHandler.obtainMessage(MainActivity.START);
                        UIHandler.sendMessage(ui);
                        break;
                }
            }
        };
    }

    //getter for the handler of the thread
    public Handler getHandler() {
        while(handler == null)
            Log.i("Player", "no handler");
        return handler;
    }

    //return a runnable used to quit the thread
    public Runnable quitThread() {
        return this::quit;
    }

}