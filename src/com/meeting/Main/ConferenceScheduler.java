package com.meeting.Main;

import com.meeting.entity.Talk;
import com.meeting.handle.Conference;

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
 * Created by Evan on 4/14/2016.
 */
public class MainUI {
    public static void main(final java.lang.String[] args) {
        List<Talk> talks = new ArrayList<Talk>();
        java.awt.EventQueue.invokeLater(new java.lang.Runnable(){
            List<Talk> talks = new ArrayList<Talk>();
            List<String> results = new ArrayList<String>();
            @Override public void run(){
                final JFrame frame = new JFrame("Schedule Meeting");
                final JTextField text1=new JTextField();
                final JButton load = new JButton("Choice");
                final JLabel label = new JLabel("Choice File: ");
                final JTextArea textarea = new JTextArea(25, 80);

                textarea.setEditable(false);
                textarea.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent mouseEvent) {
                        textarea.setCursor(new Cursor(Cursor.TEXT_CURSOR)); //鼠标进入Text区后变为文本输入指针
                    }
                    public void mouseExited(MouseEvent mouseEvent) {
                        textarea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //鼠标离开Text区后恢复默认形态
                    }
                });

                final JOptionPane jOptionPane1 = new JOptionPane();
                textarea.setLineWrap(true);
                textarea.setWrapStyleWord(true);
                JButton button1 = new JButton("Schedule Meeting");
                JButton button2=new JButton("Export As text File");

                final JScrollPane scroller = new JScrollPane(textarea);
                load.addActionListener(new ActionListener(){
                    private JFileChooser filechooser = null;
                    @Override public void actionPerformed(ActionEvent e){
                        if (filechooser == null) {
                            filechooser = new JFileChooser(System.getProperty("user.home"));
                        }
                        filechooser.setFileFilter(new FileNameExtensionFilter("Text Files","txt","text","java"));
                        if (filechooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            text1.setText(filechooser.getSelectedFile().getAbsolutePath());
                            FileReader reader = null;
                            BufferedReader br = null;
                            try {
                                reader = new FileReader(filechooser.getSelectedFile());
                                br = new BufferedReader(reader);
                                boolean contentTag = true;
                                String line = "";
                                int i = 0;
                                    textarea.setText(" ");
                                    textarea.append("Meeting Basic Information: " + "\n");
                                    while ((line = br.readLine()) != null) {
                                        i++;
                                        if (line.length() != 0) {
                                            Talk meeting = new Talk();
                                            String[] tokens = line.split(" ");
                                            int duration = GetDuration(tokens[tokens.length - 1]);
                                            String title = StringArrayToString(tokens);
                                            if (hasNumberic(title)) {
                                                jOptionPane1.showMessageDialog(null, "Talk title has number in it. Please input again !", "Error", JOptionPane.ERROR_MESSAGE);
                                                contentTag = false;
                                                break;
                                            } else if (duration == 0) {
                                                jOptionPane1.showMessageDialog(null, "Can't identify the meeting time. Please input again !", "Error", JOptionPane.ERROR_MESSAGE);
                                                contentTag = false;
                                                break;
                                            }
                                            talks.add(new Talk(StringArrayToString(tokens), GetDuration(tokens[tokens.length - 1])));
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
                        return;
                    }
                });
                button1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (talks.size() != 0) {
                            Conference conference = new Conference();
                            results = conference.ScheduleTalks(talks);
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

                button2.addActionListener(new ActionListener() {
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

             final Box top = Box.createHorizontalBox();
                top.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                top.add(label);
                top.add(text1);
                top.add(load);
                top.add(button1);
                top.add(button2);
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



    public static String StringArrayToString(String [] str) {
        String result = "";
        for (int i = 0; i < str.length - 1; i++)
            result += str[i] + " ";
        return result;
    }

    public static int GetDuration(String time) {
        if ("lightning".equals(time))
            return 5;
        else if (TimeType(time))
            return Integer.parseInt(time.substring(0,2));
        else
            return 0;
    }

    public static boolean hasNumberic(String str) {
        Pattern pattern = Pattern.compile(".*\\d+.*");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches())
            return false;
        return true;
    }

    public static boolean TimeType(String str) {
        Pattern pattern = Pattern.compile("\\d+min");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.matches())
            return false;
        return true;
    }



}
