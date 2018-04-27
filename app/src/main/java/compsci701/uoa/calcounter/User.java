package compsci701.uoa.calcounter;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by natha on 24/04/2018.
 */

public class User implements Serializable {

    public enum Gender {
        male(5),
        female(-161)
        ;

        private final int genderValue;

        Gender(int genderValue) {
            this.genderValue = genderValue;
        }

        public int getGenderValue() {
            return this.genderValue;
        }
    }

    public enum ActivityFactor {
        sedentary(1.2),
        somewhat_active(1.3),
        active(1.4)
        ;

        private final double energyFactor;

        private ActivityFactor(double energyFactor) {
            this.energyFactor = energyFactor;
        }

        public double getEnergyFactor() {
            return this.energyFactor;
        }
    }

    private String _name;
    private int _age;
    private Gender _gender;
    private double _height; // unit: cm
    private double _weight; // unit: kg
    private ActivityFactor _activityFactor;
    private double _bodyMassIndex = 0;
    private double _dailyCaloricNeeds = 0;

    public User(String name, int age, Gender gender, double height, double weight, ActivityFactor activityFactor) {
        _name = name;
        _age = age;
        _gender = gender;
        _height = height;
        _weight = weight;
        _activityFactor = activityFactor;

        _bodyMassIndex = calculateBMI(_height, _weight);
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    private double calculateBMI(double height, double weight) {
        double heightSquared = height*height;
        return weight / heightSquared;
    }

    private double calculateDCN(int age, Gender gender, double height, double weight, ActivityFactor activityFactor) {
        double tenWeight = weight*10;
        double sixPointTwoFiveHeight = height*6.25;
        double fiveAge =(double) age*5;

        double unweightedDCN = tenWeight + sixPointTwoFiveHeight - fiveAge + gender.getGenderValue();
        double weightedDCN = unweightedDCN*activityFactor.getEnergyFactor();

        return weightedDCN;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return  _name;
    }

    public void setAge(int age) {
        _age = age;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public int getAge() {
        return _age;
    }

    public void setGender(Gender gender) {
        _gender = gender;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public Gender getGender() {
        return _gender;
    }

    public void setHeight(double height) {
        _height = height;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public double getHeight() {
        return _height;
    }

    public void setWeight(double weight) {
        _weight = weight;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public double getWeight() {
        return _weight;
    }

    public void setActivityFactor(ActivityFactor activityFactor) {
        _activityFactor = activityFactor;
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public ActivityFactor getActivityFactor() {
        return _activityFactor;
    }

    public void recalculateBMI() {
        _bodyMassIndex = calculateBMI(_height, _weight);
    }

    public double getBMI() {
        return _bodyMassIndex;
    }

    public void recalculateDCN() {
        _dailyCaloricNeeds = calculateDCN(_age, _gender, _height, _weight, _activityFactor);
    }

    public double getDCN() {
        return _dailyCaloricNeeds;
    }

}
