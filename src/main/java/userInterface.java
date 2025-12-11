
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;
import java.awt.GraphicsEnvironment;

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

   private JList fileList;
   private JButton clear, convert, select;
   private JFileChooser fileChooser;
   public userInterface()
   {
      final int DEFAULT_FRAME_WIDTH = 800;
      final int DEFAULT_FRAME_HEIGHT = 600;
      setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);

      JPanel panel_1 = new JPanel();
      fileList = new JList(model);
      try
      {
         updateList(model);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      panel_1.add(fileList);
      add(panel_1, "North");

      JPanel panel_2 = new JPanel();
      select = new JButton("Select");
      panel_2.add(select);
      add(panel_2, "Center");

      JPanel panel_3 = new JPanel();
      convert = new JButton("Convert");
      clear = new JButton("Clear");
      panel_3.setLayout(new GridLayout(1, 2));
      panel_3.add(convert);
      panel_3.add(clear);
      add(panel_3, "South");

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
               System.out.println(file + " chosen.");
               Path outputDirectory = FileSystems.getDefault().getPath("output/").toAbsolutePath();
               String output = outputDirectory + "/" + file.getName();
               new File(outputDirectory.toUri()).mkdirs();
               try
               {
                  System.out.println("Copying from:\t" + file);
                  System.out.println("Copying to:\t" + Path.of(output));
                  java.nio.file.Files.copy(file.toPath(), Path.of(output));
                  updateList(model);
               }
               catch (IOException e)
               {
                  throw new RuntimeException(e);
               }
            }
         }
         if (source == clear)
         {
            try
            {
               Path outputDirectory = FileSystems.getDefault().getPath("output").toAbsolutePath();
               java.util.List<String> fileList = Files
                     .walk(outputDirectory)
                     .filter(Files::isRegularFile)
                     .map(Path::toString)
                     .filter(string -> string.endsWith(".ahk"))
                     .toList();
               for (String filename : fileList)
               {
                  String deleting = filename;
                  System.out.println("Deleteing:\t" + deleting);
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
                     .filter(Files::isRegularFile)
                     .map(Path::toString)
                     .filter(string -> string.endsWith(".ahk"))
                     .toList();
               for (String filename : fileList)
               {
                  String deleting = filename;
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
      }
   }
}

