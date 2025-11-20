package com.workoutplanner.MiniProject.Payload.Request;

import java.math.BigDecimal;
import java.time.Instant;

public class UserInBodyRequest {
    private BigDecimal height;
    private BigDecimal weight;
    private Instant measuredAt;
    private String notes;

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Instant getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(Instant measuredAt) {
        this.measuredAt = measuredAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
