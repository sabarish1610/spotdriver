package com.example.suriya.spotdrivers.fragment.car;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suriya on 07-10-2017.
 */

public class Model implements Parcelable{
    private String sMobileNumber;
    private String sCatagoryofCar;
    private String sModelofCar;
    private String sCarRegisterNumberofCar;
    private String sTravelWithType;
    private String sFuelTypeofCar;
    private String sCarManufacturingofCar;
    private String sSeatingCapacityofCar;
    private int carTripType;
    private String sKMLimitedPerDay;
    private String sLocalLimitedTimePerDay;
    private String sPriceLimitedPerDayCost;
    private String sExcceedperKMPrice;
    private String sLocalDRiverBeta;
    private String sLocalTripWaitingCharges;
    private int carLongTrip;
    private String sLongTripPerKmPrice;
    private String sLongDRiverBeta;
    private String sLongTripWaitingCharges;
    private String sLongHaltingChargesPrice;
    private String sHillsAllowanceprice;
    private int sLongHaltingCharges;
    private int sHillsAllowance;
    private String vechileType;
    public Model()
    {

    }
    protected Model(Parcel in) {
        sCatagoryofCar = in.readString();
        sModelofCar = in.readString();
        sCarRegisterNumberofCar = in.readString();
        sTravelWithType = in.readString();
        sFuelTypeofCar = in.readString();
        sCarManufacturingofCar = in.readString();
        sSeatingCapacityofCar = in.readString();
        sKMLimitedPerDay = in.readString();
        sPriceLimitedPerDayCost = in.readString();
        sExcceedperKMPrice = in.readString();
        sLocalDRiverBeta = in.readString();
        sLocalTripWaitingCharges = in.readString();
        sLongTripPerKmPrice = in.readString();
        sLongDRiverBeta = in.readString();
        sLongTripWaitingCharges = in.readString();
        sLongHaltingCharges = in.readInt();
        sHillsAllowance = in.readInt();
    }

    public String getsMobileNumber() {
        return sMobileNumber;
    }

