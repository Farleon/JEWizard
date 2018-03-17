/*
 * Copyright (c) 2012-2017
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.sample.projecttype;

import static org.eclipse.che.sample.shared.Constants.JUJU_PROJECT_TYPE_ID;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.inject.Inject;
import java.util.ArrayList;
import org.eclipse.che.api.project.server.type.AttributeValue;
import org.eclipse.che.api.project.server.type.ProjectTypeDef;
import org.eclipse.che.sample.commander.JujuApplication;
import org.eclipse.che.sample.commander.JujuCommander;

/**
 * C wizard type
 *
 * @author Vitalii Parfonov
 */
public class JujuProjectType extends ProjectTypeDef {
  @Inject
  public JujuProjectType() {
    super(JUJU_PROJECT_TYPE_ID, "Juju Project", true, false, true);
    addVariableDefinition(PROJECT_TYPE, "Project type", false);
    // addVariableDefinition(DEPLOYGOAL, "deploy goal", false);
    addVariableDefinition(TECHNOLOGY, "technology", false);

    AttributeValue av = new AttributeValue(new ArrayList<>());
    for (String k : JujuCommander.getJujuSupported().keySet()) {
      System.err.println(k);
    }
    for (JujuApplication jujuApp : JujuCommander.getJujuApplications()) {
      av.getList().add(jujuApp.getSupportString());
    }

    addConstantDefinition("jujusuppconst", "jujusupp", av);
    /*  attributes.put("jujusupp", v);

    System.err.println(attributes.size());
    System.err.println(attributes.get("jujusupp"));*/
  }
}
