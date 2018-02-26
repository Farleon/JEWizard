/*
 * Copyright (c) 2012-2017
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.sample.inject;

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.eclipse.che.api.project.server.handlers.ProjectHandler;
import org.eclipse.che.api.project.server.type.ProjectTypeDef;
import org.eclipse.che.inject.DynaModule;
import org.eclipse.che.sample.handler.JujuCreateProjectHandler;
import org.eclipse.che.sample.projecttype.JujuProjectType;

@DynaModule
public class JujuWizardModule extends AbstractModule {
  @Override
  protected void configure() {
    Multibinder<ProjectTypeDef> projectTypeMultibinder =
        Multibinder.newSetBinder(binder(), ProjectTypeDef.class);
    projectTypeMultibinder.addBinding().to(JujuProjectType.class);

    Multibinder<ProjectHandler> projectHandlerMultibinder =
        newSetBinder(binder(), ProjectHandler.class);
    projectHandlerMultibinder.addBinding().to(JujuCreateProjectHandler.class);
  }
}
