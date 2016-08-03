package myProject.LAS2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"entity"
})
public class Node {

@JsonProperty("entity")
private String entity;
@JsonIgnore
//private Map<String, String> additionalProperties = new HashMap<String, String>();
private List<String> properties;

/**
* No args constructor for use in serialization
* 
*/
public Node() {
}

/**
* 
* @param entity
*/
public Node(String entity) {
this.entity = entity;
}

/**
* 
* @return
* The entity
*/
@JsonProperty("entity")
public String getEntity() {
return entity;
}

/**
* 
* @param entity
* The entity
*/
@JsonProperty("entity")
public void setEntity(String entity) {
this.entity = entity;
}

public Node withEntity(String entity) {
this.entity = entity;
return this;
}
public List<String> getProperties() {
	return properties;
}

public void setProperties(List<String> properties) {
	this.properties = properties;
}

public boolean addProperties(String pro){
	if(this.properties == null){
		this.properties = new ArrayList<String>();
	}
	return this.properties.add(pro);
}
/*
@JsonAnyGetter
public Map<String, String> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, String value) {
this.additionalProperties.put(name, value);
}

public Node withAdditionalProperty(String name, String value) {
this.additionalProperties.put(name, value);
return this;
}
*/
}