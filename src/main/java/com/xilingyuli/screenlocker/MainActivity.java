package com.xilingyuli.screenlocker;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView wordOutput;
    private TextView wordInf;
    private TextView tipView;
    private EditText wordInput;
    private WordSwitcher wordSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordOutput = (TextView)findViewById(R.id.textView);
        wordInf = (TextView)findViewById(R.id.textView3);
        tipView = (TextView)findViewById(R.id.textView2);
        wordInput = (EditText)findViewById(R.id.editText);
        Button changeButton = (Button)findViewById(R.id.button);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordSwitcher.nextWord();
                setText();
            }
        });
        Button sureButton = (Button)findViewById(R.id.button2);
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(compareWord())
                    finish();
                else{
                    tipView.setText("输入错误");
                    tipView.setTextSize(20f);
                }
            }
        });

        wordSwitcher = new WordSwitcher(this);
        setText();
    }

    @Override
    public void finish()
    {
        wordSwitcher.save();
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean compareWord()
    {
        return (wordOutput.getText()+"").equals(wordInput.getText()+"");
    }

    public void setText()
    {
        tipView.setText("");
        tipView.setTextSize(0f);
        String s = wordSwitcher.getWord();
        if(s.equals(""))
        {
            tipView.setText("无法打开单词文件\n或单词文件含空行");
            tipView.setTextSize(20f);
            return;
        }
        if(s.indexOf(" ")!=-1) {
            wordOutput.setText(s.substring(0, s.indexOf(" ")).replace("_"," "));
            s = s.substring(s.indexOf(" ") + 1).replace(";", ";\n");
            wordInf.setText(s);
        }
        else{
            wordOutput.setText(s.replace("_"," "));
            wordInf.setText("");
        }
    }

}
