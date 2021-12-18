package model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import yanson.annotation.JsonField;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BaseTypeVo implements Serializable {

	private String string;
	private int integer;
	@JsonField(value = "float2", aliasNames = {"float"})
	@JSONField(alternateNames = {"float"})
	@JsonAlias(value = {"float"})
	@SerializedName(value = "float2", alternate = {"float"})
	private Float float2;
	@JsonField(value = "boolean2", aliasNames = {"boolean"})
	@JSONField(alternateNames = {"boolean"})
	@JsonAlias(value = {"boolean"})
	@SerializedName(value = "boolean2", alternate = {"boolean"})
	private Boolean boolean2;
	private Object[] array = new Object[4];
	private List<EmployeeVo> employees;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date date;

	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public void setStringWithTimer(String string) {
		long l = System.nanoTime();
		this.string = string;
		System.out.println(String.format("Consumed time is %d with argument of %s", System.nanoTime() - l, string));
	}
	public Integer getInteger() {
		return integer;
	}
	public void setInteger(Integer integer) {
		this.integer = integer;
	}
	public Float getFloat2() {
		return float2;
	}
	public void setFloat2(Float float2) {
		this.float2 = float2;
	}
	public Boolean getBoolean2() {
		return boolean2;
	}
	public void setBoolean2(Boolean boolean2) {
		this.boolean2 = boolean2;
	}
	public Object[] getArray() {
		return array;
	}
	public void setArray(Object[] array) {
		this.array = array;
	}
	public List<EmployeeVo> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeVo> employees) {
		this.employees = employees;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "BaseTypeVo{" +
				"string='" + string + '\'' +
				", integer=" + integer +
				", float2=" + float2 +
				", boolean2=" + boolean2 +
				", array=" + Arrays.toString(array) +
				", employees=" + employees +
				", date=" + date +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseTypeVo that = (BaseTypeVo) o;
		return integer == that.integer && Objects.equals(string, that.string) && Objects.equals(float2, that.float2) && Objects.equals(boolean2, that.boolean2) && Arrays.equals(array, that.array) && Objects.equals(employees, that.employees);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(string, integer, float2, boolean2, employees);
		result = 31 * result + Arrays.hashCode(array);
		return result;
	}

}