    public void setsMobileNumber(String sMobileNumber) {
        this.sMobileNumber = sMobileNumber;
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sCatagoryofCar);
        dest.writeString(sModelofCar);
        dest.writeString(sCarRegisterNumberofCar);
        dest.writeString(sTravelWithType);
        dest.writeString(sFuelTypeofCar);
        dest.writeString(sCarManufacturingofCar);
        dest.writeString(sSeatingCapacityofCar);
        dest.writeString(sKMLimitedPerDay);
        dest.writeString(sPriceLimitedPerDayCost);
        dest.writeString(sExcceedperKMPrice);
        dest.writeString(sLocalDRiverBeta);
        dest.writeString(sLocalTripWaitingCharges);
        dest.writeString(sLongTripPerKmPrice);
        dest.writeString(sLongDRiverBeta);
        dest.writeString(sLongTripWaitingCharges);
        dest.writeInt(sLongHaltingCharges);
        dest.writeInt(sHillsAllowance);
    }

    public String getVechileType() {
        return vechileType;
    }

    public void setVechileType(String vechileType) {
        this.vechileType = vechileType;
    }

    public int getCarLongTrip() {
        return carLongTrip;
    }

    public void setCarLongTrip(int carLongTrip) {
        this.carLongTrip = carLongTrip;
    }

    public String getsCatagoryofCar() {
        return sCatagoryofCar;
    }

    public void setsCatagoryofCar(String sCatagoryofCar) {
        this.sCatagoryofCar = sCatagoryofCar;
    }

    public String getsModelofCar() {
        return sModelofCar;
    }

    public void setsModelofCar(String sModelofCar) {
        this.sModelofCar = sModelofCar;
    }

    public String getsCarRegisterNumberofCar() {
        return sCarRegisterNumberofCar;
    }

    public void setsCarRegisterNumberofCar(String sCarRegisterNumberofCar) {
        this.sCarRegisterNumberofCar = sCarRegisterNumberofCar;
    }

    public String getsTravelWithType() {
        return sTravelWithType;
    }

    public void setsTravelWithType(String sTravelWithType) {
        this.sTravelWithType = sTravelWithType;
    }

    public String getsFuelTypeofCar() {
        return sFuelTypeofCar;
    }

    public void setsFuelTypeofCar(String sFuelTypeofCar) {
        this.sFuelTypeofCar = sFuelTypeofCar;
    }

    public String getsCarManufacturingofCar() {
        return sCarManufacturingofCar;
    }

    public void setsCarManufacturingofCar(String sCarManufacturingofCar) {
        this.sCarManufacturingofCar = sCarManufacturingofCar;
    }

    public String getsSeatingCapacityofCar() {
        return sSeatingCapacityofCar;
    }

    public void setsSeatingCapacityofCar(String sSeatingCapacityofCar) {
        this.sSeatingCapacityofCar = sSeatingCapacityofCar;
    }

    public String getsKMLimitedPerDay() {
        return sKMLimitedPerDay;
    }

    public void setsKMLimitedPerDay(String sKMLimitedPerDay) {
        this.sKMLimitedPerDay = sKMLimitedPerDay;
    }

    public String getsPriceLimitedPerDayCost() {
        return sPriceLimitedPerDayCost;
    }

    public void setsPriceLimitedPerDayCost(String sPriceLimitedPerDayCost) {
        this.sPriceLimitedPerDayCost = sPriceLimitedPerDayCost;
    }

    public String getsExcceedperKMPrice() {
        return sExcceedperKMPrice;
    }

    public void setsExcceedperKMPrice(String sExcceedperKMPrice) {
        this.sExcceedperKMPrice = sExcceedperKMPrice;
    }

    public String getsLocalDRiverBeta() {
        return sLocalDRiverBeta;
    }

    public void setsLocalDRiverBeta(String sLocalDRiverBeta) {
        this.sLocalDRiverBeta = sLocalDRiverBeta;
    }

    public String getsLocalTripWaitingCharges() {
        return sLocalTripWaitingCharges;
    }

    public void setsLocalTripWaitingCharges(String sLocalTripWaitingCharges) {
        this.sLocalTripWaitingCharges = sLocalTripWaitingCharges;
    }

    public String getsLongTripPerKmPrice() {
        return sLongTripPerKmPrice;
    }

    public void setsLongTripPerKmPrice(String sLongTripPerKmPrice) {
        this.sLongTripPerKmPrice = sLongTripPerKmPrice;
    }

    public String getsLongDRiverBeta() {
        return sLongDRiverBeta;
    }

    public void setsLongDRiverBeta(String sLongDRiverBeta) {
        this.sLongDRiverBeta = sLongDRiverBeta;
    }

    public String getsLongTripWaitingCharges() {
        return sLongTripWaitingCharges;
    }

    public void setsLongTripWaitingCharges(String sLongTripWaitingCharges) {
        this.sLongTripWaitingCharges = sLongTripWaitingCharges;
    }

    public String getsLongHaltingChargesPrice() {
        return sLongHaltingChargesPrice;
    }

    public void setsLongHaltingChargesPrice(String sLongHaltingChargesPrice) {
        this.sLongHaltingChargesPrice = sLongHaltingChargesPrice;
    }

    public String getsHillsAllowanceprice() {
        return sHillsAllowanceprice;
    }

    public void setsHillsAllowanceprice(String sHillsAllowanceprice) {
        this.sHillsAllowanceprice = sHillsAllowanceprice;
    }

    public int getsLongHaltingCharges() {
        return sLongHaltingCharges;
    }

    public void setsLongHaltingCharges(int sLongHaltingCharges) {
        this.sLongHaltingCharges = sLongHaltingCharges;
    }

    public int getsHillsAllowance() {
        return sHillsAllowance;
    }

    public void setsHillsAllowance(int sHillsAllowance) {
        this.sHillsAllowance = sHillsAllowance;
    }

    public String getsLocalLimitedTimePerDay() {
        return sLocalLimitedTimePerDay;
    }

    public void setsLocalLimitedTimePerDay(String sLocalLimitedTimePerDay) {
        this.sLocalLimitedTimePerDay = sLocalLimitedTimePerDay;
    }

    public int getCarTripType() {
        return carTripType;
    }

    public void setCarTripType(int carTripType) {
        this.carTripType = carTripType;
    }
}
