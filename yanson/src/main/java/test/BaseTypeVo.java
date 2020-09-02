package test;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.gson.annotations.SerializedName;

import annotation.JsonField;

public class BaseTypeVo {

	private String string;
	private Integer integer;
	@JsonField(value = "float2", aliasKeys = {"float"})
	@JSONField(alternateNames = {"float"})
	@JsonAlias(value = {"float"})
	@SerializedName(value = "float2", alternate = {"float"})
	private Float float2;
	@JsonField(value = "boolean2", aliasKeys = {"boolean"})
	@JSONField(alternateNames = {"boolean"})
	@JsonAlias(value = {"boolean"})
	@SerializedName(value = "boolean2", alternate = {"boolean"})
	private Boolean boolean2;
	private int[] array;
	private List<EmployeeVo> employees;

	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
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

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

	public List<EmployeeVo> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeVo> employees) {
		this.employees = employees;
	}

	@Override
	public String toString() {
		return "BaseTypeVo [string=" + string + ", integer=" + integer + ", float2=" + float2 + ", boolean2=" + boolean2
				+ ", employees=" + employees + "]";
	}

}
