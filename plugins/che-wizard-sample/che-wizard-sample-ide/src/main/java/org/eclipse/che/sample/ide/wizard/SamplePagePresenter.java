/*
 * Copyright (c) 2012-2017
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.sample.ide.wizard;

import static org.eclipse.che.sample.shared.Constants.DEPLOYGOAL;
import static org.eclipse.che.sample.shared.Constants.PROJECT_TYPE;
import static org.eclipse.che.sample.shared.Constants.TECHNOLOGY;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.eclipse.che.ide.api.project.MutableProjectConfig;
import org.eclipse.che.ide.api.wizard.AbstractWizardPage;

public class SamplePagePresenter extends AbstractWizardPage<MutableProjectConfig>
    implements SamplePageView.ActionDelegate {

  protected final SamplePageView view;
  protected final EventBus eventBus;

  @Inject
  public SamplePagePresenter(SamplePageView view, EventBus eventBus) {
    this.view = view;
    this.eventBus = eventBus;
    view.setDelegate(this);
  }

  @Override
  public void init(MutableProjectConfig dataObject) {
    super.init(dataObject);
    setAttribute(PROJECT_TYPE, "testPROJ2");
    setAttribute(TECHNOLOGY, "testTECH2");
    setAttribute(DEPLOYGOAL, "testCOMP2");
  }

  @Override
  public void go(AcceptsOneWidget container) {
    container.setWidget(view);
    view.setDeployGoal("arangodb");
  }

  @Override
  public void onDeployGoalChanged() {
    setAttribute(DEPLOYGOAL, view.getDeployGoal());
  }

  /** Sets single value of attribute of data-object. */
  private void setAttribute(String attrId, String value) {
    Map<String, List<String>> attributes = dataObject.getAttributes();
    attributes.put(attrId, Arrays.asList(value));
  }

  @Override
  public void onProjectTypeChanged() {
    setAttribute(PROJECT_TYPE, view.getSelectedProjectType());
    setAttribute(TECHNOLOGY, view.getSelectedTechnology());
  }
}
