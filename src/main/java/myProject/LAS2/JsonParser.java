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
"nodes",
"links"
})

public class JsonParser {

@JsonProperty("nodes")
private List<Node> nodes = new ArrayList<Node>();
@JsonProperty("links")
private List<Link> links = new ArrayList<Link>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* No args constructor for use in serialization
* 
*/
public JsonParser() {
}

/**
* 
* @param nodes
* @param links
*/
public JsonParser(List<Node> nodes, List<Link> links) {
this.nodes = nodes;
this.links = links;
}

/**
* 
* @return
* The nodes
*/
@JsonProperty("nodes")
public List<Node> getNodes() {
return nodes;
}

/**
* 
* @param nodes
* The nodes
*/
@JsonProperty("nodes")
public void setNodes(List<Node> nodes) {
this.nodes = nodes;
}

public JsonParser withNodes(List<Node> nodes) {
this.nodes = nodes;
return this;
}

/**
* 
* @return
* The links
*/
@JsonProperty("links")
public List<Link> getLinks() {
return links;
}

/**
* 
* @param links
* The links
*/
@JsonProperty("links")
public void setLinks(List<Link> links) {
this.links = links;
}

public JsonParser withLinks(List<Link> links) {
this.links = links;
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

public JsonParser withAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
return this;
}

}

