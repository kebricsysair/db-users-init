import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(readFromCSV().length);
    }

    public static void getRequest() throws IOException {
        URL url = new URL("http://localhost:8080/systemair/users/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println(content);
        int status = con.getResponseCode();
        System.out.println(status);
        con.disconnect();
    }

    public static void postUserToServer () throws IOException {
        URL url = new URL("http://localhost:8080/systemair/users/");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        String postBody = stringToJSON(readFromCSV());
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
        outputStream.writeBytes(postBody);
        outputStream.flush();
        outputStream.close();
        int responseCode = http.getResponseCode();
        System.out.println(responseCode);
    }

    public static String []  readFromCSV () {
        File file = new File("Contacts.csv");
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            Scanner scan = new Scanner(file);
            scan.useDelimiter(";");
            while (scan.hasNext()){
                arrayList.add(scan.next());
                System.out.println(arrayList);
            }
            scan.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        String [] temp = arrayList.toArray(new String[0]);
        return temp;
    }

    public static String stringToJSON (String [] array){
        JSONArray jsonArray = new JSONArray();
        for (String element : array) {
            jsonArray.put(element);
        }
        return jsonArray.toString();
    }
}