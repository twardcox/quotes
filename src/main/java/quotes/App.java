/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package quotes;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {
        String getQuote = getQuoteFromWeb();
        JsonArray quote = addQuote(getQuote);
        addToFile(quote);

    }

    public static JsonArray addQuote(String jsonString) {
        JsonArray recentQuote = new JsonArray();
        try {
            Gson gson = new Gson();
            JsonObject inputObj = gson.fromJson(jsonString, JsonObject.class);
            JsonObject updatedObj = new JsonObject();
            updatedObj.add("text", inputObj.get("starWarsQuote"));
            recentQuote = gson.fromJson(new FileReader("./src/main/resources/recentquotes.json"), JsonArray.class);
            recentQuote.add(updatedObj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recentQuote;
    }

    public static void addToFile(JsonArray jsonString) throws IOException {
        File file = new File("./src/main/resources/recentquotes.json");
        BufferedWriter bw = null;

        FileWriter fw = new FileWriter(file);
        bw = new BufferedWriter(fw);
        bw.write(jsonString.toString());
        System.out.println("File written Successfully");
        bw.close();

    }


    public static String getQuoteFromWeb() throws FileNotFoundException {

        String response = "";
     try {
    // https://www.baeldung.com/java-http-request
    // http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en
    URL url = new URL("http://swquotesapi.digitaljedi.dk/api/SWQuote/RandomStarWarsQuote");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    // System.out.println(connection.getResponseCode());

    // synchronous: java is going to be working on running line 15 for a while
    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();

    System.out.println(content);

    response = content.toString();

  } catch (IOException e) {
     String fileString = readFile("src/main/resources/recentquotes.json");
     Quote[] quotes = buildQuotes(fileString);
     int randomQuoteLocation = generateRandomNumberBetween(0, quotes.length);
     System.out.println(quotes[randomQuoteLocation]);
     e.printStackTrace();
  }
        return response;
}
        // read in the file, return it as a string
        // in: file location (String)
        // out: String

        // could use scanner or buffered reader
        // Scanner reader = new Scanner(new File("src/main/resources/unicorns.json"));

        // buffered reader info: https://www.baeldung.com/java-buffered-reader
        // BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/input.txt"));

    public static String readFile(String fileLocation) throws FileNotFoundException {
        String locationOfFile = fileLocation == null ? "src/main/resources/recentquotes.json": fileLocation;
        Scanner reader = new Scanner(new File(fileLocation));
        String entireFile = reader.nextLine();

        while ( reader.hasNextLine() ){
            entireFile += reader.nextLine();
        }

        return entireFile;
    }




    // turn the string into an array of Quote objects
        // in: string
        // out: Quote[]
    public static Quote[] buildQuotes(String entireFile){
        Gson gson = new Gson();

        Quote[] quotes = gson.fromJson(entireFile, Quote[].class);

        return quotes;
    }


    // generate a random number between two given values (zero and length of quotes array)
        // in: min & max
        // out: random int between

    // min is inclusive, max is not
    public static int generateRandomNumberBetween(int min, int max){
        // cite source here
        if ( min >= max ){
            throw new IllegalArgumentException("max must be greater than min");
        }
        int ranNum = (int)((Math.random() * max ) + min);
        return ranNum;
    }


}
