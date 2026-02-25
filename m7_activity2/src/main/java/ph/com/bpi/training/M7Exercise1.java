package ph.com.bpi.training;

import java.util.List;

public class M7Exercise1 {
	private Long id;
	private String name;
	private String group;
	private List<String> apiCallsMade;
	
	public M7Exercise1() {}
	
	public M7Exercise1(Long id, String name, String group, List<String> apiCallsMade) {
		this.id = id;
		this.name = name;
		this.group = group;
		this.apiCallsMade = apiCallsMade;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<String> getApiCallsMade() {
		return apiCallsMade;
	}
	public void setApiCallsMade(List<String> apiCallsMade) {
		this.apiCallsMade = apiCallsMade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
}
