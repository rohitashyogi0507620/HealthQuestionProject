package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.MasterEntity;
import in.probusinsurance.healthquestionproject.R;


public class SpinnerItemMasterEntryAdapter extends ArrayAdapter<MasterEntity> {

    private Context context;
    List<MasterEntity> list;
    LayoutInflater layoutInflater;


    public SpinnerItemMasterEntryAdapter(Activity context, int resouceId, int textviewId, List<MasterEntity> list) {
        super(context, resouceId, textviewId, list);
        this.context = context;
        layoutInflater = context.getLayoutInflater();
        this.list = list;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String rowItem = list.get(position).getName();
        View rowview = layoutInflater.inflate(R.layout.spinner_item, null, true);
        TextView txtTitle = rowview.findViewById(R.id.spinner_text_view);
        txtTitle.setText(rowItem);

        return rowview;
    }


}
