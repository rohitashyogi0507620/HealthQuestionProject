
package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthQuestions {

    @SerializedName("QuestionCode")
    private String questionCode;
    @SerializedName("QuestionName")
    private String questionName;
    @SerializedName("HasSubQuestion")
    private Boolean hasSubQuestion;
    @SerializedName("SubQuestions")
    private List<SubQuestion> subQuestions = null;
    @SerializedName("InputType")
    private String inputType;
    @SerializedName("InputValue")
    private Object inputValue;
    @SerializedName("Category")
   
    private Object category;

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public Boolean getHasSubQuestion() {
        return hasSubQuestion;
    }

    public void setHasSubQuestion(Boolean hasSubQuestion) {
        this.hasSubQuestion = hasSubQuestion;
    }

    public List<SubQuestion> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<SubQuestion> subQuestions) {
        this.subQuestions = subQuestions;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public Object getInputValue() {
        return inputValue;
    }

    public void setInputValue(Object inputValue) {
        this.inputValue = inputValue;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

}
