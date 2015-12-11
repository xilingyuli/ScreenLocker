package com.xilingyuli.screenlocker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by xilingyuli on 2015/11/7.
 */
public class FileReader {

    private InputStream inStream = null;
    private InputStreamReader inputReader = null;
    private BufferedReader bufferReader = null;

    FileReader(String path)
    {
        try {
            File file = new File(path);
            if (file.isDirectory())
                return;
            inStream = new FileInputStream(file);
            inputReader = new InputStreamReader(inStream);
            bufferReader = new BufferedReader(inputReader);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public String[] read()
    {
        if(bufferReader==null)
            return null;
        ArrayList<String> list = new ArrayList<>();
        String line;
        try {
            while (( line = bufferReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return list.toArray(new String[]{""});
    }

    public void closeFile()
    {
        try {
            if(bufferReader!=null)
                bufferReader.close();
            if(inputReader!=null)
                inputReader.close();
            if(inStream!=null)
                inStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
