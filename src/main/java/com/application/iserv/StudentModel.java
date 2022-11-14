package com.application.iserv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentModel {

    @CsvBindByName(column = "student")
    String student;

    @CsvBindByName(column = "grade")
    String grade;

    @CsvBindByName(column = "pass/fail")
    String pass_fail;

}
