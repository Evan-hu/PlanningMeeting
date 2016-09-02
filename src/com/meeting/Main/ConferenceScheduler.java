package com.meeting.Main;

import com.meeting.entity.Talk;
import com.meeting.handle.Conference;
import com.meeting.util.Check;
import com.meeting.util.Format;
import com.sun.org.apache.xml.internal.utils.StringBufferPool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by Evan on 9/2/2016.
 */
public class ConferenceScheduler {
    private static JFrame frame;
    private static JTextField inputField;
    private static JButton chooseBtn;
    private static JLabel label;
    private static JTextArea textarea;
    private static JOptionPane jOptionPane1;
    private static JScrollPane scroller;
    private static JButton scheduleBtn;
    private static JButton exportBtn;

    static List<Talk> talks = new ArrayList<Talk>();
    static List<String> results = new ArrayList<String>();

    public static void main(final java.lang.String[] args) {
        java.awt.EventQueue.invokeLater(new java.lang.Runnable(){
           // List<Talk> talks = new ArrayList<Talk>();
            @Override public void run(){     // 初始化AWT UI
                frame = new JFrame("Schedule Meeting");
                inputField=new JTextField();
                chooseBtn = new JButton("Choice");
                label = new JLabel("Choice File: ");
                textarea = new JTextArea(25, 80);
                jOptionPane1 = new JOptionPane();
                scroller = new JScrollPane(textarea);

                textarea.setEditable(false);
                textarea.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent mouseEvent) {
                        textarea.setCursor(new Cursor(Cursor.TEXT_CURSOR)); //鼠标进入Text区后变为文本输入指针
                    }
                    public void mouseExited(MouseEvent mouseEvent) {
                        textarea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //鼠标离开Text区后恢复默认形态
                    }
                });

                textarea.setLineWrap(true);
                textarea.setWrapStyleWord(true);
                scheduleBtn = new JButton("Schedule Meeting");
                exportBtn=new JButton("Export As text File");
                // 选择文件并解析
                chooseFileAndParse();
                // 计划会议
                scheduleMeeting();
                // 导出计划会议内容
                exportResult();
                // AWT UI  设置
                final Box top = Box.createHorizontalBox();
                top.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                top.add(label);
                top.add(inputField);
                top.add(chooseBtn);
                top.add(scheduleBtn);
                top.add(exportBtn);
                top.add(Box.createHorizontalStrut(5));
                frame.add(top,BorderLayout.PAGE_START);
                frame.add(scroller);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }


    public static void scheduleMeeting() {
        scheduleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (talks.size() != 0) {
                    Conference conference = new Conference();
                    results = conference.scheduleTalks(talks);
                    if (0 != results.size()) {
                        textarea.setText(" ");
                        for (String str : results)
                            textarea.append(str + "\n");
                    }
                } else {
                    jOptionPane1.showMessageDialog(null, "Please Choice a meeting file","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void exportResult() {
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (results.size() != 0 && talks.size() ==0) {
                    JFileChooser jfc = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
                    jfc.setFileFilter(filter);
                    jfc.setMultiSelectionEnabled(false);
                    int result = jfc.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = jfc.getSelectedFile();
                        if (!file.getPath().endsWith(".txt")) {
                            file = new File(file.getPath() + ".txt");
                        }
                        FileOutputStream fos = null;
                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            fos = new FileOutputStream(file);
                            OutputStreamWriter opw = new OutputStreamWriter(fos, "UTF-8");
                            BufferedWriter bw = new BufferedWriter(opw);
                            for (String arr : results)
                                bw.write(arr + "\t\n");
                            bw.close();
                            opw.close();
                            fos.close();
                        } catch (Exception e2) {
                            jOptionPane1.showMessageDialog(null, "Create File Failed !", "Error", JOptionPane.ERROR_MESSAGE);
                            e2.printStackTrace();
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    jOptionPane1.showMessageDialog(null, "Please scheduler meeting !","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void chooseFileAndParse() {
        chooseBtn.addActionListener(new ActionListener(){
            private JFileChooser filechooser = null;
            @Override public void actionPerformed(ActionEvent e){
                if (filechooser == null) {
                    filechooser = new JFileChooser(System.getProperty("user.home"));
                }
                filechooser.setFileFilter(new FileNameExtensionFilter("Text Files","txt","text","java"));
                if (filechooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    inputField.setText(filechooser.getSelectedFile().getAbsolutePath());
                    FileReader reader = null;
                    BufferedReader br = null;
                    try {
                        reader = new FileReader(filechooser.getSelectedFile());
                        br = new BufferedReader(reader);
                        boolean contentTag = true;
                        String line;
                        int i = 0;
                        textarea.setText(" ");
                        textarea.append("Meeting Basic Information: " + "\n");
                        while ((line = br.readLine()) != null) {
                            i++;
                            if (line.length() != 0) {
                                String[] tokens = line.split(" ");
                                int duration = Check.getDuration(tokens[tokens.length - 1]);
                                String title = Format.stringArrayToString(tokens);
                                if (Check.hasNumberic(title)) {
                                    jOptionPane1.showMessageDialog(null, "Talk title has number in it. Please input again !", "Error", JOptionPane.ERROR_MESSAGE);
                                    contentTag = false;
                                    break;
                                } else if (duration == 0) {
                                    jOptionPane1.showMessageDialog(null, "Can't identify the meeting time. Please input again !", "Error", JOptionPane.ERROR_MESSAGE);
                                    contentTag = false;
                                    break;
                                }
                                talks.add(new Talk(Format.stringArrayToString(tokens), Check.getDuration(tokens[tokens.length - 1])));
                                textarea.append(line + "\n");
                            }
                            if (!contentTag)
                                break;
                        }
                        if (0 == i) {
                            jOptionPane1.showMessageDialog(null, "The file is empty","Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception xe) {
                        System.err.println(xe.getMessage());
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException ioe) {
                                System.err.println(ioe.getMessage());
                            }
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException ioe) {
                                System.err.println(ioe.getMessage());
                            }
                        }
                    }
                    textarea.setCaretPosition(0);
                }
            }
        });
    }




}
