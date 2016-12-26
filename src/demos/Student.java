package demos;

/** A class to store information about a Student
 *  Used in module 4 of the UC San Diego MOOC Object Oriented Programming in Java
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 */
public class Student extends Person
{
	public Student(String name)  {
		super(name);
	}
	public void status(int hr){
		if(this.isAsleep(hr)){
			System.out.println("Now offline: " + this);
		}else{
			System.out.println("Now online :" + this );
		}
	}
	public boolean isAsleep( int hr ) // override 
	{ 
		return 2 < hr && 8 > hr; 
	}
	
	public static void main(String[] args)
	{
		// runtime environment will figure out the object that created is student
//		although the ref of the object is Person class  
		Person p = new Student("Sally");
		((Student)p).status(1);
	}
}
