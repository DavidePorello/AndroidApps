package com.example.project4_davide_porello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //values to be used by handleMessage()
    public static final int REMOVE = 0; //only for UIThread
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int START = 3; //only for UIThread
    public static final int RESET = 4; //only for HandlerThread

    private boolean finish = true;
    private ImageView images[][] = new ImageView[3][3];
    public static int game[][] = new int[3][3];
    private MyThread player1, player2;
    private Handler handler1, handler2;
    private int n = 0;

    //define the handler running on the UI thread
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                //remove a piece from the board when a player has already placed 3 pieces and need to perform the next play
                case REMOVE:
                    if(!finish)
                        images[msg.arg1][msg.arg2].setImageDrawable(null);
                    break;

                //player1: place a circle on the table and check if player1 has won
                case PLAYER_1:
                    if(!finish) {
                        images[msg.arg1][msg.arg2].setImageResource(R.drawable.circle);
                        checkWinner();
                        //if player1 has won post runnables to quit threads
                        if(finish) {
                            Toast.makeText(MainActivity.this, "Player 1 win! ", Toast.LENGTH_SHORT).show();
                            handler1.post(player1.quitThread());
                            handler2.post(player2.quitThread());
                            handler.removeCallbacksAndMessages(null);
                        }
                        //if not send message to player2 to figure out the next move
                        else {
                            Message p2 = handler2.obtainMessage(MainActivity.PLAYER_2);
                            handler2.sendMessage(p2);
                        }
                    }
                    break;

                //player2: place a cross on the table and check if player2 has won
                case PLAYER_2:
                    if(!finish) {
                        images[msg.arg1][msg.arg2].setImageResource(R.drawable.cross);
                        checkWinner();
                        //if player2 has won post runnables to quit threads
                        if(finish) {
                            Toast.makeText(MainActivity.this, "Player 2 win! ", Toast.LENGTH_SHORT).show();
                            handler1.post(player1.quitThread());
                            handler2.post(player2.quitThread());
                            handler.removeCallbacksAndMessages(null);
                        }
                        //if not send message to player1 to figure out the next move
                        else {
                            Message p1 = handler1.obtainMessage(MainActivity.PLAYER_1);
                            handler1.sendMessage(p1);
                        }
                    }
                    break;

                //restart the game if the 2 threads has reset their states and are ready to play again
                case START:
                    n++;
                    if(n == 2) {

                        n = 0;

                        for(int i=0;i<3;i++)
                            for(int j=0;j<3;j++) {
                                game[i][j]=0;
                                images[i][j].setImageDrawable(null);
                            }

                        finish = false;

                        Toast.makeText(MainActivity.this, "Game restarted! ", Toast.LENGTH_SHORT).show();

                        Message p1 = handler1.obtainMessage(MainActivity.PLAYER_1);
                        handler1.sendMessage(p1);
                    }
                    break;
            }
        }
    };

    //check if someone has won after a play
    private void checkWinner() {
        boolean win = false;
        for(int i=0;i<3 && !win;i++) {
            if(game[i][0] != 0 && game[i][0] == game[i][1] && game[i][0] == game[i][2])
                win = true;
            if(game[0][i] != 0 && game[0][i] == game[1][i] && game[0][i] == game[2][i])
                win = true;
        }
        if(win)
            finish = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the ImageView of all the images
        images[0][0] = findViewById(R.id.image00);
        images[0][1] = findViewById(R.id.image01);
        images[0][2] = findViewById(R.id.image02);
        images[1][0] = findViewById(R.id.image10);
        images[1][1] = findViewById(R.id.image11);
        images[1][2] = findViewById(R.id.image12);
        images[2][0] = findViewById(R.id.image20);
        images[2][1] = findViewById(R.id.image21);
        images[2][2] = findViewById(R.id.image22);

        //start button listener
        Button start = findViewById(R.id.button1);
        start.setOnClickListener(v -> {

            //if the game is not finished yet send to the threads a reset message
            if(!finish) {
                finish = true;

                Message p1 = handler1.obtainMessage(MainActivity.RESET);
                handler1.sendMessage(p1);
                Message p2 = handler2.obtainMessage(MainActivity.RESET);
                handler2.sendMessage(p2);

            }
            //if the game is finished (or not started yet) create 2 new threads, clean the board and restart the game
            else {
                player1 = new MyThread("Player1", handler);
                player2 = new MyThread("Player2", handler);
                player1.start();
                player2.start();
                handler1 = player1.getHandler();
                handler2 = player2.getHandler();

                finish = false;
                for(int i=0;i<3;i++)
                    for(int j=0;j<3;j++) {
                        game[i][j]=0;
                        images[i][j].setImageDrawable(null);
                    }

                Toast.makeText(MainActivity.this, "Game started! ", Toast.LENGTH_SHORT).show();

                Message msg = handler1.obtainMessage(MainActivity.PLAYER_1);
                handler1.sendMessage(msg);
            }


        });
    }

}