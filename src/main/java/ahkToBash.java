import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class ahkToBash
{
   public static void convert()
   {
      Function<String, keyStroke> mapper = line ->
      {
         try
         {
            String[] token = line.split("[\\s,{}\n]+");
            if (Objects.equals(token[0], "SendInput")) {
               if (token.length == 3)
                  return new keyStroke("separate", token[1], -1, token[2]);
               if (token.length == 2)
                  return new keyStroke("combined", token[1], -1, "");
            }
            if (Objects.equals(token[0], "Sleep") && token.length == 2)
               return new keyStroke("delay", "Dummy", Integer.parseInt(token[1]), "");
         }
         catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
         {
            return new keyStroke("invalid", "Dummy", -1, "");
         }
         return new keyStroke("invalid", "Dummy", -1, "");
      };

      Path outputDirectory =
            FileSystems
                  .getDefault()
                  .getPath("output")
                  .toAbsolutePath();
      new File(outputDirectory.toUri()).mkdirs();
      try
      {
         Files
               .walk(outputDirectory)
               .toList()
               .parallelStream()
               .filter(Files::isRegularFile)
               .map(Path::toString)
               .filter(string -> string.endsWith(".ahk"))
               .forEach(filename ->
                        {
                           try
                           {
                              File shellScript = new File(filename + ".sh"); // Create File
                              System.out.println("Converting:\t\t" + filename + ".sh");
                              OutputStream FileOut = new FileOutputStream(shellScript);
                              StringBuilder output = readFile(Path.of(filename), mapper)
                                    .stream()
                                    .map(keyStroke::toString)
                                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
                              String finalOutput = output.toString();
                              FileOut.write("sleep 5\n".getBytes());
                              FileOut.write(finalOutput.getBytes());
                              System.out.println("Done converting:\t" + filename);
                              Files.delete(Path.of(filename));
                           }
                           catch (IOException | NumberFormatException e) {
                              System.out.println("Error occured while processing:");
                              System.out.println("\t" + filename);
                              throw new RuntimeException(e);
                           }
                        }
                       );
         System.out.println("DONE CONVERTING!");
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   static <T> List<T> readFile(Path filename, Function<String, T> mapper)
   {
      List<T> list = null;
      try (Stream<String> lines = Files.lines(filename))
      {
         list = lines.map(mapper).toList();
      }
      catch (IOException e)
      {
         System.err.println("Error in accessing datafile " + filename);
         System.exit(1);
      }
      return list;
   }
}