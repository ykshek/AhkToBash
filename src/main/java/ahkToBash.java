import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ahkToBash
{
   public static void convert()
   {
      Function<String, keyStroke> mapper = line ->
      {
         String[] token = line.split("[\\s,{}\n]+");
         if (Objects.equals(token[0], "SendInput"))
         {
            if (token.length == 3)
               return new keyStroke("separate", 200 + token[1].charAt(6) - 48, -1, token[2] );
            else
               return new keyStroke("combined", 200 + token[1].charAt(6) - 48, -1, "");
         }
         if (Objects.equals(token[0], "Sleep"))
            return new keyStroke("delay", -1, Integer.parseInt(token[1]), "");
         return new keyStroke("invalid",-1, -1, "");
      };

      Path outputDirectory =
         FileSystems
            .getDefault()
            .getPath("output")
            .toAbsolutePath();
      new File(outputDirectory.toUri()).mkdirs();
      System.out.println(outputDirectory);
      try
      {
         Files
            .walk(outputDirectory)
            .collect(Collectors.toList())
            .parallelStream()
            .filter(Files::isRegularFile)
            .map(Path::toString)
            .filter(string -> string.endsWith(".ahk"))
            .forEach(filename ->
               {
                  try
                  {
                     File shellScript = new File(filename + ".sh"); // Create File
                     System.out.println("Converting:\t" + filename + ".sh");
                     OutputStream FileOut = new FileOutputStream(shellScript);
                     String output = readFile(Path.of(filename), mapper)
                        .stream()
                        .map(keyStroke::toString)
                        .reduce("", String::concat);
                     FileOut.write("sleep 5\n".getBytes());
                     FileOut.write(output.getBytes());
                  }
                  catch (IOException e)
                  {
                     throw new RuntimeException(e);
                  }
               }
            );
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   static <T> List<T> readFile (Path filename, Function<String, T> mapper)
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