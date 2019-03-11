package com.inspiregeniussquad.handstogether.appData;

public class CircularDataItems {

    private String circularImgPath, pDate, pTime, pdfPath;
    private String cTitle, cDesc;
    private String postedBy;

    public CircularDataItems() {

    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getCircularImgPath() {
        return circularImgPath;
    }

    public void setCircularImgPath(String circularImgPath) {
        this.circularImgPath = circularImgPath;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
