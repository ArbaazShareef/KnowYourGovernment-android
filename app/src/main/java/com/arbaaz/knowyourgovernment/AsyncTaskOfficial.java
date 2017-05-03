package com.arbaaz.knowyourgovernment;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arbaaz on 09-04-2017.
 */

public class AsyncTaskOfficial extends AsyncTask<String, Void, String> {
    MainActivity mainActivity;
    private List OfficialsList = new ArrayList<>();
    private final static  String TAG = "ASYNCOFFICIAL";
    private String API_KEY = "AIzaSyA9Rk70p36mJsZDTUaLXEhaKye5XkzaQoM";
    private String nDisplay;
    private String initialURL = "https://www.googleapis.com/civicinfo/v2/representatives?key="+API_KEY+"&address=";
    public AsyncTaskOfficial(MainActivity mainActivity) {

        this.mainActivity = mainActivity;


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mainActivity.setOfficialList(OfficialsList);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "Starting the Async Task");
        Log.d(TAG, "doInBackground: " );
        OfficialsList.clear();
        String FinalURL = initialURL+strings[0];
        String lineWithoutSpaces = FinalURL.replaceAll("\\s+","");
        Log.d(TAG, "URL: "+lineWithoutSpaces );

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(lineWithoutSpaces);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            if(statusCode!=200)
            {
                return null;
            }
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        parseJSON(sb.toString().trim());

