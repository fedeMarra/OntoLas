package myProject.LAS2;

import java.util.HashMap;
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
"source",
"target",
"label"
})
public class Link {

@JsonProperty("source")
private String source;
@JsonProperty("target")
private String target;
@JsonProperty("label")
private String label;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
* 
*/
public Link() {
}

/**
* 
* @param source
* @param target
* @param label
*/
public Link(String source, String target, String label) {
this.source = source;
this.target = target;
this.label = label;
}

/**
* 
* @return
* The source
*/
@JsonProperty("source")
public String getSource() {
return source;
}

/**
* 
* @param source
* The source
*/
@JsonProperty("source")
public void setSource(String source) {
this.source = source;
}

public Link withSource(String source) {
this.source = source;
return this;
}

/**
* 
* @return
* The target
*/
@JsonProperty("target")
public String getTarget() {
return target;
}

/**
* 
* @param target
* The target
*/
@JsonProperty("target")
public void setTarget(String target) {
this.target = target;
}

public Link withTarget(String target) {
this.target = target;
return this;
}

/**
* 
* @return
* The label
*/
@JsonProperty("label")
public String getLabel() {
return label;
}

/**
* 
* @param label
* The label
*/
@JsonProperty("label")
public void setLabel(String label) {
this.label = label;
}

public Link withLabel(String label) {
this.label = label;
return this;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

public Link withAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
return this;
}

}
