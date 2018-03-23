/*
 * Copyright (c) 2012-2017
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.sample.ide.wizard;

import com.google.inject.ImplementedBy;
import java.util.HashMap;
import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.sample.shared.logic.Technology;

@ImplementedBy(SamplePageViewImpl.class)
public interface SamplePageView extends View<SamplePageView.ActionDelegate> {

  String getDeployGoal();

  void setDeployGoal(String version);

  String getSelectedProjectType();

  String getSelectedTechnology();

  void setTechnologies(HashMap<String, Technology> tech);

  interface ActionDelegate {
    void onDeployGoalChanged();

    void onProjectTypeChanged();
  }
}
