
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GraphicsEnvironment;
import java.util.stream.Collectors;

public class userInterface extends JFrame
{
   DefaultListModel model = new DefaultListModel();

   public static void main(String[] args)
   {
      System.out.println("Headless Java: "+GraphicsEnvironment.isHeadless());
      userInterface frame = new userInterface();
      frame.setTitle("Autohotkey to ydotool bash script");
      frame.setVisible(true);
   }

   private JTextArea textArea;
   private PrintStream standardOut;
   private JPanel contentPane;
   private JScrollPane scrollPane, scrollPane2;
   private JList fileList;
   private JButton clear, convert, select, refresh;
   public userInterface()
   {
      final int DEFAULT_FRAME_WIDTH = 800;
      final int DEFAULT_FRAME_HEIGHT = 600;
      setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      JPanel panel_1 = new JPanel();
      fileList = new JList(model);
      scrollPane = new JScrollPane();
      scrollPane.setViewportView(fileList);
      try
      {
         updateList(model);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      panel_1.add(scrollPane);
      contentPane.add(panel_1, "West");

      JPanel panel_2 = new JPanel();
      select = new JButton("Select");
      panel_2.add(select);
      contentPane.add(panel_2, "Center");

      JPanel panel_3 = new JPanel();
      convert = new JButton("Convert");
      refresh = new JButton("Refresh");
      clear = new JButton("Clear");
      panel_3.setLayout(new GridLayout(1, 3));
      panel_3.add(convert);
      panel_3.add(refresh);
      panel_3.add(clear);
      contentPane.add(panel_3, "East");

      JPanel panel_4 = new JPanel();
      textArea = new JTextArea(40, 100);
      textArea.setEditable(false);
      PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
         //@author www.codejava.net
         // keeps reference of standard output stream
         standardOut = System.out;
         // re-assigns standard output stream and error output stream
         System.setOut(printStream);
         System.setErr(printStream);
         //@author www.codejava.net
      scrollPane2 = new JScrollPane();
      scrollPane2.setViewportView(textArea);
      panel_4.add(scrollPane2);
      contentPane.add(panel_4,"South");

      add(contentPane);
      ActionListener listener = new MyListener();
      select.addActionListener(listener);
      clear.addActionListener(listener);
      convert.addActionListener(listener);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }


   public static void updateList(DefaultListModel model) throws IOException
   {
       Path outputDirectory = FileSystems.getDefault().getPath("output").toAbsolutePath();
       new File(outputDirectory.toUri()).mkdirs();
       java.util.List<String> fileList = Files
              .walk(outputDirectory)
              .toList()
              .parallelStream()
              .filter(Files::isRegularFile)
              .map(Path::toString)
              .filter(string -> string.endsWith(".ahk"))
              .toList();
        model.removeAllElements();
        int i = 0;
        for (String filename : fileList)
        {
           model.add(i,filename);
           i++;
        }
   }

   private class MyListener implements ActionListener
   {
      @Override
      public void actionPerformed (ActionEvent event)
      {
         Object source = event.getSource();
         if (source == select)
         {
            FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
            dialog.setFile("*.ahk");
            dialog.setMultipleMode(true);
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);
            File[] fileList =  dialog.getFiles();
            dialog.dispose();
            for (File file : fileList)
            {
               Path outputDirectory = FileSystems.getDefault().getPath("output/").toAbsolutePath();
               String output = outputDirectory + "/" + file.getName();
               new File(outputDirectory.toUri()).mkdirs();
               try
               {
                  System.out.println("Copying from:\t" + file);
                  System.out.println("Copying to:\t" + Path.of(output));
                  java.nio.file.Files.copy(file.toPath(), Path.of(output));
               }
               catch (IOException e)
               {
                  System.out.println("Error occurred while copying:\t" + file);
                  throw new RuntimeException(e);
               }
            }
            try
            {
                updateList(model);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            System.out.println("DONE COPYING!");
         }
         if (source == clear)
         {
            try
            {
               Path outputDirectory = FileSystems.getDefault().getPath("output").toAbsolutePath();
               java.util.List<String> fileList = Files
                     .walk(outputDirectory)
                     .toList()
                     .parallelStream()
                     .filter(Files::isRegularFile)
                     .map(Path::toString)
                     .filter(string -> string.endsWith(".ahk"))
                     .toList();
               for (String deleting : fileList)
               {
                  System.out.println("Deleting:\t" + deleting);
                  Files.delete(Path.of(deleting));
               }
               updateList(model);
            }
            catch (IOException e)
            {
               throw new RuntimeException(e);
            }
         }
         if (source == convert)
         {
            ahkToBash.convert();
            try
            {
               Path outputDirectory = FileSystems.getDefault().getPath("output").toAbsolutePath();
               java.util.List<String> fileList = Files
                     .walk(outputDirectory)
                     .toList()
                     .parallelStream()
                     .filter(Files::isRegularFile)
                     .map(Path::toString)
                     .filter(string -> string.endsWith(".ahk"))
                     .toList();
               for (String deleting : fileList)
               {
                  System.out.println("Deleting:\t" + deleting);
                  Files.delete(Path.of(deleting));
               }
               updateList(model);
            }
            catch (IOException e)
            {
               throw new RuntimeException(e);
            }
         }
         if (source == refresh)
         {
             try
             {
                updateList(model);
             }
             catch (IOException e)
             {
                throw new RuntimeException(e);
             }
         }
      }
   }
}

