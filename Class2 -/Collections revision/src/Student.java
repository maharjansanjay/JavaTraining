public class Student {

    public Student(String firstName, String lastName, String grade)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    private String firstName;
    private String lastName;
    private String grade;

    public void display()
    {
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Grade: " + grade);
    }
}
