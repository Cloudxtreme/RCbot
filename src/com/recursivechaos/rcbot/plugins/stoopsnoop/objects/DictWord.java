package com.recursivechaos.rcbot.plugins.stoopsnoop.objects;
/**
 * DictWord represents a single word from the WORDLIST table, to be used in queries
 * 
 * @author Andrew Bell www.recursivechaos.com
 */
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DictWord {
	
	@Id
	String word;
	
	
	public DictWord(){
	
	}
	
	public DictWord(String word){
		this.word = word;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

}
