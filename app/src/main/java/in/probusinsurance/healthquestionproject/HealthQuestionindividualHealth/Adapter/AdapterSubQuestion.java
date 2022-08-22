package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.FormatAndValidateCredentials;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass.SubQuestion;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass.SubQuestionChild;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.QuestionTypeConstant;
import in.probusinsurance.healthquestionproject.R;

public class AdapterSubQuestion extends RecyclerView.Adapter<AdapterSubQuestion.ViewHolder> {

    private final List<SubQuestionChild> subquestionchildlist;
    private Context mContext;
    private Calendar myCalendar;
    String[] spinnerdata;

    public AdapterSubQuestion(Context context, List<SubQuestionChild> items) {
        subquestionchildlist = items;
        mContext = context;
        myCalendar = Calendar.getInstance();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_healthsubquestion, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.subQuestionTitle.setText(subquestionchildlist.get(position).getSubQuestionNameChild());

        // Set Question Text and Date Type
        if (subquestionchildlist.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT) || subquestionchildlist.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTNUMBER) || subquestionchildlist.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_DATE)) {
            holder.ll_editext.setVisibility(View.VISIBLE);
            if (subquestionchildlist.get(position).getInputValue() != null) {
                holder.subeditText.setText(subquestionchildlist.get(position).getInputValue().toString());
                holder.subeditText.setSelection(holder.subeditText.length());
            } else {
                holder.subeditText.setText("");
            }
            if (subquestionchildlist.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT)) {
                holder.subeditText.setInputType(InputType.TYPE_CLASS_TEXT);
                holder.subeditText.setHint("Enter");

            }
            if (subquestionchildlist.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_DATE)) {
                holder.subeditText.setFocusable(false);
                holder.subeditText.setHint("DD-MM-YYYY");
                holder.subeditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog departureDatePicker = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dates[] = FormatAndValidateCredentials.formatDate(year, month, dayOfMonth);
                                if (dates != null) {
                                    String dateOfBirth = dates[1];
                                    holder.subeditText.setText(dateOfBirth);
                                    holder.subeditText.setError(null, null);
                                }
                            }
                        }, myCalendar
                                .get(Calendar.YEAR), myCalendar
                                .get(Calendar.MONTH), myCalendar
                                .get(Calendar.DAY_OF_MONTH));
                        departureDatePicker.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                        departureDatePicker.show();

                    }
                });
            }


        }//Set Question Soinner Type Data
        else if (subquestionchildlist.get(position).getInputType().equalsIgnoreCase(QuestionTypeConstant.QUESTION_TYPE_SPINNER)) {
            holder.ll_spinner.setVisibility(View.VISIBLE);
            spinnerdata = new String[0];

            if (subquestionchildlist.get(position).getSubQuestionNameChild().toLowerCase().contains("current") || subquestionchildlist.get(position).getSubQuestionNameChild().toLowerCase().contains("status")) {
                spinnerdata = mContext.getResources().getStringArray(R.array.currentStatus);
            }
            if (subquestionchildlist.get(position).getSubQuestionNameChild().toLowerCase().contains("line") || subquestionchildlist.get(position).getSubQuestionNameChild().toLowerCase().contains("management")) {
                spinnerdata = mContext.getResources().getStringArray(R.array.LineOfManagement);
            }
            holder.subSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, spinnerdata));
            if (subquestionchildlist.get(position).getInputValue() != null) {
                holder.subSpinner.setSelection(GetSpinnerPosition(subquestionchildlist.get(position).getInputValue(), spinnerdata));
            }


        } else {
            holder.ll_spinner.setVisibility(View.GONE);
            holder.ll_editext.setVisibility(View.GONE);
        }

        // Set Data into Object when On Text Changed
        holder.subeditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subquestionchildlist.get(position).setInputValue(holder.subeditText.getText().toString().trim());
            }
        });

        // Set Spinner Data When Item Selected
        holder.subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerpos, long id) {
                subquestionchildlist.get(position).setInputValue(holder.subSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private int GetSpinnerPosition(Object inputValue, String[] spinnerdata) {
        int pos = 0;
        for (int i = 0; i < spinnerdata.length; i++) {
            if (inputValue.toString().equals(spinnerdata[i].toString())) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    @Override
    public int getItemCount() {
        return subquestionchildlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public SubQuestionChild mItem;
        public TextView subQuestionTitle;
        public Spinner subSpinner;
        public EditText subeditText;

        public RelativeLayout ll_spinner, ll_editext;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // View Items
            subQuestionTitle = (TextView) view.findViewById(R.id.health_sub_question_title);
            subSpinner = view.findViewById(R.id.health_sub_question_spinner);
            subeditText = view.findViewById(R.id.health_sub_question_editetxt);
            // Layout File
            ll_editext = view.findViewById(R.id.health_sub_question_editext_layout);
            ll_spinner = view.findViewById(R.id.health_sub_question_spinner_layout);

        }

    }

    public List<SubQuestionChild> GetSubQuestionChildData() {
        return subquestionchildlist;
    }
}
