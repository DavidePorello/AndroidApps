// ClipPlayer.aidl
package com.example.project5_davide_porello;

// Declare any non-default types here with import statements

interface ClipPlayer {
    void play(int n);
    void pause();
    void resume();
    void stop();
    boolean checkStarted();
}