        return null;

    }

    private List<Official> parseJSON(String s) {
        if(s==null)
            return null;
        try {
            JSONObject govOffice = new JSONObject(s);
            //   jsonarray = new JSONArray(s);
            JSONArray offices_name = govOffice.getJSONArray("offices");
            JSONArray officials = govOffice.getJSONArray("officials");

            String nInput = govOffice.getString("normalizedInput");
            JSONObject inputObject = new JSONObject(nInput);
            String nCity = inputObject.has("city")?inputObject.getString("city").equals("")?"":inputObject.getString("city")+",":"";
            String nState = inputObject.has("state")?inputObject.getString("state"):"";
            String nZip = inputObject.has("zip")?inputObject.getString("zip"):"";
            nDisplay = nCity+nState+" "+nZip;

            int count = 0;
            while (count < offices_name.length()) {
                JSONObject officeObject = offices_name.getJSONObject(count);
                count++;
                String gov_office = officeObject.getString("name");

                String index = officeObject.getString("officialIndices");
                String replace = index.replace("[","");
                String replace1 = replace.replace("]","");
                List<String> arrayList = new ArrayList<String>    (Arrays.asList(replace1.split(",")));
                List<Integer> myList = new ArrayList<Integer>();
                for(String fav:arrayList){
                    myList.add(Integer.parseInt(fav.trim()));
                }
                //List<String> myList = new ArrayList<String>(Arrays.asList(index.split(",")));
                for( int i=0;i<myList.size();i++)
                {
                    JSONObject officialsObject = officials.getJSONObject(myList.get(i));
                    String officialName = officialsObject.getString("name");
                    JSONArray address =null,keyArray2 =null,keyArray =null,keyArray1=null;;
                    JSONObject addressObject = null;
                    String addLine1 = "",addLine2= "", addLine3="",city = "",state = "";
                    String zip =  "",party = "",phone="",url = "",photoUrl ="",email="";
                    String googleplus ="",facebook="",twitter ="", youtube = "";

                    address = officialsObject.has("address")?officialsObject.getJSONArray("address"):null;
                    if(address!=null&&address.length()>0){
                        addressObject = address.getJSONObject(0);
                        addLine1 = addressObject.has("line1")?addressObject.getString("line1"):"";
                        addLine2= addressObject.has("line2")?addressObject.getString("line2"):"";
                        addLine3= addressObject.has("line3")?addressObject.getString("line3"):"";
                        city = addressObject.has("city")?addressObject.getString("city"):"";
                        state = addressObject.has("state")?addressObject.getString("state"):"";
                        zip =  addressObject.has("zip")?addressObject.getString("zip"):"";
                    }
                    party = officialsObject.has("party")?officialsObject.getString("party"):"";
                    keyArray = officialsObject.has("phones")?officialsObject.getJSONArray("phones"):null;
                    if(keyArray!=null&&keyArray.length()>0) {
                        phone = keyArray.getString(0);
                    }
                    keyArray1 = officialsObject.has("urls")?officialsObject.getJSONArray("urls"):null;
                    if(keyArray1!=null&&keyArray1.length()>0) {
                        url = keyArray1.getString(0);
                    }
                    keyArray1 = officialsObject.has("emails")?officialsObject.getJSONArray("emails"):null;
                    if(keyArray1!=null&&keyArray1.length()>0) {
                        email = keyArray1.getString(0).equals("")?"":keyArray1.getString(0);
                    }
                    photoUrl = officialsObject.has("photoUrl")?officialsObject.getString("photoUrl"):"";

                    keyArray2 = officialsObject.has("channels")?officialsObject.getJSONArray("channels"):null;
                    if(keyArray2!=null&&keyArray2.length()>0) {
                        for (int j = 0; j < keyArray2.length(); j++) {
                            JSONObject channel = keyArray2.getJSONObject(j);
                            if (channel.has("type")?channel.getString("type").compareToIgnoreCase("GooglePlus")==0:false)
                                googleplus = channel.has("id")?channel.getString("id"):"";
                            else if (channel.has("type")?channel.getString("type").compareToIgnoreCase("Facebook")==0:false)
                                facebook = channel.has("id")?channel.getString("id"):"";
                            else if (channel.has("type")?channel.getString("type").compareToIgnoreCase("Twitter")==0:false)
                                twitter = channel.has("id")?channel.getString("id"):"";
                            else if (channel.has("type")?channel.getString("type").compareToIgnoreCase("YouTube")==0:false)
                                youtube = channel.has("id")?channel.getString("id"):"";

                        }
                    }
                    Official official = new Official();
                    official.setTitle(gov_office);
                    official.setName(officialName);
                    official.setParty(party);
                    official.setNormAddress(nDisplay);
                    official.setAddress(addLine1+"\n"+addLine2+"\n"+addLine3);
                    if(email.isEmpty()){
                        official.setEmail("Data Not Provided");
                    }else{
                        official.setEmail(email);
                    }
                    official.setPhno(phone);
                    official.setFacebookURL(facebook);
                    official.setGooglePlusURL(googleplus);
                    official.setWebsite(url);
                    official.setYoutubeUrl(youtube);
                    official.setTwitter(twitter);
                    official.setPhotoURL(photoUrl);
                    official.setA1(addLine1);
                    official.setA2(addLine2);
                    official.setA3(addLine3);
                    OfficialsList.add(official);
                    Log.d(TAG,"-----------------------------------------------------------------------------");
                    Log.d(TAG,"Title:"+official.getTitle());
                    Log.d(TAG,"Name:"+official.getName());
                    Log.d(TAG,"Address"+official.getAddress());
                    Log.d(TAG,"Email:"+official.getEmail());
                    Log.d(TAG,"Party:"+official.getParty());
                    Log.d(TAG,"Phno:"+official.getPhno());
                    Log.d(TAG,"PhotoURL:"+official.getPhotoURL());
                    Log.d(TAG,"FacebookURL:"+official.getFacebookURL());
                    Log.d(TAG,"YoutubeURL:"+official.getYoutubeUrl());
                    Log.d(TAG,"Twitter:"+official.getTwitter());
                    Log.d(TAG,"Website:"+official.getWebsite());

                    Log.d(TAG,"-----------------------------------------------------------------------------");
                }
            }
            return OfficialsList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("crash", "input =" + nDisplay);
            return null;
        }
    }
    public List<Integer> readIntegersArray(JsonReader reader) throws IOException {
        List<Integer> indices = new ArrayList<Integer>();
        reader.beginArray();
        while (reader.hasNext()) {
            indices.add(reader.nextInt());
        }
        reader.endArray();
        return indices;
    }
}
