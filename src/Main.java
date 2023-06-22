import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] readData = CSVReader();
/*
        int j = 0;
        for(String value : readData){
            System.out.print((j % 9) + " " + j + value + " ");
            j++;
        }

 */


        String[] temp = new String[9];
        int i = 0;
        for (String data : readData) {
            System.out.println(data);
            temp[i] = data;
            if (i == 8) {
                postUserToServer(stringToJSON(temp));
                temp = new String[9];
                i = -1;
            }
            i++;
        }

    }

    public static void test() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Ales");
        jsonObject.put("surname", "Janžič");
        jsonObject.put("email", "ales.janzic@systemair.si");
        jsonObject.put("department", "Logistika");
        jsonObject.put("businessPhone", "+386 2 4501 707");
        jsonObject.put("jobTitle", "Referent nabave in logistike");
        jsonObject.put("postcode", 2000);
        jsonObject.put("number", 123);
        postUserToServer(jsonObject);
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

    public static void postUserToServer(JSONObject postBody) throws IOException {
        URL url = new URL("http://localhost:8080/systemair/users/private/post/user/new");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
        outputStream.write(postBody.toString().getBytes("UTF-8"));
        //outputStream.writeBytes(postBody.toString());
        outputStream.flush();
        outputStream.close();
        int responseCode = http.getResponseCode();
        System.out.println(responseCode);
    }

    public static String[] readFromCSV() {
        File file = new File("Contacts.csv");
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Scanner scan = new Scanner(file);
            scan.useDelimiter(";");
            while (scan.hasNext()) {
                arrayList.add(scan.next());
                System.out.println(arrayList);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] temp = arrayList.toArray(new String[0]);
        return temp;
    }

    public static JSONObject stringToJSON(String[] array) {
        JSONObject jsonObject = new JSONObject();
        String [] temp = {"surname", "name", "email", "jobTitle", "businessPhone", "mobilePhone", "postcode", "number", "department"};
        int i = -1;
        for (String element : array) {
            i++;
            System.out.println(i + element);
            if(element == "")
                continue;
            if(temp[i].equals("postcode") || temp[i].equals("number")){
                jsonObject.put(temp[i], Integer.parseInt(element));
            }
            else
                jsonObject.put(temp[i], element);

        }
        System.out.println(jsonObject);
        return jsonObject;
    }

    public static String[] CSVReader() {
        String csvFile = "Contacts.csv";
        String line;
        String csvSplitBy = ";";
        ArrayList<String> arrayList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                for (String value : data) {
                    arrayList.add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }
}