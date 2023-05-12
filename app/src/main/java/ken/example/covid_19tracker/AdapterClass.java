package ken.example.covid_19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;

import ken.example.covid_19tracker.Api.ApiUtilities;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.viewHolder>
{
    int m = 1;
    Context context;
    List<ModelClass> modelClassList;

    public AdapterClass(Context context, List<ModelClass> modelClassList) {
        this.context = context;
        this.modelClassList = modelClassList;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AdapterClass.viewHolder holder, int position) {

        ModelClass modelClass = modelClassList.get(position);

        if (m==1){
            holder.txtCases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getCases())));
        }
        else if(m == 2){
            holder.txtCases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getRecovered())));
        }

        else if(m == 3){
            holder.txtCases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getDeaths())));
        }
        else {
            holder.txtCases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getActive())));
        }

        holder.txtCountry.setText(modelClass.getCountry());
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public void filter(String itemText) {
        if (itemText.equals("cases")){
            m = 1;
        }
        else if(itemText.equals("active")){
            m = 2;
        }
        else if(itemText.equals("recovered")){
            m = 3;
        }
        else{
            m = 4;
        }
        notifyDataSetChanged();

    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView txtCountry,txtCases;

        public viewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);

            txtCountry =  itemView.findViewById(R.id.txtCountry);
            txtCases = itemView.findViewById(R.id.txtCases);

        }
    }
}
