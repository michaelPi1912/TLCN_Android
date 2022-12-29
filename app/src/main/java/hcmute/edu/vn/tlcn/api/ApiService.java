package hcmute.edu.vn.tlcn.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import hcmute.edu.vn.tlcn.Models.Ticket;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://michaelpi1912.github.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("/api.v1/data.json")
    Call<List<Ticket>> getListTicket();
}
