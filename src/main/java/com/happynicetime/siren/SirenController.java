/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.happynicetime.siren;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javax.sound.sampled.*;
/**
 *
 * @author incur
 */
public class SirenController {
    protected Clip clip1 = null;
    @FXML private CheckBox check1;
    @FXML private TextField text1;
    @FXML protected void toggleSiren1(ActionEvent event){
        if(check1.selectedProperty().get()){//checked
            if(text1.getText().equals("")){
                System.out.println("missing siren pattern");
                check1.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text1.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check1.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check1.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip1 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
                int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip1.open(audioFormat, soundData, 0,endOfWaves);
                clip1.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip1.start();
                //test sound
                /*
                int frequencyToPlay = sirenPattern[0];
                double oneWaveLength = 2 * Math.PI;
                double samplesPerWave = sampleRate / frequencyToPlay;
                int numberOfSamples = Float.valueOf(sirenPattern[1] * sampleRate).intValue();
                byte[] soundData = new byte[numberOfSamples];

                for(int i = 0;i < numberOfSamples;i++){
                    double wavePosition = i / samplesPerWave;
                    wavePosition = wavePosition % 1;
                    double sinPosition = wavePosition * oneWaveLength;
                    soundData[i] = Double.valueOf(Math.sin(sinPosition) * 127).byteValue();
                    //if(i < 200)System.out.println(soundData[i]+" ");
                }
                
                clip1.open(audioFormat, soundData, 0,numberOfSamples);
                clip1.start();
                */
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip1 != null){
                clip1.stop();
                clip1.drain();
                clip1.close();
            }
        }
    }
    protected Clip clip2 = null;
    @FXML private CheckBox check2;
    @FXML private TextField text2;
    @FXML protected void toggleSiren2(ActionEvent event){
        if(check2.selectedProperty().get()){//checked
            if(text2.getText().equals("")){
                System.out.println("missing siren pattern");
                check2.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text2.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check2.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check2.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip2 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
                int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip2.open(audioFormat, soundData, 0,endOfWaves);
                clip2.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip2.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip2 != null){
                clip2.stop();
                clip2.drain();
                clip2.close();
            }
        }
    }
    protected Clip clip3 = null;
    @FXML private CheckBox check3;
    @FXML private TextField text3;
    @FXML protected void toggleSiren3(ActionEvent event){
        if(check3.selectedProperty().get()){//checked
            if(text3.getText().equals("")){
                System.out.println("missing siren pattern");
                check3.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text3.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check3.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check3.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip3 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
                int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip3.open(audioFormat, soundData, 0,endOfWaves);
                clip3.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip3.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip3 != null){
                clip3.stop();
                clip3.drain();
                clip3.close();
            }
        }
    }
    protected Clip clip4 = null;
    @FXML private CheckBox check4;
    @FXML private TextField text4;
    @FXML protected void toggleSiren4(ActionEvent event){
        if(check4.selectedProperty().get()){//checked
            if(text4.getText().equals("")){
                System.out.println("missing siren pattern");
                check4.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text4.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check4.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check4.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip4 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip4.open(audioFormat, soundData, 0,endOfWaves);
                clip4.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip4.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip4 != null){
                clip4.stop();
                clip4.drain();
                clip4.close();
            }
        }
    }
    protected Clip clip5 = null;
    @FXML private CheckBox check5;
    @FXML private TextField text5;
    @FXML protected void toggleSiren5(ActionEvent event){
        if(check5.selectedProperty().get()){//checked
            if(text5.getText().equals("")){
                System.out.println("missing siren pattern");
                check5.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text5.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check5.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check5.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip5 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip5.open(audioFormat, soundData, 0,endOfWaves);
                clip5.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip5.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip5 != null){
                clip5.stop();
                clip5.drain();
                clip5.close();
            }
        }
    }
    protected Clip clip6 = null;
    @FXML private CheckBox check6;
    @FXML private TextField text6;
    @FXML protected void toggleSiren6(ActionEvent event){
        if(check6.selectedProperty().get()){//checked
            if(text6.getText().equals("")){
                System.out.println("missing siren pattern");
                check6.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text6.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check6.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check6.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip6 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip6.open(audioFormat, soundData, 0,endOfWaves);
                clip6.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip6.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip6 != null){
                clip6.stop();
                clip6.drain();
                clip6.close();
            }
        }
    }
    protected Clip clip7 = null;
    @FXML private CheckBox check7;
    @FXML private TextField text7;
    @FXML protected void toggleSiren7(ActionEvent event){
        if(check7.selectedProperty().get()){//checked
            if(text7.getText().equals("")){
                System.out.println("missing siren pattern");
                check7.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text7.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check7.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check7.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip7 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip7.open(audioFormat, soundData, 0,endOfWaves);
                clip7.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip7.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip7 != null){
                clip7.stop();
                clip7.drain();
                clip7.close();
            }
        }
    }
    protected Clip clip8 = null;
    @FXML private CheckBox check8;
    @FXML private TextField text8;
    @FXML protected void toggleSiren8(ActionEvent event){
        if(check8.selectedProperty().get()){//checked
            if(text8.getText().equals("")){
                System.out.println("missing siren pattern");
                check8.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text8.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check8.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check8.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip8 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip8.open(audioFormat, soundData, 0,endOfWaves);
                clip8.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip8.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip8 != null){
                clip8.stop();
                clip8.drain();
                clip8.close();
            }
        }
    }
    protected Clip clip9 = null;
    @FXML private CheckBox check9;
    @FXML private TextField text9;
    @FXML protected void toggleSiren9(ActionEvent event){
        if(check9.selectedProperty().get()){//checked
            if(text9.getText().equals("")){
                System.out.println("missing siren pattern");
                check9.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text9.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check9.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check9.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip9 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip9.open(audioFormat, soundData, 0,endOfWaves);
                clip9.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip9.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip9 != null){
                clip9.stop();
                clip9.drain();
                clip9.close();
            }
        }
    }
    protected Clip clip10 = null;
    @FXML private CheckBox check10;
    @FXML private TextField text10;
    @FXML protected void toggleSiren10(ActionEvent event){
        if(check10.selectedProperty().get()){//checked
            if(text10.getText().equals("")){
                System.out.println("missing siren pattern");
                check10.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text10.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check10.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check10.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip10 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip10.open(audioFormat, soundData, 0,endOfWaves);
                clip10.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip10.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip10 != null){
                clip10.stop();
                clip10.drain();
                clip10.close();
            }
        }
    }
    protected Clip clip11 = null;
    @FXML private CheckBox check11;
    @FXML private TextField text11;
    @FXML protected void toggleSiren11(ActionEvent event){
        if(check11.selectedProperty().get()){//checked
            if(text11.getText().equals("")){
                System.out.println("missing siren pattern");
                check11.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text11.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check11.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check11.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip11 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip11.open(audioFormat, soundData, 0,endOfWaves);
                clip11.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip11.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip11 != null){
                clip11.stop();
                clip11.drain();
                clip11.close();
            }
        }
    }
    protected Clip clip12 = null;
    @FXML private CheckBox check12;
    @FXML private TextField text12;
    @FXML protected void toggleSiren12(ActionEvent event){
        if(check12.selectedProperty().get()){//checked
            if(text12.getText().equals("")){
                System.out.println("missing siren pattern");
                check12.setSelected(false);
                return;
            }
            String[] sirenPatternStrings = text12.getText().split(" ");
            if(sirenPatternStrings.length % 2 != 0){
                System.out.println("siren pattern format error");
                check12.setSelected(false);
                return;
            }
            double[] sirenPattern = new double[sirenPatternStrings.length];
            try{
                for(int i = 0;i < sirenPattern.length;i++){
                    sirenPattern[i] = Double.parseDouble(sirenPatternStrings[i]);
                }
            }catch(NumberFormatException e){
                System.out.println("siren pattern format error");
                check12.setSelected(false);
                return;
            }
            //play the sound
            try{
                float sampleRate = 44100;
                int sampleSizeInBits = 8;//size = 1 byte//The sample size indicates how many bits are used to store each snapshot; 8 and 16 are typical values.
                int channels = 1;//mono
                boolean signed = true;
                boolean bigEndian = true;//Big Endian: places the most significant byte first
                AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
                DataLine.Info dataLineInfo = new DataLine.Info(Clip.class,audioFormat);
                Line line = AudioSystem.getLine(dataLineInfo);
                clip12 = (Clip) line;//turns out it's a clip
                
                //make sound
                //figure out the total number of samples
                int totalSamples = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    totalSamples = (int) (totalSamples + (sirenPattern[whichTonePlayingIndex + 1] * sampleRate));
                }
                //System.out.println(totalSamples+" totalSamples");
                //make the array for sound data
                byte[] soundData = new byte[totalSamples];
              int soundDataIndex = 0;
                double wavePosition = 0;
                int endOfWaves = 0;
                for(int whichTonePlayingIndex = 0;whichTonePlayingIndex < sirenPattern.length - 1/*second to last number*/;whichTonePlayingIndex += 2){
                    //figure out the frequency from and frequency to
                    double frequencyFrom = sirenPattern[whichTonePlayingIndex];
                    double frequencyTo;
                    if(whichTonePlayingIndex == sirenPattern.length - 2)
                        frequencyTo = sirenPattern[0];
                    else
                        frequencyTo = sirenPattern[whichTonePlayingIndex + 2];
                    //System.out.println("frequencyFrom "+frequencyFrom);
                    //System.out.println("frequencyTo "+frequencyTo);
                    //put in the soundData
                    //find out number of samples
                    int numberOfSamples = (int) (sirenPattern[whichTonePlayingIndex + 1] * (double)sampleRate);
                    //numberOfSamples works
                    //for() loop between frequencyFrom and frequencyTo
                    for(int sampleNumber = 0;sampleNumber < numberOfSamples;sampleNumber++){
                        double percentToNextTone = (double)(sampleNumber) / (double)(numberOfSamples - 1);
                        //percentToNextTone works
                        int frequencyToPlay = (int) ((((double)1 - percentToNextTone) * (double)frequencyFrom) +
                                ((double)percentToNextTone * (double)frequencyTo));
                        //frequencyToPlay works
                        //set wave position
                        wavePosition += (double)frequencyToPlay / (double)sampleRate;
                        if(wavePosition >= 1)
                            endOfWaves = soundDataIndex;
                        wavePosition = wavePosition % 1f;
                        double sinPosition = wavePosition * 2f * Math.PI;
                        soundData[soundDataIndex++] = Double.valueOf(Math.sin(sinPosition) * 127f).byteValue();
                        //if(soundDataIndex - 1 < 421)
                         //System.out.println(soundData[soundDataIndex - 1]);
                    }
                }
                //System.out.println(soundDataIndex+" now should be same as "+soundData.length); // and same as totalSamples
                clip12.open(audioFormat, soundData, 0,endOfWaves);
                clip12.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
                clip12.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{//unchecked
            //System.out.println("unchecked");
            if(clip12 != null){
                clip12.stop();
                clip12.drain();
                clip12.close();
            }
        }
    }
    //TextField text1
    //TextField text2
    @FXML protected void save(ActionEvent event){
        //System.out.println("save");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(text1.getText());stringBuilder.append("\n");
        stringBuilder.append(text2.getText());stringBuilder.append("\n");
        stringBuilder.append(text3.getText());stringBuilder.append("\n");
        stringBuilder.append(text4.getText());stringBuilder.append("\n");
        stringBuilder.append(text5.getText());stringBuilder.append("\n");
        stringBuilder.append(text6.getText());stringBuilder.append("\n");
        stringBuilder.append(text7.getText());stringBuilder.append("\n");
        stringBuilder.append(text8.getText());stringBuilder.append("\n");
        stringBuilder.append(text9.getText());stringBuilder.append("\n");
        stringBuilder.append(text10.getText());stringBuilder.append("\n");
        stringBuilder.append(text11.getText());stringBuilder.append("\n");
        stringBuilder.append(text12.getText());
        //System.out.println(stringBuilder);
        FileWriter outputStream = null;
        try{
            outputStream = new FileWriter("sirens.txt");
            outputStream.write(stringBuilder.toString());
        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    @FXML protected void load(ActionEvent event){
        //System.out.println("load");
        FileReader inputStream = null;
        try{
            inputStream = new FileReader("sirens.txt");
            StringBuilder stringBuilder = new StringBuilder();
            int readByte;
            while((readByte = inputStream.read()) != -1){
                //System.out.print((char)readByte);
                stringBuilder.append((char)readByte);
            }
            String[] stringsForBoxes = stringBuilder.toString().split("\n");
            text1.setText(stringsForBoxes[0]);
            text2.setText(stringsForBoxes[1]);
            text3.setText(stringsForBoxes[2]);
            text4.setText(stringsForBoxes[3]);
            text5.setText(stringsForBoxes[4]);
            text6.setText(stringsForBoxes[5]);
            text7.setText(stringsForBoxes[6]);
            text8.setText(stringsForBoxes[7]);
            text9.setText(stringsForBoxes[8]);
            text10.setText(stringsForBoxes[9]);
            text11.setText(stringsForBoxes[10]);
            text12.setText(stringsForBoxes[11]);
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
