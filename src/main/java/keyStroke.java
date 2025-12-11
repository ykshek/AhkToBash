import java.util.Objects;

public class keyStroke
{
   private final String type;
   private final int keyCode;
   private final int delay;
   private String upDown;

   public keyStroke(String t, int k, int d, String u)
   {
      type = t;
      keyCode = k;
      delay = d;
      if (u.contains("up")) upDown = "0";
      else if (u.contains("down")) upDown = "1";
      else upDown = u;
   }

   @Override
   public String toString()
   {
      switch (type)
      {
         case "combined" -> {return "ydotool key "+keyCode+":1\n"+"ydotool key "+keyCode+":0\n";}
         case "separate" -> {return "ydotool key "+keyCode+":"+upDown+"\n";}
         case "delay" -> {return "sleep ."+Integer.toString(delay)+"\n";}
         case "invalid" -> {return "";} //return an empty string(i.e. skip it) if it is invalid
         default -> {throw new RuntimeException();}
      }
   }
}
