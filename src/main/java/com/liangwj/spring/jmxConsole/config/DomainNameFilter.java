package com.liangwj.spring.jmxConsole.config;

import java.util.HashSet;
import java.util.Set;

public class DomainNameFilter {
	private final Set<String> ignoreSet = new HashSet<>();

	public DomainNameFilter() {
		this.addIgnoreDomain("Tomcat");
		this.addIgnoreDomain("Tomcat-1");
		this.addIgnoreDomain("com.sun.management");
		this.addIgnoreDomain("java.lang");
		this.addIgnoreDomain("java.nio");
		this.addIgnoreDomain("java.util.logging");
		this.addIgnoreDomain("JMImplementation");
	}

	public void addIgnoreDomain(String domainName) {
		if (domainName != null) {
			this.ignoreSet.add(domainName.toLowerCase());
		}
	}

	public void removeIgnoreDomain(String domainName) {
		if (domainName != null) {
			this.ignoreSet.remove(domainName.toLowerCase());
		}
	}

	public boolean show(String domainName) {
		if (domainName == null) {
			return false;
		} else {
			return !this.ignoreSet.contains(domainName.toLowerCase());
		}
	}
}
