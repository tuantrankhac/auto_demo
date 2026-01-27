package utilities;

import com.github.javafaker.Faker;

public class DataUtils {
	private Faker faker;
	
	public static DataUtils getData() {
		return new DataUtils();
	}
	
	public DataUtils() {
		faker = new Faker();
	}
	
	public String getFirstName() {
		return faker.name().firstName();
	}
	
	public String getEditFirstName() {
		return faker.name().firstName();
	}
	
	public String getLastName() {
		return faker.name().lastName();
	}
	
	public String getEditLastName() {
		return faker.name().lastName();
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
	
	public String getEmailAddress() {
		return faker.internet().emailAddress();
	}
	
	public String getPassword() {
		return faker.internet().password();
	}
	
	public String getUsername() {
		return faker.name().username();
	}
	
	public String cardNumber() {
		return faker.finance().creditCard();
		
	}
}
