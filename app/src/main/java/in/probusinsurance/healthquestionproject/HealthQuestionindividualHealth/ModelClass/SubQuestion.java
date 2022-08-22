
package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubQuestion {

    @SerializedName("QuestionID")
    private Integer questionID;
    @SerializedName("QuestionParentID")
    private Integer questionParentID;
    @SerializedName("SubQuestionCode")
    private String subQuestionCode;
    @SerializedName("SubQuestionName")
    private String subQuestionName;
    @SerializedName("HasSubQuestion")
    private Boolean hasSubQuestion;
    @SerializedName("InputType")
    private String inputType;
    @SerializedName("InputValue")
    private Object inputValue;
    @SerializedName("SubQuestionChild")
    private List<SubQuestionChild> subQuestionChild = null;
    @SerializedName("Value")
    private Object value;

    public Integer getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Integer questionID) {
        this.questionID = questionID;
    }

    public Integer getQuestionParentID() {
        return questionParentID;
    }

    public void setQuestionParentID(Integer questionParentID) {
        this.questionParentID = questionParentID;
    }

    public String getSubQuestionCode() {
        return subQuestionCode;
    }

    public void setSubQuestionCode(String subQuestionCode) {
        this.subQuestionCode = subQuestionCode;
    }

    public String getSubQuestionName() {
        return subQuestionName;
    }

    public void setSubQuestionName(String subQuestionName) {
        this.subQuestionName = subQuestionName;
    }

    public Boolean getHasSubQuestion() {
        return hasSubQuestion;
    }

    public void setHasSubQuestion(Boolean hasSubQuestion) {
        this.hasSubQuestion = hasSubQuestion;
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

    public List<SubQuestionChild> getSubQuestionChild() {
        return subQuestionChild;
    }

    public void setSubQuestionChild(List<SubQuestionChild> subQuestionChild) {
        this.subQuestionChild = subQuestionChild;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
