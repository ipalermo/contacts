package com.solstice.evaluation.model;

import java.util.Date;
import java.util.Map;

/**
 * Created by Nacho on 22/10/2015.
 */
public class Contact {
    Integer employeeId;
    String name;
    String company;
    String detailsUrl;
    String smallImageUrl;
    Date birthDate;
    Map<String, String> phones;

    public Contact() {
    }

    public Contact(Integer employeeId, String name, String company, String detailsUrl,
                   String smallImageUrl, Date birthDate, Map<String, String> phones) {
        this.employeeId = employeeId;
        this.name = name;
        this.company = company;
        this.detailsUrl = detailsUrl;
        this.smallImageUrl = smallImageUrl;
        this.birthDate = birthDate;
        this.phones = phones;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Map<String, String> getPhones() {
        return phones;
    }

    public void setPhones(Map<String, String> phones) {
        this.phones = phones;
    }
}
