package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.FormatAndValidateCredentials;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.HealthQuestionActivity;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.MasterEntity;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass.SubQuestion;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.QuestionTypeConstant;
import in.probusinsurance.healthquestionproject.R;


public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.ViewHolder> {

    Context context;
    int qnumber = 0;
    List<SubQuestion> subQuestionList;
    OnItemClickListner mlistner;
    Activity activity;
    private Calendar myCalendar;
    AdapterSubQuestion adaptersubquestion;
    int lastquestionposition = 0;
    String[] spinnerdata;

    public interface OnItemClickListner {
        void onItemClick(int posistion, String type);
    }

    public void setOnItemClickListener(OnItemClickListner listener) {
        mlistner = listener;
    }


    public AdapterQuestion(Context context, int qnumber, List<SubQuestion> subQuestionList) {
        this.context = context;
        this.qnumber = qnumber;
        this.subQuestionList = subQuestionList;
        myCalendar = Calendar.getInstance();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_healthquestion, parent, false);
        return new ViewHolder(view, mlistner);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(AdapterQuestion.ViewHolder holder, int position) {


        holder.txt_serialnumber.setText(String.valueOf(qnumber) + "." + String.valueOf(position + 1) + " ");
        holder.txt_question.setText(subQuestionList.get(position).getSubQuestionName());

        if (subQuestionList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT) || subQuestionList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_DATE)) {
            holder.radiogroup.setVisibility(View.GONE);
            holder.il_txt.setVisibility(View.VISIBLE);
            holder.spinner.setVisibility(View.GONE);
            if (subQuestionList.get(position).getInputValue() != null) {
                holder.ed_txt.setText(subQuestionList.get(position).getInputValue().toString());
                holder.ed_txt.setSelection(holder.ed_txt.length());
            } else {
                holder.ed_txt.setText("");
            }
            if (subQuestionList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT)) {
                holder.ed_txt.setInputType(InputType.TYPE_CLASS_TEXT);
                holder.ed_txt.setHint("Enter");

            }
            if (subQuestionList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_DATE)) {
                holder.ed_txt.setFocusable(false);
                holder.ed_txt.setHint("DD-MM-YYYY");
                holder.ed_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePickerDialog departureDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dates[] = FormatAndValidateCredentials.formatDate(year, month, dayOfMonth);
                                if (dates != null) {
                                    String dateOfBirth = dates[1];
                                    holder.ed_txt.setText(dateOfBirth);
                                    holder.ed_txt.setError(null, null);
                                    subQuestionList.get(position).setInputValue(holder.ed_txt.getText().toString().trim());
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
        }
        if (subQuestionList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_BOOLEAN)) {
            holder.radiogroup.setVisibility(View.VISIBLE);
            if (subQuestionList.get(position).getInputValue() != null) {
                if (subQuestionList.get(position).getInputValue() == "true") {
                    holder.radioyes.setChecked(true);
                    if (!subQuestionList.get(position).getHasSubQuestion()) {
                        setSubQuestionChild(position, holder.recyclerView, holder.progressBar);

                    } else {
                        setSubQuestionChild(position, holder.recyclerView, holder.progressBar);
                    }
                }
                if (subQuestionList.get(position).getInputValue() == "false") {
                    holder.radiono.setChecked(true);
                    holder.recyclerView.setVisibility(View.GONE);
                }
            }
        }
        if (subQuestionList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_SPINNER)) {
            spinnerdata = new String[0];
            holder.spinner.setVisibility(View.VISIBLE);

            if (subQuestionList.get(position).getSubQuestionName().toLowerCase().contains("current") || subQuestionList.get(position).getSubQuestionName().toLowerCase().contains("status")) {
                spinnerdata = context.getResources().getStringArray(R.array.currentStatus);
            } else if (subQuestionList.get(position).getSubQuestionName().toLowerCase().contains("line") || subQuestionList.get(position).getSubQuestionName().toLowerCase().contains("management")) {
                spinnerdata = context.getResources().getStringArray(R.array.LineOfManagement);
            }

            holder.spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, spinnerdata));
            if (subQuestionList.get(position).getInputValue() != null) {
                holder.spinner.setSelection(GetSpinnerPosition(subQuestionList.get(position).getInputValue(), spinnerdata));
            }

        }

        holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.health_radiobuttonyes) {
                    subQuestionList.get(position).setInputValue(true);
                    if (position != 0 && lastquestionposition != position) {
                        if (adaptersubquestion != null) {
                            subQuestionList.get(lastquestionposition).setSubQuestionChild(adaptersubquestion.GetSubQuestionChildData());
                        }
                    }
                    if (!subQuestionList.get(position).getHasSubQuestion()) {
                        lastquestionposition = position;
                        setSubQuestionChild(position, holder.recyclerView, holder.progressBar);
                    } else {
                        setSubQuestionChild(position, holder.recyclerView, holder.progressBar);
                    }
                }
                if (i == R.id.health_radiobuttonno) {
                    subQuestionList.get(position).setInputValue(false);
                    holder.recyclerView.setVisibility(View.GONE);

                }

            }
        });
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerpos, long id) {
                subQuestionList.get(position).setInputValue(holder.spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.ed_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                subQuestionList.get(position).setInputValue(holder.ed_txt.getText().toString().trim());
            }
        });


    }

    private void setSubQuestionChild(int position, RecyclerView recyclerView, ProgressBar progressBar) {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            adaptersubquestion = new AdapterSubQuestion(context, subQuestionList.get(position).getSubQuestionChild());
            handler.post(() -> {
                recyclerView.setAdapter(adaptersubquestion);
                progressBar.setVisibility(View.GONE);

            });
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
        return subQuestionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txt_question, txt_serialnumber;
        RadioButton radioyes, radiono;
        RadioGroup radiogroup;
        EditText ed_txt;
        TextInputLayout il_txt;
        Spinner spinner;
        private RecyclerView recyclerView;
        ProgressBar progressBar;

        public ViewHolder(View itemView, OnItemClickListner listner) {
            super(itemView);
            txt_serialnumber = itemView.findViewById(R.id.health_Questionnumber);
            txt_question = itemView.findViewById(R.id.health_Questiontitle);
            radiono = itemView.findViewById(R.id.health_radiobuttonno);
            radioyes = itemView.findViewById(R.id.health_radiobuttonyes);
            radiogroup = itemView.findViewById(R.id.health_radiogroup);
            il_txt = itemView.findViewById(R.id.health_inputlayout);
            ed_txt = itemView.findViewById(R.id.health_editext);
            spinner = itemView.findViewById(R.id.health_spinner);
            progressBar = itemView.findViewById(R.id.progessbar_subquestion_chlid);

            recyclerView = itemView.findViewById(R.id.recyclearview_subtosubquestion);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

    }


    public List<SubQuestion> getSubQuestionList() {
        return subQuestionList;
    }


}
