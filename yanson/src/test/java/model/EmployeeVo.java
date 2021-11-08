package model;

import java.util.Objects;

public class EmployeeVo {

	private String firstName;
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return "EmployeeVo [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EmployeeVo that = (EmployeeVo) o;
		return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName);
	}

}
