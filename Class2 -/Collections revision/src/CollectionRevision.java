import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CollectionRevision {
    public static void main(String[] args)
    {
        Set<Student> students = new HashSet<Student>();
        Scanner sc = new Scanner(System.in);
        int input;
        do {
            System.out.println("Menu");
            System.out.println("Enter 1 to add student");
            System.out.println("Enter 2 to display students");
            System.out.println("Enter 0 t exit");

            input = sc.nextInt();
            switch (input)
            {
                case 1:
                    System.out.println("Enter first name: ");
                    String firstName = sc.next();
                    System.out.println("Enter last name: ");
                    String lastName = sc.next();
                    System.out.println("Enter grade: ");
                    String grade = sc.next();
                    students.add(new Student(firstName, lastName, grade));
                    break;
                case 2:
                    int counter = 1;
                    for (Student student:students) {
                        System.out.println("Student " + counter++);
                        student.display();
                    }
                    break;
            }

        }while(input > 0);
    }
}
