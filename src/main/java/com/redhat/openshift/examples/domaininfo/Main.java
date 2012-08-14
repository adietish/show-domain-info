/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc. Distributed under license by Red Hat, Inc.
 * All rights reserved. This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.openshift.examples.domaininfo;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.openshift.client.IApplication;
import com.openshift.client.IDomain;
import com.openshift.client.IEmbeddedCartridge;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.IUser;
import com.openshift.client.OpenShiftConnectionFactory;
import com.openshift.client.OpenShiftException;
import com.openshift.client.configuration.OpenShiftConfiguration;

/**
 * @author Andre Dietisheim
 */
public class Main {
	public static void main(String[] argv) throws OpenShiftException, FileNotFoundException, IOException {
		if (argv.length < 2) {
			System.out.println("You have to provide username and password.");
			return;
		}
		
		String username = argv[0];
		String password = argv[1];
		IOpenShiftConnection connection =
				new OpenShiftConnectionFactory()
						.getConnection("rhc-domain-show", username, password, new OpenShiftConfiguration().getLibraServer());
		IUser user = connection.getUser();
		printUserInfo(user);
		printApplicationInfo(user);
	}
	
	private static void printUserInfo(IUser user) {
		System.out.println("User Info");
		System.out.println("================");
		IDomain domain = user.getDefaultDomain();
		if (domain == null) {
			System.out.println("No namespaces found. You can use 'rhc domain create -n <namespace>' to create a namespace for your applications.");
		} else {
			System.out.println("Namespace:\t" + domain.getId());
			System.out.println("RHLogin:\t" + user.getRhlogin());
		}
		System.out.println("");
		
	}

	private static void printApplicationInfo(IUser user) {
		System.out.println("Application Info");
		System.out.println("================");
		IDomain domain = user.getDefaultDomain();
		if (domain == null) {
			System.out.println("No applications found.  You can use 'rhc app create' to create new applications.");
		} else {
			for (IApplication application : domain.getApplications()) {
				System.out.println(application.getName());
				System.out.println("\tFramework:\t" + application.getCartridge().getName());
				System.out.println("\tCreation:\t" + application.getCreationTime());
				System.out.println("\tUUID:\t\t" + application.getUUID());
				System.out.println("\tGit URL:\t" + application.getGitUrl());
				System.out.println("\tPublic URL:\t" + application.getApplicationUrl() + "\n");

				System.out.println(" Embedded:");
				for(IEmbeddedCartridge cartridge : application.getEmbeddedCartridges()) {
					System.out.println("\t" + cartridge.getName() + " - URL:" + cartridge.getUrl());
				}
				System.out.println("");

			}
		}
		
	}

}
