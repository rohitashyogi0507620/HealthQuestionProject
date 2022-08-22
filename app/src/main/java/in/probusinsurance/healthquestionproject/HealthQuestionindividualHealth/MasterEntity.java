package in.probusinsurance.healthquestionproject.HealthQuestionindividualHealth;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MasterEntity implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("Name")
    private String name;

    public MasterEntity() {
    }

    public MasterEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected MasterEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<MasterEntity> CREATOR = new Creator<MasterEntity>() {
        @Override
        public MasterEntity createFromParcel(Parcel in) {
            return new MasterEntity(in);
        }

        @Override
        public MasterEntity[] newArray(int size) {
            return new MasterEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }
}

