package com.example.pickcountrystatecityjava;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private Spinner countrySpinner;
    private Spinner stateSpinner;
    private Spinner citySpinner;
    private String state;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countrySpinner = findViewById(R.id.countrySpinner);
        stateSpinner = findViewById(R.id.stateSpinner);
        citySpinner = findViewById(R.id.citySpinner);

        populateCountrySpinner();
        populateStateSpinner("India");
        popularCitySpinner2("India", "Maharashtra");

    }

    private void popularCitySpinner2(String country, String state) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://countriesnow.space/api/v0.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<CityData> call = apiService.getCities(country, state);
        call.enqueue(new Callback<CityData>() {
            @Override
            public void onResponse(Call<CityData> call, Response<CityData> response) {
                if (response.isSuccessful()) {
                    CityData cityData = response.body();
                    if (cityData != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_spinner_item,
                                cityData.getData()
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(adapter);
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<CityData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void populateStateSpinner(String country) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://countriesnow.space/api/v0.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        CountryRequestBody requestBody = new CountryRequestBody(country);
        Call<ShowStatesResponse> call = apiService.getStates(requestBody);

        call.enqueue(new Callback<ShowStatesResponse>() {
            @Override
            public void onResponse(Call<ShowStatesResponse> call, Response<ShowStatesResponse> response) {
                if (response.isSuccessful()) {
                    ShowStatesResponse showStatesResponse = response.body();
                    if (showStatesResponse != null) {
                        Data data = showStatesResponse.getData();
                        List<State> states = data.getStates();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_spinner_item
                        );
                        for (State state : states) {
                            adapter.add(state.getName());
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        stateSpinner.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch states data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowStatesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error occurred while fetching states data", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void populateCountrySpinner() {
        ApiService apiService = ApiClient.getApiService();
        Call<CountryResponse> countryCall = apiService.getCountries();
        countryCall.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.isSuccessful()) {
                    CountryResponse countryResponse = response.body();
                    if (countryResponse != null) {
                        List<CountryData> countries = countryResponse.getData();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_spinner_item
                        );
                        for (CountryData countryData : countries) {
                            adapter.add(countryData.getCountry());
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        countrySpinner.setAdapter(adapter);
                    }
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}

