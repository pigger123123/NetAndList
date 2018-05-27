package weather;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class parseWeatherJson {
    public  Weather parseWeatherJSONWithGSON(String data)
    {
        try{
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            Weather weather=new Gson().fromJson(weatherContent, Weather.class);
            return weather;

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
