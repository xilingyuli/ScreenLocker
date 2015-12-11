package com.xilingyuli.screenlocker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0X111;
    private SharedPreferences.Editor editor = null;
    private ArrayList<Map<String, String>> listData = null;
    private SimpleAdapter listItemAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //启动后台服务
        Intent intent = new Intent().setClass(SettingActivity.this, MyService.class);
        startService(intent);

        //获取以前的设置
        final SharedPreferences settingsPreferences = getSharedPreferences("WordSettings", Activity.MODE_PRIVATE);
        editor = settingsPreferences.edit();

        //新建更改单词重复次数的对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("单词重复次数");
        View v = getLayoutInflater().inflate(R.layout.dialog_number_picker,null);
        builder.setView(v);
        final NumberPicker np = (NumberPicker)v.findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(99);
        np.setValue(settingsPreferences.getInt("repeatTimes",5));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putInt("repeatTimes", np.getValue());
                editor.commit();
                changeData(1, np.getValue() + "");
            }
        });
        builder.setNegativeButton("取消",null);
        final Dialog numPicker = builder.create();

        //新建更改单词顺序的对话框
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingActivity.this);
        builder2.setTitle("单词顺序");
        builder2.setSingleChoiceItems(new String[]{"乱序", "顺序"}, settingsPreferences.getBoolean("isRandom",false) ? 0 : 1,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changeData(2, i==0?"乱序":"顺序");
            }
        });
        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putBoolean("isRandom", listData.get(2).get("SettingValue").equals("乱序"));
                editor.commit();
            }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changeData(2, settingsPreferences.getBoolean("isRandom",false)?"乱序":"顺序");
            }
        });
        final Dialog randomChooser = builder2.create();

        //新建更改新增单词的对话框
        AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingActivity.this);
        builder3.setTitle("增加新单词");
        View v2 = getLayoutInflater().inflate(R.layout.dialog_add_word,null);
        builder3.setView(v2);
        final EditText wordEditText = (EditText)v2.findViewById(R.id.editText2);
        final EditText meanEditText = (EditText)v2.findViewById(R.id.editText3);
        builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if((wordEditText.getText()+"").equals(""))
                    return;
                try {
                    FileWriter fw = new FileWriter(settingsPreferences.getString("path",null),true);
                    fw.write("\n"+(wordEditText.getText()+"").replace(" ","_")+" "+(meanEditText.getText()+"").replace("\n",";"));
                    fw.flush();
                    fw.close();
                    wordEditText.setText("");
                    meanEditText.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder3.setNegativeButton("取消", null);
        final Dialog wordAdder = builder3.create();

        //新建帮助详情对话框
        AlertDialog.Builder builder4 = new AlertDialog.Builder(SettingActivity.this);
        builder4.setTitle("帮助");
        builder4.setMessage(getText(R.string.app_help));
        builder4.setPositiveButton("确定", null);
        final Dialog helpDialog = builder4.create();

        //新建关于详情对话框
        AlertDialog.Builder builder5 = new AlertDialog.Builder(SettingActivity.this);
        builder5.setTitle("关于");
        builder5.setMessage(getText(R.string.app_about));
        builder5.setPositiveButton("确定",null);
        final Dialog aboutDialog = builder5.create();

        //向列表中填入数据
        ListView list = (ListView) findViewById(R.id.listView);
        listData = new ArrayList<Map<String, String>>();
        Map<String,String> data1 = new HashMap<String,String>();
        data1.put("SettingKey","单词文件");
        data1.put("SettingValue",settingsPreferences.getString("path","未设置"));
        listData.add(data1);
        Map<String,String> data2 = new HashMap<String,String>();
        data2.put("SettingKey","单词重复次数");
        data2.put("SettingValue",settingsPreferences.getInt("repeatTimes",5)+"");
        listData.add(data2);
        Map<String,String> data3 = new HashMap<String,String>();
        data3.put("SettingKey","单词顺序");
        data3.put("SettingValue",settingsPreferences.getBoolean("isRandom",false)?"乱序":"顺序");
        listData.add(data3);
        Map<String,String> data4 = new HashMap<String,String>();
        data4.put("SettingKey","增加新单词");
        data4.put("SettingValue","");
        listData.add(data4);
        Map<String,String> data5 = new HashMap<String,String>();
        data5.put("SettingKey","帮助");
        data5.put("SettingValue","");
        listData.add(data5);
        Map<String,String> data6 = new HashMap<String,String>();
        data6.put("SettingKey","关于");
        data6.put("SettingValue","");
        listData.add(data6);
        listItemAdapter = new SimpleAdapter(
                this,
                listData,
                R.layout.item_setting,
                new String[] {"SettingKey","SettingValue"},
                new int[] {R.id.textView4,R.id.textView5}
        );
        list.setAdapter(listItemAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                switch (arg2)
                {
                    case 0:
                        showFileChooser();
                        break;
                    case 1:
                        numPicker.show();
                        break;
                    case 2:
                        randomChooser.show();
                        break;
                    case 3:
                        wordAdder.show();
                        break;
                    case 4:
                        helpDialog.show();
                        break;
                    case 5:
                        aboutDialog.show();
                        break;
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个单词文件"),FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT) .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            editor.putString("path", uri.getPath());
            editor.commit();
            changeData(0,uri.getPath());
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    //动态更新设置项
    public void changeData(int pos, String data)
    {
        Map<String, String> map = listData.get(pos);
        map.put("SettingValue", data);
        listData.set(pos, map);
        listItemAdapter.notifyDataSetChanged();
    }

}
