package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.Adapter.AdapterQuestion;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass.HealthQuestions;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass.Response;
import in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass.SubQuestion;
import in.probusinsurance.healthquestionproject.R;


public class HealthQuestionActivity extends AppCompatActivity {

    ImageView imagegoback;
    List<HealthQuestions> healthQuestionsList;
    ProgressBar progressBar, progressbar_subquestion;
    Button btnnext, btnprevious;
    static int position = 0;

    RecyclerView recyclerViewSubquestion;
    AdapterQuestion adpater;


    TextView txt_question, txt_serialnumber;
    Switch switchyesno;
    EditText ed_txt;
    TextInputLayout il_txt;

    List<SubQuestion> subQuestionList;
    ProgressDialog progressDialog;
    Gson gson = null;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_question);
        Intialization();
        LoadHealthQuestion();
        Onclicks();
    }

    private void Onclicks() {

        imagegoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position < healthQuestionsList.size() - 1) {
                    SetInputData();
                    position = position + 1;
                    ChecksubmitButton();
                    CheckValidatationQuestion(position);
                    SetHealthQuestion(position);

                } else {
                    SetInputData();
                    StartnewActiivty();
                }
            }
        });

        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    SetInputData();
                    position = position - 1;
                    ChecksubmitButton();
                    SetHealthQuestion(position);
                } else {
                    showAlertDialog("Go Back", "Do You Want exit ??", "Yes", "cancle", false);
                }
            }
        });


    }

    private void CheckValidatationQuestion(int position) {
        boolean[] isvalidated = new boolean[healthQuestionsList.get(position).getSubQuestions().size()];

        if (healthQuestionsList.get(position).getInputValue() != null) {
            Log.i("Validation", "Question Number " + String.valueOf(position) + " Value is :" + healthQuestionsList.get(position).getInputValue());
            // Check Question  true and false validation then go to sub question validation
            if (healthQuestionsList.get(position).getInputValue().equals(true)) {
                // When Question is Set yes then check subquestion has or not
                if (healthQuestionsList.get(position).getHasSubQuestion()) {

                    for (int subpos = 0; subpos < healthQuestionsList.get(position).getSubQuestions().size(); subpos++) {

                        if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getInputValue() != null) {


                            Log.i("Validation", "Sub Question Number " + String.valueOf(subpos + 1) + " Value is :" + healthQuestionsList.get(position).getSubQuestions().get(subpos).getInputValue());
                            // Check Sub Question  true and false validation then go to sub question Child validation

                            if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getInputValue().equals(true)) {
                                return;
                            }

                            if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getHasSubQuestion()) {
                                for (int subposchild = 0; subposchild < healthQuestionsList.get(position).getSubQuestions().get(subpos).getSubQuestionChild().size(); subposchild++) {
                                    if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getSubQuestionChild().get(subposchild).getInputValue() != null) {
                                        Log.i("Validation", "Sub Question Child Number " + String.valueOf(subposchild + 1) + " Value is :" + healthQuestionsList.get(position).getSubQuestions().get(subpos).getSubQuestionChild().get(subposchild).getInputValue());
                                    } else {
                                        Log.i("Validation", "Sub Question Child Number " + String.valueOf(subposchild + 1) + " Value is null Or Not Set");
                                    }
                                }

                            }

                        } else {
                            Log.i("Validation", "Sub Question Number " + String.valueOf(subpos + 1) + " Value is null Or Not Set");
                        }
                    }
                } else {
                    Log.i("Validation", "Question Number " + String.valueOf(position) + " don't Have Sub Question");
                }
            }

        } else {
            Log.i("Validation", "Question Number " + String.valueOf(position) + " Value is null Or Not Set");
        }

    }

    private void CheckValidatationFullQuestion(int position) {
        boolean[] isvalidated = new boolean[healthQuestionsList.get(position).getSubQuestions().size()];

        if (healthQuestionsList.get(position).getInputValue() != null) {
            Log.i("Validation", "Question Number " + String.valueOf(position) + " Value is :" + healthQuestionsList.get(position).getInputValue());
            // Check Question  true and false validation then go to sub question validation
            if (healthQuestionsList.get(position).getInputValue().equals(true)) {
                // When Question is Set yes then check subquestion has or not
                if (healthQuestionsList.get(position).getHasSubQuestion()) {

                    for (int subpos = 0; subpos < healthQuestionsList.get(position).getSubQuestions().size(); subpos++) {

                        if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getInputValue() != null) {

                            Log.i("Validation", "Sub Question Number " + String.valueOf(subpos + 1) + " Value is :" + healthQuestionsList.get(position).getSubQuestions().get(subpos).getInputValue());
                            // Check Sub Question  true and false validation then go to sub question Child validation

                            if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getInputValue().equals(true)) {

                                for (int subposchild = 0; subposchild < healthQuestionsList.get(position).getSubQuestions().get(subpos).getSubQuestionChild().size(); subposchild++) {
                                    if (healthQuestionsList.get(position).getSubQuestions().get(subpos).getSubQuestionChild().get(subposchild).getInputValue() != null) {
                                        Log.i("Validation", "Sub Question Child Number " + String.valueOf(subposchild + 1) + " Value is :" + healthQuestionsList.get(position).getSubQuestions().get(subpos).getSubQuestionChild().get(subposchild).getInputValue());
                                    } else {
                                        Log.i("Validation", "Sub Question Child Number " + String.valueOf(subposchild + 1) + " Value is null Or Not Set");
                                    }
                                }
                            }

                        } else {
                            Log.i("Validation", "Sub Question Number " + String.valueOf(subpos + 1) + " Value is null Or Not Set");
                        }
                    }
                } else {
                    Log.i("Validation", "Question Number " + String.valueOf(position) + " don't Have Sub Question");
                }
            }

        } else {
            Log.i("Validation", "Question Number " + String.valueOf(position) + " Value is null Or Not Set");
        }

    }

    private void StartnewActiivty() {
        showAlertDialog("Submit", "do you want to submit this form ", "Yes", "cancle", true);
    }

    private void showAlertDialog(String title, String message, String posbtn, String negbtn,
                                 boolean issubmit) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_refereshicon);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(posbtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (issubmit) {
                    String finaldata = gson.toJson(healthQuestionsList);
                    Log.d("HealthQuestionList", finaldata);
                    Toast.makeText(getApplicationContext(), "All question Are Done", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        });
        alertDialog.setNegativeButton(negbtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }


    private void SetInputData() {

        if (healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT) && il_txt.getVisibility() == View.VISIBLE) {
            healthQuestionsList.get(position).setInputValue(ed_txt.getText().toString());
            ed_txt.setText("");
        }
        if (healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_BOOLEAN) && switchyesno.getVisibility() == View.VISIBLE) {
            if (switchyesno.isChecked()) {
                healthQuestionsList.get(position).setInputValue(true);
            } else {
                healthQuestionsList.get(position).setInputValue(false);
            }

        }
        if (healthQuestionsList.get(position).getHasSubQuestion()) {
            if (healthQuestionsList.get(position).getInputValue().toString().equals("true")) {
                healthQuestionsList.get(position).setSubQuestions(adpater.getSubQuestionList());
            }
        }

        //String singlequestion = new Gson().toJson(healthQuestionsList.get(position));
        //Log.e("SubQuestionList", singlequestion);


    }

    private void ChecksubmitButton() {
        if (position == healthQuestionsList.size() - 1) {
            btnnext.setText("Submit");

        } else {
            btnnext.setText("Next");
        }
    }

    private void Intialization() {
        imagegoback = findViewById(R.id.imageview_goback);
        progressBar = findViewById(R.id.progressbar);
        progressbar_subquestion = findViewById(R.id.progessbarsubquestion);
        btnnext = findViewById(R.id.btn_nextQuestion);
        btnprevious = findViewById(R.id.btn_previousQuestion);

        healthQuestionsList = new ArrayList<>();
        subQuestionList = new ArrayList<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        myCalendar = Calendar.getInstance();

        btnnext.setEnabled(true);
        btnnext.setBackgroundColor(getResources().getColor(R.color.colorprimary));
        btnnext.setTextColor(getResources().getColor(R.color.white));
        btnprevious.setEnabled(true);
        btnprevious.setBackgroundColor(getResources().getColor(R.color.colorprimary));
        btnprevious.setTextColor(getResources().getColor(R.color.white));


        txt_serialnumber = findViewById(R.id.health_Questionnumber);
        txt_question = findViewById(R.id.health_Questiontitle);
        switchyesno = findViewById(R.id.health_switchyesno);
        il_txt = findViewById(R.id.health_inputlayout);
        ed_txt = findViewById(R.id.health_editext);

        recyclerViewSubquestion = findViewById(R.id.recyclearview_subQuestion);
        recyclerViewSubquestion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Your Application Submitting ....");


    }

    private void LoadHealthQuestion() {


        String jsondata = "{\n" +
                "    \"Error\": null,\n" +
                "    \"IsAuthenticated\": true,\n" +
                "    \"Message\": null,\n" +
                "    \"Response\": [\n" +
                "        {\n" +
                "            \"QuestionCode\": \"1\",\n" +
                "            \"QuestionName\": \"Does the insured member have any ailment/disability/deformity, due to accident or congenital disease?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_1\",\n" +
                "                    \"SubQuestionName\": \"Hypertension/ High blood pressure\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_2\",\n" +
                "                    \"SubQuestionName\": \"Diabetes/ High blood sugar/Sugar in urine\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_3\",\n" +
                "                    \"SubQuestionName\": \"Cancer, Tumour, Growth or Cyst of any kind\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_4\",\n" +
                "                    \"SubQuestionName\": \"Chest Pain/ Heart Attack or any other Heart Disease/ Problem\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_5\",\n" +
                "                    \"SubQuestionName\": \"Liver or Gall Bladder ailment/Jaundice/Hepatitis B or C\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_6\",\n" +
                "                    \"SubQuestionName\": \"Kidney ailment or Diseases of Reproductive organs\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_7\",\n" +
                "                    \"SubQuestionName\": \"Tuberculosis/ Asthma or any other Lung disorder\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_8\",\n" +
                "                    \"SubQuestionName\": \"Ulcer (Stomach/ Duodenal), or any ailment of Digestive System\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_9\",\n" +
                "                    \"SubQuestionName\": \"Any Blood disorder (example Anaemia, Haemophilia, Thalassaemia) or any genetic disorder\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_10\",\n" +
                "                    \"SubQuestionName\": \"HIV Infection/AIDS or Positive test for HIV\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_11\",\n" +
                "                    \"SubQuestionName\": \"Nervous, Psychiatric or Mental or Sleep disorder\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_12\",\n" +
                "                    \"SubQuestionName\": \"Stroke/ Paralysis/ Epilepsy (Fits) or any other Nervous disorder (Brain / Spinal Cord etc.)\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_13\",\n" +
                "                    \"SubQuestionName\": \"Abnormal Thyroid Function/ Goiter or any Endocrine organ disorders\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_14\",\n" +
                "                    \"SubQuestionName\": \"Eye or vision disorders/ Ear/ Nose or Throat diseases\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_15\",\n" +
                "                    \"SubQuestionName\": \"Arthritis, Spondylitis, Fracture or any other disorder of Muscle Bone/ Joint/ Ligament/ Cartilage\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"SUB_16\",\n" +
                "                    \"SubQuestionName\": \"Any other disease/condition not mentioned above\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"bool\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB002\",\n" +
                "                            \"SubQuestionNameChild\": \"ConsultationDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB004\",\n" +
                "                            \"SubQuestionNameChild\": \"CurrentStatus\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB003\",\n" +
                "                            \"SubQuestionNameChild\": \"DiagnosisDate\",\n" +
                "                            \"InputType\": \"date\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB001\",\n" +
                "                            \"SubQuestionNameChild\": \"ExactDiagnosis\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB005\",\n" +
                "                            \"SubQuestionNameChild\": \"LineOfManagement\",\n" +
                "                            \"InputType\": \"drop\",\n" +
                "                            \"InputValue\": null\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"SubQuestionCodeChild\": \"HDFCERGOSUB006\",\n" +
                "                            \"SubQuestionNameChild\": \"TreatmentDetails\",\n" +
                "                            \"InputType\": \"text\",\n" +
                "                            \"InputValue\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"2\",\n" +
                "            \"QuestionName\": \"Is there any surgery planned for the insured member?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB001\",\n" +
                "                    \"SubQuestionName\": \"ExactDiagnosis\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB002\",\n" +
                "                    \"SubQuestionName\": \"ConsultationDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB007\",\n" +
                "                    \"SubQuestionName\": \"SurgeryDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB004\",\n" +
                "                    \"SubQuestionName\": \"CurrentStatus\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"drop\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB008\",\n" +
                "                    \"SubQuestionName\": \"SurgeryFor\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"3\",\n" +
                "            \"QuestionName\": \"Is the insured member on any regular medication?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB001\",\n" +
                "                    \"SubQuestionName\": \"ExactDiagnosis\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB002\",\n" +
                "                    \"SubQuestionName\": \"ConsultationDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB003\",\n" +
                "                    \"SubQuestionName\": \"DiagnosisDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB004\",\n" +
                "                    \"SubQuestionName\": \"CurrentStatus\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"drop\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB006\",\n" +
                "                    \"SubQuestionName\": \"TreatmentDetails\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"4\",\n" +
                "            \"QuestionName\": \"Has the insured member undergone any tests or medical investigation?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB009\",\n" +
                "                    \"SubQuestionName\": \"TestDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB010\",\n" +
                "                    \"SubQuestionName\": \"TypeOfTest\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB011\",\n" +
                "                    \"SubQuestionName\": \"TestFinding\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"5\",\n" +
                "            \"QuestionName\": \"Is there any hospitalisation history of the insured member?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB001\",\n" +
                "                    \"SubQuestionName\": \"ExactDiagnosis\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB002\",\n" +
                "                    \"SubQuestionName\": \"ConsultationDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB003\",\n" +
                "                    \"SubQuestionName\": \"DiagnosisDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB004\",\n" +
                "                    \"SubQuestionName\": \"CurrentStatus\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"drop\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB005\",\n" +
                "                    \"SubQuestionName\": \"LineOfManagement\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"drop\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB006\",\n" +
                "                    \"SubQuestionName\": \"TreatmentDetails\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"6\",\n" +
                "            \"QuestionName\": \"Is the insured member expecting a baby/pregnant (only for females)?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCERGOSUB012\",\n" +
                "                    \"SubQuestionName\": \"DeliveryDate\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"date\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": [],\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"StatusCode\": 200\n" +
                "}";

        String data = "{\n" +
                "    \"Error\": null,\n" +
                "    \"IsAuthenticated\": true,\n" +
                "    \"Message\": null,\n" +
                "    \"Response\": [\n" +
                "        {\n" +
                "            \"QuestionCode\": \"HDFCHealth001\",\n" +
                "            \"QuestionName\": \"Has Any Pre-Existing Disease?\",\n" +
                "            \"HasSubQuestion\": false,\n" +
                "            \"SubQuestions\": [],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"HDFCHealth002\",\n" +
                "            \"QuestionName\": \"How much cigarettes you can somke in day?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCHealthSub002\",\n" +
                "                    \"SubQuestionName\": \"Quantity?\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": null,\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"HDFCHealth003\",\n" +
                "            \"QuestionName\": \"How much Pouches you can drink in day \",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCHealthSub003\",\n" +
                "                    \"SubQuestionName\": \"Quantity?\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": null,\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"HDFCHealth004\",\n" +
                "            \"QuestionName\": \"How much LiquorPeg you can drink in week ?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCHealthSub004\",\n" +
                "                    \"SubQuestionName\": \"Quantity?\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": null,\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"HDFCHealth005\",\n" +
                "            \"QuestionName\": \"How much BeerBottle you can drink in week ?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCHealthSub005\",\n" +
                "                    \"SubQuestionName\": \"Quantity?\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": null,\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        },\n" +
                "        {\n" +
                "            \"QuestionCode\": \"HDFCHealth006\",\n" +
                "            \"QuestionName\": \"How much WineGlass you can drink in week ?\",\n" +
                "            \"HasSubQuestion\": true,\n" +
                "            \"SubQuestions\": [\n" +
                "                {\n" +
                "                    \"QuestionID\": 0,\n" +
                "                    \"QuestionParentID\": 0,\n" +
                "                    \"SubQuestionCode\": \"HDFCHealthSub006\",\n" +
                "                    \"SubQuestionName\": \"Quantity?\",\n" +
                "                    \"HasSubQuestion\": false,\n" +
                "                    \"InputType\": \"text\",\n" +
                "                    \"InputValue\": null,\n" +
                "                    \"SubQuestionChild\": null,\n" +
                "                    \"Value\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"InputType\": \"bool\",\n" +
                "            \"InputValue\": null,\n" +
                "            \"Category\": null\n" +
                "        }\n" +
                "    ],\n" +
                "    \"StatusCode\": 200\n" +
                "}";

        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getInt("StatusCode") == 200) {

                Type listType = new TypeToken<Response>() {
                }.getType();
                Response responsedata = new Gson().fromJson(String.valueOf(jsonObject), listType);
                healthQuestionsList = responsedata.getResponse();
                progressBar.setMax(healthQuestionsList.size());

                SetHealthQuestion(position);

                // String model = new Gson().toJson(healthQuestionsList);
                // Log.e("HealthQuestion", model);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Health Question Error", e.toString());

        }
    }

    private void SetHealthQuestion(int position) {


        progressBar.setProgress(position + 1);
        txt_serialnumber.setText(String.valueOf(position + 1) + ". ");
        txt_question.setText(healthQuestionsList.get(position).getQuestionName());

        if (healthQuestionsList.get(position).equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT) || healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTNUMBER) || healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_DATE)) {
            SetEditextinput();
        } else if (healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_BOOLEAN)) {
            SetBooleanType();
        }


    }


    public static String ShowDataPickerDialog(Context context) {

        DatePickerDialog datePickerDialog;
        final String[] selecteddate = {""};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -18);
            datePickerDialog = new DatePickerDialog(context);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String smonth, sdate;
                    int Year = i;
                    int month = i1 + 1;
                    int date = i2;

                    if (month < 10) {
                        smonth = "0" + String.valueOf(month);
                    } else {
                        smonth = String.valueOf(month);
                    }
                    if (date < 10) {
                        sdate = "0" + String.valueOf(date);
                    } else {
                        sdate = String.valueOf(date);
                    }
                    selecteddate[0] = sdate + "-" + smonth + "-" + Year;

                }
            });
        }
        return selecteddate[0];

    }


    private void SetBooleanType() {

        switchyesno.setVisibility(View.VISIBLE);
        il_txt.setVisibility(View.GONE);

        if (healthQuestionsList.get(position).getInputValue() != null) {
            if (healthQuestionsList.get(position).getInputValue().toString().equals("true")) {
                switchyesno.setChecked(true);
                if (healthQuestionsList.get(position).getHasSubQuestion()) {
                    recyclerViewSubquestion.setVisibility(View.VISIBLE);
                    subQuestionList = healthQuestionsList.get(position).getSubQuestions();
                    SetHealthSubQuestion();
                } else {
                    recyclerViewSubquestion.setVisibility(View.GONE);
                }

            } else {
                switchyesno.setChecked(false);
            }

        } else {
            switchyesno.setChecked(false);
        }

        switchyesno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (healthQuestionsList.get(position).getHasSubQuestion()) {
                        recyclerViewSubquestion.setVisibility(View.VISIBLE);
                        subQuestionList = healthQuestionsList.get(position).getSubQuestions();
                        SetHealthSubQuestion();
                    } else {
                        recyclerViewSubquestion.setVisibility(View.GONE);

                    }
                } else {
                    recyclerViewSubquestion.setVisibility(View.GONE);
                }

            }
        });
    }


    private void SetEditextinput() {
        // For Edit Text Input Type Only

        il_txt.setVisibility(View.VISIBLE);
        switchyesno.setVisibility(View.GONE);
        recyclerViewSubquestion.setVisibility(View.GONE);

        if (healthQuestionsList.get(position).getInputValue() != null) {
            ed_txt.setText(healthQuestionsList.get(position).getInputValue().toString());
            ed_txt.setSelection(ed_txt.length());

        } else {
            ed_txt.setText("");
        }
        if (healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTTEXT)) {
            ed_txt.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_INPUTNUMBER)) {
            ed_txt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        if (healthQuestionsList.get(position).getInputType().equals(QuestionTypeConstant.QUESTION_TYPE_DATE)) {
            ed_txt.setFocusable(false);
            ed_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ed_txt.setText(ShowDataPickerDialog(getApplicationContext()));
                }
            });
        }

    }


    private void SetHealthSubQuestion() {

        progressbar_subquestion.setVisibility(View.VISIBLE);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            adpater = new AdapterQuestion(HealthQuestionActivity.this, position + 1, subQuestionList);
            handler.post(() -> {
                recyclerViewSubquestion.setAdapter(adpater);
                progressbar_subquestion.setVisibility(View.GONE);

            });
        });


    }


}