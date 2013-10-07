package com.premiumminds.webapp.wicket.test.bootstrap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.form.BootstrapCrudifierForm;

public class BootstrapCrudifierFormTest {

	@SuppressWarnings("serial")
	@Test
	public void testRender() {
		WicketTester tester = createTester();
		
		final Entity2[] entities = new Entity2[]{ new Entity2(1),new Entity2(3), new Entity2(2)};
		
		Set<Entity2> entitiesSet = new HashSet<Entity2>();
		entitiesSet.add(entities[0]);
		entitiesSet.add(entities[1]);
		
		Entity entity = new Entity(false, 0, "", entities[0], entitiesSet, Entity.Enume.ENUM1 );
		
		CrudifierSettings config = new CrudifierSettings();
		
		EntityProvider<Entity2> providerEntity2 = new EntityProvider<Entity2>() {
			public List<Entity2> load() {
				return Arrays.asList(entities);
			}
		};
		
		config.getProviders().put("entity2", providerEntity2);
		config.getProviders().put("entities2", providerEntity2);
		
		tester.startComponentInPage(new BootstrapCrudifierForm<Entity>("crud", Model.of(entity), config));
	}
	
	/* TODO: need this test */
	public void testNotNull(){
	}
	
	
	@SuppressWarnings("serial")
	public static class Entity implements Serializable {
		public enum Enume { ENUM1, ENUM2 }
		
		private boolean checkbox;
		private int textboxInt;
		private String textboxString;
		@NotNull
		private Entity2 entity2;
		private Set<Entity2> entities2;
		private Enume enume;
		
		public Entity(boolean checkbox, int textboxInt, String textboxString,
				Entity2 entity2, Set<Entity2> entities2, Enume enume) {
			this.checkbox = checkbox;
			this.textboxInt = textboxInt;
			this.textboxString = textboxString;
			this.entity2 = entity2;
			this.entities2 = entities2;
			this.enume = enume;
		}
		
		public boolean isCheckbox() {
			return checkbox;
		}
		public void setCheckbox(boolean checkbox) {
			this.checkbox = checkbox;
		}
		public int getTextboxInt() {
			return textboxInt;
		}
		public void setTextboxInt(int textboxInt) {
			this.textboxInt = textboxInt;
		}
		public String getTextboxString() {
			return textboxString;
		}
		public void setTextboxString(String textboxString) {
			this.textboxString = textboxString;
		}
		public Entity2 getEntity2() {
			return entity2;
		}
		public void setEntity2(Entity2 entity2) {
			this.entity2 = entity2;
		}
		public Set<Entity2> getEntities2() {
			return entities2;
		}
		public void setEntities2(Set<Entity2> entities2) {
			this.entities2 = entities2;
		}
		public Enume getEnume() {
			return enume;
		}
		public void setEnume(Enume enume) {
			this.enume = enume;
		}
	}
	
	public static class Entity2 {
		private int id;

		public Entity2(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		@Override
		public String toString() {
			return "Entity2 [id=" + id + "]";
		}
	}

	private WicketTester createTester(){
		WicketTester tester = new WicketTester(new WebApplication() {
			
			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}
			
			@Override
			public Session newSession(Request request, Response response) {
				Session session = super.newSession(request, response);
				session.setLocale(Locale.FRENCH);
				return session;
			}
		}){
			@Override
			protected String createPageMarkup(String componentId) {
				return "<form wicket:id=\"crud\">"+
						"</form>";
			}
		};
		
		return tester;
	}
	
}
