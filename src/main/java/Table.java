import java.util.concurrent.ConcurrentHashMap;

public class Table
{
   public ConcurrentHashMap<String,String> lookUpTable;

   public Table()
   {
      lookUpTable = new ConcurrentHashMap<>();
      lookUpTable.put("Dummy", "");
      lookUpTable.put("Numpad0", "200");
      lookUpTable.put("Numpad1", "201");
      lookUpTable.put("Numpad2", "202");
      lookUpTable.put("Numpad3", "203");
      lookUpTable.put("Numpad4", "204");
      lookUpTable.put("Numpad5", "205");
      lookUpTable.put("Numpad6", "206");
      lookUpTable.put("Numpad7", "207");
      lookUpTable.put("Numpad8", "208");
      lookUpTable.put("Numpad9", "209");
   }

   public String lookUp(String input)
   {
      return lookUpTable.get(input); //output will be empty if no match
   }
}
