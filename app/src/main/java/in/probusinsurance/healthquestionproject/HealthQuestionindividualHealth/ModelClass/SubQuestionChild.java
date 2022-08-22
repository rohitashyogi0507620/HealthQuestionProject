
package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass;

import com.google.gson.annotations.SerializedName;

public class SubQuestionChild {

    @SerializedName("SubQuestionCodeChild")
    private String subQuestionCodeChild;
    @SerializedName("SubQuestionNameChild")
    private String subQuestionNameChild;
    @SerializedName("InputType")
    private String inputType;
    @SerializedName("InputValue")

    private Object inputValue;

    public String getSubQuestionCodeChild() {
        return subQuestionCodeChild;
    }

    public void setSubQuestionCodeChild(String subQuestionCodeChild) {
        this.subQuestionCodeChild = subQuestionCodeChild;
    }

    public String getSubQuestionNameChild() {
        return subQuestionNameChild;
    }

    public void setSubQuestionNameChild(String subQuestionNameChild) {
        this.subQuestionNameChild = subQuestionNameChild;
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

}
