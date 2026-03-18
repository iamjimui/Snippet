package com.quest.etna;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quest.etna.model.JwtUserDetails;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Test
	protected void testAuthenticate() throws Exception {
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isConflict());
		
		MvcResult result = this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/authenticate")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
	      )
	      .andDo(print())
	      .andExpect(jsonPath("$.token").exists())
	      .andExpect(status().isOk())
	      .andReturn();
		
        ObjectMapper mapper = new ObjectMapper();
		String fullJWT = result.getResponse().getContentAsString();
		JsonNode json = mapper.readTree(fullJWT);
	    String jwt_value = json.get("token").asText();
        
	    MvcResult me_result = this.mockMvc
	      .perform(MockMvcRequestBuilders.get("/me")
	              .header("authorization", "Bearer " + jwt_value))
	              .andDo(print())
	              .andExpect(status().isOk())
	              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	              .andReturn();
	    
	    String me_string = me_result.getResponse().getContentAsString();
	    JsonNode me_json = mapper.readTree(me_string);
	    String me_id_value = me_json.get("id").asText();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value)
	              .header("authorization", "Bearer " + jwt_value))
	              .andDo(print())
	              .andExpect(status().isOk());
		
		
	}
	
	@Test
	protected void testUser() throws Exception {
		//sans token Bearer, la route /user retourne bien un statut 401
		this.mockMvc
	      .perform(
	    		  MockMvcRequestBuilders.get("/me")
	       )
	              .andDo(print())
	              .andExpect(status().isUnauthorized());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"leTestMVC\", \"password\":\"leTestMVC\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated());
		
		MvcResult result = this.mockMvc
			      .perform(MockMvcRequestBuilders.post("/authenticate")
			    		  .contentType(MediaType.APPLICATION_JSON)
			    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
			      )
			      .andDo(print())
			      .andExpect(jsonPath("$.token").exists())
			      .andExpect(status().isOk())
			      .andReturn();
				
		ObjectMapper mapper = new ObjectMapper();
		String fullJWT = result.getResponse().getContentAsString();
		JsonNode json = mapper.readTree(fullJWT);
		String jwt_value = json.get("token").asText();
		
		MvcResult me_result = this.mockMvc
			      .perform(MockMvcRequestBuilders.get("/me")
			              .header("authorization", "Bearer " + jwt_value))
			              .andDo(print())
			              .andExpect(status().isOk())
			              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			              .andReturn();
			    
		String me_string = me_result.getResponse().getContentAsString();
	    JsonNode me_json = mapper.readTree(me_string);
	    String me_id_value = me_json.get("id").asText();
	    
		//avec un token Bearer valide, la route /user retourne bien un statut 200
		this.mockMvc
	      .perform(MockMvcRequestBuilders.get("/user")
	              .header("authorization", "Bearer " + jwt_value))
	              .andDo(print())
	              .andExpect(status().isOk());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"admin\", \"password\":\"admin\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated())
	      .andReturn();

		MvcResult temp_result = this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/authenticate")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"admin\", \"password\":\"admin\"}")
	      )
	      .andDo(print())
	      .andExpect(jsonPath("$.token").exists())
	      .andExpect(status().isOk())
	      .andReturn();
				
		ObjectMapper temp_mapper = new ObjectMapper();
		String temp_full_data = temp_result.getResponse().getContentAsString();
		JsonNode temp_json = temp_mapper.readTree(temp_full_data);
		String jwt_value_admin = temp_json.get("token").asText();
		
		MvcResult me_result_admin = this.mockMvc
			      .perform(MockMvcRequestBuilders.get("/me")
			              .header("authorization", "Bearer " + jwt_value_admin))
			              .andDo(print())
			              .andExpect(status().isOk())
			              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			              .andReturn();
			    
		String me_string_admin = me_result_admin.getResponse().getContentAsString();
	    JsonNode me_json_admin = mapper.readTree(me_string_admin);
	    String me_id_value_admin = me_json_admin.get("id").asText();
	    
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value_admin)
	              .header("authorization", "Bearer " + jwt_value))
	              .andDo(print())
	              .andExpect(status().isForbidden());

		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value_admin)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
	}
	
	@Test
	protected void testTag() throws Exception {
		this.mockMvc
	      .perform(
	    		  MockMvcRequestBuilders.get("/tags")
	      )
	              .andDo(print())
	              .andExpect(status().isUnauthorized());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated());
		
		MvcResult result = this.mockMvc
			      .perform(MockMvcRequestBuilders.post("/authenticate")
			    		  .contentType(MediaType.APPLICATION_JSON)
			    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
			      )
			      .andDo(print())
			      .andExpect(jsonPath("$.token").exists())
			      .andExpect(status().isOk())
			      .andReturn();
				
		ObjectMapper mapper = new ObjectMapper();
		String fullJWT = result.getResponse().getContentAsString();
		JsonNode json = mapper.readTree(fullJWT);
		String jwt_value = json.get("token").asText();
		
		MvcResult user_tag_result = this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/tag")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .header("authorization", "Bearer " + jwt_value)
	    		  .content("{\"name\":\"letestmvctag\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated())
	      .andReturn();
		
		ObjectMapper user_tag_result_map = new ObjectMapper();
		String user_tag_result_string = user_tag_result.getResponse().getContentAsString();
		JsonNode user_tag_result_json_node = user_tag_result_map.readTree(user_tag_result_string);
		String user_tag_id = user_tag_result_json_node.get("id").asText();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"admin\", \"password\":\"admin\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated())
	      .andReturn();
		
		MvcResult temp_result_admin = this.mockMvc
			      .perform(MockMvcRequestBuilders.post("/authenticate")
			    		  .contentType(MediaType.APPLICATION_JSON)
			    		  .content("{\"username\":\"admin\", \"password\":\"admin\"}")
			      )
			      .andDo(print())
			      .andExpect(jsonPath("$.token").exists())
			      .andExpect(status().isOk())
			      .andReturn();
				
		ObjectMapper mapper_admin = new ObjectMapper();
		String fullJWT_admin = temp_result_admin.getResponse().getContentAsString();
		JsonNode json_admin = mapper_admin.readTree(fullJWT_admin);
		String jwt_value_admin = json_admin.get("token").asText();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/tag/" + user_tag_id)
	              .header("authorization", "Bearer " + jwt_value))
	              .andDo(print())
	              .andExpect(status().isForbidden());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/tag/" + user_tag_id)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
		
		MvcResult me_result = this.mockMvc
			      .perform(MockMvcRequestBuilders.get("/me")
			              .header("authorization", "Bearer " + jwt_value))
			              .andDo(print())
			              .andExpect(status().isOk())
			              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			              .andReturn();
			    
		String me_string = me_result.getResponse().getContentAsString();
	    JsonNode me_json = mapper.readTree(me_string);
	    String me_id_value = me_json.get("id").asText();
	    
		MvcResult me_result_admin = this.mockMvc
			      .perform(MockMvcRequestBuilders.get("/me")
			              .header("authorization", "Bearer " + jwt_value_admin))
			              .andDo(print())
			              .andExpect(status().isOk())
			              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			              .andReturn();
			    
		String me_string_admin = me_result_admin.getResponse().getContentAsString();
	    JsonNode me_json_admin = mapper.readTree(me_string_admin);
	    String me_id_value_admin = me_json_admin.get("id").asText();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value_admin)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
	}
	
	@Test
	protected void testLanguage() throws Exception {
		this.mockMvc
	      .perform(
	    		  MockMvcRequestBuilders.get("/languages")
	      )
	              .andDo(print())
	              .andExpect(status().isUnauthorized());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated());
		
		MvcResult result = this.mockMvc
			      .perform(MockMvcRequestBuilders.post("/authenticate")
			    		  .contentType(MediaType.APPLICATION_JSON)
			    		  .content("{\"username\":\"letestmvc\", \"password\":\"leTestMVC\"}")
			      )
			      .andDo(print())
			      .andExpect(jsonPath("$.token").exists())
			      .andExpect(status().isOk())
			      .andReturn();
				
		ObjectMapper mapper = new ObjectMapper();
		String fullJWT = result.getResponse().getContentAsString();
		JsonNode json = mapper.readTree(fullJWT);
		String jwt_value = json.get("token").asText();
		
		MvcResult user_language_result = this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/language")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .header("authorization", "Bearer " + jwt_value)
	    		  .content("{\"name\":\"letestmvctag\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isForbidden())
	      .andReturn();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.post("/register")
	    		  .contentType(MediaType.APPLICATION_JSON)
	    		  .content("{\"username\":\"admin\", \"password\":\"admin\"}")
	      )
	      .andDo(print())
	      .andExpect(status().isCreated())
	      .andReturn();
		
		MvcResult temp_result_admin = this.mockMvc
			      .perform(MockMvcRequestBuilders.post("/authenticate")
			    		  .contentType(MediaType.APPLICATION_JSON)
			    		  .content("{\"username\":\"admin\", \"password\":\"admin\"}")
			      )
			      .andDo(print())
			      .andExpect(jsonPath("$.token").exists())
			      .andExpect(status().isOk())
			      .andReturn();
				
		ObjectMapper mapper_admin = new ObjectMapper();
		String fullJWT_admin = temp_result_admin.getResponse().getContentAsString();
		JsonNode json_admin = mapper_admin.readTree(fullJWT_admin);
		String jwt_value_admin = json_admin.get("token").asText();
		
		MvcResult admin_language_result = this.mockMvc
			      .perform(MockMvcRequestBuilders.post("/language")
			    		  .contentType(MediaType.APPLICATION_JSON)
			    		  .header("authorization", "Bearer " + jwt_value_admin)
			    		  .content("{\"name\":\"admintag\"}")
			      )
			      .andDo(print())
			      .andExpect(status().isCreated())
			      .andReturn();
		
		ObjectMapper admin_language_result_map = new ObjectMapper();
		String admin_language_result_string = admin_language_result.getResponse().getContentAsString();
		JsonNode admin_language_result_json_node = admin_language_result_map.readTree(admin_language_result_string);
		String admin_language_id =admin_language_result_json_node.get("id").asText();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/language/" + admin_language_id)
	              .header("authorization", "Bearer " + jwt_value))
	              .andDo(print())
	              .andExpect(status().isForbidden());
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/language/" + admin_language_id)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
		
		MvcResult me_result = this.mockMvc
			      .perform(MockMvcRequestBuilders.get("/me")
			              .header("authorization", "Bearer " + jwt_value))
			              .andDo(print())
			              .andExpect(status().isOk())
			              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			              .andReturn();
			    
		String me_string = me_result.getResponse().getContentAsString();
	    JsonNode me_json = mapper.readTree(me_string);
	    String me_id_value = me_json.get("id").asText();
	    
		MvcResult me_result_admin = this.mockMvc
			      .perform(MockMvcRequestBuilders.get("/me")
			              .header("authorization", "Bearer " + jwt_value_admin))
			              .andDo(print())
			              .andExpect(status().isOk())
			              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			              .andReturn();
			    
		String me_string_admin = me_result_admin.getResponse().getContentAsString();
	    JsonNode me_json_admin = mapper.readTree(me_string_admin);
	    String me_id_value_admin = me_json_admin.get("id").asText();
		
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
		this.mockMvc
	      .perform(MockMvcRequestBuilders.delete("/user/" + me_id_value_admin)
	              .header("authorization", "Bearer " + jwt_value_admin))
	              .andDo(print())
	              .andExpect(status().isOk());
	}

	
}
