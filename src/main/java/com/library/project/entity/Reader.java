package com.library.project.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "doc_gia") // Tên bảng trong MySQL sẽ là 'doc_gia'
@PrimaryKeyJoinColumn(name = "user_id")
public class Reader extends User {


    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String readerCode; // Mã độc giả (thay cho studentCode)

    private LocalDate dob; // Ngày sinh
    private String membershipClass; // Hạng thành viên (Vàng, Bạc, Đồng...)
    private String gender; // Giới tính


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getReaderCode() {
        return readerCode;
    }

    public void setReaderCode(String readerCode) {
        this.readerCode = readerCode;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getMembershipClass() {
        return membershipClass;
    }

    public void setMembershipClass(String membershipClass) {
        this.membershipClass = membershipClass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}