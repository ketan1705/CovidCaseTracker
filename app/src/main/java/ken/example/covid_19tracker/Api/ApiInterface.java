package ken.example.covid_19tracker.Api;

import java.util.List;

import ken.example.covid_19tracker.ModelClass;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

     String BASE_URL = "https://corona.lmao.ninja/v2/";

     @GET("countries")
         Call<List<ModelClass>> getcountrydata();

}
