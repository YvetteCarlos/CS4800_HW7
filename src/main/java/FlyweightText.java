
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

interface TextTypes{ //interface
    String getFont(); //intrinsic
    String getSize(); //intrinsic
    String getColor(); //intrinsic

    void Create(String characterType);
}

   public class FlyweightText implements TextTypes { //concrete class: HANDLES INTRINSIC STATE, represents flyweight object

    //intrinsic attributes
    private String font;
    private String size;
    private String color;



      public FlyweightText(String font, String size, String color){
          this.font = font;
          this.color = color;
          this.size = size;

      }


       @Override
       public String getFont() {
           return font;
       }

       @Override
       public String getSize() {
           return size;
       }

       @Override
       public String getColor() {
           return color;
       }

       @Override
       public void Create(String characterType) {
       }
   }

    class FlyweightFactory{ //factory: manages the flyweights, makes shared and reused when requested.
    private static HashMap<String, TextTypes> flyweightMap = new HashMap<>();

    public static TextTypes getFlyweight(String font, String size, String color){ //uses interface
       String key = font + size + color;
        FlyweightText flyweightText = (FlyweightText) flyweightMap.get(key);

        if(flyweightText == null){
            flyweightText = new FlyweightText(font,size,color);
            flyweightMap.put(key,flyweightText);
            System.out.println("HelloWorldCS5800 has Color:" + color);
            System.out.println("                      Size:" + size);
            System.out.println("                      Font:" + font);
        }
        return flyweightText;
    }


    }

    class CharacterProperties{
    String CharacterType; //extrinsic
    TextTypes flyweight; //reference to flyweight object

    public CharacterProperties(String CharacterType, TextTypes flyweight){ //constructing attributes of the object
      this.CharacterType = CharacterType;
      this.flyweight = flyweight;
    }

    public void Create(){
        flyweight.Create(CharacterType);
        }

        @Override
        public String toString() {
            return "HelloWorldCS5800 has Color: " + flyweight.getColor() +
                    "\n                      Size: " + flyweight.getSize() +
                    "\n                      Font: " + flyweight.getFont();
        }


    }

    class Document{
    private final List<CharacterProperties> text = new ArrayList<>();

    public void addText(CharacterProperties character){
        text.add(character);
    }

    public void Create(){
        for(CharacterProperties character: text){
            character.Create();
        }
        }

    public void saveText(String filename){
        try(FileWriter writer = new FileWriter(filename)) {
            for(CharacterProperties character: text){
                writer.write(character.toString());
                writer.write(System.lineSeparator());
            }
            System.out.println("Document saved to file: "+ filename);
        } catch(IOException e){
            System.err.println("Error saving doc" + e.getMessage());
        }
    }

    public static Document loadDoc(String filename){
        Document document = new Document();
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                TextTypes textTypes = FlyweightFactory.getFlyweight("Arial", "12", "Black");
                CharacterProperties character = new CharacterProperties(line, textTypes);
                document.addText(character);
            }
            System.out.println("Document loaded: " + filename);
        } catch (IOException e){
            System.err.println("Error loading: " + e.getMessage());
        }
        return document;
    }

    static class Client {  //client or driver program it HANDLES THE EXTRINSIC STATE
        public static void main(String[] args) {
            final String[] colors = {"Red", "Blue", "Black"};
            final String[] sizes = {"12", "14", "16"};
            final String[] fonts = {"Arial", "Calibri", "Verdana"};

            Document document = new Document();
            Random random = new Random();

            for (int i = 0; i < colors.length ; i++) {
                    int randomFontIndex = random.nextInt(fonts.length);
                    int randomSizeIndex = random.nextInt(sizes.length);
                    FlyweightText flyweightText = (FlyweightText) FlyweightFactory.getFlyweight(fonts[i], sizes[randomSizeIndex], colors[randomFontIndex]);
                    CharacterProperties character = new CharacterProperties("" + (i + 1), flyweightText);
                    document.addText(character);
            }

            document.Create();

            document.saveText("document.txt");

            Document loadDocument = Document.loadDoc("document.txt");

            loadDocument.Create();

        }
}}

