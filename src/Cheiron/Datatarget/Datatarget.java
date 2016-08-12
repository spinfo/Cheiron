package Cheiron.Datatarget;

import java.awt.Component;
import java.util.Map;

import org.apache.uima.jcas.JCas;

public abstract class Datatarget extends Component {

	public abstract void write(Map<String, JCas> data) throws Exception;

	public Object get(String field) throws Exception {
		return this.getClass().getDeclaredField(field).get(this);
	}

	public void set(String field, Object value) throws Exception {
		this.getClass().getDeclaredField(field).set(this, value);
	}

}
