
package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth.ModelClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("Error")
    private Object error;
    @SerializedName("IsAuthenticated")
    private Boolean isAuthenticated;
    @SerializedName("Message")
    private Object message;
    @SerializedName("Response")
    private List<HealthQuestions> healthQuestions = null;
    @SerializedName("StatusCode")
   
    private Integer statusCode;

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(Boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<HealthQuestions> getResponse() {
        return healthQuestions;
    }

    public void setResponse(List<HealthQuestions> healthQuestions) {
        this.healthQuestions = healthQuestions;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
