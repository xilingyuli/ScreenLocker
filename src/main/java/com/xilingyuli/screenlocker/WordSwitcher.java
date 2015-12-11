package com.xilingyuli.screenlocker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

/**
 * Created by xilingyuli on 2015/11/7.
 */
public class WordSwitcher {
    private int leftTimes = 0;
    private String thisWord = "";
    private int thisIndex = -1;
    private SharedPreferences preferences = null;
    private Context context = null;

    WordSwitcher(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences("WordHistory", Activity.MODE_PRIVATE);
        leftTimes = preferences.getInt("leftTimes", 0);
        thisWord = preferences.getString("thisWord","");
        thisIndex = preferences.getInt("thisIndex",-1);
    }
    public String getWord()
    {
        while (leftTimes==0)
            nextWord();
        return thisWord;
    }
    public void nextWord()
    {
        SharedPreferences settingsPreferences = context.getSharedPreferences("WordSettings",Activity.MODE_PRIVATE);
        FileReader f = new FileReader(settingsPreferences.getString("path",null));
        String[] l = f.read();
        f.closeFile();
        if(l==null||l.length==0)
        {
            leftTimes = 1;
            thisWord = "";
            return;
        }
        leftTimes = settingsPreferences.getInt("repeatTimes",5);
        if(settingsPreferences.getBoolean("isRandom",false))
        {
            Random r = new Random();
            thisIndex = r.nextInt(l.length);
            thisWord = l[thisIndex];
        }
        else
            thisWord = l[(++thisIndex)%l.length];
    }
    public void save()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("leftTimes",--leftTimes);
        editor.putString("thisWord", thisWord);
        editor.putInt("thisIndex", thisIndex);
        editor.commit();
    }
}
