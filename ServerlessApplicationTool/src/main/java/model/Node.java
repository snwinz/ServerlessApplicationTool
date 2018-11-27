package model;

import java.util.LinkedList;
import java.util.List;

public class Node {
	private String name;
	private List<Arc> outgoingArcs = new LinkedList<Arc>();;
	private Setting settings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Arc> getOutgoingArcs() {
		return outgoingArcs;
	}

	public void addOutgoingArc(Arc arc) {
		outgoingArcs.add(arc);
	}

	public void setOutgoingArcs(List<Arc> outgoingArcs) {
		this.outgoingArcs = outgoingArcs;
	}

	public Setting getSettings() {
		return settings;
	}

	public void setSettings(Setting settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		String result = "Name: " + name + System.lineSeparator();
		for (Arc arc : outgoingArcs) {
			result += "Successor: " + arc.getSuccessor().getName() + System.lineSeparator();
			result += "Order: " + arc.getOrder() + System.lineSeparator();
			if (arc.getAccessMode() != dbAccessmode.NOTSET) {
				result += "AccessMode: " + arc.getAccessMode() + System.lineSeparator();
			}
			if (arc.isSynchronizedCall()) {
				result += "Synchronization: yes" + System.lineSeparator();
				;
			}
		}
		return result;
	}

}
