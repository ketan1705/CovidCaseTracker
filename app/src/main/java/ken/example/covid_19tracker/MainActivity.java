package ken.example.covid_19tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ken.example.covid_19tracker.Api.ApiUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CountryCodePicker countryCodePicker;
    TextView totalCases, todayCases, totalActive, totalRecovered, todayRecovered, totalDeath, todayDeath, totalTests;
    String country;
    Spinner spinner;
    TextView mFilter;
    String[] types = {"cases", "death", "active", "recovered"};
    RecyclerView recyclerView;
    PieChart pieChart;
    AdapterClass adapter;
    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        countryCodePicker = findViewById(R.id.countryCodeHolder);
        totalCases = findViewById(R.id.total_cases);
        todayCases = findViewById(R.id.today_total_cases);
        totalDeath = findViewById(R.id.death_cases);
        todayDeath = findViewById(R.id.today_death_cases);
        totalActive = findViewById(R.id.active_cases);
        totalRecovered = findViewById(R.id.recovered_cases);
        todayRecovered = findViewById(R.id.today_recovered_cases);
        totalTests = findViewById(R.id.tests_cases);
        spinner = findViewById(R.id.spinner);
        mFilter = findViewById(R.id.filter);
        recyclerView = findViewById(R.id.recycler_view);
        pieChart = findViewById(R.id.pieChart);

        modelClassList = new ArrayList<>();
        modelClassList2 = new ArrayList<>();

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        ApiUtilities.getApiInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {
                if (response.body() != null) {
                    modelClassList2.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });

        adapter = new AdapterClass(getApplicationContext(), modelClassList2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        countryCodePicker.setAutoDetectedCountry(true);
        country = countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country = countryCodePicker.getSelectedCountryName();
                fetchData();
            }
        });
        fetchData();

    }

    private void fetchData() {

        ApiUtilities.getApiInterface().getcountrydata().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(@NotNull Call<List<ModelClass>> call, @NotNull Response<List<ModelClass>> response) {

                modelClassList.addAll(response.body());

                for (int i = 0; i < modelClassList.size(); i++) {
                    Log.d("checkCountry", country);
                    if (modelClassList.get(i).getCountry().equals(country)) {
                        Log.i("TotalActivecases",modelClassList.get(i).getActive());
                        totalActive.setText((modelClassList.get(i).getActive()));
                        todayCases.setText((modelClassList.get(i).getTodayCases()));
                        todayDeath.setText((modelClassList.get(i).getTodayDeaths()));
                        todayRecovered.setText((modelClassList.get(i).getTodayRecovered()));
                        totalCases.setText((modelClassList.get(i).getCases()));
                        totalDeath.setText((modelClassList.get(i).getDeaths()));
                        totalRecovered.setText((modelClassList.get(i).getRecovered()));
                        totalTests.setText((modelClassList.get(i).getTests()));

                        int active, deaths, cases, recovered;
                        active = Integer.parseInt(modelClassList.get(i).getActive());
                        deaths = Integer.parseInt(modelClassList.get(i).getDeaths());
                        cases = Integer.parseInt(modelClassList.get(i).getCases());
                        recovered = Integer.parseInt(modelClassList.get(i).getRecovered());
//                        tests = Integer.parseInt(modelClassList.get(i).getTests());

                        updateGraph(active, deaths, cases, recovered);


                    }
                    Log.i("AfterIf","check");

                }
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });

    }

    private void updateGraph(int active, int deaths, int cases, int recovered) {
        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("Cases", cases, Color.parseColor("#135ed0")));
        pieChart.addPieSlice(new PieModel("Active", active, Color.parseColor("#038A09")));
        pieChart.addPieSlice(new PieModel("Recovered", recovered, Color.parseColor("#D3BF13")));
//        pieChart.addPieSlice(new PieModel("Tests", tests, Color.parseColor("#F66A5F")));
        pieChart.addPieSlice(new PieModel("Deaths", deaths, Color.parseColor("#BD1417")));
        pieChart.startAnimation();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String itemText = types[position];
        mFilter.setText(itemText);
        adapter.filter(itemText);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}