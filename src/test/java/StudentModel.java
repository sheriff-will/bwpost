public class StudentModel {

    String student, grade, pass_fail;

    public StudentModel() {

    }

    public StudentModel(String student, String grade, String pass_fail) {
        this.student = student;
        this.grade = grade;
        this.pass_fail = pass_fail;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPass_fail() {
        return pass_fail;
    }

    public void setPass_fail(String pass_fail) {
        this.pass_fail = pass_fail;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "student='" + student + '\'' +
                ", grade='" + grade + '\'' +
                ", pass_fail='" + pass_fail + '\'' +
                '}';
    }

}
