package com.example.dhruvshah.newburghmap.model;

/**
 * Created by Dhruv Shah on 2/9/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class DataItem implements Parcelable {

        private Double latitude;
        private Double longitude;
        private String icon;
        private String group;
        private String name;
        private String address;
        private String phone;
        private String link;
        private String description;
        private String hours;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeString(this.icon);
        dest.writeString(this.group);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.hours);
        dest.writeInt(this.additionalProperties.size());
        for (Map.Entry<String, Object> entry : this.additionalProperties.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable((Parcelable) entry.getValue(), flags);
        }
    }

    public DataItem() {
    }

    protected DataItem(Parcel in) {
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.icon = in.readString();
        this.group = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.link = in.readString();
        this.description = in.readString();
        this.hours = in.readString();
        int additionalPropertiesSize = in.readInt();
        this.additionalProperties = new HashMap<String, Object>(additionalPropertiesSize);
        for (int i = 0; i < additionalPropertiesSize; i++) {
            String key = in.readString();
            Object value = in.readParcelable(Object.class.getClassLoader());
            this.additionalProperties.put(key, value);
        }
    }

    public static final Parcelable.Creator<DataItem> CREATOR = new Parcelable.Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel source) {
            return new DataItem(source);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}

