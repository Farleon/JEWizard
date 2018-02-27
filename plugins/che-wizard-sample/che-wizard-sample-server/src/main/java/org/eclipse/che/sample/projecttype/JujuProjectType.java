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

import com.google.inject.Inject;
import org.eclipse.che.api.project.server.type.ProjectTypeDef;

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
  }
}
