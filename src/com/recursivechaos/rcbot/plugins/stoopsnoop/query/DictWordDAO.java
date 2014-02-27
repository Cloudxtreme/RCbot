package com.recursivechaos.rcbot.plugins.stoopsnoop.query;

import java.util.List;

import com.recursivechaos.rcbot.plugins.stoopsnoop.objects.DictWord;

/**
 * DictWordDAO provides manipulation of DictWord objects
 * 
 * @author Andrew Bell www.recursivechaos.com
 */

public interface DictWordDAO {

	List<DictWord> getIgnoredWordList();
	
}
