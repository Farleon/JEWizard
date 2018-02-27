/*
 * Copyright (c) 2012-2017
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.sample.ide.wizard;

import com.google.inject.ImplementedBy;
import org.eclipse.che.ide.api.mvp.View;

@ImplementedBy(SamplePageViewImpl.class)
public interface SamplePageView extends View<SamplePageView.ActionDelegate> {

  String getCompilerVersion();

  void setCompilerVersion(String version);

  String getSelectedProjectType();

  String getSelectedTechnology();

  interface ActionDelegate {
    void onCompilerVersionChanged();

    void onProjectTypeChanged();
  }
}
